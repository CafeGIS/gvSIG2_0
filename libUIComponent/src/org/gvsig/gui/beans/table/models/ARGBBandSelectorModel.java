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
 * Componente tabla. Selector de bandas ARGB para la visualización. Una file consta
 * de cuatro JRadioButtons para la selección de ARGB y una entrada de texto para 
 * el nombre de la banda.
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class ARGBBandSelectorModel extends DefaultTableModel implements IModel {
	final private static long serialVersionUID = -3370601314380922368L;

	public ARGBBandSelectorModel(String[] columnNames) {
		super(new Object[0][5], columnNames);
	}

	public Class getColumnClass(int c) {
		if (c < 4) {
			return Boolean.class;
		}

		return String.class;
	}

	public void setValueAt(Object value, int row, int col) {
		if ((col < 4) && ((Boolean) value).booleanValue()) {
			for (int i = 0; i < getRowCount(); i++) {
				if (i != row) {
					setValueAt(new Boolean(false), i, col);
				}
			}
		}
		super.setValueAt(value, row, col);
	}

	public void addNew() {
		super.addRow(new Object[] { new Boolean(false), new Boolean(false), new Boolean(false), new Boolean(false), "" });
	}

	public Object[] getNewLine() {
		return new Object[] { new Boolean(false), new Boolean(false), new Boolean(false), new Boolean(false), "" };
	}
}