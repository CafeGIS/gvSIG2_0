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
package org.gvsig.rastertools.clipping;

import javax.swing.JFrame;
import javax.swing.UIManager;

import org.gvsig.rastertools.clipping.ui.ClippingPanel;
/**
 * Clase para poder ver la ventana de HistrogramPanel
 *
 * @version 17/04/2007
 * @author BorSanZa - Borja S�nchez Zamorano (borja.sanchez@iver.es)
 */
public class TestClippingPanel {
	private JFrame 			frame = new JFrame();
	private ClippingPanel	hp = null;

	public TestClippingPanel() {
		super();
		initialize();
	}

	public static void main(String[] args){
		try {
			UIManager.setLookAndFeel("com.jgoodies.looks.plastic.PlasticXPLookAndFeel");
		} catch( Exception e ) {
			System.err.println( "No se puede cambiar al LookAndFeel");
		}
		new TestClippingPanel();
	}

	private void initialize() {
		frame.setSize(new java.awt.Dimension(370, 310));
		hp = new ClippingPanel(null);

	//Par�metros de inicializaci�n del histograma
		frame.setContentPane(hp);
		frame.setResizable(true);
		frame.setTitle("Panel de recorte");
		frame.setVisible(true);
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			}
		});
	}
}