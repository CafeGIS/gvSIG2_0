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
 * 2009 {DiSiD Technologies}  {Create Manager to register MapContextDrawer implementation}
 */
package org.gvsig.fmap.mapcontext;

/**
 * Manager of the MapContext library.
 * 
 * Holds the default implementation of the {@link MapContextDrawer}.
 * 
 * @author <a href="mailto:cordinyana@gvsig.org">Cèsar Ordiñana</a>
 */
public interface MapContextManager {
    
    /**
     * Sets the class to use as the default implementation for the
     * {@link MapContextDrawer}.
     * 
     * @param drawerClazz
     *            the {@link MapContextDrawer} class to use
     * @throws MapContextException
     *             if there is any error setting the class
     */
    void setDefaultMapContextDrawer(Class drawerClazz)
            throws MapContextException;

    /**
     * Creates a new instance of the default {@link MapContextDrawer}
     * implementation.
     * 
     * @return the new {@link MapContextDrawer} instance
     * @throws MapContextException
     *             if there is an error creating the new object instance
     */
    MapContextDrawer createDefaultMapContextDrawerInstance()
            throws MapContextException;

    /**
     * Creates a new instance of the provided {@link MapContextDrawer}
     * implementation.
     * 
     * @param drawerClazz
     *            the {@link MapContextDrawer} implementation class
     * @return the new {@link MapContextDrawer} instance
     * @throws MapContextException
     *             if there is an error creating the new object instance
     */
    MapContextDrawer createMapContextDrawerInstance(Class drawerClazz)
            throws MapContextException;
}