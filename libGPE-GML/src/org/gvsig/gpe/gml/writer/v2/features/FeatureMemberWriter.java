package org.gvsig.gpe.gml.writer.v2.features;

import java.io.IOException;

import javax.xml.namespace.QName;

import org.gvsig.gpe.GPELocator;
import org.gvsig.gpe.GPEManager;
import org.gvsig.gpe.gml.utils.GMLTags;
import org.gvsig.gpe.gml.utils.GMLUtilsParser;
import org.gvsig.gpe.gml.writer.GPEGmlWriterHandlerImplementor;
import org.gvsig.gpe.xml.XmlProperties;
import org.gvsig.gpe.xml.stream.IXmlStreamWriter;

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
 * $Id: FeatureMemberWriter.java 350 2008-01-09 12:53:07Z jpiera $
 * $Log$
 * Revision 1.6  2007/06/07 14:53:30  jorpiell
 * Add the schema support
 *
 * Revision 1.5  2007/05/15 09:35:09  jorpiell
 * the tag names cant have blanc spaces
 *
 * Revision 1.4  2007/05/08 10:24:16  jorpiell
 * Add comments to create javadocs
 *
 * Revision 1.3  2007/04/14 16:07:30  jorpiell
 * The writer has been created
 *
 * Revision 1.2  2007/04/13 13:16:00  jorpiell
 * Add the multiple geometries
 *
 * Revision 1.1  2007/04/12 17:06:44  jorpiell
 * First GML writing tests
 *
 *
 */
/**
 * A geographic feature is essentially a named list of properties. 
 * Some or all of these properties may be geospatial, describing 
 * the position and shape of the feature. Each feature has a type, 
 * which is equivalent to a class in object modeling terminology, 
 * such that the class-definition prescribes the named properties
 * that a particular feature of that type is required to have.
 * <br>
 * This class is used to write a feature
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class FeatureMemberWriter {	
	private static GPEManager gpeManager = GPELocator.getGPEManager();
	
	/**
	 * It writes a Feature init tag
	 * @param writer
	 * Writer to write the labels
	 * @param handler
	 * The writer handler implementor
	 * @param id
	 * Feature id
	 * @param namespace
	 * Feature namespace
	 * @param name
	 * Feature name
	 * @throws IOException
	 */
	public void start(IXmlStreamWriter writer, GPEGmlWriterHandlerImplementor handler, String id, String namespace, String name) throws IOException{
		writer.writeStartElement(GMLTags.GML_FEATUREMEMBER);
		String myName = name;
		String myNamespace = namespace;
		if (name == null){
			QName qname = GMLUtilsParser.createDefaultFeature();
			myName = qname.getLocalPart();
			myNamespace = qname.getNamespaceURI();
		}else if (namespace == null){
			myNamespace = gpeManager.getStringProperty(XmlProperties.DEFAULT_NAMESPACE_URI);
		}				
		writer.writeStartElement(myNamespace, GMLUtilsParser.addBlancSymbol(myName));
		if (id != null){
			writer.writeStartAttribute(GMLTags.GML_FID);
			writer.writeValue(id);
			writer.writeEndAttributes();
		}		
	}
	
	/**
	 * It writes a Feature end tag
	 * @param writer
	 * Writer to write the labels
	 * @param handler
	 * The writer handler implementor
	 * @param namespace
	 * Feature namespace
	 * @param name
	 * Feature name
	 * @throws IOException
	 */
	public void end(IXmlStreamWriter writer, GPEGmlWriterHandlerImplementor handler, String namespace, String name) throws IOException{
		writer.writeEndElement();
		writer.writeEndElement();			
	}
	
	
}
