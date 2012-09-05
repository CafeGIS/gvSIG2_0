/*
 * Created on 25-ene-2007 by azabala
 *
 */
package org.gvsig.dwg.lib.readers.v1314;

import java.util.List;
import java.util.Vector;

import org.gvsig.dwg.lib.CorruptedDwgEntityException;
import org.gvsig.dwg.lib.DwgHandleReference;
import org.gvsig.dwg.lib.DwgObject;
import org.gvsig.dwg.lib.DwgUtil;
import org.gvsig.dwg.lib.objects.DwgBlockControl;


/**
 * @author alzabord
 */
public class DwgBlockControlReader1314 extends AbstractDwg1314Reader{

	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.readers.IDwgObjectReader#readSpecificObj(int[], int, com.iver.cit.jdwglib.dwg.DwgObject)
	 */
	public void readSpecificObj(int[] data, int offset, DwgObject dwgObj) throws RuntimeException, CorruptedDwgEntityException {
		 if(! (dwgObj instanceof DwgBlockControl))
		    	throw new RuntimeException("ArcReader 14 solo puede leer DwgBlockControl");
		 DwgBlockControl blk = (DwgBlockControl) dwgObj;
		 int bitPos = offset;
		 
		 List val = DwgUtil.getBitLong(data, bitPos);
		 bitPos = ((Integer) val.get(0)).intValue();
		 int numReactors = ((Integer) val.get(1)).intValue();
		 blk.setNumReactors(numReactors);
		 
		 val = DwgUtil.getBitShort(data, bitPos);
		 bitPos = ((Integer) val.get(0)).intValue();
		 int numHdl = ((Integer) val.get(1)).intValue();
		 
		 DwgHandleReference handle = new DwgHandleReference();
		 bitPos = handle.read(data, bitPos);
		 blk.setNullHandle(handle);
		 
		 handle = new DwgHandleReference();
		 bitPos = handle.read(data, bitPos);
		 blk.setXDicObjHandle(handle);
		 
		 if(numHdl > 0){
			 Vector handles = new Vector();
			 for(int i = 0; i < numHdl; i++){
				 handle = new DwgHandleReference();
				 bitPos = handle.read(data, bitPos);
				 handles.add(handle);
			 }
			 blk.setCode2Handles(handles);
		 }
		 
		 handle = new DwgHandleReference();
		 bitPos = handle.read(data, bitPos);
		 blk.setModelSpaceHandle(handle);
		 
		 handle = new DwgHandleReference();
		 bitPos = handle.read(data, bitPos);
		 blk.setPaperSpaceHandle(handle);
	}
}
