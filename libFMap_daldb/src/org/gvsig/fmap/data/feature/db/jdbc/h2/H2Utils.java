package org.gvsig.fmap.data.feature.db.jdbc.h2;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.AttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.exception.IsNotAttributeSettingException;
import org.gvsig.fmap.dal.feature.impl.DefaultFeatureType;
import org.gvsig.fmap.data.feature.db.DBAttributeDescriptor;
import org.gvsig.fmap.data.feature.db.DBFeatureType;
import org.gvsig.fmap.data.feature.db.jdbc.SQLException;
import org.gvsig.fmap.geom.Geometry;


public class H2Utils {



	static String getDefaultSchema(Connection conn, String catalog) throws InitializeException {
		String sql= "SELECT * FROM INFORMATION_SCHEMA.SCHEMATA WHERE IS_DEFAULT = TRUE";
		if (catalog != null && catalog !=""){
			sql= sql+ " AND CATALOG_NAME = '"+catalog+"'";
		}

		String schema = null;
		Statement st = null;
		ResultSet rs= null;
		try{
//			if (conn.isClosed()){
//				return "";
//			}
			st = conn.createStatement(ResultSet.FETCH_FORWARD, ResultSet.CONCUR_READ_ONLY);
			rs = st.executeQuery(sql);
			if (!rs.next()){
				throw new InitializeException("Can't find default schema.","getDefaulSchema");
			}
			schema = rs.getString("SCHEMA_NAME");
			if (rs.next()){
				throw new InitializeException("Checks catalog parm.","getDefaulSchema");
			}


		} catch (java.sql.SQLException e) {
			// TODO Auto-generated catch block
			throw new SQLException(sql,"getDefaultSchema",e);
		} finally{
			try{
				if (rs != null){
					rs.close();
				} else if (st != null){
					st.close();
				}
			} catch (java.sql.SQLException e1){
				//Ignore ??
			}

		}
		return schema;
	}

	private static void loadFieldsToFeatureType(Connection conn,H2StoreParameters params,DBFeatureType featureType) throws ReadException{
		String sql="";
		String columns=params.getFieldsString();

		if (params.getSqlSoure() != null && !params.getSqlSoure().equals("")){
			sql = params.getSqlSoure();
		} else {
			sql = "Select "+columns+" from " + params.tableID() + " limit 1;";
		}

		try {

			Statement stAux = conn.createStatement();
			Statement stAux1 = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = stAux.executeQuery(sql);
			ResultSetMetaData rsMetadata = rs.getMetaData();
			String schemaFilter="";
			if (params.getSchema() != null && params.getSchema() != ""){
				schemaFilter = " TABLE_SCHEMA='" + params.getSchema() +"' AND ";
			}
			String sqlAllMeta = "SELECT * FROM INFORMATION_SCHEMA.COLUMNS where "
				+ schemaFilter
				+ "TABLE_NAME='"+ params.getTableName() +"'";

			ResultSet rsAllMeta = stAux1.executeQuery(sqlAllMeta);

//			ResultSet rsAllMeta = conn.getMetaData().getAttributes(null, null, tableName, "*");

			String colName;
			int i;

			featureType.setTableID(params.tableID());
			AttributeDescriptor attr;
			for (i=1;i<=rsMetadata.getColumnCount();i++){
				colName= rsMetadata.getColumnName(i);
				rsAllMeta.first();

				while (true){
					if  (rsAllMeta.isAfterLast()){
						attr = getAttributeFromJDBC(featureType,conn,rsMetadata,i);
						featureType.add(attr, false);
//						attr.setOrdinal(i-1);
						break;
					} else if(rsAllMeta.getString("COLUMN_NAME").equals(colName)){
						attr = getAttributeFromJDBC(featureType,conn,rsAllMeta);
						featureType.add(attr, false);
//						attr.setOrdinal(i-1);
						break;
					}
					rsAllMeta.next();
				}
				if (attr.getName().equals(params.getDefaultGeometryField())){
					if (attr.getDataType().equals(FeatureAttributeDescriptor.OBJECT)){
						attr.loading();
						attr.setType(FeatureAttributeDescriptor.GEOMETRY);
						attr.stopLoading();
					}else{
						throw new InitializeException(
							"H2Utils.getFeatureType",
							new Exception("Geometry Field '"
								+ params.getDefaultGeometryField()
								+ "' is a "
								+ attr.getDataType()
								+ " but sould be "
								+ FeatureAttributeDescriptor.OBJECT));
					}

				}
			}

			rs.close();
			rsAllMeta.close();
		} catch (java.sql.SQLException e) {
			// TODO Auto-generated catch block
			throw new SQLException(sql,"getFeatureType",e);
		} catch (IsNotAttributeSettingException e) {
			e.printStackTrace();
		}

	}

