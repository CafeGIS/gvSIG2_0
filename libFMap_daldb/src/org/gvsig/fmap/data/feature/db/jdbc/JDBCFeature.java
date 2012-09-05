package org.gvsig.fmap.data.feature.db.jdbc;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.AbstractFeature;
import org.gvsig.fmap.dal.feature.AttributeDescriptor;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureReference;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.exception.IsNotFeatureSettingException;
import org.gvsig.fmap.data.feature.db.DBFeature;
import org.gvsig.fmap.data.feature.db.DBFeatureType;
import org.gvsig.fmap.geom.primitive.Envelope;

public abstract class JDBCFeature extends DBFeature {

	protected JDBCStore store;
	protected Object[] featureKey;

	public JDBCFeature(FeatureType featureType, JDBCStore store, ResultSet rs) throws ReadException {
		super((DBFeatureType)featureType);
		this.store= store;
		this.load(rs);
	}

	protected JDBCFeature(JDBCFeature feature) throws ReadException {
		super((DBFeatureType)feature.featureType);
		this.store= feature.store;
		this.featureKey = new Object[feature.featureKey.length];
		System.arraycopy(feature.featureKey, 0, this.featureKey, 0, feature.featureKey.length);
		Iterator iter=this.featureType.iterator();

		Object value;
		FeatureAttributeDescriptor featureAttribute;
		int column;
		this.loading();
		while(iter.hasNext()){
			featureAttribute=(FeatureAttributeDescriptor)iter.next();
			column=((AttributeDescriptor)featureAttribute).originalPosition();
			value=feature.getAttribute(column);
			try{
				if (featureAttribute.equals(FeatureAttributeDescriptor.GEOMETRY)){
					//TODO: Falta clonar geometria
					try {
						this.setGeometry(column, value);
					} catch (IsNotFeatureSettingException e) {
						throw new ReadException(this.store.getName(),e);
					}

				}else if (featureAttribute.equals(FeatureAttributeDescriptor.OBJECT)){
					//TODO falta clonar el objeto
//					((Cloneable)get(i)).clo
//					set(i,.);
				}else {
					this.setAttribute(column, value);
				}
			} catch (IsNotFeatureSettingException e){
				throw new ReadException(this.store.getName(),e);
			}
		}
		this.stopLoading();

	}


	protected void load(ResultSet rs) throws ReadException{
		try {
			this.loading();
			this.featureKey = getPkFromResulset(rs, (DBFeatureType)this.featureType);

			Iterator iter = this.featureType.iterator();
			ResultSetMetaData rsMeta = rs.getMetaData();
			while (iter.hasNext()) {
				AttributeDescriptor fad=(AttributeDescriptor)iter.next();
				if (fad.isEvaluated()){
					continue;
				}else if (fad.originalPosition() > -1 && this.isResulsetColumns(rsMeta, fad)){
					this.loadValueFromResulset(rs, fad);
				} else{
					try {
						this.setAttribute(fad.ordinal(),fad.getDefaultValue());
					} catch (IsNotFeatureSettingException e) {
						throw new ReadException(this.store.getName(),e);
					}
				}

			}
			this.stopLoading();
		} catch (java.sql.SQLException e) {
			throw new ReadException(this.store.getName(),e);
		}
	}

	protected boolean isResulsetColumns(ResultSetMetaData rsMeta,FeatureAttributeDescriptor fad) throws SQLException{
		String name = fad.getName();
		for (int i=1;i<=rsMeta.getColumnCount();i++){
			if (rsMeta.getColumnName(i).equalsIgnoreCase(name)){
				return true;
			}
		}
		return false;
	}

	protected abstract void loadValueFromResulset(ResultSet rs, FeatureAttributeDescriptor attr) throws ReadException;


	protected Object[] getPkFromResulset(ResultSet rs, DBFeatureType featureType) throws java.sql.SQLException{
		String[] fieldsId = featureType.getFieldsId();
		Object[] result = new Object[fieldsId.length];
		for (int i=0;i<fieldsId.length;i++){
			result[i] = rs.getObject(fieldsId[i]);
		}
		return result;

	}



	public void editing() throws DataException {
		if (!this.store.isEditing()){
			throw new DataException("Read Only Feature");
		}
		super.editing();
	}

	protected Object[] getPkFromValues(ResultSet rs, DBFeatureType featureType) throws java.sql.SQLException{
		String[] fieldsId = featureType.getFieldsId();
		//TODO: optimizar esto usando el ordinal de los ids
		Object[] result = new Object[fieldsId.length];
		for (int i=0;i<fieldsId.length;i++){
			result[i] = this.getAttribute(fieldsId[i]);
		}
		return result;

	}


	public abstract FeatureReference getID();

	public Envelope getDefaultExtent() {
		return null;
	}

	public List getSRSs() {
		return null;
	}

	public String getDefaultSRS() {
		return null;
	}

	public String getFilterForID() {
		return this.store.getFilterForID((DBFeatureType)this.featureType,this.featureKey);
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


}