package org.gvsig.gpe.gml.parser.sfp0.geometries;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.namespace.QName;

import org.gvsig.gpe.gml.parser.GPEDefaultGmlParser;
import org.gvsig.gpe.gml.parser.v2.coordinates.CoordTypeIterator;
import org.gvsig.gpe.gml.parser.v2.coordinates.CoordinatesTypeIterator;
import org.gvsig.gpe.gml.parser.v2.geometries.GeometryBinding;
import org.gvsig.gpe.gml.utils.GMLTags;
import org.gvsig.gpe.xml.stream.IXmlStreamReader;
import org.gvsig.gpe.xml.stream.XmlStreamException;
import org.gvsig.gpe.xml.utils.CompareUtils;

public class CurveTypeBinding extends GeometryBinding{

	public Object parse(IXmlStreamReader parser,GPEDefaultGmlParser handler) throws XmlStreamException, IOException {
		boolean endFeature = false;
		int currentTag;
		Object curve = null;

		super.setAtributtes(parser, handler.getErrorHandler());

		QName tag = parser.getName();
		currentTag = parser.getEventType();

		while (!endFeature){
			switch(currentTag){
			case IXmlStreamReader.START_ELEMENT:
				if(CompareUtils.compareWithNamespace(tag,GMLTags.GML_COORDINATES)){
					CoordinatesTypeIterator coordinatesIterator = handler.getProfile().getCoordinatesTypeBinding();
					coordinatesIterator.initialize(parser, handler,GMLTags.GML_CURVE);
					curve = handler.getContentHandler().startCurve(id,
							coordinatesIterator,								
							srsName);
					if (curve != null){
				 		return curve;
				 	}
				}else if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_COORD)){
					CoordTypeIterator coordinatesIterator = handler.getProfile().getCoordTypeBinding();
					coordinatesIterator.initialize(parser, handler,GMLTags.GML_CURVE);
					curve = handler.getContentHandler().startCurve(id,
							coordinatesIterator,								
							srsName);	
					if (curve != null){
				 		return curve;
				 	}
				}else if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_SEGMENTS)){
					curve = handler.getContentHandler().startCurve(id, srsName);
					ArrayList segments = handler.getProfile().getSegmentsTypeBinding().
					parse(parser, handler);
					for (int i=0 ; i<segments.size() ; i++){
						handler.getContentHandler().addSegmentToCurve(
								segments.get(i), curve);
					}
					if (curve != null){
				 		return curve;
				 	}
				}
				break;
			case IXmlStreamReader.END_ELEMENT:
				if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_CURVE)){	
					endFeature = true;
					handler.getContentHandler().endCurve(curve);
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
