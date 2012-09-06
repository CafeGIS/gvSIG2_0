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
package org.cresques.geo;

import java.awt.Dimension;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.text.DecimalFormat;

import org.cresques.cts.ICoordTrans;
import org.cresques.cts.IProjection;
import org.cresques.px.Extent;


/**
 * Datos de vista sobre las capas.
 *
 * Mantiene un conjunto de datos necesarios, que describen el modo de
 * ver las capas actual.
 *
 * cmartinez: Esta clase no debería formar parte de una API, pero
 * se deja hasta que se aborde el refactoring de libProjection.
 *
 * @author "Luis W. Sevilla" <sevilla_lui@gva.es>
 */
public class ViewPortData implements Projected {
    /**
     * Tipo de proyección de la vista.
     */
    IProjection proj = null;

    /**
     * Sistema de coordenadas de la vista.
     */
    IProjection cs = null;

    /**
     * Amplitud de la vista, en coordenadas proyectadas.
     */
    Extent extent = null;

    /**
     * Tamaño de la vista, en coordenadas de dispositivo.
     */
    Dimension size = null;

    /**
     * Transformación afín usada en la vista actual.
     */
    public AffineTransform mat = null;

    /**
     * Resolución (Puntos por pulgada) de la vista actual.
     * Se necesita para los cálculos de escala geográfica.
     */
    int dpi = java.awt.Toolkit.getDefaultToolkit().getScreenResolution();

    public ViewPortData() {
    }

    public ViewPortData(IProjection proj, Extent extent, Dimension size) {
        this.proj = proj;
        this.extent = extent;
        this.size = size;
        mat = new AffineTransform();
        mat.scale(1.0, -1.0);
    }

    public IProjection getProjection() {
        return proj;
    }

    public void setProjection(IProjection proj) {
        this.proj = proj;
    }

    public void reProject(ICoordTrans rp) {
        // TODO metodo reProject pendiente de implementar
    }

    public void setCoordSys(IProjection cs) {
        this.cs = cs;
    }

    //public void setCoordTrans(ICoordTrans ct) { this.ct = ct; }
    public AffineTransform getMat() {
        return mat;
    }

    public void setMat(AffineTransform mat) {
        this.mat = mat;
    }

    public Object clone() {
        ViewPortData vp = new ViewPortData();

        if (mat != null) {
            vp.mat = new AffineTransform(mat);
        }

        if (extent != null) {
            vp.extent = new Extent(extent);
        }

        vp.proj = proj;
        vp.size = size;
        vp.dpi = dpi;

        return vp;
    }

    public double getWidth() {
        return size.getWidth();
    }

    public double getHeight() {
        return size.getHeight();
    }

    /**
     *
     */
    public Dimension getSize() {
        return size;
    }

    public void setSize(double w, double h) {
        setSize(new Dimension((int) w, (int) h));
    }

    public void setSize(Dimension sz) {
        size = sz;
        reExtent();
    }

    public Extent getExtent() {
        return extent;
    }

    public void setExtent(Dimension sz) {
        Point2D.Double pt0 = new Point2D.Double(0, 0);
        Point2D.Double ptSz = new Point2D.Double(sz.getWidth(), sz.getHeight());

        try {
            mat.inverseTransform(pt0, pt0);
            mat.inverseTransform(ptSz, ptSz);
        } catch (Exception e) {
            e.printStackTrace();
        }

        extent = new Extent(pt0, ptSz);
    }

    public void reExtent() {
        setExtent(size);
    }

    public void setDPI(int dpi) {
        this.dpi = dpi;
    }

    public int getDPI() {
        return this.dpi;
    }

    /**
     * zoom a un marco.
     *
     * @param extent
     */
    public void zoom(Extent extent) {
        double[] scale = extent.getScale(getWidth(), getHeight());
        double escala = Math.min(scale[0], scale[1]);

        mat.setToIdentity();
        mat.scale(escala, -escala);
        mat.translate(-extent.minX(), -extent.maxY());
        this.extent = extent;
        reExtent();
    }

