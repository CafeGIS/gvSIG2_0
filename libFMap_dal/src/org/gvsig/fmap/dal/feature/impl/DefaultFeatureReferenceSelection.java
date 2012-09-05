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
 * 2008 {DiSiD Technologies}  {Implement data selection}
 */
package org.gvsig.fmap.dal.feature.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.gvsig.fmap.dal.DataStore;
import org.gvsig.fmap.dal.DataStoreNotification;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.FeatureReference;
import org.gvsig.fmap.dal.feature.FeatureReferenceSelection;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureStoreNotification;
import org.gvsig.fmap.dal.feature.impl.undo.FeatureCommandsStack;
import org.gvsig.tools.exception.BaseException;
import org.gvsig.tools.observer.Observable;
import org.gvsig.tools.observer.impl.BaseWeakReferencingObservable;
import org.gvsig.tools.persistence.PersistenceException;
import org.gvsig.tools.persistence.PersistentState;
import org.gvsig.tools.visitor.Visitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of a FeatureReferenceSelection, based on the usage of
 * a java.util.Set to store individual selected or not selected
 * FeatureReferences, depending on the usage of the {@link #reverse()} method.
 *
 * @author <a href="mailto:cordin@disid.com">Cèsar Ordiñana</a>
 */
public class DefaultFeatureReferenceSelection extends
        BaseWeakReferencingObservable implements FeatureReferenceSelection {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(DefaultFeatureReferenceSelection.class);

    protected SelectionData selectionData = new SelectionData();

    private FeatureStore featureStore;

    private FeatureSelectionHelper helper;

	/**
	 * Creates a new Selection with the total size of Features from which the
	 * selection will be performed.
	 *
	 * @param featureStore
	 *            the FeatureStore of the selected FeatureReferences
	 * @throws DataException
	 *             if there is an error while getting the total number of
	 *             Features of the Store.
	 */
    public DefaultFeatureReferenceSelection(DefaultFeatureStore featureStore)
            throws DataException {
        super();
        this.featureStore = featureStore;
        this.helper = new DefaultFeatureSelectionHelper(featureStore);
        selectionData.setTotalSize(featureStore.getFeatureCount());
    }

    /**
     * Creates a new Selection with the total size of Features from which the
     * selection will be performed.
     *
     * @param featureStore
     *            the FeatureStore of the selected FeatureReferences
     * @param helper
     *            to get some information of the Store
     * @throws DataException
     *             if there is an error while getting the total number of
     *             Features of the Store.
     */
    public DefaultFeatureReferenceSelection(FeatureStore featureStore,
            FeatureSelectionHelper helper)
            throws DataException {
        super();
        this.featureStore = featureStore;
        this.helper = helper;
        selectionData.setTotalSize(featureStore.getFeatureCount());
    }

    public boolean select(FeatureReference reference) {
        return select(reference, true);
    }

    /**
     * @see #select(FeatureReference)
     * @param undoable
     *            if the action must be undoable
     */
    public boolean select(FeatureReference reference, boolean undoable) {
        if (isSelected(reference)) {
            return false;
        }

        if (undoable && getFeatureStore().isEditing()) {
            getCommands().select(this, reference);
        }
        boolean change = false;
        if (selectionData.isReversed()) {
            change = selectionData.remove(reference);
        } else {
            change = selectionData.add(reference);
        }

        if (change) {
            notifyObservers(DataStoreNotification.SELECTION_CHANGE);
        }

        return change;
    }

    public boolean deselect(FeatureReference reference) {
        return deselect(reference, true);
    }

    /**
     * @see #deselect(FeatureReference)
     * @param undoable
     *            if the action must be undoable
     */
    public boolean deselect(FeatureReference reference, boolean undoable) {
        if (!isSelected(reference)) {
            return false;
        }

        if (undoable && getFeatureStore().isEditing()) {
            getCommands().deselect(this, reference);
        }
        boolean change = false;
        if (selectionData.isReversed()) {
            change = selectionData.add(reference);
        } else {
            change = selectionData.remove(reference);
        }

        if (change) {
            notifyObservers(DataStoreNotification.SELECTION_CHANGE);
        }

        return change;
    }

    public void selectAll() throws DataException {
        selectAll(true);
    }

    /**
     * @see #selectAll()
     * @param undoable
     *            if the action must be undoable
     */
    public void selectAll(boolean undoable) throws DataException {
        if (undoable && getFeatureStore().isEditing()) {
            getCommands().startComplex("_selectionSelectAll");
            getCommands().selectAll(this);
        }
        if (!selectionData.isReversed()) {
            selectionData.setReversed(true);
        }
        clearFeatureReferences();
        if (undoable && getFeatureStore().isEditing()) {
            getCommands().endComplex();
        }
        notifyObservers(DataStoreNotification.SELECTION_CHANGE);
    }

    public void deselectAll() throws DataException {
        deselectAll(true);
    }

    /**
     * @see #deselectAll()
     * @param undoable
     *            if the action must be undoable
     */
    public void deselectAll(boolean undoable) throws DataException {
        if (undoable && getFeatureStore().isEditing()) {
            getCommands().startComplex("_selectionDeselectAll");
            getCommands().deselectAll(this);
        }
        if (selectionData.isReversed()) {
            selectionData.setReversed(false);
        }
        clearFeatureReferences();
        if (undoable && getFeatureStore().isEditing()) {
            getCommands().endComplex();
        }

        notifyObservers(DataStoreNotification.SELECTION_CHANGE);
    }

    public boolean isSelected(FeatureReference reference) {
        if (selectionData.isReversed()) {
            return !selectionData.contains(reference);
        } else {
            return selectionData.contains(reference);
        }
    }

    public void reverse() {
        reverse(true);
    }

    /**
     * @see #reverse()
     * @param undoable
     *            if the action must be undoable
     */
    public void reverse(boolean undoable) {
        if (undoable && getFeatureStore().isEditing()) {
            getCommands().selectionReverse(this);
        }
        selectionData.setReversed(!selectionData.isReversed());
        notifyObservers(DataStoreNotification.SELECTION_CHANGE);
    }

    public long getSelectedCount() {
        if (selectionData.isReversed()) {
                return selectionData.getTotalSize() - selectionData.getSize()
                        + helper.getFeatureStoreDeltaSize();
        } else {
            return selectionData.getSize();
        }
    }

    public Iterator referenceIterator() {
        return Collections.unmodifiableSet(selectionData.getSelected())
                .iterator();
    }

    public void dispose() {
        try {
            deselectAll();
        } catch (DataException ex) {
            LOGGER.error("Error on dispose(), deselecting all selected values",
                    ex);
        }
    }

    public boolean isFromStore(DataStore store) {
        return featureStore.equals(store);
    }

    public void accept(Visitor visitor) throws BaseException {
        for (Iterator iter = selectionData.getSelected().iterator(); iter
                .hasNext();) {
            visitor.visit(iter.next());
        }
    }

    public void update(Observable observable,
			Object notification) {
        // If a Feature is deleted, remove it from the selection Set.
        if (notification instanceof FeatureStoreNotification) {
            FeatureStoreNotification storeNotif = (FeatureStoreNotification) notification;
            if (FeatureStoreNotification.AFTER_DELETE
                    .equalsIgnoreCase(storeNotif.getType())) {
                selectionData.remove(storeNotif.getFeature().getReference());
            }
        }
    }

    public void saveToState(PersistentState state) throws PersistenceException {
        state.set("reversed", selectionData.isReversed());
        state.set("totalSize", selectionData.getTotalSize());
        state.set("selected", selectionData.getSelected().iterator());
    }

    public void loadFromState(PersistentState state) throws PersistenceException {
        selectionData.setReversed(state.getBoolean("reversed"));
        selectionData.setTotalSize(state.getLong("totalSize"));
        Iterator it = state.getIterator("selected");
        // FIXME: Esto no funcionara bien. Hay que darle una pensada mas.
        while (it.hasNext()) {
            DefaultFeatureReference ref = (DefaultFeatureReference) it.next();
            selectionData.add(ref);
        }
    }

    public SelectionData getData() {
        return selectionData;
    }

    public void setData(SelectionData selectionData) {
        this.selectionData = selectionData;
        notifyObservers(DataStoreNotification.SELECTION_CHANGE);
    }

    public String toString() {
        return getClass().getName() + ": " + getSelectedCount()
                + " features selected, reversed = "
                + selectionData.isReversed() + ", featureIds contained: "
                + selectionData.getSelected();
    }

    protected boolean isReversed() {
        return selectionData.isReversed();
    }

    /**
     * Removes all the stored FeatureRefence objects.
     */
    protected void clearFeatureReferences() {
        selectionData.clear();
    }

	/**
	 * Returns the FeatureStore of the selected FeatureReferences.
	 *
	 * @return the featureStore
	 */
    protected FeatureStore getFeatureStore() {
        return featureStore;
    }

	/**
	 * Returns the reference to the commands record.
	 *
	 * @return the reference to the commands record
	 */
    protected FeatureCommandsStack getCommands() {
        return helper.getFeatureStoreCommandsStack();
    }

    public static class SelectionData {
        private Set selected = new HashSet();

        /**
         * Sets how the Set of selected values has to be dealt.
         * <p>
         * If selected is FALSE, then values into the Set are the selected ones,
         * anything else is not selected.
         * </p>
         * <p>
         * If selected is TRUE, then values into the Set are values not
         * selected, anything else is selected.
         * </p>
         */
        private boolean reversed = false;

        private long totalSize;

        /**
         * @return the selected
         */
        public Set getSelected() {
            return selected;
        }

        /**
         * @param selected
         *            the selected to set
         */
        public void setSelected(Set selected) {
            this.selected = selected;
        }

        /**
         * @return the reversed
         */
        public boolean isReversed() {
            return reversed;
        }

        /**
         * @param reversed
         *            the reversed to set
         */
        public void setReversed(boolean reversed) {
            this.reversed = reversed;
        }

        /**
         * @return the totalSize
         */
        public long getTotalSize() {
            return totalSize;
        }

        /**
         * @param totalSize
         *            the totalSize to set
         */
        public void setTotalSize(long totalSize) {
            this.totalSize = totalSize;
        }

        public boolean add(FeatureReference reference) {
            return selected.add(reference);
        }

        public boolean remove(FeatureReference reference) {
            return selected.remove(reference);
        }

        public void clear() {
            selected.clear();
        }

        public boolean contains(FeatureReference reference) {
            return selected.contains(reference);
        }

        public int getSize() {
            return selected.size();
        }

        public Object clone() throws CloneNotSupportedException {
            SelectionData clone = new SelectionData();
            clone.setReversed(isReversed());
            clone.setTotalSize(getTotalSize());
            clone.setSelected(new HashSet(getSelected()));
            return clone;
        }
    }
}