package org.gvsig.fmap.data.feature.db.jdbc.postgresqlbin;

import java.lang.ref.WeakReference;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Locale;

import org.gvsig.fmap.dal.DataServerExplorer;
import org.gvsig.fmap.dal.DataSet;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.data.feature.db.DBFeatureType;
import org.gvsig.fmap.data.feature.db.DBResource;
import org.gvsig.fmap.data.feature.db.jdbc.JDBCFeaturesWriter;
import org.gvsig.fmap.data.feature.db.jdbc.JDBCStoreParameters;
import org.gvsig.fmap.data.feature.db.jdbc.postgresql.PostgresqlStore;
import org.gvsig.metadata.Metadata;
import org.gvsig.tools.exception.BaseException;

public class PostgresqlBinStore extends PostgresqlStore{
	public static final String CONNECTION_STRING = "postgresql";
	public static String DATASTORE_NAME = "PostgresqlStore";
	protected static Locale ukLocale = new Locale("en", "UK"); // English, UK version
    PostgresqlBinStoreParameters getParametersPostgresql(){
		return (PostgresqlBinStoreParameters)this.parameters;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.db.jdbc.JDBCStore#createResource(org.gvsig.fmap.dal.feature.file.dbf.DBFStoreParameters)
	 */
	protected DBResource createResource(JDBCStoreParameters params) {
		return new PostgresqlBinResource(params);
	}

	public String getName() {
		return DATASTORE_NAME;
	}

	public DataSet getDataCollection(FeatureType type, String filter, String order) throws ReadException {
		try {
			type = this.checkFeatureTypeForCollection(type);
		} catch (DataException e) {
			throw new ReadException(this.getName(), e);
		}


		if (useSqlSource ){
			if (filter != null || order != null || type != getDefaultFeatureType()){
				throw new ReadException("Unsuported filter/order in sqlSource mode",this.getName());
			}
		}
		FeatureSet coll=null;
		if (featureManager == null){
			coll=new PostgresqlBinFeatureCollection(this,type,filter,order);
		}else{
			throw new ReadException("Unsuported mode",this.getName());
//			if ((order != null && order != "")){
//				coll=new H2FeatureCollectionWithFeatureID(featureManager,this,type,filter,order);
//			} else{
//				if (filter == null || filter == ""){
//					coll=new H2FeatureCollectionEditing(featureManager,this,type);
//				} else {
//					coll=new H2FeatureCollectionEditingFiltered(featureManager,this,type,filter);
//				}
//			}
//

		}
		this.addObserver(new WeakReference(coll));

		return coll;
	}


	public Feature getFeatureByID(FeatureType featureType2, Object[] featureKey) throws ReadException{
		if (useSqlSource){
			throw new ReadException(this.getName(),
					new UnsupportedOperationException("Unsuported featureByID in sqlSource mode"));
		}
		ResultSet rs=null;
		try{
			this.open();

			Statement st=this.getConnection().createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			String sql = this.getSqlSelectPart() +
					" WHERE "+
					this.getFilterForID(
							(DBFeatureType)this.getDefaultFeatureType(),
							featureKey);
			String cursorName = PostgresqlBinStoreUtils.createCursorName();
			String mSql="BEGIN";
			st.execute(mSql);
			mSql="declare " + cursorName + " binary cursor for " + sql;
			st.execute(mSql);
			rs=st.executeQuery("fetch 1 in "+ cursorName);
			if (rs.isLast()) {

				return null;
			}else{
				if(rs.next()){
					return this.createFeatureFromResulset(
							rs,
							(DBFeatureType)this.getDefaultFeatureType());
				}

			}

		} catch (java.sql.SQLException e) {
			e.printStackTrace();
			throw new ReadException(this.getName(), e);
		} finally{
			if (rs != null) {
				try {
					rs.close();
				} catch (java.sql.SQLException e) {
					// TODO ?????
					e.printStackTrace();
				}
			}
		}
		return null;
	}



	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.db.jdbc.postgresql.PostgresqlStore#getConnection()
	 */
	protected Connection getConnection() throws ReadException {
		return super.getConnection();
	}

	public boolean canAlterFeatureType() {
		return true;
	}



	public JDBCFeaturesWriter getFeaturesWriter() {
//		FeaturesWriter writer = new H2FeaturesWriter();
//		writer.init(this);
//		return writer;
		return null;
	}

	public DataServerExplorer getExplorer() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isEditable() {
		return false;

//		return super.isEditable();
	}


	public Metadata getMetadata() throws BaseException {
		if (metadata==null){
			Metadata tmp=super.getMetadata();

			return tmp;
		}else{
			return super.getMetadata();
		}

	}


	protected Feature createFeatureFromResulset(ResultSet rs, DBFeatureType featureType2) throws ReadException {
		return new PostgresqlBinFeature(featureType2,this,rs);
	}

}