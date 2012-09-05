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
 * $Id: FolderBinding.java 357 2008-01-09 17:50:08Z jpiera $
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
 * Revision 1.2  2007/04/13 13:16:21  jorpiell
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
 * This class parses a Folder tag. Example:
 * <p>
 * <pre>
 * <code>
 * &lt;Folder&gt;
 * &lt;Placemark&gt;
 * &lt;name&gt;CDATA example&lt;/name&gt;
 * &lt;description&gt;Description example&lt;/description&gt;
 * &lt;Point&gt;
 * &lt;oordinates&gt;102.595626,14.996729&lt;/coordinates&gt;
 * &lt;/Point&gt;
 * &lt;/Placemark&gt;
 * &lt;/Folder&gt;
 * </code>
 * </pre>
 * </p>
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 * @see http://code.google.com/apis/kml/documentation/kml_tags_21.html#folder
 */
public class FolderBinding {

	/**
	 * It parses the Folder tag
	 * @param parser
	 * The XML parser
	 * @param handler
	 * The GPE parser that contains the content handler and
	 * the error handler
	 * @return
	 * A Layer
	 * @throws IOException 
	 * @throws XmlStreamException 
	 */
	public Object parse(IXmlStreamReader parser,GPEDeafultKmlParser handler, Object parentLayer) throws XmlStreamException, IOException  {
		boolean endFeature = false;
		int currentTag;			

		XMLAttributesIterator attributesIterator = new XMLAttributesIterator(parser);   
		
		String id = handler.getProfile().getFeatureBinding().getID(parser, handler);
		Object layer = handler.getContentHandler().startLayer(id, parser.getName().getNamespaceURI(), null, null, null, attributesIterator, parentLayer, null);

		parser.next();
		QName tag = parser.getName();
		currentTag = parser.getEventType();

		while (!endFeature){
			switch(currentTag){
			case IXmlStreamReader.START_ELEMENT:
				if (CompareUtils.compareWithNamespace(tag,Kml2_1_Tags.NAME)){
					parser.next();
					handler.getContentHandler().addNameToLayer(parser.getText(),layer);
				}else if (CompareUtils.compareWithNamespace(tag,Kml2_1_Tags.DESCRIPTION)){
					parser.next();
					handler.getContentHandler().addDescriptionToLayer(parser.getText(),layer);
				}else if (CompareUtils.compareWithNamespace(tag,Kml2_1_Tags.LOOKAT)){
					 handler.getProfile().getLookAtBinding().parse(parser, handler);
				}else if (CompareUtils.compareWithNamespace(tag,Kml2_1_Tags.PLACEMARK)){
					Object feature = PlaceMarketBinding.parse(parser, handler);
					handler.getContentHandler().addFeatureToLayer(feature, layer);
				}else if (CompareUtils.compareWithNamespace(tag,Kml2_1_Tags.FOLDER)){
					 handler.getProfile().getFolderBinding().parse(parser, handler, layer);
				}else if (CompareUtils.compareWithNamespace(tag,Kml2_1_Tags.REGION)){
					Object bbox =  handler.getProfile().getRegionBinding().parse(parser, handler);
					handler.getContentHandler().addBboxToLayer(bbox, layer);
				}
				break;
			case IXmlStreamReader.END_ELEMENT:
				if (CompareUtils.compareWithNamespace(tag,Kml2_1_Tags.FOLDER)){
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
