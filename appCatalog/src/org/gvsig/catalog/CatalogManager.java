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
 
package org.gvsig.catalog;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;

import org.gvsig.catalog.drivers.ICatalogServiceDriver;
import org.gvsig.catalog.loaders.LayerLoader;
import org.gvsig.catalog.metadataxml.XMLNode;
import org.gvsig.catalog.schemas.Record;
import org.gvsig.catalog.schemas.Resource;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public interface CatalogManager {
	/**
	 * It is used to retrieve a driver that supports a concrete 
	 * protocol
	 * @param protocol
	 * Catalog protocol
	 * @return
	 * The concrete catalog service driver
	 */
	public ICatalogServiceDriver getDriver(String protocol);
	
	/**
	 * @return a list with all the catalog drivers
	 */
	public ICatalogServiceDriver[] getDrivers();
	
	/**
	 * This method is used to register a new catalog driver 
	 * that manage a concrete protocol
	 * @param driver
	 * Catalog driver to register
	 */
	public void register(String name, Class driver);
	
	/**
	 * Adds a new record
	 * @param record
	 * New record to add
	 */
	public void addRecord(Record record);	

	/**
	 * Try to identify the XML format and return a record
	 * @param uri
	 * Server URI (used to retrieve the images)
	 * @param node
	 * XML node
	 * @return
	 */
	public Record createRecord(URI uri, XMLNode node);
	
	public void addLayerLoader(String key, Class loader);
	
	public LayerLoader getLayerLoader(Resource resource) throws IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException;
		
		
}

