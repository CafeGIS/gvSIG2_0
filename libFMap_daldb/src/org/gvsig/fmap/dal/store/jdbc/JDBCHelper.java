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
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.NewDataStoreParameters;
import org.gvsig.fmap.dal.exception.CloseException;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.exception.OpenException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.exception.WriteException;
import org.gvsig.fmap.dal.feature.EditableFeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.EditableFeatureType;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.exception.UnsupportedDataTypeException;
import org.gvsig.fmap.dal.resource.exception.AccessResourceException;
import org.gvsig.fmap.dal.resource.exception.ResourceBeginException;
import org.gvsig.fmap.dal.resource.spi.ResourceConsumer;
import org.gvsig.fmap.dal.resource.spi.ResourceManagerProviderServices;
import org.gvsig.fmap.dal.resource.spi.ResourceProvider;
import org.gvsig.fmap.dal.store.jdbc.exception.JDBCException;
import org.gvsig.fmap.dal.store.jdbc.exception.JDBCExecuteSQLException;
import org.gvsig.fmap.dal.store.jdbc.exception.JDBCSQLException;
import org.gvsig.fmap.dal.store.jdbc.exception.JDBCTransactionCommitException;
import org.gvsig.fmap.dal.store.jdbc.exception.JDBCTransactionRollbackException;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.operation.fromwkb.FromWKB;
import org.gvsig.fmap.geom.operation.fromwkb.FromWKBGeometryOperationContext;
import org.gvsig.fmap.geom.operation.towkb.ToWKB;
import org.gvsig.fmap.geom.operation.towkb.ToWKBOperationContext;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.tools.exception.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jmvivo
 *
 */
public class JDBCHelper implements ResourceConsumer {

	private static Logger logger = LoggerFactory.getLogger(JDBCHelper.class);

	protected JDBCHelperUser user;
	protected boolean isOpen;
	protected String name;
	protected String defaultSchema;
	protected JDBCConnectionParameters params;
	private JDBCResource resource;

	protected FromWKB fromWKB = null;
	protected FromWKBGeometryOperationContext fromWKBContext = null;
	protected ToWKBOperationContext toWKBContext = new ToWKBOperationContext();

	private Boolean allowAutomaticValues = null;
	private Boolean supportsUnions = null;

	private String identifierQuoteString;

	protected JDBCHelper(JDBCHelperUser consumer,
			JDBCConnectionParameters params) throws InitializeException {

		this.user = consumer;
		this.name = user.getName();
		this.params = params;
		initializeResource();

	}

	protected void initializeResource() throws InitializeException {
		ResourceManagerProviderServices manager = (ResourceManagerProviderServices) DALLocator
				.getResourceManager();
		JDBCResource resource = (JDBCResource) manager
				.createResource(
				JDBCResource.NAME, new Object[] { params.getUrl(),
						params.getHost(), params.getPort(), params.getDBName(),
						params.getUser(), params.getPassword(),
						params.getJDBCDriverClassName() });
		this.setResource(resource);

	}

	protected final void setResource(JDBCResource resource) {
		this.resource = resource;
		this.resource.addConsumer(this);
	}

	public boolean closeResourceRequested(ResourceProvider resource) {
		return user.closeResourceRequested(resource);
	}

	public void resourceChanged(ResourceProvider resource) {
		user.resourceChanged(resource);

	}

	/**
	 * open the resource
	 *
	 * @return true if the resourse was open in this call
	 * @throws OpenException
	 */
	public boolean open() throws OpenException {
		if (isOpen) {
			return false;
		}
		try {
			begin();
		} catch (ResourceBeginException e1) {
			throw new OpenException(name, e1);
		}
		try {
			resource.connect();
			resource.notifyOpen();

			this.user.opendDone();

			isOpen = true;

			return true;
		} catch (DataException e) {
			throw new OpenException(name, e);
		} finally {
			end();
		}

	}

	public void close() throws CloseException {
		if (!isOpen) {
			return;
		}
		try {
			begin();
		} catch (ResourceBeginException e) {
			throw new CloseException(name, e);
		}
		try {

			isOpen = false;

			resource.notifyClose();
			this.user.closeDone();
		} catch (DataException e) {
			throw new CloseException(this.name, e);
		} finally {
			end();
		}
	}

