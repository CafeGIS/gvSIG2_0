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
public class IndexOf extends AbstractOperator{

	public String addText(String s) {
		return toString()+"("+s+", \"\")";
	}
	public String toString() {
		return "indexOf";
	}
	public void eval(BSFManager interpreter) throws BSFException {
//		interpreter.eval(ExpressionFieldExtension.BEANSHELL,null,-1,-1,"int indexOf(String value1,String value2){return value1.indexOf(value2);};");
		interpreter.exec(ExpressionFieldExtension.JYTHON,null,-1,-1,"def indexOf(value1,value2):\n" +
				"  return value1.index(value2)");
	}
	public boolean isEnable() {
		return (getType()==IOperator.STRING);
	}
	public String getTooltip(){
		return PluginServices.getText(this,"operator")+":  "+toString()+"("+PluginServices.getText(this,"parameter")+"1,"+PluginServices.getText(this,"parameter")+"2"+")"+"\n"+getDescription();
	}
	public String getDescription() {
        return PluginServices.getText(this, "parameter") + "1"+": " +
        PluginServices.getText(this, "string_value") + "\n"+
        PluginServices.getText(this, "parameter") + "2"+": " +
        PluginServices.getText(this, "string_value") + "\n"+
        PluginServices.getText(this, "returns") + ": " +
        PluginServices.getText(this, "integer_value") + "\n" +
        PluginServices.getText(this, "description") + ": " +
        "Returns the index within parameter1 of the first occurrence of the parameter2.";
    }
}
