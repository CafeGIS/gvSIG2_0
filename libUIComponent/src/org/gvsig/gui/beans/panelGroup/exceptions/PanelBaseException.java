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
import org.gvsig.tools.exception.BaseException;

/**
 * <p>Adapts any <i>Java</i> exception produced working with panels to be dealed in as a {@link BaseException BaseException}.</p>
 * 
 * @version 11/12/2007
 * @author Pablo Piqueras Bartolomé (pablo.piqueras@iver.es) 
 */
public class PanelBaseException extends BaseException {
	private static final long serialVersionUID = -5248981827020054295L;
	protected HashMap<String, String> values;
	public static final String PANEL_LABEL = "PANEL_LABEL";
	public static final String CAUSE_MESSAGE = "CAUSE_MESSAGE";

	/**
	 * <p>Creates and initializes a new instance of <code>PanelBaseException</code>.</p>
	 */
	public PanelBaseException() {
		super();
		initialize();
	}

	/**
	 * <p>Creates and initializes a new instance of <code>PanelBaseException</code>.</p>
	 * 
	 * @param e the exception to be wrappered
	 */
	public PanelBaseException(Exception e) {
		super();
		initialize();
		initCause(e);
	}

	/**
	 * <p>Creates and initializes a new instance of <code>PanelBaseException</code>.</p>
	 * 
	 * @param e the exception to be wrappered
	 * @param panelLabel label of the panel which is the source of the exception wrappered
	 */
	public PanelBaseException(Exception e, String panelLabel) {
		super();
		initialize();
		initCause(e);
		setPanelLabel(panelLabel);
	}

	protected Map<String, String> values() {
		return values;
	}

	/**
	 * <p>Initializes a <code>PanelBaseException</code> with the needed information.</p>
	 */
	protected void initialize() {
		// Identifier of this kind of exception
		this.code = serialVersionUID;
		
		// Default text that explains this kind of exception. If there is no translation associated to the
		// "messageKey" of this exception, then the value shown will be the value of "formatString".
		this.formatString = "Error loading %(" + PANEL_LABEL + ")%(" + CAUSE_MESSAGE + ").";

		 // Key to the sentence that explains this exception. That key will be use for multilingual purposes.
		this.messageKey = "panel_base_exception";
		
		// Map with the label of the panel
		values = new HashMap<String, String>();
		values.put(PANEL_LABEL, Messages.getText("a_panel"));
		values.put(CAUSE_MESSAGE, "");
		
		setTranslator(new Messages());
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
		if ((panelLabel.equals("")) || (panelLabel == null)) {
			values.put(PANEL_LABEL, Messages.getText("a_panel"));
		} else {
			values.put(PANEL_LABEL, Messages.getText("the_panel") + " \"" + panelLabel + "\"");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Throwable#initCause(java.lang.Throwable)
	 */
	public Throwable initCause(Throwable cause) {
		Throwable t = super.initCause(cause);
		
		if ((cause == null) || (cause.getLocalizedMessage() == null)) {
			values.put(CAUSE_MESSAGE, "");
		} else {
			values.put(CAUSE_MESSAGE, ": " + cause.getLocalizedMessage());
		}
		
		return t;
	}
}
