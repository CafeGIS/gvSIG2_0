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
 * 2008 {DiSiD Technologies}  {Create a Table component for Features}
 */
package org.gvsig.fmap.data.feature.swing;

import java.awt.Color;

import javax.swing.JTable;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureStoreNotification;
import org.gvsig.fmap.data.feature.swing.table.*;
import org.gvsig.fmap.data.feature.swing.table.notification.ColumnHeaderSelectionChangeNotification;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.tools.observer.Observable;
import org.gvsig.tools.observer.Observer;

/**
 * Table extension to show Feature values.
 *
 * It's based on the usage of a FeatureTableModel, and adds renderers for
 * Geometry and Feature cell values.
 *
 * Observers are notified about column header selection changes, with a
 * {@link ColumnHeaderSelectionChangeNotification}.
 *
 * @author <a href="mailto:cordin@disid.com">Cèsar Ordiñana</a>
 */
public class FeatureTable extends JTable implements Observer, Observable {

    /**
     * Generated Serial UID
     */
    private static final long serialVersionUID = -6139395189283163964L;
    private final FeatureTableModel featureTableModel;
    private JToggleButtonHeaderCellRenderer headerCellRenderer;

    /**
     * Creates a new FeatureTable with a {@link FeatureTableModel}.
     *
     * @param featureTableModel
     *            the table model to get data to be shown on the table
     * @throws DataException
     *             if there is an error while loading the Features to show
     * @see JTable#JTable(TableModel)
     */
    public FeatureTable(FeatureTableModel featureTableModel)
            throws DataException {
        super(featureTableModel);
        this.featureTableModel = featureTableModel;
        init();
    }

    /**
     * Creates a new FeatureTable with a {@link FeatureTableModel}.
     *
     * @param featureTableModel
     *            the table model to get data to be shown on the table
     * @param cm
     *            the table column model to use
     * @throws DataException
     *             if there is an error while loading the Features to show
     * @see JTable#JTable(TableModel, TableColumnModel)
     */
    public FeatureTable(FeatureTableModel featureTableModel, TableColumnModel cm)
            throws DataException {
        super(featureTableModel, cm);
        this.featureTableModel = featureTableModel;
        init();
    }

    public void update(Observable observable, Object notification) {
        if (notification instanceof FeatureStoreNotification) {
            FeatureStoreNotification fsNotification = (FeatureStoreNotification) notification;
            String type = fsNotification.getType();
            // If the selection has changed, repaint the table to show the new
            // selected rows
            if (FeatureStoreNotification.SELECTION_CHANGE.equals(type)) {
                repaint();
            }
        }
    }

    /**
     * Returns the FeatureAttributeDescriptor related to the selected columns.
     *
     * @return an array of FeatureAttributeDescriptor
     *
     * @see org.gvsig.fmap.data.feature.swing.table.JToggleButtonHeaderCellRenderer#getSelectedColumns()
     */
    public FeatureAttributeDescriptor[] getSelectedColumnsAttributeDescriptor() {
        int[] columns = headerCellRenderer.getSelectedColumns();
        FeatureAttributeDescriptor[] descriptors = new FeatureAttributeDescriptor[columns.length];

        for (int i = 0; i < descriptors.length; i++) {
            descriptors[i] = featureTableModel
                    .getDescriptorForColumn(columns[i]);
        }

        return descriptors;
    }

    public void addObserver(Observer observer) {
        headerCellRenderer.addObserver(observer);
    }

    public void deleteObserver(Observer observer) {
        headerCellRenderer.deleteObserver(observer);
    }

    public void deleteObservers() {
        headerCellRenderer.deleteObservers();
    }

    // @Override
    // public void tableChanged(TableModelEvent e) {
    // super.tableChanged(e);
    // if (headerCellRenderer != null) {
    // headerCellRenderer.deselectAll();
    // }
    // }

    @Override
    protected void initializeLocalVars() {
        super.initializeLocalVars();
        // Add a cell renderer for Geometries and Features
        setDefaultRenderer(Geometry.class, new GeometryWKTCellRenderer());
        setDefaultEditor(Geometry.class, new GeometryWKTCellEditor());
        setDefaultRenderer(Feature.class, new FeatureCellRenderer());

        // Set the selected row colors
        setSelectionForeground(Color.blue);
        setSelectionBackground(Color.yellow);
    }

    /**
     * Initializes the table GUI.
     */
    private void init() throws DataException {
        featureTableModel.getFeatureStore().addObserver(this);
        // Change the selection model to link with the FeatureStore selection
        // through the FeatureTableModel
        setRowSelectionAllowed(true);
        setColumnSelectionAllowed(false);
        setSelectionModel(new FeatureSelectionModel(featureTableModel));

        headerCellRenderer = new JToggleButtonHeaderCellRenderer(this);
        getTableHeader().setDefaultRenderer(headerCellRenderer);
    }
    /**
     * Returns the number of selected columns.
     *
     * @return the number of selected columns, 0 if no columns are selected
     */
    public int getSelectedColumnCount() {
        return headerCellRenderer.getSelectedColumns().length;
    }
}