package org.gvsig.gpe.kml.parser.v21.features;

import java.io.IOException;

import javax.xml.namespace.QName;

import org.gvsig.gpe.kml.parser.GPEDeafultKmlParser;
import org.gvsig.gpe.kml.utils.KMLUtilsParser;
import org.gvsig.gpe.xml.stream.IXmlStreamReader;
import org.gvsig.gpe.xml.stream.XmlStreamException;
import org.gvsig.gpe.xml.utils.CompareUtils;
import org.gvsig.gpe.xml.utils.XMLAttributesIterator;

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
 * $Id: ElementBinding.java 357 2008-01-09 17:50:08Z jpiera $
 * $Log$
 * Revision 1.2  2007/06/07 14:53:59  jorpiell
 * Add the schema support
 *
 * Revision 1.1  2007/05/16 15:54:20  jorpiell
 * Add elements support
 *
 *
 */
/**
 * It parses an element. An element is a tag inside of a 
 * metadata tag
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class ElementBinding {
	/**
	 * It parses a xml element
	 * @param parser
	 * The XML parser
	 * @param handler
	 * The GPE parser that contains the content handler and
	 * the error handler
	 * @param parentElement
	 * The parent element
	 * @return
	 * A Element
	 * @throws XmlStreamException
	 * @throws IOException
	 */
	public  Object parse(IXmlStreamReader parser,GPEDeafultKmlParser handler, Object feature, Object parentElement) throws XmlStreamException, IOException {
		boolean endFeature = false;
		int currentTag;		
		Object element = null;	
		Object value = null;
		boolean isInitialized = false;
		//Used to finish to parse the current element
		QName elementRootType = parser.getName();

		String type = getType(elementRootType.getLocalPart());

		QName tag = parser.getName();
		currentTag = parser.getEventType();

		XMLAttributesIterator attributesIterator = new XMLAttributesIterator(parser);
		
		while (!endFeature){
			switch(currentTag){
			case IXmlStreamReader.START_ELEMENT:
				if (!(CompareUtils.compareWithNamespace(tag,elementRootType))){
					if (!isInitialized){
						element = handler.getContentHandler().startElement(elementRootType.getNamespaceURI(),
								KMLUtilsParser.removeBlancSymbol(elementRootType), 
								null,
								attributesIterator,
								parentElement);
						isInitialized = true;
					}
					handler.getProfile().getElementBinding().parse(parser, handler, feature, element);
				}

				break;
			case IXmlStreamReader.END_ELEMENT:
				if (CompareUtils.compareWithNamespace(tag,elementRootType)){						
					endFeature = true;
					if (!isInitialized){
						element = handler.getContentHandler().startElement(
								elementRootType.getNamespaceURI(),
								KMLUtilsParser.removeBlancSymbol(elementRootType), 
								value,	
								attributesIterator,
								parentElement);
						isInitialized = true;
					}
					handler.getContentHandler().endElement(element);
				}
				break;
			case IXmlStreamReader.CHARACTERS:					
				value = getValue(type, parser.getText());
				break;
			}
			if (!endFeature){					
				currentTag = parser.next();
				tag = parser.getName();
			}
		}
		return element;		
	}

	/**
	 * This method has to calculate the element type
	 * @param name
	 * Element name
	 * @return
	 * Element type
	 */
	private  String getType(String name){
		return "xs:string";
	}

	/**
	 * This method must return the element value
	 * @param type
	 * XS Element type
	 * @param value
	 * Element value like a String
	 * @return
	 * Element value
	 */
	private  Object getValue(String type, String value){
		return value;
	}

}
