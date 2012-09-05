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
package org.gvsig.dwg.lib.readers;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.gvsig.dwg.lib.CorruptedDwgEntityException;
import org.gvsig.dwg.lib.DwgFile;
import org.gvsig.dwg.lib.DwgObject;


/**
 * The IDwgFileReader reads all the DWG format versions
 * 
 * @author jmorell
 */
public interface IDwgFileReader {
    
    /**
     * Reads all the DWG format versions
     * 
	 * @param dwgFile Represents the DWG file that we want to read
     * @param bb  Memory mapped byte buffer of the whole DWG file
     * @throws IOException When DWG file path is wrong
     */
	public void read(DwgFile dwgFile, ByteBuffer bb) throws IOException;
	
	/**
	 * Reads the header of a dwg object
	 * */
	public int readObjectHeader(int[] data, int offset, DwgObject dwgObject) throws RuntimeException, CorruptedDwgEntityException;
	
	/**
	 * Reads the tailer of a dwg object.
	 * */
	public int readObjectTailer(int[] data, int offset, DwgObject dwgObject) throws RuntimeException, CorruptedDwgEntityException;
}
