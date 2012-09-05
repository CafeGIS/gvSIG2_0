package org.gvsig.fmap.data.feature.db.jdbc.postgresqlbin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.AttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.exception.IsNotAttributeSettingException;
import org.gvsig.fmap.dal.feature.impl.DefaultFeatureType;
import org.gvsig.fmap.data.feature.db.DBAttributeDescriptor;
import org.gvsig.fmap.data.feature.db.DBFeatureType;
import org.gvsig.fmap.data.feature.db.jdbc.JDBCDriverNotFoundException;
import org.gvsig.fmap.data.feature.db.jdbc.SQLException;
import org.gvsig.fmap.data.feature.db.jdbc.postgresql.PostgresqlStore;
import org.gvsig.fmap.data.feature.db.jdbc.postgresql.PostgresqlStoreParameters;

public class PostgresqlBinStoreUtils {

	private static int cursorCount=0;
	private static String baseCursorName=null;

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

	static String createCursorName() {
		if (baseCursorName == null){
			baseCursorName = "gv_"+ System.currentTimeMillis();
		}
		cursorCount++;
		return baseCursorName+"_"+cursorCount;
	}



	static DBFeatureType getFeatureType(Connection connection, PostgresqlStoreParameters params) throws ReadException{
		DBFeatureType featureType = new DBFeatureType();


		loadFieldsToFeatureType(connection, params, featureType);


		try {
			featureType.setFieldsId(params.getFieldsId());
		} catch (DataException e) {
			throw new ReadException(PostgresqlBinStore.DATASTORE_NAME,e);
		}

		if (params.getDefaultGeometryField() != null && params.getDefaultGeometryField() != ""){
			if (featureType.getIndex(params.getDefaultGeometryField())< 0){
				// FIXME: crear una nueva excepcion??
				throw new InitializeException(
						PostgresqlStore.DATASTORE_NAME,
						new Exception("Geometry Field '"+ params.getDefaultGeometryField() +"' not Found"));

			}

			featureType.setDefaultGeometry(params.getDefaultGeometryField());
		}

		return featureType;

	}

	private static void loadFieldsToFeatureType(Connection conn,PostgresqlStoreParameters params,DBFeatureType featureType) throws ReadException{
		String sql="";
		String columns=params.getFieldsString();

		if (params.getSqlSoure() != null){
			sql = params.getSqlSoure();
		} else {
			sql = "Select "+columns+" from " + params.tableID() + " limit 1;";
		}

		try {

			Statement stAux = conn.createStatement();
			ResultSet rs = stAux.executeQuery(sql);
			ResultSetMetaData rsMetadata = rs.getMetaData();

			int i;

			featureType.setTableID(params.tableID());
			AttributeDescriptor attr;
			for (i=1;i<=rsMetadata.getColumnCount();i++){
				attr = getAttributeFromJDBC(featureType,conn,rsMetadata,i);
				featureType.add(attr, false);
//				attr.setOrdinal(i-1);
			}
			rs.close();
		} catch (java.sql.SQLException e) {
			// TODO Auto-generated catch block
			throw new SQLException(sql,"getFeatureType",e);
		}

	}

	private static AttributeDescriptor getAttributeFromJDBC(DefaultFeatureType fType, Connection conn,ResultSetMetaData rsMetadata,int colIndex) throws SQLException{
		DBAttributeDescriptor column= (DBAttributeDescriptor) fType.createAttributeDescriptor();
		try {
			column.setName(rsMetadata.getColumnName(colIndex));
			column.setSqlType(rsMetadata.getColumnType(colIndex));
			column.setAllowNull(rsMetadata.isNullable(colIndex) == ResultSetMetaData.columnNullable);
			column.setAutoIncrement(rsMetadata.isAutoIncrement(colIndex));
			column.setReadOnly(rsMetadata.isReadOnly(colIndex));

			switch (rsMetadata.getColumnType(colIndex)) {
			case java.sql.Types.INTEGER:
				column.setType(FeatureAttributeDescriptor.INT);
				break;
			case java.sql.Types.BIGINT:
				column.setType(FeatureAttributeDescriptor.LONG);
				break;
			case java.sql.Types.REAL:
				column.setType(FeatureAttributeDescriptor.DOUBLE);
				column.setPrecision(rsMetadata.getPrecision(colIndex));
				break;
			case java.sql.Types.DOUBLE:
				column.setType(FeatureAttributeDescriptor.DOUBLE);
				column.setPrecision(rsMetadata.getPrecision(colIndex));
				break;
			case java.sql.Types.CHAR:
				column.setType(FeatureAttributeDescriptor.STRING);
				column.setSize(rsMetadata.getColumnDisplaySize(colIndex));
				break;
			case java.sql.Types.VARCHAR:
				column.setType(FeatureAttributeDescriptor.STRING);
				column.setSize(rsMetadata.getColumnDisplaySize(colIndex));
				break;
			case java.sql.Types.FLOAT:
				column.setType(FeatureAttributeDescriptor.FLOAT);
				column.setSize(rsMetadata.getColumnDisplaySize(colIndex));
				column.setPrecision(rsMetadata.getPrecision(colIndex));
				break;
			case java.sql.Types.DECIMAL:
				column.setType(FeatureAttributeDescriptor.FLOAT);
				column.setSize(rsMetadata.getColumnDisplaySize(colIndex));
				column.setPrecision(rsMetadata.getPrecision(colIndex));
				break;
			case java.sql.Types.DATE:
				column.setType(FeatureAttributeDescriptor.DATE);
				break;
			case java.sql.Types.BOOLEAN:
				column.setType(FeatureAttributeDescriptor.BOOLEAN);
				break;
			default:
				column.setType(FeatureAttributeDescriptor.OBJECT);
				break;
			}
		} catch (java.sql.SQLException e){
			throw new SQLException("","load attribute definition",e);
		} catch (IsNotAttributeSettingException e) {
			e.printStackTrace();
		}

		return column;

	}

	static String getJDBCUrl(String host, String db, String port) {
		String url;
		url = "jdbc:postgresql://"+host+":" + port +"/"+db;

		return url;
	}

}

