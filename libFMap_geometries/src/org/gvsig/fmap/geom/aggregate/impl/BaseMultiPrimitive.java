package org.gvsig.fmap.geom.aggregate.impl;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import org.cresques.cts.ICoordTrans;
import org.cresques.cts.IProjection;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.aggregate.MultiPrimitive;
import org.gvsig.fmap.geom.handler.Handler;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.geom.primitive.FShape;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.geom.primitive.Primitive;
import org.gvsig.fmap.geom.primitive.impl.AbstractPrimitive;
import org.gvsig.fmap.geom.type.GeometryType;

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
/* CVS MESSAGES:
 *
 * $Id: FGeometryCollection.java,v 1.2 2008/03/25 08:47:41 cvs Exp $
 * $Log: FGeometryCollection.java,v $
 * Revision 1.2  2008/03/25 08:47:41  cvs
 * Visitors removed
 *
 * Revision 1.1  2008/03/12 08:46:20  cvs
 * *** empty log message ***
 *
 *
 */
/**
 * @author Jorge Piera Llodrá (jorge.piera@iver.es)
 */
public abstract class BaseMultiPrimitive extends AbstractPrimitive implements
		MultiPrimitive {
	private static final long serialVersionUID = 8023609161647736932L;
	protected ArrayList geometries = null;
	
	/**
	 * The constructor with the GeometryType like and argument 
	 * is used by the {@link GeometryType}{@link #create()}
	 * to create the geometry
	 * @param type
	 * The geometry type
	 */
	public BaseMultiPrimitive(GeometryType geometryType) {
		super(geometryType);
		geometries = new ArrayList();
	}

	BaseMultiPrimitive(GeometryType geometryType, String id, IProjection projection,
			Geometry[] geometries) {
		super(geometryType, id, projection);
		this.geometries = new ArrayList();
		for (int i=0 ; i<geometries.length ; i++){
			this.geometries.add(geometries[i]);
		}
	}	
	
	BaseMultiPrimitive(GeometryType geometryType, String id, IProjection projection) {
		super(geometryType, id, projection);
		this.geometries = new ArrayList();		
	}	

	/*
	 * (non-Javadoc)
	 *
	 * @see java.awt.Shape#contains(double, double)
	 */
	public boolean contains(double x, double y) {
		boolean bResul;
		for (int i = 0; i < getPrimitivesNumber(); i++) {
			bResul = ((Geometry)geometries.get(i)).contains(x, y);
			if (bResul)
				return true;
		}
		return false;
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
	 * @see java.awt.Shape#contains(java.awt.geom.Point2D)
	 */
	public boolean contains(Point2D p) {
		boolean bResul;
		for (int i = 0; i < getPrimitivesNumber(); i++) {
			bResul = ((Geometry)geometries.get(i)).contains(p);
			if (bResul)
				return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.awt.Shape#contains(java.awt.geom.Rectangle2D)
	 */
	public boolean contains(Rectangle2D r) {
		boolean bResul;
		for (int i = 0; i < getPrimitivesNumber(); i++) {
			bResul = ((Geometry)geometries.get(i)).contains(r);
			if (bResul)
				return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.gvsig.geometries.iso.GM_Object#coordinateDimension()
	 */
	public int getDimension() {
		return 2;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.iver.cit.gvsig.fmap.core.IGeometry#fastIntersects(double,
	 *      double, double, double)
	 */
	public boolean fastIntersects(double x, double y, double w, double h) {
		for (int i = 0; i < getPrimitivesNumber(); i++) {
			if (((Geometry)geometries.get(i)).intersects(x, y, w, h))
				return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.awt.Shape#getBounds()
	 */
	public Rectangle getBounds() {
		Rectangle r = null;
		if (getPrimitivesNumber() > 0) {
			r = ((Geometry)geometries.get(0)).getBounds();
		}
		for (int i = 1; i < getPrimitivesNumber(); i++) {
			Rectangle r2 = ((Geometry)geometries.get(i)).getBounds();
			r.add(r2);
		}
		return r;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.iver.cit.gvsig.fmap.core.IGeometry#getBounds2D()
	 */
	public Rectangle2D getBounds2D() {
		return null;
	}

	public Handler[] getHandlers(int type) {
		int numPrimitives = getPrimitivesNumber();
		Handler[] handlers = new Handler[numPrimitives];
		for (int i = 0; i < numPrimitives; i++) {
			handlers[i] = ((Geometry)geometries.get(i)).getHandlers(type)[0];
		}
		return handlers;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.iver.cit.gvsig.fmap.core.IGeometry#getInternalShape()
	 */
	public Shape getInternalShape() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.iver.cit.gvsig.fmap.core.IGeometry#getPathIterator(java.awt.geom.AffineTransform)
	 */
	public PathIterator getPathIterator(AffineTransform at) {
		GeneralPathX gpx = new GeneralPathX();
		int primiNum = getPrimitivesNumber();
		if (primiNum > 0) {
			Point2D p = ((Geometry)geometries.get(0)).getHandlers(Geometry.SELECTHANDLER)[0]
			                                                                              .getPoint();
			gpx.moveTo(p.getX(), p.getY());

			for (int i = 1; i < primiNum; i++) {
				p = ((Geometry)geometries.get(i)).getHandlers(Geometry.SELECTHANDLER)[0]
				                                                                              .getPoint();
				gpx.lineTo(p.getX(), p.getY());
			}
			Point2D p2 = ((Geometry)geometries.get(primiNum-1)).getHandlers(Geometry.SELECTHANDLER)[((Geometry)geometries.get(primiNum-1)).getHandlers(Geometry.SELECTHANDLER).length-1]
			                                                                                        .getPoint();
			gpx.lineTo(p2.getX(), p2.getY());
		}
		return gpx.getPathIterator(at);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.iver.cit.gvsig.fmap.core.IGeometry#getPathIterator(java.awt.geom.AffineTransform,
	 *      double)
	 */
	public PathIterator getPathIterator(AffineTransform at, double flatness) {
		GeneralPathX gpx = new GeneralPathX();
		int primiNum = getPrimitivesNumber();
		if (primiNum > 0) {
			Point2D p = ((Geometry)geometries.get(0)).getHandlers(Geometry.SELECTHANDLER)[0]
			                                                                              .getPoint();
			gpx.moveTo(p.getX(), p.getY());

			for (int i = 1; i < primiNum; i++) {
				p = ((Geometry)geometries.get(i)).getHandlers(Geometry.SELECTHANDLER)[0]
				                                                                              .getPoint();
				gpx.lineTo(p.getX(), p.getY());
			}
			Point2D p2 = ((Geometry)geometries.get(primiNum-1)).getHandlers(Geometry.SELECTHANDLER)[((Geometry)geometries.get(primiNum-1)).getHandlers(Geometry.SELECTHANDLER).length-1]
			                                                                                        .getPoint();
			gpx.lineTo(p2.getX(), p2.getY());
		}
		return gpx.getPathIterator(at, flatness);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.gvsig.geometries.iso.aggregate.GM_Aggregate#getPrimitiveAt(int)
	 */
	public Primitive getPrimitiveAt(int i) {
		if (i < getPrimitivesNumber()) {
			return (Primitive) ((Geometry)geometries.get(i));
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.gvsig.geometries.iso.aggregate.GM_Aggregate#getPrimitivesNumber()
	 */
	public int getPrimitivesNumber() {
		if (geometries == null) {
			return 0;
		}
		return geometries.size();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.awt.Shape#intersects(double, double, double, double)
	 */
	public boolean intersects(double x, double y, double w, double h) {
		boolean bResul;
		for (int i = 0; i < getPrimitivesNumber(); i++) {
			bResul = ((Geometry)geometries.get(i)).contains(x, y, w, h);
			if (bResul)
				return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.iver.cit.gvsig.fmap.core.IGeometry#intersects(java.awt.geom.Rectangle2D)
	 */
	public boolean intersects(Rectangle2D r) {
		for (int i = 0; i < getPrimitivesNumber(); i++) {
			Point2D p = ((Geometry)geometries.get(i)).getHandlers(Geometry.SELECTHANDLER)[0]
					.getPoint();
			if (r.contains(p.getX(), p.getY()))
				return true;
		}
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
	 * @see com.iver.cit.gvsig.fmap.core.IGeometry#reProject(org.cresques.cts.ICoordTrans)
	 */
	public void reProject(ICoordTrans ct) {
		for (int i = 0; i < getPrimitivesNumber(); i++) {
			((Geometry)geometries.get(i)).reProject(ct);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.iver.cit.gvsig.fmap.core.IGeometry#transform(java.awt.geom.AffineTransform)
	 */
	public void transform(AffineTransform at) {
		for (int i = 0; i < getPrimitivesNumber(); i++) {
			((Geometry)geometries.get(i)).transform(at);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.gvsig.geometries.iso.GM_Object#getBoundary()
	 */
	public Envelope getEnvelope() {
		Envelope r = null;
		if (getPrimitivesNumber() > 0) {
			r = ((Geometry)geometries.get(0)).getEnvelope();
		}
		for (int i = 1; i < getPrimitivesNumber(); i++) {
			Envelope r2 = ((Geometry)geometries.get(i)).getEnvelope();
			r.add(r2);
		}
		return r;
	}

	/**
	 * @return the geometries
	 * @deprecated
	 */
	public Geometry[] getGeometries() {
		Geometry[] _geometries = new Geometry[geometries.size()];
		for (int i=0 ; i<geometries.size() ; i++){
			_geometries[i] = ((Geometry)geometries.get(i));
		}
		return _geometries;
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

	public GeneralPathX getGeneralPath() {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.aggregate.MultiPrimitive#addPrimitive(org.gvsig.fmap.geom.primitive.Primitive)
	 */
	public void addPrimitive(Primitive primitive) {
		geometries.add(primitive);
	}
}
