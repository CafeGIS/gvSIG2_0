package org.gvsig.fmap.data.feature.db.jdbc.postgresql;

import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.impl.DefaultDataManager;

public class Register {
	public static void selfRegister() {
		DataManager dsm = DefaultDataManager.getManager();

		dsm.registerStoreProvider(PostgresqlStore.DATASTORE_NAME,
				PostgresqlStore.class,
				PostgresqlStoreParameters.class
		);

		dsm.registerExplorerProvider(PostgresqlExplorer.DATAEXPLORER_NAME,
				PostgresqlExplorer.class,
				PostgresqlExplorerParameters.class);
	}


}
