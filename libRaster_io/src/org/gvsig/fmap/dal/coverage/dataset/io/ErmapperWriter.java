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
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;

import org.cresques.cts.IProjection;
import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.dataset.GeoRasterWriter;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.IDataWriter;
import org.gvsig.raster.dataset.Params;
import org.gvsig.raster.dataset.WriteFileFormatFeatures;
import org.gvsig.raster.dataset.Params.Param;
import org.gvsig.raster.util.RasterUtilities;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.extensionpoint.ExtensionPoint;
import org.gvsig.tools.extensionpoint.ExtensionPointManager;

import es.gva.cit.jecwcompress.EcwException;
import es.gva.cit.jecwcompress.JniObject;
import es.gva.cit.jecwcompress.NCSEcwCompressClient;
import es.gva.cit.jecwcompress.ReadCallBack;
/**
 * Driver para la compresión en formato Ecw.
 *
 * Puede exportar un fichero desde un GeoRasterFile en cualquier formato soportado
 * por los drivers de lectura a uno en formato Ecw.
 *
 * Puede salvar a disco en formato Ecw obteniendo los datos que van siendo servidos
 * desde el cliente. Este cliente debe implementar un IDataWriter o tener un objeto
 * que lo implemente. Inicialmente le pasará los parámetros de la imagen de salida
 * y cuando el driver comience a escribir le irá solicitando más a través del método
 * readData de IDataWriter. El cliente será el que lleve el control de lo que va
 * sirviendo y lo que le queda por servir.
 * @author Nacho Brodin (brodin_ign@gva.es)
 */
public class ErmapperWriter extends GeoRasterWriter {

	public static void register() {
		ExtensionPointManager extensionPoints =ToolsLocator.getExtensionPointManager();
		ExtensionPoint point=extensionPoints.get("RasterWriter");
		WriteFileFormatFeatures features = null;

		String os = System.getProperties().getProperty("os.version");
		if (os.startsWith("2.4")){
			point.append("ecw", "", ErmapperWriter.class);
			features = new WriteFileFormatFeatures("Ecw", "ecw", new int[]{3}, null, ErmapperWriter.class);
			fileFeature.put("ecw", features);
		}
		point.append("jp2", "", ErmapperWriter.class);
		features = new WriteFileFormatFeatures("Jpeg2000", "jp2", new int[]{3}, null, ErmapperWriter.class);
		fileFeature.put("jp2", features);
	}

	public final int 				windowSizeX = 386;
	public final int 				windowSizeY = 220;
	public final int 				panelSizeX = 358;
	public final int 				panelSizeY = 125;
	public final String 			panelLayout = "BorderLayout";
	private NCSEcwCompressClient 	compressclient = null;
	private Reader 					readerObj;
	private double 					pixelSizeX;
	private double 					pixelSizeY;
	private double 					geoCoordOrigenX;
	private double 					geoCoordOrigenY;
	private boolean 				consulta = false;

	/**
	 * Carga los parámetros de este driver.
	 */
	public void loadParams() {
		WriteFileFormatFeatures wfff = new WriteFileFormatFeatures();
		wfff.loadParams();
		driverParams = wfff.getParams();

		driverParams.setParam(	"compression",
				new Integer(10),
				Params.SLIDER,
				new String[]{ "1", "20", "10", "1", "5" }); //min, max, valor defecto, intervalo pequeño, intervalo grande;

		driverParams.setParam(	"format",
				new Integer(4),
				Params.CHOICE,
				new String[]{ "NONE", "UINT8", "YUV", "MULTI", "RGB"});
	}

	/**
	 * Constructor para la obtención de parámetros del driver.
	 */
	public ErmapperWriter(String fileName) {
		ident = RasterUtilities.getExtensionFromFileName(fileName);
		driver = ((WriteFileFormatFeatures)fileFeature.get(ident)).getDriverName();

		loadParams();

		consulta = true;
	}

