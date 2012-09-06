/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2006 IVER T.I. and Generalitat Valenciana.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 */
package org.gvsig.fmap.dal.coverage.dataset.io;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.io.IOException;

import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.dataset.BandList;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.properties.DatasetColorInterpretation;
import org.gvsig.raster.dataset.properties.DatasetMetadata;
import org.gvsig.raster.datastruct.ColorTable;
import org.gvsig.raster.datastruct.Extent;
import org.gvsig.raster.datastruct.Transparency;
import org.gvsig.raster.process.RasterTask;
import org.gvsig.raster.process.RasterTaskQueue;
import org.gvsig.raster.util.RasterUtilities;

import es.gva.cit.jgdal.Gdal;
import es.gva.cit.jgdal.GdalBuffer;
import es.gva.cit.jgdal.GdalException;
import es.gva.cit.jgdal.GdalRasterBand;
import es.gva.cit.jgdal.GeoTransform;
/**
 * Soporte 'nativo' para ficheros desde GDAL.
 * 
 * @author Luis W. Sevilla (sevilla_lui@gva.es)
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class GdalNative extends Gdal {
	private String                       fileName = null;
	/**
	 * Nombre corto del driver de gdal
	 */
	private String                       shortName = "";
	public 	GeoTransform                 trans = null;
	
	public int                           width = 0, height = 0;
	public double                        originX = 0D, originY = 0D;
	public String                        version = "";
	protected int                        rBandNr = 1, gBandNr = 2, bBandNr = 3, aBandNr = 4;
	private int[]                        dataType = null;
	/**
	 * Metadatos leidos de la imagen
	 */
	DatasetMetadata                      metadata = null;
	protected boolean                    georeferenced = true;
	
	/**
	 * Vectores que contiene los desplazamientos de un pixel cuando hay supersampling.
	 * , es decir el número de pixels de pantalla que tiene un pixel de imagen. Como todos
	 * los pixeles no tienen el mismo ancho y alto ha de meterse en un array y no puede ser
	 * una variable. Además hay que tener en cuenta que el primer y último pixel son de 
	 * distinto tamaño que el resto.   
	 */
	public int[]                          stepArrayX = null, stepArrayY = null;
	protected GdalRasterBand[]            gdalBands = null;
	private double                        lastReadLine = -1;
	private int                           currentFullWidth = -1;
	private int                           currentFullHeight = -1;
	private int                           currentViewWidth = -1;
	private int                           currentViewHeight = -1;
	private double                        currentViewX = 0D;
	private double                        viewportScaleX = 0D;
	private double                        viewportScaleY = 0D;
	//private double                        wcWidth = 0D;
	private double                        stepX = 0D;
	private double                        stepY = 0D;
	public boolean                        isSupersampling = false;
	/**
	 * Estado de transparencia del raster.
	 */
	Transparency                          fileTransparency = null;
	ColorTable                            palette = null;
	DatasetColorInterpretation            colorInterpr = null;
	AffineTransform                       ownTransformation = null;
	AffineTransform                       externalTransformation = new AffineTransform();
	
	/**
	 * Overview usada en el último setView
	 */
	int currentOverview = -1;
	
	
	public GdalNative(String fName) throws GdalException, IOException {
		super();
		init(fName);
	}
	
	private void init(String fName) throws GdalException, IOException {
		fileName = fName;
		open(fName, GA_ReadOnly);
		if (getPtro() == -1)
			throw new GdalException("Error en la apertura del fichero. El fichero no tiene un formato válido.");
//		ext = RasterUtilities.getExtensionFromFileName(fName);
		width = getRasterXSize();
		height = getRasterYSize();
		int[] dt = new int[getRasterCount()];
		for (int i = 0; i < getRasterCount(); i++)
			dt[i] = this.getRasterBand(i + 1).getRasterDataType();
		setDataType(dt);
		shortName = getDriverShortName();
		fileTransparency = new Transparency();
		colorInterpr = new DatasetColorInterpretation();
		metadata = new DatasetMetadata(getMetadata(), colorInterpr);

		// Asignamos la interpretación de color leida por gdal a cada banda. Esto
		// nos sirve para saber que banda de la imagen va asignada a cada banda de
		// visualización (ARGB)
		colorInterpr.initColorInterpretation(getRasterCount());
		metadata.initNoDataByBand(getRasterCount());
		for (int i = 0; i < getRasterCount(); i++) {
			GdalRasterBand rb = getRasterBand(i + 1);
			String colorInt = getColorInterpretationName(rb.getRasterColorInterpretation());
			metadata.setNoDataValue(i, (rb.getRasterNoDataValue() == -9999.0) ? RasterLibrary.defaultNoDataValue : rb.getRasterNoDataValue());
			metadata.setNoDataEnabled(rb.existsNoDataValue());
			colorInterpr.setColorInterpValue(i, colorInt);
			if (colorInt.equals("Alpha"))
				fileTransparency.setTransparencyBand(i);

			if (rb.getRasterColorTable() != null && palette == null) {
				palette = new ColorTable();
				palette.createPaletteFromGdalColorTable(rb.getRasterColorTable());
//				fileTransparency.setTransparencyRangeList(palette.getTransparencyRange());
			}
		}
		fileTransparency.setTransparencyByPixelFromMetadata(metadata);

		try {
			trans = getGeoTransform();

			boolean isCorrect = false;
			for (int i = 0; i < trans.adfgeotransform.length; i++)
				if (trans.adfgeotransform[i] != 0)
					isCorrect = true;
			if (!isCorrect)
				throw new GdalException("");

			ownTransformation = new AffineTransform(trans.adfgeotransform[1], trans.adfgeotransform[4], trans.adfgeotransform[2], trans.adfgeotransform[5], trans.adfgeotransform[0], trans.adfgeotransform[3]);
			externalTransformation = (AffineTransform) ownTransformation.clone();
			currentFullWidth = width;
			currentFullHeight = height;

			this.georeferenced = true;
		} catch (GdalException exc) {
			// Transformación para ficheros sin georreferenciación. Se invierte la Y
			// ya que las WC decrecen de
			// arriba a abajo y los pixeles crecen de arriba a abajo
			ownTransformation = new AffineTransform(1, 0, 0, -1, 0, height);
			externalTransformation = (AffineTransform) ownTransformation.clone();
			currentFullWidth = width;
			currentFullHeight = height;
			this.georeferenced = false;
		}
	}
	
	/**
	 * Obtiene el flag que informa de si el raster tiene valor no data o no.
	 * Consultará todas las bandas del mismo y si alguna tiene valor no data
	 * devuelve true sino devolverá false.
	 * @return true si tiene valor no data y false si no lo tiene
	 * @throws GdalException
	 */
	public boolean existsNoDataValue() throws GdalException {
		for (int i = 0; i < getRasterCount(); i++) {
			GdalRasterBand rb = getRasterBand(i + 1);
			if (rb.existsNoDataValue())
				return true;
		}
		return false;
	}
	
	/**
	 * Obtiene el flag que informa de si el raster tiene valor no data o no
	 * en una banda concreta.
	 * @return true si tiene valor no data en esa banda y false si no lo tiene
	 * @param band Posición de la banda a consultar (0..n)
	 * @throws GdalException
	 */
	public boolean existsNoDataValue(int band) throws GdalException {
		GdalRasterBand rb = getRasterBand(band + 1);
		return rb.existsNoDataValue();
	}

	/**
	 * Devuelve el valor NoData en caso de existir, sino existe devuelve null.
	 * @return
	 */
	public double getNoDataValue() {
		if (metadata == null)
			return RasterLibrary.defaultNoDataValue;

		if (metadata.getNoDataValue().length == 0)
			return RasterLibrary.defaultNoDataValue;

		return metadata.getNoDataValue()[0];
	}

	/**
	 * Asigna el tipo de dato
	 * @param dt entero que representa el tipo de dato
	 */
	public void setDataType(int[] dt) { 
		dataType = dt; 
	}
	
	/**
	 * Obtiene el tipo de dato
	 * @return entero que representa el tipo de dato
	 */
	public int[] getDataType() { 
		return dataType; 
	}
	
	/**
	 * Obtiene un punto 2D con las coordenadas del raster a partir de uno en coordenadas
	 * del punto real.
	 * Supone rasters no girados
	 * @param pt	punto en coordenadas del punto real
	 * @return	punto en coordenadas del raster
	 */
	public Point2D worldToRasterWithoutRot(Point2D pt) {
		Point2D p = new Point2D.Double();
		AffineTransform at = new AffineTransform(	externalTransformation.getScaleX(), 0, 
													0, externalTransformation.getScaleY(), 
													externalTransformation.getTranslateX(), externalTransformation.getTranslateY());
		try {
			at.inverseTransform(pt, p);
		} catch (NoninvertibleTransformException e) {
			return pt;
		}
		return p;
	}
		
	/**
	 * Obtiene un punto 2D con las coordenadas del raster a partir de uno en coordenadas
	 * del punto real.
	 * Supone rasters no girados
	 * @param pt	punto en coordenadas del punto real
	 * @return	punto en coordenadas del raster
	 */
	public Point2D worldToRaster(Point2D pt) {
		Point2D p = new Point2D.Double();
		try {
			externalTransformation.inverseTransform(pt, p);
		} catch (NoninvertibleTransformException e) {
			return pt;
		}
		return p;
	}
	
	/**
	 * Obtiene un punto del raster en coordenadas pixel a partir de un punto en coordenadas
	 * reales. 
	 * @param pt Punto en coordenadas reales
	 * @return Punto en coordenadas pixel.
	 */
	public Point2D rasterToWorld(Point2D pt) {
		Point2D p = new Point2D.Double();
		externalTransformation.transform(pt, p);
		return p;
	}
	
	/**
	 * Calcula el overview a usar. Hay que tener en cuenta que tenemos que tener calculadas las variables
	 * viewPortScale, currentFullWidth y currentFulHeight
	 * @param coordenada pixel expresada en double que indica la posición superior izquierda
	 * @throws GdalException
	 */
	private void calcOverview(Point2D tl, Point2D br) throws GdalException {
		gdalBands[0] = getRasterBand(1);
		currentOverview = -1;
		if (gdalBands[0].getOverviewCount() > 0) {
			GdalRasterBand ovb = null;
			for (int i = gdalBands[0].getOverviewCount() - 1; i > 0; i--) {
				ovb = gdalBands[0].getOverview(i);
				if (ovb.getRasterBandXSize() > getRasterXSize() * viewportScaleX) {
					currentOverview = i;
					viewportScaleX *= ((double) width / (double) ovb.getRasterBandXSize());
					viewportScaleY *= ((double) height / (double) ovb.getRasterBandYSize());
					stepX = 1D / viewportScaleX;
					stepY = 1D / viewportScaleY;
					currentFullWidth = ovb.getRasterBandXSize();
					currentFullHeight = ovb.getRasterBandYSize();
					currentViewX = Math.min(tl.getX(), br.getX());
					lastReadLine = Math.min(tl.getY(), br.getY());
					break;
				}
			}
		}
	}
	
	public void setView(double dWorldTLX, double dWorldTLY,
						double dWorldBRX, double dWorldBRY,
						int nWidth, int nHeight) throws GdalException {
		currentFullWidth = width;
		currentFullHeight = height;
		Point2D tl = worldToRaster(new Point2D.Double(dWorldTLX, dWorldTLY));
		Point2D br = worldToRaster(new Point2D.Double(dWorldBRX, dWorldBRY));
		// Calcula cual es la primera línea a leer;
		currentViewWidth = nWidth;
		currentViewHeight = nHeight;
//		wcWidth = Math.abs(br.getX() - tl.getX());

		currentViewX = Math.min(tl.getX(), br.getX());

		viewportScaleX = (double) currentViewWidth / (br.getX() - tl.getX());
		viewportScaleY = (double) currentViewHeight / (br.getY() - tl.getY());
		stepX = 1D / viewportScaleX;
		stepY = 1D / viewportScaleY;

		lastReadLine = Math.min(tl.getY(), br.getY());
		
		//Para lectura del renderizado (ARGB). readWindow selecciona las bandas que necesita.

		// calcula el overview a usar
		gdalBands = new GdalRasterBand[4];
		calcOverview(tl, br);

		// Selecciona las bandas y los overviews necesarios
		/*gdalBands[0] = getRasterBand(rBandNr);
		gdalBands[1] = gdalBands[0]; 
		gdalBands[2] = gdalBands[1]; 

		if(getRasterCount() >= 2) {
			gdalBands[1] = getRasterBand(gBandNr);
			gdalBands[2] = gdalBands[1]; 
		}
		if(this.getRasterCount() >= 3) 
			gdalBands[2] = getRasterBand(bBandNr);
		if(colorInterpr.isAlphaBand())
			gdalBands[3] = getRasterBand(aBandNr);			

		assignDataTypeFromGdalRasterBands(gdalBands);

		if (currentOverview > 0) {
			gdalBands[0] = gdalBands[0].getOverview(currentOverview);
			if(getRasterCount() >= 2) {
				gdalBands[1] = gdalBands[1].getOverview(currentOverview);
			}
			if(this.getRasterCount() >= 3) 
				gdalBands[2] = gdalBands[2].getOverview(currentOverview);
			if(colorInterpr.isAlphaBand())
				gdalBands[3] = gdalBands[3].getOverview(currentOverview);			

		}*/
	}
	
	/**
	 * Selecciona bandas y overview en el objeto GdalRasterBand[] para el número de bandas solicitado.
	 * @param nbands Número de bandas solicitado.
	 * @throws GdalException
	 */
	public void selectGdalBands(int nbands) throws GdalException {
		gdalBands = new GdalRasterBand[nbands];
		// Selecciona las bandas y los overviews necesarios
		gdalBands[0] = getRasterBand(1);
		for (int i = 0; i < nbands; i++)
			gdalBands[i] = gdalBands[0];

		assignDataTypeFromGdalRasterBands(gdalBands);
//		setDataType(gdalBands[0].getRasterDataType());

		for (int i = 2; i <= nbands; i++) {
			if (getRasterCount() >= i) {
				gdalBands[i - 1] = getRasterBand(i);
				for (int j = i; j < nbands; j++)
					gdalBands[j] = gdalBands[i - 1];
			}
		}

		if (currentOverview > 0) {
			gdalBands[0] = gdalBands[0].getOverview(currentOverview);
			for (int i = 2; i <= nbands; i++) {
				if (getRasterCount() >= i)
					gdalBands[i - 1] = gdalBands[i - 1].getOverview(currentOverview);
			}
		}
	}
		
	int lastY = -1;
	
	/**
	 * Lee una línea de bytes
	 * @param line Buffer donde se cargan los datos
	 * @param initOffset Desplazamiento inicial desde el margen inzquierdo. Esto es necesario para cuando
	 * se supersamplea ya que cada pixel de imagen ocupa muchos pixeles de pantalla y puede empezar a dibujarse
	 * por la izquierda a mitad de pixel
	 * @param gdalBuffer Buffer con la línea de datos original
	 */
	private void readLine(byte[][] line, double initOffset, GdalBuffer[] gdalBuffer) {
		double j = 0D;
		int i = 0;
		for (int iBand = 0; iBand < gdalBuffer.length; iBand++) {
			for (i = 0, j = initOffset; i < currentViewWidth && j < gdalBuffer[0].getSize(); i++, j += stepX) {
				line[iBand][i] = gdalBuffer[iBand].buffByte[(int) j];
			}
		}
	}
	
	/**
	 * Lee una línea de shorts
	 * @param line Buffer donde se cargan los datos
	 * @param initOffset Desplazamiento inicial desde el margen inzquierdo. Esto es necesario para cuando
	 * se supersamplea ya que cada pixel de imagen ocupa muchos pixeles de pantalla y puede empezar a dibujarse
	 * por la izquierda a mitad de pixel
	 * @param gdalBuffer Buffer con la línea de datos original
	 */
	private void readLine(short[][] line, double initOffset, GdalBuffer[] gdalBuffer) {
		double j = 0D;
		int i = 0;
		for (int iBand = 0; iBand < gdalBuffer.length; iBand++) {
			for (i = 0, j = initOffset; i < currentViewWidth && j < gdalBuffer[0].getSize(); i++, j += stepX) {
				line[iBand][i] = (short) (gdalBuffer[iBand].buffShort[(int) j] & 0xffff);
			}
		}
	}

	/**
	 * Lee una línea de ints
	 * @param line Buffer donde se cargan los datos
	 * @param initOffset Desplazamiento inicial desde el margen inzquierdo. Esto es necesario para cuando
	 * se supersamplea ya que cada pixel de imagen ocupa muchos pixeles de pantalla y puede empezar a dibujarse
	 * por la izquierda a mitad de pixel
	 * @param gdalBuffer Buffer con la línea de datos original
	 */
	private void readLine(int[][] line, double initOffset, GdalBuffer[] gdalBuffer) {
		double j = 0D;
		int i = 0;
		for (int iBand = 0; iBand < gdalBuffer.length; iBand++) {
			for (i = 0, j = initOffset; i < currentViewWidth && j < gdalBuffer[0].getSize(); i++, j += stepX) {
				line[iBand][i] = (gdalBuffer[iBand].buffInt[(int) j] & 0xffffffff);
			}
		}
	}

	/**
	 * Lee una línea de float
	 * @param line Buffer donde se cargan los datos
	 * @param initOffset Desplazamiento inicial desde el margen izquierdo. Esto es necesario para cuando
	 * se supersamplea ya que cada pixel de imagen ocupa muchos pixeles de pantalla y puede empezar a dibujarse
	 * por la izquierda a mitad de pixel
	 * @param gdalBuffer Buffer con la línea de datos original
	 */
	private void readLine(float[][] line, double initOffset, GdalBuffer[] gdalBuffer) {
		double j = 0D;
		int i = 0;
		for (int iBand = 0; iBand < gdalBuffer.length; iBand++) {
			for (i = 0, j = initOffset; i < currentViewWidth && j < gdalBuffer[0].getSize(); i++, j += stepX) {
				line[iBand][i] = gdalBuffer[iBand].buffFloat[(int) j];
			}
		}
	}
	
	/**
	 * Lee una línea de doubles
	 * @param line Buffer donde se cargan los datos
	 * @param initOffset Desplazamiento inicial desde el margen inzquierdo. Esto es necesario para cuando
	 * se supersamplea ya que cada pixel de imagen ocupa muchos pixeles de pantalla y puede empezar a dibujarse
	 * por la izquierda a mitad de pixel
	 * @param gdalBuffer Buffer con la línea de datos original
	 */
	private void readLine(double[][] line, double initOffset, GdalBuffer[] gdalBuffer) {
		double j = 0D;
		int i = 0;
		for (int iBand = 0; iBand < gdalBuffer.length; iBand++) {
			for (i = 0, j = initOffset; i < currentViewWidth && j < gdalBuffer[0].getSize(); i++, j += stepX) {
				line[iBand][i] = gdalBuffer[iBand].buffDouble[(int) j];
			}
		}
	}

	/**
	 * Lee una línea completa del raster y devuelve un array del tipo correcto. Esta función es util
	 * para una lectura rapida de todo el fichero sin necesidad de asignar vista. 
	 * @param nLine Número de línea a leer
	 * @param band Banda requerida
	 * @return Object que es un array unidimendional del tipo de datos del raster
	 * @throws GdalException
	 */
	public Object readCompleteLine(int nLine, int band) throws GdalException {
		GdalRasterBand gdalBand = super.getRasterBand(band + 1);
		GdalBuffer gdalBuf = null;

		gdalBuf = gdalBand.readRaster(0, nLine, getRasterXSize(), 1, getRasterXSize(), 1, dataType[band]);

		if (dataType[band] == GDT_Byte)
			return gdalBuf.buffByte;

		if (dataType[band] == GDT_Int16 || dataType[band] == GDT_UInt16)
			return gdalBuf.buffShort;

		if (dataType[band] == GDT_Int32 || dataType[band] == GDT_UInt32)
			return gdalBuf.buffInt;

		if (dataType[band] == GDT_Float32)
			return gdalBuf.buffFloat;

		if (dataType[band] == GDT_Float64)
			return gdalBuf.buffDouble;

		if (dataType[band] == GDT_CInt16 || dataType[band] == GDT_CInt32 ||
				dataType[band] == GDT_CFloat32 || dataType[band] == GDT_CFloat64)
			return null;
		
		return null;
	}
	
	/**
	 * Lee una bloque completo del raster y devuelve un array tridimensional del tipo correcto. Esta función es util
	 * para una lectura rapida de todo el fichero sin necesidad de asignar vista. 
	 * @param nLine Número de línea a leer
	 * @param band Banda requerida
	 * @return Object que es un array unidimendional del tipo de datos del raster
	 * @throws GdalException
	 */
	public Object readBlock(int pos, int blockHeight) throws GdalException, InterruptedException {
		bBandNr = super.getRasterCount();
		int nX = getRasterXSize();

		RasterTask task = RasterTaskQueue.get(Thread.currentThread().toString());
				
		GdalRasterBand[] gdalBand = new GdalRasterBand[bBandNr];
		for (int iBand = 0; iBand < gdalBand.length; iBand++) 
			gdalBand[iBand] = super.getRasterBand(iBand + 1);
				
		GdalBuffer[] gdalBuf = new GdalBuffer[bBandNr];
				
		if (dataType[0] == GDT_Byte) {
			byte[][][] buf = new byte[bBandNr][blockHeight][getRasterXSize()];
			for (int iBand = 0; iBand < gdalBuf.length; iBand++) {
				gdalBuf[iBand] = gdalBand[iBand].readRaster(0, pos, nX, blockHeight, nX, blockHeight, dataType[0]);
				for (int iRow = 0; iRow < blockHeight; iRow++) {
					for (int iCol = 0; iCol < nX; iCol++) 
						buf[iBand][iRow][iCol] = gdalBuf[iBand].buffByte[iRow * nX + iCol];
					if(task.getEvent() != null)
						task.manageEvent(task.getEvent());
				}
			}	
			return buf;
		} else if (dataType[0] == GDT_CInt16 || dataType[0] == GDT_Int16  || dataType[0] == GDT_UInt16) {
			short[][][] buf = new short[bBandNr][blockHeight][getRasterXSize()];
			for (int iBand = 0; iBand < gdalBuf.length; iBand++) {
				gdalBuf[iBand] = gdalBand[iBand].readRaster(0, pos, nX, blockHeight, nX, blockHeight, dataType[0]);
				for (int iRow = 0; iRow < blockHeight; iRow++) {
					for (int iCol = 0; iCol < nX; iCol++) 
						buf[iBand][iRow][iCol] = gdalBuf[iBand].buffShort[iRow * nX + iCol];
					if(task.getEvent() != null)
						task.manageEvent(task.getEvent());
				}
			}	
			return buf;
		} else if (dataType[0] == GDT_CInt32 || dataType[0] == GDT_Int32  || dataType[0] == GDT_UInt32) {
			int[][][] buf = new int[bBandNr][blockHeight][getRasterXSize()];
			for (int iBand = 0; iBand < gdalBuf.length; iBand++) {
				gdalBuf[iBand] = gdalBand[iBand].readRaster(0, pos, nX, blockHeight, nX, blockHeight, dataType[0]);
				for (int iRow = 0; iRow < blockHeight; iRow++) { 
					for (int iCol = 0; iCol < nX; iCol++)
						buf[iBand][iRow][iCol] = gdalBuf[iBand].buffInt[iRow * nX + iCol];
					if(task.getEvent() != null)
						task.manageEvent(task.getEvent());
				}
			}	
			return buf;
		} else if(dataType[0] == GDT_Float32 || dataType[0] == GDT_CFloat32) {
			float[][][] buf = new float[bBandNr][blockHeight][getRasterXSize()];
			for (int iBand = 0; iBand < gdalBuf.length; iBand++) {
				gdalBuf[iBand] = gdalBand[iBand].readRaster(0, pos, nX, blockHeight, nX, blockHeight, dataType[0]);
				for (int iRow = 0; iRow < blockHeight; iRow++) {
					for (int iCol = 0; iCol < nX; iCol++)
						buf[iBand][iRow][iCol] = gdalBuf[iBand].buffFloat[iRow * nX + iCol];
					if(task.getEvent() != null)
						task.manageEvent(task.getEvent());
				}
			}	
			return buf;
		} else if(dataType[0] == GDT_Float64 || dataType[0] == GDT_CFloat64) {
			double[][][] buf = new double[bBandNr][blockHeight][getRasterXSize()];
			for (int iBand = 0; iBand < gdalBuf.length; iBand++) {
				gdalBuf[iBand] = gdalBand[iBand].readRaster(0, pos, nX, blockHeight, nX, blockHeight, dataType[0]);
				for (int iRow = 0; iRow < blockHeight; iRow++) {
					for (int iCol = 0; iCol < nX; iCol++) 
						buf[iBand][iRow][iCol] = gdalBuf[iBand].buffDouble[iRow * nX + iCol];
					if(task.getEvent() != null)
						task.manageEvent(task.getEvent());
				}
			}		
			return buf;
		}
				
			return null;
	}
	
	/**
	 * Lectura de una línea de datos.
	 * @param line
	 * @throws GdalException
	 */
	public void readLine(Object line) throws GdalException {
				int w = (int) (Math.ceil(((double)currentViewWidth)*stepX) + 1);
				int x = (int) (currentViewX);
				int y = (int) (lastReadLine);
				GdalBuffer r = null, g = null, b = null;
				GdalBuffer a = new GdalBuffer();
				
				while(y >= gdalBands[0].getRasterBandYSize())
					y--;
				
				if (x+w > gdalBands[0].getRasterBandXSize()) 
					w = gdalBands[0].getRasterBandXSize()-x;
				
				if(gdalBands[0].getRasterColorTable() != null) {
					palette = new ColorTable();
					palette.createPaletteFromGdalColorTable(gdalBands[0].getRasterColorTable());
					r = gdalBands[0].readRaster(x, y, w, 1, w, 1, dataType[0]);
				} else {
					a.buffByte = new byte[w];
			r = gdalBands[0].readRaster(x, y, w, 1, w, 1, dataType[0]);
			g = b = r;
			if (getRasterCount() > 1 && gdalBands[1] != null)
					g = gdalBands[1].readRaster(x, y, w, 1, w, 1, dataType[0]);
			if (getRasterCount() > 2 && gdalBands[2] != null)
					b = gdalBands[2].readRaster(x, y, w, 1, w, 1, dataType[0]);
				}
							
				lastReadLine += stepY;
				
			double initOffset =  Math.abs(currentViewX - ((int)currentViewX));
			GdalBuffer[] bands = {r, g, b};
						
			if (dataType[0] == GDT_Byte)
				readLine((byte[][])line, initOffset, bands);
		else if (dataType[0] == GDT_CInt16 || dataType[0] == GDT_Int16  || dataType[0] == GDT_UInt16)
			readLine((short[][])line, initOffset, bands);
		else if (dataType[0] == GDT_CInt32 || dataType[0] == GDT_Int32  || dataType[0] == GDT_UInt32)
			readLine((int[][])line, initOffset, bands);
			else if(dataType[0] == GDT_Float32 || dataType[0] == GDT_CFloat32)
				readLine((float[][])line, initOffset, bands);
			else if(dataType[0] == GDT_Float64 || dataType[0] == GDT_CFloat64)
				readLine((double[][])line, initOffset, bands);
		
		return;
	}
			
	/**
	 * Cuando se hace una petición de carga de buffer la extensión pedida puede
	 * estar ajustada a la extensión del raster o no estarlo. En caso de no
	 * estarlo los pixeles del buffer que caen fuera de la extensión del raster
	 * tendrán valor de NoData. Esta función calcula en que pixel del buffer hay
	 * que empezar a escribir en caso de que este sea mayor que los datos a leer.
	 * 
	 * @param dWorldTLX Posición X superior izquierda en coord reales
	 * @param dWorldTLY Posición Y superior izquierda en coord reales
	 * @param dWorldBRX Posición X inferior derecha en coord reales
	 * @param dWorldBRY Posición Y inferior derecha en coord reales
	 * @param nWidth Ancho en pixeles del buffer
	 * @param nHeight Alto en pixeles del buffer
	 * @return desplazamiento dentro del buffer en X e Y
	 */ 
	private int[] calcStepBuffer(Extent dataExtent, int nWidth, int nHeight, int[] stpBuffer) {
			Extent imageExtent = getExtentWithoutRot();
			Extent ajustDataExtent = RasterUtilities.calculateAdjustedView(dataExtent, imageExtent);
			if(!RasterUtilities.compareExtents(dataExtent, ajustDataExtent)){
				Point2D p1 = worldToRasterWithoutRot(new Point2D.Double(ajustDataExtent.minX(), ajustDataExtent.maxY()));
				Point2D p2 = worldToRasterWithoutRot(new Point2D.Double(ajustDataExtent.maxX(), ajustDataExtent.minY()));
				Point2D p3 = worldToRasterWithoutRot(new Point2D.Double(dataExtent.minX(), dataExtent.maxY()));
//    		Point2D p4 = worldToRasterWithoutRot(new Point2D.Double(dataExtent.maxX(), dataExtent.minY()));
				//Ese es el ancho y alto q tendría el buffer en caso de haberse ajustado
				int w = (int)Math.abs(Math.ceil(p2.getX()) - Math.floor(p1.getX())); 
				int h = (int)Math.abs(Math.floor(p1.getY()) - Math.ceil(p2.getY()));
				
				stpBuffer[0] = (int)(p1.getX() + (-p3.getX()));
				stpBuffer[1] = (int)(p1.getY() + (-p3.getY()));
				stpBuffer[2] = stpBuffer[0] + w; 
				stpBuffer[3] = stpBuffer[1] + h;
				return new int[]{w, h};
			}
			return new int[]{nWidth, nHeight};
	}
	
	/**
	 * Lee una ventana de datos sin resampleo a partir de coordenadas reales.
	 * @param buf Buffer donde se almacenan los datos
	 * @param bandList Lista de bandas que queremos leer y sobre que bandas del buffer de destino queremos escribirlas
	 * @param dWorldTLX Posición X superior izquierda en coord reales
	 * @param dWorldTLY Posición Y superior izquierda en coord reales
	 * @param dWorldBRX Posición X inferior derecha en coord reales
	 * @param dWorldBRY Posición Y inferior derecha en coord reales
	 * @param nWidth Ancho en pixeles del buffer
	 * @param nHeight Alto en pixeles del buffer
	 * @throws GdalException
	 */
	public void readWindow(IBuffer buf, BandList bandList, double ulx, double uly,double lrx, double lry,
										int nWidth, int nHeight, boolean adjustToExtent) throws GdalException, InterruptedException {
		Extent petExtent = new Extent(ulx, uly, lrx, lry);
		setView(ulx, uly, lrx, lry, nWidth, nHeight);
		Point2D tl = worldToRaster(new Point2D.Double(ulx, uly));
		Point2D br = worldToRaster(new Point2D.Double(lrx, lry));
				
		if(gdalBands.length == 0)
			return;
		
		selectGdalBands(buf.getBandCount());
				
				int x = (int) Math.round(Math.min(tl.getX(), br.getX()));
				int y = (int) Math.round(Math.min(tl.getY(), br.getY()));
				
				int[] stpBuffer = new int[]{0, 0 , buf.getWidth(), buf.getHeight()};
				//Si el buffer no se ajusta al extent entonces calculamos en que posición comienza a escribirse dentro del buffer
				//ya que lo que cae fuera serán valores NoData
				if(!adjustToExtent){
					int[] wh = calcStepBuffer(petExtent, nWidth, nHeight, stpBuffer);
					if(x < 0)
						x  = 0;
					if(y < 0)
						y  = 0;
					readData(buf, bandList, x, y, wh[0], wh[1], wh[0], wh[1], 0, 0, stpBuffer);
					return;
				}
					
		readData(buf, bandList, x, y, nWidth, nHeight, nWidth, nHeight, 0, 0, stpBuffer);
	}
			
	/**
	 * Lee una ventana de datos con resampleo a partir de coordenadas reales. Este método lee la
	 * ventana de una vez cargando los datos de un golpe en el buffer. Las coordenadas se solicitan
	 * en coordenadas del mundo real por lo que estas pueden caer en cualquier parte de un pixel.
	 * Esto se hace más evidente cuando supersampleamos en la petición, es decir el buffer de de 
	 * mayor tamaño que el número de pixels solicitado.
	 * 
	 * Para resolver esto escribiremos con la función readRaster los datos sobre un buffer mayor 
	 * que el solicitado. Después calcularemos el desplazamiento en pixels dentro de este buffer 
	 * de mayor tamaño hasta llegar a la coordenada real donde comienza la petición real que ha
	 * hecho el usuario. Esto es así porque cuando supersampleamos no queremos los pixeles del 
	 * raster de disco completos sino que en los bordes del buffer quedan cortados.  
	 *  
	 * @param buf Buffer donde se almacenan los datos
	 * @param bandList Lista de bandas que queremos leer y sobre que bandas del buffer de destino queremos escribirlas
	 * @param dWorldTLX Posición X superior izquierda en coord reales
	 * @param dWorldTLY Posición Y superior izquierda en coord reales
	 * @param dWorldBRX Posición X inferior derecha en coord reales
	 * @param dWorldBRY Posición Y inferior derecha en coord reales
	 * @param nWidth Ancho en pixeles de la petición
	 * @param nHeight Alto en pixeles de la petición
	 * @param bufWidth Ancho del buffer
	 * @param bufHeight Alto del buffer
	 * @throws GdalException
	 */
	public void readWindow(IBuffer buf, BandList bandList, double ulx, double uly, double lrx, double lry,
										double nWidth, double nHeight, int bufWidth, int bufHeight, boolean adjustToExtent) throws GdalException, InterruptedException {
		Extent petExtent = new Extent(ulx, uly, lrx, lry);
		setView(ulx, uly, lrx, lry, bufWidth, bufHeight);
		Point2D tl = worldToRaster(new Point2D.Double(ulx, uly));
		Point2D br = worldToRaster(new Point2D.Double(lrx, lry));
				
		if(gdalBands.length == 0)
			return;
		
		selectGdalBands(buf.getBandCount());
				
		int x = (int) Math.min(tl.getX(), br.getX());
			int y = (int) Math.min(tl.getY(), br.getY());
			int endX = (int) Math.ceil(Math.max(br.getX(), tl.getX()));
			int endY = (int) Math.ceil(Math.max(br.getY(), tl.getY()));
											
				int stpX = 0;
				int stpY = 0;
				
		if(bufWidth > Math.ceil(nWidth)){
			stpX = (int)(((tl.getX() - x) * bufWidth) / nWidth);
			bufWidth = (int)((Math.abs(endX - x) * bufWidth) / nWidth);
		}
		if(bufHeight > Math.ceil(nHeight)){
			stpY = (int)(((tl.getY() - y) * bufHeight) / nHeight);
			bufHeight = (int)((Math.abs(endY - y) * bufHeight) / nHeight);
		}
						
				nWidth = (int)Math.abs(endX - x);
				nHeight = (int)Math.abs(endY - y);
				
				nWidth = (nWidth * currentFullWidth) / width;
				nHeight = (nHeight * currentFullHeight) / height;
				x = (x * currentFullWidth) / width;
				y = (y * currentFullHeight) / height;
							 
				int[] stpBuffer = new int[]{0, 0 , buf.getWidth(), buf.getHeight()};
				//Si el buffer no se ajusta al extent entonces calculamos en que posición comienza a escribirse dentro del buffer
				//ya que lo que cae fuera serán valores NoData
				if(!adjustToExtent){
					int[] wh = calcStepBuffer(petExtent, bufWidth, bufHeight, stpBuffer);
					if(x < 0)
						x  = 0;
					if(y < 0)
						y  = 0;
					stpBuffer[0] = (int)((stpBuffer[0] * bufWidth) / nWidth);
					stpBuffer[1] = (int)((stpBuffer[1] * bufHeight) / nHeight);
					stpBuffer[2] = (int)((stpBuffer[2] * bufWidth) / nWidth);
					stpBuffer[3] = (int)((stpBuffer[3] * bufHeight) / nHeight);
					bufWidth = (int)Math.abs(stpBuffer[2] - stpBuffer[0]);
					bufHeight = (int)Math.abs(stpBuffer[3] - stpBuffer[1]);
					readData(buf, bandList, x, y, wh[0], wh[1], bufWidth, bufHeight, 0, 0, stpBuffer);
					return;
				}
				
				if ((x + nWidth) > gdalBands[0].getRasterBandXSize()) 
					nWidth = gdalBands[0].getRasterBandXSize() - x;
				
				if ((y + nHeight) > gdalBands[0].getRasterBandYSize()) 
					nHeight = gdalBands[0].getRasterBandYSize() - y;
				
		readData(buf, bandList, x, y, (int)nWidth, (int)nHeight, bufWidth, bufHeight, stpX, stpY, stpBuffer);
	}
	
	/**
	 * Lee una ventana de datos sin resampleo a partir de coordenadas en pixeles.
	 * @param buf Buffer donde se almacenan los datos
	 * @param bandList Lista de bandas que queremos leer y sobre que bandas del buffer de destino queremos escribirlas
	 * @param x Posición X en pixeles
	 * @param y Posición Y en pixeles
	 * @param w Ancho en pixeles
	 * @param h Alto en pixeles
	 * @throws GdalException
	 */
	public void readWindow(IBuffer buf, BandList bandList, int x, int y, int w, int h) 
		throws GdalException, InterruptedException {
		gdalBands = new GdalRasterBand[getRasterCount()];
		isSupersampling = false;
		if(gdalBands.length == 0)
			return;
		
		// Selecciona las bandas
		gdalBands[0] = getRasterBand(1);
		
		for(int iBand = 1; iBand < gdalBands.length; iBand++)
			gdalBands[iBand] = getRasterBand(iBand + 1);
		
		assignDataTypeFromGdalRasterBands(gdalBands);
		
		int yMax = y + h;
		readDataByLine(buf, bandList, x, y, w, yMax);
	}
	
	/**
	 * Lee una ventana de datos con resampleo a partir de coordenadas en pixeles. Este método lee la
	 * ventana de una vez cargando los datos de un golpe en el buffer.
	 * @param buf Buffer donde se almacenan los datos
	 * @param bandList Lista de bandas que queremos leer y sobre que bandas del buffer de destino queremos escribirlas
	 * @param x Posición X en pixeles
	 * @param y Posición Y en pixeles
	 * @param w Ancho en pixeles
	 * @param h Alto en pixeles
	 * @param bufWidth Ancho del buffer
	 * @param bufHeight Alto del buffer
	 * @throws GdalException
	 */
	public void readWindow(IBuffer buf, BandList bandList, int x, int y, int w, int h, int bufWidth, int bufHeight) throws GdalException, InterruptedException {
		gdalBands = new GdalRasterBand[getRasterCount()];
		
		if(gdalBands.length == 0)
			return;
		
		// Selecciona las bandas
		gdalBands[0] = getRasterBand(1);
		
		for(int iBand = 1; iBand < gdalBands.length; iBand++)
			gdalBands[iBand] = getRasterBand(iBand + 1);
		
		assignDataTypeFromGdalRasterBands(gdalBands);
		
		int[] stpBuffer = new int[]{0, 0 , buf.getWidth(), buf.getHeight()};
		readData(buf, bandList, x, y, w, h, bufWidth, bufHeight, 0, 0, stpBuffer);
	}
	
	/**
	 * Asigna el tipo de datos de las bandas a partir de una lista de GdalRasterBands
	 * @param gdalBands
	 * @throws GdalException
	 */
	private void assignDataTypeFromGdalRasterBands(GdalRasterBand[] gdalBands) throws GdalException {
		int[] dt = new int[gdalBands.length];
		for (int i = 0; i < gdalBands.length; i++) {
			if(gdalBands[i] != null)
				dt[i] = gdalBands[i].getRasterDataType();
		}
		setDataType(dt);
	}
		
	/**
	 * Lee una ventana de datos sin resampleo a partir de coordenadas en pixeles. Esta función es usuada por
	 * readWindow para coordenadas reales y readWindow en coordenadas pixel. 
	 * @param buf Buffer donde se almacenan los datos
	 * @param bandList Lista de bandas que queremos leer y sobre que bandas del buffer de destino queremos escribirlas
	 * @param x Posición X en pixeles
	 * @param y Posición Y en pixeles
	 * @param w Ancho en pixeles
	 * @param h Alto en pixeles
	 * @param bufWidth Ancho del buffer
	 * @param bufHeight Alto del buffer
	 * @param stepX Desplazamiento en pixeles en X a partir de la posición x. Este desplazamiento es util cuando hay un 
	 * supersampleo ya que puede ser que de los pixeles que están en el borde izquierdo de la petición solo queramos una
	 * parte de ellos. 
	 * @param stepY Desplazamiento en pixeles en Y a partir de la posición y. Este desplazamiento es util cuando hay un 
	 * supersampleo ya que puede ser que de los pixeles que están en el borde superior de la petición solo queramos una
	 * parte de ellos.
	 * @param stepBuffer El buffer puede empezar a escribirse a partir de un pixel determinado y acabar de escribir antes 
	 * de fin de buffer. Este parámetro indica el desplazamiento desde el inicio del buffer y la posición final.
	 * <UL>
	 * <LI>stepBuffer[0]:Desplazamiento en X desde el inicio</LI>
	 * <LI>stepBuffer[1]:Desplazamiento en Y desde el inicio</LI>
	 * <LI>stepBuffer[2]:Posición X final</LI>
	 * <LI>stepBuffer[3]:Posición Y final</LI>
	 * </UL>
	 * @throws GdalException
	 */
	private void readData(IBuffer buf, BandList bandList, int x, int y, int w, int h, 
			int bufWidth, int bufHeight, int stpX, int stpY, int[] stepBuffer) throws GdalException, InterruptedException {
		
		RasterTask task = RasterTaskQueue.get(Thread.currentThread().toString()); 
		
		GdalBuffer gdalBuf = null;
		for(int iBand = 0; iBand < gdalBands.length; iBand++) {
			int[] drawableBands = bandList.getBufferBandToDraw(fileName, iBand);
			if(drawableBands == null || (drawableBands.length == 1 && drawableBands[0] == -1))
				continue;	
			int init = (int)((bufWidth * stpY) + stpX); //Pos inicial. Desplazamos stpX pixels hacia la derecha y bajamos stpY lineas
			int pos = init;
			gdalBuf = gdalBands[iBand].readRaster(x, y, w, h, bufWidth, bufHeight, dataType[iBand]);
			if(dataType[iBand] == Gdal.GDT_Byte){
				for (int line = stepBuffer[1]; line < stepBuffer[3]/*buf.getHeight()*/; line++) {
					pos = (int)((bufWidth * (line - stepBuffer[0])) + init);
					for (int col = stepBuffer[0]; col < stepBuffer[2]/*buf.getWidth()*/; col ++) {
						buf.setElem(line, col, iBand, gdalBuf.buffByte[pos]);
						pos ++;
					}
					if(task.getEvent() != null)
						task.manageEvent(task.getEvent());
				}
			}else if((dataType[iBand] == Gdal.GDT_UInt16) || (dataType[iBand] == Gdal.GDT_Int16) || (dataType[iBand] == Gdal.GDT_CInt16)){
				for (int line = stepBuffer[1]; line < stepBuffer[3]; line++) {
					pos = (int)((bufWidth * (line - stepBuffer[0])) + init);
					for (int col = stepBuffer[0]; col < stepBuffer[2]; col ++){
						buf.setElem(line, col, iBand, gdalBuf.buffShort[pos]);
						pos ++;
					}
					if(task.getEvent() != null)
						task.manageEvent(task.getEvent());
				}
			}else if((dataType[iBand] == Gdal.GDT_UInt32) || (dataType[iBand] == Gdal.GDT_Int32) || (dataType[iBand] == Gdal.GDT_CInt32)){
				for (int line = stepBuffer[1]; line < stepBuffer[3]; line++) {
					pos = (int)((bufWidth * (line - stepBuffer[0])) + init);
					for (int col = stepBuffer[0]; col < stepBuffer[2]; col ++){
						buf.setElem(line, col, iBand, gdalBuf.buffInt[pos]);
						pos ++;
					}
					if(task.getEvent() != null)
						task.manageEvent(task.getEvent());
				}
			}else if(dataType[iBand] == Gdal.GDT_Float32){
				for (int line = stepBuffer[1]; line < stepBuffer[3]; line++) {
					pos = (int)((bufWidth * (line - stepBuffer[0])) + init);
					for (int col = stepBuffer[0]; col < stepBuffer[2]; col ++){
						buf.setElem(line, col, iBand, gdalBuf.buffFloat[pos]);
						pos ++;
					}
					if(task.getEvent() != null)
						task.manageEvent(task.getEvent());
				}
			}else if(dataType[iBand] == Gdal.GDT_Float64){
				for (int line = stepBuffer[1]; line < stepBuffer[3]; line++) {
					pos = (int)((bufWidth * (line - stepBuffer[0])) + init);
					for (int col = stepBuffer[0]; col < stepBuffer[2]; col ++){
						buf.setElem(line, col, iBand, gdalBuf.buffDouble[pos]);
						pos ++;
					}
					if(task.getEvent() != null)
						task.manageEvent(task.getEvent());
				}
			}
		}
	}
	
	/**
	 * Lee una ventana de datos sin resampleo a partir de coordenadas en pixeles. Esta función es usuada por
	 * readWindow para coordenadas reales y readWindow en coordenadas pixel. 
	 * @param buf Buffer donde se almacenan los datos
	 * @param bandList Lista de bandas que queremos leer y sobre que bandas del buffer de destino queremos escribirlas
	 * @param x Posición X en pixeles
	 * @param y Posición Y en pixeles
	 * @param w Ancho en pixeles
	 * @param yMax altura máxima de y
	 * @throws GdalException
	 */
	private void readDataByLine(IBuffer buf, BandList bandList, int x, int y, int w, int yMax) throws GdalException, InterruptedException {
		GdalBuffer gdalBuf = null;
		int rasterBufLine;
		RasterTask task = RasterTaskQueue.get(Thread.currentThread().toString());
		
		for(int iBand = 0; iBand < gdalBands.length; iBand++) {
			int[] drawableBands = bandList.getBufferBandToDraw(fileName, iBand);
			if(drawableBands == null || (drawableBands.length == 1 && drawableBands[0] == -1))
				continue;	
			if(dataType[iBand] == Gdal.GDT_Byte) {
				for (int line = y; line < yMax; line++) {
					gdalBuf = gdalBands[iBand].readRaster(x, line, w, 1, w, 1, dataType[iBand]);
					rasterBufLine = line - y;
					buf.setLineInBandByte(gdalBuf.buffByte, rasterBufLine, iBand);
					if(task.getEvent() != null)
						task.manageEvent(task.getEvent());
				}
			}else if((dataType[iBand] == Gdal.GDT_UInt16) || (dataType[iBand] == Gdal.GDT_Int16) || (dataType[iBand] == Gdal.GDT_CInt16)) {
				for (int line = y; line < yMax; line++) {
					gdalBuf = gdalBands[iBand].readRaster(x, line, w, 1, w, 1, dataType[iBand]);
					rasterBufLine = line - y;
					buf.setLineInBandShort(gdalBuf.buffShort, rasterBufLine, iBand);
					if(task.getEvent() != null)
						task.manageEvent(task.getEvent());
				}
			}else if((dataType[iBand] == Gdal.GDT_UInt32) || (dataType[iBand] == Gdal.GDT_Int32) || (dataType[iBand] == Gdal.GDT_CInt32)) {
				for (int line = y; line < yMax; line++) {
					gdalBuf = gdalBands[iBand].readRaster(x, line, w, 1, w, 1, dataType[iBand]);
					rasterBufLine = line - y;
					buf.setLineInBandInt(gdalBuf.buffInt, rasterBufLine, iBand);
					if(task.getEvent() != null)
						task.manageEvent(task.getEvent());
				}
			}else if(dataType[iBand] == Gdal.GDT_Float32){
				for (int line = y; line < yMax; line++) {
					gdalBuf = gdalBands[iBand].readRaster(x, line, w, 1, w, 1, dataType[iBand]);
					rasterBufLine = line - y;
					buf.setLineInBandFloat(gdalBuf.buffFloat, rasterBufLine, iBand);
					if(task.getEvent() != null)
						task.manageEvent(task.getEvent());
				}
			}else if(dataType[iBand] == Gdal.GDT_Float64){
				for (int line = y; line < yMax; line++) {
					gdalBuf = gdalBands[iBand].readRaster(x, line, w, 1, w, 1, dataType[iBand]);
					rasterBufLine = line - y;
					buf.setLineInBandDouble(gdalBuf.buffDouble, rasterBufLine, iBand);
					if(task.getEvent() != null)
						task.manageEvent(task.getEvent());
				}
			}
		}
	}
	
	/**
	 * Obtiene el valor de un pixel determinado por las coordenadas x e y que se pasan
	 * por parámetro
	 * @param x Coordenada X del pixel
	 * @param y Coordenada Y del pixel
	 * @return Array de Object donde cada posición representa una banda y el valor será Integer
	 * en caso de ser byte, shot o int, Float en caso de ser float y Double en caso de ser double.
	 */
	public Object[] getData(int x, int y) {
		try {
			Object[] data = new Object[getRasterCount()];
			for(int i = 0; i < getRasterCount(); i++){
				GdalRasterBand rb = getRasterBand(i + 1);
				GdalBuffer r = rb.readRaster(x, y, 1, 1, 1, 1, dataType[i]);
				switch(dataType[i]){
				case 0:	break;									//Sin tipo
				case 1:	data[i] = new Integer(r.buffByte[0]); 	//Buffer byte (8)
						break;
				case 2:											//Buffer short (16)
				case 3:	data[i] = new Integer(r.buffShort[0]);	//Buffer short (16)
						break;
				case 4:											//Buffer int (32)
				case 5: data[i] = new Integer(r.buffInt[0]);	//Buffer int (32)
						break;
				case 6:	data[i] = new Float(r.buffFloat[0]);	//Buffer float (32)
						break;
				case 7:	data[i] = new Double(r.buffDouble[0]);	//Buffer double (64)
						break;
				}
			}
			return data;
		} catch (GdalException e) {
			return null;
		}
	}
	
	public int getBlockSize(){
		return this.getBlockSize();
	}

	/**
	 * Devuelve la transformación del fichero de georreferenciación
	 * @return AffineTransform
	 */
	public AffineTransform getOwnTransformation() {
		return ownTransformation;
	}
		
	/**
	 * Calcula el extent en coordenadas del mundo real sin rotación. Solo coordenadas y tamaño de pixel
	 * @return Extent
	 */
	public Extent getExtentWithoutRot() {
		AffineTransform at = new AffineTransform(	externalTransformation.getScaleX(), 0, 
													0, externalTransformation.getScaleY(), 
													externalTransformation.getTranslateX(), externalTransformation.getTranslateY());
		Point2D p1 = new Point2D.Double(0, 0);
		Point2D p2 = new Point2D.Double(width, height);
		at.transform(p1, p1);
		at.transform(p2, p2);
		return new Extent(p1, p2);
	}
	
	/**
	 * Asigna una transformación que es aplicada sobre la que ya tiene el propio fichero
	 * @param t
	 */
	public void setExternalTransform(AffineTransform t){
		externalTransformation = t;
	}

	/**
	 * Obtiene el nombre del driver de Gdal
	 * @return Cadena que representa el nombre del driver de gdal
	 */
	public String getGdalShortName() {
		return shortName;
	}
		
}




