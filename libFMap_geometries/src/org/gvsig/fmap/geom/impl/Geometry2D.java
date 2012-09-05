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
 
package org.gvsig.fmap.geom.impl;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.cresques.cts.ICoordTrans;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.handler.Handler;
import org.gvsig.fmap.geom.operation.GeometryOperationContext;
import org.gvsig.fmap.geom.operation.GeometryOperationException;
import org.gvsig.fmap.geom.operation.GeometryOperationNotSupportedException;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.geom.primitive.FShape;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.geom.primitive.Primitive;
import org.gvsig.fmap.geom.type.GeometryType;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class Geometry2D implements Geometry{
	private Geometry geometry = null;
		
	/**
	 * @param primitive
	 */
	public Geometry2D(Geometry geometry) {
		super();
		this.geometry = geometry;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.Geometry#cloneGeometry()
	 */
	public Geometry cloneGeometry() {
		return new Geometry2D((Geometry)geometry.cloneGeometry());
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.Geometry#fastIntersects(double, double, double, double)
	 */
	public boolean fastIntersects(double x, double y, double w, double h) {
		return geometry.fastIntersects(x, y, w, h);
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.Geometry#getBounds2D()
	 */
	public Rectangle2D getBounds2D() {
		return geometry.getBounds2D();
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.Geometry#getDimension()
	 */
	public int getDimension() {
		return geometry.getDimension();
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.Geometry#getEnvelope()
	 */
	public Envelope getEnvelope() {
		return geometry.getEnvelope();
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.Geometry#getGeneralPath()
	 */
	public GeneralPathX getGeneralPath() {
		return geometry.getGeneralPath();
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.Geometry#getGeometryType()
	 */
	public GeometryType getGeometryType() {
		return geometry.getGeometryType();
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.Geometry#getHandlers(int)
	 */
	public Handler[] getHandlers(int type) {
		return geometry.getHandlers(type);
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.Geometry#getInternalShape()
	 */
	public Shape getInternalShape() {
		return geometry.getInternalShape();
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.Geometry#getPathIterator(java.awt.geom.AffineTransform)
	 */
	public PathIterator getPathIterator(AffineTransform at) {
		return geometry.getPathIterator(at);
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.Geometry#getPathIterator(java.awt.geom.AffineTransform, double)
	 */
	public PathIterator getPathIterator(AffineTransform at, double flatness) {
		return geometry.getPathIterator(at, flatness);
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.Geometry#getType()
	 */
	public int getType() {
		return geometry.getType();
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.Geometry#intersects(java.awt.geom.Rectangle2D)
	 */
	public boolean intersects(Rectangle2D r) {
		return geometry.intersects(r);
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.Geometry#invokeOperation(int, org.gvsig.fmap.geom.operation.GeometryOperationContext)
	 */
	public Object invokeOperation(int index, GeometryOperationContext ctx)
			throws GeometryOperationNotSupportedException,
			GeometryOperationException {
		return geometry.invokeOperation(index, ctx);
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.Geometry#invokeOperation(java.lang.String, org.gvsig.fmap.geom.operation.GeometryOperationContext)
	 */
	public Object invokeOperation(String opName, GeometryOperationContext ctx)
			throws GeometryOperationNotSupportedException,
			GeometryOperationException {
		return geometry.invokeOperation(opName, ctx);
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.Geometry#isSimple()
	 */
	public boolean isSimple() {
		return geometry.isSimple();
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.Geometry#reProject(org.cresques.cts.ICoordTrans)
	 */
	public void reProject(ICoordTrans ct) {
		geometry.reProject(ct);
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.Geometry#transform(java.awt.geom.AffineTransform)
	 */
	public void transform(AffineTransform at) {
		geometry.transform(at);
	}

	/* (non-Javadoc)
	 * @see java.awt.Shape#contains(java.awt.geom.Point2D)
	 */
	public boolean contains(Point2D p) {
		return geometry.contains(p);
	}

	/* (non-Javadoc)
	 * @see java.awt.Shape#contains(java.awt.geom.Rectangle2D)
	 */
	public boolean contains(Rectangle2D r) {
		return geometry.contains(r);
	}

	/* (non-Javadoc)
	 * @see java.awt.Shape#contains(double, double)
	 */
	public boolean contains(double x, double y) {
		return geometry.contains(x, y);
	}

	/* (non-Javadoc)
	 * @see java.awt.Shape#contains(double, double, double, double)
	 */
	public boolean contains(double x, double y, double w, double h) {
		return geometry.contains(x, y, w, h);
	}

	/* (non-Javadoc)
	 * @see java.awt.Shape#getBounds()
	 */
	public Rectangle getBounds() {
		return geometry.getBounds();
	}

	/* (non-Javadoc)
	 * @see java.awt.Shape#intersects(double, double, double, double)
	 */
	public boolean intersects(double x, double y, double w, double h) {
		return geometry.intersects(x, y, w, h);
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object arg0) {
		return geometry.compareTo(arg0);
	}
	

}

