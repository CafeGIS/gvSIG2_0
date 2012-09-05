/* gvSIG. Sistema de InformaciÃ³n GeogrÃ¡fica de la Generalitat Valenciana
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
 *   Av. Blasco IbÃ¡Ã±ez, 50
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

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import org.cresques.cts.ICoordTrans;
import org.cresques.cts.IProjection;
import org.gvsig.fmap.geom.DirectPosition;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.handler.AbstractHandler;
import org.gvsig.fmap.geom.handler.FinalHandler;
import org.gvsig.fmap.geom.handler.Handler;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.geom.primitive.FShape;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.geom.primitive.Point;
import org.gvsig.fmap.geom.primitive.PointIterator;
import org.gvsig.fmap.geom.type.GeometryType;

/**
 * Punto 2D.
 *
 * @author Vicente Caballero Navarro
 */
public class Point2D extends AbstractPrimitive implements Point, DirectPosition {
	private static final long serialVersionUID = 1836257305083881299L;
	protected double[] coordinates;
		
	/**
	 * The constructor with the GeometryType like and argument 
	 * is used by the {@link GeometryType}{@link #create()}
	 * to create the geometry
	 * @param type
	 * The geometry type
	 */
	public Point2D(GeometryType geometryType) {
		this(geometryType, null, null, 0.0, 0.0);
	}	
		
	/**
	 * Constructor used in the {@link Geometry#cloneGeometry()} method
	 */
	Point2D(GeometryType geometryType, String id, IProjection projection, double x, double y) {
		super(geometryType, id, projection);
		coordinates = new double[getDimension()];
		coordinates[0] = x;
		coordinates[1] = y;
	}
	
	public Point2D (double x, double y) {
		super(TYPES.POINT, SUBTYPES.GEOM2D);
		coordinates = new double[getDimension()];
		coordinates[0] = x;
		coordinates[1] = y;
	}
	
	public Point2D (java.awt.geom.Point2D point) {
		super(TYPES.POINT, SUBTYPES.GEOM2D);
		coordinates = new double[getDimension()];
		coordinates[0] = point.getX();
		coordinates[1] = point.getY();
	}

