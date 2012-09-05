
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
package org.gvsig.gazetteer.wfs.drivers;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import org.gvsig.catalog.drivers.DiscoveryServiceCapabilities;
import org.gvsig.catalog.utils.CatalogConstants;
import org.gvsig.gazetteer.drivers.AbstractGazetteerServiceDriver;
import org.gvsig.gazetteer.drivers.GazetteerCapabilities;
import org.gvsig.gazetteer.querys.Feature;
import org.gvsig.gazetteer.querys.FeatureType;
import org.gvsig.gazetteer.querys.FeatureTypeAttribute;
import org.gvsig.gazetteer.querys.GazetteerQuery;
import org.gvsig.gazetteer.utils.GazetteerFormatter;
import org.gvsig.gpe.exceptions.ParserCreationException;
import org.gvsig.gpe.gml.exceptions.GMLException;
import org.gvsig.gpe.gml.parser.GPEGmlSFP0Parser;
import org.gvsig.gpe.parser.GPEParser;
import org.gvsig.i18n.Messages;
import org.gvsig.remoteClient.wfs.WFSClient;
import org.gvsig.remoteClient.wfs.WFSFeature;
import org.gvsig.remoteClient.wfs.WFSStatus;
import org.gvsig.remoteClient.wfs.filters.FilterEncoding;
import org.gvsig.remoteClient.wfs.schema.XMLElement;
import org.gvsig.remoteClient.wfs.schema.type.XMLComplexType;

import com.iver.utiles.swing.jcomboServer.ServerData;


