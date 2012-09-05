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
import java.util.Iterator;
import java.util.Vector;

import org.cresques.cts.ICoordTrans;
import org.cresques.cts.IProjection;
import org.cresques.geo.Point3D;
import org.cresques.geo.ViewPortData;
import org.cresques.io.DxfGroup;
import org.cresques.px.Extent;


/**
 * Entidad POLYLINE de un fichero DXF.
 * @author "Luis W. Sevilla" <sevilla_lui@gva.es>
 * @author jmorell
 */
public class DxfPolyline extends DxfEntity {
    final static Color baseColor = new Color(69, 106, 121);
    Vector pts = null;
    Vector faces = null;
    GeneralPath gp = null;
    int flags = 0;
    boolean closed = false;
    boolean hasFaces = false;
    private Vector bulges;
    private Color color = baseColor; //Color(255,214,132,255);
    private double elevation;
    private String subclassMarker;

    /**
     * Constructor de DxfPolyline.
     * @param proj, proyección cartográfica en la que se encuentra el DxfPolyline.
     * @param layer, capa del DXF en la que se encuentra el DxfPolyline.
     */
    public DxfPolyline(IProjection proj, DxfLayer layer) {
        super(proj, layer);
        extent = new Extent();
        pts = new Vector();
        bulges = new Vector();
    }

    /**
     * Añade un punto a la polilínea.
     * @param pt
     */
    public void add(Point2D pt) {
        pts.add(pt);
        extent.add(pt);
    }

    /**
     * Añade un bulge o parámetro de curvatura a la lista. Estos parámetros se
     * corresponden con los vértices.
     * @param bulge, parámetro de curvatura.
     */
    /**
     * 050301, jmorell: Solución para implementar la lectura de polilíneas
     * con arcos.
     */
    public void addBulge(Double bulge) {
        bulges.add(bulge);
    }

    /**
     * Añade una face a la polilínea.
     * @param face
     */
    public void addFace(int[] face) {
        hasFaces = true;

        if (faces == null) {
            faces = new Vector();
        }

        faces.add(face);
    }

    /**
     * Devuelve el color de la DxfPolyline.
     * @return Color
     */
    public Color c() {
        return color;
    }

    /**
     * Establece el color de la DxfPolyline.
     * @param color
     * @return Color
     */
    public Color c(Color color) {
        this.color = color;

        return color;
    }

    /**
     * Permite reproyectar una DxfPolyline dado un conjunto de coordenadas de transformación.
     * @param rp, coordenadas de transformación.
     */
    public void reProject(ICoordTrans rp) {
        Vector savePts = pts;

        pts = new Vector();
        extent = new Extent();

        Point2D ptDest = null;

        for (int i = 0; i < savePts.size(); i++) {
            ptDest = rp.getPDest().createPoint(0.0, 0.0);
            ptDest = rp.convert((Point2D) savePts.get(i), ptDest);
            pts.add(ptDest);
            extent.add(ptDest);
        }

        setProjection(rp.getPDest());
    }

    /**
     * Permite dibujar una DxfPolyline.
     */
    public void draw(Graphics2D g, ViewPortData vp) {
        //AffineTransform msave=g.getTransform();
        //g.setTransform(vp.mat);
        Color color = null;

        // pinto el poligono si es preciso
        if (dxfColor == AcadColor.BYLAYER) {
            color = layer.getColor();
        } else {
            color = AcadColor.getColor(dxfColor);
        }

        System.out.println("PLINE color=" + color);
        newGP(vp);

        if (closed) {
            g.setColor(new Color(color.getRed(), color.getBlue(),
                                 color.getGreen(), 0x20));
            g.fill(gp);
        }

        g.setColor(color);
        g.draw(gp);

        //g.setTransform(msave);
    }

