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
package org.cresques.px.dxf;

import java.awt.geom.Point2D;
import java.util.Iterator;

import org.cresques.cts.IProjection;
import org.cresques.io.DxfGroup;


/**
 * Entidad SPLINE de un fichero DXF.
 * @author "Luis W. Sevilla" <sevilla_lui@gva.es>
 */
public class DxfSpline extends DxfPolyline {

    /**
     * Constructor de DxfSpline.
     * @param proj, proyección cartográfica en la que se encuentra el DxfSpline.
     * @param layer, capa del DXF en la que se encuentra el DxfSpline.
     */
    public DxfSpline(IProjection proj, DxfLayer layer) {
        super(proj, layer);
    }

    /* (non-Javadoc)
     * @see org.cresques.px.dxf.DxfEntity#toDxfFileString()
     */
    /**
     * Actualmente en desarrollo. Hay que terminarlo para que de verdad escriba un
     * DxfSpline.
     */
    public String toDxfString() {
        StringBuffer sb = null;
        sb = new StringBuffer(DxfGroup.toString(0, "LWPOLYLINE"));
        sb.append(DxfGroup.toString(8, layer.getName()));
        sb.append(DxfGroup.toString(62, dxfColor));
        sb.append(DxfGroup.toString(100, "AcDbPolyline"));
        sb.append(DxfGroup.toString(90, pts.size()));

        Point2D pt = null;
        Iterator iter = pts.iterator();

        while (iter.hasNext()) {
            pt = (Point2D) iter.next();
            sb.append(DxfGroup.toString(10, pt.getX(), 6));
            sb.append(DxfGroup.toString(20, pt.getY(), 6));
        }

        return sb.toString();
    }
}
