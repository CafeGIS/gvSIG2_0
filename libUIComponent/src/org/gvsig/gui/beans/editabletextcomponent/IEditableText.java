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

import javax.swing.text.JTextComponent;

import org.gvsig.gui.beans.editabletextcomponent.event.UndoRedoEditListener;

/**
 * <p>All graphical components which inherit from {@link JTextComponent JTextComponent}
 *  can enhance their functionality with graphical edition options support implementing
 *  this interface.</p>
 * 
 * @version 03/01/2008
 * @author Pablo Piqueras Bartolomé (pablo.piqueras@iver.es) 
 */
public interface IEditableText {
	/**
     * Sets the limit of actions that can hold the UndoManager of this component.
     * 
     * @param limit sets the limit of actions that can hold
     */
	public void setUndoRedoLimitActions(int limit);

	/**
     * Gets the limit of actions that can hold the UndoManager.
     * 
     * @return int limit of actions that can hold
     */
	public int getUndoRedoLimitActions();
	
	/**
     * Adds a listener for notification of an <i>undo</i> or <i>redo</i> operation executed.
     *
     * @param listener the listener to be added
     * @see javax.swing.event.UndoRedoEditEvent
     */
    public void addUndoRedoEditListener(UndoRedoEditListener listener);

    /**
     * Removes a listener for notification of an <i>undo</i> or <i>redo</i> operation executed.
     *
     * @param listener the listener to be removed
     * @see javax.swing.event.UndoRedoEditEvent
     */
    public void removeUndoRedoEditListener(UndoRedoEditListener listener);

    /**
     * Returns an array of all the <code>UndoRedoEditListener</code>
     * registered on this text component.
     *
     * @return all of this component's <code>UndoRedoEditListener</code>s 
     *         or an empty
     *         array if no listeners of that kind are currently registered
     *
     * @see #addUndoRedoEditListener
     * @see #removeUndoRedoEditListener
     */
    public UndoRedoEditListener[] getUndoRedoEditListeners();
}
