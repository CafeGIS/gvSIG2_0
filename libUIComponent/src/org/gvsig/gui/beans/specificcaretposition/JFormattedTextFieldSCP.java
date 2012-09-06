package org.gvsig.gui.beans.specificcaretposition;

import java.io.Serializable;
import java.text.Format;

import javax.swing.JFormattedTextField;

import org.gvsig.gui.awt.event.specificCaretPosition.ISpecificCaretPosition;
import org.gvsig.gui.awt.event.specificCaretPosition.SpecificCaretPositionListeners;


/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */

/**
 * This class is a JFormattedTextField with two listeners that allow authomatically reset the caret
 *   position of this component to left (LEFT_POSITIONS), right (RIGHT_POSITIONS) or like JFormattedTextField (LIKE_JTEXTCOMPONENT) (do nothing).
 *   By default is to left. 
 * 
 * @author Pablo Piqueras Bartolomé (pablo.piqueras@iver.es)
 */
public class JFormattedTextFieldSCP extends JFormattedTextField implements ISpecificCaretPosition, Serializable {
	private static final long serialVersionUID = -8224060521711689694L;

	private int caretPositionMode;

	/**
	 * @see JFormattedTextField#JFormattedTextField()
	 */
	public JFormattedTextFieldSCP() {
		super();
		caretPositionMode = LEFT_POSITIONS;
		initialize();
	}
	
	/**
	 * @see JFormattedTextField#JFormattedTextField(javax.swing.JFormattedTextField.AbstractFormatter)
	 */
	public JFormattedTextFieldSCP(JFormattedTextField.AbstractFormatter formatter) {
		super(formatter);
		caretPositionMode = LEFT_POSITIONS;
		initialize();
	}
	
	/**
	 * @see JFormattedTextField#JFormattedTextField(javax.swing.JFormattedTextField.AbstractFormatterFactory)
	 */
	public JFormattedTextFieldSCP(JFormattedTextField.AbstractFormatterFactory factory) {
		super(factory);
		caretPositionMode = LEFT_POSITIONS;
		initialize();
	}
	
	/**
	 * @see JFormattedTextField#JFormattedTextField(javax.swing.JFormattedTextField.AbstractFormatterFactory, java.lang.Object)
	 */
	public JFormattedTextFieldSCP(JFormattedTextField.AbstractFormatterFactory factory, Object currentValue) {
		super(factory, currentValue);
		caretPositionMode = LEFT_POSITIONS;
		initialize();
	}
	
	/**
	 * @see JFormattedTextField#JFormattedTextField()
	 */
	public JFormattedTextFieldSCP(Format format) {
		super(format);
		caretPositionMode = LEFT_POSITIONS;
		initialize();
	}
	
	/**
	 * @see JFormattedTextField#JFormattedTextField(java.lang.Object)
	 */
	public JFormattedTextFieldSCP(Object value) {
		super(value);
		caretPositionMode = LEFT_POSITIONS;
		initialize();
	}
	
	/**
	 * This method adds three listeners to this component:
	 */
	private void initialize() {
		SpecificCaretPositionListeners.setListeners(this);
	}

	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.gui.awt.event.ISpecificCaretPosition#getCaretPositionMode()
	 */
	public int getCaretPositionMode() {
		return caretPositionMode;
	}

	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.gui.awt.event.ISpecificCaretPosition#setCaretPositionMode(int)
	 */
	public void setCaretPositionMode(int caretPositionMode) {
		this.caretPositionMode = caretPositionMode;
	}
}