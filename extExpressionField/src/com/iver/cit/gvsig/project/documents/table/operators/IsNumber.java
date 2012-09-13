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
public class IsNumber extends AbstractOperator{

	public String addText(String s) {
		return toString()+"("+s+")";
	}
	public String toString() {
		return "isNumber";
	}
	public void eval(BSFManager interpreter) throws BSFException {
//		interpreter.eval(ExpressionFieldExtension.BEANSHELL,null,-1,-1,"boolean isNumber (String value){try{java.lang.Double.parseDouble(value);}catch(java.lang.NumberFormatException e){return false;}return true;};");
		interpreter.exec(ExpressionFieldExtension.JYTHON,null,-1,-1,"def isNumber(value):\n" +
				"  import java.lang.Double\n" +
				"  import java.lang.NumberFormatException\n" +
				"  try:\n" +
				"    java.lang.Double.parseDouble(value)\n" +
				"  except java.lang.NumberFormatException:\n" +
				"    return 0==1 #false\n" +
				"  return 1==1 #true\n");
	}
	public boolean isEnable() {
		return (getType()==IOperator.STRING);
	}
	public String getDescription() {
	    return PluginServices.getText(this, "parameter") + ": " +
	    PluginServices.getText(this, "string_value") + "\n" +
	    PluginServices.getText(this, "returns") + ": " +
	    PluginServices.getText(this, "boolean_value") + "\n" +
	    PluginServices.getText(this, "description") + ": " +
	    "Returns true if the string parameter is a number.";
	}
}
