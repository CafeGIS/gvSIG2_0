package org.gvsig.catalog.csw.messages;

import org.apache.commons.httpclient.NameValuePair;
import org.gvsig.catalog.csw.drivers.CSWCapabilities;
import org.gvsig.catalog.csw.drivers.profiles.CSWAbstractProfile;
import org.gvsig.catalog.csw.parsers.CSWConstants;
import org.gvsig.catalog.languages.FilterEncoding;
import org.gvsig.catalog.querys.CatalogQuery;
import org.gvsig.catalog.querys.Search;
import org.gvsig.catalog.utils.CatalogConstants;
import org.gvsig.catalog.utils.Strings;
import org.gvsig.i18n.Messages;

import com.iver.utiles.swing.jcomboServer.ServerData;


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
public abstract class CSWAbstractMessages {
	private CSWCapabilities capabilities = null;
	private CatalogQuery query = null;
	private CSWAbstractProfile profile = null;
	
	/**
	 * @param profile
	 * The profile to create the query
	 */
	public CSWAbstractMessages(CSWAbstractProfile profile) {
		super();
		this.profile = profile;		
	}

	public static NameValuePair[] getHTTPGETCapabilities() {        
		NameValuePair nvp1 = new NameValuePair("request", CSWConstants.OPERATION_GETCAPABILITIES);
		NameValuePair nvp2 = new NameValuePair("service", ServerData.SERVER_SUBTYPE_CATALOG_CSW);
		NameValuePair nvp3 = new NameValuePair("acceptFormats","text/xml");
		return new NameValuePair[] { nvp1, nvp2, nvp3 };
	} 
	
	/**
	 * The get capabilities operation started with lower case.
	 * There are some servers that has this bug 
	 * @return
	 */
	public static NameValuePair[] getHTTPGETCapabilitiesLower() {        
		NameValuePair nvp1 = new NameValuePair("request", CSWConstants.OPERATION_GETCAPABILITIESToLOWER);
		NameValuePair nvp2 = new NameValuePair("service", ServerData.SERVER_SUBTYPE_CATALOG_CSW);
		NameValuePair nvp3 = new NameValuePair("acceptFormats","text/xml");
		return new NameValuePair[] { nvp1, nvp2, nvp3 };
	}

	public static NameValuePair[] getHTTPGETDescribeRecord(String version) {        
		NameValuePair nvp1 = new NameValuePair("request", CSWConstants.OPERATION_DESCRIBERECORD);
		NameValuePair nvp2 = new NameValuePair("service", ServerData.SERVER_SUBTYPE_CATALOG_CSW);
		NameValuePair nvp3 = new NameValuePair("version", version);
		return new NameValuePair[] { nvp1, nvp2, nvp3 };
	} 	

	/**
	 * Creates and returns the GetRecords request
	 * @param query
	 * @param firstRecord
	 * @return
	 */	 
	public String getHTTPPostGetRecordsMessage(CSWCapabilities capabilities, CatalogQuery query, ServerData serverData, int firstRecord) {
		this.capabilities = capabilities;
		this.query = query;		
		profile.setServerData(serverData);
		StringBuffer buffer = new StringBuffer();
		buffer.append(createGetRecordsHeader(firstRecord));
		return buffer.toString();
	}

