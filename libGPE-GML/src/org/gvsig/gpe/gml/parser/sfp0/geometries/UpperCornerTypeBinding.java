package org.gvsig.gpe.gml.parser.sfp0.geometries;

import java.io.IOException;

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

public class UpperCornerTypeBinding {
		
	public double[] parse(IXmlStreamReader parser,GPEDefaultGmlParser handler) throws XmlStreamException, IOException  {
		boolean endFeature = false;
		int currentTag;
		
		String TUPLES_SEPARATOR = GMLTags.GML_DEFAULT_TUPLES_SEPARATOR;
			
		double[] aCoordinates = null;
		String[] coordinate = null;
		
		QName tag = parser.getName();
		currentTag = parser.getEventType();

		while (!endFeature){
			switch(currentTag){
			case IXmlStreamReader.START_ELEMENT:
				if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_UPPERCORNER)){
					parser.next();
					String[] coordinates = CompatLocator.getStringUtils().split(parser.getText().trim(),TUPLES_SEPARATOR);
					//String[] coordinates = org.gvsig.gpe.utils.StringUtils.splitString(parser.getText().trim(),TUPLES_SEPARATOR);
					//String[] coordinates = parser.getText().trim().split(TUPLES_SEPARATOR);
					aCoordinates = new double[3];
					for (int i=0 ; i < coordinates.length; i++){					
							aCoordinates[i] = DoubleTypeBinding.parse(coordinates[i],".");
					}
					if (coordinates.length == 2){
						aCoordinates[2] = 0.0;
					}
					else if (coordinates.length == 1){
						aCoordinates[1] = 0.0;
						aCoordinates[2] = 0.0;	
					}
					else
					{
						aCoordinates[0] = 0.0;
						aCoordinates[1] = 0.0;
						aCoordinates[2] = 0.0;
					}
				}
				break;
			case IXmlStreamReader.END_ELEMENT:
				if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_UPPERCORNER)){						
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
		return aCoordinates;	
	}
}

