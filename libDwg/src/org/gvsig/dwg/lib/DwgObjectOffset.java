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
 * The DwgObjectOffset class is useful to store the handle of an object with its
 * offset in the DWG file
 * 
 * @author jmorell
 */
public class DwgObjectOffset {
	private int handle;
	private int offset;
	private int size;
	
	/**
	 * Create a new DwgObjectOffset object
	 * 
	 * @param handle Handle of the object
	 * @param offset Offset in the DWG file of the object
	 */
	public DwgObjectOffset(int handle, int offset) {
		this.handle = handle;
		this.offset = offset;
	}
	
    /**
     * @return Returns the handle.
     */
    public int getHandle() {
        return handle;
    }
    /**
     * @return Returns the offset.
     */
    public int getOffset() {
        return offset;
    }
    
    public void setSize(int size){
    	this.size=size;
    }
    
    public int getSize(){
    	return this.size;
    }
    
    
}
