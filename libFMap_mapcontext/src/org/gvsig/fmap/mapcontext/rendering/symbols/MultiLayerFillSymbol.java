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
* $Id: MultiLayerFillSymbol.java 21071 2008-06-02 10:55:35Z vcaballero $
* $Log$
* Revision 1.12  2007-09-21 12:25:32  jaume
* cancellation support extended down to the IGeometry and ISymbol level
*
* Revision 1.11  2007/09/19 16:20:45  jaume
* removed unnecessary imports
*
* Revision 1.10  2007/08/13 11:36:50  jvidal
* javadoc
*
* Revision 1.9  2007/08/08 12:04:15  jvidal
* javadoc
*
* Revision 1.8  2007/07/23 06:52:25  jaume
* default selection color refactored, moved to MapContext
*
* Revision 1.7  2007/07/03 10:58:29  jaume
* first refactor on CartographicSupport
*
* Revision 1.6  2007/06/29 13:07:01  jaume
* +PictureLineSymbol
*
* Revision 1.5  2007/03/29 16:02:01  jaume
* *** empty log message ***
*
* Revision 1.4  2007/03/26 14:25:29  jaume
* implemented Print
*
* Revision 1.3  2007/03/13 16:58:36  jaume
* Added QuantityByCategory (Multivariable legend) and some bugfixes in symbols
*
* Revision 1.2  2007/03/09 11:20:57  jaume
* Advanced symbology (start committing)
*
* Revision 1.1.2.3  2007/02/21 16:09:02  jaume
* *** empty log message ***
*
* Revision 1.1.2.2  2007/02/21 07:34:09  jaume
* labeling starts working
*
* Revision 1.1.2.1  2007/02/16 10:54:12  jaume
* multilayer splitted to multilayerline, multilayermarker,and  multilayerfill
*
*
*/
package org.gvsig.fmap.mapcontext.rendering.symbols;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import org.gvsig.compat.print.PrintAttributes;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.tools.task.Cancellable;

import com.iver.utiles.XMLEntity;
import com.iver.utiles.XMLException;

/**
 * MultiLayerFillSymbol is a symbol which allows to group several kind of fill symbols
 * (xxxFillSymbol implementing IFillSymbol)in one and treats it like single symbol.
 */

public class MultiLayerFillSymbol extends AbstractFillSymbol implements IFillSymbol, IMultiLayerSymbol{
	private static final double OPACITY_SELECTION_FACTOR = .8;
	private IFillSymbol[] layers = new IFillSymbol[0];
	private MultiLayerFillSymbol selectionSymbol;
	private Object symbolType;

	public Color getFillColor() {
		/*
		 * a multilayer symbol does not define any color, the color
		 * of each layer is defined by the layer itself
		 */
		return null;
	}

	public int getOnePointRgb() {
		// will paint only the last layer pixel
		return layers[layers.length-1].getOnePointRgb();
	}

	public ILineSymbol getOutline() {
		/*
		 * a multilayer symbol does not define any outline, the outline
		 * of each layer is defined by the layer it self
		 */
		return null;
	}

	public boolean isSuitableFor(Geometry geom) {
		return geom.getType() == Geometry.TYPES.SURFACE;
	}

	public void setFillColor(Color color) {
		/*
		 * Will apply the color to each layer
		 */
		for (int i = 0; i < layers.length; i++) {
			layers[i].setFillColor(color);
		}
	}

	public void setOutline(ILineSymbol outline) {
		for (int i = 0; i < layers.length; i++) {
			layers[i].setOutline(null);
		}
		layers[layers.length-1].setOutline(outline);
	}

	public void draw(Graphics2D g, AffineTransform affineTransform, Geometry geom, Cancellable cancel) {
		for (int i = 0; (cancel==null || !cancel.isCanceled()) && i < layers.length; i++) {
			layers[i].draw(g, affineTransform, geom, cancel);
		}
	}

	public void drawInsideRectangle(Graphics2D g,
			AffineTransform scaleInstance, Rectangle r, PrintAttributes properties) throws SymbolDrawingException {
		for (int i = 0; i < layers.length; i++) {
			layers[i].drawInsideRectangle(g, scaleInstance, r, properties);
		}
	}

	public void getPixExtentPlus(Geometry geom, float[] distances,
			ViewPort viewPort, int dpi) {
		float[] myDistances = new float[] {0,0};
		distances[0] = 0;
		distances[1] = 0;
		for (int i = 0; i < layers.length; i++) {
			layers[i].getPixExtentPlus(geom, myDistances, viewPort, dpi);
			distances[0] = Math.max(myDistances[0], distances[0]);
			distances[1] = Math.max(myDistances[1], distances[1]);
		}
	}

	public ISymbol getSymbolForSelection() {
		if (selectionSymbol == null) {
			selectionSymbol = new MultiLayerFillSymbol();
			selectionSymbol.setDescription(getDescription());
			selectionSymbol.symbolType = symbolType;
			for (int i = 0; i < layers.length; i++) {
				selectionSymbol.addLayer(layers[i].getSymbolForSelection());
			}
			SimpleFillSymbol selLayer = new SimpleFillSymbol();
			Color c = MapContext.getSelectionColor();
			c = new Color(
					c.getRed(),
					c.getGreen(),
					c.getBlue(),
					(int) (255*OPACITY_SELECTION_FACTOR));
			selLayer.setFillColor(c);
			selLayer.setOutline(getOutline());
			selectionSymbol.addLayer(selLayer);
		}
		return selectionSymbol;

	}


