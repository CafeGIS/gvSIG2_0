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
public class Before extends AbstractOperator{

	public String addText(String s) {
		return toString()+"("+s+",)";
	}
	public String toString() {
		return "before";
	}
	public void eval(BSFManager interpreter) throws BSFException {
//		interpreter.eval(ExpressionFieldExtension.BEANSHELL,null,-1,-1,"boolean before(java.lang.Object value1,java.lang.Object value2){return value1.before(value2);};");
		interpreter.exec(ExpressionFieldExtension.JYTHON,null,-1,-1,"def before(value1, value2):\n" +
				"  return value1.before(value2)");
	}
	public boolean isEnable() {
		return (getType()==IOperator.DATE);
	}
	public String getTooltip(){
		return PluginServices.getText(this,"operator")+":  "+toString()+"("+PluginServices.getText(this,"parameter")+"1,"+PluginServices.getText(this,"parameter")+"2"+")"+"\n"+getDescription();
	}
	public String getDescription() {
        return PluginServices.getText(this, "parameter") + "1"+": " +
        PluginServices.getText(this, "date_value") + "\n"+
        PluginServices.getText(this, "parameter") + "2"+": " +
        PluginServices.getText(this, "date_value") + "\n"+
        PluginServices.getText(this, "returns") + ": " +
        PluginServices.getText(this, "boolean_value") + "\n" +
        PluginServices.getText(this, "description") + ": " +
        "Tests if parameter1 date is before the parameter2 date.";
    }

}
