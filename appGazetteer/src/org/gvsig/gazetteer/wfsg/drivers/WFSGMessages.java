
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
package org.gvsig.gazetteer.wfsg.drivers;
import org.gvsig.gazetteer.querys.GazetteerQuery;
import org.gvsig.gazetteer.wfsg.filters.WFSGFilter;

/**
 * This class has methods that return the WFSG operations messages.
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class WFSGMessages {

	public static String getHTTPPOSTCapabilities() {        
		return "<?xml version=\"1.0\" ?>" +
		"<GetCapabilities " +
		"service=\"WFS\" " +
		"xmlns:csw=\"http://www.opengis.net/wfs\" " +
		"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
		"xsi:schemaLocation=\"http://www.opengis.net/wfs http://schemas.opengis.net/wfs/1.0.0/WFS-basic.xsd\">" +
		"</GetCapabilities>";
	} 

	public static String getHTTPPOSTFeature(GazetteerQuery query, int firstRecord) {        

		String message = "<wfs:GetFeature service=\"WFS\" version=\"1.0.0\" " +
		"outputFormat=\"GML2\" " +
		"xmlns:topp=\"http://www.openplans.org/topp\" " +
		"xmlns:wfs=\"http://www.opengis.net/wfs\" " +
		"xmlns:ogc=\"http://www.opengis.net/ogc\" " +
		"xmlns:gml=\"http://www.opengis.net/gml\" " +
		"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
		"xsi:schemaLocation=\"http://www.opengis.net/wfs " +
		"http://schemas.opengis.net/wfs/1.0.0/WFS-basic.xsd\">" +
		"<wfs:Query typeName=\"" + query.getFeatures()[0].getName() + "\">" +
			new WFSGFilter().getQuery(query) + 
		"</wfs:Query>" +
		"</wfs:GetFeature>";                 
		return message;    
	} 
}
