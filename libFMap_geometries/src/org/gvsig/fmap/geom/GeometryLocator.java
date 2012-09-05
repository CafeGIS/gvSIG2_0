/* gvSIG. Geographic Information System of the Valencian Government
*
* Copyright (C) 2007-2008 Infrastructures and Transports Department
* of the Valencian Government (CIT)
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
* 2009 {Iver T.I.}   {Task}
*/
 
package org.gvsig.fmap.geom;

import org.gvsig.tools.locator.AbstractLocator;
import org.gvsig.tools.locator.Locator;
import org.gvsig.tools.locator.LocatorException;

/**
 * This Locator provides the entry point for the gvSIG 
 * {@link GeometryManager}. 
 * @see {@link Locator}
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class GeometryLocator extends AbstractLocator {

	private static final String LOCATOR_NAME = "GeometryLocator";
	
	/**
	 * GeometryManager name used by the locator to access the instance
	 */
	public static final String GEOMETRY_MANAGER_NAME = "GeometryManager";
	
	private static final String GEOMETRY_MANAGER_DESCRIPTION = "GeometryManager of gvSIG";
	
	/**
	 * Unique instance.
	 */
	private static final GeometryLocator instance = new GeometryLocator();

	/**
	 * Return the singleton instance.
	 *
	 * @return the singleton instance
	 */
	public static GeometryLocator getInstance() {
		return instance;
	}
	
	/* (non-Javadoc)
	 * @see org.gvsig.tools.locator.Locator#getLocatorName()
	 */
	public String getLocatorName() {
		return LOCATOR_NAME;
	}
	
	/**
	 * Return a reference to {@link GeometryManager}.
	 *
	 * @return a reference to GeometryManager
	 * @throws LocatorException
	 *             if there is no access to the class or the class cannot be
	 *             instantiated
	 * @see Locator#get(String)
	 */
	public static GeometryManager getGeometryManager() throws LocatorException {
		return (GeometryManager) getInstance().get(GEOMETRY_MANAGER_NAME);
	}
	
	/**
	 * Registers the Class implementing the {@link GeometryManager} interface.
	 *
	 * @param clazz
	 *            implementing the GeometryManager interface
	 */
	public static void registerGeometryManager(Class clazz) {
		getInstance().register(GEOMETRY_MANAGER_NAME, GEOMETRY_MANAGER_DESCRIPTION,
				clazz);
	}
}
