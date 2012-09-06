/*
 * Cresques Mapping Suite. Graphic Library for constructing mapping applications.
 *
 * Copyright (C) 2004-5.
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
 * cresques@gmail.com
 */
package org.cresques.impl.geo;

import org.cresques.cts.ICoordTrans;
import org.cresques.cts.IProjection;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;


/**
 * Transformada para cambios de proyección
 *
 * @author "Luis W. Sevilla" <sevilla_lui@gva.es>
 */
public class ReProjection implements ICoordTrans {
    Projection pOrig;
    Projection pDest;

    public ReProjection(Projection pOrig, Projection pDest) {
        this.pOrig = pOrig;
        this.pDest = pDest;
    }

    public ICoordTrans getInverted() {
        return new ReProjection(pDest, pOrig);
    }

    public IProjection getPOrig() {
        return pOrig;
    }

    public IProjection getPDest() {
        return pDest;
    }

    public Point2D convert(Point2D ptOrig, Point2D ptDest) {
        if (pOrig.getClass() == UtmZone.class) {
            GeoPoint pt1 = null;
            pt1 = (GeoPoint) ((UtmZone) pOrig).toGeo((UtmPoint) ptOrig);

            if (pDest.getClass() == UtmZone.class) {
                ((UtmZone) pDest).fromGeo(pt1, (UtmPoint) ptDest,
                                          (UtmZone) pDest);
            } else if (pDest.getClass() == Geodetic.class) {
                ptDest.setLocation(pt1.getX(), pt1.getY());
                ((GeoPoint) ptDest).proj = pt1.proj;
            } else if (pDest.getClass() == Mercator.class) {
                ((Mercator) pDest).fromGeo(pt1, (ProjPoint) ptDest);
            }
        } else if (pOrig.getClass() == Geodetic.class) {
            if (pDest.getClass() == UtmZone.class) {
                ((UtmZone) pDest).fromGeo((GeoPoint) ptOrig, (UtmPoint) ptDest,
                                          (UtmZone) pDest);
            } else if (pDest.getClass() == Mercator.class) {
                ((Mercator) pDest).fromGeo((GeoPoint) ptOrig, (ProjPoint) ptDest);
            }
        } else if (pOrig.getClass() == Mercator.class) {
            GeoPoint pt1 = null;
            pt1 = (GeoPoint) ((Mercator) pOrig).toGeo((ProjPoint) ptOrig);

            if (pDest.getClass() == UtmZone.class) {
                ((UtmZone) pDest).fromGeo(pt1, (UtmPoint) ptDest,
                                          (UtmZone) pDest);
            } else if (pDest.getClass() == Geodetic.class) {
                ptDest.setLocation(pt1.getX(), pt1.getY());
                ((ProjPoint) ptDest).proj = pt1.proj;
            }
        }

        return ptDest;
    }

    /* (non-Javadoc)
     * @see org.cresques.cts.ICoordTrans#convert(java.awt.geom.Rectangle2D)
     */
    public Rectangle2D convert(Rectangle2D rect) {
        Point2D pt1 = new Point2D.Double(rect.getMinX(), rect.getMinY());
        Point2D pt2 = new Point2D.Double(rect.getMaxX(), rect.getMaxY());
        pt1 = convert(pt1, null);
        pt2 = convert(pt2, null);
        rect = new Rectangle2D.Double();
        rect.setFrameFromDiagonal(pt1, pt2);

        return rect;
    }
}
