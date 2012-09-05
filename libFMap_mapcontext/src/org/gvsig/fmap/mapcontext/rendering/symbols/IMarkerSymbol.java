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
 * $Id: IMarkerSymbol.java 20989 2008-05-28 11:05:57Z jmvivo $
 * $Log$
 * Revision 1.8  2007-08-14 11:17:31  jvidal
 * javadoc updated
 *
 * Revision 1.7  2007/08/13 11:36:50  jvidal
 * javadoc
 *
 * Revision 1.6  2007/08/08 12:03:38  jvidal
 * javadoc
 *
 * Revision 1.5  2007/08/02 10:19:41  jvidal
 * *** empty log message ***
 *
 * Revision 1.4  2007/05/09 16:07:26  jaume
 * *** empty log message ***
 *
 * Revision 1.3  2007/05/08 08:47:40  jaume
 * *** empty log message ***
 *
 * Revision 1.2  2007/03/09 11:20:57  jaume
 * Advanced symbology (start committing)
 *
 * Revision 1.1.2.4  2007/02/16 10:54:12  jaume
 * multilayer splitted to multilayerline, multilayermarker,and  multilayerfill
 *
 * Revision 1.1.2.3  2007/02/15 16:23:44  jaume
 * *** empty log message ***
 *
 * Revision 1.1.2.2  2007/02/14 15:53:35  jaume
 * *** empty log message ***
 *
 * Revision 1.1.2.1  2007/02/09 07:47:05  jaume
 * Isymbol moved
 *
 *
 */

package org.gvsig.fmap.mapcontext.rendering.symbols;

import java.awt.Color;
import java.awt.geom.Point2D;

import org.gvsig.fmap.mapcontext.rendering.symbols.styles.IMask;



/**
 * Represents an ISymbol that draws a <b>marker symbol</b>.It has the different methods to obtain or
 * define the attributes of a marker such as rotation, offset, size, color and
 * IMask.
 *
 * @author   jaume dominguez faus - jaume.dominguez@iver.es
 */
public interface IMarkerSymbol extends ISymbol, CartographicSupport {

	/**
	 * Returns the rotation of the marker symbol
	 * @return double (rotation)
	 */
	public abstract double getRotation();
	/**
	 * Sets the rotation of the marker symbol
	 * @param rotation
	 */
	public abstract void setRotation(double rotation);
	/**
	 * Gets the offset for a marker symbol
	 * @return Point2D
	 */
	public abstract Point2D getOffset();
	/**
	 * Establishes the offset currently set for the marker symbol.
	 * @param offset
	 */
	public abstract void setOffset(Point2D offset);
	/**
	 * Obtains the size of a marker symbol
	 *
	 */
	public abstract double getSize();
	/**
	 * Sets the size of marker symbol
	 * @param size
	 */
	public abstract void setSize(double size);

	/**
	 * Returns the color of the marker symbol.
	 * @return Color
	 */
	public abstract Color getColor();

	/**
	 * Establishes a color for the marker symbol
	 * @param color
	 */
	public abstract void setColor(Color color);
	/**
	 *
	 * @return the mask of the symbol
	 */
	public abstract IMask getMask();
	/**
	 * Defines a mask for the symbol
	 * @param mask,IMask
	 */
	public abstract void setMask(IMask mask);

}