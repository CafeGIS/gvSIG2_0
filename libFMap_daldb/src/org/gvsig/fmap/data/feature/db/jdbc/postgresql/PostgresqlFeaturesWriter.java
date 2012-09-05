package org.gvsig.fmap.data.feature.db.jdbc.postgresql;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

import org.gvsig.fmap.dal.DataServerExplorer;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.exception.WriteException;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.NewFeatureStoreParameters;
import org.gvsig.fmap.dal.feature.exception.InitializeWriterException;
import org.gvsig.fmap.data.feature.db.DBAttributeDescriptor;
import org.gvsig.fmap.data.feature.db.DBFeatureType;
import org.gvsig.fmap.data.feature.db.jdbc.JDBCFeature;
import org.gvsig.fmap.data.feature.db.jdbc.JDBCFeaturesWriter;
import org.gvsig.fmap.data.feature.db.jdbc.JDBCTypes;
import org.gvsig.fmap.geom.Geometry;

public class PostgresqlFeaturesWriter extends JDBCFeaturesWriter {
	DBFeatureType featureType;
	boolean bCreateTable=false;
	private String toEncode;
	PostgresqlStoreParameters parameters;

	private PreparedStatement insertSt;
	private PreparedStatement updateSt;

	PostgresqlFeaturesWriter(){
	}

	public void init(FeatureStore store) throws InitializeWriterException {
		super.init(store);
		PostgresqlStore pgStore = (PostgresqlStore)store;
		this.parameters=pgStore.getParametersPostgresql();

		this.featureType = (DBFeatureType)this.store.getDefaultFeatureType();

	}

	public void preProcess() throws WriteException, ReadException {
		super.preProcess();
		//??????????????????????????
		// ATENTION: We will transform (in PostGIS class; doubleQuote())
        // to UTF-8 strings. Then, we tell the PostgreSQL server
        // that we will use UTF-8, and it can translate
        // to its charset
        // Note: we have to translate to UTF-8 because
        // the server cannot manage UTF-16
		//??????????????????????????


		if (bCreateTable) {

			DataServerExplorer explorer = this.store.getExplorer();

			DataStoreParameters params =this.store.getParameters();
			explorer.remove(params);

			NewFeatureStoreParameters newParam = (NewFeatureStoreParameters)explorer.createNewDataStoreParameter();


			newParam.setFeatureType(this.featureType);
			explorer.add(newParam, false);
		}

//        ResultSet rsAux;
//		try {
////				conex.setAutoCommit(false);
////			alterTable();
//
////			rsAux = st.executeQuery("SHOW server_encoding;");
////	        rsAux.next();
////	        String serverEncoding = rsAux.getString(1);
////	        System.out.println("Server encoding = " + serverEncoding);
//	        // st.execute("SET CLIENT_ENCODING TO 'UNICODE';");
//	        // Intentamos convertir nuestras cadenas a ese encode.
////	        setEncoding(serverEncoding);
//		} catch (SQLException e) {
//			throw new InitializeWriterException("H2",e);
//		}

	}

	public void deleteFeature(Feature feature) throws WriteException {
		Statement st;
		String sqlDelete = getSqlDeleteFeature(featureType, feature);
		System.out.println("sql = " + sqlDelete);
		try {
			st = this.conex.createStatement();
			st.execute(sqlDelete);
		} catch (SQLException e) {
			throw new WriteException(this.store.getName(),e);
		}

	}

	public void insertFeature(Feature feature) throws WriteException {

		DBFeatureType ftype = (DBFeatureType)feature.getType();

		try {
			PreparedStatement ps=this.getInsertFeatureStatement(ftype);
			Iterator it = ftype.iterator();

			int index= 1;
			while (it.hasNext()){
				DBAttributeDescriptor fad=(DBAttributeDescriptor)it.next();
				if (fad.isReadOnly())
					continue;


				loadValueInPreparedStatement(ps, index, fad, feature);
				index++;
			}
//			ps.setObject(index, feature.get(ftype.getFieldIdIndex()));

			ps.execute();

		} catch (SQLException e) {
			throw new WriteException(this.store.getName(),e);
		}
	}

	private PreparedStatement getInsertFeatureStatement(DBFeatureType ftype) throws SQLException {
		if (this.insertSt == null){
			StringBuffer fields = new StringBuffer();
			StringBuffer values = new StringBuffer();
			StringBuffer sql = new StringBuffer();

			Iterator iter = ftype.iterator();
			while (iter.hasNext()){
				DBAttributeDescriptor fad=(DBAttributeDescriptor)iter.next();
				String name = fad.getName();
				if (fad.isReadOnly())
					continue;
				fields.append(name+",");
				values.append("?,");

			}
			sql.append("INSERT INTO "+ftype.getTableID()+" (");
			sql.append(fields.substring(0, fields.length()-1));
			sql.append(") VALUES (");
			sql.append(values.substring(0, values.length()-1));
			sql.append(")");

			this.insertSt= this.conex.prepareStatement(sql.toString());
			System.out.println(sql.toString());
		} else{
			this.insertSt.clearParameters();
		}
		return this.insertSt;

	}

