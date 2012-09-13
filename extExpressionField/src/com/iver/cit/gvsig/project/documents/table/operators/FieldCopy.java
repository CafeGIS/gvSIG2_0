package com.iver.cit.gvsig.project.documents.table.operators;

import org.apache.bsf.BSFException;
import org.apache.bsf.BSFManager;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;

import com.iver.cit.gvsig.ExpressionFieldExtension;
import com.iver.cit.gvsig.project.documents.table.AbstractOperator;
/**
 * @author Vicente Caballero Navarro
 */
public class FieldCopy extends AbstractOperator{
	private FeatureAttributeDescriptor fd;
	public FieldCopy() {
	}
	public void setFieldDescription(FeatureAttributeDescriptor fd) {
		this.fd=fd;
	}
	public String addText(String s) {
		return s.concat(toString());
	}

	public void eval(BSFManager interpreter) throws BSFException {
		interpreter.declareBean(fd.getName(),this,FieldCopy.class);
		interpreter.eval(ExpressionFieldExtension.JYTHON,null,-1,-1,"java.lang.Object "+ fd.getName()+ "(){return "+fd.getName()+".getValue(feature);};");
	}
	public Object getValue(Feature feature){
		return feature.get(fd.getName());
	}
//	public Object getValue(Index indexRow,FeatureStore sds) {
//		try {
//			int index=sds.getFieldIndexByName(fd.getName());
//			Value value=sds.getFieldValue(indexRow.get(),index);
//			if (value instanceof NumericValue) {
//				double dv=((NumericValue)value).doubleValue();
//				return new Double(dv);
//			}else if (value instanceof DateValue) {
//				Date date=((DateValue)value).getValue();
//				return date;
//			}else if (value instanceof BooleanValue){
//				boolean b=((BooleanValue)value).getValue();
//				return new Boolean(b);
//			}else {
//				return value.toString();
//			}
//		} catch (ReadDriverException e) {
//			throw new RuntimeException(e.getMessage());
//		}
//	}
	public String toString() {
		return "["+fd.getName()+"]";
	}
	public boolean isEnable() {
		return true;
	}
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}
}
