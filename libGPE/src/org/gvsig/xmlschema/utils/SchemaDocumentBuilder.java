package org.gvsig.xmlschema.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.gvsig.gpe.parser.IAttributesIterator;
import org.gvsig.xmlschema.exceptions.SchemaCreationException;
import org.gvsig.xmlschema.som.IXSSchema;
import org.gvsig.xmlschema.som.impl.XSSchemaImpl;
import org.gvsig.xmlschema.warnings.SchemaLocationWarning;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
 * $Id: SchemaDocumentBuilder.java 164 2007-07-02 10:00:46Z jorpiell $
 * $Log$
 * Revision 1.4  2007/07/02 09:57:35  jorpiell
 * The generated xsd schemas have to be valid
 *
 * Revision 1.3  2007/06/28 13:04:33  jorpiell
 * The Qname has been updated to the 1.5 JVM machine. The schema validation is made in the GPEWriterHandlerImplementor class
 *
 * Revision 1.2  2007/06/22 12:20:48  jorpiell
 * The typeNotFoundException has been deleted. It never was thrown
 *
 * Revision 1.1  2007/06/14 16:15:03  jorpiell
 * builds to create the jars generated and add the schema code to the libGPEProject
 *
 * Revision 1.1  2007/06/14 13:50:07  jorpiell
 * The schema jar name has been changed
 *
 * Revision 1.2  2007/06/07 14:54:13  jorpiell
 * Add the schema support
 *
 * Revision 1.1  2007/05/30 12:25:48  jorpiell
 * Add the element collection
 *
 * Revision 1.1  2007/05/28 12:38:03  jorpiell
 * Some bugs fixed
 *
 *
 */
/**
 * Singleton to create schemas
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class SchemaDocumentBuilder {
	private static SchemaDocumentBuilder instance = null;
	private DocumentBuilder builder = null;
	
	/**
	 * This method cretaes the singleton instance
	 *
	 */
	private synchronized static void createInstance() {
		if (instance == null) { 
			instance = new SchemaDocumentBuilder();
		}
	}
	
	/**
	 * @return the schema builder instance
	 */
	public static SchemaDocumentBuilder getInstance() {
		if (instance == null){
			createInstance();
		}
		return instance;
	}
	
	/**
	 * Creates a new schema from a namespace
	 * @param namespaceURI
	 * Schema namspace
	 * @return
	 * A new schema
	 * @throws SchemaCreationException
	 */	
	public IXSSchema createXSSchema(String namespaceURI, String namespacePrefix) throws SchemaCreationException {
		IXSSchema schema;
		try {
			Document document = getBuilder().newDocument();
			Element element = document.createElement(SchemaTags.SCHEMA_ROOT);
			element.setAttribute(SchemaTags.XMLNS_NS + ":" + SchemaTags.XS_NS,
					SchemaTags.XS_NS_URI);
			element.setAttribute(SchemaTags.XMLNS_NS + ":" + namespacePrefix,
					namespaceURI);
			element.setAttribute(SchemaTags.TARGET_NAMESPACE,
					namespaceURI);
			document.appendChild(element);
			schema = new XSSchemaImpl(document);
		} catch (ParserConfigurationException e) {
			throw new SchemaCreationException(e);
		}
		return schema;
	}
	
	/**
	 * Parse a schema file
	 * @param is
	 * XSD schema file
	 * @return
	 * A schema
	 * @throws SchemaCreationException 
	 */
	public IXSSchema parse(InputStream is) throws SchemaCreationException {
		try {
			Document document = getBuilder().parse(is);
			return new XSSchemaImpl(document);
		} catch (Exception e) {
			throw new SchemaCreationException(e);
		}		
	}
	
	/**
	 * Parse a set of attributes (header of a XML file)
	 * @param attributesIterator
	 * The attribute iterator
	 * @throws IOException
	 * @throws SchemaCreationException 
	 * @throws SchemaLocationWarning 
	 */
	public ArrayList parse(IAttributesIterator attributesIterator) throws IOException, SchemaCreationException, SchemaLocationWarning{
		ArrayList schemas = new ArrayList();
		while(attributesIterator.hasNext()){
			QName attributeName = attributesIterator.nextAttributeName();
			if (SchemaTags.SCHEMA_LOCATION_ATTR_NAME.equals(attributeName.getLocalPart())){
				String schemaLocation = (String)attributesIterator.nextAttribute();
				StringTokenizer tokenizer = new StringTokenizer(schemaLocation, " \b\n\t");
		        while (tokenizer.hasMoreTokens()){
		            tokenizer.nextToken();
		            if (tokenizer.hasMoreTokens()){
		               	schemaLocation = tokenizer.nextToken();
		               	URI uri = getSchemaURI(schemaLocation);
		               	schemas.add(parse(new FileInputStream(uri.getPath())));
		           }
		        }				
			}			
		}
		return schemas;
	}
	
	/****************************************************************************
	 * <getSchemaFile>
	 * It downloads the schema if it's a remote schema
	 * else it tries to open a local file and return if it's succesfull
	 * @param String schema location
	 * @return Uri
	 * @throws SchemaLocationWarning 
	 ****************************************************************************/
	private URI getSchemaURI(String schemaLocation) throws SchemaLocationWarning{
		File f = null;
		//If it is a local file, it has to construct the absolute route
		if (schemaLocation.indexOf("http://") != 0){
			f = new File(schemaLocation);
			if (!(f.isAbsolute())){
				schemaLocation = new File(schemaLocation).getParentFile().getAbsolutePath() + File.separator +  schemaLocation;
				f = new File(schemaLocation);
			}
			try {
				return new URI(f.getAbsolutePath());
			} catch (URISyntaxException e) {
				throw new SchemaLocationWarning(schemaLocation,e);
			}			
		}
		//Else it is an URL direction and it has to download it.
		else {
			URL url;		
			try {
				url = new URL(schemaLocation);
				//Download the schema without cancel option.
				f = DownloadUtilities.downloadFile(url,"gml_schmema.xsd");	
				return new URI(f.getAbsolutePath());
			} catch (Exception e) {
				throw new SchemaLocationWarning(schemaLocation,e);
			}
		}
	}
	
	/**
	 * @return the builder
	 * @throws ParserConfigurationException 
	 */
	private DocumentBuilder getBuilder() throws ParserConfigurationException {
		if (builder == null){
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
			builder = factory.newDocumentBuilder();
		}
		return builder;
	}	
}
