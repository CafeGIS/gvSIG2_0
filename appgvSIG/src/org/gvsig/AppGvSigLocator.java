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
* 2009 IVER T.I   {{Task}}
*/

package org.gvsig;

import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.locator.BaseLocator;
import org.gvsig.tools.locator.Locator;
import org.gvsig.tools.locator.LocatorException;

public class AppGvSigLocator extends BaseLocator {

	private static final String LOCATOR_NAME = "appgsigLocator";

	public static final String APPGVSIG_MANAGER_NAME = "appgsigLocator.manager.persistence";

	private static final String APPGVSIG_MANAGER_DESCRIPTION = "Manager of appgvSIG";
	
	/**
	 * Unique instance.
	 */
	private static final ToolsLocator instance = new ToolsLocator();

	/**
	 * Return the singleton instance.
	 *
	 * @return the singleton instance
	 */
	public static ToolsLocator getInstance() {
		return instance;
	}

	public String getLocatorName() {
		return LOCATOR_NAME;
	}

	/**
	 * Return a reference to PersistenceManager.
	 *
	 * @return a reference to PersistenceManager
	 * @throws LocatorException
	 *             if there is no access to the class or the class cannot be
	 *             instantiated
	 * @see Locator#get(String)
	 */
	public static AppGvSigManager getAppGvSigManager()
			throws LocatorException {
		return (AppGvSigManager) getInstance().get(APPGVSIG_MANAGER_NAME);
	}

	/**
	 * Registers the Class implementing the PersistenceManager interface.
	 *
	 * @param clazz
	 *            implementing the PersistenceManager interface
	 */
	public static void registerAppGvSigManager(Class clazz) {
		getInstance().register(APPGVSIG_MANAGER_NAME,
				APPGVSIG_MANAGER_DESCRIPTION, clazz);
	}

	public static void registerDefaultAppGvSigManager(Class clazz) {
		getInstance().registerDefault(APPGVSIG_MANAGER_NAME,
				APPGVSIG_MANAGER_DESCRIPTION, clazz);
	}		
}
