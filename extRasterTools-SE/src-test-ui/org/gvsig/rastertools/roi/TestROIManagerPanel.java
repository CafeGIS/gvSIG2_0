/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
 *
 * Copyright (C) 2007 Instituto de Desarrollo Regional and Generalitat Valenciana.
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
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ib��ez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   Instituto de Desarrollo Regional (Universidad de Castilla La-Mancha)
 *   Campus Universitario s/n
 *   02071 Alabacete
 *   Spain
 *
 *   +34 967 599 200
 */

package org.gvsig.rastertools.roi;

import javax.swing.JFrame;
import javax.swing.UIManager;

import org.gvsig.rastertools.roi.ui.ROIsManagerPanel;

/**
 * Test para el panel de gesti�n de ROIs.
 *  
 * @author Diego Guerrero Sevilla (diego.guerrero@uclm.es)
 *
 */
public class TestROIManagerPanel {
	
	private JFrame 			jFrame = new JFrame();
	private ROIsManagerPanel roiManagerPanel = null;
	
	public static void main(String[] args){
		try {
			UIManager.setLookAndFeel("com.jgoodies.looks.plastic.PlasticXPLookAndFeel");
		} catch( Exception e ) {
			System.err.println( "No se puede cambiar al LookAndFeel");
		}
		new TestROIManagerPanel();
	}
	
	public TestROIManagerPanel() {
		super();
		initialize();
	}
	
	private void initialize() {
		jFrame.setSize(new java.awt.Dimension(640,480));
		roiManagerPanel = new ROIsManagerPanel(null);
		jFrame.setContentPane(roiManagerPanel);
		jFrame.setResizable(true);
		jFrame.setTitle("Gestor de ROIs");
		jFrame.setVisible(true);
		jFrame.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			}
		});
	}

}
