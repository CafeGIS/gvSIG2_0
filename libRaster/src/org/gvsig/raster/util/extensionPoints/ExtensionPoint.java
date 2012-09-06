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
package org.gvsig.raster.util.extensionPoints;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
/**
 * ExtensionPoint es una clase en la que se pueden registrar elementos asociados a un identificador.
 * <br><br>
 * Esta clase no posee constructores. La única forma de poder obtener una instancia a dicha clase
 * es usando el método <code>getExtensionPoint(String)</code>
 * 
 * @version 21/07/2008
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class ExtensionPoint {
	static private TreeMap map = new TreeMap();
	private LinkedHashMap point = new LinkedHashMap();

	private String name;
	private String description;

	/**
	 * Construye un punto de extension.
	 * <br>
	 * @param extensionPointName Nombre del punto de extension.
	 */
	private ExtensionPoint(String extensionPointName) {
		this.name = extensionPointName;
	}
	
	/**
	 * Construye un punto de extension.
	 * <br>
	 * @param extensionPointName Nombre del punto de extension
	 * @param description Descripcion del punto de extension
	 */
	private ExtensionPoint(String extensionPointName, String description) {
		this.name = extensionPointName;
		this.description = description;
	}

	/**
	 * Devuelve un punto de extensión asociado al identificador pasado por parametro.
	 * En caso de no existir dicho punto de extensión, lo crea.
	 * @param extensionPointName
	 * @return
	 */
	public static ExtensionPoint getExtensionPoint(String extensionPointName) {
		ExtensionPoint extensionPoint = (ExtensionPoint) map.get(extensionPointName);
		if (extensionPoint == null) {
			extensionPoint = new ExtensionPoint(extensionPointName);
			map.put(extensionPoint.getName(), extensionPoint);
		}
		return extensionPoint;
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
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Devuelve el nombre del punto de extensión actual
	 * @return
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
	public Iterator getIterator() {
		return point.entrySet().iterator();
	}

	/**
	 * Devuelve el número de elementos que hay registrados en el punto de extensión
	 * @return
	 */
	public int size() {
		return point.size();
	}
	
	/**
	 * Devuelve <tt>true</tt> si el punto de extensión contiene un key en su lista de elementos
	 * @param key
	 * @return
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
	 * @return
	 */
	public String[] getKeys() {
		String[] list = new String[size()];
		Iterator iterator = getIterator();
		int i = 0;
		while (iterator.hasNext()) {
			list[i] = (String) ((Map.Entry) iterator.next()).getKey();
			i++;
		}
		return list;
	}
}
