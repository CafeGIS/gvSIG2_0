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
public class DwgOle2Frame extends DwgObject {

	/**
	 * @param index
	 */
	public DwgOle2Frame(int index) {
		super(index);
		// TODO Auto-generated constructor stub
	}
	public Object clone(){
		DwgOle2Frame obj = new DwgOle2Frame(index);
		this.fill(obj);
		return obj;
	}
	
	protected void fill(DwgObject obj){
		super.fill(obj);
		//DwgOle2Frame myObj = (DwgOle2Frame)obj;

	}

}
