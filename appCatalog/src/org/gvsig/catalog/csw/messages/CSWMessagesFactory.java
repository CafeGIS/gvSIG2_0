package org.gvsig.catalog.csw.messages;

import java.util.HashMap;

import org.gvsig.catalog.csw.drivers.profiles.CSWAbstractProfile;
import org.gvsig.catalog.csw.parsers.CSWConstants;
import org.gvsig.catalog.exceptions.NotSupportedVersionException;

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
 * $Id$
 * $Log$
 *
 */
/**
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class CSWMessagesFactory {
	private static HashMap messages = null;
	
	static{
		messages = new HashMap();
		messages.put(CSWConstants.VERSION_0_9_0, CSWMessages0_9_0.class);
		messages.put(CSWConstants.VERSION_2_0_0, CSWMessages2_0_0.class);
		messages.put(CSWConstants.VERSION_2_0_1, CSWMessages2_0_1.class);
	}
	
	/**
	 * Gets a messages class
	 * @param version
	 * The CSW version
	 * @param profile
	 * The CSW profile
	 * @return
	 * A CSWAbstractMessages class
	 * @throws NotSupportedVersionException 
	 */
	public static CSWAbstractMessages getMessages(String version, CSWAbstractProfile profile) throws NotSupportedVersionException{
		if ((version != null) && (messages.containsKey(version))){
			Class clazz = (Class)messages.get(version);
			Class[] parameters = {CSWAbstractProfile.class};
			Object[] args = {profile};
			try {
				return (CSWAbstractMessages)clazz.getConstructor(parameters).newInstance(args);
			} catch (Exception e) {
				throw new NotSupportedVersionException(e);
			} 
		}
		throw new NotSupportedVersionException();
	}
}
