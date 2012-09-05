/*
 * Cresques Mapping Suite. Graphic Library for constructing mapping applications.
 *
 * Copyright (C) 2004-5.
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
 * cresques@gmail.com
 */
package org.cresques.px.dxf;

import java.awt.geom.Point2D;
import java.util.Vector;


/**
 * Calcula puntos sobre un arco. Originalmente implementada para calcular los puntos
 * con un espaciamiento de un grado sexagesimal. Para el cálculo de los puntos del arco
 * utiliza el primer y el útlimo punto del arco y un parámetro de curvatura.
 * @author jmorell
 *
 */
public class DxfCalArcs {
    final boolean debug = true;
    Point2D coord1;
    Point2D coord2;
    Point2D center;
    double radio;
    double empieza;
    double acaba;
    double bulge;
    double d;
    double dd;
    double aci;
    Point2D coordAux;

    //private Point2D centralPoint;
    
    /**
     * Constructor de DxfCalArcs.
     * @param p1, punto inicial del arco.
     * @param p2, punto final del arco.
     * @param bulge, parámetro de curvatura.
     */
    public DxfCalArcs(Point2D p1, Point2D p2, double bulge) {
        this.bulge = bulge;

        /*System.out.println("DxfCalArcs: p1 = " + p1);
        System.out.println("DxfCalArcs: p2 = " + p2);
        System.out.println("DxfCalArcs: bulge = " + bulge);*/
        if (bulge < 0.0) {
            coord1 = p2;
            coord2 = p1;

            //System.out.println("DxfCalArcs: Bulge negativo; coord1 = " + coord1 + ", coord2 = " + coord2);
        } else {
            coord1 = p1;
            coord2 = p2;
        }

        calculate();
    }
    
    /**
     * Calcula puntos sobre un arco.
     * @return DxfCalArcs
     */
    DxfCalArcs calculate() {
        d = Math.sqrt(((coord2.getX() - coord1.getX()) * (coord2.getX() -
                      coord1.getX())) +
                      ((coord2.getY() - coord1.getY()) * (coord2.getY() -
                      coord1.getY())));

        //System.out.println("DxfCalArcs: distancia reducida = " + d);
        coordAux = new Point2D.Double((coord1.getX() + coord2.getX()) / 2.0,
                                      (coord1.getY() + coord2.getY()) / 2.0);

        //System.out.println("DxfCalArcs: punto medio = " + coordAux);			
        double b = Math.abs(bulge);

        //System.out.println("DxfCalArcs: Bulge(valor absoluto) = " + b);
        double beta = Math.atan(b);

        //System.out.println("DxfCalArcs: Angulo beta(rad) = " + beta);
        //double beta = Math.atan(bulge);
        double alfa = beta * 4.0;

        //System.out.println("DxfCalArcs: Angulo alfa(rad)(angulo que define el arco) = " + alfa);
        double landa = alfa / 2.0;

        //System.out.println("DxfCalArcs: Angulo landa(rad) = " + landa);
        dd = (d / 2.0) / (Math.tan(landa));
        radio = (d / 2.0) / (Math.sin(landa));

        //System.out.println("DxfCalArcs: radio del arco = " + radio);
        aci = Math.atan((coord2.getX() - coord1.getX()) / (coord2.getY() -
                        coord1.getY()));

        //System.out.println("DxfCalArcs: Acimut de coord1(pto origen) a coord2(pto final) = " + aci);
        double aciDegree = (aci * 180.0) / Math.PI;

        //System.out.println("DxfCalArcs: Acimut en grados = " + aciDegree);
        if (coord2.getY() > coord1.getY()) {
            aci += Math.PI;

            //System.out.println("DxfCalArcs: La coord Y de coord2 es mayor que la");
            //System.out.println("DxfCalArcs: coord Y de coord1, acimut = acimut + PI, nuevo acimut = " + aci);
            aciDegree = (aci * 180.0) / Math.PI;

            //System.out.println("DxfCalArcs: Nuevo acimut en grados = " + aciDegree);
        }

        center = new Point2D.Double(coordAux.getX() +
                                    (dd * Math.sin(aci + (Math.PI / 2.0))),
                                    coordAux.getY() +
                                    (dd * Math.cos(aci + (Math.PI / 2.0))));

        //System.out.println("DxfCalArcs: El centro del arco es: centro = " + center);
        calculateEA(alfa);

        return this;
    }
    
