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
* $Id: SimpleMarkerSymbol.java 21071 2008-06-02 10:55:35Z vcaballero $
* $Log$
* Revision 1.19  2007-09-21 12:25:32  jaume
* cancellation support extended down to the IGeometry and ISymbol level
*
* Revision 1.18  2007/09/19 16:22:04  jaume
* removed unnecessary imports
*
* Revision 1.17  2007/08/09 07:32:20  jvidal
* javadoc
*
* Revision 1.16  2007/07/23 06:52:25  jaume
* default selection color refactored, moved to MapContext
*
* Revision 1.15  2007/07/18 06:54:35  jaume
* continuing with cartographic support
*
* Revision 1.14  2007/07/03 10:58:29  jaume
* first refactor on CartographicSupport
*
* Revision 1.13  2007/06/27 06:31:17  jaume
* *** empty log message ***
*
* Revision 1.12  2007/06/07 06:50:40  jaume
* *** empty log message ***
*
* Revision 1.11  2007/05/29 15:46:37  jaume
* *** empty log message ***
*
* Revision 1.10  2007/05/28 15:36:42  jaume
* *** empty log message ***
*
* Revision 1.9  2007/05/09 16:07:26  jaume
* *** empty log message ***
*
* Revision 1.8  2007/05/08 08:47:40  jaume
* *** empty log message ***
*
* Revision 1.7  2007/03/27 07:11:32  jaume
* *** empty log message ***
*
* Revision 1.6  2007/03/13 16:58:36  jaume
* Added QuantityByCategory (Multivariable legend) and some bugfixes in symbols
*
* Revision 1.5  2007/03/09 11:20:56  jaume
* Advanced symbology (start committing)
*
* Revision 1.3.2.7  2007/02/21 07:34:09  jaume
* labeling starts working
*
* Revision 1.3.2.6  2007/02/16 10:54:12  jaume
* multilayer splitted to multilayerline, multilayermarker,and  multilayerfill
*
* Revision 1.3.2.5  2007/02/15 16:23:44  jaume
* *** empty log message ***
*
* Revision 1.3.2.4  2007/02/13 16:19:19  jaume
* graduated symbol legends (start commiting)
*
* Revision 1.3.2.3  2007/02/12 15:15:20  jaume
* refactored interval legend and added graduated symbol legend
*
* Revision 1.3.2.2  2007/02/09 07:47:05  jaume
* Isymbol moved
*
* Revision 1.3.2.1  2007/02/05 14:59:03  jaume
* *** empty log message ***
*
* Revision 1.3  2007/01/25 16:25:23  jaume
* *** empty log message ***
*
* Revision 1.2  2007/01/10 16:39:41  jaume
* ISymbol now belongs to com.iver.cit.gvsig.fmap.core.symbols package
*
* Revision 1.1  2007/01/10 16:31:36  jaume
* *** empty log message ***
*
* Revision 1.1  2006/11/14 11:10:27  jaume
* *** empty log message ***
*
*
*/
package org.gvsig.fmap.mapcontext.rendering.symbols;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.rendering.symbols.styles.IMask;
import org.gvsig.tools.task.Cancellable;

import com.iver.utiles.StringUtilities;
import com.iver.utiles.XMLEntity;

/**
 * SimpleMarkerSymbol is the most basic symbol for the representation of point objects
 * which can define its size and color apart from the rotation and the offset from a point
 * in the map.
 * @author   jaume dominguez faus - jaume.dominguez@iver.es
 */
public class SimpleMarkerSymbol extends AbstractMarkerSymbol {

	public static final int CIRCLE_STYLE = 0;
	public static final int SQUARE_STYLE = 1;
	public static final int CROSS_STYLE = 2;
	public static final int DIAMOND_STYLE = 3;
	public static final int X_STYLE = 4;
	public static final int TRIANGLE_STYLE = 5;
	public static final int STAR_STYLE = 6;
	private boolean outlined;
	private Color outlineColor;
	private double outlineSize;
	private ISymbol selectionSymbol;
	private int markerStyle;

	public ISymbol getSymbolForSelection() {
		if (selectionSymbol == null) {
			XMLEntity xml = getXMLEntity();
			xml.putProperty("color", StringUtilities.color2String(MapContext.getSelectionColor()));
			selectionSymbol = SymbologyFactory.
					createSymbolFromXML(xml, getDescription()+" version for selection");
		}
		return selectionSymbol;
	}

