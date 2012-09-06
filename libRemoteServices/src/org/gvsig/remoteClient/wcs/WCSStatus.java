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
/*
 * $Id: WCSStatus.java 9217 2006-12-05 16:27:15Z fdiaz $ 
 * $Log$
 * Revision 1.9  2006-12-05 16:27:15  fdiaz
 * *** empty log message ***
 *
 * Revision 1.8  2006/05/12 07:45:49  jaume
 * some warnings removed
 *
 * Revision 1.7  2006/04/25 06:47:50  jaume
 * clean up unnecessary imports
 *
 * Revision 1.6  2006/04/19 11:04:51  jaume
 * *** empty log message ***
 *
 * Revision 1.5  2006/03/27 15:20:15  jaume
 * *** empty log message ***
 *
 * Revision 1.3  2006/03/21 11:30:26  jaume
 * some wcs client operation stuff
 *
 * Revision 1.2  2006/03/15 08:54:42  jaume
 * *** empty log message ***
 *
 * Revision 1.1.2.1  2006/03/08 09:08:31  jaume
 * *** empty log message ***
 *
 * Revision 1.1  2006/03/06 15:18:32  jaume
 * *** empty log message ***
 * 
 */
package org.gvsig.remoteClient.wcs;

import java.awt.geom.Rectangle2D;

import org.gvsig.remoteClient.RemoteClientStatus;
/**
 * Describes the status of a WCSclient, so it adds to the Remote client status
 * a list of layers, a list of layer styles, the extent of the map.
 * Provides the functionality to modify these lists.
 * 
 * @author jaume domínguez faus - jaume.dominguez@iver.es
 */
public class WCSStatus extends RemoteClientStatus {
	//private String		resolution		= null; TODO do I need it?
	private Rectangle2D	bBox			= null;
	private String		coverageName	= null;
	private String		onlineResource	= null;
	private String		depth			= null;
	private String		times			= null;
	private String		parameters		= null;
	private String		message			= null;
	
	/**
	 * Sets the Bounding Box that is going to be requested to the server.
	 * @param bBox, Rectangle2D containing the edges of the bounding box
	 */
	public void setExtent(Rectangle2D bBox) {
		this.bBox = bBox;
	}
	
	/**
	 * Sets the name of the coverage requested to the server.
	 * @param coverageName, String containing the name of the requesting coverage
	 */
	public void setCoveraName(String coverageName) {
		this.coverageName = coverageName;
	}

	/**
	 * Sets the parameter string. The parameter string is the part of the request 
	 * containing the value for the axis as described in the DescribeCoverage
	 * document.
	 * @param parameters
	 */
	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	/**
	 * Will set the address of the host that serves the following request
	 * @param onlineResources
	 */
	public void setOnlineResource(String onlineResources) {
		this.onlineResource = onlineResources;
	}
	
	/**
	 * Sets the depth. The depth is the third dimension for the size. 
	 * @param depth, String
	 */
	public void setDepth(String depth) {
		this.depth = depth;
	}

	/**
	 * Returns the address host that will serve the request 
	 * @return String
	 */
	public String getOnlineResource() {
		return onlineResource;
	}

	/**
	 * Return the name of the coverage requested to the server.
	 * @return String containing the name of the requesting coverage
	 */
	public String getCoverageName() {
		return this.coverageName;
	}
	
	/**
	 * Returns the Bounding Box that is going to be requested to the server.
	 * @return Rectangle2D containing the edges of the bounding box
	 */
	public Rectangle2D getExtent() {
		return this.bBox;
	}

	/**
	 * Returns the depth of the next request.
	 * @return String
	 */
	public String getDepth() {
		return depth;
	}

	/**
	 * Returns the value for the time parameter that will be used in the next request
	 * @return String
	 */
	public String getTime() {
		return times;
	}
	
	/**
	 * Returns the parameter string.
	 * The parameter string is the part of the request containing the value for the
	 * axis as described in the DescribeCoverage.
	 * @return String
	 */
	public String getParameters() {
		return parameters;
	}

	/**
	 * Sets the value(s) for the time that will be used in the next request. 
	 * @param times
	 */
	public void setTime(String times) {
		this.times = times;
	}

	/**
	 * Returns the message string
	 * @return String
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * Sets the message string 
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
}
