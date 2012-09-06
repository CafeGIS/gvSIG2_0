package org.gvsig.remoteClient.wfs.exceptions;

import java.util.Hashtable;
import java.util.Map;

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
/* CVS MESSAGES:
 *
 * $Id: InvalidFormatException.java 18271 2008-01-24 09:06:43Z jpiera $
 * $Log$
 * Revision 1.1  2007-02-09 14:11:01  jorpiell
 * Primer piloto del soporte para WFS 1.1 y para WFS-T
 *
 *
 */
/**
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 */
public class InvalidFormatException extends WFSException {
	private static final long serialVersionUID = 8833249742771533911L;
	private String msg = null;		
	
	public InvalidFormatException(String msg) {
		this.msg = msg;
		init();			
	}	
	
	protected Map values() {		
		Hashtable params;
		params = new Hashtable();		
		return params;
	}

	public void init() {
		messageKey = "wfs_invalidFormat";
		formatString = msg;
		code = serialVersionUID;
	}
}
