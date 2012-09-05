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
import java.util.ArrayList;
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
import org.gvsig.fmap.geom.exception.CreateGeometryException;

/**
 * The DwgSolid class represents a DWG Solid
 *
 * @author jmorell, azabala
 */
public class DwgSolid extends DwgObject
	implements IDwgExtrusionable,
	IDwg3DTestable, IDwg2FMap, IDwgBlockMember{


	public DwgSolid(int index) {
		super(index);
	}
	private double thickness;
	private double elevation;
	private double[] corner1;
	private double[] corner2;
	private double[] corner3;
	private double[] corner4;
	private double[] extrusion;

	/**
	 * Read a Solid in the DWG format Version 15
	 *
	 * @param data Array of unsigned bytes obtained from the DWG binary file
	 * @param offset The current bit offset where the value begins
	 * @throws Exception If an unexpected bit value is found in the DWG file. Occurs
	 * 		   when we are looking for LwPolylines.
	 */
	public void readDwgSolidV15(int[] data, int offset) throws Exception {

	}
	/**
	 * @return Returns the corner1.
	 */
	public double[] getCorner1() {
		return corner1;
	}
	/**
	 * @param corner1 The corner1 to set.
	 */
	public void setCorner1(double[] corner1) {
		this.corner1 = corner1;
	}
	/**
	 * @return Returns the corner2.
	 */
	public double[] getCorner2() {
		return corner2;
	}
	/**
	 * @param corner2 The corner2 to set.
	 */
	public void setCorner2(double[] corner2) {
		this.corner2 = corner2;
	}
	/**
	 * @return Returns the corner3.
	 */
	public double[] getCorner3() {
		return corner3;
	}
	/**
	 * @param corner3 The corner3 to set.
	 */
	public void setCorner3(double[] corner3) {
		this.corner3 = corner3;
	}
	/**
	 * @return Returns the corner4.
	 */
	public double[] getCorner4() {
		return corner4;
	}
	/**
	 * @param corner4 The corner4 to set.
	 */
	public void setCorner4(double[] corner4) {
		this.corner4 = corner4;
	}
    /**
     * @return Returns the elevation.
     */
    public double getElevation() {
        return elevation;
    }
    /**
     * @param elevation The elevation to set.
     */
    public void setElevation(double elevation) {
        this.elevation = elevation;
    }
    /**
     * @return Returns the extrusion.
     */
    public double[] getExtrusion() {
        return extrusion;
    }
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
//	public Object clone() {
//		DwgSolid dwgSolid = new DwgSolid(index);
//		dwgSolid.setType(type);
//		dwgSolid.setHandle(handle);
//		dwgSolid.setVersion(version);
//		dwgSolid.setMode(mode);
//		dwgSolid.setLayerHandle(layerHandle);
//		dwgSolid.setColor(color);
//		dwgSolid.setNumReactors(numReactors);
//		dwgSolid.setNoLinks(noLinks);
//		dwgSolid.setLinetypeFlags(linetypeFlags);
//		dwgSolid.setPlotstyleFlags(plotstyleFlags);
//		dwgSolid.setSizeInBits(sizeInBits);
//		dwgSolid.setExtendedData(extendedData);
//		dwgSolid.setGraphicData(graphicData);
//		//dwgSolid.setInsideBlock(insideBlock);
//		dwgSolid.setThickness(thickness);
//		dwgSolid.setElevation(elevation);
//		dwgSolid.setCorner1(corner1);
//		dwgSolid.setCorner2(corner2);
//		dwgSolid.setCorner3(corner3);
//		dwgSolid.setCorner4(corner4);
//		dwgSolid.setExtrusion(extrusion);
//		return dwgSolid;
//	}
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
		 double[] corner1 = getCorner1();
         double[] corner2 = getCorner2();
         double[] corner3 = getCorner3();
         double[] corner4 = getCorner4();
         double[] solidExt = getExtrusion();
         corner1 = AcadExtrusionCalculator.extrude2(corner1, solidExt);
         corner2 = AcadExtrusionCalculator.extrude2(corner2, solidExt);
         corner3 = AcadExtrusionCalculator.extrude2(corner3, solidExt);
         corner4 = AcadExtrusionCalculator.extrude2(corner4, solidExt);


         setCorner1(corner1);
         setCorner2(corner2);
         setCorner3(corner3);
         setCorner4(corner4);
	}
	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.IDwg3DTestable#has3DData()
	 */
	public boolean has3DData() {
		return (getElevation() != 0.0);
	}
	public double getZ() {
		return getElevation();
	}
	public Geometry toFMapGeometry(boolean is3DFile) throws CreateGeometryException {
		int subType = getGeometrySubType(is3DFile);

		double[] p1 = getCorner1();
		double[] p2 = getCorner2();
		double[] p3 = getCorner3();
		double[] p4 = getCorner4();
		double elev = getElevation();
		List pts = new ArrayList();

		if (is3DFile) {
			double[] p13d = new double[]{p1[0], p1[1], elev};
			double[] p23d = new double[]{p2[0], p2[1], elev};
			double[] p33d = new double[]{p3[0], p3[1], elev};
			double[] p43d = new double[]{p4[0], p4[1], elev};
			pts.add(p13d);
			pts.add(p23d);
			pts.add(p33d);
			pts.add(p43d);
		} else {
			pts.add(p1);
			pts.add(p2);
			pts.add(p3);
			pts.add(p4);
		}
		return FMapUtil.ptsToPolygon(pts, subType);
//		return ShapeFactory.createGeometry(solid);

	}
	public String toFMapString(boolean is3DFile) {
		if(is3DFile){
			return "FPolyline3D";
		}else{
			return "FPolyline2D";
		}
	}

	public String toString(){
		return "Solid";
	}
	public void transform2Block(double[] bPoint, Point2D insPoint, double[] scale, double rot,
			List dwgObjectsWithoutBlocks,
			Map handleObjWithoutBlocks,
			DwgFile callBack) {
		DwgSolid transformedEntity = null;
		double[] corner1 = this.getCorner1();
		double[] corner2 = this.getCorner2();
		double[] corner3 = this.getCorner3();
		double[] corner4 = this.getCorner4();
		Point2D pointAux = new Point2D.Double(corner1[0] - bPoint[0], corner1[1] - bPoint[1]);
		double laX = insPoint.getX() + ((pointAux.getX()*scale[0])*Math.cos(rot) + (pointAux.getY()*scale[1])*(-1)*Math.sin(rot));
		double laY = insPoint.getY() + ((pointAux.getX()*scale[0])*Math.sin(rot) + (pointAux.getY()*scale[1])*Math.cos(rot));
		double[] transformedP1 = new double[]{laX, laY};
		pointAux = new Point2D.Double(corner2[0] - bPoint[0], corner2[1] - bPoint[1]);
		laX = insPoint.getX() + ((pointAux.getX()*scale[0])*Math.cos(rot) + (pointAux.getY()*scale[1])*(-1)*Math.sin(rot));
		laY = insPoint.getY() + ((pointAux.getX()*scale[0])*Math.sin(rot) + (pointAux.getY()*scale[1])*Math.cos(rot));
		double[] transformedP2 = new double[]{laX, laY};
		pointAux = new Point2D.Double(corner3[0] - bPoint[0], corner3[1] - bPoint[1]);
		laX = insPoint.getX() + ((pointAux.getX()*scale[0])*Math.cos(rot) + (pointAux.getY()*scale[1])*(-1)*Math.sin(rot));
		laY = insPoint.getY() + ((pointAux.getX()*scale[0])*Math.sin(rot) + (pointAux.getY()*scale[1])*Math.cos(rot));
		double[] transformedP3 = new double[]{laX, laY};
		pointAux = new Point2D.Double(corner4[0] - bPoint[0], corner4[1] - bPoint[1]);
		laX = insPoint.getX() + ((pointAux.getX()*scale[0])*Math.cos(rot) + (pointAux.getY()*scale[1])*(-1)*Math.sin(rot));
		laY = insPoint.getY() + ((pointAux.getX()*scale[0])*Math.sin(rot) + (pointAux.getY()*scale[1])*Math.cos(rot));
		double[] transformedP4 = new double[]{laX, laY};
		transformedEntity = (DwgSolid)this.clone();
		transformedEntity.setCorner1(transformedP1);
		transformedEntity.setCorner2(transformedP2);
		transformedEntity.setCorner3(transformedP3);
		transformedEntity.setCorner4(transformedP4);
		transformedEntity.setElevation(this.getElevation() * scale[2]);
		dwgObjectsWithoutBlocks.add(transformedEntity);
		handleObjWithoutBlocks.put(new Integer(transformedEntity.getHandle().getOffset()), transformedEntity);
	}

	public Object clone(){
		DwgSolid obj = new DwgSolid(index);
		this.fill(obj);
		return obj;
	}

	protected void fill(DwgObject obj){
		super.fill(obj);
		DwgSolid myObj = (DwgSolid)obj;

		myObj.setCorner1(corner1);
		myObj.setCorner2(corner2);
		myObj.setCorner3(corner3);
		myObj.setCorner4(corner4);
		myObj.setElevation(elevation);
		myObj.setExtrusion(extrusion);
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
		// return Geometry.TYPES.SOLID; FIXME not implemented
		return Geometry.TYPES.SURFACE;
	}

}
