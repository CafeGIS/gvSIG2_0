/*
 * Created on 25-ene-2007 by azabala
 *
 */
package org.gvsig.dwg.lib.readers.v15;

import java.util.ArrayList;
import java.util.Vector;

import org.gvsig.dwg.lib.CorruptedDwgEntityException;
import org.gvsig.dwg.lib.DwgHandleReference;
import org.gvsig.dwg.lib.DwgObject;
import org.gvsig.dwg.lib.DwgUtil;
import org.gvsig.dwg.lib.objects.DwgBlockControl;


/**
 * @author azabala
 */
public class DwgBlockControlReader15 extends AbstractDwg15Reader{

	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.readers.IDwgObjectReader#readSpecificObj(int[], int, com.iver.cit.jdwglib.dwg.DwgObject)
	 */
	public void readSpecificObj(int[] data, int offset, DwgObject dwgObj) throws RuntimeException, CorruptedDwgEntityException{
		
		 if(! (dwgObj instanceof DwgBlockControl))
		    	throw new RuntimeException("ArcReader 15 solo puede leer DwgBlockControl");
		    DwgBlockControl blk = (DwgBlockControl) dwgObj;
			int bitPos = offset;
			ArrayList v = DwgUtil.getBitLong(data, bitPos);
			bitPos = ((Integer)v.get(0)).intValue();
			int numReactors = ((Integer)v.get(1)).intValue();
			blk.setNumReactors(numReactors);
			
			v = DwgUtil.getBitShort(data, bitPos);
			bitPos = ((Integer)v.get(0)).intValue();
			int enumeration = ((Integer)v.get(1)).intValue();
			
			DwgHandleReference hr = new DwgHandleReference();
			bitPos = hr.read(data, bitPos);
			blk.setNullHandle(hr);
			
			hr = new DwgHandleReference();
			bitPos = hr.read(data, bitPos);
			blk.setXDicObjHandle(hr);
		    
			if (enumeration>0) {
				Vector handles = new Vector();
				for (int i=0;i<enumeration;i++) {
					hr = new DwgHandleReference();
					bitPos = hr.read(data, bitPos);
					handles.add(hr);
				}
				blk.setCode2Handles(handles);
			}
			
			hr = new DwgHandleReference();
			bitPos = hr.read(data, bitPos);
			blk.setModelSpaceHandle(hr);

			hr = new DwgHandleReference();
			bitPos = hr.read(data, bitPos);
			blk.setPaperSpaceHandle(hr);
		}
}
