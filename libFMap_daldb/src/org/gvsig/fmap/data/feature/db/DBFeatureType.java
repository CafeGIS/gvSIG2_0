package org.gvsig.fmap.data.feature.db;

import java.util.ArrayList;
import java.util.Iterator;

import org.gvsig.fmap.dal.feature.AttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.impl.DefaultFeatureType;

public class DBFeatureType extends DefaultFeatureType {
	private String tableID;
	private boolean readOnly=false;

	public String getTableID() {
		return tableID;
	}

	public void setTableID(String tableID) {
		this.tableID = tableID;
	}

	protected FeatureType newFeatureType() {
		return new DBFeatureType();
	}

	public FeatureType getCopy() {
		DBFeatureType newFType = (DBFeatureType)super.getCopy();
		newFType.tableID = this.tableID;
		return newFType;
	}

	public void setReadOnly(boolean readOnly){
		this.readOnly = readOnly;
	}

	public boolean isReadOnly(){
		return this.readOnly;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.FeatureType#createNewAttributeDescriptor()
	 */
	public FeatureAttributeDescriptor createNewAttributeDescriptor() {
		return new DBAttributeDescriptor(this,true);
	}


	public FeatureAttributeDescriptor createAttributeDescriptor() {
		return new DBAttributeDescriptor(this,false);
	}

}
