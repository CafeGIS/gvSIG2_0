/*
 * Created on 02-feb-2007 by azabala
 *
 */
package org.gvsig.dwg.lib.readers.v1314;

import java.util.ArrayList;
import java.util.List;

import org.gvsig.dwg.lib.CorruptedDwgEntityException;
import org.gvsig.dwg.lib.DwgHandleReference;
import org.gvsig.dwg.lib.DwgObject;
import org.gvsig.dwg.lib.DwgUtil;
import org.gvsig.dwg.lib.objects.DwgIdBuffer;


/**
 * @author alzabord
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DwgIdBufferReader1314 extends AbstractDwg1314Reader {

	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.readers.IDwgObjectReader#readSpecificObj(int[], int, com.iver.cit.jdwglib.dwg.DwgObject)
	 */
	public void readSpecificObj(int[] data, int offset, DwgObject dwgObj)
			throws RuntimeException, CorruptedDwgEntityException {
		 if(! (dwgObj instanceof DwgIdBuffer))
		    	throw new RuntimeException("ArcReader 14 solo puede leer DwgIdBuffer");
		DwgIdBuffer idB = (DwgIdBuffer) dwgObj;
		int bitPos = offset;
		
		List val = DwgUtil.getBitLong(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		int numReactors = ((Integer) val.get(1)).intValue();
		idB.setNumReactors(numReactors);
		
		val = DwgUtil.getRawChar(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		int rcharVal = ((Integer) val.get(1)).intValue();
		
		val = DwgUtil.getBitLong(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		int nids = ((Integer) val.get(1)).intValue();
		
		DwgHandleReference handle = new DwgHandleReference();
		bitPos = handle.read(data, bitPos);
		idB.setParentHandle(handle);
		
		for(int i = 0; i < numReactors; i++){
			handle = new DwgHandleReference();
			bitPos = handle.read(data, bitPos);
			idB.addReactorHandle(handle);
		}
	
		handle = new DwgHandleReference();
		bitPos = handle.read(data, bitPos);
		idB.setXDicObjHandle(handle);
	    
		if(nids > 0){
			List handles = new ArrayList();
			for(int i = 0; i < nids; i++){
				handle = new DwgHandleReference();
				bitPos = handle.read(data, bitPos);
				handles.add(handle);
			}
			idB.setObjidHandles(handles);
		}
	}

}
