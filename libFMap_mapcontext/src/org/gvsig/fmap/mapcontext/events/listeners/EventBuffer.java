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
package org.gvsig.fmap.mapcontext.events.listeners;

import java.util.ArrayList;
import java.util.Iterator;

import org.gvsig.fmap.mapcontext.events.AtomicEvent;
import org.gvsig.fmap.mapcontext.events.ColorEvent;
import org.gvsig.fmap.mapcontext.events.ExtentEvent;
import org.gvsig.fmap.mapcontext.events.ProjectionEvent;
import org.gvsig.fmap.mapcontext.layers.CancelationException;
import org.gvsig.fmap.mapcontext.layers.LayerCollectionEvent;
import org.gvsig.fmap.mapcontext.layers.LayerCollectionListener;
import org.gvsig.fmap.mapcontext.layers.LayerEvent;
import org.gvsig.fmap.mapcontext.layers.LayerListener;
import org.gvsig.fmap.mapcontext.layers.LayerPositionEvent;
import org.gvsig.fmap.mapcontext.layers.SelectionEvent;
import org.gvsig.fmap.mapcontext.layers.SelectionListener;
import org.gvsig.fmap.mapcontext.rendering.legend.events.LegendChangedEvent;
import org.gvsig.fmap.mapcontext.rendering.legend.events.listeners.LegendListener;




/**
 * <p><code>EventBuffer</code> represents a buffer of events that allows store listeners of events produced in layers
 *  of a <code>MapContext</code> instance, and configure its dispatching mode.</p>
 * 
 * <p>The <i>dispatching mode</i>:
 * <ul>
 *  <li><code>true</code> : dispatches each new event received.</li>
 *  <li><code>false</code> : accumulates all new events received in a internal buffer, and only will dispatch them
 *   (according to the order they were received) when changes the mode.</li>
 * </ul>
 * </p>
 *
 * @see LegendListener
 * @see LayerCollectionListener
 * @see SelectionListener
 * @see ViewPortListener
 * @see LegendListener
 *
 * @author Fernando González Cortés
 */
