/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */
package org.gvsig.project.document.table.gui;


import java.util.BitSet;

import javax.swing.table.AbstractTableModel;

import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.project.document.table.FeatureTableDocument;

import com.iver.andami.PluginServices;


/**
 * Modelo para la JTable de MapProperties
 *
 * @author Fernando González Cortés
 */
public class TableModelProperties extends AbstractTableModel {
    /**
	 *
	 */
	private static final long serialVersionUID = -5988213143909485019L;
	private BitSet visibles = new BitSet();
    private String[] alias;
    private FeatureTableDocument table;

    /**
     * Creates a new MyTableModel object.
     *
     * @param t Tabla del proyecto que se quiere mostrar
     * @throws DriverException
     */
    public TableModelProperties(FeatureTableDocument t) {
        FeatureStore store = t.getStore();
		// alias = t.getAliases();
		// int[] mapping = t.getMapping();
		// visibles = new BitSet();
		// for (int i = 0; i < mapping.length; i++) {
		// visibles.set(mapping[i]);
		// }

        table = t;
    }

    /**
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount() {
        return 3;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getRowCount()
     */
    public int getRowCount() {
		// try {
		// return table.getModelo().getRecordset().getFieldCount();
		// } catch (ReadDriverException e) {
		// NotificationManager.addError("No se pudo leer la información de la tabla",
		// e);
		// return 0;
		// }
		return 0; // FIXME
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
		// if (columnIndex == 0) {
		// return new Boolean(visibles.get(rowIndex));
		// } else if (columnIndex == 1) {
		// try {
		// return table.getModelo().getRecordset().getFieldName(rowIndex);
		// } catch (ReadDriverException e) {
		// NotificationManager.addError("Error accediendo al nombre del campo",
		// e);
		// return "error!";
		// }
		// } else {
		// // rowIndex++;
		// return alias[rowIndex];
		// }
		return null; // FIXME
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnClass(int)
     */
    public Class getColumnClass(int columnIndex) {
        if (columnIndex == 0) {
            return Boolean.class;
        }

        return String.class;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#isCellEditable(int, int)
     */
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return (columnIndex != 1);
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#setValueAt(java.lang.Object, int, int)
     */
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            visibles.set(rowIndex, ((Boolean) aValue).booleanValue());
        } else {
            alias[rowIndex] = aValue.toString();
        }
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnName(int)
     */
    public String getColumnName(int column) {
        String ret = null;

        switch (column) {
        case 0:
            ret = PluginServices.getText(this, "visible");

            break;

        case 1:
            ret = PluginServices.getText(this, "campo");

            break;

        case 2:
            ret = PluginServices.getText(this, "alias");

            break;
        }

        return ret;
    }

    public String[] getAliases() {
        // return table.getAliases();
    	return alias;
    }

    public int[] getFieldMapping() {
        int[] mapping = new int[visibles.cardinality()];
        int index = 0;
        for(int i=visibles.nextSetBit(0); i>=0; i=visibles.nextSetBit(i+1)) {
            mapping[index] = i;
            index++;
        }

        return mapping;
    }
}
