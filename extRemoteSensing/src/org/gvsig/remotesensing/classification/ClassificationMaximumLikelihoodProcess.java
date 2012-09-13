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
import org.gvsig.raster.util.RasterToolsUtil;

import Jama.Matrix;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.documents.view.gui.View;

/** ClassificationMaximumLikelihoodProcess implementa el método de clasificación de 
 * máxima probabilidad.
 * 
 * @see ClassificationGeneralProcess
 * @author Alejandro Muñoz Sanchez (alejandro.munoz@uclm.es)
 * @author Diego Guerrero Sevilla (diego.guerrero@uclm.es)
 * @version 19/10/2007 
*/

public class ClassificationMaximumLikelihoodProcess extends ClassificationGeneralProcess{
	
	private Matrix 					Y						= null;
	private Matrix 					result					= null;
	private Matrix 					inverseVarCovMatrix []	= null;
	private double					detS []					= null;
	private double 					means[][] 				= null;
	private int 					bandCount 				= 0;

	/** 
	* Class constructor.
	*/
	public  ClassificationMaximumLikelihoodProcess(){
	
	}
	
	
	/**
	* Metodo que implementa el clasificador de maxima probabilidad. Para cada 
	* pixel, obtiene la clase que minimiza la expresion: -Ln(P(x))= Ln(|Si|)+Y'*inverse(Si)*Y
	* 
	* @param  array de tipo byte con valores del pixel en cada una de las bandas 
	* @return clase a la que pertenece el pixel
	*/
	public int getPixelClassForTypeByte(byte pixelBandsValues[]){
		double probability=0.0, finalProbability=0.0; 
		int claseFinal=0;
		for (int clase=0; clase<numClases;clase++)
		{
			double[][] y = new double[bandCount][1];
			for (int i=0;i<bandCount;i++){
				y[i][0]=pixelBandsValues[i]-means[clase][i];
			}
			Y = new Matrix(y);
			result= (Y.transpose().times(inverseVarCovMatrix[clase])).times(Y);
			// Obtencion probabilidad de pertenencia del pixel a la clase clase
			probability= Math.log(detS[clase])+ result.get(0, 0);
			if(clase==0)
				finalProbability=probability;
			else if(probability<finalProbability){
				finalProbability=probability;
				claseFinal=clase;
				}
		}	
		return claseFinal;
	}
	
	
	/**
	 * Metodo que implementa el clasificador de maxima probabilidad. 
	 * Para cada pixel, obtiene la calase que minimiza la expresion: -Ln(P(x))= Ln(|Si|)+Y'* inverse(Si)*Y
	 * 
	 * @param array de tipo short con valores del pixel en cada una de las bandas
	 * @return clase a la que pertenece el pixel (por el metodo de maxima probabilidad)
	 */
	public int getPixelClassForTypeShort(short pixelBandsValues[]){
		double probability=0.0, finalProbability=0.0; 
		int claseFinal=0;
		for (int clase=0; clase<numClases;clase++)
		{
			double[][] y = new double[bandCount][1];
			for (int i=0;i<bandCount;i++){
				y[i][0]=pixelBandsValues[i]-means[clase][i];
			}
			Y = new Matrix(y);
			result= (Y.transpose().times(inverseVarCovMatrix[clase])).times(Y);
			// Obtencion probabilidad de pertenencia del pixel a la clase clase
			probability= Math.log(detS[clase])+ result.get(0, 0);
			if(clase==0)
				finalProbability=probability;
			else if(probability<finalProbability){
				finalProbability=probability;
				claseFinal=clase;
				}
		}	
		return claseFinal;
	}
	
	
	/**
	* Metodo que implementa el clasificador de maxima probabilidad. 
	* Para cada pixel, obtiene la calase que minimiza la expresion: -Ln(P(x))= Ln(|Si|)+Y'* inverse(Si)*Y
	* 
	* @param array de tipo int con valores del pixel en cada una de las bandas
	* @return clase a la que pertenece el pixel (por el metodo de maxima probabilidad)
	*/
	public int getPixelClassForTypeInt(int pixelBandsValues[]){
		double probability=0.0, finalProbability=0.0; 
		int claseFinal=0;
		for (int clase=0; clase<numClases;clase++)
		{
			double[][] y = new double[bandCount][1];
			for (int i=0;i<bandCount;i++){
				y[i][0]=pixelBandsValues[i]-means[clase][i];
			}
			Y = new Matrix(y);
			result= (Y.transpose().times(inverseVarCovMatrix[clase])).times(Y);
			// Obtencion probabilidad de pertenencia del pixel a la clase clase
			probability= Math.log(detS[clase])+ result.get(0, 0);
			if(clase==0)
				finalProbability=probability;
			else if(probability<finalProbability){
				finalProbability=probability;
				claseFinal=clase;
				}
		}	
		return claseFinal;
	}
	
	
	/**
	* Metodo que implementa el clasificador de maxima probabilidad. 
	* Para cada pixel, obtiene la calase que minimiza la expresion: -Ln(P(x))= Ln(|Si|)+Y'* inverse(Si)*Y
	* 
	* @param array de tipo float con valores del pixel en cada una de las bandas
	* @return clase a la que pertenece el pixel (por el metodo de maxima probabilidad)
	*/
	public int getPixelClassForTypeFloat(float pixelBandsValues[]){
		double probability=0.0, finalProbability=0.0; 
		int claseFinal=0;
		for (int clase=0; clase<numClases;clase++)
		{
			double[][] y = new double[bandCount][1];
			for (int i=0;i<bandCount;i++){
				y[i][0]=pixelBandsValues[i]-means[clase][i];
			}
			Y = new Matrix(y);
			result= (Y.transpose().times(inverseVarCovMatrix[clase])).times(Y);
			// Obtencion probabilidad de pertenencia del pixel a la clase clase
			probability= Math.log(detS[clase])+ result.get(0, 0);
			if(clase==0)
				finalProbability=probability;
			else if(probability<finalProbability){
				finalProbability=probability;
				claseFinal=clase;
				}
		}	
		return claseFinal;
	}
	

