package org.gvsig.gpe.kml.parser.v21.geometries;

import java.io.IOException;

import javax.xml.namespace.QName;

import org.gvsig.gpe.kml.parser.GPEDeafultKmlParser;
import org.gvsig.gpe.kml.utils.Kml2_1_Tags;
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
 * $Id:PolygonTypeBinding.java 357 2008-01-09 17:50:08Z jpiera $
 * $Log$
 * Revision 1.2  2007/06/07 14:53:59  jorpiell
 * Add the schema support
 *
 * Revision 1.1  2007/05/11 07:06:29  jorpiell
 * Refactoring of some package names
 *
 * Revision 1.5  2007/05/08 08:22:37  jorpiell
 * Add comments to create javadocs
 *
 * Revision 1.4  2007/05/02 11:46:50  jorpiell
 * Writing tests updated
 *
 * Revision 1.3  2007/04/20 08:38:59  jorpiell
 * Tests updating
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
 * It writes a Polygon tag. Example:
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
 * @see http://code.google.com/apis/kml/documentation/kml_tags_21.html#polygon
 */
public class PolygonTypeBinding {

	/**
	 * It parses the Polygon tag
	 * @param parser
	 * The XML parser
	 * @param handler
	 * The GPE parser that contains the content handler and
	 * the error handler
	 * @return
	 * A polygon
	 * @throws IOException 
	 * @throws XmlStreamException 
	 * @throws XmlStreamException
	 * @throws IOException
	 */
	public Object parse(IXmlStreamReader parser,GPEDeafultKmlParser handler) throws XmlStreamException, IOException {
		boolean endFeature = false;
		int currentTag;			
		Object polygon = null;

		String id = handler.getProfile().getGeometryBinding().getID(parser, handler);		

		QName tag = parser.getName();
		currentTag = parser.getEventType();

		while (!endFeature){
			switch(currentTag){
			case IXmlStreamReader.START_ELEMENT:
				if (CompareUtils.compareWithNamespace(tag,Kml2_1_Tags.OUTERBOUNDARYIS)){
					OuterBoundaryIsBinding coordinatesBinding = handler.getProfile().getOuterBoundaryIsBinding();
					ICoordinateIterator coordinatesIterator = coordinatesBinding.parse(parser, handler);
					polygon = handler.getContentHandler().startPolygon(id,
							coordinatesIterator,
							Kml2_1_Tags.DEFAULT_SRS);					
				}else if (CompareUtils.compareWithNamespace(tag,Kml2_1_Tags.INNERBOUNDARYIS)){
					InnerBoundaryIsBinding innerPolygonBinding = handler.getProfile().getInnerBoundaryIsBinding();
					ICoordinateIterator coordinatesIterator = innerPolygonBinding.parse(parser, handler);
					Object innerPolygon = handler.getContentHandler().startInnerPolygon(id,
							coordinatesIterator,
							Kml2_1_Tags.DEFAULT_SRS);	
					handler.getContentHandler().endInnerPolygon(innerPolygon);
					handler.getContentHandler().addInnerPolygonToPolygon(innerPolygon,polygon);
				}
				break;
			case IXmlStreamReader.END_ELEMENT:
				if (CompareUtils.compareWithNamespace(tag,Kml2_1_Tags.POLYGON)){						
					endFeature = true;
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
}