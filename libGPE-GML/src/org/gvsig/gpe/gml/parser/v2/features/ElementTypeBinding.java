package org.gvsig.gpe.gml.parser.v2.features;

import java.io.IOException;

import javax.xml.namespace.QName;

import org.gvsig.gpe.gml.parser.GPEDefaultGmlParser;
import org.gvsig.gpe.gml.utils.GMLGeometries;
import org.gvsig.gpe.gml.utils.GMLUtilsParser;
import org.gvsig.gpe.xml.stream.IXmlStreamReader;
import org.gvsig.gpe.xml.stream.XmlStreamException;
import org.gvsig.gpe.xml.utils.CompareUtils;
import org.gvsig.gpe.xml.utils.XMLAttributesIterator;
import org.gvsig.xmlschema.som.IXSElementDeclaration;
import org.gvsig.xmlschema.utils.TypeUtils;

/* gvSIG. Sistema de InformaciÛn Geogr·fica de la Generalitat Valenciana
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
 *   Av. Blasco Ib·Òez, 50
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
 * $Id: ElementTypeBinding.java 350 2008-01-09 12:53:07Z jpiera $
 * $Log$
 * Revision 1.8  2007/06/14 13:50:05  jorpiell
 * The schema jar name has been changed
 *
 * Revision 1.7  2007/06/07 14:53:30  jorpiell
 * Add the schema support
 *
 * Revision 1.6  2007/05/18 10:41:01  csanchez
 * Actualizaci√≥n libGPE-GML eliminaci√≥n de clases inecesarias
 *
 * Revision 1.5  2007/05/16 13:00:48  csanchez
 * Actualizaci√≥n de libGPE-GML
 *
 * Revision 1.4  2007/05/15 09:51:59  jorpiell
 * The namespace is deleted from the element name
 *
 * Revision 1.3  2007/05/15 09:35:09  jorpiell
 * the tag names cant have blanc spaces
 *
 * Revision 1.2  2007/05/15 08:04:16  jorpiell
 * If the element type is a geometry, the element value wiil be the toString geometry method
 *
 * Revision 1.1  2007/05/14 09:30:30  jorpiell
 * Add the FeatureMember tag
 *
 *
 */
/**
 * This class is used to parse some xml tags that
 * represents a xml element. 
 * @author Jorge Piera LLodr· (jorge.piera@iver.es)
 * @see http://www.w3.org/XML/Schema
 */
public class ElementTypeBinding {
	
	/**
	 * It parses a xml element
	 * @param parser
	 * The XML parser
	 * @param handler
	 * The GPE parser that contains the content handler and
	 * the error handler
	 * @param feature
	 * The feature is needed to add the geometry
	 * @param parentElement
	 * The parent element
	 * @return
	 * A Element
	 * @throws XmlStreamException
	 * @throws IOException
	 */
	public Object parse(IXmlStreamReader parser,GPEDefaultGmlParser handler, Object feature, Object parentElement, IXSElementDeclaration parentType) throws XmlStreamException, IOException {
		boolean endFeature = false;
		boolean isGeometryTag = false;
		int currentTag;		
		Object element = null;	
		Object value = null;
		Object geometry = null;
		boolean isInitialized = false;
		//Used to finish to parse the current element
		QName elementRootType = parser.getName();
		String type = null;	
		
		XMLAttributesIterator attributesIterator = new XMLAttributesIterator(parser);
		
		//Find the element type
		IXSElementDeclaration elementType = null;
		if (parentType != null){
			elementType = parentType.getSubElementByName(elementRootType.getLocalPart());
			if (elementType == null){
				type = TypeUtils.getXSType(String.class);
			}else{
				type = elementType.getTypeName();
			}
		}else{
			type = TypeUtils.getXSType(String.class);
		}
		
		QName tag = parser.getName();
		currentTag = parser.getEventType();

		while (!endFeature){
			switch(currentTag){
			case IXmlStreamReader.START_ELEMENT:
				if (!(CompareUtils.compareWithNamespace(tag,elementRootType))){
					//If is a geometry
					if (GMLGeometries.isGML(tag)){
						geometry = handler.getProfile().getGeometryBinding().
								parse(parser, handler);
						handler.getContentHandler().addGeometryToFeature(geometry, feature);
						if (geometry==null){
							System.out.println("\t\t\tGEOMETRIA VACIA");
							//Warning geometria vacia
						}
						isGeometryTag=true;
					}else {
						//If is not a geometry could be a complex feature
						if (!isInitialized){
							element = handler.getContentHandler().startElement(elementRootType.getNamespaceURI(),
									GMLUtilsParser.removeBlancSymbol(elementRootType.getLocalPart()),
									null,
									attributesIterator,
									parentElement);
							isInitialized = true;
						}
						handler.getProfile().getElementTypeBinding().
							parse(parser, handler, feature, element, null);
					}				
				}
				break;
			case IXmlStreamReader.END_ELEMENT:
				if (CompareUtils.compareWithNamespace(tag,elementRootType)){						
					endFeature = true;
					//If not is complex the element has not been created yet
					if (!isInitialized){
						element = handler.getContentHandler().startElement(
								elementRootType.getNamespaceURI(),
								GMLUtilsParser.removeBlancSymbol(elementRootType.getLocalPart()), 
								value,	
								attributesIterator,
								parentElement);
						isInitialized = true;
					}
					handler.getContentHandler().endElement(element);
				}
				break;
			case IXmlStreamReader.CHARACTERS:					
				if (geometry == null){
					value = TypeUtils.getValue(elementType, parser.getText());
				}
				break;
			}
			if (!endFeature){					
				currentTag = parser.next();
				tag = parser.getName();
			}
		}			
		return element;		
	}
}
