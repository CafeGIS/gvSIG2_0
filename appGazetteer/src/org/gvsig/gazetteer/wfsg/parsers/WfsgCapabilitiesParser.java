
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
package org.gvsig.gazetteer.wfsg.parsers;
import java.util.Vector;

import org.gvsig.catalog.metadataxml.XMLNode;
import org.gvsig.catalog.metadataxml.XMLTree;
import org.gvsig.catalog.querys.Coordinates;
import org.gvsig.gazetteer.drivers.GazetteerCapabilitiesParser;
import org.gvsig.gazetteer.drivers.IGazetteerServiceDriver;
import org.gvsig.gazetteer.querys.FeatureType;
import org.gvsig.i18n.Messages;


/**
 * This class parses a WFs-G getCapabilities file
 * 
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class WfsgCapabilitiesParser extends GazetteerCapabilitiesParser{
	private XMLNode rootNode = null;
	
	public WfsgCapabilitiesParser(IGazetteerServiceDriver driver) {
		super(driver);		
	}

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.gazetteer.drivers.GazeteerCapabilitiesParser#parseCapabilities(es.gva.cit.catalog.metadataxml.XMLNode)
	 */
	protected void parseCapabilities(XMLNode node) {
		if ((node == null) || !(node.getName().equals("WFS_Capabilities"))){
			capabilities.setServerMessage(Messages.getText("errorNotSupportedProtocol"));
			capabilities.setAvailable(false);
			return;
		}

		setRootNode(node);

		driver.setServerAnswerReady(XMLTree.searchNodeValue(node,
		"Service->Title") + "\n" +
		XMLTree.searchNodeValue(node,"Service->Abstract"));
		FeatureType[] features = parseFeatures();
		parseProjection();
		capabilities.setFeatureTypes(features);
	} 

	/**
	 * Parses the server projection
	 */
	private void parseProjection(){
		driver.setProjection(XMLTree.searchNodeValue(getRootNode(),"FeatureTypeList->FeatureType->SRS"));
	}


	/**
	 * It parses the Feature Types
	 * 
	 */
	private FeatureType[] parseFeatures() {        
		XMLNode[] nodes = XMLTree.searchMultipleNode(getRootNode(),"FeatureTypeList->FeatureType");
		FeatureType[] features = new FeatureType[nodes.length];
		for (int i=0 ; i<features.length ; i++){
			features[i] = (parseFeature(nodes[i]));
		}
		return features;
	} 

	/**
	 * It parses a Feature Node.
	 * @return 
	 * @param featureNode 
	 */
	private FeatureType parseFeature(XMLNode featureNode) {        
		FeatureType f = new FeatureType();
		f.setName(XMLTree.searchNodeValue(featureNode,"Name"));
		f.setTitle(XMLTree.searchNodeValue(featureNode,"Title"));
		if (f.getTitle() == null)
			f.setTitle(f.getName());
		f.setAbstract(XMLTree.searchNodeValue(featureNode,"Abstract"));
		f.setKeywords(XMLTree.searchNodeValue(featureNode,"Keywords"));
		f.setSrs(XMLTree.searchNodeValue(featureNode,"SRS"));
		f.setCoordinates(new Coordinates(XMLTree.searchNodeAtribute(featureNode,"LatLongBoundingBox","minx"),
				XMLTree.searchNodeAtribute(featureNode,"LatLongBoundingBox","miny"),
				XMLTree.searchNodeAtribute(featureNode,"LatLongBoundingBox","maxx"),
				XMLTree.searchNodeAtribute(featureNode,"LatLongBoundingBox","maxy")));

		return f;
	} 

	/**
	 * This function parses the protocols of an operation
	 * @return 
	 * @param operation Operation to find the supported protocols
	 */
	private String[] getOperations(String operation) {        
		XMLNode[] protocols = XMLTree.searchMultipleNode(getRootNode(),"Capability->Request->" + operation + "->DCPType");
		Vector vProtocols = new Vector();

		for (int i=0 ; i<protocols.length ; i++){
			if (XMLTree.searchNode(protocols[i],"HTTP->Get") != null){
				vProtocols.add("GET");
			}
			if (XMLTree.searchNode(protocols[i],"HTTP->Post") != null){
				vProtocols.add("POST");
			}

		}  

		String[] sProtocols = new String[vProtocols.size()];
		for (int i=0 ; i<vProtocols.size() ; i++){
			sProtocols[i] = (String) vProtocols.get(i);
		}        

		return sProtocols;
	} 

	/**
	 * @return Returns the rootNode.
	 */
	private XMLNode getRootNode() {        
		return rootNode;
	} 

	/**
	 * @param rootNode The rootNode to set.
	 */
	private void setRootNode(XMLNode rootNode) {        
		this.rootNode = rootNode;
	}
}
