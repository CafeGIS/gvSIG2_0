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
 * Entidad ELLIPSE de un fichero DXF.
 * @author jmorell
 */
public class DxfEllipse extends DxfEntity {
    final static Color baseColor = new Color(69, 106, 121);

    //Vector points = null;
    GeneralPath gp = null;
    boolean closed = true;
    Point2D[] pts;
    private double minorAxisLength;
    private Point2D center;
    private double minorToMajorAxisRatio;
    private Color color = baseColor; //Color(255,214,132,255);
    
    /**
     * Constructor de DxfEllipse.
     * @param proj, proyección cartográfica en la que se encuentra el DxfEllipse.
     * @param layer, capa del DXF en la que se encuentra el DxfEllipse.
     * @param pt1, primer punto del semieje mayor.
     * @param pt2, segundo punto del semieje mayor.
     * @param minorAxisLength, longitud del semieje menor.
     */
    public DxfEllipse(IProjection proj, DxfLayer layer, Point2D pt1,
                      Point2D pt2, double minorAxisLength) {
        super(proj, layer);
        pts = new Point2D[2];
        pts[0] = pt1;
        pts[1] = pt2;
        this.minorAxisLength = minorAxisLength;
        extent = new Extent();

        for (int i = 0; i < pts.length; i++) {
            extent.add(pts[i]);
        }

        center = new Point2D.Double((pts[0].getX() + pts[1].getX()) / 2.0,
                                    (pts[0].getY() + pts[1].getY()) / 2.0);

        double majorAxisLength = pt1.distance(pt2);

        //System.out.println("minorAxisLength = " + minorAxisLength);
        //System.out.println("majorAxisLength = " + majorAxisLength);
        minorToMajorAxisRatio = minorAxisLength / majorAxisLength;
    }
    
    /**
     * Devuelve el color del DxfEllipse.
     * @return Color
     */
    public Color c() {
        return color;
    }
    
    /**
     * Establece el color del DxfEllipse.
     * @param color
     * @return Color
     */
    public Color c(Color color) {
        this.color = color;

        return color;
    }
    
    /**
     * Permite reproyectar un DxfEllipse dado un conjunto de coordenadas de transformación.
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
     * Permite dibujar un DxfEllipse.
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
     * DxfEllipse.
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
     * Permite la escritura de entidades DxfEllipse en un fichero DXF2000.
     * @return String, la cadena que se escribirá en el fichero con la información
     * del DxfEllipse.
     */
    public String toDxfString() {
        StringBuffer sb = null;
        sb = new StringBuffer(DxfGroup.toString(0, "ELLIPSE"));
        sb.append(DxfGroup.toString(5, getHandle()));
        sb.append(DxfGroup.toString(100, "AcDbEntity"));
        sb.append(DxfGroup.toString(8, layer.getName()));
        sb.append(DxfGroup.toString(62, dxfColor));
        sb.append(DxfGroup.toString(100, "AcDbEllipse"));
        sb.append(DxfGroup.toString(10, getCenter().getX(), 6));
        sb.append(DxfGroup.toString(20, getCenter().getY(), 6));
        sb.append(DxfGroup.toString(30, 0.0, 6));
        sb.append(DxfGroup.toString(11, pts[1].getX() - getCenter().getX(), 6));
        sb.append(DxfGroup.toString(21, pts[1].getY() - getCenter().getY(), 6));
        sb.append(DxfGroup.toString(31, 0.0, 6));
        sb.append(DxfGroup.toString(40, getMinorToMajorAxisRatio(), 6));
        sb.append(DxfGroup.toString(41, 0.0, 6));
        sb.append(DxfGroup.toString(42, 2 * Math.PI, 6));

        return sb.toString();
    }
    
    /**
     * Devuelve el array de puntos que conforman el DxfEllipse.
     * @return Point2D[], puntos del DxfEllipse.
     */
    public Point2D[] getPts() {
        return pts;
    }

    /**
     * Devuelve el GeneralPath qie conforma el DxfEllipse.
     * @return GeneralPath del DxfEllipse.
     */
    /*public GeneralPath getGeneralPath(ViewPort vp) {
            newGP(vp);
            return (GeneralPath) gp.clone();
    }*/

    /**
     * @return Returns the minorAxisLength.
     */
    public double getMinorAxisLength() {
        return minorAxisLength;
    }

    /**
     * @param minorAxisLength The minorAxisLength to set.
     */
    public void setMinorAxisLength(double minorAxisLength) {
        this.minorAxisLength = minorAxisLength;
    }

    /**
     * @param pts The pts to set.
     */
    public void setPts(Point2D[] pts) {
        this.pts = pts;
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

    /**
     * @return Returns the majorToMinorAxisRatio.
     */
    public double getMinorToMajorAxisRatio() {
        return minorToMajorAxisRatio;
    }

    /**
     * @param majorToMinorAxisRatio The majorToMinorAxisRatio to set.
     */
    public void setMinorToMajorAxisRatio(double majorToMinorAxisRatio) {
        this.minorToMajorAxisRatio = majorToMinorAxisRatio;
    }
}
