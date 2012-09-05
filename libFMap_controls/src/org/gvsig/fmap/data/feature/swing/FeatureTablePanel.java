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

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.FeatureQuery;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.data.feature.swing.table.ConfigurableFeatureTableModel;
import org.gvsig.fmap.data.feature.swing.table.FeatureTableConfigurationPanel;

/**
 * Panel to show a table of Feature data.
 *
 * @author <a href="mailto:cordin@disid.com">Cèsar Ordiñana</a>
 */
public class FeatureTablePanel extends JPanel {

    private static final long serialVersionUID = -9199073063283531216L;

    private FeatureStore featureStore;
    private FeatureQuery featureQuery;
    private ConfigurableFeatureTableModel tableModel;

    private JScrollPane jScrollPane = null;

    private FeatureTable table = null;

    /**
     * Constructs a Panel to show a table with the features of a FeatureStore.
     *
     * @param featureStore
     *            to extract the features from
     * @throws DataException
     *             if there is an error reading data from the FeatureStore
     */
    public FeatureTablePanel(FeatureStore featureStore) throws DataException {
        this(featureStore, true);
    }

    /**
     * Constructs a Panel to show a table with the features of a FeatureStore.
     *
     * @param featureStore
     *            to extract the features from
     * @param isDoubleBuffered
     *            a boolean, true for double-buffering, which uses additional
     *            memory space to achieve fast, flicker-free updates
     * @throws DataException
     *             if there is an error reading data from the FeatureStore
     */
    public FeatureTablePanel(FeatureStore featureStore, boolean isDoubleBuffered)
            throws DataException {
        this(featureStore, null, isDoubleBuffered);
    }

    /**
     * @throws DataException
     *
     */
    public FeatureTablePanel(FeatureStore featureStore,
            FeatureQuery featureQuery) throws DataException {
        this(featureStore, featureQuery, true);
    }

    /**
     * @param isDoubleBuffered
     * @throws DataException
     */
    public FeatureTablePanel(FeatureStore featureStore,
            FeatureQuery featureQuery, boolean isDoubleBuffered)
            throws DataException {
        super(isDoubleBuffered);
        this.featureStore = featureStore;
        if (featureQuery != null) {
			this.featureQuery = featureQuery;
		} else {
			this.featureQuery = featureStore.createFeatureQuery();
		}
        createModel();
        initialize();
        // createGUI();
    }

    /**
     * This method initializes this
     *
     * @throws DataException
     *
     */
    private void initialize() throws DataException {
        this.setLayout(new BorderLayout());
        this.add(getJScrollPane(), BorderLayout.CENTER);
    }

    public JPanel createConfigurationPanel() {
        return new FeatureTableConfigurationPanel(tableModel);
    }

    /**
     * Returns the internal Table Model for the Features of the FeatureStore.
     *
     * @return the internal Table Model
     */
    public ConfigurableFeatureTableModel getTableModel() {
        return tableModel;
    }

    private void createModel() throws DataException {
        tableModel = new ConfigurableFeatureTableModel(featureStore,
                featureQuery);
    }

    public FeatureTable getTable() throws DataException {
        if (table == null) {
            table = new FeatureTable(tableModel);
            // Change the selection model to link with the FeatureStore
            // selection
            // through the FeatureTableModel
            table.setRowSelectionAllowed(true);
            table.setColumnSelectionAllowed(false);
            // table.setSelectionModel(new FeatureSelectionModel(tableModel));
        }
        return table;
    }

    /**
     * This method initializes jScrollPane
     *
     * @return javax.swing.JScrollPane
     * @throws DataException
     */
    private JScrollPane getJScrollPane() throws DataException {
        if (jScrollPane == null) {
            jScrollPane = new JScrollPane();
            jScrollPane.setViewportView(getTable());
        }
        return jScrollPane;
    }
}