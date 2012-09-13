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
 * 2009 {DiSiD Technologies}  {New extension for installation and update of text translations}
 */
package org.gvsig.i18n;

import com.iver.andami.messages.NotificationManager;

/**
 * Exception for errors of the I18nManager.
 * 
 * TODO: hacer que extienda BaseException cuando se pase a gvSIG trunk.
 * 
 * @author <a href="mailto:dcervera@disid.com">David Cervera</a>
 */
public class I18nException extends Exception {

    private static final long serialVersionUID = 2877056472517898602L;
    
    private String key;

    /**
     * Creates a new exception with an error message.
     * 
     * @param message
     *            the error message
     * @param key
     *            the i18n key to localize the exception message
     */
    public I18nException(String message, String key) {
	super(message);
	this.key = key;
    }

    /**
     * Creates a new exception with an error message and a cause exception.
     * 
     * @param message
     *            the error message
     * @param key
     *            the i18n key to localize the exception message
     * @param cause
     *            the error that caused the exception
     */
    public I18nException(String message, String key, Throwable cause) {
	super(message, cause);
	this.key = key;
    }
    
    public String getLocalizedMessage() {
	String msg = Messages.getText(key);
	if (msg.equals(key)) {
	    msg = getMessage();
	}
	return msg;
    }

    /**
     * Show the exception error in through the NotificationManager.
     */
    public void showError() {
	NotificationManager.addError(getLocalizedMessage(), this);
    }
}