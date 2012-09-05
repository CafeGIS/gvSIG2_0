package org.gvsig.gpe.gml.parser.v2.coordinates;

import java.io.IOException;
import java.util.StringTokenizer;

import javax.xml.namespace.QName;

import org.gvsig.compat.CompatLocator;
import org.gvsig.gpe.gml.parser.GPEDefaultGmlParser;
import org.gvsig.gpe.gml.parser.v2.geometries.DoubleTypeBinding;
import org.gvsig.gpe.gml.utils.GMLTags;
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
/* CVS MESSAGES:
*
* $Id:CoordinatesTypeBinding.java 371 2008-01-10 09:30:19Z jpiera $
* $Log$
* Revision 1.3  2007/05/14 09:52:53  jorpiell
* Nullpointer exception fixed
*
* Revision 1.2  2007/05/14 09:31:06  jorpiell
* Add the a new class to compare tags
*
* Revision 1.1  2007/05/07 12:58:42  jorpiell
* Add some methods to manage the multigeometries
*
* Revision 1.1  2007/05/07 07:06:46  jorpiell
* Add a constructor with the name and the description fields
*
*
*/
/**
 * It parses a gml:CoordinatesType object. Example:
 * <p>
 * <pre>
 * <code>
 * &lt;coordinates&gt;60.0,60.0 60.0,90.0 90.0,90.0&lt;/coordinates&gt;
 * </code>
 * </pre>
 * </p> 
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class CoordinatesTypeIterator extends GmlCoodinatesIterator{
	private StringTokenizer coordinatesString = null;	
	
	/**
	 * It parses the gml:coordinates tag
	 * @param parser
	 * The XML parser
	 * @param handler
	 * The GPE parser that contains the content handler and
	 * the error handler
	 * @return
	 * It retuns a matrix of doubles with 3 columns (x,y,z) and
	 * one row for each coordinate.
	 * @throws XmlStreamException
	 * @throws IOException
	 */
	/* (non-Javadoc)
	 * @see org.gvsig.gpe.gml.parser.v2.coordinates.GmlCoodinatesIterator#initialize(org.gvsig.gpe.xml.stream.IXmlStreamReader, org.gvsig.gpe.gml.parser.GPEDefaultGmlParser, java.lang.String)
	 */
	public void initialize(IXmlStreamReader parser,
			GPEDefaultGmlParser handler, QName lastTag)
			throws XmlStreamException, IOException {
		super.initialize(parser, handler, lastTag);
		
		boolean endFeature = false;
		int currentTag;				

		QName tag = parser.getName();
		currentTag = parser.getEventType();

		while (!endFeature){
			switch(currentTag){
			case IXmlStreamReader.START_ELEMENT:
				if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_COORDINATES)){
					parser.next();						
					String coordinatesTag = parser.getText().trim();
					if (dimension == -1){
						dimension = caculateDimension(coordinatesTag);
					}						
					coordinatesString = new StringTokenizer(coordinatesTag, TUPLES_SEPARATOR);
					//Parses until the last tag...
					parseAll();
					return;
				}
				break;
			case IXmlStreamReader.END_ELEMENT:
				if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_COORDINATES)){						
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
		String firstPair;
		StringTokenizer st = new StringTokenizer(coordinatesTag,TUPLES_SEPARATOR);
		if (st.hasMoreTokens()){
			firstPair = st.nextToken();
		}else{			
			firstPair = coordinatesTag;
		}
		return CompatLocator.getStringUtils().split(firstPair, COORDINATES_SEPARATOR).length;
		//return StringUtils.splitString(firstPair, COORDINATES_SEPARATOR).length;		
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
		String[] coordinates = CompatLocator.getStringUtils().split(next.trim(),COORDINATES_SEPARATOR);
		//String[] coordinates = org.gvsig.gpe.utils.StringUtils.splitString(next.trim(),COORDINATES_SEPARATOR);
		for (int i=0 ; i<coordinates.length ; i++){
			buffer[i] = DoubleTypeBinding.parse(coordinates[i],COORDINATES_DECIMAL);
		}	
	}


}
