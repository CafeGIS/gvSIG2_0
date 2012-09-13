/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
	 *
	 * Copyright (C) 2006 Instituto de Desarrollo Regional and Generalitat Valenciana.
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
	 *
	 * For more information, contact:
	 *
	 *  Generalitat Valenciana
	 *   Conselleria d'Infraestructures i Transport
	 *   Av. Blasco Ibañez, 50
	 *   46010 VALENCIA
	 *   SPAIN
	 *
	 *      +34 963862235
	 *   gvsig@gva.es
	 *      www.gvsig.gva.es
	 *
	 *    or
	 *
	 *   Instituto de Desarrollo Regional (Universidad de Castilla La-Mancha)
	 *   Campus Universitario s/n
	 *   02071 Alabacete
	 *   Spain
	 *
	 *   +34 967 599 200
	 */

package org.gvsig.remotesensing.mosaic.process;

import java.awt.image.DataBuffer;
import java.io.File;
import java.io.IOException;

import org.gvsig.fmap.mapcontext.exceptions.LoadLayerException;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.RasterProcess;
import org.gvsig.raster.buffer.BufferFactory;
import org.gvsig.raster.buffer.RasterBuffer;
import org.gvsig.raster.buffer.WriterBufferServer;
import org.gvsig.raster.dataset.FileNotOpenException;
import org.gvsig.raster.dataset.GeoRasterWriter;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.IRasterDataSource;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.datastruct.Histogram;
import org.gvsig.raster.grid.filter.RasterFilter;
import org.gvsig.raster.grid.filter.enhancement.EnhancementStretchListManager;
import org.gvsig.raster.grid.filter.enhancement.LinearStretchParams;
import org.gvsig.raster.grid.filter.histogramMatching.HistogramMatchingByteFilter;
import org.gvsig.raster.util.RasterToolsUtil;

import com.iver.andami.PluginServices;


/**
 * Clase que implementa el proceso de Histogram Matching para un conjuto de  imagenes
 * que se quieren hacer corresponder con un histogram de referencia. El proceso tomara como parametros
 * un conjunto de capas y un histograma maestro y ejecutara de forma secuencial un filtro de histogramMatch
 * para cada una de ellas. El proceso se realiza sobre las bandas de visulaización.
 *
 * @params
 * <LI>FLyrRasterSE[] "inputRasterLayers": Capas raster de entrada</LI>
 * <LI>Histogram "masterHistogram": Histograma con el que se hace la correspondencia</LI>
 * <LI>int "numbandas": PROVISIONAL.</LI>
 * <LI>String "outputPath": Ruta completa al fichero de salida del proceso</LI>
 *
 * @result
 * <LI>outputRassterLayers[]: Capas raster resultantes</LI>
 *
 * par histograma source - histograma referencia.
 *
 * @author aMuÑoz (alejandro.muñoz@uclm.es)
 * @version 30/4/2008
 * */
public class HistogramMatchProcess  extends RasterProcess{

	private FLyrRasterSE 			inputRasterLayers[]				= null;
	private FLyrRasterSE 			outputRassterLayers[] 			= null;
	private Histogram 				histogramMaster					= null;
	private String 					layerInProcess					= null;
	private int 					countInProcess					= 0,totalcount= 0;
	private int 					numbands 						= 0;
	private RasterFilter 			filtro							= new HistogramMatchingByteFilter();
	private IBuffer 				rasterResult					= null;
	private WriterBufferServer 		writerBufferServer				= null;
	private String 					path 							= null;


	/** Metodo en el que se recogen los parámetros para el proceso
	 *  layers conjunto de capas a las que se aplica el proceso
	 * 	histogramMaster histograma de referencia
	 *  view vista sobre la que se cargan los resultados
	 * */

	public void init() {
		inputRasterLayers =(FLyrRasterSE []) getParam("inputRasterLayers");
		outputRassterLayers = new FLyrRasterSE[inputRasterLayers.length];
		histogramMaster= (Histogram) getParam("masterHistogram");
		numbands = ((Integer)getParam("numbands")).intValue(); //******************************
		path = (String) getStringParam("outputPath");
	}


