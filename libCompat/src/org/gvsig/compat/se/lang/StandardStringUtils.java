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
 * 2008 DiSiD Technologies   Create initial implementation of StringUtils for Java SE.
 */
package org.gvsig.compat.se.lang;

import org.gvsig.compat.lang.StringUtils;

/**
 * String utilities implementation for Java Standard Edition.
 * 
 * @author <a href="mailto:cordin@disid.com">Cèsar Ordiñana</a>
 */
public class StandardStringUtils implements StringUtils {

    public String[] split(String input, String regex) {
        return input == null ? null : input.split(regex);
    }

    public String[] split(String input, String regex, int limit) {
        return input == null ? null : input.split(regex, limit);
    }

    public String replaceAll(String input, String regex, String replacement) {
        return input == null ? null : input.replaceAll(regex, replacement);
    }

	public String replaceFirst(String input, String regex, String replacement) {
		return input == null ? null : input.replaceFirst(regex, replacement);
	}

}