	/**
	 * Constructor para la lectura de datos desde el objeto cliente a partir de un
	 * viewport dado.
	 * @param dataWriter Objeto que sirve datos para el escritor
	 * @param outFileName Fichero de salida
	 * @param nBands Número de bandas
	 * @param at
	 * @param outSizeX
	 * @param outSizeY
	 * @param dataType
	 * @param params
	 * @param proj
	 * @param geo
	 * @throws EcwException
	 * @throws IOException
	 */
	public ErmapperWriter(	IDataWriter dataWriter,
			String outFileName,
			Integer nBands,
			AffineTransform at,
			Integer outSizeX,
			Integer outSizeY,
			Integer dataType,
			Params params,
			IProjection proj,
			Boolean geo) throws EcwException, IOException {

		//La libreria de ECW no se traga caracteres raros al escribir. Los cambiamos por X de momento y ya se apaña el usuario

		String ext = RasterUtilities.getExtensionFromFileName(outFileName);
		String fname = outFileName.substring(outFileName.lastIndexOf(File.separator) + 1, outFileName.lastIndexOf(".")).replaceAll("[^a-zA-Z0-9_]", "X");
		outFileName = outFileName.substring(0, outFileName.lastIndexOf(File.separator) + 1) + fname + "." + ext;

		this.proj = proj;
		ident = RasterUtilities.getExtensionFromFileName(outFileName);
		driver = ((WriteFileFormatFeatures)fileFeature.get(ident)).getDriverName();
		this.dataType = dataType.intValue();

		if (nBands.intValue() <= 0)
			throw new EcwException("Número de bandas erroneo.");

		this.outFileName = outFileName;
		this.dataWriter = dataWriter;

		this.nBands = nBands.intValue();

		this.sizeWindowX = outSizeX.intValue();
		this.sizeWindowY = outSizeY.intValue();

		//Calculamos la georeferenciación a partir del extend pasado por el cliente.
		geoCoordOrigenX = at.getTranslateX();
		geoCoordOrigenY = at.getTranslateY();

		pixelSizeX = (at.getScaleX() > 0) ? at.getScaleX() : -at.getScaleX();
		pixelSizeY = (at.getScaleY() < 0) ? at.getScaleY() : -at.getScaleY();

		String outRmf = RasterUtilities.getNameWithoutExtension(outFileName);
		if(geo.booleanValue())
			RasterUtilities.saveGeoInfo(outRmf, at, new Point2D.Double(sizeWindowX, sizeWindowY));

		if (pixelSizeX == 0)
			pixelSizeX = 1.0;

		if (pixelSizeY == 0)
			pixelSizeY = 1.0;

		if(params == null)
			loadParams();
		else
			driverParams = params;

		init();
	}

	/**
	 * Inicialización de los parámetros del compresor que serán obtenidos
	 * de PxRaster.
	 * @throws EcwException
	 */
	private void init() throws EcwException {
		percent = 0;
		int comp = ((Integer)(driverParams.getParamById("compression")).defaultValue).intValue();
		Param param = driverParams.getParamById("format");
		String format = param.list[((Integer)param.defaultValue).intValue()];
		if ( comp == 0 )
			driverParams.changeParamValue("compression", "1");

		if (compressclient == null)
			compressclient = new NCSEcwCompressClient();

		compressclient.setOutputFilename(outFileName);
		compressclient.setInputFilename(inFileName);
		compressclient.setTargetCompress(comp);
		compressclient.setInOutSizeX(sizeWindowX);
		compressclient.setInOutSizeY(sizeWindowY);
		compressclient.setInputBands(nBands);
		compressclient.setCompressFormat(convertFormatToInt(format));

		/* boolean georef = new Boolean(((Param)driverParams.getParamById("georef")).defaultValue).booleanValue();
				if (georef) {*/
		compressclient.setCellIncrementX(pixelSizeX);
		compressclient.setCellIncrementY(pixelSizeY);
		compressclient.setOriginX(geoCoordOrigenX);
		compressclient.setOriginY(geoCoordOrigenY);
		//}

		compressclient.setCellSizeUnits(1);
		int blocksize = RasterLibrary.blockHeight;

		if (dataWriter != null)
			readerObj = new Reader(dataWriter, compressclient, sizeWindowX,
					sizeWindowY, blocksize, nBands, this, dataType);
	}

