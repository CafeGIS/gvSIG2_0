/*
 * Created on 02-feb-2007 by azabala
 *
 */
package org.gvsig.dwg.lib.objects;

import org.gvsig.dwg.lib.DwgObject;

/**
 * @author alzabord
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DwgSpatialFilter extends DwgObject {

	/**
	 * @param index
	 */
	public DwgSpatialFilter(int index) {
		super(index);
		// TODO Auto-generated constructor stub
	}
	public Object clone(){
		DwgSpatialFilter obj = new DwgSpatialFilter(index);
		this.fill(obj);
		return obj;
	}
	
	protected void fill(DwgObject obj){
		super.fill(obj);
		//DwgSpatialFilter myObj = (DwgSpatialFilter)obj;

	}

}
