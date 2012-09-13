package com.iver.cit.gvsig.project.documents.table.operators;

import org.apache.bsf.BSFException;
import org.apache.bsf.BSFManager;
import org.gvsig.fmap.dal.feature.Feature;

import com.iver.cit.gvsig.ExpressionFieldExtension;
import com.iver.cit.gvsig.project.documents.table.AbstractOperator;

public abstract class AbstractField extends AbstractOperator{
	private boolean isEval=false;
	public Object getValue(Feature feature, String nameField){
		if (feature != null)
			return feature.get(nameField);
		return null;
	}
//	public Object getValue(String nameField,Index indexRow,SelectableDataSource sds) {
//		try {
//			int index=sds.getFieldIndexByName(nameField);
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
	public void eval(BSFManager interpreter) throws BSFException {
		if (!isEval){
		interpreter.declareBean("jfield",this,Field.class);
		interpreter.exec(ExpressionFieldExtension.JYTHON,null,-1,-1,"def field(nameField):\n" +
				"  return jfield.getValue(featureContainer.getFeature(), nameField)");
		isEval=true;
		}
	}
}
