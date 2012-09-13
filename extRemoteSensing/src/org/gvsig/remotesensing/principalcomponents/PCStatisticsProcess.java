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
 * GNU General Public License for more details.		super();
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

import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.RasterProcess;
import org.gvsig.raster.buffer.BufferFactory;
import org.gvsig.raster.buffer.RasterBuffer;
import org.gvsig.raster.buffer.RasterBufferInvalidException;
import org.gvsig.raster.dataset.IRasterDataSource;
import org.gvsig.raster.grid.Grid;
import org.gvsig.raster.grid.GridException;
import org.gvsig.raster.util.RasterToolsUtil;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;

import com.iver.andami.PluginServices;

/**
 *PCStatisticsProcess es la clase que implementa el proceso cálculo de estadísticas avanzado
 *para el análisis de componentes principales. Se calcula la matriz de varianza-covarianza, 
 *los atovalores y autovectrores.
 *
 *@parms
 *<LI>FLyrRasterSE "inputRasterLayer": Capa raster de entrada</LI>
 *<LI>boolean[] "selectedBands": Bandas del raster original que se tienen en cuenta para la transformación</LI>
 *
 *@result
 *<LI>PCStatistics: Estadísticas reusltantes del ACP.</LI>
 *
 *@author Alejandro Muñoz Sanchez (alejandro.munoz@uclm.es)
 *@author Diego Guerrero Sevilla (diego.guerrero@uclm.es)
 *@version 19/10/2007 
 */

public class PCStatisticsProcess extends RasterProcess{
	
	private Grid 				inputGrid 			= null;
	private double				autovalors[]		= null;
	private Matrix 				coVarMatrix			= null;
	private Matrix 				autoVectorMatrix	= null;
	private int 				percent 		  	= 0;
	private boolean 			cancel 				= false;
	private	boolean 			selectedBands[]		 =null;
	private FLyrRasterSE		inputRasterLayer	= null;
	private PCStatistics		reusltStatistics    = null;
	
	/**
	 * Constructor
	 */
	public PCStatisticsProcess() {
		
	}
	
