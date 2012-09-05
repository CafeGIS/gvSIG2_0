/*
 * Created on 25-ene-2007 by azabala
 *
 */
package org.gvsig.dwg.lib.readers.v15;

import java.util.ArrayList;

import org.gvsig.dwg.lib.CorruptedDwgEntityException;
import org.gvsig.dwg.lib.DwgHandleReference;
import org.gvsig.dwg.lib.DwgObject;
import org.gvsig.dwg.lib.DwgUtil;
import org.gvsig.dwg.lib.objects.DwgPolyline3D;


/**
 * @author alzabord
 */
public class DwgPolyline3DReader15 extends AbstractDwg15Reader{

	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.readers.IDwgObjectReader#readSpecificObj(int[], int, com.iver.cit.jdwglib.dwg.DwgObject)
	 */
	public void readSpecificObj(int[] data, int offset, DwgObject dwgObj) throws RuntimeException, CorruptedDwgEntityException {
		if(! (dwgObj instanceof DwgPolyline3D))
			throw new RuntimeException("ArcReader 15 solo puede leer DwgPolyline3D");
		DwgPolyline3D ln = (DwgPolyline3D) dwgObj;
		int bitPos = offset;
		bitPos = headTailReader.readObjectHeader(data, bitPos, ln);
		ArrayList v = DwgUtil.getRawChar(data, bitPos);
		bitPos = ((Integer)v.get(0)).intValue();
		int sflags = ((Integer)v.get(1)).intValue();
		ln.setSplineFlags(sflags);
		v = DwgUtil.getRawChar(data, bitPos);
		bitPos = ((Integer)v.get(0)).intValue();
		int cflags = ((Integer)v.get(1)).intValue();
		ln.setClosedFlags(cflags);
		bitPos = headTailReader.readObjectTailer(data, bitPos, ln);
		
		DwgHandleReference hr = new DwgHandleReference();
		bitPos = hr.read(data, bitPos);
		ln.setFirstVertexHandle(hr);

		hr = new DwgHandleReference();
		bitPos = hr.read(data, bitPos);
		ln.setLastVertexHandle(hr);

		hr = new DwgHandleReference();
		bitPos = hr.read(data, bitPos);
	    ln.setSeqendHandle(hr);
	}
}
