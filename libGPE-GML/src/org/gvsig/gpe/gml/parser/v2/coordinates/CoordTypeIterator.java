package org.gvsig.gpe.gml.parser.v2.coordinates;

import java.io.IOException;

import javax.xml.namespace.QName;

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
 * $Id:CoordTypeBinding.java 371 2008-01-10 09:30:19Z jpiera $
 * $Log$
 * Revision 1.2  2007/05/14 09:31:06  jorpiell
 * Add the a new class to compare tags
 *
 * Revision 1.1  2007/05/07 12:58:42  jorpiell
 * Add some methods to manage the multigeometries
 *
 *
 */
/**
 * It parses a gml:CoordType object. Example:
 * <p>
 * <pre>
 * <code>
 * &lt;gml:coord&gt;&lt;gml:X&gt;0&lt;/gml:X&gt;&lt;gml:Y&gt;0&lt;/gml:Y&gt;&lt;/gml:coord&gt;
 * </code>
 * </pre>
 * </p> 
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class CoordTypeIterator extends GmlCoodinatesIterator{
		
	/**
	 * It parses the gml:coord tag
	 * @param parser
	 * The XML parser
	 * @param handler
	 * The GPE parser that contains the content handler and
	 * the error handler
	 * @return
	 * It retuns an array of doubles with 3 values (x,y,z)
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
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.parser.ICoordinateIterator#getDimension()
	 */
	public int getDimension() {
		//TODO gets the dimension!!!
		return 3;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.parser.ICoordinateIterator#hasNext()
	 */
	public boolean hasNext() throws IOException {
		QName tag = parser.getName();
		
		if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_COORD)){		
			return true;
		}
		parseAll();
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.parser.ICoordinateIterator#next(double[])
	 */
	public void next(double[] buffer) throws IOException {
		boolean endCoord = false;
		int currentTag;			
				
		QName tag = parser.getName();
		currentTag = parser.getEventType();
		
		while (!endCoord){
			switch(currentTag){
			case IXmlStreamReader.START_ELEMENT:
				if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_X)){
					parser.next();		
					buffer[0] = DoubleTypeBinding.parse(parser.getText(),COORDINATES_DECIMAL);
				}else if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_Y)){
					parser.next();		
					buffer[1] = DoubleTypeBinding.parse(parser.getText(),COORDINATES_DECIMAL);
				}else if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_Z)){
					parser.next();		
					buffer[2] = DoubleTypeBinding.parse(parser.getText(),COORDINATES_DECIMAL);
				}
				break;
			case IXmlStreamReader.END_ELEMENT:
				if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_COORD)){						
					//Advance until netx XML tag
					parser.nextTag();
					endCoord = true;
				}
				break;
			case IXmlStreamReader.CHARACTERS:					

				break;
			}
			if (!endCoord){					
				currentTag = parser.next();
				tag = parser.getName();
			}
		}				
	}	
}
