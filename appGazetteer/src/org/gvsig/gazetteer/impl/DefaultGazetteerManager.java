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
 
package org.gvsig.gazetteer.impl;

import java.util.Iterator;

import org.gvsig.gazetteer.GazetteerManager;
import org.gvsig.gazetteer.drivers.IGazetteerServiceDriver;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.extensionpoint.ExtensionPoint;
import org.gvsig.tools.extensionpoint.ExtensionPointManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class DefaultGazetteerManager implements GazetteerManager{
	private static final Logger logger = LoggerFactory.getLogger(DefaultGazetteerManager.class);
	private static final String DRIVER_REGISTER_NAME = "GazetteerDrivers";	
	
	/* (non-Javadoc)
	 * @see org.gvsig.gazetteer.GazetteerManager#getDriver(java.lang.String)
	 */
	public IGazetteerServiceDriver getDriver(String protocol) {
		ExtensionPointManager extensionPointManager = ToolsLocator
		.getExtensionPointManager();
		ExtensionPoint extensionPoint = extensionPointManager.add(DRIVER_REGISTER_NAME);
		
		Iterator extensions = extensionPoint.iterator();
		while (extensions.hasNext()){
			ExtensionPoint.Extension extension = (ExtensionPoint.Extension)extensions.next();
			IGazetteerServiceDriver driver;
			try {
				driver = (IGazetteerServiceDriver)extension.create();
				if (driver.getServiceName().toLowerCase().compareTo(protocol.toLowerCase()) == 0){
					return driver;
				}
			} catch (InstantiationException e) {
				logger.error("Impossible to create a gazetteer driver", e);
			} catch (IllegalAccessException e) {
				logger.error("Impossible to create a gazetteer driver", e);
			}			
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.gazetteer.GazetteerManager#getDrivers()
	 */
	public IGazetteerServiceDriver[] getDrivers() {
		IGazetteerServiceDriver[] drivers = null;
		ExtensionPointManager extensionPointManager = ToolsLocator
		.getExtensionPointManager();
		ExtensionPoint extensionPoint = extensionPointManager.add(DRIVER_REGISTER_NAME);
		drivers = new IGazetteerServiceDriver[extensionPoint.getCount()];
		Iterator extensions = extensionPoint.iterator();		
		int i = 0;		
		while (extensions.hasNext()){
			ExtensionPoint.Extension extension = (ExtensionPoint.Extension)extensions.next();
			IGazetteerServiceDriver driver;
			try {
				driver = (IGazetteerServiceDriver)extension.create();
				drivers[i] = driver;				
			} catch (InstantiationException e) {
				logger.error("Impossible to create a gazetteer driver", e);
			} catch (IllegalAccessException e) {
				logger.error("Impossible to create a gazetteer driver", e);
			}			
			
			i++;
		}
		return drivers;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.gazetteer.GazetteerManager#register(java.lang.String)
	 */
	public void register(String name, Class driver) {
		ExtensionPointManager extensionPointManager = ToolsLocator
		.getExtensionPointManager();
		ExtensionPoint extensionPoint = extensionPointManager.add(DRIVER_REGISTER_NAME);
		extensionPoint.append(name.toLowerCase(), "", driver);
	}

}