	/**
	* Metodo que implementa el clasificador de maxima probabilidad. 
	* Para cada pixel, obtiene la calase que minimiza la expresion: -Ln(P(x))= Ln(|Si|)+Y'* inverse(Si)*Y
	* 
	* @param array de tipo double con valores del pixel en cada una de las bandas
	* @return clase a la que pertenece el pixel (por el metodo de maxima probabilidad)
	*/
	public int getPixelClassForTypeDouble(double pixelBandsValues[]){
		double probability=0.0, finalProbability=0.0; 
		int claseFinal=0;
		for (int clase=0; clase<numClases;clase++)
		{
			double[][] y = new double[bandCount][1];
			for (int i=0;i<bandCount;i++){
				y[i][0]=pixelBandsValues[i]-means[clase][i];
			}
			Y = new Matrix(y);
			result= (Y.transpose().times(inverseVarCovMatrix[clase])).times(Y);
			// Obtencion probabilidad de pertenencia del pixel a la clase clase
			probability= Math.log(detS[clase])+ result.get(0, 0);
			if(clase==0)
				finalProbability=probability;
			else if(probability<finalProbability){
				finalProbability=probability;
				claseFinal=clase;
				}
		}	
		return claseFinal;
	}

	/** Metodo que recoge los parametros del proceso de clasificacion de 
	* máxima probabilidad 
	* <LI>rasterSE: Capa de entrada para la clasificación</LI>
	* <LI> rois: lista de rois</LI> 
	* <LI> bandList:bandas habilitadas </LI> 
	* <LI>view: vista sobre la que se carga la capa al acabar el proceso</LI>
	* <LI>filename: path con el fichero de salida</LI>
	*/
	public void init() {
		rasterSE= (FLyrRasterSE)getParam("layer");
		rois = (ArrayList)getParam("rois");
		view=(View)getParam("view");
		filename= getStringParam("filename");
		bandList = (int[])getParam("bandList");
		numClases = rois.size();
	}


