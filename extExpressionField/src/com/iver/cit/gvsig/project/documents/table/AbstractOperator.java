package com.iver.cit.gvsig.project.documents.table;

import com.iver.andami.PluginServices;

/**
 * @author Vicente Caballero Navarro
 */
public abstract class AbstractOperator implements IOperator{
	private int type;

	public void setType(int type) {
		this.type=type;
	}
	public int getType() {
		return type;
	}
	public abstract String toString();

	public String getTooltip(){
		return PluginServices.getText(this,"operator")+":  "+addText(PluginServices.getText(this,"parameter"))+"\n"+getDescription();
	}
	public abstract String getDescription();
}
