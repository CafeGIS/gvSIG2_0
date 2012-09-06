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
package org.gvsig.raster.dataset;

import java.util.ArrayList;
import java.util.Iterator;


/**
* Parámetros para los drivers de escritura. Las variables estaticas contenidas representan
* los tipos de parámetro posibles.
*
* @version 17/04/2007
* @author Nacho Brodin (nachobrodin@gmail.com)
*/
public class Params implements Cloneable {
	public static final int NONE        = -1;
	public static final int CHECK       = 1;
	public static final int CHOICE      = 2;
	public static final int SLIDER      = 3;
	public static final int MULTI_CHECK = 4;

	/**
	 * Clase que representa un parámetro. Los atributos principales son:
	 * <UL>
	 * <LI>type: tipo de parámetro que tendrá un valor correspondiente a las variables estaticas de
	 * Params.</LI>
	 * <LI>id: que contiene el identificador del parámetro en cadena de texto</LI>
	 * <LI>defaultValue: que será el valor por defecto asignado al parámetro</LI>
	 * <LI>list: que contiene una lista de valores con datos asociados al parámetro en concreto</LI>
	 * </UL>
	 * . Por ejemplo, para una selección por lista de valores tendrá la lista de valores a
	 * seleccionar. Para un slider tendrá el valor máximo, el mínimo, el intervalo menor
	 * y el mayor. Para una selección por check esta lista puede ser vacia.
	 *
	 * @version 17/04/2007
	 * @author Nacho Brodin (nachobrodin@gmail.com)
	 *
	 */
	public class Param {
		public int type = -1;
		public String id = null;
		public Object defaultValue = null;
		public String[] list = null;
	}

	private ArrayList params = new ArrayList();

	/**
	 * Obtiene el parámetro de la posición definida por param
	 * @param param Posición del parámetro
	 * @return Objeto Param
	 */
	public Param getParam(int param) {
		return (Param)params.get(param);
	}

	/**
	 * Asigna el parámetro pasado a la lista de parámetros necesitados por el driver
	 * @param param
	 */
	public void setParam(Param param) {
		params.add(param);
	}

	/**
	 * Inicializa la lista de parámetros
	 */
	public void clear() {
		params.clear();
	}

	/**
	 * Obtiene un parámetro de la lista a partir de su identificador
	 * @param id Identificador del parámetro
	 * @return Parámetro o null si no existe
	 */
	public Param getParamById(String id) {
		for (Iterator iter = params.iterator(); iter.hasNext();) {
			Param p = (Param) iter.next();
			if (p.id.equals(id))
				return p;
		}
		return null;
	}

	/**
	 * Asigna un parámetro. Si existe este lo reemplaza.
	 * @param id Identificador
	 * @param value Valor
	 * @param type Tipo
	 * @param list Lista de valores
	 */
	public void setParam(String id, Object value, int type, String[] list) {
		Param p = getParamById(id);
		if (p == null)
			p = new Param();

		p.id = id;
		p.defaultValue = value;
		p.type = type;
		p.list = list;
		params.add(p);
	}

	/**
	 * Borra los parametros asociados a ese id
	 * @param id
	 */
	public void removeParam(String id) {
		for (int i = 0; i < params.size(); i++) {
			Param p = (Param) params.get(i);
			if (p.id.equals(id)) {
				params.remove(i);
				i--;
			}
		}
	}

	/**
	 * Asigna un valor para un parámetro existens. Si no existe no hace nada.
	 *
	 * @param id Identificador del parámetro
	 * @param value Valor a asignar
	 */
	public void changeParamValue(String id, Object value) {
		for (Iterator iter = params.iterator(); iter.hasNext();) {
			Param p = (Param) iter.next();
			if(p.id.equals(id))
				p.defaultValue = value;
		}
	}

	/**
	 * Obtiene el número de parámetros.
	 * @return Número de parámetros.
	 */
	public int getNumParams() {
		return params.size();
	}

	/**
	 * Devuelve el array de los Params
	 * @return
	 */
	public ArrayList getParams() {
		return params;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone() throws CloneNotSupportedException {
		Object aux = super.clone();
		((Params) aux).params = (ArrayList) this.params.clone();
		return aux;
	}
}
