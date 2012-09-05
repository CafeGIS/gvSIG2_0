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
 * $Id: ErrorEvent.java 20989 2008-05-28 11:05:57Z jmvivo $
 * $Log$
 * Revision 1.2  2005-12-20 11:23:43  jaume
 * Added ErrorEvent to any Fmap, and error handling to FLayers
 *
 * Revision 1.1  2005/12/20 10:56:34  jaume
 * Added an error event to fmap
 *
 *
 */
/**
 * 
 */
package org.gvsig.fmap.mapcontext.events;


/**
 * <p><code>ErrorEvent</code> stores all necessary information of an error produced on a layer.</p>
 * 
 * @see FMapEvent
 * 
 * @deprecated As of release 1.0.2, don't used
 * 
 * @author jaume
 */
public class ErrorEvent extends FMapEvent {
	/**
	 * <p>Extra information about the error, like which layer was produced.</p>
	 * 
	 * @see #getMessage()
	 */
	private String message;
    
    /**
     * <p>Exception associated to the error produced.</p>
     * 
     * @see #getException()
     */
	private Exception exception;

    /**
     * <p>Constructs an <code>ErrorEvent</code> with the specified, detailed message as extra information, and the exception thrown.</p>
     * 
     * @param message detailed error information
     * @param e the exception thrown when the error was produced
     */
	public ErrorEvent(String message, Exception e){
		this.message = message;
		this.exception = e;
	}

    /**
     * <p>Gets the <code>Exception</code> associated to the error produced.</p>
     * 
     * @return the exception thrown when the error was produced
     */
	public Exception getException() {
		return exception;
	}

    /**
     * <p>Gets detailed message with extra information.</p>
     * 
     * @return the detail message with extra information
     */
	public String getMessage() {
		return message;
	}

}
