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
package org.gvsig.fmap.dal.feature.impl;

import junit.framework.TestCase;

import org.easymock.MockControl;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureReference;
import org.gvsig.fmap.dal.feature.FeatureSelection;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureType;

/**
 * @author <a href="mailto:cordin@disid.com">Cèsar Ordiñana</a>
 */
public class DefaultFeatureSelectionTest extends TestCase {
    private FeatureSelection selection;
    private MockControl refControl1;
    private FeatureReference ref1;
    private MockControl refControl2;
    private FeatureReference ref2;
    private MockControl fControl1;
    private Feature feature1;
    private MockControl fControl2;
    private Feature feature2;
    private MockControl storeControl;
    private FeatureType featureType;
    private MockControl fTypeControl;
    private FeatureStore store;
    private MockControl fsetControl;
    private FeatureSet featureSet;
    private MockControl helperControl;
    private FeatureSelectionHelper helper;

    private int total = 10;

    protected void setUp() throws Exception {
        super.setUp();
        refControl1 = MockControl.createNiceControl(FeatureReference.class);
        ref1 = (FeatureReference) refControl1.getMock();
        refControl2 = MockControl.createNiceControl(FeatureReference.class);
        ref2 = (FeatureReference) refControl2.getMock();

        fControl1 = MockControl.createControl(Feature.class);
        feature1 = (Feature) fControl1.getMock();
        fControl2 = MockControl.createControl(Feature.class);
        feature2 = (Feature) fControl2.getMock();

        fTypeControl = MockControl.createNiceControl(FeatureType.class);
        featureType = (FeatureType) fTypeControl.getMock();

        fControl1.expectAndDefaultReturn(feature1.getType(), featureType);
        fControl2.expectAndDefaultReturn(feature2.getType(), featureType);

        storeControl = MockControl.createNiceControl(FeatureStore.class);
        store = (FeatureStore) storeControl.getMock();
        storeControl.expectAndReturn(store.getFeatureCount(), total);

        helperControl = MockControl
                .createNiceControl(FeatureSelectionHelper.class);
        helper = (FeatureSelectionHelper) helperControl.getMock();
        helperControl.expectAndDefaultReturn(helper.getFeatureStoreDeltaSize(),
                0);

        fsetControl = MockControl.createNiceControl(FeatureSet.class);
        featureSet = (FeatureSet) fsetControl.getMock();

        storeControl.expectAndReturn(store.getFeatureSet(), featureSet);
        fsetControl.expectAndReturn(featureSet.getSize(), total);

        storeControl.replay();
        fsetControl.replay();
        helperControl.replay();

        selection = new DefaultFeatureSelection(store, helper);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test method for
     * {@link org.gvsig.fmap.dal.feature.impl.DefaultFeatureSelection#select(org.gvsig.fmap.dal.feature.Feature)}
     * .
     */
    public void testSelectFeature() {
        fControl1.expectAndReturn(feature1.getReference(), ref1, 2);
        fControl2.expectAndReturn(feature2.getReference(), ref2, 3);
        fControl1.replay();
        fControl2.replay();

        // Add a feature
        selection.select(feature1);
        assertTrue("Selected feature1 is not recognized as selected", selection
                .isSelected(feature1));
        assertFalse("Selected feature2 is recognized as selected", selection
                .isSelected(feature2));

        selection.select(feature2);
        assertTrue(selection.isSelected(feature2));
    }

    /**
     * Test method for
     * {@link org.gvsig.fmap.dal.feature.impl.DefaultFeatureSelection#deselect(org.gvsig.fmap.dal.feature.Feature)}
     * .
     */
    public void testDeselectFeature() {
        fControl1.expectAndReturn(feature1.getReference(), ref1, 3);
        fControl2.expectAndReturn(feature2.getReference(), ref2, 4);
        fControl1.replay();
        fControl2.replay();

        // Remove a feature
        selection.select(feature1);
        selection.select(feature2);
        assertTrue(selection.isSelected(feature2));
        selection.deselect(feature2);
        assertFalse(selection.isSelected(feature2));
        selection.deselect(feature1);
        assertFalse(selection.isSelected(feature1));
    }

    /**
     * Test method for
     * {@link org.gvsig.fmap.dal.feature.impl.DefaultFeatureSelection#getSize()}
     * .
     */
    public void testGetSize() throws Exception {
        fControl1.expectAndReturn(feature1.getReference(), ref1, 1);
        fControl2.expectAndReturn(feature2.getReference(), ref2, 4);
        fControl1.replay();
        fControl2.replay();

        // None selected
        assertEquals(0, selection.getSize());
        selection.select(feature1);
        selection.select(feature2);
        // Two selected
        assertEquals(2, selection.getSize());
        // Deselect one, so one left selected
        selection.deselect(feature2);
        assertEquals(1, selection.getSize());
        // Reverse the selection, so all buy one selected
        selection.reverse();
        assertEquals(total - 1, selection.getSize());
        // Deselect another one, so all but two selected
        selection.deselect(feature2);
        assertEquals(total - 2, selection.getSize());
        // Deselect an already deselected one, nothing changes
        selection.deselect(feature2);
        assertEquals(total - 2, selection.getSize());
    }

    /**
     * Test method for
     * {@link org.gvsig.fmap.dal.feature.impl.DefaultFeatureSelection#isEmpty()}
     * .
     */
    public void testIsEmpty() throws Exception {
        fControl1.expectAndReturn(feature1.getReference(), ref1, 1);
        fControl2.expectAndReturn(feature2.getReference(), ref2, 4);
        fControl1.replay();
        fControl2.replay();

        // None selected
        assertTrue(selection.isEmpty());

        // One selected
        selection.select(feature1);
        assertFalse(selection.isEmpty());

        // Deselect all
        selection.deselectAll();
        assertTrue(selection.isEmpty());

        // Reverse
        selection.reverse();
        assertFalse(selection.isEmpty());
    }

}