	/**
	 * Create the GetRecords header. 
	 * @param firstRecord
	 * @return
	 */
	protected String createGetRecordsHeader(int firstRecord){
		StringBuffer buffer = new StringBuffer();
		buffer.append(CatalogConstants.XML_HEADER_ENCODING); 
		buffer.append("<" + CSWConstants.CSW_NAMESPACE + 
				":" + CSWConstants.CSW_GET_RECORDS);
		buffer.append(" " + CSWConstants.CSW_SERVICE + "=\"" + ServerData.SERVER_SUBTYPE_CATALOG_CSW + "\" ");
		buffer.append(CSWConstants.CSW_VERSION + "=\"" + capabilities.getVersion() + "\" ");
		buffer.append(CatalogConstants.XML_NS + "=\"" +CSWConstants.CSW_NAMESPACE_URI + "\" ");
		buffer.append(CatalogConstants.XML_NS + ":" + CSWConstants.CSW_NAMESPACE + 
				"=\"" + CSWConstants.CSW_NAMESPACE_URI + "\" ");
		buffer.append(CatalogConstants.XML_NS + ":" + CSWConstants.OGC_NAMESPACE + 
				"=\"" + CSWConstants.OGC_NAMESPACE_URI + "\" ");
		buffer.append(CatalogConstants.XML_NS + ":" + CSWConstants.GML_NAMESPACE + 
				"=\"" + CSWConstants.GML_NAMESPACE_URI + "\" ");
		buffer.append(CSWConstants.CSW_START_POSITION + "='" +	firstRecord  + "' ");
		buffer.append(CSWConstants.CSW_MAX_RECORDS + "='10' ");
		if (capabilities.getOutputFormat() != null){
			buffer.append(CSWConstants.CSW_OUTPUTFORMAT + "=\"" + capabilities.getOutputFormat() + "\" ");
		}
		if (getOutputSchema(capabilities.getOutputSchema()) != null){
			buffer.append(CSWConstants.CSW_OUTPUTSCHEMA + "=\"" + getOutputSchema(capabilities.getOutputSchema()) + "\" ");
		}else{
			buffer.append(CSWConstants.CSW_OUTPUTSCHEMA + "=\"csw:IsoRecord\" ");
		}
		buffer.append("resultType=\"" +
				getResultType(capabilities.getResultType()) + "\">");
		buffer.append(createGetRecordsQuery());
		buffer.append("</" + CSWConstants.CSW_NAMESPACE + ":" + CSWConstants.OPERATION_GETRECORDS + ">");
		return buffer.toString();
	}

	/**
	 * Returns a common reslut type if the getCapabilities doesn't have
	 * one. Sometimes it works.
	 * 
	 * 
	 * @return Just one String
	 * @param resultType The array of result types parsed.
	 */
	protected String getResultType(String[] resultType) {        
		if (resultType == null)
			return "results";

		for (int i=0 ; i<resultType.length ; i++){

		}
		return resultType[0];
	} 

	/**
	 * Returns the OutputSchema. If the ISO19115 is supported,
	 * we prefer this
	 * @param OutputFormat The array of outputFormats parsed.
	 * @return Just one String
	 * @param outputSchemas 
	 */
	protected String getOutputSchema(String[] outputSchemas) {        
		if (outputSchemas == null){
			return null;
		}		    
		return outputSchemas[0];
	} 

	/**
	 * @return the Query xml sub tree of the GetRecords
	 * operation
	 */
	protected String createGetRecordsQuery(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("<" + CSWConstants.CSW_NAMESPACE + ":" + CSWConstants.QUERY + " ");
		if (capabilities.getTypeNames() != null){
			buffer.append(CSWConstants.TYPENAMES + "=\"" +
					Strings.getBlankSeparated(capabilities.getTypeNames()) + "\"");
		}
		buffer.append(">");
		buffer.append(getElementSetNameLabel(query.getService()));
		buffer.append(createGetRecordsConstraint());
		buffer.append("</" + CSWConstants.CSW_NAMESPACE + ":" + CSWConstants.QUERY + ">");
		return buffer.toString();
	}

	/**
	 * Create and return the ElementName and the ElementSetName tags
	 * @param searchType
	 * @return
	 */
	protected String getElementSetNameLabel(Search searchType) {        
		StringBuffer buffer = new StringBuffer();
		buffer.append("<" + CSWConstants.CSW_NAMESPACE + ":" + CSWConstants.ELEMENTSETNAME + ">");
		buffer.append(CSWConstants.FULL);
		buffer.append("</" + CSWConstants.CSW_NAMESPACE + ":" + CSWConstants.ELEMENTSETNAME + ">");
		if (profile.getElementName() != null){
			buffer.append("<" + CSWConstants.CSW_NAMESPACE + ":" + CSWConstants.ELEMENTNAME + ">");
			buffer.append(profile.getElementName());
			buffer.append("</" + CSWConstants.CSW_NAMESPACE + ":" + CSWConstants.ELEMENTNAME + ">");
		}
		return buffer.toString();        
	} 	

