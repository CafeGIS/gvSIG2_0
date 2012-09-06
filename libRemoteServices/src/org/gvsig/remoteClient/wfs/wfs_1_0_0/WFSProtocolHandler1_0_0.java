package org.gvsig.remoteClient.wfs.wfs_1_0_0;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;

import org.gvsig.remoteClient.utils.CapabilitiesTags;
import org.gvsig.remoteClient.wfs.WFSOperation;
import org.gvsig.remoteClient.wfs.WFSProtocolHandler;
import org.gvsig.remoteClient.wfs.WFSStatus;
import org.gvsig.remoteClient.wfs.edition.WFSTTags;
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
import org.gvsig.remoteClient.wfs.wfs_1_0_0.request.WFSDescribeFeatureTypeRequest1_0_0;
import org.gvsig.remoteClient.wfs.wfs_1_0_0.request.WFSGetFeatureRequest1_0_0;
import org.gvsig.remoteClient.wfs.wfs_1_0_0.request.WFSTLockFeatureRequest1_0_0;
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
 * $Id: WFSProtocolHandler1_0_0.java 27881 2009-04-07 14:47:27Z jpiera $
 * $Log$
 * Revision 1.12  2007-02-19 11:43:10  jorpiell
 * AÃ±adidos los filtros por Ã¡rea
 *
 * Revision 1.11  2007/02/09 14:11:01  jorpiell
 * Primer piloto del soporte para WFS 1.1 y para WFS-T
 *
 * Revision 1.10  2007/01/16 08:30:22  csanchez
 * Sistema de Warnings y Excepciones adaptado a BasicException
 *
 * Revision 1.9  2006/12/22 11:31:01  jorpiell
 * Cambio del parser 2.x
 *
 * Revision 1.8  2006/10/31 09:36:51  jorpiell
 * Se devuelve el tipo de la entidad completo, y no sus hijos
 *
 * Revision 1.7  2006/10/10 12:52:28  jorpiell
 * Soporte para features complejas.
 *
 * Revision 1.6  2006/06/14 07:54:18  jorpiell
 * Se parsea el online resource que antes se ignoraba
 *
 * Revision 1.5  2006/05/25 10:20:57  jorpiell
 * Se ha cambiado el nombre de la clase WFSField por la clase WFSAttribute, porque resultaba confuso
 *
 * Revision 1.4  2006/05/23 13:23:22  jorpiell
 * Se ha cambiado el final del bucle de parseado y se tiene en cuenta el online resource
 *
 * Revision 1.2  2006/04/20 16:39:16  jorpiell
 * Añadida la operacion de describeFeatureType y el parser correspondiente.
 *
 * Revision 1.1  2006/04/19 12:51:35  jorpiell
 * Añadidas algunas de las clases del servicio WFS
 *
 *
 */
