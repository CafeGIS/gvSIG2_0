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
package com.iver.cit.gvsig.project.documents.layout.geometryadapters;

import java.awt.geom.Point2D;

import org.gvsig.fmap.geom.primitive.GeneralPathX;

/**
 * DOCUMENT ME!
 *
 * @author Vicente Caballero Navarro
 */
public class RectangleAdapter extends PolygonAdapter {
   /**
     * DOCUMENT ME!
     *
     * @param p DOCUMENT ME!
     */
    public void obtainShape(Point2D p) {
    	Point2D[] points=getPoints();
        GeneralPathX elShape = new GeneralPathX(GeneralPathX.WIND_EVEN_ODD,
                points.length);

        if (points.length > 0) {
            elShape.moveTo(((Point2D) points[0]).getX(),
                ((Point2D) points[0]).getY());
        }

        if (points.length > 0) {
            elShape.lineTo(p.getX(), ((Point2D) points[0]).getY());
            elShape.lineTo(p.getX(), p.getY());
            elShape.lineTo(((Point2D) points[0]).getX(), p.getY());
            elShape.lineTo(((Point2D) points[0]).getX(),
                ((Point2D) points[0]).getY());
        }

        setGPX(elShape);
    }

    /*  public int addPoint(Point2D point) {
       if (points.size()==0){
               points.add(point);
       }else{
               points.add(new Point2D.Double(point.getX(),((Point2D) points.get(0)).getY()));
           points.add(point);
           points.add(new Point2D.Double(((Point2D) points.get(0)).getX(),point.getY()));
       }
       return points.size();
       }*/
}