	/**
	 * @return the Constarint xml sub tree of the GetRecords
	 * operation
	 */
	protected String createGetRecordsConstraint(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("<" + CSWConstants.CSW_NAMESPACE + ":" + CSWConstants.CONSTRAINT);
		buffer.append(" " + CSWConstants.VERSION + "='" + getContraintVersion() + "'>");
		buffer.append(createFilterEncoding());
		buffer.append("</" + CSWConstants.CSW_NAMESPACE + ":" + CSWConstants.CONSTRAINT + ">");
		return buffer.toString();
	}	

	/**
	 * @return the constraint version
	 */
	protected abstract String getContraintVersion();

	/**
	 * Creates the filter encoding. It can be
	 * overridden by the children classes. 
	 * @return
	 * The filter encoding to use
	 */
	protected FilterEncoding getFilterEncoding(){
		return new FilterEncoding();
	}
	
	/**
	 * Creates the filter encoding query
	 * @param query
	 * @return
	 */
	protected String createFilterEncoding(){		
		FilterEncoding filter = getFilterEncoding();
		if (query.getTitle() != null){
			filter.addClauses(profile.getTitle(),
					query.getTitle(),
					query.getTitleFilter(),					
					FilterEncoding.PROPERTY_IS_LIKE, 
					FilterEncoding.TYPE_LITERAL,
					FilterEncoding.AND);
		}
		if (query.isMinimized()){
			if (query.getAbstract() != null) {
				filter.addClauses(profile.getAbstract(), 
						query.getAbstract(),
						FilterEncoding.ANY_WORDS,
						FilterEncoding.PROPERTY_IS_LIKE,
						FilterEncoding.TYPE_LITERAL,
						FilterEncoding.OR);
			}
		}else{
			if (query.getAbstract() != null) {
				filter.addClauses(profile.getAbstract(), 
						query.getAbstract(),
						FilterEncoding.ANY_WORDS);
			}
			if (query.getThemeKey() != null) {
				filter.addClauses(profile.getKeywords(), query.getThemeKey(),
						FilterEncoding.ANY_WORDS);

			}
			if ((query.getTopic() != null) && (!query.getTopic().equals(Messages.getText("cathegoryAny")))) {
				filter.addClauses(profile.getTopic(), profile.getTopicValue(query.getTopic()),
						FilterEncoding.EXACT_WORDS);
			}
			if (query.getScale() != null) {
				filter.addClauses(profile.getScale(), query.getScale(),
						FilterEncoding.EXACT_WORDS);
			}
			if (query.getProvider() != null) {
				filter.addClauses(profile.getProvider(), 
						filter.getWildCard() + query.getProvider() + filter.getWildCard(),
						FilterEncoding.EXACT_WORDS,
						FilterEncoding.PROPERTY_IS_EQUALS_TO,
						FilterEncoding.TYPE_LITERAL,
						FilterEncoding.AND);						
			}

			if (query.getDateFrom() != null) {
				filter.addClauses(profile.getDateFrom(), query.getDateFrom(), 
						FilterEncoding.EXACT_WORDS,
						FilterEncoding.PROPERTY_IS_GREATER_THAN,
						FilterEncoding.TYPE_LITERAL,
						FilterEncoding.AND);								
			}
			if (query.getDateTo() != null) {
				filter.addClauses(profile.getDateTo(), query.getDateTo(),
						FilterEncoding.EXACT_WORDS,
						FilterEncoding.PROPERTY_IS_LESS_THAN,
						FilterEncoding.TYPE_LITERAL,
						FilterEncoding.AND);					
			}
		}
		if ((query.getCoordenates() != null) && (query.isCoordinatesClicked())){
			filter.addBoundingBox(query.getCoordenates(), profile.getCoordinates(),
					getCoordinatesOption(query.getCoordenatesFilter()));

		}	
		return filter.toString();
	}

	/**
	 * This function returns true only when the user has choosen the
	 * "Fully Outside Of" of the coordinates option.
	 * @param coordinatesOption 
	 * @return
	 */
	protected boolean getCoordinatesOption(String coordinatesOption) {        
		if ((coordinatesOption.equals(Messages.getText("coordinatesEqual"))) ||
				(coordinatesOption.equals(Messages.getText("coordinatesContains"))) ||
				(coordinatesOption.equals(Messages.getText("coordinatesEnclose"))))
			return false;

		return true; 
	} 
}
