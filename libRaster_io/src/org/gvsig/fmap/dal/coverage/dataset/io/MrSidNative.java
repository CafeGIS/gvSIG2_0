/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2007 IVER T.I. and Generalitat Valenciana.
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

import org.gvsig.raster.dataset.BandList;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.process.RasterTask;
import org.gvsig.raster.process.RasterTaskQueue;

import es.gva.cit.jgdal.GdalException;
import es.gva.cit.jmrsid.LTIDataType;
import es.gva.cit.jmrsid.LTIGeoCoord;
import es.gva.cit.jmrsid.LTIImageStage;
import es.gva.cit.jmrsid.LTIMetadataDatabase;
import es.gva.cit.jmrsid.LTIMetadataRecord;
import es.gva.cit.jmrsid.LTIPixel;
import es.gva.cit.jmrsid.LTIScene;
import es.gva.cit.jmrsid.LTISceneBuffer;
import es.gva.cit.jmrsid.LTIUtils;
import es.gva.cit.jmrsid.MrSIDException;
import es.gva.cit.jmrsid.MrSIDImageReader;

public class MrSidNative extends MrSIDImageReader {
	static boolean WITH_OVERVIEWS = true;

	public int                      width = 0;
	public int                      height = 0;
	public double 					originX = 0D;
	public double 					originY = 0D;
	public String 					version = "";
	public LTIMetadataDatabase 		metadata;
	public LTIPixel 				pixel = null;
	private int 					alpha = 0;
	protected int 					rBandNr = 1;
	protected int 					gBandNr = 2;
	protected int 					bBandNr = 3;
	protected byte[] 				bandR;
	protected byte[] 				bandG;
	protected byte[] 				bandB;
	private int 					dataType = LTIDataType.LTI_DATATYPE_UINT8;

	//View
	private double                  zoomoverview = 0.0;
	private int 					eColorSpace;
	private int 					eSampleType;
	private int 					noverviews;
	public int                      xini;
	public int                      yini;
	/**
	 * Ancho y alto de la overview
	 */
	public int                      anchoOver;
	public int                      altoOver;
	public int                      blocksize = 1;
	/**
	 * Número de bandas de la imagen
	 */
	public int                      nbands;
	/**
	 * Posición de la esquina superior izquierda en coordenadas pixel
	 */
	private double                  currentViewY = -1;
	private double                  currentViewX = 0D;
	/**
	 * Ancho y alto de la imágen (pixeles en pantalla)
	 */
	private int 					currentViewWidth = -1;
	private int 					currentViewHeight = -1;
	/**
	 * Ancho y alto de la imagen completa en pixeles
	 */
	private int 					currentFullWidth = -1;
	private int 					currentFullHeight = -1;
	/**
	 * Escala del viewport en X e Y
	 */
	private double                  viewportScaleX = 0D;
	private double                  viewportScaleY = 0D;
	/**
	 * Último extent de la ventana seleccionada por el usuario. Este extent corresponde al de la
	 * imagen, no al del viewport de la vista.
	 */
	private double[]				currentImageView = new double[4];
	/**
	 * 
	 */
	public int[]					stepArrayX = null, stepArrayY = null;
	/**
	 * 
	 */
	public boolean  				isSupersampling = false;
	/**
	 * Nombre del fichero MrSID
	 */
	private String					fileName = null;
	protected AffineTransform       ownTransformation = null;
	protected AffineTransform       externalTransformation = new AffineTransform();
	/**
	 *Array de transformaciones afines donde cada elemento es la transformación correspondiente a una overview
	 */
	protected AffineTransform[]     overviewsTransformation = null;
	

	/**
	 * Constructor
	 * @param fName
	 * @throws MrSIDException
	 * @throws IOException
	 */
	public MrSidNative(String fName) throws MrSIDException, IOException {
		super(fName);
		init(fName);
	}

