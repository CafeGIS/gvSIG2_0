package org.gvsig.gpe.gml.parser.sfp0.geometries;

import java.io.IOException;

import javax.xml.namespace.QName;

import org.gvsig.gpe.gml.parser.GPEDefaultGmlParser;
import org.gvsig.gpe.gml.parser.sfp0.coordinates.PosListTypeIterator;
import org.gvsig.gpe.gml.parser.v2.geometries.GeometryBinding;
import org.gvsig.gpe.gml.utils.GMLTags;
import org.gvsig.gpe.xml.stream.IXmlStreamReader;
import org.gvsig.gpe.xml.stream.XmlStreamException;
import org.gvsig.gpe.xml.utils.CompareUtils;

public class LinestringSegmentTypeBinding extends GeometryBinding{
	public Object parse(IXmlStreamReader parser,GPEDefaultGmlParser handler) throws XmlStreamException, IOException {
		boolean endFeature = false;
		int currentTag;
		Object linestringsegment = null;
		
		/********************************************************************
		 * Falta ver como pasar la información del tipo de interpolacion	*
		 * en el ContentHandler o en un hashtable de info para el usuario	*
		 * de la libreria, o crear una entrada de ContentHandler para cada 	*
		 * uno.																*			
		 ********************************************************************/
		String interpolacion = GMLTags.GML_DEFAULT_INTERPOLATION;
		
		for (int i=0 ; i<parser.getAttributeCount() ; i++){
			if (CompareUtils.compareWithNamespace(parser.getAttributeName(i),GMLTags.GML_INTERPOLATION)){
			    interpolacion = parser.getAttributeValue(i);
			}
		}
		
		super.setAtributtes(parser, handler.getErrorHandler());

		QName tag = parser.getName();
		currentTag = parser.getEventType();

		while (!endFeature){
			switch(currentTag){
			case IXmlStreamReader.START_ELEMENT:
				if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_POSLIST)){
					PosListTypeIterator coordinatesIterator = handler.getProfile().getPosListTypeBinding();
					coordinatesIterator.initialize(parser, handler,GMLTags.GML_LINESTRINGSEGMENT);
					linestringsegment = handler.getContentHandler().startLineString(id,
							coordinatesIterator,								
							srsName);			
				}
				break;
			case IXmlStreamReader.END_ELEMENT:
				if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_LINESTRINGSEGMENT)){	
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
		return linestringsegment;	
	}
}
