package org.gvsig.addo;

/**
 * Interfaz para la asignacion y recuperacion del incremento
 * de la tarea.
 *
 * 17-nov-2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public interface IOverviewIncrement {
	/**
	 * Devuelve el porcentaje del incremento
	 * @return int
	 */
	public int getPercent();
	
	/**
	 * Asigna el porcentaje de incremento de la construccion de overview.
	 * Esto se hace automaticamente desde el callback que asigna el porcentaje. 
	 */
	public void setPercent(int value);
}