	/**
	 * @return array con los autovalores
	 */
	public Object getResult() {
		if (reusltStatistics==null)
			reusltStatistics = new PCStatistics(autoVectorMatrix,autovalors,coVarMatrix);
		return reusltStatistics;
	}
	
	
	public double[] getAutoValors(){
		return  autovalors;
	}
	
	
	/**
	 * @return Matriz de autovectores
	 */
	public Matrix getAutoVectorMatrix(){
		return autoVectorMatrix;
	}
	
	
	/**
	 * @return Matriz varianza-covarianza
	 */
	public Matrix getcoVarMatrix(){
		return coVarMatrix;
	}
	
	
	/**
	 * Cálculo de la matriz varianza covarianza de las bandas de un Grid. 
	 */
	private double[][] covarianceOptimize() {
		
		double dSum = 0;
		int iValues = 0;
		buildGrid();
		double[][] coV=new double[inputGrid.getRasterBuf().getBandCount()][inputGrid.getRasterBuf().getBandCount()];
		
		double cancelMatrix[][]=  new double[][]{{0}};
		double valorBandai=0, valorBandaj=0;
		int bandCount = inputGrid.getRasterBuf().getBandCount();
		if(inputGrid.getRasterBuf().getDataType() == RasterBuffer.TYPE_BYTE){
			// Se recorre el grid obener la matriz de cov
			for (int i = 0; i < bandCount; i++) {
				for (int j = i; j < bandCount; j++) {
					// si  cancelado se devuelve  cancelMatrix
					if(cancel)
						return cancelMatrix;
					iValues=0;
					dSum = 0;
					// Calculo covarianza entre las bandas i,j
					for (int k=0; k<inputGrid.getNX(); k++){
						for (int l=0;l<inputGrid.getNY();l++){	
								try{
									inputGrid.setBandToOperate(i);
									valorBandai=inputGrid.getCellValueAsByte(k,l) -inputGrid.getMeanValue();
									inputGrid.setBandToOperate(j);
									valorBandaj=inputGrid.getCellValueAsByte(k,l) -inputGrid.getMeanValue();
								} catch (GridException e) {
									RasterToolsUtil.messageBoxError(PluginServices.getText(this, "grid_error"), this, e);
								}
								dSum += valorBandai*valorBandaj;
								iValues++;
						}
					}
					// Se asigna el valor a la matriz 
					if (iValues>1)
						coV[i][j]=dSum/(double)(iValues);	
					else
						coV[i][j]= inputGrid.getNoDataValue();
				}
		
			if (bandCount>1)
				percent = (i+1)*100/(bandCount-1);
			else
				percent= (i+1)*100/(1);
			}

		} // Fin tipo Byte
		
		
		if(inputGrid.getRasterBuf().getDataType() == RasterBuffer.TYPE_SHORT){
			// Se recorre el grid obener la matriz de cov
			for (int i = 0; i < bandCount; i++) {
				for (int j = i; j < bandCount; j++) {
					if(cancel)
						return cancelMatrix;
					iValues=0;
					dSum = 0;
					// Calculo covarianza entre las bandas i,j
					for (int k=0; k<inputGrid.getNX(); k++){
						for (int l=0;l<inputGrid.getNY();l++){
								try{
									inputGrid.setBandToOperate(i);
									valorBandai=inputGrid.getCellValueAsShort(k,l) -inputGrid.getMeanValue();
									inputGrid.setBandToOperate(j);
									valorBandaj=inputGrid.getCellValueAsShort(k,l) -inputGrid.getMeanValue();
								} catch (GridException e) {
									RasterToolsUtil.messageBoxError(PluginServices.getText(this, "grid_error"), this, e);
								}
								dSum += valorBandai*valorBandaj;
								iValues++;
						}
					}
					// Se asigna el valor a la matriz 
					if (iValues>1)
						coV[i][j]=dSum/(double)(iValues);	
					else
						coV[i][j]= inputGrid.getNoDataValue();
	
				}
		
			if (bandCount>1)
				percent = (i+1)*100/(bandCount-1);
			else
				percent= (i+1)*100/(1);
			}

		} // Fin tipo Short
		
		
		if(inputGrid.getRasterBuf().getDataType() == RasterBuffer.TYPE_INT){
			// Se recorre el grid obener la matriz de cov
			for (int i = 0; i < bandCount; i++) {
				for (int j = i; j < bandCount; j++) {
					if(cancel)
						return cancelMatrix;
					iValues=0;
					dSum = 0;
					// Calculo covarianza entre las bandas i,j
					for (int k=0; k<inputGrid.getNX(); k++){
						for (int l=0;l<inputGrid.getNY();l++){
								try{
									inputGrid.setBandToOperate(i);
									valorBandai=inputGrid.getCellValueAsInt(k,l) -inputGrid.getMeanValue();
									inputGrid.setBandToOperate(j);
									valorBandaj=inputGrid.getCellValueAsInt(k,l) -inputGrid.getMeanValue();
								} catch (GridException e) {
									RasterToolsUtil.messageBoxError(PluginServices.getText(this, "grid_error"), this, e);
								}
								dSum += valorBandai*valorBandaj;
								iValues++;
						}
					}
					// Se asigna el valor a la matriz 
					if (iValues>1)
						coV[i][j]=dSum/(double)(iValues);	
					else
						coV[i][j]= inputGrid.getNoDataValue();
				}
		
			if (bandCount>1)
				percent = (i+1)*100/(bandCount-1);
			else
				percent= (i+1)*100/(1);
			}

		} // Fin tipo Int
		
		
		if(inputGrid.getRasterBuf().getDataType() == RasterBuffer.TYPE_FLOAT){
			// Se recorre el grid obener la matriz de cov
			for (int i = 0; i < bandCount; i++) {
				for (int j = i; j < bandCount; j++) {
					if(cancel)
						return  cancelMatrix;
					iValues=0;
					dSum = 0;
					// Calculo la covarianza entre las bandas i,j
					for (int k=0; k<inputGrid.getNX(); k++){
						for (int l=0;l<inputGrid.getNY();l++){						
								try{
									inputGrid.setBandToOperate(i);
									valorBandai=inputGrid.getCellValueAsFloat(k,l) -inputGrid.getMeanValue();
									inputGrid.setBandToOperate(j);
									valorBandaj=inputGrid.getCellValueAsFloat(k,l) -inputGrid.getMeanValue();
								} catch (GridException e) {
									RasterToolsUtil.messageBoxError(PluginServices.getText(this, "grid_error"), this, e);
								}
								dSum += valorBandai*valorBandaj;
								iValues++;
						}
					}
					// Se asigna el valor a la matriz 
					if (iValues>1)
						coV[i][j]=dSum/(double)(iValues);	
					else
						coV[i][j]= inputGrid.getNoDataValue();
				}
		
			if (bandCount>1)
				percent = (i+1)*100/(bandCount-1);
			else
				percent= (i+1)*100/(1);
			}

		} // Fin tipo Float
		
	
		if(inputGrid.getRasterBuf().getDataType() == RasterBuffer.TYPE_DOUBLE){
			// Se recorre el grid obener la matriz de cov
			for (int i = 0; i < bandCount; i++) {
				for (int j = i; j < bandCount; j++) {
					if(cancel)
						return cancelMatrix;
					iValues=0;
					dSum = 0;
					// Calculo la covarianza entre las bandas i,j
					for (int k=0; k<inputGrid.getNX(); k++){
						for (int l=0;l<inputGrid.getNY();l++){
								try{
									inputGrid.setBandToOperate(i);
									valorBandai=inputGrid.getCellValueAsDouble(k,l) -inputGrid.getMeanValue();
									inputGrid.setBandToOperate(j);
									valorBandaj=inputGrid.getCellValueAsDouble(k,l) -inputGrid.getMeanValue();
								} catch (GridException e) {
									RasterToolsUtil.messageBoxError(PluginServices.getText(this, "grid_error"), this, e);
								}
								dSum += valorBandai*valorBandaj;
								iValues++;
						}
					}
					// Asigno el valor a la matriz 
					if (iValues>1)
						coV[i][j]=dSum/(double)(iValues);	
					else
						coV[i][j]= inputGrid.getNoDataValue();
				}
		
			if (bandCount>1)
				percent = (i+1)*100/(bandCount-1);
			else
				percent= (i+1)*100/(1);
			}

		} // Fin tipo Double

		for (int i = 0; i < bandCount; i++) {
			for (int j = 0; j < bandCount; j++) {
				if(j<i)
					coV[i][j]=coV[j][i];
			}
		}
	
		return coV;
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
		return PluginServices.getText(this,"calculando_estadisticas")+"...";
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.incrementabletask.IIncrementable#getPercent()
	 */
	public int getPercent() {
		return percent;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.incrementabletask.IIncrementable#getTitle()
	 */
	public String getTitle() {
		return PluginServices.getText(this,"principal_components");
	}

	/**
	 * @return grid 
	 */
	public Grid getInputGrid() {
		return inputGrid;
	}
	
	/**
	 * @return raster de entrada
	 * */
	public FLyrRasterSE getRasterLayer(){
		return inputRasterLayer;
	}

	public void init() {
		// Se toman los parametros del proceso
		selectedBands = (boolean []) getParam("selectedBands");
		inputRasterLayer = getLayerParam("inputRasterLayer");
	}
	
	/**
	 *  Proceso de calculo de estadisticas para Principal Component
	 * */
	public void process() throws InterruptedException {

			double coVar[][]= covarianceOptimize();
			 // Calculo de autovectores:
			coVarMatrix = new Matrix(coVar);
			EigenvalueDecomposition eigenvalueDecomp = new EigenvalueDecomposition(coVarMatrix); 
			//Autovectores
			autoVectorMatrix= eigenvalueDecomp.getV();
			// Autovalores
			autovalors= eigenvalueDecomp.getRealEigenvalues();	
			
			if(!cancel)
				if(incrementableTask!=null)
					incrementableTask.processFinalize();
					
			if (externalActions!=null)
				externalActions.end(getResult());
	}

	public void interrupted() {
		// TODO Auto-generated method stub
	}
}