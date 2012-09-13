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
public class Exp extends AbstractOperator{

	public String addText(String s) {
		return toString()+"("+s+")";
	}
	public String toString() {
		return "exp";
	}
	public void eval(BSFManager interpreter) throws BSFException {
//		interpreter.eval(ExpressionFieldExtension.BEANSHELL,null,-1,-1,"double exp(double value){return java.lang.Math.exp(value);};");
		interpreter.exec(ExpressionFieldExtension.JYTHON,null,-1,-1,"def exp(value):\n" +
				"  import java.lang.Math\n" +
				"  return java.lang.Math.exp(value)");
	}
	public boolean isEnable() {
		return (getType()==IOperator.NUMBER);
	}
	public String getDescription() {
        return PluginServices.getText(this, "parameter") + ": " +
        PluginServices.getText(this, "numeric_value") + "\n" +
        PluginServices.getText(this, "returns") + ": " +
        PluginServices.getText(this, "numeric_value") + "\n" +
        PluginServices.getText(this, "description") + ": " +
        "Returns Euler's number e raised to the power of a double value. Special cases:\n" +
        "* If the argument is NaN, the result is NaN.\n" +
        "* If the argument is positive infinity, then the result is positive infinity.\n" +
        "* If the argument is negative infinity, then the result is positive zero.";
    }
}
