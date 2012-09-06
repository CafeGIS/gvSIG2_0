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
* 2008 {DiSiD Technologies}   {Create a base Locator implementation}
*/
package org.gvsig.tools.locator;

import java.util.HashMap;
import java.util.Map;

import org.gvsig.tools.exception.BaseRuntimeException;

/**
 * Exception for errors related to the retrieval of references through a
 * Locator.
 * 
 * @author <a href="mailto:cordin@disid.com">Cèsar Ordiñana</a>
 */
public class LocatorReferenceException extends BaseRuntimeException {

    private static final long serialVersionUID = -6623935035803501270L;

    private static final String KEY = "locator_exception";

    private static final String MESSAGE = "Error getting a reference to %(referenceName), "
            + "through the Locator %(locatorName)";
    
    private String referenceName;

    private String locatorName;
    
    public LocatorReferenceException(String referenceName, Locator locator) {
        this(MESSAGE, KEY, serialVersionUID, referenceName, locator);
    }
    
    public LocatorReferenceException(Throwable cause, String referenceName,
            Locator locator) {
        this(MESSAGE, cause, KEY, serialVersionUID, referenceName, locator);
    }
    
    public LocatorReferenceException(String message, String key, long code,
            String referenceName, Locator locator) {
        super(message, key, code);
        setReferenceName(referenceName);
        setLocatorName(locator.getLocatorName());
    }

    public LocatorReferenceException(String message, Throwable cause, String key,
            long code, String referenceName, Locator locator) {
        super(message, cause, key, code);
        setReferenceName(referenceName);
        setLocatorName(locator.getLocatorName());
    }
    
    protected Map values() {
        Map values = new HashMap(2);
        values.put("referenceName", getReferenceName());
        values.put("locatorName", getLocatorName());
        return values;
    }
    
    private String getReferenceName() {
        return referenceName;
    }
    
    private void setReferenceName(String name) {
        this.referenceName = name;
    }
    
    private String getLocatorName() {
        return locatorName;
    }

    private void setLocatorName(String name) {
        this.locatorName = name;
    }
}