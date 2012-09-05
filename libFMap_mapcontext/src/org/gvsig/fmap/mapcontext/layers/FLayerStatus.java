/*
 * Created on 01-sep-2006
 *
 * gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
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
/* CVS MESSAGES:
*
* $Id: FLayerStatus.java 20989 2008-05-28 11:05:57Z jmvivo $
* $Log$
* Revision 1.2  2007-03-06 17:08:55  caballero
* Exceptions
*
* Revision 1.1  2006/09/21 17:23:39  azabala
* First version in cvs
*
*
*/
package org.gvsig.fmap.mapcontext.layers;

import java.util.ArrayList;
import java.util.List;

import org.gvsig.tools.exception.BaseException;


/**
 * <p>All layers in <i>libFMap</i> can be in a set of <i>states</i>, that their combination represent the <i>status</i> of a layer.</p>
 * 
 * <p><code>FLayerStatus</code> class supports the common status for all kind of {@link FLyrDefault FLyrDefault} layers.</p>
 * 
 * <p>This means that not necessarily all layers can stay in all possible status, it depends on its particular
 * nature.</p>
 * 
 * <p>Layer status states defined in <code>FLayerStatus</code> are:
 *  <ul>
 *  <li><i><b>Available</b></i>: the layer is available if the source of its data is on-line.</li>
 *  <li><i><b>Visible</b></i>: one layer is visible if it's selected its <i>check box</i> associated in TOC. This implies that 
 *  layer will be called to be painted unless the necessary data would be unavailable; this situation is more frequent with layers of
 *  remote services.</li>
 *  <li><i><b>Active</b></i>: is active if it's selected at the TOC.</li>
 *  <li><i><b>Dirty</b></i>: is dirty if needs to be refreshed.</li>
 *  <li><i><b>In TOC</b></i>: if it's being listed in a TOC GUI.</li>
 *  <li><i><b>Editing</b></i>: if the layer it's being edited now.</li>
 *  <li><i><b>Writable</b></i>: if can be modified and saved the changes.</li>
 *  <li><i><b>Cache drawn layers</b></i>: if stores previous draws. This is useful to accelerate the draw of the layer.</li>
 *  <li><i><b>Driver loaded</b></i>: if driver for managing the layer has been loaded successfully.</li>
 *  </ul>
 * </p>
 * 
 * @see IFLayerStatus
 * 
 * @author azabala
 */
public class FLayerStatus implements IFLayerStatus {
	/**
	 * <p>Layer's availability: the layer is available if the source of its data is on-line.</p>
	 */
	public boolean available;
	
	/**
	 * <p>Layer's visibility: the layer is visible if it's selected its <i>check box</i> associated in TOC. This implies that 
	 *  layer will tried to be painted unless the necessary data would be unavailable; this situation is more frequent with layers of
	 *  remote services.</p>
	 */
	public boolean visible;
	
	/**
	 * <p>The layer is active if it's selected at the TOC.</p>
	 */
	public boolean active;
	
	/**
	 * <p>One layer is dirty if needs to be refreshed.</p>
	 */
	public boolean dirty;
	
	/**
	 * <p>The layer is in TOC if it's being listed in a TOC GUI.</p>
	 */
	public boolean inTOC;
	
	/**
	 * <p>This parameter reports if the layer it's being edited now.</p>
	 */
	public boolean editing;
	
	/**
	 * <p>This parameter reports if the layer can be modified and saved the changes.</p>
	 */
	public boolean writable;
	
	/**
	 * <p>This state reports if the layer stores previous draws. That's useful to accelerate the draw of the layer.</p>
	 */
	public boolean cacheDrawnLayers;

	
	/**
	 * <p>List with the information of the driver errors produced working this layer.</p>
	 */
	private ArrayList layerErrors;
		
	/**
	 * <p>This state reports if the driver for managing the layer has been loaded successfully.</p>
	 */
	private boolean driverLoaded=true;

