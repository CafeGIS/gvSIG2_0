/*
* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
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

package org.gvsig.remotesensing.imagefusion.process;

import org.gvsig.raster.buffer.RasterBuffer;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.remotesensing.principalcomponents.PCImageProcess;
import org.gvsig.remotesensing.principalcomponents.PCStatistics;
import org.gvsig.remotesensing.principalcomponents.PCStatisticsProcess;

import Jama.Matrix;

import com.iver.andami.PluginServices;

/**
 * Proceso de fusion por componenetes principales.
 * 
 * @version 27/02/2008
 * @author Alejandro Muñoz Sánchez (alejandro.munoz@uclm.es)
 * 
 */
public class PCAFusionProcess extends ImageFusionProcess {

	// bandas para las que se realiza el procedimiento. Pueden ser 3 caso de opcion 
	// bandas de visualización o todas las bandas del raster en caso de elegirse la opcion allBands
	int bands =0;
	// Proceso para el cálculo de estadisticas de componentes principales
	PCStatisticsProcess calcStatisticProcess= null;
	
	// Proceso para la obtencion de la imagen de componentes principales (imagen tipo float)
	PCImageProcess imageProcess= null;
	private int percent =0;
	
	
	public void init(){
		super.init();
		bands= getIntParam("bands");
	}
	

	public void process() throws InterruptedException {
		
		insertLineLog(RasterToolsUtil.getText(this, "fusion_pc"));
//		 Calulo de componentes principales 
		calcStatisticProcess = new PCStatisticsProcess();
		calcStatisticProcess.addParam("inputRasterLayer",rasterSE);
		// TODO: Cambiar bandas de visualizacion o todas las bandas
		calcStatisticProcess.addParam("selectedBands",new boolean[]{true,true,true});
		calcStatisticProcess.run();
		
//       Imagen resultante componentes principales
		PCStatistics pcStatistics = new PCStatistics(calcStatisticProcess.getAutoVectorMatrix(),
				(double[])(((PCStatistics) (calcStatisticProcess.getResult())).getAutovalues()), null);
		
		imageProcess = new PCImageProcess();
		imageProcess.addParam("inputRasterLayer",calcStatisticProcess.getRasterLayer());
		imageProcess.addParam("statistics",pcStatistics);
//		 TODO: Cambiar bandas de visualizacion o todas las bandas
		imageProcess.addParam("selectedComponents",new boolean[]{true,true,true});
		imageProcess.addParam("selectedBands",new boolean[]{true,true,true});
		imageProcess.addParam("outputPath", filename);
		
		Matrix autoV= calcStatisticProcess.getAutoVectorMatrix();
		int resultOrden[]= new int[autoV.getRowDimension()];
		int cont = autoV.getRowDimension()-1;
		for(int i=0;i<autoV.getRowDimension();i++){
					resultOrden[i]=cont;
					cont--;
		}
		
		double order[][]= new double[autoV.getRowDimension()][autoV.getColumnDimension()];
		for(int i=0; i<resultOrden.length;i++)
			for(int j=0; j<autoV.getColumnDimension();j++)
				order[i][j]=autoV.get(j,resultOrden[i]);

		calcStatisticProcess=null;
		imageProcess.run();
		RasterBuffer pcResult= imageProcess.getBufferResult();
		imageProcess=null;
	
		
// 		Supersampleo de la multiespectral a la pancromatica. A la vez aplicar transformacion inversa PCA-1 y obtener la imagen.
		IBuffer pcTmp= RasterBuffer.getBuffer(RasterBuffer.TYPE_FLOAT, highBandGrid.getRasterBuf().getWidth(), highBandGrid.getRasterBuf().getHeight(), pcResult.getBandCount(), true);	
		rasterResult= RasterBuffer.getBuffer(RasterBuffer.TYPE_BYTE, highBandGrid.getRasterBuf().getWidth(), highBandGrid.getRasterBuf().getHeight(), pcResult.getBandCount(), true);
		byte newData []= new byte[pcResult.getBandCount()];
		for(int row=0; row<pcResult.getHeight(); row++){	 
			for(int col=0; col<pcResult.getWidth();col++){	
				float data[]= new float[pcResult.getBandCount()];
				pcResult.getElemFloat(row,col,data);
				for(int facx= 0; facx<cellFactorX; facx++){	 
					int line= (int)(cellFactorY*row)+facx;
					for(int facy= 0; facy<cellFactorY; facy++)	{	
							int colum= (int)(cellFactorY*col)+facy;
							pcTmp.setElemFloat(line,colum,data);
							pcTmp.setElem(line,colum,0,(float)(getValuePanc(line,colum)));
							pcTmp.getElemFloat(line,colum,data);
							newData=solveSystem(new Matrix(order),data);
							rasterResult.setElemByte(line,colum,newData);
					} 
				}	 
			}	
			percent = row*100/pcResult.getHeight();	 
		}	
			
// 		Ecritura del resultado
		writeToFile();
	}


	/** @return String con el log correspondiente a cada parte del proceso*/
	public String getLog(){
		if(calcStatisticProcess!=null)
			return  PluginServices.getText(this,"log_fusion1");
		else if (imageProcess!=null)
			return PluginServices.getText(this,"log_fusion2");
		else {
			if(writerBufferServer==null)
				return PluginServices.getText(this,"log_fusion3");
			else
				return PluginServices.getText(this,"write_to_file");
		}
	}
	
	/** @return int con porcentaje asociado a cada tarea*/
	public int getPercent(){
		if(calcStatisticProcess!=null)
			return  calcStatisticProcess.getPercent();
		else if (imageProcess!=null)
			return imageProcess.getPercent();
		else {
			if(writerBufferServer==null)
				return percent;
			else
				return writerBufferServer.getPercent();
		}
	}
	
	
	/** 
	 * @ return valor de la posicion line col de la banda pancromática
	 * 
	 * */
	protected double getValuePanc(int line, int col){

		double value= 0.0;
		switch (highBandGrid.getRasterBuf().getDataType()){
			case RasterBuffer.TYPE_BYTE:
				value= highBandGrid.getRasterBuf().getElemByte(line,col,0)&0xff;
				break;
			case RasterBuffer.TYPE_SHORT:
				value= highBandGrid.getRasterBuf().getElemShort(line,col,0);
				break;
			case RasterBuffer.TYPE_INT:
				value= highBandGrid.getRasterBuf().getElemInt(line,col,0);
				break;
			case RasterBuffer.TYPE_FLOAT:
				value= highBandGrid.getRasterBuf().getElemFloat(line,col,0);
				break;
			case RasterBuffer.TYPE_DOUBLE:
				value= highBandGrid.getRasterBuf().getElemDouble(line,col,0);
		}
		return  value;
	}
	
	public void interrupted() {
	}

	
	
	public byte[] solveSystem(Matrix matrix, float columResult[]){
		byte xCoef[] = new byte[columResult.length];
		double[][] a = new double[columResult.length][1];
		for (int i = 0; i < columResult.length; i++)
			a[i][0] = columResult[i];
		Matrix c = null;
		c = matrix.solve(new Matrix(a));
		for (int i = 0; i < columResult.length; i++){
			
			xCoef[i] = (byte)(c.get(i, 0));
		
		}
	//	}
		return xCoef;
	}
}
