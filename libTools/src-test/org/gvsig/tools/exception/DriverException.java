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
 */
package org.gvsig.tools.exception;

import java.util.HashMap;
import java.util.Map;

public class DriverException extends BaseException {
	
	private static final long serialVersionUID = -8985920349210629999L;
	
    private static final String KEY = "Error_in_the_driver_%(driverName)s";

    private static final String MESSAGE = "Error in the driver %(driverName)s";
	
	private String driverName;
	
	public DriverException(String driverName) {
		super(MESSAGE, KEY, serialVersionUID);
		this.driverName = driverName;
	}
	
	public DriverException(String driverName, Throwable cause) {
        super(MESSAGE, cause, KEY, serialVersionUID);
		this.driverName = driverName;
	}
    
    public DriverException(String driverName, String message, Throwable cause,
            String key, long code) {
        super(message, cause, key, code);
        this.driverName = driverName;
    }

	public String getDriverName() {
		return driverName;
	}
	
	protected Map values() {
		HashMap values = new HashMap();
		values.put("driverName",this.driverName);
		return values;
	}
}
