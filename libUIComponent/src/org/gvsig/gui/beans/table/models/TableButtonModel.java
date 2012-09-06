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

import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;
/**
 * Componente tabla
 *
 * @author Nacho Brodin (brodin_ign@gva.es)
 */
public class TableButtonModel extends DefaultTableModel implements IModel {
	final private static long serialVersionUID = -3370601314380922368L;

	public TableButtonModel(String[] columnNames) {
		super(new Object[0][4], columnNames);
	}

	public Class getColumnClass(int c) {
		if ((c < 1) || (c == 4))
			return JButton.class;

		return String.class;
	}

	public void removeRow(int row) {
		super.setValueAt(null, row, 0);
		super.setValueAt("", row, 1);
		super.setValueAt("", row, 2);
		super.setValueAt("", row, 3);
		super.setValueAt(null, row, 4);
		super.removeRow(row);
	}

	public void setValueAt(Object value, int row, int col) {
		super.setValueAt(value, row, col);
	}

	public Object[] getNewLine() {
		return new Object[] { Color.WHITE, "", "", "", "255" };
	}

	public void addRow(Object[] list) {
		super.addRow(new Object[] { list[0], list[1], list[2], list[3], list[4] });
	}
}