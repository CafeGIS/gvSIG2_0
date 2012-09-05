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

package org.gvsig.fmap.dal.resource.db;

import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.resource.spi.AbstractResourceParameters;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.dynobject.DelegatedDynObject;
import org.gvsig.tools.dynobject.DynClass;
import org.gvsig.tools.dynobject.DynField;
import org.gvsig.tools.dynobject.DynObjectManager;


public abstract class DBResourceParameters extends AbstractResourceParameters
		implements DBParameters {

    public static final String DYNCLASS_NAME = "DBResourceParameters";


    public DBResourceParameters() {
		this.delegatedDynObject = (DelegatedDynObject) ToolsLocator
				.getDynObjectManager().createDynObject(getDynClassName());
	}

    protected abstract String getDynClassName();

	static void registerDynClass() {
		DynObjectManager dynman = ToolsLocator.getDynObjectManager();
		DynClass dynClass = dynman.get(DYNCLASS_NAME);
		DynField field;
		if (dynClass == null) {
			dynClass = dynman.add(DYNCLASS_NAME);

			field = dynClass.addDynField(DYNFIELDNAME_HOST);
			field.setTheTypeOfAvailableValues(DynField.SINGLE);
			field.setDescription("DB Host");
			field.setMandatory(true);
			field.setType(DataTypes.STRING);

			field = dynClass.addDynField(DYNFIELDNAME_PORT);
			field.setTheTypeOfAvailableValues(DynField.SINGLE);
			field.setDescription("Host Port");
			field.setType(DataTypes.INT);

			field = dynClass.addDynField(DYNFIELDNAME_DBNAME);
			field.setTheTypeOfAvailableValues(DynField.SINGLE);
			field.setDescription("DB name");
			field.setType(DataTypes.STRING);

			field = dynClass.addDynField(DYNFIELDNAME_USER);
			field.setTheTypeOfAvailableValues(DynField.SINGLE);
			field.setDescription("User name");
			field.setType(DataTypes.STRING);

			field = dynClass.addDynField(DYNFIELDNAME_PASSWORD);
			field.setTheTypeOfAvailableValues(DynField.SINGLE);
			field.setDescription("Password");
			field.setType(DataTypes.STRING);
		}
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


}
