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
package org.gvsig.gui.beans.coorddatainput;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.gvsig.gui.beans.TestUI;
import org.gvsig.gui.beans.coordinatespanel.CoordinatesPanel;
/**
 *
 * @version 06/09/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class TestCoordDataInput extends TestUI {

	public TestCoordDataInput() {
		super("TestCoordDataInput");

		CoordinatesPanel coord = new CoordinatesPanel();
		coord.setTitlePanel("Coordenadas pixel");
		coord.setEnabled(true);

		CoordinatesPanel coord2 = new CoordinatesPanel();
		coord2.setTitlePanel("Coordenadas reales");
		coord2.setEnabled(true);

		JPanel panel = new JPanel(new GridLayout(2, 1, 2, 2));
		panel.add(coord);
		panel.add(coord2);

		JTextField field = new JTextField();
		panel.add(field);

		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		add(panel);
		pack();
		setVisible(true);
	}

	public static void main(String[] args) {
		new TestCoordDataInput();
	}
}