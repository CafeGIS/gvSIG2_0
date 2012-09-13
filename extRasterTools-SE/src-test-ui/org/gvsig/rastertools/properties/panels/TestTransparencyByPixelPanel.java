/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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
package org.gvsig.rastertools.properties.panels;

import javax.swing.JFrame;
/**
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class TestTransparencyByPixelPanel {
	private static final long serialVersionUID = -5820080370596334427L;
	JFrame frame = null;
	TranspByPixelPanel tbp = null;

	public TestTransparencyByPixelPanel() {
		tbp = new TranspByPixelPanel();
		tbp.setControlEnabled(true);

		frame = new JFrame();
		frame.setSize(450, 300);
		frame.getContentPane().add(tbp);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		new TestTransparencyByPixelPanel();
	}
}