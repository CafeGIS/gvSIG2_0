package org.gvsig.raster.datastruct;


/**
 * Clase que define un intervalo de datos. Es util para cosas como el calculo de histogramas
 * con tipo de datos en coma flotante en el cual hay que dividir los intervalos en
 * clases. Las clases se tendrán en cuenta como un intervalo el cual el menor es cerrado y el 
 * mayor abierto, es decir
 * <PRE>
 * En las clases:
 * 0-1000
 * 1000-2000
 * los intervalos son 
 * [0, 1000 [  de 0-999.9p
 * [1000, 2000[ de 1000-1999.9p
 * </PRE>
 *  
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class HistogramClass {	
	private double 	value = 0D;
	private double 	min = 0D;
	private double 	max = 0D;

	/**
	 * Constructor.
	 * @param a Entero que representa al un valor de la clase
	 * @param b Entero que representa al un valor de la clase
	 */
	public HistogramClass(double a, double b){
		this.max = (a >= b) ? a : b;
		this.min = (a <= b) ? a : b;
	}
	
	/**
	 * Comprueba si el valor pasado por parámetro está dentro del intervalo
	 * @param value Valor a comprobar
	 * @return true si está dentro del intervalo y false si no lo está
	 */
	public boolean isIn(double value){
		return (value >= min && value < max);
	}
	
	/**
	 * Obtiene el valor máximo de la clase
	 * @return Entero que representa al valor máximo de la clase
	 */
	public double getMax() {
		return max;
	}
	
	/**
	 * Asigna el valor máximo de la clase
	 * @param max Entero que representa al valor máximo de la clase
	 */
	public void setMax(double max) {
		this.max = max;
	}
	
	/**
	 * Obtiene el valor mínimo de la clase
	 * @return Entero que representa al valor mínimo de la clase
	 */
	public double getMin() {
		return min;
	}
	
	/**
	 * ASigna el valor mínimo de la clase
	 * @param min Entero que representa al valor mínimo de la clase
	 */
	public void setMin(double min) {
		this.min = min;
	}

	/**
	 * Obtiene el valor de la clase
	 * @return double con el valor
	 */
	public double getValue() {
		return value;
	}

	/**
	 * Asigna el valor de la clase
	 * @param double con el valor
	 */
	public void setValue(double value) {
		this.value = value;
	}	
	
	/**
	 * Incrementa en n el valor especificado
	 * @param double n
	 */
	public void increment(double n) {
		this.value += n;
	}
}
