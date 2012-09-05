
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
package org.gvsig.gazetteer.ui.serverconnect;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.TreeMap;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.gvsig.catalog.drivers.DiscoveryServiceCapabilities;
import org.gvsig.catalog.utils.CatalogConstants;
import org.gvsig.gazetteer.GazetteerClient;
import org.gvsig.gazetteer.GazetteerLocator;
import org.gvsig.gazetteer.GazetteerManager;
import org.gvsig.gazetteer.drivers.IGazetteerServiceDriver;
import org.gvsig.gazetteer.ui.search.SearchDialog;
import org.gvsig.i18n.Messages;

import com.iver.utiles.swing.jcomboServer.ServerData;


/**
 * It implements the Jpanel thas has the events control
 * for the connection panel. It is composed by some buttons
 * and a panel.
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class ServerConnectDialogPanel extends JPanel implements ActionListener {
	private static final GazetteerManager gazetteerManager = GazetteerLocator.getGazetteerManager();
	private static final long serialVersionUID = 7022169996238047820L;
	private static TreeMap serverList = new TreeMap();
	private ServerConnectPanel controlsPanel = null;
	private JFrame parent = null;
	protected GazetteerClient client = null;
	protected String serversFile = "servers/GazetteerServers.txt";
	protected String currentServer = "";
	private ConnectThread connectThread = null;

	/**
	 * Constructor
	 * @param parent 
	 */
	public  ServerConnectDialogPanel(JFrame parent) {
		this.parent = parent;
		this.setLayout(new BorderLayout());
		add(getControlsPanel(),BorderLayout.CENTER);
		//Loads the servers
		loadServerList(serversFile);
		//Load the protocols
		controlsPanel.loadDrivers(
				gazetteerManager.getDrivers());
		//Load the first protocol
		controlsPanel.setProtocol(controlsPanel.getServer().getServiceSubType());
	} 

	/**
	* @return the main panel
	 */
	public ServerConnectPanel getControlsPanel() {        
		if (controlsPanel == null) {
			controlsPanel = new ServerConnectPanel();
			controlsPanel.addActionListener(this);
			controlsPanel.enableSearchButton(false);
		}
		return controlsPanel;
	} 
	
	/**
	 * It adds a server in the TreeMap Object
	 * @param server 
	 */
	protected static void addTreeMapServer(ServerData server) {        
		if (ServerConnectDialogPanel.serverList == null) {
			ServerConnectDialogPanel.serverList = new TreeMap();
		}
		serverList.put(server.getServerAddress(), server);
	} 

	/**
	 * This method loads a server list in the combo
	 * @param sfile 
	 */
	private void loadServerList(String sfile) {        
		loadServersFromFile(sfile);
		Iterator iter = serverList.keySet().iterator();
		while (iter.hasNext()) {
			ServerData server = (ServerData) serverList.get((String) iter.next());
			controlsPanel.addServer(server);
		}		
	} 

	/**
	 * It loads a server list from a text file
	 * @param sfile 
	 * File that contains the rervers
	 */
	private void loadServersFromFile(String sfile) {        
		File file = null;
		try {
			file = new File(sfile);
			if (file.exists()) {
				BufferedReader fr = new BufferedReader(new FileReader(file));
				String s;
				while ((s = fr.readLine()) != null) {
					addTreeMapServer(new ServerData(s,"",""));
				}
			} else {
				System.out.println("No se encuentra el fichero '" +
						file.getPath() + "'");
			}
		} catch (FileNotFoundException e) {
			System.out.println("No se encuentra el fichero '" + file.getPath() +
			"'");
			//e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error de entrada salida en la lectura del fichero");
			//e.printStackTrace();
		}
	} 
	
	/*
	 * (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {        
		if (e.getActionCommand().compareTo(CatalogConstants.CONNECT_BUTTON_ACTION_COMMAND)==0) {
			connectButtonActionPerformed();
		}
		if (e.getActionCommand().compareTo(CatalogConstants.SEARCH_BUTTON_ACTION_COMMAND)==0) {
			searchButtonActionPerformed();
		}
		if (e.getActionCommand().compareTo(CatalogConstants.CLOSE_BUTTON_ACTION_COMMAND)==0) {
			closeButtonActionPerformed();
		}
		if (e.getActionCommand().compareTo(CatalogConstants.PROTOCOL_COMBO_ACTION_COMMAND)==0){
			GazetteerClient client = new GazetteerClient(controlsPanel.getServerAddress(),
					(IGazetteerServiceDriver)controlsPanel.getDriver());
			controlsPanel.setServerTextEnabled(client.getOneServer()==null);
			if (client.getOneServer() != null){
				ServerData[] servers = controlsPanel.getAllServers();
				boolean found = false;
				for (int i=0 ; i<servers.length ; i++){
					if (client.getOneServer().getServerAddress().equals(servers[i].getServerAddress())){
						found = true;
					}
				}
				if (!found){
					controlsPanel.addServer(client.getOneServer());
				}
			}
		}
		if (e.getActionCommand().compareTo(CatalogConstants.SERVER_COMBO_ACTION_COMMAND)==0) {
			controlsPanel.updateProtocol();
		}
	} 

	/**
	 * Action when the search button is clicked
	 */
	protected void searchButtonActionPerformed() {        
		setEnabled(false);
		new SearchDialog(client,parent);
	} 

	/**
	 * It is thrown the the connect button is clicked
	 */
	protected void connectButtonActionPerformed() {        
		controlsPanel.enableSearchButton(false);		
		//Create a new Gazetteer client
		client = new GazetteerClient(controlsPanel.getServerAddress(),
				(IGazetteerServiceDriver)controlsPanel.getDriver());
		if (connectThread != null){
			connectThread.stop();
		}
		connectThread = new ConnectThread();
		setCursor(new Cursor(Cursor.WAIT_CURSOR));
	} 

	/**
	 *  * It is thrown the the close button is clicked
	 */
	protected void closeButtonActionPerformed() {        
		parent.setVisible(false);
		System.exit(0);
	} 

	/**
	 * @return Returns the serversFile.
	 */
	public String getServersFile() {        
		return serversFile;
	} 

	/**
	 * @param serversFile The serversFile to set.
	 */
	public void setServersFile(String serversFile) {        
		this.serversFile = serversFile;
	} 

	/**
	 * @return Returns the currentServer.
	 */
	public String getCurrentServer() {        
		return currentServer;
	} 

	/**
	 * @return Returns the client.
	 */
	public GazetteerClient getCliente() {        
		return client;
	} 
	
	/**
	 * This class is used to manage the searches.
	 * It contains method to start and to stop a thread. It is
	 * necessary to create because "stop" method (for the Thread class)
	 * is deprecated.
	 * 
	 * 
	 * @author Jorge Piera Llodra (piera_jor@gva.es)
	 */
	private class ConnectThread implements Runnable {
		volatile Thread myThread = null;

		public  ConnectThread() {        
			myThread = new Thread(this);
			myThread.start();
		} 

		public void stop(){
			myThread.stop();
		}
		/*
		 * (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {        
			try {
				DiscoveryServiceCapabilities capabilities = client.getCapabilities();
				if (capabilities.isAvailable()){
					controlsPanel.enableSearchButton(true);
					currentServer = controlsPanel.getServerAddress();
					searchButtonActionPerformed();
				}	
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));   
				controlsPanel.setServerReply(capabilities.getServerMessage());				
			} catch (Exception e) {
				controlsPanel.setServerReply(Messages.getText(e.toString()));
				e.printStackTrace();
			}	
		}
	}	
}
