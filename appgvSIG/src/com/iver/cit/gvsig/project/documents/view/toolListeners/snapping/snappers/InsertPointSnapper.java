package com.iver.cit.gvsig.project.documents.view.toolListeners.snapping.snappers;

import java.awt.Graphics;
import java.awt.geom.Point2D;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.mapcontrol.tools.snapping.snappers.AbstractSnapper;
import org.gvsig.fmap.mapcontrol.tools.snapping.snappers.ISnapperVectorial;



/**
 * Insert point snapper.
 *
 * @author Vicente Caballero Navarro
 */
public class InsertPointSnapper extends AbstractSnapper
    implements ISnapperVectorial {
	/* (non-Javadoc)
     * @see com.iver.cit.gvsig.gui.cad.snapping.ISnapper#getSnapPoint(Point2D point,
     * IGeometry geom,double tolerance, Point2D lastPointEntered)
     */
    public Point2D getSnapPoint(Point2D point, Geometry geom,
        double tolerance, Point2D lastPointEntered) {
        Point2D resul = null;

        return resul;
    }

    /* (non-Javadoc)
     * @see com.iver.cit.gvsig.gui.cad.snapping.ISnapper#getToolTipText()
     */
    public String getToolTipText() {
        return "insert_point";
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
        int y3 = (int) pPixels.getY();

        g.drawLine(x1, y1, x3, y1);
        g.drawLine(x1, y1, x1, y3);
        g.drawLine(x1, y3, x3, y3);
        g.drawLine(x3, y1, x3, y3);

        g.drawLine(x3, y3, x2, y3);
        g.drawLine(x3, y3, x3, y2);
        g.drawLine(x3, y2, x2, y2);
        g.drawLine(x2, y3, x2, y2);
    }
}
