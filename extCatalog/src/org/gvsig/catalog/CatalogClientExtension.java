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
package org.gvsig.catalog;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.gvsig.catalog.gui.ConnectDialog;
import org.gvsig.catalog.loaders.ARCIMSLayerLoader;
import org.gvsig.catalog.loaders.LinkLoader;
import org.gvsig.catalog.loaders.PostgisLayerLoader;
import org.gvsig.catalog.loaders.WCSLayerLoader;
import org.gvsig.catalog.loaders.WFSLayerLoader;
import org.gvsig.catalog.loaders.WMSLayerLoader;
import org.gvsig.catalog.schemas.Resource;
import org.gvsig.fmap.dal.store.catalog.CatalogDataStoreLibrary;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.extensionpoint.ExtensionPointManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.andami.PluginServices;
import com.iver.andami.persistence.serverData.ServerDataPersistence;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;
import com.iver.utiles.swing.jcomboServer.ServerData;



/**
 * DOCUMENT ME!
 *
 * @author Luis W. Sevilla
 */
public class CatalogClientExtension extends Extension {
	private static final Logger logger = LoggerFactory.getLogger(CatalogClientExtension.class);
	public static final String CATALOGLAYERS = "CatalogLayers";
	private CatalogLibrary catalogLibrary = null;

	/* (non-Javadoc)
	 * @see com.iver.andami.plugins.Extension#inicializar()
	 */
	public void initialize() {
		catalogLibrary = new CatalogDataStoreLibrary();
		catalogLibrary.initialize();

		ExtensionPointManager extensionPointManager = ToolsLocator
		.getExtensionPointManager();
		extensionPointManager.add(CATALOGLAYERS, "List of possible layers that can be loaded from a catalog");
		registerIcons();
	}

	private void registerIcons(){
		PluginServices.getIconTheme().registerDefault(
				"catalog-search",
				this.getClass().getClassLoader().getResource("images/SearchButton.png")
		);
		PluginServices.getIconTheme().registerDefault(
				"catalog-up",
				this.getClass().getClassLoader().getResource("images/up.png")
		);
		PluginServices.getIconTheme().registerDefault(
				"catalog-down",
				this.getClass().getClassLoader().getResource("images/down.png")
		);
		PluginServices.getIconTheme().registerDefault(
				"catalog-record",
				this.getClass().getClassLoader().getResource("images/IcoRecord.png")
		);
		PluginServices.getIconTheme().registerDefault(
				"catalog-no-image",
				this.getClass().getClassLoader().getResource("images/no_image.png")
		);
		PluginServices.getIconTheme().registerDefault(
				"catalog-properties",
				this.getClass().getClassLoader().getResource("images/serverProperties.png")
		);
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.andami.plugins.Extension#postInitialize()
	 */
	public void postInitialize(){
		catalogLibrary.postInitialize();   

		//Initialazing the layer loaders
		CatalogManager catalogManager = CatalogLocator.getCatalogManager();
		catalogManager.addLayerLoader(Resource.WMS, WMSLayerLoader.class);
		catalogManager.addLayerLoader(Resource.POSTGIS, PostgisLayerLoader.class);
		catalogManager.addLayerLoader(Resource.WCS, WCSLayerLoader.class);
		catalogManager.addLayerLoader(Resource.WEBSITE, LinkLoader.class);
		catalogManager.addLayerLoader(Resource.DOWNLOAD, LinkLoader.class);
		catalogManager.addLayerLoader(Resource.WFS, WFSLayerLoader.class);
		catalogManager.addLayerLoader(Resource.ARCIMS_IMAGE, ARCIMSLayerLoader.class);
		catalogManager.addLayerLoader(Resource.ARCIMS_VECTORIAL, ARCIMSLayerLoader.class);
	}

	/* (non-Javadoc)
	 * @see com.iver.andami.plugins.Extension#execute(java.lang.String)
	 */
	public void execute(String actionCommand) {
		actionConnectDialogStart();
	}

	/**
	 * DOCUMENT ME!
	 */
	private void actionConnectDialogStart() {
		System.out.println("Botón Cliente de metadatos pulsado");
		restoreServerList();

		ConnectDialog connectDialog = new ConnectDialog();
		PluginServices.getMDIManager().addWindow(connectDialog);
	}

	/**
	 * It restores a server list. If this list does't exist it create  a server
	 * list by default.
	 */
	private void restoreServerList() {
		ServerDataPersistence persistence = new ServerDataPersistence(this,ServerData.SERVER_TYPE_CATALOG);

		ServerData[] servers = persistence.getArrayOfServerData();

		boolean found = false;
		for (int i=0 ; i<servers.length ; i++){
			if (servers[i].getServiceType().equals(ServerData.SERVER_TYPE_CATALOG)){
				found = true;
			}
		}       

		if (!found){
			if (servers.length == 0){
				servers = getDefaultServers();
			}else{
				ServerData[] newServers = new ServerData[servers.length + getDefaultServers().length ];
				System.arraycopy(servers, 0, newServers, 0, servers.length);
				System.arraycopy(getDefaultServers(), 0, newServers, servers.length, getDefaultServers().length);
				servers = newServers;
			}
			persistence.setArrayOfServerData(servers);
		}


		for (int i = 0; i < servers.length; i++) {
			if (servers[i].getServiceType().equals(ServerData.SERVER_TYPE_CATALOG)){
				ConnectDialog.addServer(servers[i]);
			}
		}


	}

	/**
	 * It creates a server list by default
	 *
	 * @return
	 */
	private ServerData[] getDefaultServers() {
		ServerData[] servers = new ServerData[4];
		Calendar cal = new GregorianCalendar();
		Date date = cal.getTime();


		servers[0] = new ServerData("http://delta.icc.es/indicio/csw", date, date, ServerData.SERVER_TYPE_CATALOG, ServerData.SERVER_SUBTYPE_CATALOG_CSW);
		servers[1] = new ServerData("mapas.euitto.upm.es:2100", date, date, ServerData.SERVER_TYPE_CATALOG, ServerData.SERVER_SUBTYPE_CATALOG_Z3950);
		servers[2] = new ServerData("193.43.36.137:2100", date, date, ServerData.SERVER_TYPE_CATALOG, ServerData.SERVER_SUBTYPE_CATALOG_Z3950);
		servers[3] = new ServerData("http://idee.unizar.es/SRW/servlet/search/ExplainSOAP",date,date,ServerData.SERVER_TYPE_CATALOG,ServerData.SERVER_SUBTYPE_CATALOG_SRW);
		return servers;
	}

	/* (non-Javadoc)
	 * @see com.iver.andami.plugins.Extension#isEnabled()
	 */
	public boolean isEnabled() {
		return true;
	}

	/* (non-Javadoc)
	 * @see com.iver.andami.plugins.Extension#isVisible()
	 */
	public boolean isVisible() {
		com.iver.andami.ui.mdiManager.IWindow f = PluginServices.getMDIManager()
		.getActiveWindow();

		if (f == null) {
			return false;
		}

		return (f instanceof BaseView);
	}
}
