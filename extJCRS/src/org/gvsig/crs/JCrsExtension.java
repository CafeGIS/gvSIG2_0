/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2006 Instituto de Desarrollo Regional and Generalitat Valenciana.
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
 *   Instituto de Desarrollo Regional (Universidad de Castilla La-Mancha
 *   Campus Universitario s/n
 *   02071 Alabacete
 *   Spain
 *
 *   +34 967 599 200
 */


package org.gvsig.crs;

import java.util.Arrays;
import java.util.Iterator;

import org.geotools.factory.FactoryRegistry;
import org.geotools.referencing.operation.MathTransformProvider;
import org.gvsig.crs.gui.panels.CrsUIFactory;
import org.gvsig.crs.gui.panels.ProjChooserPanel;
import org.gvsig.fmap.crs.CRSFactory;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.About;
import com.iver.cit.gvsig.gui.panels.CRSSelectPanel;
import com.iver.cit.gvsig.gui.panels.FPanelAbout;
import com.iver.cit.gvsig.project.Project;
import com.iver.utiles.XMLEntity;

public class JCrsExtension extends Extension {

	private static final String DEFAULT_PROJECTION_KEY_NAME = "DefaultProjection";
	private static final String FACTORY_DEFAULT_PROJECTION = "EPSG:23030";

	public void initialize() {

		/*
		 * Se eliminan del registro de geotools las proyecciones que aporta libJCrs para asegurar que
		 * son estas �ltimas las que se utilizan.
		 */
		final Class[] categories = {org.geotools.referencing.operation.MathTransformProvider.class};
		FactoryRegistry registry = new FactoryRegistry(Arrays.asList(categories));
		Iterator providers = registry.getServiceProviders(MathTransformProvider.class);
		Iterator providers2 = null;
		MathTransformProvider provider = null;
		MathTransformProvider provider2 = null;

		while (providers.hasNext()){
			provider = (MathTransformProvider) providers.next();
			if(provider.nameMatches("IDR")){
				providers2 = registry.getServiceProviders(MathTransformProvider.class);
				while (providers2.hasNext()){
					provider2 = (MathTransformProvider) providers2.next();
					if(provider2.nameMatches(provider.getName().toString()) && !provider2.nameMatches("IDR")) {
						registry.deregisterServiceProvider(provider2, categories[0]);
					}
				}
			}
		}

		//TODO: Solo para depurar: BORRAR
		providers = registry.getServiceProviders(MathTransformProvider.class);
		while (providers.hasNext()) {
            provider = (MathTransformProvider) providers.next();
            System.out.println(provider.toString());
		}
		/*
		 *
		 */


		CRSFactory.cp = new CrsFactory();
		CRSSelectPanel.registerPanelClass(ProjChooserPanel.class);
		CRSSelectPanel.registerUIFactory(CrsUIFactory.class);
		//	Default Projection
		PluginServices ps = PluginServices.getPluginServices("com.iver.cit.gvsig");
		XMLEntity xml = ps.getPersistentXML();
		String projCode = null;
		if (xml.contains(DEFAULT_PROJECTION_KEY_NAME)) {
			projCode = xml.getStringProperty(DEFAULT_PROJECTION_KEY_NAME);
		} else {
			projCode = FACTORY_DEFAULT_PROJECTION;
		}
		Project.setDefaultProjection(CRSFactory.getCRS(projCode));

		About about=(About)PluginServices.getExtension(About.class);
		FPanelAbout panelAbout=about.getAboutPanel();
		java.net.URL aboutURL = this.getClass().getResource("/about.htm");
		panelAbout.addAboutUrl("JCRS",aboutURL);
	}

	public void execute(String actionCommand) {


	}

	public boolean isEnabled() {

		return true;
	}

	public boolean isVisible() {

		return false;
	}

}
