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
package org.gvsig.gui.beans.propertiespanel;

import java.awt.Component;

import javax.swing.JLabel;
/**
 * Clase con todos los datos posibles que puede tener una clave/valor
 * especificada.
 *
 * @version 23/04/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class PropertyStruct {
	/**
	 * Representa al contenido que tendra el label
	 */
	private String textLabel = "";

	/**
	 * Clave del elemento
	 */
	private String key = "";

	/**
	 * Valor inicial del elemento
	 */
	private Object oldValue = null;

	/**
	 * Valor después de haberlo modificado en la ventana
	 */
	private Object newValue = null;

	/**
	 * Lista de opciones extras que determina como sera un
	 * <code>Component</code>
	 */
	private Object[] extras = null;

	/**
	 * <code>JLabel</code> que representa al componente
	 */
	private JLabel jLabel = null;

	/**
	 * Componente en si donde se modificará el valor
	 */
	private Component component = null;

	/**
	 * Crea un PropertyStruct vacio
	 */
	public PropertyStruct() {
	}

	/**
	 * Crea un PropertyStruct;
	 * @param textLabel
	 * @param key
	 * @param value
	 * @param extras
	 */
	public PropertyStruct(String textLabel, String key, Object value, Object[] extras) {
		setTextLabel(textLabel);
		setKey(key);
		setOldValue(value);
		setNewValue(value);
		setExtras(extras);
	}

	/**
	 * @return the component
	 */
	public Component getComponent() {
		return component;
	}

	/**
	 * @param component the component to set
	 */
	public void setComponent(Component component) {
		this.component = component;
	}

	/**
	 * @return the extras
	 */
	public Object[] getExtras() {
		return extras;
	}

	/**
	 * @param extras the extras to set
	 */
	public void setExtras(Object[] extras) {
		this.extras = extras;
	}

	/**
	 * @return the jLabel
	 */
	public JLabel getJLabel() {
		return jLabel;
	}

	/**
	 * @param label the jLabel to set
	 */
	public void setJLabel(JLabel label) {
		jLabel = label;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the newValue
	 */
	public Object getNewValue() {
		return newValue;
	}

	/**
	 * @param newValue the newValue to set
	 */
	public void setNewValue(Object newValue) {
		this.newValue = newValue;
	}

	/**
	 * @return the oldValue
	 */
	public Object getOldValue() {
		return oldValue;
	}

	/**
	 * @param oldValue the oldValue to set
	 */
	public void setOldValue(Object oldValue) {
		this.oldValue = oldValue;
	}

	/**
	 * @return the textLabel
	 */
	public String getTextLabel() {
		return textLabel;
	}

	/**
	 * @param textLabel the textLabel to set
	 */
	public void setTextLabel(String textLabel) {
		this.textLabel = textLabel;
	}
}