
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
package org.gvsig.gazetteer.adl.protocols;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;

import org.apache.commons.httpclient.NameValuePair;
import org.gvsig.catalog.metadataxml.XMLNode;
import org.gvsig.catalog.metadataxml.XMLTree;
import org.gvsig.catalog.protocols.HTTPGetProtocol;
import org.gvsig.gazetteer.querys.FeatureType;


/**
 * This class implemets an ADL thesaurus client
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 * @see http://middleware.alexandria.ucsb.edu:8080/thes/FTT/index.html
 */
public class ADLThesaurus {
	private URL url;
	private FeatureType[] features;

	public  ADLThesaurus(URL url) {        
		setUrl(url);
		getNarrower();
	} 

	/**
	 * It implements the getNarrower ADL thesaurus operation
	 * 
	 */
	private void getNarrower() {        
		URL urlNarrower;
		try {
			urlNarrower = new URL ("http", getUrl().getHost(), getUrl().getPort(),getUrl().getFile() + "/get-narrower");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		Collection nodes = new HTTPGetProtocol().doQuery(urlNarrower, getNarrowerParams(), 0);
		parseGetNarrowAnswer((XMLNode)nodes.toArray()[0]);       
	} 

	/**
	 * It returns the name-value pair for the getNarrower opertaion
	 * @return Name-value pair
	 */
	private NameValuePair[] getNarrowerParams() {        
		String message = "";
		System.out.println(message);
		NameValuePair nvp1 = new NameValuePair("starting-term", "");
		NameValuePair nvp2 = new NameValuePair("max-levels", "-1");
		NameValuePair nvp3 = new NameValuePair("format", "term");

		return new NameValuePair[] { nvp1, nvp2, nvp3 };
	} 

	/**
	 * It parses the getNarrow answer into a Feature arrray
	 * @param node 
	 */
	private void parseGetNarrowAnswer(XMLNode node) {        
		XMLNode[] rootNodes = XMLTree.searchMultipleNode(node,"hierarchy->node->node");
		FeatureType[] features = new FeatureType[rootNodes.length];
		for (int i=0 ; i<rootNodes.length ; i++){
			FeatureType thesaurus = new FeatureType(XMLTree.searchNodeValue(rootNodes[i],"term"));
			thesaurus.setFeatures(parseRecursiveFeatures(rootNodes[i],thesaurus));
			thesaurus.setTitle(thesaurus.getName());
			features[i] = (thesaurus);
		}
		setFeatures(features);
	} 

	/**
	 * It is used to parse a feature using recursivity.
	 * @return 
	 * @param node Feature tree
	 * @param thesaurus Feature to add the child Features
	 */
	private FeatureType[] parseRecursiveFeatures(XMLNode node, FeatureType thesaurus) {        
		XMLNode[] rootNodes = XMLTree.searchMultipleNode(node,"node");

		if ((rootNodes == null) || (rootNodes.length == 0)){
			return null;
		}

		FeatureType[] features = new FeatureType[rootNodes.length];
		for (int i=0 ; i<rootNodes.length ; i++){
			FeatureType f = new FeatureType(XMLTree.searchNodeValue(rootNodes[i],"term"));
			f.setTitle(f.getName());
			f.setFeatures(parseRecursiveFeatures(rootNodes[i],f));
			features[i] = f;
		}        
		return features;
	} 

	/**
	 * @return Returns the url.
	 */
	public URL getUrl() {        
		return url;
	} 

	/**
	 * @param url The url to set.
	 */
	public void setUrl(URL url) {        
		this.url = url;
	} 

	/**
	 * @return Returns the features.
	 */
	public FeatureType[] getFeatures() {        
		return features;
	} 

	/**
	 * @param features The features to set.
	 */
	public void setFeatures(FeatureType[] features) {        
		this.features = features;
	} 
}
