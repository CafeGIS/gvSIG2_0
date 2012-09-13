package com.iver.cit.gvsig.project.documents.table.operators;

import org.apache.bsf.BSFException;
import org.apache.bsf.BSFManager;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.ExpressionFieldExtension;
import com.iver.cit.gvsig.project.documents.table.AbstractOperator;
import com.iver.cit.gvsig.project.documents.table.IOperator;

/**
 * @author Vicente Caballero Navarro
 */
public class SetTimeDate extends AbstractOperator{

	public String addText(String s) {
		return toString()+"("+s+")";
	}
	public String toString() {
		return "setTimeDate";
	}
	public void eval(BSFManager interpreter) throws BSFException {
//		interpreter.eval(ExpressionFieldExtension.BEANSHELL,null,-1,-1,"java.util.Date setTimeDate(java.lang.Object value1,double value2){value1.setTime((long)value2);return value1;};");
		interpreter.exec(ExpressionFieldExtension.JYTHON,null,-1,-1,
				"import java.util.Date as jDate\n"+
				"def setTimeDate(value):\n"+
				"  aux = jDate()\n"+
				"  aux.setTime(long(value))\n"+
				"  return aux");
	}
	public boolean isEnable() {
		return (getType()==IOperator.DATE);
	}

	public String getDescription() {
	       return PluginServices.getText(this, "parameter") + ": " +
	        PluginServices.getText(this, "numeric_value") + "\n"+
	        PluginServices.getText(this, "returns") + ": " +
	        PluginServices.getText(this, "date_value") + "\n" +
	        PluginServices.getText(this, "description") + ": " +
	        "Returns a date object to represent a point in time that is 'parameter' milliseconds after January 1, 1970 00:00:00 GMT.";
    }
}
