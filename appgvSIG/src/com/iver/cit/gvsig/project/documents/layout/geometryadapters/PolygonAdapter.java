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

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.geom.util.UtilFunctions;
import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.cit.gvsig.gui.styling.SymbolSelector;



/**
 * DOCUMENT ME!
 *
 * @author Vicente Caballero Navarro
 */
public class PolygonAdapter extends PolyLineAdapter {
	private static final GeometryManager geomManager = GeometryLocator.getGeometryManager();
	private static final Logger logger = LoggerFactory.getLogger(PolygonAdapter.class);
	
	/**
     * DOCUMENT ME!
     *
     * @param p DOCUMENT ME!
     */
    public void obtainShape(Point2D p) {
        Point2D[] points = getPoints();
        GeneralPathX elShape = new GeneralPathX(GeneralPathX.WIND_EVEN_ODD,
                points.length);

        if (points.length > 0) {
            elShape.moveTo(((Point2D) points[0]).getX(),
                ((Point2D) points[0]).getY());
        }

        for (int i = 1; i < points.length; i++) {
            elShape.lineTo(((Point2D) points[i]).getX(),
                ((Point2D) points[i]).getY());
        }

        if (points.length > 0) {
            elShape.lineTo(p.getX(), p.getY());
            elShape.lineTo(((Point2D) points[0]).getX(),
                ((Point2D) points[0]).getY());
        }

        setGPX(elShape);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Geometry getGeometry() {
        try {
			return geomManager.createSurface(getGPX(), SUBTYPES.GEOM2D);
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
        GeneralPathX rectangle = new GeneralPathX(getGeometry());
        rectangle.transform(at);

        Geometry shapeAux;
		try {
			shapeAux = geomManager.createSurface(rectangle, SUBTYPES.GEOM2D);
		       symbol.draw(g,at,shapeAux, null);
		} catch (CreateGeometryException e) {
			logger.error("Error creating a surface", e);
		} 
    }
}
