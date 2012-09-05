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
package org.gvsig.fmap.mapcontext.rendering.legend;


import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;


/**
 * Interface for the legend with unique values.
 *
 */
public interface IVectorialUniqueValueLegend extends IClassifiedVectorLegend {
    /**
     * Establishes the symbol for the value which is the argument of the function.
     *
     * @param id index.
     * @param symbol symbol.
     */
    void setValueSymbolByID(int id, ISymbol symbol);


    /**
     * Returns the symbols starting from an ID. It looks in m_ArrayKeys for the
     * element ID and with this key obtains the FSymbol.

     * @param key ID.
     *
     * @return symbol associated with the ID
     */

    // public FSymbol getSymbolByID(int ID);

    /**
     * Returns the symbols starting from its key.
     *
     * @param key ID.
     *
     * @return symbol associated with the key.
     */
    public ISymbol getSymbolByValue(Object key);
}
