/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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

/* CVS MESSAGES:
 *
 * $Id: ILineStyle.java 20989 2008-05-28 11:05:57Z jmvivo $
 * $Log$
 * Revision 1.7  2007-08-13 11:36:30  jvidal
 * javadoc
 *
 * Revision 1.6  2007/07/26 12:36:52  jaume
 * character encoding mispell
 *
 * Revision 1.5  2007/07/23 06:53:56  jaume
 * Added support for arrow line decorator (start commiting)
 *
 * Revision 1.4  2007/05/09 16:07:26  jaume
 * *** empty log message ***
 *
 * Revision 1.3  2007/05/08 08:47:39  jaume
 * *** empty log message ***
 *
 * Revision 1.2  2007/03/09 11:20:56  jaume
 * Advanced symbology (start committing)
 *
 * Revision 1.1.2.3  2007/02/15 16:23:44  jaume
 * *** empty log message ***
 *
 * Revision 1.1.2.2  2007/02/12 15:15:20  jaume
 * refactored interval legend and added graduated symbol legend
 *
 * Revision 1.1.2.1  2007/02/09 07:47:04  jaume
 * Isymbol moved
 *
 *
 */

package org.gvsig.fmap.mapcontext.rendering.symbols.styles;

import java.awt.Stroke;




/**
 * Interface that controls the properties that define the style of a line.This
 * properties are the width, stroke, offset and arrow decorator style( for example
 * if the line includes markers as an arrow to specify an orientation).
 * @author   jaume dominguez faus - jaume.dominguez@iver.es
 */
public interface ILineStyle extends IStyle {
	/**
	 * Returns the width of the line
	 * @return
	 */
	public abstract float getLineWidth();
	/**
	 * Establishes the width of the line
	 * @param width of the line
	 */
	public abstract void setLineWidth(float width);
	/**
	 * Returns the stroke of the line
	 * @return Stroke of the line
	 */
	public abstract Stroke getStroke();
	/**
	 * Sets the stroke of the line
	 * @param stroke,stroke to be established
	 */
	public abstract void setStroke(Stroke stroke);
	/**
	 * Gets the arrow decorator style that has the line
	 * @return ArrowDecoratorStyle
	 */
	public abstract ArrowDecoratorStyle getArrowDecorator();
	/**
	 * Sets the arrow decorator style of the line
	 * @param arrowDecoratorStyle, ArrowDecoratorStyle
	 */
	public abstract void setArrowDecorator(ArrowDecoratorStyle arrowDecoratorStyle);
	/**
	 * Returns the offset of the line
	 * @return
	 */
	public abstract double getOffset();
	/**
	 * Establishes the offset of the line
	 * @param offset
	 */
	public abstract void setOffset(double offset);

}