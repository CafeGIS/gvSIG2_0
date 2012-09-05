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
 * 2008 {DiSiD Technologies}   {{Test indexed iterator retrieval}}
 */
package org.gvsig.fmap.dal.feature;

import junit.framework.TestCase;

import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.exception.DataException;

/**
 * Unit tests for the {@link AbstractFeatureCollection} class.
 *
 * @author <a href="mailto:cordin@disid.com">Cèsar Ordiñana</a>
 */
public abstract class FeatureSetTestParent extends TestCase {


    protected void setUp() throws Exception {
        super.setUp();
        register();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test method for
     * {@link org.gvsig.fmap.dal.feature.AbstractFeatureCollection#iterator(int)}
     * .
     *
     * @throws Exception
     */
    public void testIteratorInt() throws Exception {
        FeatureSet featureSet = createFeatureCollection();
        testIteratorInt(featureSet);
        featureSet.dispose();
    }

    public void testIteratorInt(FeatureSet featureSet) throws DataException {

        if (featureSet.getSize() < 3) {
            fail("The Collection to create for the test must contain "
                    + "at least 3 values");
        }

        try {
            featureSet.iterator(-5);
            fail("Iterator index accepted with a value < 0");
        } catch (Exception ex) {
            // Good
        }

        try {
            featureSet.iterator(featureSet.getSize() + 2);
            fail("Iterator index accepted with a value > collection size");
        } catch (Exception ex) {
            // Good
        }

        long index = featureSet.getSize() - 3l;
        DisposableIterator iter = featureSet.iterator(index);
        int iterations = 0;
        while (iter.hasNext()) {
            iter.next();
            iterations++;
        }
        assertEquals("The number of iterations remaining is not correct", 3,
                iterations);
        iter.dispose();

        iter = featureSet.iterator(0);
        iterations = 0;
        while (iter.hasNext()) {
            iter.next();
            iterations++;
        }
        assertEquals("The number of iterations is not the "
                + "total size of the collection", featureSet.getSize(),
                iterations);
        iter.dispose();
    }

    protected FeatureSet createFeatureCollection() throws Exception {
        FeatureStore store = createFeatureStore();
        FeatureType ft = store.getDefaultFeatureType();
        return createFeatureCollection(store, ft);
    }

    protected FeatureStore createFeatureStore() throws Exception {
        DataManager manager = DALLocator.getDataManager();
        return (FeatureStore) manager
                .createStore(createStoreParameters(manager));
    }

    protected abstract void register();

    protected abstract DataStoreParameters createStoreParameters(
            DataManager manager)
            throws Exception;

    /**
     * If this method is rewritten, the returned FeatureCollection must have at
     * least 3 values.
     */
    protected abstract FeatureSet createFeatureCollection(
            FeatureStore store,
            FeatureType ft) throws DataException;
}