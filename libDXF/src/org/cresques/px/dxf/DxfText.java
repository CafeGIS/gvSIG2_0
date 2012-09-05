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

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import org.cresques.cts.ICoordTrans;
import org.cresques.cts.IProjection;
import org.cresques.geo.ViewPortData;
import org.cresques.io.DxfGroup;
import org.cresques.px.Extent;


/**
 * Entidad TEXT de un fichero DXF.
 * @author "Luis W. Sevilla" <sevilla_lui@gva.es>
 * @author jmorell
 * jmorell, 050406: El segundo punto es opcional.
 */
public class DxfText extends DxfEntity {
    public final static int ALIGN_LEFT = 0;
    public final static int ALIGN_CENTER = 1;
    public final static int ALIGN_RIGHT = 2;
    public final static int ALIGN_ALIGNED = 3;
    public final static int ALIGN_MIDDLE = 4;
    public final static int ALIGN_FIT = 5;
    private String text = null;
    Point2D[] pts;
    private Point2D pt;
    private double rot = 0.0;
    private double h = 1.0;
    int align = ALIGN_LEFT;
    private boolean twoPointsFlag;

    /**
     * Constructor de DxfText.
     * @param proj, proyección cartográfica en la que se encuentra el DxfText.
     * @param layer, capa del DXF en la que se encuentra el DxfText.
     * @param txt, texto.
     */
    public DxfText(IProjection proj, DxfLayer layer, String txt) {
        super(proj, layer);

        //System.out.println("Dxf: TEXT '"+txt+"'.");
        extent = new Extent();
        text = txt;
        pts = new Point2D[2];
        pt = new Point2D.Double();
        twoPointsFlag = false;
    }

    /**
     * Establece el punto de inserción del DxfText.
     * @param pt, punto de inserción.
     */
    public void setPt(Point2D pt) {
        this.pt = pt;
    }

    /**
     * @return Returns the pt.
     */
    public Point2D getPt() {
        return pt;
    }

    /**
     * Establece si el texto se situa a través de dos puntos de inserción. Estos dos
     * puntos definirán su orientación.
     * @param f
     */
    public void setTwoPointsFlag(boolean f) {
        twoPointsFlag = f;
    }

    /**
     * Informa sobre si el texto se situa a través de dos puntos de inserción. Estos dos
     * puntos definirán su orientación.
     * @return boolean
     */
    public boolean getTwoPointsFlag() {
        return twoPointsFlag;
    }

    /**
     * Establece el primer punto de inserción del texto cuando este se inserta a través
     * de dos puntos de inserción.
     * @param pt
     */
    public void setPt1(Point2D pt) {
        pts[0] = pt;
        extent.add(pt);
    }

    /**
     * Obtiene el primer punto de inserción del texto cuando este se inserta a través
     * de dos puntos de inserción.
     * @return Point2D
     */
    public Point2D getPt1() {
        return pts[0];
    }

    /**
     * Establece el segundo punto de inserción del texto cuando este se inserta a través
     * de dos puntos de inserción.
     * @param pt
     */
    public void setPt2(Point2D pt) {
        pts[1] = pt;
        extent.add(pt);
    }

    /**
     * Obtiene el segundo punto de inserción del texto cuando este se inserta a través
     * de dos puntos de inserción.
     * @return Point2D
     */
    public Point2D getPt2() {
        return pts[1];
    }

    /**
     * Establece la altura del texto.
     * @param h, altura del texto.
     */
    public void setHeight(double h) {
        this.h = h;
    }

    /**
     * Establece la inclinación del texto.
     * @param r, ángulo de inclinación del texto.
     */
    public void setRotation(double r) {
        rot = r;
    }

    /**
     * Devuelve la inclinación del texto.
     * @return double
     */
    public double getRotation() {
        return rot;
    }

    /**
     * Devuelve el texto.
     * @return String
     */
    public String getText() {
        return text;
    }

