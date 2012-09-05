package org.gvsig.gpe.kml.parser.v21.header;

import javax.xml.namespace.QName;

import org.gvsig.gpe.kml.exceptions.KmlHeaderParseException;
import org.gvsig.gpe.kml.exceptions.KmlNotRootTagException;
import org.gvsig.gpe.kml.parser.GPEDeafultKmlParser;
import org.gvsig.gpe.kml.utils.Kml2_1_Tags;
import org.gvsig.gpe.xml.stream.IXmlStreamReader;
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
 * $Id:HeaderBinding.java 357 2008-01-09 17:50:08Z jpiera $
 * $Log$
 * Revision 1.1  2007/07/02 09:59:44  jorpiell
 * The generated xsd schemas have to be valid
 *
 * Revision 1.2  2007/06/07 14:53:59  jorpiell
 * Add the schema support
 *
 * Revision 1.1  2007/05/11 07:06:29  jorpiell
 * Refactoring of some package names
 *
 * Revision 1.3  2007/05/08 09:28:17  jorpiell
 * Add comments to create javadocs
 *
 * Revision 1.2  2007/04/20 08:38:59  jorpiell
 * Tests updating
 *
 * Revision 1.1  2007/04/13 13:16:21  jorpiell
 * Add KML reading support
 *
 * Revision 1.1  2007/03/07 08:19:10  jorpiell
 * Pasadas las clases de KML de libGPE-GML a libGPE-KML
 *
 * Revision 1.1  2007/02/28 11:48:31  csanchez
 * *** empty log message ***
 *
 * Revision 1.1  2007/02/20 10:53:20  jorpiell
 * AÃ±adidos los proyectos de kml y gml antiguos
 *
 * Revision 1.1  2007/02/12 13:49:18  jorpiell
 * Añadido el driver de KML
 *
 *
 */
/**
 * This class parses the KML header and retrieves
 * the namespace that will be used to obtain the
 * kml version
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 */
public class HeaderBinding {
	
	/**
	 * This method parses the kml header file
	 * @throws KmlHeaderParseException
	 */
	public String parse(IXmlStreamReader parser,GPEDeafultKmlParser handler) throws KmlHeaderParseException{
		String namespace = Kml2_1_Tags.UNKNOWN_VERSION;
		try {	
			parser.nextTag();
			QName name = parser.getName();	
			if (!CompareUtils.compareWithNamespace(name,Kml2_1_Tags.ROOT)){
				throw new KmlNotRootTagException(name);
			}
			for (int i=0 ; i<parser.getAttributeCount() ; i++){
				namespace = parser.getAttributeValue(i);				
			}
		} catch (Exception e) {
			throw new KmlHeaderParseException(e);
		} 
		return namespace;
	}
}