	public void draw(Graphics2D g, AffineTransform affineTransform, Geometry geom, Cancellable cancel) {
		int x, y;
//		Point2D p = new Point2D.Double(((FPoint2D) shp).getX(), ((FPoint2D) shp)
//				.getY());
		org.gvsig.fmap.geom.primitive.Point p = (org.gvsig.fmap.geom.primitive.Point) geom;

		int size = (int) getSize();

		int halfSize = size/2;
		x = ((int) (p.getX() + getOffset().getX()) - halfSize);
		y = ((int) (p.getY() + getOffset().getY()) - halfSize);

		IMask mask = getMask();
		if (mask != null) {
			IFillSymbol maskShape = mask.getFillSymbol();
//			maskShape.draw(g, null, mask.getHaloShape(shp));
		}

		g.setColor(getColor());
		GeneralPathX genPath = null;
		g.setStroke(new BasicStroke(1));

		switch (markerStyle) {
		case CIRCLE_STYLE:
			g.fillOval(x, y, size, size);
			break;
		case SQUARE_STYLE:
			g.fillRect(x, y, size, size);
			break;
		case CROSS_STYLE:
			x = x + halfSize;
			y = y + halfSize;
			genPath = new GeneralPathX();
			genPath.moveTo(x, y - halfSize);
			genPath.lineTo(x, y + halfSize);
			genPath.moveTo(x - halfSize, y);
			genPath.lineTo(x + halfSize, y);
			g.draw(genPath);
			break;
		case DIAMOND_STYLE:
			x = x + halfSize;
			y = y + halfSize;
			genPath = new GeneralPathX();
			genPath.moveTo(x-halfSize, y);
			genPath.lineTo(x, y+halfSize);
			genPath.lineTo(x+halfSize, y);
			genPath.lineTo(x, y-halfSize);
			genPath.lineTo(x-halfSize, y);
			genPath.closePath();
			g.fill(genPath);
			break;
		case X_STYLE:
			x = x + halfSize;
			y = y + halfSize;
			genPath = new GeneralPathX();
			genPath.moveTo(x-halfSize, y - halfSize);
			genPath.lineTo(x+halfSize, y + halfSize);
			genPath.moveTo(x - halfSize, y + halfSize);
			genPath.lineTo(x + halfSize, y - halfSize);
			g.draw(genPath);
			break;
		case TRIANGLE_STYLE:
			x = x + halfSize;
			y = y + halfSize;
			int otherSize = (int) (size * 0.55);
			genPath = new GeneralPathX();
			genPath.moveTo(x - halfSize,
				y + halfSize);
			genPath.lineTo(x + halfSize,
				y + halfSize);
			genPath.lineTo(x, y - otherSize);
			genPath.closePath();

			g.fill(genPath);
			break;
		case STAR_STYLE:
			x = x + halfSize;
			y = y + halfSize;
			genPath = new GeneralPathX();
			genPath.moveTo(x-halfSize, y);

			genPath.lineTo(x-2*(halfSize/3), y+(halfSize/3));
			genPath.lineTo(x-2*(halfSize/3), y+2*(halfSize/3));
			genPath.lineTo(x-(halfSize/3), y+2*(halfSize/3));

			genPath.lineTo(x, y+halfSize);

			genPath.lineTo(x+(halfSize/3), y+2*(halfSize/3));
			genPath.lineTo(x+2*(halfSize/3), y+2*(halfSize/3));
			genPath.lineTo(x+2*(halfSize/3), y+(halfSize/3));

			genPath.lineTo(x+(halfSize), y);

			genPath.lineTo(x+2*(halfSize/3), y-(halfSize/3));
			genPath.lineTo(x+2*(halfSize/3), y-2*(halfSize/3));
			genPath.lineTo(x+(halfSize/3), y-2*(halfSize/3));

			genPath.lineTo(x, y-halfSize);

			genPath.lineTo(x-(halfSize/3), y-2*(halfSize/3));
			genPath.lineTo(x-2*(halfSize/3), y-2*(halfSize/3));
			genPath.lineTo(x-2*(halfSize/3), y-(halfSize/3));

			genPath.lineTo(x-halfSize, y);

			genPath.closePath();


			g.fill(genPath);

			break;
		}


		if (outlined) {
			g.setColor(outlineColor);
			switch (markerStyle) {
			case CIRCLE_STYLE:
				g.drawOval(x, y, size, size);
				break;
			case SQUARE_STYLE:
				g.drawRect(x, y, size, size);
				break;
			case CROSS_STYLE:
			case DIAMOND_STYLE:
			case STAR_STYLE:
			case X_STYLE:
			case TRIANGLE_STYLE:
				g.draw(genPath);
				break;
			}
		}
	}

