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

import org.gvsig.raster.buffer.RasterBuffer;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.grid.GridException;
import org.gvsig.raster.grid.roi.ROI;

import com.iver.cit.gvsig.project.documents.view.gui.View;

/** ClassificationMinimumDistanceProcess implementa el método de clasificación de 
 * mínima distancia.
 * 
 * @see ClassificationGeneralProcess
 * @author Alejandro Muñoz Sanchez (alejandro.munoz@uclm.es)
 * @author Diego Guerrero Sevilla (diego.guerrero@uclm.es)
 * @version 19/10/2007 
 */
public class ClassificationMinimumDistanceProcess extends ClassificationGeneralProcess {
	
	private double 		means[][] 	= null;
	private int 		bandCount 	= 0;
	
	/**
	 * Constructor
	 * */
	public  ClassificationMinimumDistanceProcess(){
	}
		
	
	/**
	* Método que implementa el clasificador de mínima distancia. 
	* Para cada pixel, obtiene la clase para la que la distancia euclídea respecto al 
	* vector de medias es mínima
	* 
	* @param  array de tipo byte con los valores del pixel en cada una de las bandas 
	* @return clase a la que pertenece el pixel
    */
	public int getPixelClassForTypeByte(byte[] pixelBandsValues) {
		int clasefinal=0; double distanciaFinal=0.0;
		for (int clase=0; clase<numClases; clase++)
		{
			double[] y = new double[bandCount];
			double distancia=0;
			for (int i=0;i<bandCount;i++){
				y[i]=(pixelBandsValues[i]-means[clase][i]) * (pixelBandsValues[i]-means[clase][i]);
				//y[i]=Math.pow((pixelBandsValues[i]-means[clase][i]),2);
				distancia+=y[i];
			}
			//distancia= Math.sqrt(distancia); //No hacer la raiz: Para comparar da igual la distancia que la distancia al cuadrado...
			if (clase==0)
				distanciaFinal=distancia;
			
			else if (distancia<distanciaFinal){
				distanciaFinal = distancia;
				clasefinal = clase;
				}
		}	
		return clasefinal;
	}
	
	
	/**
	* Método que implementa el clasificador de mínima distancia. 
	* Para cada pixel, obtiene la clase para la que la distancia euclídea respecto al 
	* vector de medias es mínima
	* 
	* @param  array de tipo short con los valores del pixel en cada una de las bandas 
	* @return clase a la que pertenece el pixel
    */
	public int getPixelClassForTypeShort(short[] pixelBandsValues) {
		int clasefinal=0; double distanciaFinal=0.0;
		for (int clase=0; clase<numClases; clase++)
		{
			double[] y = new double[bandCount];
			double distancia=0;
			for (int i=0;i<bandCount;i++){
				y[i]=(pixelBandsValues[i]-means[clase][i]) * (pixelBandsValues[i]-means[clase][i]);
				//y[i]=Math.pow((pixelBandsValues[i]-means[clase][i]),2);
				distancia+=y[i];
			}
			//distancia= Math.sqrt(distancia); //No hacer la raiz: Para comparar da igual la distancia que la distancia al cuadrado...
			if (clase==0)
				distanciaFinal=distancia;	
			else if (distancia<distanciaFinal){
				distanciaFinal= distancia;
				clasefinal=clase;
				}
		}	
		return clasefinal;
	}
	
	
	/**
	* Método que implementa el clasificador de mínima distancia. 
	* Para cada pixel, obtiene la clase para la que la distancia euclídea respecto al 
	* vector de medias es mínima
	* 
	* @param  array de tipo int con los valores del pixel en cada una de las bandas 
	* @return clase a la que pertenece el pixel
    */
	public int getPixelClassForTypeInt(int[] pixelBandsValues) {
		
		int clasefinal=0; double distanciaFinal=0.0;
		for (int clase=0; clase<numClases; clase++)
		{
			double[] y = new double[bandCount];
			double distancia=0;
			for (int i=0;i<bandCount;i++){
				y[i]=(pixelBandsValues[i]-means[clase][i]) * (pixelBandsValues[i]-means[clase][i]);
				//y[i]=Math.pow((pixelBandsValues[i]-means[clase][i]),2);
				distancia+=y[i];
			}
			//distancia= Math.sqrt(distancia); //No hacer la raiz: Para comparar da igual la distancia que la distancia al cuadrado...
			if (clase==0)
				distanciaFinal=distancia;
			else if (distancia<distanciaFinal){
				distanciaFinal= distancia;
				clasefinal=clase;
				}
		}	
		return clasefinal;
	}
	
	
	/**
	* Método que implementa el clasificador de mínima distancia. 
	* Para cada pixel, obtiene la clase para la que la distancia euclídea respecto al 
	* vector de medias es mínima
	* 
	* @param  array de tipo float con los valores del pixel en cada una de las bandas 
	* @return clase a la que pertenece el pixel
    */
	public int getPixelClassForTypeFloat(float[] pixelBandsValues) {
		int clasefinal=0; double distanciaFinal=0.0;
		for (int clase=0; clase<numClases; clase++)
		{
			double[] y = new double[bandCount];
			double distancia=0;
			for (int i=0;i<bandCount;i++){
				y[i]=(pixelBandsValues[i]-means[clase][i]) * (pixelBandsValues[i]-means[clase][i]);
				//y[i]=Math.pow((pixelBandsValues[i]-means[clase][i]),2);
				distancia+=y[i];
			}
			//distancia= Math.sqrt(distancia); //No hacer la raiz: Para comparar da igual la distancia que la distancia al cuadrado...
			if (clase==0)
				distanciaFinal=distancia;
			else if (distancia<distanciaFinal){
				distanciaFinal= distancia;
				clasefinal=clase;
				}
		}	
		return clasefinal;
	}
	
	
	/**
	* Método que implementa el clasificador de mínima distancia. 
	* Para cada pixel, obtiene la clase para la que la distancia euclídea respecto al 
	* vector de medias es mínima
	* 
	* @param  array de tipo double con los valores del pixel en cada una de las bandas 
	* @return clase a la que pertenece el pixel
    */
	public int getPixelClassForTypeDouble(double[] pixelBandsValues) {
		int clasefinal=0; double distanciaFinal=0.0;
		for (int clase=0; clase<numClases; clase++)
		{
			double[] y = new double[bandCount];
			double distancia=0;
			for (int i=0;i<bandCount;i++){
				y[i]=(pixelBandsValues[i]-means[clase][i]) * (pixelBandsValues[i]-means[clase][i]);
				//y[i]=Math.pow((pixelBandsValues[i]-means[clase][i]),2);
				distancia+=y[i];
			}
			//distancia= Math.sqrt(distancia); //No hacer la raiz: Para comparar da igual la distancia que la distancia al cuadrado...
			if (clase==0)
				distanciaFinal=distancia;
			else if (distancia<distanciaFinal){
				distanciaFinal= distancia;
				clasefinal=clase;
				}
		}	
		return clasefinal;
	}

	
	/** Metodo que recoge los parametros del proceso de clasificacion de 
	* mínima distancia 
	* <LI>rasterSE: Capa de entrada para la clasificación</LI>
	* <LI> rois: lista de rois</LI> 
	* <LI> bandList:bandas habilitadas </LI> 
	* <LI>view: vista sobre la que se carga la capa al acabar el proceso</LI>
	* <LI>filename: path con el fichero de salida</LI>
	*/
	public void init() {
		rasterSE= getLayerParam("layer");
		rois = (ArrayList)getParam("rois");
		view=(View)getParam("view");
		filename= getStringParam("filename");
		bandList = (int[])getParam("bandList");
		numClases = rois.size();	
	}

	
	/** Proceso de clasificación de mínima distancia */
	public void process() throws InterruptedException {
		setGrid();
		rasterResult= RasterBuffer.getBuffer(IBuffer.TYPE_BYTE, inputGrid.getRasterBuf().getWidth(), inputGrid.getRasterBuf().getHeight(), 1, true);
		int c=0;
		int iNY= inputGrid.getLayerNY();
		int iNX= inputGrid.getLayerNX();	
		
		bandCount  = inputGrid.getBandCount();
		means = new double[numClases][bandCount];
		for (int clase=0; clase<numClases; clase++)
			for (int i=0;i<bandCount;i++){
				((ROI)rois.get(clase)).setBandToOperate(bandList[i]);
					try {
						means[clase][i]=((ROI)rois.get(clase)).getMeanValue();
						
					} catch (GridException e) {
						e.printStackTrace();
					}
			}
		
				
		int inputGridNX = inputGrid.getNX();
		int datType = inputGrid.getRasterBuf().getDataType();
			
//		Caso Buffer tipo Byte
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
		
//		Caso Buffer tipo Short
		else if (datType == RasterBuffer.TYPE_SHORT){
			short data[]= new short[bandCount];
			for(int i=0; i<iNY;i++){
				for(int j=0; j<iNX;j++){
					inputGrid.getRasterBuf().getElemShort(i, j, data);
					c= getPixelClassForTypeShort(data);
					rasterResult.setElem(i, j, 0,(byte)c);
				}
				percent = i*100/inputGridNX;
			}
		}
			
//		Caso Buffer tipo Int
		else if (datType == RasterBuffer.TYPE_INT){
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
		
		
//		Caso Buffer tipo Float
		else if (datType == RasterBuffer.TYPE_FLOAT){
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
				
			
//		Caso Buffer tipo Float
		else if (datType == RasterBuffer.TYPE_DOUBLE){
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