	public void updateFeatureType(FeatureType featureType) {
		this.featureType=(DBFeatureType)featureType;
	}

	/**
	 * @param createTable
	 *            The bCreateTable to set.
	 */
	public void setCreateTable(boolean createTable) {
		bCreateTable = createTable;
	}
	boolean dropTableIfExist() throws SQLException{
		if (!this.existTable(parameters.getSchema(), parameters.getTableName())){
			return false;
		}
		Statement st = conex.createStatement();
		st.execute("DROP TABLE " + parameters.tableID() + ";");
		st.close();
		return false;
	}
	private boolean existTable(String schema, String tableName) throws SQLException{
		boolean exists =false;
		DatabaseMetaData metadata = conex.getMetaData();

		ResultSet rs = metadata.getTables(null, schema, tableName, null);

		exists = !rs.isAfterLast();
		rs.close();

		return exists;
	}

	public void updateFeature(Feature feature) throws WriteException, ReadException {

		DBFeatureType ftype = (DBFeatureType)feature.getType();

		try {
			PreparedStatement ps=this.getUpdateFeatureStatement(ftype);
			Iterator it = ftype.iterator();

			int index= 1;
			while (it.hasNext()){
				DBAttributeDescriptor fad=(DBAttributeDescriptor)it.next();
				if (fad.isPrimaryKey())
					continue;
				loadValueInPreparedStatement(ps, index, fad, feature);
				index++;
			}

			loadPkInPreparedStatement(ps, index, ftype, feature);

			ps.execute();

		} catch (SQLException e) {
			throw new WriteException(this.store.getName(),e);
		}
	}

	public void deleteAttribute(FeatureAttributeDescriptor attribute) throws WriteException {
		try {
			Statement st = conex.createStatement();
			String sql = "ALTER TABLE " + parameters.tableID() + " DROP COLUMN "
				+ attribute.getName() + ";";
			st.execute(sql);
		} catch (SQLException e) {
			throw new WriteException(this.store.getName(),e);
		}
	}

	public void updateAttribute(FeatureAttributeDescriptor oldAttribute, FeatureAttributeDescriptor attribute) throws WriteException, ReadException {
		try {
			Statement st = conex.createStatement();
			String sql = "ALTER TABLE " + parameters.tableID() + " RENAME COLUMN "
			+ oldAttribute.getName() + " TO " + attribute.getName() + ";";
			st.execute(sql);
		} catch (SQLException e) {
			throw new WriteException(this.store.getName(),e);
		}
	}

	public void insertAttribute(FeatureAttributeDescriptor attribute) throws WriteException {
		try {
			Statement st = conex.createStatement();

			String sql = "ALTER TABLE "
				+ parameters.tableID()
				+ " ADD COLUMN "
				+ attribute.getName()
				+ " "
				+ JDBCTypes.fieldTypeToString(attribute.getDataType())
				+ " "
				+ "DEFAULT " + attribute.getDefaultValue()
				+ ";";
			st.execute(sql);
		} catch (SQLException e) {
			throw new WriteException(this.store.getName(),e);
		}
	}

	/** @deprecated ???
	*
	*/
	public void setEncoding(String toEncode){
		if (toEncode.compareToIgnoreCase("SQL_ASCII") == 0){
		   this.toEncode = "ASCII";
		  } else {
			  this.toEncode = toEncode;
		  }
	}

	/** @deprecated ???
	*
	*/
	public String getEncoding() {
		return toEncode;
	}

	public boolean canWriteGeometry(int gvSIGgeometryType) {
		switch (gvSIGgeometryType) {
		case Geometry.TYPES.POINT:
			return true;
		case Geometry.TYPES.CURVE:
			return true;
		case Geometry.TYPES.SURFACE:
			return true;
		case Geometry.TYPES.ARC:
			return false;
		case Geometry.TYPES.ELLIPSE:
			return false;
		case Geometry.TYPES.MULTIPOINT:
			return true;
		case Geometry.TYPES.TEXT:
			return false;
		}
		return false;
	}


	/** @deprecated ???
	*
	*/
	protected String addQuotes(Object value) {
		String retString;

		if (value != null) {
			retString = "'" + doubleQuote(value) + "'";

		} else {
			retString = "null";
		}

		return retString;
	}