	static DBFeatureType getFeatureType(Connection conn,H2StoreParameters params) throws ReadException{
		DBFeatureType featureType = new DBFeatureType();


		loadFieldsToFeatureType(conn, params, featureType);

		try {
			if (params.getFieldsId() == null || params.getFieldsId().length == 0){
				String[] ids =loadFieldsId(conn,params);
				if (ids == null){
					featureType.setReadOnly(true);
					ids = new String[featureType.size()];
					Iterator iter = featureType.iterator();
					for (int i=0;i<featureType.size();i++){
						ids[i]=((AttributeDescriptor)iter.next()).getName();
					}
				}
				featureType.setFieldsId(ids);
			} else{
				featureType.setFieldsId(params.getFieldsId());
			}
		} catch (DataException e) {
			throw new ReadException(H2Store.DATASTORE_NAME,e);
		}

		if (params.getDefaultGeometryField() != null && params.getDefaultGeometryField() != ""){
//			if (featureType.getFieldIndex(params.getGeometryField())< 0){
//				// FIXME: crear una nueva excepcion??
//				throw new InitializeException(
//						H2Store.DATASTORE_NAME,
//						new Exception("Geometry Field '"+ params.getGeometryField() +"' not Found"));
//
//			}

			featureType.setDefaultGeometry(params.getDefaultGeometryField());
			featureType.setGeometryTypes(new int[]{Geometry.TYPES.GEOMETRY});
		}

		return featureType;

	}

