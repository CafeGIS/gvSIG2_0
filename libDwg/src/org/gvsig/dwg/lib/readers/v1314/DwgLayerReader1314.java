/*
 * Created on 25-ene-2007 by azabala
 *
 */
package org.gvsig.dwg.lib.readers.v1314;

import java.util.List;

import org.gvsig.dwg.lib.CorruptedDwgEntityException;
import org.gvsig.dwg.lib.DwgHandleReference;
import org.gvsig.dwg.lib.DwgObject;
import org.gvsig.dwg.lib.DwgUtil;
import org.gvsig.dwg.lib.objects.DwgLayer;


/**
 * @author alzabord
 */
public class DwgLayerReader1314 extends AbstractDwg1314Reader{

	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.readers.IDwgObjectReader#readSpecificObj(int[], int, com.iver.cit.jdwglib.dwg.DwgObject)
	 */
	public void readSpecificObj(int[] data, int offset, DwgObject dwgObj) throws RuntimeException, CorruptedDwgEntityException {
		if(! (dwgObj instanceof DwgLayer))
	    	throw new RuntimeException("ArcReader 14 solo puede leer DwgLayer");
		DwgLayer lyr = (DwgLayer) dwgObj;
		int bitPos = offset;
		
		List val = DwgUtil.getBitLong(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		int numReactors = ((Integer) val.get(1)).intValue(); 
		lyr.setNumReactors(numReactors);
		
		val = DwgUtil.getTextString(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		String name = (String) val.get(1); 
		lyr.setName(name);
		
		val = DwgUtil.testBit(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		boolean flag64 = ((Boolean) val.get(1)).booleanValue();
		lyr.setFlag64(flag64);
		
		//TODO Las layers tb pueden ser referencia externa??
		val = DwgUtil.getBitShort(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		int xRefPlus = ((Integer) val.get(1)).intValue();
		lyr.setXRefPlus(xRefPlus);
		
		val = DwgUtil.testBit(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		boolean xdep = ((Boolean) val.get(1)).booleanValue();
		lyr.setXdep(xdep);
		
		val = DwgUtil.testBit(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		boolean frozen = ((Boolean) val.get(1)).booleanValue();
		lyr.setFrozen(frozen);
		
		val = DwgUtil.testBit(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		boolean on = ((Boolean) val.get(1)).booleanValue();
		lyr.setOn(on);
		
		val = DwgUtil.testBit(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		boolean frozenInNew = ((Boolean) val.get(1)).booleanValue();
		lyr.setFrozenInNew(frozenInNew);
		
		val = DwgUtil.testBit(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		boolean locked = ((Boolean) val.get(1)).booleanValue();
		lyr.setLocked(locked);
		
		val = DwgUtil.getBitShort(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		int color = ((Integer) val.get(1)).intValue();
		lyr.setColor(color);
		
		DwgHandleReference handle = new DwgHandleReference();
		bitPos = handle.read(data, bitPos);
		lyr.setLayerControlHandle(handle);
		
		for(int i = 0; i < numReactors; i++){
			handle = new DwgHandleReference();
			bitPos = handle.read(data, bitPos);
			lyr.addReactorHandle(handle);
		}
		
		handle = new DwgHandleReference();
		bitPos = handle.read(data, bitPos);
		lyr.setXDicObjHandle(handle);
		
		handle = new DwgHandleReference();
		bitPos = handle.read(data, bitPos);
		lyr.setNullHandle(handle);
		
		handle = new DwgHandleReference();
		bitPos = handle.read(data, bitPos);
		lyr.setLineTypeHandle(handle);
	}

}
