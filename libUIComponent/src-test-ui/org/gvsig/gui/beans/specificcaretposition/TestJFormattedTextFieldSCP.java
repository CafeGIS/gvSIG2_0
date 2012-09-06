package org.gvsig.gui.beans.specificcaretposition;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.gvsig.gui.awt.event.specificCaretPosition.ISpecificCaretPosition;
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
 * Tests for JFormattedTextFieldWithSpecificCaretPosition
 * 
 * @author Pablo Piqueras Bartolomé (p_queras@hotmail.com)
 */
public class TestJFormattedTextFieldSCP {
	private static JFormattedTextFieldSCP jTF1;
	private static JFormattedTextFieldSCP jTF2;
	private static JFormattedTextFieldSCP jTF3;
	private static JFormattedTextFieldSCP jTF4;
	
	/**
	 * Test method for the TestJCalendarCDatePanel class
	 * @param args
	 */
	public static void main(String[] args)
	{
		final int height = 60;
		final int width = 300;
		
		final int heightTF = 25;
		final int widthTF = 40;
		
		final int heightJB = 25;
		final int widthJB = 100;
		
		// Objects creation
		JFrame jF = new JFrame();		

		JPanel jP = new JPanel();
		jP.setPreferredSize(new Dimension(width, height));
		
		jTF1 = new JFormattedTextFieldSCP();
		jTF1.setPreferredSize(new Dimension(widthTF, heightTF));
		jTF1.setToolTipText(Messages.getText("default"));
		
		jTF2 = new JFormattedTextFieldSCP();
		jTF2.setCaretPositionMode(ISpecificCaretPosition.RIGHT_POSITIONS);
		jTF2.setPreferredSize(new Dimension(widthTF, heightTF));
		jTF2.setToolTipText(Messages.getText("right"));

		jTF3 = new JFormattedTextFieldSCP();
		jTF3.setCaretPositionMode(ISpecificCaretPosition.LEFT_POSITIONS);
		jTF3.setPreferredSize(new Dimension(widthTF, heightTF));
		jTF3.setToolTipText(Messages.getText("left"));

		jTF4 = new JFormattedTextFieldSCP();
		jTF4.setCaretPositionMode(ISpecificCaretPosition.LIKE_JTEXTCOMPONENT);
		jTF4.setPreferredSize(new Dimension(widthTF, heightTF));
		jTF4.setToolTipText(Messages.getText("like_JTextField"));
		
		JButton jButton = new JButton();
		jButton.setToolTipText(Messages.getText("enable/Disable"));
		jButton.setText("En./Dis.");
		jButton.setPreferredSize(new Dimension(widthJB, heightJB));
		jButton.addMouseListener(new MouseAdapter() {
			/*
			 *  (non-Javadoc)
			 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
			 */
			public void mouseClicked(MouseEvent e) {
				enableOrDisableJTextFields();
			}			
		});

	    
		jP.add(jTF1);
		jP.add(jTF2);
		jP.add(jTF3);
		jP.add(jTF4);
		jP.add(jButton);

//		jF.setVisible(false);		
	    // Test adding text to text fields

	    // Set properties
		jF.setTitle("Test JFormattedTextFieldWithSpecificCaretPosition");
	    jF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    jF.setSize(jP.getPreferredSize());
	    jF.getContentPane().add(jP);	
	    
		jF.setVisible(true);
		
	    jTF1.setEnabled(false);
	    jTF1.setText("+123456.78901234141241");
	    jTF2.setEnabled(false);
	    jTF2.setText("-0917429817.10934712");
	    jTF3.setEnabled(false);
	    jTF3.setText("-184091.3095174");
	    jTF4.setEnabled(false);
	    jTF4.setText("+128947.09345734");

	}
	
	private static void enableOrDisableJTextFields() {
		jTF1.setEnabled(!jTF1.isEnabled());
		jTF2.setEnabled(!jTF2.isEnabled());
		jTF3.setEnabled(!jTF3.isEnabled());
		jTF4.setEnabled(!jTF4.isEnabled());
	}
}
