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
* $Id: ErrorListener.java 20989 2008-05-28 11:05:57Z jmvivo $
* $Log$
* Revision 1.2  2006-09-21 17:20:50  azabala
* added method to report many driver exceptions
*
* Revision 1.1  2005/12/20 10:56:34  jaume
* Added an error event to fmap
*
*
*/
/**
 * 
 */
package org.gvsig.fmap.mapcontext.events.listeners;

import java.util.List;

import org.gvsig.fmap.mapcontext.events.ErrorEvent;


/**
 * @author jaume
 *
 */
public interface ErrorListener {
    /**
     * Listen for error events
     * (usually caused by driverExceptions)
     * @param e
     */
	void errorThrown(ErrorEvent e);

	/**
	 * Report a bundle of driver exceptions caused in the same
	 * fmap atomic transaction
	 * @param introductoryText introductory text specified by developer. It null, used ""
	 * @param driverExceptions list with a bundle of driver exceptions catched during
	 * an atomic event
	 */
	void reportDriverExceptions(String introductoryText, List driverExceptions);
}
