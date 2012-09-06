/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2006 IVER T.I. and Generalitat Valenciana.
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
 */
package org.gvsig.raster.datastruct;

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
    
    Point2D ul = new Point2D.Double(999999999.0, 999999999.0);
    Point2D lr = new Point2D.Double(999999999.0, 999999999.0);
    Point2D ur = new Point2D.Double(999999999.0, 999999999.0);
    Point2D ll = new Point2D.Double(999999999.0, 999999999.0);
    
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
    public Extent(Point2D ul, Point2D lr) {
    	this.ul = ul;
    	this.lr = lr;
        newExtent(ul.getX(), ul.getY(), lr.getX(), lr.getY());
    }
    
    /**
     * Constructor 
     * @param ul punto que representa la esquina superior izquierda
     * @param lr punto que representa la esquina inferior derecha
     * @param ur punto que representa la esquina superior derecha
     * @param ll punto que representa la esquina inferior izquierda
     */
    public Extent(Point2D ul, Point2D lr, Point2D ur, Point2D ll) {
    	this.ul = ul;
    	this.lr = lr;
    	this.ur = ur;
    	this.ll = ll;
    	min = new Point2D.Double( 
    				Math.min(Math.min(ul.getX(), lr.getX()), Math.min(ur.getX(), ll.getX())), 
    				Math.min(Math.min(ul.getY(), lr.getY()), Math.min(ur.getY(), ll.getY()))); 
      	max = new Point2D.Double( 
      				Math.max(Math.max(ul.getX(), lr.getX()), Math.max(ur.getX(), ll.getX())), 
      				Math.max(Math.max(ul.getY(), lr.getY()), Math.max(ur.getY(), ll.getY())));
    }

    /**
     * Contructor
     * @param x1 punto que representa la coordenada X de la esquina superior izquierda
     * @param y1 punto que representa la coordenada Y de la esquina superior izquierda
     * @param x2 punto que representa la coordenada X de la esquina inferior derecha
     * @param y2 punto que representa la coordenada Y de la esquina inferior derecha
     */
    public Extent(double x1, double y1, double x2, double y2) {
    	ul.setLocation(x1, y1);
    	lr.setLocation(x2, y2);
        newExtent(x1, y1, x2, y2);
    }

    /**
     * Constructor
     * @param r	Rectangulo 2D
     */
    public Extent(Rectangle2D r) {
    	ul.setLocation(r.getX(), r.getY() + r.getHeight());
    	lr.setLocation(r.getX() + r.getWidth(), r.getY());
        newExtent(r.getX(), r.getY(), r.getX() + r.getWidth(),
                  r.getY() + r.getHeight());
    }

    /**
     * Constructor de copia
     * @param ext	Objeto Extent
     */
    public Extent(Extent ext) {
    	ul.setLocation(ext.getULX(), ext.getULY());
    	lr.setLocation(ext.getLRX(), ext.getLRY());
    	ur.setLocation(ext.getURX(), ext.getURY());
    	ll.setLocation(ext.getLLX(), ext.getLLY());
        min = new Point2D.Double(ext.minX(), ext.minY());
        max = new Point2D.Double(ext.maxX(), ext.maxY());
    }

    /**
     * Crea un objeto extent identico y lo retorna
     * @return Objeto extent
     */
    public Object clone() {
        Extent e = (Extent) clone();
        e.min = (Point2D) min.clone();
        e.max = (Point2D) max.clone();
        
        e.ul = (Point2D) ul.clone();
        e.lr = (Point2D) lr.clone();
        e.ur = (Point2D) ur.clone();
        e.ll = (Point2D) ll.clone();
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
        if (pt.getX() < minX()) 
            return false;
        
        if (pt.getX() > maxX()) 
            return false;
        
        if (pt.getY() < minY())
            return false;
        
        if (pt.getY() > maxY()) 
            return false;
        
        return true;
    }

    public double width() {
        return Math.abs(maxX() - minX());
    }

    public double height() {
        return Math.abs(maxY() - minY());
    }
    
    /**
     * Obtiene el centro en X del extent
     * @return
     */
    public double getCenterX() {
    	return minX() + (width() / 2);
    }
    
    /**
     * Obtiene el centro en Y del extent
     * @return
     */
    public double getCenterY() {
    	return minY() + (height() / 2);
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
    
    /**
     * Obtiene la coordenada X de la esquina superior izquierda. Esta, en condiciones normales
     * conincide con el minimo en X pero cuando un raster está rotado esto puede variar.
     * @return ulx
     */
    public double getULX() {
    	if(ul.getX() == -999999999.0)
    		return minX();
    	return ul.getX();
    }
    
    /**
     * Obtiene la coordenada Y de la esquina superior izquierda. Esta, en condiciones normales
     * conincide con el maximo en Y pero cuando un raster está rotado esto puede variar.
     * @return uly
     */
    public double getULY() {
    	if(ul.getY() == -999999999.0)
    		return maxY();
    	return ul.getY();
    }
    
    /**
     * Obtiene la coordenada X de la esquina inferior derecha. Esta, en condiciones normales
     * conincide con el máximo en X pero cuando un raster está rotado esto puede variar.
     * @return lrx
     */
    public double getLRX() {
    	if(lr.getX() == -999999999.0)
    		return maxX();
    	return lr.getX();
    }
    
    /**
     * Obtiene la coordenada Y de la esquina inferior derecha. Esta, en condiciones normales
     * conincide con el minimo en Y pero cuando un raster está rotado esto puede variar.
     * @return lry
     */
    public double getLRY() {
    	if(lr.getX() == -999999999.0)
    		return minY();
    	return lr.getY();
    }
    
    /**
     * Obtiene la coordenada X de la esquina superior derecha. 
     * @return urx
     */
    public double getURX() {
    	return ur.getX();
    }
    
    /**
     * Obtiene la coordenada Y de la esquina superior derecha.
     * @return uly
     */
    public double getURY() {
    	return ur.getY();
    }
    
    /**
     * Obtiene la coordenada X de la esquina inferior izquierda.
     * @return lrx
     */
    public double getLLX() {
    	return ll.getX();
    }
    
    /**
     * Obtiene la coordenada Y de la esquina inferior izquierda.
     * @return lly
     */
    public double getLLY() {
    	return ll.getY();
    }
    
    /**
     * Asigna la coordenada X de la esquina superior izquierda al mínimo X
     */
    public void setULXToMin() {
    	ul.setLocation(minX(), ul.getY());
    	lr.setLocation(maxX(), lr.getY());
    }
    
    /**
     * Asigna la coordenada X de la esquina superior izquierda al máximo X
     */
    public void setULXToMax() {
    	ul.setLocation(maxX(), ul.getY());
    	lr.setLocation(minX(), lr.getY());
    }
    
    /**
     * Asigna la coordenada Y de la esquina superior izquierda al mínimo Y
     */
    public void setULYToMin() {
    	ul.setLocation(ul.getX(), minY());
    	lr.setLocation(lr.getX(), maxY());
    }
    
    /**
     * Asigna la coordenada Y de la esquina superior izquierda al máximo Y
     */
    public void setULYToMax() {
    	ul.setLocation(ul.getX(), maxY());
    	lr.setLocation(lr.getX(), minY());
    }
    
    /**
     * Asigna la coordenada X de la esquina inferior derecha al mínimo X
     */
    public void setLRXToMin() {
    	lr.setLocation(minX(), lr.getY());
    	ul.setLocation(maxX(), ul.getY());
    	
    }
    
    /**
     * Asigna la coordenada X de la esquina inferior derecha al máximo X
     */
    public void setLRXToMax() {
    	lr.setLocation(maxX(), lr.getY());
    	ul.setLocation(minX(), ul.getY());
    }
    
    /**
     * Asigna la coordenada Y de la esquina inferior derecha al mínimo Y
     */
    public void setLRYToMin() {
    	lr.setLocation(lr.getX(), minY());
    	ul.setLocation(ul.getX(), maxX());
    }
    
    /**
     * Asigna la coordenada Y de la esquina inferior derecha al máximo Y
     */
    public void setLRYToMax() {
    	lr.setLocation(lr.getX(), maxY());
    	ul.setLocation(ul.getX(), minY());
    }
}
