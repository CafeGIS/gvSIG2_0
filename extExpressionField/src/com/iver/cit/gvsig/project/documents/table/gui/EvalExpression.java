package com.iver.cit.gvsig.project.documents.table.gui;

import java.util.Date;

import org.apache.bsf.BSFException;
import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.EditableFeature;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.project.document.table.gui.FeatureTableDocumentPanel;

/**
 * @author Vicente Caballero Navarro
 */
public class EvalExpression {
	private FeatureType featureType;
	private FeatureAttributeDescriptor selectedDescriptor;
	public EvalExpression() {
	}
	public void setTable(FeatureTableDocumentPanel table) {
		try {
			selectedDescriptor = table.getTablePanel().getTable().getSelectedColumnsAttributeDescriptor()[0];
		    featureType = table.getModel().getStore().getDefaultFeatureType();
		} catch (DataException e) {
			e.printStackTrace();
		}
	}

	public void setValue(FeatureSet featureSet,Feature feature,Object obj) {
		Object objBoolean=obj;
		EditableFeature eFeature=feature.getEditable();
		int type=((FeatureAttributeDescriptor)featureSet.getDefaultFeatureType().get(selectedDescriptor.getName())).getDataType();
		if (type==DataTypes.BOOLEAN){
			Integer integer=((Integer)obj).intValue();
			if (integer==0){
				objBoolean=new Boolean(false);
			}else{
				objBoolean=new Boolean(true);
			}
		}
		eFeature.set(selectedDescriptor.getName(), objBoolean);
		try {
			featureSet.update(eFeature);
		} catch (DataException e) {
			e.printStackTrace();
		}
	}
	 public void isCorrectValue(Object obj) throws BSFException {
	        if (obj instanceof Number || obj instanceof Date || obj instanceof Boolean || obj instanceof String || obj == null ) {

	        }else{
	        	throw new BSFException("incorrect");
	        }
	 }

	public FeatureAttributeDescriptor getFieldDescriptorSelected() {
		return selectedDescriptor;
	}
	public FeatureType getFieldDescriptors() {
		return featureType;
	}
}
