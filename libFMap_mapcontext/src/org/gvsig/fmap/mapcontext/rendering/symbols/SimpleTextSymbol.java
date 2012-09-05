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
package org.gvsig.fmap.mapcontext.rendering.symbols;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;


import org.gvsig.compat.print.PrintAttributes;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.geom.util.UtilFunctions;
import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.tools.task.Cancellable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.utiles.StringUtilities;
import com.iver.utiles.XMLEntity;

/**
 * SimpleTextSymbol is a class used to create symbols composed using a text defined by
 * the user.This text can be edited (changing the color, the font of the characters, and
 * the rotation of the text).
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 *
 */
public class SimpleTextSymbol extends AbstractSymbol implements ITextSymbol {
	private static final GeometryManager geomManager = GeometryLocator.getGeometryManager();
	private String text = "";
	private Font font = new Font("Arial", Font.PLAIN, 12);
	private Color textColor = Color.BLACK;
	private double rotation;
	private FontRenderContext frc = new FontRenderContext(
			new AffineTransform(), false, true);
	private boolean autoresize;
	final static private Logger logger = LoggerFactory.getLogger(SimpleTextSymbol.class);
	public void draw(Graphics2D g, AffineTransform affineTransform, Geometry geom, Cancellable cancel) {
		if (!isShapeVisible()) {
			return;
		}
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(textColor);
		g.setFont(font);
		g.translate(((org.gvsig.fmap.geom.primitive.Point) geom).getX(), ((org.gvsig.fmap.geom.primitive.Point) geom).getY());

		g.rotate(rotation);
		Rectangle2D bounds = getBounds();
//		 getBounds devuelve el bounds de un texto dibujado de manera que
		// la linea base de la primera letra está en el punto (0,0).
		// Por eso, si queremos alinear el texto de manera que la parte superior
		// izquierda de la primera letra esté en (0,0) debemos moverlo según
		// el valor de la ordenada, en lugar de según su altura.
		g.drawString(getText(), 0, (int)-bounds.getY());//(int) bounds.getHeight());
		g.rotate(-rotation);
		g.translate(-((org.gvsig.fmap.geom.primitive.Point) geom).getX(), -((org.gvsig.fmap.geom.primitive.Point) geom).getY());
	}

	public void drawInsideRectangle(Graphics2D g,
			AffineTransform scaleInstance, Rectangle r, PrintAttributes properties) throws SymbolDrawingException {
		int s = getFont().getSize();
		if (autoresize) {
			if (s==0) {
				s =1;
				setFontSize(s);
			}
			g.setFont(getFont());
		    FontMetrics fm = g.getFontMetrics();
		    Rectangle2D rect = fm.getStringBounds(text, g);
		    double width = rect.getWidth();
		    double height = rect.getHeight();
		    double rWidth = r.getWidth();
		    double rHeight = r.getHeight();
		    double ratioText = width/height;
		    double ratioRect = rWidth/rHeight;

		    if (ratioText>ratioRect) {
		    	s = (int) (s*(rWidth/width));
		    } else {
		    	s = (int) (s*(rHeight/height));
		    }
//		    r.setLocation(r.x, (int) Math.round(r.getY()+r.getHeight()));
		}
		setFontSize(s);
//		g.setColor(Color.red);
//		g.draw(r);
		try {
			if (properties==null)
				draw(g, null, geomManager.createPoint(r.getX(), r.getY(), SUBTYPES.GEOM2D), null);
			else
				print(g, new AffineTransform(), geomManager.createPoint(r.getX(), r.getY(), SUBTYPES.GEOM2D), properties);
		} catch (CreateGeometryException e) {
			logger.error("Error creatint a point", e);
			e.printStackTrace();
		}


	}

	public int getOnePointRgb() {
		return textColor.getRGB();
	}

	public void getPixExtentPlus(org.gvsig.fmap.geom.Geometry geom, float[] distances,
			ViewPort viewPort, int dpi) {
		throw new Error("Not yet implemented!");

	}

	public ISymbol getSymbolForSelection() {
		return this; // a text is not selectable
	}

	public int getSymbolType() {
		return Geometry.TYPES.TEXT;
	}

	public XMLEntity getXMLEntity() {
		XMLEntity xml = new XMLEntity();
		xml.putProperty("className", getClassName());
		xml.putProperty("desc", getDescription());
		xml.putProperty("isShapeVisible", isShapeVisible());
		xml.putProperty("font", font.getName());
		xml.putProperty("fontStyle", font.getStyle());
		xml.putProperty("size", font.getSize());
		xml.putProperty("text", text);
		xml.putProperty("textColor", StringUtilities.color2String(textColor));
		xml.putProperty("unit", getUnit());
		xml.putProperty("referenceSystem", getReferenceSystem());
		xml.putProperty("autoresizeFlag", isAutoresizeEnabled());
		return xml;
	}

