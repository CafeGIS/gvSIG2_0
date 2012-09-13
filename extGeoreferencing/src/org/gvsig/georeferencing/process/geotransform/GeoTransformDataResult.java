package org.gvsig.georeferencing.process.geotransform;

import org.gvsig.raster.datastruct.GeoPointList;
/**
 * Clase que representa el resultado de un GeoTransformProcess. Recoge todos los
 * calculos realizados en un GeoTransformProcess y que la UI va a consumir.
 * 
 * @author Alejandro Muñoz Sanchez (alejandro.munoz@uclm.es)
 * @version 20/1/2008
 */
public class GeoTransformDataResult {
	private double       pixelToMapCoefX[] = null;
	private double       pixelToMapCoefY[] = null;
	private double       mapToPixelCoefX[] = null;
	private double       mapToPixelCoefY[] = null;
	private GeoPointList gpcs              = null;
	private double       rms[]             = null;
	private double       rmsXTotal         = 0;
	private double       rmsYTotal         = 0;
	private double       rmsTotal          = 0;
	private int          polynomialorden   = 0;
	private int          exp[][]           = null;

	public GeoTransformDataResult(){}
	
	
	/** @return lista de GeoPoint con que se efectuo la transformacion */	
	public GeoPointList getGpcs() {
		return gpcs;
	}
	
	/**Asignación de la lista de puntos*/
	public void setGpcs(GeoPointList gpcs) {
		this.gpcs = gpcs;
	}
	
	/**
	 * @retrun array con los coeficientes para la transformacion a coord mapas de 
	 * unas coordenadas pixel. (para las x)
	 * */
	public double[] getPixelToMapCoefX() {
		return pixelToMapCoefX;
	}
	
	/**
	 * @param con los coeficientes para las x en transformacion a coordenadas mapa de unas 
	 * coordenadas pixel. (para las x)
	 * */
	public void setPixelToMapCoefX(double[] polxCoef) {
		this.pixelToMapCoefX = polxCoef;
	}
	
	/**
	 * @retrun array con los coeficientes para la transformacion a coord mapas de 
	 * unas coordenadas pixel. (para las y)
	 * */
	public double[] getPixelToMapCoefY() {
		return pixelToMapCoefY;
	}
	
	/**
	 * @param con los coeficientes para las x en transformacion a coordenadas mapa de unas 
	 * coordenadas pixel. (para las y)
	 * */
	public void setPixelToMapCoefY(double[] polyCoef) {
		this.pixelToMapCoefY = polyCoef;
	}
	
	
	/**
	 * @retrun array con los coeficientes para la transformacion a coord pixel de 
	 * unas coordenadas map. (para las x)
	 * */
	public double[] getMapToPixelCoefX() {
		return mapToPixelCoefX;
	}


	/**
	 * @param con los coeficientes para las x en transformacion a coordenadas mapa de unas 
	 * coordenadas pixel. (para las x)
	 * */
	public void setMapToPixelCoefX(double[] mapToPixelCoefX) {
		this.mapToPixelCoefX = mapToPixelCoefX;
	}


	/**
	 * @retrun array con los coeficientes para la transformacion a coord pixel de 
	 * unas coordenadas map. (para las y)
	 * */
	public double[] getMapToPixelCoefY() {
		return mapToPixelCoefY;
	}


	/**
	 * @retrun array con los coeficientes para la transformacion a coord pixel de 
	 * unas coordenadas mapa. (para las y)
	 * */
	public void setMapToPixelCoefY(double[] mapToPixelCoefY) {
		this.mapToPixelCoefY = mapToPixelCoefY;
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
	 * @return error total cometido para las y
	 * */
	public void setRmsYTotal(double rmsYTotal) {
		this.rmsYTotal = rmsYTotal;
	}
	
	
	/** @return orden del polinomio*/
	public int getPolynomialOrden() {
		return polynomialorden;
	}

	/** @param orden del polinomio*/
	public void setPolynomialOrden(int polynomialorden) {
		this.polynomialorden = polynomialorden;
	}
	
	
	/**
	 *  Función que evalua el polinomio de transformación para obtener las coordenadas
	 *  reales de unas coordenadas pixeles que se pasan como parametros.
	 * @param x coordenada x del punto
	 * @param y coordenada y del punto
	 * @return resultado de la evaluación para x e y
	 * */
	public double[] getCoordMap(double x, double y){
		setExpPolinomial();
		double eval[]= new double [2];	
		for(int i=0; i<pixelToMapCoefX.length;i++)
		{
			eval[0]+=pixelToMapCoefX[i] * Math.pow(x, exp[i][0]) * Math.pow(y,exp[i][1]);
			eval[1]+=pixelToMapCoefY[i] * Math.pow(x, exp[i][0]) * Math.pow(y, exp[i][1]);
		}	
		return eval;
	}

	
	/**
	 *  Función que evalua el polinomio de transformación para obtener las coordenadas
	 *  pixeles de unas coordenadas mapa en un proceso de transformacion.
	 * @param x coordenada x del punto
	 * @param y coordenada y del punto
	 * @return resultado de la evaluación para x e y
	 * */
	public double[] getCoordPixel(double x, double y){
		setExpPolinomial();
		double eval[]= new double [2];	
		for(int i=0; i<pixelToMapCoefX.length;i++)
		{
			eval[0]+=mapToPixelCoefX[i] * Math.pow(x, exp[i][0]) * Math.pow(y,exp[i][1]);
			eval[1]+=mapToPixelCoefY[i] * Math.pow(x, exp[i][0]) * Math.pow(y, exp[i][1]);
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
