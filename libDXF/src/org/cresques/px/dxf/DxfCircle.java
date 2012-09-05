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
 * Entidad CIRCLE de un fichero DXF.
 * @author "Luis W. Sevilla" <sevilla_lui@gva.es>
 * @author jmorell
 */
public class DxfCircle extends DxfEntity {
    final static Color baseColor = new Color(69, 106, 121);

    //Vector points = null;
    Point2D[] pts;
    GeneralPath gp = null;
    boolean closed = true;
    private Point2D center;
    private double radius;
    private Color color = baseColor; //Color(255,214,132,255);
    
    /**
     * Constructor de DxfCircle.
     * @param proj, proyección cartográfica en la que se encuentra el DxfCircle.
     * @param layer, capa del DXF en la que se encuentra el DxfCircle.
     * @param pts, puntos 2D que componen el DxfCircle.
     */
    public DxfCircle(IProjection proj, DxfLayer layer, Point2D[] pts) {
        super(proj, layer);
        this.pts = pts;
        extent = new Extent();

        for (int i = 0; i < pts.length; i++) {
            extent.add(pts[i]);
        }
    }
    
    /**
     * Devuelve el color del DxfCircle.
     * @return Color
     */
    public Color c() {
        return color;
    }
    
    /**
     * Establece el color del DxfCircle.
     * @param color
     * @return Color
     */
    public Color c(Color color) {
        this.color = color;

        return color;
    }
    
    /**
     * Permite reproyectar un DxfCircle dado un conjunto de coordenadas de transformación.
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
            this.pts[i] = ptDest;
            extent.add(ptDest);
        }

        setProjection(rp.getPDest());
    }
    
    /**
     * Permite dibujar un DxfCircle.
     */
    public void draw(Graphics2D g, ViewPortData vp) {
        //System.out.println("Va a pintar un circle");
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
                                 color.getGreen(), 0x80));
            g.fill(gp);
        }

        g.setColor(color);
        g.draw(gp);
    }
    
    /**
     * Permite generar un GeneralPath partiendo del array de Point2D que conforma el
     * DxfCircle.
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

    /**
     * Permite la escritura de entidades DxfCircle en un fichero DXF2000.
     * @return String, la cadena que se escribirá en el fichero con la información
     * del DxfCircle.
     */
    public String toDxfString() {
        StringBuffer sb = null;
        sb = new StringBuffer(DxfGroup.toString(0, "CIRCLE"));
        sb.append(DxfGroup.toString(5, getHandle()));
        sb.append(DxfGroup.toString(100, "AcDbEntity"));
        sb.append(DxfGroup.toString(8, layer.getName()));
        sb.append(DxfGroup.toString(62, dxfColor));
        sb.append(DxfGroup.toString(100, "AcDbCircle"));
        sb.append(DxfGroup.toString(10, getCenter().getX(), 6));
        sb.append(DxfGroup.toString(20, getCenter().getY(), 6));
        // sb.append(DxfGroup.toString(30, 0.0, 1));
        sb.append(DxfGroup.toString(40, getRadius(), 6));

        return sb.toString();
    }

    /**
     * Devuelve el array de puntos que conforman el DxfCircle.
     * @return Point2D[], puntos del DxfCircle.
     */
    public Point2D[] getPts() {
        return pts;
    }

    /**
     * Devuelve el GeneralPath qie conforma el DxfCircle.
     * @return GeneralPath del DxfCircle.
     */
    /*public GeneralPath getGeneralPath(ViewPort vp) {
            newGP(vp);
            return (GeneralPath) gp.clone();
    }*/

    /**
     * @return Returns the radius.
     */
    public double getRadius() {
        return radius;
    }

    /**
     * @param radius The radius to set.
     */
    public void setRadius(double radius) {
        this.radius = radius;
    }

    /**
     * @return Returns the center.
     */
    public Point2D getCenter() {
        return center;
    }

    /**
     * @param center The center to set.
     */
    public void setCenter(Point2D center) {
        this.center = center;
    }
}
