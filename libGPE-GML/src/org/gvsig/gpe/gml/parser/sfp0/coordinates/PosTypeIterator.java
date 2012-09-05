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

public class PosTypeIterator extends GmlCoodinatesIterator{

	/* (non-Javadoc)
	 * @see org.gvsig.gpe.gml.parser.v2.coordinates.GmlCoodinatesIterator#initialize(org.gvsig.gpe.xml.stream.IXmlStreamReader, org.gvsig.gpe.gml.parser.GPEDefaultGmlParser, java.lang.String)
	 */
	public void initialize(IXmlStreamReader parser,
			GPEDefaultGmlParser handler, QName lastTag)
	throws XmlStreamException, IOException {
		// TODO Auto-generated method stub
		super.initialize(parser, handler, lastTag);			
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.parser.ICoordinateIterator#hasNext()
	 */
	public boolean hasNext() throws IOException {
		QName tag = parser.getName();

		if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_POS)){		
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
		parser.next();

		StringTokenizer coordinatesString = new StringTokenizer(parser.getText().trim(),TUPLES_SEPARATOR);
		int i=0;

		while(coordinatesString.hasMoreTokens() && i<buffer.length){
			buffer[i] = DoubleTypeBinding.parse(coordinatesString.nextToken(),COORDINATES_DECIMAL);
			i++;
		}	
		//Advance until the end pos label
		boolean endPos = false;
		while (!endPos){
			parser.next();
			if (CompareUtils.compareWithNamespace(GMLTags.GML_POS,parser.getName())){
				if (parser.getEventType() == IXmlStreamReader.END_ELEMENT){
					continue;
				}else if (parser.getEventType() == IXmlStreamReader.START_ELEMENT){
					endPos = true;
				}					
			}else{
				if (parser.getEventType() == IXmlStreamReader.END_ELEMENT){
					endPos = true;
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.gvsig.gpe.gml.parser.v2.coordinates.GmlCoodinatesIterator#getDimension()
	 */
	public int getDimension() {
		//TODO Calculate the dimension
		if (dimension == -1){
			dimension = 3;
		}
		return dimension;
	}	
	
	
}
