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

import org.gvsig.catalog.CatalogLibrary;
import org.gvsig.catalog.CatalogLocator;
import org.gvsig.catalog.CatalogManager;
import org.gvsig.catalog.csw.drivers.CSWISO19115CatalogServiceDriver;
import org.gvsig.catalog.csw.drivers.CSWebRIMCatalogServiceDriver;
import org.gvsig.catalog.impl.DefaultCatalogManager;
import org.gvsig.catalog.srw.drivers.SRWCatalogServiceDriver;
import org.gvsig.catalog.z3950.drivers.Z3950CatalogServiceDriver;
import org.gvsig.gazetteer.GazetteerLibrary;
import org.gvsig.gazetteer.GazetteerLocator;
import org.gvsig.gazetteer.GazetteerManager;
import org.gvsig.gazetteer.adl.drivers.ADLGazetteerServiceDriver;
import org.gvsig.gazetteer.geonames.GeonamesServiceDriver;
import org.gvsig.gazetteer.idec.drivers.IDECGazetteerServiceDriver;
import org.gvsig.gazetteer.wfs.drivers.WFSServiceDriver;
import org.gvsig.gazetteer.wfsg.drivers.WFSGServiceDriver;
import org.gvsig.tools.locator.ReferenceNotRegisteredException;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class DefaultGazetteerLibrary extends GazetteerLibrary {
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.tools.locator.BaseLibrary#initialize()
	 */
	public void initialize() throws ReferenceNotRegisteredException {
        super.initialize();
		
        //Register the default CatalogManager
        GazetteerLocator.registerGazetteerManager(DefaultGazetteerManager.class);
    }

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.tools.locator.BaseLibrary#postInitialize()
	 */
	public void postInitialize() {
		super.postInitialize();	
		
		//Register the default catalog drivers
		GazetteerManager gazetteerManager = GazetteerLocator.getGazetteerManager();
		gazetteerManager.register("adl", ADLGazetteerServiceDriver.class);
		gazetteerManager.register("wfs", WFSServiceDriver.class);
		gazetteerManager.register("idec", IDECGazetteerServiceDriver.class);
		gazetteerManager.register("wfs-g", WFSGServiceDriver.class);
		gazetteerManager.register("geonames", GeonamesServiceDriver.class);
	}
}

