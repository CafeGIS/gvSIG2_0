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


/**
 * Medium point snapper.
 *
 * @author Vicente Caballero Navarro
 */
public class MediumPointSnapper extends AbstractSnapper
    implements ISnapperVectorial {
	/* (non-Javadoc)
     * @see com.iver.cit.gvsig.gui.cad.snapping.ISnapper#getSnapPoint(Point2D point,
     * IGeometry geom,double tolerance, Point2D lastPointEntered)
     */
    public Point2D getSnapPoint(Point2D point, Geometry geom,
        double tolerance, Point2D lastPointEntered) {
        if (geom instanceof Circle ||
                geom instanceof Arc ||
                geom instanceof Ellipse ||
                geom instanceof Spline) {
            return null;
        }

        Point2D resul = null;
        Coordinate c = new Coordinate(point.getX(), point.getY());

        PathIterator theIterator = geom.getPathIterator(null,
                Converter.FLATNESS); //polyLine.getPathIterator(null, flatness);
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

                Coordinate to = new Coordinate(theData[0], theData[1]);
                Coordinate mediumPoint = new Coordinate((to.x + from.x) / 2,
                        (to.y + from.y) / 2);
                double dist = c.distance(mediumPoint);

                if ((dist < minDist)) {
                    resul = new Point2D.Double(mediumPoint.x, mediumPoint.y);
                    minDist = dist;
                }

                from = to;

                break;

            case PathIterator.SEG_CLOSE:
                mediumPoint = new Coordinate((first.x + from.x) / 2,
                        (first.y + from.y) / 2);
                dist = c.distance(mediumPoint);

                if ((dist < minDist)) {
                    resul = new Point2D.Double(mediumPoint.x, mediumPoint.y);
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
        return "medium_point";
    }

    /* (non-Javadoc)
     * @see com.iver.cit.gvsig.gui.cad.snapping.ISnapper#draw(java.awt.Graphics, java.awt.geom.Point2D)
     */
    public void draw(Graphics g, Point2D pPixels) {
        g.setColor(getColor());

        int half = getSizePixels() / 2;
        int x1 = (int) (pPixels.getX() - half);
        int x2 = (int) (pPixels.getX() + half);
        int x3 = (int) pPixels.getX();
        int y1 = (int) (pPixels.getY() - half);
        int y2 = (int) (pPixels.getY() + half);

        g.drawLine(x1, y2, x2, y2);
        g.drawLine(x1, y2, x3, y1);
        g.drawLine(x2, y2, x3, y1);
    }

}
