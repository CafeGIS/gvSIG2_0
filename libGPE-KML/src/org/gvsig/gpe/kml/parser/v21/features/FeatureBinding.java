package org.gvsig.gpe.kml.parser.v21.features;

import org.gvsig.gpe.kml.parser.GPEDeafultKmlParser;
import org.gvsig.gpe.kml.utils.Kml2_1_Tags;
import org.gvsig.gpe.xml.stream.IXmlStreamReader;
import org.gvsig.gpe.xml.stream.XmlStreamException;
import org.gvsig.gpe.xml.utils.CompareUtils;

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
 * $Id: FeatureBinding.java 357 2008-01-09 17:50:08Z jpiera $
 * $Log$
 * Revision 1.1  2007/05/11 07:06:29  jorpiell
 * Refactoring of some package names
 *
 * Revision 1.1  2007/05/08 08:22:37  jorpiell
 * Add comments to create javadocs
 *
 * Revision 1.1  2007/04/20 08:38:59  jorpiell
 * Tests updating
 *
 *
 */
/**
 * This is an abstract element and cannot be used 
 * directly in a KML file. Document, Folder, NetworkLink,
 * Placemark, GroundOverLay and ScreenOverlay inherit from
 * it. It parses the common attributes
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 * @see http://code.google.com/apis/kml/documentation/kml_tags_21.html#feature
 */
public class FeatureBinding {
	
	/**
	 * It returns the feature id attribute
	 * @param parser
	 * The XML parser
	 * @param handler
	 * The GPE parser that contains the content handler and
	 * the error handler
	 * @return
	 * The feature id
	 * @throws XmlStreamException 
	 */
	public  String getID(IXmlStreamReader parser,GPEDeafultKmlParser handler) throws XmlStreamException{
		String id = null;
		for (int i=0 ; i<parser.getAttributeCount() ; i++){
			if (CompareUtils.compareWithNamespace(parser.getAttributeName(i),Kml2_1_Tags.ID)){
				id = parser.getAttributeValue(i);
			}
		}
		return id;	
	}
}
