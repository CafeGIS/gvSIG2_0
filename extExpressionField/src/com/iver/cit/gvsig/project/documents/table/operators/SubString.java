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
public class SubString extends AbstractOperator{

	public String addText(String s) {
		return toString()+"("+s+",,)";
	}
	public String toString() {
		return "subString";
	}
	public void eval(BSFManager interpreter) throws BSFException {
//		interpreter.eval(ExpressionFieldExtension.BEANSHELL,null,-1,-1,"String subString(String value1,int value2,int value3){return value1.substring(value2,value3);};");
		interpreter.exec(ExpressionFieldExtension.JYTHON,null,-1,-1,"def subString(value1,value2,value3):\n" +
				"  return value1[value2:value3]");
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
        PluginServices.getText(this, "numeric_value") + "\n"+
        PluginServices.getText(this, "parameter") + "3"+": " +
        PluginServices.getText(this, "numeric_value") + "\n"+
        PluginServices.getText(this, "returns") + ": " +
        PluginServices.getText(this, "string_value") + "\n" +
        PluginServices.getText(this, "description") + ": " +
        "Returns a new string that is a substring of parameter1.\n" +
        "The substring begins at the specified parameter2 and extends to the character at index parameter3 - 1.\n" +
        "Thus the length of the substring is endIndex-beginIndex.";
    }
}
