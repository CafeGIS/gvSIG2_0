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

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;

import com.iver.utiles.XMLEntity;


/**
 * Interface of a vectorial legend.
 *
 * @author Vicente Caballero Navarro
 */
public interface IVectorLegend extends ILegend {
	/**
	 * Inserts the DataSource.
	 *
	 * @param ds DataSource.
	 * @throws DataException TODO
	 * @throws FieldNotFoundException when the field is not found.
	 * @throws DriverException When the driver fails.
	 */
	void setFeatureStore(FeatureStore fs)
		throws DataException;

	/**
	 * Returns the symbol to be used to represent the feature in the i-th
	 * record in the DataSource
	 * @param i, the record index
	 *
	 * @return ISymbol.
	 *
	 * @throws DriverException
	 */
//	ISymbol getSymbol(int i) throws ReadException;
	/**
     * Returns a symbol starting from an IFeature.
	 *
	 * TAKE CARE!! When we are using a feature iterator as a database
	 * the only field that will be filled is the fieldID.
	 * The rest of fields will be null to reduce the time of creation
	 *
	 * @param feat IFeature.
	 *
	 * @return Símbolo.
	 */
    ISymbol getSymbolByFeature(Feature feat);

	/**
	 * Returns the type of the shape.
	 *
	 * @return Returns the type of the shapes that the legend is ready to use.
	 *
	 */
	int getShapeType();

	/**
	 * Defines the type of the shape.
	 *
	 * @param shapeType type of the shape.
	 */
	void setShapeType(int shapeType);

	/**
	 * Establishes the default symbol of a legend. In a SingleSymbolLegend the symbol
	 * is established by calling this method.
	 *
	 * @param s default symbol.
	 * @throws IllegalArgumentException, if the symbol isn't suitable for the
	 * layer's data type.
	 */
	void setDefaultSymbol(ISymbol s) throws IllegalArgumentException;

	/**
     * Returns true or false depending on if the rest of values are used.
	 *
	 * @return  True if the rest of values are used.
	 */
    public boolean isUseDefaultSymbol();

    void useDefaultSymbol(boolean b);

    public ZSort getZSort();

	public void setZSort(ZSort zSort);

	public boolean isSuitableForShapeType(int shapeType);

}
