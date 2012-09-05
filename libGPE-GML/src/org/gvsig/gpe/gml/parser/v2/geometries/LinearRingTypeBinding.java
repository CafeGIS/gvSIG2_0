package org.gvsig.gpe.gml.parser.v2.geometries;

import java.io.IOException;

import javax.xml.namespace.QName;

import org.gvsig.gpe.gml.parser.GPEDefaultGmlParser;
import org.gvsig.gpe.gml.parser.v2.coordinates.CoordTypeIterator;
import org.gvsig.gpe.gml.parser.v2.coordinates.CoordinatesTypeIterator;
import org.gvsig.gpe.gml.utils.GMLTags;
import org.gvsig.gpe.parser.ICoordinateIterator;
import org.gvsig.gpe.warnings.PolygonNotClosedWarning;
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
 * $Id:LinearRingTypeBinding.java 195 2007-11-26 09:02:22Z jpiera $
 * $Log$
 * Revision 1.4  2007/05/16 09:29:12  jorpiell
 * The polygons has to be closed
 *
 * Revision 1.3  2007/05/15 11:55:11  jorpiell
 * MultiGeometry is now supported
 *
 * Revision 1.2  2007/05/14 09:31:06  jorpiell
 * Add the a new class to compare tags
 *
 * Revision 1.1  2007/05/07 12:58:42  jorpiell
 * Add some methods to manage the multigeometries
 *
 *
 */
/**
 * It parses a gml:linearRingType object. Example:
 * <p>
 * <pre>
 * <code>
 * &lt;LinearRing&gt;
 * &lt;coordinates&gt;0.0,0.0 100.0,0.0 50.0,100.0 0.0,0.0&lt;/coordinates&gt;
 * &lt;/LinearRing&gt;
 * </code>
 * </pre>
 * </p> 
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class LinearRingTypeBinding extends GeometryBinding{
	
	/**
	 * It parses the gml:LinearRing tag and return the 
	 * Object
	 * @param parser
	 * The XML parser
	 * @param handler
	 * The GPE parser that contains the content handler and
	 * the error handler
	 * @return
	 * A linear ring
	 * @throws XmlStreamException
	 * @throws IOException
	 */
	public Object parse(IXmlStreamReader parser,GPEDefaultGmlParser handler) throws XmlStreamException, IOException{
		Object linearRing = null;
				
		super.setAtributtes(parser, handler.getErrorHandler());
			
		ICoordinateIterator coordinatesIteartor = parseCoordinates(parser, handler);	
		
		linearRing = handler.getContentHandler().startLinearRing(id, coordinatesIteartor, srsName);
		handler.getContentHandler().endLinearRing(linearRing);
		
		return linearRing;
	}
	
	/**
	 * It parses the gml:LinearRing tag and return the 
	 * coordinates
	 * @param parser
	 * The XML parser
	 * @param handler
	 * The GPE parser that contains the content handler and
	 * the error handler
	 * @return
	 * An array of coordinates
	 * @throws XmlStreamException
	 * @throws IOException
	 * @throws PolygonNotClosedWarning 
	 */
	public ICoordinateIterator parseCoordinates(IXmlStreamReader parser,GPEDefaultGmlParser handler) throws XmlStreamException, IOException {
		boolean endFeature = false;
		int currentTag;
		
		QName tag = parser.getName();
		currentTag = parser.getEventType();

		while (!endFeature){
			switch(currentTag){
			case IXmlStreamReader.START_ELEMENT:
				ICoordinateIterator iterator = parseTag_(parser, handler, tag);
				if (iterator != null){
					return iterator;
				}
			case IXmlStreamReader.END_ELEMENT:
				if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_LINEARRING)){						
								
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
		//The first and the last coordinates has to be the same
//		if ((coordinates[0][0] != coordinates[0][coordinates[0].length - 1]) ||
//				(coordinates[1][0] != coordinates[1][coordinates[1].length - 1]) ||
//				(coordinates[2][0] != coordinates[2][coordinates[2].length - 1])){
//			handler.getErrorHandler().addWarning(new PolygonNotClosedWarning(coordinates));
//		}
		return null;
	}
	
	/**
	 * 
	 * @param parser
	 * @param handler
	 * @param tag
	 * @param coordsContainer
	 * @return
	 * @throws XmlStreamException
	 * @throws IOException
	 */
	protected ICoordinateIterator parseTag_(IXmlStreamReader parser,GPEDefaultGmlParser handler, QName tag) throws XmlStreamException, IOException{
		if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_COORDINATES)){
			CoordinatesTypeIterator coordinatesIterator = handler.getProfile().getCoordinatesTypeBinding();
			coordinatesIterator.initialize(parser, handler,GMLTags.GML_LINEARRING);
			return coordinatesIterator;
		}else if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_COORD)){
			CoordTypeIterator coordinatesIterator = handler.getProfile().getCoordTypeBinding();
			coordinatesIterator.initialize(parser, handler,GMLTags.GML_LINEARRING);
			return coordinatesIterator;
		}
		return null;
	}
	
	
}
