/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
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

/* CVS MESSAGES:
 *
 * $Id: SimpleLineStyle.java 21072 2008-06-02 10:56:01Z vcaballero $
 * $Log$
 * Revision 1.14  2007-09-17 15:26:24  jaume
 * *** empty log message ***
 *
 * Revision 1.13  2007/08/14 11:17:31  jvidal
 * javadoc updated
 *
 * Revision 1.12  2007/07/30 06:59:50  jaume
 * finished (maybe) LineFillSymbol
 *
 * Revision 1.11  2007/07/26 12:28:48  jaume
 * maybe finished ArrowMarkerSymbol and ArrowDecoratorStyle
 *
 * Revision 1.10  2007/07/23 06:53:56  jaume
 * Added support for arrow line decorator (start commiting)
 *
 * Revision 1.9  2007/07/18 06:54:34  jaume
 * continuing with cartographic support
 *
 * Revision 1.8  2007/07/03 10:58:29  jaume
 * first refactor on CartographicSupport
 *
 * Revision 1.7  2007/05/22 10:05:30  jaume
 * *** empty log message ***
 *
 * Revision 1.6  2007/05/17 09:32:06  jaume
 * *** empty log message ***
 *
 * Revision 1.5  2007/05/09 16:07:26  jaume
 * *** empty log message ***
 *
 * Revision 1.4  2007/05/08 08:47:39  jaume
 * *** empty log message ***
 *
 * Revision 1.3  2007/04/04 15:41:05  jaume
 * *** empty log message ***
 *
 * Revision 1.2  2007/03/09 11:20:56  jaume
 * Advanced symbology (start committing)
 *
 * Revision 1.1.2.4  2007/02/21 07:34:09  jaume
 * labeling starts working
 *
 * Revision 1.1.2.3  2007/02/15 16:23:44  jaume
 * *** empty log message ***
 *
 * Revision 1.1.2.2  2007/02/12 15:15:20  jaume
 * refactored interval legend and added graduated symbol legend
 *
 * Revision 1.1.2.1  2007/02/09 07:47:04  jaume
 * Isymbol moved
 *
 *
 */
package org.gvsig.fmap.mapcontext.rendering.symbols.styles;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.mapcontext.rendering.symbols.CartographicSupport;
import org.gvsig.fmap.mapcontext.rendering.symbols.CartographicSupportToolkit;
import org.gvsig.fmap.mapcontext.rendering.symbols.ILineSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.SymbologyFactory;

import com.iver.utiles.XMLEntity;
import com.iver.utiles.XMLException;
import org.gvsig.compat.CompatLocator;

/**
 * @see http://www.oreilly.com/catalog/java2d/chapter/ch04.html
 * @author  jaume dominguez faus - jaume.dominguez@iver.es
 */
public class SimpleLineStyle extends AbstractStyle implements ILineStyle, CartographicSupport {
	private final static Color PREVIEW_COLOR_1= new Color(150, 255, 200); //light blue
	private final static Color PREVIEW_COLOR_2 = new Color(255, 200, 100); //orange
	private final static int COLOR1_STROKE = 1;
	private final static int COLOR2_STROKE = 3;
	private float[] dashArray, tempDashArray;
	private float dashPhase;
	private int endCap = BasicStroke.CAP_BUTT;
	private int lineJoin;
	private float miterlimit;
	private float lineWidth;
	private int measureUnit;// for the offset distance
	private double offset = 0, csOffset = 0;
	private int referenceSystem;
	private ArrowDecoratorStyle arrowDecorator;
	/**
	 * Constructor method
	 *
	 */
	public SimpleLineStyle() {
		BasicStroke dummy = new BasicStroke();
		dashArray = dummy.getDashArray();
		tempDashArray = dummy.getDashArray();
		dashPhase = dummy.getDashPhase();
		endCap = BasicStroke.CAP_BUTT;
		lineJoin = BasicStroke.JOIN_BEVEL;
		miterlimit = dummy.getMiterLimit();
	}
	/**
	 * Constructor method
	 *
	 * @param width
	 * @param cap
	 * @param join
	 * @param miterlimit
	 * @param dash
	 * @param dash_phase
	 */
	public SimpleLineStyle(float width, int cap, int join, float miterlimit, float[] dash, float dash_phase) {
		this.lineWidth = width;
		this.endCap = cap;
		this.lineJoin = join;
		this.miterlimit = miterlimit;
		this.dashArray = dash;
		this.tempDashArray = dash;
		this.dashPhase = dash_phase;
	}

	public void drawInsideRectangle(Graphics2D g, Rectangle r) {
		Stroke oldStroke = g.getStroke();
		int h = (int) (r.getHeight() / 2);
		int margins = 2;

		BasicStroke stroke;
		stroke = new BasicStroke(COLOR1_STROKE, endCap, lineJoin, miterlimit, tempDashArray, dashPhase);
		g.setStroke(stroke);
		g.setColor(PREVIEW_COLOR_1);
		g.drawLine(margins, h, r.width-margins-margins, h);

		stroke = new BasicStroke(COLOR2_STROKE, endCap, lineJoin, miterlimit, tempDashArray, dashPhase);
		g.setStroke(stroke);
		g.setColor(PREVIEW_COLOR_2);
		g.drawLine(margins, h, r.width-margins-margins, h);
		g.setStroke(oldStroke);
	}

