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
/**
 * Test para la clase Table
 * @version 30/10/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class TestTable extends TestUI {
	private static final long serialVersionUID = -1071011269441792511L;
	private TableContainer table = null;

	public TestTable() throws NotInitializeException {
		super("TestTable");
		String[] columnNames = {"columna 1", "columna 2", "columna 3", "columna 4"};
		int[] columnWidths = {70, 90, 110, 130};
		table = new TableContainer(columnNames, columnWidths);
		table.initialize();
		table.setControlVisible(true);
		table.setMoveRowsButtonsVisible(true);
		table.setEditable(true);

		String row[] = {"", "", "", ""};
		for (int i = 0; i < 10; i++) {
			row[0] = String.valueOf(i);
			row[1] = String.valueOf(i);
			row[2] = String.valueOf(i);
			row[3] = String.valueOf(i);
			table.addRow(row);
		}

		table.swapRow(5, 6);
		table.delRow(3);

		getContentPane().add(table);
		setSize(400, 300);
		setVisible(true);
	}

	public static void main(String[] args) {
		try {
			new TestTable();
		} catch (NotInitializeException ex){
			System.out.println("Tabla no inicializada");
		}
	}
}