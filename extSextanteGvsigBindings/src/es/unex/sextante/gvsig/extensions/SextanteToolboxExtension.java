/*******************************************************************************
SextanteToolboxExtension.java
Copyright (C) Victor Olaya

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*******************************************************************************/
package es.unex.sextante.gvsig.extensions;

import java.awt.Frame;
import java.io.File;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;

import es.unex.sextante.core.Sextante;
import es.unex.sextante.gui.core.SextanteGUI;
import es.unex.sextante.gvsig.core.gvOutputFactory;
import es.unex.sextante.gvsig.gui.gvGUIFactory;
import es.unex.sextante.gvsig.gui.gvInputFactory;
import es.unex.sextante.gvsig.gui.gvPostProcessTaskFactory;

public class SextanteToolboxExtension extends Extension {

	public void initialize(){

		Sextante.initialize();
		SextanteGUI.initialize();
		SextanteGUI.setHelpPath(System.getProperty("user.dir") + File.separator + "gvSIG"
				+ File.separator + "extensiones" + File.separator + "sextante_help");
		SextanteGUI.setMainFrame(((Frame)PluginServices.getMainFrame()));
		SextanteGUI.setOutputFactory(new gvOutputFactory());
		SextanteGUI.setGUIFactory(new gvGUIFactory());
		SextanteGUI.setInputFactory(new gvInputFactory());
		SextanteGUI.setPostProcessTaskFactory(new gvPostProcessTaskFactory());

	}

	public void execute(String actionCommand) {

		SextanteGUI.getGUIFactory().showToolBoxDialog();

	}

	public boolean isEnabled() {

		return true;

	}

	public boolean isVisible() {

		return true;

	}

}