package org.gvsig.fmap.data.feature.db.jdbc.h2;

import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.impl.DefaultDataManager;

public class Register {
	public static void selfRegister() {
		DataManager dm = DefaultDataManager.getManager();

		dm.registerStoreProvider(H2Store.DATASTORE_NAME,
				H2Store.class,
				H2StoreParameters.class
		);

		dm.registerExplorerProvider(
				H2Explorer.DATAEXPLORER_NAME,
				H2Explorer.class,
				H2ExplorerParameters.class
		);
	}


}
