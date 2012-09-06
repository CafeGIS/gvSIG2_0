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
package org.gvsig.raster.grid.filter;

import java.util.ArrayList;

import org.gvsig.raster.dataset.Params;


/**
 * Interfaz que deben implementar los Gestores de pila de filtros
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public interface IRasterFilterListManager {

		/**
		 * Convierte cada filtro o elemento de un filtro de la pila en un String de la forma
		 * elemento=valor. Esta versión la implementan las extensiones que registren filtros
		 * de forma externa para que pueda ser ejecutado por el RasterFilterListManager.
		 * @param filterList Lista de cadenas de la forma variable=valor a la que se añadiran las
		 * cadenas que generen el gestor de filtros actual.
		 * @param rf Filtro analizado. Cada gestor de filtros comprobará si el filtro pasado
		 * es gestionado por el y si lo hace introduce las cadenas correspondientes.
		 * @return ArrayList donde cada elemento es una cadena
		 */
		public ArrayList getStringsFromFilterList(ArrayList filterList, RasterFilter rf);

		/**
		 * Crea una pila de filtros a partir de un Array de Strings. Cada elemento del array debe
		 * tener la forma elemento=valor. Esta versión la implementan las extensiones que registren filtros
		 * de forma externa para que pueda ser ejecutado por el RasterFilterListManager.
		 * @param filters	Lista de filtros
		 * @param fil Cadena que representa el identificador del elemento
		 * @param filteri Número de filtro de la lista analizado
		 * @return filteri modificado
		 */
		public int createFilterListFromStrings(ArrayList filters, String fil, int filteri) throws FilterTypeException;

		/**
		 * Devuelve la lista de filtros que puede manejar un RasterFilterListManager
		 * @return
		 */
		public ArrayList getRasterFilterList();

		/**
		 * Añade un filtro
		 * @param classFilter Clase del filtro a añadir
		 * @param params Parámetros de carga
		 */
		public void addFilter(Class classFilter, Params params) throws FilterTypeException;
}