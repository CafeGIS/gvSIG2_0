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
* $Id: WCSException.java 4493 2006-03-21 11:30:27Z jaume $
* $Log$
* Revision 1.1  2006-03-21 11:30:27  jaume
* some wcs client operation stuff
*
*
*/
package org.gvsig.remoteClient.exceptions;

/**
 * Excepción provocada por el WCS.
 *
 * @author Jaume Dominguez Faus - jaume.dominguez@iver.es
 */
public class WCSException extends Exception 
{	
	private String wcs_message = null;
	
	/**
	 *
	 */
	public WCSException() {
		super();
	}

	/**
	 * Creates a new WCS Exception
	 *
	 * @param message
	 */
	public WCSException(String message) {
		super(message);
	}

	/**
	 * Creates a new WCS Exception
	 *
	 * @param message
	 * @param cause
	 */
	public WCSException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Creates a new WCSException
	 *
	 * @param cause
	 */
	public WCSException(Throwable cause) {
		super(cause);
	}
	
	public String getWCSMessage()
	{
		if (wcs_message == null)
			return "";
		else
			return wcs_message;
	}
	
	public void setWCSMessage(String mes)
	{
		wcs_message = mes;
	}
}


