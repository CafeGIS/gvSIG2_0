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
package org.gvsig.fmap.mapcontext.rendering.symbols.styles;

import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Hashtable;

import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineSegment;
/**
 *
 * Line2DOffset.java
 *
 *
 * @author jaume dominguez faus - jaume.dominguez@iver.es Jan 3, 2008
 *
 */

public class Line2DOffset {
	final static private Logger logger = LoggerFactory.getLogger(Line2DOffset.class);

	public static GeneralPathX offsetLine(Shape p, double offset) {

		if (Math.abs(offset) < 1) {
			return new GeneralPathX(p);
		}
		PathIterator pi = p.getPathIterator(null);
		double[] dataCoords = new double[6];
		Coordinate from = null, first = null;
		ArrayList segments = new ArrayList(); //<LineSegment>
		GeneralPathX offsetSegments = new GeneralPathX();
		LineSegment line;
		try {
			while (!pi.isDone()) {
				// while not done
				int type = pi.currentSegment(dataCoords);

				switch (type) {
				case PathIterator.SEG_MOVETO:
					from = new Coordinate(dataCoords[0], dataCoords[1]);
					first = from;
					break;

				case PathIterator.SEG_LINETO:

					// System.out.println("SEG_LINETO");
					Coordinate to = new Coordinate(dataCoords[0], dataCoords[1]);
					if(from.compareTo(to)!=0){
						line = new LineSegment(from, to);
						segments.add(line);
						from = to;
					}
					break;
				case PathIterator.SEG_CLOSE:
					line = new LineSegment(from, first);
					segments.add(line);
					from = first;
					try {
						offsetSegments.append(offsetAndConsumeClosedSegments(
								offset, segments), false);
					} catch (NotEnoughSegmentsToClosePathException e) {
						logger.error(
								e.getMessage(), e);
					}
					break;

				} // end switch

				pi.next();
			}
			offsetSegments.append(offsetAndConsumeSegments(offset, segments),
					true);

			return offsetSegments;
		} catch (ParallelLinesCannotBeResolvedException e) {
			logger.error(e.getMessage(), e);
			return new GeneralPathX(p);
		}
	}

	private static GeneralPathX offsetAndConsumeSegments(double offset,
			ArrayList segments)
	throws ParallelLinesCannotBeResolvedException {
		Hashtable offsetLines = new Hashtable(); //<LineSegment, LineEquation>
		int segmentCount = segments.size();
		// first calculate offset lines with the starting point
		for (int i = 0; i < segmentCount; i++) {
			LineSegment segment = (LineSegment)segments.get(i);
			double theta = segment.angle();
			//FIXME: ¿Esto para qué es?
//			if (Math.abs(theta) % (Math.PI*0.5) < 0.00001){
//				theta=theta+0.00000000000001;
//			}

			double xOffset = offset * Math.sin(theta);
			double yOffset = offset * Math.cos(theta);

			Coordinate p0 = segment.p0;
			double x0 = p0.x + xOffset;
			double y0 = p0.y - yOffset;

			Coordinate p1 = segment.p1;
			double x1 = p1.x + xOffset;
			double y1 = p1.y - yOffset;

			LineEquation offsetLine = new LineEquation(theta, x0, y0, x1, y1);
			offsetLines.put(segment, offsetLine);
		}

		/*
		 * let's now calculate the end point of each segment with the point
		 * where each line crosses the next one. this point will be the end
		 * point of the first line, and the start point of its next one.
		 */
		Point2D pIni = null;
		Point2D pEnd = null;
		GeneralPathX gpx = new GeneralPathX();
		for (int i = 0; i < segmentCount; i++) {
			LineSegment segment = (LineSegment)segments.get(0);
			LineEquation eq = (LineEquation)offsetLines.get(segment);
			if (i == 0) {
				pIni = new Point2D.Double(eq.x, eq.y);
			} else {
				pIni = pEnd;
			}

			if (i < segmentCount - 1) {
				LineEquation eq1 = (LineEquation) offsetLines.get(segments.get(1));
				try{
					pEnd = eq.resolve(eq1);
				} catch (ParallelLinesCannotBeResolvedException e) { //Las lineas son paralelas y NO son la misma.
					pEnd = new Point2D.Double(eq.xEnd, eq.yEnd);
					gpx.append(new Line2D.Double(pIni, pEnd), true); // añadimos una linea hasta el final del primer segmento
//					 y asignamos como punto final el principio del siguiente segmento
//					 para que en la siguiente iteración lo tome como punto inicial.
					pIni = pEnd;
					pEnd = new Point2D.Double(eq1.x, eq1.y);
					segments.remove(0);
					continue;
				}
			} else {
				pEnd = new Point2D.Double(eq.xEnd, eq.yEnd);
			}

			gpx.append(new Line2D.Double(pIni, pEnd), true);
			segments.remove(0);
		}
		return gpx;
	}

