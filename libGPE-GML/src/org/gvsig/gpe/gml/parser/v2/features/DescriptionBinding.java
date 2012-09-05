package org.gvsig.gpe.gml.parser.v2.features;

import java.io.IOException;

import org.gvsig.gpe.gml.parser.GPEDefaultGmlParser;
import org.gvsig.gpe.xml.stream.IXmlStreamReader;
import org.gvsig.gpe.xml.stream.XmlStreamException;

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
 * $Id: DescriptionBinding.java 195 2007-11-26 09:02:22Z jpiera $
 * $Log$
 * Revision 1.1  2007/05/07 12:58:42  jorpiell
 * Add some methods to manage the multigeometries
 *
 *
 */
/**
 * This class parses the gml:description tag. Example:
 * <p>
 * <pre>
 * <code>
 * &lt;gml:description&gt;GML tag desciption&lt;/gml:description&gt;
 * </code>
 * </pre>
 * </p>
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class DescriptionBinding{
	
	/**
	 * It parses the gml:description tag
	 * @param parser
	 * The XML parser
	 * @param handler
	 * The GPE parser that contains the content handler and
	 * the error handler
	 * @return
	 * The description
	 * @throws XmlStreamException
	 * @throws IOException
	 */
	public String parse(IXmlStreamReader parser,GPEDefaultGmlParser handler) throws XmlStreamException, IOException {
		//Put the pointer into the value tag
		parser.next();
		//Retrieve the value
		String description = parser.getText();
		//Put the value into the end tag
		parser.next();
		return description;
	}
}
