package org.gvsig.fmap.data.feature.db.jdbc.postgresql;

import org.gvsig.fmap.data.feature.db.jdbc.JDBCStoreParameters;

public class PostgresqlStoreParameters extends JDBCStoreParameters{

	public PostgresqlStoreParameters(){
		this.setPort("5432");
	}
	public String getDataStoreName() {
		return PostgresqlStore.DATASTORE_NAME;
	}

	public String getUrl() {
		return PostgresqlStoreUtils.getJDBCUrl(this.getHost(), this.getDb(),this.getPort());
	}

	public String getDescription() {
		return "Postgres Database";
	}

}
