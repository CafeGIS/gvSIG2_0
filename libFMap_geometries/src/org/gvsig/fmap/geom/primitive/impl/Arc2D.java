/*
 * Created on 09-feb-2005
 *
 * gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
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

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.handler.AbstractHandler;
import org.gvsig.fmap.geom.handler.CenterHandler;
import org.gvsig.fmap.geom.handler.FinalHandler;
import org.gvsig.fmap.geom.handler.Handler;
import org.gvsig.fmap.geom.primitive.Arc;
import org.gvsig.fmap.geom.primitive.FShape;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.geom.primitive.Point;
import org.gvsig.fmap.geom.type.GeometryType;
import org.gvsig.fmap.geom.util.UtilFunctions;



/**
 * DOCUMENT ME!
 *
 * @author Vicente Caballero Navarro
 */
public class Arc2D extends Curve2D implements Arc {
	private static final long serialVersionUID = 6416027005106924030L;

	private Point2D init;
	private Point2D center;
	private Point2D end;
	
	private static final GeometryManager geomManager = GeometryLocator.getGeometryManager();
		
	/**
	 * The constructor with the GeometryType like and argument 
	 * is used by the {@link GeometryType}{@link #create()}
	 * to create the geometry
	 * @param type
	 * The geometry type
	 */
	public Arc2D(GeometryType geometryType) {
		super(geometryType);		
	}
	
	/**
	 * Constructor used in the {@link Geometry#cloneGeometry()} method
	 * @param id
	 * @param projection
	 * @param gpx
	 * @param i
	 * @param c
	 * @param e
	 */
	protected Arc2D(GeometryType geometryType, String id, IProjection projection, GeneralPathX gpx, Point2D i,Point2D c, Point2D e) {
		super(geometryType, id, projection, gpx);
		init=i;
		center=c;
		end=e;
	}	
	
