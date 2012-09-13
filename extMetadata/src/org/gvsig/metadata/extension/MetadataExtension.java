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
 * 2008 Geographic Information research group: http://www.geoinfo.uji.es
 * Departamento de Lenguajes y Sistemas Informáticos (LSI)
 * Universitat Jaume I   
 * {{Task}}
 */

package org.gvsig.metadata.extension; 



import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.gvsig.metadata.MDLibrary;
import org.gvsig.metadata.MDLocator;
import org.gvsig.metadata.MDManager;
import org.gvsig.metadata.extended.ExtendedMDLibrary;
import org.gvsig.metadata.extension.gui.DBPreferencesPage;
import org.gvsig.metadata.extension.gui.MDGUITab;
import org.gvsig.metadata.extension.gui.MDPreferencesPage;
import org.gvsig.metadata.simple.SimpleMDLibrary;
import org.gvsig.personaldb.PersonalDBLibrary;
import org.gvsig.tools.locator.Library;

import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.project.documents.view.legend.gui.ThemeManagerWindow;
import com.iver.utiles.extensionPoints.ExtensionPointsSingleton;

public class MetadataExtension extends Extension {

	public void initialize() {
		
		System.out.println("\nInitializing Metadata Extension...\n");
		
		// PROVISIONAL ------------------------------------------------------------------
		// Instalamos la implementación Simple, se debe realizar en el arranque de gvSIG.
		Library api = new MDLibrary();
		api.initialize();
		Library impl = new SimpleMDLibrary();
		impl.initialize();
		Library db = new PersonalDBLibrary();
		db.initialize();
	
		// EXTENDED Implementation Setup ------------------------------------------------
		Library implExt = new ExtendedMDLibrary();
		implExt.initialize();
		
		// ADD MD Properties TAB in SHP Layers ------------------------------------------
		ThemeManagerWindow.addPage(MDGUITab.class);
		ThemeManagerWindow.setTabEnabledForLayer(MDGUITab.class, FLyrVect.class, true);
		
		// ADD MD Preferences Panels ----------------------------------------------------
		ExtensionPointsSingleton.getInstance().add("AplicationPreferences", "MDPreferencesPage", new MDPreferencesPage());
		ExtensionPointsSingleton.getInstance().add("AplicationPreferences", "DBPreferencesPage", new DBPreferencesPage());

		// ------------------------------------------------------------------------------
		
	}
	
	public void postInitialize() {
		
		System.out.println("\nPostitializing Metadata Extension...");
		
		// PROVISIONAL ------------------------------------------------------------------
		Library api = new MDLibrary();
		api.postInitialize();
		Library impl = new SimpleMDLibrary();
		impl.postInitialize();
		Library db = new PersonalDBLibrary();
		db.postInitialize();
		
		// EXTENDED Implementation Setup ------------------------------------------------
		Library implExt = new ExtendedMDLibrary();
		implExt.postInitialize();
		
		// TEST -------------------------------------------------------------------------
		MDManager mdm = MDLocator.getInstance().getMDManager();
		System.out.println("MDManager Implementation: " + mdm.getClass().getName() + "\n");
		
		// ------------------------------------------------------------------------------
	}
	
	public void execute(String arg0) {
		// TODO Auto-generated method stub
	}

	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isVisible() {
		// TODO Auto-generated method stub
		return true;
	}

}
