/*
 * Created on 02-feb-2007 by azabala
 *
 */
package org.gvsig.dwg.lib.readers.v1314;

import java.util.List;

import org.gvsig.dwg.lib.CorruptedDwgEntityException;
import org.gvsig.dwg.lib.DwgHandleReference;
import org.gvsig.dwg.lib.DwgObject;
import org.gvsig.dwg.lib.DwgUtil;
import org.gvsig.dwg.lib.objects.DwgSortEntStable;


/**
 * @author alzabord
 */
public class DwgSortEntsTableReader1314 extends AbstractDwg1314Reader {

	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.readers.IDwgObjectReader#readSpecificObj(int[], int, com.iver.cit.jdwglib.dwg.DwgObject)
	 */
	public void readSpecificObj(int[] data, int offset, DwgObject dwgObj)
			throws RuntimeException, CorruptedDwgEntityException {
		
		if(! (dwgObj instanceof DwgSortEntStable))
	    	throw new RuntimeException("ArcReader 14 solo puede leer DwgSortEntStable");
		DwgSortEntStable stable = (DwgSortEntStable) dwgObj;
		int bitPos = offset;
		
		List val = DwgUtil.getBitLong(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		int numReactors = ((Integer)val.get(1)).intValue();
		
		val = DwgUtil.getBitLong(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		int ne = ((Integer)val.get(1)).intValue();
		if(ne > 0){
			DwgHandleReference[] handles = new DwgHandleReference[ne];
			for(int i = 0; i < ne; i++){
				handles[i] = new DwgHandleReference();
				bitPos = handles[i].read(data, bitPos);
			}//for
			stable.setSortedHandles(handles);
		}
		
		DwgHandleReference handle = new DwgHandleReference();
		bitPos = handle.read(data, bitPos);
		stable.setParentHandle(handle);
		
		for(int i = 0; i < numReactors; i++){
			handle = new DwgHandleReference();
			bitPos = handle.read(data, bitPos);
			stable.addReactorHandle(handle);
		}
		
		handle = new DwgHandleReference();
		bitPos = handle.read(data, bitPos);
		stable.setXDicObjHandle(handle);
		
		if(ne > 0){
			DwgHandleReference[] handles = new DwgHandleReference[ne];
			for(int i = 0; i < ne; i++){
				handles[i] = new DwgHandleReference();
				bitPos = handles[i].read(data, bitPos);
			}//for
			stable.setObjHandles(handles);
		}
	}

}
