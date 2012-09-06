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


/**
 * <p>An event indicating the kind of operation occurred: undo or redo.</p>
 * 
 * @version 12/02/2008
 * @author Pablo Piqueras Bartolomé (pablo.piqueras@iver.es) 
 */
public class UndoRedoEditEvent extends java.util.EventObject {
	public static final short UNDO = 0;
	public static final short REDO = 1;

	protected transient short myType;
	protected transient String myOldText;
	protected transient String myNewText;
	
    /**
     * Constructs an UndoableEditEvent object.
     *
     * @param source  the Object that originated the event
     *                (typically <code>this</code>)
     * @param type    type of operation done <i>(undo or redo)</i>
     * @param oldText text before the operation
     * @param newText text after the operation
     */
    public UndoRedoEditEvent(Object source, short type, String oldText, String newText) {
    	super(source);
    	myType = type;
    	myOldText = oldText;
    	myNewText = newText;
    }
    
    /**
     * Returns the edit operation identifier value.
     *
     * @return the UndoableEdit object encapsulating the edit
     */
    public short getType() {
    	return myType;
    }
    
    /**
     * Returns the previous text.
     *
     * @return the previous text
     */
    public String getOldText() {
    	return myOldText;
    }
    
    /**
     * Returns the new text.
     *
     * @return the new text
     */
    public String getNewText() {
    	return myNewText;
    }

    /**
     * Returns a <code>String</code> representation of this <code>UndoRedoEditEvent</code>.
     *
     * @return a <code>String</code> representation of this <code>UndoRedoEditEvent</code>
     */
    public String toString() {
        return getClass().getName() + "[source=" + source +
        ",myType=" + myType +
    	",myOldText=" + myOldText +
    	",myNewText=" + myNewText +
        "]" ;
    }
}