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
package org.gvsig.gui.beans.comboscale;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.gvsig.gui.beans.TestUI;
import org.gvsig.gui.beans.controls.comboscale.ComboScale;

public class TestComboScale {
	private ComboScale cs;

	public TestComboScale() {
		TestUI frame = new TestUI("TestComboScale");
		cs = new ComboScale();
		long[] scale = new long[5];
		scale[0] = 100;
		scale[1] = 500;
		scale[2] = 1000;
		scale[3] = 5000;
		scale[4] = 10000;
		cs.setItems(scale);
		cs.setScale(400);
		JButton btn = new JButton("Reset");
		btn.setPreferredSize(new Dimension(100, 50));
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				cs.setScale(500);
			}
		});

		JPanel panel = new JPanel();
		frame.setContentPane(panel);
		panel.setLayout(new BorderLayout());
		panel.add(cs, BorderLayout.CENTER);
		panel.add(btn, BorderLayout.EAST);

		frame.setSize(280, 80);
		frame.setVisible(true);

		cs.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				System.out.println("ActionListener: " + e.getActionCommand());
			}
		});
	}

	public static void main(String[] args) {
		new TestComboScale();
	}
}