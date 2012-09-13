package com.iver.cit.gvsig.project.documents.table.operators;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.apache.bsf.BSFException;
import org.apache.bsf.BSFManager;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.ViewPort;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.ExpressionFieldExtension;
import com.iver.cit.gvsig.project.documents.table.GraphicOperator;
import com.iver.cit.gvsig.project.documents.table.IOperator;
/**
 * @author Vicente Caballero Navarro
 */
public class Perimeter extends GraphicOperator{

	public String addText(String s) {
		return s.concat(toString()+"()");
	}
	public double process(Feature feature) {
//		ReadableVectorial adapter = getLayer().getSource();
	   	org.gvsig.fmap.geom.Geometry geom=null;
			geom = feature.getDefaultGeometry();//adapter.getShape(index.get());
		ArrayList parts=getXY(geom);
	   	double perimeter=0;
	   	for (int j=0;j<parts.size();j++){
	   	Double[][] xsys=(Double[][])parts.get(j);//getXY(geom);
	    double dist = 0;
        double distAll = 0;

        ViewPort vp = getLayer().getMapContext().getViewPort();
        for (int i = 0; i < (xsys[0].length - 1); i++) {
            dist = 0;

            Point2D p = new Point2D.Double(xsys[0][i].doubleValue(), xsys[1][i].doubleValue());//vp.toMapPoint(new Point(event.getXs()[i].intValue(), event.getYs()[i].intValue()));
            Point2D p2 = new Point2D.Double(xsys[0][i + 1].doubleValue(), xsys[1][i + 1].doubleValue());//vp.toMapPoint(new Point(event.getXs()[i + 1].intValue(), event.getYs()[i + 1].intValue()));
            dist = vp.distanceWorld(p,p2);
            //System.out.println("distancia parcial = "+dist);
            distAll += dist;
        }
        int distanceUnits=vp.getDistanceUnits();
		perimeter+= distAll/MapContext.getDistanceTrans2Meter()[distanceUnits];
	   	}
	   	return perimeter;
	}
	public void eval(BSFManager interpreter) throws BSFException {
		interpreter.declareBean("jperimeter",this,Perimeter.class);
//		interpreter.eval(ExpressionFieldExtension.BEANSHELL,null,-1,-1,"double perimeter(){return jperimeter.process(indexRow);};");
		interpreter.exec(ExpressionFieldExtension.JYTHON,null,-1,-1,"def perimeter():\n" +
				"  return jperimeter.process(featureContainer.getFeature())");
	}
	public String toString() {
		return "perimeter";
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
		return (getType()==IOperator.NUMBER && (geomType==org.gvsig.fmap.geom.Geometry.TYPES.SURFACE || geomType==org.gvsig.fmap.geom.Geometry.TYPES.CURVE));
	}
	public String getTooltip(){
		return PluginServices.getText(this,"operator")+":  "+addText("")+"\n"+getDescription();
	}
	public String getDescription() {
        return PluginServices.getText(this, "returns") + ": " +
        PluginServices.getText(this, "numeric_value") + "\n" +
        PluginServices.getText(this, "description") + ": " +
        "Returns the perimeter of polygon or line geometry  of this row.";
    }
}
