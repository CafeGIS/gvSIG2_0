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
package org.cresques.px;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import java.text.DecimalFormat;

/**
 *	Clase que getiona el extent de una imagen
 *	
 *  @author Luis W.Sevilla (sevilla_lui@gva.es)
 */
public class Extent {
    Point2D min = null;
    Point2D max = null;

    /**
     * Constructor sin parámetros
     */
    public Extent() {
        min = new Point2D.Double(999999999.0, 999999999.0);
        max = new Point2D.Double(-999999999.0, -999999999.0);
    }

    /**
     * Constructor 
     * @param pt1	punto que representa la esquina superior izquierda
     * @param pt2	punto que representa la esquina inferior derecha
     */
    public Extent(Point2D pt1, Point2D pt2) {
        newExtent(pt1.getX(), pt1.getY(), pt2.getX(), pt2.getY());
    }

    /**
     * Contructor
     * @param x1 punto que representa la coordenada X de la esquina superior izquierda
     * @param y1 punto que representa la coordenada Y de la esquina superior izquierda
     * @param x2 punto que representa la coordenada X de la esquina inferior derecha
     * @param y2 punto que representa la coordenada Y de la esquina inferior derecha
     */
    public Extent(double x1, double y1, double x2, double y2) {
        newExtent(x1, y1, x2, y2);
    }

    /**
     * Constructor
     * @param r	Rectangulo 2D
     */
    public Extent(Rectangle2D r) {
        newExtent(r.getX(), r.getY(), r.getX() + r.getWidth(),
                  r.getY() + r.getHeight());
    }

    /**
     * Constructor de copia
     * @param ext	Objeto Extent
     */
    public Extent(Extent ext) {
        newExtent(ext.minX(), ext.minY(), ext.maxX(), ext.maxY());
    }

    /**
     * Crea un objeto extent identico y lo retorna
     * @return Objeto extent
     */
    public Object clone() {
        Extent e = (Extent) clone();
        e.min = (Point2D) min.clone();
        e.max = (Point2D) max.clone();

        return e;
    }

    private void newExtent(double x1, double y1, double x2, double y2) {
        min = new Point2D.Double(Math.min(x1, x2), Math.min(y1, y2));
        max = new Point2D.Double(Math.max(x1, x2), Math.max(y1, y2));
    }

    /**
     * Obtiene la coordenada X mínima
     * @return valor de la coordenada X mínima
     */
    public double minX() {
        return min.getX();
    }

    /**
     * Obtiene la coordenada Y mínima
     * @return valor de la coordenada X mínima
     */
    public double minY() {
        return min.getY();
    }

    /**
     * Obtiene la coordenada X máxima
     * @return valor de la coordenada X máxima
     */
    public double maxX() {
        return max.getX();
    }

    /**
     * Obtiene la coordenada Y máxima
     * @return valor de la coordenada Y máxima
     */
    public double maxY() {
        return max.getY();
    }
    
    /**
     * Obtiene el punto mínimo
     * @return mínimo
     */
    public Point2D getMin() {
        return min;
    }

    /**
     * Obtiene el punto máximo
     * @return máximo
     */
    public Point2D getMax() {
        return max;
    }

    public boolean isAt(Point2D pt) {
        if (pt.getX() < minX()) {
            return false;
        }

        if (pt.getX() > maxX()) {
            return false;
        }

        if (pt.getY() < minY()) {
            return false;
        }

        if (pt.getY() > maxY()) {
            return false;
        }

        return true;
    }

    public double width() {
        return Math.abs(maxX() - minX());
    }

    public double height() {
        return Math.abs(maxY() - minY());
    }

    /**
     * Verifica un punto, y modifica el extent si no está incluido
     */
    public void add(Point2D pt) {
        if (pt == null) {
            return;
        }

        min.setLocation(Math.min(pt.getX(), minX()), Math.min(pt.getY(), minY()));
        max.setLocation(Math.max(pt.getX(), maxX()), Math.max(pt.getY(), maxY()));
    }

    public void add(Extent ext) {
        if (ext == null) {
            return;
        }

        min.setLocation(Math.min(ext.minX(), minX()),
                        Math.min(ext.minY(), minY()));
        max.setLocation(Math.max(ext.maxX(), maxX()),
                        Math.max(ext.maxY(), maxY()));
    }

    /**
     * Obtiene la escala
     * @param width	Ancho
     * @param height	Alto
     * @return
     */
    public double[] getScale(int width, int height) {
        return getScale((double) width, (double) height);
    }

    public double[] getScale(double width, double height) {
        double[] scale = new double[2];
        scale[0] = ((float) width) / width();
        scale[1] = ((float) height) / height();

        return scale;
    }

    public Rectangle2D toRectangle2D() {
        return new Rectangle2D.Double(minX(), minY(), width(), height());
    }

    public String toString() {
        DecimalFormat format = new DecimalFormat("####.000");

        return "Extent: (" + format.format(minX()) + "," +
               format.format(minY()) + "), (" + format.format(maxX()) + "," +
               format.format(maxY()) + ")";
    }

    public interface Has {
        public Extent getExtent();
    }
}
