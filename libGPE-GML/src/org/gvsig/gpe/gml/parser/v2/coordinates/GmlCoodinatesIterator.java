package org.gvsig.gpe.gml.parser.v2.coordinates;

import java.io.IOException;
import java.util.StringTokenizer;

import javax.xml.namespace.QName;

import org.gvsig.gpe.gml.parser.GPEDefaultGmlParser;
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
 * $Id$
 * $Log$
 *
 */
/**
 * @author Jorge Piera Llodrá (jorge.piera@iver.es)
 */
public abstract class GmlCoodinatesIterator implements IGmlCoordinatesIterator {
	protected IXmlStreamReader parser = null;
	protected GPEDefaultGmlParser handler = null;
	protected QName lastTag = null;
	protected int dimension = 0;
	protected StringTokenizer coordinatesString = null;
	protected String TUPLES_SEPARATOR  = null;
	protected String COORDINATES_SEPARATOR = null;
	protected String COORDINATES_DECIMAL = null;
	
	/* (non-Javadoc)
	 * @see org.gvsig.gpe.gml.parser.IGmlCoordinatesIterator#initialize(org.gvsig.gpe.xml.stream.IXmlStreamReader, org.gvsig.gpe.gml.parser.GPEDefaultGmlParser, java.lang.String)
	 */
	public void initialize(IXmlStreamReader parser,
			GPEDefaultGmlParser handler, QName lastTag)
			throws XmlStreamException, IOException {		
		this.parser = parser;
		this.handler = handler;
		this.lastTag = lastTag;
		this.dimension = -1;
		TUPLES_SEPARATOR  = GMLTags.GML_DEFAULT_TUPLES_SEPARATOR;
		COORDINATES_SEPARATOR = GMLTags.GML_DEFAULT_COORDINATES_SEPARATOR;
		COORDINATES_DECIMAL = GMLTags.GML_DEFAULT_COORDINATES_DECIMAL;
		
		for (int i=0 ; i<parser.getAttributeCount() ; i++){
			if (CompareUtils.compareWithNamespace(parser.getAttributeName(i),GMLTags.GML_COORDINATES_DECIMAL)){
				COORDINATES_DECIMAL = parser.getAttributeValue(i);
			}else if (CompareUtils.compareWithNamespace(parser.getAttributeName(i),GMLTags.GML_COORDINATES_CS)){
				COORDINATES_SEPARATOR = parser.getAttributeValue(i);
			}else if (CompareUtils.compareWithNamespace(parser.getAttributeName(i),GMLTags.GML_COORDINATES_TS)){
				TUPLES_SEPARATOR = parser.getAttributeValue(i);
			}else if (CompareUtils.compareWithNamespace(parser.getAttributeName(i),GMLTags.GML_SRSDIMENSION)){
			   	dimension = Integer.valueOf(parser.getAttributeValue(i)).intValue();
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.gvsig.gpe.gml.parser.IGmlCoordinatesIterator#parseAll()
	 */
	public void parseAll() throws XmlStreamException {
		QName tag = parser.getName();
		int currentTag = parser.getEventType();
		boolean endCoordinates = false;
		
		while (!endCoordinates){
			switch(currentTag){
				case IXmlStreamReader.END_ELEMENT:
					if (CompareUtils.compareWithNamespace(tag,lastTag)){
						endCoordinates = true;						
					}
					break;
			}
			if (!endCoordinates){					
				currentTag = parser.next();
				tag = parser.getName();				
			}			
		}	
	}
	
	/* (non-Javadoc)
	 * @see org.gvsig.gpe.parser.ICoordinateIterator#getDimension()
	 */
	public int getDimension() {
		return dimension;
	}
}
