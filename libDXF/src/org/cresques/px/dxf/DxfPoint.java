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

import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import org.cresques.cts.ICoordTrans;
import org.cresques.cts.IProjection;
import org.cresques.geo.ViewPortData;
import org.cresques.io.DxfGroup;
import org.cresques.px.Extent;


/**
 * Entidad POINT de un fichero DXF.
 * @author "Luis W. Sevilla" <sevilla_lui@gva.es>
 * @author jmorell
 */
public class DxfPoint extends DxfEntity {
    private Point2D pt;

    /**
     * Constructor de DxfPoint.
     * @param proj, proyección cartográfica en la que se encuentra el DxfPoint.
     * @param layer, capa del DXF en la que se encuentra el DxfPoint.
     */
    public DxfPoint(IProjection proj, DxfLayer layer) {
        super(proj, layer);
        extent = new Extent();
        pt = new Point2D.Double();
    }

    /**
     * Establece el punto de inserción del DxfPoint.
     * @param pt, punto de inserción.
     */
    public void setPt(Point2D pt) {
        this.pt = pt;
        extent.add(pt);
    }

    /**
     * Permite reproyectar un DxfPoint dado un conjunto de coordenadas de transformación.
     * @param rp, coordenadas de transformación.
     */
    public void reProject(ICoordTrans rp) {
        Point2D savePt = pt;

        pt = new Point2D.Double();
        extent = new Extent();

        Point2D ptDest = rp.getPDest().createPoint(0.0, 0.0);

        if (savePt == null) {
            ptDest = null;
        } else {
            ptDest = rp.convert((Point2D) savePt, ptDest);
            extent.add(ptDest);
        }

        pt = ptDest;
        setProjection(rp.getPDest());
    }

    /**
     * Permite dibujar un DxfPoint.
     */
    public void draw(Graphics2D g, ViewPortData vp) {
        //AffineTransform msave=g.getTransform();
        //g.setTransform(vp.mat);
        if (dxfColor == AcadColor.BYLAYER) {
            g.setColor(layer.getColor());
        } else {
            g.setColor(AcadColor.getColor(dxfColor));
        }

        Point2D ptT = new Point2D.Double(0, 0);
        vp.mat.transform(ptT, ptT);
        ptT.setLocation(pt.getX(), pt.getY());
        vp.mat.transform(ptT, ptT);
        g.drawLine((int) ptT.getX(), (int) ptT.getY(), (int) ptT.getX(),
                   (int) ptT.getY());

        //g.setTransform(msave);
    }

    /**
     * Permite la escritura de entidades DxfPoint en un fichero DXF2000.
     * @return String, la cadena que se escribirá en el fichero con la información
     * del DxfPoint.
     */
    public String toDxfString() {
        StringBuffer sb = null;
        sb = new StringBuffer(DxfGroup.toString(0, "POINT"));
        sb.append(DxfGroup.toString(5, getHandle()));
        sb.append(DxfGroup.toString(100, "AcDbEntity"));
        sb.append(DxfGroup.toString(8, layer.getName()));
        sb.append(DxfGroup.toString(62, dxfColor));
        sb.append(DxfGroup.toString(100, "AcDbPoint"));
        sb.append(DxfGroup.toString(10, pt.getX(), 6));
        sb.append(DxfGroup.toString(20, pt.getY(), 6));
        sb.append(DxfGroup.toString(30, 0.0, 6));

        return sb.toString();
    }

    /**
     * @return Returns the pt.
     */
    public Point2D getPt() {
        return pt;
    }
}
