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
public class Pow extends AbstractOperator{

	public String addText(String s) {
		return toString()+"("+s+", )";
	}

	public void eval(BSFManager interpreter) throws BSFException {
//		interpreter.eval(ExpressionFieldExtension.BEANSHELL,null,-1,-1,"double pow(double value1,double value2){return java.lang.Math.pow(value1,value2);};");
		interpreter.exec(ExpressionFieldExtension.JYTHON,null,-1,-1,"def pow(value1, value2):\n" +
				"  import java.lang.Math\n" +
				"  return java.lang.Math.pow(value1,value2)");
	}
	public String toString() {
		return "pow";
	}
	public boolean isEnable() {
		return (getType()==IOperator.NUMBER);
	}
	public String getTooltip(){
		return PluginServices.getText(this,"operator")+":  "+ toString()+ "(" +PluginServices.getText(this,"parameter")+"1,"+PluginServices.getText(this,"parameter")+"2"+")\n"+getDescription();
	}
	public String getDescription() {
        return PluginServices.getText(this, "parameter") + "1"+": " +
        PluginServices.getText(this, "numeric_value") + "\n"+
        PluginServices.getText(this, "parameter") + "2"+": " +
        PluginServices.getText(this, "numeric_value") + "\n"+
        PluginServices.getText(this, "returns") + ": " +
        PluginServices.getText(this, "numeric_value") + "\n" +
        PluginServices.getText(this, "description") + ": " +
        "Returns the value of the first argument raised to the power of the second argument. Special cases:\n" +
        "* If the second argument is positive or negative zero, then the result is 1.0.\n" +
        "* If the second argument is 1.0, then the result is the same as the first argument.\n" +
        "* If the second argument is NaN, then the result is NaN.\n" +
        "* If the first argument is NaN and the second argument is nonzero, then the result is NaN.\n" +
        "* If\n" +
        "   - the absolute value of the first argument is greater than 1 and the second argument is positive infinity, or\n" +
        "   - the absolute value of the first argument is less than 1 and the second argument is negative infinity,\n" +
        "then the result is positive infinity.\n" +
        "* If\n" +
        "   - the absolute value of the first argument is greater than 1 and the second argument is negative infinity, or\n" +
        "   - the absolute value of the first argument is less than 1 and the second argument is positive infinity,\n" +
        "then the result is positive zero.\n" +
        "* If the absolute value of the first argument equals 1 and the second argument is infinite, then the result is NaN.\n" +
        "* If\n" +
        "   - the first argument is positive zero and the second argument is greater than zero, or\n" +
        "   - the first argument is positive infinity and the second argument is less than zero,\n" +
        "then the result is positive zero.\n" +
        "* If\n" +
        "   - the first argument is positive zero and the second argument is less than zero, or\n" +
        "   - the first argument is positive infinity and the second argument is greater than zero,\n" +
        "then the result is positive infinity.\n" +
        "* If\n" +
        "   - the first argument is negative zero and the second argument is greater than zero but not a finite odd integer, or\n" +
        "   - the first argument is negative infinity and the second argument is less than zero but not a finite odd integer,\n" +
        "then the result is positive zero.\n" +
        "* If\n" +
        "   - the first argument is negative zero and the second argument is a positive finite odd integer, or\n" +
        "   - the first argument is negative infinity and the second argument is a negative finite odd integer,\n" +
        "then the result is negative zero.\n" +
        "* If\n" +
        "   - the first argument is negative zero and the second argument is less than zero but not a finite odd integer, or\n" +
        "   - the first argument is negative infinity and the second argument is greater than zero but not a finite odd integer,\n" +
        "then the result is positive infinity.\n" +
        "* If\n" +
        "   - the first argument is negative zero and the second argument is a negative finite odd integer, or\n" +
        "   - the first argument is negative infinity and the second argument is a positive finite odd integer,\n" +
        "then the result is negative infinity.\n" +
        "* If the first argument is finite and less than zero\n" +
        "   - if the second argument is a finite even integer, the result is equal to the result of raising the absolute value of the first argument to the power of the second argument\n" +
        "   - if the second argument is a finite odd integer, the result is equal to the negative of the result of raising the absolute value of the first argument to the power of the second argument\n" +
        "   - if the second argument is finite and not an integer, then the result is NaN.\n" +
        "* If both arguments are integers, then the result is exactly equal to the mathematical result of raising the first argument to the power of the second argument if that result can in fact be represented exactly as a double value.";
    }
}
