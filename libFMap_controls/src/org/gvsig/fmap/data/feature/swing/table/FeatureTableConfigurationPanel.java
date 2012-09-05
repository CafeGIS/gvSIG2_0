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
package org.gvsig.fmap.data.feature.swing.table;

import javax.swing.*;


/**
 * Panel to configure a FeatureTable. Allows to configure visible columns and
 * column aliases.
 * 
 * @author <a href="mailto:cordin@disid.com">Cèsar Ordiñana</a>
 */
public class FeatureTableConfigurationPanel extends JPanel {

    private static final long serialVersionUID = -4912657164727512361L;

    private ConfigurationTableModel tableModel;

    /**
     * Creates a new FeatureTableConfigurationPanel.
     * 
     * @param configurableTableModel
     *            the table model of the FeatureTable to configure.
     */
    public FeatureTableConfigurationPanel(
            ConfigurableFeatureTableModel configurableTableModel) {
        this(configurableTableModel, true);
    }

    /**
     * Creates a new FeatureTableConfigurationPanel.
     * 
     * @param configurableTableModel
     *            the table model of the FeatureTable to configure.
     * @param isDoubleBuffered
     *            a boolean, true for double-buffering, which uses additional
     *            memory space to achieve fast, flicker-free updates
     */
    public FeatureTableConfigurationPanel(
            ConfigurableFeatureTableModel configurableTableModel,
            boolean isDoubleBuffered) {
        super(isDoubleBuffered);
        tableModel = new ConfigurationTableModel(configurableTableModel);
        createGUI();
    }

    private void createGUI() {
        // TODO: set a fixed column width for the first column with the
        // checkboxes
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(scrollPane);
    }
}