    /**
     * zoom centrado en un punto.
     *
     * @param zoom
     * @param pt
     */
    public void zoom(double zoom, Point2D pt) {
        zoom(zoom, zoom, pt);
    }

    public void zoom(double zx, double zy, Point2D pt) {
        centerAt(pt);
        mat.scale(zx, zy);
        centerAt(pt);
        reExtent();
    }

    /**
     * Zoom a una escala (geográfica);
     *
     * @param scale
     */
    public void zoomToGeoScale(double scale) {
        double actual = getGeoScale();
        double f = actual / scale;
        zoomToCenter(f);
    }

    /**
     * Zoom a una escala (geográfica);
     *
     * @param scale
     */
    public void zoomToCenter(double f) {
        Point2D.Double ptCenter = new Point2D.Double(getWidth() / 2.0,
                                                     getHeight() / 2.0);

        try {
            mat.inverseTransform(ptCenter, ptCenter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        zoom(f, ptCenter);
    }

    /**
     * Centrar en un punto.
     *
     * @param pt
     */
    public void centerAt(Point2D pt) {
        Point2D.Double ptCenter = new Point2D.Double(getWidth() / 2.0,
                                                     getHeight() / 2.0);

        try {
            mat.inverseTransform(ptCenter, ptCenter);
            mat.translate(ptCenter.x - pt.getX(), ptCenter.y - pt.getY());
        } catch (Exception e) {
            e.printStackTrace();
        }

        reExtent();
    }

    /**
     * Desplaza la vista actual.
     *
     * @param pt
     */
    public void pan(Point2D ptIni, Point2D ptFin) {
        mat.translate(ptFin.getX() - ptIni.getX(), ptFin.getY() - ptIni.getY());
        reExtent();
    }

    public Point2D getCenter() {
        Point2D.Double ptCenter = new Point2D.Double(getWidth() / 2.0,
                                                     getHeight() / 2.0);

        try {
            mat.inverseTransform(ptCenter, ptCenter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ptCenter;
    }

    /**
     * Escala Geográfica.
     *
     * @param dpi resolucion en puntos por pulgada
     */
    public double getGeoScale() {
        /* // TODO Actulizarlo para Geotools2
        double scale = 0.0;
        if (proj.getClass() == UtmZone.class) { // UTM;
                scale = (extent.maxX()-extent.minX())*        // metros
                        (dpi / 2.54 * 100.0)/                                // px / metro
                        getWidth();                                                        // pixels
        } else if (proj.getClass() == Geodetic.class) { // Geodetic
                scale = (extent.maxX()-extent.minX())*                // grados
                        // 1852.0 metros x minuto de meridiano
                        (dpi / 2.54 * 100.0 * 1852.0 * 60.0)/        // px / metro
                        getWidth();                                                                // pixels
        } else if (proj.getClass() == Mercator.class) { // Mercator
                Projection prj = Geodetic.getProjection((Ellipsoid) proj.getDatum());
                GeoPoint pt1 = (GeoPoint) prj.createPoint(1.0,0.0);
                GeoPoint pt2 = (GeoPoint) prj.createPoint(2.0,0.0);
                ProjPoint ppt1 = (ProjPoint) proj.createPoint(0.0, 0.0);
                ProjPoint ppt2 = (ProjPoint) proj.createPoint(0.0, 0.0);
                ((Mercator) proj).fromGeo(pt1, ppt1);
                ((Mercator) proj).fromGeo(pt2, ppt2);
                //scale = ppt2.getX()-ppt1.getX();
                scale =  ((extent.maxX()-extent.minX())/ (ppt2.getX()-ppt1.getX()) ) *
                //scale = ((extent.maxX()-extent.minX())/ getWidth());// *
                        (dpi / 2.54 * 100.0 * 1852.0 * 60.0) /
                        getWidth();
        } */
        return proj.getScale(extent.minX(), extent.maxX(), getWidth(), dpi);
    }

    public String getGeoScaleAsString(String fmt) {
        DecimalFormat format = new DecimalFormat(fmt);

        return "1:" + format.format(getGeoScale());
    }
}
