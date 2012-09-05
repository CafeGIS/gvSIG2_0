/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
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
package org.gvsig.fmap.mapcontext.layers.operations;

import java.awt.Graphics2D;

import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.mapcontext.exceptions.LoadLayerException;
import org.gvsig.fmap.mapcontext.layers.CancelationException;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.LayerCollectionListener;



/**
 * <p>Interface with methods for a collection of layers.</p>
 */
public interface LayerCollection extends LayersVisitable {
	/**
	 * <p>Adds a listener of events produced on a collection of layers.</p>
	 *
	 * @param listener a <code>LayerCollectionListener</code>
	 *
	 * @see #removeLayerCollectionListener(LayerCollectionListener)
	 */
	public void addLayerCollectionListener(LayerCollectionListener listener);

	/**
	 * <p>Removes a listener of events produced on a collection of layers.</p>
	 *
	 * @param listener a <code>LayerCollectionListener</code>
	 *
	 * @see #addLayerCollectionListener(LayerCollectionListener)
	 */
	public void removeLayerCollectionListener(LayerCollectionListener listener);

	/**
	 * <p>Adds a new layer to this collection.</p>
	 *
	 * @param layer the new layer
	 *
	 * @throws CancelationException any exception produced during the cancellation of the driver.
	 * @throws LoadLayerException any exception produced loading the layer.
	 *
	 * @see #removeLayer(FLayer)
	 * @see #removeLayer(int)
	 * @see #removeLayer(String)
	 */
	public void addLayer(FLayer layer) throws CancelationException, LoadLayerException;

	/**
	 * <p>Moves a layer of the collection to another position in internal list. It doesn't consider sub-nodes of layers.</p>
	 *
	 * @param from origin position
	 * @param to destination position
	 *
	 * @throws CancelationException any exception produced during the cancellation of the driver.
	 */
	public void moveTo(int from, int to) throws CancelationException;

	/**
	 * <p>Removes a layer from this collection.</p>
	 *
	 * @param lyr a layer
	 *
	 * @throws CancelationException any exception produced during the cancellation of the driver.
	 *
	 * @see #removeLayer(int)
	 * @see #removeLayer(String)
	 * @see #addLayer(FLayer)
	 */
	public void removeLayer(FLayer lyr) throws CancelationException;

	/**
	 * <p>Removes a layer using its identifier.</p>
	 *
	 * @param idLayer layer identifier
	 *
	 * @see #removeLayer(FLayer)
	 * @see #removeLayer(String)
	 * @see #addLayer(FLayer)
	 */
	public void removeLayer(int idLayer);

	/**
	 * <p>Removes a layer using its name.</p>
	 *
	 * @param layerName the name of the layer
	 *
	 * @see #removeLayer(FLayer)
	 * @see #removeLayer(int)
	 * @see #addLayer(FLayer)
	 */
	public void removeLayer(String layerName);

	/**
	 * <p>Returns an array with all visible layers in this collection.</p>
	 *
	 * @return array with first-level visible layer nodes, or <code>null</code> if no there is no layer visible
	 *
	 * @see #setAllVisibles(boolean)
	 */
	public FLayer[] getVisibles();

	/**
	 * <p>Returns an array with all active layers in this collection.</p>
	 *
	 * @return array with first-level active layer nodes, or <code>null</code> if no there is no layer actived
	 *
	 * @see #setAllActives(boolean)
	 */
	public FLayer[] getActives();

	/**
	 * <p>Returns the ith-output directed son (from bottom up) of this collection.</p>
	 *
	 * @param index index of the ith-output layer in this collection.
	 *
	 * @return a layer
	 *
	 * @see #getLayer(String)
	 */
	public FLayer getLayer(int index);

	/**
	 * <p>Returns a first-level layer of this collection, using its name.</p>
	 *
	 * @param layerName name of a layer of this collection
	 *
	 * @return a layer
	 *
	 * @see #getLayer(int)
	 */
	public FLayer getLayer(String layerName);

	/**
	 * <p>Returns the number of layers that are at the first level inside this one.</p>
	 *
	 * <p>Doesn't counts the sublayers (of <code>FLayers</code> subnodes).</p>
	 *
	 * @return number >= 0 with layers that are at the same first-level
	 *
	 * @see #getLayer(int)
	 * @see #getLayer(String)
	 */
	public int getLayersCount();

	/**
	 * <p>Changes the status of all layers to active or inactive.</p>
	 *
	 * @param active a boolean value
	 *
	 * @see #getActives()
	 */
	public void setAllActives(boolean active);

	/**
	 * <p>Changes the status of all layers to visible or invisible.</p>
	 *
	 * @param visible a boolean value
	 *
	 * @see #getVisibles()
	 */
	public void setAllVisibles(boolean visible);


	public void beginDraw(Graphics2D g, ViewPort viewPort);
	public void endDraw(Graphics2D g, ViewPort viewPort);

}
