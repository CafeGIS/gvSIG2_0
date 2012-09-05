package org.gvsig.fmap.data.feature.db.jdbc.postgresql;

import java.security.KeyException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.gvsig.fmap.dal.DataServerExplorerParameters;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.NewDataStoreParameters;
import org.gvsig.fmap.dal.exception.CloseException;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.NewFeatureStoreParameters;
import org.gvsig.fmap.dal.feature.exception.InitializeWriterException;
import org.gvsig.fmap.dal.resource.ResourceManager;
import org.gvsig.fmap.dal.resource.impl.DefaultResourceManager;
import org.gvsig.fmap.data.feature.db.DBAttributeDescriptor;
import org.gvsig.fmap.data.feature.db.DBFeatureType;
import org.gvsig.fmap.data.feature.db.jdbc.JDBCExplorer;
import org.gvsig.fmap.data.feature.db.jdbc.JDBCExplorerParameter;

public class PostgresqlExplorer extends JDBCExplorer {
	public static String DATAEXPLORER_NAME = "PostgresqlExplorer";
	private String defaultSchema;

	private void appendFieldToCreteSQL(DBAttributeDescriptor attr,StringBuffer sql) throws InitializeException{
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

		//name
		sql.append(attr.getName());
		sql.append(" ");

		//dataType
		String type =attr.getDataType();
		if (type.equals(FeatureAttributeDescriptor.STRING)){
			if (attr.getSize() < 1 || attr.getSize() > 255) {
				sql.append("text");
			} else {
				sql.append("varchar(" + attr.getSize() + ")");
			}
		} else if (type.equals(FeatureAttributeDescriptor.BOOLEAN)){
			sql.append("bool");
		} else if (type.equals(FeatureAttributeDescriptor.BYTE)){
			sql.append("smallint");
		} else if (type.equals(FeatureAttributeDescriptor.DATE)){
			sql.append("date");
		} else if (type.equals(FeatureAttributeDescriptor.TIMESTAMP)){
			sql.append("timestamp");
		} else if (type.equals(FeatureAttributeDescriptor.TIME)){
			sql.append("time");
		} else if (type.equals(FeatureAttributeDescriptor.DOUBLE)){
			sql.append("double precision");
			if (attr.getPrecision() > 0){
				sql.append(" "+ attr.getPrecision());
			}
		} else if (type.equals(FeatureAttributeDescriptor.FLOAT)){
			sql.append("real");
		} else if (type.equals(FeatureAttributeDescriptor.GEOMETRY)){
			sql.append("geometry");
		} else if (type.equals(FeatureAttributeDescriptor.INT)){
			if (attr.isAutoIncrement()){
				sql.append("serial");
			}else{
				sql.append("integer");
			}
		} else if (type.equals(FeatureAttributeDescriptor.LONG)){
			if (attr.isAutoIncrement()){
				sql.append("bigserial");
			}else{
				sql.append("bigint");
			}
		} else {
			throw new InitializeException(this.getName(),new Exception("Unsuported type "+type));
		}
		sql.append(" ");

//		//DefeaultValue
//		if (attr.getDefaultValue() != null || (attr.getDefaultValue() == null && attr.isAllowNull() )){
//			sql.append("DEFAULT ? ");
//			sqlParams.add(attr.getDefaultValue());
//
//		}

		//Null
		if (attr.isAllowNull()){
			sql.append("NOT NULL ");
		} else {
			sql.append("NULL ");
		}

		// Primery key
		if (attr.isPrimaryKey()) {
			sql.append("PRIMARY KEY ");
		}

	}


	public DataStoreParameters add(NewFeatureStoreParameters ndsp)
			throws InitializeException, InitializeWriterException {
		Connection conn;
		try {
			conn = this.getConnection();
		} catch (ReadException e1) {
			throw new InitializeException(this.getName(),e1);
		}

		try {
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			throw new InitializeException(this.getName(),e);
		}
		DataStoreParameters params = this.add(ndsp,conn);

		return params;
	}


	protected DataStoreParameters add(NewFeatureStoreParameters ndsp,Connection conn)
		throws InitializeException, InitializeWriterException {

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

//		PreparedStatement st=null;
		Statement st=null;
		StringBuffer sql = new StringBuffer();

		if (!ndsp.isValid()){
			//TODO Exception
			throw new InitializeException(this.getName(),new Exception("Parameters not valid"));
		}
		PostgresqlStoreParameters param = (PostgresqlStoreParameters) ndsp
				.getDataStoreParameters();
		DBFeatureType fType = (DBFeatureType)ndsp.getFeatureType();

		sql.append("Create table " + param.tableID() + "(");
		DBAttributeDescriptor attr;
		FeatureAttributeDescriptor[] fads=(FeatureAttributeDescriptor[])fType.toArray(new FeatureAttributeDescriptor[0]);
		for (int j = 0; j < fads.length-1; j++) {

//			appendFieldToCreteSQL(attr, sql,sqlParamas);
			appendFieldToCreteSQL((DBAttributeDescriptor)fads[j], sql);
			sql.append(",");
		}
		attr = (DBAttributeDescriptor)fads[fads.length-1];
		appendFieldToCreteSQL(attr, sql);
		sql.append(")");

		try{
//			st = conn.prepareStatement(sql.toString());
//			for (i=0;i<sqlParamas.size();i++){
//				st.setObject(i+1, sqlParamas.get(i));
//			}
			st = conn.createStatement();
			st.execute(sql.toString());
		} catch (SQLException e) {
			throw new InitializeException(this.getName(),e);
		} finally{
			if (st != null){
				try {
					st.close();
				} catch (SQLException e) {
					// TODO ???
					e.printStackTrace();
				}
			}
		}
		return param;
	}

