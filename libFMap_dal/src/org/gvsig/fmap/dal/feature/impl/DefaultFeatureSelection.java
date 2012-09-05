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

import java.util.*;
import java.util.Map.Entry;

import org.gvsig.fmap.dal.DataStoreNotification;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.*;
import org.gvsig.fmap.dal.feature.exception.ReversedSelectionIteratorException;
import org.gvsig.tools.persistence.PersistenceException;
import org.gvsig.tools.persistence.PersistentState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of the FeatureSelection interface. Internally, only
 * FeatureReference values are stored.
 *
 * This implementation performs better if used with the selection related
 * methods: select, deselect and isSelected ones.
 *
 * @author <a href="mailto:cordin@disid.com">Cèsar Ordiñana</a>
 */
public class DefaultFeatureSelection extends DefaultFeatureReferenceSelection
        implements FeatureSelection {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(DefaultFeatureSelection.class);

    private Map featureTypeCounts = new HashMap(1);

    /**
     * Creates a DefaultFeatureSelection, with a FeatureStore.
     *
     * @param featureStore
     *            the FeatureStore to load Features from
     * @throws DataException
     *             if there is an error while getting the total number of
     *             Features of the Store.
     * @see AbstractSetBasedDataSelection#DefaultSelection(int)
     */
    public DefaultFeatureSelection(DefaultFeatureStore featureStore)
            throws DataException {
        super(featureStore);
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
    public DefaultFeatureSelection(FeatureStore featureStore,
            FeatureSelectionHelper helper) throws DataException {
        super(featureStore, helper);
    }

    public boolean select(Feature feature) {
        return select(feature, true);
    }

    /**
     * @see #select(Feature)
     * @param undoable
     *            if the action must be undoable
     */
    public boolean select(Feature feature, boolean undoable) {
        // TODO: should we check if the feature is from the same FeatureStore??
        if (feature == null) {
            return false;
        }

//        LOGGER.debug("Selected feature: {}", feature);

        if (isReversed()) {
            getFeatureTypeCount(feature.getType()).remove();
        } else {
            getFeatureTypeCount(feature.getType()).add();
        }
        return select(feature.getReference(), undoable);
    }

    public boolean select(FeatureSet features) throws DataException {
        return select(features, true);
    }

    /**
     * @see #select(FeatureSet)
     * @param undoable
     *            if the action must be undoable
     */
    public boolean select(FeatureSet features, boolean undoable)
            throws DataException {
        boolean change = false;
        boolean inComplex = false;
        if (undoable && getFeatureStore().isEditing()
				&& !getCommands().inComplex()) {
			inComplex = getCommands().inComplex();
			getCommands().startComplex("_selectionSelectFeatureSet");
        }

        disableNotifications();
        for (DisposableIterator iter = features.fastIterator(); iter.hasNext();) {
            change |= select((Feature) iter.next(), undoable);
        }
        enableNotifications();
        if (undoable && getFeatureStore().isEditing() && !inComplex) {
            getCommands().endComplex();
        }
        if (change) {
            notifyObservers(DataStoreNotification.SELECTION_CHANGE);
        }
        return change;
    }

    public boolean deselect(Feature feature) {
        return deselect(feature, true);
    }

    /**
     * @see #deselect(Feature)
     * @param undoable
     *            if the action must be undoable
     */
    public boolean deselect(Feature feature, boolean undoable) {
        if (feature == null) {
            return false;
        }

        LOGGER.debug("Deselected feature: {}", feature);

        if (isReversed()) {
            getFeatureTypeCount(feature.getType()).add();
        } else {
            getFeatureTypeCount(feature.getType()).remove();
        }
        return deselect(feature.getReference(), undoable);
    }

    public boolean deselect(FeatureSet features) throws DataException {
        return deselect(features, true);
    }

    /**
     * @see #deselect(FeatureSet)
     * @param undoable
     *            if the action must be undoable
     */
    public boolean deselect(FeatureSet features, boolean undoable)
            throws DataException {
        boolean change = false;
        if (undoable && getFeatureStore().isEditing()) {
            getCommands().startComplex("_selectionDeselectFeatureSet");
        }
        disableNotifications();
        for (DisposableIterator iter = features.fastIterator(); iter.hasNext();) {
            change |= deselect((Feature) iter.next(), undoable);
        }
        enableNotifications();
        if (undoable && getFeatureStore().isEditing()) {
            getCommands().endComplex();
        }
        if (change) {
            notifyObservers(DataStoreNotification.SELECTION_CHANGE);
        }
        return change;
    }

    public boolean isSelected(Feature feature) {
        if (feature == null) {
            return false;
        }
        return isSelected(feature.getReference());
    }

    public FeatureType getDefaultFeatureType() {
        try {
            return getFeatureStore().getDefaultFeatureType();
        } catch (DataException ex) {
            LOGGER.error("Error getting the default feature type "
                    + "of the FeatureStore: " + getFeatureStore(), ex);
        }
        return null;
    }

    public List getFeatureTypes() {
        // Go through the map of FeatureTypes, and return only the ones that
        // have at least a Feature.
        List types = new ArrayList();
        for (java.util.Iterator iterator = featureTypeCounts.entrySet()
				.iterator(); iterator
                .hasNext();) {
            Map.Entry entry = (Entry) iterator.next();
            FeatureType type = (FeatureType) entry.getKey();
            FeatureTypeCount count = (FeatureTypeCount) entry.getValue();

            if (count.getCount() > 0) {
                types.add(type);
            }
        }

        return types;
    }

    public long getSize() throws DataException {
        return getSelectedCount();
    }

    public boolean isEmpty() throws DataException {
        return getSelectedCount() == 0;
    }

    /**
     * Returns the list of selected values, or the deselected ones if the
     * selection has been reversed.
     */
    public DisposableIterator iterator() {
        return iterator(0);
    }

    /**
     * Returns the list of selected values, or the deselected ones if the
     * selection has been reversed.
     *
     * WARN: not very good performance implementation.
     */
    public DisposableIterator iterator(long index) {
        return iterator(index, false);
    }

    /**
     * Returns the list of selected values, or the deselected ones if the
     * selection has been reversed.
     *
     * WARN: not really a fast implementation.
     */
    public DisposableIterator fastIterator() {
        return fastIterator(0);
    }

    /**
     * Returns the list of selected values, or the deselected ones if the
     * selection has been reversed.
     *
     * WARN: not really a fast implementation.
     */
    public DisposableIterator fastIterator(long index) {
        return iterator(index, true);
    }

    public void saveToState(PersistentState state) throws PersistenceException {
        super.saveToState(state);
        for (DisposableIterator iterator = fastIterator(); iterator.hasNext();) {
            Feature feature = (Feature) iterator.next();
            getFeatureTypeCount(feature.getType()).add();
        }
    }

    protected void clearFeatureReferences() {
        super.clearFeatureReferences();
        featureTypeCounts.clear();
    }

    /**
     * Creates an iterator for the Selection.
     */
    private DisposableIterator iterator(long index, boolean fastIterator) {
        if (isReversed()) {
            DisposableIterator iter = new ReversedFeatureIteratorFacade(getData(),
                    getFeatureStore(), fastIterator);
            for (long l = 0; l < index && iter.hasNext(); l++) {
                iter.next();
            }
            return iter;

        } else {
            // TODO: maybe we could add a new referenceIterator(int index)
            // method that could be implemented in a more performant way

            java.util.Iterator iter = referenceIterator();
            for (long l = 0; l < index && iter.hasNext(); l++) {
                iter.next();
            }
            return new FeatureIteratorFacade(iter, getFeatureStore());
        }
    }

    private FeatureTypeCount getFeatureTypeCount(FeatureType featureType) {
        FeatureTypeCount count = (FeatureTypeCount) featureTypeCounts
                .get(featureType);
        if (count == null) {
            count = new FeatureTypeCount();
            featureTypeCounts.put(featureType, count);
        }
        return count;
    }

    /**
     * Facade over a Iterator of FeatureReferences, to return Features instead.
     *
     * @author <a href="mailto:cordin@disid.com">Cèsar Ordiñana</a>
     */
    private class FeatureIteratorFacade implements DisposableIterator {

        final Logger logger = LoggerFactory
                .getLogger(FeatureIteratorFacade.class);

        private java.util.Iterator refIterator;

        private FeatureStore featureStore;

        public FeatureIteratorFacade(java.util.Iterator iter,
                FeatureStore featureStore) {
            this.refIterator = iter;
            this.featureStore = featureStore;
        }

        public boolean hasNext() {
            return refIterator.hasNext();
        }

        public Object next() {
            FeatureReference ref = nextFeatureReference();
            try {
                return featureStore.getFeatureByReference(ref);
            } catch (DataException ex) {
                logger.error(
                        "Error loading the Feature with FeatureReference: "
                                + ref, ex);
                return null;
            }
        }

        /**
         * Returns the next FeatureReference.
         *
         * @return the next FeatureReference
         */
        public FeatureReference nextFeatureReference() {
            return (FeatureReference) refIterator.next();
        }

        public void remove() {
            refIterator.remove();
        }

        /**
         * Returns a Feature for the FeatureReference.
         */
        protected Feature getFeature(FeatureReference ref) throws DataException {
            return ref.getFeature();
        }

		public void dispose() {
			if (refIterator instanceof DisposableIterator) {
				((DisposableIterator) refIterator).dispose();
			}
			refIterator = null;
			featureStore = null;
		}
    }

    /**
     * Facade over a Iterator of FeatureReferences, to return Features instead,
     * when the Selection is reversed
     *
     * @author <a href="mailto:cordin@disid.com">Cèsar Ordiñana</a>
     */
    private class ReversedFeatureIteratorFacade implements DisposableIterator {

        final Logger logger = LoggerFactory
                .getLogger(ReversedFeatureIteratorFacade.class);


        private SelectionData selectionData;

        private DisposableIterator iterator;

        private Feature nextFeature = null;

		private FeatureSet featureSet;

        public ReversedFeatureIteratorFacade(SelectionData selectionData,
                FeatureStore featureStore, boolean fastIterator) {
            this.selectionData = selectionData;

            // Load a Set with all the store features
            try {
                featureSet = featureStore.getFeatureSet();
                if (fastIterator) {
                    iterator = featureSet.fastIterator();
                } else {
                    iterator = featureSet.iterator();
                }
            } catch (DataException ex) {
                throw new ReversedSelectionIteratorException(ex);
            }

            // Filter the features not selected and position in the next
            // selected feature
            positionInNextElement();
        }

        public boolean hasNext() {
            return nextFeature != null;
        }

        public Object next() {
            Feature tmp = nextFeature;
            positionInNextElement();
            return tmp;
        }

        public void remove() {
            iterator.remove();
        }

        private void positionInNextElement() {
            nextFeature = null;
            while (iterator.hasNext()) {
                nextFeature = (Feature) iterator.next();
                if (selectionData.contains(nextFeature.getReference())) {
                    nextFeature = null;
                } else {
                    break;
                }
            }
        }

		public void dispose() {
			this.featureSet.dispose();
			this.iterator.dispose();
			this.selectionData = null;
			this.nextFeature = null;
		}
    }

    private class FeatureTypeCount {
        private long count = 0;

        public void add() {
            count++;
        }

        public void remove() {
            count--;
        }

        public long getCount() {
            return count;
        }
    }

    public void delete(Feature feature) throws DataException {
        throw new UnsupportedOperationException();
    }

    public void insert(EditableFeature feature) throws DataException {
        throw new UnsupportedOperationException();
    }

    public void update(EditableFeature feature) throws DataException {
        throw new UnsupportedOperationException();
    }
}