/* jdwglib. Java Library for reading Dwg files.
 *
 * Author: Jose Morell Rama (jose.morell@gmail.com).
 * Port from the Pythoncad Dwg library by Art Haas.
 *
 * Copyright (C) 2005 Jose Morell, IVER TI S.A. and Generalitat Valenciana
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 *
 * For more information, contact:
 *
 * Jose Morell (jose.morell@gmail.com)
 *
 * or
 *
 * IVER TI S.A.
 *  C/Salamanca, 50
 *  46005 Valencia
 *  Spain
 *  +34 963163400
 *  dac@iver.es
 */
package org.gvsig.dwg.lib.objects;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Map;

import org.gvsig.dwg.lib.DwgFile;
import org.gvsig.dwg.lib.DwgObject;
import org.gvsig.dwg.lib.IDwg2FMap;
import org.gvsig.dwg.lib.IDwg3DTestable;
import org.gvsig.dwg.lib.IDwgBlockMember;
import org.gvsig.dwg.lib.IDwgExtrusionable;
import org.gvsig.dwg.lib.util.AcadExtrusionCalculator;
import org.gvsig.dwg.lib.util.FMapUtil;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.primitive.Arc;
import org.gvsig.fmap.geom.primitive.Point;
import org.gvsig.fmap.geom.util.UtilFunctions;


/**
 * The DwgArc class represents a DWG Arc
 *
 * @author jmorell
 */