/**
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 */
public class WFSProtocolHandler1_0_0 extends WFSProtocolHandler{
	public WFSProtocolHandler1_0_0(){
		this.version = "1.0.0";
		this.name = "WFS1.0.0";		
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
				//Parse the capabilities document
				tag = parser.nextTag();
				while(tag != KXmlParser.END_DOCUMENT)
				{
					switch(tag)
					{
					case KXmlParser.START_TAG:
						if (parser.getName().compareTo(CapabilitiesTags.SERVICE)==0)
						{
							parseServiceTag(parser);
						} 
						else if (parser.getName().compareTo(CapabilitiesTags.CAPABILITY)==0)
						{
							parseCapabilityTag(parser);
						} 
						else if (parser.getName().compareTo(CapabilitiesTags.WFS_FEATURETYPELIST)==0)
						{
							parseFeatureTypeListTag(parser);
						} 
						break;
					case KXmlParser.END_TAG:                            
						break;
					case KXmlParser.TEXT:									                         
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
	 * <p>Parses the Service Information </p>
	 */    
	private void parseServiceTag(KXmlParser parser) throws IOException, XmlPullParserException 
	{
		int currentTag;
		boolean end = false;

		parser.require(KXmlParser.START_TAG, null, CapabilitiesTags.SERVICE);
		currentTag = parser.next();

		while (!end) 
		{
			switch(currentTag)
			{
			case KXmlParser.START_TAG:
				if (parser.getName().compareToIgnoreCase(CapabilitiesTags.NAME)==0)
				{
					serviceInfo.name = parser.nextText(); 
				}   
				else if (parser.getName().compareTo(CapabilitiesTags.WFS_TITLE)==0)
				{
					serviceInfo.title = parser.nextText(); 
				}
				else if (parser.getName().compareTo(CapabilitiesTags.WFS_ABSTRACT)==0)
				{
					serviceInfo.abstr = parser.nextText(); 
				} 
				else if (parser.getName().compareTo(CapabilitiesTags.ONLINERESOURCE)==0)
				{
					serviceInfo.online_resource = parser.nextText();

				}	                       
				break;
			case KXmlParser.END_TAG:
				if (parser.getName().compareTo(CapabilitiesTags.SERVICE) == 0)
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

	private void parseCapabilityTag(KXmlParser parser) throws IOException, XmlPullParserException
	{   
		int currentTag;
		boolean end = false;

		parser.require(KXmlParser.START_TAG, null, CapabilitiesTags.CAPABILITY);
		currentTag = parser.next();

		while (!end) 
		{
			switch(currentTag)
			{
			case KXmlParser.START_TAG:
				if (parser.getName().compareTo(CapabilitiesTags.REQUEST)==0)
				{
					parseRequestTag(parser); 
				}   
				else if (parser.getName().compareTo(CapabilitiesTags.EXCEPTION)==0)
				{
					//TODO Parse exception tags...
					parser.skipSubTree();
				}
				else if ((parser.getName().compareTo(CapabilitiesTags.VENDORSPECIFICCAPABILITIES)==0) ||
						(parser.getName().compareTo(CapabilitiesTags.USERDEFINEDSYMBOLIZATION )==0))
				{
					parser.skipSubTree();
				}                   
				break;
			case KXmlParser.END_TAG:
				if (parser.getName().compareTo(CapabilitiesTags.CAPABILITY) == 0)
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
	 * <p>Parses the Request tag </p>
	 */ 
	private void parseRequestTag(KXmlParser parser) throws IOException, XmlPullParserException
	{   
		int currentTag;
		boolean end = false;

		parser.require(KXmlParser.START_TAG, null, CapabilitiesTags.REQUEST);
		currentTag = parser.next();

		while (!end) 
		{
			switch(currentTag)
			{
			case KXmlParser.START_TAG:
				if (parser.getName().compareTo(CapabilitiesTags.GETCAPABILITIES)==0){
					parserDcpType(parser, CapabilitiesTags.GETCAPABILITIES);					
				} else if (parser.getName().compareTo(CapabilitiesTags.WFS_DESCRIBEFEATURETYPE)==0)	{
					parserDcpType(parser, CapabilitiesTags.WFS_DESCRIBEFEATURETYPE);		                                             
				}else if (parser.getName().compareTo(CapabilitiesTags.WFS_GETFEATURE)==0){
					parserDcpType(parser, CapabilitiesTags.WFS_GETFEATURE);
				}else if (parser.getName().compareTo(CapabilitiesTags.WFS_LOCKFEATURE)==0){
					parserDcpType(parser, CapabilitiesTags.WFS_LOCKFEATURE);
				}else if (parser.getName().compareTo(CapabilitiesTags.WFS_TRANSACTION)==0){
					parserDcpType(parser, CapabilitiesTags.WFS_TRANSACTION);
				}               
				break;
			case KXmlParser.END_TAG:
				if (parser.getName().compareTo(CapabilitiesTags.REQUEST) == 0)
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
	 * It parses the feature typelist tag
	 * @param parser
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	private void parseFeatureTypeListTag(KXmlParser parser) throws IOException, XmlPullParserException{
		int currentTag;
		boolean end = false;

		parser.require(KXmlParser.START_TAG, null, CapabilitiesTags.WFS_FEATURETYPELIST);
		currentTag = parser.next();

		while (!end) 
		{
			switch(currentTag)
			{
			case KXmlParser.START_TAG:
				if (parser.getName().compareToIgnoreCase(CapabilitiesTags.WFS_FEATURETYPE)==0){
					//Parse the namespaces...
					parseNamespaces(parser);	
					WFSFeature1_0_0 feature = new WFSFeature1_0_0();
					feature.parse(parser);	
					features.put(feature.getName(),feature);
				} 				         
				break;
			case KXmlParser.END_TAG:
				if (parser.getName().compareTo(CapabilitiesTags.WFS_FEATURETYPELIST) == 0)
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

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.remoteClient.wfs.WFSProtocolHandler#parseDescribeFeatureType(java.io.File, java.lang.String)
	 */
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
	protected boolean parseGetFeature(File f, String nameSpace) throws WFSException {
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
	protected boolean parseTransaction(File f, String nameSpace, WFSTTransaction transaction) throws WFSException {
		int tag;
		XMLSchemaParser parser = null;
		parser = new XMLSchemaParser();
		try
		{
			parser.setInput(f);
			parser.nextTag();
			if (parser.getName().compareTo(CapabilitiesTags.SERVICE_EXCEPTION_REPORT)==0){
				throw ExceptionsFactory.parseExceptionReport(parser);
			}		


			if ( parser.getEventType() != KXmlParser.END_DOCUMENT ) 
			{     				      
				tag = parser.nextTag();
				while(tag != KXmlParser.END_DOCUMENT)
				{
					switch(tag)
					{
					case KXmlParser.START_TAG:
						if (parser.getName().compareTo(WFSTTags.WFST_TRANSACTIONRESULT)==0){
							parseTransactionResult(parser, transaction);
						}else if (parser.getName().compareTo(WFSTTags.WFST_TRANSACTIONRESPONSE)==0){
							parseFeaturesLocked(parser);
						}						
						break;
					case KXmlParser.END_TAG:                            
						break;
					case KXmlParser.TEXT:
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
	 * Parse the transaction result XML sub tree
	 * @param parser
	 * @param transaction
	 * @throws IOException 
	 * @throws XmlPullParserException 
	 */
	private void parseTransactionResult(XMLSchemaParser parser,
			WFSTTransaction transaction) throws XmlPullParserException, IOException {
		int currentTag;
		boolean end = false;		

		currentTag = parser.next();

		while (!end) 
		{
			switch(currentTag)
			{
			case KXmlParser.START_TAG:
				if (parser.getName().compareTo(WFSTTags.WFST_STATUS)==0){
					parseStatus(parser, transaction);
				}else if (parser.getName().compareTo(WFSTTags.WFST_TRANSACTIONMESSAGE)==0){
					parser.next();
					transaction.setMessage(parser.getText());
				}
				break;
			case KXmlParser.END_TAG:
				if (parser.getName().compareTo(WFSTTags.WFST_TRANSACTIONRESULT)==0)
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
	 * Parser WFST Status subtree
	 * @param parser
	 * Teh xML parser
	 * @param transaction
	 * The current transaction
	 * @throws IOException 
	 * @throws XmlPullParserException 
	 */
	private void parseStatus(XMLSchemaParser parser, WFSTTransaction transaction) throws XmlPullParserException, IOException {
		int currentTag;
		boolean end = false;		

		currentTag = parser.next();

		while (!end) 
		{
			switch(currentTag)
			{
			case KXmlParser.START_TAG:
				if (parser.getName().compareTo(WFSTTags.WFST_SUCCESS)==0){
					transaction.setStatus(WFSTTransaction.STATUS_SUCCESS);
				}else if (parser.getName().compareTo(WFSTTags.WFST_FAILED)==0){
					transaction.setStatus(WFSTTransaction.STATUS_FAILED);
				}
				break;
			case KXmlParser.END_TAG:
				if (parser.getName().compareTo(WFSTTags.WFST_STATUS)==0)
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

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.remoteClient.wfs.WFSProtocolHandler#parseLockFeature(java.io.File, java.lang.String)
	 */
	protected boolean parseLockFeature(File f, String nameSpace, WFSStatus status) throws WFSException {
		int tag;
		XMLSchemaParser parser = null;
		parser = new XMLSchemaParser();
		try
		{
			parser.setInput(f);
			parser.nextTag();
			if (parser.getName().compareTo(CapabilitiesTags.SERVICE_EXCEPTION_REPORT)==0){
				throw ExceptionsFactory.parseExceptionReport(parser);
			}		


			if ( parser.getEventType() != KXmlParser.END_DOCUMENT ) 
			{     				      
				tag = parser.nextTag();
				while(tag != KXmlParser.END_DOCUMENT)
				{
					switch(tag)
					{
					case KXmlParser.START_TAG:
						if (parser.getName().compareTo(WFSTTags.WFST_LOCKID)==0){
							parser.next();
							status.addFeatureLocked(parser.getText());
						}else if (parser.getName().compareTo(WFSTTags.WFST_FEATURESLOCKED)==0){
							parseFeaturesLocked(parser);
						}						
						break;
					case KXmlParser.END_TAG:                            
						break;
					case KXmlParser.TEXT:
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
	 * It parses the featuresLocked tag
	 * @param parser
	 * @throws IOException 
	 * @throws XmlPullParserException 
	 */
	private void parseFeaturesLocked(XMLSchemaParser parser) throws XmlPullParserException, IOException {
		int currentTag;
		boolean end = false;		

		currentTag = parser.next();

		while (!end) 
		{
			switch(currentTag)
			{
			case KXmlParser.START_TAG:
				if (parser.getName().compareTo(WFSTTags.WFST_FEATURESID)==0){

				}
				break;
			case KXmlParser.END_TAG:
				if (parser.getName().compareTo(WFSTTags.WFST_FEATURESLOCKED)==0)
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

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.remoteClient.wfs.WFSProtocolHandler#createDescribeFeatureTypeRequest(org.gvsig.remoteClient.wfs.WFSStatus)
	 */
	protected WFSDescribeFeatureTypeRequest createDescribeFeatureTypeRequest(
			WFSStatus status) {
		return new WFSDescribeFeatureTypeRequest1_0_0(status, this);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.remoteClient.wfs.WFSProtocolHandler#createGetFeatureRequest(org.gvsig.remoteClient.wfs.WFSStatus)
	 */
	protected WFSGetFeatureRequest createGetFeatureRequest(WFSStatus status) {
		return new WFSGetFeatureRequest1_0_0(status, this);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.remoteClient.wfs.WFSProtocolHandler#createLockFeatureRequest(org.gvsig.remoteClient.wfs.WFSStatus)
	 */
	protected WFSTLockFeatureRequest createLockFeatureRequest(WFSStatus status) {
		return new WFSTLockFeatureRequest1_0_0(status, this);
	}
}