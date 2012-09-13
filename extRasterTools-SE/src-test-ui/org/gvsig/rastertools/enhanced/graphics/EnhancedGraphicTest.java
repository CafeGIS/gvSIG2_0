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
package org.gvsig.rastertools.enhanced.graphics;

import java.awt.Dimension;

import org.gvsig.rastertools.TestUI;
import org.gvsig.rastertools.enhanced.graphics.panels.EnhancedGraphic;

/**
 * 
 *
 * 14-oct-2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class EnhancedGraphicTest {
	private TestUI            jFrame = null;
	private EnhancedGraphic   component = null;

	public EnhancedGraphicTest() {
		initialize();
	}

	private void initialize() {
		jFrame = new TestUI("EnhancedGraphicTest");
		
		component = new EnhancedGraphic();	
		jFrame.setSize(new Dimension(200, 300));
		jFrame.setContentPane(component);

		jFrame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		jFrame.setVisible(true);
	}
	
	public static void main(String[] args) {
		new EnhancedGraphicTest();
	}

}