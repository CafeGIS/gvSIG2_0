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
package org.gvsig.rastertools.generictoolbar;

import org.gvsig.rastertools.GenericToolBarModule;
import org.gvsig.rastertools.RasterModule;
import org.gvsig.rastertools.TestUI;
/**
 * Clase para poder ver la ventana de Filtros
 *
 * @version 17/04/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class TestGenericToolBarPanel {
	private TestUI 			jFrame = new TestUI("TestRasterFilterPanel");
	private GenericToolBarPanel	genericToolBarPanel = null;

	public TestGenericToolBarPanel() {
		super();
		RasterModule rasterModule = new RasterModule();
		rasterModule.initialize();
		GenericToolBarModule toolBarModule = new GenericToolBarModule();
		toolBarModule.initialize();
		initialize();
	}

	public static void main(String[] args){
		new TestGenericToolBarPanel();
	}

	private void initialize() {
		jFrame.setSize(new java.awt.Dimension(645, 480));
		genericToolBarPanel = new GenericToolBarPanel();
		jFrame.setContentPane(genericToolBarPanel);
		jFrame.setResizable(true);
		jFrame.setTitle("TestRasterFilterPanel");
		jFrame.setVisible(true);
	}
}