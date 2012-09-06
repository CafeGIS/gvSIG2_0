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
* 2008 Geographic Information research group: http://www.geoinfo.uji.es
* Departamento de Lenguajes y Sistemas Inform�ticos (LSI)
* Universitat Jaume I   
* {{Task}}
*/

package org.gvsig.metadata;

import org.gvsig.tools.locator.BaseLocator;
import org.gvsig.tools.locator.LocatorException;


public class MDLocator extends BaseLocator {

	public static final String METADATA_MANAGER_NAME = "MDManager";
	
	private static final String METADATA_MANAGER_DESCRIPTION = "Compatible implementation for MDManager";
	
	/**
     * Unique instance.
     */
    private static final MDLocator instance = new MDLocator();

    /**
     * Return the singleton instance.
     * 
     * @return the singleton instance
     */
    public static MDLocator getInstance() {
        return instance;
    }
	
	/**
     * Return a reference to MDManager.
     * 
     * @return a reference to MDManager
     * @throws LocatorException
     *             if there is no access to the class or the class cannot be
     *             instantiated
     * @see Locator#get(String)
     */
    public static MDManager getMDManager() throws LocatorException {
        return (MDManager) getInstance().get(METADATA_MANAGER_NAME);
    }
    
    /**
     * Registers the Class implementing the MDManager interface.
     * 
     * @param clazz
     *            implementing the MDManager interface
     */
    public static void registerMDManager(Class clazz) {
        getInstance().register(METADATA_MANAGER_NAME, METADATA_MANAGER_DESCRIPTION, clazz);
    }
	
}