	public void end() {
		resource.end();
	}

	public void begin() throws ResourceBeginException {
		this.resource.begin();
	}

	public Connection getConnection() throws AccessResourceException {
		return resource.getJDBCConnection();

	}

	public void dispose() {
		resource.removeConsumer(this);
	}

	public boolean isOpen() {
		return isOpen;
	}

	/**
	 * Executes an atomic action that uses an DB Connection.<br>
	 *
	 * This methos prepares a connection and close it at the end of execution of
	 * action.<br>
	 *
	 * if <code>action</code> is an instance of {@link TransactionalAction} the
	 * action will be execute inside of a DB transaction.
	 *
	 *
	 * @param action
	 * @throws Exception
	 */
	public Object doConnectionAction(ConnectionAction action) throws Exception {
		Object result = null;
		Connection conn = null;
		boolean beginTrans = false;
		this.open();
		this.begin();
		try {

			conn = this.getConnection();
			if (action instanceof TransactionalAction) {
				// XXX OJO esta condicion NO ES FIABLE
				if (!conn.getAutoCommit()) {
					if (!((TransactionalAction) action)
							.continueTransactionAllowed()) {
						// FIXME exception
						throw new Exception();
					}
				}
				try {
					conn.setAutoCommit(false);
				} catch (SQLException e) {
					throw new JDBCSQLException(e);
				}
				beginTrans = true;
			}

			result = action.action(conn);

			if (beginTrans) {
				try {
					conn.commit();
				} catch (SQLException e) {
					throw new JDBCTransactionCommitException(e);
				}
			}

			return result;

		} catch (Exception e) {

			if (beginTrans) {
				try {
					conn.rollback();
				} catch (Exception e1) {
					throw new JDBCTransactionRollbackException(e1, e);
				}
			}
			throw e;

		} finally {
			try {
				conn.close();
			} catch (Exception e1) {
				logger.error("Exception on close connection", e1);
			}
			this.end();
		}

	}

	protected String getDefaultSchema(Connection conn) throws JDBCException {
		return defaultSchema;
	}

	protected EditableFeatureAttributeDescriptor createAttributeFromJDBC(
			EditableFeatureType fType, Connection conn,
			ResultSetMetaData rsMetadata, int colIndex)
			throws java.sql.SQLException {

		EditableFeatureAttributeDescriptor column;
		switch (rsMetadata.getColumnType(colIndex)) {
		case java.sql.Types.INTEGER:
			column = fType.add(rsMetadata.getColumnName(colIndex),
					DataTypes.INT);
			break;
		case java.sql.Types.BIGINT:
			column = fType.add(rsMetadata.getColumnName(colIndex),
					DataTypes.LONG);
			break;
		case java.sql.Types.REAL:
			column = fType.add(rsMetadata.getColumnName(colIndex),
					DataTypes.DOUBLE);
			break;
		case java.sql.Types.DOUBLE:
			column = fType.add(rsMetadata.getColumnName(colIndex),
					DataTypes.DOUBLE);
			break;
		case java.sql.Types.CHAR:
			column = fType.add(rsMetadata.getColumnName(colIndex),
					DataTypes.STRING);
			break;
		case java.sql.Types.VARCHAR:
		case java.sql.Types.LONGVARCHAR:
			column = fType.add(rsMetadata.getColumnName(colIndex),
					DataTypes.STRING);
			break;
		case java.sql.Types.FLOAT:
			column = fType.add(rsMetadata.getColumnName(colIndex),
					DataTypes.FLOAT);
			break;
		case java.sql.Types.DECIMAL:
			column = fType.add(rsMetadata.getColumnName(colIndex),
					DataTypes.FLOAT);
			break;
		case java.sql.Types.DATE:
			column = fType.add(rsMetadata.getColumnName(colIndex),
					DataTypes.DATE);
			break;
		case java.sql.Types.TIME:
			column = fType.add(rsMetadata.getColumnName(colIndex),
					DataTypes.TIME);
			break;
		case java.sql.Types.TIMESTAMP:
			column = fType.add(rsMetadata.getColumnName(colIndex),
					DataTypes.TIMESTAMP);
			break;
		case java.sql.Types.BOOLEAN:
			column = fType.add(rsMetadata.getColumnName(colIndex),
					DataTypes.BOOLEAN);
			break;
		case java.sql.Types.BLOB:
		case java.sql.Types.BINARY:
		case java.sql.Types.LONGVARBINARY:
			column = fType.add(rsMetadata.getColumnName(colIndex),
					DataTypes.BYTEARRAY);
			break;

		default:
			column = fType.add(rsMetadata.getColumnName(colIndex),
					DataTypes.OBJECT);
			column.setAdditionalInfo("SQLType", new Integer(rsMetadata
					.getColumnType(colIndex)));
			column.setAdditionalInfo("SQLTypeName", rsMetadata
					.getColumnTypeName(colIndex));

			break;
		}

		return column;

	}

