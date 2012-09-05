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
import org.gvsig.dwg.lib.util.FMapUtil;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.primitive.Point;
import org.gvsig.fmap.geom.primitive.Spline;


/**
 * The DwgSpline class represents a DWG Spline
 *
 * @author jmorell
 */
public class DwgSpline extends DwgObject
	implements IDwg3DTestable,  IDwg2FMap {

	public DwgSpline(int index) {
		super(index);
	}
	private int scenario;
	private int degree;
	private double fitTolerance;
	private double[] beginTanVector;
	private double[] endTanVector;
	private boolean rational;
	private boolean closed;
	private boolean periodic;
	private double knotTolerance;
	private double controlTolerance;
	private double[] knotPoints;
	private double[][] controlPoints;
	private double[] weights;
	private double[][] fitPoints;

	/**
	 * @return Returns the closed.
	 */
	public boolean isClosed() {
		return closed;
	}
	/**
	 * @param closed The closed to set.
	 */
	public void setClosed(boolean closed) {
		this.closed = closed;
	}
	/**
	 * @return Returns the controlPoints.
	 */
	public double[][] getControlPoints() {
		return controlPoints;
	}
	/**
	 * @param controlPoints The controlPoints to set.
	 */
	public void setControlPoints(double[][] controlPoints) {
		this.controlPoints = controlPoints;
	}
	/**
	 * @return Returns the fitPoints.
	 */
	public double[][] getFitPoints() {
		return fitPoints;
	}
	/**
	 * @param fitPoints The fitPoints to set.
	 */
	public void setFitPoints(double[][] fitPoints) {
		this.fitPoints = fitPoints;
	}
	/**
	 * @return Returns the knotPoints.
	 */
	public double[] getKnotPoints() {
		return knotPoints;
	}
	/**
	 * @param knotPoints The knotPoints to set.
	 */
	public void setKnotPoints(double[] knotPoints) {
		this.knotPoints = knotPoints;
	}
	/**
	 * @return Returns the scenario.
	 */
	public int getScenario() {
		return scenario;
	}
	/**
	 * @param scenario The scenario to set.
	 */
	public void setScenario(int scenario) {
		this.scenario = scenario;
	}

	/**
	 * @return Returns the beginTanVector.
	 */
	public double[] getBeginTanVector() {
		return beginTanVector;
	}
	/**
	 * @param beginTanVector The beginTanVector to set.
	 */
	public void setBeginTanVector(double[] beginTanVector) {
		this.beginTanVector = beginTanVector;
	}
	/**
	 * @return Returns the controlTolerance.
	 */
	public double getControlTolerance() {
		return controlTolerance;
	}
	/**
	 * @param controlTolerance The controlTolerance to set.
	 */
	public void setControlTolerance(double controlTolerance) {
		this.controlTolerance = controlTolerance;
	}
	/**
	 * @return Returns the degree.
	 */
	public int getDegree() {
		return degree;
	}
	/**
	 * @param degree The degree to set.
	 */
	public void setDegree(int degree) {
		this.degree = degree;
	}
	/**
	 * @return Returns the endTanVector.
	 */
	public double[] getEndTanVector() {
		return endTanVector;
	}
	/**
	 * @param endTanVector The endTanVector to set.
	 */
	public void setEndTanVector(double[] endTanVector) {
		this.endTanVector = endTanVector;
	}
	/**
	 * @return Returns the fitTolerance.
	 */
	public double getFitTolerance() {
		return fitTolerance;
	}
	/**
	 * @param fitTolerance The fitTolerance to set.
	 */
	public void setFitTolerance(double fitTolerance) {
		this.fitTolerance = fitTolerance;
	}
	/**
	 * @return Returns the knotTolerance.
	 */
	public double getKnotTolerance() {
		return knotTolerance;
	}
	/**
	 * @param knotTolerance The knotTolerance to set.
	 */
	public void setKnotTolerance(double knotTolerance) {
		this.knotTolerance = knotTolerance;
	}
	/**
	 * @return Returns the periodic.
	 */
	public boolean isPeriodic() {
		return periodic;
	}
	/**
	 * @param periodic The periodic to set.
	 */
	public void setPeriodic(boolean periodic) {
		this.periodic = periodic;
	}
	/**
	 * @return Returns the rational.
	 */
	public boolean isRational() {
		return rational;
	}
	/**
	 * @param rational The rational to set.
	 */
	public void setRational(boolean rational) {
		this.rational = rational;
	}
	/**
	 * @return Returns the weights.
	 */
	public double[] getWeights() {
		return weights;
	}
	/**
	 * @param weights The weights to set.
	 */
	public void setWeights(double[] weights) {
		this.weights = weights;
	}
	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.IDwg3DTestable#has3DData()
	 */
	public boolean has3DData() {
		double[][] pts = getControlPoints();
		if(pts == null) {
			return false;
		}
		double z = 0d;
		for (int j=0;j<pts.length;j++) {
				z = pts[j][2];
				if (z != 0.0) {
					return true;
				}
		}//for
		return false;
	}
	public double getZ() {
		return 0d;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone(){
		DwgSpline obj = new DwgSpline(index);
		this.fill(obj);
		return obj;
	}

	protected void fill(DwgObject obj){
		super.fill(obj);
		DwgSpline myObj = (DwgSpline)obj;

		myObj.setBeginTanVector(beginTanVector);
		myObj.setClosed(closed);
		myObj.setControlPoints(controlPoints);
		myObj.setControlTolerance(controlTolerance);
		myObj.setDegree(degree);
		myObj.setEndTanVector(endTanVector);
		myObj.setFitPoints(fitPoints);
		myObj.setFitTolerance(fitTolerance);
		myObj.setKnotPoints(knotPoints);
		myObj.setKnotTolerance(knotTolerance);
		myObj.setPeriodic(periodic);
		myObj.setRational(rational);
		myObj.setScenario(scenario);
		myObj.setWeights(weights);

	}
	public Geometry toFMapGeometry(boolean is3DFile) throws CreateGeometryException {
		//FIXME: Implementación provisional para ver si podemos leer SPLINES
		//De momento creamos una polilinea cuando deberíamos crear un spline o un bspline
		GeometryManager gMan = GeometryLocator.getGeometryManager();
		Spline pline = null;
		int subType = getGeometrySubType(is3DFile);
		double elev = getZ();
		double[][] points = getFitPoints();
		if (points == null) {
			points = getControlPoints();
		}


		if (points == null) {
			gMan.createNullGeometry(subType);
		}
		pline = (Spline) gMan.create(getGeometryType(), subType);
		Point point;
		for (int i = 0; i < points.length; i++) {
			point = (Point) gMan.create(Geometry.TYPES.POINT, subType);
			pline.addVertex(point);
		}
		return pline;
	}

	public String toFMapString(boolean is3DFile) {
		if(is3DFile) {
			return "FPolyline3D";
		} else {
			return "FPolyline2D";
		}
	}
	public String toString(){
		return "Spline";
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
		return Geometry.TYPES.SPLINE;
	}

}
