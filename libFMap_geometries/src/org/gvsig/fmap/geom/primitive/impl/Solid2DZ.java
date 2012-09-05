package org.gvsig.fmap.geom.primitive.impl;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.cresques.cts.ICoordTrans;
import org.gvsig.fmap.geom.handler.Handler;
import org.gvsig.fmap.geom.primitive.Appearance;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.geom.primitive.FShape;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.geom.primitive.Solid;
import org.gvsig.fmap.geom.primitive.Surface;
import org.gvsig.fmap.geom.type.GeometryType;

/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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
 *   Av. Blasco Ib��ez, 50
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
 * $Id: FSolid.java,v 1.2 2008/03/25 08:47:41 cvs Exp $
 * $Log: FSolid.java,v $
 * Revision 1.2  2008/03/25 08:47:41  cvs
 * Visitors removed
 *
 * Revision 1.1  2008/03/12 08:46:20  cvs
 * *** empty log message ***
 *
 *
 */
/**
 * @author Jorge Piera Llodr� (jorge.piera@iver.es)
 */
public class Solid2DZ extends AbstractPrimitive implements Solid {
	private static final long serialVersionUID = 5702298296398677297L;

	/**
	 * The constructor with the GeometryType like and argument 
	 * is used by the {@link GeometryType}{@link #create()}
	 * to create the geometry
	 * @param type
	 * The geometry type
	 */
	public Solid2DZ(GeometryType geometryType) {
		super(geometryType);
	}

	public FShape cloneFShape() {
		// TODO Auto-generated method stub
		return null;
	}

	public Handler[] getSelectHandlers() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getShapeType() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Handler[] getStretchingHandlers() {
		// TODO Auto-generated method stub
		return null;
	}

	public void reProject(ICoordTrans ct) {
		// TODO Auto-generated method stub

	}

	public void transform(AffineTransform at) {
		// TODO Auto-generated method stub

	}

	public boolean contains(Point2D arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean contains(Rectangle2D arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean contains(double arg0, double arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean contains(double arg0, double arg1, double arg2, double arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	public Rectangle getBounds() {
		// TODO Auto-generated method stub
		return null;
	}

	public Rectangle2D getBounds2D() {
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

	public boolean intersects(double arg0, double arg1, double arg2, double arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.gvsig.geometries.iso.GM_Object#coordinateDimension()
	 */
	public int getDimension() {
		return 3;
	}	

	public Envelope getEnvelope() {
		// TODO Auto-generated method stub
		return null;
	}

	public GeneralPathX getGeneralPath() {
		// TODO Auto-generated method stub
		return null;
	}

	public double[] getZs() {
		// TODO Auto-generated method stub
		return null;
	}

	public void addSurface(Surface surface) {
		// TODO Auto-generated method stub
		
	}

	public Appearance getAppearance() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getNumSurfaces() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Surface getSurfaceAt(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	public void removeSurface(int position) {
		// TODO Auto-generated method stub
		
	}

	public void setAppearance(Appearance app) {
		// TODO Auto-generated method stub
		
	}



}
