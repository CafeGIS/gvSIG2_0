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
* 2008 IVER T.I. S.A.   {{Task}}
*/

package org.gvsig.fmap.dal;

import org.gvsig.fmap.dal.serverexplorer.filesystem.spi.FilesystemServerExplorerManager;
import org.gvsig.tools.locator.AbstractLocator;
import org.gvsig.tools.locator.Locator;
import org.gvsig.tools.locator.LocatorException;

public class DALFileLocator extends AbstractLocator {


	private static final String LOCATOR_NAME = "DALFileLocator";

	/**
	 * DataManager name used by the locator to access the instance
	 */
	public static final String FILESYSTEMSERVEREXPLORERE_MANAGER_NAME = "FilesystemServerExplorereManager";

	private static final String FILESYSTEMSERVEREXPLORERE_MANAGER_DESCRIPTION = "FilesystemServerExplorereManager of gvSIG Data Access Library";

	/**
	 * Unique instance.
	 */
	private static final DALFileLocator instance = new DALFileLocator();

	/**
	 * Return the singleton instance.
	 *
	 * @return the singleton instance
	 */
	public static DALFileLocator getInstance() {
		return instance;
	}

	public String getLocatorName() {
		return LOCATOR_NAME;
	}

	/**
	 * Return a reference to FilesystemServerExplorerManager.
	 *
	 * @return a reference to FilesystemServerExplorerManager
	 * @throws LocatorException
	 *             if there is no access to the class or the class cannot be
	 *             instantiated
	 * @see Locator#get(String)
	 */
	public static FilesystemServerExplorerManager getFilesystemServerExplorerManager()
			throws LocatorException {
		return (FilesystemServerExplorerManager) getInstance().get(
				FILESYSTEMSERVEREXPLORERE_MANAGER_NAME);
	}

	/**
	 * Registers the Class implementing the FilesystemSeverExplorerManager
	 * interface.
	 * 
	 * @param clazz
	 *            implementing the FilesystemSeverExplorerManager interface
	 */
	public static void registerFilesystemSeverExplorerManager(Class clazz) {
		getInstance().register(FILESYSTEMSERVEREXPLORERE_MANAGER_NAME,
				FILESYSTEMSERVEREXPLORERE_MANAGER_DESCRIPTION,
				clazz);
	}

}
