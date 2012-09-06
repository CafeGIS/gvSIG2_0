package org.gvsig.gui.awt.event.specificCaretPosition;
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
 * Interface with the prototype of methods to have a component with an specific
 *   caret position
 * 
 * @author Pablo Piqueras Bartolomé (p_queras@hotmail.com)
 */
public interface ISpecificCaretPosition {
	public static final int LEFT_POSITIONS = 1;
	public static final int RIGHT_POSITIONS = 2;
	public static final int LIKE_JTEXTCOMPONENT = 3;
	
	/**
	 * Gets the position of text that will be seen
	 *
	 * @return The position of text that will be seen (LEFT_POSITIONS, RIGHT_POSITIONS or LIKE_JTEXTCOMPONENT)
	 */
	public int getCaretPositionMode();

	/**
	 * Sets the position of text that will be seen
	 * 
	 * @param caretPositionMode The position of text that will be seen (LEFT_POSITIONS, RIGHT_POSITIONS or LIKE_JTEXTCOMPONENT)
	 */
	public void setCaretPositionMode(int caretPositionMode);
}
