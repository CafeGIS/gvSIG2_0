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

package org.gvsig.remotesensing.principalcomponents;

import java.io.IOException;

import org.gvsig.fmap.mapcontext.exceptions.LoadLayerException;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.RasterProcess;
import org.gvsig.raster.buffer.BufferFactory;
import org.gvsig.raster.buffer.RasterBuffer;
import org.gvsig.raster.buffer.RasterBufferInvalidException;
import org.gvsig.raster.buffer.WriterBufferServer;
import org.gvsig.raster.dataset.GeoRasterWriter;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.IRasterDataSource;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.grid.Grid;
import org.gvsig.raster.grid.GridException;
import org.gvsig.raster.grid.GridExtent;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.raster.util.RasterUtilities;

import com.iver.andami.PluginServices;

/**
 *Proceso de contrucción de la imagen resultante del analisis de componentes principales a partir de los
 *componentes seleccionados.
 *
 *@params
 * <LI>FLyrRasterSE "inputRasterLayer": Capa raster de entrada</LI>
 * <LI>PCStatistics "statistics": Estadísticas del Análisis de C.P. (ej.: las generadas por PCStatisticsProcess)</LI>
 * <LI>boolean[] "selectedBands": Bandas del raster original que se tienen en cuenta para la transformación</LI>
 * <LI>boolean[] "selectedComponents": Componentes que se usarán para consturir la imagen transformada</LI>
 * <LI>String "outputPath": Ruta completa al fichero de salida del proceso</LI>
 *
 * @result
 * <LI>FLyrRasterSE: Raster transformado</LI>
 *
 *@author Alejandro Muñoz Sanchez (alejandro.munoz@uclm.es)
 *@author Diego Guerrero Sevilla (diego.guerrero@uclm.es)
 *@version 19/10/2007
 */
public class PCImageProcess extends RasterProcess{

	private boolean[] 			selectedPCs 		= null;
	private String 				layerName 			= null;
	private Grid 				inputGrid			= null;
	private RasterBuffer		rasterResult		= null;
	private int 				percent 		  	= 0;
	private WriterBufferServer  writerBufferServer	= null;;
	private FLyrRasterSE		inputRasterLayer	= null;
	private FLyrRasterSE 		outputRasterLayer 	= null;
	private boolean[] 			selectedBands		= null;
	private PCStatistics 		pcStatistics		= null;


	/**
	 * Connstructor
	 */
	public PCImageProcess() {
	}


	/**
	 * @return buffer resultante tras la transformacion
	 */
	public RasterBuffer getBufferResult(){
		return rasterResult;
	}

