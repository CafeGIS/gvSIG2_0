package org.gvsig.catalog.csw.drivers;

import java.net.URL;

import org.gvsig.catalog.csw.parsers.CSWSupportedProtocolOperations;
import org.gvsig.catalog.drivers.CatalogCapabilities;


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
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class CSWCapabilities extends CatalogCapabilities {
	private CSWSupportedProtocolOperations operations = null;
	private String responseHandler = null;
	private String hopCount = null;
	private String distributedSearch = null;
	private String constraint = null;
	private String[] CONSTRAINTLANGUAGE = null;
	private String[] elementSetName = null;
	private String[] typeNames = null;
	private String[] resultType = null;
	private String[] NAMESPACE = null;
	private String[] outputFormat = null;
	private String[] outputSchema = null;
	private CSWException exception = null;
		
	/**
	 * @return the exception
	 */
	public CSWException getException() {
		return exception;
	}

	/**
	 * @param exception the exception to set
	 */
	public void setException(CSWException exception) {
		this.exception = exception;
	}
	
	/**
	 * @return the outputSchema
	 */
	public String[] getOutputSchema() {
		return outputSchema;
	}

	/**
	 * @param outputSchema the outputSchema to set
	 */
	public void setOutputSchema(String[] outputSchema) {
		this.outputSchema = outputSchema;
	}

	/**
	 * @return the operations
	 */
	public CSWSupportedProtocolOperations getOperations() {
		return operations;
	}

	/**
	 * @param operations the operations to set
	 */
	public void setOperations(CSWSupportedProtocolOperations operations) {
		this.operations = operations;
	}

	/**
	 * @return the outputFormat
	 */
	public String[] getOutputFormat() {
		return outputFormat;
	}

	/**
	 * @param outputFormat the outputFormat to set
	 */
	public void setOutputFormat(String[] outputFormat) {
		this.outputFormat = outputFormat;
	}

	public CSWCapabilities(URL url) {
		super();
		operations = new CSWSupportedProtocolOperations(url);
	}
	
	/**
	 * @return the responseHandler
	 */
	public String getResponseHandler() {
		return responseHandler;
	}
	
	/**
	 * @param responseHandler the responseHandler to set
	 */
	public void setResponseHandler(String responseHandler) {
		this.responseHandler = responseHandler;
	}
	
	/**
	 * @return the hopCount
	 */
	public String getHopCount() {
		return hopCount;
	}
	
	/**
	 * @param hopCount the hopCount to set
	 */
	public void setHopCount(String hopCount) {
		this.hopCount = hopCount;
	}
	
	/**
	 * @return the distributedSearch
	 */
	public String getDistributedSearch() {
		return distributedSearch;
	}
	
	/**
	 * @param distributedSearch the distributedSearch to set
	 */
	public void setDistributedSearch(String distributedSearch) {
		this.distributedSearch = distributedSearch;
	}
	
	/**
	 * @return the constraint
	 */
	public String getConstraint() {
		return constraint;
	}
	
	/**
	 * @param constraint the constraint to set
	 */
	public void setConstraint(String constraint) {
		this.constraint = constraint;
	}
	
	/**
	 * @return the cONSTRAINTLANGUAGE
	 */
	public String[] getCONSTRAINTLANGUAGE() {
		return CONSTRAINTLANGUAGE;
	}
	
	/**
	 * @param constraintlanguage the cONSTRAINTLANGUAGE to set
	 */
	public void setCONSTRAINTLANGUAGE(String[] constraintlanguage) {
		CONSTRAINTLANGUAGE = constraintlanguage;
	}
	
	/**
	 * @return the elementSetName
	 */
	public String[] getElementSetName() {
		return elementSetName;
	}
	
	/**
	 * @param elementSetName the elementSetName to set
	 */
	public void setElementSetName(String[] elementSetName) {
		this.elementSetName = elementSetName;
	}
	
	/**
	 * @return the typeNames
	 */
	public String[] getTypeNames() {
		return typeNames;
	}
	
	/**
	 * @param typeNames the typeNames to set
	 */
	public void setTypeNames(String[] typeNames) {
		this.typeNames = typeNames;
	}
	
	/**
	 * @return the resultType
	 */
	public String[] getResultType() {
		return resultType;
	}
	
	/**
	 * @param resultType the resultType to set
	 */
	public void setResultType(String[] resultType) {
		this.resultType = resultType;
	}
	
	/**
	 * @return the nAMESPACE
	 */
	public String[] getNAMESPACE() {
		return NAMESPACE;
	}
	
	/**
	 * @param namespace the nAMESPACE to set
	 */
	public void setNAMESPACE(String[] namespace) {
		NAMESPACE = namespace;
	}
	
	
}