	/**
	 * Convierte la cadena que representa el formato en un valor númerico
	 * que entiende el driver.
	 * @param format Cadena que representa el formato
	 * @return Entero que representa a la cadena
	 */
	private int convertFormatToInt(String format) {
		if(format.compareTo("NONE") == 0)
			return 0;
		if(format.compareTo("UINT8") == 0)
			return 1;
		if(format.compareTo("YUV") == 0)
			return 2;
		if(format.compareTo("MULTI") == 0)
			return 3;
		if(format.compareTo("RGB") == 0)
			return 4;
		return -1;
	}
	/**
	 * A partir de un elemento que contiene una propiedad y un valor
	 * lo parsea y asigna el valor a su variable.
	 * @param propValue        elemento con la forma propiedad=valor
	 */
	private void readProperty(String propValue) {
		String prop = propValue.substring(0, propValue.indexOf("="));

		if (propValue.startsWith(prop)) {
			String value = propValue.substring(propValue.indexOf("=") + 1, propValue.length());

			if ((value != null) && !value.equals("")) {
				if (prop.equals("BLOCKSIZE"))
					driverParams.changeParamValue("blocksize", value);
				if (prop.equals("FORMAT"))
					driverParams.changeParamValue("format", value);
				if (prop.equals("COMPRESSION"))
					driverParams.changeParamValue("compression", value);
			}
		}
	}

