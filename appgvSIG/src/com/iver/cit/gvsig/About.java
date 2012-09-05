/*
 * Created on 17-feb-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
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
package com.iver.cit.gvsig;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.gui.panels.FPanelAbout;


/**
 * Extensión que abre una nueva ventana mostrando la información sobre el
 * gvSIG.
 *
 * @author Francisco José Peñarrubia
 */
public class About extends Extension {
    private static FPanelAbout panelAbout = new FPanelAbout();
	/**
	 * @see com.iver.andami.plugins.IExtension#isEnabled()
	 */
	public boolean isEnabled() {
		return true;
	}

	/**
	 * @see com.iver.mdiApp.plugins.IExtension#isVisible()
	 */
	public boolean isVisible() {
		return true;
	}

	/**
	 * @see com.iver.andami.plugins.IExtension#initialize()
	 */
	public void initialize() {
        java.net.URL aboutURL = About.class.getResource(
        "/about.htm");
        panelAbout.addAboutUrl("gvSIG",aboutURL);

        // EXAMPLE TO MAKE USE OF ABOUT WINDOW FROM AN EXTERNAL
        // EXTENSION
        /* About claseAbout = (About) PluginServices.getExtension(com.iver.cit.gvsig.About.class);
        java.net.URL aboutURL2 = About.class.getResource(
        "/about.htm");

        claseAbout.getAboutPanel().addAboutUrl("NewExtensionName", aboutURL2); */
        
        
	}

	/**
	 * @see com.iver.andami.plugins.IExtension#execute(java.lang.String)
	 */
	public void execute(String actionCommand) {
		PluginServices.getMDIManager().addWindow(panelAbout);
        /*
         * Ejemplo de cómo crear una ventana para hacer paletas.
         * Cuando tenga tiempo, hay que hacerlo con Andami para
         * que Andami no dependa de los JInternalFrame, y algún
         * día (si hace falta) se cambie.
         *  
         *  JInternalFrame panel = new JInternalFrame();
        panel.setClosable(true);
        panel.setSize(200,200);
        panel.setTitle("Hola");
        
        MDIFrame mainFrame = (MDIFrame) PluginServices.getMainFrame();
        JLayeredPane lyrPane = mainFrame.getLayeredPane();
        lyrPane.add(panel, JDesktopPane.PALETTE_LAYER);        
        panel.show(); */
	}
    
    /**
     * An external plugin must call this method to obtain
     * the AboutPanel and add its own about.
     * Example:
     * In initialize, call PluginServices.getExtension and 
     * cast it to About class.
     * Then, call getAboutPanel, and addAboutUrl
     * PluginServices.getExtension(com.iver.cit.gvsig.About);
     * See the initalize method inside this extension
     * @return the About Panel
     */
    public FPanelAbout getAboutPanel()
    {
        return panelAbout;
    }
}
