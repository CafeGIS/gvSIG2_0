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
import org.gvsig.fmap.geom.primitive.Solid;
import org.gvsig.fmap.geom.type.GeometryType;

//TO DO IMPLEMENTS SOLID

public class MultiSolid extends AbstractPrimitive {

	private static final long serialVersionUID = 1L;
	private Vector<Solid> solids = new Vector<Solid>();

	/** Instancia de GeometryType obtenida al registrar esta clase */
	private static final GeometryType geomType = GeometryLocator.getGeometryManager()
	.registerGeometryType(MultiSolid.class, "MultiSolid", TYPES.MULTISOLID, SUBTYPES.GEOM3DM);

	public MultiSolid(GeometryType geomType,String id, IProjection projection) {
		super(geomType,id, projection);
	}

	public MultiSolid(IProjection projection) {
		super(null,null,projection);
	}

	public void addSolid(Solid ge) {
		solids.add(ge);
	}

	public GeometryType getGeometryType() {
		return geomType;
	}

	public int getType() {
		return geomType.getType();
	}
	
	public int getCoordinateDimension() {
		// TODO Auto-generated method stub
		return 3;
	}

	public Envelope getEnvelope() {
		return null;
	}

	public void reProject(ICoordTrans arg0) {
		// TODO Auto-generated method stub
	}
	public Vector<Solid> getSolids() {
		return solids;
	}

	public void setSolids(Vector<Solid> solids) {
		this.solids = solids;
	}
	
	/* **********************************/
	/* Do not use in 3D all this methods  */
	/* **********************************/
	
	
	public int getShapeType() {
		return 0;
	}
	
	public Rectangle2D getBounds2D() {
		return null;
	}

	public GeneralPathX getGeneralPath() {
		return null;
	}
	
	public PathIterator getPathIterator(AffineTransform arg0) {
		return null;
	}
	
	public PathIterator getPathIterator(AffineTransform arg0, double arg1) {
		return null;
	}
	
	public boolean intersects(Rectangle2D arg0) {
		return false;
	}
	public void transform(AffineTransform arg0) {
		// TODO Auto-generated method stub

	}

	public boolean contains(Point2D p) {
		return false;
	}

	public boolean contains(Rectangle2D r) {
		return false;
	}

	public boolean contains(double x, double y) {
		return false;
	}

	public boolean contains(double x, double y, double w, double h) {
		return false;
	}

	public Rectangle getBounds() {
		return null;
	}

	public boolean intersects(double x, double y, double w, double h) {
		return false;
	}

	public FShape cloneFShape() {
		return null;
	}

	public Handler[] getSelectHandlers() {
		return null;
	}

	public Handler[] getStretchingHandlers() {
		return null;
	}

	public int getDimension() {
		// TODO Auto-generated method stub
		return 0;
	}

	
}
