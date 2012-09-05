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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

import org.gvsig.compat.print.PrintAttributes;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.mapcontext.rendering.symbols.ILineSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.SimpleLineSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.SymbologyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




/**
 * DOCUMENT ME!
 *
 * @author Vicente Caballero Navarro
 */
public class CircleAdapter extends GeometryAdapter {
	private static final GeometryManager geomManager = GeometryLocator.getGeometryManager();
	private static final Logger logger = LoggerFactory.getLogger(CircleAdapter.class);
	private Point2D pointPosition = new Point2D.Double();
    private AffineTransform identity = new AffineTransform();
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Geometry getGeometry(AffineTransform at) {
    	GeneralPathX polyLine;
		try {
			polyLine = new GeneralPathX(geomManager.createSurface(getGPX(), SUBTYPES.GEOM2D));
			polyLine.transform(at);
	    	return geomManager.createSurface(polyLine, SUBTYPES.GEOM2D);
		} catch (CreateGeometryException e) {
			logger.error("Error creating a surface", e);
		}
    	return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param at DOCUMENT ME!
     * @param symbol DOCUMENT ME!
     */
    public void draw(Graphics2D g, AffineTransform at, ISymbol symbol) {
    	Geometry shapeAux=getGeometry(at);
    	symbol.draw(g,at,shapeAux, null);
        // FGraphicUtilities.DrawShape(g, at, shapeAux, symbol);
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param at DOCUMENT ME!
     * @param andLastPoint DOCUMENT ME!
     */
    public void paint(Graphics2D g, AffineTransform at, boolean andLastPoint) {
        if (getPoints().length > 0) {
            if (andLastPoint) {
                obtainShape(pointPosition);
            }

            Geometry shapeAux=getGeometry(at);

            ILineSymbol symbol = SymbologyFactory.createDefaultLineSymbol();
            SimpleLineSymbol redOutline = new SimpleLineSymbol();
            redOutline.setLineColor(Color.red);
            symbol.draw(g, identity, shapeAux, null);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param p DOCUMENT ME!
     */
    public void pointPosition(Point2D p) {
        pointPosition = p;
    }

    /**
     * DOCUMENT ME!
     *
     * @param p DOCUMENT ME!
     */
    public void obtainShape(Point2D p) {
        Point2D[] points = getPoints();

        double x = (points[0]).getX(); //-(pointPosition.getX()-((Point2D)points.get(0)).getX());
        double y = (points[0]).getY(); //-(pointPosition.getY()-((Point2D)points.get(0)).getY());
        double r = p.distance(points[0]);
        Ellipse2D ellipse = new Ellipse2D.Double(x - r, y - r, 2. * r, 2. * r);

        setGPX(new GeneralPathX(ellipse));
    }

	public void print(Graphics2D g, AffineTransform at, ISymbol symbol,
			PrintAttributes properties) {
		Geometry shapeAux=getGeometry(at);
    	symbol.print(g,at,shapeAux, properties);
	}

}
