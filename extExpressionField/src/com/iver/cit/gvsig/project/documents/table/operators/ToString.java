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
public class ToString extends AbstractOperator{

	public String addText(String s) {
		return toString()+"("+s+")";
	}
	public String toString() {
		return "toString";
	}
	public void eval(BSFManager interpreter) throws BSFException {
//		interpreter.eval(ExpressionFieldExtension.BEANSHELL,null,-1,-1,"String toString(java.lang.Object value){" +
//				"if (value instanceof java.util.Date)" +
//					"return ((java.util.Date)value).toString();" +
//				"return String.valueOf(value);};");
		interpreter.exec(ExpressionFieldExtension.JYTHON,null,-1,-1,"def toString(value):\n" +
				"  return str(value)");
	}
	public boolean isEnable() {
		return (getType()==IOperator.NUMBER);// || getType()==IOperator.DATE);
	}
	public String getDescription() {
	    return PluginServices.getText(this, "parameter") + ": " +
	    PluginServices.getText(this, "value") + "\n" +
	    PluginServices.getText(this, "returns") + ": " +
	    PluginServices.getText(this, "string_value") + "\n" +
	    PluginServices.getText(this, "description") + ": " +
	    "Returns the string representation of the Object argument";
	}
}
