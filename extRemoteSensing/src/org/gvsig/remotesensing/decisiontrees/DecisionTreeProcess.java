/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2007 Instituto de Desarrollo Regional and Generalitat Valenciana.
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

package org.gvsig.remotesensing.decisiontrees;

import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.gvsig.raster.RasterProcess;
import org.gvsig.raster.buffer.RasterBuffer;
import org.gvsig.raster.buffer.WriterBufferServer;
import org.gvsig.raster.dataset.GeoRasterWriter;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.grid.GridExtent;
import org.gvsig.raster.process.RasterTaskQueue;
import org.gvsig.raster.util.RasterToolsUtil;

import com.iver.andami.PluginServices;

/**
 * Proceso para la generación de un raster de clasificación mediante un árbol de decisión.
 * 
 * @author Diego Guerrero Sevilla (diego.guerrero@uclm.es)
 *
 */
public class DecisionTreeProcess extends RasterProcess {

	private DecisionTreeNode 			tree 				= null;
	private HashMap 					varsTable 			= null;
	private String 						fileName 			= null;
	private int 						percent 		  	= 0;
	private GridExtent					resultExtent		= null;
	private RasterBuffer 				rasterResult 		= null;	
	private boolean 					cancel 				= false;
	private WriterBufferServer 			writerBufferServer	= null;

	
	/**
	*  Escritura del resultado en disco.
	*/
	private void loadLAyer(){
		
		try{
			writerBufferServer = new WriterBufferServer(rasterResult);
			AffineTransform aTransform = new AffineTransform(resultExtent.getCellSizeX(),0.0,0.0,-resultExtent.getCellSizeY(),resultExtent.getMin().getX(),resultExtent.getMax().getY());
			GeoRasterWriter grw = GeoRasterWriter.getWriter(writerBufferServer, fileName, rasterResult.getBandCount(),aTransform, resultExtent.getNX(),resultExtent.getNY(),rasterResult.getDataType(), GeoRasterWriter.getWriter(fileName).getParams(),null);
			grw.dataWrite();
			grw.writeClose();
		
		}catch(NotSupportedExtensionException e){
			RasterToolsUtil.messageBoxError(PluginServices.getText(this, "error_writer_notsupportedextension"), this, e);
		} catch (IOException e) {
			RasterToolsUtil.messageBoxError(PluginServices.getText(this, "error_writer"), this, e);
		}catch(InterruptedException e){
			Thread.currentThread().interrupt();
		} catch (RasterDriverException e) {
			RasterToolsUtil.messageBoxError(PluginServices.getText(this, "raster_buffer_invalid_extension"), this, e);
		}
	}

	public int getPercent() {
		if (writerBufferServer==null)
			return percent;
		else
			return writerBufferServer.getPercent();
	}
	
	public String getLog(){
		return PluginServices.getText(this,"decision_process_log");
	}

	public String getTitle() {
		return PluginServices.getText(this,"arbol_decision");
	}

	public void init() {
		tree = (DecisionTreeNode)getParam("tree");
		varsTable = (HashMap)getParam("varsTable");
		fileName = getStringParam("filename");
		resultExtent = (GridExtent)getParam("resultExtent");
	}

	public void process() throws InterruptedException {
		RasterTaskQueue.register(rasterTask);
		RasterBuffer inputBuffer=null;
		try{
			
			// Construccion del rasterbuffer que recoge el resultado del calculo 
			rasterResult = RasterBuffer.getBuffer(RasterBuffer.TYPE_BYTE, resultExtent.getNX() ,resultExtent.getNY(), 1, true);
				
			// Calculo de grid resultante
			int iNX = resultExtent.getNX();
			int iNY = resultExtent.getNY();
		
			// Calculo de grid resultante
			for (int x=0;x<iNX;x++){
				if (cancel) return;  //Proceso cancelado 
				for(int y=0;y<iNY;y++){
					int i=0;
					for (Iterator iter = varsTable.keySet().iterator(); iter.hasNext();) {
						String varName = (String)iter.next();
						Object data[]= (Object[])varsTable.get(varName);
						inputBuffer= (RasterBuffer)data[0];
					
						int dataType= ((Integer)data[1]).intValue();
						double value=0;
					
						//	BUFFER TIPO_BYTE
						if(dataType == RasterBuffer.TYPE_BYTE){		
							value = inputBuffer.getElemByte(y, x, 0);			
							if(value!=inputBuffer.getNoDataValue()){	
								tree.setVarValue(varName,new Double(value));
								i++;					
							}
												
							else{					
								rasterResult.setElem(y, x, 0, (int)rasterResult.noDataValue);
								break;	
							}					
						}
						// BUFFER TIPO_SHORT
						else if(dataType == RasterBuffer.TYPE_SHORT){		
							value = inputBuffer.getElemShort(y, x, 0);			
							if(value!=inputBuffer.getNoDataValue()){	
								tree.setVarValue(varName,new Double(value));
								i++;					
							}
												
							else{					
								rasterResult.setElem(y, x, 0, (byte)rasterResult.noDataValue);
								break;	
							}					
						}
						// BUFFER TIPO_INT
						else if(dataType == RasterBuffer.TYPE_INT){		
							value = inputBuffer.getElemInt(y, x, 0);			
							if(value!=inputBuffer.getNoDataValue()){	
								tree.setVarValue(varName,new Double(value));
								i++;					
							}
												
							else{					
								rasterResult.setElem(y, x, 0, (byte)rasterResult.noDataValue);
								break;	
							}					
						}
						// BUFFER TIPO_FLOAT
						else if(dataType == RasterBuffer.TYPE_FLOAT){		
							value = inputBuffer.getElemFloat(y, x, 0);			
							if(value!=inputBuffer.getNoDataValue()){	
								tree.setVarValue(varName,new Double(value));
								i++;					
							}
												
							else{					
								rasterResult.setElem(y, x, 0, (byte)rasterResult.noDataValue);
								break;	
							}					
						}
						// BUFFER TIPO_DOUBLE
						else if(dataType == RasterBuffer.TYPE_DOUBLE){		
							value = inputBuffer.getElemDouble(y, x, 0);			
							if(value!=inputBuffer.getNoDataValue()){	
								tree.setVarValue(varName,new Double(value));
								i++;					
							}
												
							else{					
								rasterResult.setElem(y, x, 0, (byte)rasterResult.noDataValue);
								break;	
							}					
						}
					}
					
					// Evaluacion de la exprsion en el x,y.
					if (i == varsTable.size()){
						rasterResult.setElem(y, x, 0, (byte)tree.execute());
						percent = x*100/rasterResult.getWidth();
					}	
					
				}
			}
			loadLAyer();
		}finally{
			externalActions.end(fileName);
		}
	}
}
