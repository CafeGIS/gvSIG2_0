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
 * 2008 {DiSiD Technologies}  {Create a JTable TableModel for a FeatureCollection}
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class to access the values of a FeatureCollection by position. Handles
 * pagination automatically to avoid filling the memory in case of big
 * collections.
 *
 * TODO: evaluate if its more convenient to read values in the background when
 * the returned value is near the end of the page, instead of loading a page on
 * demand.
 *
 * @author <a href="mailto:cordin@disid.com">Cèsar Ordiñana</a>
 */
public class FeaturePagingHelperImpl implements FeaturePagingHelper {

    final Logger logger = LoggerFactory
            .getLogger(FeaturePagingHelperImpl.class);

    private FeatureQuery query;

    private FeatureSet featureSet;

    private FeatureStore featureStore;

    private int maxPageSize;

    private long numPages;

    private long currentPage = -1;

    private Feature[] values;

    /**
     * Constructs a FeaturePagingHelperImpl from data of a FeatureStore.
     *
     * @param featureStore
     *            to extract data from
     * @throws DataException
     *             if there is an error initializing the helper
     */
    public FeaturePagingHelperImpl(FeatureStore featureStore)
            throws DataException {
        this(featureStore, DEFAULT_PAGE_SIZE);
    }

    /**
     * Constructs a FeaturePagingHelperImpl from data of a FeatureStore.
     *
     * @param featureSet
     *            to extract data from
     * @param pageSize
     *            the number of elements per page data
     * @throws DataException
     *             if there is an error initializing the helper
     */
    public FeaturePagingHelperImpl(FeatureStore featureStore, int pageSize)
            throws DataException {
        this(featureStore, null, pageSize);
    }

    /**
     * Constructs a FeaturePagingHelperImpl from data of a FeatureStore.
     *
     * @param featureSet
     *            to extract data from
     * @throws DataException
     *             if there is an error initializing the helper
     */
    public FeaturePagingHelperImpl(FeatureStore featureStore,
            FeatureQuery featureQuery) throws DataException {
        this(featureStore, featureQuery, DEFAULT_PAGE_SIZE);
    }

    /**
     * Constructs a FeaturePagingHelperImpl from data of a FeatureStore.
     *
     * @param featureSet
     *            to extract data from
     * @param pageSize
     *            the number of elements per page data
     * @throws DataException
     *             if there is an error initializing the helper
     */
    public FeaturePagingHelperImpl(FeatureStore featureStore,
            FeatureQuery featureQuery, int pageSize) throws DataException {
    	if (featureQuery == null) {
			featureQuery = featureStore.createFeatureQuery();
			featureQuery.setFeatureType(featureStore.getDefaultFeatureType());
		}
        this.maxPageSize = pageSize;
        this.featureStore = featureStore;
        this.query = featureQuery;
        loadFeatureCollection();
        setNumPages(calculateNumPages());

        if (logger.isDebugEnabled()) {
            logger.debug("FeaturePagingHelperImpl created with {} pages, "
                    + "and a page size of {}", new Long(numPages), new Integer(
                    pageSize));
        }
    }

    public int getMaxPageSize() {
        return maxPageSize;
    }

    public void setMaxPageSize(int pageSize) throws DataException {
        this.maxPageSize = pageSize;
        reloadCurrentPage();
    }

    public long getNumPages() {
        return numPages;
    }

    /**
     * Sets the number of pages.
     */
    private void setNumPages(long numPages) {
        this.numPages = numPages;
    }

    public long getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(long page) throws DataException {

        if (logger.isDebugEnabled()) {
            logger.debug("Current page: {}", Long.toString(page));
        }

        if (page < 0) {
            throw new IndexOutOfBoundsException(
                    "Error, unable to set helper current page to a "
                            + "negative value: " + page);
        }
        if (page >= getNumPages()) {
            throw new IndexOutOfBoundsException(
                    "Error, unable to set helper current page to the page num. "
                            + page + ", as the Collection only has "
                            + getNumPages() + " pages of Features");
        }
        currentPage = page;
        loadCurrentPageData();
    }

    public long getTotalSize() throws DataException {
        return getFeatureSet().getSize();
    }

    public Feature getFeatureAt(long index) throws DataException {
        // Check if we have currently loaded the viewed page data,
        // or we need to load a new one
        long pageForIndex = (long) Math.floor(index / getMaxPageSize());

        if (pageForIndex != getCurrentPage()) {
            setCurrentPage(pageForIndex);
        }

        long positionForIndex = index
                - (getCurrentPage() * getMaxPageSize());

        return values[(int) positionForIndex];
    }

    public Feature[] getCurrentPageFeatures() {
        return values;
    }

    public FeatureSet getFeatureSet() {
        return featureSet;
    }

    public void reloadCurrentPage() throws DataException {
        setNumPages(calculateNumPages());
        if (currentPage > -1) {
            loadCurrentPageData();
        }
    }

    public void reload() throws DataException {
        loadFeatureCollection();
        reloadCurrentPage();
    }

    public FeatureStore getFeatureStore() {
        return featureStore;
    }

    public FeatureQuery getFeatureQuery() {
        return query;
    }

    /**
     * Calculates the number of pages.
     */
    private long calculateNumPages() throws DataException {
        return ((long) Math.floor(getTotalSize() / getMaxPageSize())) + 1;
    }

    /**
     * Loads all the Features of the current page.
     */
    private void loadCurrentPageData() throws DataException {
        long currentPage = getCurrentPage();
        int currentPageSize;
        if (currentPage < (numPages - 1)) {
            currentPageSize = getMaxPageSize();
        } else {
            currentPageSize = (int) (getTotalSize() - (currentPage * getMaxPageSize()));
        }

        values = new Feature[currentPageSize];

        long firstPosition = currentPage * getMaxPageSize();

        if (logger.isDebugEnabled()) {
            logger.debug("Loading {} Features starting at position {}",
                    new Integer(currentPageSize), new Long(firstPosition));
        }

        Iterator iter = getFeatureSet().iterator(firstPosition);
        int i = 0;
        while (iter.hasNext() && i < currentPageSize) {
            values[i] = (Feature) iter.next();
            i++;
        }
    }

    private void loadFeatureCollection() throws DataException {
        if (featureSet != null) {
            featureSet.dispose();
        }
        featureSet = getFeatureStore().getFeatureSet(query);
    }

	public void delete(Feature feature) throws DataException {
		if(featureSet == null){
			// FIXME change to RuntimeDalException
			throw new IllegalStateException();
		}
		featureSet.delete(feature);
		reloadCurrentPage();
	}

	public void insert(EditableFeature feature) throws DataException {
		if (featureSet == null) {
			// FIXME change to RuntimeDalException
			throw new IllegalStateException();
		}
		featureSet.insert(feature);
		reloadCurrentPage();
	}

	public void update(EditableFeature feature) throws DataException {
		if (featureSet == null) {
			// FIXME change to RuntimeDalException
			throw new IllegalStateException();
		}
		featureSet.update(feature);
		reloadCurrentPage();
	}

	public FeatureType getFeatureType() {
		return featureSet.getDefaultFeatureType();
	}
}