	private static String[] loadFieldsId(Connection conn, H2StoreParameters params) throws ReadException {
		Statement st;
		StringBuffer sql = new StringBuffer();
		ResultSet rs;
		ArrayList list = new ArrayList();
		/*SELECT column_name FROM INFORMATION_SCHEMA.INDEXES
		 *   WHERE table_name='' AND
		 *         table_schema='' AND
		 *         table_catalog=''
		 *         AND index_type_name='PRIMARY KEY'
		 *
		 */
		sql.append("SELECT column_name FROM INFORMATION_SCHEMA.INDEXES WHERE table_name like '");
		sql.append(params.getTableName());
		sql.append("' AND table_schema like '");

		if (params.getSchema() == null || params.getSchema() == ""){
			sql.append(getDefaultSchema(conn, params.getDb()));
		} else{
			sql.append(params.getSchema());
		}


		if (params.getCatalog() == null || params.getCatalog() == ""){
			if (params.getDb() != null && params.getDb() != ""){
				sql.append("' AND table_catalog like '");
				sql.append(params.getDb());
			}
		} else {
			sql.append("' AND table_catalog like '");
			sql.append(params.getCatalog());
		}



		sql.append("' AND index_type_name='PRIMARY KEY'");

//		System.out.println(sql.toString());
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql.toString());
			while (rs.next()){
				list.add(rs.getString(1));
			}
			rs.close();
			st.close();

		} catch (java.sql.SQLException e) {
			throw new ReadException(params.getDataStoreName(),e);
		}
		if (list.size() == 0){
			return null;
		}

		String[] x = new String[] {""};
		return (String[])list.toArray(x);

	}

	private static AttributeDescriptor getAttributeFromJDBC(DefaultFeatureType fType,Connection conn,ResultSetMetaData rsMetadata,int colIndex) throws SQLException{
		DBAttributeDescriptor column= (DBAttributeDescriptor) fType.createAttributeDescriptor();
		try {
			column.loading();
			column.setName(rsMetadata.getColumnName(colIndex));
			column.setCaseSensitive(rsMetadata.isCaseSensitive(colIndex));
			column.setSqlType(rsMetadata.getColumnType(colIndex));
			column.setAllowNull(rsMetadata.isNullable(colIndex) == ResultSetMetaData.columnNullable);
			column.setAutoIncrement(rsMetadata.isAutoIncrement(colIndex));
			column.setReadOnly(rsMetadata.isReadOnly(colIndex));
			column.setWritable(rsMetadata.isWritable(colIndex));
			column.setClassName(rsMetadata.getColumnClassName(colIndex));
			column.setCatalogName(rsMetadata.getCatalogName(colIndex));
			column.setDefinitelyWritable(rsMetadata.isDefinitelyWritable(colIndex));
			column.setLabel(rsMetadata.getColumnLabel(colIndex));
			column.setSchemaName(rsMetadata.getSchemaName(colIndex));
			column.setTableName(rsMetadata.getTableName(colIndex));
			column.setCatalogName(rsMetadata.getCatalogName(colIndex));
			column.setSqlTypeName(rsMetadata.getColumnTypeName(colIndex));
			column.setSearchable(rsMetadata.isSearchable(colIndex));
			column.setSigned(rsMetadata.isSigned(colIndex));
			column.setCurrency(rsMetadata.isCurrency(colIndex));
			column.setPrecision(rsMetadata.getPrecision(colIndex));
			column.setSize(rsMetadata.getColumnDisplaySize(colIndex));			switch (rsMetadata.getColumnType(colIndex)) {
			case java.sql.Types.INTEGER:
				column.setType(FeatureAttributeDescriptor.INT);
				break;
			case java.sql.Types.BIGINT:
				column.setType(FeatureAttributeDescriptor.LONG);
				break;
			case java.sql.Types.REAL:
				column.setType(FeatureAttributeDescriptor.DOUBLE);
				break;
			case java.sql.Types.DOUBLE:
				column.setType(FeatureAttributeDescriptor.DOUBLE);
				break;
			case java.sql.Types.CHAR:
				column.setType(FeatureAttributeDescriptor.STRING);
				break;
			case java.sql.Types.VARCHAR:
				column.setType(FeatureAttributeDescriptor.STRING);
				break;
			case java.sql.Types.FLOAT:
				column.setType(FeatureAttributeDescriptor.FLOAT);
				break;
			case java.sql.Types.DECIMAL:
				column.setType(FeatureAttributeDescriptor.FLOAT);
				break;
			case java.sql.Types.DATE:
				column.setType(FeatureAttributeDescriptor.DATE);
				break;
			case java.sql.Types.TIME:
				column.setType(FeatureAttributeDescriptor.TIME);
				break;
			case java.sql.Types.TIMESTAMP:
				column.setType(FeatureAttributeDescriptor.TIMESTAMP);
				break;
			case java.sql.Types.BOOLEAN:
				column.setType(FeatureAttributeDescriptor.BOOLEAN);
				break;
			default:
				column.setType(FeatureAttributeDescriptor.OBJECT);
				break;
			}
			column.stopLoading();
		} catch (java.sql.SQLException e){
			throw new SQLException("","load attribute definition",e);
		} catch (IsNotAttributeSettingException e) {
			e.printStackTrace();
		}

		return column;

	}

	private static AttributeDescriptor getAttributeFromJDBC(DefaultFeatureType fType, Connection conn, ResultSet rsMetadata) throws SQLException{
		DBAttributeDescriptor column= (DBAttributeDescriptor) fType.createAttributeDescriptor();
		try {
			column.loading();
			column.setName(rsMetadata.getString("COLUMN_NAME"));
			column.setSqlType(rsMetadata.getInt("DATA_TYPE"));
			switch (rsMetadata.getInt("DATA_TYPE")) {
			case java.sql.Types.INTEGER:
				column.setType(FeatureAttributeDescriptor.INT);
				break;
			case java.sql.Types.BIGINT:
				column.setType(FeatureAttributeDescriptor.LONG);
				break;
			case java.sql.Types.REAL:
				column.setType(FeatureAttributeDescriptor.DOUBLE);
//				column.setPrecision(rsMetadata.getInt("DECIMAL_DIGITS"));
				column.setPrecision(rsMetadata.getInt("NUMERIC_PRECISION"));
				break;
			case java.sql.Types.DOUBLE:
				column.setType(FeatureAttributeDescriptor.DOUBLE);
//				column.setPrecision(rsMetadata.getInt("DECIMAL_DIGITS"));
				column.setPrecision(rsMetadata.getInt("NUMERIC_PRECISION"));
				break;
			case java.sql.Types.CHAR:
				column.setType(FeatureAttributeDescriptor.STRING);
//				column.setSize(rsMetadata.getInt("COLUMN_SIZE"));
				column.setSize(rsMetadata.getInt("CHARACTER_MAXIMUM_LENGTH"));
				break;
			case java.sql.Types.VARCHAR:
				column.setType(FeatureAttributeDescriptor.STRING);
//				column.setSize(rsMetadata.getInt("COLUMN_SIZE"));
				column.setSize(rsMetadata.getInt("CHARACTER_MAXIMUM_LENGTH"));

				break;
			case java.sql.Types.FLOAT:
				column.setType(FeatureAttributeDescriptor.FLOAT);
//				column.setSize(rsMetadata.getInt("COLUMN_SIZE"));
				column.setSize(rsMetadata.getInt("CHARACTER_MAXIMUM_LENGTH"));
				break;
			case java.sql.Types.DECIMAL:
				column.setType(FeatureAttributeDescriptor.FLOAT);
//				column.setSize(rsMetadata.getInt("COLUMN_SIZE"));
				column.setSize(rsMetadata.getInt("CHARACTER_MAXIMUM_LENGTH"));
//				column.setPrecision(rsMetadata.getInt("DECIMAL_DIGITS"));
				column.setPrecision(rsMetadata.getInt("NUMERIC_PRECISION"));
				break;
			case java.sql.Types.DATE:
				column.setType(FeatureAttributeDescriptor.DATE);
				break;
			case java.sql.Types.BOOLEAN:
				column.setType(FeatureAttributeDescriptor.BOOLEAN);
				break;
			default:
				//FIXME: Falta comprobar si es geometrica!!!
				column.setType(FeatureAttributeDescriptor.OBJECT);
				break;
			}

			column.setDefaultValue(rsMetadata.getObject("COLUMN_DEFAULT"));
			column.setAllowNull(rsMetadata.getBoolean("IS_NULLABLE"));
			column.stopLoading();
		} catch (java.sql.SQLException e){
			throw new SQLException("","load attribute definition",e);
		} catch (IsNotAttributeSettingException e) {
			e.printStackTrace();
		}

		return column;

	}