	/** Proceso.
	 *  Para cada capa de las contenidas en layers, se aplica un filtro de histogramMatching
	 *  entre la capa y el histograma de referencia pasado como paramero.
	 * */
	public void process() throws InterruptedException {
		try{
			int k=0;
			totalcount= inputRasterLayers.length-1;
			IBuffer inputBuffer= null;
			for (int i=0; i< inputRasterLayers.length;i++){
				// Para el resto de los histogramas seleccionados
				if(inputRasterLayers[i]!=null){
					k++;
					layerInProcess= inputRasterLayers[i].getName();
					IRasterDataSource dsetCopy = null;
					dsetCopy = inputRasterLayers[i].getDataSource().newDataset();
					BufferFactory bufferFactory = new BufferFactory(dsetCopy);
					bufferFactory.setDrawableBands(inputRasterLayers[i].getRenderBands());
					if (!RasterBuffer.loadInMemory(dsetCopy))
						bufferFactory.setReadOnly(true);
					bufferFactory.setAreaOfInterest();

					inputBuffer = bufferFactory.getRasterBuf();
					//Aplicar filtro de realce si es necesario:

					if(inputBuffer.getDataType()!=DataBuffer.TYPE_BYTE){
						LinearStretchParams leParams = null;
						try {
							leParams = LinearStretchParams.createStandardParam(inputRasterLayers[i].getRenderBands(), 0.0, bufferFactory.getDataSource().getStatistics(), false);
						} catch (FileNotOpenException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (RasterDriverException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						RasterFilter linearStretchEnhancementFilter = EnhancementStretchListManager.createEnhancedFilter(leParams, bufferFactory.getDataSource().getStatistics(),
								inputRasterLayers[i].getRenderBands(), false);
						linearStretchEnhancementFilter.addParam("raster", inputBuffer);
						linearStretchEnhancementFilter.execute();

						inputBuffer = (IBuffer)linearStretchEnhancementFilter.getResult("raster");
					}

					filtro.addParam("raster",inputBuffer);
					filtro.addParam("histogramReference",histogramMaster);
					filtro.addParam("numbands",new Integer(numbands));
					filtro.addParam("filterName",new String("histogram"));
					filtro.execute();
					rasterResult=(RasterBuffer) filtro.getResult("raster");
					outputRassterLayers[i] = createLayer(path + File.separator + "hist" + i + ".tif",i);
				}
			}

		}catch (Exception e2) {
			RasterToolsUtil.messageBoxError(PluginServices.getText(this, "filter_error"), this, e2);
		}
		finally{
			externalActions.end(outputRassterLayers);
		}

	}



	public String getTitle() {
		return PluginServices.getText(this,"histohramMatchProcess");
	}

	public int getPercent() {
		if(writerBufferServer!=null)
			return writerBufferServer.getPercent();
		else
			return filtro.getPercent();
	}

	/** log con la información del proceso */
	public String getLog(){
		return PluginServices.getText(this,"hiatogramMatchLog")+layerInProcess+"  ("+countInProcess+"/"+totalcount+")";

	}


	public FLyrRasterSE createLayer(String path, int i){

		FLyrRasterSE lyr = null;
		try{
			if(path==null)
				return null;
			GeoRasterWriter grw = null;
			writerBufferServer = new WriterBufferServer(rasterResult);
			grw = GeoRasterWriter.getWriter(writerBufferServer, path, rasterResult.getBandCount(),inputRasterLayers[i].getAffineTransform(), rasterResult.getWidth(), rasterResult.getHeight(), rasterResult.getDataType(), GeoRasterWriter.getWriter(path).getParams(), inputRasterLayers[i].getProjection());

			grw.dataWrite();
			grw.setWkt(inputRasterLayers[i].getWktProjection());
			grw.writeClose();
			rasterResult.free();

			int endIndex = path.lastIndexOf(".");
			if (endIndex < 0)
				endIndex = path.length();
			lyr = FLyrRasterSE.createLayer(
					path.substring(path.lastIndexOf(File.separator) + 1, endIndex),
					path,
					null
					);

		} catch (NotSupportedExtensionException e) {
			RasterToolsUtil.messageBoxError(PluginServices.getText(this, "error_writer_notsupportedextension"), this, e);
		} catch (RasterDriverException e) {
			RasterToolsUtil.messageBoxError(PluginServices.getText(this, "error_writer"), this, e);
		} catch (IOException e) {
			RasterToolsUtil.messageBoxError(PluginServices.getText(this, "error_writer"), this, e);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} catch (LoadLayerException e) {
			RasterToolsUtil.messageBoxError("error_cargar_capa", this, e);
		}
		return lyr;
	}


	public Object getResult() {
		return outputRassterLayers;
	}

}
