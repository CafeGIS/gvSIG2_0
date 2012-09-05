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
 
package org.gvsig.catalog.impl;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

import org.gvsig.catalog.CatalogManager;
import org.gvsig.catalog.drivers.ICatalogServiceDriver;
import org.gvsig.catalog.loaders.LayerLoader;
import org.gvsig.catalog.metadataxml.XMLNode;
import org.gvsig.catalog.schemas.Record;
import org.gvsig.catalog.schemas.Resource;
import org.gvsig.catalog.schemas.UnknownRecord;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.extensionpoint.ExtensionPoint;
import org.gvsig.tools.extensionpoint.ExtensionPointManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class DefaultCatalogManager implements CatalogManager {
	private static final Logger logger = LoggerFactory.getLogger(DefaultCatalogManager.class);
	private static final String DRIVER_REGISTER_NAME = "CatalogDrivers";
	private ArrayList records = null; 
	private static TreeMap loadersPool = null;
	
	public DefaultCatalogManager() {
		super();
		records = new ArrayList();
		loadersPool = new TreeMap();
	}

	/* (non-Javadoc)
	 * @see org.gvsig.catalog.CatalogManager#getDriver(java.lang.String)
	 */
	public ICatalogServiceDriver getDriver(String protocol) {
		ExtensionPointManager extensionPointManager = ToolsLocator
		.getExtensionPointManager();
		ExtensionPoint extensionPoint = extensionPointManager.add(DRIVER_REGISTER_NAME);
		
		Iterator extensions = extensionPoint.iterator();
		while (extensions.hasNext()){
			ExtensionPoint.Extension extension = (ExtensionPoint.Extension)extensions.next();
			ICatalogServiceDriver driver;
			try {
				driver = (ICatalogServiceDriver)extension.create();
				if (driver.getServiceName().toLowerCase().compareTo(protocol.toLowerCase()) == 0){
					return driver;
				}
			} catch (InstantiationException e) {
				logger.error("Impossible to create a catalog driver", e);
			} catch (IllegalAccessException e) {
				logger.error("Impossible to create a catalog driver", e);
			}			
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.catalog.CatalogManager#getDrivers()
	 */
	public ICatalogServiceDriver[] getDrivers() {
		ICatalogServiceDriver[] drivers = null;
		ExtensionPointManager extensionPointManager = ToolsLocator
		.getExtensionPointManager();
		ExtensionPoint extensionPoint = extensionPointManager.add(DRIVER_REGISTER_NAME);
		drivers = new ICatalogServiceDriver[extensionPoint.getCount()];
		Iterator extensions = extensionPoint.iterator();		
		int i = 0;		
		while (extensions.hasNext()){
			ExtensionPoint.Extension extension = (ExtensionPoint.Extension)extensions.next();
			ICatalogServiceDriver driver;
			try {
				driver = (ICatalogServiceDriver)extension.create();
				drivers[i] = driver;				
			} catch (InstantiationException e) {
				logger.error("Impossible to create a catalog driver", e);
			} catch (IllegalAccessException e) {
				logger.error("Impossible to create a catalog driver", e);
			}			
			
			i++;
		}
		return drivers;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.catalog.CatalogManager#register(java.lang.String, java.lang.Class)
	 */
	public void register(String name, Class driver) {
		ExtensionPointManager extensionPointManager = ToolsLocator
		.getExtensionPointManager();
		ExtensionPoint extensionPoint = extensionPointManager.add(DRIVER_REGISTER_NAME);
		extensionPoint.append(name.toLowerCase(), "", driver);
	}

	/* (non-Javadoc)
	 * @see org.gvsig.catalog.CatalogManager#addRecord(org.gvsig.catalog.schemas.Record)
	 */
	public void addRecord(Record record) {
		records.add(record);		
	}

	/* (non-Javadoc)
	 * @see org.gvsig.catalog.CatalogManager#createRecord(java.net.URI, org.gvsig.catalog.metadataxml.XMLNode)
	 */
	public Record createRecord(URI uri, XMLNode node) {
		for (int i=0 ; i<records.size() ; i++){
			Record record = (Record)records.get(i);
			if (node != null){
				if (record.accept(uri, node)){
					Object[] values = {uri, node};
					Class[] types = {URI.class, XMLNode.class}; 
					try {
						return (Record)record.getClass().getConstructor(types).newInstance(values);
					} catch (Exception e) {
						//It the instance can be created the 
						//default record has to be returned
					} 
				}	
			}
		}
		return new UnknownRecord(uri,node);	
	}

	/* (non-Javadoc)
	 * @see org.gvsig.catalog.CatalogManager#addLayerLoader(java.lang.String, java.lang.Class)
	 */
	public void addLayerLoader(String key, Class loader) {
		loadersPool.put(key, loader);		
	}

	/* (non-Javadoc)
	 * @see org.gvsig.catalog.CatalogManager#getLayerLoader(org.gvsig.catalog.schemas.Resource)
	 */
	public LayerLoader getLayerLoader(Resource resource)
			throws IllegalArgumentException, SecurityException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		if (loadersPool.containsKey(resource.getType())) {
			Class llClass = (Class) loadersPool.get(resource.getType());
			Class [] args = {Resource.class};
			Object [] params = {resource};
			return (LayerLoader) llClass.getConstructor(args).newInstance(params);
		}
		return null;
	}

}

