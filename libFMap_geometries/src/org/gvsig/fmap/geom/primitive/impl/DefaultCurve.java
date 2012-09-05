/* gvSIG. Geographic Information System of the Valencian Government
*
* Copyright (C) 2007-2008 Infrastructures and Transports Department
* of the Valencian Government (CIT)
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
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
* MA  02110-1301, USA.
* 
*/

/*
* AUTHORS (In addition to CIT):
* 2009 {Iver T.I.}   {Task}
*/
 
package org.gvsig.fmap.geom.primitive.impl;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.geom.primitive.Curve;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.geom.primitive.Point;
import org.gvsig.fmap.geom.type.GeometryType;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public abstract class DefaultCurve extends OrientableCurve2D implements Curve {

	/**
	 * The constructor with the GeometryType like and argument 
	 * is used by the {@link GeometryType}{@link #create()}
	 * to create the geometry
	 * @param type
	 * The geometry type
	 */
	public DefaultCurve(GeometryType geometryType) {
		super(geometryType);		
	}

	/**
	 * @param id
	 * @param projection
	 * @param gpx
	 */
	public DefaultCurve(GeometryType geometryType, String id, IProjection projection, GeneralPathX gpx) {
		super(geometryType, id, projection, gpx);		
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.Curve#addVertex(org.gvsig.fmap.geom.primitive.Point)
	 */
	public void addVertex(Point point) {
		throw new UnsupportedOperationException("Method not implemented");		
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.Curve#getNumVertex()
	 */
	public int getNumVertices() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.Curve#getVertex(int)
	 */
	public Point getVertex(int index) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.Curve#insertVertex(int, org.gvsig.fmap.geom.primitive.Point)
	 */
	public void insertVertex(int index, Point p) {
		throw new UnsupportedOperationException("Method not implemented");		
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.Curve#removeVertex(int)
	 */
	public void removeVertex(int index) {
		throw new UnsupportedOperationException("Method not implemented");		
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.Curve#setCoordinateAt(int, int, double)
	 */
	public void setCoordinateAt(int index, int dimension, double value) {
		throw new UnsupportedOperationException("Method not implemented");		
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.Curve#setGeneralPath(org.gvsig.fmap.geom.primitive.GeneralPathX)
	 */
	public void setGeneralPath(GeneralPathX generalPathX) {
		throw new UnsupportedOperationException("Method not implemented");		
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.Curve#setPoints(org.gvsig.fmap.geom.primitive.Point, org.gvsig.fmap.geom.primitive.Point)
	 */
	public void setPoints(Point startPoint, Point endPoint) {
		throw new UnsupportedOperationException("Method not implemented");		
	}
	
}