public class DwgArc extends DwgObject
	implements IDwgExtrusionable, IDwg3DTestable, IDwg2FMap, IDwgBlockMember {

	private double[] center;
	private double radius;
	private double thickness;
	private double[] extrusion;
	private double initAngle;
	private double endAngle;

	public DwgArc(int index) {
		super(index);
	}


	/**
	 * @return Returns the center.
	 */
	public double[] getCenter() {
		return center;
	}
	/**
	 * @param center The center to set.
	 */
	public void setCenter(double[] center) {
		this.center = center;
	}
	/**
	 * @return Returns the endAngle.
	 */
	public double getEndAngle() {
		return endAngle;
	}
	/**
	 * @param endAngle The endAngle to set.
	 */
	public void setEndAngle(double endAngle) {
		this.endAngle = endAngle;
	}
	/**
	 * @return Returns the initAngle.
	 */
	public double getInitAngle() {
		return initAngle;
	}
	/**
	 * @param initAngle The initAngle to set.
	 */
	public void setInitAngle(double initAngle) {
		this.initAngle = initAngle;
	}
	/**
	 * @return Returns the radius.
	 */
	public double getRadius() {
		return radius;
	}
	/**
	 * @param radius The radius to set.
	 */
	public void setRadius(double radius) {
		this.radius = radius;
	}
    /**
     * @return Returns the extrusion.
     */
    public double[] getExtrusion() {
        return extrusion;
    }

	/**
	 * @return Returns the thickness.
	 */
	public double getThickness() {
		return thickness;
	}
	/**
	 * @param thickness The thickness to set.
	 */
	public void setThickness(double thickness) {
		this.thickness = thickness;
	}
	/**
	 * @param extrusion The extrusion to set.
	 */
	public void setExtrusion(double[] extrusion) {
		this.extrusion = extrusion;
	}

	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.IDwgExtrusionable#applyExtrussion()
	 */
	public void applyExtrussion() {
		 double[] arcCenter = getCenter();
         double[] arcExt = getExtrusion();
         arcCenter = AcadExtrusionCalculator.
		 	extrude2(arcCenter, arcExt);
         setCenter(arcCenter);

	}

	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.IDwg3DTestable#has3DData()
	 */
	public boolean has3DData() {
		return (getCenter()[2] != 0.0);
	}

	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.IDwg2FMap#toFMapGeometry()
	 */
	public Geometry toFMapGeometry(boolean is3DFile) throws CreateGeometryException {

		GeometryManager gMan = GeometryLocator.getGeometryManager();

		double[] center = getCenter();
		//FIXME: ¿qué hacer cuando llega un radio negativo?
		//	De momento, tomamos el valor absoluto del radio, pero
		//	¿habría que modificar de alguna manera los angulos?
		double radius = Math.abs(getRadius());
		double angSt = getInitAngle();
		double angEnd = getEndAngle();
		double angExt = UtilFunctions.angleDistance(angSt, angEnd);
		Arc arc = (Arc) gMan.create(getGeometryType(),getGeometrySubType(is3DFile));
		Point point = (Point) gMan.create(Geometry.TYPES.POINT,getGeometrySubType(is3DFile));
		point.setCoordinates(center);
		arc.setPoints(point, radius, angSt, -angExt);
		return arc;
	}

	public String toString(){
		return "Arc";
	}

	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.IDwg2FMap#toFMapString()
	 */
	public String toFMapString(boolean is3dFile) {
		if(is3dFile) {
			return "FPolyline3D";
		} else {
			return "FPolyline2D";
		}
	}

	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.IDwg3DTestable#getZ()
	 */
	public double getZ() {
		return getCenter()[2];
	}

	public void transform2Block(double[] bPoint, Point2D insPoint, double[] scale, double rot,
			List dwgObjectsWithoutBlocks, Map handleObjWithoutBlocks, DwgFile callBack) {
		//DwgArc transformedEntity = new DwgArc(getIndex());
		double[] center = this.getCenter();
		Point2D pointAux = new Point2D.Double(center[0] - bPoint[0], center[1] - bPoint[1]);
		double laX = insPoint.getX() + ((pointAux.getX()*scale[0])*Math.cos(rot) + (pointAux.getY()*scale[1])*(-1)*Math.sin(rot));
		double laY = insPoint.getY() + ((pointAux.getX()*scale[0])*Math.sin(rot) + (pointAux.getY()*scale[1])*Math.cos(rot));
		double laZ = center[2] * scale[2];
		double[] transformedCenter = new double[]{laX, laY, laZ};
		double radius = this.getRadius();
		double transformedRadius = radius * scale[0];
		double initAngle = this.getInitAngle();
		double endAngle = this.getEndAngle();
		double transformedInitAngle = initAngle + rot;
		if (transformedInitAngle<0) {
		    transformedInitAngle = transformedInitAngle + (2*Math.PI);
		} else if (transformedInitAngle>(2*Math.PI)) {
		    transformedInitAngle = transformedInitAngle - (2*Math.PI);
		}
		double transformedEndAngle = endAngle + rot;
		if (transformedEndAngle<0) {
		    transformedEndAngle = transformedEndAngle + (2*Math.PI);
		} else if (transformedEndAngle>(2*Math.PI)) {
		    transformedEndAngle = transformedEndAngle - (2*Math.PI);
		}
		DwgArc transformedEntity = (DwgArc) this.clone();
		transformedEntity.setCenter(transformedCenter);
		transformedEntity.setRadius(transformedRadius);
		transformedEntity.setInitAngle(transformedInitAngle);
		transformedEntity.setEndAngle(transformedEndAngle);
		transformedEntity.setHandle(this.getHandle());

		dwgObjectsWithoutBlocks.add(transformedEntity);
		handleObjWithoutBlocks.put(new Integer(transformedEntity.getHandle().getOffset()), transformedEntity);

//		setCenter(transformedCenter);
//		setRadius(transformedRadius);
//		setInitAngle(transformedInitAngle);
//		setEndAngle(transformedEndAngle);
//		dwgObjectsWithoutBlocks.add(this);
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
public Object clone(){
		DwgArc obj = new DwgArc(index);
		this.fill(obj);
		return obj;
	}

	protected void fill(DwgObject obj){
		super.fill(obj);
		DwgArc myObj = (DwgArc)obj;

		myObj.setCenter(center);
		myObj.setEndAngle(endAngle);
		myObj.setExtrusion(extrusion);
		myObj.setInitAngle(initAngle);
		myObj.setRadius(radius);
		myObj.setThickness(thickness);

	}


	public int getGeometrySubType(boolean is3DFile) {
		if (is3DFile) {
			return FMapUtil.SUBTYPE_GEOM3D;
		} else {
			return FMapUtil.SUBTYPE_GEOM2D;
		}
	}

	public int getGeometryType() {
		return Geometry.TYPES.ARC;
	}

}
