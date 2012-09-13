/*******************************************************************************
SextanteModelerExtension.java
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

import com.iver.andami.plugins.Extension;

import es.unex.sextante.gui.core.SextanteGUI;

public class SextanteModelerExtension extends Extension {


	public void initialize(){}


	public void execute(String actionCommand) {

		SextanteGUI.getGUIFactory().showModelerDialog();

	}

	public boolean isEnabled() {

		return true;

	}

	public boolean isVisible() {

		return true;

	}

}