	private static GeneralPathX offsetAndConsumeClosedSegments(double offset,
			ArrayList segments)
	throws ParallelLinesCannotBeResolvedException,
	NotEnoughSegmentsToClosePathException {
		int segmentCount = segments.size();
		if (segmentCount > 1) {
			GeneralPathX openPath = offsetAndConsumeSegments(offset, segments);
			openPath.closePath();
			return openPath;
		}
		throw new NotEnoughSegmentsToClosePathException(segments);
	}
}

class LineEquation {
	double theta, m, x, y;

	double xEnd, yEnd; // just for simplicity of code

	public LineEquation(double theta, double x, double y, double xEnd,
			double yEnd) {
//		this.theta = Math.tan(theta); //Esto es un error, no podemos confundir el angulo de la recta con su pendiente
		this.theta = theta;
		this.m = Math.tan(theta);
		this.x = x;
		this.y = y;
		this.xEnd = xEnd;
		this.yEnd = yEnd;
	}

	public Point2D resolve(LineEquation otherLine)
	throws ParallelLinesCannotBeResolvedException {
		/*
		 * line1 (this): y - y0 = m*(x - x0)
		 * line2 (otherLine): y' - y'0 = m'*(x' - x'0)
		 */

		double X;
		double Y;
		if(Math.abs(this.x - this.xEnd)<0.00001) { //Esta linea es vertical
//			System.out.println("1 VERTICAL");
			X = this.xEnd;
			if (Math.abs(otherLine.x - otherLine.xEnd)<0.00001){//La otra linea es vertical
//				System.out.println("    2 PERPENDICULAR");
				if(Math.abs(this.x - otherLine.x)<0.00001){ //Son la misma linea, devolvemos el primer punto de la otra linea.
//					System.out.println("        MISMA LINEA");
					Y = otherLine.y;
				} else { //No son la misma linea, entonces son paralelas, excepcion.
//					System.out.println("        CASCO POR 1");
					throw new ParallelLinesCannotBeResolvedException(this, otherLine);
				}
			} else if (Math.abs(otherLine.y - otherLine.yEnd)<0.00001) { //La otra linea es horizontal
//				System.out.println("    2 HORIZONTAL");
				Y = otherLine.y;
			} else { //Si no
//				System.out.println("    2 CUALQUIERA");
				Y = otherLine.m*(X - otherLine.x)+otherLine.y;
			}

		} else if (Math.abs(this.y - this.yEnd)<0.00001) { //Esta linea es horizontal
//			System.out.println("1 HORIZONTAL");
			Y = this.yEnd;
			if (Math.abs(otherLine.y - otherLine.yEnd)<0.00001) { //La otra linea es horizontal
//				System.out.println("    2 HORIZONTAL");
				if(Math.abs(this.y - otherLine.y)<0.00001){ //Son la misma linea, devolvemos el primer punto de la otra linea.
//					System.out.println("        MISMA LINEA");
					X = otherLine.x;
				} else { //No son la misma linea, entonces son paralelas, excepcion.
//					System.out.println("        CASCO POR 2");
					throw new ParallelLinesCannotBeResolvedException(this, otherLine);
				}
			} else if (Math.abs(otherLine.x - otherLine.xEnd)<0.00001){//La otra linea es vertical
//				System.out.println("    2 VERTICAL");
				X = otherLine.x;
			} else { //Si no
//				System.out.println("    2 CUALQUIERA");
				X = (Y - otherLine.y)/otherLine.m +otherLine.x;
			}
		} else { //Esta linea no es ni vertical ni horizontal
//			System.out.println("1 CUALQUIERA");
			if (Math.abs(otherLine.y - otherLine.yEnd)<0.00001) { //La otra linea es horizontal
//				System.out.println("    2 HORIZONTAL");
				Y = otherLine.y;
				X = (Y - this.y)/this.m +this.x;
			} else if (Math.abs(otherLine.x - otherLine.xEnd)<0.00001){//La otra linea es vertical
//				System.out.println("    2 VERTICAL");
				X = otherLine.x;
				Y = this.m*(X - this.x)+this.y;
			} else if ((Math.abs(otherLine.m - this.m)<0.00001)) { //Tienen la misma pendiente
//				System.out.println("    MISMA PENDIENTE");
				Y = otherLine.m*(this.x - otherLine.x)+otherLine.y;
				if (Math.abs(this.y - Y)<0.00001){ //Las lineas son la misma
//					System.out.println("        MISMA LINEA");
					X = otherLine.x;
					Y = otherLine.y;
				} else {
//					System.out.println("        CASCO POR 3");
					throw new ParallelLinesCannotBeResolvedException(this, otherLine);
				}
			} else {
//				System.out.println("    AMBAS CUALESQUIERA");
				double mTimesX = this.m * this.x;
				X = (mTimesX - this.y - otherLine.m * otherLine.x + otherLine.y) / (this.m - otherLine.m);
				Y = this.m * X - mTimesX + this.y;
			}
		}

//		System.out.println("DEVOLVEMOS X = "+X+" Y = "+Y);
		return new Point2D.Double(X, Y);

	}

