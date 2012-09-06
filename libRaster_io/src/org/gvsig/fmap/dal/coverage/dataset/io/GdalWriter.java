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
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.dal.coverage.dataset.io.features.BMPFeatures;
import org.gvsig.fmap.dal.coverage.dataset.io.features.GTiffFeatures;
import org.gvsig.fmap.dal.coverage.dataset.io.features.HFAFeatures;
import org.gvsig.fmap.dal.coverage.dataset.io.features.IDRISIFeatures;
import org.gvsig.fmap.dal.coverage.dataset.io.features.ILWIS_MprFeatures;
import org.gvsig.fmap.dal.coverage.dataset.io.features.PNM_PgmFeatures;
import org.gvsig.fmap.dal.coverage.dataset.io.features.PNM_PpmFeatures;
import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.buffer.RasterBuffer;
import org.gvsig.raster.dataset.GeoRasterWriter;
import org.gvsig.raster.dataset.IDataWriter;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.Params;
import org.gvsig.raster.dataset.RasterDataset;
import org.gvsig.raster.dataset.WriteFileFormatFeatures;
import org.gvsig.raster.dataset.Params.Param;
import org.gvsig.raster.dataset.properties.DatasetColorInterpretation;
import org.gvsig.raster.dataset.serializer.RmfSerializerException;
import org.gvsig.raster.process.RasterTask;
import org.gvsig.raster.process.RasterTaskQueue;
import org.gvsig.raster.util.RasterUtilities;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.extensionpoint.ExtensionPoint;
import org.gvsig.tools.extensionpoint.ExtensionPointManager;

import es.gva.cit.jecwcompress.EcwException;
import es.gva.cit.jgdal.Gdal;
import es.gva.cit.jgdal.GdalBuffer;
import es.gva.cit.jgdal.GdalException;
import es.gva.cit.jgdal.GdalRasterBand;
import es.gva.cit.jgdal.GeoTransform;


