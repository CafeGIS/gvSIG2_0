/*
 * Created on 09-feb-2005
 *
 * gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
   USA.
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
package org.gvsig.fmap.geom.primitive.impl;

import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.handler.AbstractHandler;
import org.gvsig.fmap.geom.handler.CuadrantHandler;
import org.gvsig.fmap.geom.handler.Handler;
import org.gvsig.fmap.geom.primitive.EllipticArc;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.geom.primitive.Point;
import org.gvsig.fmap.geom.primitive.SurfaceAppearance;
import org.gvsig.fmap.geom.type.GeometryType;
import org.gvsig.fmap.geom.util.UtilFunctions;



/**
 * DOCUMENT ME!
 *
 * @author Vicente Caballero Navarro
 */
public class EllipticArc2D extends Curve2D implements EllipticArc {
	private static final long serialVersionUID = 2988037614443119814L;

	private Point2D axis1Start;
	private Point2D axis1End;
	private double semiAxis2Length;
	private double angSt;
	private double angExt;

	/**
	 * The constructor with the GeometryType like and argument 
	 * is used by the {@link GeometryType}{@link #create()}
	 * to create the geometry
	 * @param type
	 * The geometry type
	 */
	public EllipticArc2D(GeometryType geometryType) {
		super(geometryType);		
	}
	
