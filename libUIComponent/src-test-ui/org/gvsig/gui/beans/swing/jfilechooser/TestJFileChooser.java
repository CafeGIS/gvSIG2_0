/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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
package org.gvsig.gui.beans.swing.jfilechooser;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;

import org.gvsig.gui.beans.TestUI;
import org.gvsig.gui.beans.swing.JFileChooser;
/**
 * Test para el componente JFileChooser. Como se puede apreciar, recuerda el
 * último directorio elegido para cada caso.
 *
 * @version 05/12/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class TestJFileChooser implements ActionListener  {

	private TestUI frame = new TestUI("TestJFileChooser");
	JComboBox comboBox = null;

	public TestJFileChooser() {

		frame.setLayout(new BorderLayout());
		frame.setResizable(false);
		comboBox = new JComboBox();
		comboBox.addItem("Tablas de color");
		comboBox.addItem("Vectorial");
		comboBox.addItem("Raster");
		comboBox.addItem("Otros");
		frame.add(comboBox, BorderLayout.CENTER);
		JButton button = new JButton("Abrir fichero");
		button.addActionListener(this);
		frame.add(button, BorderLayout.SOUTH);

		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		new TestJFileChooser();
	}

	public void actionPerformed(ActionEvent e) {
		JFileChooser chooser = new JFileChooser(comboBox.getSelectedItem().toString(), (File) null);
		chooser.showOpenDialog(frame);
	}
}