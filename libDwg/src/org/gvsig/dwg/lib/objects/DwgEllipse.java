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
import org.gvsig.dwg.lib.util.FMapUtil;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.primitive.EllipticArc;
import org.gvsig.fmap.geom.primitive.Point;
import org.gvsig.fmap.geom.util.UtilFunctions;


/**
 * The DwgEllipse class represents a DWG Ellipse
 *
 * @author jmorell
 */
public class DwgEllipse extends DwgObject
	implements IDwg3DTestable, IDwg2FMap, IDwgBlockMember{
	public DwgEllipse(int index) {
		super(index);
		// TODO Auto-generated constructor stub
	}
	private double[] center;
	private double[] semiMajorAxisVector;
	private double[] extrusion;
	private double axisRatio;
	private double initAngle;
	private double endAngle;

    /**
     * @return Returns the axisRatio.
     */
    public double getAxisRatio() {
        return axisRatio;
    }
    /**
     * @param axisRatio The axisRatio to set.
     */
    public void setAxisRatio(double axisRatio) {
        this.axisRatio = axisRatio;
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
     * @return Returns the majorAxisVector.
     */
    public double[] getSemiMajorAxisVector() {
        return semiMajorAxisVector;
    }
    /**
     * @param semiMajorAxisVector The majorAxisVector to set.
     */
    public void setSemiMajorAxisVector(double[] semiMajorAxisVector) {
        this.semiMajorAxisVector = semiMajorAxisVector;
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
	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.IDwg3DTestable#has3DData()
	 */
	public boolean has3DData() {
		return (getCenter()[2] !=0.0);
	}
	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.IDwg3DTestable#getZ()
	 */
	public double getZ() {
		return getCenter()[2];
	}
	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.IDwg2FMap#toFMapGeometry(boolean)
	 */
	public Geometry toFMapGeometry(boolean is3DFile) throws CreateGeometryException {

		GeometryManager gMan = GeometryLocator.getGeometryManager();
		double[] c = getCenter();
		double[] semiMajorAxisVector = getSemiMajorAxisVector();
		double axisRatio = getAxisRatio();
		double angSt = getInitAngle();
		double angEnd = getEndAngle();
		double angExt = UtilFunctions.angleDistance(angSt, angEnd);


		//Point2D axis1Start = new Point2D.Double(center.getX()+smav.getX(), center.getY()+smav.getY());
		Point axis1Start = (Point)gMan.create(Geometry.TYPES.POINT,
				getGeometrySubType(is3DFile));
		double[] tmp;
		tmp = new double[c.length];
		for (int i=0;i< c.length;i++){
			tmp[i]=c[i]+semiMajorAxisVector[i];
		}
		axis1Start.setCoordinates(tmp);

		//Point2D axis1End = new Point2D.Double(center.getX()-smav.getX(), center.getY()-smav.getY());
		Point axis1End = (Point)gMan.create(Geometry.TYPES.POINT,
				getGeometrySubType(is3DFile));

		tmp = new double[c.length];
		for (int i=0;i< c.length;i++){
			tmp[i]=c[i]-semiMajorAxisVector[i];
		}
		axis1End.setCoordinates(tmp);


		Point2D smav = new Point2D.Double(semiMajorAxisVector[0],semiMajorAxisVector[1]);
		Point2D origen = new Point2D.Double(0.0,0.0);
		double semiMajorAxisLenght = origen.distance(smav);
		double semiMinorAxisLenght = semiMajorAxisLenght * axisRatio;



		EllipticArc ellipse = (EllipticArc) gMan.create(getGeometryType(),
				getGeometrySubType(is3DFile));
		ellipse.setPoints(axis1Start, axis1End, semiMinorAxisLenght, -angSt,
				-angExt);

		return ellipse;
	}

	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.IDwg2FMap#toFMapString(boolean)
	 */
	public String toFMapString(boolean is3DFile) {
		if(is3DFile) {
			return "EllipticArc2DZ";
		} else {
			return "EllipticArc2D";
		}
	}

	public String toString(){
		return "Ellipse";
	}
	public void transform2Block(double[] bPoint, Point2D insPoint,
			double[] scale, double rot,
			List dwgObjectsWithoutBlocks,
			Map handleObjWithoutBlocks,
			DwgFile callBack) {
		DwgEllipse transformedEntity = null;
		double[] center = this.getCenter();
		Point2D pointAux = new Point2D.Double(center[0] - bPoint[0], center[1] - bPoint[1]);
		double laX = insPoint.getX() + ((pointAux.getX()*scale[0])*Math.cos(rot) + (pointAux.getY()*scale[1])*(-1)*Math.sin(rot));
		double laY = insPoint.getY() + ((pointAux.getX()*scale[0])*Math.sin(rot) + (pointAux.getY()*scale[1])*Math.cos(rot));
		double laZ = center[2] * scale[2];
		double[] transformedCenter = new double[]{laX, laY, laZ};
		double[] majorAxisVector = this.getSemiMajorAxisVector();
		double[] transformedMajorAxisVector = new double[]{majorAxisVector[0] * scale[0], majorAxisVector[1] * scale[1], majorAxisVector[2] * scale[2]};
		//TODO: Rotar un �ngulo rot el vector majorAxisVector fijado en
		// center.
		double axisRatio = this.getAxisRatio();
		double transformedAxisRatio = axisRatio;
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
		transformedEntity = (DwgEllipse)this.clone();
		transformedEntity.setCenter(transformedCenter);
		transformedEntity.setSemiMajorAxisVector(transformedMajorAxisVector);
		transformedEntity.setAxisRatio(transformedAxisRatio);
		transformedEntity.setInitAngle(transformedInitAngle);
		transformedEntity.setEndAngle(transformedEndAngle);
		dwgObjectsWithoutBlocks.add(transformedEntity);
		handleObjWithoutBlocks.put(new Integer(transformedEntity.getHandle().getOffset()), transformedEntity);
//		dwgObjectsWithoutBlocks.add(this);
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone(){
		DwgEllipse obj = new DwgEllipse(index);
		this.fill(obj);
		return obj;
	}

	protected void fill(DwgObject obj){
		super.fill(obj);
		DwgEllipse myObj = (DwgEllipse)obj;

		myObj.setAxisRatio(axisRatio);
		myObj.setCenter(center);
		myObj.setEndAngle(endAngle);
		myObj.setExtrusion(extrusion);
		myObj.setInitAngle(initAngle);
		myObj.setSemiMajorAxisVector(semiMajorAxisVector);

	}

	public int getGeometrySubType(boolean is3DFile) {
		if (is3DFile) {
			return FMapUtil.SUBTYPE_GEOM3D;
		} else {
			return FMapUtil.SUBTYPE_GEOM2D;
		}
	}
	public int getGeometryType() {
		return Geometry.TYPES.ELLIPTICARC;
	}
}
