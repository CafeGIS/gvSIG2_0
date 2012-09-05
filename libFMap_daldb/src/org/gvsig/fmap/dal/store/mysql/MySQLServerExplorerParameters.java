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

/**
 *
 */
package org.gvsig.fmap.dal.store.mysql;

import org.gvsig.fmap.dal.exception.ValidateDataParametersException;
import org.gvsig.fmap.dal.store.jdbc.JDBCServerExplorerParameters;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.dynobject.DynClass;
import org.gvsig.tools.dynobject.DynField;
import org.gvsig.tools.dynobject.DynObjectManager;

/**
 * @author jmvivo
 *
 */
public class MySQLServerExplorerParameters extends
		JDBCServerExplorerParameters implements MySQLConnectionParameters {


	public static final String DYNCLASS_NAME = "MySQLServerExplorerParameters";

	protected DynClass registerDynClass() {
		DynObjectManager dynman = ToolsLocator.getDynObjectManager();
		DynClass dynClass = dynman.get(DYNCLASS_NAME);
		DynField field;

		if (dynClass == null) {
			dynClass = dynman.add(DYNCLASS_NAME);

			dynClass.extend(super.registerDynClass());

			dynClass.extend(MySQLResourceParameters.DYNCLASS_NAME);

		}
		return dynClass;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.DataServerExplorerParameters#getExplorerName()
	 */
	public String getExplorerName() {
		return MySQLServerExplorer.NAME;
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

	public void validate() throws ValidateDataParametersException {
		if (getJDBCDriverClassName() == null) {
			setJDBCDriverClassName(MySQLLibrary.DEFAULT_JDCB_DRIVER_NAME);
		}
		if (getUrl() == null) {
			setUrl(MySQLLibrary.getJdbcUrl(getHost(), getPort(),
					getDBName()));
		}

		if (getPort() == null) {
			setPort(new Integer(3306));
		}
		super.validate();
	}


	protected String getSQLForList(int mode, boolean showInformationDBTables) {
		StringBuffer sqlBuf = new StringBuffer();
		sqlBuf
				.append("SELECT TABLE_CATALOG, TABLE_SCHEMA, TABLE_NAME, false as ISVIEW ");
		sqlBuf.append(" FROM INFORMATION_SCHEMA.TABLES ");
		sqlBuf.append(" xxWHERExx ");
		sqlBuf.append(" union ");
		sqlBuf
				.append("SELECT TABLE_CATALOG, TABLE_SCHEMA, TABLE_NAME, true as ISVIEW ");
		sqlBuf.append(" FROM INFORMATION_SCHEMA.VIEWS ");
		sqlBuf.append(" xxWHERExx ");

		if (showInformationDBTables) {
			return sqlBuf.toString().replaceAll("xxWHERExx", "");
		} else {
			return sqlBuf.toString().replaceAll("xxWHERExx",
					"WHERE TABLE_SCHEMA NOT IN ('information_schema','mysql')");

		}

	}


}
