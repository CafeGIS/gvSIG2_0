/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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
package com.iver.cit.gvsig.gui.wcs;

import java.awt.Component;
import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.gvsig.fmap.dal.exception.ReadException;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.drivers.wcs.FMapWCSDriver;
import com.iver.cit.gvsig.fmap.drivers.wcs.FMapWCSDriverFactory;
import com.iver.cit.gvsig.fmap.layers.WCSLayer;
import com.iver.cit.gvsig.gui.wizards.WizardData;



/**
 * This class holds the WCSWizard's info
 *
 * Contiene la información del asistente WCS
 *
 * @author jaume - jaume.dominguez@iver.es
 *
 */

public class WCSWizardData extends WizardData{
    private String title;
    private FMapWCSDriver wcs;
	private String theAbstract;
	private WCSLayer[] coverageList;


    public void setHost(URL host, boolean override) throws ReadException {
        try {
        	wcs = FMapWCSDriverFactory.getFMapDriverForURL(host);

			//wcs.createClient(host);

        // Send getCapabilities and describe coverage request;
		if (!wcs.connect(override, null)) {
			throw new ReadException(host.getPath(), null);
		}
        } catch (ConnectException e) {
        	JOptionPane.showMessageDialog((Component)PluginServices.getMainFrame(), PluginServices.getText(this,"wcs_server_timeout"));
			return;
		} catch (IOException e) {
			JOptionPane.showMessageDialog((Component)PluginServices.getMainFrame(), PluginServices.getText(this, "wcs_cant_connect"));
			return;
		}

		if (wcs.getLabel()!=null) {
			title = wcs.getLabel();
		}

        if (wcs.getDescription()  != null) {
			theAbstract = wcs.getDescription();
		}

        coverageList = wcs.getLayerList();
    }

    /**
     * The server description.
     *
     * La descripción del servidor
     * @return String
     */
    public String getAbstract() {
    	if (theAbstract == null) {
			return "None";
		}
        return theAbstract;
    }

    /**
     * An ArrayList with the coverage formats.
     *
     * Los formatos de la cobertura
     *
     * @return
     */
    public ArrayList getCoverageFormatos(String nomCobertura) {
        return getLayer(nomCobertura).getFormats();
    }

    /**
     * Finds the coverage within the coverage list
     *
     * Busca la cobertura en la lista de coberturas
     *
     * @return CoverageInfo
     */
    public WCSLayer getLayer(String name) {
    	for (int i = 0; i < coverageList.length; i++) {
			if (coverageList[i].getName().equals(name)) {
				return coverageList[i];
			}
		}
    	return null;
    }

    /**
     * Returns an ArrayList containing the supported SRS of the coverage specified
     * by name.
     *
     * Devuelve una lista de SRSs soportados por la cobertura "name".
     *
     * @param name
     * @return ArrayList
     */
    public ArrayList getCoverageSRSs(String name){
    	return getLayer(name).getSRSs();
    }
    /**
     * Server's title (not used in gvSIG)
     *
     * Título del servidor (no se usa en gvSIG)
     *
     * @return
     */
    public String getTitle() {
    	if (title == null) {
			return "None";
		}
        return title;
    }

    /**
     * Equivalent to the setDescription method
     *
     * Equivalente a setDescription
     *
     * @param string
     */
    public void setAbstract(String string) {
        theAbstract = string;
    }

    /**
     * Sets the server's title (not used in gvSIG)
     *
     * Establece el título del servidor (no se usa en gvSIG)
     *
     * @param string
     */
    public void setTitle(String string) {
        title = string;
    }

    /**
     * Sets the server's description.
     *
     * Establece la descripción del servidor.
     * @param String
     */
    public void setDescription(String s) {
    	setAbstract(s);
    }

    /**
     * Returns the server's descrition
     *
     * Recupera la descripción del servidor
     * @return "None" if the server doesn't specify any description
     */
    public String getDescription(){
    	if (getAbstract() == null) {
			return "None";
		}
    	return getAbstract();
    }

	public WCSLayer[] getCoverageList() {
		if (coverageList == null) {
			coverageList = new WCSLayer[0];
		}
		return coverageList;
	}

	public FMapWCSDriver getDriver() {
		return wcs;
	}

	public String getHost() {
		return wcs.getHost();
	}

	public String getServerType() {
		if (wcs.getVersion()==null) {
			return "WCS";
		}
		return "WCS "+wcs.getVersion();
	}


}
