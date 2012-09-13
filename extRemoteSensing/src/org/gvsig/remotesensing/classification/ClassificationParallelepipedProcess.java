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

package org.gvsig.remotesensing.classification;

import java.util.ArrayList;

import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.buffer.RasterBuffer;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.grid.GridException;
import org.gvsig.raster.grid.roi.ROI;

import com.iver.cit.gvsig.project.documents.view.gui.View;

/** ClassificationParallelepipedProcess implementa el método de clasificación de 
 * paralelepipedos o hipercubos.
 * 
 * @see ClassificationGeneralProcess
 * @author Alejandro Muñoz Sanchez (alejandro.munoz@uclm.es)
 * @author Diego Guerrero Sevilla (diego.guerrero@uclm.es)
 * @version 19/10/2007 
*/

public class ClassificationParallelepipedProcess extends ClassificationGeneralProcess{
	
	private int 					bandCount 				= 0;
	double 							varianza[][]			= null;
	double 							medias[][]				= null; 
	int 							defaultClass			=0;
	double 							stevcoef				=0;

	
	/**
	* Método que implementa el clasificador por paralelepipedos. 
	* 
	* @param  array de tipo byte con los valores del pixel en cada una de las bandas 
	* @return clase a la que pertenece el pixel
    */
	
	public int getPixelClassForTypeByte(byte[] pixelBandsValues) {
		for (int clase=0; clase<numClases; clase++)
		{
			boolean inClass= true;
			for(int i=0; i< bandCount; i++)
				{
				 if(!((medias[clase][i]-stevcoef*Math.sqrt(varianza[clase][i])<pixelBandsValues[i]) && 
						 (pixelBandsValues[i] <medias[clase][i]+stevcoef*Math.sqrt(varianza[clase][i]))))
				 {
					 inClass= false;
				 }
				}
			 if(inClass==true)
				 return clase;
		}
		
		return defaultClass;
	} // Fin del metodo


	
	/**
	* Método que implementa el clasificador por paralelepipedos. 
	* 
	* @param  array de tipo short con los valores del pixel en cada una de las bandas 
	* @return primera clase a la que  pertenece el pixel.
    */
	
	public int getPixelClassForTypeShort(short[] pixelBandsValues) {
		for (int clase=0; clase<numClases; clase++)
		{
			boolean inClass= true;
			for(int i=0; i< bandCount; i++)
				{
				 if(!((medias[clase][i]-stevcoef*Math.sqrt(varianza[clase][i])<pixelBandsValues[i]) && 
						 (pixelBandsValues[i] <medias[clase][i]+stevcoef*Math.sqrt(varianza[clase][i]))))
				 {
					 inClass= false;
				 }
				}
			 if(inClass==true)
				 return clase;
		}
		
		return defaultClass;
	}


	/**
	* Método que implementa el clasificador por paralelepipedos. 
	* 
	* @param  array de tipo int con los valores del pixel en cada una de las bandas 
	* @return primera clase a la que  pertenece el pixel.
    */
	public int getPixelClassForTypeInt(int[] pixelBandsValues) {
		for (int clase=0; clase<numClases; clase++)
		{
			boolean inClass= true;
			for(int i=0; i< bandCount; i++)
				{
				 if(!((medias[clase][i]-stevcoef*Math.sqrt(varianza[clase][i])<pixelBandsValues[i]) && 
						 (pixelBandsValues[i] <medias[clase][i]+stevcoef*Math.sqrt(varianza[clase][i]))))
				 {
					 inClass= false;
				 }
				}
			 if(inClass==true)
				 return clase;
		}
		
		return defaultClass;
	}


	
	/**
	* Método que implementa el clasificador por paralelepipedos. 
	* 
	* @param  array de tipo float con los valores del pixel en cada una de las bandas 
	* @return primera clase a la que  pertenece el pixel.
    */
	public int getPixelClassForTypeFloat(float[] pixelBandsValues) {
		for (int clase=0; clase<numClases; clase++)
		{
			boolean inClass= true;
			for(int i=0; i< bandCount; i++)
				{
				 if(!((medias[clase][i]-stevcoef*Math.sqrt(varianza[clase][i])<pixelBandsValues[i]) && 
						 (pixelBandsValues[i] <medias[clase][i]+stevcoef*Math.sqrt(varianza[clase][i]))))
				 {
					 inClass= false;
				 }
				}
			 if(inClass==true)
				 return clase;
		}
		
		return defaultClass;
	}


	/**
	* Método que implementa el clasificador por paralelepipedos. 
	* 
	* @param  array de tipo double con los valores del pixel en cada una de las bandas 
	* @return primera clase a la que  pertenece el pixel.
    */
	public int getPixelClassForTypeDouble(double[] pixelBandsValues) {
		for (int clase=0; clase<numClases; clase++)
		{
			boolean inClass= true;
			for(int i=0; i< bandCount; i++)
				{
				 if(!((medias[clase][i]-stevcoef*Math.sqrt(varianza[clase][i])<pixelBandsValues[i]) && 
						 (pixelBandsValues[i] <medias[clase][i]+stevcoef*Math.sqrt(varianza[clase][i]))))
				 {
					 inClass= false;
				 }
				}
			 if(inClass==true)
				 return clase;
		}
		
		return defaultClass;
	}


