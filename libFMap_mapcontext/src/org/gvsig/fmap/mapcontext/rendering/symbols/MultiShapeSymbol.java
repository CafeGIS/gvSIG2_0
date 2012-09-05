package org.gvsig.fmap.mapcontext.rendering.symbols;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.logging.Logger;

import org.gvsig.compat.print.PrintAttributes;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.mapcontext.rendering.symbols.styles.ILineStyle;
import org.gvsig.fmap.mapcontext.rendering.symbols.styles.IMask;
import org.gvsig.tools.task.Cancellable;

import com.iver.utiles.XMLEntity;
import com.iver.utiles.XMLException;

/**
 * MultiShapeSymbol class allows to create a composition of several symbols with
 * different shapes and be treated as a single symbol.These shapes can be marker,line
 * or fill.
 *
 * @author   jaume dominguez faus - jaume.dominguez@iver.es
 */

public class MultiShapeSymbol implements ILineSymbol, IMarkerSymbol, IFillSymbol {
	private IMarkerSymbol marker = SymbologyFactory.createDefaultMarkerSymbol();
	private ILineSymbol line = SymbologyFactory.createDefaultLineSymbol();
	private IFillSymbol fill = SymbologyFactory.createDefaultFillSymbol();
	private IMask mask;
	private String desc;
	private int referenceSystem;
	private MultiShapeSymbol symSelect;

	public Color getLineColor() {
		return line.getColor();
	}

	public void setLineColor(Color color) {
		line.setLineColor(color);
	}

	public ILineStyle getLineStyle() {
		return line.getLineStyle();
	}

	public void setLineStyle(ILineStyle lineStyle) {
		line.setLineStyle(lineStyle);
	}

	public void setLineWidth(double width) {
		line.setLineWidth(width);
	}

	public double getLineWidth() {
		return line.getLineWidth();
	}

	public int getAlpha() {
		return line.getAlpha();
	}

	public void setAlpha(int outlineAlpha) {
		line.setAlpha(outlineAlpha);
	}

	public ISymbol getSymbolForSelection() {
		if (symSelect == null) {
			symSelect = new MultiShapeSymbol();
		}

		if (marker!=null){
			symSelect.setMarkerSymbol((IMarkerSymbol) marker.getSymbolForSelection());
		}

		if (line!=null){
			symSelect.setLineSymbol((ILineSymbol) line.getSymbolForSelection());
		}

		if (fill!=null ){
			symSelect.setFillSymbol((IFillSymbol) fill.getSymbolForSelection());
		}

		return symSelect;

	}

	public void draw(Graphics2D g, AffineTransform affineTransform, Geometry geom, Cancellable cancel) {
		switch (geom.getType()) {
		case Geometry.TYPES.POINT: //Tipo punto
           	if (marker != null) {
				marker.draw(g, affineTransform, geom, cancel);
			}
			break;
		case Geometry.TYPES.CURVE:
		case Geometry.TYPES.ARC:
		if (line != null) {
				line.draw(g, affineTransform, geom, cancel);
			}
			break;

		case Geometry.TYPES.SURFACE:
        case Geometry.TYPES.ELLIPSE:
		case Geometry.TYPES.CIRCLE:
			if (fill != null) {
				fill.draw(g, affineTransform, geom, cancel);
			}
			break;
		}


	}

	public void getPixExtentPlus(Geometry geom, float[] distances,
			ViewPort viewPort, int dpi) {
		// TODO Implement it
		throw new Error("Not yet implemented!");

	}

	public int getOnePointRgb() {
		// will return a mixture of all symbol's getOnePointRgb() value

		int rMarker = 0;
		int gMarker = 0;
		int bMarker = 0;
		int aMarker = 0;

		if (marker!=null && marker.getColor() != null) {
			rMarker = marker.getColor().getRed();
			gMarker = marker.getColor().getGreen();
			bMarker = marker.getColor().getBlue();
			aMarker = marker.getColor().getAlpha();
		}

		int rLine = 0;
		int gLine = 0;
		int bLine = 0;
		int aLine = 0;

		if (line != null  && line.getColor() != null) {
			rLine = line.getColor().getRed();
			gLine = line.getColor().getGreen();
			bLine = line.getColor().getBlue();
			aLine = line.getColor().getAlpha();
		}

		int rFill = 0;
		int gFill = 0;
		int bFill = 0;
		int aFill = 0;

		if (fill != null ) {
			Color colorOfFill = null;
			if (fill.getOutline()!=null) {
				colorOfFill = fill.getOutline().getColor();
			} else if (fill.getFillColor()!=null) {
				colorOfFill = fill.getFillColor();
			}
			if (colorOfFill != null) {
				rFill = colorOfFill.getRed();
				gFill = colorOfFill.getGreen();
				bFill = colorOfFill.getBlue();
				aFill = colorOfFill.getAlpha();

			}
		}

		int red = (rMarker + rLine + rFill) / 3;
		int green = (gMarker + gLine + gFill) / 3;
		int blue = (bMarker + bLine + bFill) / 3;
		int alpha = (aMarker + aLine + aFill) / 3;

		return (alpha) << 24 + (red << 16) + (green << 8) + blue;
	}

