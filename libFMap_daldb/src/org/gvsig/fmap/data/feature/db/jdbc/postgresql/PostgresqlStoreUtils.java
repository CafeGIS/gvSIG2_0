package org.gvsig.fmap.data.feature.db.jdbc.postgresql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.exception.IsNotAttributeSettingException;
import org.gvsig.fmap.dal.feature.impl.DefaultFeatureType;
import org.gvsig.fmap.data.feature.db.DBAttributeDescriptor;
import org.gvsig.fmap.data.feature.db.DBFeatureType;
import org.gvsig.fmap.data.feature.db.DBStoreParameters;
import org.gvsig.fmap.data.feature.db.jdbc.JDBCDriverNotFoundException;
import org.gvsig.fmap.data.feature.db.jdbc.SQLException;
import org.gvsig.fmap.geom.Geometry;

public class PostgresqlStoreUtils {

	static String getJDBCUrl(String host, String db, String port) {
		String url;
		url = "jdbc:postgresql://"+host+":" + port +"/"+db;

		return url;
	}

	private static void addConditionForSerialField(DBAttributeDescriptor attr,StringBuffer sqlSeq){
		sqlSeq.append(" (");
		sqlSeq.append(" column_name = '" + attr.getName() +"'");
		sqlSeq.append("  and table_name = '" + attr.getTableName()+ "'");
		if (attr.getSchemaName() != null && attr.getSchemaName().length() > 0){
			sqlSeq.append("  and table_schema = '" + attr.getSchemaName() +"'");
		}

		sqlSeq.append("  and table_catalog = '" + attr.getCatalogName()+ "'");
		sqlSeq.append(")");

	}

	private static void initializeSerialFields(Connection connection,DBFeatureType featureType) throws java.sql.SQLException, DataException{
		DBAttributeDescriptor attr;

		ArrayList serialCandidates= new ArrayList();
		Iterator iter = featureType.iterator();
		while(iter.hasNext()){
			attr = (DBAttributeDescriptor)iter.next();
			if (attr.getSqlTypeName().equals("int4") &&
					attr.getTableName() != null	&&
					attr.getTableName().length() > 0){
				serialCandidates.add(attr);
			}
		}
		if (serialCandidates.size() == 0){
			return;
		}
		Statement st = connection.createStatement();
		StringBuffer sqlSeq= new StringBuffer("select table_catalog,table_schema,table_name,column_name from information_schema.columns where column_default like 'nextval(%'  and ( ");
		iter = serialCandidates.iterator();
		String sql;
		int i;
		for (i=0;i<serialCandidates.size()-1;i++){
			attr = (DBAttributeDescriptor)serialCandidates.get(i);
			addConditionForSerialField(attr,sqlSeq);
			sqlSeq.append(" or ");
		}
		attr = (DBAttributeDescriptor)serialCandidates.get(i);
		addConditionForSerialField(attr,sqlSeq);


		sqlSeq.append(")");
		sql=sqlSeq.toString();
		ResultSet rs = st.executeQuery(sql);
		while (rs.next()){
			iter = serialCandidates.iterator();
			while (iter.hasNext()){
				attr = (DBAttributeDescriptor)iter.next();
				if (rs.getString("column_name").equals(attr.getName())){
					attr.setAutoIncrement(true);
					serialCandidates.remove(attr);
					break;
				}
			}

		}


	}


