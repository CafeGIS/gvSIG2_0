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
 * $Id: ILineSymbol.java 20989 2008-05-28 11:05:57Z jmvivo $
 * $Log$
 * Revision 1.6  2007-08-14 11:17:31  jvidal
 * javadoc updated
 *
 * Revision 1.5  2007/08/08 12:03:13  jvidal
 * javadoc
 *
 * Revision 1.4  2007/05/08 08:47:39  jaume
 * *** empty log message ***
 *
 * Revision 1.3  2007/03/20 16:00:24  jaume
 * removed unuseful comments
 *
 * Revision 1.2  2007/03/09 11:20:56  jaume
 * Advanced symbology (start committing)
 *
 * Revision 1.1.2.5  2007/02/21 16:09:02  jaume
 * *** empty log message ***
 *
 * Revision 1.1.2.4  2007/02/15 16:23:44  jaume
 * *** empty log message ***
 *
 * Revision 1.1.2.3  2007/02/14 15:53:35  jaume
 * *** empty log message ***
 *
 * Revision 1.1.2.2  2007/02/13 16:19:19  jaume
 * graduated symbol legends (start commiting)
 *
 * Revision 1.1.2.1  2007/02/09 07:47:04  jaume
 * Isymbol moved
 *
 *
 */

package org.gvsig.fmap.mapcontext.rendering.symbols;

import java.awt.Color;

import org.gvsig.fmap.mapcontext.rendering.symbols.styles.ILineStyle;



/**
 * Interface for <b>line symbols</b>.It has the different methods to obtain or define
 * the attributes of a line such as color, style, width and alpha (transparency).
 *
 * @author   jaume dominguez faus - jaume.dominguez@iver.es
 */
public interface ILineSymbol extends ISymbol, CartographicSupport{

	/**
	 * Returns the color of the line symbol
	 * @return Color
	 */
	public abstract Color getColor();
	/**
	 * Sets the color for a line symbol
	 * @param color
	 */

	public abstract void setLineColor(Color color);
	/**
	 * Obtains the style of the line symbol.
	 * @return ILineStyle, the style of the line symbol
	 */
	public abstract ILineStyle getLineStyle();
	/**
	 * Defines the style of the line symbol to be used.
	 * @param lineStyle
	 */
	public abstract void setLineStyle(ILineStyle lineStyle);
	/**
	 * Sets the width of the line symbol.
	 * @param width, the width of the line
	 */
	public abstract void setLineWidth(double width);
	/**
	 * Returns the width of the line symbol
	 * @return
	 */

	public abstract double getLineWidth();

	/**
	 * Obtains the transparency of the line symbol
	 * @return
	 */
	public abstract int getAlpha();

	/**
	 * Defines the transparency of a line symbol.
	 * @param outlineAlpha, the transparency
	 */
	public abstract void setAlpha(int outlineAlpha);
}