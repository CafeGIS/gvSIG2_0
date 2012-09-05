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
 * 2008 DiSiD Technologies   Extract interface for the StringUtils utility
 */
package org.gvsig.compat.lang;

/**
 * String Utilities used for Java SE-ME compatibility.
 * 
 * @author <a href="mailto:cordin@disid.com">C�sar Ordi�ana</a>
 */
public interface StringUtils {
    
    /**
     * Compatible implementation of the String.split(regexp) method.
     * @see String#split(String)
     */
    String[] split(String input, String regex);
    
    /**
     * Compatible implementation of the String.split(regexp, limit) method.
     * @see String#split(String, int)
     */
    String[] split(String input, String regex, int limit);
    
    /**
     * Compatible implementation of the String.replaceAll(regexp, replacement)
     * method.
     * 
     * @see String#replaceAll(String, String)
     */
    String replaceAll(String input, String regex,
            String replacement);
    
    /**
     * Compatible implementation of the String.replaceFirst(regexp, replacement)
     * method.
     * 
     * @see String#replaceFirst(String, String)
     */
    String replaceFirst(String input, String regex,
            String replacement);
}