	public XMLEntity getXMLEntity() {
		XMLEntity xml = new XMLEntity();
		xml.putProperty("className", getClassName());
		xml.putProperty("desc", getDescription());
		xml.putProperty("isShapeVisible", isShapeVisible());
		xml.putProperty("color", StringUtilities.color2String(getColor()));
		xml.putProperty("rotation", getRotation());
		xml.putProperty("offsetX", getOffset().getX());
		xml.putProperty("offsetY", getOffset().getY());
		xml.putProperty("size", getSize());
		xml.putProperty("outline", outlined);
		xml.putProperty("unit", getUnit());
		xml.putProperty("referenceSystem", getReferenceSystem());
		xml.putProperty("markerStyle", getStyle());

		if (outlined) {
			xml.putProperty("outlineColor", StringUtilities.color2String(outlineColor));
			xml.putProperty("outlineSize", outlineSize);
		}
		return xml;
	}


	public String getClassName() {
		return getClass().getName();
	}

	public void setXMLEntity(XMLEntity xml) {
		setDescription(xml.getStringProperty("desc"));
		setIsShapeVisible(xml.getBooleanProperty("isShapeVisible"));
		setColor(StringUtilities.string2Color(xml.getStringProperty("color")));
		setRotation(xml.getDoubleProperty("rotation"));
		setOffset(new Point.Double(xml.getDoubleProperty("offsetX"),
						xml.getDoubleProperty("offsetY")));
		setSize(xml.getDoubleProperty("size"));
		setOutlined(xml.getBooleanProperty("outline"));
		setUnit(xml.getIntProperty("unit"));
		setReferenceSystem(xml.getIntProperty("referenceSystem"));
		setStyle(xml.getIntProperty("markerStyle"));
		if (hasOutline()) {
			setOutlineColor(StringUtilities.string2Color(xml.getStringProperty("outlineColor")));
			setOutlineSize(xml.getDoubleProperty("outlineSize"));
		}
	}

	/**
	 * Returns true or false depending if the simple marker symbol has an outline or not.
	 * @return Returns the outline.
	 */
	public boolean hasOutline() {
		return outlined;
	}

	/**
	 * Establishes the outline for the simple marker symbol.
	 * @param outline  The outline to set.
	 */
	public void setOutlined(boolean outlined) {
		this.outlined = outlined;
	}

	/**
	 * Returns the outline color for the symple marker symbol
	 *
	 * @return Color,outlineColor.
	 */
	public Color getOutlineColor() {
		return outlineColor;
	}

	/**
	 * Sets the outline color for the simple marker symbol
	 * @param outlineColor, Color
	 */
	public void setOutlineColor(Color outlineColor) {
		this.outlineColor = outlineColor;
	}

	/**
	 * Gets the size of the outline for the simple marker symbol
	 * @return  Returns the outlineSize.
	 */
	public double getOutlineSize() {
		return outlineSize;
	}

	/**
	 * Establishes the size for the outline of the simple marker symbol
	 * @param outlineSize  The outlineSize to set.
	 */
	public void setOutlineSize(double outlineSize) {
		this.outlineSize = outlineSize;
	}

	/**
	 * @return  Returns the selectionSymbol.
	 */
	public ISymbol getSelectionSymbol() {
		return selectionSymbol;
	}

	/**
	 * @param selectionSymbol  The selectionSymbol to set.
	 */
	public void setSelectionSymbol(ISymbol selectionSymbol) {
		this.selectionSymbol = selectionSymbol;
	}
	/**
	 * Sets the style for the simple marker symbol
	 * @param style
	 */
	public void setStyle(int style) {
		this.markerStyle = style;
	}

	/**
	 * Obtains the style for the simple marker symbol
	 * @return markerStyle,int
	 */
	public int getStyle() {
		return markerStyle;
	}

}