//	static void initializeTableEPSG_and_shapeType(Connection conn,String tableID, DBFeatureType dbld) throws ReadException {
//		try {
//			Statement stAux = conn.createStatement();
//
////			String sql = "SELECT * FROM GEOMETRY_COLUMNS WHERE F_TABLE_NAME = '"
////					+ getTableName() + "' AND F_GEOMETRY_COLUMN = '" + getLyrDef().getFieldGeometry() + "'";
//			String sql= "SELECT SRID("+dbld.getDefaultGeometry()+"), GeometryType("+dbld.getDefaultGeometry()+") FROM "+tableID +" WHERE "+dbld.getDefaultGeometry()+" is not null LIMIT 1";
//
//			ResultSet rs = stAux.executeQuery(sql);
//			if(!rs.next()){
//				dbld.setDefaultSRS("");
//				dbld.setGeometryTypes(new int[]{FShape.MULTI});
//				return;
//			}
//			dbld.setDefaultSRS("EPSG:"+rs.getInt(1));
//
//			String geometryType = rs.getString(2);
//			int shapeType = FShape.MULTI;
//			if (geometryType.compareToIgnoreCase("Point") == 0)
//				shapeType = FShape.POINT;
//			else if (geometryType.compareToIgnoreCase("LineString") == 0)
//				shapeType = FShape.LINE;
//			else if (geometryType.compareToIgnoreCase("Polygon") == 0)
//				shapeType = FShape.POLYGON;
//			else if (geometryType.compareToIgnoreCase("MultiPoint") == 0)
//				shapeType = FShape.POINT;
//			else if (geometryType.compareToIgnoreCase("MultiLineString") == 0)
//				shapeType = FShape.LINE;
//			else if (geometryType.compareToIgnoreCase("MultiPolygon") == 0)
//				shapeType = FShape.POLYGON;
//
//			dbld.setGeometryTypes(new int[]{shapeType});
//			rs.close();
//
//		} catch (java.sql.SQLException e) {
//			dbld.setDefaultSRS("");
//			dbld.setGeometryTypes(new int[]{FShape.MULTI});
//			throw new ReadException("H2Utils.getTableEPSG_and_shapeType",e);
//		}
//
//	}

	static String getConnectionResourceID(String dbUrl,String dbUser){
		return H2Store.CONNECTION_STRING+";"+dbUrl+";"+dbUser;

	}


	static String getJDBCUrl(String host,String db){
		String url;
		url = "jdbc:h2:tcp://"+host;
		if (db == null || db == ""){
			url=url+"/default";
		}else {
			url=url+"/"+db;
		}

		return url;
	}

	static String addLimitsToSQL(String aSql, int fetchSize, int page) {
		return aSql + " limit " + fetchSize + " offset " + (fetchSize * page);
	}

}
