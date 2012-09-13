/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
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
package org.gvsig.raster.beans.canvas.layers;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Modelo de datos para la capa de selección de tramos. Representa
 * un array de datos Double donde cada valor está entre 0 y 1. Un valor
 * del array representa la posición de ese tramo de forma porcentual. El 
 * primer elemento será 0 y el último 1. El resto de valores se reparten 
 * dentro del rango ordenados de menor a mayor. 
 * 
 * 07/08/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class StretchLayerDataModel extends ArrayList {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Obtiene la lista de valores en forma de array de double
	 * @return double[]
	 */
	public double[] getValues() {
		double[] list = new double[size()];
		for (int i = 0; i < size(); i++) { 
			try {
				list[i] = ((Double)get(i)).doubleValue();
			} catch(NumberFormatException e) {
				//Continua con los siguientes valores
			}
		}
		return list;
	}
	
	/**
	 * Genera n tramos equidistantes 
	 * @param n Número de intervalos a crear
	 */
	public void generate(int n) {
		if(n <= 0)
			return;
		double distance = 1D / n;
		clear();
		for (int i = 0; i <= n; i++) 
			add(new Double(i * distance));
	}
	
	/**
	 * Genera los tramos a la distancia especificada. El último tramo será de tamaño 
	 * menor en caso de no ser divisibles
	 * @param distance. Distancia entre tramos
	 * @parma min. Valor mínimo
	 * @param max. Valor máximo
	 */
	public void generate(double distance, double min, double max) {
		if(distance <= 0 || min > max)
			return;
		double step = distance / (max - min);
		clear();
		add(new Double(0));
		double value = step;
		while(value < 1) {
			add(new Double(value));
			value += step;
		}
		if((value - step) < 1)
			add(new Double(1));
	}
	
	/**
	 * Ordena los elementos del array
	 */
	public void sort() {
		double[] list =  new double[size()];
		for (int i = 0; i < size(); i++) 
			list[i] = ((Double)get(i)).doubleValue();
		Arrays.sort(list);
		clear();
		for (int i = 0; i < list.length; i++)
			add(new Double(list[i]));
	}
}
