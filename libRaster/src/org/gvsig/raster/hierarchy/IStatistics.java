/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
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
	 * Devuelve una lista con los máximos de todas las bandas
	 * @return Valor máximo
	 */
	public double[] getMax();
	
	/**
	 * Obtiene el valor del segundo máximo
	 * @return Valor del segundo máximo
	 */
	public double[] getSecondMax();
	
	/**
	 * Obtiene el valor del segundo mínimo
	 * @return Valor del segundo mínimo
	 */
	public double[] getSecondMin();
	
	/**
	 * Devuelve el máximo valor de todos los máximos de las bandas
	 * @return Máximo
	 */
	public double getMaximun();
	
	/**
	 * Devuelve el mínimo valor de todos los mínimos de las bandas
	 * @return Máximo
	 */
	public double getMinimun();

	/**
	 * Devuelve el máximo valor RGB de todos los máximos de las bandas
	 * @return Máximo
	 */
	public double getMaximunRGB();
	
	/**
	 * Devuelve el mínimo valor RGB de todos los mínimos de las bandas
	 * @return Máximo
	 */
	public double getMinimunRGB();

	/**
	 * Obtiene el valor médio
	 * @return Valor medio
	 */
	public double[] getMean();

	/**
	 * Devuelve una lista con los mínimos de todas las bandas
	 * @return Valor mínimo
	 */
	public double[] getMin();
	
	/**
	 * Obtiene el mínimo cuando el raster es de typo byte RGB
	 * @return
	 */
	public double[] getMinRGB();
	
	/**
	 * Obtiene el máximo cuando el raster es de typo byte RGB
	 * @return
	 */
	public double[] getMaxRGB();
	
	/**
	 * Obtiene el segundo máximo cuando el raster es de typo byte RGB
	 * @return
	 */
	public double[] getSecondMaxRGB();

	/**
	 * Obtiene el segundo mínimo cuando el raster es de typo byte RGB
	 * @return
	 */
	public double[] getSecondMinRGB();

	/**
	 * Obtiene la varianza
	 * @return Varianza
	 */
	public double[] getVariance();
	
	/**
	 * Número de bandas
	 * @return
	 */
	public int getBandCount();
	
	/**
	 * Asigna un valor de recorte de colas para un porcentaje dado. El valor será un array 
	 * bidimensional ([Número de bandas][2]) donde para cada banda se almacena el valor en
	 * esa posición del recorte de colas. Este recorte consiste en ordenar los elementos del 
	 * raster (o una muestra de ellos) y devolver el valor que corresponde al porcentaje comenzando
	 * desde el principio del array ordenado y desde el final de él (mínimo y máximo).
	 * @param percent Porcentaje de recorte
	 * @param valueByBand array bidimensional de enteros o doubles. Depende del tipo de dato del raster.
	 */
	public void setTailTrimValue(double percent, Object valueByBand);
	
	/**
	 * Obtiene un valor de recorte de colas para un porcentaje dado. El valor será un array 
	 * bidimensional ([Número de bandas][2]) donde para cada banda se almacena el valor en
	 * esa posición del recorte de colas. Este recorte consiste en ordenar los elementos del 
	 * raster (o una muestra de ellos) y devolver el valor que corresponde al porcentaje comenzando
	 * desde el principio del array ordenado y desde el final de él (mínimo y máximo).
	 * @param percent Porcentaje de recorte
	 * @param valueByBand array bidimensional de enteros o doubles. Depende del tipo de dato del raster.
	 */
	public Object getTailTrimValue(double percent);
	
	/**
	 * Obtiene un valor de recorte de colas para una posición dada. El valor será un array 
	 * bidimensional ([Número de bandas][2]) donde para cada banda se almacena el valor en
	 * esa posición del recorte de colas. Este recorte consiste en ordenar los elementos del 
	 * raster (o una muestra de ellos) y devolver el valor que corresponde al porcentaje comenzando
	 * desde el principio del array ordenado y desde el final de él (mínimo y máximo).
	 * @param percent Porcentaje de recorte
	 * @param valueByBand array bidimensional de enteros o doubles. Depende del tipo de dato del raster.
	 */
	public Object[] getTailTrimValue(int pos);
	
	/**
	 * Devuelve el número de valores de recorte de colas calculados.
	 * @return Número de valores de recorte de colas calculados.
	 */
	public int getTailTrimCount();
	
	/**
	 * Calcula las estadisticas recorriendo todo el fichero.
	 */
	public void calcFullStatistics()throws FileNotOpenException, RasterDriverException, InterruptedException;
	
	/**
	 * Obtiene el flag que informa de si las estadísticas están calculadas o no.
	 * @return true indica que están calculadas y false que no lo están
	 */
	public boolean isCalculated();
}
