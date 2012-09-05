
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
package com.iver.utiles.swing.jcomboServer;
import javax.swing.JComboBox;

/**
 * This class is a user interface component that looks equal to
 * a JComboBox component, but it contains ServerData objects and 
 * it is able to show them in last access order.It has methods to 
 * add ServerData objects and to retrieve them.
 * 
 * @see java.swing.JComboBox
 * @see es.gva.cit.catalogClient.utils.comboserver.ServerData
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class JComboServer extends JComboBox {

/**
 * Just a simple constructor
 * 
 */
    public  JComboServer() {        
        super();
    } 

/**
 * A constructor
 * 
 * 
 * @param servers Array with all the rervers to show
 */
    public  JComboServer(ServerData[] servers) {        
        super(setLastAccessOrder(servers));
    } 

/**
 * This method returns the selected server
 * 
 * 
 * @return A Server
 */
    public ServerData getSelectedServer() {        
        try {
            return (ServerData) getSelectedItem();
        }catch(ClassCastException e){
            return new ServerData((String)getSelectedItem(),"","");
        }
    } 

/**
 * This method adds a new Server in order
 * 
 * 
 * @param server New Server
 */
    public void addServer(ServerData server) {        
        ServerData[] servers = getAllServers();
        ServerData[] newServers = new ServerData[servers.length + 1];
        System.arraycopy(servers, 0, newServers, 0, servers.length);
        newServers[servers.length] = server;
        newServers = setLastAccessOrder(newServers);
        setServerList(newServers);
    } 

/**
 * This method returns an array with all the servers that the
 * combo contains
 * 
 * 
 * @return An array of servers
 */
    public ServerData[] getAllServers() {        
        ServerData[] servers = new ServerData[getItemCount()];
        for (int i=0 ; i<getItemCount() ; i++){
            servers[i] = (ServerData) this.getItemAt(i);
        }
        return servers;
    } 

/**
 * It adds a server list
 * 
 * 
 * @param servers Array with servers
 */
    public void setServerList(ServerData[] servers) {        
        removeAllItems();
        servers = setLastAccessOrder(servers);
        for (int i=0 ; i<servers.length ; i++){
           try{
        	   if (!(servers[i].getServerAddress().equals(""))){
        		   addItem(servers[i]);
        	   }
           }catch(java.lang.NullPointerException e){
        	   //Server is null
           }
        }
    } 

/**
 * This method order all the servers in the combo
 * 
 */
    public void setServersOrder() {        
        ServerData[] servers = getAllServers();
        servers = setLastAccessOrder(servers);
        setServerList(servers);
    } 

/**
 * This static method ordered an array of servers by last access
 * 
 * 
 * @return A new array
 * @param servers Array of servers
 */
    private static ServerData[] setLastAccessOrder(ServerData[] servers) {        
    	ServerData[] orderedServerData = new ServerData[servers.length];
                
    	for (int i=0 ; i<servers.length ; i++){
    	    int pos = getServerPosition(servers, i);
    	    orderedServerData[pos] = servers[i];
        }
    	return orderedServerData;
    } 

/**
 * This static function return the ordered position of a server. The algorithm to
 * order can be changed.
 * 
 * 
 * @return Number with the new position
 * @param servers Array that contains all the servers
 * @param serverPos Server position
 */
    private static int getServerPosition(ServerData[] servers, int serverPos) {        
        int pos=0;
        for (int i=0 ; i<servers.length ; i++){
            if (!(servers[serverPos].getServerAddress().equals(servers[i].getServerAddress()))){
                if (servers[serverPos].getLastAccess().before(servers[i].getLastAccess())){
                    pos++;
                }
                if (servers[serverPos].getLastAccess().equals(servers[i].getLastAccess())){
                    if (serverPos < i){
                        pos++;
                    }
                }
                
            }
        }
        return pos;
    } 
 }
