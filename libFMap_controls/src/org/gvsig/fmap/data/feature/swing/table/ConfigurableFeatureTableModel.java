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
 * 2008 {DiSiD Technologies}  {Create a JTable TableModel for a FeatureQuery}
 */
package org.gvsig.fmap.data.feature.swing.table;

import java.security.InvalidParameterException;
import java.util.*;

import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.*;

/**
 * Extends the FeatureTableModel to add more configurable options, like the
 * visible columns, column name aliases and row order.
 *
 * TODO: añadir la persistencia.
 *
 * @author <a href="mailto:cordin@disid.com">Cèsar Ordiñana</a>
 */
public class ConfigurableFeatureTableModel extends FeatureTableModel {

    private static final long serialVersionUID = -8223987814719746492L;

    private List<String> columnNames;

    private List<String> visibleColumnNames;

    private Map<String, String> name2Alias;

    /**
     * @see FeatureTableModel#FeatureTableModel(FeatureStore, FeatureQuery)
     */
    public ConfigurableFeatureTableModel(FeatureStore featureStore,
            FeatureQuery featureQuery) throws DataException {
        super(featureStore, featureQuery);
    }

    /**
     * @see FeatureTableModel#FeatureTableModel(FeatureStore, FeatureQuery, int)
     */
    public ConfigurableFeatureTableModel(FeatureStore featureStore,
            FeatureQuery featureQuery, int pageSize) throws DataException {
        super(featureStore, featureQuery, pageSize);
    }

    @Override
    public int getColumnCount() {
        return visibleColumnNames.size();
    }

    public int getOriginalColumnCount() {
        return super.getColumnCount();
    }

    @Override
    public String getColumnName(int column) {
        int originalIndex = getOriginalColumnIndex(column);
        return getAliasForColumn(getOriginalColumnName(originalIndex));
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        int originalIndex = getOriginalColumnIndex(columnIndex);
        return super.getColumnClass(originalIndex);
    }
    
    @Override
    public FeatureAttributeDescriptor getDescriptorForColumn(int columnIndex) {
        int originalIndex = getOriginalColumnIndex(columnIndex);
        return super.getDescriptorForColumn(originalIndex);
    }

    /**
     * Returns the original name of the column, ignoring the alias.
     *
     * @param column
     *            the original index of the column
     * @return the original column name
     */
    public String getOriginalColumnName(int column) {
        return super.getColumnName(column);
    }

    /**
     * Sets the visibility of a table column.
     *
     * @param columnIndex
     *            the index of the column to update
     * @param visible
     *            if the column will be visible or not
     */
    public void setVisible(String name, boolean visible) {
        // If we don't have already the column as visible,
        // add to the list, without order, and recreate
        // the visible columns list in the original order
		if (!columnNames.contains(name)) {
			throw new InvalidParameterException(name); // FIXME
		}
        if (visible && !visibleColumnNames.contains(name)) {
            visibleColumnNames.add(name);
            setVisibleColumns(visibleColumnNames);
        } else {
            visibleColumnNames.remove(name);
            fireTableStructureChanged();
        }

    }

    @Override
	public void setFeatureType(FeatureType featureType) {
    	// checks if all columns was visible before change
		boolean equalsBefore = this.columnNames.size() == this.visibleColumnNames
				.size()
				&&
			visibleColumnNames.containsAll(columnNames);

		// Update column names
		columnNames.clear();
		Iterator<FeatureAttributeDescriptor> attrIter = featureType.iterator();
		String colName;
		while (attrIter.hasNext()) {
			colName = attrIter.next().getName();
			columnNames.add(colName);
			if ((!visibleColumnNames.contains(colName)) && equalsBefore) {
				// Add new columns
				visibleColumnNames.add(colName);
			}
		}

		// remove from visible columns removed columns
		visibleColumnNames.retainAll(columnNames);

		// remove from alias map removed columns
		name2Alias.keySet().retainAll(visibleColumnNames);

		super.setFeatureType(featureType);

	}

	/**
	 * Sets the current visible columns list, in the original order.
	 *
	 * @param names
	 *            the names of the columns to set as visible
	 */
    public void setVisibleColumns(List<String> names) {
        // Recreate the visible column names list
        // to maintain the original order
        visibleColumnNames = new ArrayList<String>(names.size());
        for (int i = 0; i < columnNames.size(); i++) {
            String columnName = columnNames.get(i);
            if (names.contains(columnName)) {
                visibleColumnNames.add(columnName);
            }
        }
        fireTableStructureChanged();
    }

    /**
     * Changes all columns to be visible.
     */
    public void setAllVisible() {
        visibleColumnNames.clear();
        visibleColumnNames.addAll(columnNames);
        fireTableStructureChanged();
    }

    /**
     * Sets the alias for a column.
     *
     * @param name
     *            the name of the column
     * @param alias
     *            the alias for the column
     */
    public void setAlias(String name, String alias) {
        name2Alias.put(name, alias);
        fireTableStructureChanged();
    }

    public void orderByColumn(String name, boolean ascending)
            throws DataException {
        FeatureQueryOrder order = getHelper().getFeatureQuery().getOrder();
        order.clear();
        order.add(name, ascending);
        getHelper().reload();
        fireTableDataChanged();
    }

    @Override
    protected void initialize() {
        super.initialize();

        initializeVisibleColumns();
        initializeAliases();
    }

    /**
     * Returns if a column is visible.
     *
     * @param name
     *            the name of the column
     * @return if the column is visible
     */
    public boolean isVisible(String name) {
        return visibleColumnNames.contains(name);
    }

    /**
     * Initializes the table name aliases.
     */
    private void initializeAliases() {
        int columns = super.getColumnCount();
        name2Alias = new HashMap<String, String>(columns);
		//
		// for (int i = 0; i < columns; i++) {
		// String columnName = super.getColumnName(i);
		// name2Alias.put(columnName, columnName);
		// }
    }

    /**
     * Initializes the table visible columns.
     */
    protected void initializeVisibleColumns() {
        int columns = super.getColumnCount();
        columnNames = new ArrayList<String>(columns);
        visibleColumnNames = new ArrayList<String>(columns);

        for (int i = 0; i < columns; i++) {
            String columnName = super.getColumnName(i);
            columnNames.add(columnName);

            // By default, geometry columns will not be visible
            FeatureAttributeDescriptor descriptor = super
                    .getDescriptorForColumn(i);
            if (descriptor.getDataType() != DataTypes.GEOMETRY) {
                visibleColumnNames.add(columnName);
            }
        }
    }

    /**
     * Returns the alias for the name of a column.
     *
     * @param name
     *            of the column
     * @return the alias
     */
    protected String getAliasForColumn(String name) {
        String alias = name2Alias.get(name);
		return alias == null ? name : alias;
    }

    /**
     * Returns the original position of a column.
     *
     * @param columnIndex
     *            the current visible column index
     * @return the original column index
     */
    protected int getOriginalColumnIndex(int columnIndex) {
        String columnName = visibleColumnNames.get(columnIndex);
        return columnNames.indexOf(columnName);
    }

    @Override
    protected Object getFeatureValue(Feature feature, int columnIndex) {
        int realColumnIndex = getOriginalColumnIndex(columnIndex);
        return super.getFeatureValue(feature, realColumnIndex);
    }

    @Override
    protected EditableFeature setFeatureValue(Feature feature, int columnIndex,
            Object value) {
        int realColumnIndex = getOriginalColumnIndex(columnIndex);
        return super.setFeatureValue(feature, realColumnIndex, value);
    }
}
