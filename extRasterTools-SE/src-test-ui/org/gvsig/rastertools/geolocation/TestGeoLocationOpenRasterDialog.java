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
package org.gvsig.rastertools.geolocation;

import javax.swing.JFrame;
import javax.swing.UIManager;

import org.gvsig.rastertools.geolocation.ui.GeoLocationOpeningRasterPanel;
/**
 * Test para el panel de geolocalización
 *
 * @version 30/07/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class TestGeoLocationOpenRasterDialog {
	private JFrame 			frame = new JFrame();

	public TestGeoLocationOpenRasterDialog() {
		super();
		initialize();
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.jgoodies.looks.plastic.PlasticXPLookAndFeel");
		} catch( Exception e ) {
			System.err.println( "No se puede cambiar al LookAndFeel");
		}
		new TestGeoLocationOpenRasterDialog();
	}

	private void initialize() {
		GeoLocationOpeningRasterPanel glp = new GeoLocationOpeningRasterPanel(null);
		frame.setContentPane(glp);

		frame.setSize(new java.awt.Dimension(220, 230));
		frame.setResizable(true);
		frame.setTitle("Geolocalización");
		frame.setVisible(true);
		frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
	}
}