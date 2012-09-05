/* gvSIG. Geographic Information System of the Valencian Government
 *
 * Copyright (C) 2007-2008 Infrastructures and Transports Department
 * of the Valencian Government (CIT)
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 * 
 */

/*
 * AUTHORS (In addition to CIT):
 * 2008 Prodevelop S.L. main development
 */

package org.gvsig.geocoding.geommatches;

import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.primitive.Point;
import org.gvsig.fmap.geom.util.Converter;
import org.gvsig.fmap.geom.util.UtilFunctions;
import org.gvsig.geocoding.result.DissolveResult;
import org.gvsig.geocoding.result.ScoredFeature;
import org.gvsig.geocoding.result.impl.DefaultDissolveResult;
import org.gvsig.tools.locator.LocatorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.linearref.LengthIndexedLine;
import com.vividsolutions.jts.operation.overlay.OverlayOp;

/**
 * This class has the utilities to work with geometries and search the geocoding
 * point
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */

public class MatcherUtils {

	private static final Logger log = LoggerFactory
			.getLogger(MatcherUtils.class);

	public static final String LINES = "LINES";
	public static final String POLYS = "POLYS";

	/**
	 * Get internal point from one polygon geometry
	 * 
	 * @param geomGV
	 * @return
	 */
	public static Point internalPointGeometry(Geometry geomGV) {

		if (geomGV != null) {
			com.vividsolutions.jts.geom.Geometry geomJTS = Converter
					.geometryToJts(geomGV);
			if (geomJTS != null) {
				com.vividsolutions.jts.geom.Point pto = geomJTS
						.getInteriorPoint();
				if (pto != null) {
					return getPoint(pto.getX(), pto.getY());
				}
			}
		}
		return null;
	}

	/**
	 * parse geometries of gvSIG model to JTS model
	 * 
	 * @param geoms
	 *            list of gvSIG geometries
	 * @return list of JTS geometries
	 */
	public static List<com.vividsolutions.jts.geom.Geometry> parseGeomsGVToJTS(
			List<Geometry> geoms) {

		List<com.vividsolutions.jts.geom.Geometry> list = new ArrayList<com.vividsolutions.jts.geom.Geometry>();

		if (geoms != null && geoms.size() > 0) {
			com.vividsolutions.jts.geom.Geometry ge = null;
			for (Geometry geometry : geoms) {
				ge = Converter.geometryToJts(geometry);
				list.add(ge);
			}
		}
		return list;
	}

	/**
	 * parse a list of gvSIG geometries to JTS geometries
	 * 
	 * @param geoms
	 *            list gvSIG geometries to JTS geometries
	 * @return
	 * @throws CreateGeometryException 
	 */
	public static List<Geometry> parseGeomsJTSToGV(
			List<com.vividsolutions.jts.geom.Geometry> geoms) throws CreateGeometryException {

		List<Geometry> list = new ArrayList<Geometry>();

		if (geoms != null && geoms.size() > 0) {
			for (com.vividsolutions.jts.geom.Geometry geometry : geoms) {
				list.add(Converter.jtsToGeometry(geometry));
			}
		}
		return list;
	}

	/**
	 * This method intersect two lines and return the intersection point. If
	 * result is null, two lines doesn't intersect
	 * 
	 * @param geom1Jts
	 * @param geom2Jts
	 * @return
	 */
	public static Point intersectTwoLinesJTS(
			com.vividsolutions.jts.geom.Geometry geom1Jts,
			com.vividsolutions.jts.geom.Geometry geom2Jts) {

		com.vividsolutions.jts.geom.Geometry interBBoxJTS = null;
		com.vividsolutions.jts.geom.Geometry geomPointJTS = null;

		if (geom1Jts != null && geom2Jts != null) {

			interBBoxJTS = OverlayOp.overlayOp(geom1Jts.getEnvelope(), geom2Jts
					.getEnvelope(), OverlayOp.INTERSECTION);
			if (interBBoxJTS.getGeometryType().compareTo("LineString") == 0
					|| interBBoxJTS.getGeometryType().compareTo("Polygon") == 0) {
				log.debug("Intersect: Intersect two BBOX");
				geomPointJTS = OverlayOp.overlayOp(geom1Jts, geom2Jts,
						OverlayOp.INTERSECTION);

				if (geomPointJTS.getGeometryType().compareTo("Point") == 0) {
					log.debug("Intersect: Intersect in the point X= "
							+ geomPointJTS.getCoordinate().x + " Y= "
							+ geomPointJTS.getCoordinate().y);
					Point pto = getPoint(geomPointJTS.getCoordinate().x,
							geomPointJTS.getCoordinate().y);

					return pto;
				} else {
					log.debug("Intersect: Two lines don't intersect");
					return null;
				}
			} else {
				log.debug("Intersect: Two BBOX don't intersect");
				return null;
			}
		}
		log.debug("Some Geometries are NULL  ......");
		return null;
	}

