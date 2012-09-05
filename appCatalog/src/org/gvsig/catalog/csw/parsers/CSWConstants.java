package org.gvsig.catalog.csw.parsers;
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
public class CSWConstants {
	public static final String VERSION = "version";
			
	//Capabilities 0.9.0
	public static final String VERSION_0_9_0 = "0.9.0";
	public static final String CONSTRAINT_VERSION_0_9_0 = "1.0.20";
	
	//Capabilities 2.0.0
	public static final String VERSION_2_0_0 = "2.0.0";
	public static final String CONSTRAINT_VERSION_2_0_0 = "1.0.0";
	
	//Capabilities 2.0.1
	public static final String VERSION_2_0_1 = "2.0.1";
	public static final String CONSTRAINT_VERSION_2_0_1 = "1.1.0";
	public static final String SERVICE_IDENTIFICATION= "ServiceIdentification";
	public static final String SERVICE_PROVIDER = "ServiceProvider";
	public static final String OPERATIONS_METADATA= "OperationsMetadata";
	public static final String SERVICE_TYPE_VERSION = "ServiceTypeVersion";
	
	//Service identification
	public static final String TITLE = "Title";	
	public static final String ABSTRACT = "Abstract";
	
	//Common tags
	public static final String OPERATION = "Operation";
	public static final String PARAMETER = "Parameter"; 
	public static final String PARAMETER_VALUE = "Value";
	public static final String PARAMETER_NAME = "name";
	public static final String DEFAULTVALUE = "DefaultValue";
	public static final String TYPENAME = "typeName";
	public static final String TYPENAMES = "typeNames";
	public static final String OUTPUTFORMAT = "outputFormat";
	public static final String OUTPUTSCHEMA = "outputSchema";
	public static final String RESULTTYPE = "resultType";
	public static final String CONSTRAINTLANGUAGE = "CONSTRAINTLANGUAGE";
	public static final String FILTER = "FILTER";
	public static final String TEXT_XML = "text/xml";
	public static final String APPLICATION_XML = "application/xml";
	public static final String ELEMENTSETNAME = "ElementSetName";
	public static final String ELEMENTNAME = "ElementName";
	public static final String FULL = "full";
	public static final String CONSTRAINT = "Constraint";
	
	//Protocols
	public static final String DCP = "DCP";
	public static final String HTTP = "HTTP";
	public static final String GET = "Get";
	public static final String POST = "Post";
	
	//Operations
	public static final String OPERATION_GETCAPABILITIES = "GetCapabilities";
	//Some server only support the getCapabilities request (started with lower case)
	public static final String OPERATION_GETCAPABILITIESToLOWER = "getCapabilities";
	public static final String OPERATION_DESCRIBERECORD = "DescribeRecord";
	public static final String OPERATION_GETDOMAIN = "GetDomain";
	public static final String OPERATION_GETRECORDS = "GetRecords";
	public static final String OPERATION_GETRECORDSBYID = "GetRecordsById";
	public static final String OPERATION_HARVEST = "Harvest";	
	public static final String OPERATION_TRANSACTION = "Transaction";
	
	public static final String CSW_NAMESPACE = "csw";
	public static final String CSW_NAMESPACE_URI = "http://www.opengis.net/cat/csw";
	public static final String OGC_NAMESPACE = "ogc";
	public static final String OGC_NAMESPACE_URI = "http://www.opengis.net/ogc";
	public static final String GML_NAMESPACE = "gml";
	public static final String GML_NAMESPACE_URI = "http://www.opengis.net/gml";
	public static final String CSW_GET_RECORDS = "GetRecords";
	public static final String CSW_SERVICE = "service";
	public static final String CSW_VERSION = "version";
	public static final String CSW_START_POSITION = "startPosition";
	public static final String CSW_MAX_RECORDS = "maxRecords";
	public static final String CSW_OUTPUTFORMAT = "outputFormat";
	public static final String CSW_OUTPUTSCHEMA = "outputSchema";
	public static final String QUERY = "Query";
	public static final String SEARCH_RESULTS = "SearchResults";
	public static final String XLINK_HREF = "xlink:href";
	
	//Exceptions
	public static final String OWSEXCEPTION = "OWSException";
	public static final String OWSEXCEPTIONCODE = "code";
	public static final String OWSEXCEPTIONSUBCODE = "subcode";
	public static final String OWSEXCEPTIONREASON = "reason";
	
	//ebRIM
	public static final String REQUEST = "request";
	public static final String EXTRISIC_CONTENT = "getExtrinsicContent";
	public static final String ID = "id";
	public static final String HOME = "home";
	
	//Describe Record
	public static final String SCHEMA_COMPONENT = "SchemaComponent";
	public static final String SCHEMA = "schema";
	
	//Error codes
	public static final String SERVICE_EXPECTION_REPORT = "ServiceExceptionReport";
}
