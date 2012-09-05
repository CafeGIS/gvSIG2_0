package org.gvsig.gpe.kml.parser;

import java.io.IOException;

import javax.xml.namespace.QName;

import org.gvsig.tools.exception.BaseException;
import org.gvsig.gpe.kml.exceptions.KmlHeaderParseException;
import org.gvsig.gpe.kml.parser.profiles.IBindingProfile;
import org.gvsig.gpe.kml.utils.Kml2_1_Tags;
import org.gvsig.gpe.xml.parser.GPEXmlParser;
import org.gvsig.gpe.xml.stream.IXmlStreamReader;
import org.gvsig.gpe.xml.stream.XmlStreamException;
import org.gvsig.gpe.xml.utils.CompareUtils;
import org.gvsig.gpe.xml.utils.XMLAttributesIterator;

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
 * $Id: GPEDeafultKmlParser.java 366 2008-01-10 09:09:38Z jpiera $
 * $Log$
 * Revision 1.11  2007/07/02 09:59:44  jorpiell
 * The generated xsd schemas have to be valid
 *
 * Revision 1.10  2007/06/07 14:53:59  jorpiell
 * Add the schema support
 *
 * Revision 1.9  2007/05/11 07:06:29  jorpiell
 * Refactoring of some package names
 *
 * Revision 1.8  2007/05/09 06:54:24  jorpiell
 * Change the File by URI
 *
 * Revision 1.7  2007/05/08 09:28:17  jorpiell
 * Add comments to create javadocs
 *
 * Revision 1.6  2007/05/07 07:07:18  jorpiell
 * Add a constructor with the name and the description fields
 *
 * Revision 1.5  2007/04/20 08:38:59  jorpiell
 * Tests updating
 *
 * Revision 1.4  2007/04/14 16:08:07  jorpiell
 * Kml writing support added
 *
 * Revision 1.3  2007/04/13 13:16:21  jorpiell
 * Add KML reading support
 *
 * Revision 1.2  2007/04/12 17:06:43  jorpiell
 * First GML writing tests
 *
 * Revision 1.1  2007/04/12 10:21:52  jorpiell
 * Add the writers
 *
 *
 */
/**
 * This is a KML parser. This class must be registered in 
 * the GPE register to read and write KML/KMZ files.
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public abstract class GPEDeafultKmlParser extends GPEXmlParser {
	private IBindingProfile profile = null;
	private Object layer = null;
	
	/**
	 * @return the profile
	 */
	public IBindingProfile getProfile() {
		return profile;
	}

	/**
	 * @param profile the profile to set
	 */
	public void setProfile(IBindingProfile profile) {
		this.profile = profile;
	}

	public GPEDeafultKmlParser() {
		super();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.xml.GPEXmlParser#initParse()
	 */
	protected void initParse() {
		layer = null;
		try {
			String namespace = getProfile().getHeaderBinding().parse(getParser(),this);
			boolean endFeature = false;
			int currentTag;		

			QName tag = getParser().getName();
			currentTag = getParser().getEventType();

			XMLAttributesIterator attributesIterator = new XMLAttributesIterator(getParser());
			
			while (!endFeature){
				switch(currentTag){
				case IXmlStreamReader.START_ELEMENT:
					if (CompareUtils.compareWithNamespace(tag,Kml2_1_Tags.DOCUMENT)){
						getProfile().getDocumentBinding().parse(getParser(), this);
					}else if (CompareUtils.compareWithNamespace(tag,Kml2_1_Tags.FOLDER)){
						getProfile().getFolderBinding().parse(getParser(), this, null);
					}else if (CompareUtils.compareWithNamespace(tag,Kml2_1_Tags.PLACEMARK)){
						if (layer == null){
							layer = getContentHandler().startLayer(null, Kml2_1_Tags.NAMESPACE_21, null, null, Kml2_1_Tags.DEFAULT_SRS, attributesIterator, null, null);
						}
						Object feature = getProfile().getPlaceMarketBinding().parse(getParser(), this);
						getContentHandler().addFeatureToLayer(feature, layer);			
					}
					break;
				case IXmlStreamReader.END_DOCUMENT:
					endFeature = true;
					break;					
				}
				if (!endFeature){					
					currentTag = getParser().next();
					tag = getParser().getName();
				}
			}
			if (layer != null){
				getContentHandler().endLayer(layer);
			}			
		} catch (KmlHeaderParseException e) {
			getErrorHandler().addError(e);
		} catch (BaseException e) {
			getErrorHandler().addError(e);
		} catch (XmlStreamException e) {
			getErrorHandler().addError(e);
		} catch (IOException e) {
			getErrorHandler().addError(e);
		}		
	}

}