/**
 * Driver for the WFS protocol
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class WFSServiceDriver extends AbstractGazetteerServiceDriver {
	protected WFSClient client = null;
	protected WFSStatus status = null;

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalogClient.drivers.IDiscoveryServiceDriver#getCapabilities(java.net.URI)
	 */
	public DiscoveryServiceCapabilities getCapabilities(URI uri) {
		GazetteerCapabilities capabilities = new GazetteerCapabilities();
		String sURL = null;
		try {
			sURL = uri.toURL().toString();
		} catch (MalformedURLException e) {
			setServerAnswerReady("errorServerNotFound");
			return null;
		}
		status = new WFSStatus(sURL);
		try {
			client = new WFSClient(sURL);
			client.getCapabilities(status, true, null);
		} catch (Exception e) {
			capabilities.setServerMessage(Messages.getText("errorNotParsedReply"));
			capabilities.setAvailable(false);
			return capabilities;
		}
		setServerAnswerReady(client.getServiceInformation().name);
		Hashtable features = client.getFeatures();
		setFeatureTypes(convertFeatureNames(features));
		capabilities.setServerMessage(client.getServiceInformation().name +
				client.getServiceInformation().abstr);
		capabilities.setFeatureTypes(getFeatureTypes());
		return capabilities;
	}

	/**
	 * Convert the features from the remote services format to
	 * the gazetteer format
	 * @param features
	 * @return
	 */
	private FeatureType[] convertFeatureNames(Hashtable features) {
		Iterator it = features.keySet().iterator();
		FeatureType[] featureTypes = new FeatureType[features.size()];
		int i = 0;
		while (it.hasNext()){
			String featureName = (String)it.next();
			WFSFeature feature = (WFSFeature)features.get(featureName);
			featureTypes[i] = new FeatureType(featureName);
			featureTypes[i].setTitle(feature.getTitle());
			i++;
		}
		return featureTypes;
	}

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.gazetteer.drivers.IGazetteerServiceDriver#describeFeatureType(java.net.URI, java.lang.String)
	 */
	public FeatureTypeAttribute[] describeFeatureType(URI uri, String feature) {
		status.setFeatureName(feature);
		try{
			client.describeFeatureType(status, false, null);
		}catch (Exception e){
			//Impossible to retrieve the attributes
			return new FeatureTypeAttribute[0];
		}
		WFSFeature wfsFeature = (WFSFeature)client.getFeatures().get(feature);
		if ((wfsFeature.getSrs() != null) && (wfsFeature.getSrs().size() > 0)){
			this.setProjection((String)wfsFeature.getSrs().get(0));
		}
		return covertFeatuteAttributes(wfsFeature);
	}

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.gazetteer.drivers.AsbtractGazetteerServiceDriver#isDescribeFeatureTypeNeeded()
	 */
	public boolean isDescribeFeatureTypeNeeded(){
		return true;
	}

	/**
	 * Convert the feature attributes
	 * @param feature
	 * a Remote clients feature
	 * @return
	 * A list of attributes
	 */
	private FeatureTypeAttribute[] covertFeatuteAttributes(WFSFeature feature) {
		XMLElement element = (XMLElement)feature.getFields().get(0);
		XMLComplexType type = (XMLComplexType)element.getEntityType();
		Vector fields = type.getAttributes();
		FeatureTypeAttribute[] attributes = new FeatureTypeAttribute[fields.size()];
		for (int i=0 ; i<fields.size(); i++){
			XMLElement child = (XMLElement)fields.get(i);
			attributes[i] = new FeatureTypeAttribute(child.getName(),0,false,child.getEntityType().getName());
		}
		return attributes;
	}

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.gazetteer.drivers.IGazetteerServiceDriver#getFeature(java.net.URI, es.gva.cit.gazetteer.querys.Query)
	 */
	public Feature[] getFeature(URI uri, GazetteerQuery query) throws GMLException {
		//Set the filter
		String sQuery = getFilterSQL(query);
		if (sQuery != null){
			status.setFilterQuery(sQuery);
		}
		status.setFields(new String[0]);
		try{
			File file = client.getFeature(status, false, null);
			return parseOutputFile(file,query.getFieldAttribute());
		}catch(Exception e){
			e.printStackTrace();
			return new Feature[0];
		}
	}

	/**
	 * Parses the GML file
	 * @param file
	 * @return
	 * @throws GPEParserCreationException 
	 * @throws URISyntaxException 
	 */
	protected Feature[] parseOutputFile(File file, String fieldAttribute) throws URISyntaxException, ParserCreationException {
		URI uri = file.toURI();
		GPEParser parser = new GPEGmlSFP0Parser();
		WFSGPEErrorHandler errorHandler = new WFSGPEErrorHandler();
		WFSGPEContentHandler contentHandler = new WFSGPEContentHandler(fieldAttribute);
		parser.parse(contentHandler, errorHandler, uri);
		ArrayList features = contentHandler.getFeatures();
		Feature[] auxFeatures = new Feature[features.size()];
		for (int i=0 ; i<features.size() ; i++){
			auxFeatures[i] = (Feature)features.get(i);
		}
		return auxFeatures;
	}

	/**
	 * Creates a SQL filter to do the search
	 * @return
	 * A standard SQL query
	 */
	protected String getFilterSQL(GazetteerQuery query){
		StringBuffer buffer = new StringBuffer();
		if ((query.getName() == null) || (query.getName().equals(""))){
			return null;
		}
		if (query.getNameFilter().equals(CatalogConstants.EXACT_WORDS)){
			buffer.append("(" + query.getFieldAttribute() + " = " + query.getName() + ")");
		}else{
			String conector = null;
			if (query.getNameFilter().equals(CatalogConstants.ALL_WORDS)){
				conector = "AND";
			}else if (query.getNameFilter().equals(CatalogConstants.ANY_WORD)){
				conector = "OR";
			}
			String[] words = query.getName().split(" ");
			for (int i=0 ; i<words.length ; i++){
				buffer.append("(" + query.getFieldAttribute() + " = " + words[i] + ")");
				if (i  < words.length - 1){
					buffer.append(" " + conector + " ");
				}
			}
		}
		FilterEncoding fe = GazetteerFormatter.createFilter();
		fe.setQuery(buffer.toString());
		return fe.toString();
	}

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.gazetteer.drivers.IGazetteerServiceDriver#isProtocolSupported(java.net.URI)
	 */
	public boolean isProtocolSupported(URI uri) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.gazetteer.drivers.IGazetteerServiceDriver#getDefaultPort()
	 */
	public int getDefaultPort() {
		return 80;
	}

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.gazetteer.drivers.IGazetteerServiceDriver#getDefaultSchema()
	 */
	public String getDefaultSchema() {
		return "http";
	}

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.gazetteer.drivers.IGazetteerServiceDriver#getServiceName()
	 */
	public String getServiceName() {
		return ServerData.SERVER_SUBTYPE_GAZETTEER_WFS;
	}
}
