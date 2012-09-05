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
* $Id: MultiLayerMarkerSymbol.java 21071 2008-06-02 10:55:35Z vcaballero $
* $Log$
* Revision 1.17  2007-09-21 12:25:32  jaume
* cancellation support extended down to the IGeometry and ISymbol level
*
* Revision 1.16  2007/09/19 16:21:32  jaume
* removed unnecessary imports
*
* Revision 1.13  2007/09/17 14:16:11  jaume
* multilayer symbols sizing bug fixed
*
* Revision 1.12  2007/09/17 11:37:55  jaume
* fixed multilayermarkersymbol unit symbol
*
* Revision 1.11  2007/08/09 07:57:51  jvidal
* javadoc
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
* Revision 1.7  2007/06/07 06:50:40  jaume
* *** empty log message ***
*
* Revision 1.6  2007/05/08 08:47:40  jaume
* *** empty log message ***
*
* Revision 1.5  2007/04/17 07:01:53  bsanchez
* - Corregido fallo de Double.MIN_VALUE por Double.NEGATIVE_INFINITY comentado por Victor Olaya.
*
* Revision 1.4  2007/03/29 16:02:01  jaume
* *** empty log message ***
*
* Revision 1.3  2007/03/26 14:26:02  jaume
* implemented Print
*
* Revision 1.2  2007/03/09 11:20:57  jaume
* Advanced symbology (start committing)
*
* Revision 1.1.2.2  2007/02/21 07:34:08  jaume
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
import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.gvsig.compat.print.PrintAttributes;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.tools.task.Cancellable;

import com.iver.utiles.XMLEntity;
import com.iver.utiles.XMLException;
/**
 * MultiLayerMarkerSymbol allows to group several marker symbols (xxxMarkerSymbol
 * implementing IMarkerSymbol)in one and treat it as an only one symbol.
 *
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 */
public class MultiLayerMarkerSymbol extends AbstractMarkerSymbol implements IMarkerSymbol, IMultiLayerSymbol {
	private IMarkerSymbol[] layers = new IMarkerSymbol[0];
	private MultiLayerMarkerSymbol selectionSymbol;
	private Point2D offset = new Point2D.Double();
	private double markerSize;
	private double rotation;
	public Color getColor() {
		/*
		 * a multilayer symbol does not define any color, the color
		 * of each layer is defined by the layer itself
		 */
		return null;
	}

	public Point2D getOffset() {
		return offset;
	}

	public double getRotation() {
		return rotation;
	}


	public void setColor(Color color) {
		/*
		 * will apply the color to each layer
		 */
		for (int i = 0; i < layers.length; i++) {
			layers[i].setColor(color);
		}
	}

	public void setOffset(Point2D offset) {
		this.offset = offset;
	}

	public void setRotation(double rotation) {
		this.rotation = rotation;
	}

	public double getSize() {
		double myMarkerSize = 0;

		for (int i = 0; i < getLayerCount(); i++) {
			myMarkerSize = Math.max(myMarkerSize, ((IMarkerSymbol) getLayer(i)).getSize());
		}

		if (markerSize != myMarkerSize) {
			markerSize = myMarkerSize;
		}
		return markerSize;
	}

	public void setSize(double size) {
		if (size > 0 && size != getSize()) {

			double scale = size / getSize();
			this.markerSize = size;
			for (int i = 0; i < layers.length; i++) {
				double lSize = layers[i].getSize();
				layers[i].setSize(lSize*scale);
			}
		}
	}



	public void draw(Graphics2D g, AffineTransform affineTransform, Geometry geom, Cancellable cancel) {
		Point2D p = (Point2D) geom;
		g.rotate(rotation, p.getX(), p.getY());
		for (int i = 0; (cancel==null || !cancel.isCanceled()) && i < layers.length; i++) {
			layers[i].draw(g, affineTransform, geom, cancel);
		}
		g.rotate(-rotation, p.getX(), p.getY());
	}

	public void drawInsideRectangle(Graphics2D g, AffineTransform scaleInstance, Rectangle r, PrintAttributes properties) throws SymbolDrawingException {
		g.rotate(rotation, r.getCenterX(), r.getCenterY());
		for (int i = 0; i < layers.length; i++) {
			layers[i].drawInsideRectangle(g, scaleInstance, r, properties);
		}
		g.rotate(-rotation, r.getCenterX(), r.getCenterY());
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
			selectionSymbol = new MultiLayerMarkerSymbol();
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
		xml.putProperty("size",	getSize());
		xml.putProperty("unit", getUnit());
		xml.putProperty("referenceSystem", getReferenceSystem());
		for (int i = 0; i < layers.length; i++) {
			xml.addChild(layers[i].getXMLEntity());
		}
		return xml;
	}

