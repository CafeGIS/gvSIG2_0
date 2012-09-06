package org.gvsig.remoteClient.wfs.wfs_1_1_0;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;

import org.gvsig.remoteClient.utils.CapabilitiesTags;
import org.gvsig.remoteClient.wfs.WFSOperation;
import org.gvsig.remoteClient.wfs.WFSProtocolHandler;
import org.gvsig.remoteClient.wfs.WFSStatus;
import org.gvsig.remoteClient.wfs.edition.WFSTTransaction;
import org.gvsig.remoteClient.wfs.exceptions.ExceptionsFactory;
import org.gvsig.remoteClient.wfs.exceptions.WFSException;
import org.gvsig.remoteClient.wfs.exceptions.WFSGetFeatureException;
import org.gvsig.remoteClient.wfs.request.WFSDescribeFeatureTypeRequest;
import org.gvsig.remoteClient.wfs.request.WFSGetFeatureRequest;
import org.gvsig.remoteClient.wfs.request.WFSTLockFeatureRequest;
import org.gvsig.remoteClient.wfs.schema.XMLElement;
import org.gvsig.remoteClient.wfs.schema.XMLElementsFactory;
import org.gvsig.remoteClient.wfs.schema.XMLSchemaParser;
import org.gvsig.remoteClient.wfs.wfs_1_1_0.request.WFSDescribeFeatureTypeRequest1_1_0;
import org.gvsig.remoteClient.wfs.wfs_1_1_0.request.WFSGetFeatureRequest1_1_0;
import org.gvsig.remoteClient.wfs.wfs_1_1_0.request.WFSTLockFeatureRequest1_1_0;
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
 * $Id: WFSProtocolHandler1_1_0.java 27881 2009-04-07 14:47:27Z jpiera $
 * $Log$
 * Revision 1.1  2007-02-09 14:11:01  jorpiell
 * Primer piloto del soporte para WFS 1.1 y para WFS-T
 *
 *
 */
/**
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 */
public class WFSProtocolHandler1_1_0 extends WFSProtocolHandler{
	
