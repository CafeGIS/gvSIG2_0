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

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.cresques.cts.ICoordTrans;
import org.gvsig.fmap.geom.handler.Handler;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.geom.primitive.FShape;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.geom.primitive.NullGeometry;
import org.gvsig.fmap.geom.type.GeometryType;

/**
 * DOCUMENT ME!
 *
 * @author Vicente Caballero Navarro
 */
public class DefaultNullGeometry extends AbstractPrimitive implements NullGeometry {
	private static final long serialVersionUID = 1L;

	/**
	 * The constructor with the GeometryType like and argument 
	 * is used by the {@link GeometryType}{@link #create()}
	 * to create the geometry
	 * @param type
	 * The geometry type
	 */
	public DefaultNullGeometry(GeometryType geometryType) {
		super(geometryType);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.iver.cit.gvsig.fmap.core.IGeometry#intersects(java.awt.geom.Rectangle2D)
	 */
	public boolean intersects(Rectangle2D r) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.iver.cit.gvsig.fmap.core.IGeometry#getBounds2D()
	 */
	public Rectangle2D getBounds2D() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.iver.cit.gvsig.fmap.core.IGeometry#cloneGeometry()
	 */
	public org.gvsig.fmap.geom.Geometry cloneGeometry() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.iver.cit.gvsig.fmap.core.IGeometry#reProject(org.cresques.cts.ICoordTrans)
	 */
	public void reProject(ICoordTrans ct) {
	}

	/**
	 * @see org.gvsig.fmap.geom.Geometry#getPathIterator(AffineTransform)
	 */
	public PathIterator getPathIterator(AffineTransform at) {
		// TODO falta implementar.
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.iver.cit.gvsig.fmap.core.IGeometry#fastIntersects(double,
	 *      double, double, double)
	 */
	public boolean fastIntersects(double x, double y, double w, double h) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.iver.cit.gvsig.fmap.core.IGeometry#getHandlers(int)
	 */
	public Handler[] getHandlers(int type) {
		// TODO Auto-generated method stub
		return null;
	}

	public void transform(AffineTransform at) {

	}

	public PathIterator getPathIterator(AffineTransform at, double flatness) {
		return null;
	}

	public boolean contains(double arg0, double arg1) {
		return false;
	}

	public boolean contains(double arg0, double arg1, double arg2, double arg3) {
		return false;
	}

	public boolean intersects(double arg0, double arg1, double arg2, double arg3) {
		return false;
	}

	public Rectangle getBounds() {
		return null;
	}

	public boolean contains(Point2D arg0) {
		return false;
	}

	public boolean contains(Rectangle2D arg0) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.iver.cit.gvsig.fmap.core.IGeometry#isSimple()
	 */
	public boolean isSimple() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.gvsig.geometries.iso.GM_Object#coordinateDimension()
	 */
	public int getDimension() {
		return 0;
	}

	public FShape cloneFShape() {
		// TODO Auto-generated method stub
		return null;
	}

	public Handler[] getSelectHandlers() {
		// TODO Auto-generated method stub
		return null;
	}

	public Handler[] getStretchingHandlers() {
		// TODO Auto-generated method stub
		return null;
	}

	public Envelope getEnvelope() {
		// TODO Auto-generated method stub
		return null;
	}
	public GeneralPathX getGeneralPath() {
		// TODO Auto-generated method stub
		return null;
	}

}
