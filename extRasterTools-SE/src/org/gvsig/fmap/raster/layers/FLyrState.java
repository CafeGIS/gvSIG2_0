/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2007 IVER T.I. and Generalitat Valenciana.
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
 */
package org.gvsig.fmap.raster.layers;


/**
 * Estados de una capa. Cada capa tiene uno o varios estados activos en cada instante.
 * Hay estados no compatibles entre si, es decir, no pueden estar activos a la vez en cada
 * instante. Por ejemplo, una capa no puede estar cerrada y renderizando ya que sus datos no
 * están accesibles para esa operación. Los estados de una capa sir
 * @version 13/09/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class FLyrState implements ILayerState {	
	private boolean open                 = false;
	private boolean closed               = true;
	private boolean awake                = false;
	private boolean stopped              = false;
	//private boolean renderizing        = false;
	
	private int lastBeforeStop           = UNDEFINED;

	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.raster.layers.IState#isAwake()
	 */
	public boolean isAwake() {
		return awake;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.raster.layers.IState#enableAwake()
	 */
	public void enableAwake() throws NotAvailableStateException {
		if(open == true || closed == true || stopped == true) {
			this.awake = true;
			stopped = closed = open = false;
			lastBeforeStop = UNDEFINED;
		} else 
			throw new NotAvailableStateException("Awake state not available");
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.raster.layers.IState#isOpen()
	 */
	public boolean isOpen() {
		return open;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.raster.layers.IState#enableOpen()
	 */
	public void enableOpen() throws NotAvailableStateException {
		if(stopped == true || awake == true) {
			this.open = true;
			stopped = closed = awake = false;
			lastBeforeStop = UNDEFINED;
		} else 
			throw new NotAvailableStateException("Open state not available");
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.raster.layers.IState#isClosed()
	 */
	public boolean isClosed() {
		return closed;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.raster.layers.IState#enableClosed()
	 */
	public void enableClosed() throws NotAvailableStateException {
		if(open == true || awake == true || stopped == true) {
			this.closed = true;
			stopped = open = awake = false;
			lastBeforeStop = UNDEFINED;
		} else 
			throw new NotAvailableStateException("Closed state not available");
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.raster.layers.IState#isStopped()
	 */
	public boolean isStopped() {
		return stopped;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.raster.layers.IState#enableStopped()
	 */
	public void enableStopped() {
		if(open == true || awake == true || closed == true) {
			lastBeforeStop = (open == true) ? OPEN : ((closed == true) ? CLOSED : ((awake == true) ? AWAKE : UNDEFINED));
			if(lastBeforeStop != UNDEFINED) {
				stopped = true;
				closed = open = awake = false;
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.raster.layers.IState#disableStopped()
	 */
	public void disableStopped() {
		if(stopped == true && lastBeforeStop != UNDEFINED) {
			switch(lastBeforeStop) {
			case OPEN: open = true; closed = awake = false; break;
			case CLOSED: closed = true; awake = open = false; break;
			case AWAKE: awake = true; closed = open = false; break;
			}
			stopped = false;
			lastBeforeStop = UNDEFINED;
		} 
	}
	
}
