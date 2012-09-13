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
package org.gvsig.rastertools.colortable;

import java.awt.Dimension;

import org.gvsig.rastertools.TestUI;
import org.gvsig.rastertools.colortable.ui.ColorTablePanel;
/**
 * Clase para poder ver la ventana de las Tablas de color
 *
 * @version 17/04/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class TestColorTablePanel {
	private TestUI frame = new TestUI("TestColorTablePanel");

	public TestColorTablePanel() {
		super();
		initialize();
	}

	public static void main(String[] args){
		new TestColorTablePanel();
	}

	private void initialize() {
		ColorTablePanel	ctp = new ColorTablePanel(null, null);
		frame.setContentPane(ctp);
		frame.setSize(new Dimension(660, 450));
		frame.setResizable(true);
		frame.setTitle("Tablas de color");
		frame.setVisible(true);
	}
}