	protected EditableFeatureAttributeDescriptor getAttributeFromJDBC(
			EditableFeatureType fType, Connection conn,
			ResultSetMetaData rsMetadata, int colIndex) throws JDBCException {
		EditableFeatureAttributeDescriptor column;
		try {

			column = createAttributeFromJDBC(fType, conn, rsMetadata, colIndex);
			// column.setCaseSensitive(rsMetadata.isCaseSensitive(colIndex));
			// column.setSqlType(rsMetadata.getColumnType(colIndex));
			column.setAllowNull(
					rsMetadata.isNullable(colIndex) == ResultSetMetaData.columnNullable);
			column.setIsAutomatic(rsMetadata.isAutoIncrement(colIndex));
			column.setIsReadOnly(rsMetadata.isReadOnly(colIndex));
			// column.setWritable(rsMetadata.isWritable(colIndex));
			// column.setClassName(rsMetadata.getColumnClassName(colIndex));
			// column.setCatalogName(rsMetadata.getCatalogName(colIndex));
			// column.setDefinitelyWritable(rsMetadata
			// .isDefinitelyWritable(colIndex));
			// column.setLabel(rsMetadata.getColumnLabel(colIndex));
			// column.setSchemaName(rsMetadata.getSchemaName(colIndex));
			// column.setTableName(rsMetadata.getTableName(colIndex));
			// column.setCatalogName(rsMetadata.getCatalogName(colIndex));
			// column.setSqlTypeName();
			// column.setSearchable(rsMetadata.isSearchable(colIndex));
			// column.setSigned(rsMetadata.isSigned(colIndex));
			// column.setCurrency(rsMetadata.isCurrency(colIndex));
			column.setPrecision(rsMetadata.getPrecision(colIndex));
			column.setSize(rsMetadata.getColumnDisplaySize(colIndex));

		} catch (java.sql.SQLException e) {
			throw new JDBCSQLException(e);
		}

		return column;

	}

	/**
	 * Fill <code>featureType</code> geometry attributes with SRS and ShapeType
	 * information
	 *
	 * <b>Override this if provider has native eometry support</b>
	 *
	 * @param conn
	 * @param rsMetadata
	 * @param featureType
	 * @throws ReadException
	 */
	protected void loadSRS_and_shapeType(Connection conn,
			ResultSetMetaData rsMetadata, EditableFeatureType featureType,
			String baseSchema, String baseTable) throws JDBCException {

		// Nothing to do

	}

	public void loadFeatureType(EditableFeatureType featureType,
			JDBCStoreParameters storeParams) throws DataException {
		if (storeParams.getSQL() != null
				&& storeParams.getSQL().trim().length() == 0) {
			loadFeatureType(featureType, storeParams, storeParams.getSQL(),
					null, null);
		} else {
			String sql = "Select * from " + storeParams.tableID()
					+ " where false";
			loadFeatureType(featureType, storeParams, sql, storeParams
					.getSchema(), storeParams.getTable());
		}
	}

	public void loadFeatureType(EditableFeatureType featureType,
			JDBCStoreParameters storeParams, String sql, String schema,
			String table) throws DataException {
		Connection conn = null;
		this.open();
		this.begin();
		try {
			conn = getConnection();

			String[] pks = storeParams.getPkFields();
			if (pks == null || pks.length < 1) {
				if (storeParams.getTable() != null
						&& storeParams.getTable().trim().length() > 0) {
					pks = getPksFrom(conn, storeParams);

				}
			}

			loadFeatureType(conn, featureType, sql, pks, storeParams
					.getDefaultGeometry(), schema, table);

		} finally {
			try {
				conn.close();
			} catch (Exception e) {
			}
			;
			this.end();
		}
	}

