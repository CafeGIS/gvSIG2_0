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
package org.gvsig.fmap.dal.store.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.DataServerExplorerParameters;
import org.gvsig.fmap.dal.DataStore;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.NewDataStoreParameters;
import org.gvsig.fmap.dal.exception.CloseException;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.exception.OpenException;
import org.gvsig.fmap.dal.exception.ProviderNotRegisteredException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.exception.RemoveException;
import org.gvsig.fmap.dal.exception.ValidateDataParametersException;
import org.gvsig.fmap.dal.feature.EditableFeatureType;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.NewFeatureStoreParameters;
import org.gvsig.fmap.dal.resource.spi.ResourceProvider;
import org.gvsig.fmap.dal.serverexplorer.db.spi.AbstractDBServerExplorer;
import org.gvsig.fmap.dal.spi.DataManagerProviderServices;
import org.gvsig.fmap.dal.store.jdbc.exception.JDBCExecuteSQLException;
import org.gvsig.fmap.dal.store.jdbc.exception.JDBCSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author jmvivo
 *
 */
public class JDBCServerExplorer extends AbstractDBServerExplorer
		implements JDBCHelperUser {

	final static private Logger logger = LoggerFactory
			.getLogger(JDBCServerExplorer.class);

	public static final String NAME = "JDBCServerExplorer";
	protected JDBCServerExplorerParameters parameters;
	protected JDBCHelper helper;

	private Boolean canAdd;

	public JDBCServerExplorer(JDBCServerExplorerParameters parameters)
			throws InitializeException {
		this.init(parameters);
	}

	protected void init(JDBCServerExplorerParameters parameters)
			throws InitializeException {
		this.parameters = parameters;
		this.helper = createHelper();
	}

	protected JDBCHelper createHelper() throws InitializeException {
		return new JDBCHelper(this, parameters);
	}

	protected JDBCHelper getHelper() {
		return helper;
	}


	public List list() throws DataException {
		return this.list(MODE_ALL);
	}

	public List list(boolean showInformationDBTables) throws DataException {
		return this.list(MODE_ALL, showInformationDBTables);
	}

	public List list(int mode) throws DataException {
		if (parameters.getShowInformationDBTables() != null) {
			return this.list(mode, parameters.getShowInformationDBTables()
					.booleanValue());
		}
		Boolean show = (Boolean) parameters
				.getDynClass()
				.getDynField(
						JDBCServerExplorerParameters.DYNFIELDNAME_SHOWINFORMATIONDBTABLES)
				.getDefaultValue();
		if (show == null){
			show = Boolean.FALSE;
		}

		return this.list(mode, show.booleanValue());
	}


	protected DataManagerProviderServices getManager() {
		return (DataManagerProviderServices) DALLocator.getDataManager();
	}

	public boolean hasGeometrySupport() {
		return false;
	}

	public boolean closeResourceRequested(ResourceProvider resource) {
		try {
			this.helper.close();
		} catch (CloseException e) {
			logger.error("Exception in close Request", e);
		}
		return !this.helper.isOpen();
	}

	public void resourceChanged(ResourceProvider resource) {
		// Nothing to do
	}

	public void remove(DataStoreParameters dsp) throws RemoveException {
		final JDBCStoreParameters dsParams =(JDBCStoreParameters) dsp;

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
					+ dsParams.tableID();

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
		JDBCNewStoreParameters params = new JDBCNewStoreParameters();
		params.setHost(this.parameters.getHost());
		params.setPort(this.parameters.getPort());
		params.setDBName(this.parameters.getDBName());
		params.setUser(this.parameters.getUser());
		params.setPassword(this.parameters.getPassword());
		params.setCatalog(this.parameters.getCatalog());
		params.setSchema(this.parameters.getSchema());
		params.setJDBCDriverClassName(this.parameters.getJDBCDriverClassName());
		params.setUrl(this.parameters.getUrl());

		params.setDefaultFeatureType(this.getServerExplorerProviderServices()
				.createNewFeatureType());

		return params;
	}

	public void closeDone() throws DataException {
		// Nothing to do
	}

	public void opendDone() throws DataException {
		// Nothin to do

	}

	public DataStore open(DataStoreParameters dsp) throws DataException {
		checkIsMine(dsp);
		DataManager dataMan = DALLocator.getDataManager();
		DataStore store;
		try {
			store = dataMan.createStore(dsp);
		} catch (ValidateDataParametersException e) {
			throw new InitializeException(e);
		}

		return store;
	}

	protected void checkIsMine(DataStoreParameters dsp) {
		if (!(dsp instanceof JDBCStoreParameters)) {
			// FIXME Excpetion ???
			throw new IllegalArgumentException(
					"not instance of FilesystemStoreParameters");
		}

		try {
			dsp.validate();
		} catch (ValidateDataParametersException e) {
			throw new IllegalArgumentException("check parameters", e);
		}


		JDBCStoreParameters pgp = (JDBCStoreParameters) dsp;
		if (!compare(pgp.getHost(), parameters.getHost())) {
			// FIXME Excpetion ???
			throw new IllegalArgumentException("worng explorer: Host (mine: "
					+ parameters.getHost() + " other:" + pgp.getHost() + ")");
		}
		if (!compare(pgp.getPort(), parameters.getPort())) {
			// FIXME Excpetion ???
			throw new IllegalArgumentException("worng explorer: Port (mine: "
					+ parameters.getPort() + " other:" + pgp.getPort() + ")");
		}
		if (!compare(pgp.getDBName(), parameters.getDBName())) {
			// FIXME Excpetion ???
			throw new IllegalArgumentException("worng explorer: DBName (mine: "
					+ parameters.getDBName() + " other:" + pgp.getDBName()
					+ ")");
		}
		if (parameters.getCatalog() != null) {
			// implicit catalog
			if (!compare(pgp.getCatalog(), parameters.getCatalog())) {
				// FIXME Excpetion ???
				throw new IllegalArgumentException(
						"worng explorer: Catalog (mine: "
								+ parameters.getCatalog() + " other:"
								+ pgp.getCatalog() + ")");
			}
		}
		if (parameters.getSchema() != null) {
			// implicit schema
			if (!compare(pgp.getSchema(), parameters.getSchema())) {
				// FIXME Excpetion ???
				throw new IllegalArgumentException(
						"worng explorer: Schema (mine: "
								+ parameters.getSchema() + " other:"
								+ pgp.getSchema() + ")");
			}
		}
		if (!compare(pgp.getJDBCDriverClassName(), parameters
				.getJDBCDriverClassName())) {
			// FIXME Excpetion ???
			throw new IllegalArgumentException(
					"worng explorer: JDBCDriverClassName");
		}
	}

	protected boolean compare(Object str1, Object str2) {
		if (str1 == str2){
			return true;
		}
		if (str1 == null){
			return false;
		}
		return  str1.equals(str2);
	}

	protected JDBCStoreParameters createStoreParams()
			throws InitializeException, ProviderNotRegisteredException {
		DataManagerProviderServices manager = this.getManager();
		JDBCStoreParameters orgParams = (JDBCStoreParameters) manager
				.createStoreParameters(getStoreName());
		orgParams.setHost(parameters.getHost());
		orgParams.setPort(parameters.getPort());
		orgParams.setDBName(parameters.getDBName());
		orgParams.setUser(parameters.getUser());
		orgParams.setPassword(parameters.getPassword());
		return orgParams;
	}

	protected List getSQLForList(int mode, boolean showInformationDBTables) {
		List result = new ArrayList(2);
		String toReplace;
		if (showInformationDBTables) {
			toReplace = "";
		} else {
			toReplace = "WHERE TABLE_SCHEMA NOT IN ('information_schema')";

		}


		if (this.helper.supportsUnion()) {
			StringBuffer sqlBuf = new StringBuffer();
			sqlBuf
					.append("SELECT TABLE_CATALOG, TABLE_SCHEMA, TABLE_NAME, false as ISVIEW ");
			sqlBuf.append(" FROM INFORMATION_SCHEMA.TABLES ");
			sqlBuf.append(" xxWHERExx ");
			sqlBuf.append(" union ");
			sqlBuf
					.append("SELECT TABLE_CATALOG, TABLE_SCHEMA, TABLE_NAME, true as ISVIEW ");
			sqlBuf.append(" FROM INFORMATION_SCHEMA.VIEWS ");
			sqlBuf.append(" xxWHERExx ");

			if (showInformationDBTables) {
				result.add(sqlBuf.toString().replaceAll("xxWHERExx", ""));
			} else {
				result.add(sqlBuf.toString().replaceAll("xxWHERExx",
						"WHERE TABLE_SCHEMA NOT IN ('information_schema')"));

			}
			return result;
		} else {
			StringBuffer sqlBuf = new StringBuffer();
			sqlBuf
					.append("SELECT TABLE_CATALOG, TABLE_SCHEMA, TABLE_NAME, false as ISVIEW ");
			sqlBuf.append(" FROM INFORMATION_SCHEMA.TABLES ");
			sqlBuf.append(" xxWHERExx ");

			if (showInformationDBTables) {
				result.add(sqlBuf.toString().replaceAll("xxWHERExx", ""));
			} else {
				result.add(sqlBuf.toString().replaceAll("xxWHERExx",
						"WHERE TABLE_SCHEMA NOT IN ('information_schema')"));

			}
			sqlBuf = new StringBuffer();
			sqlBuf
					.append("SELECT TABLE_CATALOG, TABLE_SCHEMA, TABLE_NAME, true as ISVIEW ");
			sqlBuf.append(" FROM INFORMATION_SCHEMA.VIEWS ");
			sqlBuf.append(" xxWHERExx ");
			if (showInformationDBTables) {
				result.add(sqlBuf.toString().replaceAll("xxWHERExx", ""));
			} else {
				result.add(sqlBuf.toString().replaceAll("xxWHERExx",
						"WHERE TABLE_SCHEMA NOT IN ('information_schema')"));

			}

			return result;
		}

	}

	public List list(final int mode, final boolean showInformationDBTables)
			throws DataException {

		final JDBCStoreParameters orgParams = createStoreParams();

		ConnectionAction action = new ConnectionAction() {

			public Object action(Connection conn) throws DataException {
				ResultSet rs = null;
				Statement st = null;
				List sqls = getSQLForList(mode, showInformationDBTables);

				try {
					JDBCStoreParameters params;

					List paramList = new ArrayList();

					conn = helper.getConnection();
					st = conn.createStatement();
					String sql;
					Iterator sqlIter = sqls.iterator();
					while (sqlIter.hasNext()){
						sql = (String) sqlIter.next();
						rs = st.executeQuery(sql);
						while (rs.next()) {
							params = (JDBCStoreParameters) orgParams
									.getCopy();
							params.setCatalog(rs.getString(1));
							params.setSchema(rs.getString(2));
							params.setTable(rs.getString(3));
							// TODO
//							if (rs.getBoolean(4)) {
//								param.setIsView(param.IS_VIEW);
//							} else {
//								param.setIsView(param.NOT_IS_VIEW);
//							}
							paramList.add(params);
						}
					}

					return paramList;
				} catch (SQLException e) {
					throw new JDBCSQLException(e);
				} finally {
					try {rs.close();} catch (Exception e) {	};
					try {st.close();} catch (Exception e) {	};
				}
			}

		};

		try {
			return (List) helper.doConnectionAction(action);
		} catch (Exception e) {
			throw new ReadException(this.getName(), e);
		}
	}


	public void open() throws OpenException {
		helper.open();
	}

	public void close() throws CloseException {
		helper.close();
	}

	public void dispose() throws DataException {
		helper.dispose();
		helper = null;

	}

	public String getName() {
		return NAME;
	}

	public DataServerExplorerParameters getParameters() {
		return this.parameters;
	}

	protected String getStoreName() {
		return JDBCStoreProvider.NAME;
	}

	public boolean canAdd() {
		if (this.canAdd == null){
			ConnectionAction action = new ConnectionAction(){

				public Object action(Connection conn) throws DataException {
					try {
					DatabaseMetaData metadata = conn.getMetaData();
						if (metadata.isReadOnly()) {
							return Boolean.FALSE;
						}
						return Boolean.TRUE;
					} catch (SQLException e) {
						throw new JDBCSQLException(e);
					}
				}

			};

			try {
				this.canAdd = (Boolean) helper.doConnectionAction(action);
			} catch (Exception e) {
				// FIXME Exception
				throw new RuntimeException(e);
			}
		}
		return this.canAdd.booleanValue();
	}

	public FeatureType getFeatureType(DataStoreParameters dsp)
			throws DataException {
		checkIsMine(dsp);

		// TODO: checks geometry columns and driver geometry supports
		EditableFeatureType edType = this.getServerExplorerProviderServices()
				.createNewFeatureType();
		helper.loadFeatureType(edType, (JDBCStoreParameters) dsp);

		return edType;

	}


	public boolean add(NewDataStoreParameters ndsp, boolean overwrite)
			throws DataException {

		/**
		 * CREATE [ [ GLOBAL | LOCAL ] { TEMPORARY | TEMP } ] TABLE table_name (
		 * { column_name data_type [ DEFAULT default_expr ] [ column_constraint
		 * [ ... ] ] | table_constraint | LIKE parent_table [ { INCLUDING |
		 * EXCLUDING } DEFAULTS ] } [, ... ] ) [ INHERITS ( parent_table [, ...
		 * ] ) ] [ WITH OIDS | WITHOUT OIDS ] [ ON COMMIT { PRESERVE ROWS |
		 * DELETE ROWS | DROP } ]
		 *
		 * where column_constraint is:
		 *
		 * [ CONSTRAINT constraint_name ] { NOT NULL | NULL | UNIQUE | PRIMARY
		 * KEY | CHECK (expression) | REFERENCES reftable [ ( refcolumn ) ] [
		 * MATCH FULL | MATCH PARTIAL | MATCH SIMPLE ] [ ON DELETE action ] [ ON
		 * UPDATE action ] } [ DEFERRABLE | NOT DEFERRABLE ] [ INITIALLY
		 * DEFERRED | INITIALLY IMMEDIATE ]
		 *
		 * and table_constraint is:
		 *
		 * [ CONSTRAINT constraint_name ] { UNIQUE ( column_name [, ... ] ) |
		 * PRIMARY KEY ( column_name [, ... ] ) | CHECK ( expression ) | FOREIGN
		 * KEY ( column_name [, ... ] ) REFERENCES reftable [ ( refcolumn [, ...
		 * ] ) ] [ MATCH FULL | MATCH PARTIAL | MATCH SIMPLE ] [ ON DELETE
		 * action ] [ ON UPDATE action ] } [ DEFERRABLE | NOT DEFERRABLE ] [
		 * INITIALLY DEFERRED | INITIALLY IMMEDIATE ]
		 */

		if (!(ndsp instanceof NewFeatureStoreParameters)) {
			// FIXME exception
			throw new IllegalArgumentException();
		}
		checkIsMine(ndsp);

		NewFeatureStoreParameters nfdsp = (NewFeatureStoreParameters) ndsp;

		StringBuilder sql = new StringBuilder();

		if (!nfdsp.isValid()) {
			// TODO Exception
			throw new InitializeException(this.getName(), new Exception(
					"Parameters not valid"));
		}
		try {
			nfdsp.validate();
		} catch (ValidateDataParametersException e1) {
			throw new InitializeException(this.getName(), e1);
		}

		FeatureType fType = nfdsp.getDefaultFeatureType();

		sql.append("Create table " + ((JDBCStoreParameters) ndsp).tableID()
				+ "(");
		Iterator attrs = fType.iterator();
		String sqlAttr;
		List sqlAttrs = new ArrayList();

		while (attrs.hasNext()) {
			sqlAttr = helper
					.getSqlFieldDescription((FeatureAttributeDescriptor) attrs
							.next());
			if (sqlAttr != null) {
				sqlAttrs.add(sqlAttr);
			}
		}

		helper.stringJoin(sqlAttrs, ", ", sql);

		sql.append(")");

		final String sqlCreate = sql.toString();
		final List sqlAdditional = helper
				.getAdditionalSqlToCreate(ndsp, fType);

		TransactionalAction action = new TransactionalAction() {

			public boolean continueTransactionAllowed() {
				// TODO Auto-generated method stub
				return false;
			}

			public Object action(Connection conn) throws DataException {
				Statement st = null;

				try {
					st = conn.createStatement();
				} catch (SQLException e1) {
					throw new JDBCSQLException(e1);
				}
				String sql = null;

				try {
					sql = sqlCreate;
					st.execute(sql);
					if (sqlAdditional != null) {
						Iterator iter = sqlAdditional.iterator();
						while (iter.hasNext()) {
							sql = (String) iter.next();
							st.execute(sql);
						}
					}

				} catch (SQLException e) {
					throw new JDBCExecuteSQLException(sql, e);
				} finally {
					try {
						st.close();
					} catch (SQLException e) {
						logger.error("Exception clossing statement", e);
					}
					;
				}

				return Boolean.TRUE;
			}

		};

		Boolean result = Boolean.FALSE;

		try {
			result = (Boolean) helper.doConnectionAction(action);
		} catch (Exception e) {
			// FIXME Exception
			throw new RuntimeException(e);
		}

		return result.booleanValue();
	}


}
