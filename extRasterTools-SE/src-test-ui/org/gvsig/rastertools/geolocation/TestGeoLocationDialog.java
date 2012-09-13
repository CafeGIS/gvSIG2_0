/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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

import org.gvsig.rastertools.geolocation.ui.GeoLocationPanel;
/**
 * Test para el panel de geolocalizaci�n
 *
 * @version 30/07/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class TestGeoLocationDialog {
	private JFrame 			frame = new JFrame();

	public TestGeoLocationDialog() {
		super();
		initialize();
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.jgoodies.looks.plastic.PlasticXPLookAndFeel");
		} catch( Exception e ) {
			System.err.println( "No se puede cambiar al LookAndFeel");
		}
		new TestGeoLocationDialog();
	}

	private void initialize() {
		GeoLocationPanel glp = new GeoLocationPanel(null);
		frame.setContentPane(glp);

		frame.setSize(new java.awt.Dimension(220, 230));
		frame.setResizable(true);
		frame.setTitle("Geolocalizaci�n");
		frame.setVisible(true);
		frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
	}
}