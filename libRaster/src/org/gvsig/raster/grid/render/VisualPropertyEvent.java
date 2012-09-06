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
package org.gvsig.raster.grid.render;

import java.util.EventObject;

/**
 * Evento de una propiedad cuando esta cambia
 * 
 * 07/01/2008
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class VisualPropertyEvent extends EventObject {
	private static final long serialVersionUID = -1649548367781607532L;
	private String name = null;
	private Object value = null;

	/**
	 * Constructor. Se asignan los valores de nombre y valor de la 
	 * propiedad que ha disparado el evento.
	 * @param source Objeto fuente
	 * @param name Nombre de la propiedad
	 * @param value Valor de la propiedad
	 */
	public VisualPropertyEvent(Object source) {
		super(source);
	}

	/**
	 * Obtiene el nombre de la propiedad que disparó el evento
	 * @return cadena con el nombre de la propiedad que disparó el evento
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Obtiene el valor de la propiedad antes de que se modificara.
	 * @return Object con el valor de la propiedad que disparó el evento
	 */
	public Object getValue() {
		return value;
	}
}