	/** @deprecated ???
	*
	*/
	private String doubleQuote(Object obj) {
		String aux = obj.toString().replaceAll("'", "''");
		StringBuffer strBuf = new StringBuffer(aux);
		ByteArrayOutputStream out = new ByteArrayOutputStream(strBuf.length());
		PrintStream printStream = new PrintStream(out);
		printStream.print(aux);
		String aux2 = "ERROR";
		try {
			aux2 = out.toString(toEncode);
			System.out.println(aux + " " + aux2);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return aux2;
	}

	private PreparedStatement getUpdateFeatureStatement(DBFeatureType dbFeatureType) throws SQLException{
		if (this.updateSt == null){
			StringBuffer sqlBuf = new StringBuffer("UPDATE "
					+ this.parameters.tableID() + " SET");
			String sql = null;

			Iterator iter = dbFeatureType.iterator();
			while (iter.hasNext()){
				FeatureAttributeDescriptor fad=(FeatureAttributeDescriptor)iter.next();
				String name = fad.getName();
				// El campo gid no lo actualizamos.
				if (fad.isPrimaryKey())
					continue;
				sqlBuf.append(" " + name + " = ? ,");

			}
			sqlBuf.deleteCharAt(sqlBuf.lastIndexOf(","));
			sqlBuf.append(" WHERE ");
			sqlBuf.append(getFliterForIDForPStatement(dbFeatureType));
			sql = sqlBuf.toString();


			this.updateSt= this.conex.prepareStatement(sql);
		} else{
			this.updateSt.clearParameters();
		}
		return this.updateSt;

	}

	public String getSqlDeleteFeature(DBFeatureType dbFeatureType, Feature feature) {
		StringBuffer sqlBuf = new StringBuffer("DELETE FROM "
				+ this.parameters.tableID() + " WHERE ");
		String sql = null;
		sqlBuf.append(((JDBCFeature)feature).getFilterForID());
		sql = sqlBuf.toString();

		return sql;
	}

	public static void create(PostgresqlStoreParameters parameters, FeatureType featureType)throws InitializeWriterException, InitializeException {
		Connection con = PostgresqlStoreUtils.getConnection(parameters.getUrl(), parameters.getUser(), parameters.getPassw());

		StringBuffer sb=new StringBuffer();
		sb.append("CREATE TABLE ");
		sb.append(parameters.tableID());
		sb.append(" (");
		sb.append("id int, ");
		Iterator iterator=featureType.iterator();
		while (iterator.hasNext()) {
			FeatureAttributeDescriptor descriptor = (FeatureAttributeDescriptor) iterator.next();
			String type = descriptor.getDataType();

			if (type.equals(FeatureAttributeDescriptor.BOOLEAN)){
				sb.append(descriptor.getName());
				sb.append(" bit, ");
			}else if (type.equals(FeatureAttributeDescriptor.BYTE)){
				sb.append(descriptor.getName());
				sb.append(" byte, ");
			}else if (type.equals(FeatureAttributeDescriptor.DATE)){
				sb.append(descriptor.getName());
				sb.append(" date, ");
			}else if (type.equals(FeatureAttributeDescriptor.DOUBLE)){
				sb.append(descriptor.getName());
				sb.append(" double, ");
			}else if (type.equals(FeatureAttributeDescriptor.FLOAT)){
				sb.append(descriptor.getName());
				sb.append(" float, ");
			}else if (type.equals(FeatureAttributeDescriptor.INT)){
				sb.append(descriptor.getName());
				sb.append(" int, ");
			}else if (type.equals(FeatureAttributeDescriptor.LONG)){
				sb.append(descriptor.getName());
				sb.append(" bigint, ");
			}else if (type.equals(FeatureAttributeDescriptor.STRING)){
				sb.append(descriptor.getName());
				sb.append(" varchar, ");
			}else if (type.equals(FeatureAttributeDescriptor.GEOMETRY)){
				sb.append(descriptor.getName());
				sb.append(" other, ");
			}else {
				System.out.print(" ---- " + "TYPE UNKNOWN");
			}
		}
		String createTable=sb.toString();
		createTable=createTable.substring(0, createTable.length()-2);
		createTable+=")";

		try{
			Statement st=con.createStatement();
			st.execute(createTable);
			st.close();
		}
		catch(SQLException except){
			throw new InitializeWriterException(parameters.getDataStoreName(),except);

		}
	}

	protected void loadPkInPreparedStatement(PreparedStatement ps,int paramIndex,DBFeatureType fType,Feature feature) throws java.sql.SQLException, WriteException{
		if (fType.getFieldsId().length != 1)
			throw new UnsupportedOperationException("ID fields > 1");
		String id =fType.getFieldsId()[0];
		loadValueInPreparedStatement(ps, paramIndex, (DBAttributeDescriptor)fType.get(id), feature);
	}
	protected static String getFliterForIDForPStatement(DBFeatureType fType) {
		if (fType.getFieldsId().length != 1)
			throw new UnsupportedOperationException("ID fields > 1");
		String id =fType.getFieldsId()[0];
		return id + " = ?";
	}
}