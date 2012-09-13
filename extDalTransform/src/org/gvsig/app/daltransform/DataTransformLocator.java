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
 
package org.gvsig.app.daltransform;

import org.gvsig.tools.locator.AbstractLocator;
import org.gvsig.tools.locator.Locator;
import org.gvsig.tools.locator.LocatorException;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class DataTransformLocator extends AbstractLocator {
	private static final String LOCATOR_NAME = "DataTransformLocator";
	/**
	 * GPEManager name used by the locator to access the instance
	 */
	public static final String DATA_TRANSFORM_MANAGER_NAME = "DataTransformManager";
	private static final String DATA_TRANSFORM_MANAGER_DESCRIPTION = "DataTransformManager of gvSIG";

	/**
	 * Unique instance.
	 */
	private static final DataTransformLocator instance = new DataTransformLocator();
	
	/* (non-Javadoc)
	 * @see org.gvsig.tools.locator.Locator#getLocatorName()
	 */
	public String getLocatorName() {
		return LOCATOR_NAME;
	}
	
	/**
	 * Return a reference to {@link DataTransformManager}.
	 *
	 * @return a reference to DataTransformManager
	 * @throws LocatorException
	 *             if there is no access to the class or the class cannot be
	 *             instantiated
	 * @see Locator#get(String)
	 */
	public static DataTransformManager getDataTransformManager() throws LocatorException {
		return (DataTransformManager) getInstance().get(DATA_TRANSFORM_MANAGER_NAME);
	}
	
	/**
	 * Return the singleton instance.
	 *
	 * @return the singleton instance
	 */
	public static DataTransformLocator getInstance() {
		return instance;
	}
	
	/**
	 * Registers the Class implementing the {@link DataTransformManager} interface.
	 *
	 * @param clazz
	 *            implementing the DataTransformManager interface
	 */
	public static void registerDataTransformManager(Class clazz) {
		getInstance().register(DATA_TRANSFORM_MANAGER_NAME, 
				DATA_TRANSFORM_MANAGER_DESCRIPTION,
				clazz);
	}
}
