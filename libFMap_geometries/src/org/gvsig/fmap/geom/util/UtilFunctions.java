/*
 * Created on 10-feb-2005
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
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
package org.gvsig.fmap.geom.util;

import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.Geometry.TYPES;
import org.gvsig.fmap.geom.aggregate.MultiPoint;
import org.gvsig.fmap.geom.exception.CreateEnvelopeException;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.primitive.Curve;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.geom.primitive.NullGeometry;
import org.gvsig.fmap.geom.primitive.Point;
import org.gvsig.fmap.geom.primitive.Surface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.algorithm.RobustCGAlgorithms;
import com.vividsolutions.jts.geom.Coordinate;

/**
 * @author FJP
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class UtilFunctions {
	private static final Logger logger = LoggerFactory.getLogger(GeometryManager.class);

	static public Arc2D createCircle(Point2D p1, Point2D p2, Point2D p3) //, Graphics g)
    {
        double xC, yC, w, h;

        // Calculamos 2 secantes, tiramos perpendiculares por sus puntos
        // medios y obtenemos el centro. Luego calculamos el radio.
        // Puntos medios de los segmentos.
        double xm1, ym1, xm2, ym2;
        xm1 = (p1.getX() + p2.getX())/ 2.0;
        ym1 = (p1.getY() + p2.getY())/ 2.0;
        xm2 = (p2.getX() + p3.getX())/ 2.0;
        ym2 = (p2.getY() + p3.getY())/ 2.0;

        /* g.setColor(Color.GRAY);
        g.draw3DRect((int)xm1, (int) ym1, 1, 1, true);
        g.draw3DRect((int)xm2, (int) ym2, 1, 1, true); */
        // Pendientes de las perpendiculares y constantes
        double mP1=0, mP2=0, A1, A2;
        boolean bPerp1 = false;
        //boolean bPerp2 = false;
        if (p2.getY() - p1.getY() == 0)
        {
            A1 = ym1;
            bPerp1 = true;
        }
        else
        {
            mP1 = (p2.getX() - p1.getX()) /(p1.getY() - p2.getY());
            A1 = ym1 - xm1 * mP1;
        }
        if (p2.getY() - p3.getY() == 0)
        {
            A2 = ym2;
            //bPerp2 = true;
        }
        else
        {
            mP2 = (p3.getX() - p2.getX()) /(p2.getY() - p3.getY());
            A2 = ym2 - xm2 * mP2;
        }
        if (mP2 == mP1)
        {
            return null; // Error, 3 puntos alineados. No puede pasar un arco
        }
        else
        {
            xC = (A2 - A1)/(mP1-mP2);
            if (!bPerp1) {
				yC = xC * mP1 + A1;
			} else {
				yC = xC * mP2 + A2;
			}
        }
        double Radio = p1.distance(xC, yC);
        double xR = xC - Radio ;
        double yR = yC - Radio ;
        w = 2.0* Radio;
        h = w;
        Rectangle2D.Double rBounds = new Rectangle2D.Double(xR,yR, w,h);
        Arc2D.Double resul = new Arc2D.Double(rBounds, 0.0, 360.0, Arc2D.OPEN);
		/* g.setColor(Color.RED);
		((Graphics2D) g).draw(resul);
		g.setColor(Color.BLUE);
		((Graphics2D) g).draw(rBounds);
		g.draw3DRect((int)p1.getX(), (int) p1.getY(), 1, 1, true);
		g.draw3DRect((int)p2.getX(), (int) p2.getY(), 2, 2, true);
		g.draw3DRect((int)p3.getX(), (int) p3.getY(), 1, 1, true);
		g.drawString("1", (int) p1.getX(), (int) p1.getY());
		g.drawString("2", (int) p2.getX(), (int) p2.getY());
		g.drawString("3", (int) p3.getX(), (int) p3.getY());
		g.drawString("C", (int) xC, (int) yC);
		g.draw3DRect((int)xC, (int) yC, 2, 2, true); */

        return resul;
    }
    /**
	 * Obtiene un par de puntos que definen la recta perpendicular a p1-p2 que
	 * pasa por el punto perp
	 *
	 * @param p1 punto de la recta p1-p2
	 * @param p2 punto de la recta p1-p2
	 * @param perp Punto por el que pasa la recta perpendicular, debe ser
	 * 		  distinto a p2
	 *
	 * @return Array con dos puntos que definen la recta resultante
	 */
	public static Point2D[] getPerpendicular(Point2D p1, Point2D p2,
		Point2D perp) {
		if ((p2.getY() - p1.getY()) == 0) {
			return new Point2D[] {
				new Point2D.Double(perp.getX(), 0),
				new Point2D.Double(perp.getX(), 1)
			};
		}

		//Pendiente de la recta perpendicular
		double m = (p1.getX() - p2.getX()) / (p2.getY() - p1.getY());

		//b de la funcion de la recta perpendicular
		double b = perp.getY() - (m * perp.getX());

		//Obtenemos un par de puntos
		Point2D[] res = new Point2D[2];

		res[0] = new Point2D.Double(0, (m * 0) + b);
		res[1] = new Point2D.Double(1000, (m * 1000) + b);

		return res;
	}
	public static Point2D[] getParallel(Point2D p1,Point2D p2,double distance) {
		Point2D[] pParallel=new Point2D[2];
		pParallel[0]=getPerpendicularPoint(p1,p2,p1,distance);
		pParallel[1]=getPerpendicularPoint(p1,p2,p2,distance);
		return pParallel;
	}

	/**
	 * Obtiene el punto que se encuentra a una distancia 'dist' de la recta
	 * p1-p2 y se encuentra en la recta perpendicular que pasa por perpPoint
	 *
	 * @param p1 Punto de la recta p1-p2
	 * @param p2 Punto de la recta p1-p2
	 * @param perpPoint Punto de la recta perpendicular
	 * @param dist Distancia del punto que se quiere obtener a la recta p1-p2
	 *
	 * @return DOCUMENT ME!
	 */
	public static Point2D getPerpendicularPoint(Point2D p1, Point2D p2,
		Point2D perpPoint, double dist) {
		Point2D[] p = getPerpendicular(p1, p2, perpPoint);
		Point2D unit = getUnitVector(p[0], p[1]);

		return new Point2D.Double(perpPoint.getX() + (unit.getX() * dist),
			perpPoint.getY() + (unit.getY() * dist));
	}

	/**
	 * Devuelve un vector unitario en forma de punto a partir de dos puntos.
	 *
	 * @param p1 punto origen.
	 * @param p2 punto destino.
	 *
	 * @return vector unitario.
	 */
	public static Point2D getUnitVector(Point2D p1, Point2D p2) {
		Point2D paux = new Point2D.Double(p2.getX() - p1.getX(),
				p2.getY() - p1.getY());
		double v = Math.sqrt(Math.pow(paux.getX(), 2d) +
				Math.pow(paux.getY(), 2d));
		paux = new Point2D.Double(paux.getX() / v, paux.getY() / v);

		return paux;
	}
	/**
	 * Obtiene el centro del c�rculo que pasa por los tres puntos que se pasan
	 * como  par�metro
	 *
	 * @param p1 primer punto del c�rculo cuyo centro se quiere obtener
	 * @param p2 segundo punto del c�rculo cuyo centro se quiere obtener
	 * @param p3 tercer punto del c�rculo cuyo centro se quiere obtener
	 *
	 * @return Devuelve null si los puntos est�n alineados o no son 3 puntos
	 * 		   distintos
	 */
	public static Point2D getCenter(Point2D p1, Point2D p2, Point2D p3) {
		if (p1.equals(p2) || p2.equals(p3) || p1.equals(p3)) {
			return null;
		}

		Point2D[] perp1 = getPerpendicular(p1, p2,
				new Point2D.Double((p1.getX() + p2.getX()) / 2,
					(p1.getY() + p2.getY()) / 2));
		Point2D[] perp2 = getPerpendicular(p2, p3,
				new Point2D.Double((p2.getX() + p3.getX()) / 2,
					(p2.getY() + p3.getY()) / 2));

		return getIntersection(perp1[0], perp1[1], perp2[0], perp2[1]);
	}


	/**
	 * Devuelve el punto de la intersecci�n entre las lineas p1-p2 y p3-p4.
	 *
	 * @param p1 punto de la recta p1-p2
	 * @param p2 punto de la recta p1-p2
	 * @param p3 punto de la recta p3-p4
	 * @param p4 punto de la recta p3-p4
	 *
	 * @return DOCUMENT ME!
	 *
	 * @throws RuntimeException DOCUMENT ME!
	 */
	public static Point2D getIntersection(Point2D p1, Point2D p2, Point2D p3,
		Point2D p4) {
		double m1 = Double.POSITIVE_INFINITY;

		if ((p2.getX() - p1.getX()) != 0) {
			m1 = (p2.getY() - p1.getY()) / (p2.getX() - p1.getX());
		}

		double m2 = Double.POSITIVE_INFINITY;

		if ((p4.getX() - p3.getX()) != 0) {
			m2 = (p4.getY() - p3.getY()) / (p4.getX() - p3.getX());
		}

		if ((m1 == Double.POSITIVE_INFINITY) &&
				(m2 == Double.POSITIVE_INFINITY)) {
			return null;
		}

		double b1 = p2.getY() - (m1 * p2.getX());

		double b2 = p4.getY() - (m2 * p4.getX());

		if ((m1 != Double.POSITIVE_INFINITY) &&
				(m2 != Double.POSITIVE_INFINITY)) {
			if (m1 == m2) {
				return null;
			}

			double x = (b2 - b1) / (m1 - m2);

			return new Point2D.Double(x, (m1 * x) + b1);
		} else if (m1 == Double.POSITIVE_INFINITY) {
			double x = p1.getX();

			return new Point2D.Double(x, (m2 * x) + b2);
		} else if (m2 == Double.POSITIVE_INFINITY) {
			double x = p3.getX();

			return new Point2D.Double(x, (m1 * x) + b1);
		}

		//no llega nunca
		throw new RuntimeException("BUG!");
	}
	/**
	 * Obtiene el �ngulo del vector que se pasa como par�metro con el vector
	 * horizontal de izquierda a derecha
	 *
	 * @param start punto origen del vector
	 * @param end punto destino del vector
	 *
	 * @return angulo en radianes
	 */
	public static double getAngle(Point2D start, Point2D end) {
		double angle = Math.acos((end.getX() - start.getX()) / start.distance(
					end));

		if (start.getY() > end.getY()) {
			angle = -angle;
		}

		if (angle < 0) {
			angle += (2 * Math.PI);
		}

		return angle;
	}
	/**
	 * Devuelve la distancia desde angle1 a angle2. Angulo en radianes de
	 * diferencia entre angle1 y angle2 en sentido antihorario
	 *
	 * @param angle1 angulo en radianes. Debe ser positivo y no dar ninguna
	 * 		  vuelta a la circunferencia
	 * @param angle2 angulo en radianes. Debe ser positivo y no dar ninguna
	 * 		  vuelta a la circunferencia
	 *
	 * @return distancia entre los �ngulos
	 */
	public static double angleDistance(double angle1, double angle2) {
		if (angle1 < angle2) {
			return angle2 - angle1;
		} else {
			return ((Math.PI * 2) - angle1) + angle2;
		}
	}
	/**
	 * Devuelve el punto de la recta que viene dada por los puntos p1 y p2 a
	 * una distancia radio de p1.
	 *
	 * @param p1 DOCUMENT ME!
	 * @param p2 DOCUMENT ME!
	 * @param radio DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public static Point2D getPoint(Point2D p1, Point2D p2, double radio) {
		Point2D paux = new Point2D.Double(p2.getX() - p1.getX(),
				p2.getY() - p1.getY());
		double v = Math.sqrt(Math.pow(paux.getX(), 2d) +
				Math.pow(paux.getY(), 2d));
		paux = new Point2D.Double(paux.getX() / v, paux.getY() / v);

		Point2D aux1 = new Point2D.Double(p1.getX() + (radio * paux.getX()),
				p1.getY() + (radio * paux.getY()));

		return aux1;
	}
	/**
	 * Devuelve la menor distancia desde angle1 a angle2.
	 *
	 * @param angle1 angulo en radianes. Debe ser positivo y no dar ninguna
	 * 		  vuelta a la circunferencia
	 * @param angle2 angulo en radianes. Debe ser positivo y no dar ninguna
	 * 		  vuelta a la circunferencia
	 *
	 * @return distancia entre los �ngulos
	 */
	public static double absoluteAngleDistance(double angle1, double angle2) {
		double d = Math.abs(angle1 - angle2);

		if (d < Math.PI) {
			return d;
		} else {
			if (angle1 < angle2) {
				angle2 -= (Math.PI * 2);
			} else {
				angle1 -= (Math.PI * 2);
			}

			return Math.abs(angle1 - angle2);
		}
	}
	/**
	 * Obtiene un arco a partir de 3 puntos. Devuelve null si no se puede crear
	 * el arco porque los puntos est�n alineados o los 3 puntos no son
	 * distintos
	 *
	 * @param p1
	 * @param p2
	 * @param p3
	 *
	 * @return Arco
	 */
	public static Arc2D createArc(Point2D p1, Point2D p2, Point2D p3) {
		Point2D center = getCenter(p1, p2, p3);

		if (center == null) {
			return null;
		}

		double angle1 = getAngle(center, p1);
		double angle2 = getAngle(center, p3);
		double extent = angleDistance(angle1, angle2);

		Coordinate[] coords = new Coordinate[4];
		coords[0] = new Coordinate(p1.getX(), p1.getY());
		coords[1] = new Coordinate(p2.getX(), p2.getY());
		coords[2] = new Coordinate(p3.getX(), p3.getY());
		coords[3] = new Coordinate(p1.getX(), p1.getY());

		if (!RobustCGAlgorithms.isCCW(coords)) {
			extent = (Math.PI * 2) - extent;
		} else {
			extent = -extent;
		}

		//System.err.println("angle1:" + angle1);
		//System.err.println("angle2:" + getAngle(center, p2));
		//System.err.println("angle3:" + angle2);
		//System.err.println("extent:" + extent);
		double Radio = p1.distance(center);
		double xR = center.getX() - Radio;
		double yR = center.getY() - Radio;
		double w = 2.0 * Radio;
		double h = w;

		Rectangle2D.Double rBounds = new Rectangle2D.Double(xR, yR, w, h);
		Arc2D.Double resul = new Arc2D.Double(rBounds,
				Math.toDegrees((Math.PI * 2) - angle1), Math.toDegrees(extent),
				Arc2D.OPEN);

		return resul;
	}

	/**
	 * Obtiene un arco a partir del centro, radio, angulo inicial y extension del angulo.
	 * Devuelve null si no lo puede crear.
	 *
	 * @param center
	 * @param radius
	 * @param angSt en radianes
	 * @param angExt en radianes
	 *
	 * @return Arco
	 */
	public static Arc2D createArc(Point2D center, double radius, double angSt, double angExt) {
		double xR = center.getX() - radius;
		double yR = center.getY() - radius;
		double w = 2.0 * radius;
		double h = w;

		Rectangle2D.Double rBounds = new Rectangle2D.Double(xR, yR, w, h);
		Arc2D.Double resul = new Arc2D.Double(rBounds,
				Math.toDegrees((Math.PI * 2) - angSt), Math.toDegrees(angExt),
				Arc2D.OPEN);

		return resul;
	}

	/**
	 * Obtiene un arco a partir del
	 *  centro del arco y punto inicio y punto final
	 *  Suponemos un Arco definicio CCW (CounterClockWise)
	 * @param center
	 * @param init
	 * @param end
	 *
	 * @return Arco
	 */
	public static Arc2D createArc2points(Point2D center, Point2D init, Point2D end) {

		double angle1 = getAngle(center, init);
		double angle2 = getAngle(center, end);
		double extent = angleDistance(angle1, angle2);

		extent = -extent; // CCW

		//System.err.println("angle1:" + angle1);
		//System.err.println("angle2:" + getAngle(center, p2));
		//System.err.println("angle3:" + angle2);
		//System.err.println("extent:" + extent);
		double Radio = init.distance(center);
		double xR = center.getX() - Radio;
		double yR = center.getY() - Radio;
		double w = 2.0 * Radio;
		double h = w;

		Rectangle2D.Double rBounds = new Rectangle2D.Double(xR, yR, w, h);
		Arc2D.Double resul = new Arc2D.Double(rBounds,
				Math.toDegrees((Math.PI * 2) - angle1), Math.toDegrees(extent),
				Arc2D.OPEN);

		return resul;
	}

	/**
	 * Devuelve el punto a una distancia radio del punto p1 y aplicandole un �ngulo an.
	 * una distancia radio de p1.
	 *
	 * @param p1 DOCUMENT ME!
	 * @param p2 DOCUMENT ME!
	 * @param radio DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public static Point2D getPoint(Point2D p1, double an, double radio) {
		double x=(radio*Math.cos(an))+p1.getX();
		double y=(radio*Math.sin(an))+p1.getY();

		Point2D p=new Point2D.Double(x,y);

		return p;
	}

	/**
	 * Obtiene una linea a partir de dos puntos.
	 * Devuelve null si no lo puede crear.
	 *
	 * @param start
	 * @param end
	 *
	 * @return Linea
	 */
	public static Line2D createLine(Point2D start, Point2D end) {
		return new Line2D.Double(start, end);

	}


	/**
	 * DOCUMENT ME!
	 *
	 * @param antp DOCUMENT ME!
	 * @param lastp DOCUMENT ME!
	 * @param interp DOCUMENT ME!
	 * @param point DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public static boolean isLowAngle(Point2D antp, Point2D lastp,
		Point2D interp, Point2D point) {
		///double ob=lastp.distance(point);
		///Point2D[] aux=getPerpendicular(lastp,interp,point);
		///Point2D intersect=getIntersection(aux[0],aux[1],lastp,interp);
		///double pb=intersect.distance(point);
		///double a=Math.asin(pb/ob);
		Coordinate[] coords = new Coordinate[4];
		coords[0] = new Coordinate(lastp.getX(), lastp.getY());
		coords[1] = new Coordinate(interp.getX(), interp.getY());
		coords[2] = new Coordinate(point.getX(), point.getY());
		coords[3] = new Coordinate(lastp.getX(), lastp.getY());

		try {
			double angle1 = getAngle(antp, lastp);
			System.out.println("angle1= " + angle1);

			double angle2 = getAngle(lastp, point);
			System.out.println("angle2= " + angle2);

			/*if (lastp.getX()<antp.getX()){
			   System.out.println("angleDiff 2 1= "+angleDistance(angle2,angle1));
			   System.out.println("angleDiff 1 2= "+angleDistance(angle1,angle2));
			   if (angleDistance(angle2,angle1)>Math.PI){

			   if (RobustCGAlgorithms.isCCW(coords)) {
			           System.out.println("izquierda,arriba,true");
			           return true;
			   } else{
			           System.out.println("izquierda,arriba,false");
			   }
			   }else {
			           if (!RobustCGAlgorithms.isCCW(coords)) {
			                   System.out.println("izquierda,abajo,true");
			                   return true;
			           } else{
			                   System.out.println("izquierda,abajo,false");
			           }
			   }
			   }else if (lastp.getX()>antp.getX()){
			 */
			System.out.println("angleDifl 2 1= " +
				angleDistance(angle2, angle1));
			System.out.println("angleDifl 1 2= " +
				angleDistance(angle1, angle2));

			if (angleDistance(angle2, angle1) > Math.PI) {
				if (RobustCGAlgorithms.isCCW(coords)) {
					System.out.println("derecha,arriba,true");

					return true;
				} else {
					System.out.println("derecha,arriba,false");
				}
			} else {
				if (!RobustCGAlgorithms.isCCW(coords)) {
					System.out.println("derecha,abajo,true");

					return true;
				} else {
					System.out.println("derecha,abajo,false");
				}
			}

			//}
		} catch (Exception e) {
			System.out.println("false");

			return true;
		}

		return false;
	}

	/* movido a AbstractPrimitive */

	public static void rotateGeom(Geometry geometry, double radAngle, double basex, double basey) {
		AffineTransform at = new AffineTransform();
		at.rotate(radAngle,basex,basey);
		geometry.transform(at);

	}
	public static void moveGeom(Geometry geometry, double dx, double dy) {
        AffineTransform at = new AffineTransform();
        at.translate(dx, dy);
        geometry.transform(at);


	}
	public static void scaleGeom(Geometry geometry, Point2D basePoint, double sx, double sy) {
		AffineTransform at = new AffineTransform();
		at.setToTranslation(basePoint.getX(),basePoint.getY());
		at.scale(sx,sy);
		at.translate(-basePoint.getX(),-basePoint.getY());
		geometry.transform(at);
	} 

}
