package org.gvsig.catalog.csw.parsers;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.gvsig.catalog.csw.drivers.CSWCapabilities;
import org.gvsig.catalog.csw.drivers.CSWCatalogServiceDriver;
import org.gvsig.catalog.metadataxml.XMLNode;
import org.gvsig.catalog.metadataxml.XMLTree;
import org.gvsig.catalog.utils.Strings;




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
 * $Id: CSWCapabilitiesParserv2_0_1.java,v 1.1.2.1 2007/07/23 07:14:24 jorpiell Exp $
 * $Log: CSWCapabilitiesParserv2_0_1.java,v $
 * Revision 1.1.2.1  2007/07/23 07:14:24  jorpiell
 * Catalog refactoring
 *
 *
 */
/**
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class CSWCapabilitiesParser{
	private URL url = null;
	private CSWCatalogServiceDriver driver = null;
	private CSWCapabilities capabilities = null; 
	
	public CSWCapabilitiesParser(URL url, CSWCatalogServiceDriver driver) {
		this.url = url;
		this.driver = driver;
	}

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalogClient.csw.parsers.CSWAbstractCapabilitiesParser#parse(es.gva.cit.catalogClient.metadataxml.XMLNode)
	 */
	public CSWCapabilities parse(XMLNode node) {		
		capabilities = new CSWCapabilities(url);
		if (node.getName().endsWith(CSWConstants.SERVICE_EXPECTION_REPORT)){
			capabilities.setException(CSWExceptionParser.parse(node));
			capabilities.setAvailable(false);
			capabilities.setServerMessage(node.getXmlTree());
		}
		parseHeaderAttributes(node);
		parseServiceIdentification(node.searchNode(CSWConstants.SERVICE_IDENTIFICATION));
		parseServiceProvider(node.searchNode(CSWConstants.SERVICE_PROVIDER));
		parseOperationsMetadata(node.searchNode(CSWConstants.OPERATIONS_METADATA));
		return capabilities;
	}

	/**
	 * Parse the header attributes
	 */
	private void parseHeaderAttributes(XMLNode node){
		capabilities.setVersion(node.searchAtribute(CSWConstants.VERSION));
	}
	
	/**
	 * Parse the service identification tag
	 * @param node
	 */
	private void parseServiceIdentification(XMLNode node){
		if (node != null){		
			XMLNode title = node.searchNode(CSWConstants.TITLE);
			XMLNode abstract_ = node.searchNode(CSWConstants.ABSTRACT);
			String sTitle = "";
			if (title != null){
				sTitle = title.getText() + "\n";
			}
			if (abstract_ != null){
				sTitle = sTitle + abstract_.getText();
			}
			driver.setServerAnswerReady(sTitle);
			capabilities.setServerMessage(sTitle);
			XMLNode version = node.searchNode(CSWConstants.SERVICE_TYPE_VERSION);
			if (version != null){
				capabilities.setVersion(version.getText());
			}
			
		}
	}

	/**
	 * Parse the service provider tag
	 * @param node
	 */
	private void parseServiceProvider(XMLNode node){
		if (node != null){		

		}
	}

	/**
	 * Parse the operations metadata tag
	 * @param node
	 */
	private void parseOperationsMetadata(XMLNode node){
		if (node != null){		
			XMLNode[] operations = XMLTree.searchMultipleNode(node, CSWConstants.OPERATION);
			for (int i = 0; i < operations.length; i++) {
				String sOperation = XMLTree.searchAtribute(operations[i], "name");
				
				if (sOperation.equals(CSWConstants.OPERATION_GETCAPABILITIES)){
					parseCapabilities(operations[i]);
				}
				if (sOperation.equals(CSWConstants.OPERATION_DESCRIBERECORD)){
					parseDescribeRecord(operations[i]);
				}
				if (sOperation.equals(CSWConstants.OPERATION_GETDOMAIN)){
					parseGetDomain(operations[i]);
				}
				if (sOperation.equals(CSWConstants.OPERATION_GETRECORDS)){
					parseGetRecords(operations[i]);
				}
				if (sOperation.equals(CSWConstants.OPERATION_GETRECORDSBYID)){
					parseGetRecordsByID(operations[i]);
				}
				if (sOperation.equals(CSWConstants.OPERATION_TRANSACTION)){
					parseTransaction(operations[i]);
				}
				if (sOperation.equals(CSWConstants.OPERATION_HARVEST)){
					parseHarvest(operations[i]);
				}
			} 
			//Patch for the Galdos severs
			if (capabilities.getVersion().equals(CSWConstants.VERSION_0_9_0)){
				capabilities.getOperations().getGetExtrinsicContent().put(
						CSWConstants.GET,url);
			}
		}
	}
	
	/**
	 * Parse the capabilitioes operation
	 * @param node
	 */
	private void parseCapabilities(XMLNode node) {        
		capabilities.getOperations().setGetCapabilities(getSupportedProtocols(node));
		XMLNode[] parameters = XMLTree.searchMultipleNode(node, CSWConstants.PARAMETER);
		for (int i = 0; i < parameters.length; i++) {
			String sParameter = XMLTree.searchAtribute(parameters[i], CSWConstants.PARAMETER_NAME);
			String[] values = XMLTree.searchMultipleNodeValue(parameters[i], CSWConstants.PARAMETER_VALUE);
			if ((sParameter.toLowerCase().equals(CSWConstants.VERSION)) &&
					(values != null) &&
					values.length == 1){
				capabilities.setVersion(values[0]);
			}
		}
	} 

	/**
 	 * It parses the describe record operation
	 * @param node 
	 * @param prefix 
	 */
	private void parseDescribeRecord(XMLNode node) {        
		capabilities.getOperations().setDescribeRecords(getSupportedProtocols(node));
		XMLNode[] parameters = XMLTree.searchMultipleNode(node, CSWConstants.PARAMETER);
		for (int i = 0; i < parameters.length; i++) {
			String sParameter = XMLTree.searchAtribute(parameters[i], CSWConstants.PARAMETER_NAME);
			String[] values = XMLTree.searchMultipleNodeValue(parameters[i], CSWConstants.PARAMETER_VALUE);
			if ((sParameter.equals(CSWConstants.TYPENAME)) &&
					(values != null)){					
				capabilities.setTypeNames(values);
			}
		}
	} 

	/**
	 * It parses the getRecordsByID operation
	 * @param node 
	 * @param prefix 
	 */
	private void parseGetRecordsByID(XMLNode node) {        
		capabilities.getOperations().setGetRecordsById(getSupportedProtocols(node));
	} 

	/**
     * It parses the getDomain operation
	 * @param node 
	 * @param prefix 
	 */
	private void parseGetDomain(XMLNode node) {        
		capabilities.getOperations().setGetDomain(getSupportedProtocols(node));
	} 

	/**
	 * It parses the getRecords operation
	 * @param node 
	 * @param prefix 
	 */
	private void parseGetRecords(XMLNode node){
		capabilities.getOperations().setGetRecords(getSupportedProtocols(node));
		XMLNode[] parameters = XMLTree.searchMultipleNode(node, CSWConstants.PARAMETER);
		for (int i = 0; i < parameters.length; i++) {
			String sParameter = XMLTree.searchAtribute(parameters[i], CSWConstants.PARAMETER_NAME);
			String[] values = XMLTree.searchMultipleNodeValue(parameters[i], CSWConstants.PARAMETER_VALUE);
			String[] defaultValue = XMLTree.searchMultipleNodeValue(parameters[i], CSWConstants.DEFAULTVALUE);
			if (sParameter.equals("TypeName")) {
				capabilities.setTypeNames(Strings.join(defaultValue, values));
			}
			if (sParameter.equals(CSWConstants.OUTPUTFORMAT)) {
				if (!(Strings.find(CSWConstants.TEXT_XML, defaultValue) ||
						Strings.find(CSWConstants.TEXT_XML, values))) {
					if (!(Strings.find(CSWConstants.APPLICATION_XML, defaultValue) ||
							Strings.find(CSWConstants.APPLICATION_XML, values))) {
						capabilities.setServerMessage("El servidor no devuelve " +
						"texto en formato XML");
						return;
					}
				}
			}
			driver.setOutputFormat(CSWConstants.TEXT_XML);
			if (sParameter.equals(CSWConstants.OUTPUTSCHEMA)) {
				driver.setOutputSchema(Strings.join(defaultValue, values));
			}


			if (sParameter.equals(CSWConstants.RESULTTYPE)) {
				capabilities.setResultType(Strings.join(defaultValue, values));
			}
			if (sParameter.equals(CSWConstants.ELEMENTSETNAME)) {
				capabilities.setElementSetName(Strings.join(defaultValue, values));
			}
			if (sParameter.equals(CSWConstants.CONSTRAINTLANGUAGE)) {
				capabilities.setCONSTRAINTLANGUAGE(Strings.join(defaultValue, values));
				for (int j = 0; j < capabilities.getCONSTRAINTLANGUAGE().length;
				j++) {
					if (capabilities.getCONSTRAINTLANGUAGE()[j].toUpperCase().equals("FILTER")) {
						break;
					} else {
						capabilities.setServerMessage("errorFENotSupported");

					}
				}
			}
		}		
	} 

	/**
	 *  * It parses the Transaction operation
	 * @param node 
	 * @param prefix 
	 */
	private void parseTransaction(XMLNode node) {        
		capabilities.getOperations().setTransaction(getSupportedProtocols(node));
	} 

	/**
	 * It parses the Harvest operation
	 * @param node 
	 * @param prefix 
	 */
	private void parseHarvest(XMLNode node) {        
		capabilities.getOperations().setHarvest(getSupportedProtocols(node));
	} 
	
	/**
	 * This function returns the supported protocols of an operation
	 * defined by node.
	 * @return 
	 * @param node Node that contains the operation information
	 */
	private HashMap getSupportedProtocols(XMLNode node) {        
		HashMap operations = new HashMap();

		XMLNode protocols = XMLTree.searchNode(node, CSWConstants.DCP);

		XMLNode HTTPoperations = XMLTree.searchNode(protocols,CSWConstants.HTTP);
		XMLNode[] childNodes = HTTPoperations.getSubnodes();
		for (int i=0 ; i<childNodes.length ; i++){			
			String sUrl = XMLTree.searchAtribute(childNodes[i],CSWConstants.XLINK_HREF);
			URL url = null;
			try {
				url = new URL(sUrl);
				if ((childNodes[i].getName().endsWith(CSWConstants.GET))){
					operations.put(CSWConstants.GET,
							url);
				}
				if ((childNodes[i].getName().endsWith(CSWConstants.POST))){
					operations.put(CSWConstants.POST,
							url);
				}

			} catch (MalformedURLException e) {
				//The URL can be added
			}			
		}
		return operations;
	}
}
