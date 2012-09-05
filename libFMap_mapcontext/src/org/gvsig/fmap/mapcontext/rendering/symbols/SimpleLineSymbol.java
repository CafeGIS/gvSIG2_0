/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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

/* CVS MESSAGES:
*
* $Id: SimpleLineSymbol.java 21071 2008-06-02 10:55:35Z vcaballero $
* $Log$
* Revision 1.16  2007-09-21 12:25:32  jaume
* cancellation support extended down to the IGeometry and ISymbol level
*
* Revision 1.15  2007/09/19 16:22:04  jaume
* removed unnecessary imports
*
* Revision 1.14  2007/09/17 09:33:47  jaume
* some multishapedsymbol bugs fixed
*
* Revision 1.13  2007/08/09 07:38:32  jvidal
* javadoc
*
* Revision 1.12  2007/07/23 06:52:55  jaume
* Added support for arrow line decorator (start commiting)
*
* Revision 1.11  2007/07/18 06:54:35  jaume
* continuing with cartographic support
*
* Revision 1.10  2007/06/29 13:07:01  jaume
* +PictureLineSymbol
*
* Revision 1.9  2007/06/07 06:50:40  jaume
* *** empty log message ***
*
* Revision 1.8  2007/05/08 08:47:40  jaume
* *** empty log message ***
*
* Revision 1.7  2007/03/26 15:02:49  jaume
* Refactored IPrintable
*
* Revision 1.6  2007/03/20 16:02:24  jaume
* rename method
*
* Revision 1.5  2007/03/09 11:20:56  jaume
* Advanced symbology (start committing)
*
* Revision 1.3.2.5  2007/02/21 07:34:09  jaume
* labeling starts working
*
* Revision 1.3.2.4  2007/02/15 16:23:44  jaume
* *** empty log message ***
*
* Revision 1.3.2.3  2007/02/13 16:19:19  jaume
* graduated symbol legends (start commiting)
*
* Revision 1.3.2.2  2007/02/12 15:15:20  jaume
* refactored interval legend and added graduated symbol legend
*
* Revision 1.3.2.1  2007/02/09 07:47:05  jaume
* Isymbol moved
*
* Revision 1.3  2007/01/25 16:25:23  jaume
* *** empty log message ***
*
* Revision 1.2  2007/01/24 17:58:22  jaume
* new features and architecture error fixes
*
* Revision 1.1  2007/01/16 11:50:44  jaume
* *** empty log message ***
*
*
*/
package org.gvsig.fmap.mapcontext.rendering.symbols;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

import org.gvsig.compat.print.PrintAttributes;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.util.UtilFunctions;
import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.rendering.symbols.styles.ArrowDecoratorStyle;
import org.gvsig.fmap.mapcontext.rendering.symbols.styles.ILineStyle;
import org.gvsig.fmap.mapcontext.rendering.symbols.styles.Line2DOffset;
import org.gvsig.tools.task.Cancellable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.utiles.StringUtilities;
import com.iver.utiles.XMLEntity;
import com.iver.utiles.XMLException;

/**
 * SimpleLineSymbol is the most basic symbol for the representation of line objects.
 * Allows to define the width of the line, the color and the drawn pattern.
 *
 * @author   jaume dominguez faus - jaume.dominguez@iver.es
 */
public class SimpleLineSymbol extends AbstractLineSymbol {
	SimpleLineSymbol symbolForSelection;
	private double width;
	private static final GeometryManager geomManager = GeometryLocator.getGeometryManager();
	private static final Logger logger = LoggerFactory.getLogger(SimpleLineSymbol.class);


	public ISymbol getSymbolForSelection() {
		if (symbolForSelection == null) {
			XMLEntity xml=null;
			try {
				xml = getXMLEntity();
			} catch (XMLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			xml.putProperty("color", StringUtilities.color2String(MapContext.getSelectionColor()));
			symbolForSelection = (SimpleLineSymbol) SymbologyFactory.
					createSymbolFromXML(xml, getDescription()+" version for selection");
		}
		return symbolForSelection;
	}

	public void draw(Graphics2D g, AffineTransform affineTransform, Geometry geom, Cancellable cancel) {
		g.setStroke(getLineStyle().getStroke());

		if (getLineStyle().getOffset() != 0) {
			double offset = getLineStyle().getOffset();
			try {
				geom = geomManager.createSurface(Line2DOffset.offsetLine(geom, offset), SUBTYPES.GEOM2D);
			} catch (CreateGeometryException e) {
				logger.error("Creating a Surface", e);
				e.printStackTrace();
			}
		}
		g.setColor(getColor());
		g.draw(geom);

		ArrowDecoratorStyle arrowDecorator = getLineStyle().getArrowDecorator();

		if (arrowDecorator != null) {
			try {
				arrowDecorator.draw(g, affineTransform, geom);
			} catch (CreateGeometryException e) {
				logger.error("Error creating a geometry");
			}
		}
	}

	public int getOnePointRgb() {
		return getColor().getRGB();
	}

	public XMLEntity getXMLEntity() throws XMLException {
		XMLEntity xml = new XMLEntity();
		xml.putProperty("className", getClassName());
		xml.putProperty("isShapeVisible", isShapeVisible());
		xml.putProperty("desc", getDescription());
		xml.putProperty("unit", getUnit());

		// reference system
		xml.putProperty("referenceSystem", getReferenceSystem());

		Color c = getColor();
		if (c!= null) {
			xml.putProperty("color", StringUtilities.color2String(getColor()));
		}

//		setLineWidth(getLineWidth()); // not a joke
		xml.addChild(getLineStyle().getXMLEntity());
		return xml;
	}

	public void drawInsideRectangle(Graphics2D g,
			AffineTransform scaleInstance, Rectangle r, PrintAttributes properties) throws SymbolDrawingException {
		g.setColor(getColor());
		g.setStroke(getLineStyle().getStroke());
		super.drawInsideRectangle(g, scaleInstance, r, properties);
	}

	public String getClassName() {
		return getClass().getName();
	}

	public void setXMLEntity(XMLEntity xml) {
		setIsShapeVisible(xml.getBooleanProperty("isShapeVisible"));
		setDescription(xml.getStringProperty("desc"));
		if (xml.contains("color")) {
			setLineColor(StringUtilities.
				string2Color(xml.getStringProperty("color")));
		}
		setLineStyle((ILineStyle) SymbologyFactory.createStyleFromXML(xml.getChild(0), null));
		setReferenceSystem(xml.getIntProperty("referenceSystem"));
		setUnit(xml.getIntProperty("unit"));
		width = getLineStyle().getLineWidth(); // not a joke
	}

	public void setLineWidth(double width) {
		this.width = width;
		getLineStyle().setLineWidth((float) width);
	}

	public double getLineWidth() {
		return width;
	}

}
