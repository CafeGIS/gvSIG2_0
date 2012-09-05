package org.gvsig.fmap.data.feature.db.jdbc.h2;

import org.gvsig.fmap.data.feature.db.jdbc.JDBCStoreParameters;

public class H2StoreParameters extends JDBCStoreParameters{

	public String getDataStoreName() {
		return H2Store.DATASTORE_NAME;
	}
	public String getUrl() {
		return H2Utils.getJDBCUrl(this.getHost(), this.getDb());
	}

	public String getDescription() {
		return "H2 Database";
	}


}
