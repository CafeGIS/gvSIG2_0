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
package org.gvsig.i18n.impl;

import org.gvsig.i18n.I18nException;

/**
 * Exception for errors installing a locale or locales from an import file.
 * 
 * @author <a href="mailto:dcervera@disid.com">David Cervera</a>
 */
public class LocaleLanguageRequiredException extends I18nException {

    private static final long serialVersionUID = 8426119666919876086L;

    private static final String MSG = "A line in the CSV file with the locales to install or update does not contain the locale language code";

    private static final String KEY = "_LocaleLanguageRequiredException";
    
    private String line;

    public LocaleLanguageRequiredException(String line) {
	super(MSG, KEY);
	this.line = line;
    }

    public LocaleLanguageRequiredException(String line, Throwable cause) {
	super(MSG, KEY, cause);
	this.line = line;
    }
 
    public String getLocalizedMessage() {
	return super.getLocalizedMessage() + ": " + line;
    }
}