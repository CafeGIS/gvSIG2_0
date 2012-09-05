package org.gvsig.gpe.xml.parser;

//import org.gvsig.xmlschema.exceptions.SchemaCreationException;
//import org.gvsig.xmlschema.som.impl.XSSchemaDocumentImpl;
//import org.gvsig.xmlschema.utils.DownloadUtilities;
//import org.gvsig.xmlschema.warnings.SchemaLocationWarning;

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
 * $Id: GPEXmlSchemaDocument.java 157 2007-06-22 12:22:53Z jorpiell $
 * $Log$
 * Revision 1.1  2007/06/22 12:22:40  jorpiell
 * The typeNotFoundException has been deleted. It never was thrown
 *
 * Revision 1.4  2007/06/14 16:15:05  jorpiell
 * builds to create the jars generated and add the schema code to the libGPEProject
 *
 * Revision 1.3  2007/06/14 13:50:05  jorpiell
 * The schema jar name has been changed
 *
 * Revision 1.2  2007/06/08 13:01:12  jorpiell
 * Add the targetNamespace to the file
 *
 * Revision 1.1  2007/06/07 14:53:30  jorpiell
 * Add the schema support
 *
 *
 */
/**
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class GPEXmlSchemaDocument {}//extends XSSchemaDocumentImpl{
//	private IXmlStreamReader parser = null;
//	private URI xmlURI = null;
//	private GPEErrorHandler errorHandler = null;
//	
//	public GPEXmlSchemaDocument(IXmlStreamReader parser, URI xmlURI, GPEErrorHandler errorHandler){
//		this.parser = parser;
//		this.xmlURI = xmlURI;
//		this.errorHandler = errorHandler;		
//	}
//	
//	/**
//	 * Parser the xml header
//	 * @throws XmlStreamException
//	 * @throws IOException
//	 */
//	public void parse() throws XmlStreamException, IOException{
//		while ((parser.getName() == null) && 
//				!(parser.getEventType() == IXmlStreamReader.END_DOCUMENT)){
//			parser.next();
//		}
//		for (int i = 0 ; i < parser.getAttributeCount() ; i++){
//			QName attName = parser.getAttributeName(i);
//			String attValue = parser.getAttributeValue(i);
//								
//			//it splits the attributes names at the both sides from ":"
//			//String[] ns = attName.split(":");
//			String[] ns = CompatLocator.getStringUtils().split(attName.getLocalPart(),":");
//			//String[] ns = org.gvsig.gpe.utils.StringUtils.splitString(attName.getLocalPart(),":");
//			
//			//if is the targetNamespace declaration
//			if ((ns.length == 1) && (ns[0].compareTo(XMLTags.XML_NAMESPACE)==0)){
//				setTargetNamespace(attValue);
//			}
//			
//			//If it founds the 'xmlns' is a new namespace declaration and it has to parse it
//			if ((ns.length>1) && (ns[0].compareTo(XMLTags.XML_NAMESPACE)==0)){
//				parseNameSpace(ns[1],attValue);
//			}
//			
//			//If its the "SCHEMA LOCATION" attribute, it means that there are schema and it tries to parse it
//			if ((ns.length>1) && (ns[1].compareTo(XMLTags.XML_SCHEMA_LOCATION)==0)){
//				parseSchemaLocation(ns[0],attValue);
//			} 
//		}		
//	}
//	
//	/***********************************************
//	 * <parseSchemaLocation>
//	 * It downloads the schema's file and parse it
//	 * @param xmlnsName : Alias
//	 * @param xmlnsValue: URI 
//	 ***********************************************/
//	private void parseSchemaLocation(String schemaAlias, String schemaURI){
//		//If the XML Schema validation is not selected 
//		if (!GPEDefaults.getBooleanProperty(XmlProperties.XML_SCHEMA_VALIDATED)){
//			return;
//		}		
//		//It take the name of the schemas file to open or downlad 
//		StringTokenizer tokenizer = new StringTokenizer(schemaURI, " \t");
//        while (tokenizer.hasMoreTokens()){
//            String URI = tokenizer.nextToken();
//            if (!tokenizer.hasMoreTokens()){
//            	//If it hasn't the name of the schemas file or dont find it,
//            	//it exits, and tries to parse without schema
//            	System.out.println("Error, esquema no encontrado.PARSEO SIN ESQUEMA ");
//            }else{
//            	String schemaLocation = tokenizer.nextToken();
//            	//TODO This line must be replaced by the new downloader
//            	try {
//					URI uri = getSchemaURI(schemaLocation);					
//					addSchema(uri);
//				} catch (SchemaCreationException e) {
//					errorHandler.addError(e);
//				} catch (SchemaLocationWarning e) {
//					errorHandler.addWarning(e);
//				}
//			}
//        }
//	}
//	
//	/****************************************************************************
//	 * <getSchemaFile>
//	 * It downloads the schema if it's a remote schema
//	 * else it tries to open a local file and return if it's succesfull
//	 * @param String schema location
//	 * @return Uri
//	 * @throws SchemaLocationWarning 
//	 ****************************************************************************/
//	private URI getSchemaURI(String schemaLocation) throws SchemaLocationWarning{
//		File f = null;
//		//If it is a local file, it has to construct the absolute route
//		if (schemaLocation.indexOf("http://") != 0){
//			f = new File(schemaLocation);
//			if (!(f.isAbsolute())){
//				schemaLocation = new File(xmlURI).getParentFile().getAbsolutePath() + File.separator +  schemaLocation;
//				f = new File(schemaLocation);
//			}
//			try {
//				return new URI(f.getAbsolutePath());
//			} catch (URISyntaxException e) {
//				throw new SchemaLocationWarning(schemaLocation,e);
//			}			
//		}
//		//Else it is an URL direction and it has to download it.
//		else {
//			URL url;		
//			try {
//				url = new URL(schemaLocation);
//				//Download the schema without cancel option.
//				f = DownloadUtilities.downloadFile(url,"gml_schmema.xsd");	
//				return new URI(f.getAbsolutePath());
//			} catch (Exception e) {
//				throw new SchemaLocationWarning(schemaLocation,e);
//			}
//		}
//	}
//	
//	/************************************************
//	 * <parseNamespace>
//	 * It adds an XML namespace tag to the hashtable
//	 * @param xmlnsName : Namespace
//	 * @param xmlnsValue: URI 
//	 ************************************************/
//	private void parseNameSpace(String xmlnsName,String xmlnsValue){
//		addNamespacePrefix(xmlnsName, xmlnsValue);	
//	}
//}
