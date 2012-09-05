package org.gvsig.fmap.data.feature.db.jdbc.h2;

import java.security.KeyException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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

public class H2Explorer extends JDBCExplorer {
	public static String DATAEXPLORER_NAME = "H2Explorer";
	private String defaultSchema;

	private void appendFieldToCreteSQL(DBAttributeDescriptor attr,StringBuffer sql) throws InitializeException{
		/**
		 * {name dataType
			[{AS computedColumnExpression | DEFAULT expression}]
			[[NOT] NULL]
			[{AUTO_INCREMENT | IDENTITY}[(startInt [, incrementInt])]]
			[SELECTIVITY selectivity]
			[PRIMARY KEY [HASH] | UNIQUE]
			| constraint}
		 */

		//name
		sql.append(attr.getName());
		sql.append(" ");

		//dataType
		String type =attr.getDataType();
		if (type.equals(FeatureAttributeDescriptor.STRING)){
			if (attr.getSize() < 1){
				sql.append("VARCHAR");
			} else {
				sql.append("VARCHAR("+attr.getSize()+")");
			}
		} else if (type.equals(FeatureAttributeDescriptor.BOOLEAN)){
			sql.append("BOOL");
		} else if (type.equals(FeatureAttributeDescriptor.BYTE)){
			sql.append("TINYINT");
		} else if (type.equals(FeatureAttributeDescriptor.DATE)){
			sql.append("DATE");
		} else if (type.equals(FeatureAttributeDescriptor.TIMESTAMP)){
			sql.append("TIMESTAMP");
		} else if (type.equals(FeatureAttributeDescriptor.TIME)){
			sql.append("TIME");
		} else if (type.equals(FeatureAttributeDescriptor.DOUBLE)){
			sql.append("DOUBLE");
			if (attr.getPrecision() > 0){
				sql.append(" "+ attr.getPrecision());
			}
		} else if (type.equals(FeatureAttributeDescriptor.FLOAT)){
			sql.append("REAL");
		} else if (type.equals(FeatureAttributeDescriptor.GEOMETRY)){
			sql.append("OTHER");
		} else if (type.equals(FeatureAttributeDescriptor.INT)){
			if (attr.isAutoIncrement()){
				sql.append("IDENTITY");
			}else{
				sql.append("INT");
			}
		} else if (type.equals(FeatureAttributeDescriptor.LONG)){
			if (attr.isAutoIncrement()){
				sql.append("IDENTITY");
			}else{
				sql.append("BIGINT");
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

		//Primery key
		if (attr.isPrimaryKey()){
			sql.append("PRIMARY KEY ");
		}

	}

	/**
	 * @deprecated
	 *
	 * @param attr
	 * @param sql
	 * @param sqlParams
	 * @throws InitializeException
	 */
	private void appendFieldToCreteSQL(DBAttributeDescriptor attr,StringBuffer sql,List sqlParams) throws InitializeException{
		/**
		 * {name dataType
			[{AS computedColumnExpression | DEFAULT expression}]
			[[NOT] NULL]
			[{AUTO_INCREMENT | IDENTITY}[(startInt [, incrementInt])]]
			[SELECTIVITY selectivity]
			[PRIMARY KEY [HASH] | UNIQUE]
			| constraint}
		 */

		//name
		sql.append(attr.getName());
		sql.append(" ");

		//dataType
		String type =attr.getDataType();
		if (type.equals(FeatureAttributeDescriptor.STRING)){
			if (attr.getSize() < 1){
				sql.append("VARCHAR");
			} else {
				sql.append("VARCHAR("+attr.getSize()+")");
			}
		} else if (type.equals(FeatureAttributeDescriptor.BOOLEAN)){
			sql.append("BOOL");
		} else if (type.equals(FeatureAttributeDescriptor.BYTE)){
			sql.append("TINYINT");
		} else if (type.equals(FeatureAttributeDescriptor.DATE)){
			sql.append("DATE");
		} else if (type.equals(FeatureAttributeDescriptor.TIMESTAMP)){
			sql.append("TIMESTAMP");
		} else if (type.equals(FeatureAttributeDescriptor.TIME)){
			sql.append("TIME");
		} else if (type.equals(FeatureAttributeDescriptor.DOUBLE)){
			sql.append("DOUBLE");
			if (attr.getPrecision() > 0){
				sql.append(" "+ attr.getPrecision());
			}
		} else if (type.equals(FeatureAttributeDescriptor.FLOAT)){
			sql.append("REAL");
		} else if (type.equals(FeatureAttributeDescriptor.GEOMETRY)){
			sql.append("OTHER");
		} else if (type.equals(FeatureAttributeDescriptor.INT)){
			if (attr.isAutoIncrement()){
				sql.append("IDENTITY");
			}else{
				sql.append("INT");
			}
		} else if (type.equals(FeatureAttributeDescriptor.LONG)){
			if (attr.isAutoIncrement()){
				sql.append("IDENTITY");
			}else{
				sql.append("BIGINT");
			}
		} else {
			throw new InitializeException(this.getName(),new Exception("Unsuported type "+type));
		}
		sql.append(" ");

		//DefeaultValue
		if (attr.getDefaultValue() != null || (attr.getDefaultValue() == null && attr.isAllowNull() )){
			sql.append("DEFAULT ? ");
			sqlParams.add(attr.getDefaultValue(), false);
		}

		//Null
		if (attr.isAllowNull()){
			sql.append("NOT NULL ");
		}

		//Primery key
		if (attr.isPrimaryKey()){
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
		 CREATE [CACHED | MEMORY | TEMP | [GLOBAL | LOCAL] TEMPORARY]
			TABLE [IF NOT EXISTS] name
			{ ( {name dataType
			[{AS computedColumnExpression | DEFAULT expression}]
			[[NOT] NULL]
			[{AUTO_INCREMENT | IDENTITY}[(startInt [, incrementInt])]]
			[SELECTIVITY selectivity]
			[PRIMARY KEY [HASH] | UNIQUE]
			| constraint} [,...] ) [ AS select ] } | { AS select }
		 */

//		PreparedStatement st=null;
		Statement st=null;
		StringBuffer sql = new StringBuffer();
		ArrayList sqlParamas = new ArrayList();

		if (!ndsp.isValid()){
			//TODO Exception
			throw new InitializeException(this.getName(),new Exception("Parameters not valid"));
		}
		H2StoreParameters h2Param = (H2StoreParameters)ndsp.getDataStoreParameters();
		DBFeatureType fType = (DBFeatureType)ndsp.getFeatureType();

		sql.append("Create table "+ h2Param.tableID()+"(");
		DBAttributeDescriptor attr;
		int i;
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
		return h2Param;
	}

	public NewDataStoreParameters createNewDataStoreParameter()
			throws InitializeException {
		return new H2NewStoreParameter(this.parameters.newStoreParameters());
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
			st.execute("Drop table "+ ((H2StoreParameters)dsp).tableID());
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

		H2Resource tmpResource = new H2Resource((H2ExplorerParameters)parameters);
		H2Resource theResource;
		DefaultResourceManager resMan = DefaultResourceManager.getResourceManager();

		try {
			theResource = (H2Resource) resMan.addResource(tmpResource, this);
		} catch (DataException e1) {
			throw new InitializeException(this.getName(),e1);
		}

		super.init(parameters,theResource);
		try {
			((H2Resource)this.resource).close();
			this.defaultSchema = H2Utils.getDefaultSchema(this.getConnection(),
					((JDBCExplorerParameter) this.parameters).getCatalog());
		} catch (ReadException e) {
			throw new InitializeException(this.getName(),e);
		}
	}


	public DataStoreParameters[] list() throws ReadException {
		return this.list(((H2ExplorerParameters)this.parameters).isShowInformationDBTables());
	}

	public DataStoreParameters[] list(boolean showInformationDBTables) throws ReadException {
		Connection conn = this.getConnection();
		String sql = "SELECT TABLE_CATALOG, TABLE_SCHEMA, TABLE_NAME FROM INFORMATION_SCHEMA.TABLES";
		if (!showInformationDBTables){
			sql = sql + " WHERE TABLE_SCHEMA <> 'INFORMATION_SCHEMA'";
		}
		ArrayList paramList = new ArrayList();
		H2StoreParameters h2param = null;

		try{
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()){
				h2param = (H2StoreParameters) this.parameters
						.newStoreParameters();
				h2param.setCatalog(rs.getString(1));
				h2param.setSchema(rs.getString(2));
				h2param.setTableName(rs.getString(3));
				h2param.setFields(new String[] {"*"});
				paramList.add(h2param);

			}


		} catch (SQLException e) {
			throw new ReadException(this.getName(),e);
		}

		return (H2StoreParameters[])paramList.toArray(new H2StoreParameters[0]);

	}

	public FeatureType[] getFeatureTypes(DataStoreParameters dsp) throws ReadException {
		return new FeatureType[] {H2Utils.getFeatureType(this.getConnection(), (H2StoreParameters)dsp)};
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
