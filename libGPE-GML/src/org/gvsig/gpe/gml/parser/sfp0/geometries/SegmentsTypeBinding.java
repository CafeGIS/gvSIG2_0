package org.gvsig.gpe.gml.parser.sfp0.geometries;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.namespace.QName;

import org.gvsig.gpe.gml.parser.GPEDefaultGmlParser;
import org.gvsig.gpe.gml.utils.GMLTags;
import org.gvsig.gpe.xml.stream.IXmlStreamReader;
import org.gvsig.gpe.xml.stream.XmlStreamException;
import org.gvsig.gpe.xml.utils.CompareUtils;

public class SegmentsTypeBinding{
	
	public ArrayList parse(IXmlStreamReader parser,GPEDefaultGmlParser handler) throws XmlStreamException, IOException {
		boolean endFeature = false;
		int currentTag;
		ArrayList segments = new ArrayList();		

		QName tag = parser.getName();
		currentTag = parser.getEventType();

		while (!endFeature){
			switch(currentTag){
			case IXmlStreamReader.START_ELEMENT:
				if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_LINESTRINGSEGMENT)){
					segments.add(handler.getProfile().getLinestringSegmentTypeBinding().
					parse(parser, handler));
				}else if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_LINESTRING)){
					segments.add(handler.getProfile().getLineStringTypeBinding().
					parse(parser, handler));
				}
				break;
			case IXmlStreamReader.END_ELEMENT:
				if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_SEGMENTS)){	
					endFeature = true;
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
		return segments;	
	}
}
