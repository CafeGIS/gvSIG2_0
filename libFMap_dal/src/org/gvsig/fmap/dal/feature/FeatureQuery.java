/* gvSIG. Geographic Information System of the Valencian Government
*
* Copyright (C) 2007-2008 Infrastructures and Transports Department
* of the Valencian Gobernment (CIT)
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
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
* MA  02110-1301, USA.
*
*/

/*
 * AUTHORS (In addition to CIT):
 * 2008 {DiSiD Technologies}  {Create Parameter object to define FeatureCollections queries}
 */
package org.gvsig.fmap.dal.feature;

import org.gvsig.fmap.dal.DataQuery;
import org.gvsig.tools.evaluator.Evaluator;

/**
 * Defines the properties of a collection of Features, as a result of a query
 * through a FeatureStore.
 * <p>
 * A FeatureQuery is always defined by a FeatureType, or by the list of
 * attribute names of the FeatureStore to return.
 * </p>
 * <p>
 * The filter allows to select Features whose properties have values with the
 * characteristics defined by the filter.
 * </p>
 * <p>
 * The order is used to set the order of the result FeatureCollection, based on
 * the values of the properties of the Features.
 * </p>
 * <p>
 * The scale parameter can be used by the FeatureStore as a hint about the
 * quality or resolution of the data needed to view or operate with the data
 * returned. As an example, the FeatureStore may use the scale to return only a
 * representative subset of the data, or maybe to return Features with less
 * detail, like a point or a line instead of a polygon.
 * </p>
 * <p>
 * If an implementation of FeatureStore is able to get other parameters to
 * customize the behavior of the getDataCollection methods, there is an option
 * to set more parameters through the setAttribute method.
 * </p>
 *
 * @author <a href="mailto:cordin@disid.com">César Ordiñana</a>
 */
public interface FeatureQuery extends DataQuery {



    /**
     * @param featureType
     *            the featureType to set
     */
    public void setFeatureType(FeatureType featureType);

	/**
	 * @return the attribute names
	 */
    public String[] getAttributeNames();

	/**
	 * @param attributeNames
	 *            the attribute names to set
	 */
    public void setAttributeNames(String[] attributeNames);

    /**
     * @return the filter
     */
    public Evaluator getFilter();

    /**
     * @param filter
     *            the filter to set
     */
    public void setFilter(Evaluator filter);

    /**
     * @return the order
     */
    public FeatureQueryOrder getOrder();

    /**
     *
     *
     */
	public void setOrder(FeatureQueryOrder order);

	public boolean hasFilter();

	public boolean hasOrder();

	public FeatureQuery getCopy();

	public String getFeatureTypeId();

	public void setFeatureTypeId(String featureTypeId);

}