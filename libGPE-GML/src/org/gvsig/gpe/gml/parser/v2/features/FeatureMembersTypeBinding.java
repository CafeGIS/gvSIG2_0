/* gvSIG. Geographic Information System of the Valencian Government
*
* Copyright (C) 2007-2008 Infrastructures and Transports Department
* of the Valencian Government (CIT)
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
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
* MA  02110-1301, USA.
* 
*/

/*
* AUTHORS (In addition to CIT):
* 2009 Iver T.I.  {{Task}}
*/
 
package org.gvsig.gpe.gml.parser.v2.features;

import java.io.IOException;

import javax.xml.namespace.QName;

import org.gvsig.gpe.gml.parser.GPEDefaultGmlParser;
import org.gvsig.gpe.gml.utils.GMLObject;
import org.gvsig.gpe.gml.utils.GMLTags;
import org.gvsig.gpe.xml.stream.IXmlStreamReader;
import org.gvsig.gpe.xml.stream.XmlStreamException;
import org.gvsig.gpe.xml.utils.CompareUtils;

public class FeatureMembersTypeBinding {
	
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
	public void parse(IXmlStreamReader parser,GPEDefaultGmlParser handler, Object layer) throws XmlStreamException, IOException {
		boolean endFeatureMembers = false;
		int currentTag;		
						
		QName tag = parser.getName();
		currentTag = parser.getEventType();

		while (!endFeatureMembers){
			switch(currentTag){
			case IXmlStreamReader.START_ELEMENT:
				if (!CompareUtils.compareWithNamespace(tag, GMLTags.GML_FEATUREMEMBERS)){
					GMLObject object = (GMLObject)handler.getProfile().getFeatureTypeBinding().parse(parser, handler);
					if (object != null){
						handler.getContentHandler().addFeatureToLayer(object.getObject(), layer);
					}
				}	
				break;
			case IXmlStreamReader.END_ELEMENT:
				if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_FEATUREMEMBERS)){						
					endFeatureMembers = true;					
				}
				break;
			case IXmlStreamReader.CHARACTERS:					

				break;
			}
			if (!endFeatureMembers){					
				currentTag = parser.next();
				tag = parser.getName();
			}
		}				
	}
}

