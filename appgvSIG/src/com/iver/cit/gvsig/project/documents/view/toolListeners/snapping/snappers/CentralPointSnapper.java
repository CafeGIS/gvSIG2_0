package com.iver.cit.gvsig.project.documents.view.toolListeners.snapping.snappers;

import java.awt.Graphics;
import java.awt.geom.Point2D;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.handler.CenterHandler;
import org.gvsig.fmap.geom.handler.Handler;
import org.gvsig.fmap.mapcontrol.tools.snapping.snappers.AbstractSnapper;
import org.gvsig.fmap.mapcontrol.tools.snapping.snappers.ISnapperVectorial;


/**
 * Central point snapper.
 *
 * @author Vicente Caballero Navarro
 */
public class CentralPointSnapper extends AbstractSnapper
    implements ISnapperVectorial {
	/* (non-Javadoc)
     * @see com.iver.cit.gvsig.gui.cad.snapping.ISnapper#getSnapPoint(Point2D point,
     * IGeometry geom,double tolerance, Point2D lastPointEntered)
     */
	public Point2D getSnapPoint(Point2D point, Geometry geom,
        double tolerance, Point2D lastPointEntered) {
        Point2D resul = null;

        Handler[] handlers = geom.getHandlers(Geometry.SELECTHANDLER);

        double minDist = tolerance;

        for (int j = 0; j < handlers.length; j++) {
            if (handlers[j] instanceof CenterHandler) {
                Point2D handlerPoint = handlers[j].getPoint();
                double dist = handlerPoint.distance(point);

                if ((dist < minDist)) {
                    resul = handlerPoint;
                    minDist = dist;
                }
            }
        }

        return resul;
    }
	/* (non-Javadoc)
     * @see com.iver.cit.gvsig.gui.cad.snapping.ISnapper#getToolTipText()
     */
    public String getToolTipText() {
        return "central_point";
    }

    /* (non-Javadoc)
     * @see com.iver.cit.gvsig.gui.cad.snapping.ISnapper#draw(java.awt.Graphics, java.awt.geom.Point2D)
     */
    public void draw(Graphics g, Point2D pPixels) {
        g.setColor(getColor());

        int half = getSizePixels() / 2;
        g.drawOval((int) (pPixels.getX() - half),
            (int) (pPixels.getY() - half), getSizePixels(), getSizePixels());
    }
}
