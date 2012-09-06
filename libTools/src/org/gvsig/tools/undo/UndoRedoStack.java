/* gvSIG. Geographic Information System of the Valencian Government
*
* Copyright (C) 2007-2008 Infrastructures and Transports Department
* of the Valencian Gobernment (CIT)
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
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
* MA  02110-1301, USA.
* 
*/

/*
* AUTHORS (In addition to CIT):
* 2008 {DiSiD Technologies}  {{Task}}
*/
package org.gvsig.tools.undo;

import java.util.List;

import org.gvsig.tools.observer.WeakReferencingObservable;

/**
 * An Stack of Undo/Redo operations.
 * 
 * @author <a href="mailto:cordin@disid.com">Cèsar Ordiñana</a>
 */
public interface UndoRedoStack extends WeakReferencingObservable {

    /**
     * Undoes the following undoable elements.
     * 
     * @throws UndoException
     *             if there is an error performing de undo element
     */
    void undo() throws UndoException;

    /**
     * Undoes a number of the following undoable elements.
     * 
     * @param num
     *            the number of elements to undo
     * @throws UndoException
     *             if there is an error performing de undo element
     */
    void undo(int num) throws UndoException;

    /**
     * Re-does the following redoable elements.
     * 
     * @throws RedoException
     *             if there is an error performing de redo element
     */
    void redo() throws RedoException;

    /**
     * Re-does a number of the following redoable element.
     * 
     * @param num
     *            the number of elements to redo
     * @throws RedoException
     *             if there is an error performing de redo element
     */
    void redo(int num) throws RedoException;

    /**
     * Returns the list of UndoRedoInfo in the UNDO stack
     * 
     * @return list of UndoRedoInfo in the UNDO stack
     */
    List getUndoInfos();

    /**
     * Returns the list of UndoRedoInfo in the REDO stack
     * 
     * @return list of UndoRedoInfo in the REDO stack
     */
    List getRedoInfos();

    /**
     * If an undo can be performed on the stack.
     * 
     * @return an undo can be performed
     */
    boolean canUndo();

    /**
     * If a redo can be performed on the stack.
     * 
     * @return a redo can be performed
     */
    boolean canRedo();
}