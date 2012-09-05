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
 * $Id: PlaceMarketBinding.java 357 2008-01-09 17:50:08Z jpiera $
 * $Log$
 * Revision 1.4  2007/06/07 14:53:59  jorpiell
 * Add the schema support
 *
 * Revision 1.3  2007/05/16 15:54:20  jorpiell
 * Add elements support
 *
 * Revision 1.2  2007/05/15 12:37:45  jorpiell
 * Add multyGeometries
 *
 * Revision 1.1  2007/05/11 07:06:29  jorpiell
 * Refactoring of some package names
 *
 * Revision 1.5  2007/05/09 08:36:24  jorpiell
 * Add the bbox to the layer
 *
 * Revision 1.4  2007/05/08 08:22:37  jorpiell
 * Add comments to create javadocs
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
 * This class parses a PlaceMark tag. Example:
 * <p>
 * <pre>
 * <code>
 * &lt;Placemark&gt;
 * &lt;name&gt;CDATA example&lt;/name&gt;
 * &lt;description&gt;Description example&lt;/description&gt;
 * &lt;Point&gt;
 * &lt;oordinates&gt;102.595626,14.996729&lt;/coordinates&gt;
 * &lt;/Point&gt;
 * &lt;/Placemark&gt;
 * </code>
 * </pre>
 * </p>
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 * @see http://code.google.com/apis/kml/documentation/kml_tags_21.html#placemark
 */
public class PlaceMarketBinding {

	/**
	 * It parses the PlaceMark tag
	 * @param parser
	 * The XML parser
	 * @param handler
	 * The GPE parser that contains the content handler and
	 * the error handler
	 * @return
	 * A Feature
	 * @throws IOException 
	 * @throws XmlStreamException 
	 * @throws XmlStreamException 
	 * @throws IOException 
	 */
	public static Object parse(IXmlStreamReader parser,GPEDeafultKmlParser handler) throws XmlStreamException, IOException{
		boolean endFeature = false;
		int currentTag;		

		XMLAttributesIterator attributesIterator = new XMLAttributesIterator(parser);
		
		String id = handler.getProfile().getFeatureBinding().getID(parser, handler);
		Object feature = handler.getContentHandler().startFeature(id, parser.getName().getNamespaceURI(), null, attributesIterator, null);
		
		QName tag = parser.getName();
		currentTag = parser.getEventType();

		while (!endFeature){
			switch(currentTag){
			case IXmlStreamReader.START_ELEMENT:
				if (CompareUtils.compareWithNamespace(tag,Kml2_1_Tags.NAME)){
					parser.next();
					handler.getContentHandler().addNameToFeature(parser.getText(), feature);
				}else if (CompareUtils.compareWithNamespace(tag,Kml2_1_Tags.DESCRIPTION)){
					parser.next();
					String description = parser.getText();
				}else if (CompareUtils.compareWithNamespace(tag,Kml2_1_Tags.VISIBILITY)){
					parser.next();
					String visibility = parser.getText();
				}else if (CompareUtils.compareWithNamespace(tag,Kml2_1_Tags.LOOKAT)){
					 handler.getProfile().getLookAtBinding().parse(parser, handler);
				}else if (CompareUtils.compareWithNamespace(tag,Kml2_1_Tags.STYLEURL)){
					parser.next();
					String styleURL = parser.getText();
				}else if (CompareUtils.compareWithNamespace(tag,Kml2_1_Tags.POINT)){
					Object point =  handler.getProfile().getPointTypeBinding().parse(parser, handler);
					handler.getContentHandler().addGeometryToFeature(point, feature);
				}else if (CompareUtils.compareWithNamespace(tag,Kml2_1_Tags.LINESTRING)){
					Object lineString =  handler.getProfile().getLineStringTypeBinding().parse(parser, handler);
					handler.getContentHandler().addGeometryToFeature(lineString, feature);
				}else if (CompareUtils.compareWithNamespace(tag,Kml2_1_Tags.POLYGON)){
					Object polygon =  handler.getProfile().getPolygonTypeBinding().parse(parser, handler);
					handler.getContentHandler().addGeometryToFeature(polygon, feature);
				}else if (CompareUtils.compareWithNamespace(tag,Kml2_1_Tags.MULTIGEOMETRY)){
					Object multiGeometry =  handler.getProfile().getMultiGeometryBinding().parse(parser, handler);
					handler.getContentHandler().addGeometryToFeature(multiGeometry, feature);
				}else if (CompareUtils.compareWithNamespace(tag,Kml2_1_Tags.REGION)){
					Object bbox =  handler.getProfile().getRegionBinding().parse(parser, handler);
				}else if (CompareUtils.compareWithNamespace(tag,Kml2_1_Tags.METADATA)){
					MetadataBinding.parse(parser, handler, feature);
				}
				break;
			case IXmlStreamReader.END_ELEMENT:
				if (CompareUtils.compareWithNamespace(tag,Kml2_1_Tags.PLACEMARK)){						
					endFeature = true;
					handler.getContentHandler().endFeature(feature);
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

		return feature;
	}
}
