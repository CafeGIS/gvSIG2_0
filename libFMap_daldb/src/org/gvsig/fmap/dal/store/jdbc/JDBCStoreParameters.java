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
* 2009 IVER T.I   {{Task}}
*/

package org.gvsig.fmap.dal.store.jdbc;

import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.store.db.DBStoreParameters;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.dynobject.DelegatedDynObject;
import org.gvsig.tools.dynobject.DynClass;
import org.gvsig.tools.dynobject.DynField;
import org.gvsig.tools.dynobject.DynObjectManager;

public class JDBCStoreParameters extends DBStoreParameters
		implements
		JDBCConnectionParameters {

	public static final String DYNCLASS_NAME = "JDBCStoreParameters";


	public JDBCStoreParameters() {
		super();
		initialize();
	}

	protected void initialize() {
		DynObjectManager dynman = ToolsLocator.getDynObjectManager();
		DynClass dynClass = dynman.get(DYNCLASS_NAME);
		this.delegatedDynObject = (DelegatedDynObject) dynman
				.createDynObject(dynClass);
	}

	public String getDataStoreName() {
		return JDBCStoreProvider.NAME;
	}

	public String getDescription() {
		return JDBCStoreProvider.DESCRIPTION;
	}



    protected static void registerDynClass() {
		DynObjectManager dynman = ToolsLocator.getDynObjectManager();
		DynClass dynClass = dynman.get(DYNCLASS_NAME);
		DynField field;
		if (dynClass == null) {
			dynClass = dynman.add(DYNCLASS_NAME);

			dynClass.extend(JDBCResourceParameters.DYNCLASS_NAME);

			dynClass.extend(DBStoreParameters.DYNCLASS_NAME);

			field = dynClass.addDynField(DYNFIELDNAME_JDBC_DRIVER_CLASS_NAME);
			field.setTheTypeOfAvailableValues(DynField.SINGLE);
			field.setDescription("JDBC Driver class");
			field.setMandatory(true);
			field.setType(DataTypes.STRING);

			field = dynClass.addDynField(DYNFIELDNAME_CATALOG);
			field.setTheTypeOfAvailableValues(DynField.SINGLE);
			field.setDescription("DB Catalog");
			field.setType(DataTypes.STRING);

			field = dynClass.addDynField(DYNFIELDNAME_SCHEMA);
			field.setTheTypeOfAvailableValues(DynField.SINGLE);
			field.setDescription("DB Schema");
			field.setType(DataTypes.STRING);
		}
	}


	public boolean isValid() {
		return this.getHost() != null;
	}

	public String getHost() {
		return (String) this.getDynValue(DYNFIELDNAME_HOST);
	}

	public Integer getPort() {
		return (Integer) this.getDynValue(DYNFIELDNAME_PORT);
	}

	public String getDBName() {
		return (String) this.getDynValue(DYNFIELDNAME_DBNAME);
	}

	public String getUser() {
		return (String) this.getDynValue(DYNFIELDNAME_USER);
	}

	public String getPassword() {
		return (String) this.getDynValue(DYNFIELDNAME_PASSWORD);
	}

	public void setHost(String host) {
		this.setDynValue(DYNFIELDNAME_HOST, host);
	}

	public void setPort(int port) {
		this.setDynValue(DYNFIELDNAME_PORT, new Integer(port));
	}

	public void setPort(Integer port) {
		this.setDynValue(DYNFIELDNAME_PORT, port);
	}

	public void setDBName(String dbName) {
		this.setDynValue(DYNFIELDNAME_DBNAME, dbName);
	}

	public void setUser(String user) {
		this.setDynValue(DYNFIELDNAME_USER, user);
	}

	public void setPassword(String password) {
		this.setDynValue(DYNFIELDNAME_PASSWORD, password);
	}

	public void setJDBCDriverClassName(String className) {
		this.setDynValue(DYNFIELDNAME_JDBC_DRIVER_CLASS_NAME, className);
	}

	public String getJDBCDriverClassName() {
		return (String) this.getDynValue(DYNFIELDNAME_JDBC_DRIVER_CLASS_NAME);
	}

	public String getCatalog() {
		return (String) this.getDynValue(DYNFIELDNAME_CATALOG);
	}

	public void setCatalog(String catalog) {
		this.setDynValue(DYNFIELDNAME_CATALOG, catalog);
	}

	public String getSchema() {
		return (String) this.getDynValue(DYNFIELDNAME_SCHEMA);
	}

	public void setSchema(String schema) {
		this.setDynValue(DYNFIELDNAME_SCHEMA, schema);
	}

	public String getTable() {
		return (String) this.getDynValue(DYNFIELDNAME_TABLE);
	}

	public void setTable(String table) {
		this.setDynValue(DYNFIELDNAME_TABLE, table);
	}

	public String getFieldsString() {
		return (String) this.getDynValue(DYNFIELDNAME_FIELDS);
	}

	public String[] getFields() {
		String fields = (String) this.getDynValue(DYNFIELDNAME_FIELDS);
		if (fields == null) {
			return null;
		}
		// FIXME check for fields with spaces and special chars
		return fields.split(",");
	}

	public void setFields(String fields) {
		this.setDynValue(DYNFIELDNAME_FIELDS, fields);
	}

	public void setFields(String[] fields) {
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < fields.length - 1; i++) {
			str.append(fields[i]);
			str.append(",");
		}
		str.append(fields.length - 1);

		this.setDynValue(DYNFIELDNAME_FIELDS, fields);
	}

	public String getSQL() {
		return (String) this.getDynValue(DYNFIELDNAME_SQL);
	}

	public void setSQL(String sql) {
		this.setDynValue(DYNFIELDNAME_SQL, sql);
	}

	public String getInitialFilter() {
		return (String) this.getDynValue(DYNFIELDNAME_INITIALFILTER);
	}

	public void setInitialFilter(String initialFilter) {
		this.setDynValue(DYNFIELDNAME_INITIALFILTER, initialFilter);
	}

	public String getInitialOrder() {
		return (String) this.getDynValue(DYNFIELDNAME_INITIALORDER);
	}

	public void setInitialOrder(String order) {
		this.setDynValue(DYNFIELDNAME_INITIALORDER, order);
	}

	public String getPkFieldsString() {
		return (String) this.getDynValue(DYNFIELDNAME_PKFIELDS);
	}

	public String[] getPkFields() {
		String fields = (String) this.getDynValue(DYNFIELDNAME_PKFIELDS);
		if (fields == null) {
			return null;
		}
		// FIXME check for fields with spaces and special chars
		return fields.split(",");
	}

	public void setPkFields(String fields) {
		this.setDynValue(DYNFIELDNAME_PKFIELDS, fields);
	}

	public void setPkFields(String[] fields) {
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < fields.length - 1; i++) {
			str.append(fields[i]);
			str.append(",");
		}
		str.append(fields[fields.length - 1]);

		this.setDynValue(DYNFIELDNAME_PKFIELDS, str.toString());
	}



	public String tableID() {
		if (this.getSchema() == null || this.getSchema() == "") {
			return this.getTable();
		}
		return this.getSchema() + "." + this.getTable();
	}

	public String getSourceId() {
		// FIXME Arreglar
		StringBuilder str = new StringBuilder();
		str.append(this.getDataStoreName());
		str.append(':');
		str.append(this.getJDBCDriverClassName());
		str.append(':');
		str.append(getUrl());
		str.append(':');
		if (getTable() != null) {
			str.append(getTable());
		} else {
			str.append(getSQL());
		}
		return str.toString();
	}

	public String getUrl() {
		return (String) this.getDynValue(DYNFIELDNAME_URL);
	}

	public void setUrl(String url) {
		this.setDynValue(DYNFIELDNAME_URL, url);
	}
}
