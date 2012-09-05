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
package org.gvsig.fmap.dal.feature.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureQuery;
import org.gvsig.fmap.dal.feature.FeatureQueryOrder;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.tools.evaluator.Evaluator;
import org.gvsig.tools.persistence.PersistenceException;
import org.gvsig.tools.persistence.Persistent;
import org.gvsig.tools.persistence.PersistentState;

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
public class DefaultFeatureQuery implements FeatureQuery {

    private double scale;

	private Map queryParameters = new HashMap();

	private String featureTypeId = null;

    private String[] attributeNames;

    private Evaluator filter;

    private FeatureQueryOrder order = new FeatureQueryOrder();

    /**
     * Creates a FeatureQuery which will load all available Features of a type.
     *
     * @param featureType
     *            the type of Features of the query
     */
    public DefaultFeatureQuery() {
        super();
    }

	/**
	 * Creates a FeatureQuery which will load all available Features of a type.
	 *
	 * @param featureType
	 *            the type of Features of the query
	 */
   public DefaultFeatureQuery(FeatureType featureType) {
       super();
       this.setFeatureType(featureType);
   }

    /**
     * Creates a FeatureQuery with the type of features, a filter and the order
     * for the FeatureCollection.
     *
     * @param featureType
     *            the type of Features of the query
     * @param filter
     *            based on the properties of the Features
     * @param order
     *            for the result
     */
    public DefaultFeatureQuery(FeatureType featureType, Evaluator filter) {
        super();
        this.setFeatureType(featureType);
        this.filter = filter;
    }

    /**
     * Creates a FeatureQuery with the type of features, a filter, the order for
     * the FeatureCollection and the view scale.
     *
     * @param featureType
     *            the type of Features of the query
     * @param filter
     *            based on the properties of the Features
     * @param order
     *            for the result
     * @param scale
     *            to view the Features.
     */
    public DefaultFeatureQuery(FeatureType featureType, Evaluator filter,
            double scale) {
    	this.scale = scale;
        this.setFeatureType(featureType);
        this.filter = filter;
    }

	/**
	 * Creates a FeatureQuery which will load a list of attribute names of all
	 * available Features.
	 *
	 * @param attributeNames
	 *            the list of attribute names to load
	 */
    public DefaultFeatureQuery(String[] attributeNames) {
        super();
        this.attributeNames = attributeNames;
    }

	/**
	 * Creates a FeatureQuery with the list of attribute names of feature, a
	 * filter and the order for the FeatureCollection.
	 *
	 * @param attributeNames
	 *            the list of attribute names to load
	 * @param filter
	 *            based on the properties of the Features
	 * @param order
	 *            for the result
	 */
    public DefaultFeatureQuery(String[] attributeNames, Evaluator filter) {
        super();
        this.attributeNames = attributeNames;
        this.filter = filter;
    }

	/**
	 * Creates a FeatureQuery with the list of attribute names of feature, a
	 * filter, the order for the FeatureCollection and the view scale.
	 *
	 * @param attributeNames
	 *            the list of attribute names to load
	 * @param filter
	 *            based on the properties of the Features
	 * @param order
	 *            for the result
	 * @param scale
	 *            to view the Features.
	 */
    public DefaultFeatureQuery(String[] attributeNames, Evaluator filter,
            double scale) {
        this.scale = scale;
        this.attributeNames = attributeNames;
        this.filter = filter;
    }

    /**
	 * Returns the scale.
	 *
	 * @return the scale
	 */
	public double getScale() {
		return scale;
	}

	/**
	 * Sets the scale.
	 *
	 * @param scale
	 *            the scale to set
	 */
	public void setScale(double scale) {
		this.scale = scale;
	}

	/**
	 * Returns the value of an query parameter.
	 *
	 * @param name
	 *            of the parameter
	 * @return the parameter value
	 */
	public Object getQueryParameter(String name) {
		return queryParameters.get(name);
	}

	/**
	 * Sets the value of an query parameter.
	 *
	 * @param name
	 *            of the query parameter
	 * @param value
	 *            for the query parameter
	 */
	public void setQueryParameter(String name, Object value) {
		queryParameters.put(name, value);
	}


    /**
     * @param featureType
     *            the featureType to set
     */
    public void setFeatureType(FeatureType featureType) {
        this.featureTypeId = featureType.getId();
        Iterator iter = featureType.iterator();
		String attrs[] = new String[featureType.size()];
		FeatureAttributeDescriptor attr;
		int i = 0;
		while (iter.hasNext()) {
			attr = (FeatureAttributeDescriptor) iter.next();
			attrs[i] = attr.getName();
			i++;
		}
		this.attributeNames = attrs;

    }

	/**
	 * @return the attribute names
	 */
    public String[] getAttributeNames() {
		return attributeNames;
    }

	/**
	 * @param attributeNames
	 *            the attribute names to set
	 */
    public void setAttributeNames(String[] attributeNames) {
		this.attributeNames = attributeNames;
    }

    /**
     * @return the filter
     */
    public Evaluator getFilter() {
        return filter;
    }

    /**
     * @param filter
     *            the filter to set
     */
    public void setFilter(Evaluator filter) {
        this.filter = filter;
    }

    /**
     * @return the order
     */
    public FeatureQueryOrder getOrder() {
        return order;
    }

    /**
     *
     *
     */
	public void setOrder(FeatureQueryOrder order) {
		this.order = order;
	}

	public boolean hasFilter() {
		return this.filter != null;
	}

	public boolean hasOrder() {
		return this.order.size() > 0;
	}

	public FeatureQuery getCopy() {
		DefaultFeatureQuery aCopy = new DefaultFeatureQuery();

		aCopy.featureTypeId = this.featureTypeId;

		if (this.attributeNames != null) {
			aCopy.attributeNames = (String[]) Arrays
					.asList(this.attributeNames).toArray(new String[0]);
		}

		aCopy.filter = this.filter;

		if (this.order != null) {
			aCopy.order = this.order.getCopy();
		}

		return aCopy;
	}

	public String getFeatureTypeId() {
		return featureTypeId;
	}

	public void setFeatureTypeId(String featureTypeId) {
		this.featureTypeId = featureTypeId;
	}

	/**
	 * @see Persistent#loadState()
	 */
	public void saveToState(PersistentState state) throws PersistenceException {
		// FIXME: falta por terminar de implementar
		state.set("scale", scale);
	}

	/**
	 * @see Persistent#setState()
	 */
	public void loadFromState(PersistentState state) throws PersistenceException {
		// FIXME: falta por terminar de implementar
		this.scale = state.getDouble("scale");
	}

}