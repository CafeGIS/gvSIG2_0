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
 
package org.gvsig.fmap.dal.store.wfs;

import org.gvsig.fmap.dal.DALLibrary;
import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.serverexplorer.wfs.WFSServerExplorer;
import org.gvsig.fmap.dal.serverexplorer.wfs.WFSServerExplorerParameters;
import org.gvsig.fmap.dal.spi.DataManagerProviderServices;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.extensionpoint.ExtensionPoint;
import org.gvsig.tools.extensionpoint.ExtensionPointManager;
import org.gvsig.tools.locator.BaseLibrary;
import org.gvsig.tools.locator.ReferenceNotRegisteredException;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class WFSLibrary extends BaseLibrary {

	public void initialize() throws ReferenceNotRegisteredException {
		super.initialize();

		DALLibrary dal = new DALLibrary();
		dal.initialize();
	}

	public void postInitialize() throws ReferenceNotRegisteredException {     
		super.postInitialize();

		WFSStoreParameters.registerDynClass();
		WFSStoreProvider.registerDynClass();

		DataManagerProviderServices dataman = (DataManagerProviderServices) DALLocator
		.getDataManager();

		if (!dataman.getStoreProviders().contains(WFSStoreProvider.NAME)) {
			dataman.registerStoreProvider(WFSStoreProvider.NAME,
					WFSStoreProvider.class, WFSStoreParameters.class);
		}	
		
		if (!dataman.getExplorerProviders().contains(WFSServerExplorer.NAME)){
			dataman.registerExplorerProvider(WFSServerExplorer.NAME,
					WFSServerExplorer.class, WFSServerExplorerParameters.class);
		}	
		
		//Register the WFSStroreParams to be loaded from the catalog extension
		ExtensionPointManager extensionPointManager = ToolsLocator
		.getExtensionPointManager();
		ExtensionPoint extensionPoint = extensionPointManager.add("CatalogDataStoreParameters");
		extensionPoint.append("OGC:WFS", "Data store parameters to load a WFS layer", WFSStoreParameters.class);
	}
}



