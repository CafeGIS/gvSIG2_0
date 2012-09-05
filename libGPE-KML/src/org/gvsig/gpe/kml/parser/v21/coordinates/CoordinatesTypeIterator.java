package org.gvsig.gpe.kml.parser.v21.coordinates;

import java.io.IOException;
import java.util.StringTokenizer;

import javax.xml.namespace.QName;

import org.gvsig.compat.CompatLocator;
import org.gvsig.gpe.kml.parser.GPEDeafultKmlParser;
import org.gvsig.gpe.kml.utils.Kml2_1_Tags;
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
 * $Id:CoordinatesTypeBinding.java 357 2008-01-09 17:50:08Z jpiera $
 * $Log$
 * Revision 1.2  2007/06/07 14:53:59  jorpiell
 * Add the schema support
 *
 * Revision 1.1  2007/05/11 07:06:29  jorpiell
 * Refactoring of some package names
 *
 * Revision 1.4  2007/05/09 08:36:24  jorpiell
 * Add the bbox to the layer
 *
 * Revision 1.3  2007/05/08 08:22:37  jorpiell
 * Add comments to create javadocs
 *
 * Revision 1.2  2007/04/14 16:08:07  jorpiell
 * Kml writing support added
 *
 * Revision 1.1  2007/04/13 13:16:21  jorpiell
 * Add KML reading support
 *
 * Revision 1.1  2007/03/07 08:19:10  jorpiell
 * Pasadas las clases de KML de libGPE-GML a libGPE-KML
 *
 * Revision 1.1  2007/02/28 11:48:31  csanchez
 * *** empty log message ***
 *
 * Revision 1.1  2007/02/20 10:53:20  jorpiell
 * AÃ±adidos los proyectos de kml y gml antiguos
 *
 * Revision 1.1  2007/02/12 13:49:18  jorpiell
 * Añadido el driver de KML
 *
 *
 */
/**
 * This class parses a coordinates tag. Example:
 * <p>
 * <pre>
 * <code>
 * &lt;coordinates&gt;60.0,60.0 60.0,90.0 90.0,90.0&lt;/coordinates&gt;
 * </code>
 * </pre>
 * </p> 
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 * @see http://code.google.com/apis/kml/documentation/kml_tags_21.html#coordinates
 */
public class CoordinatesTypeIterator extends KmlCoodinatesIterator{
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
			GPEDeafultKmlParser handler, QName lastTag)
			throws XmlStreamException, IOException {
		super.initialize(parser, handler, lastTag);
		
		boolean endFeature = false;
		int currentTag;				

		QName tag = parser.getName();
		currentTag = parser.getEventType();

		while (!endFeature){
			switch(currentTag){
			case IXmlStreamReader.START_ELEMENT:
				if (CompareUtils.compareWithOutNamespace(tag,Kml2_1_Tags.COORDINATES)){
					parser.next();						
					String coordinatesTag = parser.getText().trim();
					if (dimension == -1){
						dimension = caculateDimension(coordinatesTag);
					}						
					coordinatesString = new StringTokenizer(coordinatesTag);
					//Parses until the last tag...
					parseAll();
					return;
				}
				break;
			case IXmlStreamReader.END_ELEMENT:
				if (CompareUtils.compareWithOutNamespace(tag,Kml2_1_Tags.COORDINATES)){						
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
		StringTokenizer st = new StringTokenizer(coordinatesTag);
		if (st.hasMoreTokens()){
			firstPair = st.nextToken();
		}else{			
			firstPair = coordinatesTag;
		}
		return CompatLocator.getStringUtils().split(firstPair, Kml2_1_Tags.COORDINATES_SEPARATOR).length;
		//return StringUtils.splitString(firstPair, Kml2_1_Tags.COORDINATES_SEPARATOR).length;		
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
		String[] coordinates = CompatLocator.getStringUtils().split(next.trim(),Kml2_1_Tags.COORDINATES_SEPARATOR);
		//String[] coordinates = org.gvsig.gpe.utils.StringUtils.splitString(next.trim(),Kml2_1_Tags.COORDINATES_SEPARATOR);
		for (int i=0 ; i<coordinates.length ; i++){
			buffer[i] = handler.getProfile().getDoubleBinding().parse(coordinates[i]);
		}	
	}


}
