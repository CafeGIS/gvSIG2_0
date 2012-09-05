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
 * Interface that offers the methods to classify the legend.
 */
public interface IClassifiedVectorLegend extends IClassifiedLegend, IVectorLegend{
	/**
	 * Deletes all the information of classification:
	 * intervals, values, or other classifying elements
	 */
	public abstract void clear();

	/**
	 * Returns the name of the field
	 *
	 * @return Name of the field.
	 *
	 * TODO refactor to allow more than one field name
	 */
	public abstract String[] getClassifyingFieldNames();

	/**
	 * Inserts the name of the field
	 *
	 * @param fieldNames Names of the fields.
	 * TODO refactor to allow more than one field name
	 */
	public abstract void setClassifyingFieldNames(String[] fieldNames);

	 /**
     * Inserts a symbol.
     *
     * @param key.
     * @param symbol.
     */
	public abstract void addSymbol(Object key, ISymbol symbol);

    /**
     * Deletes a symbol using for that its key which is the parameter of the
     * method.
     *
     * @param key clave.
     */
	public abstract void delSymbol(Object key);

	/**
	 * Removes <b>oldSymbol</b> from the Legend and substitutes it with the <b>newSymbol</b>
	 * @param oldSymbol
	 * @param newSymbol
	 */
	public abstract void replace(ISymbol oldSymbol, ISymbol newSymbol);

	public int[] getClassifyingFieldTypes();

	public void setClassifyingFieldTypes(int[] fieldTypes);
}
