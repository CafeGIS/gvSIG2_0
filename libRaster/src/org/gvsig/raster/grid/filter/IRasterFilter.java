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
/**
 * Este interfaz es implementado por el ancestro de cualquier filtro o el propio filtro.
 * Contiene las operaciones necesarias para su ejecución a través de la pila de filtros.
 *
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public interface IRasterFilter {
		/**
		 * Acciones a realizar antes de la ejecución del filtro
		 */
		public void pre();

		/**
		 * Ejecución de la función process de todo el filtro.
		 * @throws InterruptedException
		 *
		 */
		public void execute() throws InterruptedException;

		/**
		 * Procesa la posición x,y del raster
		 * @param x posición X
		 * @param y posición Y
		 */
		public void process(int x, int y);

		/**
		 * Acciones a realizar después de la ejecución del filtro
		 */
		public void post();

		/**
		 * Parámetros pasados al filtro en forma de nombre de parámetro y objeto que
		 * representa al parámetro. Este puede ser cualquier tipo de variable u objeto.
		 *
		 * Parámetros obligatorios:
		 *         inRaster (IRaster)
		 * parámetros obligatorios (si se da el caso)
		 *         previousFilter (IRasterFilter)
		 *
		 * @param name
		 * @param value
		 */
		public void addParam(String name, Object value);

		/**
		 * Devuelve los resultados despues de la ejecución del filtro.
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