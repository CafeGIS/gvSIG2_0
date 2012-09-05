package org.gvsig.fmap.data.feature.db.jdbc.postgresql;

import java.lang.ref.WeakReference;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.DataServerExplorer;
import org.gvsig.fmap.dal.DataSet;
import org.gvsig.fmap.dal.exception.CloseException;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.exception.OpenException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.impl.DefaultDataManager;
import org.gvsig.fmap.data.feature.db.DBFeatureType;
import org.gvsig.fmap.data.feature.db.DBResource;
import org.gvsig.fmap.data.feature.db.DBStoreParameters;
import org.gvsig.fmap.data.feature.db.jdbc.JDBCFeaturesWriter;
import org.gvsig.fmap.data.feature.db.jdbc.JDBCStore;
import org.gvsig.fmap.data.feature.db.jdbc.JDBCStoreParameters;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.geom.util.UtilFunctions;
import org.gvsig.metadata.Metadata;
import org.gvsig.tools.exception.BaseException;
import org.postgis.PGbox2d;
import org.postgis.PGbox3d;
import org.postgresql.util.PGobject;

public class PostgresqlStore extends JDBCStore{
	public static final String CONNECTION_STRING = "postgresql";
	public static String DATASTORE_NAME = "PostgresqlStore";
    PostgresqlStoreParameters getParametersPostgresql(){
		return (PostgresqlStoreParameters)this.parameters;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.db.jdbc.JDBCStore#createResource(org.gvsig.fmap.dal.feature.file.dbf.DBFStoreParameters)
	 */
	protected DBResource createResource(JDBCStoreParameters params) {
		return new PostgresqlResource(params);
	}


	protected void initFeatureType() throws InitializeException{
		PostgresqlStoreParameters dParams = this.getParametersPostgresql();
		try {
			this.defaultFeatureType = PostgresqlStoreUtils.getFeatureType(this.getConnection(), dParams);
		} catch (ReadException e) {
			throw new InitializeException(DATASTORE_NAME,e);
		}
		String[] fieldsID = dParams.getFieldsId();
		if (fieldsID == null || fieldsID.length < 1){
			throw new InitializeException(
					DATASTORE_NAME,
					new Exception("Field Id not set"));
		} else if (fieldsID.length > 1){
			//TODO: Falta por implementar soporte para multiples ID
			throw new InitializeException(
					DATASTORE_NAME,
					new Exception("Multy fields id not supported yet"));

		} else{
			for (int i=0;i<fieldsID.length;i++){
				if (this.getDefaultFeatureType().getIndex(fieldsID[i]) < 0) {

				throw new InitializeException(
						DATASTORE_NAME,
						new Exception("Field id '"+ fieldsID[i] +"' (id pos "+ (i+1) +") not Found"));

				}
			}
		}

		this.featureTypes = new ArrayList();
		this.featureTypes.add(defaultFeatureType, false);


	}

	public DataSet getDataCollection(FeatureType type, String filter, String order) throws ReadException {
		try {
			type = this.checkFeatureTypeForCollection(type);
		} catch (DataException e) {
			throw new ReadException(this.getName(), e);
		}

		if (useSqlSource ){
			if (filter != null || order != null) {
				throw new ReadException("Unsuported filter/order in sqlSource mode",this.getName());
			}
		}

		FeatureSet coll;
		if (featureManager == null){
			coll=new PostgresqlFeatureCollection(this,type,filter,order);
		}else{
			if ((order != null && order != "")){
				coll=new PostgresqlFeatureCollectionWithFeatureID(featureManager,this,type,filter,order);
			} else{
				if (filter == null || filter == ""){
					coll=new PostgresqlFeatureCollectionEditing(featureManager,this,type);
				} else {
					coll=new PostgresqlFeatureCollectionEditingFiltered(featureManager,this,type,filter);
				}
			}


		}
		this.addObserver(new WeakReference(coll));

		return coll;
	}

	protected Connection getConnection() throws ReadException{
		return super.getConnection();

	}


	protected void initSqlProperties() throws InitializeException{
		PostgresqlStoreParameters dParams = (PostgresqlStoreParameters)this.getParameters();
		if (dParams.getSqlSoure() != null){
			this.sqlSource = dParams.getSqlSoure();
			this.useSqlSource = true;
			this.sqlSelectPart = null;
			this.baseWhereClause = null;
			this.baseOrder = null;
		} else {
			this.sqlSelectPart = "SELECT " + dParams.getFieldsString() +" FROM "+ dParams.tableID();

			this.baseWhereClause = dParams.getBaseFilter();

			if (dParams.getWorkingArea() != null){
				String waWhere = getWorkingAreaFilter(dParams.getWorkingArea(), dParams.getSRISD());
				if (waWhere != null){
					this.baseWhereClause = "(("+this.baseWhereClause+") and "+waWhere +")";
				}

			}

			this.baseOrder = dParams.getBaseOrder();
		}
	}
	private String getWorkingAreaFilter(Envelope env, String strEPSG) {
		if (env == null) {
			return null;
		}
		String wktBox = "GeometryFromText('LINESTRING(" + env.getMinimum(0)
				+ " " + env.getMinimum(1) + ", " + env.getMaximum(0) + " "
				+ env.getMinimum(1) + ", " + env.getMaximum(0) + " "
				+ env.getMaximum(1) + ", " + env.getMinimum(0) + " "
				+ env.getMaximum(1) + ")', " + strEPSG + ")";

		return wktBox;
	}


	public boolean canAlterFeatureType() {
		return true;
	}


	protected void doDispose() throws CloseException{
		super.doDispose();
	}


	public String getName() {
		return DATASTORE_NAME;
	}


	protected JDBCFeaturesWriter getFeaturesWriter() throws InitializeWriterException {
		JDBCFeaturesWriter writer = new PostgresqlFeaturesWriter();
		writer.init(this);
		return writer;
	}

	public DataServerExplorer getExplorer() throws ReadException {
		DataManager dm = DefaultDataManager.getManager();
		PostgresqlExplorerParameters explorerParams = (PostgresqlExplorerParameters) dm
				.createServerExplorerParameters(PostgresqlExplorer.DATAEXPLORER_NAME);
		explorerParams.putAllDefaultValues(this.getParameters());
		return dm.createServerExplorer(explorerParams);
	}

	public Metadata getMetadata() throws BaseException {
		if (metadata==null){
			MetadataManager manager=DefaultMetadataManager.getManager();
			metadata=manager.create(DATASTORE_NAME);
			Envelope extent=this.getFullExtent();
			metadata.set("extent",extent);
			String srs = this.getDefaultFeatureType().getDefaultSRS();
			metadata.set("srs", srs);
		}
		if (this.alterMode){
			Envelope extent=(Envelope)metadata.get("extent");
			FeatureSet featureCollection=(FeatureSet)getDataSet();
			if (spatialManager.isFullExtentDirty()){
				if (!featureCollection.isEmpty()){
					Iterator featureIterator=featureCollection.iterator();
					extent = ((Feature)featureIterator.next()).getDefaultEnvelope();
					while(featureIterator.hasNext()){
						Feature feature=(Feature)featureIterator.next();
						Envelope boundExtent=feature.getDefaultEnvelope();
						if (boundExtent!=null) {
							extent.add(boundExtent);
						}
					}
				}
			}
			metadata.set("extent",extent);
		}
		return metadata;


//		if (metadata==null){
//			IMetadata tmp=super.getMetadata();
//
//			return tmp;
//		}else{
//			return super.getMetadata();
//		}

	}

	private Envelope getFullExtent() throws ReadException {
		Envelope fullExtent=null;
		Statement s = null;
		ResultSet r = null;


		try {
			DBStoreParameters params = this.getParametersPostgresql();
			if (params.getDefaultGeometryField() == null
					|| params.getDefaultGeometryField().length() == 0) {
				return null;
			}
			s = getConnection().createStatement();

			StringBuffer sql = new StringBuffer();
			sql.append("SELECT extent(");
			sql.append(params.getDefaultGeometryField());
			sql.append(") AS FullExtent FROM ");
			sql.append(params.tableID());
			if (this.getBaseWhereClause() != null
					&& this.getBaseWhereClause() != "") {
				sql.append(" WHERE ");
				sql.append(this.getBaseWhereClause());
			}

			r = s.executeQuery(sql.toString());
			if (!r.next()) {
				return null;
			}
			PGobject data = (PGobject) r.getObject(1);
			if (data instanceof PGbox3d) {
				PGbox3d box3d = (PGbox3d) data;
				fullExtent = UtilFunctions.createEnvelope(
						box3d.getLLB().x, box3d.getLLB().y,box3d.getLLB().z,
						box3d.getURT().x, box3d.getURT().y,box3d.getURT().z);

			} else {
				PGbox2d box2d = (PGbox2d) data;
				fullExtent = UtilFunctions.createEnvelope(
						box2d.getLLB().x, box2d.getLLB().y,
						box2d.getURT().x, box2d.getURT().y);
			}
			String strAux = r.getString(1);
			System.out.println("fullExtent = " + strAux);
		} catch (SQLException e) {
			throw new ReadException(this.getName(),e);
		} finally {
			if (r != null) {
				try {
					r.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (s != null) {
				try {
					s.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}

		return fullExtent;
	}

	protected Feature createFeatureFromResulset(ResultSet rs, DBFeatureType featureType2) throws ReadException {
		if (featureType2== null){
			return new PostgresqlFeature(this.getDefaultFeatureType(),this,rs);
		}else{
			return new PostgresqlFeature(featureType2,this,rs);
		}
	}


	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.FeatureStore#doRefresh()
	 */
	protected void doRefresh() throws OpenException, InitializeException {
		this.initFeatureType();
		this.initSqlProperties();
	}
	public boolean canWriteGeometry(int gvSIGgeometryType) {
		try {
			return getFeaturesWriter().canWriteGeometry(gvSIGgeometryType);
		} catch (InitializeWriterException e) {
			e.printStackTrace();
		}
		return false;
	}

	public Feature getByIndex(long index) throws ReadException {
		// TODO Auto-generated method stub
		return null;
	}

}