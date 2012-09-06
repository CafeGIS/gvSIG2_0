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
package org.gvsig.raster.grid.filter;

/**
 * Excepción lanzada por un filtro o proceso. Cuando es lanzada significa que los
 * tipos de datos entre los filtros de la pila son incompatibles.
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class FilterTypeException extends Exception {
	private static final long serialVersionUID = 5187136969233163574L;

	/**
	 * Constructor. Asigna el texto de la excepción
	 * @param msg
	 */
	public FilterTypeException(String msg){
		super(msg);
	}
	
	/**
	 * Constructor. Asigna el texto de la excepción y la excepción generica.
	 * @param msg
	 */
	public FilterTypeException(String msg, Exception e){
		super(msg, e);
	}
}