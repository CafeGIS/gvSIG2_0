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
 * $Id: IMultiLayerSymbol.java 20989 2008-05-28 11:05:57Z jmvivo $
 * $Log$
 * Revision 1.5  2007-08-16 06:55:19  jvidal
 * javadoc updated
 *
 * Revision 1.4  2007/08/09 08:04:36  jvidal
 * javadoc
 *
 * Revision 1.3  2007/08/09 07:57:42  jvidal
 * javadoc
 *
 * Revision 1.2  2007/03/09 11:20:56  jaume
 * Advanced symbology (start committing)
 *
 * Revision 1.1.2.1  2007/02/16 10:54:12  jaume
 * multilayer splitted to multilayerline, multilayermarker,and  multilayerfill
 *
 *
 */
package org.gvsig.fmap.mapcontext.rendering.symbols;


/**
 * Allows to create multi layer symbols using the composition of
 * several symbols of the same type.Depending on the type of the symbols that are
 * used to compose the final symbol, the user will have MultiLayerMarkerSymbol,
 * MultiLayerLineSymbol,...
 *
 */

public interface IMultiLayerSymbol extends ISymbol {

	/**
	 * Establishes a concret symbol for a layer
	 *
	 * @param index, index of the layer
	 * @param layer, symbol to be "applied" to the layer
	 * @throws IndexOutOfBoundsException
	 */
	public abstract void setLayer(int index, ISymbol layer)
	throws IndexOutOfBoundsException;
	/**
	 * Changes the position of two layers in a multilayersymbol
	 * @param index1, index of the layer
	 * @param index2, index of the layer
	 */
	public abstract void swapLayers(int index1, int index2);
	/**
	 * Obtains  the symbol that "contains" a layer whose index is the argument of the method.
	 * @param layerIndex
	 * @return
	 */
	public abstract ISymbol getLayer(int layerIndex);
	/**
	 * Returns the number of layers
	 * @return int
	 */
	public abstract int getLayerCount();

	/**
	 * Stacks a new symbol to the symbol list. The symbol is appended to the
	 * list and, in terms of use, it will be the last symbol to be processed.
	 * @param ISymbol newLayer
	 */
	public abstract void addLayer(ISymbol newLayer);
	/**
	 * Stacks a new symbol to the symbol list. The symbol is appended to the
	 * list in the specified position.
	 * @param ISymbol newLayer
	 */
	public abstract void addLayer(ISymbol newLayer, int layerIndex)
	throws IndexOutOfBoundsException;

	/**
	 * TODO maybe push it up to ISymbol
	 * @param layer
	 * @return true if this symbol contains the removed one
	 */
	public abstract boolean removeLayer(ISymbol layer);

}