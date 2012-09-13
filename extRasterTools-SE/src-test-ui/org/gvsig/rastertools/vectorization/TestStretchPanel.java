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
package org.gvsig.rastertools.vectorization;

import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.UIManager;

import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.rastertools.vectorizacion.stretch.StretchData;
import org.gvsig.rastertools.vectorizacion.stretch.StretchListener;
import org.gvsig.rastertools.vectorizacion.stretch.ui.StretchPanel;

/**
 * Test para el panel de selección de tramos en la vectorización
 * 08/08/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class TestStretchPanel {
	private int 				w = 550, h = 500;
	private JFrame 				frame = new JFrame();
	private FLyrRasterSE        lyr = null;
	
	public TestStretchPanel() {
		StretchData data = new StretchData();
		StretchPanel panel = new StretchPanel();
		new StretchListener(lyr, panel, data);
		
		frame.getContentPane().add(panel);
		frame.setSize(w, h);
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		Toolkit.getDefaultToolkit().setDynamicLayout(true);
		try {
	    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
    }
		new TestStretchPanel();
	}
}
