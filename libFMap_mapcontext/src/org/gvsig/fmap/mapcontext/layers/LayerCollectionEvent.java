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
package org.gvsig.fmap.mapcontext.layers;

import org.gvsig.fmap.mapcontext.events.FMapEvent;


/**
 * <p>Event produced when a layer is been added or removed, or has been added or
 *  removed, or its visibility or activation state has changed from a collection of layers.</p>
 *
 * @author Vicente Caballero Navarro
 */
public class LayerCollectionEvent extends FMapEvent {
	/**
	 * <p>Identifies this event as a action on a layer that has been added.</p>
	 */
	private final static int LAYER_ADDED = 0;

	/**
	 * <p>Identifies this event as a action on a layer that has been removed.</p>
	 */
	private final static int LAYER_REMOVED = 2;

	/**
	 * <p>Identifies this event as a action on a layer that is being added.</p>
	 */
	private final static int LAYER_ADDING = 3;

	/**
	 * <p>Identifies this event as a action on a layer that is being removed.</p>
	 */
	private final static int LAYER_REMOVING = 5;
	
	/**
	 * <p>Identifies this event as a action of change of the activation status of a layer.</p>
	 */
	private final static int LAYER_ACTIVATION_CHANGED = 6;
	
	/**
	 * <p>Identifies this event as a action of change of the visibility status of a layer.</p>
	 */
	private final static int LAYER_VISIBILITY_CHANGED = 7;

	/**
	 * <p>Reference to the collection of layers.</p>
	 */
	private FLayers layers;

	/**
	 * <p>Reference to the layer which this layer notifies.</p>
	 */
	private FLayer affected;

	/**
	 * <p>Creates a new layer collection event notifying a "layer added" action.</p>
	 * 
	 * @param lyr layer affected by the action
	 * 
	 * @return a new layer collection event
	 */
	public static LayerCollectionEvent createLayerAddedEvent(FLayer lyr){
		return new LayerCollectionEvent(lyr, LAYER_ADDED);
	}

	/**
	 * <p>Creates a new layer collection event notifying a "layer removed" action.</p>
	 * 
	 * @param lyr layer affected by the action
	 * 
	 * @return a new layer collection event
	 */
	public static LayerCollectionEvent createLayerRemovedEvent(FLayer lyr){
		return new LayerCollectionEvent(lyr, LAYER_REMOVED);
	}

	/**
	 * <p>Creates a new layer collection event notifying a "layer adding" action.</p>
	 * 
	 * @param lyr layer affected by the action
	 * 
	 * @return a new layer collection event
	 */
	public static LayerCollectionEvent createLayerAddingEvent(FLayer lyr){
		return new LayerCollectionEvent(lyr, LAYER_ADDING);
	}

	/**
	 * <p>Creates a new layer collection event notifying a "layer removing" action.</p>
	 * 
	 * @param lyr layer affected by the action
	 * 
	 * @return a new layer collection event
	 */
	public static LayerCollectionEvent createLayerRemovingEvent(FLayer lyr){
		return new LayerCollectionEvent(lyr, LAYER_REMOVING);
	}

	/**
	 * <p>Creates a new layer collection event notifying a "layer activation changed" action.</p>
	 * 
	 * @param lyr layer affected by the action
	 * 
	 * @return a new layer collection event
	 */
	public static LayerCollectionEvent createLayerActivationEvent(FLayer lyr){
		return new LayerCollectionEvent(lyr, LAYER_ACTIVATION_CHANGED);
	}

	/**
	 * <p>Creates a new layer collection event notifying a "layer visibility changed" action.</p>
	 * 
	 * @param lyr layer affected by the action
	 * 
	 * @return a new layer collection event
	 */
	public static LayerCollectionEvent createLayerVisibilityEvent(FLayer lyr){
		return new LayerCollectionEvent(lyr, LAYER_VISIBILITY_CHANGED);
	}
	
	/**
	 * <p>Creates a new layer collection event of the specified type.</p>
	 * 
	 * @param lyr layer affected by the action
	 * @param eventType type of layer collection event
	 * 
	 * @return a new layer collection event
	 */
	protected LayerCollectionEvent(FLayer lyr, int eventType) {
		layers = lyr.getParentLayer();
		affected = lyr;
		setEventType(eventType);
	}

	/**
	 * <p>Gets the collection of layers which the layer affected belongs.</p>
	 *
	 * @return the collection of layers affected
	 */
	public FLayers getLayers() {
		return layers;
	}

	/**
	 * <p>Gets the layer that this event references.</p>
	 *
	 * @return the layer that this event references
	 */
	public FLayer getAffectedLayer() {
		return affected;
	}
}