    /**
     * Permite reproyectar un DxfText dado un conjunto de coordenadas de transformación.
     * @param rp, coordenadas de transformación.
     */
    public void reProject(ICoordTrans rp) {
        Point2D[] savePts = pts;

        pts = new Point2D[2];
        extent = new Extent();

        Point2D ptDest = null;

        for (int i = 0; i < savePts.length; i++) {
            ptDest = rp.getPDest().createPoint(0.0, 0.0);

            if (savePts[i] == null) {
                ptDest = null;
            } else {
                ptDest = rp.convert((Point2D) savePts[i], ptDest);
                extent.add(ptDest);
            }

            pts[i] = ptDest;
        }

        // Reproyecto la altura del texto
        Point2D ptOrig = rp.getPOrig().createPoint(savePts[0].getX(),
                                                   savePts[0].getY() + h);
        ptDest = rp.getPDest().createPoint(0D, 0D);
        ptDest = rp.convert(ptOrig, ptDest);
        h = ptDest.getY() - pts[0].getY();
        setProjection(rp.getPDest());
    }

    /**
     * Permite dibujar un DxfText.
     */
    public void draw(Graphics2D g, ViewPortData vp) {
        if (dxfColor == AcadColor.BYLAYER) {
            g.setColor(layer.getColor());
        } else {
            g.setColor(AcadColor.getColor(dxfColor));
        }

        Font fntSave = g.getFont();
        Font fnt;
        Point2D ptT0 = new Point2D.Double(pts[0].getX(), pts[0].getY());
        Point2D ptT1 = new Point2D.Double(pts[0].getX() + h, pts[0].getY() + h);
        vp.mat.transform(ptT0, ptT0);
        vp.mat.transform(ptT1, ptT1);
        fnt = new Font(fntSave.getName(), fntSave.getStyle(),
                       (int) (ptT1.getX() - ptT0.getX()));
        g.setFont(fnt);
        ptT0.setLocation(pts[0].getX(), pts[0].getY());
        vp.mat.transform(ptT0, ptT0);

        // Codigo para implementar rotacion de textos.
        // Falta depurar.
        /*System.out.println("040906: rot = " + rot);
        //angle = pathLen.angleAtLength(midDistance);
        rot = Math.toRadians(rot);
        if (rot < 0) rot = rot + 2.0*Math.PI;
        if ((rot > (Math.PI/2)) && (rot < (3 * Math.PI /2))) rot = rot - (Math.PI/2.0);
        rot = Math.toDegrees(rot);
        //theLabel.setRotation(Math.toDegrees(angle));
        AffineTransform transfRot = AffineTransform.getRotateInstance(rot, ptT0.getX(), ptT0.getY());
        AffineTransform ant = g.getTransform();
        g.setTransform(transfRot);*/
        g.drawString(text, (int) ptT0.getX(), (int) ptT0.getY());
        g.setFont(fntSave);

        //g.setTransform(ant);
    }
    public double getHeight(){
    	return h;
    }
    /**
     * Permite la escritura de entidades DxfText en un fichero DXF2000.
     * @return String, la cadena que se escribirá en el fichero con la información
     * del DxfText.
     */
    public String toDxfString() {
        StringBuffer sb = null;
        sb = new StringBuffer(DxfGroup.toString(0, "TEXT"));
        sb.append(DxfGroup.toString(1, getText()));
        sb.append(DxfGroup.toString(5, getHandle()));

//        sb.append(DxfGroup.toString(100, "AcDbText"));
        sb.append(DxfGroup.toString(8, layer.getName()));
        sb.append(DxfGroup.toString(62, dxfColor));
//        sb.append(DxfGroup.toString(100, "AcDbText"));
        sb.append(DxfGroup.toString(10, pt.getX(), 6));
        sb.append(DxfGroup.toString(20, pt.getY(), 6));
        sb.append(DxfGroup.toString(30, 0.0, 6));
        sb.append(DxfGroup.toString(40, getHeight(), 6));
        sb.append(DxfGroup.toString(50, getRotation(), 6));


        return sb.toString();
    }
}
