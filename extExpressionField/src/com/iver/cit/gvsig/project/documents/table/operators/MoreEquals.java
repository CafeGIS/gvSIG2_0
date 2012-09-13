package com.iver.cit.gvsig.project.documents.table.operators;

import org.apache.bsf.BSFException;
import org.apache.bsf.BSFManager;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.documents.table.AbstractOperator;
import com.iver.cit.gvsig.project.documents.table.IOperator;

/**
 * @author Vicente Caballero Navarro
 */
public class MoreEquals extends AbstractOperator{

	public String addText(String s) {
		return s.concat(toString());
	}

	public void eval(BSFManager interpreter) throws BSFException {
	}
	public String toString() {
		return ">=";
	}
	public boolean isEnable() {
		return ( getType()==IOperator.NUMBER);
	}
	public String getTooltip(){
		return PluginServices.getText(this,"operator")+":  "+addText("")+"\n"+getDescription();
	}
	public String getDescription() {
        return PluginServices.getText(this, "returns") + ": " +
        PluginServices.getText(this, "boolean_value") + "\n" +
        PluginServices.getText(this, "description") + ": " +
        "Returns true if the first object value is more or equals than the second value.";
    }
}