	public NewDataStoreParameters createNewDataStoreParameter()
			throws InitializeException {
		return new PostgresqlNewStoreParameter(this.parameters
				.newStoreParameters());
	}

	public void remove(DataStoreParameters dsp) throws ReadException {
		Connection conn = this.getConnection();

		try {
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			throw new InitializeException(this.getName(),e);
		}
		this.remove(dsp, conn);
	}

	protected void remove(DataStoreParameters dsp,Connection conn) throws ReadException {
		Statement st=null;
		try{
			st = conn.createStatement();
			st.execute("Drop table "
					+ ((PostgresqlStoreParameters) dsp).tableID());
		} catch (SQLException e) {
			throw new ReadException(this.getName(),e);
		} finally{
			if (st != null){
				try {
					st.close();
				} catch (SQLException e) {
					// TODO ???
					e.printStackTrace();
				}
			}
		}
	}

	public boolean canCreate() {
		return true;
	}

	public String getName() {
		return DATAEXPLORER_NAME;
	}

	public void init(DataServerExplorerParameters parameters) throws InitializeException {

		PostgresqlResource tmpResource = new PostgresqlResource(
				(JDBCExplorerParameter) parameters);
		PostgresqlResource theResource;
		DefaultResourceManager resMan = DefaultResourceManager.getResourceManager();

		try {
			theResource = (PostgresqlResource) resMan.addResource(tmpResource,
					this);
		} catch (DataException e1) {
			throw new InitializeException(this.getName(),e1);
		}

		super.init(parameters,theResource);
		try {
			this.defaultSchema = PostgresqlStoreUtils.getDefaultSchema(this
					.getConnection(), ((JDBCExplorerParameter) this.parameters)
					.getCatalog());
		} catch (ReadException e) {
			throw new InitializeException(this.getName(),e);
		}
	}

	public DataStoreParameters[] list() throws ReadException {
		return this.list(((JDBCExplorerParameter)this.parameters).isShowInformationDBTables());
	}

	public DataStoreParameters[] list(boolean showInformationDBTables) throws ReadException {
		Connection conn = this.getConnection();
		StringBuffer sqlBuf = new StringBuffer();
		String sql;
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
			sql = sqlBuf
					.toString()
					.replaceAll("xxWHERExx",
							"");
		} else {
			sql = sqlBuf
					.toString()
					.replaceAll("xxWHERExx",
							"WHERE TABLE_SCHEMA NOT IN ('information_schema','pg_catalog')");

		}
		ArrayList paramList = new ArrayList();
		PostgresqlStoreParameters param = null;
		ResultSet rs = null;
		Statement st = null;
		try{
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()){
				param = (PostgresqlStoreParameters) this.parameters
						.newStoreParameters();
				param.setCatalog(rs.getString(1));
				param.setSchema(rs.getString(2));
				param.setTableName(rs.getString(3));
				param.setFields(new String[] { "*" });
				if (rs.getBoolean(4)) {
					param.setIsView(param.IS_VIEW);
				} else {
					param.setIsView(param.NOT_IS_VIEW);
				}
				paramList.add(param);
			}


		} catch (SQLException e) {
			throw new ReadException(this.getName(),e);
		} finally {
			if (st != null) {
				if (rs != null) {
					try {
						rs.close();
					} catch (java.sql.SQLException e) {
						throw new InitializeException(
								PostgresqlStore.DATASTORE_NAME, e);
					}
				}
				try {
					st.close();
				} catch (java.sql.SQLException e) {
					throw new InitializeException(
							PostgresqlStore.DATASTORE_NAME, e);
				}

			}

		}


		return (DataStoreParameters[]) paramList
				.toArray(new PostgresqlStoreParameters[0]);

	}

	public FeatureType[] getFeatureTypes(DataStoreParameters dsp) throws ReadException {
		return new FeatureType[] { PostgresqlStoreUtils.getFeatureType(this
				.getConnection(), (PostgresqlStoreParameters) dsp) };
	}

	public String getDefaultSchema(){
		return this.defaultSchema;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.DataServerExplorer#dispose()
	 */
	public void dispose() throws DataException {
		ResourceManager resMan = DefaultResourceManager.getResourceManager();

    	try {
			resMan.remove(this.resource, this);
		} catch (DataException e1) {
			throw new CloseException(this.getName(),e1);
		} catch (KeyException e) {
			throw new CloseException(this.getName(),e);
		}

	}

}
