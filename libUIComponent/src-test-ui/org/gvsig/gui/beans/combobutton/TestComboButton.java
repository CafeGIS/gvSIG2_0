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
package org.gvsig.gui.beans.combobutton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import org.gvsig.gui.beans.TestUI;
import org.gvsig.gui.beans.controls.combobutton.ComboButton;

public class TestComboButton implements ActionListener {
	
	public TestComboButton() {
		TestUI frame = new TestUI("TestComboButton");
		ComboButton cb = new ComboButton();
		ImageIcon icon1 = new ImageIcon(getClass().getResource("images/backward.png"));
		ImageIcon icon2 = new ImageIcon(getClass().getResource("images/forward.png"));
		JButton b1 = new JButton("Action 1", icon1);
		b1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Pulsando el botón 1");
				System.out.println("Action command: " + arg0.getActionCommand());
			}
		});
		b1.setActionCommand("action1");
		JButton b2 = new JButton("Action 2", icon2);
		b2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Pulsando el botón 2");
				System.out.println("Action command: " + arg0.getActionCommand());
			}
		});
		b2.setActionCommand("action2");
		cb.setName("Prueba");
		cb.addButton(b1);
		cb.addSeparator();
		cb.addButton(b2);
		cb.addActionListener(this);
		frame.getContentPane().add(cb);
		frame.setSize(80, 80);
		frame.setVisible(true);
	}
	

	public static void main(String[] args) {
		new TestComboButton();
	}


	public void actionPerformed(ActionEvent e) {
		System.out.println("actionPerformed() : " + e.getActionCommand());
	}
}