	static DBFeatureType getFeatureType(Connection connection, PostgresqlStoreParameters params) throws ReadException{
		DBFeatureType featureType = new DBFeatureType();
		String[] ids =params.getFieldsId();
		int i;
		DBAttributeDescriptor attr;


		loadFieldsToFeatureType(connection, params, featureType);



		try {
			featureType.setFieldsId(ids);
		} catch (DataException e) {
			throw new ReadException(PostgresqlStore.DATASTORE_NAME,e);
		}

		//Inicializamos los 'serial' ya que en postgres el
		//'isAutonumeric' devuelve false
//		try{
//			initializeSerialFields(connection,featureType);
//		} catch (java.sql.SQLException e) {
//			throw new InitializeException(PostgresqlStore.DATASTORE_NAME,e);
//
//		}
//


		//Inicializar campos geometricos si los hubiese
		//TODO Datos geometricos
//		featureType.setDefaultGeometry(params.getGeometryField());
		getTableEPSG_and_shapeType(connection,params,featureType);

		//Inicializar la geometria por defecto
		if (params.getDefaultGeometryField() != null && params.getDefaultGeometryField() != ""){
			if (featureType.getIndex(params.getDefaultGeometryField())< 0){
				throw new InitializeException(
						PostgresqlStore.DATASTORE_NAME,
						new Exception("Geometry Field '"+ params.getDefaultGeometryField() +"' not Found"));

			}
			attr = (DBAttributeDescriptor)featureType.get(params.getDefaultGeometryField());
			if (attr.getDataType() != FeatureAttributeDescriptor.GEOMETRY){
				throw new InitializeException(
						PostgresqlStore.DATASTORE_NAME,
						new Exception("Field '"+ params.getDefaultGeometryField() +"' isn't a geometry"));

			}


		}
		featureType.setDefaultGeometry(params.getDefaultGeometryField());
//		featureType.setGeometryTypes(new int[]{Geometry.TYPES.GEOMETRY});

		return featureType;

	}

	private static void getTableEPSG_and_shapeType(Connection conn,
			DBStoreParameters params, FeatureType featureType) throws ReadException {
		try {
			if (params.getDefaultGeometryField() == null
					|| params.getTableName() == null
					|| params.getTableName().length() == 0) {
				return;
			}
			Statement stAux = conn.createStatement();

//			String sql = "SELECT * FROM GEOMETRY_COLUMNS WHERE F_TABLE_NAME = '"
//					+ getTableName() + "' AND F_GEOMETRY_COLUMN = '" + getLyrDef().getFieldGeometry() + "'";
			String sql;
			if (params.getSchema() == null || params.getSchema().equals("")){
				sql = "SELECT * FROM GEOMETRY_COLUMNS WHERE F_TABLE_SCHEMA = current_schema() AND F_TABLE_NAME = '"
					+ params.getTableName() + "' AND F_GEOMETRY_COLUMN = '" + params.getDefaultGeometryField() + "'";
			}else{
				sql = "SELECT * FROM GEOMETRY_COLUMNS WHERE F_TABLE_SCHEMA = '"+ params.getSchema() + "' AND F_TABLE_NAME = '"
						+ params.getTableName() + "' AND F_GEOMETRY_COLUMN = '" + params.getDefaultGeometryField() + "'";
			}

			ResultSet rs = stAux.executeQuery(sql);
			rs.next();
			String originalEPSG = "" + rs.getInt("SRID");
			String geometryType = rs.getString("TYPE");
			int shapeType = Geometry.TYPES.GEOMETRY;
			if (geometryType.compareToIgnoreCase("POINT") == 0) {
				shapeType = Geometry.TYPES.POINT;
			} else if (geometryType.compareToIgnoreCase("LINESTRING") == 0) {
				shapeType = Geometry.TYPES.CURVE;
			} else if (geometryType.compareToIgnoreCase("POLYGON") == 0) {
				shapeType = Geometry.TYPES.SURFACE;
			} else if (geometryType.compareToIgnoreCase("MULTIPOINT") == 0) {
				shapeType = Geometry.TYPES.POINT;
			} else if (geometryType.compareToIgnoreCase("MULTILINESTRING") == 0) {
				shapeType = Geometry.TYPES.CURVE;
			} else if (geometryType.compareToIgnoreCase("MULTIPOLYGON") == 0) {
				shapeType = Geometry.TYPES.SURFACE;
			}

			featureType.setGeometryTypes(new int[]{shapeType});
			featureType.setDefaultSRS(originalEPSG);
//			params.setShapeType(shapeType);
			rs.close();
		} catch (java.sql.SQLException e) {
			throw new ReadException(PostgresqlStore.DATASTORE_NAME, e);
		}

	}



