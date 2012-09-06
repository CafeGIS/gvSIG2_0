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

package org.gvsig.gui.beans.editabletextcomponent;

import javax.swing.JTextArea;
import javax.swing.text.Document;

import org.gvsig.gui.beans.editabletextcomponent.event.UndoRedoEditListener;

/**
 * <p>Text area with options to edit its text easily.</p>
 * 
 * @see JTextArea
 * 
 * @version 03/01/2008
 * @author Pablo Piqueras Bartolomé (pablo.piqueras@iver.es) 
 */
public class JEditableTextArea extends JTextArea implements IEditableText {
	private static final long serialVersionUID = -8094155271679157582L;

	// REFERENCE TO THE TEXT EDITOR DECORATOR
	private EditableTextDecorator editableTextDecorator;
	// END REFERENCE TO THE TEXT EDITOR DECORATOR
	
	/**
	 * @see JEditableTextArea#JEditableTextArea()
	 */
	public JEditableTextArea() {
		super();
		
		initialize();
	}

	/**
	 * @see JEditableTextArea#JEditableTextArea(Document, String, int, int)
	 */
	public JEditableTextArea(Document doc, String text, int rows, int columns) {
		super(doc, text, rows, columns);
		
		initialize();
	}

	/**
	 * @see JEditableTextArea#JEditableTextArea(Document)
	 */
	public JEditableTextArea(Document doc) {
		super(doc);
		
		initialize();
	}

	/**
	 * @see JEditableTextArea#JEditableTextArea(int, int)
	 */
	public JEditableTextArea(int rows, int columns) {
		super(rows, columns);
		
		initialize();
	}

	/**
	 * @see JEditableTextArea#JEditableTextArea(String, int, int)
	 */
	public JEditableTextArea(String text, int rows, int columns) {
		super(text, rows, columns);
		
		initialize();
	}

	/**
	 * @see JEditableTextArea#JEditableTextArea(String)
	 */
	public JEditableTextArea(String text) {
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
