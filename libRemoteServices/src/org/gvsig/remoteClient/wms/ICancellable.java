/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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
 * $Id: ICancellable.java 20098 2008-04-14 08:42:53Z jmvivo $
 * $Log$
 * Revision 1.1  2006-05-24 16:37:34  jaume
 * *** empty log message ***
 *
 *
 */
package org.gvsig.remoteClient.wms;

/**
 * <p>When a task is accessing to remote data, takes an indeterminate time, and occasionally gets locked. That's
 * the reason a task should support to be cancelable.</p>
 * <p><code>ICancellable</code> interface is designed for getting information about the cancellation of a
 * task of downloading remote information.</p>
 */
public interface ICancellable {
	/**
	 * <p>Returns <code>true</code> if a download or a group of downloads tasks has been canceled.</p>
	 * 
	 * @return <code>true</code> if a download or a group of downloads tasks has been canceled, otherwise <code>false</code>
	 */
	public boolean isCanceled();
	
	/**
	 * <p>Used to cancel only a group of downloads tasks with the same identifier.</p>
	 * 
	 * @return the identifier
	 */
	public Object getID();
}
