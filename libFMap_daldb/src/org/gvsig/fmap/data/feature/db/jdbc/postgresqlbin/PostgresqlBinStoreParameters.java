package org.gvsig.fmap.data.feature.db.jdbc.postgresqlbin;

import org.gvsig.fmap.data.feature.db.jdbc.postgresql.PostgresqlStoreParameters;

public class PostgresqlBinStoreParameters extends PostgresqlStoreParameters{

	public String getDataStoreName() {
		return PostgresqlBinStore.DATASTORE_NAME;
	}

	public String getUrl() {
		return PostgresqlBinStoreUtils.getJDBCUrl(this.getHost(), this.getDb(),this.getPort());
	}

}
