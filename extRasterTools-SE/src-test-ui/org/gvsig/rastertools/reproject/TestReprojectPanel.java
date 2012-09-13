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
package org.gvsig.rastertools.reproject;

import org.gvsig.rastertools.BaseTest;
import org.gvsig.rastertools.TestUI;
import org.gvsig.rastertools.reproject.ui.LayerReprojectPanel;
/**
 * Testeo del panel de reproyeccion
 * 
 * @version 21/05/2008
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class TestReprojectPanel extends BaseTest {
	
	public TestReprojectPanel() {
		openLayer(byteImg);
		TestUI frame = new TestUI();
		LayerReprojectPanel panel = new LayerReprojectPanel(lyr, Boolean.FALSE);
		frame.setWindowInfo(panel.getWindowInfo());
		frame.setContentPane(panel);
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		new TestReprojectPanel();
	}
}