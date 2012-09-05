package org.gvsig.fmap.data.feature.db.jdbc;

import java.sql.Connection;

import org.gvsig.fmap.dal.DataServerExplorerParameters;
import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.DataStore;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.exception.InitializeWriterException;
import org.gvsig.fmap.dal.impl.DefaultDataManager;
import org.gvsig.fmap.data.feature.db.DBExplorer;

public abstract class JDBCExplorer extends DBExplorer {

	protected JDBCResource resource;


	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.DataServerExplorer#init(org.gvsig.fmap.dal.DataServerExplorerParameters)
	 */
	public void init(DataServerExplorerParameters parameters,JDBCResource resource) throws InitializeException {
		super.init(parameters);
		this.resource = resource;
	}

	protected Connection getConnection() throws ReadException{
		return this.resource.getConnection();
	}

	public DataStore createDataStore(DataStoreParameters dsp) throws InitializeException, InitializeWriterException {
		DataManager manager = DefaultDataManager.getManager();
		return manager.createStore(dsp);
	}


}
