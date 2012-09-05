
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
package org.gvsig.catalog.utils.xmltreetable;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.EventObject;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;
import javax.swing.table.TableCellEditor;
import javax.swing.tree.TreeCellEditor;

/**
 * DOCUMENT ME!
 * 
 * 
 * @author $author$
 */
public class XMLTreeTableCellEditor implements Serializable, TableCellEditor, TreeCellEditor {
/**
 * 
 * 
 */
    protected EventListenerList listenerList = new EventListenerList();
/**
 * 
 * 
 */
    protected transient ChangeEvent changeEvent = null;
/**
 * 
 * 
 */
    protected JComponent editorComponent = null;
/**
 * 
 * 
 */
    protected JComponent container = null;
// Can be tree or table

/**
 * DOCUMENT ME!
 * 
 * 
 * @return DOCUMENT ME!
 */
    public Component getComponent() {        
        return editorComponent;
    } 

/**
 * DOCUMENT ME!
 * 
 * 
 * @return DOCUMENT ME!
 */
    public Object getCellEditorValue() {        
        return editorComponent;
    } 

/**
 * DOCUMENT ME!
 * 
 * 
 * @return DOCUMENT ME!
 * @param anEvent DOCUMENT ME!
 */
    public boolean isCellEditable(EventObject anEvent) {        
        return true;
    } 

/**
 * DOCUMENT ME!
 * 
 * 
 * @return DOCUMENT ME!
 * @param anEvent DOCUMENT ME!
 */
    public boolean shouldSelectCell(EventObject anEvent) {        
        if ((editorComponent != null) && anEvent instanceof MouseEvent &&
                (((MouseEvent) anEvent).getID() == MouseEvent.MOUSE_PRESSED)) {
            Component dispatchComponent = SwingUtilities.getDeepestComponentAt(editorComponent,
                    3, 3);
            MouseEvent e = (MouseEvent) anEvent;
            MouseEvent e2 = new MouseEvent(dispatchComponent,
                    MouseEvent.MOUSE_RELEASED, e.getWhen() + 100000,
                    e.getModifiers(), 3, 3, e.getClickCount(),
                    e.isPopupTrigger());
            dispatchComponent.dispatchEvent(e2);
            e2 = new MouseEvent(dispatchComponent, MouseEvent.MOUSE_CLICKED,
                    e.getWhen() + 100001, e.getModifiers(), 3, 3, 1,
                    e.isPopupTrigger());
            dispatchComponent.dispatchEvent(e2);
        }
        return false;
    } 

/**
 * DOCUMENT ME!
 * 
 * 
 * @return DOCUMENT ME!
 */
    public boolean stopCellEditing() {        
        fireEditingStopped();
        return true;
    } 

/**
 * DOCUMENT ME!
 * 
 */
    public void cancelCellEditing() {        
        fireEditingCanceled();
    } 

/**
 * DOCUMENT ME!
 * 
 * 
 * @param l DOCUMENT ME!
 */
    public void addCellEditorListener(CellEditorListener l) {        
        listenerList.add(CellEditorListener.class, l);
    } 

/**
 * DOCUMENT ME!
 * 
 * 
 * @param l DOCUMENT ME!
 */
    public void removeCellEditorListener(CellEditorListener l) {        
        listenerList.remove(CellEditorListener.class, l);
    } 

/**
 * DOCUMENT ME!
 * 
 */
    protected void fireEditingStopped() {        
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == CellEditorListener.class) {
                // Lazily create the event:
                if (changeEvent == null) {
                    changeEvent = new ChangeEvent(this);
                }
                ((CellEditorListener) listeners[i + 1]).editingStopped(changeEvent);
            }
        }
    } 

/**
 * DOCUMENT ME!
 * 
 */
    protected void fireEditingCanceled() {        
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == CellEditorListener.class) {
                // Lazily create the event:
                if (changeEvent == null) {
                    changeEvent = new ChangeEvent(this);
                }
                ((CellEditorListener) listeners[i + 1]).editingCanceled(changeEvent);
            }
        }
    } 
// implements javax.swing.tree.TreeCellEditor

/**
 * 
 * 
 * 
 * @return 
 * @param tree 
 * @param value 
 * @param isSelected 
 * @param expanded 
 * @param leaf 
 * @param row 
 */
    public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {        
        tree.convertValueToText(value, isSelected,
                expanded, leaf, row, false);
        editorComponent = (JComponent) value;
        container = tree;
        return editorComponent;
    } 
// implements javax.swing.table.TableCellEditor

/**
 * 
 * 
 * 
 * @return 
 * @param table 
 * @param value 
 * @param isSelected 
 * @param row 
 * @param column 
 */
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {        
        editorComponent = (JComponent) value;
        container = table;
        return editorComponent;
    } 
 }
// End of class JComponentCellEditor