	/**
	 * Inicializa las variables de instancia con los valores de la imagen
	 * @param fName
	 * @throws MrSIDException
	 * @throws IOException
	 */
	private void init(String fName) throws MrSIDException, IOException {
		this.initialize();

		fileName = fName;
		width = this.getWidth();
		height = this.getHeight();
		eSampleType = this.getDataType();
		nbands = this.getNumBands();
		eColorSpace = this.getColorSpace();
		noverviews = this.getNumLevels();
		overviewsTransformation = new AffineTransform[noverviews];
		
		//Creamos una matriz de transformación por nivel de overview con el factor de escala de esta overview
		overviewsTransformation[0] = new AffineTransform();
		int[] dims = this.getDimsAtMag(LTIUtils.levelToMag(0));
		int dimLastX = dims[0];
		int dimLastY = dims[1];
		for (int i = 1; i < noverviews; i++) {
			dims = this.getDimsAtMag(LTIUtils.levelToMag(i));
						
			double scaleFactorX = (double)dims[0] / (double)dimLastX;
			double scaleFactorY = (double)dims[1] / (double)dimLastY;
			//System.out.println("Over:" + i + " zoom:" + LTIUtils.levelToMag(i) + " dims:" + dims[0] + "X" + dims[1]);
			overviewsTransformation[i] = new AffineTransform(scaleFactorX, 0, 0, scaleFactorY, 0, 0);
		}
		
		metadata = this.getMetadata();

		double ox = 0D;
		double oy = 0D;
		double resx = 0D;
		double resy = 0D;

		LTIGeoCoord geoc = this.getGeoCoord();

		ox = geoc.getX();
		oy = geoc.getY();
		resx = geoc.getXRes();
		resy = geoc.getYRes();
		
    	ownTransformation = new AffineTransform(resx, 0, 0, resy, ox, oy);
    	externalTransformation = (AffineTransform)ownTransformation.clone();
		
		currentFullWidth = width;
		currentFullHeight = height;

		blocksize = this.getStripHeight();
		
		if(nbands == 1) {
			rBandNr = gBandNr = bBandNr = 1;
		}
		if(nbands == 2) {
			rBandNr = gBandNr = 1;
			bBandNr = 2;
		}
	}

	/**
	 * Asigna el valor de Alpha
	 * @param a        alpha
	 */
	public void setAlpha(int a) {
		alpha = a;
	}

