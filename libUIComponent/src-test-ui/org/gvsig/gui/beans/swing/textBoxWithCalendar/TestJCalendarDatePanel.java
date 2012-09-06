package org.gvsig.gui.beans.swing.textBoxWithCalendar;

import java.awt.Dimension;
import java.io.Serializable;

import javax.swing.JFrame;


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
 * Tests the JCalendarCDatePanel -> creates a JFrame that allows use a JCalendarCDatePanel object
 * 
 * @author Pablo Piqueras Bartolomé (p_queras@hotmail.com)
 */
public class TestJCalendarDatePanel extends JFrame implements Serializable {
	private static final long serialVersionUID = 6214293384150864091L;

	/**
	 * Test method for the TestJCalendarCDatePanel class
	 * @param args
	 */
	public static void main(String[] args)
	{
		int old_width = 200;
		int old_height = 50;
		int new_width = 445;
		int new_height = 370;
		
		// Objects creation
		JFrame jF = new JFrame();		
		JCalendarDatePanel t = new JCalendarDatePanel();
		
		// Set properties
		jF.setTitle("Test JCalendarDatePanel");
		jF.setSize(new Dimension(old_width, old_height));	    
	    jF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    jF.getContentPane().add(t);	    
		
		jF.setVisible(true);
		
		// Test Modal
		t.setModal(true);
		
		// Test resize the panel (and its button)
		jF.setSize(new Dimension(new_width, new_height)); // For see all panel with the new size
		t.setPreferredSize(new Dimension (355, 325));
		t.setSize(new Dimension (360, 320));
		t.setSize(365, 325);
		t.revalidate();
		
		// Test Disable Calendar Selection
		//t.disableCalendar();
		
		// Tests resizing the Calendar dialog
		// Test set minimum dimension
		t.getJCalendarDateDialog().setMinimumWidth(350);
		t.getJCalendarDateDialog().setMinimumHeight(170);
//		t.getJCalendarDateDialog().setMinimumWidthScreenResolutionPercentage(0.30);
//		t.getJCalendarDateDialog().setMinimumHeightScreenResolutionPercentage(0.20);
		
		// Test set maximum dimension
		t.getJCalendarDateDialog().setMaximumWidth(500);
		t.getJCalendarDateDialog().setMaximumHeight(400);		
//		t.getJCalendarDateDialog().setMaximumWidthScreenResolutionPercentage(0.60);
//		t.getJCalendarDateDialog().setMaximumHeightScreenResolutionPercentage(0.40);		

		// Test Resize the component:
//		t.getJCalendarDateDialog().setSizeResize(350, 300);
	}
}
