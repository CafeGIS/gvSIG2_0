package org.gvsig.gpe.gml.parser.v2.geometries;

import java.io.IOException;

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
 * $Id:GeometryPropertyTypeBinding.java 371 2008-01-10 09:30:19Z jpiera $
 * $Log$
 * Revision 1.1  2007/05/15 07:30:38  jorpiell
 * Add the geometryProperties tags
 *
 *
 */
/**
 * It parses a gml:geometryProperty object. Example:
 * <p>
 * <pre>
 * <code>
 * &lt;geometryProperty&gt;
 * &lt;Point gid="P6776"&gt;
 * &lt;coord&gt;&lt;X&gt;50.0&lt;/X&gt;&lt;Y&gt;50.0&lt;/Y&gt;&lt;/coord&gt;
 * &lt;/Point&gt;
 * &lt;/geometryProperty&gt;
 * </code>
 * </pre>
 * </p> 
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */


public class GeometryPropertyTypeBinding {
	
	/**
	 * It parses the gml:geometryPropertyr tag
	 * @param parser
	 * The XML parser
	 * @param handler
	 * The GPE parser that contains the content handler and
	 * the error handler
	 * @return
	 * A Geometry
	 * @throws XmlStreamException
	 * @throws IOException
	 */
	public Object parse(IXmlStreamReader parser,GPEDefaultGmlParser handler) throws XmlStreamException, IOException {
		boolean endFeature = false;
		int currentTag;
		Object geometry = null;		
		
		QName tag = parser.getName();
		currentTag = parser.getEventType();

		while (!endFeature){
			switch(currentTag){
			case IXmlStreamReader.START_ELEMENT:
					if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_POINT)){
						geometry = handler.getProfile().getPointTypeBinding().
						parse(parser, handler);
					}else if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_LINESTRING)){
						geometry = handler.getProfile().getLineStringTypeBinding().
						parse(parser, handler);
					}if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_POLYGON)){
						geometry = handler.getProfile().getPolygonTypeBinding().
						parse(parser, handler);
					}
					break;
				case IXmlStreamReader.END_ELEMENT:
					if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_GEOMETRYPROPERTY)){						
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
		return geometry;	
	}
}