	/**
	 * This method does the union the geometries (Features) with a attribute
	 * common
	 * 
	 * @param sFeats
	 * @param geomType
	 *            LINES, POLYS
	 * @return
	 * @throws DataException
	 */
	public static List<DissolveResult> dissolveGeomsJTS(
			List<ScoredFeature> sFeats, String geomType) throws DataException {

		List<DissolveResult> results = new ArrayList<DissolveResult>();

		// None elements
		if (sFeats.size() == 0) {
			return results;
		}

		// Only one element
		else if (sFeats.size() == 1) {
			DissolveResult result = new DefaultDissolveResult();
			Feature feat = null;
			try {
				feat = sFeats.get(0).getFeature();
				result.setGeom(feat.getDefaultGeometry());
				List<ScoredFeature> list = new ArrayList<ScoredFeature>();
				list.add(sFeats.get(0));
				result.setScoredFeatures(list);
				results.add(result);
			} catch (DataException e) {
				log.debug("Error getting the feature", e);
			}
			return results;
		}

		// more elements
		else {

			LinkedList<ScoredFeature> list = new LinkedList<ScoredFeature>();
			com.vividsolutions.jts.geom.Geometry geo1, geo2;

			for (ScoredFeature scoredFeature : sFeats) {
				list.add(scoredFeature);
			}

			// Iterate over the linked list
			while (list.size() > 0) {
				// Remove the first element of the list
				ScoredFeature sFeat = list.poll();

				// create first dissolve result and add it to the list
				DissolveResult result = new DefaultDissolveResult();
				ArrayList<ScoredFeature> dissolveList = new ArrayList<ScoredFeature>();
				dissolveList.add(sFeat);

				geo1 = Converter.geometryToJts(sFeat.getFeature()
						.getDefaultGeometry());

				ScoredFeature touchingSFeat = null;
				// LINES

				if (geomType.compareTo(LINES) == 0) {
					touchingSFeat = getFirstLineTouchingSF(geo1, list);
				}
				// POLYS
				if (geomType.compareTo(POLYS) == 0) {
					touchingSFeat = getFirstPolyTouchingSF(geo1, list);
				}

				while (touchingSFeat != null) {
					list.remove(touchingSFeat);
					geo2 = Converter.geometryToJts(touchingSFeat.getFeature()
							.getDefaultGeometry());

					geo1 = OverlayOp.overlayOp(geo1, geo2, OverlayOp.UNION);

					dissolveList.add(touchingSFeat);

					touchingSFeat = getFirstLineTouchingSF(geo1, list);
				}
				result.setJTSGeom(geo1);
				result.setScoredFeatures(dissolveList);
				results.add(result);
			}
		}
		return results;
	}

	/**
	 * Get first line of the list that it is touching the in geometry
	 * 
	 * @param geometry
	 * @param list
	 * @return
	 * @throws DataException
	 */
	private static ScoredFeature getFirstLineTouchingSF(
			com.vividsolutions.jts.geom.Geometry geometry,
			LinkedList<ScoredFeature> list) throws DataException {

		com.vividsolutions.jts.geom.Geometry geo2;
		for (ScoredFeature listedSF : list) {
			geo2 = Converter.geometryToJts(listedSF.getFeature()
					.getDefaultGeometry());
			if (geometry.touches(geo2))
				return listedSF;
		}
		return null;
	}

	/**
	 * Get first poly of the list that it is touching the in geometry
	 * 
	 * @param geometry
	 * @param list
	 * @return
	 * @throws DataException
	 */
	private static ScoredFeature getFirstPolyTouchingSF(
			com.vividsolutions.jts.geom.Geometry geometry,
			LinkedList<ScoredFeature> list) throws DataException {

		com.vividsolutions.jts.geom.Geometry geo2;
		com.vividsolutions.jts.geom.Geometry geo3;
		for (ScoredFeature listedSF : list) {
			geo2 = Converter.geometryToJts(listedSF.getFeature()
					.getDefaultGeometry());
			geo3 = geometry.intersection(geo2);
			for (int i = 0; i < geo3.getNumGeometries(); i++) {
				int dim = geo3.getGeometryN(i).getDimension();
				if (dim == 1) {
					return listedSF;
				}
			}
		}
		return null;
	}

	/**
	 * This method group geometries to one attribute
	 * 
	 * @param desc
	 * @param features
	 * @return
	 */
	public static HashMap<String, List<ScoredFeature>> groupScoredFeaturesByAttribute(
			String desc, List<ScoredFeature> features) {

		HashMap<String, List<ScoredFeature>> groups = new HashMap<String, List<ScoredFeature>>();
		Iterator<ScoredFeature> it = features.iterator();
		// Go for all geometries of the collection
		while (it.hasNext()) {
			// Get feature
			ScoredFeature sFeat = (ScoredFeature) it.next();
			Feature feat = null;
			try {
				feat = sFeat.getFeature();
				String key = feat.get(desc).toString();
				// Store the geometries for attribute in the List
				boolean contiene = groups.containsKey(key);
				if (!contiene) {
					List<ScoredFeature> featss = new ArrayList<ScoredFeature>();
					featss.add(sFeat);
					groups.put(key, featss);
				} else {
					((List<ScoredFeature>) groups.get(key)).add(sFeat);
				}
			} catch (DataException e) {
				log.debug("Error clustering element", e);
				continue;
			}
		}
		return groups;
	}

