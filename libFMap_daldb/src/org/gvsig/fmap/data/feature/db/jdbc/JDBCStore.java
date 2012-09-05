package org.gvsig.fmap.data.feature.db.jdbc;


import java.security.KeyException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;

import org.gvsig.fmap.data.DataExplorer;
import org.gvsig.fmap.data.DataStoreParameters;
import org.gvsig.fmap.data.exceptions.CloseException;
import org.gvsig.fmap.data.exceptions.DataException;
import org.gvsig.fmap.data.exceptions.InitializeException;
import org.gvsig.fmap.data.exceptions.OpenException;
import org.gvsig.fmap.data.exceptions.ReadException;
import org.gvsig.fmap.data.exceptions.WriteException;
import org.gvsig.fmap.data.feature.AttributeDescriptor;
import org.gvsig.fmap.data.feature.CreatedFeature;
import org.gvsig.fmap.data.feature.Feature;
import org.gvsig.fmap.data.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.data.feature.FeatureReference;
import org.gvsig.fmap.data.feature.FeatureType;
import org.gvsig.fmap.data.feature.db.DBFeatureID;
import org.gvsig.fmap.data.feature.db.DBFeatureType;
import org.gvsig.fmap.data.feature.db.DBResource;
import org.gvsig.fmap.data.feature.db.DBStore;
import org.gvsig.fmap.data.feature.exceptions.InitializeWriterException;
import org.gvsig.fmap.data.feature.impl.commands.implementation.AttributeCommand;
import org.gvsig.fmap.data.feature.impl.commands.implementation.FeatureCommand;
import org.gvsig.fmap.data.feature.impl.commands.implementation.UpdateAttributeCommand;
import org.gvsig.fmap.data.feature.impl.commands.implementation.FeatureUpdateCommand;

import org.gvsig.fmap.data.feature.impl.commands.implementation.FeatureCommandUpdate;
import org.gvsig.fmap.data.resource.ResourceManager;
import org.gvsig.fmap.data.resource.impl.DefaultResourceManager;
import org.gvsig.fmap.data.resource.spi.AbstractResource;
import org.gvsig.metadata.Metadata;
import org.gvsig.metadata.MetadataManager;
import org.gvsig.metadata.DefaultMetadataManager;
import org.gvsig.tools.exception.BaseException;

public abstract class JDBCStore extends DBStore {

	protected JDBCResource resource = null;
	protected String sqlSelectPart;
	protected String baseWhereClause = null;
	protected String baseOrder = null;
	protected String sqlSource = null;
	protected boolean useSqlSource = false;
	protected Metadata metadata;


	public void init(DataStoreParameters parameters) throws InitializeException {

		DBResource tmpResource = this.createResource((JDBCStoreParameters)parameters);
		DefaultResourceManager resMan = DefaultResourceManager.getResourceManager();

		try {
			this.resource = (JDBCResource) resMan
					.addResource(tmpResource, this);
		} catch (DataException e1) {
			throw new InitializeException(this.getName(),e1);
		}

		super.init(parameters,this.resource);


		this.initFeatureType();
		this.initSqlProperties();
	}


