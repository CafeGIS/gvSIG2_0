/*
 * Created on 26-oct-2005
 *
 * gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
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
package com.iver.cit.gvsig.vectorialdb;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.DataServerExplorerParameters;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.serverexplorer.db.DBServerExplorerParameters;
import org.gvsig.gui.beans.AcceptCancelPanel;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.utiles.NotExistInXMLEntity;
import com.iver.utiles.XMLEntity;

public class DlgConnection extends JDialog {

    private JPanel jContentPane = null;
    private ConnectionPanel jConnPanel = null;
    private JButton jBtnOK = null;
    private JPanel jPanel1 = null;
    private JButton jBtnCancel = null;
    private ConnectionSettings connSettings = null;

    /**
     * This is the default constructor
     */
    public DlgConnection() {
        super();
        initialize();
    }

    private void setPreferences()
    {
        XMLEntity xml = PluginServices.getPluginServices(this).getPersistentXML();

        if (xml == null) {
            xml = new XMLEntity();
        }

        if (!xml.contains("db-connections")) {
            String[] servers = new String[0];
            xml.putProperty("db-connections", servers);
        }

        try {
            String[] servers = xml.getStringArrayProperty("db-connections");
            HashMap settings = new HashMap();
            for (int i = 0; i < servers.length; i++) {
                ConnectionSettings cs = new ConnectionSettings();
                cs.setFromString(servers[i]);
                settings.put(cs.getName(), cs);
            }
            getJConnPanel().setSettings(settings);
        } catch (NotExistInXMLEntity e) {
        }

    }
    /**
     * This method initializes this
     *
     * @return void
     */
    private void initialize() {
        this.setSize(320, 332);
        this.setTitle(PluginServices.getText(this, "database_connection"));
        this.setContentPane(getJContentPane());
        setPreferences();
        DataManager dm= DALLocator.getDataManager();
        List explorers = dm.getExplorerProviders();
        Iterator iter = explorers.iterator();
		DataServerExplorerParameters exParam = null;
		String name;
		List<String> dbExplores = new ArrayList<String>(explorers.size());
		while (iter.hasNext()) {
			name = (String) iter.next();
			try {
				exParam = dm.createServerExplorerParameters(name);
			} catch (DataException e) {
				NotificationManager.addError(e);
			}
			if (exParam instanceof DBServerExplorerParameters) {
				dbExplores.add(name);
			}
		}



        jConnPanel
				.setDrivers(dbExplores
				.toArray(new String[dbExplores.size()]));
    }

//    private String[] getDriverNames(){
//        Class[] classes = new Class[] { IVectorialDatabaseDriver.class };
//
//        ArrayList ret = new ArrayList();
//        String[] driverNames = LayerFactory.getDM().getDriverNames();
//
//        for (int i = 0; i < driverNames.length; i++) {
//            boolean is = false;
//
//            for (int j = 0; j < classes.length; j++) {
//                if (LayerFactory.getDM().isA(driverNames[i], classes[j])) {
//                    ret.add(driverNames[i]);
//                }
//            }
//        }
//
//        return (String[]) ret.toArray(new String[0]);
//
//    }

    /**
     * This method initializes jContentPane
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.add(getJConnPanel(), java.awt.BorderLayout.CENTER);
            jContentPane.add(getJPanel1(), java.awt.BorderLayout.SOUTH);

        }
        return jContentPane;
    }

    /**
     * This method initializes jPanel
     *
     * @return javax.swing.JPanel
     */
    private ConnectionPanel getJConnPanel() {
    	if (jConnPanel == null) {
    		jConnPanel = new ConnectionPanel();
    	}
    	return jConnPanel;
    }



    /**
     * This method initializes jPanel1
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel1() {
    	if (jPanel1 == null) {
    		ActionListener okAction = new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                	if (!jConnPanel.done()) {
                		JOptionPane.showMessageDialog(DlgConnection.this, "No estan todos los datos rellenos", "Error", JOptionPane.ERROR_MESSAGE);
                		return;
                	}
                	jConnPanel.saveConnectionSettings();
                    connSettings = jConnPanel.getConnectionSettings();
                    dispose();
                }
    		};

            ActionListener cancelAction = new java.awt.event.ActionListener() {
    			public void actionPerformed(java.awt.event.ActionEvent e) {
                    connSettings = null;
    				dispose();
    			}
    		};
    		jPanel1 = new AcceptCancelPanel(okAction, cancelAction);


    	}
    	return jPanel1;
    }


    public ConnectionSettings getConnSettings() {
        return connSettings;
    }

}  //  @jve:decl-index=0:visual-constraint="10,10"
