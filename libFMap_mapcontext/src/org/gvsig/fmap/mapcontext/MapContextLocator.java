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
package org.gvsig.fmap.mapcontext;

import org.gvsig.tools.locator.BaseLocator;
import org.gvsig.tools.locator.Locator;
import org.gvsig.tools.locator.LocatorException;

/**
 * Locator for the MapContext library.
 * 
 * @author <a href="mailto:cordinyana@gvsig.org">Cèsar Ordiñana</a>
 */
public class MapContextLocator extends BaseLocator {

    public static final String MAPCONTEXT_MANAGER_NAME = "mapcontextlocator.manager";

    private static final String MAPCONTEXT_MANAGER_DESCRIPTION = "Manager of the MapContext Library";

    private static final MapContextLocator instance = new MapContextLocator();

    /**
     * Private constructor, we don't want it to be able to be instantiated
     * outside this class.
     */
    private MapContextLocator() {
        // Nothing to do
    }

    public static MapContextLocator getInstance() {
        return instance;
    }
    
    /**
     * Return a reference to MapContextManager.
     * 
     * @return a reference to MapContextManager
     * @throws LocatorException
     *             if there is no access to the class or the class cannot be
     *             instantiated
     * @see Locator#get(String)
     */
    public static MapContextManager getMapContextManager()
            throws LocatorException {
        return (MapContextManager) getInstance().get(MAPCONTEXT_MANAGER_NAME);
    }

    /**
     * Registers the Class implementing the MapContextManager interface.
     * 
     * @param clazz
     *            implementing the MapContextManager interface
     */
    public static void registerMapContextManager(Class clazz) {
        getInstance().register(MAPCONTEXT_MANAGER_NAME,
                MAPCONTEXT_MANAGER_DESCRIPTION, clazz);
    }
}