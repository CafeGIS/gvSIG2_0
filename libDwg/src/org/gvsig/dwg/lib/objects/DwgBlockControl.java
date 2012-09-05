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
package org.gvsig.dwg.lib.objects;

import java.util.Vector;

import org.gvsig.dwg.lib.DwgHandleReference;
import org.gvsig.dwg.lib.DwgObject;


/**
 * The DwgBlockControl class represents a DWG Block control
 * 
 * @author jmorell
 */
public class DwgBlockControl extends DwgObject {

	private DwgHandleReference nullHandle = null;
	private Vector code2Handles;
	private DwgHandleReference modelSpaceHandle = null;
	private DwgHandleReference paperSpaceHandle = null;
	
	public DwgBlockControl(int index) {
		super(index);
	}
	
	public Vector getCode2Handles() {
		return code2Handles;
	}
	public void setCode2Handles(Vector code2Handles) {
		this.code2Handles = code2Handles;
	}
	public DwgHandleReference getModelSpaceHandle() {
		return modelSpaceHandle;
	}
	public void setModelSpaceHandle(DwgHandleReference modelSpaceHandle) {
		this.modelSpaceHandle = modelSpaceHandle;
	}
	public DwgHandleReference getNullHandle() {
		return nullHandle;
	}
	public void setNullHandle(DwgHandleReference nullHandle) {
		this.nullHandle = nullHandle;
	}
	public DwgHandleReference getPaperSpaceHandle() {
		return paperSpaceHandle;
	}
	public void setPaperSpaceHandle(DwgHandleReference paperSpaceHandle) {
		this.paperSpaceHandle = paperSpaceHandle;
	}
	public Object clone(){
		DwgBlockControl obj = new DwgBlockControl(index);
		this.fill(obj);
		return obj;
	}
	
	protected void fill(DwgObject obj){
		super.fill(obj);
		DwgBlockControl myObj = (DwgBlockControl)obj;

		myObj.setCode2Handles(code2Handles);
		myObj.setModelSpaceHandle(modelSpaceHandle);
		myObj.setNullHandle(nullHandle);
		myObj.setPaperSpaceHandle(paperSpaceHandle);
	}

}