	public String getClassName() {
		return getClass().getName();
	}

	public XMLEntity getXMLEntity() throws XMLException {
		XMLEntity xml = new XMLEntity();
		xml.putProperty("className", getClassName());
		xml.putProperty("desc", getDescription());

		if (dashArray != null) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < dashArray.length; i++) {
				sb.append(dashArray[i]);
				if (i< dashArray.length)
					sb.append(",");
			}
			xml.putProperty("dashArray", sb.toString());

		}
		xml.putProperty("lineWidth", lineWidth);
		xml.putProperty("dashPhase", dashPhase);
		xml.putProperty("endCap", endCap);
		xml.putProperty("lineJoin", lineJoin);
		xml.putProperty("miterLimit", miterlimit);
		xml.putProperty("offset", offset);
		xml.putProperty("unit", measureUnit);
		if (arrowDecorator != null) {
			XMLEntity decXML = arrowDecorator.getXMLEntity();
			decXML.putProperty("id", "arrowDecorator");
			xml.addChild(decXML);
		}
		return xml;
	}

	public void setXMLEntity(XMLEntity xml) {

		setDescription(xml.getStringProperty("desc"));
		if (xml.contains("dashArray")) {
			String[] dashProp = CompatLocator.getStringUtils().split(xml.getStringProperty("dashArray"),",");
			dashArray = new float[dashProp.length];
			for (int i = 0; i < dashProp.length; i++) {
				dashArray[i] = Float.parseFloat(dashProp[i]);
			}
			tempDashArray = dashArray;
		}
		lineWidth = xml.getFloatProperty("lineWidth");
		dashPhase = xml.getFloatProperty("dashPhase");
		endCap = xml.getIntProperty("endCap");
		lineJoin = xml.getIntProperty("lineJoin");
		miterlimit = xml.getFloatProperty("miterLimit");
		offset = xml.getDoubleProperty("offset");
		csOffset = offset;
		measureUnit = xml.getIntProperty("unit");
		XMLEntity decXML = xml.firstChild("id", "arrowDecorator");
		if (decXML != null) {
			// it contains an arrow decorator
			arrowDecorator = (ArrowDecoratorStyle) SymbologyFactory.createStyleFromXML(decXML, "arrowDecorator");
		}

	}

	public Stroke getStroke() {
		return new BasicStroke((float) lineWidth, endCap, lineJoin, miterlimit, tempDashArray, dashPhase);
	}

	public float getLineWidth() {
		return lineWidth;
	}


	public void setLineWidth(float width) {
		if (lineWidth != 0) {
			double scale = width / this.lineWidth;
			if (dashArray != null) {
				for (int i = 0; scale > 0 && i < dashArray.length; i++) {
					tempDashArray[i] = (float) (dashArray[i] * scale);
				}
			}
			this.csOffset = offset * scale;
		}
		this.lineWidth = width;

	}

	public boolean isSuitableFor(ISymbol symbol) {
		return symbol instanceof ILineSymbol;
	}

	public void setStroke(Stroke stroke) {
		if (stroke != null) {
			BasicStroke dummy = (BasicStroke) stroke;
			dashArray = dummy.getDashArray();
			if (dashArray != null) {
				tempDashArray = new float[dashArray.length];
				for (int i = 0; i < dashArray.length; i++) {
					tempDashArray[i] = dashArray[i];
				}
			} else
				tempDashArray = null;


			dashPhase = dummy.getDashPhase();
			endCap = dummy.getEndCap();
			lineJoin = dummy.getLineJoin();
			miterlimit = dummy.getMiterLimit();
			lineWidth = dummy.getLineWidth();
			offset = getOffset();
			csOffset = offset;
		}
	}

	public void drawOutline(Graphics2D g, Rectangle r) {
		drawInsideRectangle(g, r);
	}

	public double getOffset() {
		return csOffset;
	}

	public void setOffset(double offset) {
		this.offset = offset;
		this.csOffset = offset;
	}

	public ArrowDecoratorStyle getArrowDecorator() {
		return arrowDecorator;

	}

	public void setArrowDecorator(ArrowDecoratorStyle arrowDecoratorStyle) {
		this.arrowDecorator = arrowDecoratorStyle;
	}

	public void setUnit(int unitIndex) {
		this.measureUnit = unitIndex;
	}

	public int getUnit() {
		return measureUnit;
	}

	public int getReferenceSystem() {
		return referenceSystem;
	}

	public void setReferenceSystem(int referenceSystem) {
		this.referenceSystem = referenceSystem;
	}

	public double toCartographicSize(ViewPort viewPort, double dpi, Geometry geom) {
		double oldWidth = getLineWidth();
		setCartographicSize(getCartographicSize(viewPort, dpi, geom), geom);
		return oldWidth;
	}

	public void setCartographicSize(double cartographicSize, Geometry geom) {
		setLineWidth((float) cartographicSize);

	}

	public double getCartographicSize(ViewPort viewPort, double dpi, Geometry geom) {
		return CartographicSupportToolkit.
		getCartographicLength(this,
				getOffset(),
				viewPort,
				dpi);

	}
}