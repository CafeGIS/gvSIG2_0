/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
 *
 * Copyright (C) 2006 IVER T.I. and Generalitat Valenciana.
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
package org.gvsig.raster.hierarchy;

import org.gvsig.raster.dataset.FileNotOpenException;
import org.gvsig.raster.dataset.RasterDriverException;



/**
 * Interfaz a implementar por las clases que ofrecen estadisticas raster.
 *  
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public interface IStatistics {
	
	/**
	 * Devuelve una lista con los m�ximos de todas las bandas
	 * @return Valor m�ximo
	 */
	public double[] getMax();
	
	/**
	 * Obtiene el valor del segundo m�ximo
	 * @return Valor del segundo m�ximo
	 */
	public double[] getSecondMax();
	
	/**
	 * Obtiene el valor del segundo m�nimo
	 * @return Valor del segundo m�nimo
	 */
	public double[] getSecondMin();
	
	/**
	 * Devuelve el m�ximo valor de todos los m�ximos de las bandas
	 * @return M�ximo
	 */
	public double getMaximun();
	
	/**
	 * Devuelve el m�nimo valor de todos los m�nimos de las bandas
	 * @return M�ximo
	 */
	public double getMinimun();

	/**
	 * Devuelve el m�ximo valor RGB de todos los m�ximos de las bandas
	 * @return M�ximo
	 */
	public double getMaximunRGB();
	
	/**
	 * Devuelve el m�nimo valor RGB de todos los m�nimos de las bandas
	 * @return M�ximo
	 */
	public double getMinimunRGB();

	/**
	 * Obtiene el valor m�dio
	 * @return Valor medio
	 */
	public double[] getMean();

	/**
	 * Devuelve una lista con los m�nimos de todas las bandas
	 * @return Valor m�nimo
	 */
	public double[] getMin();
	
	/**
	 * Obtiene el m�nimo cuando el raster es de typo byte RGB
	 * @return
	 */
	public double[] getMinRGB();
	
	/**
	 * Obtiene el m�ximo cuando el raster es de typo byte RGB
	 * @return
	 */
	public double[] getMaxRGB();
	
	/**
	 * Obtiene el segundo m�ximo cuando el raster es de typo byte RGB
	 * @return
	 */
	public double[] getSecondMaxRGB();

	/**
	 * Obtiene el segundo m�nimo cuando el raster es de typo byte RGB
	 * @return
	 */
	public double[] getSecondMinRGB();

	/**
	 * Obtiene la varianza
	 * @return Varianza
	 */
	public double[] getVariance();
	
	/**
	 * N�mero de bandas
	 * @return
	 */
	public int getBandCount();
	
	/**
	 * Asigna un valor de recorte de colas para un porcentaje dado. El valor ser� un array 
	 * bidimensional ([N�mero de bandas][2]) donde para cada banda se almacena el valor en
	 * esa posici�n del recorte de colas. Este recorte consiste en ordenar los elementos del 
	 * raster (o una muestra de ellos) y devolver el valor que corresponde al porcentaje comenzando
	 * desde el principio del array ordenado y desde el final de �l (m�nimo y m�ximo).
	 * @param percent Porcentaje de recorte
	 * @param valueByBand array bidimensional de enteros o doubles. Depende del tipo de dato del raster.
	 */
	public void setTailTrimValue(double percent, Object valueByBand);
	
	/**
	 * Obtiene un valor de recorte de colas para un porcentaje dado. El valor ser� un array 
	 * bidimensional ([N�mero de bandas][2]) donde para cada banda se almacena el valor en
	 * esa posici�n del recorte de colas. Este recorte consiste en ordenar los elementos del 
	 * raster (o una muestra de ellos) y devolver el valor que corresponde al porcentaje comenzando
	 * desde el principio del array ordenado y desde el final de �l (m�nimo y m�ximo).
	 * @param percent Porcentaje de recorte
	 * @param valueByBand array bidimensional de enteros o doubles. Depende del tipo de dato del raster.
	 */
	public Object getTailTrimValue(double percent);
	
	/**
	 * Obtiene un valor de recorte de colas para una posici�n dada. El valor ser� un array 
	 * bidimensional ([N�mero de bandas][2]) donde para cada banda se almacena el valor en
	 * esa posici�n del recorte de colas. Este recorte consiste en ordenar los elementos del 
	 * raster (o una muestra de ellos) y devolver el valor que corresponde al porcentaje comenzando
	 * desde el principio del array ordenado y desde el final de �l (m�nimo y m�ximo).
	 * @param percent Porcentaje de recorte
	 * @param valueByBand array bidimensional de enteros o doubles. Depende del tipo de dato del raster.
	 */
	public Object[] getTailTrimValue(int pos);
	
	/**
	 * Devuelve el n�mero de valores de recorte de colas calculados.
	 * @return N�mero de valores de recorte de colas calculados.
	 */
	public int getTailTrimCount();
	
	/**
	 * Calcula las estadisticas recorriendo todo el fichero.
	 */
	public void calcFullStatistics()throws FileNotOpenException, RasterDriverException, InterruptedException;
	
	/**
	 * Obtiene el flag que informa de si las estad�sticas est�n calculadas o no.
	 * @return true indica que est�n calculadas y false que no lo est�n
	 */
	public boolean isCalculated();
}
