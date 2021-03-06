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

import java.util.Collections;
import java.util.Map;

import org.gvsig.tools.exception.BaseRuntimeException;

/**
 * Exception thrown when there is an error while getting a Feature in a
 * position.
 * 
 * @author <a href="mailto:cordin@disid.com">C�sar Ordi�ana</a>
 */
public class GetFeatureAtException extends BaseRuntimeException {

    private static final long serialVersionUID = 3981212631490713671L;

    private static final String KEY = "get_feature_at_exception";

    private static final String MESSAGE = "Error getting the feature "
            + "at row =  %(rowIndex)";

    private int rowIndex;

    /**
     * Creates a new Exception when getting a Feature.
     * 
     * @param rowIndex
     *            the position of the Feature
     */
    public GetFeatureAtException(int rowIndex) {
        super(MESSAGE, KEY, serialVersionUID);
        this.rowIndex = rowIndex;
    }

    /**
     * Creates a new Exception when getting a Feature.
     * 
     * @param rowIndex
     *            the position of the Feature
     * @param cause
     *            the original Throwable
     */
    public GetFeatureAtException(int rowIndex, Throwable cause) {
        super(MESSAGE, cause, KEY, serialVersionUID);
        this.rowIndex = rowIndex;
    }

    /**
     * @return the rowIndex
     */
    public int getRowIndex() {
        return rowIndex;
    }

    @Override
    protected Map<String, ?> values() {
        return Collections.singletonMap("rowIndex", getRowIndex());
    }

}