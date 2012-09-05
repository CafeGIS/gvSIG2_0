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
 * $Id: IFillSymbol.java 21071 2008-06-02 10:55:35Z vcaballero $
 * $Log$
 * Revision 1.3  2007-08-13 11:36:50  jvidal
 * javadoc
 *
 * Revision 1.2  2007/03/09 11:20:56  jaume
 * Advanced symbology (start committing)
 *
 * Revision 1.1.2.2  2007/02/21 16:09:02  jaume
 * *** empty log message ***
 *
 * Revision 1.1.2.1  2007/02/16 10:54:12  jaume
 * multilayer splitted to multilayerline, multilayermarker,and  multilayerfill
 *
 *
 */
package org.gvsig.fmap.mapcontext.rendering.symbols;

import java.awt.Color;

import org.gvsig.fmap.geom.Geometry;


/**
 * Interface that extends ISymbol interface in order to define methods for
 * fill symbols which can manage specific attributes of them.
 *
 *
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 */

public interface IFillSymbol extends ISymbol, CartographicSupport{

	public abstract boolean isSuitableFor(Geometry geom);

	public abstract int getOnePointRgb();

	/**
	 * Sets the color that will be used to draw the filling pattern of this symbol.
	 *
	 * @param Color
	 */
	public abstract void setFillColor(Color color);

	/**
	 * Sets the color of the outline.
	 * @deprectated will be substituted by setOutline(AbstractLineSymbol);
	 * @param color
	 */
	public abstract void setOutline(ILineSymbol outline);

	/**
	 * @return Returns the color.
	 */
	public abstract Color getFillColor();

	/**
	 * Obtains the ILineSymbol interface of the outline
	 * @return the outline,ILineSymbol.
	 */
	public abstract ILineSymbol getOutline();
	/**
	 * Obtains the transparency of the fill symbol
	 * @return the transparency of the fill symbol
	 */

	public abstract int getFillAlpha();

	public abstract boolean hasFill();

	public abstract void setHasFill(boolean hasFill);

	public abstract boolean hasOutline();

	public abstract void setHasOutline(boolean hasOutline);
}