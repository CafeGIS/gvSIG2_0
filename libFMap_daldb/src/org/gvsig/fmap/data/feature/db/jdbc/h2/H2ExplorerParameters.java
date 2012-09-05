package org.gvsig.fmap.data.feature.db.jdbc.h2;

import java.util.Map;

import org.gvsig.fmap.data.feature.db.jdbc.JDBCExplorerParameter;


public class H2ExplorerParameters extends JDBCExplorerParameter {
	protected Map createDefaultValuesMap() {
		Map defaultValues = super.createDefaultValuesMap();
		defaultValues.put("port", "");//TODO poner el puerto por defecto
		return defaultValues;
	}

	public String getUrl() {
		return H2Utils.getJDBCUrl(this.getHost(), this.getDb());
	}

	public String getDataExplorerName() {
		return H2Explorer.DATAEXPLORER_NAME;
	}

	public String getDescription() {
		return "H2 DataBase";
	}

	public String getDataStoreName() {
		return H2Store.DATASTORE_NAME;
	}

}
