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
* $Id: MultiLayerLineSymbol.java 15675 2007-10-30 16:41:53Z jdominguez $
* $Log$
* Revision 1.18  2007-09-21 12:25:32  jaume
* cancellation support extended down to the IGeometry and ISymbol level
*
* Revision 1.17  2007/09/18 07:30:15  cesar
* Revert last change
*
* Revision 1.15  2007/09/17 15:26:24  jaume
* *** empty log message ***
*
* Revision 1.14  2007/09/17 14:16:11  jaume
* multilayer symbols sizing bug fixed
*
* Revision 1.13  2007/09/17 09:33:47  jaume
* some multishapedsymbol bugs fixed
*
* Revision 1.12  2007/08/09 08:04:48  jvidal
* javadoc
*
* Revision 1.11  2007/07/23 06:52:55  jaume
* Added support for arrow line decorator (start commiting)
*
* Revision 1.10  2007/07/18 06:54:34  jaume
* continuing with cartographic support
*
* Revision 1.9  2007/07/03 10:58:29  jaume
* first refactor on CartographicSupport
*
* Revision 1.8  2007/06/29 13:07:01  jaume
* +PictureLineSymbol
*
* Revision 1.7  2007/05/17 09:32:06  jaume
* *** empty log message ***
*
* Revision 1.6  2007/05/08 08:47:40  jaume
* *** empty log message ***
*
* Revision 1.5  2007/04/17 07:01:53  bsanchez
* - Corregido fallo de Double.MIN_VALUE por Double.NEGATIVE_INFINITY comentado por Victor Olaya.
*
* Revision 1.4  2007/03/26 14:26:02  jaume
* implemented Print
*
* Revision 1.3  2007/03/20 16:01:21  jaume
* *** empty log message ***
*
* Revision 1.2  2007/03/09 11:20:56  jaume
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
import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.mapcontext.rendering.symbols.styles.ILineStyle;
import org.gvsig.tools.task.Cancellable;

import com.iver.utiles.XMLEntity;
import com.iver.utiles.XMLException;
/**
 * MultiLayerLineSymbol allows to create new symbols using a composition of several lineal
 * symbols (xxxLineSymbol implementing ILineSymbol)and be treated as an only one symbol.
 *
 * @author  jaume dominguez faus - jaume.dominguez@iver.es
 */
