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

import javax.swing.table.AbstractTableModel;

import org.gvsig.i18n.Messages;

/**
 * TableModel to configure a ConfigurableFeatureTableModel.
 * 
 * Allows to set Feature attributes as visible or not, and to set aliases for
 * the Feature attribute names.
 * 
 * @author <a href="mailto:cordin@disid.com">Cèsar Ordiñana</a>
 */
public class ConfigurationTableModel extends AbstractTableModel {

    private static final long serialVersionUID = -825156698327593853L;

    private static final int VISIBILITY_COLUMN = 0;
    private static final int NAME_COLUMN = 1;
    private static final int ALIAS_COLUMN = 2;

    private ConfigurableFeatureTableModel configurable;

    public ConfigurationTableModel(ConfigurableFeatureTableModel configurable) {
        super();
        this.configurable = configurable;
    }

    public int getColumnCount() {
        // 1: visibility, 2: field name, 3: alias
        return 3;
    }

    public int getRowCount() {
        return configurable.getOriginalColumnCount();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        String name = configurable.getOriginalColumnName(rowIndex);
        switch (columnIndex) {
        case VISIBILITY_COLUMN:
            return configurable.isVisible(name);
        case NAME_COLUMN:
            return name;
        case ALIAS_COLUMN:
            return configurable.getAliasForColumn(name);
        default:
            return null;
        }
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        String name = configurable.getOriginalColumnName(rowIndex);
        switch (columnIndex) {
        case VISIBILITY_COLUMN:
            configurable.setVisible(name, Boolean.TRUE.equals(value));
            break;
        case ALIAS_COLUMN:
            configurable.setAlias(name, (String) value);
            break;
        }
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        switch (columnIndex) {
        case VISIBILITY_COLUMN:
        case ALIAS_COLUMN:
            return true;
        default:
            return false;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
        case VISIBILITY_COLUMN:
            return Boolean.class;
        case NAME_COLUMN:
        case ALIAS_COLUMN:
            return String.class;
        default:
            return Object.class;
        }

    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
        case VISIBILITY_COLUMN:
            return Messages.getText("Visible");
        case NAME_COLUMN:
            return Messages.getText("Nombre");
        case ALIAS_COLUMN:
            return Messages.getText("Alias");
        default:
            return "";
        }
    }
}