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
import org.gvsig.dwg.lib.DwgHandleReference;
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
import org.gvsig.fmap.geom.primitive.Point;


/**
 * The DwgText class represents a DWG Text
 *
 * @author jmorell
 */
public class DwgText extends DwgObject
					implements IDwgExtrusionable,
					IDwg3DTestable,
					IDwg2FMap,
					IDwgBlockMember{
	public DwgText(int index) {
		super(index);
		// TODO Auto-generated constructor stub
	}
	private int dataFlag;
	private double elevation;
	private Point2D insertionPoint;
	private Point2D alignmentPoint;
	private double[] extrusion;
	private double thickness;
	private double obliqueAngle;
	private double rotationAngle;
	private double height;
	private double widthFactor;
	private String text;
	private int generation;
	private int halign;
	private int valign;
	private DwgHandleReference styleHdl;

	/**
	 * @return Returns the dataFlag.
	 */
	public int getDataFlag() {
		return dataFlag;
	}
	/**
	 * @param dataFlag The dataFlag to set.
	 */
	public void setDataFlag(int dataFlag) {
		this.dataFlag = dataFlag;
	}
	/**
	 * @return Returns the height.
	 */
	public double getHeight() {
		return height;
	}
	/**
	 * @param height The height to set.
	 */
	public void setHeight(double height) {
		this.height = height;
	}
	/**
	 * @return Returns the insertionPoint.
	 */
	public Point2D getInsertionPoint() {
		return insertionPoint;
	}
	/**
	 * @param insertionPoint The insertionPoint to set.
	 */
	public void setInsertionPoint(Point2D insertionPoint) {
		this.insertionPoint = insertionPoint;
	}
	/**
	 * @return Returns the rotationAngle.
	 */
	public double getRotationAngle() {
		return rotationAngle;
	}

	public double getRotationAngleInDegrees(){
		int dflag = getDataFlag();
		if ((dflag & 0x8) == 0) {
			double textRot = getRotationAngle();
			return Math.toDegrees(textRot);
		} else {
			return 0d;
		}
	}
	/**
	 * @param rotationAngle The rotationAngle to set.
	 */
	public void setRotationAngle(double rotationAngle) {
		this.rotationAngle = rotationAngle;
	}
	/**
	 * @return Returns the text.
	 */
	public String getText() {
		return text;
	}
	/**
	 * @param text The text to set.
	 */
	public void setText(String text) {
		this.text = text;
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
     * @return Returns the extrusion.
     */
    public double[] getExtrusion() {
        return extrusion;
    }

	/**
	 * @return Returns the alignmentPoint.
	 */
	public Point2D getAlignmentPoint() {
		return alignmentPoint;
	}
	/**
	 * @param alignmentPoint The alignmentPoint to set.
	 */
	public void setAlignmentPoint(Point2D alignmentPoint) {
		this.alignmentPoint = alignmentPoint;
	}
	/**
	 * @return Returns the generation.
	 */
	public int getGeneration() {
		return generation;
	}
	/**
	 * @param generation The generation to set.
	 */
	public void setGeneration(int generation) {
		this.generation = generation;
	}
	/**
	 * @return Returns the halign.
	 */
	public int getHalign() {
		return halign;
	}
	/**
	 * @param halign The halign to set.
	 */
	public void setHalign(int halign) {
		this.halign = halign;
	}
	/**
	 * @return Returns the obliqueAngle.
	 */
	public double getObliqueAngle() {
		return obliqueAngle;
	}
	/**
	 * @param obliqueAngle The obliqueAngle to set.
	 */
	public void setObliqueAngle(double obliqueAngle) {
		this.obliqueAngle = obliqueAngle;
	}
	/**
	 * @return Returns the valign.
	 */
	public int getValign() {
		return valign;
	}
	/**
	 * @param valign The valign to set.
	 */
	public void setValign(int valign) {
		this.valign = valign;
	}
	/**
	 * @return Returns the widthFactor.
	 */
	public double getWidthFactor() {
		return widthFactor;
	}
	/**
	 * @param widthFactor The widthFactor to set.
	 */
	public void setWidthFactor(double widthFactor) {
		this.widthFactor = widthFactor;
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
		 Point2D tpoint = getInsertionPoint();
         double elev = getElevation();
         double[] textPoint = new double[]{tpoint.getX(), tpoint.getY(), elev};
         double[] textExt = getExtrusion();
         textPoint = AcadExtrusionCalculator.extrude2(textPoint, textExt);
         setInsertionPoint(new Point2D.Double(textPoint[0], textPoint[1]));
         setElevation(elev);
	}
	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.IDwg3DTestable#has3DData()
	 */
	public boolean has3DData() {
		return (getElevation() !=0.0);
	}
	public double getZ() {
		return getElevation();
	}
	public Geometry toFMapGeometry(boolean is3DFile)
			throws CreateGeometryException {
		GeometryManager gMan = GeometryLocator.getGeometryManager();
		Point2D p = getInsertionPoint();
		double elev = 0.0;
		if ((getDataFlag() & 0x1) == 0) {
			elev = getElevation();
		}
		Point point = (Point) gMan.create(getGeometryType(),
				getGeometrySubType(is3DFile));
		point.setX(p.getX());
		point.setX(p.getY());
		if (is3DFile) {
			point.setCoordinateAt(2, elev);

		}
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
		return "Text";
	}
	/**
	 * @param styleHdl
	 */
	public void setStyleHandle(DwgHandleReference styleHdl) {
		this.styleHdl = styleHdl;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone(){
		DwgText obj = new DwgText(index);
		this.fill(obj);
		return obj;
	}

	protected void fill(DwgObject obj){
		super.fill(obj);
		DwgText myObj = (DwgText)obj;

		myObj.setAlignmentPoint(alignmentPoint);
		myObj.setDataFlag(dataFlag);
		myObj.setElevation(elevation);
		myObj.setExtrusion(extrusion);
		myObj.setGeneration(generation);
		myObj.setHalign(halign);
		myObj.setHeight(height);
		myObj.setInsertionPoint(insertionPoint);
		myObj.setObliqueAngle(obliqueAngle);
		myObj.setRotationAngle(rotationAngle);
		myObj.setStyleHandle(styleHdl);
		myObj.setText(text);
		myObj.setThickness(thickness);
		myObj.setValign(valign);
		myObj.setWidthFactor(widthFactor);
	}

	//TODO Implement
	public void transform2Block(double[] bPoint, Point2D insPoint, double[] scale, double rot, List dwgObjectsWithoutBlocks, Map handle_objectsWithoutBlocks, DwgFile callBack) {
		 Point2D pointAux = new Point2D.Double(insertionPoint.getX() - bPoint[0],
				 insertionPoint.getY() - bPoint[1]);
		 double laX = insPoint.getX() + ((pointAux.getX()*scale[0])*Math.cos(rot) + (pointAux.getY()*scale[1])*(-1)*Math.sin(rot));
		 double laY = insPoint.getY() + ((pointAux.getX()*scale[0])*Math.sin(rot) + (pointAux.getY()*scale[1])*Math.cos(rot));

		 Point2D.Double newInsertionPoint = new Point2D.Double(laX, laY);
		 DwgText transformedEntity = (DwgText) this.clone();
		 transformedEntity.setInsertionPoint(newInsertionPoint);

		 if(alignmentPoint != null){
			 pointAux = new Point2D.Double(alignmentPoint.getX() - bPoint[0],
					 alignmentPoint.getY() - bPoint[1]);
			 laX = insPoint.getX() + ((pointAux.getX()*scale[0])*Math.cos(rot) + (pointAux.getY()*scale[1])*(-1)*Math.sin(rot));
			 laY = insPoint.getY() + ((pointAux.getX()*scale[0])*Math.sin(rot) + (pointAux.getY()*scale[1])*Math.cos(rot));
			 Point2D.Double newAlignPoint = new Point2D.Double(laX, laY);
			 transformedEntity.setAlignmentPoint(newAlignPoint);
		 }
		 //TODO Transformar el resto de parametros en base a los datos del bloque
		 //altura, etc.
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
