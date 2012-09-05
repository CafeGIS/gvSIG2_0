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
* $Id: SimpleFillSymbol.java 21071 2008-06-02 10:55:35Z vcaballero $
* $Log$
* Revision 1.14  2007-09-21 12:25:32  jaume
* cancellation support extended down to the IGeometry and ISymbol level
*
* Revision 1.13  2007/09/19 16:22:04  jaume
* removed unnecessary imports
*
* Revision 1.12  2007/09/18 14:50:31  caballero
* Leyendas sobre el Layout
*
* Revision 1.11  2007/07/23 06:52:25  jaume
* default selection color refactored, moved to MapContext
*
* Revision 1.10  2007/03/26 15:02:49  jaume
* Refactored IPrintable
*
* Revision 1.9  2007/03/21 11:37:00  jaume
* *** empty log message ***
*
* Revision 1.8  2007/03/21 11:02:17  jaume
* *** empty log message ***
*
* Revision 1.7  2007/03/20 17:00:50  jaume
* *** empty log message ***
*
* Revision 1.6  2007/03/20 16:02:09  jaume
* *** empty log message ***
*
* Revision 1.5  2007/03/09 11:20:57  jaume
* Advanced symbology (start committing)
*
* Revision 1.3.2.5  2007/02/21 07:34:09  jaume
* labeling starts working
*
* Revision 1.3.2.4  2007/02/16 10:54:12  jaume
* multilayer splitted to multilayerline, multilayermarker,and  multilayerfill
*
* Revision 1.3.2.3  2007/02/15 16:23:44  jaume
* *** empty log message ***
*
* Revision 1.3.2.2  2007/02/12 15:15:20  jaume
* refactored interval legend and added graduated symbol legend
*
* Revision 1.3.2.1  2007/02/09 07:47:05  jaume
* Isymbol moved
*
* Revision 1.3  2007/01/24 17:58:22  jaume
* new features and architecture error fixes
*
* Revision 1.2  2007/01/10 16:39:41  jaume
* ISymbol now belongs to com.iver.cit.gvsig.fmap.core.symbols package
*
* Revision 1.1  2007/01/10 16:31:36  jaume
* *** empty log message ***
*
* Revision 1.7  2006/11/14 11:10:27  jaume
* *** empty log message ***
*
* Revision 1.6  2006/11/09 18:39:05  jaume
* *** empty log message ***
*
* Revision 1.5  2006/11/09 10:22:50  jaume
* *** empty log message ***
*
* Revision 1.4  2006/11/08 13:05:51  jaume
* *** empty log message ***
*
* Revision 1.3  2006/11/08 10:56:47  jaume
* *** empty log message ***
*
* Revision 1.2  2006/11/06 16:06:52  jaume
* *** empty log message ***
*
* Revision 1.1  2006/10/30 19:30:35  jaume
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
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.geom.util.UtilFunctions;
import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.tools.task.Cancellable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.utiles.StringUtilities;
import com.iver.utiles.XMLEntity;
import com.iver.utiles.XMLException;

/**
 * Basic fill symbol. It will allow to paint a shape with its filling color (and transparency) and the outline.
 * @author   jaume dominguez faus - jaume.dominguez@iver.es
 */
public class SimpleFillSymbol extends AbstractFillSymbol {
	private SimpleFillSymbol symbolForSelection;
	private static final GeometryManager geomManager = GeometryLocator.getGeometryManager();
	private static final Logger logger = LoggerFactory.getLogger(SimpleFillSymbol.class);

