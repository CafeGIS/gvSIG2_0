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

import org.cresques.cts.IProjection;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.primitive.FShape;
import org.gvsig.fmap.geom.primitive.Point;
import org.gvsig.fmap.geom.type.GeometryType;

/**
 * Punto 3D.
 *
 * @author Vicente Caballero Navarro
 */
public class Point2DZ extends Point2D implements Point{
	private static final long serialVersionUID = 1L;

	/**
	 * The constructor with the GeometryType like and argument
	 * is used by the {@link GeometryType}{@link #create()}
	 * to create the geometry
	 * @param type
	 * The geometry type
	 */
	public Point2DZ(GeometryType geomType) {
		super(geomType);
		coordinates[2] = 0;
	}

	/**
	 * Constructor used in the {@link Geometry#cloneGeometry()} method
	 * @param id
	 * @param projection
	 * @param x
	 * @param y
	 * @param z
	 */
	Point2DZ(GeometryType geomType, String id, IProjection projection, double x, double y, double z) {
		super(geomType, id, projection, x, y);
		coordinates[2] = z;
	}

	public Point2DZ (double x, double y, double z) {
		super(TYPES.POINT, SUBTYPES.GEOM2DZ);
		coordinates[0] = x;
		coordinates[1] = y;
		coordinates[2] = z;
	}


	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.impl.Point2D#getShapeType()
	 */
	public int getShapeType() {
		return TYPES.POINT;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.impl.Point2D#cloneFShape()
	 */
	public FShape cloneFShape() {
		return new Point2DZ(getGeometryType(), id, projection, coordinates[0], coordinates[1], coordinates[2]);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.impl.Point2D#getDimension()
	 */
	public int getDimension() {
		return 3;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.impl.Point2D#equals(java.lang.Object)
	 */
	public boolean equals(Object other) {
		if (!super.equals(other)) {
			return false;
		}

		Point2DZ pother = (Point2DZ) other;
		if (Math.abs(this.coordinates[2] - pother.coordinates[2]) > 0.0000001) {
			return false;
		}
		return true;
	}

	public void setCoordinates(double[] values) {
		super.setCoordinates(values);
		if (values.length > 2) {
			coordinates[2] = values[2];
		}
	}
}
