package org.gvsig.gpe.kml.parser.v21.coordinates;

import java.io.IOException;
import java.util.StringTokenizer;

import javax.xml.namespace.QName;

import org.gvsig.gpe.kml.parser.GPEDeafultKmlParser;
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
public abstract class KmlCoodinatesIterator implements IKmlCoordinatesIterator {
	protected IXmlStreamReader parser = null;
	protected GPEDeafultKmlParser handler = null;
	protected QName lastTag = null;
	protected int dimension = 0;
	protected StringTokenizer coordinatesString = null;
	
	/* (non-Javadoc)
	 * @see org.gvsig.gpe.gml.parser.IGmlCoordinatesIterator#initialize(org.gvsig.gpe.xml.stream.IXmlStreamReader, org.gvsig.gpe.gml.parser.GPEDefaultGmlParser, java.lang.String)
	 */
	public void initialize(IXmlStreamReader parser,
			GPEDeafultKmlParser handler, QName lastTag)
			throws XmlStreamException, IOException {		
		this.parser = parser;
		this.handler = handler;
		this.lastTag = lastTag;
		this.dimension = -1;	
	}

	
	/* (non-Javadoc)
	 * @see org.gvsig.gpe.parser.ICoordinateIterator#getDimension()
	 */
	public int getDimension() {
		return dimension;
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
}
