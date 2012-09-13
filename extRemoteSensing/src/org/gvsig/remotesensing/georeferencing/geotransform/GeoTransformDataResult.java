package org.gvsig.remotesensing.georeferencing.geotransform;


import org.gvsig.raster.datastruct.GeoPoint;

/**
 *  Clase que representa el resultado de un GeoTransformProcess. Recoge todos los calculos realizados en
 *  un GeoTransformProcess y que la UI va a consumir.
 *  @author Alejandro Muñoz Sanchez (alejandro.munoz@uclm.es)
 * 	@version 20/1/2008
 **/

public class GeoTransformDataResult {
	
	private double polxCoef []= null;
	private double polyCoef []= null;
	private double xError [] = null;
	private double yError [] = null;
	private GeoPoint gpcs[]= null;
	private double rms[]=null;
	private double rmsXTotal=0;
	private double rmsYTotal=0;
	private double rmsTotal=0;
	private double xEvaluate[] = null;
	private double yEvaluate[] = null;
	private int polynomialorden= 0;
	private int exp [][]=null;

	
	public GeoTransformDataResult(){}
	
	
	/** @return lista de GeoPoint con que se efectuo la transformacion */	
	public GeoPoint[] getGpcs() {
		return gpcs;
	}
	
	/**Asignación de la lista de puntos*/
	public void setGpcs(GeoPoint[] gpcs) {
		this.gpcs = gpcs;
	}
	
	/**
	 * @retrun array con los coeficientes para las x.
	 * */
	public double[] getPolxCoef() {
		return polxCoef;
	}
	
	/**
	 * array con los coeficientes para las x.
	 * */
	public void setPolxCoef(double[] polxCoef) {
		this.polxCoef = polxCoef;
	}
	
	/**
	 * @retrun array con los coeficientes para las y.
	 * */
	public double[] getPolyCoef() {
		return polyCoef;
	}
	
	/**
	 * array con los coeficientes para las y.
	 * */
	public void setPolyCoef(double[] polyCoef) {
		this.polyCoef = polyCoef;
	}
	
	/**
	 * @return array con los rms para cada punto.
	 * */
	public double[] getRms() {
		return rms;
	}
	
	/**
	 * asignacion del vector con los rms para cada punto
	 * */
	public void setRms(double[] rms) {
		this.rms = rms;
	}
	
	/**
	 * @return array con los rms para cada punto
	 * */
	public double getRmsTotal() {
		return rmsTotal;
	}
	
	
	public void setRmsTotal(double rmsTotal) {
		this.rmsTotal = rmsTotal;
	}
	
	
	/**
	 * @return error total cometido para las x
	 * */
	public double getRmsXTotal() {
		return rmsXTotal;
	}
	
	/**
	 * return error total cometido para las x
	 * */
	public void setRmsXTotal(double rmsXTotal) {
		this.rmsXTotal = rmsXTotal;
	}
	
	/**
	 * error total cometido para las y
	 * */
	public double getRmsYTotal() {
		return rmsYTotal;
	}
	
	/**
	 * return error total cometido para las y
	 * */
	public void setRmsYTotal(double rmsYTotal) {
		this.rmsYTotal = rmsYTotal;
	}
	
	/**
	 * @array con los errores cometidos para cada x
	 * */
	public double[] getXError() {
		return xError;
	}
	
	/**
	 * @array con los errores cometidos para cada x
	 * */
	public void setXError(double[] error) {
		xError = error;
	}
	
	
	/**
	 *@return array con evaluacion de las coordenadas y de la lista de 
	 *puntos de control. 
	 * */
	public double[] getXEvaluate() {
		return xEvaluate;
	}
	

	/**
	 * resultado de la evaluacion del polinomio en las coord x de la lista de 
	 * los puntos de control
	 * */
	public void setXEvaluate(double[] evaluate) {
		xEvaluate = evaluate;
	}
	
	/**
	 * @array con los errores cometidos para cada y
	 * */
	public double[] getYError() {
		return yError;
	}
	
	/**
	 * array con los errores cometidos para cada x
	 * */
	public void setYError(double[] error) {
		yError = error;
	}
	
	/**
	 *@return array con evaluacion de las coordenadas y de la lista de 
	 *puntos de control. 
	 * */
	public double[] getYEvaluate() {
		return yEvaluate;
	}
	
	/**
	 * resultado de la evaluacion del polinomio en las coord y de la lista de 
	 * los puntos de control
	 * */
	public void setYEvaluate(double[] evaluate) {
		yEvaluate = evaluate;
	}
	

	public int getPolynomialOrden() {
		return polynomialorden;
	}


	public void setPolynomialOrden(int polynomialorden) {
		this.polynomialorden = polynomialorden;
	}
	
	
	/**
	 *  Función que evalua el polinomio de transformación para un punto especifico
	 * @param x coordenada x del punto
	 * @param y coordenada y del punto
	 * @return resultado de la evaluación para x e y
	 * */
	public double[] evaluate(double x, double y){
		setExpPolinomial();
		double eval[]= new double [2];	
		for(int i=0; i<polxCoef.length;i++)
		{
			eval[0]+=polxCoef[i] * Math.pow(x, exp[i][0]) * Math.pow(y,exp[i][1]);
			eval[1]+=polyCoef[i] * Math.pow(x, exp[i][0]) * Math.pow(y, exp[i][1]);
		}	
		return eval;
	}


	private void setExpPolinomial(){
		if(exp==null)
			{
				int minGPC=(getPolynomialOrden()+1)*(getPolynomialOrden()+2)/2;
				exp=new int[minGPC][2];
				int k=-1;
					for (int i=0; i<=getPolynomialOrden(); i++){
						for(int j=0; j<=i; j++){
							k++;
							exp[k][0]=i-j;
							exp[k][1]=j;
						}
					}
			}	
	}
	
}
