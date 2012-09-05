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

import org.gvsig.tools.undo.UndoException;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;

public class LayoutUndoExtension extends Extension{

	public void initialize() {
		registerIcons();
	}

	private void registerIcons(){
		PluginServices.getIconTheme().registerDefault(
				"layout-undo",
				this.getClass().getClassLoader().getResource("images/Undo.png")
			);
	}

	public void execute(String actionCommand) {
		Layout layout=(Layout)PluginServices.getMDIManager().getActiveWindow();
		if (actionCommand.equals("UNDO")){
			try {
				layout.getLayoutContext().getFrameCommandsRecord().undo();
			} catch (UndoException e) {
				NotificationManager.showMessageError("Undo layout", e);
			}
			layout.getLayoutContext().updateFFrames();
			layout.getLayoutControl().refresh();
			layout.getModel().setModified(true);
		}
	}

	public boolean isEnabled() {
		if (PluginServices.getMDIManager().getActiveWindow() instanceof Layout){
			Layout layout=(Layout)PluginServices.getMDIManager().getActiveWindow();
			if (layout.getLayoutContext().getFrameCommandsRecord().canUndo() && layout.getLayoutContext().isEditable())
				return true;
		}
		return false;
	}

	public boolean isVisible() {
		if (PluginServices.getMDIManager().getActiveWindow() instanceof Layout){
			return true;
		}
		return false;
	}

}
