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
 * Exception thrown when there is an error while converting a WKT text to a
 * Geometry into a table cell.
 * 
 * @author <a href="mailto:cordin@disid.com">Cèsar Ordiñana</a>
 */
public class WKTToGeometryException extends BaseRuntimeException {

    private static final long serialVersionUID = 7450289270030694675L;

    private static final String KEY = "wkt_to_geometry_exception";

    private static final String MESSAGE = "Error getting the geometry value of "
            + "the WKT value %(wkt)";

    private String wkt;

    /**
     * Creates a new Exception when getting a Geometry cell as WKT.
     * 
     * @param wkt
     *            the geometry in WKT format
     */
    public WKTToGeometryException(String wkt) {
        super(MESSAGE, KEY, serialVersionUID);
        this.wkt = wkt;
    }

    /**
     * Creates a new Exception when getting a Geometry cell as WKT.
     * 
     * @param wkt
     *            the geometry in WKT format
     * @param cause
     *            the original Throwable
     */
    public WKTToGeometryException(String wkt, Throwable cause) {
        super(MESSAGE, cause, KEY, serialVersionUID);
        this.wkt = wkt;
    }

    public Object getWKT() {
        return wkt;
    }

    @Override
    protected Map<String, ?> values() {
        return Collections.singletonMap("wkt", getWKT());
    }

}