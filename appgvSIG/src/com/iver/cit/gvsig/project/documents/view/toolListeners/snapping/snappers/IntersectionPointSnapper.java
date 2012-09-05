package com.iver.cit.gvsig.project.documents.view.toolListeners.snapping.snappers;

import java.awt.Graphics;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.primitive.Arc;
import org.gvsig.fmap.geom.primitive.Circle;
import org.gvsig.fmap.geom.primitive.Ellipse;
import org.gvsig.fmap.geom.primitive.Spline;
import org.gvsig.fmap.geom.util.Converter;
import org.gvsig.fmap.mapcontrol.tools.snapping.snappers.AbstractSnapper;
import org.gvsig.fmap.mapcontrol.tools.snapping.snappers.ISnapperGeometriesVectorial;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineSegment;


/**
 * Intersection point snapper.
 *
 * @author Vicente Caballero Navarro
 */
public class IntersectionPointSnapper extends AbstractSnapper
    implements ISnapperGeometriesVectorial {
    private Geometry[] geometries;
    public IntersectionPointSnapper() {
        System.err.println("Construido IntersectionPoinSnapper");
    }

    /* (non-Javadoc)
     * @see com.iver.cit.gvsig.gui.cad.snapping.ISnapper#getSnapPoint(Point2D point,
     * IGeometry geom,double tolerance, Point2D lastPointEntered)
     */
    public Point2D getSnapPoint(Point2D point, Geometry geom,
        double tolerance, Point2D lastPointEntered) {
    	if (geom instanceof Circle ||
    			geom instanceof Arc ||
    			geom instanceof Ellipse ||
    			geom instanceof Spline){
    				return null;
    	}
    	Point2D result = null;

        if (geometries == null) {
            return null;
        }

        for (int i = 0; i < geometries.length; i++) {
        	Point2D r = intersects(geom, geometries[i], point, tolerance);

            if (r != null) {
                result = r;
            }
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param g1 DOCUMENT ME!
     * @param g2 DOCUMENT ME!
     * @param point DOCUMENT ME!
     * @param tolerance DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private Point2D intersects(Geometry g1, Geometry g2, Point2D point,
        double tolerance) {
        Point2D resul = null;
        Coordinate c = new Coordinate(point.getX(), point.getY());
        PathIterator theIterator = g1.getPathIterator(null, Converter.FLATNESS);
        double[] theData = new double[6];
        Coordinate from = null;
        Coordinate first = null;
        LineSegment[] lines = getLines(g2);
        while (!theIterator.isDone()) {
        	int theType = theIterator.currentSegment(theData);

            switch (theType) {
            case PathIterator.SEG_MOVETO:
                from = new Coordinate(theData[0], theData[1]);
                first = from;

                break;

            case PathIterator.SEG_LINETO:

                Coordinate to = new Coordinate(theData[0], theData[1]);
                LineSegment segmentLine = new LineSegment(from,to);
                for (int i = 0; i < lines.length; i++) {
//                    if (lines[i].equals(segmentLine)) {
//                        continue;
//                    }
                    Coordinate intersects = segmentLine.intersection(lines[i]);
                    if (intersects == null || lines[i].equals(segmentLine)) {
                        continue;
                    }

                    double dist = c.distance(intersects);

                    if ((dist < tolerance)) {
                        resul = new Point2D.Double(intersects.x, intersects.y);
                        return resul;
                    }
                }

                from = to;

                break;

            case PathIterator.SEG_CLOSE:
            	 LineSegment segment = new LineSegment(from,first);

            	for (int i = 0; i < lines.length; i++) {
//                    if (lines[i].equals(segment)) {
//                        continue;
//                    }

                    Coordinate intersects = segment.intersection(lines[i]);

                    if (intersects == null) {
                        continue;
                    }

                    double dist = c.distance(intersects);

                    if ((dist < tolerance)) {
                        resul = new Point2D.Double(intersects.x, intersects.y);
                        return resul;
                    }
                }

                from = first;

                break;
            } //end switch

            theIterator.next();
        }
        return resul;
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private LineSegment[] getLines(Geometry g) {
        ArrayList lines = new ArrayList();
        PathIterator theIterator = g.getPathIterator(null, Converter.FLATNESS);
        double[] theData = new double[6];
        Coordinate from = null;
        Coordinate first = null;

        while (!theIterator.isDone()) {
            //while not done
            int theType = theIterator.currentSegment(theData);

            switch (theType) {
            case PathIterator.SEG_MOVETO:
                from = new Coordinate(theData[0], theData[1]);
                first = from;

                break;

            case PathIterator.SEG_LINETO:

                Coordinate to = new Coordinate(theData[0], theData[1]);
                LineSegment line = new LineSegment(from, to);
                lines.add(line);
                from = to;

                break;

            case PathIterator.SEG_CLOSE:
                line = new LineSegment(from, first);
                lines.add(line);
                from = first;

                break;
            } //end switch

            theIterator.next();
        }

        return (LineSegment[]) lines.toArray(new LineSegment[0]);
    }

    /* (non-Javadoc)
     * @see com.iver.cit.gvsig.gui.cad.snapping.ISnapper#draw(java.awt.Graphics, java.awt.geom.Point2D)
     */
    public void draw(Graphics g, Point2D pPixels) {
        g.setColor(getColor());

        int half = getSizePixels() / 2;
        int x1 = (int) (pPixels.getX() - half);
        int x2 = (int) (pPixels.getX() + half);
        int y1 = (int) (pPixels.getY() - half);
        int y2 = (int) (pPixels.getY() + half);

        g.drawLine(x1, y1, x2, y2);
        g.drawLine(x1, y2, x2, y1);
    }
    /**
     * DOCUMENT ME!
     *
     * @param geoms DOCUMENT ME!
     */
    public void setGeometries(Geometry[] geoms) {
        this.geometries = geoms;
    }

    /* (non-Javadoc)
     * @see com.iver.cit.gvsig.gui.cad.snapping.ISnapper#getToolTipText()
     */
    public String getToolTipText() {
        return "intersection_point";
    }
}
