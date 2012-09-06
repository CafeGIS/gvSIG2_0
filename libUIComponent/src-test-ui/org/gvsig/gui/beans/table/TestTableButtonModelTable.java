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

public class TestTableButtonModelTable implements ActionListener {
	private int            w     = 460;
	private int            h     = 200;
	private TestUI         frame = new TestUI("TestTableButtonModelTable");
	private TableContainer table = null;

	public TestTableButtonModelTable() throws NotInitializeException {
		String[] columnNames = { "Botón", "columna2", "columna3", "columna 4", "col5" };
		int[] columnWidths = { 54, 114, 94, 114, 60 };
		table = new TableContainer(columnNames, columnWidths);
		table.setModel("TableButtonModel");
		table.initialize();

		Object[] row = { Color.GREEN, "texto 0", "texto 0", "texto 0", "125" };
		Object[] row1 = { Color.BLUE, "texto 1", "texto 1", "texto 1", "255" };
		Object[] row2 = { Color.RED, "texto 2", "texto 2", "texto 2", "80" };
		table.addRow(row);

		/*JButton b7 = new JButton();
		b7.setBackground(Color.RED);
		b7.addActionListener(this);
		((TableButtonModel)table.getModel()).setValueAt(b7, 0, 0);*/
		table.addRow(row1);
		table.addRow(row2);

		frame.getContentPane().add(table);
		frame.setSize(w, h);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		try {
			new TestTableButtonModelTable();
		} catch(NotInitializeException ex){
			System.out.println("Tabla no inicializada");
		}
	}

	public void actionPerformed(ActionEvent e) {
		System.out.println("Evento de botón!!");
	}
}