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
package org.gvsig.fmap.geom.aggregate.impl;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.aggregate.MultiPoint;
import org.gvsig.fmap.geom.handler.AbstractHandler;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.geom.primitive.FShape;
import org.gvsig.fmap.geom.primitive.Point;
import org.gvsig.fmap.geom.primitive.impl.Envelope2D;
import org.gvsig.fmap.geom.primitive.impl.Point2D;
import org.gvsig.fmap.geom.type.GeometryType;

/**
 * Multipunto 2D.
 *
 * @author Vicente Caballero Navarro
 */
public class MultiPoint2D extends BaseMultiPrimitive implements MultiPoint {

	/**
	 * The constructor with the GeometryType like and argument 
	 * is used by the {@link GeometryType}{@link #create()}
	 * to create the geometry
	 * @param type
	 * The geometry type
	 */
	public MultiPoint2D(GeometryType geometryType) {
		super(geometryType);		
	}

	/**
	 * 
	 * @param geometryType
	 * @param id
	 * @param projection
	 */
	MultiPoint2D(GeometryType geometryType, String id, IProjection projection) {
		super(geometryType, id, projection);
	}
	
	/**
	 * 
	 * @param geometryType
	 * @param id
	 * @param projection
	 * @param points
	 */
	public MultiPoint2D(GeometryType geometryType, String id, IProjection projection, Point2D[] points) {
		super(geometryType, id, projection, points);
	}
	
	
	
	/**
	 * 
	 * @param id
	 * @param projection
	 * @param x
	 * @param y
	 */
	MultiPoint2D(GeometryType geometryType, String id, IProjection projection, double[] x,
			double[] y) {
		super(geometryType, id, projection);
		geometries = new ArrayList();
		for (int i = 0; i < x.length; i++) {
			geometries.add(new Point2D(x[i], y[i]));
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.iver.cit.gvsig.fmap.core.IGeometry#cloneGeometry()
	 */
	public Geometry cloneGeometry() {
		MultiPoint auxPoint = new MultiPoint2D(geometryType, id, projection);
		for (int i = 0; i < getPrimitivesNumber(); i++) {
			auxPoint.addPoint((Point)((Point) geometries.get(i)).cloneGeometry());
		}
		return auxPoint;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.iver.cit.gvsig.fmap.core.FGeometryCollection#getBounds()
	 */
	public Rectangle getBounds() {
		Rectangle r = null;
		if (getNumgeometries() > 0) {
			r = ((Point)geometries.get(0)).getBounds();
		}
		
		Rectangle2D r2d = new Rectangle2D.Double(
				r.x, 
				r.y, 
				r.width,
				r.height);
		
		for (int i = 1; i < getNumgeometries(); i++) {
			java.awt.geom.Point2D p = ((Point)geometries.get(i))
					.getHandlers(Geometry.SELECTHANDLER)[0].getPoint();
			r2d.add(p.getX(), p.getY());
		}
		
		Rectangle resp = new Rectangle(
				Math.round((float) r2d.getMinX()),
				Math.round((float) r2d.getMinY()),
				Math.max(1, Math.round((float) r2d.getWidth())),
				Math.max(1, Math.round((float) r2d.getHeight())));
		// w,h >= 1
		
		return resp;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.iver.cit.gvsig.fmap.core.IGeometry#getBounds2D()
	 */
	public Rectangle2D getBounds2D() {
		Rectangle2D r = null;
		if (getNumgeometries() > 0) {
			java.awt.geom.Point2D p = ((Point)geometries.get(0))
					.getHandlers(Geometry.SELECTHANDLER)[0].getPoint();

			r = new Rectangle2D.Double(p.getX(), p.getY(), 0.001, 0.001);
		}
		for (int i = 1; i < getNumgeometries(); i++) {
			java.awt.geom.Point2D p = ((Point)geometries.get(i))
					.getHandlers(Geometry.SELECTHANDLER)[0].getPoint();
			r.add(p.getX(), p.getY());
		}
		return r;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.iver.cit.gvsig.fmap.core.FShape#cloneFShape()
	 */
	public FShape cloneFShape() {
		Point2D[] aux = new Point2D[getNumgeometries()];
		for (int i = 0; i < getNumgeometries(); i++) {
			aux[i] = (Point2D) ((Point)geometries.get(i)).cloneGeometry().getInternalShape();
		}
		return (FShape) new MultiPoint2D(getGeometryType(), id, projection, aux);
	}

	/**
	 * @return the numbre of points
	 * @deprecated use getPrimitivesNumber
	 */
	public int getNumgeometries() {
		return getPrimitivesNumber();
	}

	/**
	 * @return the numbre of points
	 * @deprecated use getPrimitivesNumber
	 */
	public int getNumPoints() {
		return getPrimitivesNumber();
	}

	public Point2D getPoint(int i) {
		return (Point2D) ((Point)geometries.get(i)).getInternalShape();
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @author Vicente Caballero Navarro
	 */
	class PointHandler extends AbstractHandler {
		/**
		 * Crea un nuevo PointHandler.
		 *
		 * @param x
		 *            DOCUMENT ME!
		 * @param y
		 *            DOCUMENT ME!
		 */
		public PointHandler(int i, Point2D p) {
			point = new java.awt.geom.Point2D.Double(p.getX(), p.getY());
			index = i;
		}

		/**
		 * DOCUMENT ME!
		 *
		 * @param x
		 *            DOCUMENT ME!
		 * @param y
		 *            DOCUMENT ME!
		 *
		 * @return DOCUMENT ME!
		 */
		public void move(double x, double y) {
			java.awt.geom.Point2D p = ((Point)geometries.get(index))
					.getHandlers(Geometry.SELECTHANDLER)[0].getPoint();

			point.setLocation(p.getX() + x, p.getY() + y);
		}

		/**
		 * @see org.gvsig.fmap.geom.handler.Handler#set(double, double)
		 */
		public void set(double x, double y) {
			point.setLocation(x, y);
		}

	}

	public void transform(AffineTransform at) {
		for (int i = 0; i < getNumgeometries(); i++) {
			((Point)geometries.get(i)).transform(at);
		}

	}

	public Envelope getEnvelope() {
		Envelope r = null;
		if (getNumgeometries() > 0) {
			java.awt.geom.Point2D p = ((Point)geometries.get(0))
					.getHandlers(Geometry.SELECTHANDLER)[0].getPoint();

			r = new Envelope2D(p.getX(), p.getY(), p.getX()+0.001, p.getY()+0.001);
		}
		for (int i = 1; i < getNumgeometries(); i++) {
			java.awt.geom.Point2D p = ((Point)geometries.get(i))
					.getHandlers(Geometry.SELECTHANDLER)[0].getPoint();
			r.add(new Envelope2D(p.getX(), p.getY(),p.getX()+0.001, p.getY()+0.001));
		}
		return r;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.aggregate.MultiPoint#addPoint(org.gvsig.fmap.geom.primitive.Point)
	 */
	public void addPoint(Point point) {
		addPrimitive(point);		
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.aggregate.MultiPoint#setPoints(double[], double[])
	 */
	public void setPoints(double[] x, double[] y) {
		geometries = new ArrayList();
		for (int i = 0; i < x.length; i++) {
			geometries.add(new Point2D(x[i], y[i]));
		}	
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.aggregate.MultiPoint#getPointAt(int)
	 */
	public Point getPointAt(int index) {
		return (Point)getPrimitiveAt(index) ;
	}
}
