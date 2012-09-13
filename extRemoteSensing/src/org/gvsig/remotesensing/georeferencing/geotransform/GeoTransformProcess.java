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

package org.gvsig.remotesensing.georeferencing.geotransform;

import org.gvsig.raster.RasterProcess;
import org.gvsig.raster.datastruct.GeoPoint;

import Jama.Matrix;

import com.iver.andami.PluginServices;

/**
 *  Clase que representa una transformacion polinomial  para la convertir las
 *  coordenadas de una imagen en la imagen rectificada.
 *  
 *  
 *  @author Alejandro Muñoz Sanchez (alejandro.munoz@uclm.es)
 * 	@version 20/1/2008
 **/
public class GeoTransformProcess extends RasterProcess {

	// Lista de puntos de control
	private GeoPoint gpcs[]= null;
	
	// porcentage del proceso global de la tarea
	private int percent=0;
	
	// orden del polinomio aproximador
	protected int orden = 0;
	
	// numero minimo de puntos 
	protected int minGPC=0;
	
	//coeficientes del polinomio de transformacion para las coordenadas en X
	private double []coefX=null;
	
    //coeficientes del polinomio de transformacion para las coordenadas en Y
	private double []coefY=null;
	
	// Lista con los valores de las potencias de x e y  
	private int exp[][] = null;
	
	// vector con error de la funcion en x para cada punto
	private double[] xError= null;
	
	//vector con el error de la funcion en y para cada punto
	private double [] yError=null;
	
	// rms vector con los rms para cada punto
	private double rms[]=null;
	
	// rms total en las x
	private double rmsXTotal=0;
	
	// rms total en las y
	private double rmsYTotal=0;
	
	// rms total
	private double rmsTotal=0;
	
	GeoTransformDataResult resultData= null;
	
	double xEvaluate[]= null;
	double yEvaluate[]=null;
	
	/** 
	* Metodo que recoge los parametros del proceso de transformación 
	* <LI>gpcs: lista de puntos de control</LI>> 
	* <LI> orden: orden del polinomio de transformacion </LI> 
	*/
	public void init() {		
		gpcs = (GeoPoint[])getParam("gpcs");
		orden= (int)getIntParam("orden");
		minGPC = (orden+1)*(orden+2)/2;
	    exp=new int[minGPC][2];
	    resultData= new GeoTransformDataResult();
		// Chequear si el numero de puntos es suficiente para determinar la transformacion de orden n. 
		if(!enoughPoints()){
			// NOTIFICAR, NO SUFICIENTES PUNTOS PARA ORDEN SELECCIONADO
			minGPC=0;		
		}
	}
	
	/**
	 * @return true si se proporciona el numero minimo de puntos de entrada 
	 * para la transformación de orden seleccionado. false en caso contrario.
	 * */		
	public boolean enoughPoints() {
		return (gpcs.length>=minGPC);
	}

	
	/**
	 *  Proceso
	 **/
	public void process() {
		if(minGPC>0){
			
			// Obtencion  polinomio de transformacion en x
			calculatePolinomialCoefX();
			// Obtencion del polinomio de transformación en y
			calculatePolinomialCoefY();
			// calculo de los resultados
			calculateRMSerror();
			// Se almacenan los resultados en dataResult
			resultData.setGpcs(gpcs);
			resultData.setPolxCoef(coefX);
			resultData.setPolyCoef(coefY);
			resultData.setRms(rms);
			resultData.setRmsXTotal(rmsXTotal);
			resultData.setRmsYTotal(rmsYTotal);
			resultData.setXError(xError);
			resultData.setYError(yError);
			resultData.setRmsXTotal(rmsXTotal);
			resultData.setRmsYTotal(rmsYTotal);
			resultData.setRmsTotal(rmsTotal);
			resultData.setXEvaluate(xEvaluate);
			resultData.setYEvaluate(yEvaluate);
			resultData.setPolynomialOrden(orden);
			
			if(externalActions!=null)
				externalActions.end(resultData);
		}
	}


