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
 */
package org.gvsig.gui.beans.table;

import java.awt.Color;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.gvsig.gui.beans.TestUI;
import org.gvsig.gui.beans.table.exceptions.NotInitializeException;

public class CheckBoxModelTable extends TestUI implements TableModelListener {
	private static final long serialVersionUID = -7971006561681605303L;
	private int            w     = 400;
	private int            h     = 200;
	private TableContainer table = null;

	public CheckBoxModelTable() throws NotInitializeException {
		super("CheckBoxModelTable");
		String[] columnNames = {" ", "Nombre", ""};
		int[] columnWidths = {22, 334, 0};
		table = new TableContainer(columnNames, columnWidths);
		table.setModel("CheckBoxModel");
		table.initialize();
		table.setControlVisible(false);
		table.setMoveRowsButtonsVisible(true);

		Object row[] = {"", "", ""};
		for (int i = 0; i < 10; i++) {
			row[0] = new Boolean(true);
			row[1] = String.valueOf(i);
			row[2] = new Integer(i);
			table.addRow(row);
		}		
		
		table.delRow(1);
		table.getTable().getJTable().getColumnModel().getColumn(2).setMinWidth(0);
		table.getTable().getJTable().getColumnModel().getColumn(2).setMaxWidth(0);
		table.getTable().getJTable().getColumnModel().getColumn(0).setMinWidth(22);
		table.getTable().getJTable().getColumnModel().getColumn(0).setMaxWidth(22);

		table.getModel().addTableModelListener(this);
		getContentPane().add(table);
		setSize(w, h);
		setVisible(true);
	}

	public static void main(String[] args) {
		try {
			new CheckBoxModelTable();
		} catch (NotInitializeException ex) {
			System.out.println("Tabla no inicializada");
		}
	}

	public void tableChanged(TableModelEvent e) {
		System.out.println("Ha cambiado");
	}
}