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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

import org.cresques.cts.ICoordTrans;
import org.cresques.cts.IProjection;
import org.cresques.geo.ViewPortData;
import org.cresques.io.DxfGroup;
import org.cresques.px.Extent;


/**
 * Entidad LINE de un fichero DXF.
 * @author "Luis W. Sevilla" <sevilla_lui@gva.es>* @author administrador
 * @author jmorell
 */
public class DxfLine extends DxfEntity {
    final static Color baseColor = new Color(255, 106, 121);
    Point2D[] pts;
    GeneralPath gp = null;
    private Color color = baseColor; //Color(255,214,132,255);

    /**
     * Constructor de DxfLine.
     * @param proj, proyección cartográfica en la que se encuentra la DxfLine.
     * @param layer, capa del DXF en la que se encuentra la DxfLine.
     * @param p1, punto inicial de la línea.
     * @param p2, punto final de la línea.
     */
    public DxfLine(IProjection proj, DxfLayer layer, Point2D p1, Point2D p2) {
        super(proj, layer);
        extent = new Extent(p1, p2);
        pts = new Point2D[2];
        pts[0] = p1;
        pts[1] = p2;
    }

    /**
     * Devuelve el color de la DxfLine.
     * @return Color
     */
    public Color c() {
        return color;
    }

    /**
     * Establece el color de la DxfLine.
     * @param color
     * @return Color
     */
    public Color c(Color color) {
        this.color = color;

        return color;
    }

    /**
     * Permite reproyectar una DxfLine dado un conjunto de coordenadas de transformación.
     * @param rp, coordenadas de transformación.
     */
    public void reProject(ICoordTrans rp) {
        Point2D[] savePts = pts;

        pts = new Point2D[2];
        extent = new Extent();

        Point2D ptDest = null;

        for (int i = 0; i < savePts.length; i++) {
            ptDest = rp.getPDest().createPoint(0.0, 0.0);
            ptDest = rp.convert((Point2D) savePts[i], ptDest);
            pts[i] = ptDest;
            extent.add(ptDest);
        }

        setProjection(rp.getPDest());
    }

    /**
     * Permite dibujar una DxfLine.
     */
    public void draw(Graphics2D g, ViewPortData vp) {
        if (dxfColor == AcadColor.BYLAYER) {
            g.setColor(layer.getColor());
        } else {
            g.setColor(AcadColor.getColor(dxfColor));
        }

        newGP(vp);
        g.draw(gp);
    }

    /**
     * Permite generar un GeneralPath partiendo del array de Point2D que conforma el
     * DxfLine.
     * @param vp
     */
    private void newGP(ViewPortData vp) {
        //if (gp != null) return;
        Point2D.Double pt0 = new Point2D.Double(0.0, 0.0);
        Point2D.Double pt1 = new Point2D.Double(0.0, 0.0);
        vp.mat.transform((Point2D) pts[0], pt0);
        vp.mat.transform((Point2D) pts[1], pt1);
        gp = new GeneralPath();
        gp.moveTo((float) pt0.getX(), (float) pt0.getY());
        gp.lineTo((float) pt1.getX(), (float) pt1.getY());
    }

    /**
     * Permite la escritura de entidades DxfLine en un fichero DXF2000.
     * @return String, la cadena que se escribirá en el fichero con la información
     * de la DxfLine.
     */
    public String toDxfString() {
        StringBuffer sb = null;
        sb = new StringBuffer(DxfGroup.toString(0, "LINE"));
        sb.append(DxfGroup.toString(5, getHandle()));
        sb.append(DxfGroup.toString(100, "AcDbEntity"));
        sb.append(DxfGroup.toString(8, layer.getName()));
        sb.append(DxfGroup.toString(62, dxfColor));
        sb.append(DxfGroup.toString(100, "AcDbLine"));
        sb.append(DxfGroup.toString(10, pts[0].getX(), 6));
        sb.append(DxfGroup.toString(20, pts[0].getY(), 6));
        sb.append(DxfGroup.toString(11, pts[1].getX(), 6));
        sb.append(DxfGroup.toString(21, pts[1].getY(), 6));

        return sb.toString();
    }

    /**
     * Devuelve el array de puntos que conforman el DxfLine.
     * @return Point2D[], puntos del DxfLine.
     */
    public Point2D[] getPts() {
        return pts;
    }
}