	public XMLEntity getXMLEntity() throws XMLException {
		XMLEntity xml = new XMLEntity();
		xml.putProperty("className", getClassName());
		xml.putProperty("desc", getDescription());
		xml.putProperty("unit", getUnit());

		if (marker!=null) {
			XMLEntity markerXML = marker.getXMLEntity();
			markerXML.putProperty("id", "marker");
			xml.addChild(markerXML);
		}

		if (line!=null) {
			XMLEntity lineXML = line.getXMLEntity();
			lineXML.putProperty("id", "line");
			xml.addChild(lineXML);
		}

		if (fill!=null) {
			XMLEntity fillXML = fill.getXMLEntity();
			fillXML.putProperty("id", "fill");
			xml.addChild(fillXML);


		}
		return xml;
	}

	public void setXMLEntity(XMLEntity xml) {
		setDescription(xml.getStringProperty("desc"));
		setUnit(xml.getIntProperty("unit"));

		XMLEntity myXML;
		myXML = xml.firstChild("id", "marker");
		if (myXML != null) {
			marker = (IMarkerSymbol) SymbologyFactory.
						createSymbolFromXML(myXML, null);
		}

		myXML = xml.firstChild("id", "line");
		if (myXML != null) {
			line = (ILineSymbol) SymbologyFactory.
						createSymbolFromXML(myXML, null);
		}

		myXML = xml.firstChild("id", "fill");
		if (myXML != null) {
			fill = (IFillSymbol) SymbologyFactory.
						createSymbolFromXML(myXML, null);
		}


	}

	public String getDescription() {
		return desc;
	}

	public boolean isShapeVisible() {
		if (marker!=null) {
			return marker.isShapeVisible();
		}

		if (line != null) {
			return line.isShapeVisible();
		}

		if (fill != null) {
			fill.isShapeVisible();
		}

		return false;
	}

	public void setDescription(String desc) {
		this.desc = desc ;
	}

	public int getSymbolType() {
		return Geometry.TYPES.GEOMETRY;
	}

	public boolean isSuitableFor(Geometry geom) {
		// suitable for everything (why else does it exist?)
		return true;
	}

	public void drawInsideRectangle(Graphics2D g,
			AffineTransform scaleInstance, Rectangle r, PrintAttributes properties) throws SymbolDrawingException {
		double myWidth =  (r.getWidth()/3);

		Rectangle rect = new Rectangle(0, r.y, (int) myWidth, r.height);

		if (marker != null) {
			g.translate(r.x, r.y);
			marker.drawInsideRectangle(g, scaleInstance, rect, properties);
			g.translate(-(r.x), -(r.y));
		}

		if (line != null) {
			g.translate(r.x+myWidth, r.y);
			line.drawInsideRectangle(g, scaleInstance, rect, properties);
			g.translate(-(r.x+myWidth), -(r.y));
		}

		if (fill != null) {
			g.translate(r.x+myWidth+myWidth, r.y);
			fill.drawInsideRectangle(g, scaleInstance, rect, properties);
			g.translate(-(r.x+myWidth+myWidth), -(r.y));

		}
	}

	public String getClassName() {
		return getClass().getName();
	}

	public void print(Graphics2D g, AffineTransform at, Geometry geom, PrintAttributes properties) {
		switch (geom.getType()) {
		case Geometry.TYPES.POINT: //Tipo punto
           	if (marker != null) {
				marker.print(g, at, geom, properties);
			}
			break;
		case Geometry.TYPES.CURVE:
		case Geometry.TYPES.ARC:
			if (line != null) {
				line.print(g, at, geom, properties);
			}
			break;

		case Geometry.TYPES.SURFACE:
		case Geometry.TYPES.ELLIPSE:
        case Geometry.TYPES.CIRCLE:
			if (fill != null) {
				fill.print(g, at, geom, properties);
			}
			break;
		}
	}

	public double getRotation() {
		if (marker != null) {
			return marker.getRotation();
		}
		return 0;
	}

	public void setRotation(double rotation) {
		if (marker != null) {
			marker.setRotation(rotation);
		}
	}

	public Point2D getOffset() {
		if (marker != null) {
			return marker.getOffset();
		}
		return new Point2D.Double();
	}

	public void setOffset(Point2D offset) {
		if (marker != null) {
			marker.setOffset(offset);
		}
	}

	public double getSize() {
		if (marker != null) {
			return marker.getSize();
		}
		return 0;
	}