	public WFSProtocolHandler1_1_0(){
		this.version = "1.1.0";
		this.name = "WFS1.1.0";		
	}	
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.remoteClient.OGCProtocolHandler#parseCapabilities(java.io.File)
	 */
	public boolean parseCapabilities(File f) {
		int tag;
		XMLSchemaParser parser = null;
      	parser = new XMLSchemaParser();
    	try
    	{
    		parser.setInput(f);
    		parser.nextTag();
    		
			if ( parser.getEventType() != KXmlParser.END_DOCUMENT ) 
			{     	
				parser.require(KXmlParser.START_TAG, null, CapabilitiesTags.WFS_CAPABILITIES_ROOT1_0_0);             
				//Parses the Namespaces...
				parseNamespaces(parser);	
				tag = parser.nextTag();
				while(tag != KXmlParser.END_DOCUMENT)
				{
					switch(tag)
					{
					case KXmlParser.START_TAG:
						if (parser.getName().compareTo(CapabilitiesTags.SERVICE_IDENTIFICATION)==0)
						{
							parseServiceIdentification(parser);
						} 
						else if (parser.getName().compareTo(CapabilitiesTags.SERVICE_PROVIDER)==0)
						{
							parseServiceProvider(parser);
						} 
						else if (parser.getName().compareTo(CapabilitiesTags.OPERATIONS_METADATA)==0)
						{
							parseOperationsMetadata(parser);
						} 
						else if (parser.getName().compareTo(CapabilitiesTags.FEATURE_TYPE_LIST)==0)
						{
							parseFeatureTypeList(parser);
						} 
						else if (parser.getName().compareTo(CapabilitiesTags.FILTER_CAPABILITIES)==0)
						{
							parseFilterCapabilities(parser);
						} 
						break;
					case KXmlParser.END_TAG:                            
						break;
					case KXmlParser.TEXT:
						if (parser.getName()!=null)
							                         
						break;
					}
					tag = parser.next();
				}
				parser.require(KXmlParser.END_DOCUMENT, null, null);                
			}
		}
		catch(XmlPullParserException parser_ex){
			parser_ex.printStackTrace();
			return false;
		}
		catch (IOException ioe) {           
			ioe.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * <p>Parses the Service Identification </p>
	 */    
	private void parseServiceIdentification(KXmlParser parser) throws IOException, XmlPullParserException {
		int currentTag;
		boolean end = false;		
		
		currentTag = parser.next();
		
		while (!end) 
		{
			switch(currentTag)
			{
			case KXmlParser.START_TAG:
				if (parser.getName().compareTo(CapabilitiesTags.WFS_TITLE)==0)
				{
					serviceInfo.title = parser.nextText(); 
				}
				else if (parser.getName().compareTo(CapabilitiesTags.WFS_ABSTRACT)==0)
				{
					serviceInfo.abstr = parser.nextText(); 
				} 			                       
				break;
			case KXmlParser.END_TAG:
				if (parser.getName().compareTo(CapabilitiesTags.SERVICE_IDENTIFICATION) == 0)
					end = true;
				break;
			case KXmlParser.TEXT:                   
				break;
			}
			if (!end){
				currentTag = parser.next();
			}
		}     
	}	
	
	/**
	 * <p>Parses the Service Provider </p>
	 */    
	private void parseServiceProvider(KXmlParser parser) throws IOException, XmlPullParserException {
		int currentTag;
		boolean end = false;		
		
		currentTag = parser.next();
		
		while (!end) 
		{
			switch(currentTag)
			{
			case KXmlParser.START_TAG:
						                       
				break;
			case KXmlParser.END_TAG:
				if (parser.getName().compareTo(CapabilitiesTags.SERVICE_PROVIDER) == 0)
					end = true;
				break;
			case KXmlParser.TEXT:                   
				break;
			}
			if (!end){
				currentTag = parser.next();
			}
		}  
	}	
	/**
	 * <p>Parses the Operations Metadata </p>
	 */    
	private void parseOperationsMetadata(KXmlParser parser) throws IOException, XmlPullParserException {
		int currentTag;
		boolean end = false;	
		
		currentTag = parser.next();
		
		while (!end) 
		{
			switch(currentTag)
			{
			case KXmlParser.START_TAG:
				if (parser.getName().compareTo(CapabilitiesTags.OPERATION)==0)
				{
					String operation = null;
					for (int i=0 ; i<parser.getAttributeCount() ; i++){
						if (parser.getAttributeName(i).compareTo(CapabilitiesTags.OPERATION_NAME)==0){
							operation = parser.getAttributeValue(i);
						}
					}				
					currentTag = parser.nextTag();
					if (parser.getName().compareTo(CapabilitiesTags.DCP)==0)
					{			
						currentTag = parser.nextTag();
						if(parser.getName().compareTo(CapabilitiesTags.HTTP)==0)
						{
							currentTag = parser.nextTag();
							if(parser.getName().compareTo(CapabilitiesTags.GET)==0)
							{
								String value = null;
								for (int i=0 ; i<parser.getAttributeCount() ; i++){
									if (parser.getAttributeName(i).compareTo(CapabilitiesTags.HREF) == 0){
						    			value = parser.getAttributeValue(i);
						    		}
								}	
								if (operation != null){
									if (value == null){
										serviceInfo.addOperation(operation, WFSOperation.PROTOCOL_GET);
									}else{
										serviceInfo.addOperation(operation, WFSOperation.PROTOCOL_GET, value);
									}	
								}
							}
						}
					}					
				}  				
				break;
			case KXmlParser.END_TAG:
				if (parser.getName().compareTo(CapabilitiesTags.OPERATIONS_METADATA) == 0)
					end = true;
				break;
			case KXmlParser.TEXT:                   
				break;
			}
			if (!end){
				currentTag = parser.next();
			}
		}
	}	
	/**
	 * <p>Parses the Feature Type List </p>
	 */    
	private void parseFeatureTypeList(KXmlParser parser) throws IOException, XmlPullParserException {
		int currentTag;
		boolean end = false;

		currentTag = parser.next();
		
		while (!end) 
		{
			switch(currentTag)
			{
			case KXmlParser.START_TAG:
				if (parser.getName().compareToIgnoreCase(CapabilitiesTags.WFS_FEATURETYPE)==0)
				{
					//Parse the namespaces...
					parseNamespaces(parser);	
					WFSFeature1_1_0 feature = new WFSFeature1_1_0();
					feature.parse(parser);	
					features.put(feature.getName(),feature);
				} 				         
				break;
			case KXmlParser.END_TAG:
				if (parser.getName().compareTo(CapabilitiesTags.FEATURE_TYPE_LIST) == 0)
					end = true;
				break;
			case KXmlParser.TEXT:                   
				break;
			}
			if (!end){
				currentTag = parser.next();
			}
		}     
	}	
	/**
	 * <p>Parses the Filter Capabilities </p>
	 */    
	private void parseFilterCapabilities(KXmlParser parser) throws IOException, XmlPullParserException {
		int currentTag;
		boolean end = false;		
		
		currentTag = parser.next();
		
		while (!end) 
		{
			switch(currentTag)
			{
			case KXmlParser.START_TAG:
						                       
				break;
			case KXmlParser.END_TAG:
				if (parser.getName().compareTo(CapabilitiesTags.FILTER_CAPABILITIES) == 0)
					end = true;
				break;
			case KXmlParser.TEXT:                   
				break;
			}
			if (!end){
				currentTag = parser.next();
			}
		}  
	}	
	
	protected boolean parseDescribeFeatureType(File f,String nameSpace) {
		XMLSchemaParser schemaParser = new XMLSchemaParser();
		try {
			schemaParser.parse(f,nameSpace);
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String layerName = getCurrentFeature();
		if (getCurrentFeature().split(":").length>1){
			layerName = getCurrentFeature().split(":")[1];
		}
		XMLElement entity = XMLElementsFactory.getElement(layerName);
		if (entity != null){
			Vector vector = new Vector();
			vector.add(entity);
			setFields(vector);
		}
		return true;
	}

	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.remoteClient.wfs.WFSProtocolHandler#parseGetFeature(java.io.File, java.lang.String)
	 */
	protected boolean parseGetFeature(File f, String nameSpace) throws WFSException{
		XMLSchemaParser parser = null;
      	parser = new XMLSchemaParser();
    	try
    	{
    		parser.setInput(f);
    		parser.nextTag();				
			if (parser.getName().compareTo(CapabilitiesTags.SERVICE_EXCEPTION_REPORT)==0){
				throw ExceptionsFactory.parseExceptionReport(parser);
			}			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new WFSGetFeatureException(e);
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			throw new WFSGetFeatureException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new WFSGetFeatureException(e);
		}        
		
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.remoteClient.wfs.WFSProtocolHandler#parseTransaction(java.io.File, java.lang.String)
	 */
	protected boolean parseTransaction(File f, String nameSpace, WFSTTransaction transaction) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.remoteClient.wfs.WFSProtocolHandler#parseLockFeature(java.io.File, java.lang.String)
	 */
	protected boolean parseLockFeature(File f, String nameSpace, WFSStatus status) throws WFSException {
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.remoteClient.wfs.WFSProtocolHandler#createDescribeFeatureTypeRequest(org.gvsig.remoteClient.wfs.WFSStatus)
	 */
	protected WFSDescribeFeatureTypeRequest createDescribeFeatureTypeRequest(
			WFSStatus status) {
		return new WFSDescribeFeatureTypeRequest1_1_0(status, this);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.remoteClient.wfs.WFSProtocolHandler#createGetFeatureRequest(org.gvsig.remoteClient.wfs.WFSStatus)
	 */
	protected WFSGetFeatureRequest createGetFeatureRequest(WFSStatus status) {
		return new WFSGetFeatureRequest1_1_0(status, this);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.remoteClient.wfs.WFSProtocolHandler#createLockFeatureRequest(org.gvsig.remoteClient.wfs.WFSStatus)
	 */
	protected WFSTLockFeatureRequest createLockFeatureRequest(WFSStatus status) {
		return new WFSTLockFeatureRequest1_1_0(status, this);
	}
}
