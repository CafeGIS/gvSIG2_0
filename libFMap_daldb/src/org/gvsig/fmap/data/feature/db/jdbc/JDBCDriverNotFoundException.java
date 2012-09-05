package org.gvsig.fmap.data.feature.db.jdbc;

import org.gvsig.fmap.dal.exception.InitializeException;


public class JDBCDriverNotFoundException extends InitializeException {
	public JDBCDriverNotFoundException(String l,Throwable exception) {
		super(l,exception);
		init();
	}
	/**
	 *
	 */
	protected void init() {
		messageKey = "JDBC_Driver_not_found";
		formatString = "JDBC Driver not found: %(name) ";
	}

}
