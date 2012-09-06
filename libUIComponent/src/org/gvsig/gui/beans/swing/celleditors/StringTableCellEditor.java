/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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
package org.gvsig.gui.beans.swing.celleditors;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.EventObject;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;

import org.gvsig.gui.beans.swing.cellrenderers.StringTableCellRenderer;

public class StringTableCellEditor implements TableCellEditor {
	private StringTableCellRenderer renderer;
	private ArrayList listeners = new ArrayList();


	public StringTableCellEditor(final JTable ownerTable) {
		renderer = new StringTableCellRenderer();
	}

	public Component getTableCellEditorComponent(final JTable table, Object value, boolean isSelected, int row, int column) {
		if (value == null) return null;
		JComponent aux = (JComponent) renderer.getTableCellRendererComponent(table, value, isSelected, false, row, column);
		((JTextField) aux).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Store the value to avoid the need of pressing ENTER
				// http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4139078
				table.editingStopped(new ChangeEvent(table));
			}
		});
		return aux;
	}


	public void cancelCellEditing() {
		for (int i = 0; i < listeners.size(); i++) {
	        CellEditorListener l = (CellEditorListener) listeners.get(i);
	        ChangeEvent evt = new ChangeEvent(this);
	        l.editingCanceled(evt);
	    }
	}

	public boolean stopCellEditing() {
		for (int i = 0; i < listeners.size(); i++) {
            CellEditorListener l = (CellEditorListener) listeners.get(i);
            ChangeEvent evt = new ChangeEvent(this);
            l.editingStopped(evt);
        }
        return true;
	}


	public Object getCellEditorValue() {
		if (renderer!=null && renderer.getText()!=null)
			return new Boolean(renderer.getText());
		return null;
	}

	public boolean isCellEditable(EventObject anEvent) {
		return true;
	}

	public boolean shouldSelectCell(EventObject anEvent) {
		return true;
	}

	public void addCellEditorListener(CellEditorListener l) {
		listeners.add(l);
	}

	public void removeCellEditorListener(CellEditorListener l) {
		listeners.remove(l);
	}

}
