package org.gvsig.gui.beans.swing.treeTable;

import java.util.EventObject;

import javax.swing.CellEditor;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;


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
/* CVS MESSAGES:
 *
 * $Id: AbstractCellEditor.java 13136 2007-08-20 08:38:34Z evercher $
 * $Log$
 * Revision 1.1  2007-08-20 08:34:46  evercher
 * He fusionado LibUI con LibUIComponents
 *
 * Revision 1.1  2006/10/23 08:18:39  jorpiell
 * Añadida la clase treetable
 *
 *
 */
/**
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 */
public class AbstractCellEditor implements CellEditor {
	
	protected EventListenerList listenerList = new EventListenerList();
	
	public Object getCellEditorValue() { return null; }
	public boolean isCellEditable(EventObject e) { return true; }
	public boolean shouldSelectCell(EventObject anEvent) { return false; }
	public boolean stopCellEditing() { return true; }
	public void cancelCellEditing() {}
	
	public void addCellEditorListener(CellEditorListener l) {
		listenerList.add(CellEditorListener.class, l);
	}
	
	public void removeCellEditorListener(CellEditorListener l) {
		listenerList.remove(CellEditorListener.class, l);
	}
	
	/**
	 * Notify all listeners that have registered interest for
	 * notification on this event type.  
	 * @see EventListenerList
	 */
	protected void fireEditingStopped() {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length-2; i>=0; i-=2) {
			if (listeners[i]==CellEditorListener.class) {
				((CellEditorListener)listeners[i+1]).editingStopped(new ChangeEvent(this));
			}	       
		}
	}
	
	/**
	 * Notify all listeners that have registered interest for
	 * notification on this event type.  
	 * @see EventListenerList
	 */
	protected void fireEditingCanceled() {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length-2; i>=0; i-=2) {
			if (listeners[i]==CellEditorListener.class) {
				((CellEditorListener)listeners[i+1]).editingCanceled(new ChangeEvent(this));
			}	       
		}
	}
}

