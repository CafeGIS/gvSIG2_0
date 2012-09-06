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

package org.gvsig.gui.beans.editabletextcomponent.event;

import java.util.EventListener;

/**
 * <p>The listener interface for receiving events from undo or redo operations on a text edition.</p>
 * 
 * <p>The listener object created from that class is then registered with a
 * component using the component's <code>addUndoRedoEditListener</code> 
 * method. An event of this kind of operations is generated when an <code>EditableTextDecorator</code>
 * executes an <code>undo</code> or <code>redo</code> operation. The relevant method in the listener 
 * object is then invoked, and the <code>UndoRedoEditEvent</code> is passed to it.</p>
 * 
 * @see UndoRedoEditEvent
 * 
 * @version 12/02/2008
 * @author Pablo Piqueras Bartolomé (pablo.piqueras@iver.es) 
 */
public interface UndoRedoEditListener extends EventListener {
    /**
     * Invoked when an <i>undo</i> or <i>redo</i> operation has been executed.
     * See the class description for {@link UndoRedoEditEvent UndoRedoEditEvent} for a notification of 
     * an <i>undo</i> or <i>redo</i> operation executed on an editable text component.
     */
    public void operationExecuted(UndoRedoEditEvent e);
}
