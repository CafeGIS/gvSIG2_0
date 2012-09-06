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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.gvsig.gui.beans.TestUI;
import org.gvsig.gui.beans.table.exceptions.NotInitializeException;
/**
 * Test del TableContainer
 *
 * @version 27/05/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class TestTableColorTable implements ActionListener {
	private int            w     = 460;
	private int            h     = 200;
	private TestUI         frame = new TestUI("TestTableColorTable");
	private TableContainer table = null;

	public TestTableColorTable() throws NotInitializeException {
		String[] columnNames = { "selec", "clase", "RGB", "Valor", "Max", "Alpha" };
		int[] columnWidths = { 54, 1104, 94, 114, 114, 60 };
		table = new TableContainer(columnNames, columnWidths);
		table.setModel("TableColorModel");
		table.initialize();

		Object[] row = { Color.GREEN, "", "0,255,0", new Double(2), new Double(2), "255" };
		Object[] row1 = { Color.BLUE, "", "0,0,255", new Double(1), new Double(2), "255" };
		Object[] row2 = { Color.RED , "", "255,0,0", new Double(0), new Double(1), "255" };
		table.addRow(row);

		table.addRow(row1);
		table.addRow(row2);

		frame.getContentPane().add(table);
		frame.setSize(w, h);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		try {
			new TestTableColorTable();
		} catch(NotInitializeException ex){
			System.out.println("Tabla no inicializada");
		}
	}

	public void actionPerformed(ActionEvent e) {
		System.out.println("Evento de botón!!");
	}
}