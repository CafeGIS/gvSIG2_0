package org.gvsig.gpe.gml.parser.sfp0.geometries;

import java.io.IOException;

import javax.xml.namespace.QName;

import org.gvsig.gpe.gml.parser.GPEDefaultGmlParser;
import org.gvsig.gpe.gml.parser.GPEGml2_1_2_Parser;
import org.gvsig.gpe.gml.parser.v2.geometries.GeometryBinding;
import org.gvsig.gpe.gml.utils.GMLTags;
import org.gvsig.gpe.xml.stream.IXmlStreamReader;
import org.gvsig.gpe.xml.stream.XmlStreamException;
import org.gvsig.gpe.xml.utils.CompareUtils;

public class MultiCurveTypeBinding extends GeometryBinding{
	
	/**************************
	 * Parse the object curve
	 **************************/
	public Object parse(IXmlStreamReader parser,GPEDefaultGmlParser handler) throws XmlStreamException, IOException {
		boolean endFeature = false;
		int currentTag;
		Object multiCurve = null;

		super.setAtributtes(parser, handler.getErrorHandler());
		
		//Taking the next tag
		multiCurve = handler.getContentHandler().startMultiCurve(id, srsName);
		
		QName tag = parser.getName();
		currentTag = parser.getEventType();

		while (!endFeature){
			switch(currentTag){
			case IXmlStreamReader.START_ELEMENT:
					if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_CURVEMEMBER)){
						Object curve = CurveMemberTypeBinding.parse(parser, handler);
						handler.getContentHandler().addCurveToMultiCurve(curve, multiCurve);
					}
					break;
				case IXmlStreamReader.END_ELEMENT:
					if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_MULTICURVE)){						
						endFeature = true;	
						handler.getContentHandler().endMultiCurve(multiCurve);
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
		return multiCurve;	
	}
}
