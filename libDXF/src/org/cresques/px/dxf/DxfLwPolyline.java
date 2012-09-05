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
import java.util.Vector;

import org.cresques.cts.IProjection;
import org.cresques.io.DxfGroup;


/**
 * Entidad LWPOLYLINE de un fichero DXF.
 * @author "Luis W. Sevilla" <sevilla_lui@gva.es>
 * @author jmorell
 */
public class DxfLwPolyline extends DxfPolyline {
    
    /**
     * Constructor de DxfLwPolyline.
     * @param proj, proyección cartográfica en la que se encuentra la DxfLwPolyline.
     * @param layer, capa del DXF en la que se encuentra la DxfLwPolyline.
     */
    public DxfLwPolyline(IProjection proj, DxfLayer layer) {
        super(proj, layer);
    }

    /**
     * Permite la escritura de entidades DxfLwPolyline en un fichero DXF2000.
     * @return String, la cadena que se escribirá en el fichero con la información
     * de la DxfLwPolyline.
     */
    /**
     * 050302, jmorell: Si el 90 no está antes que el 70, Autocad 2004 no cierra
     * las polilíneas.
     */
    public String toDxfString() {
        StringBuffer sb = null;
        sb = new StringBuffer(DxfGroup.toString(0, "LWPOLYLINE"));
        sb.append(DxfGroup.toString(5, getHandle()));
        sb.append(DxfGroup.toString(100, "AcDbEntity"));
        sb.append(DxfGroup.toString(8, layer.getName()));
        sb.append(DxfGroup.toString(62, dxfColor));
        sb.append(DxfGroup.toString(100, "AcDbPolyline"));
        sb.append(DxfGroup.toString(90, pts.size()));
        sb.append(DxfGroup.toString(70, flags));
        sb.append(DxfGroup.toString(38, new Double(getElevation())));

        Point2D pt = null;
        double bulge;
        Vector bulges = getBulges();

        /*Iterator iter = pts.iterator();
        while (iter.hasNext()) {
                pt = (Point2D) iter.next();
                sb.append( DxfGroup.toString(10, pt.getX(), 6) );
                sb.append( DxfGroup.toString(20, pt.getY(), 6) );
        }*/
        for (int i = 0; i < pts.size(); i++) {
            pt = (Point2D) pts.get(i);
            bulge = ((Double) bulges.get(i)).doubleValue();
            sb.append(DxfGroup.toString(10, pt.getX(), 6));
            sb.append(DxfGroup.toString(20, pt.getY(), 6));
            sb.append(DxfGroup.toString(42, bulge, 6));
        }

        return sb.toString();
    }
}
