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
public class Random extends AbstractOperator{

	public String addText(String s) {
		return s.concat(toString()+"()");
	}
	public String toString() {
		return "random";
	}
	public void eval(BSFManager interpreter) throws BSFException {
//		interpreter.eval(ExpressionFieldExtension.BEANSHELL,null,-1,-1,"double random(){return java.lang.Math.random();};");
		interpreter.exec(ExpressionFieldExtension.JYTHON,null,-1,-1,"def random():\n" +
				"  import java.lang.Math\n" +
				"  return java.lang.Math.random()");
	}
	public boolean isEnable() {
		return (getType()==IOperator.NUMBER);
	}
	public String getTooltip(){
		return PluginServices.getText(this,"operator")+":  "+addText("")+"\n"+getDescription();
	}
	public String getDescription() {
        return PluginServices.getText(this, "returns") + ": " +
        PluginServices.getText(this, "numeric_value") + "\n" +
        PluginServices.getText(this, "description") + ": " +
        "Returns a double value with a positive sign, greater than or equal to 0.0 and less than 1.0. Returned values are chosen pseudorandomly with (approximately) uniform distribution from that range.";
    }
}
