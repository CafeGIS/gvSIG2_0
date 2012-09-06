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
package org.gvsig.gui.beans.datainput;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;

import javax.swing.JFormattedTextField;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
/**
 * Campo de texto que controla el contenido de datos del componente y solo
 * dispara un evento cuando realmente ha cambiado su valor.<p>
 * 
 * <b>RECOMENDABLE:</b> Usar JFormattedTextFields<p>
 * 
 * Ejemplo de Sun:<br>
 * &nbsp;&nbsp;<a href="http://java.sun.com/docs/books/tutorial/uiswing/examples/components/FormatterFactoryDemoProject/src/components/FormatterFactoryDemo.java">FormatterFactoryDemo.java</a>
 * 
 * @version 06/09/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class DataInputField extends Container implements PropertyChangeListener {
  private static final long serialVersionUID = 8633824284253287604L;

	private JFormattedTextField textField              = null;
	private ArrayList           actionChangedListeners = new ArrayList();

	private NumberFormat        doubleDisplayFormat;
	private NumberFormat        doubleEditFormat;
	private boolean eventsEnabled = true;


	/**
  * This is the default constructor
  */
	public DataInputField() {
		setUpFormats();
		initialize();
	}

  /**
	 * Create and set up number formats. These objects also parse numbers input by
	 * user.
	 */
  private void setUpFormats() {
      doubleDisplayFormat = NumberFormat.getNumberInstance();
      doubleDisplayFormat.setMinimumFractionDigits(0);
      doubleEditFormat = NumberFormat.getNumberInstance();
  }

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		setLayout(new BorderLayout());

		add(getJFormattedTextField(), BorderLayout.CENTER);
	}
	
	/**
	 * This method initializes jTextField
	 *
	 * @return javax.swing.JTextField
	 */
	private JFormattedTextField getJFormattedTextField() {
		if (textField == null) {
			textField = new JFormattedTextField(new DefaultFormatterFactory(
          new NumberFormatter(doubleDisplayFormat),
          new NumberFormatter(doubleDisplayFormat),
          new NumberFormatter(doubleEditFormat)));
			textField.setBackground(Color.white);
			textField.setValue(new Double(0));
			textField.setColumns(10);
			textField.addPropertyChangeListener("value", this);
		}
		return textField;
	}

	/**
	 * Devuelve el valor del campo de texto.
	 * @return
	 */
	public String getValue(){
		double value = ((Number) getJFormattedTextField().getValue()).doubleValue();

		return Double.toString(value);
	}

	/**
	 * Habilita o deshabilita el control
	 * @param en
	 */
	public void setControlEnabled(boolean en){
		getJFormattedTextField().setEnabled(en);
		if (en)
			getJFormattedTextField().setBackground(Color.white);
		else
			getJFormattedTextField().setBackground(getBackground());
	}

	/**
	 * Asigna el valor al campo de texto.
	 * @return
	 */
	public void setValue(String value) {
		eventsEnabled = false;
		getJFormattedTextField().setValue(new Double(value));
		eventsEnabled = true;
	}

	/**
	 * Añadir un listener a la lista de eventos
	 * @param listener
	 */
	public void addValueChangedListener(DataInputContainerListener listener) {
		if (!actionChangedListeners.contains(listener))
			actionChangedListeners.add(listener);
	}

	/**
	 * Añadir un listener a la lista de eventos
	 * @param listener
	 */
	public void addKeyListener(KeyListener listener) {
		textField.addKeyListener(listener);
	}
	
	/**
	 * Borrar un listener de la lista de eventos
	 * @param listener
	 */
	public void removeValueChangedListener(DataInputContainerListener listener) {
		actionChangedListeners.remove(listener);
	}
	
	/**
	 * Invocar a los eventos asociados al componente
	 */
	private void callValueChangedListeners() {
		if (eventsEnabled == false)
			return;
		Iterator acIterator = actionChangedListeners.iterator();
		while (acIterator.hasNext()) {
			DataInputContainerListener listener = (DataInputContainerListener) acIterator.next();
			listener.actionValueChanged(new EventObject(this));
		}
	}

	public void propertyChange(PropertyChangeEvent evt) {
	  callValueChangedListeners();
  }
}