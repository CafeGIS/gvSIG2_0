/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
 *
 * Copyright (C) 2007 IVER T.I. and Generalitat Valenciana.
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
 */
package org.gvsig.raster.beans.stretchselector;

import java.util.Observable;

/**
 * Modelo de datos para el componente de selecci�n de lista de tramos.
 * 
 * 06/08/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class StretchSelectorData extends Observable {
	private String         size                 = "255";
	private String         number               = "1";
	private String         min                  = "0";
	private String         max                  = "255";
	
	/**
	 * Devuelve el n�mero de tramos
	 * @return
	 */
	public int getStretchNumber() {
		try {
			return (int)Double.parseDouble(number);
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
	/**
	 * Devuelve el tama�o de tramo
	 * @return
	 */
	public double getStretchSize() {
		try {
			return Double.parseDouble(size);
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
	/**
	 * Devuelve el minimo
	 * @return
	 */
	public double getMinimum() {
		try {
			return Double.parseDouble(min);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	/**
	 * Devuelve el maximo
	 * @return
	 */
	public double getMaximum() {
		try {
			return Double.parseDouble(max);
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
	/**
	 * Obtiene el valor m�ximo del intervalo
	 * @return 
	 */
	public String getMax() {
		return max;
	}

	/**
	 * Asigna el valor m�ximo del intervalo
	 * @param max
	 */
	public void setMax(String max) {
		this.max = max;
	}

	/**
	 * Obtiene el valor m�nimo del intervalo
	 * @return
	 */
	public String getMin() {
		return min;
	}

	/**
	 * Asigna el valor m�nimo del intervalo
	 * @param min
	 */
	public void setMin(String min) {
		this.min = min;
	}

	/**
	 * Obtiene el valor del tama�o de intervalo
	 * @return
	 */
	public String getIntervalSize() {
		return size;
	}
	
	/**
	 * Asigna el valor de tama�o de intervalo
	 * @param interval
	 */
	public void setIntervalSize(String size) {
		this.size = size;
		updateObservers();
	}
	
	/**
	 * Obtiene el n�mero de intervalos
	 * @return
	 */
	public String getNumber() {
		return number;
	}
	
	/**
	 * Asigna el n�mero de intervalos
	 * @param number
	 */
	public void setNumber(String number) {
		this.number = number;
		updateObservers();
	}
	
	/**
	 * Actualiza datos y llama al update de los observadores
	 */
	public void updateObservers() {
		setChanged();
		notifyObservers();
	}
}
