/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
*
* Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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
package org.gvsig.raster.util.process;

import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.Configuration;
import org.gvsig.raster.RasterProcess;
import org.gvsig.raster.buffer.BufferFactory;
import org.gvsig.raster.buffer.BufferInterpolation;
import org.gvsig.raster.buffer.RasterBuffer;
import org.gvsig.raster.buffer.WriterBufferServer;
import org.gvsig.raster.dataset.GeoRasterWriter;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.IRasterDataSource;
import org.gvsig.raster.dataset.InvalidSetViewException;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.Params;
import org.gvsig.raster.dataset.RasterDataset;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.dataset.properties.DatasetColorInterpretation;
import org.gvsig.raster.dataset.serializer.RmfSerializerException;
import org.gvsig.raster.datastruct.ColorTable;
import org.gvsig.raster.datastruct.NoData;
import org.gvsig.raster.grid.GridPalette;
import org.gvsig.raster.grid.filter.RasterFilterList;
import org.gvsig.raster.grid.filter.bands.ColorTableFilter;
import org.gvsig.raster.util.RasterNotLoadException;
import org.gvsig.raster.util.RasterToolsUtil;
/**
 * <code>ClippingProcess</code> es un proceso que usa un <code>Thread</code>
 * para aplicar un recorte a una capa y guardarlo en disco. Muestra una barra
 * de incremento informativa.
 *
 * @version 24/04/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class ClippingProcess extends RasterProcess {
	private String                        fileName            = "";
	private WriterBufferServer            writerBufferServer  = null;
	private FLyrRasterSE                  rasterSE            = null;
	private AffineTransform               affineTransform     = new AffineTransform();
	private boolean                       oneLayerPerBand     = false;
	private int[]                         drawableBands       = { 0, 1, 2 };
	private int[]                         pValues             = null;
	private GeoRasterWriter               grw                 = null;
	private int                           interpolationMethod = BufferInterpolation.INTERPOLATION_Undefined;
	private String                        viewName            = "";
	private Params                        params              = null;
	private DatasetColorInterpretation    colorInterp         = null;

	private double[]                      wcValues             = null;
	
	/**
	 * Variables de la resolución de salida
	 */
	private int                           resolutionWidth     = 0;
	private int                           resolutionHeight    = 0;
	
	private IBuffer                       buffer              = null;

	/**
	 * Parámetros obligatorios al proceso:
	 * <UL>
	 * <LI>filename: Nombre del fichero de salida</LI>
	 * <LI>datawriter: Escritor de datos</LI>
	 * <LI>viewname: Nombre de la vista sobre la que se carga la capa al acabar el proceso</LI>
	 * <LI>pixelcoordinates: Coordenadas pixel del recorte (ulx, uly, lrx, lry)</LI>
	 * <LI>layer: Capa de entrada para el recorte</LI>
	 * <LI>drawablebands: Bandas de entrada</LI>
	 * <LI>onelayerperband: booleano que informa de si escribimos una banda por fichero de salida o todas en un fichero</LI>
	 * <LI>interpolationmethod: Método de interpolación.</LI>
	 * <LI>affinetransform: Transformación que informa al dataset de salida de su referencia geografica</LI>
	 * <LI>resolution: Ancho y alto de la capa de salida</LI>
	 * </UL> 
	 */
	public void init() {
		fileName = getStringParam("filename");
		writerBufferServer = (WriterBufferServer) getParam("datawriter");
		viewName = getStringParam("viewname");
		pValues = getIntArrayParam("pixelcoordinates");
		wcValues = getDoubleArrayParam("realcoordinates");
		rasterSE = getLayerParam("layer");
		drawableBands = getIntArrayParam("drawablebands");
		oneLayerPerBand = getBooleanParam("onelayerperband");
		interpolationMethod = getIntParam("interpolationmethod");
		affineTransform = (AffineTransform)getParam("affinetransform");
		colorInterp = (DatasetColorInterpretation)getParam("colorInterpretation");
		if(getIntArrayParam("resolution") != null) {
			resolutionWidth = getIntArrayParam("resolution")[0];
			resolutionHeight = getIntArrayParam("resolution")[1];
		}
		params = (Params) getParam("driverparams");
	}

	/**
	 * Salva la tabla de color al fichero rmf.
	 * @param fName
	 * @throws IOException
	 */
	private void saveToRmf(String fileName) {
		RasterDataset rds = null;
		int limitNumberOfRequests = 20;
		while (rds == null && limitNumberOfRequests > 0) {
			try {
				rds = rasterSE.getDataSource().getDataset(0)[0];
			} catch (IndexOutOfBoundsException e) {
				//En ocasiones, sobre todo con servicios remotos al pedir un datasource da una excepción de este tipo
				//se supone que es porque hay un refresco en el mismo momento de la petición por lo que como es más lento de
				//gestionar pilla a la capa sin datasources asociados ya que está reasignandolo. Si volvemos a pedirlo debe
				//haberlo cargado ya.
				try {
					Thread.sleep(200);
				} catch (InterruptedException e1) {
				}
			}
			limitNumberOfRequests--;
		}
		
		if (rds == null) {
			//RasterToolsUtil.messageBoxError("error_load_layer", this, new Exception("Error writing RMF. limitNumberOfRequests=" + limitNumberOfRequests));
			return;
		}

		RasterFilterList rasterFilterList = rasterSE.getRenderFilterList();

		// Guardamos en el RMF el valor NoData
		if (Configuration.getValue("nodata_transparency_enabled", Boolean.FALSE).booleanValue()) {
			try {
				RasterDataset.saveObjectToRmfFile(fileName, NoData.class, new NoData(rasterSE.getNoDataValue(), rasterSE.getNoDataType(), rasterSE.getDataType()[0]));
			} catch (RmfSerializerException e) {
				RasterToolsUtil.messageBoxError("error_salvando_rmf", this, e);
			}
		}

		// Guardamos en el RMF la tabla de color
		ColorTableFilter colorTableFilter = (ColorTableFilter) rasterFilterList.getByName(ColorTableFilter.names[0]);
		if (colorTableFilter != null) {
			GridPalette gridPalette = new GridPalette((ColorTable) colorTableFilter.getColorTable().clone());
			try {
				RasterDataset.saveObjectToRmfFile(fileName, ColorTable.class, (ColorTable) gridPalette);
			} catch (RmfSerializerException e) {
				RasterToolsUtil.messageBoxError("error_salvando_rmf", this, e);
			}
		}
	}

	/**
	 * Tarea de recorte
	 */
	public void process() throws InterruptedException {
		IRasterDataSource dsetCopy = null;
		try {
			long t2;
			long t1 = new java.util.Date().getTime();

			insertLineLog(RasterToolsUtil.getText(this, "leyendo_raster"));

			dsetCopy = rasterSE.getDataSource().newDataset();
			BufferFactory bufferFactory = new BufferFactory(dsetCopy);
			bufferFactory.setDrawableBands(drawableBands);
	
			if(	interpolationMethod != BufferInterpolation.INTERPOLATION_Undefined &&
					interpolationMethod != BufferInterpolation.INTERPOLATION_NearestNeighbour) {
				try {
					if(pValues != null) {
						if (RasterBuffer.isBufferTooBig(new double[] { pValues[0], pValues[3], pValues[2], pValues[1] }, drawableBands.length))
							bufferFactory.setReadOnly(true);
						bufferFactory.setAreaOfInterest(pValues[0], pValues[3], pValues[2] - pValues[0], pValues[1] - pValues[3]);
					} else if(wcValues != null) {
						if (RasterBuffer.isBufferTooBig(new double[] { wcValues[0], wcValues[3], wcValues[2], wcValues[1] }, rasterSE.getCellSize(), drawableBands.length))
							bufferFactory.setReadOnly(true);
						bufferFactory.setAreaOfInterest(wcValues[0], wcValues[1], Math.abs(wcValues[0] - wcValues[2]), Math.abs(wcValues[1] - wcValues[3]));
					}
				} catch (InvalidSetViewException e) {
					RasterToolsUtil.messageBoxError("No se ha podido asignar la vista al inicial el proceso de recorte.", this, e);
				}
				buffer = bufferFactory.getRasterBuf();

				insertLineLog(RasterToolsUtil.getText(this, "interpolando"));

				buffer = ((RasterBuffer) buffer).getAdjustedWindow(resolutionWidth, resolutionHeight, interpolationMethod);
			} else {
				try {
					if (RasterBuffer.isBufferTooBig(new double[] { 0, 0, resolutionWidth, resolutionHeight }, drawableBands.length))
						bufferFactory.setReadOnly(true);
					if(pValues != null) 
						bufferFactory.setAreaOfInterest(pValues[0], pValues[3], Math.abs(pValues[2] - pValues[0]) + 1, Math.abs(pValues[1] - pValues[3]) + 1, resolutionWidth, resolutionHeight);
					else if(wcValues != null) 
						bufferFactory.setAreaOfInterest(wcValues[0], wcValues[1], wcValues[2], wcValues[3], resolutionWidth, resolutionHeight);
					buffer = bufferFactory.getRasterBuf();
				} catch (InvalidSetViewException e) {
					RasterToolsUtil.messageBoxError("No se ha podido asignar la vista al inicial el proceso de recorte.", this, e);
				}
			}
			//TODO: FUNCIONALIDAD: Poner los getWriter con la proyección del fichero fuente
			
			insertLineLog(RasterToolsUtil.getText(this, "salvando_imagen"));

			String finalFileName = "";
			if (oneLayerPerBand) {
				long[] milis = new long[drawableBands.length];
				String[] fileNames = new String[drawableBands.length];
				for (int i = 0; i < drawableBands.length; i++) {
					fileNames[i] = fileName + "_B" + drawableBands[i] + ".tif";
					writerBufferServer.setBuffer(buffer, i);
					Params p = null;
					if (params == null)
						p = GeoRasterWriter.getWriter(fileNames[i]).getParams();
					else
						p = params;
					grw = GeoRasterWriter.getWriter(writerBufferServer, fileNames[i], 1,
							affineTransform, buffer.getWidth(), buffer.getHeight(),
							buffer.getDataType(), p, null);
					grw.setColorBandsInterpretation(new String[]{DatasetColorInterpretation.GRAY_BAND});
					grw.setWkt(dsetCopy.getWktProjection());
					grw.dataWrite();
					grw.writeClose();
					saveToRmf(fileNames[i]);
					t2 = new java.util.Date().getTime();
					milis[i] = (t2 - t1);
					t1 = new java.util.Date().getTime();
				}
				if (incrementableTask != null) {
					incrementableTask.processFinalize();
					incrementableTask = null;
				}
				if(viewName != null) {
					if (RasterToolsUtil.messageBoxYesOrNot("cargar_toc", this)) {
						try {
							for (int i = 0; i < drawableBands.length; i++) {
								FLayer lyr = RasterToolsUtil.loadLayer(viewName, fileNames[i], null);
								if(lyr != null && lyr instanceof FLyrRasterSE)
									((FLyrRasterSE)lyr).setRois(rasterSE.getRois());
							}
						} catch (RasterNotLoadException e) {
							RasterToolsUtil.messageBoxError("error_load_layer", this, e);
						}
					}
				}
				for (int i = 0; i < drawableBands.length; i++) {
					if (externalActions != null)
						externalActions.end(new Object[] { fileName + "_B" + drawableBands[i] + ".tif", new Long(milis[i]) });
				}
			} else {
				writerBufferServer.setBuffer(buffer, -1);
				if (params == null) {
					finalFileName = fileName + ".tif";
					params = GeoRasterWriter.getWriter(finalFileName).getParams();
				} else
					finalFileName = fileName;
				grw = GeoRasterWriter.getWriter(writerBufferServer, finalFileName,
						buffer.getBandCount(), affineTransform, buffer.getWidth(),
						buffer.getHeight(), buffer.getDataType(), params, null);
				if(colorInterp != null)
					grw.setColorBandsInterpretation(colorInterp.getValues());
				grw.setWkt(dsetCopy.getWktProjection());
				grw.dataWrite();
				grw.writeClose();
				saveToRmf(finalFileName);
				t2 = new java.util.Date().getTime();
				if (incrementableTask != null) {
					incrementableTask.processFinalize();
					incrementableTask = null;
				}
				//Damos tiempo a parar el Thread del incrementable para que no se cuelgue la ventana
				//El tiempo es como mínimo el de un bucle del run de la tarea incrementable
				Thread.sleep(600);
				cutFinalize(finalFileName, (t2 - t1));
			}

		} catch (NotSupportedExtensionException e) {
			RasterToolsUtil.messageBoxError("No se ha podido obtener el writer. Extensión no soportada", this, e);
		} catch (RasterDriverException e) {
			RasterToolsUtil.messageBoxError("No se ha podido obtener el writer.", this, e);
		} catch (IOException e) {
			RasterToolsUtil.messageBoxError("Error en la escritura en GeoRasterWriter.", this, e);
		} finally {
			if (dsetCopy != null)
				dsetCopy.close();
			buffer = null;
		}
	}
	
	/**
	 * Acciones que se realizan al finalizar de crear los recortes de imagen.
	 * Este método es llamado por el thread TailRasterProcess al finalizar.
	 */
	private void cutFinalize(String fileName, long milis) {
		if (!new File(fileName).exists())
			return;

		if(viewName != null) {
			if (RasterToolsUtil.messageBoxYesOrNot("cargar_toc", this)) {

				try {
					FLayer lyr = RasterToolsUtil.loadLayer(viewName, fileName, null);
					if(lyr != null && lyr instanceof FLyrRasterSE)
						((FLyrRasterSE)lyr).setRois(rasterSE.getRois());
				} catch (RasterNotLoadException e) {
					RasterToolsUtil.messageBoxError("error_load_layer", this, e);
				}
			}
		}

		if (externalActions != null)
			externalActions.end(new Object[]{fileName, new Long(milis)});
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.incrementabletask.IIncrementable#getPercent()
	 */
	public int getPercent() {
		if (buffer != null) {
			BufferInterpolation interpolation = ((RasterBuffer) buffer).getLastInterpolation();
			
			if (interpolation != null)
				if ((interpolation.getPercent() > 0) && (interpolation.getPercent() < 99))
					return interpolation.getPercent();
		}
		
		return (writerBufferServer != null) ? writerBufferServer.getPercent() : 0;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.incrementabletask.IIncrementable#getTitle()
	 */
	public String getTitle() {
		return RasterToolsUtil.getText(this, "incremento_recorte");
	}
}