	/**
	 * Asigna propiedades al driver a partir de un vector de
	 * strings donde cada elemento tiene la estructura de
	 * propiedad=valor.
	 * @param props        Propiedades
	 */
	public void setProps(String[] props) {
		for (int iProps = 0; iProps < props.length; iProps++)
			readProperty(props[iProps]);

		loadParams();

		try {
			if (!consulta)
				init();
		} catch (EcwException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Realiza la función de compresión a partir de un GeoRasterFile.
	 * @throws IOException
	 */
	public void fileWrite() throws IOException {
		/* try {
						compressclient.NCSEcwCompressOpen(false);
						compressclient.NCSEcwCompress(readerObj);
				} catch (EcwException e) {
						e.printStackTrace();
				}*/
		//TODO: FUNCIONALIDAD: Compresión a partir de un dataset. De momento no es necesario y es posible que nunca lo sea.
	}

	/**
	 * Realiza la función de compresión a partir de los datos pasados por el cliente.
	 * @throws IOException
	 */
	public void dataWrite() throws IOException, InterruptedException {
		if (dataWriter == null)
			throw new IOException("No se ha obtenido un objeto para la lectura valido.");

		try {
			compressclient.NCSEcwCompressOpen(false);
			compressclient.NCSEcwCompress(readerObj);
		} catch (EcwException e) {
			throw new IOException("Error en la compresión.");
		}
	}

	/**
	 * Cierra el compresor ecw.
	 */
	public void writeClose() {
		try {
			compressclient.NCSEcwCompressClose();
		} catch (EcwException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Cancela el compresor ecw.
	 */
	public void writeCancel() {
		try {
			if(readerObj != null)
				readerObj.setWrite(false);
			compressclient.NCSEcwCompressCancel();
		} catch (EcwException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.GeoRasterWriter#setWkt(java.lang.String)
	 */
	public void setWkt(String wkt) {
		//TODO: FUNCIONALIDAD: La asignación de proyección en Ecw falla haciendo petar la aplicación.
		/*if(compressclient != null && wkt != null && wkt.compareTo("unknown") != 0) {
			WktUtils wktutil = new WktUtils(wkt);
			compressclient.setProjection("EPSG:" + wktutil.getEPSG());
		}*/
	}

}


/**
 * Clase que se encarga de la lectura de datos que serán escritos. Hereda de JniObject
 * para asegurar  para asegurar que el interfaz de la libreria tiene acceso a variables
 * necesarias para la petición de datos cuando vacia el buffer. Implementa ReadCallBack
 * para obligar escribir el método loadBuffer encargado de servir los datos cuando el
 * buffer se vacia.
 *
 */
class Reader extends JniObject implements ReadCallBack {
	private NCSEcwCompressClient	compressclient = null;
	private int 					width;
	//private int 					height;
	//private int 					ulX;
	//private int 					ulY;
	private IDataWriter 			dataWriter = null;
	//private GdalRasterBand 			rband = null;
	private GeoRasterWriter			writer = null;
	private int 					blockSizeRead = 1; //Alto del bloque leido de la imagen origen
	private int 					countLine = 1; //Contador de líneas procesadas en cada lectura de bloque
	byte[][] 						buf = null;
	byte[] 							bufband1 = null;
	byte[] 							bufband2 = null;
	byte[] 							bufband3 = null;
	byte[] 							bufband4 = null;
	byte[] 							bufband5 = null;
	byte[] 							bufband6 = null;
	int[] 							dataBuffer = null;
	byte[][] 						byteBuffer = null;
	private int 					lineasBloqueFinal = 0; //Número de líneas leidas del bloque final
	private int 					nBlocks = 0; //Número de bloques completos
	private int 					countBlock = 1; //Contador de bloques completos procesados
	private int 					nBands = 0;
	private	boolean					write = true; //Cuando está a true se puede escribir en la imagen de salida. Si está a false el proceso es interrumpido
	private int						dataType = IBuffer.TYPE_BYTE;

	/**
	 * Constructor para que los datos sean servidos desde el cliente a través de un
	 * IDataWriter.
	 * @param dataWriter        Objeto servidor de datos del driver
	 * @param compressclient        Objeto que representa al compresor ecw
	 * @param width        Ancho de la imagen
	 * @param height        Alto de la imagen
	 * @param blockSizeRead        Altura del bloque en la lectura
	 * @param nBands        Número de bandas
	 */
	public Reader(IDataWriter dataWriter, NCSEcwCompressClient compressclient,
			int width, int height, int blockSizeRead, int nBands,
			GeoRasterWriter writer, int dataType) {
		this.compressclient = compressclient;
		this.width = width;
		//this.height = height;
		this.dataWriter = dataWriter;
		this.nBands = nBands;
		this.writer = writer;
		this.dataType = dataType;

		if (blockSizeRead != 0)
			this.blockSizeRead = blockSizeRead;

		nBlocks = (height / this.blockSizeRead);
		lineasBloqueFinal = height - (nBlocks * this.blockSizeRead);
	}

	/**
	 * Lectura de bandas llamando al objeto cliente para que nos cargue el buffer
	 * @param width        Ancho de la imagen
	 * @param height        Alto de la imagen
	 */
	/*private void readBands(int width, int height) {
		if(dataType == IBuffer.TYPE_IMAGE)
			dataBuffer = dataWriter.readARGBData(width, height, 0);
		if(dataType == IBuffer.TYPE_BYTE) {
			byte[][] b = dataWriter.readByteData(width, height);

		}
	}*/

	/**
	 * Método obligado por el interfaz y que es llamado desde C cuando el
	 * compresor necesita que le sirvan más datos.
	 */
	public void loadBuffer() {
		int lineasLeidas = 0;

		//si se leen bloques completos lineasLeidas es == al tamaño de bloque sino
		//es que es el último con lo que será del tamaño del bloque final
		if (countBlock <= nBlocks)
			lineasLeidas = blockSizeRead;
		else
			lineasLeidas = lineasBloqueFinal;

		try {
			// Leemos un bloque nuevo cuando se han procesado todas las líneas
			// del anterior
			if ((nNextLine % blockSizeRead) == 0) {
				if (dataWriter != null) {
					if (dataType == IBuffer.TYPE_IMAGE)
						dataBuffer = dataWriter.readARGBData(width,
								lineasLeidas, 0);
					if (dataType == IBuffer.TYPE_BYTE)
						byteBuffer = dataWriter.readByteData(width,
								lineasLeidas);
				}

				countLine = 0;
				countBlock++;
			}

			if (dataType == IBuffer.TYPE_IMAGE)
				loadBufferFromImageDataType(dataBuffer);
			if (dataType == IBuffer.TYPE_BYTE)
				loadBufferFromByteDataType(byteBuffer);
		} catch (InterruptedException e) {

		}

		/* for (int iBand = 0; iBand < this.nBands; iBand++) {
						for (int pos = 0; pos < width; pos++) {
								if (grf != null && write) {
										if (iBand == 0)
												compressclient.buffer[pos + (width * iBand)] =
													bufband1[pos + (width * countLine)];

										if (iBand == 1)
												compressclient.buffer[pos + (width * iBand)] =
													bufband2[pos + (width * countLine)];

										if (iBand == 2)
												compressclient.buffer[pos + (width * iBand)] =
													bufband3[pos + (width * countLine)];

										if (iBand == 3)
												compressclient.buffer[pos + (width * iBand)] =
													bufband4[pos + (width * countLine)];

										if (iBand == 4)
												compressclient.buffer[pos + (width * iBand)] =
													bufband5[pos + (width * countLine)];

										if (iBand == 5)
												compressclient.buffer[pos + (width * iBand)] =
													bufband6[pos + (width * countLine)];

								} else {
										if (iBand == 0)
												compressclient.buffer[pos + (width * iBand)] =
													(byte) ((dataBuffer[pos + (width * countLine)] & 0xff0000) >> 16);


										if (iBand == 1)
												compressclient.buffer[pos + (width * iBand)] =
													(byte) ((dataBuffer[pos + (width * countLine)] & 0xff00) >> 8);


										if (iBand == 2)
												compressclient.buffer[pos + (width * iBand)] =
													(byte) (dataBuffer[pos + (width * countLine)] & 0xff);

								}

								if ((pos == (width - 1)) && (iBand == (this.nBands - 1))) {
										countLine++;
								}
						}
				}*/
	}

	/**
	 * Carga el buffer de datos desde elementos de tipo entero con contenido ARGB
	 * @param data Array de datos enteros
	 */
	private void loadBufferFromImageDataType(int[] data) {
		for (int iBand = 0; iBand < this.nBands; iBand++)
			for (int pos = 0; pos < width; pos++) {
				switch(iBand) {
				case 0:
					compressclient.buffer[pos + (width * iBand)] =
						(byte) ((data[pos + (width * countLine)] & 0xff0000) >> 16);
					break;
				case 1:
					compressclient.buffer[pos + (width * iBand)] =
						(byte) ((data[pos + (width * countLine)] & 0xff00) >> 8);
					break;
				case 2:
					compressclient.buffer[pos + (width * iBand)] =
						(byte) (data[pos + (width * countLine)] & 0xff);
					break;
				}
				if ((pos == (width - 1)) && (iBand == (this.nBands - 1)))
					countLine++;
			}
	}

	/**
	 * Carga el buffer de datos a comprimir desde un buffer byte[][] donde la primera dimensión
	 * es el número de bandas y la segunda los elementos en lista (Ancho X Alto)
	 * @param b Buffer de datos de tipo byte[][]
	 */
	private void loadBufferFromByteDataType(byte[][] b) {
		for (int iBand = 0; iBand < this.nBands; iBand++)
			for (int pos = 0; pos < width; pos++) {
				compressclient.buffer[pos + (width * iBand)] = b[iBand][pos + (width * countLine)];
				if ((pos == (width - 1)) && (iBand == (this.nBands - 1)))
					countLine++;
			}
	}

	/**
	 * Método obligado por el interfaz y que es llamado desde C cuando el
	 * compresor actualiza el porcentaje de compresión
	 */
	public void updatePercent() {
		writer.setPercent(compressclient.getPercent());
		//System.out.println(compressclient.getPercent() + "%");
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

}
