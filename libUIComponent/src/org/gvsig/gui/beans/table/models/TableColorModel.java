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
 * @version 27/06/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class TableColorModel extends DefaultTableModel implements IModel {
	private static final long serialVersionUID = 5126848457976272945L;

	boolean[] canEdit = new boolean[] { true, true, false, true, false, true };
	Class[]   types   = new Class[] { JButton.class, String.class, String.class, Double.class, Double.class, JButton.class };

	public TableColorModel(String[] columnNames) {
		super(new Object[0][6], columnNames);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.table.models.IModel#getNewLine()
	 */
	public Object[] getNewLine() {
		Double valor = new Double(0.0f);
		Color color = Color.WHITE;
		if (this.getRowCount() > 0) {
			valor = (Double) getValueAt(this.getRowCount() - 1, 3);
			color = (Color) getValueAt(this.getRowCount() - 1, 0);
		}
		return new Object[] {color, "", "", valor, null, "255"};
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
	 */
	public Class getColumnClass(int columnIndex) {
		return types [columnIndex];
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.table.DefaultTableModel#isCellEditable(int, int)
	 */
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return canEdit [columnIndex];
	}
}