public class MultiLayerLineSymbol extends AbstractLineSymbol implements
		ILineSymbol, IMultiLayerSymbol {
	private ILineSymbol[] layers = new ILineSymbol[0];
	private MultiLayerLineSymbol selectionSymbol;
	private double lineWidth;

	public Color getColor() {
		/*
		 * a multilayer symbol does not define any color, the color
		 * of each layer is defined by the layer itself
		 */
		return null;
	}

	public ILineStyle getLineStyle() {
		/*
		 * a multilayer symbol does not define any style, the style
		 * of each layer is defined by the layer itself
		 */
		return null;
	}

	public double getLineWidth() {

		double myLineWidth = 0;
		for (int i = 0; i < getLayerCount(); i++) {
			myLineWidth = Math.max(myLineWidth, ((ILineSymbol) getLayer(i)).getLineWidth());
		}

		if (lineWidth != myLineWidth) {
			lineWidth = myLineWidth;
		}
		return lineWidth;
	}

	public void setLineColor(Color color) {
		/*
		 * will apply the color to each layer
		 */
		for (int i = 0; i < layers.length; i++) {
			layers[i].setLineColor(color);
		}
	}

	public void setLineStyle(ILineStyle lineStyle) {
		/*
		 * will apply the same patter to each layer
		 */
		for (int i = 0; i < layers.length; i++) {
			layers[i].setLineStyle(lineStyle);
		}
	}

	public void setLineWidth(double width) {

		if (width > 0 && width != getLineWidth()) {
			double scaleFactor = width / getLineWidth();
			this.lineWidth = width;
			for (int i = 0; i < layers.length; i++) {
				layers[i].setLineWidth(layers[i].getLineWidth()*scaleFactor);
			}
		}
	}

	public void draw(Graphics2D g, AffineTransform affineTransform, Geometry geom, Cancellable cancel) {
		for (int i = 0; (cancel==null || !cancel.isCanceled()) && i < layers.length; i++) {
			layers[i].draw(g, affineTransform, geom, cancel);
		}
	}

	public void drawInsideRectangle(Graphics2D g, AffineTransform scaleInstance, Rectangle r, PrintAttributes properties) throws SymbolDrawingException {
		for (int i = 0; i < layers.length; i++) {
			layers[i].drawInsideRectangle(g, scaleInstance, r, properties);
		}
	}

	public int getOnePointRgb() {
		// will paint only the last layer pixel
		return layers[layers.length-1].getOnePointRgb();
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
			selectionSymbol = new MultiLayerLineSymbol();
			selectionSymbol.setDescription(getDescription());
			for (int i = 0; i < layers.length; i++) {
				selectionSymbol.addLayer(layers[i].getSymbolForSelection());
			}
		}
		return selectionSymbol;
	}


	public XMLEntity getXMLEntity() throws XMLException {
		XMLEntity xml = new XMLEntity();
		xml.putProperty("className", getClass().getName());
		xml.putProperty("isShapeVisible", isShapeVisible());
		xml.putProperty("desc", getDescription());
		xml.putProperty("lineWidth", getLineWidth());
		xml.putProperty("unit", getUnit());
		xml.putProperty("referenceSystem", getReferenceSystem());
		for (int i = 0; i < layers.length; i++) {
			xml.addChild(layers[i].getXMLEntity());
		}
		return xml;
	}

	public boolean isSuitableFor(Geometry geom) {
		return geom.getType() == Geometry.TYPES.CURVE;
	}

	public String getClassName() {
		return getClass().getName();
	}

	public void setXMLEntity(XMLEntity xml) {
		setIsShapeVisible(xml.getBooleanProperty("isShapeVisible"));
		setDescription(xml.getStringProperty("desc"));
		setLineWidth(xml.getDoubleProperty("lineWidth"));
		setUnit(xml.getIntProperty("unit"));
		setReferenceSystem(xml.getIntProperty("referenceSystem"));
		for (int i = 0; i < xml.getChildrenCount(); i++) {
			addLayer(SymbologyFactory.createSymbolFromXML(xml.getChild(i), "layer" + i));
		}
	}

	public void print(Graphics2D g, AffineTransform at, Geometry geom, PrintAttributes properties) {
		for (int i = 0; i < layers.length; i++) {
			layers[i].print(g, at, geom, properties);
		}

	}

	public void setLayer(int index, ISymbol layer) throws IndexOutOfBoundsException {
		layers[index] = (ILineSymbol) layer;
	}

	public void swapLayers(int index1, int index2) {
		ISymbol aux1 = getLayer(index1), aux2 = getLayer(index2);
		layers[index2] = (ILineSymbol) aux1;
		layers[index1] = (ILineSymbol) aux2;
	}

	public ISymbol getLayer(int layerIndex) {
		return layers[layerIndex];
	}

	public int getLayerCount() {
		return layers.length;
	}

	public void addLayer(ISymbol newLayer) {
		addLayer(newLayer, layers.length);
	}

	public void addLayer(ISymbol newLayer, int layerIndex) throws IndexOutOfBoundsException {

		if (newLayer == null) {
			return; // null are not allowed
		}
		ILineSymbol newLine = (ILineSymbol) newLayer;
		if (getLayerCount() == 0) {
			// apply the new layer properties to this multilayer

			setReferenceSystem(newLine.getReferenceSystem());
			lineWidth = newLine.getLineWidth();
			setUnit(newLine.getUnit());
		} else {
			if (newLine.getLineWidth() > getLineWidth()) {
				lineWidth = newLine.getLineWidth();
			}
			newLine.setReferenceSystem(getReferenceSystem());
			newLine.setUnit(getUnit());
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
			layers = (ILineSymbol[])newLayers.toArray(new ILineSymbol[0]);
		} catch (ArrayStoreException asEx) {
			throw new ClassCastException(newLayer.getClassName()+" is not an ILineSymbol");
		}
	}

	public boolean removeLayer(ISymbol layer) {

		int capacity = 0;
		capacity = layers.length;
		ArrayList lst = new ArrayList(capacity); //<ILineSymbol>
		for (int i = 0; i < capacity; i++) {
			lst.add(layers[i]);
		}
		boolean contains = lst.remove(layer);
		layers = (ILineSymbol[])lst.toArray(new ILineSymbol[0]);
		return contains;
	}

	public int getAlpha() {
		// will compute the acumulated opacity
		double myAlpha = 0;
		for (int i = 0; i < layers.length; i++) {
			double layerAlpha = layers[i].getAlpha()/255D;
			myAlpha += (1-myAlpha)*layerAlpha;
		}
		int result = (int) Math.round(myAlpha * 255);
		return (result>255) ? 255 : result;
	}

	public void setAlpha(int outlineAlpha) {
		// first, get the biggest alpha in the layers and the index if such layer
		int maxAlpha = Integer.MIN_VALUE;
		int maxAlphaLayerIndex = 0;
		for (int i = 0; i < layers.length; i++) {
			if (layers[i].getAlpha() > maxAlpha) {
				maxAlpha = layers[i].getAlpha();
				maxAlphaLayerIndex = i;
			}
		}

		// now, max alpha takes the value of the desired alpha and the rest
		// will take a scaled (to biggest alpha) alpha value
		for (int i = 0; i < layers.length; i++) {
			if (i!=maxAlphaLayerIndex) {
				double scaledAlpha = (double) layers[i].getAlpha()/maxAlpha;
				int myAlpha = (int) (outlineAlpha*scaledAlpha);
				if (myAlpha == 0) {
					myAlpha = 1;
				}
				layers[i].setAlpha(myAlpha);
			} else {
				int myAlpha = outlineAlpha;
				if (myAlpha == 0) {
					myAlpha = 1;
				}
				layers[i].setAlpha(myAlpha);
			}
		}

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

	public void setCartographicSize(double cartographicSize, Geometry geom) {
//		super.setCartographicSize(cartographicSize, shp);
		setLineWidth(cartographicSize);
	}
}
