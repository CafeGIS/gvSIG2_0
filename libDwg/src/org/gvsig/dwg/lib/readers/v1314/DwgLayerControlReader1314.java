/*
 * Created on 25-ene-2007 by azabala
 *
 */
package org.gvsig.dwg.lib.readers.v1314;

import java.util.ArrayList;
import java.util.List;

import org.gvsig.dwg.lib.CorruptedDwgEntityException;
import org.gvsig.dwg.lib.DwgHandleReference;
import org.gvsig.dwg.lib.DwgObject;
import org.gvsig.dwg.lib.DwgUtil;
import org.gvsig.dwg.lib.objects.DwgLayerControl;


/**
 * @author alzabord
 */
public class DwgLayerControlReader1314 extends AbstractDwg1314Reader{

	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.readers.IDwgObjectReader#readSpecificObj(int[], int, com.iver.cit.jdwglib.dwg.DwgObject)
	 */
	public void readSpecificObj(int[] data, int offset, DwgObject dwgObj) throws RuntimeException, CorruptedDwgEntityException {
		if(! (dwgObj instanceof DwgLayerControl))
	    	throw new RuntimeException("ArcReader 14 solo puede leer DwgLayer");
		DwgLayerControl lyr = (DwgLayerControl) dwgObj;
		int bitPos = offset;
		
		List val = DwgUtil.getBitLong(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		int numReactors = ((Integer) val.get(1)).intValue();
		lyr.setNumReactors(numReactors);
		
		val = DwgUtil.getBitShort(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		int ne = ((Integer) val.get(1)).intValue();
		
		DwgHandleReference handle = new DwgHandleReference();
		bitPos = handle.read(data, bitPos);
		lyr.setNullHandle(handle);
		
		handle = new DwgHandleReference();
		bitPos = handle.read(data, bitPos);
		lyr.setXDicObjHandle(handle);
		
		if(ne > 0){
			List handles = new ArrayList();
			for (int i = 0; i < ne; i++){
				handle = new DwgHandleReference();
				bitPos = handle.read(data, bitPos);
				handles.add(handle);
			}
			lyr.setCode2Handles(handles);
		}
	}
}