	public boolean isSuitableFor(Geometry geom) {
		return true;
	}

	public void setXMLEntity(XMLEntity xml) {
		font = new Font(xml.getStringProperty("font"),
				xml.getIntProperty("fontStyle"),
				xml.getIntProperty("size"));
		setDescription(xml.getStringProperty("desc"));
		setIsShapeVisible(xml.getBooleanProperty("isShapeVisible"));
		text = xml.getStringProperty("text");
		textColor = StringUtilities.string2Color(xml.getStringProperty("textColor"));
		setUnit(xml.getIntProperty("unit"));
		setReferenceSystem(xml.getIntProperty("referenceSystem"));
		setAutoresizeEnabled(xml.getBooleanProperty("autoresizeFlag"));
	}

	public String getClassName() {
		return getClass().getName();
	}

	public void print(Graphics2D g, AffineTransform at, org.gvsig.fmap.geom.Geometry geom, PrintAttributes properties){
		float originalSize = getFont().getSize2D();
		float size=originalSize;
		// scale it to size
		int pq = properties.getPrintQuality();
		if (pq == PrintAttributes.PRINT_QUALITY_NORMAL){
			size *= (double) 300/72;
		}else if (pq == PrintAttributes.PRINT_QUALITY_HIGH){
			size *= (double) 600/72;
		}else if (pq == PrintAttributes.PRINT_QUALITY_DRAFT){
			// d *= 72/72; // (which is the same than doing nothing)
		}
		setFont(getFont().deriveFont(size));
		draw(g,at,geom,null);
		setFont(getFont().deriveFont(originalSize));
	}

	public String getText() {
		return text;
	}

	public Font getFont() {
		return font;
	}

	public Color getTextColor() {
		return textColor;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setFont(Font font) {
		if (font == null) {
			logger.warn("font <-- null");

			return;
		}
		this.font = font;
	}

	public void setTextColor(Color color) {
		this.textColor = color;
	}

	public void setFontSize(double size) {
		Font newFont = new Font(font.getName(), font.getStyle(), (int) size);
		if (newFont == null) {
			logger.warn(
					"Font(" + font.getName() + ", "
					+ font.getStyle() + ", " + (int) size + " --> null");
		} else {
			this.font = newFont;
		}
	}

	/**
	 * Defines the angle of rotation for the text that composes the symbol
	 *
	 * @param rotation
	 */
	public void setRotation(double rotation) {
		this.rotation = rotation;
	}

	/**
	 * Returns an Geometry which represents a rectangle containing the text in
	 * <b>screen</b> units.
	 */
	public Geometry getTextWrappingShape(org.gvsig.fmap.geom.primitive.Point p) {
		Font font = getFont();
		GlyphVector gv = font.createGlyphVector(frc, text);

		Shape shape = gv.getOutline((float) p.getX(), (float) p.getY());
		Geometry myFShape;
		try {
			myFShape = geomManager.createSurface(new GeneralPathX(shape.getBounds2D()), SUBTYPES.GEOM2D);
			myFShape.transform(AffineTransform.getTranslateInstance(p.getX(), p.getY()));

			if (rotation != 0) {
				myFShape.transform(AffineTransform.getRotateInstance(rotation));
			}
			return myFShape;
		} catch (CreateGeometryException e) {
			logger.error("Error creating a surface", e);
			e.printStackTrace();
		}
		return null;
	}

	public Rectangle getBounds() {
//		FontMetrics fm = g.getFontMetrics();
//		Rectangle2D rect = fm.getStringBounds("graphics", g);

		Rectangle bounds = null;
		try {
			bounds = getTextWrappingShape(geomManager.createPoint(0,0, SUBTYPES.GEOM2D)).getBounds();
		} catch (CreateGeometryException e) {
			logger.error("Error creating a point", e);
		}
		return bounds;
	}

	public void setCartographicSize(double cartographicSize, Geometry geom) {
		setFontSize(cartographicSize);
	}

	public double toCartographicSize(ViewPort viewPort, double dpi, Geometry geom) {
		double oldSize = getFont().getSize();
		setCartographicSize(getCartographicSize(
								viewPort,
								dpi,
								geom),
							geom);
		return oldSize;
	}

	public double getCartographicSize(ViewPort viewPort, double dpi, Geometry geom) {
		return CartographicSupportToolkit.
					getCartographicLength(this,
										  getFont().getSize(),
										  viewPort,
										  dpi);
	}
	public boolean isAutoresizeEnabled() {
		return autoresize;
	}

	public void setAutoresizeEnabled(boolean autoresizeFlag) {
		this.autoresize = autoresizeFlag;
	}
}