	/* (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.core.FShape#cloneFShape()
	 */
	public FShape cloneFShape() {
		Arc2D arc = new Arc2D(getGeometryType(), id, projection, (GeneralPathX) gp.clone(),init,center,end);
		return arc;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.impl.OrientablePrimitive2D#transform(java.awt.geom.AffineTransform)
	 */
	public void transform(AffineTransform at) {
		gp.transform(at);
		InitHandler inithandler=(InitHandler)getStretchingHandlers()[0];
		//CenterHandler centerhandler=(CenterHandler)getHandlers()[1];
		EndHandler endhandler=(EndHandler)getStretchingHandlers()[1];
		Point2D aux1=new Point2D.Double();
		Point2D aux2=new Point2D.Double();
		Point2D aux3=new Point2D.Double();
		at.transform(inithandler.getPoint(),aux1);
		inithandler.setPoint(aux1);
		//at.transform(centerhandler.getPoint(),aux2);
		//centerhandler.setPoint(aux2);
		at.transform(endhandler.getPoint(),aux3);
		endhandler.setPoint(aux3);
		CenterSelHandler centerhandler=(CenterSelHandler)getSelectHandlers()[1];
		at.transform(centerhandler.getPoint(),aux2);
		centerhandler.setPoint(aux2);

	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.impl.Curve2D#getShapeType()
	 */
	public int getShapeType() {
		return TYPES.ARC;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.impl.OrientablePrimitive2D#getStretchingHandlers()
	 */
	public Handler[] getStretchingHandlers() {
		ArrayList handlers = new ArrayList();

		handlers.add(new InitHandler(0, init.getX(), init.getY()));
		//handlers.add(new CenterHandler(1, center.getX(), center.getY()));
		handlers.add(new EndHandler(1, end.getX(), end.getY()));

		return (Handler[]) handlers.toArray(new Handler[0]);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.impl.OrientablePrimitive2D#getSelectHandlers()
	 */	
	public Handler[] getSelectHandlers() {
		ArrayList handlers = new ArrayList();

		handlers.add(new InitSelHandler(0, init.getX(), init.getY()));
		handlers.add(new CenterSelHandler(1, center.getX(), center.getY()));
		handlers.add(new EndSelHandler(2, end.getX(), end.getY()));

		return (Handler[]) handlers.toArray(new Handler[0]);
	}
	/**
	 * DOCUMENT ME!
	 *
	 * @author Vicente Caballero Navarro
	 */	
	class CenterSelHandler extends AbstractHandler implements CenterHandler{
		/**
		 * Crea un nuevo PointHandler.
		 *
		 * @param i DOCUMENT ME!
		 * @param x DOCUMENT ME!
		 * @param y DOCUMENT ME!
		 */
		public CenterSelHandler(int i, double x, double y) {
			center = new Point2D.Double(x, y);
			index = i;
		}

		/**
		 * DOCUMENT ME!
		 *
		 * @param x DOCUMENT ME!
		 * @param y DOCUMENT ME!
		 *
		 * @return DOCUMENT ME!
		 */
		public void move(double x, double y) {
		}
		public void setPoint(Point2D p){
			center=p;
		}
		public Point2D getPoint(){
			return center;
		}

		/**
		 * @see org.gvsig.fmap.geom.handler.Handler#set(double, double)
		 */
		public void set(double x, double y) {
			center=new Point2D.Double(x,y);
			java.awt.geom.Arc2D arco = UtilFunctions.createArc(init, center, end);
			gp = new GeneralPathX(arco);
		}
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @author Vicente Caballero Navarro
	 */
	class InitHandler extends AbstractHandler implements FinalHandler{
		/**
		 * Crea un nuevo PointHandler.
		 *
		 * @param i DOCUMENT ME!
		 * @param x DOCUMENT ME!
		 * @param y DOCUMENT ME!
		 */
		public InitHandler(int i, double x, double y) {
			init = new Point2D.Double(x, y);
			index = i;
		}

		/**
		 * DOCUMENT ME!
		 *
		 * @param x DOCUMENT ME!
		 * @param y DOCUMENT ME!
		 *
		 * @return DOCUMENT ME!
		 */
		public void move(double x, double y) {
			Point2D mediop=new Point2D.Double((init.getX()+end.getX())/2,(init.getY()+end.getY())/2);
			double dist=mediop.distance(center);
			init=new Point2D.Double(init.getX()+x,init.getY()+y);

			mediop=new Point2D.Double((init.getX()+end.getX())/2,(init.getY()+end.getY())/2);
			Point2D[] perp=UtilFunctions.getPerpendicular(init,end,mediop);
			if (UtilFunctions.getAngle(end,init)<=Math.PI){
				dist=-dist;
			}
			center=UtilFunctions.getPoint(mediop,perp[1],dist);

			java.awt.geom.Arc2D arco = UtilFunctions.createArc(init,center, end);
			gp=new GeneralPathX(arco);
		}
		public void setPoint(Point2D p){
			init=p;
		}
		public Point2D getPoint(){
			return init;
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
	class EndHandler extends AbstractHandler implements FinalHandler{
		/**
		 * Crea un nuevo PointHandler.
		 *
		 * @param i DOCUMENT ME!
		 * @param x DOCUMENT ME!
		 * @param y DOCUMENT ME!
		 */
		public EndHandler(int i, double x, double y) {
			end = new Point2D.Double(x, y);
			index = i;
		}

		/**
		 * DOCUMENT ME!
		 *
		 * @param x DOCUMENT ME!
		 * @param y DOCUMENT ME!
		 *
		 * @return DOCUMENT ME!
		 */
		public void move(double x, double y) {
			Point2D mediop=new Point2D.Double((init.getX()+end.getX())/2,(init.getY()+end.getY())/2);
			double dist=mediop.distance(center);
			end=new Point2D.Double(end.getX()+x,end.getY()+y);

			mediop=new Point2D.Double((init.getX()+end.getX())/2,(init.getY()+end.getY())/2);
			Point2D[] perp=UtilFunctions.getPerpendicular(init,end,mediop);
			if (UtilFunctions.getAngle(end,init)<=Math.PI){
				dist=-dist;
			}
			center=UtilFunctions.getPoint(mediop,perp[1],dist);

			java.awt.geom.Arc2D arco = UtilFunctions.createArc(init,center, end);
			gp=new GeneralPathX(arco);
		}
		public void setPoint(Point2D p){
			end=p;
		}
		public Point2D getPoint(){
			return end;
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
	class InitSelHandler extends AbstractHandler implements FinalHandler{
		/**
		 * Crea un nuevo PointHandler.
		 *
		 * @param i DOCUMENT ME!
		 * @param x DOCUMENT ME!
		 * @param y DOCUMENT ME!
		 */
		public InitSelHandler(int i, double x, double y) {
			init = new Point2D.Double(x, y);
			index = i;
		}

		/**
		 * DOCUMENT ME!
		 *
		 * @param x DOCUMENT ME!
		 * @param y DOCUMENT ME!
		 *
		 * @return DOCUMENT ME!
		 */
		public void move(double x, double y) {
			Point2D mediop=new Point2D.Double((init.getX()+end.getX())/2,(init.getY()+end.getY())/2);
			double dist=mediop.distance(center);
			init=new Point2D.Double(init.getX()+x,init.getY()+y);

			mediop=new Point2D.Double((init.getX()+end.getX())/2,(init.getY()+end.getY())/2);
			Point2D[] perp=UtilFunctions.getPerpendicular(init,end,mediop);
			if (UtilFunctions.getAngle(end,init)<=Math.PI){
				dist=-dist;
			}
			center=UtilFunctions.getPoint(mediop,perp[1],dist);

			java.awt.geom.Arc2D arco = UtilFunctions.createArc(init,center, end);
			gp=new GeneralPathX(arco);
		}
		public void setPoint(Point2D p){
			init=p;
		}
		public Point2D getPoint(){
			return init;
		}

		/**
		 * @see org.gvsig.fmap.geom.handler.Handler#set(double, double)
		 */
		public void set(double x, double y) {
			Point2D mediop=new Point2D.Double((init.getX()+end.getX())/2,(init.getY()+end.getY())/2);
			double dist=mediop.distance(center);
			init=new Point2D.Double(x,y);

			mediop=new Point2D.Double((init.getX()+end.getX())/2,(init.getY()+end.getY())/2);
			// TODO comentado para quitar warning: Point2D[] perp=UtilFunctions.getPerpendicular(init,end,mediop);
			if (UtilFunctions.getAngle(end,init)<=Math.PI){
				dist=-dist;
			}
			///center=TrigonometricalFunctions.getPoint(mediop,perp[1],dist);
			java.awt.geom.Arc2D arco = UtilFunctions.createArc(init,center, end);
			gp=new GeneralPathX(arco);
		}
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @author Vicente Caballero Navarro
	 */
	class EndSelHandler extends AbstractHandler implements FinalHandler{
		/**
		 * Crea un nuevo PointHandler.
		 *
		 * @param i DOCUMENT ME!
		 * @param x DOCUMENT ME!
		 * @param y DOCUMENT ME!
		 */
		public EndSelHandler(int i, double x, double y) {
			end = new Point2D.Double(x, y);
			index = i;
		}

		/**
		 * DOCUMENT ME!
		 *
		 * @param x DOCUMENT ME!
		 * @param y DOCUMENT ME!
		 *
		 * @return DOCUMENT ME!
		 */
		public void move(double x, double y) {
			Point2D mediop=new Point2D.Double((init.getX()+end.getX())/2,(init.getY()+end.getY())/2);
			double dist=mediop.distance(center);
			end=new Point2D.Double(end.getX()+x,end.getY()+y);

			mediop=new Point2D.Double((init.getX()+end.getX())/2,(init.getY()+end.getY())/2);
			Point2D[] perp=UtilFunctions.getPerpendicular(init,end,mediop);
			if (UtilFunctions.getAngle(end,init)<=Math.PI){
				dist=-dist;
			}
			center=UtilFunctions.getPoint(mediop,perp[1],dist);

			java.awt.geom.Arc2D arco = UtilFunctions.createArc(init,center, end);
			gp=new GeneralPathX(arco);
		}
		public void setPoint(Point2D p){
			end=p;
		}
		public Point2D getPoint(){
			return end;
		}

		/**
		 * @see org.gvsig.fmap.geom.handler.Handler#set(double, double)
		 */
		public void set(double x, double y) {
			Point2D mediop=new Point2D.Double((init.getX()+end.getX())/2,(init.getY()+end.getY())/2);
			double dist=mediop.distance(center);
			end=new Point2D.Double(x,y);

			mediop=new Point2D.Double((init.getX()+end.getX())/2,(init.getY()+end.getY())/2);
			// TODO comentado para quitar warning: Point2D[] perp=UtilFunctions.getPerpendicular(init,end,mediop);
			if (UtilFunctions.getAngle(end,init)<=Math.PI){
				dist=-dist;
			}
			///center=TrigonometricalFunctions.getPoint(mediop,perp[1],dist);
			java.awt.geom.Arc2D arco = UtilFunctions.createArc(init,center, end);
			gp=new GeneralPathX(arco);
		}
	}

	/* (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.core.FPolyline2D#intersects(java.awt.geom.Rectangle2D)
	 */
	public boolean intersects(Rectangle2D r) {
		return gp.intersects(r);
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.Arc#setPoints(org.gvsig.fmap.geom.primitive.Point, org.gvsig.fmap.geom.primitive.Point, org.gvsig.fmap.geom.primitive.Point)
	 */
	public void setPoints(Point p1, Point p2, Point p3) {
		Point2D _p1 = new java.awt.geom.Point2D.Double(p1.getCoordinateAt(0), p1.getCoordinateAt(1));
		Point2D _p2 = new java.awt.geom.Point2D.Double(p2.getCoordinateAt(0), p2.getCoordinateAt(1));
		Point2D _p3 = new java.awt.geom.Point2D.Double(p3.getCoordinateAt(0), p3.getCoordinateAt(1));
		setPoints(_p1, _p2, _p3);
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.Curve2D#setGeneralPath(org.gvsig.fmap.geom.primitive.GeneralPathX)
	 */
	public void setGeneralPath(GeneralPathX generalPathX) {
		throw new UnsupportedOperationException("Use setPoints(Point p1, Point p2, Point p3)");
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.Arc#setPoints(java.awt.geom.Point2D, java.awt.geom.Point2D, java.awt.geom.Point2D)
	 */
	private void setPoints(Point2D p1, Point2D p2, Point2D p3) {
		java.awt.geom.Arc2D arco = UtilFunctions.createArc(p1, p2, p3);
		if (arco == null) {
			throw new IllegalArgumentException();
		}
		this.gp = new GeneralPathX(arco);
		this.center = p1;
		this.init = p2;
		this.end = p3;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.Arc#setPoints(org.gvsig.fmap.geom.primitive.Point, double, double, double)
	 */
	public void setPoints(Point center, double radius, double initAngle,
			double angleExt) {
		Point2D _center = new java.awt.geom.Point2D.Double(center.getCoordinateAt(0), center.getCoordinateAt(1));
		setPoints(_center, radius, initAngle, angleExt);
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.Arc#setPoints(java.awt.geom.Point2D, double, double, double)
	 */
	private void setPoints(Point2D center, double radius, double initAngle,
			double angleExt) {
		java.awt.geom.Arc2D arco = UtilFunctions.createArc(center, radius, initAngle, angleExt);

		java.awt.geom.Arc2D semiarco = UtilFunctions.createArc(center, radius, initAngle, angleExt/2);
		if (arco == null || semiarco == null) {
			return;
		}

		Point2D p1 = arco.getStartPoint();
		Point2D p2 = semiarco.getEndPoint();
		Point2D p3 = arco.getEndPoint();

		setPoints(p1, p2, p3);		
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.Curve2D#setPoints(org.gvsig.fmap.geom.primitive.Point, org.gvsig.fmap.geom.primitive.Point)
	 */
	public void setPoints(Point startPoint, Point endPoint) {
		throw new UnsupportedOperationException("Use setGeneralPathX");
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.Arc#getEndPoint()
	 */
	public Point getEndPoint() {
		return new org.gvsig.fmap.geom.primitive.impl.Point2D(end.getX(), end.getY());
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.Arc#getInitPoint()
	 */
	public Point getInitPoint() {
		return new org.gvsig.fmap.geom.primitive.impl.Point2D(init.getX(), init.getY());
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.Arc#getCenterPoint()
	 */
	public Point getCenterPoint() {
		Point2D p = UtilFunctions.getCenter(init, center,end);
		try {
			return new org.gvsig.fmap.geom.primitive.impl.Point2D(p.getX(), p.getY());
		} catch (Exception e) {
			return  null;
		}
	}		
}
