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

import org.gvsig.gui.beans.TestUI;
import org.gvsig.gui.beans.table.exceptions.NotInitializeException;

public class TestTreeRadioButtonModelTable {
	private int            w     = 400;
	private int            h     = 200;
	private TestUI         frame = new TestUI("TestTreeRadioButtonModelTable");
	private TableContainer table = null;

	public TestTreeRadioButtonModelTable() throws NotInitializeException {
		String[] columnNames = { "A", "R", "G", "B", "Capa" };
		int[] columnWidths = { 22, 22, 22, 22, 334 };
		table = new TableContainer(columnNames, columnWidths);
		table.setModel("ARGBBandSelectorModel");
		table.initialize();

		Object[] row = { new Boolean(false), new Boolean(true), new Boolean(false), new Boolean(false), "layer1.tif" };
		Object[] row1 = { new Boolean(false), new Boolean(false), new Boolean(true), new Boolean(false), "layer2.tif" };
		Object[] row2 = { new Boolean(false), new Boolean(false), new Boolean(false), new Boolean(true), "layer3.tif" };
		Object[] row3 = { new Boolean(true), new Boolean(false), new Boolean(false), new Boolean(false), "layer4.tif" };
		table.addRow(row);
		table.addRow(row1);
		table.addRow(row2);
		table.addRow(row3);

		frame.getContentPane().add(table);
		frame.setSize(w, h);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		try {
			new TestTreeRadioButtonModelTable();
		} catch (NotInitializeException ex) {
			System.out.println("Tabla no inicializada");
		}
	}
}