	public Object getResult() {
		return outputRasterLayer;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.incrementabletask.IIncrementable#getLabel()
	 */
	public String getLabel() {
		return  PluginServices.getText(this,"procesando");
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.incrementabletask.IIncrementable#getLog()
	 */
	public String getLog() {
		if (writerBufferServer==null)
			return PluginServices.getText(this,"generando_pcs")+"...";
		else
			return PluginServices.getText(this,"escribiendo_resultado")+"...";
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.incrementabletask.IIncrementable#getPercent()
	 */
	public int getPercent() {
		if(writerBufferServer==null)
			return percent;
		return writerBufferServer.getPercent();
	}


	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.incrementabletask.IIncrementable#getTitle()
	 */
	public String getTitle() {
		return PluginServices.getText(this,"principal_components");
	}

	public void init() {
		inputRasterLayer = (FLyrRasterSE)getLayerParam("inputRasterLayer");
		pcStatistics = (PCStatistics) getParam("statistics");
		selectedBands = (boolean []) getParam("selectedBands");
		selectedPCs = (boolean[]) getParam("selectedComponents");
		layerName = getStringParam("outputPath");

		buildGrid();
	}

	public void process() throws InterruptedException {

			//	ResultExtent de la primera banda del array
			GridExtent layerExtent = null;
			layerExtent= inputGrid.getGridExtent();

			// Se determina el numero de bandas de la imagen resultante, en funcion de la seleccion.
			int numbandas=0;
			for (int i=0;i<selectedPCs.length;i++)
				if (selectedPCs[i])numbandas++;

			// Correspondencia entre entre componete seleccionado y banda
			int banda[] = new int[selectedPCs.length];
			int band=0;
			for (int i=0;i<selectedPCs.length;i++)
				{
					if (selectedPCs[i]){ banda[i]=band; band++;}
					else banda[i]=-1;
				}

			// ResultOrden: Orden correcto de autovalores (de mayor a menor)
			int resultOrden[]= new int[pcStatistics.getAutovalues().length];
			int cont = pcStatistics.getAutovalues().length-1;
			int noseleccionados=0;
			for(int i=0;i<pcStatistics.getAutovalues().length;i++){
				if (banda[i]!=-1){
					resultOrden[i]=cont;
					cont--;
					}
					else noseleccionados++;
			}

			for(int i=0;i<pcStatistics.getAutovalues().length;i++){
				resultOrden[i]=resultOrden[i]- noseleccionados;
			}

			int bandas[]=new int [resultOrden.length -noseleccionados];
			for(int i=0; i< bandas.length;i++)
				bandas[i]=i;

			// Construccion del buffer para escritura.
			rasterResult=RasterBuffer.getBuffer(IBuffer.TYPE_FLOAT, inputGrid.getLayerNX(), inputGrid.getLayerNY(), bandas.length, true);

			double valor=0;
			int bandCount= inputGrid.getRasterBuf().getBandCount();

			//		BUFFER TYPE BYTE
			if (inputGrid.getRasterBuf().getDataType()== RasterBuffer.TYPE_BYTE){
				// Construccion de la imagen en funcion de los componentes seleccionados.
				for(int i=0; i<layerExtent.getNY();i++ ) {
					for (int j=0; j<layerExtent.getNX();j++){
						for (int k=0; k<bandCount; k++){
							if (selectedPCs[k]){
								for(int s=0;s<bandCount;s++)
								{
									if (selectedPCs[s]){
										inputGrid.setBandToOperate(s);
											try {
												valor+=(double)inputGrid.getCellValueAsByte(j, i)*pcStatistics.getAutoVectorsMatrix().get(s,resultOrden[k]);
											} catch (GridException e) {
												RasterToolsUtil.messageBoxError(PluginServices.getText(this, "grid_error"), this, e);
											}
									}
								}
								rasterResult.setElem(i, j, banda[k],(float) valor);
								valor=0;
							}
						}
					}
					percent = i*100/layerExtent.getNX();
				}

			} // Fin caso RasterBuffer TypeByte


			//		BUFFER TYPE SHORT
			if (inputGrid.getRasterBuf().getDataType()== RasterBuffer.TYPE_SHORT){
				for(int i=0; i<layerExtent.getNY();i++ ) {
					for (int j=0; j<layerExtent.getNX();j++){
						for (int k=0; k<bandCount; k++){
							if (selectedPCs[k]){
								for(int s=0;s<bandCount;s++)
								{
									if (selectedPCs[s]){
										inputGrid.setBandToOperate(s);
											try {
												valor+=(double)inputGrid.getCellValueAsShort(j, i)*pcStatistics.getAutoVectorsMatrix().get(s,resultOrden[k]);
											} catch (GridException e) {
												RasterToolsUtil.messageBoxError(PluginServices.getText(this, "grid_error"), this, e);
											}
									}
								}
								rasterResult.setElem(i, j, banda[k],(float) valor);
								valor=0;
							}
						}
					}
					percent = i*100/layerExtent.getNX();
				}
			} // Fin caso RasterBuffer TypeShort

			//		BUFFER TYPE INT
			if (inputGrid.getRasterBuf().getDataType()== RasterBuffer.TYPE_INT){
				for(int i=0; i<layerExtent.getNY();i++ ) {
					for (int j=0; j<layerExtent.getNX();j++){
						for (int k=0; k<bandCount; k++){
							if (selectedPCs[k]){
								for(int s=0;s<bandCount;s++)
								{
									if (selectedPCs[s]){
										inputGrid.setBandToOperate(s);
											try {
												valor+=(double)inputGrid.getCellValueAsInt(j, i)*pcStatistics.getAutoVectorsMatrix().get(s,resultOrden[k]);
											} catch (GridException e) {
												RasterToolsUtil.messageBoxError(PluginServices.getText(this, "grid_error"), this, e);
											}
									}
								}
								rasterResult.setElem(i, j, banda[k],(float) valor);
								valor=0;
							}
						}
					}
					percent = i*100/layerExtent.getNX();
				}
				} // Fin caso RasterBuffer TypeInt


			//		BUFFER TYPE FLOAT
			if (inputGrid.getRasterBuf().getDataType()== RasterBuffer.TYPE_FLOAT){
				for(int i=0; i<layerExtent.getNY();i++ ) {
					for (int j=0; j<layerExtent.getNX();j++){
						for (int k=0; k<bandCount; k++){
							if (selectedPCs[k]){
								for(int s=0;s<bandCount;s++)
								{
									if (selectedPCs[s]){
										inputGrid.setBandToOperate(s);
											try {
												valor+=(double)inputGrid.getCellValueAsFloat(j, i)*pcStatistics.getAutoVectorsMatrix().get(s,resultOrden[k]);
											} catch (GridException e) {
												RasterToolsUtil.messageBoxError(PluginServices.getText(this, "grid_error"), this, e);
											}
									}
								}
								rasterResult.setElem(i, j, banda[k],(float) valor);
								valor=0;
							}
						}
					}
					percent = i*100/layerExtent.getNX();
				}

			} // Fin caso RasterBuffer TypeFloat


			//		BUFFER TYPE DOUBLE
			if (inputGrid.getRasterBuf().getDataType()== RasterBuffer.TYPE_DOUBLE){
				for(int i=0; i<layerExtent.getNY();i++ ) {
					for (int j=0; j<layerExtent.getNX();j++){
						for (int k=0; k<bandCount; k++){
							if (selectedPCs[k]){
								for(int s=0;s<bandCount;s++)
								{
									if (selectedPCs[s]){
										inputGrid.setBandToOperate(s);
											try {
												valor+=(double)inputGrid.getCellValueAsDouble(j, i)*pcStatistics.getAutoVectorsMatrix().get(s,resultOrden[k]);
											} catch (GridException e) {
												RasterToolsUtil.messageBoxError(PluginServices.getText(this, "grid_error"), this, e);
											}
									}
								}
								rasterResult.setElem(i, j, banda[k],(float) valor);
								valor=0;
							}
						}
					}
					percent = i*100/layerExtent.getNX();
				}
			} // Fin caso RasterBuffer TypeDouble

		// escritutra a fichero
		createLayer();

		if (externalActions != null)
			externalActions.end(outputRasterLayer);
	}

	/**
	 * Escritura del resultado en disco.
	 */
	private void createLayer(){
		try{
			// Escritura de los datos a fichero temporal
			if(layerName==null)
				return;
			String fileName=layerName;
			int endIndex = fileName.lastIndexOf(".");
			if (endIndex < 0)
				 endIndex = fileName.length();
			GeoRasterWriter grw = null;
			writerBufferServer = new WriterBufferServer(rasterResult);
			grw = GeoRasterWriter.getWriter(writerBufferServer, layerName, rasterResult.getBandCount(),inputRasterLayer.getAffineTransform(), rasterResult.getWidth(), rasterResult.getHeight(), rasterResult.getDataType(), GeoRasterWriter.getWriter(layerName).getParams(), null);
			grw.dataWrite();
			grw.setWkt(inputRasterLayer.getWktProjection());
			grw.writeClose();

			outputRasterLayer = FLyrRasterSE.createLayer(RasterUtilities.getFileNameFromCanonical(layerName),
					layerName, null);

		} catch (NotSupportedExtensionException e) {
			RasterToolsUtil.messageBoxError(PluginServices.getText(this, "error_writer_notsupportedextension"), this, e);
		} catch (IOException e) {
			RasterToolsUtil.messageBoxError(PluginServices.getText(this, "error_writer"), this, e);
		} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
		} catch (RasterDriverException e) {
			e.printStackTrace();
		} catch (LoadLayerException e) {
			RasterToolsUtil.messageBoxError("error_cargar_capa", this, e);
		}
	}

	/**
	 * Construye el grid con las bandas seleccionadas
	 */
	private void buildGrid(){

		IRasterDataSource dsetCopy = null;
		dsetCopy = inputRasterLayer.getDataSource().newDataset();
		BufferFactory bufferFactory = new BufferFactory(dsetCopy);
		if (!RasterBuffer.loadInMemory(dsetCopy))
			bufferFactory.setReadOnly(true);

		int longitud=0;
		for (int i=0; i<selectedBands.length;i++)
				if (selectedBands[i]) longitud++;

		int bands[]= new int[longitud];
		int j=0;
		for (int i=0; i<selectedBands.length; i++)
			if (selectedBands[i])
				{ bands[j]=i;
						 j++;
				}
		try {
				inputGrid = new Grid(bufferFactory, bands);
		} catch (RasterBufferInvalidException e) {
					e.printStackTrace();
		}
	}
}