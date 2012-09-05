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
* 2009 {DiSiD Technologies}  {{Task}}
*/
package org.gvsig.fmap.mapcontext.impl;

import org.gvsig.fmap.mapcontext.MapContextDrawer;
import org.gvsig.fmap.mapcontext.MapContextException;

/**
 * Exception for errors while setting the implementation class of
 * {@link MapContextDrawer}.
 * 
 * @author <a href="mailto:cordinyana@gvsig.org">Cèsar Ordiñana</a>
 */
public class InvalidMapContextDrawerClassException extends MapContextException {

    private static final long serialVersionUID = -5394252409666306233L;

    private static final String KEY = "invalid_mapcontextdrawer_class_exception";

    private static final String MESSAGE = "The provided class does not "
            + "implement the MapContextDrawer interface: %(drawerClazz)";

    /**
     * Creates a new exception with the {@link MapContextDrawer} implementation
     * class to use.
     * 
     * @param drawerClazz
     *            the {@link MapContextDrawer} implementation class
     */
    public InvalidMapContextDrawerClassException(Class drawerClazz) {
        super(MESSAGE, KEY, serialVersionUID);
        setValue("drawerClazz", drawerClazz.toString());
    }
}