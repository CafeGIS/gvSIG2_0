package org.gvsig.gpe.gml.parser.v2.features;

import java.io.IOException;
import java.util.Map;

import javax.xml.namespace.QName;

import org.gvsig.gpe.gml.parser.GPEDefaultGmlParser;
import org.gvsig.gpe.gml.utils.GMLObject;
import org.gvsig.gpe.gml.utils.GMLTags;
import org.gvsig.gpe.gml.utils.GMLUtilsParser;
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
 * $Id: FeatureCollectionBinding.java 350 2008-01-09 12:53:07Z jpiera $
 * $Log$
 * Revision 1.2  2007/06/29 12:19:34  jorpiell
 * The schema validation is made independently of the concrete writer
 *
 * Revision 1.1  2007/06/07 14:53:30  jorpiell
 * Add the schema support
 *
 *
 */
/**
 * This class parses the gml objects that has a 
 * gml:FeatureCollection type. Example:
 * <p>
 * <pre>
 * <code>
 * &lt;gml:featureCollection&gt;
 * &lt;gml:featureMember&gt; 
 * &lt;/gml:featureMember&gt;
 * &lt;gml:featureMember&gt; 
 * &lt;/gml:featureMember&gt;
 * &lt;/gml:featureCollection&gt;
 * </code>
 * </pre>
 * </p>
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class FeatureCollectionBinding {
	/**
	 * It parses a feature collection
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
		boolean endCollection = false;
		int currentTag;		
		Object layer = null;	
		//Used to finish to parse the current feature collection
		while(parser.getEventType()!= IXmlStreamReader.START_ELEMENT){
			parser.next();			
		}
		QName layerRootType = parser.getName();		
		
		XMLAttributesIterator attributesIterator = new XMLAttributesIterator(parser);		
		String fid = handler.getProfile().getFeatureTypeBinding().getID(attributesIterator.getAttributes());

		layer = handler.getContentHandler().startLayer(fid, parser.getName().getNamespaceURI(), null, null, null, attributesIterator, null, null);

		QName tag = parser.getName();
		currentTag = parser.getEventType();

		while (!endCollection){
			switch(currentTag){
			case IXmlStreamReader.START_ELEMENT:
				if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_NAME)){
					parser.next();
					handler.getContentHandler().addNameToLayer(parser.getText(), layer);
				}else if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_DESCRIPTION)){
					parser.next();
					handler.getContentHandler().addDescriptionToLayer(parser.getText(), layer);
				}else if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_BOUNDEDBY)){
					Object bbox = handler.getProfile().getBoundedByTypeBinding().
							parse(parser, handler);
					handler.getContentHandler().addBboxToLayer(bbox, layer);
				}else if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_FEATUREMEMBER)){
					GMLObject object = (GMLObject)handler.getProfile().getFeatureMemberTypeBinding().
							parse(parser, handler);
				
					if (object != null){
						if (GMLObject.FEATURE == object.getType()){
							handler.getContentHandler().addFeatureToLayer(object.getObject(), layer);
						}else{
							handler.getContentHandler().addParentLayerToLayer(layer, object.getObject());
						}
					}
				}else if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_FEATUREMEMBERS)){
					handler.getProfile().getFeatureMembersTypeBinding().
					parse(parser, handler, layer);
				}else{
					if (!(CompareUtils.compareWithNamespace(layerRootType,tag))){						
						//Feature members
						GMLObject object =(GMLObject)handler.getProfile().getFeatureTypeBinding().parse(parser, handler);
						handler.getContentHandler().addFeatureToLayer(object.getObject(), layer);
					}
				}
				break;
			case IXmlStreamReader.END_ELEMENT:
				if (CompareUtils.compareWithNamespace(tag,layerRootType)){						
					endCollection = true;
					handler.getContentHandler().endLayer(layer);
				}
				break;
			case IXmlStreamReader.CHARACTERS:					

				break;
			case IXmlStreamReader.END_DOCUMENT:
				endCollection = true;
				break;
			}
			if (!endCollection){					
				currentTag = parser.next();
				tag = parser.getName();
			}
		}			
		return layer;	
	}
}