	/** Proceso de clasificación de máxima probabilidad */
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
			try{
				means[clase][i]=((ROI)rois.get(clase)).getMeanValue();
			}catch (GridException e) {
				RasterToolsUtil.messageBoxError(PluginServices.getText(this, "grid_error"), this, e);
			}
		}
		
	
			
		int inputGridNX = inputGrid.getNX();
		int datType = inputGrid.getRasterBuf().getDataType();
			
		// Se calculan las inversas de las matrices de Varianza-covarianza de todas las rois y se almacenan en inverseVarCovMAtrix
		Matrix S = null;
		Matrix inverseS = null;
		inverseVarCovMatrix= new Matrix[numClases];
		detS = new double [numClases];
		double varCovarMatrix[][] = null;
		double subMatrix[][] = null;
			
		for (int i=0; i<numClases;i++){
			try{
				varCovarMatrix = ((ROI)rois.get(i)).getVarCovMatrix();
			}catch (GridException e) {
				RasterToolsUtil.messageBoxError(PluginServices.getText(this, "grid_error"), this, e);
			}
			if (bandList.length != rasterSE.getBandCount()){
				/*
				 * Extraer la submatiz correspondiente a las bandas que intervienen:
				 */
				subMatrix = new double[bandList.length][bandList.length];
				for (int iBand = 0; iBand < bandList.length; iBand++)
					for (int jBand = 0; jBand < bandList.length; jBand++)
						subMatrix[iBand][jBand]=varCovarMatrix[bandList[iBand]][bandList[jBand]];
				S = new Matrix(subMatrix);
				inverseS = S.inverse();
				detS[i]=S.det();
			}else
				try {
					S = new Matrix(((ROI)rois.get(i)).getVarCovMatrix());
					inverseS = S.inverse();	
					detS[i] = S.det();
			
				} catch (RuntimeException e) {
					RasterToolsUtil.messageBoxError(PluginServices.getText(this, "error_clasificacion_roi") +((ROI)rois.get(i)).getName(),this);
					return;
				} catch (GridException e) {
					RasterToolsUtil.messageBoxError(PluginServices.getText(this, "grid_error"), this, e);
				}
			
			inverseVarCovMatrix[i]= inverseS;	
		 }
		
//			 Caso Buffer tipo Byte
		if (datType == RasterBuffer.TYPE_BYTE){
			
				byte data[]= new byte[inputGrid.getBandCount()];
				for(int i=0; i<iNY;i++){
					for(int j=0; j<iNX;j++){
						inputGrid.getRasterBuf().getElemByte(i, j, data);
						c= getPixelClassForTypeByte(data);
						rasterResult.setElem(i, j, 0,(byte) c);
					}
					percent = i*100/inputGridNX;
				}
		}
		
//			Caso Buffer tipo Short
		if (datType == RasterBuffer.TYPE_SHORT){
				short data[]= new short[inputGrid.getBandCount()];
				for(int i=0; i<iNY;i++){
					for(int j=0; j<iNX;j++){
						inputGrid.getRasterBuf().getElemShort(i, j, data);
						c= getPixelClassForTypeShort(data);
						rasterResult.setElem(i, j, 0,(byte)c);
					}
					percent = i*100/inputGridNX;
				}
		}
			
//			 Caso Buffer tipo Int
		if (datType == RasterBuffer.TYPE_INT){
				int data[]= new int[inputGrid.getBandCount()];
				for(int i=0; i<iNY;i++){
					for(int j=0; j<iNX;j++){
						inputGrid.getRasterBuf().getElemInt(i, j, data);
						c= getPixelClassForTypeInt(data);
						rasterResult.setElem(i, j, 0,(byte) c);
					}
					percent = i*100/inputGridNX;
				}
		}
		
		
//			 Caso Buffer tipo Float
		if (datType == RasterBuffer.TYPE_FLOAT){
				float data[]= new float[inputGrid.getBandCount()];
				for(int i=0; i<iNY;i++){
					for(int j=0; j<iNX;j++){
						inputGrid.getRasterBuf().getElemFloat(i, j, data);
						c= getPixelClassForTypeFloat(data);
						rasterResult.setElem(i, j, 0,(byte) c);
					}
					percent = i*100/inputGridNX;
				}
		}
			
			
//			 Caso Buffer tipo Double
		if (datType == RasterBuffer.TYPE_DOUBLE){
				double data[]= new double[inputGrid.getBandCount()];
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
	//	incrementableTask.processFinalize();
	}
}
