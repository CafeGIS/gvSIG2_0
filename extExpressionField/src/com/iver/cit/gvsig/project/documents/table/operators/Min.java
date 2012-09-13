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
public class Min extends AbstractOperator{

	public String addText(String s) {
		return toString()+"("+s+")";
	}
	public String toString() {
		return "min";
	}
	public void eval(BSFManager interpreter) throws BSFException {
//		interpreter.eval(ExpressionFieldExtension.BEANSHELL,null,-1,-1,"double min(double value1,double value2){return java.lang.Math.min(value1,value2);};");
		interpreter.exec(ExpressionFieldExtension.JYTHON,null,-1,-1,"def min(value1, value2):\n" +
				"  import java.lang.Math\n" +
				"  return java.lang.Math.min(value1,value2)");
	}
	public boolean isEnable() {
		return (getType()==IOperator.NUMBER);
	}
	public String getTooltip(){
		return PluginServices.getText(this,"operator")+":  "+toString()+ "("+ PluginServices.getText(this,"parameter")+"1,"+PluginServices.getText(this,"parameter")+"2"+")\n"+getDescription();
	}
	public String getDescription() {
        return PluginServices.getText(this, "parameter") + "1"+": " +
        PluginServices.getText(this, "numeric_value") + "\n"+
        PluginServices.getText(this, "parameter") + "2"+": " +
        PluginServices.getText(this, "numeric_value") + "\n"+
        PluginServices.getText(this, "returns") + ": " +
        PluginServices.getText(this, "numeric_value") + "\n" +
        PluginServices.getText(this, "description") + ": " +
        "Returns the smaller of two int values. That is, the result the argument closer to the value of Integer.MIN_VALUE.\n" +
        "If the arguments have the same value, the result is that same value.";
    }
}