/**
 * Driver para la escritura a través de Gdal.
 * Puede exportar un fichero de un formato a otro desde un GeoRasterFile
 * en cualquier formato soportado por la lectura a un formato que este incluido
 * en la lista supportedDrv.
 *
 * Puede salvar a disco en un formato que este incluido en la lista supportedDrv
 * obteniendo los datos que van siendo servidos desde el cliente. Este cliente
 * debe implementar un IDataWriter o tener un objeto que lo implemente. Inicialmente
 * le pasará los parámetros de la imagen de salida y cuando el driver comience a
 * escribir le irá solicitando más a través del método readData de IDataWriter.
 * El cliente será el que lleve el control de lo que va sirviendo y lo que le queda
 * por servir.
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class GdalWriter extends GeoRasterWriter {

	public static void register() {
		ExtensionPointManager extensionPoints = ToolsLocator.getExtensionPointManager();
		ExtensionPoint point = extensionPoints.get("RasterWriter");

		point.append("tif", "", GdalWriter.class);
		fileFeature.put("tif", new GTiffFeatures());

		point.append("img", "", GdalWriter.class);
		fileFeature.put("img", new HFAFeatures());

		point.append("bmp", "", GdalWriter.class);
		fileFeature.put("bmp", new BMPFeatures());

		point.append("pgm", "", GdalWriter.class);
		fileFeature.put("pgm", new PNM_PgmFeatures());

		point.append("ppm", "", GdalWriter.class);
		fileFeature.put("ppm", new PNM_PpmFeatures());

		point.append("mpl", "", GdalWriter.class);
		fileFeature.put("mpl", new ILWIS_MprFeatures());

		point.append("rst", "", GdalWriter.class);
		fileFeature.put("rst", new IDRISIFeatures());

		//La exportación no es correcta del todo
		//point.register("rmf", GdalWriter.class);
		//fileFeature.put("rmf", new RMFFeatures());

		//No salva datos. Siempre sale negra la imagen
		//point.register("aux", GdalWriter.class);
		//fileFeature.put("aux", new PAuxFeatures());
	}

	private es.gva.cit.jgdal.GdalDriver		drv;
	private Gdal 							dstDataset = null;
	private GdalRasterBand 					rband = null;
	private GeoTransform 					geot = null; //Datos de georeferenciación
	//private OGRSpatialReference 			oSRS; //Datos de proyección
	private GdalBuffer[]					bufBands = null;
	private int 							nBlocks = 0; //Número de bloques en Y en el que se divide la imagen para escribir
	private int 							anchoResto = 0; //Tamaño del último bloque de la imagen.
	private boolean							write = true; //Cuando está a true se puede escribir en la imagen de salida. Si está a false el proceso es interrumpido
	private int 							dataType = RasterBuffer.TYPE_UNDEFINED;

	/**
	 * Carga los parámetros de este driver.
	 */
	public void loadParams(String ident) {
		WriteFileFormatFeatures wfff = (WriteFileFormatFeatures)fileFeature.get(ident);
		wfff.loadParams();
		driverParams = wfff.getParams();
	}

	/**
	 * Constructor para la obtención de parámetros del driver
	 * @param drvType        Tipo de driver
	 */
	public GdalWriter(String fileName) {
		ident = RasterUtilities.getExtensionFromFileName(fileName);
		driver = ((WriteFileFormatFeatures)fileFeature.get(ident)).getDriverName();

		loadParams(ident);
	}

	/**
	 * Constructor para salvar datos servidos por el cliente
	 * @param dataWriter       	Objeto servidor de datos para el driver de escritura
	 * @param outFilename      	Fichero de salida
	 * @param blockSize        	Tamaño de bloque
	 * @param Extent           	extent
	 * @param compresion	   	Compresión si la tiene
	 * @param outSizeX		  	Tamaño de salida en X
	 * @param outSizeY			Tamaño de salida en Y
	 * @param dataType			Tipo de dato
	 * @throws GdalException
	 * @throws IOException
	 */
	public GdalWriter(	IDataWriter dataWriter,
			String outFileName,
			Integer nBands,
			AffineTransform at,
			Integer outSizeX,
			Integer outSizeY,
			Integer dataType,
			Params params,
			IProjection proj) throws GdalException, IOException {
		this(dataWriter, outFileName, nBands, at, outSizeX, outSizeY, dataType, params, proj, new Boolean(true));
	}

	/**
	 * Constructor para salvar datos servidos por el cliente
	 * @param dataWriter       	Objeto servidor de datos para el driver de escritura
	 * @param outFilename      	Fichero de salida
	 * @param blockSize        	Tamaño de bloque
	 * @param Extent           	extent
	 * @param compresion	   	Compresión si la tiene
	 * @param outSizeX		  	Tamaño de salida en X
	 * @param outSizeY			Tamaño de salida en Y
	 * @param dataType			Tipo de dato
	 * @param geo				Flag que dice si se salva con georreferenciación o sin ella
	 * @throws GdalException
	 * @throws IOException
	 */
	public GdalWriter(	IDataWriter dataWriter,
			String outFileName,
			Integer nBands,
			AffineTransform at,
			Integer outSizeX,
			Integer outSizeY,
			Integer dataType,
			Params params,
			IProjection proj,
			Boolean geo)throws GdalException, IOException {

		this.proj = proj;
		ident = outFileName.toLowerCase().substring(outFileName.lastIndexOf(".") + 1);
		driver = ((WriteFileFormatFeatures)fileFeature.get(ident)).getDriverName();
		this.dataType = dataType.intValue();
		this.at = at;
		percent = 0;

		this.dataWriter = dataWriter;
		this.outFileName = outFileName;

		this.sizeWindowX = outSizeX.intValue();
		this.sizeWindowY = outSizeY.intValue();

		if ((sizeWindowX < 0) || (sizeWindowY < 0))
			throw new IOException("Tamaño del fichero de salida erroneo.");

		this.nBands = nBands.intValue();

		//Calculamos la georeferenciación a partir del extend pasado por el cliente.

		geot = new GeoTransform();
		geot.adfgeotransform[0] = at.getTranslateX();
		geot.adfgeotransform[3] = at.getTranslateY();
		geot.adfgeotransform[1] = at.getScaleX();
		geot.adfgeotransform[5] = at.getScaleY();
		geot.adfgeotransform[2] = at.getShearX();
		geot.adfgeotransform[4] = at.getShearY();

		String outRmf = outFileName.substring(0, outFileName.lastIndexOf("."));
		if(geo.booleanValue())
			RasterUtilities.saveGeoInfo(outRmf, at, new Point2D.Double(sizeWindowX, sizeWindowY));

		if(params == null)
			loadParams(ident);
		else
			this.driverParams = params;

		init();
	}

	/**
	 * Añade la proyección Wkt con la que salvar.
	 * @param wkt
	 * @throws GdalException
	 */
	public void setWkt(String wkt) {
		if(dstDataset != null && wkt != null && wkt.compareTo("unknown") != 0)
			try {
				dstDataset.setProjection(wkt);
			} catch (GdalException e) {
				System.err.println("Proyección Wkt no asignada en GdalWriter");
				return;
			}
	}

	/**
	 * Asigna el tipo de driver con el que se salvará la imagen
	 * @param drvType        Tipo de driver
	 */
	public void setDriverType(String drvType) {
		this.driver = drvType;
	}

	/**
	 * Creación del dataset de destino.
	 * @throws EcwException
	 */
	private void init() throws GdalException {
		//Controlamos que el tipo de driver sea correcto
		if (driver == null)
			throw new GdalException("Tipo de driver sin especificar.");

		boolean okdrvtype = false;

		String[] types = GeoRasterWriter.getDriversType();
		for (int i = 0; i < GeoRasterWriter.getNTypes(); i++)
			if (driver.equals(types[i]))
				okdrvtype = true;

		if (okdrvtype == false)
			throw new GdalException("El tipo de driver " + driver + " no está soportado por GdalWriter.");

		//Obtenemos el driver y creamos el dataset del destino
		drv = Gdal.getDriverByName(driver);

		if (dstDataset != null) {
			dstDataset.close();
			dstDataset = null;
		}

		dstDataset = drv.create(outFileName, sizeWindowX, sizeWindowY,
				nBands, RasterUtilities.getGdalTypeFromRasterBufType(dataType), gdalParamsFromRasterParams(driverParams));

		dstDataset.setGeoTransform(geot);

		int blockSize = RasterLibrary.blockHeight;

		nBlocks = (sizeWindowY / blockSize);
		anchoResto = sizeWindowY - (nBlocks * blockSize);
	}

	public void anotherFile(String fileName)throws GdalException {
		dstDataset = drv.create(fileName, sizeWindowX, sizeWindowY,
				nBands, RasterUtilities.getGdalTypeFromRasterBufType(dataType), gdalParamsFromRasterParams(driverParams));
	}

	/**
	 * Convierte los parámetros obtenidos desde el objeto params a parametros
	 * comprensibles por la librería gdal
	 * @param p Params
	 * @return Array de parámetros
	 */
	public String[] gdalParamsFromRasterParams(Params p) {
		if (p == null)
			return null;
		ArrayList paramList = new ArrayList();
		Param phot = p.getParamById("photometric");
		if (phot != null)
			paramList.add("PHOTOMETRIC=" + phot.list[((Integer) phot.defaultValue).intValue()]);
		Param inter = p.getParamById("interleave");
		if (inter != null)
			paramList.add("INTERLEAVE=" + inter.list[((Integer) inter.defaultValue).intValue()]);
		Param comp = p.getParamById("compression");// GIF LZW, ...
		if (comp != null)
			paramList.add("COMPRESS=" + comp.list[((Integer) comp.defaultValue).intValue()]);
		Param comp1 = p.getParamById("compress"); // HFA (YES, NO)
		if (comp1 != null)
			paramList.add("COMPRESS=" + comp1.list[((Integer) comp1.defaultValue).intValue()]);
		Param rrd = p.getParamById("rrd");
		if (rrd != null)
			paramList.add("HFA_USE_RRD=" + rrd.list[((Integer) rrd.defaultValue).intValue()]);
		Param mtw = p.getParamById("Mtw");
		if (mtw != null)
			paramList.add("MTW=" + mtw.list[((Integer) mtw.defaultValue).intValue()]);
		Param tw = p.getParamById("Tile Width");
		if (tw != null)
			paramList.add("BLOCKXSIZE=" + tw.list[((Integer) tw.defaultValue).intValue()]);
		Param th = p.getParamById("Tile Height");
		if (th != null)
			paramList.add("BLOCKYSIZE=" + th.list[((Integer) th.defaultValue).intValue()]);
		Param qt = p.getParamById("quality");
		if (qt != null)
			paramList.add("QUALITY=" + qt.defaultValue);
		Param prog = p.getParamById("progressive");
		if (prog != null)
			paramList.add("PROGRESSIVE=" + prog.defaultValue);

		if (paramList.size() == 0)
			return null;

		String[] result = new String[paramList.size()];
		for (int i = 0; i < result.length; i++)
			result[i] = (String) paramList.get(i);
		return result;
	}

	/**
	 * Escritura de datos tipo Byte.
	 * @param sizeY Alto del bloque que se escribe.
	 * @param posicionY Posicióny a partir desde donde se comienza.
	 */
	public void writeByteBand(int sizeY, int posicionY) {
		byte[][] buftmp = dataWriter.readByteData(sizeWindowX, sizeY);
		for(int iBand = 0; iBand < nBands; iBand ++)
			bufBands[iBand].buffByte = new byte[buftmp[iBand].length];

		//Escribimos el bloque destino
		for (int iBand = 0; iBand < buftmp.length; iBand++)
			for (int i = 0; i < buftmp[iBand].length; i++)
				bufBands[iBand].buffByte[i] = buftmp[iBand][i];

		for (int iBand = 0; iBand < buftmp.length; iBand++)
			try {
				rband = dstDataset.getRasterBand(iBand + 1);
				rband.writeRaster(0, posicionY, sizeWindowX, sizeY, bufBands[iBand], Gdal.GDT_Byte);
			} catch (GdalException e) {
				//No se está escribiendo ...
			}
	}

	/**
	 * Escritura de datos tipo Short.
	 * @param sizeY Alto del bloque que se escribe.
	 * @param posicionY Posicióny a partir desde donde se comienza.
	 */
	public void writeShortBand(int sizeY, int posicionY) {
		short[][] buftmp = dataWriter.readShortData(sizeWindowX, sizeY);
		for(int iBand = 0; iBand < nBands; iBand ++)
			bufBands[iBand].buffShort = new short[buftmp[iBand].length];

		//Escribimos el bloque destino
		for (int iBand = 0; iBand < nBands; iBand++)
			for (int i = 0; i < buftmp[iBand].length; i++)
				bufBands[iBand].buffShort[i] = buftmp[iBand][i];

		for (int iBand = 0; iBand < nBands; iBand++)
			try {
				rband = dstDataset.getRasterBand(iBand + 1);
				rband.writeRaster(0, posicionY, sizeWindowX, sizeY, bufBands[iBand], Gdal.GDT_Int16);
			} catch (GdalException e) {
				//No se está escribiendo ...
			}
	}

	/**
	 * Escritura de datos tipo Int.
	 * @param sizeY Alto del bloque que se escribe.
	 * @param posicionY Posicióny a partir desde donde se comienza.
	 */
	public void writeIntBand(int sizeY, int posicionY) {
		int[][] buftmp = dataWriter.readIntData(sizeWindowX, sizeY);
		for(int iBand = 0; iBand < nBands; iBand ++)
			bufBands[iBand].buffInt = new int[buftmp[iBand].length];

		//Escribimos el bloque destino
		for (int iBand = 0; iBand < buftmp.length; iBand++)
			for (int i = 0; i < buftmp[iBand].length; i++)
				bufBands[iBand].buffInt[i] = buftmp[iBand][i];

		for (int iBand = 0; iBand < buftmp.length; iBand++)
			try {
				rband = dstDataset.getRasterBand(iBand + 1);
				rband.writeRaster(0, posicionY, sizeWindowX, sizeY, bufBands[iBand], Gdal.GDT_Int32);
			} catch (GdalException e) {
				//No se está escribiendo ...
			}
	}

	/**
	 * Escritura de datos tipo Float.
	 * @param sizeY Alto del bloque que se escribe.
	 * @param posicionY Posicióny a partir desde donde se comienza.
	 */
	public void writeFloatBand(int sizeY, int posicionY) {
		float[][] buftmp = dataWriter.readFloatData(sizeWindowX, sizeY);
		for(int iBand = 0; iBand < nBands; iBand ++)
			bufBands[iBand].buffFloat = new float[buftmp[iBand].length];

		//Escribimos el bloque destino
		for (int iBand = 0; iBand < buftmp.length; iBand++)
			for (int i = 0; i < buftmp[iBand].length; i++)
				bufBands[iBand].buffFloat[i] = buftmp[iBand][i];

		for (int iBand = 0; iBand < buftmp.length; iBand++)
			try {
				rband = dstDataset.getRasterBand(iBand + 1);
				rband.writeRaster(0, posicionY, sizeWindowX, sizeY, bufBands[iBand], Gdal.GDT_Float32);
			} catch (GdalException e) {
				//No se está escribiendo ...
			}
	}

	/**
	 * Escritura de datos tipo Double.
	 * @param sizeY Alto del bloque que se escribe.
	 * @param posicionY Posicióny a partir desde donde se comienza.
	 */
	public void writeDoubleBand(int sizeY, int posicionY) {
		double[][] buftmp = dataWriter.readDoubleData(sizeWindowX, sizeY);
		for(int iBand = 0; iBand < nBands; iBand ++)
			bufBands[iBand].buffDouble = new double[buftmp[iBand].length];

		//Escribimos el bloque destino
		for (int iBand = 0; iBand < buftmp.length; iBand++)
			for (int i = 0; i < buftmp[iBand].length; i++)
				bufBands[iBand].buffDouble[i] = buftmp[iBand][i];

		for (int iBand = 0; iBand < buftmp.length; iBand++)
			try {
				rband = dstDataset.getRasterBand(iBand + 1);
				rband.writeRaster(0, posicionY, sizeWindowX, sizeY, bufBands[iBand], Gdal.GDT_Float64);
			} catch (GdalException e) {
				//No se está escribiendo ...
			}
	}
	/**
	 * Escritura para tipo de dato ARGB.
	 * @param sizeY Alto del bloque que se escribe.
	 * @param posicionY Posicióny a partir desde donde se comienza.
	 */
	public void writeARGBBand(int sizeY, int posicionY)
			throws InterruptedException, OutOfMemoryError {
		int[] buftmp = dataWriter.readARGBData(sizeWindowX, sizeY, 0);
		for(int iBand = 0; iBand < nBands; iBand ++)
			bufBands[iBand].buffByte = new byte[buftmp.length];

		//Escribimos el bloque destino
		for (int i = 0; i < buftmp.length; i++) {
			bufBands[0].buffByte[i] = (byte) (((buftmp[i] & 0xff0000) >> 16) &
					0xff);
			bufBands[1].buffByte[i] = (byte) (((buftmp[i] & 0xff00) >> 8) & 0xff);
			bufBands[2].buffByte[i] = (byte) ((buftmp[i] & 0xff) & 0xff);
		}

		try {
			rband = dstDataset.getRasterBand(1);
			rband.writeRaster(0, posicionY, sizeWindowX, sizeY, bufBands[0],
					Gdal.GDT_Byte);
			rband = dstDataset.getRasterBand(2);
			rband.writeRaster(0, posicionY, sizeWindowX, sizeY, bufBands[1],
					Gdal.GDT_Byte);
			rband = dstDataset.getRasterBand(3);
			rband.writeRaster(0, posicionY, sizeWindowX, sizeY, bufBands[2],
					Gdal.GDT_Byte);
		} catch (GdalException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Escribe tres bandas en el GDALRasterBand desde el IDataWriter con una
	 * altura definida por sizeY.
	 * @param buftmp        Buffer
	 * @param sizeY        Altura en pixels del bloque leido
	 * @param posicionY        Posición y a partir de la cual se escribe en el GDALRasterBand destino
	 */
	private void writeBands(int sizeY, int posicionY)
			throws InterruptedException, OutOfMemoryError {
		//leemos el bloque origen

		switch(dataType){
		case RasterBuffer.TYPE_IMAGE:
			writeARGBBand(sizeY, posicionY);
			break;
		case RasterBuffer.TYPE_BYTE:
			writeByteBand(sizeY, posicionY);
			break;
		case RasterBuffer.TYPE_SHORT:
			writeShortBand(sizeY, posicionY);
			break;
		case RasterBuffer.TYPE_INT:
			writeIntBand(sizeY, posicionY);
			break;
		case RasterBuffer.TYPE_FLOAT:
			writeFloatBand(sizeY, posicionY);
			break;
		case RasterBuffer.TYPE_DOUBLE:
			writeDoubleBand(sizeY, posicionY);
			break;
		}
	}

	/**
	 * Función que gestiona la lectura desde el origen y la escritura
	 * de Gdal sobre el fichero destino.
	 * @param mode        Modo de escritura
	 * @throws IOException
	 */
	private void write(int mode) throws IOException, InterruptedException,
			OutOfMemoryError {
		RasterTask task = RasterTaskQueue.get(Thread.currentThread().toString());

		bufBands = new GdalBuffer[nBands];
		for(int iBand = 0; iBand < nBands; iBand ++)
			bufBands[iBand] = new GdalBuffer();

		int blockSize = RasterLibrary.blockHeight;

		percent = 0;
		nBlocks = (sizeWindowY / blockSize);
		int increment = (blockSize * 100) / sizeWindowY;

		if (mode == GeoRasterWriter.MODE_DATAWRITE)
			for (int iBlock = 0; iBlock < nBlocks; iBlock++) {
				if(task.getEvent() != null)
					task.manageEvent(task.getEvent());
				int posicionY = iBlock * blockSize;
				if(write)
					writeBands( blockSize, posicionY);
				percent = (iBlock + 1) * increment;
			}

		if (anchoResto != 0)
			if (mode == GeoRasterWriter.MODE_DATAWRITE) {
				if(task.getEvent() != null)
					task.manageEvent(task.getEvent());
				int posicionY = nBlocks * blockSize;
				if(write)
					writeBands(anchoResto, posicionY);
				percent = nBlocks * increment;
			}

	}

	/**
	 * Realiza la función de compresión a partir de un GeoRasterFile.
	 * @throws IOException
	 */
	public void fileWrite() throws IOException, InterruptedException {
		write(GeoRasterWriter.MODE_FILEWRITE);
	}

	/**
	 * Realiza una copia en el formato especificado.
	 * @throws IOException
	 */
	public static void createCopy(es.gva.cit.jgdal.GdalDriver driverDst, String dst, String src,
			boolean bstrict, String[] params, IProjection proj) throws IOException, GdalException {
		if (dst == null || src == null)
			throw new IOException("No se ha asignado un fichero de entrada.");

		GdalDriver gdalFile;
		try {
			gdalFile = new GdalDriver(proj, src);
			Gdal dstDataset = driverDst.createCopy(dst, gdalFile.getNative(), bstrict, params);
			if(	dst.endsWith(".jpg") ||
				dst.endsWith(".jpeg") ||
				dst.endsWith(".png"))
				RasterUtilities.createWorldFile(dst, gdalFile.getExtent(), gdalFile.getWidth(), gdalFile.getHeight());
			gdalFile.close();
			dstDataset.close();
		} catch (NotSupportedExtensionException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Realiza la escritura de datos con los datos que le pasa el cliente.
	 * @throws IOException
	 * @throws RmfSerializerException
	 */
	public void dataWrite() throws IOException, InterruptedException,
			OutOfMemoryError {
		if (dataWriter == null)
			throw new IOException("No se ha obtenido un objeto de entrada para la escritura valido.");

		write(GeoRasterWriter.MODE_DATAWRITE);

		if (driverParams.getParamById("tfw") != null &&
				driverParams.getParamById("tfw").defaultValue instanceof Boolean &&
				((Boolean) driverParams.getParamById("tfw").defaultValue).booleanValue() == true)
			if (at != null)
				RasterUtilities.createWorldFile(this.outFileName, at, sizeWindowX, sizeWindowY);

		if (colorInterp != null)
			try {
				RasterDataset.saveObjectToRmfFile(outFileName, DatasetColorInterpretation.class, colorInterp);
			} catch (RmfSerializerException e) {
				throw new IOException("No se ha podido guardar la interpretacion de color");
			}
	}

	/**
	 * Cancela el salvado de datos.
	 * @throws GdalException
	 */
	public void writeClose() {
		try {
			if(dstDataset != null)
				dstDataset.close();
		} catch (GdalException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Cancela el salvado de datos.
	 */
	public void writeCancel() {
		write = false;
	}

	/**
	 * Obtiene el valor a la variable write que estará a true cuando se está escribiendo
	 *  o puede escribirse la imagen de salida. El cancelar la operación de escritura
	 * pondrá esta variable a false deteniendose la escritura y cerrandose el dataset
	 * de salida.
	 * @return True si puede escribirse y false si no puede
	 */
	public boolean isWrite() {
		return write;
	}

	/**
	 * Asigna el valor a la variable write que estará a true cuando se está escribiendo
	 *  o puede escribirse la imagen de salida. El cancelar la operación de escritura
	 * pondrá esta variable a false deteniendose la escritura y cerrandose el dataset
	 * de salida.
	 * @param write Variable booleana. True si puede escribirse y false si no puede
	 */
	public void setWrite(boolean write) {
		this.write = write;
	}

	/**
	 * Asigna los parámetros del driver modificados por el cliente.
	 * @param Params
	 */
	public void setParams(Params params) {
		this.driverParams = params;

		int blockSize = 256;
		try {
			Param param = driverParams.getParamById("blocksize");
			blockSize = Integer.parseInt(param.list[((Integer)param.defaultValue).intValue()]);
			nBlocks = (sizeWindowY / blockSize);
			anchoResto = sizeWindowY - (nBlocks * blockSize);
		}catch(NumberFormatException e) {
			//Se queda con el valor de inicialización
		}
	}
}
