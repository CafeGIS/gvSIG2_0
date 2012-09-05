/*
 * Created on 25-ene-2007 by azabala
 *
 */
package org.gvsig.dwg.lib.readers.v15;

import org.gvsig.dwg.lib.DwgObject;

/**
 * @author alzabord
 */
public class DwgLayerControlReader15 extends AbstractDwg15Reader{

	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.readers.IDwgObjectReader#readSpecificObj(int[], int, com.iver.cit.jdwglib.dwg.DwgObject)
	 */
	public void readSpecificObj(int[] data, int offset, DwgObject dwgObj) {
		//TODO Si no lo leemos, mejor ni considerarlo
		//Ver la especificación de este objeto
	}

}
