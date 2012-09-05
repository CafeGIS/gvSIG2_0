package org.gvsig.fmap.geom.primitive.impl;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.io.Serializable;

import org.cresques.cts.ICoordTrans;
import org.cresques.cts.IProjection;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.handler.Handler;
import org.gvsig.fmap.geom.operation.GeometryOperationContext;
import org.gvsig.fmap.geom.operation.GeometryOperationException;
import org.gvsig.fmap.geom.operation.GeometryOperationNotSupportedException;
import org.gvsig.fmap.geom.primitive.FShape;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.geom.primitive.Primitive;
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
 * $Id: AbstractGeometry.java,v 1.2 2008/03/25 08:47:41 cvs Exp $
 * $Log: AbstractGeometry.java,v $
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
public abstract class AbstractPrimitive implements Primitive, FShape, Serializable {
	private static final long serialVersionUID = -4334977368955260872L;
	protected String id = null;
	protected IProjection projection = null;
	protected GeometryType geometryType = null;
	private static GeometryManager geomManager = GeometryLocator.getGeometryManager();

	/**
	 * The constructor with the GeometryType like and argument 
	 * is used by the {@link GeometryType}{@link #create()}
	 * to create the geometry
	 * @param type
	 * The geometry type
	 */
	public AbstractPrimitive(GeometryType geometryType) {
		this(geometryType, null, null);		
	}
	
	AbstractPrimitive(int type, int subtype) {
		try {
			geometryType = geomManager.getGeometryType(type, subtype);
		} catch (Exception e) {
			//TODO Not registered geometry
			geometryType = null;
		}
	}	
	
	public AbstractPrimitive(GeometryType geometryType, String id, IProjection projection) {
		super();
		this.id = id;
		this.projection = projection;
		this.geometryType = geometryType;
	}

