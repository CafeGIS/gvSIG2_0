/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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
* Par�metros para los drivers de escritura. Las variables estaticas contenidas representan
* los tipos de par�metro posibles.
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
	 * Clase que representa un par�metro. Los atributos principales son:
	 * <UL>
	 * <LI>type: tipo de par�metro que tendr� un valor correspondiente a las variables estaticas de
	 * Params.</LI>
	 * <LI>id: que contiene el identificador del par�metro en cadena de texto</LI>
	 * <LI>defaultValue: que ser� el valor por defecto asignado al par�metro</LI>
	 * <LI>list: que contiene una lista de valores con datos asociados al par�metro en concreto</LI>
	 * </UL>
	 * . Por ejemplo, para una selecci�n por lista de valores tendr� la lista de valores a
	 * seleccionar. Para un slider tendr� el valor m�ximo, el m�nimo, el intervalo menor
	 * y el mayor. Para una selecci�n por check esta lista puede ser vacia.
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
	 * Obtiene el par�metro de la posici�n definida por param
	 * @param param Posici�n del par�metro
	 * @return Objeto Param
	 */
	public Param getParam(int param) {
		return (Param)params.get(param);
	}

	/**
	 * Asigna el par�metro pasado a la lista de par�metros necesitados por el driver
	 * @param param
	 */
	public void setParam(Param param) {
		params.add(param);
	}

	/**
	 * Inicializa la lista de par�metros
	 */
	public void clear() {
		params.clear();
	}

	/**
	 * Obtiene un par�metro de la lista a partir de su identificador
	 * @param id Identificador del par�metro
	 * @return Par�metro o null si no existe
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
	 * Asigna un par�metro. Si existe este lo reemplaza.
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
	 * Asigna un valor para un par�metro existens. Si no existe no hace nada.
	 *
	 * @param id Identificador del par�metro
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
	 * Obtiene el n�mero de par�metros.
	 * @return N�mero de par�metros.
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
