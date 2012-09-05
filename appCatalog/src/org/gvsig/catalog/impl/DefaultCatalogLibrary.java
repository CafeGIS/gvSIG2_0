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

import org.gvsig.catalog.CatalogLibrary;
import org.gvsig.catalog.CatalogLocator;
import org.gvsig.catalog.CatalogManager;
import org.gvsig.catalog.csw.drivers.CSWISO19115CatalogServiceDriver;
import org.gvsig.catalog.csw.drivers.CSWebRIMCatalogServiceDriver;
import org.gvsig.catalog.schemas.DeegreeISO19115Record;
import org.gvsig.catalog.schemas.DublinCoreRecord;
import org.gvsig.catalog.schemas.GeonetworkISO19115Record;
import org.gvsig.catalog.schemas.IdecISO19115Record;
import org.gvsig.catalog.schemas.IdeeISO19115Record;
import org.gvsig.catalog.schemas.Iso19139Record;
import org.gvsig.catalog.schemas.LaitsGmuEbRIMRecord;
import org.gvsig.catalog.schemas.LaitsGmuISO19115Record;
import org.gvsig.catalog.schemas.LaitsGmuServicesRecord;
import org.gvsig.catalog.srw.drivers.SRWCatalogServiceDriver;
import org.gvsig.catalog.z3950.drivers.Z3950CatalogServiceDriver;
import org.gvsig.tools.locator.ReferenceNotRegisteredException;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class DefaultCatalogLibrary extends CatalogLibrary {
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.tools.locator.BaseLibrary#initialize()
	 */
	public void initialize() throws ReferenceNotRegisteredException {
        super.initialize();
		
        //Register the default CatalogManager
        CatalogLocator.registerCatalogManager(DefaultCatalogManager.class);
    }

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.tools.locator.BaseLibrary#postInitialize()
	 */
	public void postInitialize() {
		super.postInitialize();	
		
		//Register the default catalog drivers
		CatalogManager catalogManager = CatalogLocator.getCatalogManager();
		catalogManager.register("z3950", Z3950CatalogServiceDriver.class);
		catalogManager.register("srw", SRWCatalogServiceDriver.class);
		catalogManager.register("csw/iso 19115", CSWISO19115CatalogServiceDriver.class);
		catalogManager.register("csw/ebrim", CSWebRIMCatalogServiceDriver.class);
		
		//Default the records
		catalogManager.addRecord(new GeonetworkISO19115Record());
		catalogManager.addRecord(new DeegreeISO19115Record());
		catalogManager.addRecord(new DublinCoreRecord());
		catalogManager.addRecord(new IdecISO19115Record());
		catalogManager.addRecord(new IdeeISO19115Record());
		catalogManager.addRecord(new Iso19139Record());
		catalogManager.addRecord(new LaitsGmuISO19115Record());
		catalogManager.addRecord(new LaitsGmuServicesRecord());
		catalogManager.addRecord(new LaitsGmuEbRIMRecord());
	}
}

