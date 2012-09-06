package org.gvsig.gui.beans.editabletextcomponent;

import java.awt.Dimension;

import javax.swing.JFrame;


/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */

/**
 * Tests for JEditableTextField
 * 
 * @author Pablo Piqueras Bartolom� (p_queras@hotmail.com)
 */
public class TestJEditableTextField {
	private static JEditableTextField jTCE;
	
	/**
	 * Test method for the TestJEditableTextField class
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		final int height = 40;
		final int width = 300;
		
		// Objects creation
		JFrame jF = new JFrame();		

		jTCE = new JEditableTextField();
		jTCE.setPreferredSize(new Dimension(width, height));

	    // Set properties
		jF.setTitle("Test JEditableTextField");
	    jF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    jF.setSize(jTCE.getPreferredSize());
	    jF.getContentPane().add(jTCE);	
	    
		jF.setVisible(true);
	}
}
