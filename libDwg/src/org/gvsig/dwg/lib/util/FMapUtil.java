/*
 * Created on 18-ene-2007 by azabala
 *
 */
package org.gvsig.dwg.lib.util;

import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.List;

import org.gvsig.dwg.lib.IDwgVertex;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.aggregate.MultiCurve;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.primitive.Curve;
import org.gvsig.fmap.geom.primitive.Point;
import org.gvsig.fmap.geom.primitive.Surface;


/**
 * @author alzabord
 *
 */
public class FMapUtil {

	public static final int SUBTYPE_GEOM2D = Geometry.SUBTYPES.GEOM2D;
	public static final int SUBTYPE_GEOM3D = Geometry.SUBTYPES.GEOM2DZ;

//	/**
//	 * Method that changes a Point3D array to a FPolyline3D. Is useful to
//	 * convert a polyline given by it points to a FPolyline3D, a polyline 3D in
//	 * the FMap model object
//	 *
//	 * @param pts
//	 *            Array of Point3D that defines the polyline 3D that will be
//	 *            converted in a FPolyline3D
//	 * @return FPolyline3D This FPolyline3D is build using the array of Point3D
//	 *         that is the argument of the method
//	 */
//	public static FPolyline3D points3DToFPolyline3D(List pts) {
//		GeneralPathX genPathX = getGeneralPathX(pts);
//		double[] elevations = new double[pts.size()];
//		for (int i = 0; i < pts.size(); i++) {
//			elevations[i] = ((double[])pts.get(i))[2];
//		}
//		return new FPolyline3D(genPathX, elevations);
//	}

	private static GeometryManager gManager = GeometryLocator
			.getGeometryManager();



//	/**
//	 * Method that changes a Point2D array to a FPolyline2D. Is useful to
//	 * convert a polyline given by it points to a FPolyline2D, a polyline in the
//	 * FMap model object
//	 *
//	 * @param pts
//	 *            Array of Point2D that defines the polyline that will be
//	 *            converted in a FPolyline2D
//	 * @return FPolyline2D This FPolyline2D is build using the array of Point2D
//	 *         that is the argument of the method
//	 */
//	public static FPolyline2D points2DToFPolyline2D(List pts) {
//		GeneralPathX genPathX = getGeneralPathX(pts);
//		return new FPolyline2D(genPathX);
//	}

	public static MultiCurve ptsToMultiCurve(List pts, int subType)
			throws CreateGeometryException {

		if (pts.size() < 2) {
			throw new IllegalArgumentException();
		}

		Point point, prevPoint;
		Curve curve;

		MultiCurve multi = (MultiCurve) gManager.create(
				Geometry.TYPES.MULTICURVE,
				subType);
		prevPoint = FMapUtil.createPoint(subType, pts.get(0));
		for (int i = 1; i < pts.size(); i++) {
			point = FMapUtil.createPoint(subType, pts.get(i));
			curve = (Curve) gManager.create(Geometry.TYPES.CURVE, subType);
			curve.setPoints(prevPoint, point);
			multi.addCurve(curve);
			prevPoint = FMapUtil.createPoint(subType, pts.get(i));
		}
		return multi;

	}

	public static Surface ptsToPolygon(List pts, int subType)
			throws CreateGeometryException {


		if (pts.size() < 3) {
			throw new IllegalArgumentException();
		}

		Point cur;
		Surface surface = (Surface) gManager.create(Geometry.TYPES.SURFACE,
				subType);

		Iterator iter = pts.iterator();
		while (iter.hasNext()) {
			cur = createPoint(subType, iter.next());
			surface.addVertex(cur);
		}
		return surface;
	}

	public static Point createPoint(int subType,
			Object point)
			throws CreateGeometryException {
		Point result = (Point) gManager.create(Geometry.TYPES.POINT, subType);
		if (point instanceof double[]) {
			result.setCoordinates((double[]) point);
		} else if (point instanceof Point2D) {
			Point2D p = (Point2D) point;
			result.setX(p.getX());
			result.setY(p.getY());

		} else if (point instanceof IDwgVertex) {
			result.setCoordinates(((IDwgVertex) point).getPoint());
		} else {
			throw new IllegalArgumentException();
		}
		return result;
	}

}
