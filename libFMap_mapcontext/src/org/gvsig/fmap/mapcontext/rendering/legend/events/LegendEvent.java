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
package org.gvsig.fmap.mapcontext.rendering.legend.events;

import org.gvsig.fmap.mapcontext.events.FMapEvent;
import org.gvsig.fmap.mapcontext.layers.operations.ClassifiableVectorial;




/**
 * <p><code>LegendEvent</code> represents an event produced at a legend in a layer.</p>
 * 
 * @see FMapEvent
 */
public class LegendEvent extends FMapEvent {
	/**
	 * <p>Identifies this event as a change of the value of a legend.</p>
	 */
	public static final int LEGEND_VALUE_CHANGED = 200;

	/**
	 * <p>Identifies this event as a change of the symbol of a legend.</p>
	 */
	public static final int LEGEND_SYMBOL_CHANGED = 201;

	/**
	 * <p>Identifies this event as a legend interval change.</p>
	 */
	public static final int LEGEND_INTERVAL_CHANGED = 202;

	/**
	 * <p>Identifies this event as a clear of a legend.</p>
	 */
	public static final int LEGEND_CLEARED = 203;

	/**
	 * <p>Identifies this event as a change on the classification of a legend.</p>
	 */
	public static final int LEGEND_CLASSIFICATION_CHANGED = 204;

	/**
	 * Layer of the legend where the event was produced.
	 * 
	 * @see #getLayer()
	 * @see #setLayer(ClassifiableVectorial)
	 */
	private ClassifiableVectorial layer;

	/**
	 * <p>Returns the layer of the legend where the event was produced, as a classifiable and vector layer.</p>
	 *
	 * @return the layer where the event was produced, as classifiable vector
	 * 
	 * @see #setLayer(ClassifiableVectorial)
	 */
	public ClassifiableVectorial getLayer() {
		return layer;
	}

	/**
	 * <p>Sets the layer of the legend where the event was produced, as a classifiable and vector layer.</p>
	 *
	 * @param layer the layer where the event was produced, as classifiable vector
	 * 
	 * @see #getLayer()
	 */
	public void setLayer(ClassifiableVectorial layer) {
		this.layer = layer;
	}
}
