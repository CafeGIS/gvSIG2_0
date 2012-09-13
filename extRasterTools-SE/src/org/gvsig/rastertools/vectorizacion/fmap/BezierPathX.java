/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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
 */
package org.gvsig.rastertools.vectorizacion.fmap;

import java.awt.geom.Point2D;

import org.gvsig.fmap.geom.primitive.GeneralPathX;
/**
 * Reimplementacion del GeneralPathX para que acepte curvas de Bezier
 * 
 * @version 28/08/2008
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class BezierPathX extends GeneralPathX {
	private static final long serialVersionUID = -77239133302246295L;
	private Point2D lastPoint      = new Point2D.Double(0, 0);
	private int     numberOfPoints = 1;

	/**
	 * Crea un GeneralPathX definiendo cuantos cortes tendrá en las curvas de
	 * Bezier. Cuanto mayor sea el numero de puntos, mejor definido estará
	 * @param numberOfPoints
	 */
	public BezierPathX(int numberOfPoints) {
		this.numberOfPoints = numberOfPoints;
	}

	/**
	 * Devuelve el punto de la curva de Bezier para el intervalo t
	 * @param cp
	 * @param t
	 * @return
	 */
	private Point2D pointOnCubicBezier(Point2D[] cp, double t) {
		double ax, bx, cx;
		double ay, by, cy;
		double tSquared, tCubed;
		Point2D result = new Point2D.Double();

		/* cálculo de los coeficientes polinomiales */

		cx = 3.0 * (cp[1].getX() - cp[0].getX());
		bx = 3.0 * (cp[2].getX() - cp[1].getX()) - cx;
		ax = cp[3].getX() - cp[0].getX() - cx - bx;

		cy = 3.0 * (cp[1].getY() - cp[0].getY());
		by = 3.0 * (cp[2].getY() - cp[1].getY()) - cy;
		ay = cp[3].getY() - cp[0].getY() - cy - by;

		/* calculate the curve point at parameter value t */

		tSquared = t * t;
		tCubed = tSquared * t;

		result.setLocation((ax * tCubed) + (bx * tSquared) + (cx * t) + cp[0].getX(), (ay * tCubed) + (by * tSquared) + (cy * t) + cp[0].getY());

		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.core.GeneralPathX#curveTo(double, double, double, double, double, double)
	 */
	public synchronized void curveTo(double x1, double y1, double x2, double y2, double x3, double y3) {
		Point2D points[] = new Point2D[4];
		points[0] = new Point2D.Double(lastPoint.getX(), lastPoint.getY());
		points[1] = new Point2D.Double(x1, y1);
		points[2] = new Point2D.Double(x2, y2);
		points[3] = new Point2D.Double(x3, y3);

		double dt = 1.0 / (numberOfPoints - 1);

		Point2D aux;
		for (int i = 0; i < numberOfPoints; i++) {
			aux = pointOnCubicBezier(points, i * dt);
			lineTo(aux.getX(), aux.getY());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.core.GeneralPathX#lineTo(double, double)
	 */
	public synchronized void lineTo(double x, double y) {
		super.lineTo(x, y);
		lastPoint.setLocation(x, y);
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.core.GeneralPathX#moveTo(double, double)
	 */
	public synchronized void moveTo(double x, double y) {
		super.moveTo(x, y);
		lastPoint.setLocation(x, y);
	}

	public synchronized void moveTo(Point2D point) {
		this.moveTo(point.getX(), point.getY());
	}

	public synchronized void lineTo(Point2D point) {
		this.lineTo(point.getX(), point.getY());
	}
	
	public synchronized void curveTo(Point2D point1, Point2D point2, Point2D point3) {
		this.curveTo(point1.getX(), point1.getY(), point2.getX(), point2.getY(), point3.getX(), point3.getY());
	}
}