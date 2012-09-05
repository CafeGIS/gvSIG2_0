package org.gvsig.fmap.data.feature.db.jdbc.postgresqlbin;

import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.impl.DefaultDataManager;

public class Register {
	public static void selfRegister() {
		DataManager dsm = DefaultDataManager.getManager();

		dsm.registerStoreProvider(PostgresqlBinStore.DATASTORE_NAME,
				PostgresqlBinStore.class,
				PostgresqlBinStoreParameters.class
		);
	}


}