	public void setSize(double size) {
		if (marker != null) {
			marker.setSize(size);
		}

	}

	public Color getColor() {
		if (marker != null) {
			return marker.getColor();
		}
		return null;
	}

	public void setColor(Color color) {
		if (marker != null) {
			marker.setColor(color);
		}
	}

	public void setFillColor(Color color) {
		if (fill != null) {
			fill.setFillColor(color);
		}
	}

	public void setOutline(ILineSymbol outline) {
		if (fill != null) {
			fill.setOutline(outline);
		}
	}

	public Color getFillColor() {
		if (fill != null) {
			return fill.getFillColor();
		}
		return null;
	}

	public ILineSymbol getOutline() {
		if (fill != null) {
			return fill.getOutline();
		}
		return null;
	}

	public int getFillAlpha() {
		if (fill != null) {
			return fill.getFillAlpha();
		}
		return 255;
	}

	public IMask getMask() {
		return mask;
	}

	public void setUnit(int unitIndex) {
		if (marker != null) {
			marker.setUnit(unitIndex);
		}
		if (line != null) {
			line.setUnit(unitIndex);
		}
	}

	public int getUnit() {
		if (marker != null) {
			return marker.getUnit();
		}
		if (line != null) {
			return line.getUnit();
		}
		return -1;
	}

	public void setMask(IMask mask) {
		// TODO Implement it
		throw new Error("Not yet implemented!");

	}

	public int getReferenceSystem() {
		return this.referenceSystem;
	}

	public void setReferenceSystem(int system) {
		this.referenceSystem = system;

	}

	public double toCartographicSize(ViewPort viewPort, double dpi, Geometry geom) {
		switch (geom.getType()) {
		case Geometry.TYPES.POINT: //Tipo punto
        	if (marker != null) {
				return marker.toCartographicSize(viewPort, dpi, geom);
			}
		case Geometry.TYPES.CURVE:
		case Geometry.TYPES.ARC:
			if (line != null) {
				return line.toCartographicSize(viewPort, dpi, geom);
			}
		case Geometry.TYPES.SURFACE:
		case Geometry.TYPES.ELLIPSE:
		case Geometry.TYPES.CIRCLE:
			Logger.getAnonymousLogger().warning("Cartographic size does not have any sense for fill symbols");

		}
		return -1;
	}

	public void setCartographicSize(double cartographicSize, Geometry geom) {
		switch (geom.getType()) {
		case Geometry.TYPES.POINT: //Tipo punto
        	if (marker != null) {
				marker.setCartographicSize(cartographicSize, null);
			}
        	break;
		case Geometry.TYPES.CURVE:
		case Geometry.TYPES.ARC:
			if (line != null) {
				line.setCartographicSize(cartographicSize, null);
			}
        	break;
		case Geometry.TYPES.SURFACE:
		case Geometry.TYPES.ELLIPSE:
	    case Geometry.TYPES.CIRCLE:
			Logger.getAnonymousLogger().warning("Cartographic size does not have any sense for fill symbols");
		}
	}


	public double getCartographicSize(ViewPort viewPort, double dpi, Geometry geom) {
		switch (geom.getType()) {
		case Geometry.TYPES.POINT: //Tipo punto
        	return CartographicSupportToolkit.
			getCartographicLength(marker,
								  getSize(),
								  viewPort,
								  dpi);
		case Geometry.TYPES.CURVE:
		case Geometry.TYPES.ARC:
			return CartographicSupportToolkit.
			getCartographicLength(line,
								  getSize(),
								  viewPort,
								  dpi);
		case Geometry.TYPES.SURFACE:
     	case Geometry.TYPES.ELLIPSE:
	    case Geometry.TYPES.CIRCLE:
			Logger.getAnonymousLogger().warning("Cartographic size does not have any sense for fill symbols");
		}
		return -1;
	}

	public IMarkerSymbol getMarkerSymbol() {
		return marker;
	}

	public ILineSymbol getLineSymbol() {
		return line;
	}

	public IFillSymbol getFillSymbol() {
		return fill;
	}

	public void setMarkerSymbol(IMarkerSymbol markerSymbol) {
		this.marker = markerSymbol;
	}

	public void setLineSymbol(ILineSymbol lineSymbol) {
		this.line = lineSymbol;
	}

	public void setFillSymbol(IFillSymbol fillFillSymbol) {
		this.fill = fillFillSymbol;
	}

	public boolean hasFill() {
	if (fill == null) {
		return false;
	}
	return fill.hasFill();
	}

	public void setHasFill(boolean hasFill) {
		if (fill != null) {
			fill.setHasFill(hasFill);
//			this.hasFill = hasFill;
		}
	}

	public boolean hasOutline() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setHasOutline(boolean hasOutline) {
		// TODO Auto-generated method stub

	}

}
