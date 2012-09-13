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
* $Id: FMapWCSDriverFactory.java 6042 2006-06-26 16:11:05Z jaume $
* $Log$
* Revision 1.1  2006-06-26 16:11:05  jaume
* *** empty log message ***
*
*
*/
package com.iver.cit.gvsig.fmap.drivers.wcs;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;
import java.util.Hashtable;

public class FMapWCSDriverFactory {
	private static Hashtable drivers = new Hashtable();
	private FMapWCSDriverFactory() { }

	static public final FMapWCSDriver getFMapDriverForURL(URL url) throws ConnectException, IOException {
		FMapWCSDriver drv = (FMapWCSDriver) drivers.get(url);
		if (drv == null) {
			drv = new FMapWCSDriver(url);
			drivers.put(url, drv);
		}
		return drv;
	}

}


