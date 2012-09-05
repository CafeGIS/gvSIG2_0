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
 * <p>Event produced when changes the visibility, activation, edition status, or the name of a layer.</p>
 *
 * @author Fernando González Cortés
 */
public class LayerEvent extends FMapEvent {
	/**
	 * <p>String that identifies the property that has changed.</p>
	 */
	private String property;

	/**
	 * <p>Layer affected by the action.</p>
	 */
	private FLayer source;

	/**
	 * <p>Identifies this event as a action of change of the visibility status of a layer.</p>
	 */
	public static final int VISIBILITY_CHANGED = 0;

	/**
	 * <p>Identifies this event as a action of change of the activation status of a layer.</p>
	 */
	public static final int ACTIVATION_CHANGED = 1;
	
	/**
	 * <p>Identifies this event as a action of a change of the name of a layer.</p>
	 */
	public static final int NAME_CHANGED = 2;
	
	/**
	 * <p>Identifies this event as a action of change of the edition status of a layer.</p>
	 */
	public static final int EDITION_CHANGED = 3;

	/**
	 * <p>Identifies this event as a action of change of any property that affects to draw of the layer.</p>
	 */
	public static final int DRAW_VALUES_CHANGED = 4;

	/**
	 * <p>Creates a new layer event notifying a "visibility changed" action.</p>
	 * 
	 * @param default1 layer affected by the action
	 * @param property property that has changed
	 * 
	 * @return a new layer event
	 */
	public static LayerEvent createVisibilityChangedEvent(FLyrDefault default1, String property){
		return new LayerEvent(default1, property, VISIBILITY_CHANGED);
	}

	/**
	 * <p>Creates a new layer event notifying an "activation changed" action.</p>
	 * 
	 * @param default1 layer affected by the action
	 * @param property property that has changed
	 * 
	 * @return a new layer event
	 */
	public static LayerEvent createActivationChangedEvent(FLyrDefault default1, String property){
		return new LayerEvent(default1, property, ACTIVATION_CHANGED);
	}
	
	public static LayerEvent createDrawValuesChangedEvent(FLyrDefault default1, String property){
		return new LayerEvent(default1, property, DRAW_VALUES_CHANGED);
	}

	/**
	 * <p>Creates a new layer event notifying an action of "change of the name of a layer".</p>
	 * 
	 * @param default1 layer affected by the action
	 * @param property property that has changed
	 * 
	 * @return a new layer event
	 */	
	public static LayerEvent createNameChangedEvent(FLyrDefault default1, String property){
		return new LayerEvent(default1, property, NAME_CHANGED);
	}
	
	/**
	 * <p>Creates a new layer event notifying an "edition changed" action.</p>
	 * 
	 * @param default1 layer affected by the action
	 * @param property property that has changed
	 * 
	 * @return a new layer event
	 */
	public static LayerEvent createEditionChangedEvent(FLyrDefault default1, String property){
		return new LayerEvent(default1, property, EDITION_CHANGED);
	}
	
	/**
	 * <p>Creates a new layer event of the specified type.</p>
	 * 
	 * @param default1 layer affected by the action
	 * @param property property that has changed
	 * @param eventType type of layer collection event
	 * 
	 * @return a new layer collection event
	 */
	private LayerEvent(FLayer default1, String property, int eventType) {
		source = default1;
		this.property = property;
		setEventType(eventType);
	}

	/**
	 * <p>Gets the layer affected.</p>
	 *
	 * @return the layer affected
	 */
	public FLayer getSource() {
		return source;
	}

	/**
	 * <p>Sets the layer affected.</p>
	 *
	 * @param the layer affected
	 */
	public void setSource(FLayer source) {
		this.source = source;
	}

	/**
	 * <p>Gets the property that has changed.</p>
	 *
	 * @return the property that has changed
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * <p>Sets the property that has changed.</p>
	 *
	 * @param the property that has changed
	 */
	public void setProperty(String property) {
		this.property = property;
	}
}
