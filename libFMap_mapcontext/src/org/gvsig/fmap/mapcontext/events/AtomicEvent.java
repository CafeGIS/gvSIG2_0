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
package org.gvsig.fmap.mapcontext.events;

import java.util.ArrayList;
import java.util.Iterator;

import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.layers.LayerCollectionEvent;
import org.gvsig.fmap.mapcontext.layers.LayerEvent;
import org.gvsig.fmap.mapcontext.layers.SelectionEvent;
import org.gvsig.fmap.mapcontext.rendering.legend.events.LegendEvent;




/**
 * <p>An atomic event represents a group of events that will be attended without any interruption.</p>
 * 
 * <p>This kind of events are created by the buffer of events of the {@link MapContext MapContext}.</p>
 */
public class AtomicEvent extends FMapEvent {
	/**
	 * <p>Events that constitute this one.</p>
	 */
	private ArrayList events;

	/**
	 * <p>Creates a new instance of this kind of event.</p>
	 *
	 * @param fmapEvents events that will constitute this one
	 */
	public AtomicEvent(ArrayList fmapEvents) {
		this.events = (ArrayList) fmapEvents.clone();
	}

	/**
	 * <p>Returns the event at the specified position in the internal list.</p>
	 *
	 * @param index index of event to return
	 *
	 * @return event at the specified position in this list
	 */
	public FMapEvent getEvent(int index) {
		return (FMapEvent) events.get(index);
	}

	/**
	 * <p>Returns the number of events that constitute this one.</p>
	 *
	 * @return number of events that constitute this one
	 */
	public int getEventCount() {
		return events.size();
	}

	/**
	 * <p>Returns all legend events that constitute this one.</p>
	 *
	 * @return an array with all legend events that constitute this one
	 */
	public LegendEvent[] getLegendEvents() {
		ArrayList ret = new ArrayList();

		for (Iterator iter = events.iterator(); iter.hasNext();) {
			FMapEvent event = (FMapEvent) iter.next();

			if (event instanceof LegendEvent) {
				ret.add(event);
			}
		}

		return (LegendEvent[]) ret.toArray(new LegendEvent[0]);
	}

	/**
	 * <p>Returns all layer collection events that constitute this one.</p>
	 *
	 * @return an array with all layer collection events that constitute this one
	 */
	public LayerCollectionEvent[] getLayerCollectionEvents() {
		ArrayList ret = new ArrayList();

		for (Iterator iter = events.iterator(); iter.hasNext();) {
			FMapEvent event = (FMapEvent) iter.next();

			if (event instanceof LayerCollectionEvent) {
				ret.add(event);
			}
		}

		return (LayerCollectionEvent[]) ret.toArray(new LayerCollectionEvent[0]);
	}

	/**
	 * <p>Returns all vector layer selection events that constitute this one.</p>
	 *
	 * @return an array with all vector layer selection events that constitute this one
	 */
	public SelectionEvent[] getSelectionEvents() {
		ArrayList ret = new ArrayList();

		for (Iterator iter = events.iterator(); iter.hasNext();) {
			FMapEvent event = (FMapEvent) iter.next();

			if (event instanceof SelectionEvent) {
				ret.add(event);
			}
		}

		return (SelectionEvent[]) ret.toArray(new SelectionEvent[0]);
	}

	/**
	 * <p>Returns all extent events that constitute this one.</p>
	 *
	 * @return an array with all extent events that constitute this one
	 */
	public ExtentEvent[] getExtentEvents() {
		ArrayList ret = new ArrayList();

		for (Iterator iter = events.iterator(); iter.hasNext();) {
			FMapEvent event = (FMapEvent) iter.next();

			if (event instanceof ExtentEvent) {
				ret.add(event);
			}
		}

		return (ExtentEvent[]) ret.toArray(new ExtentEvent[0]);
	}

	/**
	 * <p>Returns all layer events that constitute this one.</p>
	 *
	 * @return an array with all layer events that constitute this one
	 */
	public LayerEvent[] getLayerEvents() {
		ArrayList ret = new ArrayList();

		for (Iterator iter = events.iterator(); iter.hasNext();) {
			FMapEvent event = (FMapEvent) iter.next();

			if (event instanceof LayerEvent) {
				ret.add(event);
			}
		}

		return (LayerEvent[]) ret.toArray(new LayerEvent[0]);
	}

	/**
	 * <p>Returns all color events that constitute this one.</p>
	 *
	 * @return an array with all color events that constitute this one
	 */
	public ColorEvent[] getColorEvents() {
		ArrayList ret = new ArrayList();

		for (Iterator iter = events.iterator(); iter.hasNext();) {
			FMapEvent event = (FMapEvent) iter.next();

			if (event instanceof ColorEvent) {
				ret.add(event);
			}
		}

		return (ColorEvent[]) ret.toArray(new ColorEvent[0]);
	}

	/**
	 * <p>Returns all projection events that constitute this one.</p>
	 *
	 * @return an array with all projection events that constitute this one
	 */
	public ProjectionEvent[] getProjectionEvents() {
		ArrayList ret = new ArrayList();

		for (Iterator iter = events.iterator(); iter.hasNext();) {
			FMapEvent event = (FMapEvent) iter.next();

			if (event instanceof ProjectionEvent) {
				ret.add(event);
			}
		}

		return (ProjectionEvent[]) ret.toArray(new ProjectionEvent[0]);
	}
}
