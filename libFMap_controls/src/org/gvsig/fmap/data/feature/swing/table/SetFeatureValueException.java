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
package org.gvsig.fmap.data.feature.swing.table;

import java.util.HashMap;
import java.util.Map;

import org.gvsig.tools.exception.BaseRuntimeException;

/**
 * Exception thrown when there is an error while updating a Feature through a
 * FeatureTableModel.
 * 
 * @author <a href="mailto:cordin@disid.com">Cèsar Ordiñana</a>
 */
public class SetFeatureValueException extends BaseRuntimeException {

    private static final String KEY = "set_feature_value_exception";

    private static final String MESSAGE = "Error setting the value of the cell "
            + "at row =  %(rowIndex) and column = %(columnIndex) "
            + "with the value = %(value)";

    private static final long serialVersionUID = -414675652211552543L;

    private int rowIndex;

    private int columnIndex;

    private Object value;

    /**
     * Creates a new Exception when setting a Feature value.
     * 
     * @param rowIndex
     *            the position of the Feature
     * @param columnIndex
     *            the position of the value into the Feature
     * @param value
     *            the value to set
     */
    public SetFeatureValueException(int rowIndex, int columnIndex, Object value) {
        super(MESSAGE, KEY, serialVersionUID);
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
        this.value = value;
    }

    /**
     * Creates a new Exception when setting a Feature value.
     * 
     * @param rowIndex
     *            the position of the Feature
     * @param columnIndex
     *            the position of the value into the Feature
     * @param value
     *            the value to set
     * @param cause
     *            the original Throwable
     */
    public SetFeatureValueException(int rowIndex, int columnIndex,
            Object value, Throwable cause) {
        super(MESSAGE, cause, KEY, serialVersionUID);
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
        this.value = value;
    }

    /**
     * @return the rowIndex
     */
    public int getRowIndex() {
        return rowIndex;
    }

    /**
     * @return the columnIndex
     */
    public int getColumnIndex() {
        return columnIndex;
    }

    /**
     * @return the value
     */
    public Object getValue() {
        return value;
    }

    @Override
    protected Map<String, ?> values() {
        Map<String, Object> values = new HashMap<String, Object>(3);
        values.put("rowIndex", getRowIndex());
        values.put("columnIndex", getColumnIndex());
        values.put("value", getValue());
        return values;
    }

}