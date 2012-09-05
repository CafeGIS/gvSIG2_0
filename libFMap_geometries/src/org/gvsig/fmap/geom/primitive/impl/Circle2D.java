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
package org.gvsig.fmap.geom.primitive.impl;

import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.handler.AbstractHandler;
import org.gvsig.fmap.geom.handler.CuadrantHandler;
import org.gvsig.fmap.geom.handler.Handler;
import org.gvsig.fmap.geom.primitive.Circle;
import org.gvsig.fmap.geom.primitive.FShape;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.geom.primitive.Point;
import org.gvsig.fmap.geom.type.GeometryType;
import org.gvsig.fmap.geom.util.UtilFunctions;

/**
 * DOCUMENT ME!
 *
 * @author Vicente Caballero Navarro
 */
public class Circle2D extends Surface2D implements Circle {
	private static final long serialVersionUID = -2709867465161215668L;
	
	private Point2D center;
	private double radio;

	/**
	 * The constructor with the GeometryType like and argument 
	 * is used by the {@link GeometryType}{@link #create()}
	 * to create the geometry
	 * @param type
	 * The geometry type
	 */
	public Circle2D(GeometryType geometryType) {
		super(geometryType);		
	}
	
	/**
	 * Constructor used in the {@link Geometry#cloneGeometry()} method
	 */
	Circle2D(GeometryType geometryType, String id, IProjection projection, GeneralPathX gpx,
			Point2D c, double r) {
		super(geometryType, id, projection, gpx);
		center = c;
		radio = r;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.Circle#getCenter()
	 */
	public Point getCenter() {
		return new org.gvsig.fmap.geom.primitive.impl.Point2D(center.getX(), center.getY());
	}

    /*
     * (non-Javadoc)
     * @see org.gvsig.fmap.geom.primitive.impl.Surface2D#cloneFShape()
     */
	public FShape cloneFShape() {
		return new Circle2D(getGeometryType(), id, projection, (GeneralPathX) gp.clone(), center,
				radio);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.impl.Surface2D#getShapeType()
	 */
	public int getShapeType() {
		return TYPES.CIRCLE;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.impl.OrientablePrimitive2D#transform(java.awt.geom.AffineTransform)
	 */
	public void transform(AffineTransform at) {
		Point2D pdist = UtilFunctions.getPerpendicularPoint(new Point2D.Double(
				center.getX() + 10, center.getY()), new Point2D.Double(center
				.getX() - 10, center.getY()), center, radio);
		Point2D aux = new Point2D.Double();
		at.transform(center, aux);
		center = aux;
		Point2D aux3 = new Point2D.Double();
		at.transform(pdist, aux3);
		radio = center.distance(aux3);
		gp.transform(at);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.impl.OrientablePrimitive2D#getStretchingHandlers()
	 */
	public Handler[] getStretchingHandlers() {
		ArrayList handlers = new ArrayList();
		Rectangle2D rect = this.getBounds2D();
		handlers
				.add(new CenterHandler(0, rect.getCenterX(), rect.getCenterY()));
		// handlers.add(new RadioHandler(1, rect.getX(), rect.getCenterY()));
		// handlers.add(new RadioHandler(2, rect.getMaxX(), rect.getCenterY()));
		// handlers.add(new RadioHandler(3, rect.getCenterX(), rect.getY()));
		// handlers.add(new RadioHandler(3, rect.getCenterX(), rect.getMaxY()));

		return (Handler[]) handlers.toArray(new Handler[0]);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.impl.OrientablePrimitive2D#getSelectHandlers()
	 */
	public Handler[] getSelectHandlers() {
		ArrayList handlers = new ArrayList();
		handlers.add(new CenterSelHandler(0, center.getX(), center.getY()));
		handlers.add(new RadioSelHandler(1, center.getX() - radio, center
				.getY()));
		handlers.add(new RadioSelHandler(2, center.getX() + radio, center
				.getY()));
		handlers.add(new RadioSelHandler(3, center.getX(), center.getY()
				- radio));
		handlers.add(new RadioSelHandler(3, center.getX(), center.getY()
				+ radio));

		return (Handler[]) handlers.toArray(new Handler[0]);
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @author Vicente Caballero Navarro
	 */
	class CenterHandler extends AbstractHandler implements Handler {
		/**
		 * Crea un nuevo PointHandler.
		 *
		 * @param i
		 *            DOCUMENT ME!
		 * @param x
		 *            DOCUMENT ME!
		 * @param y
		 *            DOCUMENT ME!
		 */
		public CenterHandler(int i, double x, double y) {
			point = new Point2D.Double(x, y);
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
			center = new Point2D.Double(center.getX() + x, center.getY() + y);
			for (int i = 0; i < gp.getNumCoords() / 2; i++) {
				gp.getPointCoords()[i * 2] += x;
				gp.getPointCoords()[i * 2 + 1] += y;
			}
		}

		/**
		 * @see org.gvsig.fmap.geom.handler.Handler#set(double, double)
		 */
		public void set(double x, double y) {
		}

	}

	/**
	 * DOCUMENT ME!
	 *
	 * @author Vicente Caballero Navarro
	 */
	class CenterSelHandler extends AbstractHandler implements Handler {
		/**
		 * Crea un nuevo PointHandler.
		 *
		 * @param i
		 *            DOCUMENT ME!
		 * @param x
		 *            DOCUMENT ME!
		 * @param y
		 *            DOCUMENT ME!
		 */
		public CenterSelHandler(int i, double x, double y) {
			point = new Point2D.Double(x, y);
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
			for (int i = 0; i < gp.getNumCoords() / 2; i++) {
				gp.getPointCoords()[i * 2] += x;
				gp.getPointCoords()[i * 2 + 1] += y;
			}
		}

		/**
		 * @see org.gvsig.fmap.geom.handler.Handler#set(double, double)
		 */
		public void set(double x, double y) {
			center = new Point2D.Double(x, y);
			Arc2D.Double arc = new Arc2D.Double(center.getX() - radio, center
					.getY()
					- radio, 2 * radio, 2 * radio, 0, 360, Arc2D.OPEN);
			gp = new GeneralPathX(arc);

		}
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @author Vicente Caballero Navarro
	 */
	class RadioSelHandler extends AbstractHandler implements CuadrantHandler {

		/**
		 * Crea un nuevo PointHandler.
		 *
		 * @param i
		 *            DOCUMENT ME!
		 * @param x
		 *            DOCUMENT ME!
		 * @param y
		 *            DOCUMENT ME!
		 */
		public RadioSelHandler(int i, double x, double y) {
			point = new Point2D.Double(x, y);
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

		}

		/**
		 * @see org.gvsig.fmap.geom.handler.Handler#set(double, double)
		 */
		public void set(double x, double y) {
			radio = center.distance(x, y);
			Arc2D.Double arc = new Arc2D.Double(center.getX() - radio, center
					.getY()
					- radio, 2 * radio, 2 * radio, 0, 360, Arc2D.OPEN);
			gp = new GeneralPathX(arc);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.iver.cit.gvsig.fmap.core.FPolyline2D#intersects(java.awt.geom.Rectangle2D)
	 */
	public boolean intersects(Rectangle2D r) {
		return gp.intersects(r);
	}
	
	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.Surface2D#setGeneralPath(org.gvsig.fmap.geom.primitive.GeneralPathX)
	 */
	public void setGeneralPath(GeneralPathX generalPathX) {
		throw new UnsupportedOperationException("Use setPoints(Point center, Point radious)");
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.Circle#setPoints(org.gvsig.fmap.geom.primitive.Point, org.gvsig.fmap.geom.primitive.Point)
	 */
	public void setPoints(Point center, Point radious) {
		Point2D _center = new java.awt.geom.Point2D.Double(center.getCoordinateAt(0), center.getCoordinateAt(1));
		Point2D _radious = new java.awt.geom.Point2D.Double(radious.getCoordinateAt(0), radious.getCoordinateAt(1));
		double dRadious = _center.distance(_radious);
		setPoints(center, dRadious);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.Circle#setPoints(org.gvsig.fmap.geom.primitive.Point, double)
	 */
	public void setPoints(Point center, double radious){
		Point2D _center = new java.awt.geom.Point2D.Double(center.getCoordinateAt(0), center.getCoordinateAt(1));
		java.awt.geom.Arc2D.Double arc = 
			new java.awt.geom.Arc2D.Double(center.getX() - radious, _center
				.getY()
				- radious, 2 * radious, 2 * radious, 0, 360, Arc2D.OPEN);
		this.gp = new GeneralPathX(arc);
		this.center = _center;
		this.radio = radious;	
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.Arc#setPoints(org.gvsig.fmap.geom.primitive.Point, org.gvsig.fmap.geom.primitive.Point, org.gvsig.fmap.geom.primitive.Point)
	 */
	public void setPoints(Point p1, Point p2, Point p3) {
		Point2D _p1 = new java.awt.geom.Point2D.Double(p1.getCoordinateAt(0), p1.getCoordinateAt(1));
		Point2D _p2 = new java.awt.geom.Point2D.Double(p2.getCoordinateAt(0), p2.getCoordinateAt(1));
		Point2D _p3 = new java.awt.geom.Point2D.Double(p3.getCoordinateAt(0), p3.getCoordinateAt(1));
		setPoints(_p1, _p2, _p3);
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.Circle#setPoints(org.gvsig.fmap.geom.primitive.Point2D, org.gvsig.fmap.geom.primitive.Point2D)
	 */
	public void setPoints(org.gvsig.fmap.geom.primitive.impl.Point2D center,
			org.gvsig.fmap.geom.primitive.impl.Point2D radious) {
		Point2D _center = new java.awt.geom.Point2D.Double(center.getCoordinateAt(0), center.getCoordinateAt(1));
		Point2D _radious = new java.awt.geom.Point2D.Double(radious.getCoordinateAt(0), radious.getCoordinateAt(1));
		setPoints(_center, _radious);		
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.Circle#setPoints(org.gvsig.fmap.geom.primitive.Point2D, double)
	 */
	public void setPoints(Point2D center,
			double radious) {
		java.awt.geom.Arc2D.Double arc = 
			new java.awt.geom.Arc2D.Double(center.getX() - radious, center
				.getY()
				- radious, 2 * radious, 2 * radious, 0, 360, Arc2D.OPEN);
		this.gp = new GeneralPathX(arc);
		this.center = center;
		this.radio = radious;	
		
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.Arc#setPoints(java.awt.geom.Point2D, java.awt.geom.Point2D, java.awt.geom.Point2D)
	 */
	public void setPoints(Point2D p1, Point2D p2, Point2D p3) {
		Point2D center = UtilFunctions.getCenter(p1, p2, p3);
		if (center != null) {
			setPoints(center, p1);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.Curve#setCoordinateAt(int, int, double)
	 */
	public void setCoordinateAt(int index, int dimension, double value) {
		throw new UnsupportedOperationException("Use setGeneralPathX");
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.Circle#setPoints(java.awt.geom.Point2D, java.awt.geom.Point2D)
	 */
	private void setPoints(Point2D center, Point2D radious) {
		double radio = center.distance(radious);
		setPoints(center, radio);		
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.Arc#setPoints(org.gvsig.fmap.geom.primitive.Point, double, double, double)
	 */
	public void setPoints(Point center, double radius, double initAngle,
			double angleExt) {
		throw new UnsupportedOperationException("Use setGeneralPathX");		
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.Arc#setPoints(java.awt.geom.Point2D, double, double, double)
	 */
	private void setPoints(Point2D center, double radius, double initAngle,
			double angleExt) {
		throw new UnsupportedOperationException("Use setGeneralPathX");		
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.Circle#getRadious()
	 */
	public double getRadious() {
		return radio;
	}
	
	
}
