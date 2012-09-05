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
package org.gvsig.fmap.data.feature.swing.table;

import javax.swing.table.AbstractTableModel;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.*;
import org.gvsig.fmap.dal.feature.paging.FeaturePagingHelper;
import org.gvsig.fmap.dal.feature.paging.FeaturePagingHelperImpl;
import org.gvsig.tools.observer.Observable;
import org.gvsig.tools.observer.Observer;

/**
 * TableModel to access data of Features.
 *
 * This table model can't handle a FeatureSet with more than Integer.MAX_VALUE
 * elements. In that case, only the first Integer.MAX_VALUE elements will be
 * shown.
 *
 * @author <a href="mailto:cordin@disid.com">Cèsar Ordiñana</a>
 */
public class FeatureTableModel extends AbstractTableModel implements Observer {

    private static final long serialVersionUID = -2488157521902851301L;

    private FeaturePagingHelper helper;

    /** Used to know if a modification in the FeatureStore is created by us. */
    private EditableFeature editableFeature;

    /**
     * Constructs a TableModel from the features of a FeatureStore, with the
     * default page size.
     *
     * @param featureStore
     *            to extract the features from
     * @param featureQuery
     *            the query to get the features from the store
     * @throws DataException
     *             if there is an error reading data from the FeatureStore
     */
    public FeatureTableModel(FeatureStore featureStore,
            FeatureQuery featureQuery) throws DataException {
        this(featureStore, featureQuery, FeaturePagingHelper.DEFAULT_PAGE_SIZE);
    }

    /**
     * Constructs a TableModel from the features of a FeatureStore, with the
     * default page size.
     *
     * @param featureStore
     *            to extract the features from
     * @param featureQuery
     *            the query to get the features from the store
     * @param pageSize
     *            the number of elements per page data
     * @throws DataException
     *             if there is an error reading data from the FeatureStore
     */
    public FeatureTableModel(FeatureStore featureStore,
            FeatureQuery featureQuery, int pageSize) throws DataException {
        this(new FeaturePagingHelperImpl(featureStore, featureQuery, pageSize));
    }

    /**
     * Constructs a TableModel from a FeatureCollection and a Paging helper.
     *
     * @param featureCollection
     *            to extract data from
     * @param helper
     *            the paging helper
     * @throws DataException
     *             if there is an error reading data from the FeatureStore
     */
    protected FeatureTableModel(FeaturePagingHelper helper) {
        this.helper = helper;
        initialize();
    }

    public int getColumnCount() {
        // Return the number of fields of the Features
        FeatureType featureType = getFeatureType();
        return featureType.size();
    }

    public int getRowCount() {
        try {
            // Return the total size of the collection
            // If the size is bigger than INTEGER.MAX_VALUE, return that instead
            long totalSize = getHelper().getTotalSize();
            if (totalSize > Integer.MAX_VALUE) {
                return Integer.MAX_VALUE;
            } else {
                return (int) totalSize;
            }
        } catch (DataException ex) {
            throw new GetRowCountException(ex);
        }
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        // Get the Feature at row "rowIndex", and return the value of the
        // attribute at "columnIndex"
        Feature feature = getFeatureAt(rowIndex);
        return getFeatureValue(feature, columnIndex);
    }

    /**
     * Returns the value for a row position.
     *
     * @param rowIndex
     *            the row position
     * @return the Feature
     */
    public Feature getFeatureAt(int rowIndex) {
        try {
            return getHelper().getFeatureAt(rowIndex);
        } catch (DataException ex) {
            throw new GetFeatureAtException(rowIndex, ex);
        }
    }

    public Class<?> getColumnClass(int columnIndex) {
        // Return the class of the FeatureAttributeDescriptor for the value
        FeatureAttributeDescriptor attributeDesc = internalGetFeatureDescriptorForColumn(columnIndex);
        Class<?> clazz = attributeDesc.getObjectClass();
        return (clazz == null ? super.getColumnClass(columnIndex) : clazz);
    }

    public String getColumnName(int column) {
        // Return the Feature attribute name
        FeatureAttributeDescriptor attributeDesc = internalGetFeatureDescriptorForColumn(column);
        return attributeDesc.getName();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (getFeatureStore().isEditing()) {
            FeatureAttributeDescriptor attributeDesc = internalGetFeatureDescriptorForColumn(columnIndex);
            return !attributeDesc.isReadOnly();
        }

        return false;
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        // Get the feature at rowIndex
        Feature feature = getFeatureAt(rowIndex);
        // Only set the value if the feature exists
        if (feature != null) {
            // We only need to update if the value to set is not equal to the
            // current value
            Object currentValue = getFeatureValue(feature, columnIndex);
            if (value != currentValue
                    && (value == null || !value.equals(currentValue))) {
                try {
                    FeatureStore store = getFeatureStore();
                    // Store the editable feature to ignore the related store
                    // change notification
                    editableFeature = setFeatureValue(feature, columnIndex,
                            value);
                    this.getHelper().update(editableFeature);
                    // We'll have already received the event, so we can forget
                    // about it
                    editableFeature = null;
                    getHelper().reloadCurrentPage();
                    fireTableCellUpdated(rowIndex, columnIndex);
                } catch (DataException ex) {
                    throw new SetFeatureValueException(rowIndex, columnIndex,
                            value, ex);
                } finally {
                    // Just in case
                    editableFeature = null;
                }
            }
        }
    }

