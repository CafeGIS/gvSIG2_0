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

import org.gvsig.tools.exception.BaseRuntimeException;

/**
 * Exception thrown when an attribute of a DynObject does not exist.
 *
 * @author <a href="mailto:cordin@disid.com">Cèsar Ordiñana</a>
 */
public class DynFieldNotFoundException extends BaseRuntimeException {

    private static final long serialVersionUID = -945752158015340022L;

    private static final String KEY = "_DynFieldNotFoundException";

    private static final String MESSAGE = "The attribute %(attributeName) "
            + "does not exist in a DynObject of the DynClass %(dynClassName)";

    /**
     * Creates a new DynFieldNotFoundException.
     *
     * @param dynFieldName
     *            the name of the attribute not found
     * @param dynClassName
     *            the name of the DynClass where the attribute does not exist
     */
    public DynFieldNotFoundException(String dynFieldName, String dynClassName) {
        super(MESSAGE, KEY, serialVersionUID);
        this.setValue("attributeName", dynFieldName);
        this.setValue("dynClassName", dynClassName);
    }

    /**
     * Creates a new DynFieldNotFoundException.
     *
     * @param dynFieldName
     *            the name of the attribute not found
     * @param dynClassName
     *            the name of the DynClass where the attribute does not exist
     * @param cause
     *            the original cause exception
     */
    public DynFieldNotFoundException(String dynFieldName, String dynClassName,
            Throwable cause) {
        super(MESSAGE, cause, KEY, serialVersionUID);
        this.setValue("attributeName", dynFieldName);
        this.setValue("dynClassName", dynClassName);
    }
}