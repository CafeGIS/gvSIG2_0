/*
 * Created on 02-feb-2007 by azabala
 *
 */
package org.gvsig.dwg.lib.objects;

import java.util.List;

import org.gvsig.dwg.lib.DwgHandleReference;
import org.gvsig.dwg.lib.DwgObject;


/**
 * @author alzabord
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DwgIdBuffer extends DwgObject {

	private DwgHandleReference parentHandle;
	private List handles;

	/**
	 * @param index
	 */
	public DwgIdBuffer(int index) {
		super(index);
		// TODO Auto-generated constructor stub
	}

	public void setParentHandle(DwgHandleReference handle) {
		this.parentHandle = handle;
	}

	public void setObjidHandles(List handles) {
		this.handles = handles;
	}

	public List getHandles() {
		return handles;
	}

	public void setHandles(List handles) {
		this.handles = handles;
	}

	public DwgHandleReference getParentHandle() {
		return parentHandle;
	}
	public Object clone(){
		DwgIdBuffer obj = new DwgIdBuffer(index);
		this.fill(obj);
		return obj;
	}
	
	protected void fill(DwgObject obj){
		super.fill(obj);
		DwgIdBuffer myObj = (DwgIdBuffer)obj;

		myObj.setHandles(handles);
		myObj.setParentHandle(parentHandle);
	}

}