	protected abstract DBResource createResource(JDBCStoreParameters params);
	protected abstract void initFeatureType() throws InitializeException;
	protected abstract void initSqlProperties() throws InitializeException;


	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.FeatureStore#doClose()
	 */
	protected void doClose() throws CloseException {
		this.resource.close();

	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.FeatureStore#doDispose()
	 */
	protected void doDispose() throws CloseException {
		ResourceManager resMan = DefaultResourceManager.getResourceManager();

    	try {
			resMan.remove(this.resource, this);
		} catch (DataException e1) {
			throw new CloseException(this.getName(),e1);
		} catch (KeyException e) {
			// TODO Auto-generated catch block
			throw new CloseException(this.getName(),e);
		}
		this.metadata = null;
		super.doDispose();
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.FeatureStore#doOpen()
	 */
	protected void doOpen() throws OpenException {
		//No Operation

	}


	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.FeatureStore#canAlterFeatureType()
	 */
	public boolean canAlterFeatureType() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.DataStore#getExplorer()
	 */
	public DataExplorer getExplorer() throws ReadException {
		return null;
	}


	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.FeatureStore#init(org.gvsig.fmap.dal.DataStoreParameters, org.gvsig.fmap.dal.Resource)
	 */
	public void init(DataStoreParameters parameters, AbstractResource resource) throws InitializeException {
		super.init(parameters, resource);
		this.resource=(JDBCResource)resource;
	}

	protected Connection getConnection() throws ReadException{
		return this.resource.getConnection();
	}

	protected Connection getWriterConnection() throws ReadException{
		return this.resource.getWriterConnection();
	}

	public boolean allowWrite() {
		return super.allowWrite() && !this.useSqlSource;
	}


	public String getSqlSelectPart() {
		return sqlSelectPart;
	}

	public String getBaseOrder() {
		return this.baseOrder;
	}

	public String getBaseWhereClause() {
		return baseWhereClause;
	}

	public boolean isUseSqlSource() {
		return this.useSqlSource;
	}

	public String getSqlSource() {
		return this.sqlSource;
	}

	protected abstract JDBCFeaturesWriter getFeaturesWriter() throws InitializeWriterException;

	protected void doFinishEdition() throws WriteException, ReadException {
		Iterator commandsfeatures = null;
    	JDBCFeaturesWriter selectiveWriter = getFeaturesWriter();
    	FeatureType type = getDefaultFeatureType();
    	boolean needRefresh=false;

		Feature feature;
		Object obj;
		FeatureReference featureId;


		selectiveWriter.updateFeatureType(type);
		selectiveWriter.preProcess();

		try{
	    	commandsfeatures = commands.getCommandsAttributeDeleted().iterator();
			while( commandsfeatures.hasNext() ) {
				obj=commandsfeatures.next();
				if (obj instanceof AttributeCommand){
					FeatureAttributeDescriptor attribute = ((AttributeCommand)obj).getAttributeDescriptor();
					selectiveWriter.deleteAttribute(attribute);
					needRefresh=true;
				}
			}
			commandsfeatures = commands.getCommandsAttributeUpdated().iterator();
			while( commandsfeatures.hasNext() ) {
				obj=commandsfeatures.next();
				if (obj instanceof AttributeCommand){
					FeatureAttributeDescriptor oldAttribute = ((UpdateAttributeCommand)obj).getOldAttributeDescriptor();
					FeatureAttributeDescriptor attribute = ((UpdateAttributeCommand)obj).getAttributeDescriptor();
					selectiveWriter.updateAttribute(oldAttribute,attribute);
					needRefresh=true;
				}
			}
			commandsfeatures = commands.getCommandsAttributeInserted().iterator();
			while( commandsfeatures.hasNext() ) {
				obj=commandsfeatures.next();
				if (obj instanceof AttributeCommand){
					FeatureAttributeDescriptor attribute = ((AttributeCommand)obj).getAttributeDescriptor();
					selectiveWriter.insertAttribute(attribute);
					needRefresh=true;
				}
			}


			commandsfeatures = commands.getCommandsFeatureDeleted().iterator();
			while( commandsfeatures.hasNext() ) {
				obj=commandsfeatures.next();
				if (obj instanceof FeatureCommand){
					feature = ((FeatureCommand)obj).getFeature();
					if (feature instanceof CreatedFeature) {
						continue;
					}

					selectiveWriter.delete(feature);
				}
			}

			commandsfeatures = commands.getCommandsFeatureInserted().iterator();
			while( commandsfeatures.hasNext() ) {
				obj=commandsfeatures.next();
				if (obj instanceof FeatureCommand){
					feature = ((FeatureCommand)obj).getFeature();
					if (featureManager.isDeleted(feature)){
						continue;
					}
					selectiveWriter.insertFeature(feature);
				}
			}

			commandsfeatures = commands.getCommandsFeatureUpdated().iterator();
			HashMap toUpdate = new HashMap();
			while( commandsfeatures.hasNext() ) {
				obj=commandsfeatures.next();
				if (obj instanceof FeatureCommand){
//					Feature oldFeature = ((UpdateFeatureCommand)obj).getOldFeature();
					feature = ((FeatureUpdateCommand)obj).getFeature();
					feature = ((FeatureCommandUpdate)obj).getFeature();
					featureId =feature.getReference();
					feature = featureManager.get(featureId, this,null);
					if (feature != null){
						toUpdate.put(featureId, feature);
					}
				}
			}
			Iterator toUpdateIter = toUpdate.values().iterator();
			while (toUpdateIter.hasNext()){
				feature = (Feature) toUpdateIter.next();
				selectiveWriter.updateFeature(feature);
			}


			selectiveWriter.postProcess();
			this.resource.changed(this);
			if (needRefresh){
				this.refresh();
			}


		}catch (ReadException e) {
			selectiveWriter.cancelProcess();
			throw e;
		} catch (WriteException e){
			selectiveWriter.cancelProcess();
			throw e;
		} catch (Exception e){
			selectiveWriter.cancelProcess();
			throw new WriteException(this.getName(),e);
		}
	}

	public String getFilterForID(DBFeatureType fType, Object[] featureKey) {
		if (fType.getFieldsId().length != 1) {
			throw new UnsupportedOperationException("ID fields > 1");
		}
		String id =fType.getFieldsId()[0];
		return id + " = " + objectToSqlString(featureKey[0]);
	}

	protected String objectToSqlString(Object obj) {
		if (obj instanceof String){
			return "'"+ scapeString((String)obj) +"'";
		} else if (obj == null){
			return "null";
		}else{
			// OJO con otros tipos!!
			return obj.toString();
		}

	}

	protected String scapeString(String str) {
		return str.replace("'", "''");
	}

	public Feature getFeatureByID(FeatureReference id,FeatureType featureType) throws ReadException{
		return getFeatureByID(featureType,((DBFeatureID)id).getKey());
	}

	public Metadata getMetadata() throws BaseException {
		if (metadata==null){
			MetadataManager manager=DefaultMetadataManager.getManager();
			metadata=manager.create(this.getName());

			DBFeatureType fType = (DBFeatureType) this.getDefaultFeatureType();
			metadata.set("srs",fType.getDefaultSRS());
		}
		return metadata;
	}


	public Feature getFeatureByID(FeatureType featureType, Object[] featureKey) throws ReadException{
		//TODO: Tener en cuenta el FeatureType por si es distinto
		if (useSqlSource){
			throw new ReadException("Unsuported featureByID in sqlSource mode",this.getName());
		}
		if (featureType==null){
			featureType=getDefaultFeatureType();
		}else{
			if (!featureType.isSubtypeOf(this.getDefaultFeatureType())){
				throw new ReadException("invalid type",this.getName());
			}
		}
		ResultSet rs=null;
		try{
			this.open();
			Statement st=this.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql = this.getSqlSelectPart() + " WHERE "+ this.getFilterForID((DBFeatureType)this.getDefaultFeatureType(), featureKey);
			rs=st.executeQuery(sql);
			if(rs.next()){
				return createFeatureFromResulset(rs, (DBFeatureType)featureType);
			}

		} catch (java.sql.SQLException e) {
			e.printStackTrace();
			throw new ReadException(this.getName(), e);
		} finally{
			if (rs != null){
				try {
					rs.close();
				} catch (java.sql.SQLException e) {
					throw new ReadException(this.getName(),e);
				}
			}
		}
		return null;
	}


	protected abstract Feature createFeatureFromResulset(ResultSet rs, DBFeatureType featureType2) throws ReadException;


	protected FeatureType checkFeatureTypeForCollection(FeatureType fType)
			throws DataException {
		if (!this.useSqlSource) {
			return super.checkFeatureTypeForCollection(fType);
		}
		FeatureType defType = this.getDefaultFeatureType();
		if (fType == null || fType.equals(defType)) {
			return defType.getSubFeatureType(null);
		}
		if (!fType.isSubtypeOf(defType)) {
			throw new ReadException("invalid type", this.getName());
		}
		Iterator iterDef = defType.iterator();
		Iterator iterType = fType.iterator();

		AttributeDescriptor defAttr;
		AttributeDescriptor attr;

		while (iterDef.hasNext()) {
			defAttr = (AttributeDescriptor) iterDef.next();
			attr = (AttributeDescriptor) iterType.next();
			if (!(
					defAttr.getName().equals(attr.getName())
					&& defAttr.getDataType().equals(attr.getDataType())
					&& defAttr.getSize() == attr.getSize()
					&& defAttr.isReadOnly() == attr.isReadOnly()
					&& defAttr.isEvaluated() == attr.isEvaluated()
					)) {
				throw new ReadException("invalid type", this.getName());
			}
		}
		while (iterType.hasNext()) {
			attr = (AttributeDescriptor) iterType.next();
			if (!attr.isEvaluated()) {
				throw new ReadException("invalid type", this.getName());
			}
		}

		return fType;
	}

}