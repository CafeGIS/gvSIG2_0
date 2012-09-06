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
import org.gvsig.gui.beans.panelGroup.loaders.IPanelGroupLoader;
import org.gvsig.tools.exception.ListBaseException;

/**
 * <p>It's possible that during the loading process of an {@link IPanelGroupLoader IPanelGroupLoader}, any of the panels
 *  that are being loaded could failed. To avoid stop the process, the exception launched will be caught and
 *  stored in an exception of this kind.</p>
 *
 * @version 27/11/2007
 * @author Pablo Piqueras Bartolomé (pablo.piqueras@iver.es)
 */
public class ListCouldntLoadPanelException extends ListBaseException {
	private static final long serialVersionUID = -8474169506341379995L;


	// Default text that explains this kind of exception. If there is no
	// translation associated to the
	// "messageKey" of this exception, then the value shown will be the value of
	// "formatString".
	private static final String formatString = "Couldn't load some panels:";

	// Key to the sentence that explains this exception. That key will be use
	// for multilingual purposes.
	private static final String messageKey = "couldnt_load_some_panel_exception";



	protected ListCouldntLoadPanelException(String message, String key,
			long code) {
		super(formatString, messageKey, serialVersionUID);
	}
	/**
	 * <p>Creates an initializes a new instance of <code>ListCouldntLoadPanelException</code>.</p>
	 */
	public ListCouldntLoadPanelException() {
		super(formatString, messageKey, serialVersionUID);

		setTranslator(new Messages());
	}

	protected Map<String, String> values() {
		return null;
	}
}
