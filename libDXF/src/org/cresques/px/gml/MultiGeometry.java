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
package org.cresques.px.gml;

import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.Vector;

import org.cresques.cts.ICoordTrans;
import org.cresques.cts.IProjection;
import org.cresques.geo.ViewPortData;
import org.cresques.px.Extent;


/**
 * Clase base para geometrias múltiples.
 * @author "Luis W. Sevilla" <sevilla_lui@gva.es>
 */
public class MultiGeometry extends Geometry {
    Vector data = null;

    public MultiGeometry() {
        super();
        data = new Vector();
    }

    public void add(Geometry geometry) {
        extent.add(geometry.getExtent());
        data.add(geometry);
    }

    public IProjection getProjection() {
        return proj;
    }

    public void setProjection(IProjection p) {
        proj = p;
    }

    public void reProject(ICoordTrans rp) {
        extent = new Extent();

        Geometry geometry;
        Iterator iter = data.iterator();

        while (iter.hasNext()) {
            geometry = (Geometry) iter.next();
            geometry.reProject(rp);
            extent.add(geometry.getExtent());
        }

        setProjection(rp.getPDest());
    }

    public void draw(Graphics2D g, ViewPortData vp) {
        Geometry geometry;
        Iterator iter = data.iterator();

        while (iter.hasNext()) {
            geometry = (Geometry) iter.next();
            geometry.draw(g, vp);
        }
    }
}