	public ISymbol getSymbolForSelection() {
		if (symbolForSelection == null) {
			XMLEntity xml=null;
			try {
				xml = getXMLEntity();
			} catch (XMLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			xml.putProperty("hasFill", true);
			xml.putProperty("color", StringUtilities.color2String(MapContext.getSelectionColor()));
			symbolForSelection = (SimpleFillSymbol) SymbologyFactory.
					createSymbolFromXML(xml, getDescription()+" version for selection");
		}
		return symbolForSelection;
	}

	public void draw(Graphics2D g, AffineTransform affineTransform, Geometry geom, Cancellable cancel) {
		Color c = getFillColor();
		if (c!=null && hasFill()) {
			g.setColor(c);
			g.fill(geom);
		}
		if (getOutline() != null && hasOutline()) {
			getOutline().draw(g, affineTransform, geom, cancel);
		}
	}

	public XMLEntity getXMLEntity() throws XMLException {
		XMLEntity xml = new XMLEntity();

		// the class name
		xml.putProperty("className", getClassName());

		// color
		Color c = getFillColor();
		if (c!=null) {
			xml.putProperty("color", StringUtilities.color2String(getFillColor()));
		}

		//has Fill
		xml.putProperty("hasFill", hasFill());
		// description
		xml.putProperty("desc", getDescription());

		// is shape visible
		xml.putProperty("isShapeVisible", isShapeVisible());

		xml.putProperty("referenceSystem", getReferenceSystem());
		xml.putProperty("unit", getUnit());

		// outline
		if (getOutline() != null) {
			xml.addChild(getOutline().getXMLEntity());
		}

		//has Outline
		xml.putProperty("hasOutline", hasOutline());

		return xml;
	}

	public int getSymbolType() {
		return Geometry.TYPES.SURFACE;
	}

	public void drawInsideRectangle(Graphics2D g,
			AffineTransform scaleInstance, Rectangle r, PrintAttributes properties) throws SymbolDrawingException {
		Rectangle rect = new Rectangle(r.x, r.y, r.width, r.height);
		rect.setFrame(((int) rect.getMinX())+1, ((int) rect.getMinY())+1, ((int) rect.getWidth())-2, ((int) rect.getHeight())-2);
		Geometry geom;
		try {
			geom = geomManager.createSurface(new GeneralPathX(rect), SUBTYPES.GEOM2D);
		} catch (CreateGeometryException e) {
			logger.error("Creating a surface", e);
			throw new SymbolDrawingException(getSymbolType());
		}

		Color c = getFillColor();
		if (c != null && hasFill()) {
			g.setColor(c);
			g.fillRect(rect.x, rect.y, rect.width, rect.height);
		}

		if (getOutline() != null && hasOutline()) {
			if (properties==null)
				getOutline().draw(g, scaleInstance, geom, null);
			else
				print(g, new AffineTransform(), geom, properties);
		}
	}

	public String getClassName() {
		return getClass().getName();
	}

	public void setXMLEntity(XMLEntity xml) {
		// color
		if (xml.contains("color")) {
			setFillColor(StringUtilities.string2Color(xml.getStringProperty("color")));
		}

		//has Fill
		if(xml.contains("hasFill")) {
			setHasFill(xml.getBooleanProperty("hasFill"));
		}

		// description
		setDescription(xml.getStringProperty("desc"));

		// is shape visible
		setIsShapeVisible(xml.getBooleanProperty("isShapeVisible"));


		// outline
		if (xml.getChildrenCount() > 0) {
			setOutline((ILineSymbol) SymbologyFactory.createSymbolFromXML(xml.getChild(0), null));
		}

		if (xml.contains("unit")) { // remove this line when done
		// measure unit (for outline)
		setUnit(xml.getIntProperty("unit"));

		// reference system (for outline)
		setReferenceSystem(xml.getIntProperty("referenceSystem"));
		}

		//has Outline
		if(xml.contains("hasOutline")) {
			setHasOutline(xml.getBooleanProperty("hasOutline"));
		}
	}

	public void print(Graphics2D g, AffineTransform at, Geometry geom, PrintAttributes properties) {
		Color c = getFillColor();
		if (c!=null && hasFill()) {
			g.setColor(c);
			g.fill(geom);
		}
		if (getOutline() != null && hasOutline()) {
			getOutline().print(g, at, geom, properties);
		}
	}
}
