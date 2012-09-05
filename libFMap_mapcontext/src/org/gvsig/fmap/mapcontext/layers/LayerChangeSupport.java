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

import java.util.ArrayList;
import java.util.Iterator;

import org.gvsig.fmap.mapcontext.rendering.legend.events.LegendChangedEvent;
import org.gvsig.fmap.mapcontext.rendering.legend.events.listeners.LegendListener;



/**
 * <p>Manages all legend listeners of a layer, notifying them any legend change event produced.</p>
 *
 * @author Vicente Caballero Navarro
 */
public class LayerChangeSupport {
	/**
	 * <p>The legend listeners of a layer.</p>
	 */
	private ArrayList listeners = new ArrayList();

	/**
	 * <p>Registers a <code>LegendListener</code>.</p>
	 * 
	 * @param listener the legend listener
	 */
	public void addLayerListener(LegendListener listener) {
		listeners.add(listener);
	}

	/**
	 * <p>Removes a registered <code>LegendListener</code>.</p>
	 *
	 * @param listener the legend listener
	 */
	public void removeLayerListener(LegendListener listener) {
		listeners.remove(listener);
	}

	/**
	 * <p>Notifies a legend change to all legend listeners registered.</p>
	 *
	 * @param e a legend event with the new legend and events that compound this one
	 */
	public void callLegendChanged(LegendChangedEvent e) {
		for (Iterator iter = listeners.iterator(); iter.hasNext();) {
			LegendListener listener = (LegendListener) iter.next();

			listener.legendChanged(e);
		}
	}
}
