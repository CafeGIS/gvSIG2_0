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

package org.gvsig.remotesensing.tasseledcap;

import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;

import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.RasterProcess;
import org.gvsig.raster.buffer.BufferFactory;
import org.gvsig.raster.buffer.RasterBuffer;
import org.gvsig.raster.buffer.WriterBufferServer;
import org.gvsig.raster.dataset.GeoRasterWriter;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.IRasterDataSource;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.grid.GridExtent;
import org.gvsig.raster.util.RasterNotLoadException;
import org.gvsig.raster.util.RasterToolsUtil;

import com.iver.andami.PluginServices;

/**
 * TasseledCapProccess es la clase que implementa el proceso de transformación Tasseled Cap.
La Trasformación es aplicable a imagenes LandSat MS, LandSatTM y LandSantETM 
 
*@author Alejandro Muñoz Sanchez (alejandro.munoz@uclm.es)
*@author Diego Guerrero Sevilla (diego.guerrero@uclm.es)
*@version 19/10/2007 
*/

public class TasseledCapProcess extends RasterProcess{

	private IBuffer					buffer			  	= null;
	private String					viewName				= null;
	private int 					type 				= 0;
	private String 					fileName 			= null;
	private WriterBufferServer 		writerBufferServer	= null;
	protected IBuffer				rasterResult    	= null;
	private AffineTransform 		aTransform			= null;
	private FLyrRasterSE			rasterSE			= null;
	private int 					percent 		  	= 0;
	int bands[]=null;
	double matrixParams[][]= null;
	private boolean exec= true;
	private String transformType = null;
	
	
	// MATRICES DE COEFICIENTES PARA CADA TRANSFORMACIÓN
	double LandSatMS[][]= 
	{
		/*   Brillo de suelo */ 	{0.433, 0.632, 0.586, 0.264},
		/*	 Indice de Verdor  */	{-0.290, -0.562, 0.600, 0.491},
		/*	 Indice de Senescencia*/{-0.829,  0.522, -0.039, 0.194},
		/*	 Cuarta */				{0.223,  0.012,  -0.543, 0.810}		
	};
	
	double LandSatTM[][]={
			
			/*   Brillo  */ 	{0.33183,  0.33121,  0.55177,  0.42514,  0.48087, 0.25252  		},
			/*	 Verdor  */		{-0.24717,  -0.16263,  -0.40639,  0.85468,  0.05493,  -0.11749	},
			/*	 Tercera */		{0.13929,  0.22490,  0.40359,  0.25178,  -0.70133, -0.45732  	},	
				
		};
	
	double LandSatETM[][]={
			
			/* Brightness */ 		{0.3561,  0.3972,  0.3904,  0.6966,  0.2286,  0.1596     },
			/*	 Greenness 	*/		{-0.3344,  -0.3544,  -0.4556,  0.6966,  -0.0242,  -0.2630},
			/*	 Vetness 	*/		{0.2626,  0.2141,  0.0926,  0.0656,  -0.7629, -0.5388    },	
			/*	 Fourth 	*/		{0.0805,  -0.0498,  0.1950, -0.1327, 0.5752,  -0.7775    },
			/*	 Fifth 		*/		{-0.7252,  -0.0202,  0.6683,  0.0631,  -0.1494,  -0.0274 },
			/*	 Sixth 		*/		{0.4000,  -0.8172,  0.3832,  0.0602,  -0.1095,  -0.0985	 }
		};

	
	

	public void setBuffer(){
		
		IRasterDataSource dsetCopy = null; 
		dsetCopy = rasterSE.getDataSource().newDataset();
		BufferFactory bufferFactory = new BufferFactory(dsetCopy);
		if (!RasterBuffer.loadInMemory(dsetCopy))
			bufferFactory.setReadOnly(true);
		try {
			bufferFactory.setDrawableBands(this.bands);
			bufferFactory.setAreaOfInterest();
			buffer = bufferFactory.getRasterBuf();	
		} catch (RasterDriverException e) {
			RasterToolsUtil.messageBoxError(PluginServices.getText(this, "raster_buffer_invalid_extension"), this, e);
		} catch (InterruptedException e) {
			RasterToolsUtil.messageBoxError(PluginServices.getText(this, "error_preview_stop"), this, e);	
		} 
	}
		
	
	/**
	 * @return buffer resultante de la transformación
	 */
	public IBuffer getBufferResult(){
		return rasterResult;
	}
		
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.incrementabletask.IIncrementable#getLabel()
	 */
	public String getLabel() {
		return  PluginServices.getText(this,"procesando");
	}


