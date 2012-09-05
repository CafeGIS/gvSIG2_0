package com.iver.cit.gvsig.project.documents.view.toolListeners.snapping.snappers;

import java.awt.Graphics;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.primitive.Arc;
import org.gvsig.fmap.geom.primitive.Circle;
import org.gvsig.fmap.geom.primitive.Ellipse;
import org.gvsig.fmap.geom.primitive.Spline;
import org.gvsig.fmap.geom.util.Converter;
import org.gvsig.fmap.mapcontrol.tools.snapping.snappers.AbstractSnapper;
import org.gvsig.fmap.mapcontrol.tools.snapping.snappers.ISnapperVectorial;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineSegment;


/**
 * Tangent point snapper.
 *
 * @author Vicente Caballero Navarro
 */
public class TangentPointSnapper extends AbstractSnapper
    implements ISnapperVectorial {
	/* (non-Javadoc)
     * @see com.iver.cit.gvsig.gui.cad.snapping.ISnapper#getSnapPoint(Point2D point,
     * IGeometry geom,double tolerance, Point2D lastPointEntered)
     */
    public Point2D getSnapPoint(Point2D point, Geometry geom,
        double tolerance, Point2D lastPointEntered) {
        if (!(geom instanceof Circle ||
                geom instanceof Arc ||
                geom instanceof Ellipse ||
                geom instanceof Spline)) {
            return null;
        }

        Point2D resul = null;
        Coordinate c = new Coordinate(point.getX(), point.getY());

        PathIterator theIterator = geom.getPathIterator(null,
                Converter.FLATNESS);
        double[] theData = new double[6];
        double minDist = tolerance;
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

                // System.out.println("SEG_LINETO");
                Coordinate to = new Coordinate(theData[0], theData[1]);
                LineSegment line = new LineSegment(from, to);
                Coordinate closestPoint = line.closestPoint(c);
                double dist = c.distance(closestPoint);

                if ((dist < minDist)) {
                    resul = new Point2D.Double(closestPoint.x, closestPoint.y);
                    minDist = dist;
                }

                from = to;

                break;

            case PathIterator.SEG_CLOSE:
                line = new LineSegment(from, first);
                closestPoint = line.closestPoint(c);
                dist = c.distance(closestPoint);

                if ((dist < minDist)) {
                    resul = new Point2D.Double(closestPoint.x, closestPoint.y);
                    minDist = dist;
                }

                from = first;

                break;
            } //end switch

            theIterator.next();
        }

        return resul;
    }

    /* (non-Javadoc)
     * @see com.iver.cit.gvsig.gui.cad.snapping.ISnapper#getToolTipText()
     */
    public String getToolTipText() {
        return "tangent_point";
    }

    /* (non-Javadoc)
     * @see com.iver.cit.gvsig.gui.cad.snapping.ISnapper#draw(java.awt.Graphics, java.awt.geom.Point2D)
     */
    public void draw(Graphics g, Point2D pPixels) {
        g.setColor(getColor());

        int half = getSizePixels() / 2;
        g.drawLine((int) (pPixels.getX() - half),
            (int) (pPixels.getY() - half), (int) (pPixels.getX() + half),
            (int) (pPixels.getY() - half));
        g.drawOval((int) (pPixels.getX() - half),
            (int) (pPixels.getY() - half), getSizePixels(), getSizePixels());
    }


}