	public String toString() {
		return "Y - " + y + " = " + m + "*(X - " + x + ")";
	}
}

class NotEnoughSegmentsToClosePathException extends Exception {
	private static final long serialVersionUID = 95503944546535L;

	public NotEnoughSegmentsToClosePathException(ArrayList segments) {
		super("Need at least 2 segments to close a path. I've got "
				+ segments.size() + ".");
	}
}

class ParallelLinesCannotBeResolvedException extends Exception {
	private static final long serialVersionUID = 8322556508820067641L;

	public ParallelLinesCannotBeResolvedException(LineEquation eq1,
			LineEquation eq2) {
		super("Lines '" + eq1 + "' and '" + eq2
				+ "' are parallel and don't share any point!");
	}
}
//public class Line2DOffset {

//private static final double TOL = 1E-8;
//private static final double ANGLE_TOL = 0.01/180*Math.PI;

//public static GeneralPathX offsetLine(Shape p, double offset) {

//PathIterator pi = p.getPathIterator(null);
//double[] dataCoords = new double[6];
//Coordinate from = null, first = null;
//ArrayList<LineSegment> segments = new ArrayList<LineSegment>();
//GeneralPathX offsetSegments = new GeneralPathX();
//try {
//while (!pi.isDone()) {
//// while not done
//int type = pi.currentSegment(dataCoords);

//switch (type) {
//case PathIterator.SEG_MOVETO:
//from = new Coordinate(dataCoords[0], dataCoords[1]);
//first = from;
//break;

//case PathIterator.SEG_LINETO:

//// System.out.println("SEG_LINETO");
//Coordinate to = new Coordinate(dataCoords[0], dataCoords[1]);
//LineSegment line = new LineSegment(from, to);
//int size = segments.size();
//if (size>0) {
//LineSegment prev = segments.get(size-1);
//if (line.angle() == prev.angle()) {
//if (Math.abs(line.p0.x - prev.p1.x) < TOL &&
//Math.abs(line.p0.y - prev.p1.y) < TOL) {
//prev.p1 = line.p1;
//break;
//}
//}
//}
//from = to;
//segments.add(line);

//break;
//case PathIterator.SEG_CLOSE:
//line = new LineSegment(from, first);
//segments.add(line);
//from = first;
//try {
//offsetSegments.append(offsetAndConsumeClosedSegments(offset, segments), false);
//} catch (NotEnoughSegmentsToClosePathException e) {
//Logger.getLogger(Line2DOffset.class).error(e.getMessage(), e);
//}
//break;

//} // end switch

//pi.next();
//}
//offsetSegments.append(offsetAndConsumeSegments(offset, segments), true);

//return offsetSegments;
//} catch (ParallelLinesCannotBeResolvedException e) {
//Logger.getLogger(Line2DOffset.class).error(e.getMessage(), e);
//return new GeneralPathX(p);
//}
//}

//private static GeneralPathX offsetAndConsumeSegments(double offset, ArrayList<LineSegment> segments)  {
//Hashtable<LineSegment, LineEquation> offsetLines = new Hashtable<LineSegment, LineEquation>();
//int segmentCount = segments.size();
//// first calculate offset lines with the starting point
//for (int i = 0; i < segmentCount; i++) {
//LineSegment segment = segments.get(i);
//double theta = segment.angle();

//double xOffset = offset*Math.sin(theta);
//double yOffset = offset*Math.cos(theta);

//Coordinate p0 = segment.p0;
//double x0 = p0.x + xOffset;
//double y0 = p0.y - yOffset;

