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
package org.gvsig.gui.beans.labelslidertext;

import javax.swing.JFrame;

public class TestLabelSliderTextTable {
	private int                      w      = 375;
	private int                      h      = 150;
	private JFrame                   frame  = new JFrame();
	private LabelSliderTextContainer slider = null;

	public TestLabelSliderTextTable() {
		slider = new LabelSliderTextContainer(0, 255, 255, true, "Longaniza de nombre");
		slider.setDecimal(false);
		slider.setBorder("Cabecera");
		frame.getContentPane().add(slider);
		frame.setSize(w, h);
		frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		new TestLabelSliderTextTable();
	}
}