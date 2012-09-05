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

import org.gvsig.dwg.lib.DwgObject;
import org.gvsig.dwg.lib.IDwg2FMap;
import org.gvsig.dwg.lib.IDwg3DTestable;
import org.gvsig.dwg.lib.util.AcadExtrusionCalculator;
import org.gvsig.dwg.lib.util.FMapUtil;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.primitive.Point;


/**
 * The DwgPoint class represents a DWG Point
 *
 * @author jmorell
 */
public class DwgPoint extends DwgObject
		implements/* IDwgExtrusionable,*/ IDwg3DTestable, IDwg2FMap{
	public DwgPoint(int index) {
		super(index);
		// TODO Auto-generated constructor stub
	}
	private double[] point;
	private double thickness;
	private double[] extrusion;
	private double xAxisAngle;

	/**
	 * @return Returns the point.
	 */
	public double[] getPoint() {
		return point;
	}
	/**
	 * @param point The point to set.
	 */
	public void setPoint(double[] point) {
		this.point = point;
	}
	/**
	 * @return Returns the extrusion.
	 */
	public double[] getExtrusion() {
		return extrusion;
	}
	/**
	 * @param extrusion The extrusion to set.
	 */
	public void setExtrusion(double[] extrusion) {
		this.extrusion = extrusion;
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
	 * @return Returns the xAxisAngle.
	 */
	public double getXAxisAngle() {
		return xAxisAngle;
	}
	/**
	 * @param axisAngle The xAxisAngle to set.
	 */
	public void setXAxisAngle(double axisAngle) {
		xAxisAngle = axisAngle;
	}

	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.IDwgExtrusionable#applyExtrussion()
	 */
	public void applyExtrussion() {
		 double[] point = getPoint();
         double[] pointExt = getExtrusion();
         point = AcadExtrusionCalculator.extrude2(point, pointExt);
         setPoint(point);
	}
	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.IDwg3DTestable#has3DData()
	 */
	public boolean has3DData() {
		return getPoint()[2] != 0.0;
	}
	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.IDwg3DTestable#getZ()
	 */
	public double getZ() {
		return getPoint()[2];
	}
	public Geometry toFMapGeometry(boolean is3DFile) throws CreateGeometryException {
		GeometryManager gMan = GeometryLocator.getGeometryManager();
		double[] p = getPoint();
		Point point = (Point) gMan.create(getGeometryType(),
				getGeometrySubType(is3DFile));
		point.setCoordinates(p);
		return point;
	}

	public String toFMapString(boolean is3DFile) {
		if(is3DFile) {
			return "FPoint3D";
		} else {
			return "FPoint2D";
		}
	}

	public String toString(){
		return "Point";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone(){
		DwgPoint obj = new DwgPoint(index);
		this.fill(obj);
		return obj;
	}

	protected void fill(DwgObject obj){
		super.fill(obj);
		DwgPoint myObj = (DwgPoint)obj;

		myObj.setExtrusion(extrusion);
		myObj.setPoint(point);
		myObj.setThickness(thickness);
		myObj.setXAxisAngle(xAxisAngle);
	}
	public int getGeometrySubType(boolean is3DFile) {
		if (is3DFile) {
			return FMapUtil.SUBTYPE_GEOM3D;
		} else {
			return FMapUtil.SUBTYPE_GEOM2D;
		}
	}

	public int getGeometryType() {
		return Geometry.TYPES.POINT;
	}

}