public class EventBuffer implements LegendListener, LayerCollectionListener,
SelectionListener, ViewPortListener, LayerListener {

	/**
	 * List with all events received and don't dispatched.
	 * 
	 * @see #endAtomicEvent()
	 * @see #legendChanged(LegendChangedEvent)
	 * @see #layerAdded(LayerCollectionEvent)
	 * @see #layerMoved(LayerPositionEvent)
	 * @see #layerRemoved(LayerCollectionEvent)
	 * @see #layerAdding(LayerCollectionEvent)
	 * @see #layerMoving(LayerPositionEvent)
	 * @see #layerRemoving(LayerCollectionEvent)
	 * @see #visibilityChanged(LayerCollectionEvent)
	 * @see #visibilityChanged(LayerEvent)
	 * @see #selectionChanged(SelectionEvent)
	 * @see #extentChanged(ExtentEvent)
	 * @see #activationChanged(LayerEvent)
	 * @see #nameChanged(LayerEvent)
	 * @see #backColorChanged(ColorEvent)
	 * @see #editionChanged(LayerEvent)
	 * @see #projectionChanged(ProjectionEvent)
	 * @see #fireAtomicEventListener
	 */
	private ArrayList events = new ArrayList();

	/**
	 * List with all listeners registered.
	 * 
	 * @see #addAtomicEventListener(AtomicEventListener)
	 * @see #removeAtomicEventListener(AtomicEventListener)
	 */ 
	private ArrayList listeners = new ArrayList();

	/**
	 * Allows enable or disable the <i>dispatching mode</i>.
	 * 
	 * @see #beginAtomicEvent()
	 * @see #endAtomicEvent()
	 */
	private boolean dispatching = true;

	/**
	 * <p>Enables buffer in <i>accumulation event</i> mode.<p>
	 * 
	 * <p>All new events received, will be accumulated and won't notified to their respective listeners,
	 *  until this buffer would received a call to {@link #endAtomicEvent() endAtomicEvent}.</p>
	 * 
	 * @see #endAtomicEvent()
	 */
	public void beginAtomicEvent() {
		dispatching = false;
	}

	/**
	 * <p>Disables buffer in <i>accumulation event</i> mode.</p>
	 * 
	 * <p>All events accumulated will be notify to their respective
	 *  listeners, in the same order as they arrived.</p>
	 *  
	 * @see #beginAtomicEvent()
	 */
	public void endAtomicEvent() {
		fireAtomicEventListener();
		events.clear();
		dispatching = true;
	}

	/*
	 * @see com.iver.cit.gvsig.fmap.layers.LegendListener#legendChanged(com.iver.cit.gvsig.fmap.rendering.LegendChangedEvent)
	 */
	public void legendChanged(LegendChangedEvent e) {
		events.add(e);

		if (dispatching) {
			fireAtomicEventListener();
		}
	}

	/*
	 * @see com.iver.cit.gvsig.fmap.layers.LayerCollectionListener#layerAdded(com.iver.cit.gvsig.fmap.layers.LayerCollectionEvent)
	 */
	public void layerAdded(LayerCollectionEvent e) {
		events.add(e);

		if (dispatching) {
			fireAtomicEventListener();
		}
	}

	/*
	 * @see com.iver.cit.gvsig.fmap.layers.LayerCollectionListener#layerMoved(com.iver.cit.gvsig.fmap.layers.LayerPositionEvent)
	 */
	public void layerMoved(LayerPositionEvent e) {
		events.add(e);

		if (dispatching) {
			fireAtomicEventListener();
		}
	}

	/*
	 * @see com.iver.cit.gvsig.fmap.layers.LayerCollectionListener#layerRemoved(com.iver.cit.gvsig.fmap.layers.LayerCollectionEvent)
	 */
	public void layerRemoved(LayerCollectionEvent e) {
		events.add(e);

		if (dispatching) {
			fireAtomicEventListener();
		}
	}

	/*
	 * @see com.iver.cit.gvsig.fmap.layers.LayerCollectionListener#layerAdding(com.iver.cit.gvsig.fmap.layers.LayerCollectionEvent)
	 */
	public void layerAdding(LayerCollectionEvent e) throws CancelationException {
		events.add(e);

		if (dispatching) {
			fireAtomicEventListener();
		}
	}

	/*
	 * @see com.iver.cit.gvsig.fmap.layers.LayerCollectionListener#layerMoving(com.iver.cit.gvsig.fmap.layers.LayerPositionEvent)
	 */
	public void layerMoving(LayerPositionEvent e) throws CancelationException {
		events.add(e);

		if (dispatching) {
			fireAtomicEventListener();
		}
	}

	/*
	 * @see com.iver.cit.gvsig.fmap.layers.LayerCollectionListener#layerRemoving(com.iver.cit.gvsig.fmap.layers.LayerCollectionEvent)
	 */
	public void layerRemoving(LayerCollectionEvent e)
	throws CancelationException {
		events.add(e);

		if (dispatching) {
			fireAtomicEventListener();
		}
	}

	/*
	 * @see com.iver.cit.gvsig.fmap.layers.LayerCollectionListener#visibilityChanged(com.iver.cit.gvsig.fmap.layers.LayerCollectionEvent)
	 */
	public void visibilityChanged(LayerCollectionEvent e)
	throws CancelationException {
		events.add(e);

		if (dispatching) {
			fireAtomicEventListener();
		}
	}

	/*
	 * @see com.iver.cit.gvsig.fmap.layers.SelectionListener#selectionChanged(com.iver.cit.gvsig.fmap.layers.SelectionEvent)
	 */
	public void selectionChanged(SelectionEvent e) {
		events.add(e);

		if (dispatching) {
			fireAtomicEventListener();
		}
	}

	/*
	 * @see com.iver.cit.gvsig.fmap.ViewPortListener#extentChanged(com.iver.cit.gvsig.fmap.ExtentEvent)
	 */
	public void extentChanged(ExtentEvent e) {
		events.add(e);

		if (dispatching) {
			fireAtomicEventListener();
		}
	}

	/**
	 * Appends, if wasn't, the specified listener to the end of the internal list of atomic event listeners.
	 *
	 * @param listener an object that implements the atomic event listener
	 *
	 * @return <code>true</code> if has added the listener successfully; otherwise <code>false</code>
	 * 
	 * @see #removeAtomicEventListener(AtomicEventListener)
	 * @see #fireAtomicEventListener()
	 */
	public boolean addAtomicEventListener(AtomicEventListener listener) {
		boolean bFound = false;
		for (int i=0; i < listeners.size(); i++)
			if (listeners.get(i) == listener)
				bFound = true;
		if (!bFound)
			listeners.add(listener);
		return true; 
	}

	/**
	 * <p>Removes a single instance of the {@link AtomicEventListener AtomicEventListener} from the
     * internal list, if it is present (optional operation). Returns <tt>true</tt>
     * if the list contained the specified element (or equivalently, if the list changed as a
     * result of the call).<p>
     *
     * @param o element to be removed from this list, if present
     * @return <tt>true</tt> if the list contained the specified element
     * 
     * @see #addAtomicEventListener(AtomicEventListener)
     * @see #fireAtomicEventListener()
	 */
	public boolean removeAtomicEventListener(AtomicEventListener listener) {
		return listeners.remove(listener);
	}

	/**
	 * Executes the {@linkplain AtomicEventListener#atomicEvent(AtomicEvent)} method of all listeners registered.
	 * 
	 * @see #addAtomicEventListener(AtomicEventListener)
	 * @see #removeAtomicEventListener(AtomicEventListener)
	 */
	private void fireAtomicEventListener() {
		if (events.size() == 0)
			return; // No hay eventos que lanzar.
		for (Iterator i = listeners.iterator(); i.hasNext();) {
			AtomicEventListener listener = (AtomicEventListener) i.next();
			AtomicEvent e = new AtomicEvent(events);
			listener.atomicEvent(e);
		}

		events.clear();
	}

	/*
	 * @see com.iver.cit.gvsig.fmap.layers.LayerListener#visibilityChanged(com.iver.cit.gvsig.fmap.layers.LayerEvent)
	 */
	public void visibilityChanged(LayerEvent e) {
		events.add(e);

		if (dispatching) {
			fireAtomicEventListener();
		}
	}

	/*
	 * @see com.iver.cit.gvsig.fmap.layers.LayerListener#activationChanged(com.iver.cit.gvsig.fmap.layers.LayerEvent)
	 */
	public void activationChanged(LayerEvent e) {
		events.add(e);

		if (dispatching) {
			fireAtomicEventListener();
		}
	}

	/*
	 * @see com.iver.cit.gvsig.fmap.layers.LayerListener#nameChanged(com.iver.cit.gvsig.fmap.layers.LayerEvent)
	 */
	public void nameChanged(LayerEvent e) {
		events.add(e);

		if (dispatching) {
			fireAtomicEventListener();
		}
	}

	/*
	 * @see com.iver.cit.gvsig.fmap.ViewPortListener#backColorChanged(com.iver.cit.gvsig.fmap.ColorEvent)
	 */
	public void backColorChanged(ColorEvent e) {
		events.add(e);

		if (dispatching) {
			fireAtomicEventListener();
		}
	}

	/*
	 * @see com.iver.cit.gvsig.fmap.layers.LayerListener#editionChanged(com.iver.cit.gvsig.fmap.layers.LayerEvent)
	 */
	public void editionChanged(LayerEvent e) {
		events.add(e);

		if (dispatching) {
			fireAtomicEventListener();
		}

	}

	/*
	 * @see com.iver.cit.gvsig.fmap.ViewPortListener#projectionChanged(com.iver.cit.gvsig.fmap.ProjectionEvent)
	 */
	public void projectionChanged(ProjectionEvent e) {
		events.add(e);

		if (dispatching) {
			fireAtomicEventListener();
		}
	}

	
	public void drawValueChanged(LayerEvent e) {
		events.add(e);

		if (dispatching) {
			fireAtomicEventListener();
		}
	}
}
