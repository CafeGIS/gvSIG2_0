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
package org.gvsig.fmap.dal.store.postgresql;

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
public class PostgreSQLServerExplorer extends JDBCServerExplorer {
	final static private Logger logger = LoggerFactory
			.getLogger(PostgreSQLServerExplorer.class);

	public static final String NAME = "PostgreSQLExplorer";

	private PostgreSQLServerExplorerParameters pgParameters;


	public PostgreSQLServerExplorer(
			PostgreSQLServerExplorerParameters parameters)
			throws InitializeException {
		super(parameters);
	}

	protected void init(JDBCServerExplorerParameters parameters)
			throws InitializeException {
		pgParameters = (PostgreSQLServerExplorerParameters) parameters;
		super.init(parameters);
	}

	protected JDBCHelper createHelper() throws InitializeException {
		return new PostgreSQLHelper(this, pgParameters);
	}


	protected String getStoreName() {
		return PostgreSQLStoreProvider.NAME;
	}

	public String getName() {
		return NAME;
	}

	protected JDBCStoreParameters createStoreParams()
			throws InitializeException, ProviderNotRegisteredException {
		PostgreSQLStoreParameters orgParams = (PostgreSQLStoreParameters) super
				.createStoreParams();

		orgParams.setUseSSL(pgParameters.getUseSSL());

		return orgParams;
	}


	// ****************************


	public boolean canAdd() {
		return true;
	}

	protected void checkIsMine(DataStoreParameters dsp) {
		if (!(dsp instanceof PostgreSQLStoreParameters)) {
			// FIXME Excpetion ???
			throw new IllegalArgumentException(
					"not instance of PostgreSQLStoreParameters");
		}
		super.checkIsMine(dsp);

		PostgreSQLStoreParameters pgp = (PostgreSQLStoreParameters) dsp;
		if (pgp.getUseSSL().booleanValue() != pgParameters.getUseSSL()) {
			throw new IllegalArgumentException("worng explorer: Host");
		}
	}

	public void remove(DataStoreParameters dsp) throws RemoveException {
		final PostgreSQLStoreParameters pgParams =(PostgreSQLStoreParameters) dsp;

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
					+ pgParams.tableID();

				StringBuilder strb = new StringBuilder();
				strb.append("Delete from GEOMETRY_COLUMNS where f_table_schema = ");
				if (pgParams.getSchema() == null || pgParams.getSchema().length() ==  0) {
					strb.append("current_schema() ");
				} else {
					strb.append('\'');
					strb.append(pgParams.getSchema());
					strb.append("' ");
				}
				strb.append("and f_table_name = '");
				strb.append(pgParams.getTable());
				strb.append('\'');

				String sqlDeleteFromGeometry_column = strb.toString();
				try{
					try{
						st.execute(sqlDrop);
					} catch (SQLException e) {
						throw new JDBCExecuteSQLException(sqlDrop, e);
					}

					try {
						st.execute(sqlDeleteFromGeometry_column);
					} catch (SQLException e) {
						throw new JDBCExecuteSQLException(
								sqlDeleteFromGeometry_column, e);
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
		PostgreSQLNewStoreParameters params = new PostgreSQLNewStoreParameters();
		params.setHost(this.parameters.getHost());
		params.setPort(this.parameters.getPort());
		params.setDBName(this.parameters.getDBName());
		params.setUser(this.parameters.getUser());
		params.setPassword(this.parameters.getPassword());
		params.setCatalog(this.parameters.getCatalog());
		params.setSchema(this.parameters.getSchema());
		params.setJDBCDriverClassName(this.parameters.getJDBCDriverClassName());
		params.setUrl(this.parameters.getUrl());
		params.setUseSSL(((PostgreSQLServerExplorerParameters) this.parameters)
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

	protected PostgreSQLHelper getPgHelper() {
		return (PostgreSQLHelper) getHelper();
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
							"WHERE TABLE_SCHEMA NOT IN ('information_schema','pg_catalog')"));

		}
		return list;


	}
}