//Coordinate p1 = segment.p1;
//double x1 = p1.x + xOffset;
//double y1 = p1.y - yOffset;

//LineEquation offsetLine = new LineEquation(theta, x0, y0, x1, y1, offset);
//offsetLines.put(segment, offsetLine);
//}

///*
//* let's now calculate the end point of each segment with
//* the point where each line crosses the next one.
//* this point will be the end point of the first line, and
//* the start point of its next one.
//*/
//Point2D pIni = null;
//Point2D pEnd = null;
//GeneralPathX gpx = new GeneralPathX();
//for (int i = 0; i < segmentCount; i++) {
//LineSegment segment = segments.get(0);
//LineEquation eq = offsetLines.get(segment);
//Point2D pAux = null;
//if (i < segmentCount -1) {
//try {
//pAux = eq.resolve(offsetLines.get(segments.get(1)));
//if (i == 0) {
//pIni = new Point2D.Double(eq.x, eq.y);
//} else {
//pIni = pEnd;
//}
//} catch (ParallelLinesCannotBeResolvedException e) {
//segments.remove(0);
//continue;
//}
//}


//if (pAux != null) {
//pEnd = pAux;
//} else {
//pEnd = new Point2D.Double(eq.xEnd, eq.yEnd);
//}

//gpx.append(new Line2D.Double(pIni, pEnd), true);
//segments.remove(0);
//}
//return gpx;
//}

//private static GeneralPathX offsetAndConsumeClosedSegments(double offset, ArrayList<LineSegment> segments) throws ParallelLinesCannotBeResolvedException, NotEnoughSegmentsToClosePathException {
//int segmentCount = segments.size();
//if (segmentCount > 1) {
//GeneralPathX openPath = offsetAndConsumeSegments(offset, segments);
//openPath.closePath();
//return openPath;
//}
//throw new NotEnoughSegmentsToClosePathException(segments);
//}
//}

//class LineEquation {
//double theta, x, y;
//double xEnd, yEnd; // just for simplicity of code
//double offset;

//public LineEquation(double theta, double x, double y, double xEnd, double yEnd, double offset) {
//this.theta = theta;
//this.x = x;
//this.y = y;
//this.xEnd = xEnd;
//this.yEnd = yEnd;
//this.offset = offset;
//}

//public Point2D resolve(LineEquation otherLine) throws ParallelLinesCannotBeResolvedException {
//double X;
//double Y;


///*
//* line1 (this):      y  -  y0 =  m*(x  - x0)
//* line2 (otherLine): y' - y'0 = m'*(x' - x'0)
//*/
//if (otherLine.theta == this.theta)
//throw new ParallelLinesCannotBeResolvedException(this, otherLine);

//if (Math.cos(theta) == 0) {

//X = otherLine.x + offset*Math.cos(otherLine.theta);
//Y = otherLine.y + offset*Math.sin(otherLine.theta);
//} else if (Math.cos(otherLine.theta) == 0) {
//X = x + offset*Math.cos(theta);
//Y = y + offset*Math.sin(theta);
//} else {
///*
//* m*(X - x0) + y0 = m'*(X - x'0) + y0'
//* X = (m*x0 - y0 - m'*x0' + y'0) / (m - m')
//*/
//double tanTheta = Math.tan(theta);
//double otherTanTheta = Math.tan(otherLine.theta);
//double thetaTimesX = tanTheta*this.x;
//X = (thetaTimesX - this.y - otherTanTheta*otherLine.x + otherLine.y) / (tanTheta - otherTanTheta);

///*
//* Y - y0 = m*(X - x0)
//* Y = m*X - m*x0 + y0
//*/
//Y = tanTheta*X - thetaTimesX + this.y;
//}
//return new Point2D.Double(X, Y);
//}

//@Override
//public String toString() {
//return "Y - "+y+" = "+theta+"*(X - "+x+")";
//}
//}

//class NotEnoughSegmentsToClosePathException extends Exception {
//private static final long serialVersionUID = 95503944546535L;
//public NotEnoughSegmentsToClosePathException(ArrayList<LineSegment> segments) {
//super("Need at least 2 segments to close a path. I've got "+segments.size()+".");
//}
//}

//class ParallelLinesCannotBeResolvedException extends Exception {
//private static final long serialVersionUID = 8322556508820067641L;

//public ParallelLinesCannotBeResolvedException(LineEquation eq1, LineEquation eq2) {
//super("Lines '"+eq1+"' and '"+eq2+"' are parallel and don't share any point!");
//}
//}