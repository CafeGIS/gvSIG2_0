package com.iver.cit.gvsig.project.documents.table.operators;

import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;

import com.iver.andami.PluginServices;
/**
 * @author Vicente Caballero Navarro
 */
public class Field extends AbstractField{
	private FeatureAttributeDescriptor fd;
	private String typeField;
	public Field() {
	}
	public void setFieldDescription(FeatureAttributeDescriptor fd) {
		this.fd=fd;
		switch (fd.getDataType()) {
		case DataTypes.INT:
		case DataTypes.LONG:
		case DataTypes.FLOAT:
		case DataTypes.DOUBLE:
			typeField = PluginServices.getText(this, "numeric_value");
			break;
		case DataTypes.STRING:
			typeField=PluginServices.getText(this,"string_value");
			break;
		case DataTypes.BOOLEAN:
			typeField=PluginServices.getText(this,"boolean_value");
			break;
		case DataTypes.DATE:
			typeField=PluginServices.getText(this,"date_value");
			break;
		}

	}
	public String addText(String s) {
		return s.concat(toString());
	}
	public String toString() {
		return "["+fd.getName()+"]";
	}
	public boolean isEnable() {
		return true;
	}
	public String getTooltip(){
		return PluginServices.getText(this,"field")+":  "+fd.getName()+"\n"+getDescription();
	}
	public String getDescription() {
        return PluginServices.getText(this, "type") + ": " +
        typeField;
    }
}
