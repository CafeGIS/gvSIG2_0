package org.gvsig.remoteClient.wfs.exceptions;

import java.io.IOException;

import org.gvsig.remoteClient.utils.CapabilitiesTags;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParserException;

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
 * $Id: ExceptionsFactory.java 18271 2008-01-24 09:06:43Z jpiera $
 * $Log$
 * Revision 1.1  2007-02-09 14:11:01  jorpiell
 * Primer piloto del soporte para WFS 1.1 y para WFS-T
 *
 *
 */
/**
 * This class parses an exception and returns it
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 */
public class ExceptionsFactory {
	
	public static WFSException parseExceptionReport(KXmlParser parser) throws XmlPullParserException, IOException {
		int currentTag;
		boolean end = false;		
		
		currentTag = parser.next();
		
		while (!end) 
		{
			switch(currentTag)
			{
			case KXmlParser.START_TAG:
				if (parser.getName().compareToIgnoreCase(CapabilitiesTags.SERVICE_EXCEPTION)==0)
				{
					for (int i=0 ; i<parser.getAttributeCount() ; i++){
						String attName = parser.getAttributeName(i);
						String code = null;
						if (attName.compareTo(CapabilitiesTags.CODE)==0){
							code = parser.getAttributeValue(i);
						}
						if (code != null){
							if (code.compareTo(CapabilitiesTags.INVALID_FORMAT)==0){
								parser.next();
								return new InvalidFormatException(parser.getText());
							}
							//Code unspecified
							parser.next();
							return new WFSException(code,parser.getText());
						}
					}
				}  		                       
				break;
			case KXmlParser.END_TAG:
				if (parser.getName().compareTo(CapabilitiesTags.SERVICE_EXCEPTION_REPORT) == 0)
					end = true;
				break;
			case KXmlParser.TEXT:                   
				break;
			}
			if (!end){
				currentTag = parser.next();
			}
		}   
		return new WFSException();
	}
}
