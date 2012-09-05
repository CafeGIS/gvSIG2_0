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

import java.awt.geom.Rectangle2D;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.primitive.FShape;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.geom.primitive.Point;
import org.gvsig.fmap.geom.primitive.Surface;
import org.gvsig.fmap.geom.primitive.SurfaceAppearance;
import org.gvsig.fmap.geom.type.GeometryType;

/**
 * Polï¿½gono 2D.
 *
 * @author Vicente Caballero Navarro
 */
public class Surface2D extends OrientableSurface2D implements Surface {
	private static final long serialVersionUID = -8448256617197415743L;

	/**
	 * The constructor with the GeometryType like and argument 
	 * is used by the {@link GeometryType}{@link #create()}
	 * to create the geometry
	 * @param type
	 * The geometry type
	 */
	public Surface2D(GeometryType geomType) {
		super(geomType);
	}

	/**
	 * Constructor used in the {@link Geometry#cloneGeometry()} method.
	 * @param geomType
	 * @param id
	 * @param projection
	 * @param gpx
	 */
	Surface2D(GeometryType geomType, String id, IProjection projection, GeneralPathX gpx) {
		super(geomType, id, projection, gpx);
	}	

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.geom.Geometry#getShapeType()
	 */
	public int getShapeType() {
		return TYPES.SURFACE;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.FShape#cloneFShape()
	 */
	public FShape cloneFShape() {
		return new Surface2D(getGeometryType(), id, projection, (GeneralPathX) gp.clone());
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.impl.OrientablePrimitive2D#intersects(java.awt.geom.Rectangle2D)
	 */
	public boolean intersects(Rectangle2D r) {
		return gp.intersects(r);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.gvsig.fmap.geom.Geometry#getGeneralPath()
	 */
	public GeneralPathX getGeneralPath() {
		// TODO Auto-generated method stub
		return super.getGeneralPathX();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.Surface#setGeneralPath(org.gvsig.fmap.geom.primitive.GeneralPathX)
	 */
	public void setGeneralPath(GeneralPathX generalPathX) {
		gp = generalPathX;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.Surface#setCoordinateAt(int, int, double)
	 */
	public void setCoordinateAt(int index, int dimension, double value) {
		throw new UnsupportedOperationException("Use setGeneralPathX");
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.Surface#addVertex(org.gvsig.fmap.geom.primitive.Point)
	 */
	public void addVertex(Point point) {
		gp.lineTo(point.getX(), point.getY());
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.Surface#getNumVertex()
	 */
	public int getNumVertices() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.Surface#insertVertex(int, org.gvsig.fmap.geom.primitive.Point)
	 */
	public void insertVertex(int index, Point p) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	/*
	 * @see org.gvsig.fmap.geom.primitive.Curve#removeVertex(int)
	 */
	public void removeVertex(int index) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.Curve#getVertex(int)
	 */
	public Point getVertex(int index) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	public SurfaceAppearance getSurfaceAppearance() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setSurfaceAppearance(SurfaceAppearance app) {
		// TODO Auto-generated method stub
		
	}
}