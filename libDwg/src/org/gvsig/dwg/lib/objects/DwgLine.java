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
import org.gvsig.dwg.lib.util.AcadExtrusionCalculator;
import org.gvsig.dwg.lib.util.FMapUtil;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.primitive.Curve;
import org.gvsig.fmap.geom.primitive.Point;


/**
 * The DwgLine class represents a DWG Line
 *
 * @author jmorell, azabala
 */
public class DwgLine extends DwgObject
	implements /*IDwgExtrusionable,*/ IDwg3DTestable, IDwg2FMap, IDwgBlockMember{
	public DwgLine(int index) {
		super(index);
		// TODO Auto-generated constructor stub
	}
	private double[] p1;
	private double[] p2;
	private double thickness;
	private double[] extrusion;
	private boolean zflag = false;

	/**
	 * @return Returns the p1.
	 */
	public double[] getP1() {
		return p1;
	}
	/**
	 * @param p1 The p1 to set.
	 */
	public void setP1(double[] p1) {
		this.p1 = p1;
	}
	/**
	 * @return Returns the p2.
	 */
	public double[] getP2() {
		return p2;
	}
	/**
	 * @param p2 The p2 to set.
	 */
	public void setP2(double[] p2) {
		this.p2 = p2;
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
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
//	public Object clone() {
//		DwgLine dwgLine = new DwgLine(index);
//		dwgLine.setType(type);
//		dwgLine.setHandle(handle);
//		dwgLine.setVersion(version);
//		dwgLine.setMode(mode);
//		dwgLine.setLayerHandle(layerHandle);
//		dwgLine.setColor(color);
//		dwgLine.setNumReactors(numReactors);
//		dwgLine.setNoLinks(noLinks);
//		dwgLine.setLinetypeFlags(linetypeFlags);
//		dwgLine.setPlotstyleFlags(plotstyleFlags);
//		dwgLine.setSizeInBits(sizeInBits);
//		dwgLine.setExtendedData(extendedData);
//		dwgLine.setGraphicData(graphicData);
//		//dwgLine.setInsideBlock(insideBlock);
//		dwgLine.setP1(p1);
//		dwgLine.setP2(p2);
//		dwgLine.setThickness(thickness);
//		dwgLine.setExtrusion(extrusion);
//		dwgLine.setSubEntityHandle(subEntityHandle);
//		return dwgLine;
//	}
    /**
     * @return Returns the zflag.
     */
    public boolean isZflag() {
        return zflag;
    }
    /**
     * @param zflag The zflag to set.
     */
    public void setZflag(boolean zflag) {
        this.zflag = zflag;
    }
	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.IDwgExtrusionable#applyExtrussion()
	 */
	public void applyExtrussion() {
		 double[] lineP1 = getP1();
         double[] lineP2 = getP2();
         boolean zflag = isZflag();
         if (zflag) {
             // elev = 0.0;
             lineP1 = new double[]{lineP1[0], lineP1[1], 0.0};
             lineP2 = new double[]{lineP2[0], lineP2[1], 0.0};
         }
         double[] lineExt = getExtrusion();
         lineP1 = AcadExtrusionCalculator.extrude2(lineP1, lineExt);
         lineP2 = AcadExtrusionCalculator.extrude2(lineP2, lineExt);
         setP1(lineP1);
         setP2(lineP2);
	}
	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.IDwg3DTestable#has3DData()
	 */
	public boolean has3DData() {
	  if (!isZflag()) {
		double z1 = getP1()[2];
		double z2 = getP2()[2];
		if (z1!=0.0 || z2!=0.0) {
			return  true;
		}
	  }//TODO y si zflag vale true? REVISAR
	  return false;
	}
	public double getZ() {
		double[] p1 = getP1();
		double[] p2 = getP2();
		if(isZflag()){
			if (p1[2] == p2[2]) {
				return p1[2];
			} else {
				return 0d;
			}
		} else {
				return 0d;
		}
	}
	public Geometry toFMapGeometry(boolean is3DFile) throws CreateGeometryException {
		GeometryManager gMan = GeometryLocator.getGeometryManager();

		double[] p1 = getP1();
		double[] p2 = getP2();

		Point point1 = (Point) gMan.create(Geometry.TYPES.POINT,
				getGeometrySubType(is3DFile));
		point1.setCoordinates(p1);

		Point point2 = (Point) gMan.create(Geometry.TYPES.POINT,
				getGeometrySubType(is3DFile));
		point2.setCoordinates(p2);

		Curve line = (Curve) gMan.create(getGeometryType(),
				getGeometrySubType(is3DFile));
		line.setPoints(point1, point2);
		return line;

	}
	public String toFMapString(boolean is3DFile) {
		if(is3DFile){
			return "FPolyline3D";
		}else{
			return "FPolyline2D";
		}
	}

	public String toString(){
		return "Line";
	}
	public void transform2Block(double[] bPoint, Point2D insPoint,
			double[] scale, double rot,
			List dwgObjectsWithoutBlocks,
			Map handleObjWithoutBlocks,
			DwgFile callBack) {
		DwgLine transformedEntity = null;
		double[] p1 = this.getP1();
		if(p1.length < 3)
		{
			double[] newp1 = new double[3];
			System.arraycopy(p1, 0, newp1, 0, p1.length);
			p1 = newp1;
			setP1(newp1);
		}
		double[] p2 = this.getP2();
		if(p2.length < 3)
		{
			double[] newp2 = new double[3];
			System.arraycopy(p2, 0, newp2, 0, p2.length);
			p2 = newp2;
			setP2(newp2);
		}
		Point2D pointAux = new Point2D.Double(p1[0] - bPoint[0], p1[1] - bPoint[1]);
		double laX = insPoint.getX() + ((pointAux.getX()*scale[0])*Math.cos(rot) + (pointAux.getY()*scale[1])*(-1)*Math.sin(rot));
		double laY = insPoint.getY() + ((pointAux.getX()*scale[0])*Math.sin(rot) + (pointAux.getY()*scale[1])*Math.cos(rot));
		double[] transformedP1 = null;
        if (this.isZflag() ) {
			double laZ = p1[2] * scale[2];
			transformedP1 = new double[]{laX, laY, laZ};
		} else {
			transformedP1 = new double[]{laX, laY, 0d};
		}
		//double[] transformedP1 = new double[]{laX, laY};
		pointAux = new Point2D.Double(p2[0] - bPoint[0], p2[1] - bPoint[1]);
		laX = insPoint.getX() + ((pointAux.getX()*scale[0])*Math.cos(rot) + (pointAux.getY()*scale[1])*(-1)*Math.sin(rot));
		laY = insPoint.getY() + ((pointAux.getX()*scale[0])*Math.sin(rot) + (pointAux.getY()*scale[1])*Math.cos(rot));
        double[] transformedP2 = null;
		if (this.isZflag() ) {
			double laZ = p2[2] * scale[2];
			transformedP2 = new double[]{laX, laY, laZ};
		} else {
			transformedP2 = new double[]{laX, laY, 0d};
		}
		//double[] transformedP2 = new double[]{laX, laY};
		transformedEntity = (DwgLine)this.clone();
		transformedEntity.setP1(transformedP1);
		transformedEntity.setP2(transformedP2);
		dwgObjectsWithoutBlocks.add(transformedEntity);
		handleObjWithoutBlocks.put(new Integer(transformedEntity.getHandle().getOffset()), transformedEntity);
//		dwgObjectsWithoutBlocks.add(this);
	}
	public Object clone(){
		DwgLine dwgLine = new DwgLine(index);
		this.fill(dwgLine);
		return dwgLine;
	}

	protected void fill(DwgObject obj){
		super.fill(obj);
		DwgLine myObj = (DwgLine)obj;

		myObj.setExtrusion(extrusion);
		myObj.setP1(p1);
		myObj.setP2(p2);
		myObj.setThickness(thickness);

	}

	public int getGeometrySubType(boolean is3DFile) {
		if (is3DFile) {
//			if (isZflag()) {
//				return Geometry.SUBTYPES.GEOM3DM;
//			} else {
			return FMapUtil.SUBTYPE_GEOM3D;
			// }
		} else {
//			if (isZflag()) {
//				return Geometry.SUBTYPES.GEOM2DM;
//			} else {
			return FMapUtil.SUBTYPE_GEOM2D;
//			}
		}
	}

	public int getGeometryType() {
		return Geometry.TYPES.CURVE;
	}


}
