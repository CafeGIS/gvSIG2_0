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


/**
 * <p>Event produced when a layer has been or is being moved from a collection of layers.</p>
 *
 * @author Vicente Caballero Navarro
 */
public class LayerPositionEvent extends LayerCollectionEvent {
	/**
	 * <p>If this event is a <i>LAYER_MOVED</i> type, stores the previous index of the affected layer in the layer
	 *  collection; otherwise its value will be the same as the <code>newPos</code> one.</p>
	 */
	private int oldPos;

	/**
	 * <p>The new index in the layer collection of the layer affected.</p>
	 */
	private int newPos;

	/**
	 * <p>Identifies this event as a action on a layer that is being moved.</p>
	 */
	private final static int LAYER_MOVING = 4;

	/**
	 * <p>Identifies this event as a action on a layer that has been moved.</p>
	 */
	private final static int LAYER_MOVED = 1;

	/**
	 * <p>Creates a new layer position event notifying a "layer moved" action.</p>
	 * 
	 * @param lyr layer affected by the action
	 * @param oldp previous index of the affected layer in the layer collection
	 * @param newp new index of the affected layer in the layer collection
	 * 
	 * @return a new layer position event
	 */
	public static LayerPositionEvent createLayerMovedEvent(FLayer lyr, int oldp, int newp){
		return new LayerPositionEvent(lyr, oldp, newp, LAYER_MOVED);
	}

	/**
	 * <p>Creates a new layer position event notifying a "layer moving" action.</p>
	 * 
	 * @param lyr layer affected by the action
	 * @param oldp previous index of the affected layer in the layer collection
	 * @param newp new index of the affected layer in the layer collection
	 * 
	 * @return a new layer position event
	 */
	public static LayerPositionEvent createLayerMovingEvent(FLayer lyr, int oldp, int newp){
		return new LayerPositionEvent(lyr, oldp, newp, LAYER_MOVING);
	}

	/**
	 * <p>Creates a new layer position event of the specified type.</p>
	 * 
	 * @param lyr layer affected by the action
	 * @param oldp previous index of the affected layer in the layer collection
	 * @param newp new index of the affected layer in the layer collection
	 * @param eventType type of layer collection event
	 * 
	 * @return a new layer position event
	 */
	private LayerPositionEvent(FLayer lyr, int oldp, int newp, int eventType) {
		super(lyr, eventType);
		oldPos = oldp;
		newPos = newp;
	}

	/**
	 * <p>Gets the previous index of the affected layer in the layer collection.</p>
	 *
	 * @return the previous index
	 */
	public int getOldPos() {
		return oldPos;
	}

	/**
	 * <p>Gets the new index of the affected layer in the layer collection.</p>
	 *
	 * @return the new index
	 */
	public int getNewPos() {
		return newPos;
	}
}
