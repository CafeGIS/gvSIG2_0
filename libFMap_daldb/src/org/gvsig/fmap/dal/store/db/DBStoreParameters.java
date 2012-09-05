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

package org.gvsig.fmap.dal.store.db;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.crs.CRSFactory;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.serverexplorer.db.DBConnectionParameter;
import org.gvsig.fmap.dal.spi.AbstractDataParameters;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.dynobject.DynClass;
import org.gvsig.tools.dynobject.DynField;
import org.gvsig.tools.dynobject.DynObjectManager;

public abstract class DBStoreParameters extends AbstractDataParameters
		implements
		DataStoreParameters, DBConnectionParameter {

	public static final String DYNCLASS_NAME = "DBStoreParameters";

	public static final String DYNFIELDNAME_SQL = "sql";
	public static final String DYNFIELDNAME_FIELDS = "fields";
	public static final String DYNFIELDNAME_INITIALFILTER = "initialfilter";
	public static final String DYNFIELDNAME_INITIALORDER = "initialorder";
	public static final String DYNFIELDNAME_PKFIELDS = "pkfields";
	public static final String DYNFIELDNAME_DEFAULTGEOMETRY = "defaultgeometry";
	public static final String DYNFIELDNAME_TABLE = "table";
	public static final String DYNFIELDNAME_WORKINGAREA = "workingarea";

	public static final String DYNFIELDNAME_SRS = "srs";


    protected static void registerDynClass() {
		DynObjectManager dynman = ToolsLocator.getDynObjectManager();
		DynClass dynClass = dynman.get(DYNCLASS_NAME);
		DynField field;
		if (dynClass == null) {
			dynClass = dynman.add(DYNCLASS_NAME);

			field = dynClass.addDynField(DYNFIELDNAME_TABLE);
			field.setTheTypeOfAvailableValues(DynField.SINGLE);
			field.setDescription("Table/View name");
			field.setType(DataTypes.STRING);

			field = dynClass.addDynField(DYNFIELDNAME_SQL);
			field.setTheTypeOfAvailableValues(DynField.SINGLE);
			field.setDescription("SQL Query to use");
			field.setType(DataTypes.STRING);

			field = dynClass.addDynField(DYNFIELDNAME_FIELDS);
			field.setTheTypeOfAvailableValues(DynField.SINGLE);
			field.setDescription("Fields to use (coma separeted)");
			field.setType(DataTypes.STRING);

			field = dynClass.addDynField(DYNFIELDNAME_INITIALFILTER);
			field.setTheTypeOfAvailableValues(DynField.SINGLE);
			field.setDescription("Initial filter");
			field.setType(DataTypes.STRING);

			field = dynClass.addDynField(DYNFIELDNAME_INITIALORDER);
			field.setTheTypeOfAvailableValues(DynField.SINGLE);
			field.setDescription("Initial order");
			field.setType(DataTypes.STRING);

			field = dynClass.addDynField(DYNFIELDNAME_PKFIELDS);
			field.setTheTypeOfAvailableValues(DynField.SINGLE);
			field.setDescription("Fields of the Primary Key");
			field.setType(DataTypes.STRING);

			field = dynClass.addDynField(DYNFIELDNAME_DEFAULTGEOMETRY);
			field.setTheTypeOfAvailableValues(DynField.SINGLE);
			field.setDescription("Default geometry field");
			field.setType(DataTypes.STRING);

			field = dynClass.addDynField(DYNFIELDNAME_WORKINGAREA);
			field.setTheTypeOfAvailableValues(DynField.SINGLE);
			field.setDescription("Working area");
			field.setType(DataTypes.OBJECT);// TODO DataTypes.ENVELOPE ???

			field = dynClass.addDynField(DYNFIELDNAME_SRS);
			field.setDescription("SRS");
			field.setType(DataTypes.SRS);

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
		str.append(fields.length - 1);

		this.setDynValue(DYNFIELDNAME_PKFIELDS, fields);
	}

	public String getDefaultGeometry() {
		return (String) this.getDynValue(DYNFIELDNAME_DEFAULTGEOMETRY);
	}

	public void setDefaultGeometry(String geomName) {
		this.setDynValue(DYNFIELDNAME_DEFAULTGEOMETRY, geomName);
	}

	public Envelope getWorkingArea() {
		return (Envelope) this.getDynValue(DYNFIELDNAME_WORKINGAREA);
	}

	public void setWorkingArea(Envelope workingArea) {
		this.setDynValue(DYNFIELDNAME_WORKINGAREA, workingArea);
	}

	public String getSRSID() {
		IProjection srs = (IProjection) getDynValue(DYNFIELDNAME_SRS);
		if (srs == null) {
			return null;
		}
		return srs.getAbrev();
	}

	public void setSRSID(String srsid) {
		if (srsid == null) {
			setDynValue(DYNFIELDNAME_SRS, null);
		} else {
			setDynValue(DYNFIELDNAME_SRS, CRSFactory.getCRS(srsid));
		}
	}

	public void setSRS(IProjection srs) {
		setDynValue(DYNFIELDNAME_SRS, srs);
	}

	public IProjection getSRS() {
		if (this.getSRSID() == null) {
			return null;
		}
		return (IProjection) getDynValue(DYNFIELDNAME_SRS);
	}

}