	public boolean isSuitableFor(Geometry geom) {
		return geom.getType() == Geometry.TYPES.POINT;
	}


	public String getClassName() {
		return getClass().getName();
	}

	public void setXMLEntity(XMLEntity xml) {
		setIsShapeVisible(xml.getBooleanProperty("isShapeVisible"));
		setDescription(xml.getStringProperty("desc"));
		setSize(xml.getDoubleProperty("size"));
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
		layers[index] = (IMarkerSymbol) layer;
	}

	public void swapLayers(int index1, int index2) {
		ISymbol aux1 = getLayer(index1), aux2 = getLayer(index2);
		layers[index2] = (IMarkerSymbol) aux1;
		layers[index1] = (IMarkerSymbol) aux2;
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

		IMarkerSymbol newMarker = (IMarkerSymbol) newLayer;
		if (getLayerCount() == 0) {
			// apply the new layer properties to this multilayer

			setReferenceSystem(newMarker.getReferenceSystem());
			markerSize = newMarker.getSize();
			//setSize(newMarker.getSize());
			setUnit(newMarker.getUnit());
		} else {
			if (newMarker.getSize() > getSize()) {
				//setSize(newMarker.getSize());
				markerSize = newMarker.getSize();
			}
			newMarker.setReferenceSystem(getReferenceSystem());
			newMarker.setUnit(getUnit());
		}
		selectionSymbol = null; /* forces the selection symbol to be re-created
		 						 * next time it is required
		 						 */
		if (layerIndex < 0 || layers.length < layerIndex) {
			throw new IndexOutOfBoundsException(layerIndex+" < 0 or "+layerIndex+" > "+layers.length);
		}
		ArrayList newLayers = new ArrayList();
		for (int i = 0; i < layers.length; i++) {
			newLayers.add(layers[i]);
		}
		try {
			newLayers.add(layerIndex, newLayer);
			layers = (IMarkerSymbol[]) newLayers.toArray(new IMarkerSymbol[0]);
		} catch (ArrayStoreException asEx) {
			throw new ClassCastException(newLayer.getClassName()+" is not an IMarkerSymbol");
		}
	}

	public boolean removeLayer(ISymbol layer) {

		int capacity = 0;
		capacity = layers.length;
		ArrayList lst = new ArrayList(capacity);
		for (int i = 0; i < capacity; i++) {
			lst.add(layers[i]);
		}
		boolean contains = lst.remove(layer);
		layers = (IMarkerSymbol[])lst.toArray(new IMarkerSymbol[0]);
		return contains;
	}

	public void setUnit(int unit) {
		super.setUnit(unit);
		for (int i = 0; i < layers.length; i++) {
			layers[i].setUnit(unit);
		}
	}

	public void setReferenceSystem(int system) {
		super.setReferenceSystem(system);
		for (int i = 0; i < layers.length; i++) {
			layers[i].setReferenceSystem(system);
		}
	}

	public void setAlpha(int alpha) {
		// first, get the biggest alpha in the layers and the index if such layer
		int maxAlpha = Integer.MIN_VALUE;
		int maxAlphaLayerIndex = 0;
		for (int i = 0; i < layers.length; i++) {
			if (layers[i].getColor().getAlpha() > maxAlpha) {
				maxAlpha = layers[i].getColor().getAlpha();
				maxAlphaLayerIndex = i;
			}
		}

		// now, max alpha takes the value of the desired alpha and the rest
		// will take a scaled (to biggest alpha) alpha value
		for (int i = 0; i < layers.length; i++) {
			int r = layers[i].getColor().getRed();
			int g = layers[i].getColor().getGreen();
			int b = layers[i].getColor().getBlue();

			if (i!=maxAlphaLayerIndex) {
				double scaledAlpha = (double) layers[i].getColor().getAlpha()/maxAlpha;
				int myAlpha = (int) (alpha*scaledAlpha);
				if (myAlpha == 0) {
					myAlpha = 1;
				}
				layers[i].setColor(new Color(r, g, b, myAlpha));
			} else {
				int myAlpha = alpha;
				if (myAlpha == 0) {
					myAlpha = 1;
				}
				layers[i].setColor(new Color(r, g, b, myAlpha));
			}
		}

	}
}
