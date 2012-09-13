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
package com.iver.cit.gvsig.gui.wizards;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import org.gvsig.fmap.mapcontext.exceptions.ConnectionErrorLayerException;

import com.iver.cit.gvsig.fmap.drivers.wms.FMapWMSDriver;
import com.iver.cit.gvsig.fmap.drivers.wms.FMapWMSDriverFactory;
import com.iver.cit.gvsig.fmap.layers.WMSLayerNode;


/**
 * Class holding the data shown at the wms wizards
 *
 * @author Jaume Dominguez Faus
 */
public class WMSWizardData { // should implemement any kind of wizard data interface?
    private String title;
    private String theAbstract;
    private WMSLayerNode layer;
    private String[] formats;
    private String serverVersion;
    private FMapWMSDriver wms = null;
    private Hashtable onlineResources = null;

    /**
     * @return Returns the serverVersion.
     */
    public String getServerVersion() {
        return serverVersion;
    }
    public String getHost(){
        return wms.getHost();
    }

    public void setHost(URL host, boolean override) throws ConnectionErrorLayerException{
        try {
        	wms = FMapWMSDriverFactory.getFMapDriverForURL(host);

        	// Send a getCapabilities request;
        	if (!wms.connect(override, null)) {
				throw new ConnectionErrorLayerException(layer.getName(),null);
			}
        } catch (ConnectException e) {
        	throw new ConnectionErrorLayerException(layer.getName(),e);
        	//JOptionPane.showMessageDialog((Component)PluginServices.getMainFrame(), PluginServices.getText(this,"server_timeout"));
			//return;
		} catch (IOException e) {
			throw new ConnectionErrorLayerException(layer.getName(),e);
			//JOptionPane.showMessageDialog((Component)PluginServices.getMainFrame(), PluginServices.getText(this, "wms_cant_connect"));
			//return;
		}
        if (wms.getAbstract()  != null) {
			theAbstract = wms.getAbstract();
		}

        Vector f = wms.getFormats();
        ArrayList formatos = new ArrayList();
        for (int i = 0; i < f.size(); i++) {
        	formatos.add(f.elementAt(i));
        }
        formats = (String[]) formatos.toArray(new String[0]);
        serverVersion = (wms.getVersion() == null) ? "" : wms.getVersion();
        onlineResources = wms.getOnlineResources();
        layer = wms.getLayersTree(); // LayersTree as they are defined in the Capabilities document
    }

    public String getAbstract() {
        return theAbstract;
    }

    public String[] getFormats() {
        return formats;
    }

    public WMSLayerNode getLayer() {
        return layer;
    }

    public String getTitle() {
        return title;
    }

    public String getServerType() {
    	if (serverVersion==null) {
			return "WMS";
		}
        return "WMS "+serverVersion;
    }

    public Hashtable getOnlineResources() {
		return onlineResources;
	}

    public Rectangle2D getBoundingBox(String[] layerNames, String srs) {
        return wms.getLayersExtent(layerNames, srs);
    }

	public FMapWMSDriver getDriver() {
		return wms;
	}

    public boolean isQueryable() {
    	return wms.isQueryable();
    }

}