    /**
     * Returns a reference to the Paging Helper used to load the data from the
     * DataStore.
     *
     * @return the paging helper
     */
    public FeaturePagingHelper getHelper() {
        return helper;
    }

    /**
     * Sets the FeatureType to show in the table. Used for FeatureStores with
     * many simultaneous FeatureTypes supported. Will cause a reload of the
     * current data.
     *
     * @param featureType
     *            the FeatureType of the Features
     * @throws DataException
     *             if there is an error loading the data
     */
    public void setFeatureType(FeatureType featureType) {
        getFeatureQuery().setFeatureType(featureType);
        reloadFeatures();
        fireTableStructureChanged();
    }

    public void update(Observable observable, Object notification) {
        if (observable.equals(getFeatureStore())
                && notification instanceof FeatureStoreNotification) {
            FeatureStoreNotification fsNotification = (FeatureStoreNotification) notification;
            String type = fsNotification.getType();

            System.out.println("FeatureTableModel.update(): " + type);
            // If there are new, updated or deleted features
            // reload the table data
            if (FeatureStoreNotification.AFTER_DELETE.equals(type)
                    || FeatureStoreNotification.AFTER_INSERT.equals(type)
                    || FeatureStoreNotification.AFTER_UPDATE.equals(type)) {

                reloadIfFeatureChanged(fsNotification.getFeature());

            } else if (FeatureStoreNotification.AFTER_UPDATE_TYPE.equals(type)) {

                reloadIfTypeChanged(fsNotification.getFeatureType());

            } else if (FeatureStoreNotification.TRANSFORM_CHANGE.equals(type)) {
                reloadIfTypeTransformed();

            } else if (FeatureStoreNotification.AFTER_FINISHEDITING
					.equals(type)
					|| FeatureStoreNotification.AFTER_CANCELEDITING
							.equals(type)) {
            	reloadIfTypeTransformed();
            }

        }
    }

    /**
     * Returns the FeatureStore of the Collection.
     *
     * @return the FeatureStore
     */
    public FeatureStore getFeatureStore() {
        return getHelper().getFeatureStore();
    }

    /**
     * Returns the descriptor of a Feature attribute for a table column.
     *
     * @param columnIndex
     *            the column index
     */
    public FeatureAttributeDescriptor getDescriptorForColumn(int columnIndex) {
        return internalGetFeatureDescriptorForColumn(columnIndex);
    }

    /**
     * @param columnIndex
     * @return
     */
    protected FeatureAttributeDescriptor internalGetFeatureDescriptorForColumn(
            int columnIndex) {
        return getFeatureType().getAttributeDescriptor(columnIndex);
    }

    /**
     * Initialize the TableModel
     */
    protected void initialize() {
        // Add as observable to the FeatureStore, to detect data and selection
        // changes
        helper.getFeatureStore().addObserver(this);
    }

    /**
     * Returns the value of a Feature attribute, at the given position.
     *
     * @param feature
     *            the feature to get the value from
     * @param columnIndex
     *            the Feature attribute position
     * @return the value
     */
    protected Object getFeatureValue(Feature feature, int columnIndex) {
        return feature.get(columnIndex);
    }

    /**
     * Sets the value of an Feature attribute at the given position.
     *
     * @param feature
     *            the feature to update
     * @param columnIndex
     *            the attribute position
     * @param value
     *            the value to set
     * @throws IsNotFeatureSettingException
     *             if there is an error setting the value
     */
    protected EditableFeature setFeatureValue(Feature feature, int columnIndex,
            Object value) {
        EditableFeature editableFeature = feature.getEditable();
        editableFeature.set(columnIndex, value);
        return editableFeature;
    }

    /**
     * Returns the FeatureSet used to get the data.
     *
     * @return the FeatureSet
     */
    protected FeatureSet getFeatureSet() {
        return getHelper().getFeatureSet();
    }

    /**
     * Returns the FeatureQuery used to get the Features.
     *
     * @return the FeatureQuery
     */
    protected FeatureQuery getFeatureQuery() {
        return getHelper().getFeatureQuery();
    }

    /**
     * Returns the type of the features.
     */
    private FeatureType getFeatureType() {
        return getHelper().getFeatureType();
    }

    /**
     * Reloads the table data if a feature has been changed, not through the
     * table.
     */
    private void reloadIfFeatureChanged(Feature feature) {
        // Is any data is changed in the FeatureStore, notify the model
		// listeners. Ignore the case where the updated feature is
		// changed through us.
		if (editableFeature == null || !editableFeature.equals(feature)) {
			reloadFeatures();
			fireTableDataChanged();
		}
    }

    /**
     * Reloads data and structure if the {@link FeatureType} of the features
     * being shown has changed.
     */
    private void reloadIfTypeChanged(FeatureType updatedType) {
        // If the updated featured type is the one currently being
        // shown, reload the table.
		if (updatedType != null
				&& updatedType.getId().equals(getFeatureType().getId())) {
            setFeatureType(updatedType);
        }
    }

    /**
     * Reloads data and structure if the {@link FeatureType} of the features
     * being shown has been transformed.
     */
    private void reloadIfTypeTransformed() {
		try {
			setFeatureType(getHelper().getFeatureStore().getFeatureType(
					getHelper().getFeatureType().getId()));
		} catch (DataException e) {
			throw new FeaturesDataReloadException(e);
		}
    }

    /**
     * Reloads the features shown on the table.
     */
    private void reloadFeatures() {
        try {
            getHelper().reload();
        } catch (DataException ex) {
            throw new FeaturesDataReloadException(ex);
        }
    }
}