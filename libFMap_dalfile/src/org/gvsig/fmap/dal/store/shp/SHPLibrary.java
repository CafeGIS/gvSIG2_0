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

package org.gvsig.fmap.dal.store.shp;

import org.gvsig.fmap.dal.DALFileLocator;
import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.spi.DataManagerProviderServices;
import org.gvsig.fmap.dal.store.dbf.DBFLibrary;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.dynobject.DynObjectManager;
import org.gvsig.tools.locator.ReferenceNotRegisteredException;


public class SHPLibrary extends DBFLibrary {


	public void postInitialize() throws ReferenceNotRegisteredException {
		super.postInitialize();

		SHPStoreParameters.registerDynClass();
		SHPNewStoreParameters.registerDynClass();
		SHPStoreProvider.registerDynClass();

		DynObjectManager dynObjectManager = ToolsLocator.getDynObjectManager();

        // DynClass dbfDynClass = dynObjectManager.get("DBFStoreParameters");
        // DynClass shpDynClass = dynObjectManager.add("SHPStoreParameters",
        // "Shape File Store parameters");
        // shpDynClass.addDynField("shpfilename");
        // shpDynClass.addDynField("shxfilename");
        // shpDynClass.extend(dbfDynClass);
        //
        // shpDynClass = dynObjectManager.add("SHPNewStoreParameters",
        // "Shape File New Store parameters");
        // shpDynClass.addDynField("shpfilename");
        // shpDynClass.addDynField("shxfilename");
        // shpDynClass.extend(dbfDynClass);

		DataManagerProviderServices dataman = (DataManagerProviderServices) DALLocator
				.getDataManager();

		if (!dataman.getStoreProviders().contains(SHPStoreProvider.NAME)) {
			dataman.registerStoreProvider(SHPStoreProvider.NAME,
					SHPStoreProvider.class, SHPStoreParameters.class);
		}

		DALFileLocator.getFilesystemServerExplorerManager().registerProvider(
				SHPStoreProvider.NAME, SHPStoreProvider.DESCRIPTION,
				SHPFilesystemServerProvider.class);
	}

}
