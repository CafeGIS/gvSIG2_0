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

/**
 * Exception for errors related to the initialization of a Library.
 * 
 * @author <a href="mailto:cordin@disid.com">Cèsar Ordiñana</a>
 */
public class ReferenceNotRegisteredException extends LocatorReferenceException {

    private static final long serialVersionUID = 2459965470774180326L;

    private static final String KEY = "reference_not_registered_exception";

    private static final String MESSAGE = "The reference %(referenceName) has "
            + "not registered any Class or Factory in the Locator %(locatorName)";

    public ReferenceNotRegisteredException(String referenceName, Locator locator) {
        super(MESSAGE, KEY, serialVersionUID, referenceName, locator);
    }

    public ReferenceNotRegisteredException(Throwable cause,
            String referenceName, Locator locator) {
        super(MESSAGE, cause, KEY, serialVersionUID, referenceName, locator);
    }

}