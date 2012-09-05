package org.gvsig.gpe.gml.parser.sfp0.geometries;

import java.io.IOException;
import java.util.StringTokenizer;

import javax.xml.namespace.QName;

import org.gvsig.compat.CompatLocator;
import org.gvsig.gpe.gml.parser.GPEDefaultGmlParser;
import org.gvsig.gpe.gml.parser.v2.geometries.DoubleTypeBinding;
import org.gvsig.gpe.gml.utils.GMLTags;
import org.gvsig.gpe.parser.ICoordinateIterator;
import org.gvsig.gpe.xml.stream.IXmlStreamReader;
import org.gvsig.gpe.xml.stream.XmlStreamException;
import org.gvsig.gpe.xml.utils.CompareUtils;

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

public class LowerCornerTypeBinding implements ICoordinateIterator{
	private String TUPLES_SEPARATOR = GMLTags.GML_DEFAULT_TUPLES_SEPARATOR;
	private String COORDINATES_DECIMAL = GMLTags.GML_DEFAULT_COORDINATES_DECIMAL;	
	private StringTokenizer coordinatesString = null;
	private int dimension = 0;
	private IXmlStreamReader parser = null;
	
	public void parse(IXmlStreamReader parser,GPEDefaultGmlParser handler) throws XmlStreamException, IOException  {
		boolean endFeature = false;
		int currentTag;		
		this.parser = parser;
		
		QName tag = parser.getName();
		currentTag = parser.getEventType();

		while (!endFeature){
			switch(currentTag){
			case IXmlStreamReader.START_ELEMENT:
				if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_LOWERCORNER)){
					parser.next();	
					String coordinatesTag = parser.getText().trim();
					dimension = caculateDimension(coordinatesTag);
					coordinatesString = new StringTokenizer(coordinatesTag,TUPLES_SEPARATOR);
//					//String[] coordinates = parser.getText().trim().split(TUPLES_SEPARATOR);
//					String[] coordinates = org.gvsig.gpe.gml.utils.StringUtils.splitString(parser.getText().trim(),TUPLES_SEPARATOR);
//					aCoordinates = new double[3];
//					for (int i=0 ; i < coordinates.length; i++){					
//							aCoordinates[i] = DoubleTypeBinding.parse(coordinates[i],".");
//					}
//					if (coordinates.length == 2){
//						aCoordinates[2] = 0.0;
//					}
//					else if (coordinates.length == 1){
//						aCoordinates[1] = 0.0;
//						aCoordinates[2] = 0.0;	
//					}
//					else
//					{
//						aCoordinates[0] = 0.0;
//						aCoordinates[1] = 0.0;
//						aCoordinates[2] = 0.0;
//					}
				}
				break;
			case IXmlStreamReader.END_ELEMENT:
				if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_LOWERCORNER)){						
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
	
	/**
	 * Calculates the dimension from a array of coordinates
	 * @param coordinatesTag
	 * @return
	 */
	private int caculateDimension(String coordinatesTag) {
		return CompatLocator.getStringUtils().split(coordinatesTag, TUPLES_SEPARATOR).length;
		//return StringUtils.splitString(coordinatesTag, TUPLES_SEPARATOR).length;		
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.parser.ICoordinateIterator#getDimension()
	 */
	public int getDimension() {
		return dimension;
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
		String next = coordinatesString.nextToken();
		for (int i=0 ; i<dimension ; i++){
			buffer[i] = DoubleTypeBinding.parse(next,COORDINATES_DECIMAL);
			if (coordinatesString.hasMoreTokens()){
				next = coordinatesString.nextToken();
			}
		}
		parseLowerCorner();		
	}
	
	/**
	 * It goes to the lower corner end tag
	 * @throws XmlStreamException
	 */
	private void parseLowerCorner() throws XmlStreamException{
		parser.next();
		QName tag = parser.getName();
		int currentTag = parser.getEventType();
		if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_LOWERCORNER)){
			parser.next();
			String coordinatesTag = parser.getText().trim();
			coordinatesString = new StringTokenizer(coordinatesTag,TUPLES_SEPARATOR);
			boolean endLowerCorner = false;
			while (!endLowerCorner){
				switch(currentTag){
				case IXmlStreamReader.END_ELEMENT:
					if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_LOWERCORNER)){						
						endLowerCorner = true;
					}
					break;
				}
				if (!endLowerCorner){					
					currentTag = parser.next();
					tag = parser.getName();
				}
			}
		}
	}
}
