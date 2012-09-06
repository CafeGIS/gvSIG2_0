package org.gvsig.gui.beans.swing.textBoxWithCalendar;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JFrame;

import org.gvsig.gui.beans.Messages;

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
 * Tests the JCalendarDateDialog  -> creates a JFrame that allows use a JCalendarDateDialog  object

 * @author Pablo Piqueras Bartolomé (p_queras@hotmail.com)
 */
public class TestJCalendarDateDialog extends JFrame implements Serializable {
	private static final long serialVersionUID = 5885608277555264729L;
	private static JCalendarDateDialog t;

	/**
	 * Test method for the TestJCalendarCDatePanel class
	 * @param args
	 */
	public static void main(String[] args)
	{
		int widthJF = 200;
		int heightJF = 50;
		int widthJB = 30;
		int heightJB = 20;
		
		// Objects creation
		JFrame jF = new JFrame();
		
		// JCalendarDateDialog
		t = new JCalendarDateDialog();		
		
		// Test Modal
		t.setModal(true);
		
		// Test set minimum dimension
//		t.setMinimumWidth(200);
//		t.setMinimumHeight(150);
		t.setMinimumWidthScreenResolutionPercentage(0.30);
		t.setMinimumHeightScreenResolutionPercentage(0.20);
		
		// Test set maximum dimension
//		t.setMaximumWidth(500);
//		t.setMaximumHeight(400);		
		t.setMaximumWidthScreenResolutionPercentage(0.60);
		t.setMaximumHeightScreenResolutionPercentage(0.40);		

		// Test Resize the component:
		t.setSizeResize(350, 300);
		
		// Creates a JButton
		JButton jButton = new JButton();
		jButton.setPreferredSize(new Dimension(widthJB, heightJB));
		jButton.setText(Messages.getText("date"));
		jButton.addMouseListener(new MouseAdapter() {
			/*
			 *  (non-Javadoc)
			 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
			 */
			public void mouseClicked(MouseEvent e) {
				t.setVisible(true);
				t.setLocationRelativeTo(null);
				System.out.println("Selected date: " + new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(t.getDate()));
			}
		});
		
		// Set properties
		jF.setTitle("Test JCalendarDateDialog");
		jF.setSize(new Dimension(widthJF, heightJF));	    
	    jF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    jF.getContentPane().add(jButton);
	    jF.setLocationRelativeTo(null);
		
		jF.setVisible(true);
	
	}
}