    /**
     *  Calculo de los coeficientes del polinimio aproximador.
     *  @return array con los coeficientes para las x. 
     * 
     * */
	public void calculatePolinomialCoefX(){
		double matrixDx[][]= new double [minGPC][minGPC]; 
		double result[]= new double[minGPC];
		int k=-1;
		// Obtencion de la primera fila de la matriz
		for (int filas=0; filas<minGPC;filas++)
		{	k=-1;			
		for (int i=0; i<=orden; i++)
			for(int j=0; j<=i; j++){
				k++;
				for(int v=0; v<gpcs.length;v++)
					matrixDx[filas][k]+=(Math.pow(gpcs[v].pixelPoint.getX(),i-j)* Math.pow(gpcs[v].pixelPoint.getX(),exp[filas][0]))
							* (Math.pow(gpcs[v].pixelPoint.getY(),j)* Math.pow(gpcs[v].pixelPoint.getY(),exp[filas][1]));	
				// Para la fila 0 se guardan los exponentes
				if(filas==0){
					exp[k][0]=i-j;
					exp[k][1]=j;

					// Se calcula el resultado de !!!!!
					for(int v=0; v<gpcs.length;v++)
						result[k]+=(Math.pow(gpcs[v].pixelPoint.getX(),i-j)* Math.pow(gpcs[v].pixelPoint.getX(),exp[filas][0]))
						* (Math.pow(gpcs[v].pixelPoint.getY(),j)* Math.pow(gpcs[v].pixelPoint.getY(),exp[filas][1]))*gpcs[v].mapPoint.getX();	
				}
			}
		}
		Matrix matrixResult= new Matrix(matrixDx);
		coefX=solveSistem(matrixResult,result);	
	}
	
	
	// TO DO: Ver la manera de unificar con setDxGeneral(Parametrizar un metodo general)..... Estudiar optimizaciones!!! 
	// Cambios necesarios para el caolculo de coeficientes para coordenadas y
	 /**
     *  Calculo de los coeficientes del polinimio aproximador.
     *  @return array con los coeficientes para las x. 
     * 
     * */
	public void calculatePolinomialCoefY(){
		double matrixDy[][]= new double [minGPC][minGPC]; 
		double result[]= new double[minGPC];
		int k=-1;
		// Obtencion de la primera fila de la matriz
		for (int filas=0; filas<minGPC;filas++)
		{	k=-1;			
		for (int i=0; i<=orden; i++)
			for(int j=0; j<=i; j++){
				k++;
				for(int v=0; v<gpcs.length;v++)
					matrixDy[filas][k]+=(Math.pow(gpcs[v].pixelPoint.getX(),i-j)* Math.pow(gpcs[v].pixelPoint.getX(),exp[filas][0]))
							* (Math.pow(gpcs[v].pixelPoint.getY(),j)* Math.pow(gpcs[v].pixelPoint.getY(),exp[filas][1]));	
				// Para la fila 0 se guardan los exponentes
				if(filas==0){
					exp[k][0]=i-j;
					exp[k][1]=j;

					// Se calcula el resultado de !!!!!
					for(int v=0; v<gpcs.length;v++)
						result[k]+=(Math.pow(gpcs[v].pixelPoint.getX(),i-j)* Math.pow(gpcs[v].pixelPoint.getX(),exp[filas][0]))
						* (Math.pow(gpcs[v].pixelPoint.getY(),j)* Math.pow(gpcs[v].pixelPoint.getY(),exp[filas][1]))*gpcs[v].mapPoint.getY();	
				}
			}
		}
		Matrix matrixResult= new Matrix(matrixDy);
		coefY=solveSistem(matrixResult,result);	
	}
		
