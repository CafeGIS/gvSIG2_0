/* jdwglib. Java Library for reading Dwg files.
 *
 * Author: Equipo de desarrollo de gvSIG.
 * Port from the Pythoncad Dwg library by Art Haas.
 *
 * Copyright (C) 2007 IVER TI S.A. and Generalitat Valenciana
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
 * IVER TI S.A.
 *  C/Salamanca, 50
 *  46005 Valencia
 *  Spain
 *  +34 963163400
 *  dac@iver.es
 */package org.gvsig.dwg.lib;

import java.util.ArrayList;


public class DwgHandleReference {

	private Integer code=null;
	private Integer offset=null;
	private Integer counter=null;

	public DwgHandleReference(int code, int offset){
		this.code=new Integer(code);
		this.offset=new Integer(offset);
	}

	public DwgHandleReference(){
	}

	public void setCode(int code){
		this.code = new Integer(code);
	}

	public void setOffset(int offset){
		this.offset=new Integer(offset);
	}

	public int getCode(){
		return this.code.intValue();
	}

	public int getOffset(){
		return this.offset.intValue();
	}

	public int getCounter(){
		return this.counter.intValue();
	}

	public void setCounter(int counter){
		this.counter=new Integer(counter);
	}

	/**
	 * Read a pair of int values (the HandleCode and HandleOffset of a handle of a DWG object)
	 * from a group of unsigned bytes and initializes the properties.
	 *
	 * @param data Array of unsigned bytes obtained from the DWG binary file
	 * @param offset The current bit offset where the value begins
	 * @throws Exception If an unexpected bit value is found in the DWG file. Occurs
	 * 		   when we are looking for LwPolylines.
	 * @return int Represents the new offset.
	 *
	 * 	 */
	public int read(int[] data, int offset) throws RuntimeException, CorruptedDwgEntityException{
		ArrayList v = new ArrayList();
		int code = ((Integer)DwgUtil.getBits(data, 4, offset)).intValue();
		int counter = ((Integer)DwgUtil.getBits(data, 4, (offset + 4))).intValue();
		int read = 8;
		ArrayList hlist = new ArrayList();
		if (counter>0) {
			int hlen = counter * 8;
			Object handle = DwgUtil.getBits(data, hlen, (offset + 8));
			read = read + hlen;
			if (hlen > 8) {
				byte[] handleBytes = (byte[])handle;
				int[] handleInts = new int[handleBytes.length];
				// Hacerlos unsigned ...
				for (int i=0; i<handleBytes.length; i++) {
					handleInts[i] = ByteUtils.getUnsigned(handleBytes[i]);
				}
				for (int i=0; i<handleInts.length; i++) {
					hlist.add(new Integer(handleInts[i]));
				}
			} else {
				hlist.add(handle);
			}
		}
		v.add(new Integer(code));
		v.add(new Integer(counter));
		for (int i=0;i<hlist.size();i++) {
			v.add(hlist.get(i));
		}
		setCode(code);
		setCounter(counter);
		setOffset(DwgUtil.handleBinToHandleInt(v));
		return offset+read;
	}

}
