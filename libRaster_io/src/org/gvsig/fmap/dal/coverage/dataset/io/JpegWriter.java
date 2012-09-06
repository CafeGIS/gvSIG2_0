/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2008 IVER T.I. and Generalitat Valenciana.
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
import java.io.File;
import java.io.IOException;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.dal.coverage.dataset.io.features.JpegFeatures;
import org.gvsig.raster.dataset.GeoRasterWriter;
import org.gvsig.raster.dataset.IDataWriter;
import org.gvsig.raster.dataset.Params;
import org.gvsig.raster.dataset.WriteFileFormatFeatures;
import org.gvsig.raster.util.RasterUtilities;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.extensionpoint.ExtensionPoint;
import org.gvsig.tools.extensionpoint.ExtensionPointManager;

import es.gva.cit.jgdal.Gdal;
import es.gva.cit.jgdal.GdalDriver;
import es.gva.cit.jgdal.GdalException;
/**
 * Driver para la escritura de Jpeg.
 * Este driver utiliza GdalWriter para salvar Jpeg.
 * La escritura de un jpeg no es posible utilizando un servidor de datos
 * como el que usan los drivers comunes por lo que será necesario salvar antes
 * a Tif con el driver de Gdal para posteriormente convertir la imagen completa
 * a jpg.
 *
 * @version 22/07/2008
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class JpegWriter extends GeoRasterWriter {

	// Datos de registro de drivers
	public static void register() {
		ExtensionPointManager extensionPoints =ToolsLocator.getExtensionPointManager();
		ExtensionPoint point=extensionPoints.get("RasterWriter");
		point.append("jpg", "", JpegWriter.class);
		fileFeature.put("jpg", new JpegFeatures());
	}

	private GdalWriter gdalWriter = null;
	private String     outTif     = null;
	private String     outJpg     = null;

	/**
	 * Carga los parámetros de este driver.
	 */
	public void loadParams(String ident) {
		WriteFileFormatFeatures wfff = (WriteFileFormatFeatures) fileFeature.get(ident);
		wfff.loadParams();
		driverParams = wfff.getParams();
	}

	/**
	 * Constructor para la obtención de parámetros del driver
	 * @param drvType Tipo de driver
	 */
	public JpegWriter(String fileName) {
		ident = RasterUtilities.getExtensionFromFileName(fileName);
		driver = ((WriteFileFormatFeatures) fileFeature.get(ident)).getDriverName();
		gdalWriter = new GdalWriter(fileName);

		loadParams(ident);
	}

	/**
	 * Constructor para salvar datos servidos por el cliente
	 * @param dataWriter Objeto servidor de datos para el driver de escritura
	 * @param outSizeX Número de pixels en X de la imagen de salida
	 * @param outSizeY Número de pixels en Y de la imagen de salida
	 * @param outFilename Fichero de salida
	 * @param extentMaxX Posición en X máxima del extent
	 * @param extentMinX Posición en X mínima del extent
	 * @param extentMaxY Posición en Y máxima del extent
	 * @param extentMinY Posición en Y mínima del extent
	 * @param nBands Número de bandas
	 * @param drvType Tipo de driver
	 * @throws GdalException
	 * @throws IOException
	 */
	public JpegWriter(IDataWriter dataWriter,
							String outFileName,
							Integer nBands,
							AffineTransform at,
							Integer outSizeX,
							Integer outSizeY,
							Integer dataType,
							Params params,
							IProjection proj,
							Boolean geo)throws GdalException, IOException  {
		ident = RasterUtilities.getExtensionFromFileName(outFileName);
		driver = ((WriteFileFormatFeatures) fileFeature.get(ident)).getDriverName();
		outJpg = outFileName;
		outTif = outFileName.substring(0, outFileName.lastIndexOf("."));
		outTif += ".tif";

		gdalWriter = new GdalWriter(dataWriter, outTif, nBands, at, outSizeX, outSizeY, dataType, params, proj, geo);
		if (params == null)
			loadParams(ident);
		else
			this.driverParams = params;
	}

	/**
	 * Asigna el tipo de driver con el que se salvará la imagen
	 * @param drvType Tipo de driver
	 */
	public void setDriverType(String drvType) {
		gdalWriter.setDriverType(drvType);
	}

	/**
	 * Realiza la función de compresión a partir de un GeoRasterFile.
	 * @throws IOException
	 */
	public void fileWrite() throws IOException, InterruptedException {
		gdalWriter.fileWrite();
	}

	/**
	 * Realiza una copia en el formato especificado.
	 * @throws IOException
	 */
	public static void createCopy(GdalDriver driverDst, String dst, String src, boolean bstrict, String[] params, IProjection proj) throws IOException, GdalException {
		GdalWriter.createCopy(driverDst, dst, src, bstrict, params, proj);
	}

	/**
	 * Realiza la escritura de datos con los datos que le pasa el cliente.
	 * @throws IOException
	 */
	public void dataWrite() throws IOException, InterruptedException {
		if (colorInterp != null)
			gdalWriter.setColorBandsInterpretation(colorInterp.getValues());
		gdalWriter.dataWrite();
		if (gdalWriter.isWrite()) {
			gdalWriter.writeClose();
			if (outTif != null) {
				GdalDriver driver = null;
				try {
					driver = Gdal.getDriverByName("JPEG");
					GdalWriter.createCopy(driver, outJpg, outTif, false, gdalWriter.gdalParamsFromRasterParams(driverParams), null);
				} catch (GdalException exc) {
					throw new IOException("No se ha podido obtener el driver.");
				}
				File file = new File(outTif);
				file.delete();
			}
		}
	}

	/**
	 * Cancela el salvado de datos.
	 */
	public void writeCancel() {
		gdalWriter.setWrite(false);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.GeoRasterWriter#setParams(org.gvsig.raster.dataset.Params)
	 */
	public void setParams(Params params) {
		driverParams = params;
		if (gdalWriter != null)
			gdalWriter.setParams(params);
	}

	/**
	 * Cierra el compresor ecw.
	 * @throws GdalException
	 */
	public void writeClose() {
	// El close del tif se hizo en dataWrite
	}

	public void setWkt(String wkt) {}
}
