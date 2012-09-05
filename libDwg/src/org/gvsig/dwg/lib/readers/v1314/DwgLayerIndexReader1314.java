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
import org.gvsig.dwg.lib.objects.DwgLayerIndex;


/**
 * @author alzabord
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DwgLayerIndexReader1314 extends AbstractDwg1314Reader {

	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.readers.IDwgObjectReader#readSpecificObj(int[], int, com.iver.cit.jdwglib.dwg.DwgObject)
	 */
	public void readSpecificObj(int[] data, int offset, DwgObject dwgObj)
			throws RuntimeException, CorruptedDwgEntityException {
		
		if(! (dwgObj instanceof DwgLayerIndex))
	    	throw new RuntimeException("ArcReader 14 solo puede leer DwgLayer");
		DwgLayerIndex lyr = (DwgLayerIndex) dwgObj;
		int bitPos = offset;
		
		List val = DwgUtil.getBitLong(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		int numReactors = ((Integer) val.get(1)).intValue();
		lyr.setNumReactors(numReactors);
		
		val = DwgUtil.getBitLong(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		int timestamp1 = ((Integer) val.get(1)).intValue();
		lyr.setTimestamp1(timestamp1);
		
		val = DwgUtil.getBitLong(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		int timestamp2 = ((Integer) val.get(1)).intValue();
		lyr.setTimestamp2(timestamp1);
		
		val = DwgUtil.getBitLong(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		int ne = ((Integer) val.get(1)).intValue();
		if(ne > 0){
			for(int i = 0; i < ne; i++){
				val = DwgUtil.getBitLong(data, bitPos);
				bitPos = ((Integer) val.get(0)).intValue();
				int indexLong = ((Integer) val.get(1)).intValue();
				
				val = DwgUtil.getBitLong(data, bitPos);
				bitPos = ((Integer) val.get(0)).intValue();
				String indexStr = (String) val.get(1);
				
				lyr.addIndex(indexLong, indexStr);
			}//for
		}//if
		
		DwgHandleReference handle = new DwgHandleReference();
		bitPos = handle.read(data, bitPos);
		lyr.setParentHandle(handle);
		
		for(int i = 0; i < numReactors; i++){
			handle = new DwgHandleReference();
			bitPos = handle.read(data, bitPos);
			lyr.addReactorHandle(handle);
		}
		
		handle = new DwgHandleReference();
		bitPos = handle.read(data, bitPos);
		lyr.setXDicObjHandle(handle);
		
		if(ne > 0){
			handle = new DwgHandleReference();
			bitPos = handle.read(data, bitPos);
			lyr.addHandleEntry(handle);
		}
	}

}
