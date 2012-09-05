package org.gvsig.fmap.mapcontrol.tools.snapping;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.gvsig.fmap.geom.operation.tojts.ToJTS;
import org.gvsig.fmap.mapcontrol.tools.snapping.snappers.ISnapperGeometriesVectorial;
import org.gvsig.fmap.mapcontrol.tools.snapping.snappers.ISnapperVectorial;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.index.ItemVisitor;


/**
 * SnappingVisitor with geometries.
 *
 * @author Vicente Caballero Navarro
 */
public class GeometriesSnappingVisitor extends SnappingVisitor
    implements ItemVisitor {
    private ArrayList geometries = new ArrayList();
    private GeometryFactory geometryFactory = new GeometryFactory();

    public GeometriesSnappingVisitor(ISnapperVectorial snapper, Point2D point,
        double mapTolerance, Point2D lastPointEntered) {
        super(snapper, point, mapTolerance, lastPointEntered);
    }

    public void visitItem(Object item) {
        try {
	    	org.gvsig.fmap.geom.Geometry geom = (org.gvsig.fmap.geom.Geometry) item;

	    	Geometry geometry = (Geometry)geom.invokeOperation(ToJTS.CODE,null);//toJTSGeometry();
	        double distance = geometry.distance(geometryFactory.createPoint(
	                    new Coordinate(queryPoint.getX(), queryPoint.getY())));

	        if (distance < tolerance) {
	            geometries.add(geom);
	        }
        }catch (Exception e) {
		}
    }

    public Point2D getSnapPoint() {
        if (geometries.isEmpty()) {
            return null;
        }

        org.gvsig.fmap.geom.Geometry[] geoms = (org.gvsig.fmap.geom.Geometry[]) geometries.toArray(new Geometry[0]);
        ((ISnapperGeometriesVectorial) snapper).setGeometries(geoms);

        Point2D result = null;

        for (int i = 0; i < geoms.length; i++) {
            result = snapper.getSnapPoint(queryPoint, geoms[i], tolerance,
                    lastPointEntered);

            if (result != null) {
                return result;
            }
        }

        return result;
    }
}
