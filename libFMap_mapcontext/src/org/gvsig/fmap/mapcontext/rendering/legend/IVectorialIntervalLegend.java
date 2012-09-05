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
import org.gvsig.fmap.dal.feature.FeatureReference;
import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;


/**
 * Interface that allows the methods to classify the legend through intervals.
 *
 */
public interface IVectorialIntervalLegend extends IClassifiedVectorLegend {


    public void setDefaultSymbol(ISymbol s);

    public IInterval getInterval(Object v) ;
    public int getIntervalType();
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
    public ISymbol getSymbolByFeature(Feature feat);

	/**
	 *
	 * Returns the symbol starting from an interval
	 *
	 * @param key interval.
	 *
	 * @return symbol.
	 */
    public ISymbol getSymbolByInterval(IInterval key);


    /**
     * Inserts the type of the classification of the intervals.
	 *
	 * @param tipoClasificacion type of the classification.
	 */
    public void setIntervalType(int tipoClasificacion);


    public ISymbol getSymbol(FeatureReference id) throws DataException;

}

