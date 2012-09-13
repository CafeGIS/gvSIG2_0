/* gvSIG. Geographic Information System of the Valencian Government
*
* Copyright (C) 2007-2008 Infrastructures and Transports Department
* of the Valencian Government (CIT)
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
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
* MA  02110-1301, USA.
*
*/

/*
* AUTHORS (In addition to CIT):
* 2008 IVER T.I   {{Task}}
*/

/**
 *
 */
package com.iver.cit.gvsig.vectorialdb;

import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.resource.Resource;
import org.gvsig.fmap.dal.resource.ResourceManager;
import org.gvsig.fmap.dal.resource.ResourceNotification;
import org.gvsig.fmap.dal.resource.db.DBParameters;
import org.gvsig.fmap.dal.serverexplorer.db.DBServerExplorerParameters;
import org.gvsig.fmap.dal.store.jdbc.JDBCResource;
import org.gvsig.fmap.dal.store.jdbc.JDBCResourceParameters;
import org.gvsig.tools.observer.Observable;
import org.gvsig.tools.observer.Observer;

import com.iver.andami.PluginServices;

//TODO comentado para que compile
public class DBResourceManager implements Observer {
	private boolean checking = false;

	public DBResourceManager(){

	}
	public void update(Observable observable, Object notification) {
		if (!(notification instanceof ResourceNotification)) {
			return;
		}
		ResourceNotification resNot = (ResourceNotification) notification;
		if (!resNot.getType().equals(ResourceNotification.PREPARE)){
			return;
		}
		if (!(resNot.getResource() instanceof JDBCResource)){
			return;
		}
		JDBCResource res = (JDBCResource)resNot.getResource();
		JDBCResourceParameters resPrams = (JDBCResourceParameters) resNot
				.getParameters();
		if (!checking && resPrams.getPassword() == null) {
			checking = true;
			JPasswordDlg passwordPanel=new JPasswordDlg();
			passwordPanel.setMessage(PluginServices.getText(this,"falta_por_introducir_la_clave_en_la_conexion"));
			PluginServices.getMDIManager().addCentredWindow(passwordPanel);
			resPrams.setPassword(passwordPanel.getPassword());
			// TODO check if password is ok ??
//			if(!res.testConnection()){
//				checking=false;
//				try {
//					res.setPassword(null);
//				} catch (InitializeException e) {
//					e.printStackTrace();
//				}
//			}
//			checking=false;
		}
	}

	private JDBCResource getResource(
			DBServerExplorerParameters explorerParameters) {
		ResourceManager rManager = DALLocator.getResourceManager();
		Resource res;



		return null;
	}

	private JDBCResource getResource(DBParameters storeParameters) {
		ResourceManager rManager = DALLocator.getResourceManager();

		return null;
	}

//	public boolean isConnected(DBParameters dbParameters);
//
//
//	public boolean isConnected(DBExplorerParameters explorerParameters) {
//		DBResourceManager res = this.getResource(explorerParameters);
//		if (res == null) {
//			return false;
//		}
//		return res.isOpen();
//	}
//
//	public boolean isConnected(DBExplorer explorer) {
//		JDBCResource res = this.getResource(explorer.getParameters());
//		if (res == null) {
//			return false;
//		}
//		return res.isOpen();
//	}

}

