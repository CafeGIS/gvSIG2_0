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

import java.util.HashMap;
import java.util.Map;

import org.gvsig.gui.beans.Messages;
import org.gvsig.gui.beans.panelGroup.IPanelGroup;
import org.gvsig.tools.exception.BaseException;

/**
 * <p>If an object of type {@link IPanelGroup IPanelGroup} tries to load a panel that its preferred sized
 *  hasn't been initialized, (not the default preferred size), then an exception of this kind will be
 *  launched.</p>
 * 
 * @version 28/11/2007
 * @author Pablo Piqueras Bartolomé (pablo.piqueras@iver.es) 
 */
public class PanelWithNoPreferredSizeDefinedException extends BaseException {
	private static final long serialVersionUID = 3953831724578904518L;
	protected HashMap<String, String> values;
	public static final String PANEL_LABEL = "PANEL_LABEL";

	/**
	 * <p>Creates an initializes a new instance of <code>PanelWithNoPreferredSizedDefinedException</code>.</p>
	 */
	public PanelWithNoPreferredSizeDefinedException() {
		super();

		initialize();
	}
	
	/**
	 * <p>Creates an initializes a new instance of <code>PanelWithNoPreferredSizedDefinedException</code>.</p>
	 * 
	 * @param panelLabel label of the panel which is the source of this exception
	 */
	public PanelWithNoPreferredSizeDefinedException(String panelLabel) {
		super();

		initialize();

		// Sets the needed information
		setPanelLabel(panelLabel);
	}
	
	/**
	 * <p>Initializes a <code>PanelBaseException</code> with the needed information.</p>
	 */
	protected void initialize() {
		// Identifier of this kind of exception
		this.code = serialVersionUID;
		
		// Default text that explains this kind of exception. If there is no translation associated to the
		// "messageKey" of this exception, then the value shown will be the value of "formatString".
		this.formatString = "Panel with label \"%(" + PANEL_LABEL + ")\" without preferred size defined.";

		 // Key to the sentence that explains this exception. That key will be use for multilingual purposes.
		this.messageKey = "panel_without_preferred_size_defined_exception";
		
		// Map with the label of the panel
		values = new HashMap<String, String>();
		values.put(PANEL_LABEL, "");

		setTranslator(new Messages());
	}

	protected Map<String, String> values() {
		return values;
	}

	/**
	 * <p>Gets the label of the panel which is the source of this exception, or
	 *  <code>null</code> if hasn't been defined.</p>
	 * 
	 * @return label of the panel which is the source of this exception
	 */
	public String getPanelLabel() {
		return values.get(PANEL_LABEL);
	}

	/**
	 * <p>Sets the label of the panel which is the source of this exception.</p>
	 * 
	 * @param panelLabel label of the panel which is the source of this exception
	 */
	public void setPanelLabel(String panelLabel) {
		if (panelLabel == null) {
			values.put(PANEL_LABEL, "");
		} else {
			values.put(PANEL_LABEL, panelLabel);
		}
	}
}
