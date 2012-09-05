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
import org.gvsig.gpe.xml.utils.XMLTags;


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
 * $Id$
 * $Log$
 *
 */
/**
 * @author Jorge Piera Llodrá (jorge.piera@iver.es)
 */
public class FeatureCollectionWriter {
	private static GPEManager gpeManager = GPELocator.getGPEManager();
	
	/**
	 * It writes the FeatureCollection init tag
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
	public void start(IXmlStreamWriter writer, GPEGmlWriterHandlerImplementor handler, String fid, 
			String namespace, String name) throws IOException{
		String myName = GMLUtilsParser.addBlancSymbol(name);
		String myNamespace = namespace;
		if (name == null){
			QName qname = GMLUtilsParser.createDefaultFeatureCollection();
			myName = qname.getLocalPart();
			myNamespace = qname.getNamespaceURI();
		}else if (namespace == null){
			myNamespace = gpeManager.getStringProperty(XmlProperties.DEFAULT_NAMESPACE_URI);
		}
		writer.writeStartElement(myNamespace, myName);
		if (fid != null){
			writer.writeStartAttribute(GMLTags.GML_FID);
			writer.writeValue(fid);
		}
		//Write namespaces: this functionality must be implemented on the writer, not here!!!
		writer.writeStartAttribute(XMLTags.XML_NAMESPACE_URI, 
				XMLTags.XML_NAMESPACE_PREFIX + ":" + XMLTags.XML_SCHEMA_INSTANCE_NAMESPACE_PREFIX);
		writer.writeValue(XMLTags.XML_SCHEMA_INSTANCE_NAMESPACE_URI);
		
		writer.writeStartAttribute(XMLTags.XML_NAMESPACE_URI, 
				XMLTags.XML_NAMESPACE_PREFIX + ":" + XMLTags.XML_XLINK_NAMESPACE_PREFIX);
		writer.writeValue(XMLTags.XML_XLINK_NAMESPACE_URI);
		
		writer.writeStartAttribute(XMLTags.XML_NAMESPACE_URI, 
				XMLTags.XML_NAMESPACE_PREFIX + ":" + GMLTags.GML_NAMESPACE_PREFIX);
		writer.writeValue(GMLTags.GML_NAMESPACE_URI);	

		writer.writeStartAttribute(XMLTags.XML_NAMESPACE_URI, 
				XMLTags.XML_NAMESPACE_PREFIX + ":" + gpeManager.getStringProperty(XmlProperties.DEFAULT_NAMESPACE_PREFIX));
		writer.writeValue(gpeManager.getStringProperty(XmlProperties.DEFAULT_NAMESPACE_URI));		
		
		writer.writeStartAttribute(XMLTags.XML_SCHEMA_INSTANCE_NAMESPACE_URI, 
				XMLTags.XML_SCHEMA_INSTANCE_NAMESPACE_PREFIX + ":" + XMLTags.XML_SCHEMA_LOCATION);
		String schemaLocation = gpeManager.getStringProperty(XmlProperties.DEFAULT_NAMESPACE_URI) + 
				" " + gpeManager.getStringProperty(XmlProperties.XSD_SCHEMA_FILE);
		writer.writeValue(schemaLocation);			
	}
	
	/**
	 * It writes the FeatureCollection init tag
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
	 * @param description
	 * The feature collection decription
	 * @param srs
	 * The feature collection srs
	 * @throws IOException
	 */
	public void start(IXmlStreamWriter writer, GPEGmlWriterHandlerImplementor handler, String fid, 
			String namespace, String name, String description, String srs) throws IOException{
		start(writer, handler, fid, namespace, name);
		writer.writeEndAttributes();	
		handler.getProfile().getNameWriter().write(writer, handler, name);
		handler.getProfile().getDescriptionWriter().write(writer, handler, description);
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
	public void end(IXmlStreamWriter writer, GPEGmlWriterHandlerImplementor handler) throws IOException{
		writer.writeEndElement();
	}
	
	
}
