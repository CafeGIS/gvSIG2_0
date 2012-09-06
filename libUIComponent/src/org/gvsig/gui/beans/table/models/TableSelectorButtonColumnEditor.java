/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2006 IVER T.I. and Generalitat Valenciana.
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
package org.gvsig.gui.beans.table.models;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import org.gvsig.gui.beans.Messages;

/**
 * Componente tabla
 *
 * @author Nacho Brodin (brodin_ign@gva.es)
 *
 */
public class TableSelectorButtonColumnEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
	private static final long serialVersionUID = -2028530090765546942L;
	String currentText = "255";
	JButton button;
	JValueSelector valueSelector;
	JDialog dialog;
	protected static final String EDIT = "edit";

	public TableSelectorButtonColumnEditor() {
		button = new JButton();
		button.setActionCommand(EDIT);
		button.addActionListener(this);
		button.setBorderPainted(false);

		valueSelector = new JValueSelector();
		dialog = JValueSelector.createDialog(button, Messages.getText("seleccion_alpha"), true, valueSelector, this, null, currentText);
	}

	public void actionPerformed(ActionEvent e) {
		if (EDIT.equals(e.getActionCommand())) {
			button.setText(currentText);
			((ValueSelector)dialog).setValue(Integer.valueOf(currentText).intValue());
			dialog.setVisible(true);

			fireEditingStopped();

		}else{
			currentText = String.valueOf((int)((ValueSelector)dialog).getValue());
			button.setText(currentText);
		}
	}

	public Object getCellEditorValue() {
		return currentText;
	}

	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
															 int row, int column) {
		currentText = (String)value;
		return button;
	}
}