	/**
	 * Asigna el tipo de datos
	 * @param dt        tipo de datos
	 */
	public void setDataType(int dt) {
		dataType = dt;
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
	 * Calcula el overview a usar de la imagen y el viewport a partir del ancho, alto y
	 * coordenadas del mundo real
	 * @param dWorldTLX        Coordenada X superior izquierda
	 * @param dWorldTLY        Coordenada Y superior izquierda
	 * @param dWorldBRX        Coordenada X inferior derecha
	 * @param dWorldBRY        Coordenada Y inferior derecha
	 * @param nWidth        ancho
	 * @param nHeight        alto
	 */
	public void setView(double ulx, double uly, double lrx, double lry, 
			int nWidth, int nHeight) {
		currentImageView[0] = ulx;
		currentImageView[1] = uly;
		currentImageView[2] = lrx;
		currentImageView[3] = lry;

		//Ancho y alto de la imágen en pixeles (pixeles de la overview)
		currentFullWidth = width;
		currentFullHeight = height;

		//Ventana de la imagen. (en tamaño completo)
		//tl->esq sup izda en pixeles
		//br->esq inf der en pixeles
		Point2D a = worldToRaster(new Point2D.Double(ulx, uly));
		Point2D b = worldToRaster(new Point2D.Double(lrx, lry));
		Point2D tl = new Point2D.Double(Math.min(a.getX(), b.getX()), Math.min(a.getY(), b.getY()));
		Point2D br = new Point2D.Double(Math.max(a.getX(), b.getX()), Math.max(a.getY(), b.getY()));

		//Ancho y alto de la imágen (pixeles en pantalla)
		currentViewWidth = nWidth;
		currentViewHeight = nHeight;

		//Posición de la esquina superior izquierda en coordenadas pixel
		currentViewX = tl.getX();
		currentViewY = tl.getY();

		//Escala de la vista en X e Y
		viewportScaleX = (double) currentViewWidth / (br.getX() - tl.getX());
		viewportScaleY = (double) currentViewHeight / (br.getY() - tl.getY());

		try {
			// calcula el overview a usar
			int[] dims = null;
			double zoom = 1.0;
			zoomoverview = 1.0;

			if (WITH_OVERVIEWS && ((noverviews - 1) > 0)) {
				for (int i = (noverviews - 1); i > 0; i--) {
					zoom = LTIUtils.levelToMag(i);
					dims = this.getDimsAtMag(zoom);

					if (dims[0] > (this.getWidth() * viewportScaleX)) {
						zoomoverview = zoom;
						viewportScaleX /= zoomoverview;
						viewportScaleY /= zoomoverview;
						currentFullWidth = dims[0];
						currentFullHeight = dims[1];
						tl = worldToRaster(new Point2D.Double(ulx, uly));
						br = worldToRaster(new Point2D.Double(lrx, lry));
						tl.setLocation(Math.min(tl.getX(), br.getX()), Math.min(tl.getY(), br.getY()));
						overviewsTransformation[i].transform(tl, tl);
						//Hemos asignado currentFull solo para calcular tl en relación al tamaño de la overview
						currentFullWidth = width;
						currentFullHeight = height;
						currentViewX = tl.getX();
						currentViewY = tl.getY();
						break;
					}
				}
			}

			setDataType(eSampleType);
		} catch (MrSIDException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Muestra alguna información para la depuración
	 */
	void pintaInfo() {
		try {
			System.out.println("GeoTransform:");

			LTIGeoCoord geoc = this.getGeoCoord();

			System.out.println("  param[0]=" + geoc.getX());
			System.out.println("  param[0]=" + geoc.getY());
			System.out.println("  param[0]=" + geoc.getXRes());
			System.out.println("  param[0]=" + geoc.getYRes());
			System.out.println("  param[0]=" + geoc.getXRot());
			System.out.println("  param[0]=" + geoc.getYRot());
			System.out.println("Metadata:");

			LTIMetadataDatabase metadata = this.getMetadata();

			for (int i = 0; i < metadata.getIndexCount(); i++) {
				LTIMetadataRecord rec = null;
				rec = metadata.getDataByIndex(i);
				System.out.println(rec.getTagName());

				if (rec.isScalar()) {
					System.out.println(rec.getScalarData());
				} else if (rec.isVector()) {
					String[] s = rec.getVectorData();

					for (int j = 0; j < s.length; j++)
						System.out.println("V" + j + "->" + s[j]);
				} else if (rec.isArray()) {
					String[] s = rec.getArrayData();

					for (int j = 0; j < s.length; j++)
						System.out.println("A" + j + "->" + s[j]);
				} else {
					System.out.println("");
				}
			}
		} catch (MrSIDException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public LTISceneBuffer readBuffer(int x, int y, int SceneWidth, int SceneHeight)throws MrSIDException{
		LTISceneBuffer buffer = null;
		LTIScene scene = new LTIScene(x, y, SceneWidth, SceneHeight, zoomoverview);
		buffer = new LTISceneBuffer(pixel, SceneWidth, SceneHeight, true);
		((LTIImageStage) this).read(scene, buffer);
		return buffer;
	}

	

	/**
	 * Lee la escena de la imagen correspondiente a la vista seleccionada con
	 * currentView a través de la libreria de MrSid. Esta escena es cargada sobre
	 * un buffer y asignada al parámetro de salida.
	 * @param line        Escena leida
	 * @throws MrSIDException Lanzada si ocurre un error en la lectura de la escena
	 */
	public void readScene(int[] line, RasterTask task) throws MrSIDException, InterruptedException {
		//Posición de inicio de la escena en entero para la petición a la libreria
		int x = (int) Math.floor(currentViewX);
		int y = (int) Math.floor(currentViewY);
		//Ancho y alto de la escena en pixeles
		int SceneWidth = 0;
		int SceneHeight = 0;

		try {
			SceneWidth = (int) Math.ceil((((double) currentViewWidth) / viewportScaleX) + 1);
			if (SceneWidth > currentFullWidth)
				SceneWidth = currentFullWidth;
			SceneHeight = (int) Math.ceil((((double) currentViewHeight) / viewportScaleY) + 1);
			if (SceneHeight > currentFullHeight)
				SceneHeight = currentFullHeight;

			if (SceneWidth == 0) 
				SceneWidth = 1;

			if (SceneHeight == 0)
				SceneHeight = 1;

			if (pixel == null)
				pixel = new LTIPixel(eColorSpace, nbands, eSampleType);

			LTISceneBuffer buffer = null;

			boolean  sizeOk = false;
			while (!sizeOk) {
				sizeOk = true;
				try {
					buffer = readBuffer(x, y, SceneWidth, SceneHeight);
				} catch (MrSIDException ex) {
					SceneWidth-- ;
					try{
						buffer = readBuffer(x, y, SceneWidth, SceneHeight);
					} catch (MrSIDException ex1) {
						SceneWidth++;
						SceneHeight--;
						try{
							buffer = readBuffer(x, y, SceneWidth, SceneHeight);
						} catch (MrSIDException ex2) {
							SceneWidth-- ;
							try{
								buffer = readBuffer(x, y, SceneWidth, SceneHeight);
							} catch (MrSIDException ex3) {
								sizeOk = false;
							}
						}
					}
				}
			}

			if(task.getEvent() != null)
				task.manageEvent(task.getEvent());

			if ((dataType == LTIDataType.LTI_DATATYPE_UINT8) ||
					(dataType == LTIDataType.LTI_DATATYPE_SINT8) ||
					(dataType == LTIDataType.LTI_DATATYPE_SINT16) ||
					(dataType == LTIDataType.LTI_DATATYPE_SINT32) ||
					(dataType == LTIDataType.LTI_DATATYPE_UINT16) ||
					(dataType == LTIDataType.LTI_DATATYPE_UINT32)) {
				int kd;
				int k;
				double scaleX = 1 / viewportScaleX;
				double scaleY = 1 / viewportScaleY;

				int alpha = (this.alpha & 0xff) << 24;

				if (rBandNr == 1) {
					bandR = buffer.buf1;
				} else if (rBandNr == 2) {
					bandR = buffer.buf2;
				} else if (rBandNr == 3) {
					bandR = buffer.buf3;
				}

				if (gBandNr == 1) {
					bandG = buffer.buf1;
				} else if (gBandNr == 2) {
					bandG = buffer.buf2;
				} else if (gBandNr == 3) {
					bandG = buffer.buf3;
				}

				if (bBandNr == 1) {
					bandB = buffer.buf1;
				} else if (bBandNr == 2) {
					bandB = buffer.buf2;
				} else if (bBandNr == 3) {
					bandB = buffer.buf3;
				}

				//Desplazamiento para la X y la Y leidas. Estas tienen efecto cuando un pixel no empieza a visualizarse
				//justo en su esquina superior izquierda y tiene que ser cortado en la visualización.
				double offsetX = Math.abs(currentViewX - ((int)currentViewX));
				double offsetY = Math.abs(currentViewY - ((int)currentViewY));

				for (int y1 = 0; y1 < currentViewHeight; y1++) {
					for (int x1 = 0; x1 < currentViewWidth; x1++) {
						kd = (y1 * currentViewWidth) + x1;
						k = (((int) ((y1 * scaleY) + offsetY)) * SceneWidth) + (int) ((x1 * scaleX) + offsetX);
						try {
							line[kd] = alpha + ((0xff & bandR[k]) << 16) + ((0xff & bandG[k]) << 8) + (0xff & bandB[k]);
						} catch (java.lang.ArrayIndexOutOfBoundsException e) {
						}
					}
					if(task.getEvent() != null)
						task.manageEvent(task.getEvent());
				}
			}

			buffer = null;
		} catch (MrSIDException e) {
			e.printStackTrace();
		}
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
	public void readWindow(IBuffer buf, BandList bandList, int x, int y, int w, int h) throws MrSIDException, InterruptedException {
		RasterTask task = RasterTaskQueue.get(Thread.currentThread().toString());
		
		isSupersampling = false;
		if (pixel == null) {
			pixel = new LTIPixel(eColorSpace, nbands, eSampleType);
		}

		LTIScene scene = new LTIScene(x, y, w, h, 1.0);
		LTISceneBuffer buffer = new LTISceneBuffer(pixel, w, h, true);
		((LTIImageStage) this).read(scene, buffer);

		byte[] bandBuf = null;
		for(int iBand = 0; iBand < getNumBands(); iBand++) {
			int[] drawableBands = bandList.getBufferBandToDraw(fileName, iBand);
			if(drawableBands == null || (drawableBands.length == 1 && drawableBands[0] == -1))
				continue;
			switch(iBand) {
			case 0:  bandBuf = buffer.buf1; break;
			case 1:  bandBuf = buffer.buf2; break;
			case 2:  bandBuf = buffer.buf3; break;
			}
			for (int line = 0; line < h; line++){
				for (int col = 0; col < w; col++){
					int kd = (line * w) + col;
					buf.setElem(line, col, iBand, (byte)bandBuf[kd]);
				}
				if(task.getEvent() != null)
					task.manageEvent(task.getEvent());
			}
		}
	}

	/**
	 * Lee una ventana de la imagen y devuelve un buffer de bytes
	 * @param ulX        Coordenada X de la esquina superior izquierda
	 * @param ulY        Coordenada Y de la esquina superior izquierda
	 * @param sizeX        Tamaño X de la imagen
	 * @param sizeY        Tamaño Y de la image
	 * @param band        Número de bandas
	 * @return        buffer con la ventana leida
	 * @throws MrSIDException
	 */
	public byte[] getWindow(int ulX, int ulY, int sizeX, int sizeY, int band) throws MrSIDException {
		if (pixel == null) 
			pixel = new LTIPixel(eColorSpace, nbands, eSampleType);
		
		LTIScene scene = new LTIScene(ulX, ulY, sizeX, sizeY, 1.0);
		LTISceneBuffer buffer = new LTISceneBuffer(pixel, sizeX, sizeY, true);
		((LTIImageStage) this).read(scene, buffer);

		if (band == 1) {
			return buffer.buf1;
		} else if (band == 2) {
			return buffer.buf2;
		} else if (band == 3) {
			return buffer.buf3;
		}

		return null;
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
			Object[] data = new Object[nbands];
			for(int i = 0; i < nbands; i++){
				byte[] b = getWindow(x, y, 1, 1, nbands);
				switch(dataType){
				case 0:	break;						
				case 1:	data[i] = new Integer(b[0]);
						break;
				case 2:								
				case 3:	data[i] = new Integer(b[0]);
						break;
				case 4:								
				case 5: data[i] = new Integer(b[0]);
						break;
				case 6:	data[i] = new Float(b[0]);
						break;
				case 7:	data[i] = new Double(b[0]);
						break;
				}
			}
			return data;
		} catch (MrSIDException e) {
			return null;
		}
	}
	
	/**
	 * Asigna una transformación que es aplicada sobre la que ya tiene el propio fichero
	 * @param t
	 */
	public void setExternalTransform(AffineTransform t){
		externalTransformation = t;
	}
	
	/**
	 * Devuelve la transformación del fichero de georreferenciación
	 * @return AffineTransform
	 */
	public AffineTransform getOwnTransformation() {
		return ownTransformation;
	}
}