	/** Metodo que recoge los parametros del proceso de clasificacion de 
	* por paralelepipedos
	* <LI>rasterSE: Capa de entrada para la clasificación</LI>
	* <LI> rois: lista de rois</LI> 
	* <LI> bandList:bandas habilitadas </LI> 
	* <LI>view: vista sobre la que se carga la capa al acabar el proceso</LI>
	* <LI>filename: path con el fichero de salida</LI>
	* <LI>stevcoef: coeficiente que establece el de desviacion maxima</LI>
	*/
	public void init() {
		
		rasterSE= (FLyrRasterSE)getParam("layer");
		rois = (ArrayList)getParam("rois");
		view=(View)getParam("view");
		filename= getStringParam("filename");
		bandList = (int[])getParam("bandList");
		stevcoef= ((Double)getParam("dev")).doubleValue();
		
		bandCount  = bandList.length;
		numClases = rois.size();
		defaultClass= numClases;
		medias= new double[numClases][bandCount];
		varianza= new double[numClases][bandCount];
		
		// Calculo de estadisticas de clases
		for (int clase=0; clase<numClases; clase++)
			for (int i=0;i<bandCount;i++){
				((ROI)rois.get(clase)).setBandToOperate(bandList[i]);
					try {
						medias[clase][i]=((ROI)rois.get(clase)).getMeanValue();
						varianza[clase][i]=((ROI)rois.get(clase)).getVariance();
					} catch (GridException e) {
						e.printStackTrace();
					}
			}
	}


	
	/** Proceso de clasificacion por paralelepipedos*/
	public void process() throws InterruptedException {
		
		setGrid();
		withdefaultClass=true;
		rasterResult= RasterBuffer.getBuffer(IBuffer.TYPE_BYTE, inputGrid.getRasterBuf().getWidth(),
								inputGrid.getRasterBuf().getHeight(), 1, true);
		int c=0;
		int iNY= inputGrid.getLayerNY();
		int iNX= inputGrid.getLayerNX();	
		
		bandCount  = inputGrid.getBandCount();
		int inputGridNX = inputGrid.getNX();
		int datType = inputGrid.getRasterBuf().getDataType();
		
		// Caso buffer tipo byte
		if (datType == RasterBuffer.TYPE_BYTE){
			byte data[]= new byte[bandCount];
			for(int i=0; i<iNY;i++){
				for(int j=0; j<iNX;j++){
					inputGrid.getRasterBuf().getElemByte(i, j, data);
					c= getPixelClassForTypeByte(data);
					rasterResult.setElem(i, j, 0,(byte) c);
					
				}
				percent = i*100/inputGridNX;
			}
		}
		// Caso buffer tipo short
		if (datType == RasterBuffer.TYPE_SHORT){
			short data[]= new short[bandCount];
			for(int i=0; i<iNY;i++){
				for(int j=0; j<iNX;j++){
					inputGrid.getRasterBuf().getElemShort(i, j, data);
					c= getPixelClassForTypeShort(data);
					rasterResult.setElem(i, j, 0,(byte) c);
					
				}
				percent = i*100/inputGridNX;
			}
		}
		
		// Caso buffer tipo int
		if (datType == RasterBuffer.TYPE_INT){
			int data[]= new int[bandCount];
			for(int i=0; i<iNY;i++){
				for(int j=0; j<iNX;j++){
					inputGrid.getRasterBuf().getElemInt(i, j, data);
					c= getPixelClassForTypeInt(data);
					rasterResult.setElem(i, j, 0,(byte) c);
					
				}
				percent = i*100/inputGridNX;
			}
		}
		
		// Caso buffer tipo float
		if (datType == RasterBuffer.TYPE_FLOAT){
			float data[]= new float[bandCount];
			for(int i=0; i<iNY;i++){
				for(int j=0; j<iNX;j++){
					inputGrid.getRasterBuf().getElemFloat(i, j, data);
					c= getPixelClassForTypeFloat(data);
					rasterResult.setElem(i, j, 0,(byte) c);
					
				}
				percent = i*100/inputGridNX;
			}
		}
		
		// Caso buffer tipo double
		if (datType == RasterBuffer.TYPE_DOUBLE){
			double data[]= new double[bandCount];
			for(int i=0; i<iNY;i++){
				for(int j=0; j<iNX;j++){
					inputGrid.getRasterBuf().getElemDouble(i, j, data);
					c= getPixelClassForTypeDouble(data);
					rasterResult.setElem(i, j, 0,(byte) c);
					
				}
				percent = i*100/inputGridNX;
			}
		}
			
		writeToFile();
	}

	
}
