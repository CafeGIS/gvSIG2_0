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
import org.gvsig.fmap.dal.resource.db.DBResourceParameters;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.dynobject.DynClass;
import org.gvsig.tools.dynobject.DynField;
import org.gvsig.tools.dynobject.DynObjectManager;

public class JDBCResourceParameters extends DBResourceParameters
		implements JDBCConnectionParameters {

    public static final String DYNCLASS_NAME = "JDBCResourceParameters";

	private static DynClass DYNCLASS = null;


    public JDBCResourceParameters(String url, String host, Integer port,
			String dbName,
			String user, String password, String jdbcDriverClassName) {
		super();

		this.setUrl(url);
		this.setHost(host);
		this.setPort(port);
		this.setDBName(dbName);
		this.setUser(user);
		this.setPassword(password);
		this.setJDBCDriverClassName(jdbcDriverClassName);
	}


	public JDBCResourceParameters() {
		super();
	}

	public String getTypeName() {
		return JDBCResource.NAME;
	}

	protected String getDynClassName() {
		return DYNCLASS_NAME;
	}

	static void registerDynClass() {
		DynObjectManager dynman = ToolsLocator.getDynObjectManager();
		DynClass dynClass = dynman.get(DYNCLASS_NAME);
		DynField field;
		if (dynClass == null) {
			dynClass = dynman.add(DYNCLASS_NAME);

			dynClass.extend(DBResourceParameters.DYNCLASS_NAME);

			field = dynClass.addDynField(DYNFIELDNAME_JDBC_DRIVER_CLASS_NAME);
			field.setTheTypeOfAvailableValues(DynField.SINGLE);
			field.setDescription("JDBC Driver class");
			field.setMandatory(true);
			field.setType(DataTypes.STRING);

			field = dynClass.addDynField(DYNFIELDNAME_URL);
			field.setTheTypeOfAvailableValues(DynField.SINGLE);
			field.setDescription("JDBC connection url");
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
		DYNCLASS = dynClass;

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


	public String getUrl() {
		return (String) this.getDynValue(DYNFIELDNAME_URL);
	}

	public void setUrl(String url) {
		this.setDynValue(DYNFIELDNAME_URL, url);
	}


}
