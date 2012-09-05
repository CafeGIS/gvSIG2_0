package org.gvsig.catalog;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.About;
import com.iver.cit.gvsig.gui.panels.FPanelAbout;

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
/* CVS MESSAGES:
 *
 * $Id: CatalogClientAboutExtension.java 499 2007-07-10 11:18:10Z jorpiell $
 * $Log$
 * Revision 1.1.2.1  2007/07/10 11:18:10  jorpiell
 * Added the registers
 *
 *
 */
/**
 * This class implements the About message for the 
 * catalog and gazetteer clients
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class CatalogClientAboutExtension extends Extension{

	/*
	 * (non-Javadoc)
	 * @see com.iver.andami.plugins.IExtension#execute(java.lang.String)
	 */
	public void execute(String actionCommand) {
		// TODO Auto-generated method stub
		
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.andami.plugins.IExtension#initialize()
	 */
	public void initialize() {
		super.postInitialize();
		About about = (About) PluginServices.getExtension(About.class);
		FPanelAbout panelAbout = about.getAboutPanel();
		java.net.URL aboutURL = this.getClass().getResource("/about.htm");
		panelAbout.addAboutUrl(PluginServices.getText(this, "Catalog"),
				aboutURL);
		//Register the icons
		PluginServices.getIconTheme().registerDefault(
				"catalog-search",
				getClass().getClassLoader().getResource("images/SearchButton.png")
				); 

	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.andami.plugins.IExtension#isEnabled()
	 */
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.andami.plugins.IExtension#isVisible()
	 */
	public boolean isVisible() {
		// TODO Auto-generated method stub
		return false;
	}

}
