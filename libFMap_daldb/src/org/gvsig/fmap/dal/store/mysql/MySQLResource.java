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

import java.text.MessageFormat;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.resource.exception.AccessResourceException;
import org.gvsig.fmap.dal.store.jdbc.JDBCResource;

public class MySQLResource extends JDBCResource {

	public final static String NAME = "MySQLResource";
	public static final String DESCRIPTION = "MySQL Connection";

	public MySQLResource(MySQLResourceParameters parameters)
			throws InitializeException {
		super(parameters);
	}

	public String getName() throws AccessResourceException {
		MySQLResourceParameters params = (MySQLResourceParameters) this.getParameters();
		return MessageFormat.format("MySQLResource({0},{1})",
				new Object[] { params.getUrl(),params.getUser() });
	}

	protected void connectToDB() throws DataException {
		super.connectToDB();
	}

	protected DataSource createDataSource() {
		MySQLResourceParameters jdbcParams = (MySQLResourceParameters) this
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

}
