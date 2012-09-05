package org.gvsig.gpe.gml.parser.sfp0.geometries;

import java.io.IOException;

import javax.xml.namespace.QName;

import org.gvsig.gpe.gml.parser.GPEDefaultGmlParser;
import org.gvsig.gpe.gml.parser.GPEGml2_1_2_Parser;
import org.gvsig.gpe.gml.utils.GMLTags;
import org.gvsig.gpe.xml.stream.IXmlStreamReader;
import org.gvsig.gpe.xml.stream.XmlStreamException;
import org.gvsig.gpe.xml.utils.CompareUtils;

public class CurveMemberTypeBinding {

		public static Object parse(IXmlStreamReader parser,GPEDefaultGmlParser handler) throws XmlStreamException, IOException {
			boolean endFeature = false;
			int currentTag;
			Object curve = null;		
			
			QName tag = parser.getName();
			currentTag = parser.getEventType();

			while (!endFeature){
				switch(currentTag){
				case IXmlStreamReader.START_ELEMENT:
						if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_CURVE)){
							curve = handler.getProfile().getCurveTypeBinding().
							parse(parser, handler);
						}else if (CompareUtils.compareWithNamespace(tag, GMLTags.GML_LINESTRING)){
							curve = handler.getContentHandler().startCurve(null, null);
							Object lineString = handler.getProfile().getLineStringTypeBinding().
							parse(parser, handler);
							handler.getContentHandler().addSegmentToCurve(lineString, curve);
						}
						break;
					case IXmlStreamReader.END_ELEMENT:
						if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_CURVEMEMBER)){						
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
			return curve;	
		}
}