	/**
	 * Parámetros obligatorios al proceso:
	 * <UL>
	 * <LI>filename: Nombre del fichero de salida</LI>
	 * <LI>datawriter: Escritor de datos</LI>
	 * <LI>viewname: Nombre de la vista sobre la que se carga la capa al acabar el proceso</LI>
	 * <LI>layer: Capa de entrada para la transformación</LI>
	 * <LI>type: Tipo de transformación</LI>
	 * </UL> 
	 */
	public void init() {
		// Se toman los parametros del proceso
		fileName = getStringParam("filename");
		writerBufferServer = (WriterBufferServer) getParam("datawriter");
		viewName = getStringParam("viewname");
		bands= (int[])getParam("bands");
		rasterSE = getLayerParam("layer");
		type= getIntParam("type");
		
		// Obtenido el tipo se determina la matriz de coeficientes asociada
		switch (type){
			case 0:
				matrixParams=LandSatMS;
				transformType= PluginServices.getText(this,"landsatMS");
				if(bands.length!=4){
					this.incrementableTask.processFinalize();
					RasterToolsUtil.messageBoxError(PluginServices.getText(this, "transformMS_error"), this);
					exec=false;
				}
				break;	
			case 1:
				matrixParams= LandSatTM;	
				transformType= PluginServices.getText(this,"landsatTM");
				if(bands.length!=6){
					this.incrementableTask.processFinalize();
					RasterToolsUtil.messageBoxError(PluginServices.getText(this, "transformTM_error"), this);
					exec=false;
				}
				break;
			case 2:
				matrixParams= LandSatETM;	
				transformType= PluginServices.getText(this,"landsatETM");
				if(bands.length!=6){
					this.incrementableTask.processFinalize();
					RasterToolsUtil.messageBoxError(PluginServices.getText(this, "transformETM_error"), this);
					exec=false;
				}
				break;
		}	
		aTransform = rasterSE.getAffineTransform();	
	}

	

