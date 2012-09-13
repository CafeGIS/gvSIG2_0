package com.iver.cit.gvsig.project.documents.table.operators;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.apache.bsf.BSFException;
import org.apache.bsf.BSFManager;
import org.cresques.cts.IProjection;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.geom.Geometry;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.ExpressionFieldExtension;
import com.iver.cit.gvsig.project.documents.table.GraphicOperator;
import com.iver.cit.gvsig.project.documents.table.IOperator;
import com.vividsolutions.jts.algorithm.CGAlgorithms;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateList;
/**
 * @author Vicente Caballero Navarro
 */
public class Area extends GraphicOperator{

	public String addText(String s) {
		return s.concat(toString()+"()");
	}
	public double process(org.gvsig.fmap.dal.feature.Feature feature){
//		ReadableVectorial adapter = getLayer().getSource();
	   	org.gvsig.fmap.geom.Geometry geom=null;
//		try {
			geom = feature.getDefaultGeometry();//adapter.getShape(index.get());
//		} catch (ExpansionFileReadException e) {
//			throw new DriverIOException(e);
//		} catch (ReadDriverException e) {
//			throw new DriverIOException(e);
//		}
//	   	int distanceUnits=getLayer().getMapContext().getViewPort().getDistanceArea();
		return returnArea(geom);///Math.pow(MapContext.getAreaTrans2Meter()[distanceUnits],2);
	}
	public void eval(BSFManager interpreter) throws BSFException {
		interpreter.declareBean("jarea",this,Area.class);
//		interpreter.eval(ExpressionFieldExtension.BEANSHELL,null,-1,-1,"double area(){return area.process(indexRow);};");
		interpreter.exec(ExpressionFieldExtension.JYTHON,null,-1,-1,"def area():\n" +
				"  return jarea.process(featureContainer.getFeature())");
	}
	public String toString() {
		return "area";
	}
	public boolean isEnable() {
		if (getLayer()==null)
			return false;
		FeatureStore store;
		int geomType=Geometry.TYPES.POINT;
		try {
			store = getLayer().getFeatureStore();
			geomType = store.getDefaultFeatureType().getAttributeDescriptor(store.getDefaultFeatureType().getDefaultGeometryAttributeIndex()).getGeometryType();
		} catch (DataException e) {
			NotificationManager.addError(e);
		}
		return (getType()==IOperator.NUMBER && geomType==Geometry.TYPES.SURFACE);
	}

	private double returnArea(org.gvsig.fmap.geom.Geometry geom) {
		ArrayList parts=getXY(geom);
		double area=0;
		for (int i=0;i<parts.size();i++){
			Double[][] xsys=(Double[][])parts.get(i);//getXY(geom);
			Double[] xs=xsys[0];
			Double[] ys=xsys[1];
			IProjection proj=getLayer().getMapContext().getProjection();
			if (isCCW(xs, ys)){
				if (proj.isProjected()) {
					area-= returnCoordsArea(xs,ys,new Point2D.Double(xs[xs.length-1].doubleValue(),ys[ys.length-1].doubleValue()));
				}else{
					area-= returnGeoCArea(xs,ys);
				}
			}else{
				if (proj.isProjected()) {
					area+= returnCoordsArea(xs,ys,new Point2D.Double(xs[xs.length-1].doubleValue(),ys[ys.length-1].doubleValue()));
				}else{
					area+= returnGeoCArea(xs,ys);
				}
			}
		}
		return area;
	}

	public boolean isCCW(Double[] xs, Double[] ys){
		CoordinateList coordList = new CoordinateList();
		for (int i = 0; i < ys.length; i++) {
    	   Coordinate coord=new Coordinate(xs[i].doubleValue(),ys[i].doubleValue());
    	   coordList.add(coord);
		}
		if (coordList.isEmpty())
			return true;
		return CGAlgorithms.isCCW(coordList.toCoordinateArray());
	}


	private double returnGeoCArea(Double[] xs,Double[] ys) {
		double[] lat=new double[xs.length];
		double[] lon=new double[xs.length];
		for (int K= 0; K < xs.length; K++){
			lon[K]= xs[K].doubleValue()/org.gvsig.fmap.mapcontrol.tools.geo.Geo.Degree;
			lat[K]= ys[K].doubleValue()/org.gvsig.fmap.mapcontrol.tools.geo.Geo.Degree;
		}
		return (org.gvsig.fmap.mapcontrol.tools.geo.Geo.sphericalPolyArea(lat,lon,xs.length-1)*org.gvsig.fmap.mapcontrol.tools.geo.Geo.SqM);
	}
	/**
	 * Calcula el área.
	 *
	 * @param aux último punto.
	 *
	 * @return Área.
	 */
	public double returnCoordsArea(Double[] xs,Double[] ys, Point2D point) {
		Point2D aux=point;
		double elArea = 0.0;
		Point2D pPixel;
		Point2D p = new Point2D.Double();
		Point2D.Double pAnt = new Point2D.Double();
		org.gvsig.fmap.mapcontext.ViewPort vp = getLayer().getMapContext().getViewPort();
		for (int pos = 0; pos < xs.length-1; pos++) {
			pPixel = new Point2D.Double(xs[pos].doubleValue(),
					ys[pos].doubleValue());
			p = pPixel;
			if (pos == 0) {
				pAnt.x = aux.getX();
				pAnt.y = aux.getY();
			}
			elArea = elArea + ((pAnt.x - p.getX()) * (pAnt.y + p.getY()));
			pAnt.setLocation(p);
		}

		elArea = elArea + ((pAnt.x - aux.getX()) * (pAnt.y + aux.getY()));
		elArea = Math.abs(elArea / 2.0);
		return (elArea/(Math.pow(org.gvsig.fmap.mapcontext.MapContext.getAreaTrans2Meter()[vp.getDistanceArea()],2)));
	}
	public String getTooltip(){
		return PluginServices.getText(this,"operator")+":  "+addText("")+"\n"+getDescription();
	}
	public String getDescription() {
        return PluginServices.getText(this, "returns") + ": " +
        PluginServices.getText(this, "numeric_value") + "\n" +
        PluginServices.getText(this, "description") + ": " +
        "Returns the area of polygon geometry of this row.";
    }
}