	/**
	 * @return array con la solucion al sistema de ecuadiones.
	 * */
	public double[] solveSistem(Matrix matrix, double columResult[]){
		double xCoef []= new double[minGPC];
		double [][]a= new double[columResult.length][1]; 
		for (int i=0; i<columResult.length;i++)
			a[i][0]=columResult[i];
		Matrix c= matrix.solve(new Matrix(a));
		for (int i=0; i<columResult.length;i++)
			xCoef[i]=c.get(i,0);
		return xCoef;
	}
	
	
	/**
	 * calculo de errores
	 * */
	public void calculateRMSerror(){
	
		int numgpcs= gpcs.length;
		xEvaluate= new double [numgpcs];
		yEvaluate= new double [numgpcs];
		rms = new double [numgpcs];
		xError= new double [numgpcs];
		yError= new double[numgpcs];
		double rmsxTotal=0;
		double rmsyTotal=0;
		
		for(int im_point=0; im_point<numgpcs;im_point++){
		
			for(int i=0; i<minGPC;i++)
			{
				xEvaluate[im_point]+=coefX[i] * Math.pow(gpcs[im_point].pixelPoint.getX(), exp[i][0]) * Math.pow(gpcs[im_point].pixelPoint.getY(), exp[i][1]);
				yEvaluate[im_point]+=coefY[i] * Math.pow(gpcs[im_point].pixelPoint.getX(), exp[i][0]) * Math.pow(gpcs[im_point].pixelPoint.getY(), exp[i][1]);

			}	
			
			xError[im_point]= Math.pow(xEvaluate[im_point] -gpcs[im_point].mapPoint.getX(), 2);
			rmsxTotal+= xError[im_point];
			yError[im_point]= Math.pow(yEvaluate[im_point] -gpcs[im_point].mapPoint.getY(), 2);
			rmsyTotal+= yError[im_point];
			rms[im_point]=Math.sqrt
			( 
					xError[im_point]+ yError[im_point]		
			);
			rmsTotal+=rms[im_point];
		}
		
		this.rmsTotal= rmsTotal/numgpcs; 
		this.rmsXTotal= rmsxTotal/numgpcs;
		this.rmsYTotal= rmsyTotal/numgpcs;
		
		System.out.print("Base X\t\t");
		System.out.print("Base Y\t\t");
		System.out.print("WarpX\t\t");
		System.out.print("WarpY\t\t");
		System.out.print("PredicX\t\t\t\t");
		System.out.print("PredicY\t\t\t\t");
		System.out.print("ErrorX\t\t\t\t");
		System.out.print("ErrorY\t\t\t\t");
		System.out.print("RMS");
		// Escribir resultados
		for(int i=0; i<numgpcs;i++)
			{
				System.out.print("\n");
				System.out.print((new Double(gpcs[i].pixelPoint.getX()).toString()+"\t\t"));
				System.out.print((new Double(gpcs[i].pixelPoint.getY()).toString()+"\t\t"));
				System.out.print((new Double(gpcs[i].mapPoint.getX()).toString()+"\t\t"));
				System.out.print((new Double(gpcs[i].mapPoint.getY()).toString()+"\t\t"));
				System.out.print((new Double(xEvaluate[i]).toString()+"\t\t"));
				System.out.print((new Double(yEvaluate[i]).toString()+"\t\t"));
				System.out.print((new Double(xError[i]).toString()+"\t\t"));
				System.out.print((new Double(yError[i]).toString()+"\t\t"));
				System.out.print((new Double(rms[i]).toString()+"\t\t"));

			}
		
	}
	
	
	/**
	 * @return array con el error en la coordenada x para los puntos de entrada 
	 * 
	 * */
	public double[] getxError(){
		return xError;
		
	}
	
	/**
	 * @return array con el error en la coordenada y para los puntos de entrada 
	 * 
	 * */
	public double[] getyError(){
		return xError;	
	}
	
	
	/**
	 *  @return error total para la coordenada X
	 * */
	public double getRMSx(){
		return rmsXTotal;	
	}
	
	
	/**
	 *  @return error total para la coordenada y
	 * */
	public double getRMSy(){
		return rmsYTotal;	
	}
	
	/**
	 *  @return error total para la coordenada y
	 * */
	public double getRMSTotal(){
		return rmsTotal;	
	}
	
		
	public String getTitle() {
		return PluginServices.getText(this,"transformacion");
	}

	public String getLog() {
		return PluginServices.getText(this,"calculando_transformacion");
	}

	
	public int getPercent() {
		return percent;
	}

	
}
	