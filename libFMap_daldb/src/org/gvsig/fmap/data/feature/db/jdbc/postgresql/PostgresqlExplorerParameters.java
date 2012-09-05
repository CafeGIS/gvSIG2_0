package org.gvsig.fmap.data.feature.db.jdbc.postgresql;

import java.util.Map;

import org.gvsig.fmap.data.feature.db.jdbc.JDBCExplorerParameter;


public class PostgresqlExplorerParameters extends JDBCExplorerParameter {

	protected Map createDefaultValuesMap() {
		Map defaultValues = super.createDefaultValuesMap();
		defaultValues.put("port", "5432");
		return defaultValues;
	}

	public String getUrl() {
		return PostgresqlStoreUtils.getJDBCUrl(this.getHost(), this.getDb(),
				this.getPort());

	}

	public String getDataExplorerName() {
		return PostgresqlExplorer.DATAEXPLORER_NAME;
	}

	public String getDescription() {
		return "Postgresql DataBase";
	}

	public String getDataStoreName() {
		return PostgresqlStore.DATASTORE_NAME;
	}

}
