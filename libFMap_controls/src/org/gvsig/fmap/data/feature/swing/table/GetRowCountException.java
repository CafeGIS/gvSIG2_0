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
 * Exception thrown when getting the total row number, or the total number of
 * Features, throws an error.
 * 
 * @author <a href="mailto:cordin@disid.com">Cèsar Ordiñana</a>
 */
public class GetRowCountException extends BaseRuntimeException {

    private static final String KEY = "get_row_count_exception";

    private static final String MESSAGE = "Error getting the total table row count";

    private static final long serialVersionUID = 7803270649446415844L;

    /**
     * Creates a new GetRowCountException.
     */
    public GetRowCountException() {
        super(MESSAGE, KEY, serialVersionUID);
    }

    /**
     * Creates a new GetRowCountException.
     * 
     * @param cause
     *            the cause of the exception
     */
    public GetRowCountException(Throwable cause) {
        super(MESSAGE, cause, KEY, serialVersionUID);
    }

    @Override
    protected Map<String, ?> values() {
        return Collections.emptyMap();
    }

}