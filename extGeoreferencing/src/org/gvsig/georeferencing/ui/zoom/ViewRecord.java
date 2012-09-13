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
package org.gvsig.georeferencing.ui.zoom;

import java.util.ArrayList;

/**
 * Historial de peticiones para poder recuperar las anteriores a 
 * la actual.
 * 
 * 11/01/2008
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class ViewRecord {

	private ArrayList  record = new ArrayList();
	private int        pos = -1;

	/**
	 * Asigna la petición siguiente
	 * @param request
	 */
	public void setRequest(Object request) {
		record.add(++ pos, request);
	}
	
	/**
	 * Obtiene la petición anterior
	 * @return
	 */
	public Object getRequest() {
		if(pos > 0) {
			pos --;
			return record.get(pos);
		} else if(pos == 0)
			return record.get(pos);
		return null;
	}
	
	/**
	 * Vacia el historial
	 */
	public void clear() {
		record.clear();
	}
}
