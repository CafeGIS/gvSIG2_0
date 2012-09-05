package org.gvsig.fmap.data.feature.db.jdbc.h2;

import java.lang.ref.WeakReference;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

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
import org.gvsig.fmap.data.feature.db.jdbc.JDBCFeaturesWriter;
import org.gvsig.fmap.data.feature.db.jdbc.JDBCStore;
import org.gvsig.fmap.data.feature.db.jdbc.JDBCStoreParameters;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.geom.util.UtilFunctions;
import org.gvsig.metadata.Metadata;
import org.gvsig.tools.exception.BaseException;

public class H2Store extends JDBCStore{
	public static final String CONNECTION_STRING = "h2";
	public static String DATASTORE_NAME = "H2Store";


	protected static Locale ukLocale = new Locale("en", "UK"); // English, UK version
    public H2StoreParameters getParametersH2(){
		return (H2StoreParameters)this.parameters;
	}


	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.db.jdbc.JDBCStore#createResource(org.gvsig.fmap.dal.feature.file.dbf.DBFStoreParameters)
	 */
	protected DBResource createResource(JDBCStoreParameters params) {
		return new H2Resource(params);
	}

	protected void initFeatureType() throws InitializeException{
		H2StoreParameters dParams = this.getParametersH2();
		try {
			this.defaultFeatureType = H2Utils.getFeatureType(this.getConnection(), dParams);
		} catch (ReadException e) {
			throw new InitializeException(DATASTORE_NAME,e);
		}
		String[] fieldsID = dParams.getFieldsId();
		if (fieldsID == null || fieldsID.length < 1){
			throw new InitializeException("Field Id not set",
					this.getName());
		} else if (fieldsID.length > 1){
			//TODO: Falta por implementar soporte para multiples ID
			throw new InitializeException("Multy fields id not supported yet",
					this.getName());

		} else{
			for (int i=0;i<fieldsID.length;i++){
				if (this.getDefaultFeatureType().getIndex(fieldsID[i]) < 0) {

				throw new InitializeException("Field id '"+ fieldsID[i] +"' (id pos "+ (i+1) +") not Found",
						this.getName());

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
			if (filter != null || order != null){
				throw new ReadException("Unsuported filter/order in sqlSource mode",this.getName());
			}
		}

		FeatureSet coll;
		if (featureManager == null){
			coll=new H2FeatureCollection(this,(DBFeatureType)type,filter,order);
		}else{
			if ((order != null && order != "")){
				coll=new H2FeatureCollectionWithFeatureID(featureManager,this,type,filter,order);
			} else{
				if (filter == null || filter == ""){
					coll=new H2FeatureCollectionEditing(featureManager,this,type);
				} else {
					coll=new H2FeatureCollectionEditingFiltered(featureManager,this,type,filter);
				}
			}


		}

		this.addObserver(new WeakReference(coll));
		return coll;
	}

	protected Connection getConnection() throws ReadException{
		return ((H2Resource)this.resource).getConnection();

	}

	protected void initSqlProperties() throws InitializeException{
		H2StoreParameters dParams = (H2StoreParameters)this.getParameters();
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
				String waWhere = getWorkingAreaWhere(dParams.getWorkingArea(), dParams.getSRISD());
				if (waWhere != null){
					this.baseWhereClause = "(("+this.baseWhereClause+") and "+waWhere +")";
				}

			}

			this.baseOrder = dParams.getBaseOrder();
		}
	}
	private String getWorkingAreaWhere(Envelope r, String strEPSG) {
		//TODO????
		if (r==null){
			return null;
		}
		return null;
	}


	public boolean canAlterFeatureType() {
		return true;
	}

	protected void doDispose() throws CloseException {
		super.doDispose();
	}

	public String getName() {
		return DATASTORE_NAME;
	}

	protected JDBCFeaturesWriter getFeaturesWriter() throws InitializeWriterException {
		H2FeaturesWriter writer = new H2FeaturesWriter();
		writer.init(this);
		return writer;
	}

	public DataServerExplorer getExplorer() throws ReadException {
		DataManager dm = DefaultDataManager.getManager();
		H2ExplorerParameters explorerParams = (H2ExplorerParameters)dm.createServerExplorerParameters(H2Explorer.DATAEXPLORER_NAME);
		explorerParams.putAllDefaultValues(this.getParametersH2());
		return dm.createServerExplorer(explorerParams);
	}

	public Metadata getMetadata() throws BaseException{

		if (metadata==null){
			MetadataManager manager=DefaultMetadataManager.getManager();
			metadata=manager.create(DATASTORE_NAME);
			Envelope extent=this.getFullExtent();
			metadata.set("extent",extent);
//			String srs=this.getSRS();
//			metadata.set("srs",srs);
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


//
//
//		if (metadata==null){
//			IMetadata tmp=super.getMetadata();
//
//			return tmp;
//		}else{
//			return super.getMetadata();
//		}

	}


	private Envelope getFullExtent() throws ReadException {
		Envelope result = UtilFunctions.createEnvelope();

		String geomField = this.getParametersH2().getDefaultGeometryField();
		FeatureSet coll = (FeatureSet) this.getDataCollection(new String[] {geomField},null,null);
		Iterator iter = coll.iterator();
		Feature feature;
		Geometry geom;

		while (iter.hasNext()) {
			feature = (Feature) iter.next();
			geom = feature.getDefaultGeometry();
			if (geom != null) {
				result.add(geom.getEnvelope());
			}

		}

		geom = null;
		feature = null;
		iter = null;
		coll.dispose();

		return result;
	}


	protected Feature createFeatureFromResulset(ResultSet rs, DBFeatureType featureType2) throws ReadException{
		return new H2Feature(featureType2,this,rs);
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
