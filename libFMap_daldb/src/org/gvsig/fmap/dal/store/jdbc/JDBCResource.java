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

import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.resource.ResourceParameters;
import org.gvsig.fmap.dal.resource.db.DBResource;
import org.gvsig.fmap.dal.resource.exception.AccessResourceException;
import org.gvsig.fmap.dal.resource.exception.ResourceException;
import org.gvsig.fmap.dal.store.jdbc.exception.JDBCDriverClassNotFoundException;
import org.gvsig.fmap.dal.store.jdbc.exception.JDBCSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JDBCResource extends DBResource {

	final static private Logger logger = LoggerFactory
			.getLogger(JDBCResource.class);

	public final static String NAME = "JDBCResource";
	public static final String DESCRIPTION = "JDBC Connection";

	protected DataSource dataSource = null;

	public JDBCResource(JDBCResourceParameters parameters)
			throws InitializeException {
		super(parameters);
		registerJDBCDriver();

	}

	public String getName() throws AccessResourceException {
		JDBCResourceParameters params = (JDBCResourceParameters) this
				.getParameters();
		return MessageFormat.format("JDBCResource({0},{1},{2},{3},{4})",
				new Object[] {
				params.getJDBCDriverClassName(),
						params.getHost(), params.getPort(),
				params.getUser(), params.getDBName() });
	}

	protected void registerJDBCDriver() throws InitializeException {
		String className = ((JDBCResourceParameters) getParameters())
				.getJDBCDriverClassName();
		if (className == null) {
			return;
		}

		Class theClass = null;
		try {
			theClass = Class.forName(className);
		} catch (Exception e){
			throw new InitializeException(e);
		}
		if (theClass == null) {
			try {
				throw new JDBCDriverClassNotFoundException(this.getName(),
						className);
			} catch (AccessResourceException e) {
				throw new InitializeException(e);

			}
		}
	}


	public Connection getJDBCConnection() throws AccessResourceException {
		return (Connection) get();
	}

	private void debugPoolStatus(String src) {
		if (logger.isDebugEnabled() && dataSource instanceof BasicDataSource) {
			BasicDataSource ds = (BasicDataSource) dataSource;
			logger.debug(src + "  actives:" + ds.getNumActive() + "("
					+ ds.getMaxActive() + ") idle:" + ds.getNumIdle() + "("
					+ ds.getMaxIdle() + ")");
		}

	}

	protected synchronized Object getTheConnection() throws DataException {
		try {
			Object conn = this.dataSource.getConnection();
			debugPoolStatus("getTheConnection");
			return conn;
		} catch (SQLException e) {
			throw new JDBCSQLException(e);
		}
	}

	public boolean isThis(ResourceParameters parameters)
			throws ResourceException {
		if (!super.isThis(parameters)) {
			return false;
		}

		String dbName = ((JDBCResourceParameters) parameters).getDBName();
		String myDbName = ((JDBCResourceParameters) getParameters())
				.getDBName();
		if (dbName != myDbName) {
			if (!(dbName != null && dbName.equals(myDbName))) {
				return false;
			}

		}

		String driver = ((JDBCResourceParameters) parameters)
				.getJDBCDriverClassName();
		String myDriver = ((JDBCResourceParameters) getParameters())
				.getJDBCDriverClassName();
		if (driver != myDriver) {
			if (!(driver != null && driver.equals(myDriver))) {
				return false;
			}
		}
		return true;

	}

	public boolean isConnected() {
		if (dataSource == null) {
			return false;
		}
		if (dataSource instanceof BasicDataSource) {
			return ((BasicDataSource) dataSource).getNumActive() > 0
					|| ((BasicDataSource) dataSource).getNumIdle() > 0;
		}
		return true;
	}

	protected DataSource createDataSource() {
		JDBCResourceParameters jdbcParams = (JDBCResourceParameters) this
		.getParameters();
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(jdbcParams.getJDBCDriverClassName());
		dataSource.setUsername(jdbcParams.getUser());
		dataSource.setPassword(jdbcParams.getPassword());
		dataSource.setUrl(jdbcParams.getUrl());


		dataSource.setMaxWait(60L * 1000); // FIXME

		// FIXME Set Pool parameters:
		/*
		dataSource.setMaxActive(maxActive);
		dataSource.setMaxIdle(maxActive);
		dataSource.setMaxOpenPreparedStatements(maxActive);
		dataSource.setMaxWait(maxActive);
		dataSource.setInitialSize(initialSize);
		dataSource.setDefaultReadOnly(defaultReadOnly);
		dataSource.setDefaultTransactionIsolation(defaultTransactionIsolation);
		dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
		dataSource.setMinIdle(minIdle);
		dataSource.setTestOnBorrow(testOnBorrow);
		dataSource.setTestOnReturn(testOnReturn);
		dataSource.setTestWhileIdle(testOnReturn);
		dataSource
			.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);

		dataSource.setAccessToUnderlyingConnectionAllowed(allow);
		dataSource.setLoginTimeout(seconds);
		dataSource.setLogWriter(out);
		 */
		return dataSource;
	}

	protected void connectToDB() throws DataException {
		if (this.dataSource != null) {
			return;
		}
		JDBCResourceParameters jdbcParams = (JDBCResourceParameters) this
				.getParameters();
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(jdbcParams.getJDBCDriverClassName());
		dataSource.setUsername(jdbcParams.getUser());
		dataSource.setPassword(jdbcParams.getPassword());
		dataSource.setUrl(jdbcParams.getUrl());

		dataSource.setMaxWait(60L * 1000); // FIXME

		// FIXME Set Pool parameters:
		/*
		dataSource.setMaxActive(maxActive);
		dataSource.setMaxIdle(maxActive);
		dataSource.setMaxOpenPreparedStatements(maxActive);
		dataSource.setMaxWait(maxActive);
		dataSource.setInitialSize(initialSize);
		dataSource.setDefaultReadOnly(defaultReadOnly);
		dataSource.setDefaultTransactionIsolation(defaultTransactionIsolation);
		dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
		dataSource.setMinIdle(minIdle);
		dataSource.setTestOnBorrow(testOnBorrow);
		dataSource.setTestOnReturn(testOnReturn);
		dataSource.setTestWhileIdle(testOnReturn);
		dataSource
				.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);

		dataSource.setAccessToUnderlyingConnectionAllowed(allow);
		dataSource.setLoginTimeout(seconds);
		dataSource.setLogWriter(out);
		*/

		this.dataSource = dataSource;
	}


}
