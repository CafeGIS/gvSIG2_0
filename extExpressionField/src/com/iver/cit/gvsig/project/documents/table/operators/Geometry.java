package com.iver.cit.gvsig.project.documents.table.operators;

import org.apache.bsf.BSFException;
import org.apache.bsf.BSFManager;
import org.gvsig.fmap.dal.feature.Feature;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.ExpressionFieldExtension;
import com.iver.cit.gvsig.project.documents.table.GraphicOperator;
/**
 * @author Vicente Caballero Navarro
 */
public class Geometry extends GraphicOperator{

	public String addText(String s) {
		return s.concat(toString()+"()");
	}
	public double process(Feature feature) {
		return 0;
	}
	public org.gvsig.fmap.geom.Geometry getGeometry(Feature feature) {
//		ReadableVectorial adapter = getLayer().getSource();
	   	org.gvsig.fmap.geom.Geometry geom=feature.getDefaultGeometry();//adapter.getShape(index.get());
	   	return geom;
	}
	public void eval(BSFManager interpreter) throws BSFException {
		interpreter.declareBean("jgeometry",this,Geometry.class);
//		interpreter.eval(ExpressionFieldExtension.BEANSHELL,null,-1,-1,"java.lang.Object geometry(){return geometry.getGeometry(indexRow);};");
		interpreter.exec(ExpressionFieldExtension.JYTHON,null,-1,-1,"def geometry():\n" +
				"  return jgeometry.getGeometry(featureContainer.getFeature())");
	}
	public String toString() {
		return "geometry";
	}
	public boolean isEnable() {
		return false;
	}
	public String getTooltip(){
		return PluginServices.getText(this,"operator")+":  "+addText("")+"\n"+getDescription();
	}
	public String getDescription() {
        return PluginServices.getText(this, "returns") + ": " +
        PluginServices.getText(this, "numeric_value") + "\n" +
        PluginServices.getText(this, "description") + ": " +
        "Returns the geometry of this row.";
    }
}
