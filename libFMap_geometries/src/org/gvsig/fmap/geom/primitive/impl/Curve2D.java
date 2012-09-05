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

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.primitive.Curve;
import org.gvsig.fmap.geom.primitive.FShape;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.geom.primitive.Point;
import org.gvsig.fmap.geom.type.GeometryType;
import org.gvsig.fmap.geom.util.UtilFunctions;

/**
 * DOCUMENT ME!
 *
 * @author Fernando Gonzï¿½lez Cortï¿½s
 */
public class Curve2D extends DefaultCurve implements Curve {
	private static final long serialVersionUID = 8161943328767877860L;

	/**
	 * The constructor with the GeometryType like and argument 
	 * is used by the {@link GeometryType}{@link #create()}
	 * to create the geometry
	 * @param type
	 * The geometry type
	 */
	public Curve2D(GeometryType geomType) {
		super(geomType);
	}

	/**
	 * Constructor used in the {@link Geometry#cloneGeometry()} method.
	 * @param geomType
	 * @param id
	 * @param projection
	 * @param gpx
	 */
	public Curve2D(GeometryType geomType, String id, IProjection projection, GeneralPathX gpx) {
		super(geomType, id, projection, gpx);
		gp=gpx;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.geom.Geometry#getShapeType()
	 */
	public int getShapeType() {
		return TYPES.CURVE;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.FShape#cloneFShape()
	 */
	public FShape cloneFShape() {
		return new Curve2D(getGeometryType(), id, projection, (GeneralPathX) gp.clone());
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.geom.Geometry#getGeneralPath()
	 */
	public GeneralPathX getGeneralPath() {
		return gp;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.impl.DefaultCurve#setGeneralPath(org.gvsig.fmap.geom.primitive.GeneralPathX)
	 */
	public void setGeneralPath(GeneralPathX generalPathX) {
		gp = generalPathX;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.Curve#setCoordinateAt(int, int, double)
	 */
	public void setCoordinateAt(int index, int dimension, double value) {
		throw new UnsupportedOperationException("Use setGeneralPathX");
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.Curve#setPoints(org.gvsig.fmap.geom.primitive.Point, org.gvsig.fmap.geom.primitive.Point)
	 */
	public void setPoints(Point startPoint, Point endPoint) {
		Point2D _startPoint = new java.awt.geom.Point2D.Double(startPoint.getCoordinateAt(0), startPoint.getCoordinateAt(1));
		Point2D _endPoint = new java.awt.geom.Point2D.Double(endPoint.getCoordinateAt(0), endPoint.getCoordinateAt(1));
		setPoints(_startPoint, _endPoint);
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.Curve#setPoints(java.awt.geom.Point2D, java.awt.geom.Point2D)
	 */
	private void setPoints(Point2D startPoint, Point2D endPoint) {
		Line2D line = UtilFunctions.createLine(startPoint, endPoint);
		setGeneralPath(new GeneralPathX(line));
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.Curve#addPoint(org.gvsig.fmap.geom.primitive.Point)
	 */
	public void addVertex(Point point) {
		gp.lineTo(point.getX(), point.getY());
	}
}