	/**
	 * Aplica la transformaciónn de la matriz de transformación que se pasa como
	 * parámetro.
	 *
	 * @param at
	 *            Matriz de transformación.
	 */
	public void transform(AffineTransform at) {
		at.transform(coordinates, 0, coordinates, 0, 1);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.awt.Shape#contains(double, double)
	 */
	public boolean contains(double x, double y) {
		if ((x == coordinates[0]) || (y == coordinates[1])) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.awt.Shape#contains(double, double, double, double)
	 */
	public boolean contains(double x, double y, double w, double h) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.awt.Shape#intersects(double, double, double, double)
	 */
	public boolean intersects(double x, double y, double w, double h) {
		Rectangle2D.Double rAux = new Rectangle2D.Double(x, y, w, h);

		return rAux.contains(coordinates[0], coordinates[1]);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.awt.Shape#getBounds()
	 */
	public Rectangle getBounds() {
		return new Rectangle((int) coordinates[0], (int) coordinates[1], 0, 0);
	}

	/**
	 * Devuelve la coordenada x del punto.
	 *
	 * @return Coordenada x.
	 */
	public double getX() {
		return coordinates[0];
	}

	/**
	 * Devuelve la coordenada y del punto.
	 *
	 * @return Coordenada y.
	 */
	public double getY() {
		return coordinates[1];
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.awt.Shape#contains(java.awt.geom.Point2D)
	 */
	public boolean contains(java.awt.geom.Point2D p) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.awt.Shape#getBounds2D()
	 */
	public Rectangle2D getBounds2D() {
		return new Rectangle2D.Double(coordinates[0] - 0.01, coordinates[1] - 0.01, 0.02,
				0.02);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.awt.Shape#contains(java.awt.geom.Rectangle2D)
	 */
	public boolean contains(Rectangle2D r) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.awt.Shape#intersects(java.awt.geom.Rectangle2D)
	 */
	public boolean intersects(Rectangle2D r) {
		return r.contains(coordinates[0], coordinates[1]);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.awt.Shape#getPathIterator(java.awt.geom.AffineTransform)
	 */
	public PathIterator getPathIterator(AffineTransform at) {
		return new PointIterator(getPoint2D(), at);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.awt.Shape#getPathIterator(java.awt.geom.AffineTransform,
	 *      double)
	 */
	public PathIterator getPathIterator(AffineTransform at, double flatness) {
		return new PointIterator(getPoint2D(), at);
	}

	private java.awt.geom.Point2D getPoint2D()  {
		return new java.awt.geom.Point2D.Double(coordinates[0], coordinates[1]);
	}

	/**
	 * @see org.gvsig.fmap.geom.primitive.FShape#getShapeType()
	 */
	public int getShapeType() {
		return TYPES.POINT;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.iver.cit.gvsig.fmap.core.FShape#cloneFShape()
	 */
	public FShape cloneFShape() {
		return new Point2D(getGeometryType(), id, projection, coordinates[0], coordinates[1]);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.iver.cit.gvsig.fmap.core.FShape#reProject(org.cresques.cts.ICoordTrans)
	 */
	public void reProject(ICoordTrans ct) {
		java.awt.geom.Point2D p = getPoint2D();
		p = ct.convert(p, p);
		coordinates[0] = p.getX();
		coordinates[1] = p.getY();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.iver.cit.gvsig.fmap.core.FShape#getStretchingHandlers()
	 */
	public Handler[] getStretchingHandlers() {
		ArrayList handlers = new ArrayList();
		handlers.add(new PointHandler(0, coordinates[0], coordinates[1]));
		return (Handler[]) handlers.toArray(new Handler[0]);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.iver.cit.gvsig.fmap.core.FShape#getSelectHandlers()
	 */
	public Handler[] getSelectHandlers() {
		ArrayList handlers = new ArrayList();
		handlers.add(new PointHandler(0, coordinates[0], coordinates[1]));
		return (Handler[]) handlers.toArray(new Handler[0]);
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @author Vicente Caballero Navarro
	 */
	class PointHandler extends AbstractHandler implements FinalHandler {
		/**
		 * Crea un nuevo PointHandler.
		 *
		 * @param x
		 *            DOCUMENT ME!
		 * @param y
		 *            DOCUMENT ME!
		 */
		public PointHandler(int i, double x, double y) {
			point = new java.awt.geom.Point2D.Double(x, y);
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
			coordinates[0] = coordinates[0] + x;
			coordinates[1] = coordinates[1] + y;
		}

		/**
		 * @see org.gvsig.fmap.geom.handler.Handler#set(double, double)
		 */
		public void set(double x, double y) {
			coordinates[0] = x;
			coordinates[1] = y;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.gvsig.geometries.iso.GM_Object#coordinateDimension()
	 */
	public int getDimension() {
		return 2;
	}

	public Envelope getEnvelope() {
		return new Envelope2D(coordinates[0] - 0.01, coordinates[1] - 0.01,coordinates[0]+0.02,coordinates[1]+
				0.02);
	}

	public GeneralPathX getGeneralPath() {
		// TODO Auto-generated method stub
		return null;
	}

	public DirectPosition getDirectPosition() {
		return this;
	}

	public double getOrdinate(int dim) {
		if (dim == 0) {
			return getX();
		} else if (dim == 1) {
			return getY();
		} else {
			return 0;
		}
	}

	public boolean equals(Object other) {
		if (!super.equals(other)) {
			return false;
		}
		Point2D pother = (Point2D) other;
		if (Math.abs(this.getX() - pother.getX()) > 0.0000001) {
			return false;
		}
		if (Math.abs(this.getY() - pother.getY()) > 0.0000001) {
			return false;
		}
		return true;

	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.Point#getCoordinates()
	 */
	public double[] getCoordinates() {
		return coordinates;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.Point#getDoordinateAt(int)
	 */
	public double getCoordinateAt(int dimension) {
		return coordinates[dimension];
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.Point#setCoordinateAt(int, double)
	 */
	public void setCoordinateAt(int dimension, double value) {
		coordinates[dimension] = value;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.Point#setCoordinates(double[])
	 */
	public void setCoordinates(double[] values) {
		coordinates = values;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.Point#setX(double)
	 */
	public void setX(double x) {
		coordinates[0] = x;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.Point#setY(double)
	 */
	public void setY(double y) {
		coordinates[1] = y;
	}

	public String toString() {
		return "Point2D(" + coordinates[0] + "," + coordinates[1] + ")";
	}



}