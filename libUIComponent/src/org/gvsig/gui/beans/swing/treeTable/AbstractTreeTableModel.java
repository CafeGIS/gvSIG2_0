package org.gvsig.gui.beans.swing.treeTable;

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreePath;

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
 * $Id: AbstractTreeTableModel.java 13136 2007-08-20 08:38:34Z evercher $
 * $Log$
 * Revision 1.1  2007-08-20 08:34:46  evercher
 * He fusionado LibUI con LibUIComponents
 *
 * Revision 1.3  2006/11/01 17:27:44  jorpiell
 * Se elimina el nodo "virtual" de la raiz
 *
 * Revision 1.2  2006/10/23 11:13:55  jorpiell
 * Añadida la posibilidad de recargar el árbol tantas veces como queramos
 *
 * Revision 1.1  2006/10/23 08:18:39  jorpiell
 * Añadida la clase treetable
 *
 *
 */
/**
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 */
public abstract class AbstractTreeTableModel implements TreeTableModel {
	protected Object root;     
	protected EventListenerList listenerList = new EventListenerList();
	
	public AbstractTreeTableModel(Object root) {
		setNodes(root);
	}
	
	public void setNodes(Object root){
		this.root = root; 		
	}
	
	//
	// Default implmentations for methods in the TreeModel interface. 
	//
	
	public Object getRoot() {
		return root;
	}
	
	public boolean isLeaf(Object node) {
		return getChildCount(node) == 0; 
	}
	
	public void valueForPathChanged(TreePath path, Object newValue) {}
	
	// This is not called in the JTree's default mode: use a naive implementation. 
	public int getIndexOfChild(Object parent, Object child) {
		for (int i = 0; i < getChildCount(parent); i++) {
			if (getChild(parent, i).equals(child)) { 
				return i; 
			}
		}
		return -1; 
	}
	
	public void addTreeModelListener(TreeModelListener l) {
		listenerList.add(TreeModelListener.class, l);
	}
	
	public void removeTreeModelListener(TreeModelListener l) {
		listenerList.remove(TreeModelListener.class, l);
	}
	
	/*
	 * Notify all listeners that have registered interest for
	 * notification on this event type.  The event instance 
	 * is lazily created using the parameters passed into 
	 * the fire method.
	 * @see EventListenerList
	 */
	protected void fireTreeNodesChanged(Object source, Object[] path, 
			int[] childIndices, 
			Object[] children) {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		TreeModelEvent e = null;
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length-2; i>=0; i-=2) {
			if (listeners[i]==TreeModelListener.class) {
				// Lazily create the event:
				if (e == null)
					e = new TreeModelEvent(source, path, 
							childIndices, children);
				((TreeModelListener)listeners[i+1]).treeNodesChanged(e);
			}          
		}
	}
	
	/*
	 * Notify all listeners that have registered interest for
	 * notification on this event type.  The event instance 
	 * is lazily created using the parameters passed into 
	 * the fire method.
	 * @see EventListenerList
	 */
	protected void fireTreeNodesInserted(Object source, Object[] path, 
			int[] childIndices, 
			Object[] children) {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		TreeModelEvent e = null;
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length-2; i>=0; i-=2) {
			if (listeners[i]==TreeModelListener.class) {
				// Lazily create the event:
				if (e == null)
					e = new TreeModelEvent(source, path, 
							childIndices, children);
				((TreeModelListener)listeners[i+1]).treeNodesInserted(e);
			}          
		}
	}
	
	/*
	 * Notify all listeners that have registered interest for
	 * notification on this event type.  The event instance 
	 * is lazily created using the parameters passed into 
	 * the fire method.
	 * @see EventListenerList
	 */
	protected void fireTreeNodesRemoved(Object source, Object[] path, 
			int[] childIndices, 
			Object[] children) {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		TreeModelEvent e = null;
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length-2; i>=0; i-=2) {
			if (listeners[i]==TreeModelListener.class) {
				// Lazily create the event:
				if (e == null)
					e = new TreeModelEvent(source, path, 
							childIndices, children);
				((TreeModelListener)listeners[i+1]).treeNodesRemoved(e);
			}          
		}
	}
	
	/*
	 * Notify all listeners that have registered interest for
	 * notification on this event type.  The event instance 
	 * is lazily created using the parameters passed into 
	 * the fire method.
	 * @see EventListenerList
	 */
	protected void fireTreeStructureChanged(Object source, Object[] path, 
			int[] childIndices, 
			Object[] children) {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		TreeModelEvent e = null;
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length-2; i>=0; i-=2) {
			if (listeners[i]==TreeModelListener.class) {
				// Lazily create the event:
				if (e == null)
					e = new TreeModelEvent(source, path, 
							childIndices, children);
				((TreeModelListener)listeners[i+1]).treeStructureChanged(e);
			}          
		}
	}
	
	//
	// Default impelmentations for methods in the TreeTableModel interface. 
	//
	
	public Class getColumnClass(int column) { return Object.class; }
	
	/** By default, make the column with the Tree in it the only editable one. 
	 *  Making this column editable causes the JTable to forward mouse 
	 *  and keyboard events in the Tree column to the underlying JTree. 
	 */ 
	public boolean isCellEditable(Object node, int column) { 
		return getColumnClass(column) == TreeTableModel.class; 
	}
	
	public void setValueAt(Object aValue, Object node, int column) {}
	}

