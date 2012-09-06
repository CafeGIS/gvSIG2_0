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

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Properties;

import org.gvsig.gui.beans.buttonspanel.ButtonsPanel;
import org.gvsig.gui.beans.defaultbuttonspanel.DefaultButtonsPanel;
/**
 * Panel para crear un cuadro de propiedades de configuracion standard.
 * Tiene botones de aceptar, cancelar y aplicar.
 * @version 19/04/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class PropertiesPanel extends DefaultButtonsPanel {
	private static final long serialVersionUID = 372118344763661890L;
	PropertiesComponent propertiesComponent = null;

	/**
	 * Constructor de la calse
	 */
	public PropertiesPanel() {
		super(ButtonsPanel.BUTTONS_ACCEPTCANCELAPPLY);
		propertiesComponent = new PropertiesComponent();
		initialize();
	}

	/**
	 * Constructor para poder pasarle un ArrayList de PropertyStruct
	 * @param values
	 */
	public PropertiesPanel(ArrayList values) {
		super(ButtonsPanel.BUTTONS_ACCEPTCANCELAPPLY);
		propertiesComponent = new PropertiesComponent(values);
		initialize();
	}

	/**
	 * Constructor para poder pasarle un Properties
	 * @param values
	 */
	public PropertiesPanel(Properties properties) {
		super(ButtonsPanel.BUTTONS_ACCEPTCANCELAPPLY);
		propertiesComponent = new PropertiesComponent(properties);
		initialize();
	}

	/**
	 * Añade un PropertyStruct al componente
	 * @param property
	 */
	public void addPropertyStruct(PropertyStruct property) {
		propertiesComponent.addPropertyStruct(property);
	}

	/**
	 * Creación de la ventana con sus componentes
	 */
	private void initialize() {
		this.setLayout(new BorderLayout(0, 0));
		this.add(propertiesComponent, BorderLayout.CENTER);
	}

	/**
	 * Añade una clave/valor al panel de propiedades.<br>
	 * <br>
	 * El componente seleccionado dependera del instanceof del valor y las
	 * opciones extras que se pongan. Por ejemplo: para el instanceof de un String
	 * siempre se usara un JTextField, en cambio, para un Integer, se podran usar
	 * 3 tipos, el JSlider, JComboBox y JSpinner. Estos tipos se especifican en el
	 * array extras, poniendolo siempre en la posicion 0. En la posición 1 y 2 de
	 * un JSlider se puede especificar el mínimo y el máximo del Slider.
	 *
	 * @param textLabel
	 * @param key
	 * @param value
	 * @param extras
	 */
	public void addValue(String textLabel, String key, Object value, Object[] extras) {
		propertiesComponent.addValue(textLabel, key, value, extras);
	}

	/**
	 * Añade una clave valor al panel de propiedades.
	 * @param key
	 * @param value
	 */
	public void put(Object key, Object value) {
		propertiesComponent.put(key, value);
	}

	/**
	 * Obtener todos los valores de la ventana, esto será un
	 * <code><b>ArrayList</b></code> que contendrá elementos de tipo
	 * <code><b>PropertyStruct</b></code>, pudiendo tener el valor antes de
	 * ser modificado y el nuevo valor.
	 *
	 * @see <code>PropertyStruct</code>
	 *
	 * @return ArrayList de elementos de tipo <code>PropertyStruct</code>
	 */
	public ArrayList getValues() {
		return propertiesComponent.getValues();
	}

	/**
	 * Obtener todos los valores de la ventana en formato java.util.Properties
	 * @return
	 */
	public Properties getProperties() {
		return propertiesComponent.getProperties();
	}
	
	/**
	 * Devuelve el componente del interfaz que trata esa variable, hay que tener
	 * cuidado, puede devolver null o un componente distinto al esperado si se
	 * modífica esta clase.
	 * @param name
	 * @return
	 */
	public Component getComponentUI(String name) {
		return propertiesComponent.getComponentUI(name);
	}

	/**
	 * Añadir el disparador de cuando se pulsa un botón.
	 * @param listener
	 */
	public void addStateChangedListener(PropertiesComponentListener listener) {
		propertiesComponent.addStateChangedListener(listener);
	}

	/**
	 * Borrar el disparador de eventos de los botones.
	 * @param listener
	 */
	public void removeStateChangedListener(PropertiesComponentListener listener) {
		propertiesComponent.removeStateChangedListener(listener);
	}

	/**
	 * Devuelve el PropertiesComponent que contiene este panel.
	 * @return PropertiesComponent
	 */
	public PropertiesComponent getPropertiesComponent(){
		return propertiesComponent;
	}
}