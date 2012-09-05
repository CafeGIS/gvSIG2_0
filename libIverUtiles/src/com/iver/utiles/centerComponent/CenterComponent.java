package com.iver.utiles.centerComponent;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;

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
 * Allows center a component (Ex. JInternalFrame, JFrame, etc.) respect its parent component
 * 
 * Exceptions:<br>
 * <ul>
 *  <li>If the component is a JDialog -> better use:<br>
 *      jDialog.setLocationRelativeTo(parentComponent);</li>
 *  <li>If the component is a JFrame or a JWindow -> better use:<br>
 *      jFrame.setSize(width, height);<br>
 *      jFrame.setLocationRelativeTo(null);<br>
 *    or<br>
 *      jWindow.setSize(width, height);<br>
 *      jWindow.setLocationRelativeTo(null);</li> 
 * </ul>
 * 
 * @author Pablo Piqueras Bartolomé (p_queras@hotmail.com)
 */
public class CenterComponent {

	/**
	 * Centers a component (Ex. JInternalFrame, JFrame, etc.) according its size and the bounds of its parent<br>
	 * 
	 * Exceptions:<br>
	 * <ul>
	 *  <li>If the component is a JDialog -> better use:<br>
	 *      jDialog.setLocationRelativeTo(parentComponent);</li>
	 *  <li>If the component is a JFrame or a JWindow -> better use:<br>
	 *      jFrame.setSize(width, height);<br>
	 *      jFrame.setLocationRelativeTo(null);<br>
	 *    or<br>
	 *      jWindow.setSize(width, height);<br>
	 *      jWindow.setLocationRelativeTo(null);</li>
	 * </ul>
	 *
  	 * @param component The component to center 
	 * @param parentBounds Bounds of the parent of the component.
	 */
	public static void centerComponent(Component component, Rectangle parentBounds) {
		final int DefaultXMargin = 20;
		final int DefaultYMargin = 20;
		final int MinimumXMargin = 10;
		final int MinimumYMargin = 10;
		
		// The top-left square of JComponent reference
		Point newReferencePoint = new Point();
		newReferencePoint.x = (parentBounds.x + (parentBounds.width - component.getWidth()) / 2);
		newReferencePoint.y = (parentBounds.y + (parentBounds.height - component.getHeight()) / 2);
		
		// Calculate the new point reference (Assure that the top-left corner is showed)
		if (newReferencePoint.x < 0)
		{
			if (newReferencePoint.x > MinimumXMargin)
				newReferencePoint.x = DefaultXMargin;
			else
				newReferencePoint.x = 0;
		}
			
			
		if (newReferencePoint.y < 0)
		{
			if (newReferencePoint.y > MinimumYMargin)
				newReferencePoint.y = DefaultYMargin;
			else
				newReferencePoint.y = 0;
		}			

		// Set the new location for this JComponent object
		component.setLocation(newReferencePoint);
	}	
}
