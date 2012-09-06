/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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
package org.gvsig.raster.datastruct;

import java.util.ArrayList;
/**
 * La clase GeoPointList es un contenedor de GeoPoints.
 * 
 * Internamente se guardan en un ArrayList. Así que el interfaz que se ofrece
 * hacia afuera es el de un ArrayList, con la excepcion de que en vez de tratar
 * con Objects, se trata con GeoPoints.
 *  
 * @version 17/07/2008
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class GeoPointList {

	private ArrayList list = null;
	
	/**
	 * Construye una lista vacía de GeoPoints
	 */
	public GeoPointList() {
		list = new ArrayList();
	}

	/**
	 * @param arg0
	 * @return
	 * @see java.util.ArrayList#add(java.lang.Object)
	 */
	public boolean add(GeoPoint arg0) {
		return list.add(arg0);
	}

	/**
	 * @param index
	 * @return
	 * @see java.util.ArrayList#get(int)
	 */
	public GeoPoint get(int index) {
		return (GeoPoint) list.get(index);
	}

	/**
	 * @return
	 * @see java.util.ArrayList#size()
	 */
	public int size() {
		return list.size();
	}
}