	protected String[] getPksFrom(Connection conn, JDBCStoreParameters params)
		throws JDBCException {
		try{
			DatabaseMetaData metadata = conn.getMetaData();
			ResultSet rsPrimaryKeys = null;
			ResultSet rs = null;
			String catalog = params.getCatalog();
			String schema = params.getSchema();

			try{
				rs = metadata.getTables(catalog,
						schema, params.getTable(), null);

				if (!rs.next()) {
					// No tables found with default values, ignoring catalog
					rs.close();
					catalog = null;
					schema = null;
					rs = metadata
							.getTables(catalog, schema, params.getTable(), null);

					if (!rs.next()) {
						// table not found
						return null;
					} else if (rs.next()){
						// More that one, cant identify
						return null;
					}

				} else if (rs.next()) {
					// More that one, cant identify
					return null;
				}
				rsPrimaryKeys = metadata.getPrimaryKeys(catalog, schema, params
						.getTable());
				List pks = new ArrayList();
				while (rsPrimaryKeys.next()){
					pks.add(rsPrimaryKeys.getString("COLUMN_NAME"));
				}
				return (String[]) pks.toArray(new String[pks.size()]);


			} finally {
				try{if (rs != null) {
					rs.close();
				}} catch (SQLException ex) {logger.warn("Exception closing tables rs", ex);};
				try{if (rsPrimaryKeys != null) {
					rsPrimaryKeys.close();
				}} catch (SQLException ex) {logger.warn("Exception closing pk rs", ex);};
			}


		} catch (SQLException e) {
			logger.warn("Unable to get pk from DatabaseMetada", e);
			return getPksFromInformationSchema(conn, params);
		}

	}

	protected String[] getPksFromInformationSchema(Connection conn,
			JDBCStoreParameters params)
			throws JDBCException {
		Statement st;
		StringBuffer sql = new StringBuffer();
		ResultSet rs;
		ArrayList list = new ArrayList();

		/*
		 select column_name as primary_key
			from information_schema.table_constraints t_cons
				inner join information_schema.key_column_usage c on
					c.constraint_catalog = t_cons.table_catalog and
				    c.table_schema = t_cons.table_schema and
				    c.table_name = t_cons.table_name and
					c.constraint_name = t_cons.constraint_name
				where t_cons.table_schema = <schema>
				and t_cons.constraint_catalog = <catalog>
 				and t_cons.table_name = <table>
 				and constraint_type = 'PRIMARY KEY'
		 */
		/*
		 * SELECT column_name FROM INFORMATION_SCHEMA.constraint_column_usage
		 * left join INFORMATION_SCHEMA.table_constraints on
		 * (INFORMATION_SCHEMA.table_constraints.constraint_name =
		 * INFORMATION_SCHEMA.constraint_column_usage.constraint_name and
		 * INFORMATION_SCHEMA.table_constraints.table_name =
		 * INFORMATION_SCHEMA.constraint_column_usage.table_name and
		 * INFORMATION_SCHEMA.table_constraints.table_schema =
		 * INFORMATION_SCHEMA.constraint_column_usage.table_schema) WHERE
		 * INFORMATION_SCHEMA.constraint_column_usage.table_name like
		 * 'muni10000_peq' AND
		 * INFORMATION_SCHEMA.constraint_column_usage.table_schema like 'public'
		 * AND INFORMATION_SCHEMA.constraint_column_usage.table_catalog like
		 * 'gis' AND constraint_type='PRIMARY KEY'
		 */

		sql.append("select column_name as primary_key ");
		sql.append("from information_schema.table_constraints t_cons ");
		sql.append("inner join information_schema.key_column_usage c on ");
		sql.append("c.constraint_catalog = t_cons.constraint_catalog and ");
		sql.append("c.table_schema = t_cons.table_schema and ");
		sql.append("c.table_name = t_cons.table_name and ");
		sql.append("c.constraint_name = t_cons.constraint_name ");
		sql.append("WHERE t_cons.table_name like '");

		sql.append(params.getTable());
		sql.append("' ");
		String schema = null;


		if (params.getSchema() == null || params.getSchema() == "") {
			schema = getDefaultSchema(conn);
		} else {
			schema = params.getSchema();
		}
		if (schema != null) {
			sql.append(" and t_cons.table_schema like '");
			sql.append(schema);
			sql.append("' ");
		}

		if (params.getCatalog() != null && params.getCatalog() != "") {
			sql
					.append(" and t_cons.constraint_catalog like '");
			sql.append(params.getCatalog());
			sql.append("' ");
		}

		sql.append("' and constraint_type = 'PRIMARY KEY'");

		// System.out.println(sql.toString());
		try {
			st = conn.createStatement();
			try {
				rs = st.executeQuery(sql.toString());
			} catch (java.sql.SQLException e) {
				throw new JDBCExecuteSQLException(sql.toString(), e);
			}
			while (rs.next()) {
				list.add(rs.getString(1));
			}
			rs.close();
			st.close();

		} catch (java.sql.SQLException e) {
			throw new JDBCSQLException(e);
		}
		if (list.size() == 0) {
			return null;
		}

		return (String[]) list.toArray(new String[0]);

	}

