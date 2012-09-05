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
package org.gvsig.catalog.loaders;

import java.sql.SQLException;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.gvsig.catalog.schemas.Resource;
import org.gvsig.catalog.utils.Strings;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.i18n.Messages;
import org.gvsig.tools.extensionpoint.ExtensionPoint;

import com.iver.utiles.extensionPointsOld.ExtensionPointsSingleton;


/**
 * This class is used to load a POSTGIS resource in gvSIG
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */

public class PostgisLayerLoader extends GvSigLayerLoader{
	
	
	
	public PostgisLayerLoader(Resource resource) {
		super(resource);
	}

	/**
     * This function adds a Postgis Layer to gvSIG 
     * @param jdbcUrl
   	 * JDBC url connection
   	 * @param table
   	 * Table to load
	 * @throws LayerLoaderException 
   	 */	
   	public void loadLayer() throws LayerLoaderException {
       	FLayer flayer;
       	String jdbcUrl = getResource().getLinkage();
       	String table = getResource().getName();
        flayer = createPostgisLayer(jdbcUrl, table);
        addLayerToView(flayer);
  	}
    	
    /**
   	 * It returns a Postgis Layer
   	 * @param jdbcUrl
   	 * JDBC url connection
   	 * @param table
   	 * Table to load
   	 * @return
     * @throws SQLException
     * @throws DriverLoadException
     * @throws ClassNotFoundException
     * @throws LayerLoaderException 
   	 */
    private FLayer createPostgisLayer(String jdbcUrl, String table) throws  LayerLoaderException  {
    	ExtensionPoint extensionPoint = (ExtensionPoint)ExtensionPointsSingleton.getInstance().get("CatalogLayers");
		Map args = createParamsMap(jdbcUrl,table);
		try {
			return (FLayer)extensionPoint.create("POSTGIS", args  );
		} catch(Exception e) {
			throw new LayerLoaderException(getErrorMessage(),getWindowMessage());
		}
   }
    
    /**
     * Creates a Map with all the params
   	 * @param jdbcUrl
   	 * JDBC url connection
   	 * @param table
   	 * Table to load
     * @return
     * Map
     */
    private Map createParamsMap(String jdbcUrl, String table){
    	StringTokenizer sti = new StringTokenizer(jdbcUrl,"?");
        String dbURL = sti.nextToken();
        String p = sti.nextToken();
        TreeMap map = separateParams(p);
        String user = (String) map.get((String) "USER");
		String pwd = (String) map.get((String) "PASSWORD");
        map = Strings.separateParams(table);
        map.put("USER",user);
        map.put("PASSWORD",pwd);        
		map.put("WHERECLAUSE","");
		map.put("DBURL",dbURL);
		return map;	
    }
    
    private TreeMap separateParams(String pairValues){
        TreeMap map = new TreeMap(); 
		String[] params = pairValues.split("&");
		for (int i = 0; i < params.length; i++) {
			String[] nameValue = params[i].split("=");
			map.put(nameValue[0].toUpperCase(), nameValue[1]);
		}
		return map;
    }

    /*
     *  (non-Javadoc)
     * @see es.gva.cit.gvsig.catalogClient.loaders.LayerLoader#getErrorMessage()
     */
    protected String getErrorMessage() {
		return Messages.getText("postgisError") + ".\n" +
				Messages.getText("server") + ": " + 
				getResource().getLinkage() + "\n" +
				Messages.getText("parameters") + ": " +
				getResource().getName();
	}

    /*
     *  (non-Javadoc)
     * @see es.gva.cit.gvsig.catalogClient.loaders.LayerLoader#getWindowMessage()
     */
	protected String getWindowMessage() {
		return Messages.getText("postgisLoad");
	}

	
}
