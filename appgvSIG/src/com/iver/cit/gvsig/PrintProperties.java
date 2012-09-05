/*
 * Created on 19-may-2004
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

/**
 */
import java.awt.geom.Rectangle2D;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;


/**
 * DOCUMENT ME!
 *
 * @author vcn
 */
public class PrintProperties extends Extension /*implements IPreferenceExtension*/ {
    private Layout l;
//    private static final IPreference printPropertiesPage = new PrintPropertiesPage();
   // private Paper paper;
    Rectangle2D.Double aux = null;

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     */
    public void execute(String s) {
        l = (Layout) PluginServices.getMDIManager().getActiveWindow();
		l.showFConfig();
        //l.showPagePropertiesWindow(Print.printerJob);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isVisible() {
        IWindow f = PluginServices.getMDIManager().getActiveWindow();

        if (f == null) {
            return false;
        }

        return (f instanceof Layout);
    }

    /**
     * @see com.iver.mdiApp.plugins.IExtension#isEnabled()
     */
    public boolean isEnabled() {
        Layout f = (Layout) PluginServices.getMDIManager().getActiveWindow();

        if (f == null || !f.getLayoutContext().isEditable()) {
            return false;
        }

        return true;
    }

	/**
	 * @see com.iver.andami.plugins.IExtension#initialize()
	 */
	public void initialize() {
		registerIcons();
	}
	
	private void registerIcons(){
		PluginServices.getIconTheme().registerDefault(
				"layout-page-setup",
				this.getClass().getClassLoader().getResource("images/Frame.gif")
			);
		PluginServices.getIconTheme().registerDefault(
				"prepare-page-icon",
				this.getClass().getClassLoader().getResource("images/prepare-page.png")
			);
		PluginServices.getIconTheme().registerDefault(
				"portrait-page-setup",
				this.getClass().getClassLoader().getResource("images/portrait-page.png")
			);
		PluginServices.getIconTheme().registerDefault(
				"landscape-page-setup",
				this.getClass().getClassLoader().getResource("images/landscape-page.png")
			);
	}

//	public IPreference getPreferencesPage() {
//		return printPropertiesPage;
//	}
}
