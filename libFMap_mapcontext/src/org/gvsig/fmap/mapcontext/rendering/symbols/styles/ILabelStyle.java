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
* $Id: ILabelStyle.java 20989 2008-05-28 11:05:57Z jmvivo $
* $Log$
* Revision 1.10  2007-08-16 06:55:30  jvidal
* javadoc updated
*
* Revision 1.9  2007/08/13 11:36:30  jvidal
* javadoc
*
* Revision 1.8  2007/05/08 08:47:39  jaume
* *** empty log message ***
*
* Revision 1.7  2007/04/05 16:07:14  jaume
* Styled labeling stuff
*
* Revision 1.6  2007/04/04 15:42:03  jaume
* *** empty log message ***
*
* Revision 1.5  2007/04/04 15:41:05  jaume
* *** empty log message ***
*
* Revision 1.4  2007/04/02 16:34:56  jaume
* Styled labeling (start commiting)
*
* Revision 1.3  2007/03/29 16:02:01  jaume
* *** empty log message ***
*
* Revision 1.2  2007/03/09 11:20:56  jaume
* Advanced symbology (start committing)
*
* Revision 1.1.2.1  2007/02/15 16:23:44  jaume
* *** empty log message ***
*
* Revision 1.1.2.1  2007/02/09 07:47:05  jaume
* Isymbol moved
*
*
*/
package org.gvsig.fmap.mapcontext.rendering.symbols.styles;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;


/**
 * Defines the style that a Label symbol can contain which typically define
 * a background of the label as an Image, the texts that the label holds
 * and their position within the label in rendering time.
 *
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 *
 */
public interface ILabelStyle extends IStyle {
	/**
	 * @return int, the amount of fields this style allows
	 */
	public int getFieldCount();

	/**
	 * Sets the texts that will appear in the label.
	 * @param texts
	 */
	public void setTextFields(String[] texts);


	/**
	 * Returns an array of rectangles defining the text boxes where text is
	 * placed.
	 * @return
	 */
	public Rectangle2D[] getTextBounds();

	public Dimension getSize();

	/**
	 * Returns the position of the point labeled by this label in percent units relative
	 * to the label bounds. It determines the offset to be applied to the label to be
	 * placed and allows the user to use custom styles.
	 */
	public Point2D getMarkerPoint();

	/**
	 * Sets the position of the point labeled by this in percent units relative to the
	 * label bounds
	 * @param p
	 * @throws IllegalArgumentException if the point coordinates are >0.0 or <1.0
	 */
	public void setMarkerPoint(Point2D p) throws IllegalArgumentException;

	/**
	 * Sets a TextFieldArea using its index. With this method the user can
	 * modify the size of the rectangle for the TextFieldArea
	 * @param index
	 * @param rect
	 */
	public void setTextFieldArea(int index, Rectangle2D rect);
	
	/**
	 * Adds a new TextFieldArea with an specific size which is defined as a rectangle.
	 * @param index,int
	 * @param rect,Rectangle2D
	 */
	public void addTextFieldArea(Rectangle2D rect);
	
	/**
	 * Delete the TextFieldArea specified by its index.
	 * @param index,int
	 */
	public void deleteTextFieldArea(int index);
	
	/**
	 * Sets the size for a laber and stablishes an unit scale factor to not change
	 * too much its content
	 * @param width
	 * @param height
	 */
	public void setSize(double width, double height);

}
