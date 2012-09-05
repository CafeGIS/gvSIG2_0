/*
 * Created on 14-dic-2005
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
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
package org.gvsig.fmap.mapcontext.rendering.legend;

import java.awt.Graphics2D;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.operation.CreateLabels;
import org.gvsig.fmap.geom.operation.CreateLabelsOperationContext;
import org.gvsig.fmap.geom.operation.GeometryOperationException;
import org.gvsig.fmap.geom.operation.GeometryOperationNotSupportedException;
import org.gvsig.fmap.geom.primitive.Point;
import org.gvsig.fmap.geom.util.UtilFunctions;
import org.gvsig.fmap.geom.utils.FLabel;
import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author fjp
 *
 */
public class FGraphicLabel extends FGraphic {
	private static final GeometryManager geomManager = GeometryLocator.getGeometryManager();
	private static final Logger logger = LoggerFactory.getLogger(GeometryManager.class);
	private FLabel theLabel;
    /**
     * Le pasas la geometría que quieres etiquetar y el texto
     * con el que quieres etiquetarla.
     * @param geom
     * @param idSymbol
     */
    public FGraphicLabel(Geometry geom, int idSymbol, String theText) {
        super(geom, idSymbol);
        // TODO: Lo correcto debería ser hacer que FLabel
        // siga el patrón COMPOSITE por ejemplo para que los
        // multipoint se etiqueten bien, no solo el primer punto.
        CreateLabelsOperationContext cloc=new CreateLabelsOperationContext();
        cloc.setPosition(0);
        cloc.setDublicates(true);
        FLabel[] labels=null;
		try {
			labels = (FLabel[])geom.invokeOperation(CreateLabels.CODE,cloc);
		} catch (GeometryOperationNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GeometryOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//        FLabel[] labels = geom.createLabels(0, true);
        theLabel = labels[0];
        theLabel.setString(theText);
    }
    /**
     * @return Returns the theLabel.
     */
    public FLabel getLabel() {
        return theLabel;
    }
    /* (non-Javadoc)
     * @see com.iver.cit.gvsig.fmap.rendering.FGraphic#draw(java.awt.Graphics2D, com.iver.cit.gvsig.fmap.ViewPort, com.iver.cit.gvsig.fmap.core.v02.FSymbol)
     */
    public void draw(Graphics2D g, ViewPort viewPort, ISymbol theSymbol) {
        super.draw(g, viewPort, theSymbol);
        Point theShape;
		try {
			theShape = geomManager.createPoint(theLabel.getOrig().getX(), theLabel.getOrig().getY(), SUBTYPES.GEOM2D);
			theLabel.draw(g, viewPort.getAffineTransform(), theShape, theSymbol);
		} catch (CreateGeometryException e) {
			logger.error("Error creating a point", e);
			e.printStackTrace();
		}        
//        FGraphicUtilities.DrawLabel(g, viewPort.getAffineTransform(),
//                theShape, theSymbol, theLabel);

    }
    /**
     * @param theLabel The theLabel to set.
     */
    public void setLabel(FLabel theLabel) {
        this.theLabel = theLabel;
    }

}
