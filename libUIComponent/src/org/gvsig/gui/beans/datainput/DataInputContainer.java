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

import java.awt.ComponentOrientation;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
/**
 * Campo de texto que controla el contenido de datos del componente y solo
 * dispara un evento cuando realmente ha cambiado su valor. Contiene un
 * label para ponerle un nombre.<p>
 * 
 * <b>RECOMENDABLE:</b> Usar JFormattedTextFields<p>
 * 
 * Ejemplo de Sun:<br>
 * &nbsp;&nbsp;<a href="http://java.sun.com/docs/books/tutorial/uiswing/examples/components/FormatterFactoryDemoProject/src/components/FormatterFactoryDemo.java">FormatterFactoryDemo.java</a>
 * 
 * @version 06/09/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class DataInputContainer extends JPanel {
	private static final long serialVersionUID = 7084105134015956663L;

	private JLabel          jLabel         = null;
	private DataInputField  dataInputField = null;

	/**
	 * This is the default constructor
	 */
	public DataInputContainer() {
		initialize();
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		setLayout(new GridBagLayout());

		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.insets = new Insets(0, 0, 0, 0);
		add(getLText(), gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		add(getDataInputField(), gridBagConstraints);
	}

	private JLabel getLText() {
		if (jLabel == null) {
			jLabel = new JLabel();
			jLabel.setText("JLabel");
			jLabel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		}
		return jLabel;
	}

	/**
	 * Da nombre al campo de texto del componente.
	 * @param text
	 */
	public void setLabelText(String text){
		getLText().setText(text + ":");
	}

	/**
	 * This method initializes jTextField
	 *
	 * @return javax.swing.JTextField
	 */
	public DataInputField getDataInputField() {
		if (dataInputField == null) {
			dataInputField = new DataInputField();
		}
		return dataInputField;
	}

	/**
	 * Devuelve el valor del campo de texto.
	 * @return
	 */
	public String getValue() {
		return getDataInputField().getValue();
	}

	/**
	 * Habilita o deshabilita el control
	 * @param en
	 */
	public void setControlEnabled(boolean en){
		getLText().setEnabled(en);
		getDataInputField().setControlEnabled(en);
	}

	/**
	 * Asigna el valor al campo de texto.
	 * @return
	 */
	public void setValue(String value) {
		getDataInputField().setValue(value);
	}

	/**
	 * Añadir un listener a la lista de eventos
	 * @param listener
	 */
	public void addValueChangedListener(DataInputContainerListener listener) {
		getDataInputField().addValueChangedListener(listener);
	}
	
	/**
	 * Añadir un listener a la lista de eventos
	 * @param listener
	 */
	public void addKeyListener(KeyListener listener) {
		getDataInputField().addKeyListener(listener);
	}

	/**
	 * Borrar un listener de la lista de eventos
	 * @param listener
	 */
	public void removeValueChangedListener(DataInputContainerListener listener) {
		getDataInputField().removeValueChangedListener(listener);
	}
	
	/**
	 * Borrar un listener de la lista de eventos
	 * @param listener
	 */
	public void removeKeyListener(KeyListener listener) {
		getDataInputField().removeKeyListener(listener);
	}
}