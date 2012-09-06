/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2007 IVER T.I. and Generalitat Valenciana.
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
 */

package org.gvsig.gui.beans.panelGroup.exceptions;

import java.util.Map;

import org.gvsig.gui.beans.Messages;
import org.gvsig.gui.beans.panelGroup.IPanelGroup;
import org.gvsig.tools.exception.BaseException;

/**
 * <p>When an object of type {@link IPanelGroup IPanelGroup} hasn't any of its registered panels in its GUI
 *  launches an exception of this kind.</p> 
 * 
 * @version 27/11/2007
 * @author Pablo Piqueras Bartolomé (pablo.piqueras@iver.es) 
 */
public class EmptyPanelGroupGUIException extends BaseException {
	private static final long serialVersionUID = 1244814519514860627L;

	/**
	 * <p>Creates an initializes a new instance of <code>EmptyPanelGroupGUIException</code>.</p>
	 */
	public EmptyPanelGroupGUIException() {
		super();

		// Identifier of this kind of exception
		this.code = serialVersionUID;
		
		// Default text that explains this kind of exception. If there is no translation associated to the
		// "messageKey" of this exception, then the value shown will be the value of "formatString".
		this.formatString = "No panel in the graphical user interface.";

		 // Key to the sentence that explains this exception. That key will be use for multilingual purposes.
		this.messageKey = "empty_panel_group_gui_exception";

		setTranslator(new Messages());
	}

	protected Map<String, String> values() {
		return null;
	}
}
