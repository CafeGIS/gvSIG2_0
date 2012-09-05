package org.gvsig.gpe.gml.parser.v2.geometries;

import java.io.IOException;

import javax.xml.namespace.QName;

import org.gvsig.gpe.gml.parser.GPEDefaultGmlParser;
import org.gvsig.gpe.gml.parser.v2.coordinates.CoordTypeIterator;
import org.gvsig.gpe.gml.parser.v2.coordinates.CoordinatesTypeIterator;
import org.gvsig.gpe.gml.utils.GMLTags;
import org.gvsig.gpe.xml.stream.IXmlStreamReader;
import org.gvsig.gpe.xml.stream.XmlStreamException;
import org.gvsig.gpe.xml.utils.CompareUtils;

/* gvSIG. Sistema de InformaciÛn Geogr·fica de la Generalitat Valenciana
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
 *   Av. Blasco Ib·Òez, 50
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
 * $Id:LineStringTypeBinding.java 195 2007-11-26 09:02:22Z jpiera $
 * $Log$
 * Revision 1.6  2007/05/18 10:41:01  csanchez
 * Actualizaci√≥n libGPE-GML eliminaci√≥n de clases inecesarias
 *
 * Revision 1.5  2007/05/14 11:18:51  jorpiell
 * ProjectionFactory updated
 *
 * Revision 1.4  2007/05/14 09:31:06  jorpiell
 * Add the a new class to compare tags
 *
 * Revision 1.3  2007/05/08 10:24:16  jorpiell
 * Add comments to create javadocs
 *
 * Revision 1.2  2007/05/07 12:58:42  jorpiell
 * Add some methods to manage the multigeometries
 *
 * Revision 1.1  2007/05/07 07:06:46  jorpiell
 * Add a constructor with the name and the description fields
 *
 *
 */
/**
 *  A LineString is a special curve that consists of a
 *  single segment with linear interpolation. It is defined
 *  by two or more coordinate tuples, with linear interpolation
 *  between them.
 *  It parses a gml:LineStringType object. Example:
 * <p>
 * <pre>
 * <code>
 * &lt;LineString&gt;
 * &lt;coord&gt;&lt;X&gt;56.1&lt;/X&gt;&lt;Y&gt;0.45&lt;/Y&gt;&lt;/coord&gt;
 * &lt;coord&gt;&lt;X&gt;67.23&lt;/X&gt;&lt;Y&gt;0.98&lt;/Y&gt;&lt;/coord&gt;
 * &lt;/LineString&gt;
 * </code>
 * </pre>
 * </p> 
 * @author Jorge Piera Llodr· (piera_jor@gva.es)
 */
public class LineStringTypeBinding extends GeometryBinding{
	
	/**
	 * It parses the gml:LineQName tag
	 * @param parser
	 * The XML parser
	 * @param handler
	 * The GPE parser that contains the content handler and
	 * the error handler
	 * @return
	 * A line
	 * @throws XmlStreamException
	 * @throws IOException
	 */
	public Object parse(IXmlStreamReader parser,GPEDefaultGmlParser handler) throws XmlStreamException, IOException {
		boolean endFeature = false;
		int currentTag;
		Object lineString = null;
			
		super.setAtributtes(parser, handler.getErrorHandler());

		QName tag = parser.getName();
		currentTag = parser.getEventType();

		while (!endFeature){
			switch(currentTag){
			case IXmlStreamReader.START_ELEMENT:
				lineString = parseTag(parser, handler, tag, id, srsName);
				//The parser pointer is in the </gml:LoneString> tag
			 	if (lineString != null){
			 		return lineString;
			 	}
			 	break;
			case IXmlStreamReader.END_ELEMENT:
				if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_LINESTRING)){	
					endFeature = true;
					handler.getContentHandler().endLineString(lineString);
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

		return lineString;	
	}
	
	/**
	 * It parses next tag
	 * @param parser
	 * @param handler
	 * @param tag
	 * @param id
	 * @param srsName
	 * @return
	 * @throws XmlStreamException
	 * @throws IOException
	 */
	protected Object parseTag(IXmlStreamReader parser,GPEDefaultGmlParser handler, QName tag, String id, String srsName) throws XmlStreamException, IOException{
		Object lineString = null;
		if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_COORDINATES)){
			CoordinatesTypeIterator coordinatesIteartor = handler.getProfile().getCoordinatesTypeBinding();
			coordinatesIteartor.initialize(parser, handler,GMLTags.GML_LINESTRING);
			lineString = handler.getContentHandler().startLineString(id,
					coordinatesIteartor,								
					srsName);						
		}else if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_COORD)){
			CoordTypeIterator coordinatesIteartor = handler.getProfile().getCoordTypeBinding();
			coordinatesIteartor.initialize(parser, handler,GMLTags.GML_LINESTRING);
			lineString = handler.getContentHandler().startLineString(id,
					coordinatesIteartor,								
					srsName);			
		}
		return lineString;
	}
}
