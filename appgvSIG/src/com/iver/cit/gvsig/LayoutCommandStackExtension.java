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
import com.iver.cit.gvsig.gui.command.CommandStackDialog;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;

public class LayoutCommandStackExtension extends Extension{
	private Layout layout=null;
	public void initialize() {
		registerIcons();
	}

	private void registerIcons(){
		PluginServices.getIconTheme().registerDefault(
				"edition-command-stack",
				this.getClass().getClassLoader().getResource("images/commandstack.png")
			);
		PluginServices.getIconTheme().registerDefault(
				"edition-modify-command",
				this.getClass().getClassLoader().getResource("images/ModifyCommand.png")
			);
		PluginServices.getIconTheme().registerDefault(
				"edition-add-command",
				this.getClass().getClassLoader().getResource("images/AddCommand.png")
			);
		PluginServices.getIconTheme().registerDefault(
				"edition-del-command",
				this.getClass().getClassLoader().getResource("images/DelCommand.png")
			);
	}

	public void execute(String s) {
		layout=(Layout)PluginServices.getMDIManager().getActiveWindow();
		if (s.equals("COMMANDSTACK")) {
			CommandStackDialog csd=new CommandStackDialog();
			csd.setModel(layout.getLayoutContext().getFrameCommandsRecord());
			layout.getLayoutContext().getFrameCommandsRecord().addObserver(layout);
			PluginServices.getMDIManager().addWindow(csd);
			layout.getModel().setModified(true);
		}
	}

	public boolean isEnabled() {
		layout = (Layout) PluginServices.getMDIManager().getActiveWindow();
		if (layout.getLayoutContext().isEditable())
			return true;
		return false;
	}

	public boolean isVisible() {
		if (PluginServices.getMDIManager().getActiveWindow() instanceof Layout){
			return true;
		}
		return false;
	}

}