	/**
	 * Calculate the position inside single line from distance
	 * 
	 * @param geomJTS
	 * @param distance
	 * @return
	 */
	public static Point getLinePositionFromDistance(
			com.vividsolutions.jts.geom.Geometry geomJTS, double distance) {

		if (geomJTS != null) {
			LengthIndexedLine lenline = new LengthIndexedLine(geomJTS);
			if (distance < 0) {
				distance = 0.0;
			}
			if (distance > geomJTS.getLength()) {
				distance = geomJTS.getLength();
			}
			if (lenline.isValidIndex(distance)) {
				Coordinate coors = lenline.extractPoint(distance);

				return getPoint(coors.x, coors.y);

			}
		}
		return null;
	}

	/**
	 * Calculate the position inside single line from relative distance
	 * 
	 * @param geomJTS
	 * @param distance
	 * @return
	 */
	public static Point getLinePositionFromRelativeDistance(
			com.vividsolutions.jts.geom.Geometry geomJTS, int relative) {
		if (geomJTS != null) {
			double totaldistance = geomJTS.getLength();
			LengthIndexedLine lenline = new LengthIndexedLine(geomJTS);

			Coordinate coors = null;

			if (relative < 0) {
				coors = lenline.extractPoint(0);
				return getPoint(coors.x, coors.y);
			} else if (relative <= 100 && relative >= 0) {
				double dist = (relative * totaldistance) / 100.0;
				coors = lenline.extractPoint(dist);
				return getPoint(coors.x, coors.y);
			} else {
				coors = lenline.extractPoint(totaldistance);
				return getPoint(coors.x, coors.y);
			}
		}
		return null;
	}

	/**
	 * This method calculates a point perpendicular to the line at a distance
	 * 
	 * @param inicio
	 * @param fin
	 * @param linePoint
	 * @param dist
	 * @return
	 */
	public static Point getPerpendicularPointFromLine(Point inicio, Point fin,
			Point linePoint, double dist) {

		java.awt.geom.Point2D pto1 = new java.awt.geom.Point2D.Double(inicio
				.getX(), inicio.getY());
		java.awt.geom.Point2D pto2 = new java.awt.geom.Point2D.Double(fin
				.getX(), fin.getY());

		java.awt.geom.Point2D perpPoint = new java.awt.geom.Point2D.Double(
				linePoint.getX(), linePoint.getY());

		java.awt.geom.Point2D[] p = UtilFunctions.getPerpendicular(pto1, pto2,
				perpPoint);
		java.awt.geom.Point2D unit = UtilFunctions.getUnitVector(p[0], p[1]);

		java.awt.geom.Point2D res = new java.awt.geom.Point2D.Double(perpPoint
				.getX()
				+ (unit.getX() * dist), perpPoint.getY() + (unit.getY() * dist));

		return getPoint(res.getX(), res.getY());
	}

	/**
	 * Calculate the position in the line with offset
	 * 
	 * @param geoms
	 * @param linePoint
	 * @param dist
	 * @return
	 */
	public static Point getOffsetPosition(Geometry[] geoms, Point linePoint,
			double dist) {

		for (int j = 0; j < geoms.length; j++) {
			double[] coords = new double[6];
			Point inicio = null;
			Point fin = null;

			boolean inter = geoms[j].intersects(linePoint.getBounds2D());
			if (inter) {
				PathIterator iter = geoms[j].getGeneralPath().getPathIterator(
						null);
				iter.currentSegment(coords);
				inicio = getPoint(coords[0], coords[1]);

				iter.next();
				iter.currentSegment(coords);
				fin = getPoint(coords[0], coords[1]);

				log.debug("Return point with offset");
				return MatcherUtils.getPerpendicularPointFromLine(inicio, fin,
						linePoint, dist);
			}
		}
		log.debug("Return original point without offset");
		return linePoint;
	}

	/**
	 * get geometry dimension
	 * 
	 * @param geom
	 * @return
	 */
	public static int getGeometryDimension(Geometry geom) {
		com.vividsolutions.jts.geom.Geometry geomJTS = Converter
				.geometryToJts(geom);
		return geomJTS.getDimension();
	}

	/**
	 * Get point 2D from x and y coordinates
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public static Point getPoint(double x, double y) {
		Point pto = null;
		try {
			pto = GeometryLocator.getGeometryManager().createPoint(x, y,
					Geometry.SUBTYPES.GEOM2D);
		} catch (LocatorException e) {
			log.error("Error getting the geometry locator", e);
			return null;
		} catch (CreateGeometryException e) {
			log.error("Error creating geometry", e);
			return null;
		}
		return pto;
	}

}
