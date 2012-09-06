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
 * $Id: WMSEventListener.java 5230 2006-05-16 17:10:27Z jaume $
 * $Log$
 * Revision 1.1  2006-05-16 17:10:27  jaume
 * *** empty log message ***
 *
 *
 */

package org.gvsig.remoteClient.wms;
/**
 * Interface for monitoring events from the WMSClient.
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 *
 */
public interface WMSEventListener {
	public static final int CAPABILITIES = 1;
	public static final int MAP = 2;
	public static final int FEATURE_INFO = 2;
	public static final int FINISHED = -1;
	public static final int STARTED = -2;
	public static final int TRANSFERRING = -3;
	public static final int FAILED = -4;
	public static final int CANCELLED = -5;
	
	public abstract void newEvent(int idRequest, int eventType);
}