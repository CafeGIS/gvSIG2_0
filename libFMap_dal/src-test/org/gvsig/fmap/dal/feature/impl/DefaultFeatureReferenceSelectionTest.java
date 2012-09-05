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

import java.util.Iterator;

import junit.framework.TestCase;

import org.easymock.MockControl;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.FeatureReference;
import org.gvsig.fmap.dal.feature.FeatureReferenceSelection;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.tools.exception.BaseException;
import org.gvsig.tools.visitor.Visitor;

/**
 * @author <a href="mailto:cordin@disid.com">Cèsar Ordiñana</a>
 */
public class DefaultFeatureReferenceSelectionTest extends TestCase {

    private FeatureReferenceSelection selection;
    private MockControl refControl1;
    private FeatureReference ref1;
    private MockControl refControl2;
    private FeatureReference ref2;
    private MockControl storeControl;
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

        selection = new DefaultFeatureReferenceSelection(store, helper);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testSelect() throws Exception {
        // Add a feature
        selection.select(ref1);
        assertTrue(selection.isSelected(ref1));
        assertFalse(selection.isSelected(ref2));

        selection.select(ref2);
        assertTrue(selection.isSelected(ref2));
    }

    public void testSelectAll() throws Exception {
        selection.selectAll();
        assertTrue(selection.isSelected(ref1));
        assertTrue(selection.isSelected(ref2));
    }

    public void testDeselect() throws Exception {
        // Remove a feature
        selection.select(ref1);
        selection.select(ref2);
        assertTrue(selection.isSelected(ref2));
        selection.deselect(ref2);
        assertFalse(selection.isSelected(ref2));
        selection.deselect(ref1);
        assertFalse(selection.isSelected(ref1));
    }

    public void testDeselectAll() throws Exception {
        selection.select(ref1);
        selection.select(ref2);
        selection.deselectAll();
        assertFalse(selection.isSelected(ref1));
        assertFalse(selection.isSelected(ref2));
    }

    public void testReverse() throws Exception {
        // Reverse selection
        selection.select(ref1);
        selection.reverse();
        assertFalse(selection.isSelected(ref1));
        assertTrue(selection.isSelected(ref2));
    }

    public void testGetSelectedCount() throws Exception {
        // None selected
        assertEquals(0, selection.getSelectedCount());
        selection.select(ref1);
        selection.select(ref2);
        // Two selected
        assertEquals(2, selection.getSelectedCount());
        // Deselect one, so one left selected
        selection.deselect(ref2);
        assertEquals(1, selection.getSelectedCount());
        // Reverse the selection, so all buy one selected
        selection.reverse();
        assertEquals(total - 1, selection.getSelectedCount());
        // Deselect another one, so all but two selected
        selection.deselect(ref2);
        assertEquals(total - 2, selection.getSelectedCount());
        // Deselect an already deselected one, nothing changes
        selection.deselect(ref2);
        assertEquals(total - 2, selection.getSelectedCount());
    }

    public void testGetSelectedValues() throws DataException {
        selection.deselectAll();
        selection.select(ref1);
        selection.select(ref2);

        boolean ref1Pending = true;
        boolean ref2Pending = true;

        for (Iterator references = selection.referenceIterator(); references
                .hasNext();) {
            FeatureReference reference = (FeatureReference) references.next();
            if (reference.equals(ref1)) {
                if (ref1Pending) {
                    ref1Pending = false;
                } else {
                    fail("FeatureReference 1 was already obtained");
                }
            } else if (reference.equals(ref2)) {
                if (ref2Pending) {
                    ref2Pending = false;
                } else {
                    fail("FeatureReference 2 was already obtained");
                }
            } else {
                fail("A reference not selected was obtained: " + reference);
            }
        }
    }

    public void testDispose() {
        selection.select(ref1);
        selection.select(ref2);
        selection.dispose();
        assertFalse(selection.isSelected(ref1));
        assertFalse(selection.isSelected(ref2));
    }

    public void testAccept() throws BaseException {
        // Create a Mopck Visitor and add expected usage
        MockControl visitorControl = MockControl
                .createNiceControl(Visitor.class);
        Visitor visitor = (Visitor) visitorControl.getMock();

        visitor.visit(ref1);
        visitor.visit(ref2);
        visitorControl.replay();

        // Add selected references
        selection.select(ref1);
        selection.select(ref2);

        // Use visitor
        selection.accept(visitor);

        // check correct visitor usage
        visitorControl.verify();
    }
}