package org.gvsig.gpe.gml.parser.sfp0.geometries;

import java.io.IOException;

import javax.xml.namespace.QName;

import org.gvsig.gpe.gml.parser.GPEDefaultGmlParser;
import org.gvsig.gpe.gml.parser.sfp0.coordinates.PosTypeIterator;
import org.gvsig.gpe.gml.parser.v2.coordinates.CoordTypeIterator;
import org.gvsig.gpe.gml.parser.v2.coordinates.CoordinatesTypeIterator;
import org.gvsig.gpe.gml.parser.v2.geometries.GeometryBinding;
import org.gvsig.gpe.gml.utils.GMLTags;
import org.gvsig.gpe.xml.stream.IXmlStreamReader;
import org.gvsig.gpe.xml.stream.XmlStreamException;
import org.gvsig.gpe.xml.utils.CompareUtils;

public class EnvelopeTypeBinding extends GeometryBinding{
	
	public Object parse(IXmlStreamReader parser,GPEDefaultGmlParser handler) throws XmlStreamException, IOException {
		boolean endFeature = false;
		int currentTag;		
		Object bbox = null;		
		
		super.setAtributtes(parser, handler.getErrorHandler());
		
		QName tag = parser.getName();
		currentTag = parser.getEventType();

		while (!endFeature){
			switch(currentTag){
			case IXmlStreamReader.START_ELEMENT:
					// deprecated with GML version 3.1.0
					if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_COORDINATES)){
						CoordinatesTypeIterator coordinatesIterator = handler.getProfile().getCoordinatesTypeBinding();
						coordinatesIterator.initialize(parser, handler,GMLTags.GML_ENVELOPE);
						bbox = handler.getContentHandler().startBbox(id,
								coordinatesIterator,
								srsName);
					}// deprecated with GML version 3.0
					else if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_COORD)){
						CoordTypeIterator coordinatesIterator = handler.getProfile().getCoordTypeBinding();
						coordinatesIterator.initialize(parser, handler,GMLTags.GML_ENVELOPE);
						bbox = handler.getContentHandler().startBbox(id,
								coordinatesIterator,
								srsName);
					}// deprecated with GML version 3.1
					else if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_POS)){
						PosTypeIterator coordinatesIterator = handler.getProfile().getPosTypeBinding();
						coordinatesIterator.initialize(parser, handler,GMLTags.GML_POS);
						bbox = handler.getContentHandler().startBbox(id,
								coordinatesIterator,								
								srsName);	
						//The parser pointer is in the </gml:Envelope> tag
					 	return bbox;
					}
					else if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_LOWERCORNER)){
						LowerCornerTypeBinding coordinatesIterator = handler.getProfile().getLowerCornerTypeBinding();
						coordinatesIterator.parse(parser, handler);
						bbox = handler.getContentHandler().startBbox(id,
								coordinatesIterator,								
								srsName);
					}					
					break;
				case IXmlStreamReader.END_ELEMENT:
					if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_ENVELOPE)){						
						endFeature = true;						
						handler.getContentHandler().endBbox(bbox);
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
		return bbox;	
	}
}