	/**
	 * Proceso de calculo de la transformación tasseeledCap
	 * */
	public void process() throws InterruptedException {
		if(!exec)
			return;
		setBuffer();
		GridExtent layerExtent = new GridExtent(rasterSE.getFullRasterExtent(),rasterSE.getCellSize());
		
		// TODO: Tipo salida float
		rasterResult = RasterBuffer.getBuffer(IBuffer.TYPE_FLOAT,layerExtent.getNX(), layerExtent.getNY(), matrixParams.length, true);
		
		// Algoritmo TasseledCap. Trasformacion
		double valor=0;
		int iNX=layerExtent.getNX();
		int iNY=layerExtent.getNY();
	
		
//			 BUFFER TYPE_BYTE
			if (buffer.getDataType()== IBuffer.TYPE_BYTE){
				for(int i=0; i<iNY;i++ ) {
					for (int j=0; j<iNX;j++){
						for (int k=0; k<matrixParams.length; k++){
							for(int s=0;s<matrixParams[0].length;s++)
							{	
								valor+=(double)(buffer.getElemByte(i,j,s)&0xff)*matrixParams[k][s];
							}	
							rasterResult.setElem(i, j,k,(float) valor);
							valor=0; 
						}
					}
					percent = (int)(i*100/iNY);		
				}
			} 
			
//			 BUFFER TYPE_SHORT
			if (buffer.getDataType()== IBuffer.TYPE_SHORT){
				for(int i=0; i<iNY;i++ ) {
					for (int j=0; j<iNX;j++){
						for (int k=0; k<matrixParams.length ; k++){
							for(int s=0;s<matrixParams[0].length;s++)
							{	
								valor+=(double)buffer.getElemShort(i,j,s)*matrixParams[k][s];
							}
							rasterResult.setElem(i, j,k,(float) valor);
							valor=0;
						}
						
					}
					percent = (int)(i*100/iNY);
				}
			} 
			
//			BUFFER TYPE INT
			if (buffer.getDataType()== IBuffer.TYPE_INT){
				for(int i=0; i<iNY;i++ ) {
					for (int j=0; j<iNX;j++){
						for (int k=0; k<matrixParams.length ; k++){
								for(int s=0;s<matrixParams[0].length;s++)
								{	
									valor+=(double)buffer.getElemInt(i,j,s)*matrixParams[k][s];
								}	
								rasterResult.setElem(i, j,k,(float) valor);
								valor=0;
						}
					}
					percent = (int)(i*100/iNY);	
				}
			}
			
//			BUFFER TYPE FLOAT
			if (buffer.getDataType()== IBuffer.TYPE_FLOAT){
				for(int i=0; i<iNY;i++ ) {
					for (int j=0; j<iNX;j++){
						for (int k=0; k<matrixParams.length ; k++){
								for(int s=0;s<matrixParams[0].length;s++)
									{	
										valor+=(double)buffer.getElemFloat(i,j,s)*matrixParams[k][s];
									} 	
								rasterResult.setElem(i, j,k,(float) valor);
								valor=0;			
						}
					}
					percent = (int)(i*100/iNY);
				}
			}		
			
//			BUFFER TYPE DOUBLE
			if (buffer.getDataType()== IBuffer.TYPE_DOUBLE){
				for(int i=0; i<iNY;i++ ) {
					for (int j=0; j<iNX;j++){
						for (int k=0; k<matrixParams.length ; k++){
								for(int s=0;s<matrixParams[0].length;s++)
									 {	
										valor+=(double)buffer.getElemDouble(i,j,s)*matrixParams[k][s];
									}			
								rasterResult.setElem(i, j,k, (float)valor);
								valor=0;	
						}
					}
					percent = (int)(i*100/iNY);
				}
			}	
		
		// Escritura del resultado en disco
		GeoRasterWriter grw = null;
		writerBufferServer = new WriterBufferServer(rasterResult);

		if(fileName==null)
			return;
		
		int endIndex =fileName.lastIndexOf(".");
		if (endIndex < 0)
			 endIndex = fileName.length();
		
		try {
			grw = GeoRasterWriter.getWriter(writerBufferServer, fileName, rasterResult.getBandCount(),aTransform, rasterResult.getWidth(), rasterResult.getHeight(), rasterResult.getDataType(), GeoRasterWriter.getWriter(fileName).getParams(),null);
			grw.dataWrite();
			grw.setWkt(rasterSE.getWktProjection());
			grw.writeClose();
			rasterResult.free();
			tasseledCapFinalize(fileName);
		} catch (NotSupportedExtensionException e1) {
			RasterToolsUtil.messageBoxError(PluginServices.getText(this, "error_writer_notsupportedextension"), this, e1);
		} catch (RasterDriverException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			RasterToolsUtil.messageBoxError(PluginServices.getText(this, "error_writer"), this, e);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
	
	
	/**
	 *  Método de finalización del proceso. 
	 * */
	private void tasseledCapFinalize(String fileName) {
		if (!new File(fileName).exists())
			return;
		try {
				RasterToolsUtil.loadLayer(viewName, fileName, null);
		} catch (RasterNotLoadException e) {	
			RasterToolsUtil.messageBoxError("error_load_layer", this, e);
		}
		if (externalActions != null) {
			externalActions.end(fileName);
		}
	}

	
	public String getTitle() {
		return PluginServices.getText(this, "tasseledcap");
	}

	
	public int getPercent() {
		if (percent<99)
			return percent;
		else
			return writerBufferServer.getPercent();
	}	
	
	
	public String getLog(){
		return PluginServices.getText(this,"apply_tasseledCap")+PluginServices.getText(this,"transformation_type")+transformType;
	}
	
	
}