	public AbstractPrimitive(GeometryType geometryType, IProjection projection) {
		this(geometryType, null, projection);
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.Geometry#getGeometryType()
	 */
	public GeometryType getGeometryType() {
		return geometryType;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.Geometry#getType()
	 */
	public int getType() {
		return geometryType.getType();
	}

	/**
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.core.Geometry#getInternalShape()
	 * @deprecated this Geometry is a Shape.
	 */
	public Shape getInternalShape() {
		return this;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.geometries.iso.AbstractGeometry#getId()
	 */
	public String getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.geometries.iso.AbstractGeometry#getSRS()
	 */
	public IProjection getSRS() {
		return projection;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.geometries.iso.AbstractGeometry#transform(org.cresques.cts.IProjection)
	 */
	public AbstractPrimitive transform(IProjection newProjection) {
		Geometry newGeom = cloneGeometry();
		ICoordTrans coordTrans = projection.getCT(newProjection);
		newGeom.reProject(coordTrans);
		return (AbstractPrimitive)newGeom;
	}


	/*
	 * TODO adaptar metodo procedente de UtilFunctions
	 */
	public static void rotateGeom(Geometry geometry, double radAngle, double basex, double basey) {
		AffineTransform at = new AffineTransform();
		at.rotate(radAngle,basex,basey);
		geometry.transform(at);

	}

	/*
	 * TODO adaptar metodo procedente de UtilFunctions
	 */
	public static void moveGeom(Geometry geometry, double dx, double dy) {
        AffineTransform at = new AffineTransform();
        at.translate(dx, dy);
        geometry.transform(at);
	}

	/*
	 * TODO adaptar metodo procedente de UtilFunctions
	 */
	public static void scaleGeom(Geometry geometry, Point2D basePoint, double sx, double sy) {
		AffineTransform at = new AffineTransform();
		at.setToTranslation(basePoint.getX(),basePoint.getY());
		at.scale(sx,sy);
		at.translate(-basePoint.getX(),-basePoint.getY());
		geometry.transform(at);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.geom.Geometry#fastIntersects(double, double, double, double)
	 */
	public boolean fastIntersects(double x, double y, double w, double h) {
		return intersects(x,y,w,h);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.geom.Geometry#cloneGeometry()
	 */
	public Geometry cloneGeometry() {
		return (Geometry)cloneFShape();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.geom.Geometry#getHandlers(int)
	 */
	public Handler[] getHandlers(int type) {
		if (type==STRETCHINGHANDLER){
			return getStretchingHandlers();
		}else if (type==SELECTHANDLER){
			return getSelectHandlers();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.geom.Geometry#isSimple()
	 */
	public boolean isSimple() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.geom.Geometry#invokeOperation(int, org.gvsig.fmap.geom.operation.GeometryOperationContext)
	 */
	public Object invokeOperation(int index, GeometryOperationContext ctx) throws GeometryOperationNotSupportedException, GeometryOperationException {
		return geomManager.invokeOperation(index, this, ctx);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.geom.Geometry#invokeOperation(java.lang.String, org.gvsig.fmap.geom.operation.GeometryOperationContext)
	 */
	public Object invokeOperation(String oppName, GeometryOperationContext ctx) throws GeometryOperationNotSupportedException, GeometryOperationException {
		return geomManager.invokeOperation(oppName, this, ctx);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(T)
	 */
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String name=getGeometryType().getName();
		return name.substring(name.lastIndexOf(".")+1);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}

		AbstractPrimitive other = (AbstractPrimitive) obj;
		if (this.getGeometryType().getType() != other.getGeometryType()
				.getType()) {
			return false;

		}
		if (this.getGeometryType().getSubType() != other.getGeometryType()
				.getSubType()) {
			return false;

		}
		if (this.projection != other.projection) {
			if (this.projection == null) {
				return false;
			}
			if (this.projection.getAbrev() != other.projection.getAbrev()) { //FIXME this must be false
				return false;
			}
		}
		if (!this.getBounds().equals(other.getBounds())) {
			return false;
		}


		GeneralPathX myPath = this.getGeneralPath();
		GeneralPathX otherPath = other.getGeneralPath();
		if (myPath == null) {
			if (otherPath != null) {
				return false;
			} else {
				// TODO checkThis
				return true;
			}

		}
		if (myPath.getNumTypes() != otherPath.getNumTypes()) {
			return false;
		}
		if (Math.abs(myPath.getNumCoords() - otherPath.getNumCoords()) > this
				.getDimension()) {
			return false;
		}
		PathIterator myIter = myPath.getPathIterator(null);
		PathIterator otherIter = otherPath.getPathIterator(null);
		int myType,otherType;
		// FIXME when 3D, 2DM and 3DM
		double[] myData = new double[6];
		double[] otherData = new double[6];
		double[] myFirst = new double[] { myPath.getPointCoords()[0],myPath.getPointCoords()[1]};
		double[] otherFirst = new double[] { otherPath.getPointCoords()[0],otherPath.getPointCoords()[1]};

		while (!myIter.isDone()) {
			if (otherIter.isDone()) {
				return false;
			}
			for (int i = 0; i < myData.length; i++) {
				myData[i] = 0.0;
				otherData[i] = 0.0;
			}

			myType = myIter.currentSegment(myData);
			otherType = otherIter.currentSegment(otherData);

			switch (myType) {
			case PathIterator.SEG_LINETO:
				if (otherType != myType) {
					if (otherType == PathIterator.SEG_CLOSE){
						if (!comparePathIteratorData(otherFirst, myData)) {
							return false;
						}
					} else {
						return false;
					}
				} else {
					if (!comparePathIteratorData(myData, otherData)) {
						return false;
					}
				}
				break;


			case PathIterator.SEG_CLOSE:
				if (otherType != myType) {
					if (otherType == PathIterator.SEG_LINETO) {
						if (!comparePathIteratorData(myFirst, otherData)) {
							return false;
						}
					} else {
						return false;
					}
				} else {
					if (!comparePathIteratorData(myData, otherData)) {
						return false;
					}
				}
				break;



			case PathIterator.SEG_MOVETO:
			case PathIterator.SEG_QUADTO:
			case PathIterator.SEG_CUBICTO:
				if (otherType != myType) {
					return false;
				}
				if (!comparePathIteratorData(myData, otherData)) {
					return false;
				}
				break;

			} // end switch


			myIter.next();
			otherIter.next();
		}
		if (!otherIter.isDone()) {
			return false;
		}
		return true;
	}

	private boolean comparePathIteratorData(double[] org, double[] other) {
		for (int i = 0; i < org.length; i++) {
			if (Math.abs(org[i] - other[i]) > 0.0000001) {
				return false;
			}
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.FShape#getShapeType()
	 */
	public int getShapeType() {
		return getType();
	}
	
	
}
