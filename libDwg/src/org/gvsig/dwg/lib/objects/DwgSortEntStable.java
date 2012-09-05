/*
 * Created on 02-feb-2007 by azabala
 *
 */
package org.gvsig.dwg.lib.objects;

import org.gvsig.dwg.lib.DwgHandleReference;
import org.gvsig.dwg.lib.DwgObject;

/**
 * @author alzabord
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DwgSortEntStable extends DwgObject {

	private DwgHandleReference[] handles;
	private DwgHandleReference[] objHandles;
	private DwgHandleReference parentHdl;

	/**
	 * @param index
	 */
	public DwgSortEntStable(int index) {
		super(index);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param handles
	 */
	public void setSortedHandles(DwgHandleReference[] handles) {
		this.handles = handles;
	}

	/**
	 * @param handles2
	 */
	public void setObjHandles(DwgHandleReference[] handles2) {
		this.objHandles = handles2;
	}

	/**
	 * @param handle
	 */
	public void setParentHandle(DwgHandleReference handle) {
		this.parentHdl = handle;
	}
	public Object clone(){
		DwgSortEntStable obj = new DwgSortEntStable(index);
		this.fill(obj);
		return obj;
	}
	
	protected void fill(DwgObject obj){
		super.fill(obj);
		DwgSortEntStable myObj = (DwgSortEntStable)obj;

		myObj.setObjHandles(objHandles);
		myObj.setParentHandle(parentHdl);
		myObj.setSortedHandles(handles);
	}

}
