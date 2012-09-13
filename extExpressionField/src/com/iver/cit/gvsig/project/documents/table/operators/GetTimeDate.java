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
public class GetTimeDate extends AbstractOperator{

	public String addText(String s) {
		return toString()+"("+s+")";
	}
	public String toString() {
		return "getTimeDate";
	}
	public void eval(BSFManager interpreter) throws BSFException {
//		interpreter.eval(ExpressionFieldExtension.BEANSHELL,null,-1,-1,"double getTimeDate(java.lang.Object value){return value.getTime();};");
		interpreter.exec(ExpressionFieldExtension.JYTHON,null,-1,-1,
				"def getTimeDate(value):\n" +
				"  return value.getTime()");
	}
	public boolean isEnable() {
		return (getType()==IOperator.DATE);
	}

	public String getDescription() {
        return PluginServices.getText(this, "parameter") +": " +
        PluginServices.getText(this, "date_value") + "\n"+
        PluginServices.getText(this, "returns") + ": " +
        PluginServices.getText(this, "numeric_value") + "\n" +
        PluginServices.getText(this, "description") + ": " +
        "Returns the number of milliseconds since January 1, 1970, 00:00:00 GMT represented by this Date object.";
    }
}
