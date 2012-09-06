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
package org.gvsig.raster.grid.filter;
/**
 * Este interfaz es implementado por el ancestro de cualquier filtro o el propio filtro.
 * Contiene las operaciones necesarias para su ejecuci�n a trav�s de la pila de filtros.
 *
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public interface IRasterFilter {
		/**
		 * Acciones a realizar antes de la ejecuci�n del filtro
		 */
		public void pre();

		/**
		 * Ejecuci�n de la funci�n process de todo el filtro.
		 * @throws InterruptedException
		 *
		 */
		public void execute() throws InterruptedException;

		/**
		 * Procesa la posici�n x,y del raster
		 * @param x posici�n X
		 * @param y posici�n Y
		 */
		public void process(int x, int y);

		/**
		 * Acciones a realizar despu�s de la ejecuci�n del filtro
		 */
		public void post();

		/**
		 * Par�metros pasados al filtro en forma de nombre de par�metro y objeto que
		 * representa al par�metro. Este puede ser cualquier tipo de variable u objeto.
		 *
		 * Par�metros obligatorios:
		 *         inRaster (IRaster)
		 * par�metros obligatorios (si se da el caso)
		 *         previousFilter (IRasterFilter)
		 *
		 * @param name
		 * @param value
		 */
		public void addParam(String name, Object value);

		/**
		 * Devuelve los resultados despues de la ejecuci�n del filtro.
		 * @param name
		 * @return
		 */
		public Object getResult(String name);

		/**
		 * Obtiene el tipo de datos de entrada al filtro
		 * @return Tipo de dato
		 */
		public int getInRasterDataType();

		/**
		 * Obtiene el tipo de datos de salida del filtro
		 * @return Tipo de dato
		 */
		public int getOutRasterDataType();

		/**
		 * Obtiene el grupo del filtro
		 * @return
		 */
		public String getGroup();
}