	private static void loadFieldsToFeatureType(Connection conn,PostgresqlStoreParameters params,DBFeatureType featureType) throws ReadException{
		String sql="";
		String columns=params.getFieldsString();
		boolean fillTableData;

		if (params.getSqlSoure() != null){
			sql = params.getSqlSoure();
			fillTableData = false;
		} else {
			sql = "Select "+columns+" from " +params.tableID();
			fillTableData = true;
		}

		try {

			Statement stAux = conn.createStatement();
			stAux.setFetchSize(1);
			ResultSet rs = stAux.executeQuery(sql);
			ResultSetMetaData rsMetadata = rs.getMetaData();

			int i;

			featureType.setTableID(params.tableID());
			DBAttributeDescriptor attr;
			for (i=1;i<=rsMetadata.getColumnCount();i++){
				attr = getAttributeFromJDBC(featureType,conn,rsMetadata,i);
				featureType.add(attr);
//				attr.setOrdinal(i-1);
				attr.loading();
				attr.setCatalogName(params.getDb());
				if (fillTableData){
					attr.setSchemaName(params.getSchema());
					attr.setTableName(params.getTableName());

				}
				attr.stopLoading();
			}
			rs.close();
			stAux.close();



		} catch (java.sql.SQLException e) {
			throw new SQLException(sql,"getFeatureType",e);
		} catch (IsNotAttributeSettingException e) {
			e.printStackTrace();
		}

	}

	private static DBAttributeDescriptor getAttributeFromJDBC(DefaultFeatureType fType,Connection conn,ResultSetMetaData rsMetadata,int colIndex) throws SQLException{
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
			column.setSize(rsMetadata.getColumnDisplaySize(colIndex));


			switch (rsMetadata.getColumnType(colIndex)) {
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
			case java.sql.Types.OTHER:
				if (column.getSqlTypeName().equalsIgnoreCase("geometry")){
					column.setType(FeatureAttributeDescriptor.GEOMETRY);
					break;
				}
				//No hacemos break para que se quede en default

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




	static Connection getConnection(String dbUrl, String dbUser, String dbPass) throws InitializeException {
		//TODO: Aquí habria que implementar la llamada
		//      al Resource Manager para comprobar si ya hay
		//		una connexion a la BD
		String connID = getConnectionResourceID(dbUrl, dbUser);

		Connection conn = null;
//		IResource res = ResourceManager.getResourceManager().getResource(connID);



		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			throw new JDBCDriverNotFoundException("org.postgresql.Driver",e);
		}
		try {
			conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
			conn.setAutoCommit(false);

		} catch (java.sql.SQLException e1) {
			throw new InitializeException(PostgresqlStore.DATASTORE_NAME,e1);
		}
		//TODO: Registrar en el Resource manager
		// ResourceManager.getResourceManager().addResource(res);

		return conn;
	}

	static String getConnectionResourceID(String dbUrl,String dbUser){
		return PostgresqlStore.CONNECTION_STRING+";"+dbUrl+";"+dbUser;

	}




	static String addLimitsToSQL(String aSql,int fetchSize,int page){
		return aSql+ " limit " + fetchSize + " offset " + (fetchSize*page);
	}

	static String getDefaultSchema(Connection conn, String catalog) throws InitializeException {
		String sql = "Select current_schema()";
		ResultSet rs = null;
		Statement st = null;
		String schema = null;
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			rs.next();
			schema = rs.getString(1);
		} catch (java.sql.SQLException e) {
			throw new InitializeException(PostgresqlStore.DATASTORE_NAME, e);
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

		return schema;
	}

}

