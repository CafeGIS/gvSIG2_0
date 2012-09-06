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
package org.gvsig.gui.beans.editabletextcomponent;

import javax.swing.JTextField;
import javax.swing.text.Document;

import org.gvsig.gui.beans.editabletextcomponent.event.UndoRedoEditListener;

/**
 * <p>Text field with options to edit its text easily.</p>
 * 
 * @see JTextField
 * 
 * @version 03/01/2008
 * @author Pablo Piqueras Bartolomé (pablo.piqueras@iver.es) 
 */
public class JEditableTextField extends JTextField implements IEditableText {
	private static final long serialVersionUID = 5504131442718795532L;

	// REFERENCE TO THE TEXT EDITOR DECORATOR
	private EditableTextDecorator editableTextDecorator;
	// END REFERENCE TO THE TEXT EDITOR DECORATOR

	/**
	 * @see JTextField#JTextField()
	 */
	public JEditableTextField() {
		super();
		
		initialize();
	}

	/**
	 * @see JTextField#JTextField(Document, String, int)
	 */
	public JEditableTextField(Document doc, String text, int columns) {
		super(doc, text, columns);
		
		initialize();
	}

	/**
	 * @see JTextField#JTextField(int)
	 */
	public JEditableTextField(int columns) {
		super(columns);
		
		initialize();
	}

	/**
	 * @see JTextField#JTextField(String, int)
	 */
	public JEditableTextField(String text, int columns) {
		super(text, columns);
		
		initialize();
	}

	/**
	 * @see JTextField#JTextField(String)
	 */
	public JEditableTextField(String text) {
		super(text);
		
		initialize();
	}

	/**
	 * <p>Fits this component to be ready to be used.</p>
	 */
	protected void initialize() {
		editableTextDecorator = new EditableTextDecorator(this);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.editabletextcomponent.IEditableText#getUndoRedoLimitActions()
	 */
	public int getUndoRedoLimitActions() {
		return editableTextDecorator.getUndoRedoLimitActions();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.editabletextcomponent.IEditableText#setUndoRedoLimitActions(int)
	 */
	public void setUndoRedoLimitActions(int limit) {
		editableTextDecorator.setUndoRedoLimitActions(limit);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.editabletextcomponent.IEditableText#addUndoRedoEditListener(org.gvsig.gui.beans.editabletextcomponent.event.UndoRedoEditListener)
	 */
	public void addUndoRedoEditListener(UndoRedoEditListener listener) {
		editableTextDecorator.addUndoRedoEditListener(listener);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.editabletextcomponent.IEditableText#getUndoRedoEditListeners()
	 */
	public UndoRedoEditListener[] getUndoRedoEditListeners() {
		return editableTextDecorator.getUndoRedoEditListeners();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.editabletextcomponent.IEditableText#removeUndoRedoEditListener(org.gvsig.gui.beans.editabletextcomponent.event.UndoRedoEditListener)
	 */
	public void removeUndoRedoEditListener(UndoRedoEditListener listener) {
		editableTextDecorator.removeUndoRedoEditListener(listener);
	}
}