	public int getSymbolType() {
		return Geometry.TYPES.SURFACE;
	}

	public XMLEntity getXMLEntity() throws XMLException {
		XMLEntity xml = new XMLEntity();
		xml.putProperty("className", getClassName());
		xml.putProperty("desc", getDescription());
		xml.putProperty("isShapeVisible", isShapeVisible());
		xml.putProperty("referenceSystem", getReferenceSystem());
		xml.putProperty("unit", getUnit());

		for (int i = 0; i < layers.length; i++) {
			xml.addChild(layers[i].getXMLEntity());
		}
		return xml;
	}

	public void setPrintingProperties(PrintAttributes printProperties) {
		// TODO Implement it
		throw new Error("Not yet implemented!");

	}

	public String getClassName() {
		return getClass().getName();
	}

	public void setXMLEntity(XMLEntity xml) {
		setIsShapeVisible(xml.getBooleanProperty("isShapeVisible"));
		setDescription(xml.getStringProperty("desc"));
		for (int i = 0; i < xml.getChildrenCount(); i++) {
			addLayer(SymbologyFactory.createSymbolFromXML(xml.getChild(i), "layer" + i));
		}

		if (xml.contains("unit")) { // remove this line when done
		// measure unit (for outline)
		setUnit(xml.getIntProperty("unit"));

		// reference system (for outline)
		setReferenceSystem(xml.getIntProperty("referenceSystem"));
		}
	}

	public void print(Graphics2D g, AffineTransform at, Geometry geom, PrintAttributes properties) {
		for (int i = 0; i < layers.length; i++) {
			layers[i].print(g, at, geom, properties);
		}
	}

	public void setLayer(int index, ISymbol layer) throws IndexOutOfBoundsException {
		layers[index] = (IFillSymbol) layer;
	}

	public void swapLayers(int index1, int index2) {
		ISymbol aux1 = getLayer(index1), aux2 = getLayer(index2);
		layers[index2] = (IFillSymbol) aux1;
		layers[index1] = (IFillSymbol) aux2;
	}

	public ISymbol getLayer(int layerIndex) {
//		try{
			return layers[layerIndex];
//		} catch (Exception e) {
//			return null;
//		}
	}

	public int getLayerCount() {
		return layers.length;
	}

	public void addLayer(ISymbol newLayer) {
		addLayer(newLayer, layers.length);
	}

	public void addLayer(ISymbol newLayer, int layerIndex) throws IndexOutOfBoundsException {
		if (newLayer == null ) {
			/*|| newLayer instanceof ILabelStyle)*/ return; // null or symbols that are styles are not allowed
		}

		selectionSymbol = null; /* forces the selection symbol to be re-created
		 						 * next time it is required
		 						 */
		if (layerIndex < 0 || layers.length < layerIndex) {
			throw new IndexOutOfBoundsException(layerIndex+" < 0 or "+layerIndex+" > "+layers.length);
		}
		ArrayList newLayers = new ArrayList(); //<ISymbol>
		for (int i = 0; i < layers.length; i++) {
			newLayers.add(layers[i]);
		}
		try {
			newLayers.add(layerIndex, newLayer);
			layers = (IFillSymbol[]) newLayers.toArray(new IFillSymbol[0]);
		} catch (ArrayStoreException asEx) {
			throw new ClassCastException(newLayer.getClassName()+" is not an IFillSymbol");
		}
	}

	public boolean removeLayer(ISymbol layer) {

		int capacity = 0;
		capacity = layers.length;
		ArrayList lst = new ArrayList(capacity); //<IFillSymbol>
		for (int i = 0; i < capacity; i++) {
			lst.add(layers[i]);
		}
		boolean contains = lst.remove(layer);
		layers = (IFillSymbol[])lst.toArray(new IFillSymbol[0]);
		return contains;
	}

	public void setUnit(int unitIndex) {
		super.setUnit(unitIndex);
		for (int i = 0; i < layers.length; i++) {
			layers[i].setUnit(unitIndex);
		}
	}

	public void setReferenceSystem(int system) {
		super.setReferenceSystem(system);
		for (int i = 0; i < layers.length; i++) {
			layers[i].setReferenceSystem(system);
		}
	}

	/**
	 *Returns the transparency of the multi layer fill symbol created
	 */
	public int getFillAlpha() {
		// will compute the acumulated opacity
		double myAlpha = 0;
		for (int i = 0; i < layers.length; i++) {
			double layerAlpha = layers[i].getFillAlpha()/255D;
			myAlpha += (1-myAlpha)*layerAlpha;
		}
		int result = (int) Math.round(myAlpha * 255);
		return (result>255) ? 255 : result;
	}


	public double toCartographicSize(ViewPort viewPort, double dpi, Geometry geom) {
		double size = 0;
		for (int i = 0; i < layers.length; i++) {
			size = Math.max(size, layers[i].toCartographicSize(viewPort, dpi, geom));
		}
		return size;
	}
}
