package org.gvsig.gpe.gml.parser.v2.geometries;

import java.io.IOException;

import javax.xml.namespace.QName;

import org.gvsig.gpe.gml.parser.GPEDefaultGmlParser;
import org.gvsig.gpe.gml.utils.GMLTags;
import org.gvsig.gpe.parser.ICoordinateIterator;
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
 * $Id:PolygonTypeBinding.java 195 2007-11-26 09:02:22Z jpiera $
 * $Log$
 * Revision 1.4  2007/05/14 11:18:51  jorpiell
 * ProjectionFactory updated
 *
 * Revision 1.3  2007/05/14 09:31:06  jorpiell
 * Add the a new class to compare tags
 *
 * Revision 1.2  2007/05/08 10:24:16  jorpiell
 * Add comments to create javadocs
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
 * It parses a gml:PolygonType object. Example:
 * <p>
 * <pre>
 * <code>
 * &lt;Polygon gid="_877789"&gt;
 * &lt;outerBoundaryIs&gt;
 * &lt;LinearRing&gt;
 * &lt;coordinates&gt;0.0,0.0 100.0,0.0 50.0,100.0 0.0,0.0&lt;/coordinates&gt;
 * &lt;/LinearRing&gt;
 * &lt;/outerBoundaryIs&gt;
 * &lt;/Polygon&gt;
 * </code>
 * </pre>
 * </p> 
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class PolygonTypeBinding extends GeometryBinding{
	protected Object polygon = null;
	
	/**
	 * It parses the gml:Polygon tag
	 * @param parser
	 * The XML parser
	 * @param handler
	 * The GPE parser that contains the content handler and
	 * the error handler
	 * @return
	 * A polygon
	 * @throws XmlStreamException
	 * @throws IOException
	 */
	public Object parse(IXmlStreamReader parser,GPEDefaultGmlParser handler) throws XmlStreamException, IOException {
		boolean endFeature = false;
		int currentTag;			
		
		super.setAtributtes(parser, handler.getErrorHandler());

		QName tag = parser.getName();
		currentTag = parser.getEventType();

		while (!endFeature){
			switch(currentTag){
			case IXmlStreamReader.START_ELEMENT:
				polygon = parseTag(parser, handler, tag, id, srsName);
				break;
			case IXmlStreamReader.END_ELEMENT:
				endFeature = parseLastTag(parser, handler, tag);
				if (endFeature){
					handler.getContentHandler().endPolygon(polygon);
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

		return polygon;
	}
	
	/**
	 * It parses the XML tag
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
		if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_OUTERBOUNDARYIS)){
			OuterBoundaryIsTypeBinding coordinatesBinding = handler.getProfile().getOuterBoundaryIsTypeBinding();
			ICoordinateIterator coordinatesIterator = coordinatesBinding.parse(parser, handler);
			this.polygon = handler.getContentHandler().startPolygon(id,
					coordinatesIterator,
					srsName);
		}else if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_INNERBOUNDARYIS)){
			InnerBoundaryIsTypeBinding coordinatesBinding = handler.getProfile().getInnerBoundaryIsTypeBinding();
			ICoordinateIterator coordinatesIterator = coordinatesBinding.parse(parser, handler);
			Object innerPolygon = handler.getContentHandler().startInnerPolygon(null,
					coordinatesIterator,
					srsName);
			handler.getContentHandler().endInnerPolygon(innerPolygon);
			handler.getContentHandler().addInnerPolygonToPolygon(innerPolygon,this.polygon);
		}
		return polygon;
	}
	
	/**
	 * Parses the last tag
	 * @param parser
	 * @param handler
	 * @param tag
	 * @return
	 */
	protected boolean parseLastTag(IXmlStreamReader parser,GPEDefaultGmlParser handler, QName tag){
		if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_POLYGON)){						
			return true;
		}
		return false;
	}
}
