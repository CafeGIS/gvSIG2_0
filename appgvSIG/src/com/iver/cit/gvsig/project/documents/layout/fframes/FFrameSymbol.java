/*
 * Created on 09-jul-2004
 *
 */
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
package com.iver.cit.gvsig.project.documents.layout.fframes;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.gvsig.compat.print.PrintAttributes;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.mapcontext.rendering.symbols.SymbolDrawingException;
import org.gvsig.fmap.mapcontext.rendering.symbols.SymbologyFactory;

import com.iver.andami.PluginServices;


/**
 * FFrame para introducir una Símbolo en el Layout.
 *
 * @author Vicente Caballero Navarro
 */
public class FFrameSymbol extends FFrameGraphics {

    private PrintAttributes properties;

	/**
     * Crea un nuevo FFrameSymbol.
     */
    public FFrameSymbol() {
    	super();
    }

    /**
     * Método que dibuja sobre el graphics que se le pasa como parámetro, según
     * la transformada afin que se debe de aplicar y el rectángulo que se debe
     * de dibujar.
     *
     * @param g Graphics
     * @param at Transformada afin.
     * @param rv rectángulo sobre el que hacer un clip.
     * @param imgBase Imagen para acelerar el dibujado.
     */
    public void draw(Graphics2D g, AffineTransform at, Rectangle2D rv,
        BufferedImage imgBase) {
        Rectangle2D.Double re = getBoundingBox(at);
        g.rotate(Math.toRadians(getRotation()), re.x + (re.width / 2),
            re.y + (re.height / 2));

        if (intersects(rv, re)) {
            AffineTransform mT2 = new AffineTransform();
            mT2.setToIdentity();

            Rectangle rec = new Rectangle((int) re.x, (int) re.y,
                    (int) (re.width), (int) (re.height));

            try {
				getFSymbol().drawInsideRectangle(g, mT2, rec, properties);
			} catch (SymbolDrawingException e) {
				if (e.getType() == SymbolDrawingException.UNSUPPORTED_SET_OF_SETTINGS) {
					try {
						SymbologyFactory.getWarningSymbol(
								SymbolDrawingException.STR_UNSUPPORTED_SET_OF_SETTINGS,
								getFSymbol().getDescription(),
								SymbolDrawingException.UNSUPPORTED_SET_OF_SETTINGS).drawInsideRectangle(g, null, rec,null);
					} catch (SymbolDrawingException e1) {
						// IMPOSSIBLE TO REACH THIS
					}
				} else {
					// should be unreachable code
					throw new Error(PluginServices.getText(this, "symbol_shapetype_mismatch"));
				}
			}
        }

        g.rotate(Math.toRadians(-getRotation()), re.x + (re.width / 2),
            re.y + (re.height / 2));
    }

    /**
     * @see com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame#getNameFFrame()
     */
    public String getNameFFrame() {
        return PluginServices.getText(this, "simbolo")+num;
    }

    /**
     * @see com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame#print(java.awt.Graphics2D,
     *      java.awt.geom.AffineTransform)
     */
    public void print(Graphics2D g, AffineTransform at, Geometry geom,
			PrintAttributes printingProperties) {
        this.properties=printingProperties;
    	draw(g, at, null, null);
    	this.properties=null;
    }

    public void initialize() {

	}
    public void setBoundBox(Rectangle2D r) {
        m_BoundBox.setRect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }
}