    /**
     * Calcula el ángulo de comienzo y el de finalización del arco.
     * @param alfa, ángulo del arco. 
     */
    void calculateEA(double alfa) {
        empieza = Math.atan2(coord1.getY() - center.getY(),
                             coord1.getX() - center.getX());
        acaba = (empieza + alfa);
        empieza = (empieza * 180.0) / Math.PI;
        acaba = (acaba * 180.0) / Math.PI;

        //System.out.println("DxfCalArcs: Acimut de inicio = " + empieza + ", Acimut de finalizacion = " + acaba);
        //System.out.println("Tener en cuenta que los angulos se miden en sentido antihorario.");
    }
    
    /**
     * Devuelve un Vector con los puntos que conforman el arco.
     * @param inc, es el espaciamiento entre puntos (un grado sexagesimal por defecto).
     * @return Vector de puntos.
     */
    public Vector getPoints(double inc) {
        //System.out.println("Se incia el metodo que construye el arco.");
        //System.out.println("Todo el proceso anterior define los parametros para esta construccion.");
        Vector arc = new Vector();
        double angulo;

        int iempieza = (int) empieza + 1; // ojo aqui !!
        int iacaba = (int) acaba;

        //System.out.println("DxfCalArcs: Angulo entero de inicio: iempieza = " + iempieza + ", Angulo entero de finalizacion: iacaba = " + iacaba);
        if (empieza <= acaba) {
            //System.out.println("El angulo de inicio es menor que el angulo de finalizacion.");
            addNode(arc, empieza);

            for (angulo = iempieza; angulo <= iacaba; angulo += inc) {
                addNode(arc, angulo);
            }

            addNode(arc, acaba);
        } else {
            //System.out.println("El angulo de inicio es mayor que el angulo de finalizacion.");
            addNode(arc, empieza);

            for (angulo = iempieza; angulo <= 360; angulo += inc) {
                addNode(arc, angulo);
            }

            for (angulo = 1; angulo <= iacaba; angulo += inc) {
                addNode(arc, angulo);
            }

            addNode(arc, angulo);
        }

        Point2D aux = (Point2D) arc.get(arc.size() - 1);
        double aux1 = Math.abs(aux.getX() - coord2.getX());
        double aux2 = Math.abs(aux.getY() - coord2.getY());

        /*if (aux1<=0.000005 && aux2<=0.000005) {
                arc.remove(arc.size()-1);
                arc.remove(arc.size()-1);
                arc.add(coord2);
        }*/
        return arc;
    }

    /**
     * Devuelve un Vector con puntos pertenecientes al arco y situados cerca de su
     * punto central.
     * @return Vector
     */
    public Vector getCentralPoint() {
        Vector arc = new Vector();

        if (empieza <= acaba) {
            addNode(arc, (empieza + acaba) / 2.0);
        } else {
            addNode(arc, empieza);

            double alfa = 360 - empieza;
            double beta = acaba;
            double an = alfa + beta;
            double mid = an / 2.0;

            if (mid <= alfa) {
                addNode(arc, empieza + mid);
            } else {
                addNode(arc, mid - alfa);
            }
        }

        return arc;
    }
    
    /**
     * Añade un punto al arco basandose en un ángulo dado.
     * @param arc, arco al que se añade el punto.
     * @param angulo, ángulo donde se localiza el punto.
     */
    private void addNode(Vector arc, double angulo) {
        double yy = center.getY() +
                    (radio * Math.sin((angulo * Math.PI) / 180.0));
        double xx = center.getX() +
                    (radio * Math.cos((angulo * Math.PI) / 180.0));
        arc.add(new Point2D.Double(xx, yy));

        //System.out.println("DxfCalArcs: Añade el punto " + new Point2D.Double(xx,yy) + ", correspondiente al angulo " + angulo);
    }
}
