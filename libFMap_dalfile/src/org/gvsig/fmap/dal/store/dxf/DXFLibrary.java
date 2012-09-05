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

package org.gvsig.fmap.dal.store.dxf;

import org.gvsig.fmap.dal.DALFileLocator;
import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.spi.DataManagerProviderServices;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.dynobject.DynClass;
import org.gvsig.tools.dynobject.DynField;
import org.gvsig.tools.dynobject.DynObjectManager;
import org.gvsig.tools.locator.BaseLibrary;
import org.gvsig.tools.locator.ReferenceNotRegisteredException;


public class DXFLibrary extends BaseLibrary {

	public void postInitialize() throws ReferenceNotRegisteredException {
		super.postInitialize();

		DXFStoreParameters.registerDynClass();
		//		DXFNewStoreParameters.registerDynClass();
		DXFStoreProvider.registerDynClass();

        // DynObjectManager dynObjectManager =
		// ToolsLocator.getDynObjectManager();
		//
		// DynClass dynClass = dynObjectManager.add("DXFStoreParameters",
		// "DXF File Store parameters");
		// dynClass.addDynField("srsid");
		// dynClass.addDynField("filepath");

        DataManagerProviderServices dataman = (DataManagerProviderServices) DALLocator
				.getDataManager();

		if (!dataman.getStoreProviders().contains(DXFStoreProvider.NAME)) {
			dataman.registerStoreProvider(DXFStoreProvider.NAME,
					DXFStoreProvider.class, DXFStoreParameters.class);
		}

		DALFileLocator.getFilesystemServerExplorerManager().registerProvider(
				DXFStoreProvider.NAME, DXFStoreProvider.DESCRIPTION,
				DXFFilesystemServerProvider.class);

		DynObjectManager dynMan = ToolsLocator.getDynObjectManager();
		DynClass dynClass = dynMan.get(
				DXFStoreProvider.DYNCLASS_NAME);
		DynField field;
		if (dynClass == null) {
			dynClass = dynMan.add(
					DXFStoreProvider.DYNCLASS_NAME);

		}
	}
}
