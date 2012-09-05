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

package org.gvsig.fmap.dal.store.mysql;

import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.resource.spi.ResourceManagerProviderServices;
import org.gvsig.fmap.dal.spi.DataManagerProviderServices;
import org.gvsig.tools.locator.BaseLibrary;
import org.gvsig.tools.locator.ReferenceNotRegisteredException;


public class MySQLLibrary extends BaseLibrary {


	static String DEFAULT_JDCB_DRIVER_NAME = "com.mysql.jdbc.Driver";


	public static String getJdbcUrl(String host, Integer port, String db) {
		String url;
		String sport = "";
		if (port != null) {
			sport = ":" + port;
		}
		url = "jdbc:mysql://" + host + sport + "/" + db;

		return url;
	}


	public void initialize() throws ReferenceNotRegisteredException {
		super.initialize();

	}

	public void postInitialize() throws ReferenceNotRegisteredException {
		super.postInitialize();

    	ResourceManagerProviderServices resman = (ResourceManagerProviderServices) DALLocator
				.getResourceManager();

    	MySQLResourceParameters.registerDynClass();

    	if (!resman.getResourceProviders().contains(MySQLResource.NAME)) {
			resman.register(MySQLResource.NAME,
					MySQLResource.DESCRIPTION, MySQLResource.class,
					MySQLResourceParameters.class);
		}


    	MySQLStoreParameters.registerDynClass();
    	MySQLNewStoreParameters.registerDynClass();
		MySQLStoreProvider.registerDynClass();

        DataManagerProviderServices dataman = (DataManagerProviderServices) DALLocator
				.getDataManager();

		if (!dataman.getStoreProviders().contains(MySQLStoreProvider.NAME)) {
			dataman.registerStoreProvider(MySQLStoreProvider.NAME,
					MySQLStoreProvider.class,
					MySQLStoreParameters.class);
		}

		if (!dataman.getExplorerProviders().contains(
				MySQLStoreProvider.NAME)) {
			dataman.registerExplorerProvider(MySQLServerExplorer.NAME,
					MySQLServerExplorer.class,
					MySQLServerExplorerParameters.class);
		}



	}

}
