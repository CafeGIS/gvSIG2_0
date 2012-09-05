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
import org.cresques.px.Extent;


/**
 * Entidad SOLID de un fichero DXF.
 * @author "Luis W. Sevilla" <sevilla_lui@gva.es>
 * @author jmorell
 */
public class DxfSolid extends DxfEntity {
    final static Color baseColor = new Color(69, 106, 121);

    //Vector points = null;
    Point2D[] pts;
    GeneralPath gp = null;
    boolean closed = true;
    private Color color = baseColor; //Color(255,214,132,255);

    /**
     * Constructor de DxfSolid.
     * @param proj, proyección cartográfica en la que se encuentra el DxfSolid.
     * @param layer, capa del DXF en la que se encuentra el DxfSolid.
     * @param pts, puntos 2D que componen el DxfSolid.
     */
    public DxfSolid(IProjection proj, DxfLayer layer, Point2D[] pts) {
        super(proj, layer);

        Point2D aux = pts[2];
        pts[2] = pts[3];
        pts[3] = aux;
        this.pts = pts;
        extent = new Extent();

        for (int i = 0; i < pts.length; i++) {
            extent.add(pts[i]);
        }
    }

    /**
     * Devuelve el color del DxfSolid.
     * @return Color
     */
    public Color c() {
        return color;
    }

    /**
     * Establece el color del DxfSolid.
     * @param color
     * @return Color
     */
    public Color c(Color color) {
        this.color = color;

        return color;
    }

    /**
     * Permite reproyectar un DxfSolid dado un conjunto de coordenadas de transformación.
     * @param rp, coordenadas de transformación.
     */
    public void reProject(ICoordTrans rp) {
        Point2D[] savePts = pts;

        pts = new Point2D[savePts.length];
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
     * Permite dibujar un DxfSolid.
     */
    public void draw(Graphics2D g, ViewPortData vp) {
        //System.out.println("Va a pintar un solid");
        Color color = null;

        if (dxfColor == AcadColor.BYLAYER) {
            //g.setColor(layer.getColor());
            color = layer.getColor();
        } else {
            //g.setColor(AcadColor.getColor(dxfColor));
            color = AcadColor.getColor(dxfColor);
        }

        newGP(vp);

        if (closed) {
            g.setColor(new Color(color.getRed(), color.getBlue(),
                                 color.getGreen(), 0x20));
            g.fill(gp);
        }

        g.setColor(color);
        g.draw(gp);
    }

    /**
     * Permite generar un GeneralPath partiendo del array de Point2D que conforma el
     * DxfSolid.
     * @param vp
     */
    private void newGP(ViewPortData vp) {
        //if (gp != null) return;
        gp = new GeneralPath();

        Point2D pt0 = null;
        Point2D pt = null;
        Point2D pt1 = null;
        Point2D.Double ptTmp = new Point2D.Double(0.0, 0.0);

        //System.out.println("pts.length = " + pts.length);
        for (int i = 0; i < pts.length; i++) {
            pt1 = (Point2D) pts[i];
            vp.mat.transform(pt1, ptTmp);

            if (pt0 == null) {
                pt0 = ptTmp;
                gp.moveTo((float) ptTmp.getX(), (float) ptTmp.getY());
            } else {
                gp.lineTo((float) ptTmp.getX(), (float) ptTmp.getY());
            }
        }

        if (closed) {
            gp.closePath();
        }
    }

    /* (non-Javadoc)
     * @see org.cresques.px.dxf.DxfEntity#toDxfFileString()
     */
    public String toDxfString() {
        // TODO Auto-generated method stub
        return "";
    }

    /**
     * Devuelve el array de puntos que conforman el DxfSolid.
     * @return Point2D[], puntos del DxfSolid.
     */
    public Point2D[] getPts() {
        return pts;
    }
}
