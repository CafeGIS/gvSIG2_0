package org.gvsig.gpe.kml.parser.v21.geometries;

import java.io.IOException;

import javax.xml.namespace.QName;

import org.gvsig.gpe.kml.parser.GPEDeafultKmlParser;
import org.gvsig.gpe.kml.utils.Kml2_1_Tags;
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
 * $Id:RegionBinding.java 357 2008-01-09 17:50:08Z jpiera $
 * $Log$
 * Revision 1.1  2007/05/11 07:06:29  jorpiell
 * Refactoring of some package names
 *
 * Revision 1.1  2007/05/09 08:36:24  jorpiell
 * Add the bbox to the layer
 *
 *
 */
/**
 * This class parses a Region Kml tag. Example:
 * <p>
 * <pre>
 * <code> 
 * &lt;Region&gt;
 * &lt;LatLonAltBox&gt;
 * &lt;north&gt;43.374&lt;/north&gt;
 * &lt;south&gt;42.983&lt;/south&gt;
 * &lt;east&gt;-0.335&lt;/east&gt;
 * &lt;west&gt;-1.423&lt;/west&gt;
 * &lt;rotation&gt;39.37878630116985&lt;/rotation&gt;
 * &lt;/LatLonAltBox&gt;
 * &lt;/Region&gt;
 * </code>
 * </pre>
 * </p> 
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 * @see http://code.google.com/apis/kml/documentation/kml_tags_21.html#region
 */
public class RegionBinding {
	
	/**
	 * It parses the Region tag
	 * @param parser
	 * The XML parser
	 * @param handler
	 * The GPE parser that contains the content handler and
	 * the error handler
	 * @return
	 * A Bounding box
	 * @throws IOException 
	 * @throws XmlStreamException 
	 */
	public Object parse(IXmlStreamReader parser,GPEDeafultKmlParser handler) throws XmlStreamException, IOException {
		boolean endFeature = false;
		int currentTag;
		Object bbox = null;
		
		QName tag = parser.getName();
		currentTag = parser.getEventType();

		while (!endFeature){
			switch(currentTag){
			case IXmlStreamReader.START_ELEMENT:
				if (CompareUtils.compareWithNamespace(tag,Kml2_1_Tags.LATLONALTBOX)){
					bbox =  handler.getProfile().getLatLonAltBoxBinding().parse(parser, handler);
				}
				break;
			case IXmlStreamReader.END_ELEMENT:
				if (CompareUtils.compareWithNamespace(tag,Kml2_1_Tags.REGION)){						
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
		return bbox;	
	}
}

