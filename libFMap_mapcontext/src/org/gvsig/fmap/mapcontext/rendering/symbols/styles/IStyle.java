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
* $Id: IStyle.java 20989 2008-05-28 11:05:57Z jmvivo $
* $Log$
* Revision 1.7  2007-09-20 09:33:15  jaume
* Refactored: fixed name of IPersistAnce to IPersistence
*
* Revision 1.6  2007/09/19 16:19:27  jaume
* removed unnecessary imports
*
* Revision 1.5  2007/08/22 09:49:00  jvidal
* javadoc updated
*
* Revision 1.4  2007/08/16 06:55:30  jvidal
* javadoc updated
*
* Revision 1.3  2007/08/13 11:36:41  jvidal
* javadoc
*
* Revision 1.2  2007/04/04 15:41:05  jaume
* *** empty log message ***
*
* Revision 1.1  2007/03/09 11:20:56  jaume
* Advanced symbology (start committing)
*
* Revision 1.1.2.2  2007/02/15 16:23:44  jaume
* *** empty log message ***
*
* Revision 1.1.2.1  2007/02/09 07:47:05  jaume
* Isymbol moved
*
*
*/
package org.gvsig.fmap.mapcontext.rendering.symbols.styles;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.SymbolDrawingException;

import com.iver.utiles.IPersistence;

/**
 * Used by Objects that have to define an style for them.This interface has
 * methods to stablish a description and get it,to specify if an object
 * that implements this interface is suitable for a kind of symbol and other
 * methods to draw symbols.
 * @author   jaume dominguez faus - jaume.dominguez@iver.es
 */
public interface IStyle extends IPersistence{
	/**
	 * Defines the description of a symbol.It will be specified with an
	 * string.
	 * @param desc,String
	 */
	public abstract void setDescription(String desc);
	/**
	 * Returns the description of a symbol
	 * @return Description of a symbol
	 */
	public abstract String getDescription();
	/**
	 * Useful to render the symbol inside the TOC, or inside little
	 * rectangles. For example, think about rendering a Label with size
	 * in meters => You will need to specify a size in pixels.
	 * Of course. You may also preffer to render a prepared image, etc.
	 * @param g Graphics2D
	 * @param r Rectangle
	 */
	public abstract void drawInsideRectangle(Graphics2D g, Rectangle r) throws SymbolDrawingException;

	/**
	 * True if this symbol is ok for the style or class.
	 * @param symbol ISymbol
	 */

	public abstract boolean isSuitableFor(ISymbol symbol);

	/**
	 * Used to show an outline of the style to graphically show its properties.
	 * @param g, the Graphics2D where to draw
	 * @param r, the bounds of the style inside such Graphics2D
	 */
	public abstract void drawOutline(Graphics2D g, Rectangle r) throws SymbolDrawingException;
}
