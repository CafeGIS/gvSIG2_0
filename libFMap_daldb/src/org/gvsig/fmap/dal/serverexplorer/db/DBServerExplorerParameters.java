package org.gvsig.fmap.dal.serverexplorer.db;

import org.gvsig.fmap.dal.DataServerExplorerParameters;
import org.gvsig.fmap.dal.resource.db.DBResourceParameters;
import org.gvsig.fmap.dal.spi.AbstractDataParameters;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.dynobject.DelegatedDynObject;
import org.gvsig.tools.dynobject.DynClass;
import org.gvsig.tools.dynobject.DynObjectManager;

public abstract class DBServerExplorerParameters extends AbstractDataParameters
		implements DataServerExplorerParameters, DBConnectionParameter {

    public static final String DYNCLASS_NAME = "DBServerExplorerParameters";


	public DBServerExplorerParameters() {
		this.delegatedDynObject = (DelegatedDynObject) ToolsLocator
				.getDynObjectManager()
				.createDynObject(this.registerDynClass());
	}

	protected DynClass registerDynClass() {
	   	DynObjectManager dynman = ToolsLocator.getDynObjectManager();
    	DynClass dynClass = dynman.get(DYNCLASS_NAME);
    	if (dynClass == null) {
    		dynClass = dynman.add(DYNCLASS_NAME);

    		DynClass dbResourceDynClass = dynman
					.get(DBResourceParameters.DYNCLASS_NAME);

			dynClass.extend(dbResourceDynClass);

    	}
    	return dynClass;
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
