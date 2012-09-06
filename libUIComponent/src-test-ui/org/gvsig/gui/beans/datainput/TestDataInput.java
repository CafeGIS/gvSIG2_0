/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2007 IVER T.I. and Generalitat Valenciana.
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
package org.gvsig.gui.beans.datainput;

import java.awt.GridLayout;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class TestDataInput implements DataInputContainerListener {
	private DataInputContainer data  = null;
	private DataInputContainer data2 = null;

	public TestDataInput(){
		data = new DataInputContainer();
		data.setLabelText("Valor 1");
		data.addValueChangedListener(this);

		data2 = new DataInputContainer();
		data2.setLabelText("Valor 2");

		JPanel panel = new JPanel(new GridLayout(2, 1, 2, 2));
		panel.add(data);
		panel.add(data2);
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		JFrame frame = new JFrame("DataInputContainerDemo");
		frame.add(panel);
		frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		new TestDataInput();
	}

	public void actionValueChanged(EventObject e) {
	  System.out.println(data.getValue());
  }
}