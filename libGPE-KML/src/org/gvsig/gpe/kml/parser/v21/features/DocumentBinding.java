package org.gvsig.gpe.kml.parser.v21.features;

import java.io.IOException;

import javax.xml.namespace.QName;

import org.gvsig.gpe.kml.parser.GPEDeafultKmlParser;
import org.gvsig.gpe.kml.utils.Kml2_1_Tags;
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
 * $Id: DocumentBinding.java 357 2008-01-09 17:50:08Z jpiera $
 * $Log$
 * Revision 1.2  2007/06/07 14:53:59  jorpiell
 * Add the schema support
 *
 * Revision 1.1  2007/05/11 07:06:29  jorpiell
 * Refactoring of some package names
 *
 * Revision 1.6  2007/05/09 08:36:24  jorpiell
 * Add the bbox to the layer
 *
 * Revision 1.5  2007/05/08 08:22:37  jorpiell
 * Add comments to create javadocs
 *
 * Revision 1.4  2007/05/02 11:46:50  jorpiell
 * Writing tests updated
 *
 * Revision 1.3  2007/04/20 08:38:59  jorpiell
 * Tests updating
 *
 * Revision 1.2  2007/04/14 16:08:07  jorpiell
 * Kml writing support added
 *
 * Revision 1.1  2007/04/13 13:16:21  jorpiell
 * Add KML reading support
 *
 *
 */
/**
 * This class parsers a Document tag. Example:
 * <p>
 * <pre>
 * <code>
 * &lt;Document&gt;
 * &lt;Placemark&gt;
 * &lt;name&gt;CDATA example&lt;/name&gt;
 * &lt;description&gt;Description example&lt;/description&gt;
 * &lt;Point&gt;
 * &lt;oordinates&gt;102.595626,14.996729&lt;/coordinates&gt;
 * &lt;/Point&gt;
 * &lt;/Placemark&gt;
 * &lt;/Document&gt;
 * </code>
 * </pre>
 * </p>
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 * @see http://code.google.com/apis/kml/documentation/kml_tags_21.html#document
 */
public class DocumentBinding {
	
	/**
	 * It parses the Document tag
	 * @param parser
	 * The XML parser
	 * @param handler
	 * The GPE parser that contains the content handler and
	 * the error handler
	 * @return
	 * A Layer
	 * @throws XmlStreamException 
	 * @throws IOException 
	 */
	public Object parse(IXmlStreamReader parser,GPEDeafultKmlParser handler) throws XmlStreamException, IOException {
		boolean endFeature = false;
		int currentTag;		

		XMLAttributesIterator attributesIterator = new XMLAttributesIterator(parser);   
		
		String id = handler.getProfile().getFeatureBinding().getID(parser, handler);
		Object layer = handler.getContentHandler().startLayer(id, parser.getName().getNamespaceURI(), null, null, null, attributesIterator, null, null);

		QName tag = parser.getName();
		currentTag = parser.getEventType();

		while (!endFeature){
			switch(currentTag){
			case IXmlStreamReader.START_ELEMENT:
				if (CompareUtils.compareWithNamespace(tag,Kml2_1_Tags.OPEN)){
					parser.next();
					String open = parser.getText();						
				}else if (CompareUtils.compareWithNamespace(tag,Kml2_1_Tags.NAME)){
					parser.next();
					handler.getContentHandler().addNameToLayer(parser.getText(),layer);
				}if (CompareUtils.compareWithNamespace(tag,Kml2_1_Tags.DESCRIPTION)){
					parser.next();
					handler.getContentHandler().addDescriptionToLayer(parser.getText(),layer);
				}else if (CompareUtils.compareWithNamespace(tag,Kml2_1_Tags.STYLE)){
					StyleBinding.parse(parser, handler);
				}else if (CompareUtils.compareWithNamespace(tag,Kml2_1_Tags.FOLDER)){
					handler.getProfile().getFolderBinding().parse(parser, handler, layer);
				}else if (CompareUtils.compareWithNamespace(tag,Kml2_1_Tags.PLACEMARK)){
					Object feature = PlaceMarketBinding.parse(parser, handler);
					handler.getContentHandler().addFeatureToLayer(feature, layer);
				}else if (CompareUtils.compareWithNamespace(tag,Kml2_1_Tags.REGION)){
					Object bbox = handler.getProfile().getRegionBinding().parse(parser, handler);
					handler.getContentHandler().addBboxToLayer(bbox, layer);
				}else if (CompareUtils.compareWithNamespace(tag,Kml2_1_Tags.LOOKAT)){
					 handler.getProfile().getLookAtBinding().parse(parser, handler);
				}
				 break;
			case IXmlStreamReader.END_ELEMENT:
				if (CompareUtils.compareWithNamespace(tag,Kml2_1_Tags.DOCUMENT)){
					endFeature = true;
					handler.getContentHandler().endLayer(layer);
				}
				break;
			case IXmlStreamReader.CHARACTERS:					

				break;
			}
			if (!endFeature){					
				currentTag = parser.next();
				tag = parser.getName();
			}
		}			

		return layer;
	}
}
