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
public class ToNumber extends AbstractOperator{

	public String addText(String s) {
		return toString()+"("+s+")";
	}
	public String toString() {
		return "toNumber";
	}
	public void eval(BSFManager interpreter) throws BSFException {
//		interpreter.eval(ExpressionFieldExtension.BEANSHELL,null,-1,-1,"double toNumber(String value){return java.lang.Double.parseDouble(value);};");
		interpreter.exec(ExpressionFieldExtension.JYTHON,null,-1,-1,"def toNumber(value):\n" +
				"  import java.lang.Double\n" +
				"  return java.lang.Double.parseDouble(value)");
	}
	public boolean isEnable() {
		return (getType()==IOperator.NUMBER );
	}
	public String getDescription() {
	    return PluginServices.getText(this, "parameter") + ": " +
	    PluginServices.getText(this, "string_value") + "\n" +
	    PluginServices.getText(this, "returns") + ": " +
	    PluginServices.getText(this, "double_value") + "\n" +
	    PluginServices.getText(this, "description") + ": " +
	    "Returns a new number initialized to the value represented by the specified String";
	}
}
