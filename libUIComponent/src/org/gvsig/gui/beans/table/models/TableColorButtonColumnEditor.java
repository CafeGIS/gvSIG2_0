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

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

import org.gvsig.gui.beans.Messages;
/**
 * Editor de celda con un botón selector de color-
 *
 * @author Nacho Brodin (brodin_ign@gva.es)
 */
public class TableColorButtonColumnEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
	private static final long serialVersionUID = -6627842834708616873L;
	Color currentColor;
	JButton button;
	JColorChooser colorChooser;
	JDialog dialog;
	private DefaultTableModel tableModel = null;
	private JTable table = null;
	protected static final String EDIT = "edit";

	public TableColorButtonColumnEditor(DefaultTableModel tableModel, JTable table) {
		button = new JButton();
		button.setActionCommand(EDIT);
		button.addActionListener(this);
		button.setBorderPainted(false);
		this.tableModel = tableModel;
		this.table = table;

		colorChooser = new JColorChooser();
		dialog = JColorChooser.createDialog(button, Messages.getText("select_color"), true, colorChooser, this, null);
	}

	public void actionPerformed(ActionEvent e) {
		if (EDIT.equals(e.getActionCommand())) {
			button.setBackground(currentColor);
			colorChooser.setColor(currentColor);
			dialog.setVisible(true);
			if ((tableModel != null) && (table != null)) {
				if (!((tableModel instanceof ProfilesTableModel)||(tableModel instanceof ROIsTableModel))) {
					String newColor = currentColor.getRed() + "," + currentColor.getGreen() + "," + currentColor.getBlue();
					if (!newColor.equals(tableModel.getValueAt(table.getSelectedRow(), 2))) {
						tableModel.setValueAt(newColor, table.getSelectedRow(), 2);
					}
				}
			}
			fireEditingStopped();
		} else
			currentColor = colorChooser.getColor();
	}

	public Object getCellEditorValue() {
		return currentColor;
	}

	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		currentColor = (Color) value;
		return button;
	}
}