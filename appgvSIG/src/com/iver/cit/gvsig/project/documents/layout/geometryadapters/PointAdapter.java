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
import java.awt.geom.Rectangle2D;

import org.gvsig.compat.print.PrintAttributes;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




/**
 * DOCUMENT ME!
 *
 * @author Vicente Caballero Navarro
 */
public class PointAdapter extends PolyLineAdapter {
	private static final GeometryManager geomManager = GeometryLocator.getGeometryManager();
	private static final Logger logger = LoggerFactory.getLogger(PointAdapter.class);

	public void paint(Graphics2D g, AffineTransform at, boolean andLastPoint) {
	}
	public void obtainShape(Point2D p) {
        GeneralPathX elShape = new GeneralPathX(GeneralPathX.WIND_EVEN_ODD, 1);
        elShape.moveTo(p.getX(), p.getY());
        setGPX(elShape);
    }

	/**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param at DOCUMENT ME!
     * @param symbol DOCUMENT ME!
     */
    public void draw(Graphics2D g, AffineTransform at, ISymbol symbol) {
        symbol.draw(g,at,getGeometry(at), null);
    }
    public void print(Graphics2D g, AffineTransform at, ISymbol symbol,
			PrintAttributes properties) {
       	symbol.print(g,at,getGeometry(at), properties);
    }
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Geometry getGeometry(AffineTransform at) {
    	GeneralPathX point;
		try {
			point = new GeneralPathX(geomManager.createPoint(getGPX().getCurrentPoint().getX(),getGPX().getCurrentPoint().getX(), SUBTYPES.GEOM2D));
			point.transform(at);

			return geomManager.createPoint(point.getCurrentPoint().getX(),point.getCurrentPoint().getY(), SUBTYPES.GEOM2D);
		} catch (CreateGeometryException e) {
			logger.error("Error creating a curve", e);
		}
    	return null;
    }

    public Rectangle2D getBounds2D(){
        Rectangle2D r=getGeometry(new AffineTransform()).getBounds2D();
        double w=r.getWidth();
        double h=r.getHeight();
        double x=r.getX();
        double y=r.getY();
        boolean modified=false;
        if (r.getWidth()<0.5) {
         modified=true;
         w=1;
         x=x-0.25;
        }
        if(r.getHeight()<0.5) {
         modified=true;
         h=1;
//         y=y-0.5;
        }
        if (modified) {
			return new Rectangle2D.Double(x,y,w,h);
		}
        return r;
   }
}
