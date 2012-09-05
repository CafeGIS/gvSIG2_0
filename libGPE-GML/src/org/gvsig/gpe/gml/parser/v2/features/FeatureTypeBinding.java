package org.gvsig.gpe.gml.parser.v2.features;

import java.io.IOException;
import java.util.Map;

import javax.xml.namespace.QName;

import org.gvsig.gpe.gml.parser.GPEDefaultGmlParser;
import org.gvsig.gpe.gml.utils.GMLGeometries;
import org.gvsig.gpe.gml.utils.GMLObject;
import org.gvsig.gpe.gml.utils.GMLTags;
import org.gvsig.gpe.gml.utils.GMLUtilsParser;
import org.gvsig.gpe.xml.stream.IXmlStreamReader;
import org.gvsig.gpe.xml.stream.XmlStreamException;
import org.gvsig.gpe.xml.utils.CompareUtils;
import org.gvsig.gpe.xml.utils.XMLAttributesIterator;

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
 * $Id: FeatureTypeBinding.java 350 2008-01-09 12:53:07Z jpiera $
 * $Log$
 * Revision 1.8  2007/06/22 12:22:40  jorpiell
 * The typeNotFoundException has been deleted. It never was thrown
 *
 * Revision 1.7  2007/06/14 13:50:05  jorpiell
 * The schema jar name has been changed
 *
 * Revision 1.6  2007/06/07 14:53:30  jorpiell
 * Add the schema support
 *
 * Revision 1.5  2007/05/18 10:41:01  csanchez
 * Actualizaci√≥n libGPE-GML eliminaci√≥n de clases inecesarias
 *
 * Revision 1.4  2007/05/15 12:10:01  jorpiell
 * The bbox is linked to the feature
 *
 * Revision 1.3  2007/05/15 09:43:48  jorpiell
 * The namespace is deleted from the feature name
 *
 * Revision 1.2  2007/05/15 09:35:09  jorpiell
 * the tag names cant have blanc spaces
 *
 * Revision 1.1  2007/05/14 09:30:30  jorpiell
 * Add the FeatureMember tag
 *
 *
 */
/**
 * This class parses the gml objects that has a 
 * gml:_Feature object type. The structure of the
 * properties that this type has is variable and depends
 * on its schema. Example:
 * <p>
 * <pre>
 * <code>
 * &lt;cit:cities&gt;
 * &lt;cit:the_geom&gt;
 * &lt;gml:Point srsName='0'&gt;
 * &lt;gml:coordinates&gt;-123.06999969482422,49.411192817494346&lt;/gml:coordinates&gt;
 * &lt;/gml:Point&gt;
 * &lt;/cit:the_geom&gt;
 * &lt;cit:NAME&gt;Vancouver&lt;/cit:NAME&gt;
 * &lt;cit:CAPITAL&gt;N&lt;/cit:CAPITAL&gt;
 * &lt;cit:PROV_NAME&gt;British Columbia&lt;/cit:PROV_NAME&gt;
 * &lt;/cit:cities&gt;
 * </code>
 * </pre>
 * </p>
 * @author Jorge Piera LLodr· (jorge.piera@iver.es)
 */
public class FeatureTypeBinding {	
	/**
	 * It parses a feature
	 * @param parser
	 * The XML parser
	 * @param handler
	 * The GPE parser that contains the content handler and
	 * the error handler
	 * @return
	 * A feature
	 * @throws XmlStreamException
	 * @throws IOException
	 */
	public Object parse(IXmlStreamReader parser,GPEDefaultGmlParser handler) throws XmlStreamException, IOException {
		boolean endFeature = false;
		int currentTag;		
		Object feature = null;	
		//Used to finish to parse the current feature member
		QName featureRootType = parser.getName();

		XMLAttributesIterator attributesIterator = new XMLAttributesIterator(parser);		
		String fid = getID(attributesIterator.getAttributes());
					
		//If the element doesn't has the type it register the warning
//		IXSElementDeclaration featureElement = null;
//		featureElement = handler.getContentHandler().getSchemaDocument().getElementDeclarationByName(featureRootType);
//		String elementName = null;
//		if (featureElement!=null){
//			String substitution = featureElement.getElement().getAttribute(SchemaTags.SUBSTITUTIONGROUP);
//			//If it is a layer starts to parse the tag like a new layer
//			if (CompareUtils.compareWithNamespace(substitution,GMLTags.GML_ABSTRACT_FEATURECOLLECTION)){
//				return new GMLObject(handler.getProfile().getFeatureCollectionBinding().parse(parser, handler),
//						GMLObject.LAYER);
//			}
//			elementName = featureElement.getTypeName();
//		}
		feature = handler.getContentHandler().startFeature(fid, 
				featureRootType.getNamespaceURI(),
				GMLUtilsParser.removeBlancSymbol(featureRootType.getLocalPart()),
				attributesIterator,
				null);
		
		QName tag = parser.getName();
		currentTag = parser.getEventType();

		while (!endFeature){
			switch(currentTag){
			case IXmlStreamReader.START_ELEMENT:
				if (CompareUtils.compareWithNamespace(tag, GMLTags.GML_NAME)){
					parser.next();
					handler.getContentHandler().addNameToFeature(parser.getText(), feature);
				}else if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_DESCRIPTION)){
					parser.next();
					String description = parser.getText();
				}else if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_BOUNDEDBY)){
					Object bbox = handler.getProfile().getBoundedByTypeBinding().parse(parser, handler);
					handler.getContentHandler().addBboxToFeature(bbox, feature);
				}else{
					//If is not a GML...
					if (!(CompareUtils.compareWithNamespace(featureRootType,tag))){						
						if (GMLGeometries.isGML(tag)){
							Object geometry = handler.getProfile().getGeometryBinding().parse(parser, handler);
							handler.getContentHandler().addGeometryToFeature(geometry, feature);
						}else{
						//Elements (elements or features)
							Object element = handler.getProfile().getElementTypeBinding().parse(parser, handler, feature, null, null);
							handler.getContentHandler().addElementToFeature(element, feature);
						}
					}
				}
				break;
			case IXmlStreamReader.END_ELEMENT:
				if (CompareUtils.compareWithNamespace(featureRootType, tag)){						
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
		return new GMLObject(feature,GMLObject.FEATURE);		
	}	
	
	/**
	 * It returns a the feaure id attribute
	 * @param hash
	 * Hashtable with the XML attributes
	 * @return
	 * The id
	 */
	public String getID(Map hash){
		if (hash.containsKey(GMLTags.GML_FID)){
			return (String)hash.get(GMLTags.GML_FID);
		}
		String id = GMLTags.GML_NAMESPACE_PREFIX + ":" + GMLTags.GML_ID;
		if (hash.containsKey(id)){
			return (String)hash.get(id);
		}
		return null;
	}
}
