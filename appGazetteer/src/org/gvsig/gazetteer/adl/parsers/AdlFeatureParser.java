
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
package org.gvsig.gazetteer.adl.parsers;
import java.awt.geom.Point2D;

import org.gvsig.catalog.metadataxml.XMLNode;
import org.gvsig.catalog.metadataxml.XMLTree;
import org.gvsig.gazetteer.querys.Feature;


/**
 * This class is used to parse features returned by the ADL
 * query-request operation.
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class AdlFeatureParser {

/**
 * @return 
 * @param node 
 */
    public static Feature[] parse(XMLNode node) {        
        XMLNode[] nodeFeatures = XMLTree.searchMultipleNode(node,"query-response->standard-reports->gazetteer-standard-report");
        Feature[] features = new Feature[nodeFeatures.length];
        
        for (int i=0 ; i<nodeFeatures.length ; i++){
            String id = XMLTree.searchNodeValue(nodeFeatures[i],"identifier");
            String name = XMLTree.searchNodeValue(nodeFeatures[i],"names->name");
            String description = XMLTree.searchNodeValue(nodeFeatures[i],"display-name");
            Point2D.Double point = new Point2D.Double(Double.parseDouble(XMLTree.searchNodeValue(nodeFeatures[i],"bounding-box->gml:coord->gml:X")),
                    Double.parseDouble(XMLTree.searchNodeValue(nodeFeatures[i],"bounding-box->gml:coord->gml:Y")));
            features[i] = new Feature(id,name,description,point);
        }
        
        return features;
    } 
 }
