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

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.NewDataStoreParameters;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.exception.ProviderNotRegisteredException;
import org.gvsig.fmap.dal.exception.RemoveException;
import org.gvsig.fmap.dal.store.jdbc.JDBCHelper;
import org.gvsig.fmap.dal.store.jdbc.JDBCServerExplorer;
import org.gvsig.fmap.dal.store.jdbc.JDBCServerExplorerParameters;
import org.gvsig.fmap.dal.store.jdbc.JDBCStoreParameters;
import org.gvsig.fmap.dal.store.jdbc.TransactionalAction;
import org.gvsig.fmap.dal.store.jdbc.exception.JDBCExecuteSQLException;
import org.gvsig.fmap.dal.store.jdbc.exception.JDBCSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jmvivo
 *
 */
public class MySQLServerExplorer extends JDBCServerExplorer {
	final static private Logger logger = LoggerFactory
			.getLogger(MySQLServerExplorer.class);

	public static final String NAME = "MySQLExplorer";

	private MySQLServerExplorerParameters myParameters;


	public MySQLServerExplorer(
			MySQLServerExplorerParameters parameters)
			throws InitializeException {
		super(parameters);
	}

	protected void init(JDBCServerExplorerParameters parameters)
			throws InitializeException {
		myParameters = (MySQLServerExplorerParameters) parameters;
		super.init(parameters);
	}

	protected JDBCHelper createHelper() throws InitializeException {
		return new MySQLHelper(this, myParameters);
	}


	protected String getStoreName() {
		return MySQLStoreProvider.NAME;
	}

	public String getName() {
		return NAME;
	}

	protected JDBCStoreParameters createStoreParams()
			throws InitializeException, ProviderNotRegisteredException {
		MySQLStoreParameters orgParams = (MySQLStoreParameters) super
				.createStoreParams();

		orgParams.setUseSSL(myParameters.getUseSSL());

		return orgParams;
	}


	// ****************************


	public boolean canAdd() {
		return true;
	}

	protected void checkIsMine(DataStoreParameters dsp) {
		if (!(dsp instanceof MySQLStoreParameters)) {
			// FIXME Excpetion ???
			throw new IllegalArgumentException(
					"not instance of MySQLStoreParameters");
		}
		super.checkIsMine(dsp);

		MySQLStoreParameters myp = (MySQLStoreParameters) dsp;
		if (myp.getUseSSL().booleanValue() != myParameters.getUseSSL()) {
			throw new IllegalArgumentException("worng explorer: useSSL (mine:"
					+ myParameters.getUseSSL() + " other:" + myp.getUseSSL()
					+ ")");
		}
	}

	public void remove(DataStoreParameters dsp) throws RemoveException {
		final MySQLStoreParameters myParams =(MySQLStoreParameters) dsp;

		TransactionalAction action = new TransactionalAction() {
			public boolean continueTransactionAllowed() {
				return false;
			}
			public Object action(Connection conn) throws DataException {


				Statement st;
				try{
					st = conn.createStatement();
				} catch (SQLException e) {
					throw new JDBCSQLException(e);
				}

				String sqlDrop = "Drop table "
					+ myParams.tableID();

				try{
					try{
						st.execute(sqlDrop);
					} catch (SQLException e) {
						throw new JDBCExecuteSQLException(sqlDrop, e);
					}

				} finally{
					try{ st.close(); } catch (SQLException e) {};
				}
				return null;
			}
		};
		try {
			this.helper.doConnectionAction(action);
		} catch (Exception e) {
			throw new RemoveException(this.getName(), e);
		}
	}

	public NewDataStoreParameters getAddParameters() throws DataException {
		MySQLNewStoreParameters params = new MySQLNewStoreParameters();
		params.setHost(this.parameters.getHost());
		params.setPort(this.parameters.getPort());
		params.setDBName(this.parameters.getDBName());
		params.setUser(this.parameters.getUser());
		params.setPassword(this.parameters.getPassword());
		params.setCatalog(this.parameters.getCatalog());
		params.setSchema(this.parameters.getSchema());
		params.setJDBCDriverClassName(this.parameters.getJDBCDriverClassName());
		params.setUrl(this.parameters.getUrl());
		params.setUseSSL(((MySQLServerExplorerParameters) this.parameters)
				.getUseSSL());


		params.setDefaultFeatureType(this.getServerExplorerProviderServices()
				.createNewFeatureType());


		return params;
	}



	// ***********************
	// ***********************


	public boolean hasGeometrySupport() {
		return true;
	}

	protected MySQLHelper getMyHelper() {
		return (MySQLHelper) getHelper();
	}


	protected List getSQLForList(int mode, boolean showInformationDBTables) {
		List list = new ArrayList(1);
		StringBuffer sqlBuf = new StringBuffer();
		sqlBuf
				.append("SELECT null as TABLE_CATALOG, TABLE_SCHEMA, TABLE_NAME, false as ISVIEW ");
		sqlBuf.append(" FROM INFORMATION_SCHEMA.TABLES ");
		sqlBuf.append(" xxWHERExx ");
		sqlBuf.append(" union ");
		sqlBuf
				.append("SELECT null as TABLE_CATALOG, TABLE_SCHEMA, TABLE_NAME, true as ISVIEW ");
		sqlBuf.append(" FROM INFORMATION_SCHEMA.VIEWS ");
		sqlBuf.append(" xxWHERExx ");

		if (showInformationDBTables) {
			list.add(sqlBuf.toString().replaceAll("xxWHERExx", ""));
		} else {
			list
					.add(sqlBuf
					.toString()
					.replaceAll("xxWHERExx",
							"WHERE TABLE_SCHEMA NOT IN ('information_schema', 'mysql')"));

		}

		return list;

	}
}
