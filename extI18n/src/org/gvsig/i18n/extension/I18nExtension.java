/* gvSIG. Geographic Information System of the Valencian Government
 *
 * Copyright (C) 2007-2008 Infrastructures and Transports Department
 * of the Valencian Gobernment (CIT)
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
 * 2008 {DiSiD Technologies}  {New extension for installation and update of text translations}
 */
package org.gvsig.i18n.extension;

import java.net.URL;

import org.gvsig.i18n.extension.preferences.I18nPreferencePage;
import org.gvsig.tools.ToolsLocator;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.About;
import com.iver.cit.gvsig.gui.panels.FPanelAbout;

/**
 * Texts localization management extension.
 * 
 * @author <a href="mailto:dcervera@disid.com">David Cervera</a>
 */
public class I18nExtension extends Extension {

    public void execute(String actionCommand) {
        // Nothing to do
    }

    public void initialize() {
        // Replace the default locale Preferences page for the new one
        ToolsLocator.getExtensionPointManager()
                .add("AplicationPreferences", "").append("LanguagePage", "",
                        new I18nPreferencePage());
    }

    public void postInitialize() {
        // Register the about panel
        About about = (About) PluginServices.getExtension(About.class);
        FPanelAbout panelAbout = about.getAboutPanel();
        URL aboutURL = getClass().getResource("/about.htm");
        panelAbout.addAboutUrl("I18nExtension", aboutURL);
    }

    public boolean isEnabled() {
        return true;
    }

    public boolean isVisible() {
        return true;
    }
}