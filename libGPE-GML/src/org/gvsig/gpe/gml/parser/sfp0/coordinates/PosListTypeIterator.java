package org.gvsig.gpe.gml.parser.sfp0.coordinates;

import java.io.IOException;
import java.util.StringTokenizer;

import javax.xml.namespace.QName;

import org.gvsig.gpe.gml.parser.GPEDefaultGmlParser;
import org.gvsig.gpe.gml.parser.v2.coordinates.GmlCoodinatesIterator;
import org.gvsig.gpe.gml.parser.v2.geometries.DoubleTypeBinding;
import org.gvsig.gpe.gml.utils.GMLTags;
import org.gvsig.gpe.xml.stream.IXmlStreamReader;
import org.gvsig.gpe.xml.stream.XmlStreamException;
import org.gvsig.gpe.xml.utils.CompareUtils;

public class PosListTypeIterator extends GmlCoodinatesIterator{
	private StringTokenizer coordinatesString = null;
	
	/* (non-Javadoc)
	 * @see org.gvsig.gpe.gml.parser.v2.coordinates.GmlCoodinatesIterator#initialize(org.gvsig.gpe.xml.stream.IXmlStreamReader, org.gvsig.gpe.gml.parser.GPEDefaultGmlParser, java.lang.String)
	 */
	public void initialize(IXmlStreamReader parser,
			GPEDefaultGmlParser handler, QName lastTag)
			throws XmlStreamException, IOException {
		// TODO Auto-generated method stub
		super.initialize(parser, handler, lastTag);	
		boolean endFeature = false;
		int currentTag;	

		for (int i=0 ; i<parser.getAttributeCount() ; i++){
			if (CompareUtils.compareWithNamespace(parser.getAttributeName(i),GMLTags.GML_DIMENSION)){
				dimension = Integer.valueOf(parser.getAttributeValue(i)).intValue();
			}
		}	
		
		if (dimension == -1){
			dimension = 2;
		}

		QName tag = parser.getName();
		currentTag = parser.getEventType();

		while (!endFeature){
			switch(currentTag){
			case IXmlStreamReader.START_ELEMENT:
				if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_POSLIST)){
					parser.next();						
					coordinatesString = new StringTokenizer(parser.getText().trim(),TUPLES_SEPARATOR);
				}
				break;
			case IXmlStreamReader.END_ELEMENT:
				if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_POSLIST)){						
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
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.parser.ICoordinateIterator#hasNext()
	 */
	public boolean hasNext() throws IOException {
		return coordinatesString.hasMoreTokens();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.parser.ICoordinateIterator#next(double[])
	 */
	public void next(double[] buffer) throws IOException {
		String next = null;
		for (int i=0 ; i<dimension ; i++){
			if (i != 0){
				if (coordinatesString.hasMoreTokens()){
					next = coordinatesString.nextToken();
				}else{
					return;
				}
			}else{
				next = coordinatesString.nextToken();
			}
			buffer[i] = DoubleTypeBinding.parse(next,COORDINATES_DECIMAL);
		}
	}
}
