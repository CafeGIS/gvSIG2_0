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
* 2008 {DiSiD Technologies}  {Create DynObjects implementation}
*/
package org.gvsig.tools.dynobject.exception;

import java.util.Collections;
import java.util.Map;

import org.gvsig.tools.exception.BaseRuntimeException;

/**
 * Exception thrown when a DynClass does not exist.
 * 
 * @author <a href="mailto:cordin@disid.com">Cèsar Ordiñana</a>
 */
public class DynClassNotFoundException extends BaseRuntimeException {
    
    private static final long serialVersionUID = 3901517820197735933L;

    private static final String KEY = "_DynClassNotFoundException";

    private static final String MESSAGE = "The DynClass with the name "
            + "%(dynClassName) does not exist";

    private String dynClassName;

    /**
     * Creates a new DynClassNotFoundException.
     * 
     * @param dynClassName
     *            the name of the DynClass that does not exist
     */
    public DynClassNotFoundException(String dynClassName) {
        super(MESSAGE, KEY, serialVersionUID);
        this.dynClassName = dynClassName;
    }

    /**
     * Creates a new DynClassNotFoundException.
     * 
     * @param dynClassName
     *            the name of the DynClass that does not exist
     * @param cause
     *            the original cause exception
     */
    public DynClassNotFoundException(String dynClassName,
            Throwable cause) {
        super(MESSAGE, cause, KEY, serialVersionUID);
        this.dynClassName = dynClassName;
    }

    protected Map values() {
        return Collections.singletonMap("dynClassName", dynClassName);
    }
}