    /**
     * Permite generar un GeneralPath partiendo del Vector de puntos que conforma la
     * DxfPolyline.
     * @param vp
     */
    private void newGP(ViewPortData vp) {
        //if (gp != null) return;
        gp = new GeneralPath();

        Point2D pt0 = null;
        Point2D pt = null;
        Point2D pt1 = null;
        Point2D.Double ptTmp = new Point2D.Double(0.0, 0.0);

        if (!hasFaces) {
            Iterator iter = pts.iterator();

            while (iter.hasNext()) {
                pt1 = (Point2D) iter.next();
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
        } else {
            System.out.println("POLYLINE: caras=" + faces.size() + ", puntos=" +
                               pts.size());

            int[] face;
            int i0;
            int i1;
            Iterator iter = faces.iterator();

            while (iter.hasNext()) {
                face = (int[]) iter.next();

                i0 = face[3];

                for (int i = 0; i < 4; i++) {
                    i1 = face[i];

                    if (i0 > 0) {
                        pt0 = (Point2D) pts.get(i0 - 1);
                        vp.mat.transform(pt0, ptTmp);
                        gp.moveTo((float) ptTmp.getX(), (float) ptTmp.getY());
                        pt1 = (Point2D) pts.get(Math.abs(i1) - 1);
                        vp.mat.transform(pt1, ptTmp);
                        gp.lineTo((float) ptTmp.getX(), (float) ptTmp.getY());
                    }

                    i0 = i1;
                }
            }
        }
    }

    /**
     * Permite la escritura de entidades DxfPolyline en un fichero DXF2000.
     * @return String, la cadena que se escribirá en el fichero con la información
     * del DxfPolyline.
     */
    public String toDxfString() {
        StringBuffer sb = null;
        sb = new StringBuffer(DxfGroup.toString(0, "POLYLINE"));
        sb.append(DxfGroup.toString(5, getHandle()));
        sb.append(DxfGroup.toString(100, "AcDbEntity"));
        sb.append(DxfGroup.toString(100, getSubclassMarker()));
        sb.append(DxfGroup.toString(8, layer.getName()));
        sb.append(DxfGroup.toString(62, dxfColor));
        sb.append(DxfGroup.toString(70, flags));
        sb.append(DxfGroup.toString(66, 1));

        Point3D pt = null;
        Iterator iter = pts.iterator();
        System.out.println("pts.size() = " + pts.size());

        int i=1;
        while (iter.hasNext()) {
            pt = (Point3D) iter.next();
            sb.append(DxfGroup.toString(0, "VERTEX"));
            sb.append(DxfGroup.toString(5, getHandle()+i));
            sb.append(DxfGroup.toString(100, "AcDbEntity"));
            sb.append(DxfGroup.toString(8, layer.getName()));
            sb.append(DxfGroup.toString(100, "AcDbVertex"));
            sb.append(DxfGroup.toString(100, "AcDb3dPolylineVertex"));
            sb.append(DxfGroup.toString(70, 32));
            sb.append(DxfGroup.toString(10, pt.getX(), 6));
            sb.append(DxfGroup.toString(20, pt.getY(), 6));
            sb.append(DxfGroup.toString(30, pt.getZ(), 6));
            i++;
        }

        sb.append(DxfGroup.toString(0, "SEQEND"));
        sb.append(DxfGroup.toString(5, getHandle()+i));
        sb.append(DxfGroup.toString(100, "AcDbEntity"));
        sb.append(DxfGroup.toString(8, layer.getName()));

        return sb.toString();
    }

    /**
     * Devuelve el GeneralPath.
     * @return GeneralPath
     */
    public GeneralPath getGeneralPath(ViewPortData vp) {
        newGP(vp);

        return (GeneralPath) gp.clone();
    }

    /**
     * Devuelve la variable flags de una polilínea.
     * @return int
     */
    public int getFlags() {
        return flags;
    }

    /**
     * Invoca el método de creación de arcos para polilíneas con parámetros de
     * curvatura.
     * @param coord1, punto inicial del arco.
     * @param coord2, punto final del arco.
     * @param bulge, parámetro de curvatura.
     * @return Vector con los puntos del arco.
     */
    public static Vector createArc(Point2D coord1, Point2D coord2, double bulge) {
        return new DxfCalArcs(coord1, coord2, bulge).getPoints(1);
    }

    /**
     * @return Returns the pts.
     */
    public Vector getPts() {
        return pts;
    }

    /**
     * @param pts The pts to set.
     */
    public void setPts(Vector pts) {
        this.pts = pts;
    }

    /**
     * @return Returns the bulges.
     */
    public Vector getBulges() {
        return bulges;
    }

    /**
     * @param bulges The bulges to set.
     */
    public void setBulges(Vector bulges) {
        this.bulges = bulges;
    }

    /**
     * @return Returns the elevation.
     */
    public double getElevation() {
        return elevation;
    }

    /**
     * @param elevation The elevation to set.
     */
    public void setElevation(double elevation) {
        this.elevation = elevation;
    }

    /**
     * @return Returns the subclassMarker.
     */
    public String getSubclassMarker() {
        return subclassMarker;
    }

    /**
     * @param subclassMarker The subclassMarker to set.
     */
    public void setSubclassMarker(String subclassMarker) {
        this.subclassMarker = subclassMarker;
    }
}
