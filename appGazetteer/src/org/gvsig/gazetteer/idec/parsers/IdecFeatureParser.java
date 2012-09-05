
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
package org.gvsig.gazetteer.idec.parsers;
import java.awt.geom.Point2D;

import org.gvsig.catalog.metadataxml.XMLNode;
import org.gvsig.catalog.metadataxml.XMLTree;
import org.gvsig.gazetteer.querys.Feature;


/**
 * 
 * 
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class IdecFeatureParser {

/**
 * @return 
 * @param node 
 */
    public static Feature[] parse(XMLNode node) {        
    
        XMLNode[] nodeFeatures = XMLTree.searchMultipleNode(node,"SOAP-ENV:Body->ns1:getCoordenadesUTMResponse->return->item");
        Feature[] features = new Feature[nodeFeatures.length];
        
        //New Version
        if (features.length == 0){
        	nodeFeatures = XMLTree.searchMultipleNode(node,"soapenv:Body->multiRef");
        	features = new Feature[nodeFeatures.length];
        }
        
        for (int i=0 ; i<nodeFeatures.length ; i++){
            String id = XMLTree.searchNodeValue(nodeFeatures[i],"NOM");
            String name = id;
            String description = id;
            Point2D.Double point = new Point2D.Double(Double.parseDouble(XMLTree.searchNodeValue(nodeFeatures[i],"X")),
                Double.parseDouble(XMLTree.searchNodeValue(nodeFeatures[i],"Y")));
            features[i] = new Feature(id,name,description,point);
        }
        
    
    return features;
    } 
 }
