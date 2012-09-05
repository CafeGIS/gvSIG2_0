/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */
package com.iver.cit.gvsig.project.documents.layout.geometryadapters;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import org.gvsig.compat.print.PrintAttributes;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;

import com.iver.utiles.XMLEntity;


/**
 * Abstract adaptor to relate the geometries with the fframes and to be able to
 * integrate them in the Layout.
 *
 * @author Vicente Caballero Navarro
 */
public abstract class GeometryAdapter {
    private ArrayList points = new ArrayList();
    private GeneralPathX shape;

    /**
     * Add a point to de geometry.
     *
     * @param point Point that is added.
     *
     * @return Number of points that contains the geometry.
     */
    public int addPoint(Point2D point) {
        points.add(point);

        return points.size();
    }

    /**
     * End the creation of the geometry with the last point added.
     */
    public void end() {
        obtainShape((Point2D) points.get(points.size() - 1));
    }

    /**
     * Adds the GeneralPathX with all the points of the geometry.
     *
     * @param gpx GeneralPathX
     */
    protected void setGPX(GeneralPathX gpx) {
        shape = gpx;
    }

    /**
     * Returns the GeneralPathX.
     *
     * @return GeneralPathX.
     */
    protected GeneralPathX getGPX() {
        return shape;
    }

    /**
     * Obtains the geometry passing him as parameter the last point.
     *
     * @param p Last Point.
     */
    public abstract void obtainShape(Point2D p);

    /**
     * Applies the transformation to all the points of the geometry.
     *
     * @param at AffineTransform
     */
    public void applyTransform(AffineTransform at) {
        for (int i = 0; i < points.size(); i++) {
            at.transform((Point2D) points.get(i), (Point2D) points.get(i));
        }

        shape.transform(at);
    }

    /**
     * It draws the geometry on the Graphics that is passed like parameter.
     *
     * @param g Graphics
     * @param at AffineTransform
     * @param symbol FSymbol
     */
    public abstract void draw(Graphics2D g, AffineTransform at, ISymbol symbol);
    /**
     * It print the geometry on the Graphics that is passed like parameter.
     *
     * @param g Graphics
     * @param at AffineTransform
     * @param symbol ISymbol
     * @param properties
     */
    public abstract void print(Graphics2D g, AffineTransform at,
			ISymbol symbol, PrintAttributes properties);
    /**
     * Paints the geometry on the Graphics adding him the last point if the
     * parameter andLastPoint is true.
     *
     * @param g Graphics
     * @param at AffineTransform
     * @param andLastPoint If true add last point.
     */
    public abstract void paint(Graphics2D g, AffineTransform at,
        boolean andLastPoint);

    /**
     * Set the point of cursor.
     *
     * @param p Point of cursor.
     */
    public abstract void pointPosition(Point2D p);

    /**
     * Obtains the shape of the Geometry.
     *
     * @return Geometry.
     */
    public abstract Geometry getGeometry(AffineTransform at);

    /**
     * Returns all the points of Geometry.
     *
     * @return Array of points.
     */
    public Point2D[] getPoints() {
        return (Point2D[]) points.toArray(new Point2D[0]);
    }

    /**
     * Draws a handler in each vertex of the Geometry.
     *
     * @param g Graphics
     * @param at AffineTransform.
     */
    public void drawVertex(Graphics2D g, AffineTransform at) {
        Point2D[] ps = getPoints();
        Point2D[] pointRes = new Point2D[ps.length];
        at.transform(ps, 0, pointRes, 0, ps.length);

        int d = 3;

        for (int i = 0; i < pointRes.length; i++) {
            g.fillRect((int) pointRes[i].getX() - d,
                (int) pointRes[i].getY() - d, d * 2, d * 2);
        }
    }

    /**
     * Modifies a point of the Geometry from an index by the one that is passed
     * like parameter.
     *
     * @param pos Index
     * @param point Point
     */
    public void changePoint(int pos, Point2D point) {
        this.points.set(pos, point);
    }

    /**
     * Add all the points of Geometry.
     *
     * @param points All points.
     */
    public void setPoints(Point2D[] points) {
        this.points.clear();

        for (int i = 0; i < points.length; i++) {
            this.points.add(points[i]);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public XMLEntity getXMLEntity() {
        XMLEntity xml = new XMLEntity();
        double[] ps = new double[points.size() * 2];
        int j = 0;

        for (int i = 0; i < points.size(); i++) {
            ps[j] = (((Point2D) (points.get(i))).getX());
            ps[j + 1] = (((Point2D) (points.get(i))).getY());
            j = j + 2;
        }

        xml.putProperty("points", ps);
        xml.putProperty("className", this.getClass().getName());

        return xml;
    }

    /**
     * DOCUMENT ME!
     *
     * @param xml DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static GeometryAdapter createFromXML(XMLEntity xml) {
        GeometryAdapter geometry = null;

        try {
            Class clase = Class.forName(xml.getStringProperty("className"));
            geometry = (GeometryAdapter) clase.newInstance();
        } catch (Exception e) {
        }

        double[] ps = xml.getDoubleArrayProperty("points");
        Point2D[] pointsAux = new Point2D[ps.length / 2];
        int j = 0;

        for (int i = 0; i < ps.length; i = i + 2) {
            pointsAux[j] = new Point2D.Double(ps[i], ps[i + 1]);
            j++;
        }

        geometry.setPoints(pointsAux);
        geometry.end();

        return geometry;
    }

    /**
     * Remove last point of Geometry.
     */
    public void delLastPoint() {
        if (points.size() > 0) {
            points.remove(points.size() - 1);
        }
    }
    public GeometryAdapter cloneAdapter(){
    	GeometryAdapter cloneAdapter=null;
		try {
			cloneAdapter = this.getClass().newInstance();
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		}
    	cloneAdapter.points=(ArrayList)this.points.clone();
    	cloneAdapter.shape=(GeneralPathX)this.shape.clone();
    	return cloneAdapter;
    }
    public Rectangle2D getBounds2D(){
        Rectangle2D r=shape.getBounds2D();
        double w=r.getWidth();
        double h=r.getHeight();
        double x=r.getX();
        double y=r.getY();
        boolean modified=false;
        if (r.getWidth()<0.5) {
         modified=true;
         w=0.5;
         x=x-0.25;
        }
        if(r.getHeight()<0.5) {
         modified=true;
         h=0.5;
         y=y-0.25;
        }
        if (modified) {
			return new Rectangle2D.Double(x,y,w,h);
		}
        return r;
   }
}
