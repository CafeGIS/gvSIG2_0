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
 * 2008 {DiSiD Technologies}  {Add pagination to a FeatureCollection}
 */
package org.gvsig.fmap.dal.feature.paging;

import java.util.Iterator;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.EditableFeature;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureQuery;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureType;

/**
 * Helper interface to access the values of a FeatureCollection by position.
 *
 * @author <a href="mailto:cordin@disid.com">Cèsar Ordiñana</a>
 */
public interface FeaturePagingHelper {

    static final int DEFAULT_PAGE_SIZE = 100;

    /**
     * Returns the current maximum number of Features per page.
     *
     * @return the current maximum page size
     */
    int getMaxPageSize();

    /**
     * Sets the current maximum number of Features per page. As the page size
     * changes, the current page of data is reloaded.
     *
     * @param pageSize
     *            the maximum number of Feature per page
     * @throws DataException
     *             if there is an error reloading the current page
     */
    void setMaxPageSize(int pageSize) throws DataException;

    /**
     * Returns the number of pages available in the collection, calculated with
     * the total number of elements and the maximum number of elements per page.
     *
     * @return the number of pages available
     */
    long getNumPages();

    /**
     * Returns the number of the currently loaded page of Features (zero based).
     *
     * @return the current page number
     */
    long getCurrentPage();

    /**
     * Sets the current page number, and loads the Features for that page.
     *
     * @param page
     *            the page to load
     * @throws DataException
     *             if there is an error setting the current page
     */
    void setCurrentPage(long page) throws DataException;

    /**
     * Returns the number of elements of the entire Collection.
     *
     * @return the number of elements
     * @throws DataException
     *             if there is an error getting the total number of Features
     */
    long getTotalSize() throws DataException;

    /**
     * Returns the Feature located at the index position.
     *
     * @param index
     *            to locate the Feature in the Collection
     * @return the Feature located at the index position
     * @throws DataException
     *             if there is an error getting the Feature
     */
    Feature getFeatureAt(long index) throws DataException;

    /**
     * Returns all the values of the current loaded page.
     *
     * @return all the values of the current loaded page
     */
    Feature[] getCurrentPageFeatures();

    /**
     * Returns the FeatureSet used to fetch the data.
     *
     * @return the FeatureSet
     */
    FeatureSet getFeatureSet();

    /**
     * Returns the FeatureStore used to fetch the data.
     *
     * @return the FeatureStore
     */
    FeatureStore getFeatureStore();

    /**
     * Returns the query used to load the Features.
     *
     * @return the query used to load the Features
     */
    FeatureQuery getFeatureQuery();

	/**
	 * Returns the FeatureType used of the data.
	 *
	 * @return the FeatureType of the data
	 */
	FeatureType getFeatureType();

	/**
	 * Reloads the current page of data from the collection.
	 *
	 * @throws DataException
	 *             if there is an error reloading the current page
	 */
    void reloadCurrentPage() throws DataException;

	/**
	 * Reloads everything, using the current query.
	 *
	 * @throws DataException
	 */
    void reload() throws DataException;

	/**
	 * Updates a {@link Feature} with the given {@link EditableFeature} in the
	 * current {@link FeatureSet}. <br>
	 *
	 * @param feature
	 *            an instance of {@link EditableFeature} with which to update
	 *            the associated {@link Feature}.
	 *
	 * @throws DataException
	 *
	 * @see {@link FeatureSet#update(EditableFeature)}
	 *      {@link FeaturePagingHelper#getFeatureSet()}
	 */
	public void update(EditableFeature feature) throws DataException;

	/**
	 * Deletes a {@link Feature} from current {@link FeatureSet}.<br>
	 *
	 * @param feature
	 *            the {@link Feature} to delete.
	 *
	 * @throws DataException
	 *
	 * @see {@link FeatureSet#delete(Feature)}
	 *      {@link FeaturePagingHelper#getFeatureSet()}
	 */
	public void delete(Feature feature) throws DataException;

	/**
	 * Inserts a new feature in this {@link FeatureSet}. It needs to be an
	 * instance of {@link EditableFeature} as it has not been stored yet.<br>
	 *
	 * Any {@link Iterator} from this store that was still in use can will not
	 * reflect this change.
	 *
	 * @param feature
	 *            the {@link EditableFeature} to insert.
	 *
	 * @throws DataException
	 *
	 * @see {@link FeatureSet#insert(EditableFeature)}
	 *      {@link FeaturePagingHelper#getFeatureSet()}
	 */
	public void insert(EditableFeature feature) throws DataException;


}
