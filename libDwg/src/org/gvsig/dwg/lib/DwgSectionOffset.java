/* jdwglib. Java Library for reading Dwg files.
 * 
 * Author: Jose Morell Rama (jose.morell@gmail.com).
 * Port from the Pythoncad Dwg library by Art Haas.
 *
 * Copyright (C) 2005 Jose Morell, IVER TI S.A. and Generalitat Valenciana
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
 * Jose Morell (jose.morell@gmail.com)
 * 
 * or
 *
 * IVER TI S.A.
 *  C/Salamanca, 50
 *  46005 Valencia
 *  Spain
 *  +34 963163400
 *  dac@iver.es
 */
package org.gvsig.dwg.lib;

/**
 * The DwgSectionOffset class is useful to store the key of a DWG section with its seek
 * (or offset) and with its size
 * 
 * @author jmorell
 */
public class DwgSectionOffset {
	private String key;
	private int seek;
	private int size;
	
	/**
	 * Creates a new DwgSectionOffset object
	 * 
	 * @param key Section key
	 * @param seek Seeker or offset in the DWG file for this section
	 * @param size Size of this section
	 */
	public DwgSectionOffset(String key, int seek, int size) {
		this.key = key;
		this.seek = seek;
		this.size = size;
	}
    /**
     * @return Returns the key.
     */
    public String getKey() {
        return key;
    }
    /**
     * @param key The key to set.
     */
    public void setKey(String key) {
        this.key = key;
    }
    /**
     * @return Returns the seek.
     */
    public int getSeek() {
        return seek;
    }
    /**
     * @param seek The seek to set.
     */
    public void setSeek(int seek) {
        this.seek = seek;
    }
}
