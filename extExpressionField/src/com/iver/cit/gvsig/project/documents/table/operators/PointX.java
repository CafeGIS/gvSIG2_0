package com.iver.cit.gvsig.project.documents.table.operators;

import java.util.ArrayList;

import org.apache.bsf.BSFException;
import org.apache.bsf.BSFManager;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureStore;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.ExpressionFieldExtension;
import com.iver.cit.gvsig.project.documents.table.GraphicOperator;
import com.iver.cit.gvsig.project.documents.table.IOperator;
/**
 * @author Vicente Caballero Navarro
 */
public class PointX extends GraphicOperator{

	public String addText(String s) {
		return s.concat(toString()+"()");
	}
	public double process(Feature feature){
//		ReadableVectorial adapter = getLayer().getSource();
		org.gvsig.fmap.geom.Geometry geom=null;
//		try {
			geom = feature.getDefaultGeometry();//adapter.getShape(index.get());
//		} catch (ExpansionFileReadException e) {
//			throw new DriverIOException(e);
//		} catch (ReadDriverException e) {
//			throw new DriverIOException(e);
//		}
		ArrayList parts=getXY(geom);
		Double[][] xsys=(Double[][])parts.get(0);//getXY(geom);
		return xsys[0][0].doubleValue();
	}
	public void eval(BSFManager interpreter) throws BSFException {
		interpreter.declareBean("pointX",this,PointX.class);
//		interpreter.eval(ExpressionFieldExtension.BEANSHELL,null,-1,-1,"double x(){return pointX.process(indexRow);};");
		interpreter.exec(ExpressionFieldExtension.JYTHON,null,-1,-1,"def x():\n" +
				"  return pointX.process(featureContainer.getFeature())");
	}
	public String toString() {
		return "x";
	}
	public boolean isEnable() {
		if (getLayer()==null)
			return false;
		int geomType=org.gvsig.fmap.geom.Geometry.TYPES.POINT;
		try {
			FeatureStore store = getLayer().getFeatureStore();
			geomType = store.getDefaultFeatureType().getAttributeDescriptor(store.getDefaultFeatureType().getDefaultGeometryAttributeIndex()).getGeometryType();
		} catch (DataException e) {
			NotificationManager.addError(e);
		}
		return (getType()==IOperator.NUMBER && geomType==org.gvsig.fmap.geom.Geometry.TYPES.POINT);
	}
	public String getTooltip(){
		return PluginServices.getText(this,"operator")+":  "+addText("")+"\n"+getDescription();
	}
	public String getDescription() {
        return PluginServices.getText(this, "returns") + ": " +
        PluginServices.getText(this, "numeric_value") + "\n" +
        PluginServices.getText(this, "description") + ": " +
        "Returns the X coordenate of point geometry of this row.";
    }
}
