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
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.Iterator;
import java.util.Properties;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.gvsig.gui.beans.slidertext.SliderTextContainer;
import org.gvsig.gui.beans.slidertext.listeners.SliderEvent;
import org.gvsig.gui.beans.slidertext.listeners.SliderListener;
/**
 * Componente para crear un cuadro de propiedades de configuracion standard.
 *
 * @version 19/04/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class PropertiesComponent extends JScrollPane implements FocusListener, KeyListener, ChangeListener, ItemListener, PropertiesComponentListener, SliderListener {
	private static final long serialVersionUID = 372118344763661890L;
	private ArrayList       datalist               = new ArrayList();
	private ArrayList       actionCommandListeners = new ArrayList();

	private JPanel          jPanelContent          = null;

	static final public int TYPE_DEFAULT           = 1;
	static final public int TYPE_SLIDER            = 2;
	static final public int TYPE_COMBO             = 3;

	/**
	 * Constructor de la calse
	 */
	public PropertiesComponent() {
		initialize();
	}

	/**
	 * Constructor para poder pasarle un ArrayList de PropertyStruct
	 * @param values
	 */
	public PropertiesComponent(ArrayList values) {
		initialize();
		for (int i=0; i<values.size(); i++) {
			addPropertyStruct((PropertyStruct) values.get(i));
		}
	}

	/**
	 * Constructor para poder pasarle un Properties
	 * @param values
	 */
	public PropertiesComponent(Properties properties) {
		initialize();
		Enumeration elements = properties.keys();
		while (elements.hasMoreElements()) {
			String key = (String) elements.nextElement();
			addValue(key, key, properties.get(key), null);
		}
	}

	/**
	 * Creación de la ventana con sus componentes
	 */
	private void initialize() {
		this.setBorder(null);
		jPanelContent = new JPanel();
		jPanelContent.setLayout(new GridBagLayout());
		
		JPanel jPanelPrincipal = new JPanel();
		jPanelPrincipal.setLayout(new GridBagLayout());

		JPanel jPanelEmpty = new JPanel();
		jPanelEmpty.setPreferredSize(new Dimension(0, 0));
		
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		jPanelPrincipal.add(jPanelContent, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		jPanelPrincipal.add(jPanelEmpty, gridBagConstraints);
		
		this.setViewportView(jPanelPrincipal);
	}

	int y = 0;
	/**
	 * Añade un PropertyStruct al componente
	 * @param property
	 */
	public void addPropertyStruct(PropertyStruct property) {
		boolean without_label = false;

		JLabel label = new JLabel(property.getTextLabel() + ": ");

		Component component = new JLabel("Sin soporte para: " + property.getOldValue().getClass().toString());

		if (property.getOldValue() instanceof Component) {
			component = (Component) property.getOldValue();
			without_label = true;
		}

		// Tratamiento de Strings, como un JTextField
		if (property.getOldValue() instanceof String) {
			component = new JTextField(property.getOldValue().toString());
			((JTextField) component).setMaximumSize(new Dimension(200, 25));
			((JTextField) component).addFocusListener(this);
			((JTextField) component).addKeyListener(this);
		}

		if (property.getOldValue() instanceof JPanelProperty) {
			component = (JPanelProperty) property.getOldValue();
			((JPanelProperty) component).addStateChangedListener(this);
			without_label = true;
		}

		// Tratamiento de Integer
		if (property.getOldValue() instanceof Integer || property.getOldValue() instanceof Double) {
			boolean created = false;
			if (property.getExtras() != null) {
				switch (((Integer) property.getExtras()[0]).intValue()) {
					case TYPE_SLIDER:
						without_label = true;
						component = new SliderTextContainer();
						((SliderTextContainer) component).setBorder(javax.swing.BorderFactory.createTitledBorder(property.getTextLabel()));

						if (property.getExtras().length >= 2)
							((SliderTextContainer) component).setMinimum(((Integer) property.getExtras()[1]).intValue());
						if (property.getExtras().length >= 3)
							((SliderTextContainer) component).setMaximum(((Integer) property.getExtras()[2]).intValue());
						if (property.getOldValue() instanceof Integer) {
							((SliderTextContainer) component).setDecimal(false);
							((SliderTextContainer) component).setValue(((Integer) property.getOldValue()).intValue());
						}
						if (property.getOldValue() instanceof Double) {
							((SliderTextContainer) component).setDecimal(true);
							((SliderTextContainer) component).setValue(((Double) property.getOldValue()).doubleValue());
						}

						((SliderTextContainer) component).addValueChangedListener(this);

						created = true;
						break;
					case TYPE_COMBO:
						if(property.getOldValue() instanceof Integer) {
							component = new JComboBox();
							ArrayList aux = (ArrayList) property.getExtras()[1];
							for (int i=0; i<aux.size(); i++)
								((JComboBox) component).addItem(aux.get(i).toString());
							if(property.getOldValue() instanceof Integer)
								((JComboBox) component).setSelectedIndex(((Integer) property.getOldValue()).intValue());
							((JComboBox) component).addItemListener(this);
							created = true;
						}
						break;
				}
			}
			if (!created) {
				component = new JSpinner();
				((JSpinner) component).setValue(property.getOldValue());
				((JSpinner) component).addChangeListener(this);
			}
		}

		// Tratamiento de Boolean
		if (property.getOldValue() instanceof Boolean) {
			component = new JCheckBox();
			((JCheckBox) component).setSelected(((Boolean) property.getOldValue()).booleanValue());
			((JCheckBox) component).addItemListener(this);
		}

		GridBagConstraints gridBagConstraints;
		if (without_label) {
			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridwidth = 2;
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = y;
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.insets = new Insets(2, 5, 2, 5);
			jPanelContent.add(component, gridBagConstraints);
		} else {
			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = y;
			gridBagConstraints.anchor = GridBagConstraints.EAST;
			gridBagConstraints.insets = new Insets(2, 5, 2, 2);
			jPanelContent.add(label, gridBagConstraints);

			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = y;
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.insets = new Insets(2, 2, 2, 5);
			jPanelContent.add(component, gridBagConstraints);
		}
		y++;

		label.setLabelFor(component);

		property.setJLabel(label);
		property.setComponent(component);
		datalist.add(property);
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
		PropertyStruct propertyStruct = new PropertyStruct(textLabel, key, value, extras);
		addPropertyStruct(propertyStruct);
	}

	/**
	 * Añade una clave valor al panel de propiedades.
	 * @param key
	 * @param value
	 */
	public void put(Object key, Object value) {
		PropertyStruct propertyStruct = new PropertyStruct((String) key, (String) key, value, null);
		addPropertyStruct(propertyStruct);
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
		for (int i = 0; i < datalist.size(); i++) {
			PropertyStruct propertyStruct = ((PropertyStruct) datalist.get(i));

			if (propertyStruct.getComponent() instanceof JTextField) {
				propertyStruct.setNewValue(((JTextField) propertyStruct.getComponent()).getText());
				continue;
			}
			if (propertyStruct.getComponent() instanceof JSpinner) {
				propertyStruct.setNewValue(((JSpinner) propertyStruct.getComponent()).getValue());
				continue;
			}
			if (propertyStruct.getComponent() instanceof SliderTextContainer) {
				if (propertyStruct.getOldValue() instanceof Double)
					propertyStruct.setNewValue(new Double((double) ((SliderTextContainer) propertyStruct.getComponent()).getValue()));
				else
					propertyStruct.setNewValue(new Integer((int) ((SliderTextContainer) propertyStruct.getComponent()).getValue()));
				continue;
			}
			if (propertyStruct.getComponent() instanceof JCheckBox) {
				propertyStruct.setNewValue(new Boolean(((JCheckBox) propertyStruct.getComponent()).getSelectedObjects()!=null));
				continue;
			}
			if (propertyStruct.getComponent() instanceof JComboBox) {
				propertyStruct.setNewValue(new Integer(((JComboBox) propertyStruct.getComponent()).getSelectedIndex()));
				continue;
			}
			if (propertyStruct.getComponent() instanceof JPanelProperty) {
				// No es necesario pq el mismo JPanel esta tb en el oldValue
				continue;
			}
		}
		return datalist;
	}

	/**
	 * Devuelve el componente del interfaz que trata esa variable, hay que tener
	 * cuidado, puede devolver null o un componente distinto al esperado si se
	 * modífica esta clase.
	 * @param name
	 * @return
	 */
	public Component getComponentUI(String name) {
		for (int i = 0; i < datalist.size(); i++) {
			PropertyStruct propertyStruct = ((PropertyStruct) datalist.get(i));
			String key = propertyStruct.getKey();
			if (key.equals(name))
				return propertyStruct.getComponent();
		}
		return null;
	}
	
	/**
	 * Obtener todos los valores de la ventana en formato java.util.Properties
	 * @return
	 */
	public Properties getProperties() {
		Properties properties = new Properties();
		for (int i = 0; i < datalist.size(); i++) {
			PropertyStruct propertyStruct = ((PropertyStruct) datalist.get(i));
			String key = propertyStruct.getKey();

			if (propertyStruct.getComponent() instanceof JTextField) {
				properties.put(key, ((JTextField) propertyStruct.getComponent()).getText());
				continue;
			}
			if (propertyStruct.getComponent() instanceof JSpinner) {
				properties.put(key, ((JSpinner) propertyStruct.getComponent()).getValue());
				continue;
			}
			if (propertyStruct.getComponent() instanceof SliderTextContainer) {
				if (propertyStruct.getOldValue() instanceof Double)
					properties.put(key, new Double((double) ((SliderTextContainer) propertyStruct.getComponent()).getValue()));
				else
					properties.put(key, new Integer((int) ((SliderTextContainer) propertyStruct.getComponent()).getValue()));
				continue;
			}
			if (propertyStruct.getComponent() instanceof JCheckBox) {
				properties.put(key, new Boolean(((JCheckBox) propertyStruct.getComponent()).getSelectedObjects() != null));
				continue;
			}
			if (propertyStruct.getComponent() instanceof JComboBox) {
				properties.put(key, new Integer(((JComboBox) propertyStruct.getComponent()).getSelectedIndex()));
				continue;
			}
			if (propertyStruct.getComponent() instanceof JPanelProperty) {
				properties.put(key, (JPanelProperty) propertyStruct.getComponent());
				continue;
			}
		}
		return properties;
	}

	/**
	 * Añadir el disparador de cuando se pulsa un botón.
	 * @param listener
	 */
	public void addStateChangedListener(PropertiesComponentListener listener) {
		if (!actionCommandListeners.contains(listener))
			actionCommandListeners.add(listener);
	}

	/**
	 * Borrar el disparador de eventos de los botones.
	 * @param listener
	 */
	public void removeStateChangedListener(PropertiesComponentListener listener) {
		actionCommandListeners.remove(listener);
	}

	private void callStateChanged() {
		Iterator acIterator = actionCommandListeners.iterator();
		while (acIterator.hasNext()) {
			PropertiesComponentListener listener = (PropertiesComponentListener) acIterator.next();
			listener.actionChangeProperties(new EventObject(this));
		}
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
	 */
	public void stateChanged(ChangeEvent e) {
		callStateChanged();
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
	 */
	public void itemStateChanged(ItemEvent e) {
		callStateChanged();
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
	 */
	public void focusLost(FocusEvent e) {
		callStateChanged();
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == 10)
			callStateChanged();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.propertiespanel.PropertiesComponentListener#actionChangeProperties(java.util.EventObject)
	 */
	public void actionChangeProperties(EventObject e) {
		callStateChanged();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.slidertext.listeners.SliderListener#actionValueChanged(org.gvsig.gui.beans.slidertext.listeners.SliderEvent)
	 */
	public void actionValueChanged(SliderEvent e) {
		callStateChanged();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.slidertext.listeners.SliderListener#actionValueDragged(org.gvsig.gui.beans.slidertext.listeners.SliderEvent)
	 */
	public void actionValueDragged(SliderEvent e) {
		//callStateChanged();
	}

	public void keyTyped(KeyEvent e) {}
	public void focusGained(FocusEvent e) {}
	public void keyPressed(KeyEvent e) {}
}