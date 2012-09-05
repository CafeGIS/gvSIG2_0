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
 * 2008 {DiSiD Technologies}  {{Task}}
 */
package org.gvsig.fmap.dal.feature.paging;

import junit.framework.TestCase;

import org.easymock.MockControl;
import org.gvsig.fmap.dal.feature.*;

/**
 * @author <a href="mailto:cordin@disid.com">Cèsar Ordiñana</a>
 */
public class FeaturePagingHelperTest extends TestCase {

    private FeaturePagingHelper helper;
    private MockControl fsetControl;
    private FeatureSet fsetMock;
    private MockControl storeControl;
    private FeatureStore storeMock;
    private MockControl queryControl;
    private FeatureQuery queryMock;
    private FeatureType featureTypeMock;
    private MockControl fTypeControl;

    protected void setUp() throws Exception {
        super.setUp();
        fsetControl = MockControl.createNiceControl(FeatureSet.class);
        fsetMock = (FeatureSet) fsetControl.getMock();
        storeControl = MockControl.createNiceControl(FeatureStore.class);
        storeMock = (FeatureStore) storeControl.getMock();
        queryControl = MockControl.createNiceControl(FeatureQuery.class);
        queryMock = (FeatureQuery) queryControl.getMock();
        fTypeControl = MockControl.createNiceControl(FeatureType.class);
        featureTypeMock = (FeatureType) fTypeControl.getMock();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test method for
     * {@link org.gvsig.fmap.dal.feature.paging.FeaturePagingHelper#getMaxPageSize()}
     */
    public void testGetMaxPageSize() throws Exception {
        storeControl.expectAndReturn(storeMock.getDefaultFeatureType(),
                featureTypeMock);
        storeControl.expectAndReturn(storeMock.createFeatureQuery(), queryMock,
                2);
        storeControl.expectAndReturn(storeMock.getFeatureSet(queryMock),
                fsetMock, 2);
        fsetControl.expectAndReturn(fsetMock.getSize(), 10, 2);
        storeControl.replay();
        fsetControl.replay();

        helper = new FeaturePagingHelperImpl(storeMock);
        assertEquals(
                "El tamaño máximo de página no es el establecido en el helper",
                FeaturePagingHelper.DEFAULT_PAGE_SIZE, helper.getMaxPageSize());

        helper = new FeaturePagingHelperImpl(storeMock, 5);
        assertEquals(
                "El tamaño máximo de página no es el establecido en el helper",
                5, helper.getMaxPageSize());

        storeControl.verify();
        fsetControl.verify();
    }

    /**
     * Test method for
     * {@link org.gvsig.fmap.dal.feature.paging.FeaturePagingHelper#getNumPages()}
     * .
     */
    public void testGetNumPages() throws Exception {
        storeControl.expectAndReturn(storeMock.createFeatureQuery(), queryMock);
        storeControl.expectAndReturn(storeMock.getFeatureSet(queryMock),
                fsetMock);
        fsetControl.expectAndReturn(fsetMock.getSize(), 10);
        storeControl.replay();
        fsetControl.replay();

        helper = new FeaturePagingHelperImpl(storeMock);

        assertEquals("El nº de páginas calculadas no es correcto", 1, helper
                .getNumPages());

        storeControl.verify();
        fsetControl.verify();
    }

    /**
     * Test method for
     * {@link org.gvsig.fmap.dal.feature.paging.FeaturePagingHelper#getTotalSize()}
     * .
     */
    public void testGetTotalSize() throws Exception {
        storeControl.expectAndReturn(storeMock.createFeatureQuery(), queryMock);
        storeControl.expectAndReturn(storeMock.getFeatureSet(queryMock),
                fsetMock);
        fsetControl.expectAndReturn(fsetMock.getSize(), 10, 2);
        storeControl.replay();
        fsetControl.replay();

        helper = new FeaturePagingHelperImpl(storeMock);

        assertEquals("El nº total de features no es el de la Colección", 10,
                helper.getTotalSize());

        storeControl.verify();
        fsetControl.verify();
    }

    /**
     * Test method for
     * {@link org.gvsig.fmap.dal.feature.paging.FeaturePagingHelper#getFeatureAt(int)}
     * .
     */
    public void testGetFeatureAt() throws Exception {
        // Create two mock features for the test.
        MockControl featureControl = MockControl.createControl(Feature.class);
        Feature mockFeature1 = (Feature) featureControl.getMock();
        Feature mockFeature2 = (Feature) featureControl.getMock();

        // Create a mock Iterator to return the Features.
        MockControl iteratorControl = MockControl
                .createControl(DisposableIterator.class);
        DisposableIterator mockIterator = (DisposableIterator) iteratorControl
                .getMock();

        // Values 0 and 1
        mockIterator.hasNext();
        iteratorControl.setReturnValue(true);
        mockIterator.next();
        iteratorControl.setReturnValue(mockFeature1);
        mockIterator.hasNext();
        iteratorControl.setReturnValue(true);
        mockIterator.next();
        iteratorControl.setReturnValue(mockFeature2);
        mockIterator.hasNext();
        iteratorControl.setReturnValue(false);

        // Values 4 and 5
        mockIterator.hasNext();
        iteratorControl.setReturnValue(true);
        mockIterator.next();
        iteratorControl.setReturnValue(mockFeature2);
        mockIterator.hasNext();
        iteratorControl.setReturnValue(true);
        mockIterator.next();
        iteratorControl.setReturnValue(mockFeature1);
        mockIterator.hasNext();
        iteratorControl.setReturnValue(false);

        iteratorControl.replay();

        // Define the mock Collection operations.
        storeControl.expectAndReturn(storeMock.createFeatureQuery(), queryMock);
        storeControl.expectAndReturn(storeMock.getFeatureSet(queryMock),
                fsetMock);

        fsetControl.expectAndReturn(fsetMock.getSize(), 11);
        fsetControl.expectAndReturn(fsetMock.iterator(0), mockIterator);
        fsetControl.expectAndReturn(fsetMock.iterator(4), mockIterator);
        storeControl.replay();
        fsetControl.replay();

        helper = new FeaturePagingHelperImpl(storeMock, 2);

        // Check the returned Features are the correct ones
        Feature feature = helper.getFeatureAt(0);
        assertEquals("La Feature devuelta no es la que corresponde "
                + "al índice solicitado: 0", mockFeature1, feature);

        feature = helper.getFeatureAt(1);
        assertEquals("La Feature devuelta no es la que corresponde "
                + "al índice solicitado: 1", mockFeature2, feature);

        feature = helper.getFeatureAt(5);
        assertEquals("La Feature devuelta no es la que corresponde "
                + "al índice solicitado: 5", mockFeature1, feature);

        try {
            feature = helper.getFeatureAt(9999);
            fail("Returned a value for a non existent Feature position");
        } catch (Exception ex) {
            // Good
        }

        storeControl.verify();
        fsetControl.verify();
        iteratorControl.verify();
    }

    /**
     * Test method for
     * {@link org.gvsig.fmap.dal.feature.paging.FeaturePagingHelper#setCurrentPage(int)}
     * .
     */
    public void testSetCurrentPage() throws Exception {
        storeControl.expectAndReturn(storeMock.createFeatureQuery(), queryMock);
        storeControl.expectAndReturn(storeMock.getFeatureSet(queryMock),
                fsetMock);
        fsetControl.expectAndReturn(fsetMock.getSize(), 10);
        storeControl.replay();
        fsetControl.replay();

        helper = new FeaturePagingHelperImpl(storeMock);

        try {
            helper.setCurrentPage(-4);
            fail("Allowed to set a negative number as the current page");
        } catch (Exception ex) {
            // Good
        }

        try {
            helper.setCurrentPage(99999);
            fail("Allowed to set a current page greater than the number "
                    + "of available pages");
        } catch (Exception ex) {
            // Good
        }

        storeControl.verify();
        fsetControl.verify();
    }

}