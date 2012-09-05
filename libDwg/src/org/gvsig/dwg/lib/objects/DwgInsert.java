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
import org.gvsig.dwg.lib.DwgHandleReference;
import org.gvsig.dwg.lib.DwgObject;
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
 * The DwgInsert class represents a DWG Insert
 *
 * @author jmorell
 */
public class DwgInsert extends DwgObject
	implements IDwgExtrusionable, IDwg3DTestable,/* IDwg2FMap, */IDwgBlockMember {
	public DwgInsert(int index) {
		super(index);
	}
	private double[] insertionPoint;
	private double[] scale;
	private double rotation;
	private double[] extrusion;
	private DwgHandleReference blockHeaderHandle = null;
	private DwgHandleReference firstAttribHandle = null;
	private DwgHandleReference lastAttribHandle = null;
	private DwgHandleReference seqendHandle = null;
	private ArrayList ownedObjectsHandles = new ArrayList(); //For R2004+

	/**
	 * An insert could be nexted in other insert.
	 * We wont process and insert two times.
	 * */
	private boolean processed = false;


	public void dump(){
		System.out.println("<Insert>");
		System.out.println("<inspoint x="+insertionPoint[0]+
				" y="+insertionPoint[1]+
				" z="+insertionPoint[2]+">");
		System.out.println("<scale x="+scale[0]+
				" y="+scale[1]+
				" z="+scale[2]+">");
		System.out.println("<rotation rot="+rotation+" >");

		System.out.println("</Insert>");

	}

	/**
	 * @return Returns the blockHeaderHandle.
	 */
	public DwgHandleReference getBlockHeaderHandle() {
		return blockHeaderHandle;
	}
	/**
	 * @param blockHeaderHandle The blockHeaderHandle to set.
	 */
	public void setBlockHeaderHandle(DwgHandleReference blockHeaderHandle) {
		this.blockHeaderHandle = blockHeaderHandle;
	}
	/**
	 * @return Returns the firstAttribHandle.
	 */
	public DwgHandleReference getFirstAttribHandle() {
		return firstAttribHandle;
	}
	/**
	 * @param firstAttribHandle The firstAttribHandle to set.
	 */
	public void setFirstAttribHandle(DwgHandleReference firstAttribHandle) {
		this.firstAttribHandle = firstAttribHandle;
	}
	/**
	 * @return Returns the insertionPoint.
	 */
	public double[] getInsertionPoint() {
		return insertionPoint;
	}
	/**
	 * @param insertionPoint The insertionPoint to set.
	 */
	public void setInsertionPoint(double[] insertionPoint) {
		this.insertionPoint = insertionPoint;
	}
	/**
	 * @return Returns the lastAttribHandle.
	 */
	public DwgHandleReference getLastAttribHandle() {
		return lastAttribHandle;
	}
	/**
	 * @param lastAttribHandle The lastAttribHandle to set.
	 */
	public void setLastAttribHandle(DwgHandleReference lastAttribHandle) {
		this.lastAttribHandle = lastAttribHandle;
	}
	/**
	 * @return Returns the rotation.
	 */
	public double getRotation() {
		return rotation;
	}
	/**
	 * @param rotation The rotation to set.
	 */
	public void setRotation(double rotation) {
		this.rotation = rotation;
	}
	/**
	 * @return Returns the scale.
	 */
	public double[] getScale() {
		return scale;
	}
	/**
	 * @param scale The scale to set.
	 */
	public void setScale(double[] scale) {
		this.scale = scale;
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
	 * @return Returns the seqendHandle.
	 */
	public DwgHandleReference getSeqendHandle() {
		return seqendHandle;
	}
	/**
	 * @param seqendHandle The seqendHandle to set.
	 */
	public void setSeqendHandle(DwgHandleReference seqendHandle) {
		this.seqendHandle = seqendHandle;
	}

	/**
	 * @return Returns the owned objects handles.
	 */
	public ArrayList getOwnedObjectsHandles() {
		return ownedObjectsHandles;
	}
	/**
	 * @param objects The owned objects handles to set.
	 */
	public void setOwnedObjectsHandles(ArrayList handles) {
		this.ownedObjectsHandles = handles;
	}
	/**
	 * Add a handle to the ownedBbjectsHandle vector
	 *
	 * @param handle handle
	 */
	public void addOwnedObjectHandle(DwgHandleReference handle) {
		this.ownedObjectsHandles.add(handle);
	}


	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.IDwgExtrusionable#applyExtrussion()
	 */
	public void applyExtrussion() {
		 double[] insertPoint = getInsertionPoint();
         double[] insertExt = getExtrusion();
         insertPoint = AcadExtrusionCalculator.extrude2(insertPoint, insertExt);
         setInsertionPoint(insertPoint);
	}
	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.IDwg3DTestable#has3DData()
	 */
	public boolean has3DData() {
		return (getInsertionPoint()[2] != 0.0);
	}
	public double getZ() {
		return getInsertionPoint()[2];
	}
	public Geometry toFMapGeometry(boolean is3DFile)
			throws CreateGeometryException {
		GeometryManager gMan = GeometryLocator.getGeometryManager();
		double[] p = getInsertionPoint();
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
		return "Insert";
	}
	public void transform2Block(double[] bPoint, Point2D insPoint,
			double[] scale, double rot,
			List dwgObjectsWithoutBlocks, Map handle_objWithoutBlocks, DwgFile callBack) {

		processed = true;

		double[] p = this.getInsertionPoint();
		Point2D point = new Point2D.Double(p[0], p[1]);

		double[] newScale = this.getScale();
		double newRot = this.getRotation();
		DwgHandleReference newBlockHandle = this.getBlockHeaderHandle();

		Point2D pointAux = new Point2D.Double(point.getX() - bPoint[0], point.getY() - bPoint[1]);
		double laX = insPoint.getX() + ((pointAux.getX()*scale[0])*Math.cos(rot) + (pointAux.getY()*scale[1])*(-1)*Math.sin(rot));
		double laY = insPoint.getY() + ((pointAux.getX()*scale[0])*Math.sin(rot) + (pointAux.getY()*scale[1])*Math.cos(rot));
		double laZ = p[2] * scale[2];

		Point2D newInsPoint = new Point2D.Double(laX, laY);
		newScale = new double[]{scale[0]*newScale[0], scale[1]*newScale[1], scale[2]*newScale[2]};
        newRot = newRot + rot;
        if (newRot<0) {
            newRot = newRot + (2*Math.PI);
        } else if (newRot>(2*Math.PI)) {
            newRot = newRot - (2*Math.PI);
        }

        callBack.manageInsert2(newInsPoint,
								newScale,
								newRot,
								newBlockHandle.getOffset(),
								dwgObjectsWithoutBlocks,
								handle_objWithoutBlocks);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone(){
		DwgInsert obj = new DwgInsert(index);
		this.fill(obj);
		return obj;
	}

	protected void fill(DwgObject obj){
		super.fill(obj);
		DwgInsert myObj = (DwgInsert)obj;

		myObj.setBlockHeaderHandle(blockHeaderHandle);
		myObj.setExtrusion(extrusion);
		myObj.setFirstAttribHandle(firstAttribHandle);
		myObj.setInsertionPoint(insertionPoint);
		myObj.setLastAttribHandle(lastAttribHandle);
		myObj.setRotation(rotation);
		myObj.setScale(scale);
		myObj.setSeqendHandle(seqendHandle);
	}
	public boolean isProcessed() {
		return processed;
	}
	public void setProcessed(boolean processed) {
		this.processed = processed;
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