	/**
	 * Constructor used in the {@link Geometry#cloneGeometry()} method
	 * @param id
	 * @param projection
	 * @param gpx
	 * @param axis1Start
	 * @param axis1End
	 * @param semiAxis2Length
	 * @param angSt
	 * @param angExt
	 */
	EllipticArc2D(GeometryType geometryType, String id, IProjection projection, GeneralPathX gpx, Point2D axis1Start,Point2D axis1End, double semiAxis2Length, double angSt, double angExt) {
		super(geometryType, id, projection, gpx);
		this.axis1Start = axis1Start;
		this.axis1End = axis1End;
		this.semiAxis2Length = semiAxis2Length;
		this.angSt = angSt;
		this.angExt = angExt;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.EllipticArc#getAxis1Start()
	 */
	public Point getAxis1Start(){
		try {
			return new org.gvsig.fmap.geom.primitive.impl.Point2D(axis1Start);
		} catch (Exception e){
			return null;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.EllipticArc#getAxis1End()
	 */
	public Point getAxis1End(){
		try {
			return new org.gvsig.fmap.geom.primitive.impl.Point2D(axis1End);
		} catch (Exception e){
			return null;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.EllipticArc#getAxis2Dist()
	 */
	public double getAxis2Dist(){
		return this.semiAxis2Length;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.EllipticArc#getAngSt()
	 */
	public double getAngSt(){
		return this.angSt;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.EllipticArc#getAngExt()
	 */
	public double getAngExt(){
		return this.angExt;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.impl.OrientablePrimitive2D#transform(java.awt.geom.AffineTransform)
	 */
	public void transform(AffineTransform at) {
		Point2D center = new Point2D.Double((axis1Start.getX() + axis1End.getX()) / 2,
				(axis1Start.getY() + axis1End.getY()) / 2);
		Point2D pdist = UtilFunctions.getPerpendicularPoint(axis1Start, axis1End, center,
				semiAxis2Length);
		Point2D aux1 = new Point2D.Double();
		at.transform(axis1Start, aux1);
		axis1Start = aux1;
		Point2D aux2 = new Point2D.Double();
		at.transform(axis1End, aux2);
		axis1End = aux2;

		center = new Point2D.Double((axis1Start.getX() + axis1End.getX()) / 2, (axis1Start
				.getY() + axis1End.getY()) / 2);

		Point2D aux3 = new Point2D.Double();
		at.transform(pdist, aux3);
		semiAxis2Length = center.distance(aux3);
		gp.transform(at);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.impl.Curve2D#getShapeType()
	 */
	public int getShapeType() {
		return TYPES.ELLIPTICARC;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.impl.OrientablePrimitive2D#getStretchingHandlers()
	 */
	public Handler[] getStretchingHandlers() {
		ArrayList handlers = new ArrayList();
		Rectangle2D rect = this.getBounds2D();
		handlers.add(new CenterHandler(0, rect.getCenterX(), rect.getCenterY()));
		return (Handler[]) handlers.toArray(new Handler[0]);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.impl.OrientablePrimitive2D#getSelectHandlers()
	 */
	public Handler[] getSelectHandlers() {
		//TODO: Faltaria tener en cuenta handlers para los angulos angSt y angExt
		ArrayList handlers = new ArrayList();
		Rectangle2D rect = this.getBounds2D();
		handlers.add(new CenterSelHandler(0, rect.getCenterX(), rect.getCenterY()));
		handlers.add(new Axis1StSelHandler(1, axis1Start.getX(), axis1Start.getY()));
		handlers.add(new Axis1EndSelHandler(2, axis1End.getX(), axis1End.getY()));
		Point2D mediop = new Point2D.Double((axis1End.getX() + axis1Start.getX()) / 2,
				(axis1End.getY() + axis1Start.getY()) / 2);
		Point2D[] p = UtilFunctions.getPerpendicular(axis1Start, axis1End, mediop);
		Point2D u = UtilFunctions.getPoint(mediop, p[1], semiAxis2Length);
		Point2D d = UtilFunctions.getPoint(mediop, p[1], -semiAxis2Length);

		handlers.add(new RadioSelYHandler(3, u.getX(), u.getY()));
		handlers.add(new RadioSelYHandler(4, d.getX(), d.getY()));

		return (Handler[]) handlers.toArray(new Handler[0]);
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @author Vicente Caballero Navarro
	 */
	class CenterHandler extends AbstractHandler implements Handler {
		/**
		 * Crea un nuevo PointHandler.
		 *
		 * @param i
		 *            DOCUMENT ME!
		 * @param x
		 *            DOCUMENT ME!
		 * @param y
		 *            DOCUMENT ME!
		 */
		public CenterHandler(int i, double x, double y) {
			point = new Point2D.Double(x, y);
			index = i;
		}

		/**
		 * DOCUMENT ME!
		 *
		 * @param x
		 *            DOCUMENT ME!
		 * @param y
		 *            DOCUMENT ME!
		 *
		 * @return DOCUMENT ME!
		 */
		public void move(double x, double y) {
			for (int i = 0; i < gp.getNumCoords() / 2; i++) {
				gp.getPointCoords()[i * 2] += x;
				gp.getPointCoords()[i * 2 + 1] += y;
			}
			axis1Start = new Point2D.Double(axis1Start.getX() + x, axis1Start.getY() + y);
			axis1End = new Point2D.Double(axis1End.getX() + x, axis1End.getY() + y);
		}

		/**
		 * @see org.gvsig.fmap.geom.handler.Handler#set(double, double)
		 */
		public void set(double x, double y) {
		}
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @author Vicente Caballero Navarro
	 */
	class CenterSelHandler extends AbstractHandler implements Handler {
		/**
		 * Crea un nuevo PointHandler.
		 *
		 * @param i
		 *            DOCUMENT ME!
		 * @param x
		 *            DOCUMENT ME!
		 * @param y
		 *            DOCUMENT ME!
		 */
		public CenterSelHandler(int i, double x, double y) {
			point = new Point2D.Double(x, y);
			index = i;
		}

		/**
		 * DOCUMENT ME!
		 *
		 * @param x
		 *            DOCUMENT ME!
		 * @param y
		 *            DOCUMENT ME!
		 *
		 * @return DOCUMENT ME!
		 */
		public void move(double x, double y) {
			for (int i = 0; i < gp.getNumCoords() / 2; i++) {
				gp.getPointCoords()[i * 2] += x;
				gp.getPointCoords()[i * 2 + 1] += y;
			}
		}

		/**
		 * @see org.gvsig.fmap.geom.handler.Handler#set(double, double)
		 */
		public void set(double x, double y) {
			Point2D center = new Point2D.Double((axis1Start.getX() + axis1End.getX()) / 2,
					(axis1Start.getY() + axis1End.getY()) / 2);
			double dx = x - center.getX();
			double dy = y - center.getY();
			for (int i = 0; i < gp.getNumCoords() / 2; i++) {
				gp.getPointCoords()[i * 2] += dx;
				gp.getPointCoords()[i * 2 + 1] += dy;
			}
			axis1Start = new Point2D.Double(axis1Start.getX() + dx, axis1Start.getY() + dy);
			axis1End = new Point2D.Double(axis1End.getX() + dx, axis1End.getY() + dy);
		}
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @author Vicente Caballero Navarro
	 */
	class Axis1StSelHandler extends AbstractHandler implements CuadrantHandler {
		/**
		 * Crea un nuevo PointHandler.
		 *
		 * @param i
		 *            DOCUMENT ME!
		 * @param x
		 *            DOCUMENT ME!
		 * @param y
		 *            DOCUMENT ME!
		 */
		public Axis1StSelHandler(int i, double x, double y) {
			point = new Point2D.Double(x, y);
			index = i;
		}

		/**
		 * DOCUMENT ME!
		 *
		 * @param x
		 *            DOCUMENT ME!
		 * @param y
		 *            DOCUMENT ME!
		 *
		 * @return DOCUMENT ME!
		 */
		public void move(double x, double y) {

		}

		/**
		 * @see org.gvsig.fmap.geom.handler.Handler#set(double, double)
		 */
		public void set(double x, double y) {
			// TODO comentado para quitar warning: double dx=x-init.getX();
			// TODO comentado para quitar warning: double dy=y-init.getY();
			Point2D center = new Point2D.Double((axis1Start.getX() + axis1End.getX()) / 2,
					(axis1Start.getY() + axis1End.getY()) / 2);
			// Point2D[]
			// p1=TrigonometricalFunctions.getPerpendicular(init,end,center);
			// Point2D[]
			// p2=TrigonometricalFunctions.getPerpendicular(p1[0],p1[1],new
			// Point2D.Double(x,y));

			// Point2D
			// pl=TrigonometricalFunctions.getIntersection(p2[0],p2[1],p1[0],p1[1]);
			// double xdist=2*pl.distance(x,y);
			double xdist = 2 * center.distance(x, y);
			// init=new Point2D.Double(init.getX()+dx,init.getY()+dy);
			axis1Start = UtilFunctions.getPoint(center, axis1Start, center.distance(x, y));
			axis1End = UtilFunctions.getPoint(axis1Start, center, xdist);
			Arc2D.Double arc = new Arc2D.Double(axis1Start.getX(), axis1Start.getY()
					- semiAxis2Length, xdist, 2 * semiAxis2Length, Math.toDegrees(angSt), Math.toDegrees(angExt), Arc2D.OPEN);
			// TODO comentado para quitar warning: Point2D rotationPoint = new
			// Point2D.Double(init.getX() + xdist /2, init.getY());

			double angle = UtilFunctions.getAngle(axis1Start, axis1End);
			AffineTransform mT = AffineTransform.getRotateInstance(angle, axis1Start
					.getX(), axis1Start.getY());
			gp = new GeneralPathX(arc);
			gp.transform(mT);

		}
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @author Vicente Caballero Navarro
	 */
	class Axis1EndSelHandler extends AbstractHandler implements CuadrantHandler {
		/**
		 * Crea un nuevo PointHandler.
		 *
		 * @param i
		 *            DOCUMENT ME!
		 * @param x
		 *            DOCUMENT ME!
		 * @param y
		 *            DOCUMENT ME!
		 */
		public Axis1EndSelHandler(int i, double x, double y) {
			point = new Point2D.Double(x, y);
			index = i;
		}

		/**
		 * DOCUMENT ME!
		 *
		 * @param x
		 *            DOCUMENT ME!
		 * @param y
		 *            DOCUMENT ME!
		 *
		 * @return DOCUMENT ME!
		 */
		public void move(double x, double y) {

		}

		/**
		 * @see org.gvsig.fmap.geom.handler.Handler#set(double, double)
		 */
		public void set(double x, double y) {
			// double dx=x-getPoint().getX();
			// double dy=y-getPoint().getY();
			Point2D center = new Point2D.Double((axis1Start.getX() + axis1End.getX()) / 2,
					(axis1Start.getY() + axis1End.getY()) / 2);
			// Point2D[]
			// p1=TrigonometricalFunctions.getPerpendicular(init,end,center);
			// Point2D[]
			// p2=TrigonometricalFunctions.getPerpendicular(p1[0],p1[1],new
			// Point2D.Double(x,y));

			// Point2D
			// pl=TrigonometricalFunctions.getIntersection(p2[0],p2[1],p1[0],p1[1]);
			// double xdist=2*pl.distance(x,y);
			double xdist = 2 * center.distance(x, y);
			axis1End = UtilFunctions.getPoint(center, axis1End, center.distance(x, y));
			// end=new Point2D.Double(end.getX()+dx,end.getY()+dy);
			axis1Start = UtilFunctions.getPoint(axis1End, center, xdist);
			Arc2D.Double arc = new Arc2D.Double(axis1Start.getX(), axis1Start.getY()
					- semiAxis2Length, xdist, 2 * semiAxis2Length, Math.toDegrees(angSt), Math.toDegrees(angExt), Arc2D.OPEN);
			// Point2D rotationPoint = new Point2D.Double(init.getX() + xdist
			// /2, init.getY());

			double angle = UtilFunctions.getAngle(axis1Start, axis1End);
			AffineTransform mT = AffineTransform.getRotateInstance(angle, axis1Start
					.getX(), axis1Start.getY());
			gp = new GeneralPathX(arc);
			gp.transform(mT);

		}
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @author Vicente Caballero Navarro
	 */
	class RadioSelYHandler extends AbstractHandler implements CuadrantHandler {
		/**
		 * Crea un nuevo PointHandler.
		 *
		 * @param i
		 *            DOCUMENT ME!
		 * @param x
		 *            DOCUMENT ME!
		 * @param y
		 *            DOCUMENT ME!
		 */
		public RadioSelYHandler(int i, double x, double y) {
			point = new Point2D.Double(x, y);
			index = i;
		}

		/**
		 * DOCUMENT ME!
		 *
		 * @param x
		 *            DOCUMENT ME!
		 * @param y
		 *            DOCUMENT ME!
		 *
		 * @return DOCUMENT ME!
		 */
		public void move(double x, double y) {

		}

		/**
		 * @see org.gvsig.fmap.geom.handler.Handler#set(double, double)
		 */
		public void set(double x, double y) {
			semiAxis2Length = new Point2D.Double((axis1Start.getX() + axis1End.getX()) / 2, (axis1Start
					.getY() + axis1End.getY()) / 2).distance(x, y);
			// ydist=getSelectHandlers()[1].getPoint().distance(x,y);
			// Point2D center=new Point2D.Double((init.getX() + end.getX()) / 2,
			// (init.getY() + end.getY()) / 2);
			// Point2D[]
			// p=TrigonometricalFunctions.getPerpendicular(init,end,new
			// Point2D.Double(x,y));
			// Point2D
			// pl=TrigonometricalFunctions.getIntersection(p[0],p[1],init,end);
			// double xdist=2*pl.distance(x,y);
			double xdist = axis1Start.distance(axis1End);
			Arc2D.Double arc = new Arc2D.Double(axis1Start.getX(), axis1Start.getY()
					- semiAxis2Length, xdist, 2 * semiAxis2Length, Math.toDegrees(angSt), Math.toDegrees(angExt), Arc2D.OPEN);
			// Point2D rotationPoint = new Point2D.Double(init.getX() + xdist
			// /2, init.getY());

			double angle = UtilFunctions.getAngle(axis1Start, axis1End);
			AffineTransform mT = AffineTransform.getRotateInstance(angle, axis1Start
					.getX(), axis1Start.getY());
			gp = new GeneralPathX(arc);
			gp.transform(mT);
		}
	}

//TODO: Faltan Handlers para los angulos inicial y de extension (o final)



	/* (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.core.FPolyline2D#intersects(java.awt.geom.Rectangle2D)
	 */
	public boolean intersects(Rectangle2D r) {
		return gp.intersects(r);
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.EllipticArc#setPoints(org.gvsig.fmap.geom.primitive.Point, java.awt.geom.Point2D, double, double, double)
	 */
	public void setPoints(Point axis1Start, Point axis1End,
			double semiAxis2Length, double angSt, double angExt) {
		Point2D _axis1Start = new java.awt.geom.Point2D.Double(axis1Start.getCoordinateAt(0), axis1Start.getCoordinateAt(1));
		Point2D _axis1End = new java.awt.geom.Point2D.Double(axis1End.getCoordinateAt(0), axis1End.getCoordinateAt(1));
		setPoints(_axis1Start, _axis1End, semiAxis2Length, angSt, angExt);
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.EllipticArc#setPoints(java.awt.geom.Point2D, java.awt.geom.Point2D, double, double, double)
	 */
	private void setPoints(Point2D axis1Start, Point2D axis1End,
			double semiAxis2Length, double angSt, double angExt) {
		double axis1Lenght = axis1Start.distance(axis1End);
		Point2D center = new Point2D.Double((axis1Start.getX()+axis1End.getX())/2, (axis1Start.getY()+axis1End.getY())/2);
		double x = center.getX()-axis1Lenght/2;
		double y = center.getY()-semiAxis2Length;

		double angle = UtilFunctions.getAngle(center, axis1Start);

		Arc2D.Double arc = new Arc2D.Double(
				x,
				y,
				axis1Lenght,
				2 * semiAxis2Length,
				Math.toDegrees(angSt),
				Math.toDegrees(angExt),
				Arc2D.OPEN);
		AffineTransform mT = AffineTransform.getRotateInstance(angle,
				center.getX(), center.getY());
		GeneralPathX gp = new GeneralPathX(arc);
		gp.transform(mT);

		this.gp = gp;
		this.axis1Start = axis1Start;
		this.axis1End = axis1End;
		this.semiAxis2Length = semiAxis2Length;
		this.angSt = angSt;
		this.angExt = angExt;		
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.Curve2D#setGeneralPath(org.gvsig.fmap.geom.primitive.GeneralPathX)
	 */
	public void setGeneralPath(GeneralPathX generalPathX) {
		throw new UnsupportedOperationException("Use setPoints(Point center, Point radious)");
	}

	public SurfaceAppearance getSurfaceAppearance() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setSurfaceAppearance(SurfaceAppearance app) {
		// TODO Auto-generated method stub
		
	}
}