/*
 * Created on 25-ene-2007 by azabala
 *
 */
package org.gvsig.dwg.lib.readers.v2004;

import java.util.ArrayList;

import org.gvsig.dwg.lib.CorruptedDwgEntityException;
import org.gvsig.dwg.lib.DwgHandleReference;
import org.gvsig.dwg.lib.DwgObject;
import org.gvsig.dwg.lib.DwgUtil;
import org.gvsig.dwg.lib.objects.DwgPolyline3D;


/**
 * @author alzabord
 */
public class DwgPolyline3DReader2004 extends AbstractDwg2004Reader{

	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.readers.IDwgObjectReader#readSpecificObj(int[], int, com.iver.cit.jdwglib.dwg.DwgObject)
	 */
	public void readSpecificObj(int[] data, int offset, DwgObject dwgObj) throws RuntimeException, CorruptedDwgEntityException {
		if(! (dwgObj instanceof DwgPolyline3D))
			throw new RuntimeException(this.getClass().getName()+" solo puede leer DwgPolyline3D");
		DwgPolyline3D ln = (DwgPolyline3D) dwgObj;
		try{
			int bitPos = offset;
			ArrayList v;

			bitPos = readObjectHeader(data, bitPos, ln);

			v = DwgUtil.getRawChar(data, bitPos);
			bitPos = ((Integer)v.get(0)).intValue();
			int sflags = ((Integer)v.get(1)).intValue();
			ln.setSplineFlags(sflags);

			v = DwgUtil.getRawChar(data, bitPos);
			bitPos = ((Integer)v.get(0)).intValue();
			int cflags = ((Integer)v.get(1)).intValue();
			ln.setClosedFlags(cflags);

			v = DwgUtil.getBitLong(data, bitPos); //OWNED OBJECT COUNT
			bitPos = ((Integer)v.get(0)).intValue();
			int OwnedObj = ((Integer)v.get(1)).intValue();

			bitPos = readObjectTailer(data, bitPos, ln);

			DwgHandleReference handle= new DwgHandleReference();
			if (OwnedObj>0) {
				for (int i=0;i<OwnedObj;i++) {
					handle = new DwgHandleReference();
					bitPos = handle.read(data, bitPos);
					ln.addVertexHandle(handle);
				}
			}

			handle = new DwgHandleReference();
			bitPos = handle.read(data, bitPos);
			ln.setSeqendHandle(handle);
			ln.inserta();

		}catch(Exception e){
			System.out.println("BAD POLYLINE 3D."+e.getMessage());
		}
	}
}
