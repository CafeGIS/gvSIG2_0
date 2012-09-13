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

 /**
  * @author Ángel Fraile Griñán  e-mail: angel.fraile@iver.es
  * @since 1.1
  * 
  */
package com.iver.ai2.animationgui.gui.document;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.TreeMap;
import java.util.Map.Entry;


public class ExtensionPoint3D {

	static private TreeMap<String, ExtensionPoint3D> map = new TreeMap<String, ExtensionPoint3D>();
	private LinkedHashMap<String, Object> point = new LinkedHashMap<String, Object>();

	private String name;
	private String description;

	/**
	 * Construye un punto de extension.
	 * @param extensionPointName Nombre del punto de extension.
	 */
	private ExtensionPoint3D(String extensionPointName) {
		this.name = extensionPointName;
	}
	
	/**
	 * Construye un punto de extensión.
	 * @param extensionPointName: Nombre del punto de extensión
	 * @param description: Descripción del punto de extensión
	 */
	private ExtensionPoint3D(String extensionPointName, String description) {
		this.name = extensionPointName;
		this.description = description;
	}

	/**
	 * Devuelve un punto de extensión asociado al identificador pasado por parametro.
	 * En caso de no existir dicho punto de extensión, lo crea.
	 * @param extensionPointName: Nombre del punto de extension
	 * @return extensionPoint
	 */
	public static ExtensionPoint3D getExtensionPoint(String extensionPointName) {
		ExtensionPoint3D extensionPoint = (ExtensionPoint3D) map.get(extensionPointName);
		if (extensionPoint == null) {
			extensionPoint = new ExtensionPoint3D(extensionPointName);
			map.put(extensionPoint.getName(), extensionPoint);
		}
		return extensionPoint;
	}
	
	/**
	 * Borra un punto de extensión asociado al identificador pasado por parametro.
	 * @param extensionPointName Nombre del punto de extension
	 */
	
	public static void removeExtensionPoint(String extensionPointName) {
			map.remove(extensionPointName);
	}

	
	/**
	 * Establece la descripción del punto de extensión actual.
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Devuelve la descripción del punto de extensión actual
	 * @return String: descripción
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Devuelve el nombre del punto de extensión actual
	 * @return String: nombre
	 */
	public String getName() {
		return name;
	}

	/**
	 * Registra un elemento en el punto de extensión actual asociándolo con un key identificador.
	 * @param key
	 * @param value
	 */
	public void register(String key, Object value) {
		point.put(key, value);
	}

	/**
	 * Devuelve un iterador para poder recorrer todos los elementos del punto de extensión.
	 * @return
	 */
	public static Iterator<?> getExtensionIterator() {
		return map.entrySet().iterator();
	}
	
	/**
	 * Devuelve un iterador para poder recorrer todos los elementos del punto de extensión.
	 * @return
	 */
	public Iterator<?> getIterator() {
		return point.entrySet().iterator();
	}

	/**
	 * Devuelve el número de elementos que hay registrados en el punto de extensión
	 * @return int
	 */
	public int size() {
		return point.size();
	}
	
	/**
	 * Devuelve <tt>true</tt> si el punto de extensión contiene un key en su lista de elementos
	 * @param key: clave de búsqueda
	 * @return boolean
	 */
	public boolean existKey(String key) {
		if (point.get(key) == null)
			return false;
		return true;
	}

	/**
	 * Devuelve el elemento asociado al key pasado por parametro. En caso de no existir, devolverá
	 * <tt>null</tt>.
	 * @param key
	 * @return
	 */
	public Object getValue(String key) {
		return point.get(key);
	}
	
	/**
	 * Devuelve la lista de keys para el punto de extensión actual.
	 * @param key
	 * @return list
	 */
	public String[] getKeys() {
		String[] list = new String[size()];
		Iterator<?> iterator = getIterator();
		int i = 0;
		while (iterator.hasNext()) {
			list[i] = (String) ((Entry<?, ?>) iterator.next()).getKey();
			i++;
		}
		return list;
	}
}
