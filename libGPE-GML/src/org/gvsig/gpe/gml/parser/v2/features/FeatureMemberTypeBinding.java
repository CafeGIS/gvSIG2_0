package org.gvsig.gpe.gml.parser.v2.features;

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
 * $Id: FeatureMemberTypeBinding.java 195 2007-11-26 09:02:22Z jpiera $
 * $Log$
 * Revision 1.2  2007/05/16 13:00:48  csanchez
 * ActualizaciÃ³n de libGPE-GML
 *
 * Revision 1.1  2007/05/14 09:30:30  jorpiell
 * Add the FeatureMember tag
 *
 *
 */
/**
 * This class parses the gml objects that has a 
 * gml:FeatureMember type. The structure of the
 * properties that this type has is variable and depends
 * on its schema. Example:
 * <p>
 * <pre>
 * <code>
 * &lt;gml:featureMember&gt;
 * &lt;cit:cities&gt;
 * &lt;cit:the_geom&gt;
 * &lt;gml:Point srsName='0'&gt;
 * &lt;gml:coordinates&gt;-123.06999969482422,49.411192817494346&lt;/gml:coordinates&gt;
 * &lt;/gml:Point&gt;
 * &lt;/cit:the_geom&gt;
 * &lt;cit:NAME&gt;Vancouver&lt;/cit:NAME&gt;
 * &lt;cit:CAPITAL&gt;N&lt;/cit:CAPITAL&gt;
 * &lt;cit:PROV_NAME&gt;British Columbia&lt;/cit:PROV_NAME&gt;
 * &lt;/cit:cities&gt;
 * &lt;/gml:featureMember&gt;
 * </code>
 * </pre>
 * </p>
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class FeatureMemberTypeBinding {
	
	/**
	 * It parses a feature
	 * @param parser
	 * The XML parser
	 * @param handler
	 * The GPE parser that contains the content handler and
	 * the error handler
	 * @return
	 * A feature
	 * @throws XmlStreamException
	 * @throws IOException
	 */
	public Object parse(IXmlStreamReader parser,GPEDefaultGmlParser handler) throws XmlStreamException, IOException {
		boolean endFeature = false;
		int currentTag;		
		Object feature = null;	
				
		QName tag = parser.getName();
		currentTag = parser.getEventType();

		while (!endFeature){
			switch(currentTag){
			case IXmlStreamReader.START_ELEMENT:
				if (!CompareUtils.compareWithNamespace(tag, GMLTags.GML_FEATUREMEMBER)){
					feature = handler.getProfile().getFeatureTypeBinding().parse(parser, handler);
				}	
				break;
			case IXmlStreamReader.END_ELEMENT:
				if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_FEATUREMEMBER)){						
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

		return feature;		
	}
}
