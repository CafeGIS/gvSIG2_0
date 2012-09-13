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
public class Atan extends AbstractOperator{

	public String addText(String s) {
		return toString()+"("+s+")";
	}
	public String toString() {
		return "atan";
	}
	public void eval(BSFManager interpreter) throws BSFException {
//		interpreter.eval(ExpressionFieldExtension.BEANSHELL,null,-1,-1,"double atan(double value){return java.lang.Math.atan(value);};");
		interpreter.exec(ExpressionFieldExtension.JYTHON,null,-1,-1,"def atan(value):\n" +
				"  import java.lang.Math\n" +
				"  return java.lang.Math.atan(value)");
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
        "Returns the arc tangent of an angle, in the range of -pi/2 through pi/2. Special cases:\n" +
        "* If the argument is NaN, then the result is NaN.\n" +
        "* If the argument is zero, then the result is a zero with the same sign as the argument.\n";
    }
}
