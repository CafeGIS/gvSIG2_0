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

package org.gvsig.fmap.dal.store.mysql;

import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.store.jdbc.JDBCResourceParameters;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.dynobject.DynClass;
import org.gvsig.tools.dynobject.DynField;
import org.gvsig.tools.dynobject.DynObjectManager;

public class MySQLResourceParameters extends JDBCResourceParameters
		implements MySQLConnectionParameters {

	private static DynClass DYNCLASS = null;

	public static final String DYNCLASS_NAME = "MySQLResourceParameters";

	public MySQLResourceParameters() {
		super();
	}

    public MySQLResourceParameters(String url, String host, Integer port,
			String dbName, String user, String password,
			String jdbcDriverClassName, Boolean ssl) {
		super(url, host, port, dbName, user, password, jdbcDriverClassName);
		if (ssl != null) {
			this.setUseSSL(ssl.booleanValue());
		}
	}

	protected static void registerDynClass() {
		DynObjectManager dynman = ToolsLocator.getDynObjectManager();
		DynClass dynClass = dynman.get(DYNCLASS_NAME);
		DynField field;
		if (dynClass == null) {
			dynClass = dynman.add(DYNCLASS_NAME);

			dynClass.extend(JDBCResourceParameters.DYNCLASS_NAME);

			field = dynClass.addDynField(DYNFIELDNAME_USESSL);
			field.setTheTypeOfAvailableValues(DynField.SINGLE);
			field.setDescription("use SSL connetion");
			field.setType(DataTypes.BOOLEAN);
			field.setDefaultValue(Boolean.FALSE);

			field = dynClass.addDynField(DYNFIELDNAME_JDBC_DRIVER_CLASS_NAME);
			field.setTheTypeOfAvailableValues(DynField.SINGLE);
			field.setDescription("JDBC Driver class");
			field.setMandatory(true);
			field.setType(DataTypes.STRING);
			field.setDefaultValue(MySQLLibrary.DEFAULT_JDCB_DRIVER_NAME);


		}
		DYNCLASS = dynClass;
	}


	public String getUrl() {
		return MySQLLibrary.getJdbcUrl(getHost(),
				getPort(),
				getDBName());
	}

	public String getTypeName() {
		return MySQLResource.NAME;
	}

	public Boolean getUseSSL() {
		return (Boolean) this.getDynValue(DYNFIELDNAME_USESSL);
	}

	public void setUseSSL(Boolean useSSL) {
		this.setDynValue(DYNFIELDNAME_USESSL, useSSL);
	}

	public void setUseSSL(boolean useSSL) {
		this.setDynValue(DYNFIELDNAME_USESSL, new Boolean(useSSL));
	}

	protected String getDynClassName() {
		return DYNCLASS_NAME;
	}
}
