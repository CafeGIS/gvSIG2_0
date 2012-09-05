package org.gvsig.fmap.dal.store.lidar;

import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.spi.DataManagerProviderServices;
import org.gvsig.tools.locator.BaseLibrary;
import org.gvsig.tools.locator.ReferenceNotRegisteredException;

public class LiDARLibrary extends BaseLibrary {
	public void postInitialize() throws ReferenceNotRegisteredException {
		super.postInitialize();

		LiDARStoreParameters.registerDynClass();
		// LiDARNewStoreParameters.registerDynClass();
		LiDARStoreProvider.registerDynClass();
		//
		DataManagerProviderServices dataman = (DataManagerProviderServices) DALLocator
				.getDataManager();

		if (!dataman.getStoreProviders().contains(LiDARStoreProvider.NAME)) {
			dataman.registerStoreProvider(LiDARStoreProvider.NAME,
					LiDARStoreProvider.class, LiDARStoreParameters.class);
		}


		// DALFileLocator.getFilesystemServerExplorerManager().registerProvider(
		// LiDARStoreProvider.NAME, LiDARStoreProvider.DESCRIPTION,
		// LiDARFilesystemServerProvider.class);
	}

}
