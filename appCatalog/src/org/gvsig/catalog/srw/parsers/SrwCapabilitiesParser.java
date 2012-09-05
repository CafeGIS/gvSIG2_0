
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
package org.gvsig.catalog.srw.parsers;
import java.util.StringTokenizer;

import org.gvsig.catalog.metadataxml.XMLNode;
import org.gvsig.catalog.metadataxml.XMLTree;
import org.gvsig.catalog.srw.drivers.SRWCatalogServiceDriver;


/**
 * This class is used to parse the SRW capabilities
 * 
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class SrwCapabilitiesParser {
/**
 * 
 * 
 */
    private SRWCatalogServiceDriver driver;

/**
 * 
 * 
 * 
 * @param driver 
 */
    public  SrwCapabilitiesParser(SRWCatalogServiceDriver driver) {        
        this.driver = driver;
    } 

/**
 * 
 * 
 * 
 * @return 
 * @param node 
 */
    public boolean parse(XMLNode node) {        
        if ((node == null) || (node.getName() == null)){
            driver.setServerAnswerReady("errorNotSupportedProtocol");
            return false;
        }
        
        if (node.getName().toLowerCase().equals("serviceexceptionreport")){
            driver.setServerAnswerReady("errorServerException");
            return false;
        }
       
            String prefix = new StringTokenizer(node.getName(), ":").nextToken();
            prefix = prefix + ":";
            driver.setOutputSchema(XMLTree.searchNodeValue(node,
                    prefix + "record->" + prefix + "recordSchema"));
            driver.setOutputFormat(XMLTree.searchNodeValue(node,
                    prefix + "record->" + prefix + "recordPacking"));
            driver.setStartPosition("1");
            driver.setMaxRecords("10");
            driver.setRecordXPath("");
            driver.setResultSetTTL("0");
            String title = XMLTree.searchNodeValue(node,
                    prefix + "record->" + prefix +
            "recordData->explain->databaseInfo->title");
            
            String description = XMLTree.searchNodeValue(node,
                    prefix + "record->" + prefix +
            "recordData->explain->databaseInfo->description");
            
            if (title != null){
            	driver.setServerAnswerReady(title + "\n");
            	if (description != null){
            		driver.setServerAnswerReady(driver.getServerAnswerReady() + 
            				description + "\n");
            	}            	
            }else{
            	driver.setServerAnswerReady("");
            	if (description != null){
            		driver.setServerAnswerReady(description + "\n");
            	}
            }         	         
          
            
            
            return true;
        
    } 
 }
