
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
import java.awt.geom.Point2D;

import org.gvsig.catalog.metadataxml.XMLNode;
import org.gvsig.catalog.metadataxml.XMLTree;
import org.gvsig.gazetteer.querys.Feature;


/**
 * This class is used to parse the getFeature request
 * 
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class WfsgFeatureParser {
	private String geomType = null;
	private String namespace = null;
	private static final String POSITION_NODE = "position->gml:Point->gml:coordinates";
	private static final String GEOGRAPHICIDENTIFIER_NODE = "geographicIdentifier";
	private static final String FEATUREMEMBER = "gml:featureMember";

	/**
	 * It parses the answer
	 * 
	 * 
	 * @return Array of features
	 * @param node XML tree that contains the getFeature Answer
	 * @param featureType FEature selected in the thesaurus list
	 * @param attribute Attribute to do the search
	 */
	public Feature[] parse(XMLNode node) {        
		XMLNode[] nodeFeatures = XMLTree.searchMultipleNode(node,FEATUREMEMBER);
		namespace = "";       

		if ((nodeFeatures != null) && (nodeFeatures.length > 0)){
			if (nodeFeatures[0].getSubnodes().length > 0){
				String nodeName = nodeFeatures[0].getSubnodes()[0].getName();
				if (nodeName.split(":").length == 2){
					namespace = nodeName.split(":")[0] + ":";
				}  
				return parseWFS(nodeFeatures,nodeName);
			}        	
		}       
		return null;
	} 

	/**
	 * It parses the a WFS answer
	 * 
	 * 
	 * @return Array of features
	 * @param nodeFeatures XML tree that contains the Features
	 * @param featureType FEature selected in the thesaurus list
	 * @param attribute Attribute to do the search
	 * @param geomField Field that contains the geometry
	 */
	public Feature[] parseWFS(XMLNode[] nodeFeatures, String featureType) {        
		Feature[] features = new Feature[nodeFeatures.length];


		for (int i=0 ; i<nodeFeatures.length ; i++){
			XMLNode nodeName = XMLTree.searchNode(nodeFeatures[i],featureType + "->" + GEOGRAPHICIDENTIFIER_NODE);
			String name = "";  

			if (nodeName != null){
				if ((nodeName.getText() != null) && (!(nodeName.getText().equals("")))){
					name = nodeName.getText();
				} else if ((nodeName.getCdata() != null) && (!(nodeName.getCdata().equals("")))){
					name = nodeName.getCdata();                   
				}       
			}

			String id = XMLTree.searchNodeAtribute(nodeFeatures[i],featureType,"fid");
			String description = name;
			Point2D point = getCoordinates(XMLTree.searchNodeValue(nodeFeatures[i],
					featureType + "->" + POSITION_NODE));

			features[i] = new Feature(id,name,description,point);
		}
		return features;
	} 

	/**
	 * It returns a pair of coordinates of the Feature
	 * @return 
	 * @param sCoordinates String that contains the coordinates
	 */
	private Point2D getCoordinates(String sCoordinates) {        
		if (sCoordinates != null){
			double x = Double.parseDouble(sCoordinates.split(",")[0]);
			double y = Double.parseDouble(sCoordinates.split(",")[1]);
			return new Point2D.Double(x,y);
		}return null;    
	} 



}
