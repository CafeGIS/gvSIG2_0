package org.gvsig.fmap.data.feature.db.jdbc.h2;

import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

import org.gvsig.fmap.dal.DataStoreParameters;
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

class H2FeaturesWriter extends JDBCFeaturesWriter{
	DBFeatureType featureType;
	boolean bCreateTable=false;
	private String toEncode;
	H2StoreParameters parameters;

	private PreparedStatement insertSt;
	private PreparedStatement updateSt;

//	private static WKBWriter wkbWriter = new WKBWriter();
	private static int toWKBOperationIndex;

	H2FeaturesWriter(){
	}

	public void init(FeatureStore store) throws InitializeWriterException {
		super.init(store);
		H2Store h2Store = (H2Store)store;
		this.parameters=h2Store.getParametersH2();
		this.featureType = (DBFeatureType)this.store.getDefaultFeatureType();

	}

	public void preProcess() throws WriteException, ReadException {
		super.preProcess();

		if (bCreateTable) {
			H2Explorer explorer = (H2Explorer)this.store.getExplorer();

			DataStoreParameters params =this.store.getParameters();
			explorer.remove(params,conex);

			NewFeatureStoreParameters newParam = (NewFeatureStoreParameters)explorer.createNewDataStoreParameter();


			newParam.setFeatureType(this.featureType);
			explorer.add(newParam,conex);
		}
	}

	public void deleteFeature(Feature feature) throws WriteException {
		Statement st;
		String sqlDelete = getSqlDeleteFeature(featureType, (JDBCFeature)feature);
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
			DBAttributeDescriptor fad;
			while (iter.hasNext()){
				fad=(DBAttributeDescriptor)iter.next();
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

	public String getSqlDeleteFeature(DBFeatureType dbFeatureType, JDBCFeature feature) {
		StringBuffer sqlBuf = new StringBuffer("DELETE FROM "
				+ this.parameters.tableID() + " WHERE ");
		String sql = null;
		sqlBuf.append(((JDBCFeature)feature).getFilterForID());
		sql = sqlBuf.toString();

		return sql;
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
