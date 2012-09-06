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
package org.gvsig.raster.util;

import java.util.ArrayList;

/**
 * Mantiene la gestión de historicos. Se trata de una lista de objetos la cual nos informa
 * del actualmente vigente y nos devuelve el siguiente si existe o el anterior.
 *  
 * @version 31/07/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class Historical {
	private ArrayList             objectList = null;
	private int                   position = -1;
	
	/**
	 * Crea un nuevo historico
	 *
	 */
	public Historical() {
		objectList = new ArrayList();
		position = -1;
	}
	
	/**
	 * Obtiene el siguiente elemento.
	 * @return Siguiente elemento o null si no existe o la lista está vacia
	 */
	public Object getNext() {
		if(position < 0)
			return null;
		if(position == (objectList.size() - 1))
			return null;
		return objectList.get(++position);
	}
	
	/**
	 * Añade una elemento a la lista
	 * @param obj
	 */
	public void add(Object obj) {
		objectList.add(++position, obj);
	}
	
	/**
	 * Obtiene el elemento anterior.
	 * @return Anterior elemento o null si no existe o la lista está vacia
	 */
	public Object getBack() {
		if(position < 0)
			return null;
		if(position == 0)
			return null;
		position --;
		return objectList.get(position);
	}
	
	/**
	 * Consulta si existe un elemento anterior al actualmente vigente
	 * @return true si existe y false si no existe
	 */
	public boolean existBack() {
		if(position == -1 || position == 0)
			return false;
		return true;		
	}
	
	/**
	 * Consulta si existe un elemento posterior al actualmente vigente
	 * @return true si existe y false si no existe
	 */
	public boolean existNext() {
		if(position == -1 || position == (objectList.size() - 1))
			return false;
		return true;		
	}
	
	/**
	 * Limpia el historico
	 */
	public void clear() {
		objectList.clear();
		position = -1;
	}
	
	/**
	 * Obtiene el número de elementos del historico
	 * @return Número de elementos del historico
	 */
	public int getElementsCount() {
		return objectList.size();
	}
	
	/**
	 * Obtiene el primer elemento del historico
	 * @return Object
	 */
	public Object getFirst() {
		if(objectList.size() == 0 || position == -1)
			return null;
		position = 0;
		return objectList.get(0);
	}
	
	/**
	 * Obtiene el último elemento del historico
	 * @return Object
	 */
	public Object getLast() {
		if(objectList.size() == 0 || position == -1)
			return null;
		position = (objectList.size() - 1);
		return objectList.get(position);
	}
}
