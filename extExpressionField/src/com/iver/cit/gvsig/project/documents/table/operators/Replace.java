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
public class Replace extends AbstractOperator{

	public String addText(String s) {
		return toString()+"("+s+" , \"\",\"\")";
	}
	public String toString() {
		return "replace";
	}
	public void eval(BSFManager interpreter) throws BSFException {
//		interpreter.eval(ExpressionFieldExtension.BEANSHELL,null,-1,-1,"String replace(String value1,String value2,String value3){return value1.replaceAll(value2,value3);};");
		interpreter.exec(ExpressionFieldExtension.JYTHON,null,-1,-1,"def replace(value1,value2,value3):\n" +
				"  return value1.replace(value2,value3)");
	}
	public boolean isEnable() {
		return (getType()==IOperator.STRING);
	}
	public String getTooltip(){
		return PluginServices.getText(this,"operator")+":  "+toString()+"("+PluginServices.getText(this,"parameter")+"1,"+PluginServices.getText(this,"parameter")+"2, "+PluginServices.getText(this,"parameter")+"3"+")"+"\n"+getDescription();
	}
	public String getDescription() {
        return PluginServices.getText(this, "parameter") + "1"+": " +
        PluginServices.getText(this, "string_value") + "\n"+
        PluginServices.getText(this, "parameter") + "2"+": " +
        PluginServices.getText(this, "string_value") + "\n"+
        PluginServices.getText(this, "parameter") + "3"+": " +
        PluginServices.getText(this, "string_value") + "\n"+
        PluginServices.getText(this, "returns") + ": " +
        PluginServices.getText(this, "string_value") + "\n" +
        PluginServices.getText(this, "description") + ": " +
        "Replaces each substring(parameter2) of parameter1 string that matches the given regular expression with the given replacement parameter3";
    }
}
