package org.gvsig.fmap.data.feature.db.jdbc.postgresql;

import java.sql.SQLException;

import org.gvsig.fmap.geom.GeometryFactory;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.postgis.Geometry;
import org.postgis.LineString;
import org.postgis.LinearRing;
import org.postgis.MultiLineString;
import org.postgis.MultiPolygon;
import org.postgis.PGgeometry;
import org.postgis.Point;
import org.postgis.Polygon;

/**
 * Codigo recogido (en parte) de la lista de desarrollo de gvSIG
 * Enviado por: MAU ingmau00 en gmail.com
 * Fecha: Lun Ago 13 15:03:06 CEST 2007
 * Asunto: [Gvsig_desarrolladores] Crear una capa nueva
 *
 * @author jmvivo
 *
 */

public class PostGIS2Geometry {


	public static org.gvsig.fmap.geom.Geometry getGeneralPath(PGgeometry geom) throws
	SQLException{


		GeometryFactory gFactory = GeometryManager.getInstance().getGeometryFactory();

		GeneralPathX gp = new GeneralPathX();
		org.gvsig.fmap.geom.Geometry resultGeom = null;
		if (geom.getGeoType() == Geometry.POINT) {
			Point p = (Point) geom.getGeometry();
			resultGeom = gFactory.createPoint2D(p.x, p.y);

		} else if ( geom.getGeoType() == Geometry.LINESTRING) {
			gp = lineString2GP((LineString) geom.getGeometry());
		} else if (geom.getGeoType() == Geometry.MULTILINESTRING) {
			MultiLineString mls = (MultiLineString)  geom.getGeometry();

			for (int j = 0; j < mls.numLines(); j++) {
				gp.append(lineString2GP(mls.getLine(j)), false);
			}
			resultGeom = gFactory.createPolyline2D(gp);
		} else if (geom.getGeoType() == Geometry.POLYGON ) {
			gp = polygon2GP((Polygon) geom.getGeometry());
			resultGeom = gFactory.createPolygon2D(gp);
		} else if (geom.getGeoType() == Geometry.MULTIPOLYGON) {
			MultiPolygon mp = (MultiPolygon) geom.getGeometry();

			for (int i = 0; i <  mp.numPolygons(); i++) {
				gp.append(polygon2GP(mp.getPolygon(i)), false);
			}
			resultGeom = gFactory.createPolygon2D(gp);
		} else if (geom.getGeoType() == Geometry.GEOMETRYCOLLECTION) {
			//FIXME: Falta Implementar!!!
			throw new RuntimeException("geometryCollections not supported by this driver");
		} else {
			throw new RuntimeException("Unknown datatype");
		}
		return resultGeom;
	}

	private static GeneralPathX polygon2GP(Polygon p) {
		GeneralPathX gp = new GeneralPathX();

		for (int r = 0; r < p.numRings(); r++) {
			gp.append(linearRing2GP(p.getRing(r)), false);
		}

		return gp;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param lr DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	private static GeneralPathX linearRing2GP(LinearRing lr) {
		Point p;
		GeneralPathX gp = new GeneralPathX();

		if ((p = lr.getPoint(0)) != null) {
			gp.moveTo(p.x, p.y);

			for (int i = 1; i < lr.numPoints(); i++) {
				p = lr.getPoint(i);
				gp.lineTo(p.x, p.y );
			}
		}

		gp.closePath();

		return gp;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param ls DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	private static GeneralPathX lineString2GP(LineString ls) {
		Point p;
		GeneralPathX gp = new GeneralPathX();

		if ((p = ls.getPoint(0)) != null) {
			gp.moveTo(p.x , p.y);

			for (int i = 1; i < ls.numPoints(); i++) {
				p = ls.getPoint(i);
				gp.lineTo(p.x, p.y);
			}
		}

		return gp;
	}
}