	protected void loadFeatureType(Connection conn,
			EditableFeatureType featureType, String sql, String[] pks,
			String defGeomName, String schema, String table)
			throws DataException {

		Statement stAux = null;
		ResultSet rs = null;
		try {

			stAux = conn.createStatement();
			stAux.setFetchSize(1);

			try {
				rs = stAux.executeQuery(sql);
			} catch (SQLException e) {
				throw new JDBCExecuteSQLException(sql, e);
			}
			ResultSetMetaData rsMetadata = rs.getMetaData();

			List pksList = null;
			if (pks != null) {
				pksList = Arrays.asList(pks);

			}

			int i;
			int geometriesColumns = 0;
			String lastGeometry = null;

			EditableFeatureAttributeDescriptor attr;
			for (i = 1; i <= rsMetadata.getColumnCount(); i++) {
				attr = getAttributeFromJDBC(featureType, conn, rsMetadata, i);
				if (pksList != null && pksList.contains(attr.getName())) {
					attr.setIsPrimaryKey(true);
				}
				if (attr.getDataType() == DataTypes.GEOMETRY) {
					geometriesColumns++;
					lastGeometry = attr.getName();
					if (lastGeometry.equals(defGeomName)) {
						featureType
								.setDefaultGeometryAttributeName(defGeomName);
					}

				}

			}

			if (geometriesColumns > 0) {
				loadSRS_and_shapeType(conn, rsMetadata, featureType, schema,
						table);
			}

			if (defGeomName == null && geometriesColumns == 1) {
				featureType.setDefaultGeometryAttributeName(lastGeometry);
				defGeomName = lastGeometry;
			}

		} catch (java.sql.SQLException e) {
			throw new JDBCSQLException(e); // FIXME exception
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
			}
			try {
				stAux.close();
			} catch (Exception e) {
			}

		}

	}

	/**
	 * Override if provider has geometry support
	 *
	 * @param storeParams
	 * @param geometryAttrName
	 * @param limit
	 * @return
	 * @throws DataException
	 */
	public Envelope getFullEnvelopeOfField(JDBCStoreParameters storeParams,
			String geometryAttrName, Envelope limit) throws DataException {

		// TODO
		return null;

	}

	protected void initializeFromWKBOperation() throws BaseException {
		if (fromWKB == null) {
			fromWKB = (FromWKB) GeometryLocator.getGeometryManager()
					.getGeometryOperation(FromWKB.CODE,
							Geometry.TYPES.GEOMETRY, Geometry.SUBTYPES.GEOM2D);
			fromWKBContext = new FromWKBGeometryOperationContext();

		}
	}

	public Geometry getGeometry(byte[] buffer) throws BaseException {
		if (buffer == null) {
			return null;
		}
		initializeFromWKBOperation();
		Geometry geom;
		try {
			fromWKBContext.setData(buffer);

			geom = (Geometry) fromWKB.invoke(null, fromWKBContext);
		} finally {
			fromWKBContext.setData(null);
		}
		return geom;
	}

	public String escapeFieldName(String field) {
		if (field.matches("[a-z][a-z0-9_]*")) {
			return field;
		}
		String quote = getIdentifierQuoteString();
		return quote + field + quote;
	}

	public Object dalValueToJDBC(
			FeatureAttributeDescriptor attributeDescriptor, Object object)
			throws WriteException {
		if (object == null) {
			return null;
		}

		if (attributeDescriptor.getDataType() != DataTypes.GEOMETRY) {
			return object;
		}
		try {
			Geometry geom = (Geometry) object;

			toWKBContext.setSrID(-1);
			IProjection srs = attributeDescriptor.getSRS();
			if (srs != null) {
				toWKBContext.setSrID(getProviderSRID(srs));
			}

			// TODO optimize this
			// byte[] wkb = (byte[])
			// geom.invokeOperation(ToWKBNative.CODE,toWKBContext);
			byte[] wkb = (byte[]) geom.invokeOperation(ToWKB.CODE,
					toWKBContext);
			if (wkb == null) {
				// FIXME excpetion
				throw new IllegalArgumentException();
			}

			return wkb;
		} catch (Exception e) {
			throw new WriteException(this.name, e);
		}
	}

	public String getSqlColumnTypeDescription(FeatureAttributeDescriptor attr) {
		switch (attr.getDataType()) {
		case DataTypes.STRING:
			if (attr.getSize() < 1 || attr.getSize() > 255) {
				return "text";
			} else {
				return "varchar(" + attr.getSize() + ")";
			}
		case DataTypes.BOOLEAN:
			return "bool";

		case DataTypes.BYTE:
			return "smallint";

		case DataTypes.DATE:
			return "date";

		case DataTypes.TIMESTAMP:
			return "timestamp";

		case DataTypes.TIME:
			return "time";

		case DataTypes.BYTEARRAY:
		case DataTypes.GEOMETRY:
			return "blob";

		case DataTypes.DOUBLE:
			// if (attr.getPrecision() > 0) {
			// return "double precision(" + attr.getPrecision() + ')';
			// } else {
			return "double";
			// }
		case DataTypes.FLOAT:
			return "real";

		case DataTypes.INT:
			if (attr.isAutomatic() && allowAutomaticValues()) {
				return "serial";
			} else {
				return "integer";
			}
		case DataTypes.LONG:
			if (attr.isAutomatic()) {
				return "bigserial";
			} else {
				return "bigint";
			}

		default:
			String typeName = (String) attr.getAdditionalInfo("SQLTypeName");
			if (typeName != null) {
				return typeName;
			}

			throw new UnsupportedDataTypeException(attr.getDataTypeName(), attr
					.getDataType());
		}
	}

	public int getProviderSRID(String srs) {
		return -1;
	}

	public int getProviderSRID(IProjection srs) {
		return -1;
	}

	public String getSqlFieldName(FeatureAttributeDescriptor attribute) {
		return escapeFieldName(attribute.getName());
	}

	public String getSqlFieldDescription(FeatureAttributeDescriptor attr)
			throws DataException {

		/**
		 * column_name data_type [ DEFAULT default_expr ] [ column_constraint [
		 * ... ] ]
		 *
		 * where column_constraint is:
		 *
		 * [ CONSTRAINT constraint_name ] { NOT NULL | NULL | UNIQUE | PRIMARY
		 * KEY | CHECK (expression) | REFERENCES reftable [ ( refcolumn ) ] [
		 * MATCH FULL | MATCH PARTIAL | MATCH SIMPLE ] [ ON DELETE action ] [ ON
		 * UPDATE action ] } [ DEFERRABLE | NOT DEFERRABLE ] [ INITIALLY
		 * DEFERRED | INITIALLY IMMEDIATE ]
		 */

		StringBuilder strb = new StringBuilder();
		// name
		strb.append(escapeFieldName(attr.getName()));
		strb.append(" ");

		// Type
		strb.append(this.getSqlColumnTypeDescription(attr));
		strb.append(" ");

		boolean allowNull = attr.allowNull()
				&& !(attr.isPrimaryKey() || attr.isAutomatic());
		// Default
		if (attr.getDefaultValue() == null) {
			if (allowNull) {
				strb.append("DEFAULT NULL ");
			}
		} else {
			String value = getDefaltFieldValueString(attr);
			strb.append("DEFAULT '");
			strb.append(value);
			strb.append("' ");
		}

		// Null
		if (allowNull) {
			strb.append("NULL ");
		} else {
			strb.append("NOT NULL ");
		}

		// Primery key
		if (attr.isPrimaryKey()) {
			strb.append("PRIMARY KEY ");
		}
		return strb.toString();
	}

	protected String getDefaltFieldValueString(FeatureAttributeDescriptor attr)
			throws WriteException {
		return dalValueToJDBC(attr, attr.getDefaultValue()).toString();
	}

	public String compoundLimitAndOffset(long limit, long offset) {
		StringBuilder sql = new StringBuilder();
		// limit
		if (limit >= 1) {
			sql.append(" limit ");
			sql.append(limit);
			sql.append(' ');
		}

		// offset
		if (offset >= 1) {
			sql.append(" offset ");
			sql.append(offset);
			sql.append(' ');
		}
		return sql.toString();
	}

	public boolean supportOffset() {
		return true;
	}

	public List getAdditionalSqlToCreate(NewDataStoreParameters ndsp,
			FeatureType fType) {
		// TODO Auto-generated method stub
		return null;
	}


	public String stringJoin(List listToJoin,String sep){
		StringBuilder strb = new StringBuilder();
		stringJoin(listToJoin,sep,strb);
		return strb.toString();
	}

	public void stringJoin(List listToJoin, String sep, StringBuilder strb) {
		if (listToJoin.size() < 1) {
			return;
		}
		if (listToJoin.size() > 1) {
			for (int i = 0; i < listToJoin.size() - 1; i++) {
				strb.append(listToJoin.get(i));
				strb.append(sep);
			}
		}
		strb.append(listToJoin.get(listToJoin.size() - 1));
	}
	/**
	 * Inform that provider has supports for geometry store and operations
	 * natively
	 *
	 * @return
	 */
	boolean hasGeometrySupport() {
		return false;
	}

	public boolean allowAutomaticValues() {
		if (allowAutomaticValues == null) {
			ConnectionAction action = new ConnectionAction(){

				public Object action(Connection conn) throws DataException {

					ResultSet rs;
					try {
						DatabaseMetaData meta = conn.getMetaData();
						rs = meta.getTypeInfo();
						try{
							while (rs.next()) {
								if (rs.getInt("DATA_TYPE") == java.sql.Types.INTEGER) {
									if (rs.getBoolean("AUTO_INCREMENT")) {
										return Boolean.TRUE;
									} else {
										return Boolean.FALSE;
									}
								}
							}
						}finally{
							try{ rs.close();} catch (SQLException ex) {logger.error("Exception closing resulset", ex);};
						}
					} catch (SQLException e) {
						throw new JDBCSQLException(e);
					}
					return Boolean.FALSE;
				}

			};



			try {
				allowAutomaticValues = (Boolean) doConnectionAction(action);
			} catch (Exception e) {
				logger.error("Exception checking for automatic integers", e);
				allowAutomaticValues = Boolean.FALSE;
			}
		}
		return allowAutomaticValues.booleanValue();
	}

	public boolean supportsUnion() {
		if (supportsUnions == null) {
			ConnectionAction action = new ConnectionAction() {

				public Object action(Connection conn) throws DataException {

					try {
						DatabaseMetaData meta = conn.getMetaData();
						return new Boolean(meta.supportsUnion());
					} catch (SQLException e) {
						throw new JDBCSQLException(e);
					}
				}

			};

			try {
				supportsUnions = (Boolean) doConnectionAction(action);
			} catch (Exception e) {
				logger.error("Exception checking for unions support", e);
				supportsUnions = Boolean.FALSE;
			}
		}
		return supportsUnions.booleanValue();
	}

	protected String getIdentifierQuoteString() {
		if (identifierQuoteString == null) {
		ConnectionAction action = new ConnectionAction() {

			public Object action(Connection conn) throws DataException {

				try {
					DatabaseMetaData meta = conn.getMetaData();
					return meta.getIdentifierQuoteString();
				} catch (SQLException e) {
					throw new JDBCSQLException(e);
				}
			}

		};

		try {
			identifierQuoteString = (String) doConnectionAction(action);
		} catch (Exception e) {
			logger.error("Exception checking for unions support", e);
			identifierQuoteString = " ";
			}
		}
		return identifierQuoteString;
	}

	protected boolean isReservedWord(String field) {
		// TODO
		return false;
	}

}
