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
public class TestJCalendarCDatePanel extends JFrame implements Serializable {
	private static final long serialVersionUID = 8633128258748487018L;

	/**
	 * Test method for the CalendarPanelTest
	 * @param args
	 */
	public static void main(String[] args)
	{
		int width = 500;
		int height = 30;
		
		// Objects creation
		JFrame jF = new JFrame();		
		JCalendarCDatePanel t = new JCalendarCDatePanel();
		
		// Set properties
		jF.setTitle("Test JCalendarCDatePanel");
		jF.setSize(new Dimension(width, height));	    
	    jF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    jF.getContentPane().add(t);	    
		
		jF.setVisible(true);
		
		// Test Resize the component:
		t.setPreferredSizeResize(500, 24);
		
		// Test Disable Calendar Selection
		//t.disableCalendar();
	}
}
