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

import org.gvsig.tools.exception.BaseRuntimeException;

/**
 * Exception thrown when there is an error while reloading the Features of a
 * FeatureTable.
 * 
 * @author <a href="mailto:cordin@disid.com">Cèsar Ordiñana</a>
 */
public class FeaturesDataReloadException extends BaseRuntimeException {

    private static final long serialVersionUID = -7724873225152021219L;

    private static final String KEY = "features_data_reload_exception";

    private static final String MESSAGE = "Error reloading the features";

    /**
     * Creates a new Exception when setting a Feature value.
     */
    public FeaturesDataReloadException() {
        super(MESSAGE, KEY, serialVersionUID);
    }

    /**
     * Creates a new Exception when setting a Feature value.
     * 
     * @param cause
     *            the original Throwable
     */
    public FeaturesDataReloadException(Throwable cause) {
        super(MESSAGE, cause, KEY, serialVersionUID);
    }

}