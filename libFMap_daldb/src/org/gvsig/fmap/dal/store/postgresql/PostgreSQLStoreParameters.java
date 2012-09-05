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

package org.gvsig.fmap.dal.store.postgresql;

import org.gvsig.fmap.dal.exception.ValidateDataParametersException;
import org.gvsig.fmap.dal.store.jdbc.JDBCStoreParameters;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.dynobject.DelegatedDynObject;
import org.gvsig.tools.dynobject.DynClass;
import org.gvsig.tools.dynobject.DynField;
import org.gvsig.tools.dynobject.DynObjectManager;

public class PostgreSQLStoreParameters extends JDBCStoreParameters implements
		PostgreSQLConnectionParameters {
    public static final String DYNCLASS_NAME = "PostgreSQLStoreParameters";


    protected static void registerDynClass() {
		DynObjectManager dynman = ToolsLocator.getDynObjectManager();
		DynClass dynClass = dynman.get(DYNCLASS_NAME);
		DynField field;
		if (dynClass == null) {
			dynClass = dynman.add(DYNCLASS_NAME);

			dynClass.extend(JDBCStoreParameters.DYNCLASS_NAME);

			dynClass.extend(PostgreSQLResourceParameters.DYNCLASS_NAME);

		}

	}

	public PostgreSQLStoreParameters() {
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
		return PostgreSQLStoreProvider.NAME;
	}

	public String getDescription() {
		return PostgreSQLStoreProvider.DESCRIPTION;
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
			setJDBCDriverClassName(PostgreSQLLibrary.DEFAULT_JDCB_DRIVER_NAME);
		}
		if (getUrl() == null) {
			setUrl(PostgreSQLLibrary.getJdbcUrl(getHost(), getPort(),
					getDBName()));
		}

		if (getPort() == null) {
			setPort(new Integer(5432));
		}
		super.validate();
	}

}
