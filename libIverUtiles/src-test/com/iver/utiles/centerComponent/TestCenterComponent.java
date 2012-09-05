package com.iver.utiles.centerComponent;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;


/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */

/**
 * Tests the 'centerJComponent' method of the CenterJComponent class
 * 
 * @author Pablo Piqueras Bartolomé (p_queras@hotmail.com)
 */
public class TestCenterComponent {
	private static JFrame jFrame;
	private static JDesktopPane jDesktopPane;
	private static int i;
	
	/**
	 * Test method for the CenterJComponent
	 * 
	 * @param args
	 */
	public static void main (String args[])
	{		
		try {
			// Creates the main JFrame and configures it for test. Adds a JDesktopPane			
			jFrame = new JFrame("JFrame: Tests 1, 2 y 3");

			jDesktopPane = new JDesktopPane();
			jDesktopPane.setPreferredSize(new Dimension(300, 300));
			jDesktopPane.setSize(new Dimension(300, 300));			
			jDesktopPane.addMouseListener(new MouseAdapter() {

				/*
				 *  (non-Javadoc)
				 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
				 */
				public void mouseClicked(MouseEvent e) {
					createComponentsForTest();				
				}
			});
			
			jFrame.getContentPane().add(jDesktopPane);
			jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			jFrame.pack();
			jFrame.setVisible(true);
			
			// By default: centers the JFrame in the screen
			Point screenTopLeftCorner = new Point(0, 0);
			Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
			Rectangle screenRectangle = new Rectangle(screenTopLeftCorner, screenDimension);
			CenterComponent.centerComponent(jFrame, screenRectangle);
//			jFrame.setLocationRelativeTo(null);
			
			i = 0;
			createComponentsForTest();
		}
		catch(Exception e) {
			e.printStackTrace();
		}	
		
	}
	
	/**
	 * Creates some objects for test
	 */
	private static void createComponentsForTest() {
		
		// Creates a JInternalFrame with a JPanel, and adds it
		JInternalFrame jInternalFrame1 = new JInternalFrame("JInternalFrame" + i,
				true, // resizable
		        true, // closable
		        true, // maximizable
		        false); //iconizable
		jInternalFrame1.setPreferredSize(new Dimension(200, 200));
		jInternalFrame1.setSize(new Dimension(200, 200));
		
		JPanel jPanel1 = new JPanel();
		jPanel1.setBackground(Color.BLUE);
		jInternalFrame1.getContentPane().add(jPanel1);
		jInternalFrame1.setVisible(true);
		jDesktopPane.add(jInternalFrame1);				

		// Creates a JDialog with a JPanel, and adds it
		JDialog jDialog = new JDialog();
		jDialog.setTitle("JDialog " + i);
		jDialog.setSize(new Dimension(100, 150));		
		JPanel jPanel2 = new JPanel();
		jPanel2.setBackground(Color.GREEN);
		jDialog.getContentPane().add(jPanel2);			
		jDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		jDialog.setVisible(true);
		
		// Centers the JInternalFrame
		CenterComponent.centerComponent(jInternalFrame1, jDesktopPane.getBounds());
		
		// Centers the JDialog
		jDialog.setLocationRelativeTo(jFrame.getContentPane());

		i++;
	}
}
