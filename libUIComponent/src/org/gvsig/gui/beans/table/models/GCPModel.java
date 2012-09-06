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

import javax.swing.table.DefaultTableModel;
/**
 * Modelo correspondiente a una tabla de puntos de control
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class GCPModel extends DefaultTableModel implements IModel {
	final private static long serialVersionUID = -3370601314380922368L;
	private int               nColumns         = 0;

	public GCPModel(String[] columnNames) {
		super(new Object[0][columnNames.length], columnNames);
		this.nColumns = columnNames.length;
	}

	public Class getColumnClass(int c) {
		return String.class;
	}

	public void setValueAt(Object value, int row, int col) {
		super.setValueAt(value, row, col);
	}

	public void addNew() {
		Object[] line = new Object[nColumns];
		line[0] = new Boolean(true);
		for (int i = 0; i < nColumns; i++)
			line[i] = new String("");
		super.addRow(line);
	}

	public Object[] getNewLine() {
		Object[] o = new Object[nColumns];
		o[0] = new Boolean(true);
		for (int i = 1; i < nColumns; i++)
			o[i] = "";
		return o;
	}
}