	/**
	 * <p>Creates an instance with the default status of a <code>FLyrDefault</code> layer:
	 * <br>
	 *  <ul>
	 *  <li><b><i>Visible</i></b>: <code>true</code> .</li>
	 *  <li><b><i>Active</i></b>: <code>false</code> .</li>
	 *  <li><b><i>In TOC</i></b>: <code>true</code> .</li>
	 *  <li><b><i>Editing</i></b>: <code>false</code> .</li>
	 *  <li><b><i>Cache drawn layers</i></b>: <code>false</code> .</li>
	 *  <li><b><i>Dirty</i></b>: <code>false</code> .</li>
	 *  <li><b><i>Available</i></b>: <code>true</code> .</li>
	 *  <li><b><i>Driver loaded</i></b>: <code>true</code> .</li>
	 *  <li><b><i>Writable</i></b>: <code>false</code> .</li>
	 *  <li>Without driver errors.</li>
	 *  </ul>
	 * </p>
	 */
	public FLayerStatus(){
		this.layerErrors = new ArrayList();
		visible = true;
		active = false;
		inTOC = true;
		editing = false;
		cacheDrawnLayers = false;
		dirty = false;
		available = true;

		writable = false;

	}

	/**
	 * <p>Stores information of an exception produced working with a driver of the layer.</p>
	 * 
	 * @param error the driver exception
	 */
	public void addLayerError(BaseException error){
		layerErrors.add(error);
	}

	public FLayerStatus cloneStatus(){
		FLayerStatus newStatus = new FLayerStatus();
		newStatus.layerErrors.addAll(this.layerErrors);
		newStatus.visible = this.visible;
		newStatus.active = this.active;
		newStatus.inTOC = this.inTOC;
		newStatus.editing = this.editing;
		newStatus.cacheDrawnLayers = this.cacheDrawnLayers;
		newStatus.dirty = this.dirty;
		newStatus.available = this.available;
		newStatus.driverLoaded = this.driverLoaded;	
		newStatus.writable = this.writable;
	
		return newStatus;
	}
	
	

	public boolean equals(Object obj) {
		FLayerStatus other;
		if (obj instanceof FLayerStatus) {
			other = (FLayerStatus) obj;
		} else{
			return false;
		}

		if (other.visible != this.visible 
				|| other.active != this.active
				|| other.inTOC != this.inTOC || other.editing != this.editing
				|| other.cacheDrawnLayers != this.cacheDrawnLayers
				|| other.dirty != this.dirty
				|| other.available != this.available
				|| other.driverLoaded != this.driverLoaded
				|| other.writable != this.writable) {
			return false;
		}
		

		
		if ((!other.layerErrors.containsAll(this.layerErrors)) || other.layerErrors.size() != this.layerErrors.size()){
			return false;
		}
		return super.equals(obj);
	}

	/**
	 * <p>Gets the number of driver exceptions produced working with the layer.</p>
	 * 
	 * @return number of driver exceptions produced
	 */
	public int getNumErrors(){
		return layerErrors.size();
	}

	/**
	 * <p>Gets the information of the <i>nth</i> layer driver exception registered in the status.</p>
	 * 
	 * @param i ith layer exception registered
	 * 
	 * @return the <i>nth</i> layer driver exception registered
	 */
	public BaseException getError(int i){
		return (BaseException) layerErrors.get(i);
	}

	/**
	 * <p>Returns if there have been driver errors working with the layer.</p>
	 * 
	 * @return <code>true</code> if there have driver errors working with the layer; otherwise <code>false</code>
	 */
	public boolean isOk(){
		return layerErrors.size() == 0;
	}

	/**
	 * <p>Returns a list of errors produced in a layer working with its driver.</p>
	 * 
	 * @return list errors produced in a layer working with its driver
	 */
	public List getErrors() {
		return layerErrors;
	}

	/**
	 * <p>Returns if has been loaded successfully the driver for managing the layer.</p>
	 * 
	 * @return <code>true</code> if has been loaded successfully the driver for managing the layer; otherwise <code>false</code>
	 */
	public boolean isDriverLoaded() {
		return driverLoaded;
	}

	/**
	 * <p>Sets if has been loaded successfully the driver for managing the layer.</p>
	 * 
	 * @param driverLoaded <code>true</code> if has been loaded successfully the driver for managing the layer; otherwise <code>false</code>
	 */
	public void setDriverLoaded(boolean driverLoaded) {
		this.driverLoaded = driverLoaded;
	}
}

