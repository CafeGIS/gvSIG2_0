/*
 * Created on 25-ene-2007 by azabala
 *
 */
package org.gvsig.dwg.lib.readers.v15;

import org.gvsig.dwg.lib.CorruptedDwgEntityException;
import org.gvsig.dwg.lib.DwgObject;

/**
 * @author alzabord
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DwgSeqEndReader15 extends AbstractDwg15Reader{

	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.readers.IDwgObjectReader#readSpecificObj(int[], int, com.iver.cit.jdwglib.dwg.DwgObject)
	 */
	//TODO Algunos objetos no aportan nada al head y tail
	//ver si quitarlo (o pasar la implementacion a la clase abstracta)
	public void readSpecificObj(int[] data, int offset, DwgObject dwgObj) throws RuntimeException, CorruptedDwgEntityException {
		int bitPos = offset;
		bitPos = headTailReader.readObjectHeader(data, bitPos, dwgObj);
		bitPos = headTailReader.readObjectTailer(data, bitPos, dwgObj);
	}
}
