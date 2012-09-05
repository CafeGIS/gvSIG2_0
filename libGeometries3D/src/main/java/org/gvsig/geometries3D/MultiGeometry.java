package org.gvsig.geometries3D;

/* gvSIG. Geographic Information System of the Valencian Government
*  osgVP. OSG Virtual Planets.
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
* 2008 Instituto de Automática e Informática Industrial, UPV.
*/



import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

import org.cresques.cts.ICoordTrans;
import org.cresques.cts.IProjection;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.Geometry.TYPES;
import org.gvsig.fmap.geom.handler.Handler;
import org.gvsig.fmap.geom.primitive.impl.AbstractPrimitive;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.geom.primitive.FShape;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.geom.type.GeometryType;

public class MultiGeometry extends AbstractPrimitive {
	


	private static final long serialVersionUID = 1L;
	
	/** Instancia de GeometryType obtenida al registrar esta clase */
	private static final GeometryType geomType = GeometryLocator.getGeometryManager()
	.registerGeometryType(MultiGeometry.class, "MultiGeometry", TYPES.GEOMETRY, SUBTYPES.GEOM3DM);

	private Vector<AbstractPrimitive> geometries = new Vector<AbstractPrimitive>();
	
	public MultiGeometry(GeometryType geomType,IProjection projection) {
		super(geomType,projection);
		// TODO Auto-generated constructor stub
	}

	public MultiGeometry(GeometryType geomType,String id, IProjection projection) {
		super(geomType,id, projection);
		// TODO Auto-generated constructor stub
	}
	public void addGeometry(AbstractPrimitive fe){
		
		geometries.add(fe);
		
	}

	public GeometryType getGeometryType() {
		return geomType;
	}
	
	public int getCoordinateDimension() {
		return 3;
	}
	
	public int getType() {
		return geomType.getType();
	}

	public int getShapeType() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Rectangle2D getBounds2D() {
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

	public PathIterator getPathIterator(AffineTransform arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public PathIterator getPathIterator(AffineTransform arg0, double arg1) {
		// TODO Auto-generated method stub
		return null;
	}


	public boolean intersects(Rectangle2D arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public void reProject(ICoordTrans arg0) {
		// TODO Auto-generated method stub
		
	}

	public void transform(AffineTransform arg0) {
		// TODO Auto-generated method stub
		
	}

	public boolean contains(Point2D p) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean contains(Rectangle2D r) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean contains(double x, double y) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean contains(double x, double y, double w, double h) {
		// TODO Auto-generated method stub
		return false;
	}

	public Rectangle getBounds() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean intersects(double x, double y, double w, double h) {
		// TODO Auto-generated method stub
		return false;
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

	public Vector<AbstractPrimitive> getGeometries() {
		return geometries;
	}

	public void setGeometries(Vector<AbstractPrimitive> geometries) {
		this.geometries = geometries;
	}

	public int getDimension() {
		// TODO Auto-generated method stub
		return 0;
	}

}
