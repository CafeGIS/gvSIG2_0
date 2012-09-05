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
import org.gvsig.dwg.lib.objects.DwgInsert;


/**
 * @author alzabord
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DwgInsertReader15 extends AbstractDwg15Reader{

	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.readers.IDwgObjectReader#readSpecificObj(int[], int, com.iver.cit.jdwglib.dwg.DwgObject)
	 */
	public void readSpecificObj(int[] data, int offset, DwgObject dwgObj) throws RuntimeException, CorruptedDwgEntityException {
		if(! (dwgObj instanceof DwgInsert))
			throw new RuntimeException("ArcReader 15 solo puede leer DwgInsert");
		DwgInsert ins = (DwgInsert) dwgObj;
		int bitPos = offset;
		bitPos = headTailReader.readObjectHeader(data, bitPos, ins);
		ArrayList v = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer)v.get(0)).intValue();
		double x = ((Double)v.get(1)).doubleValue();
		v = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer)v.get(0)).intValue();
		double y = ((Double)v.get(1)).doubleValue();
		v = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer)v.get(0)).intValue();
		double z = ((Double)v.get(1)).doubleValue();
		double[] coord = new double[]{x, y, z};
		ins.setInsertionPoint(coord);
		int dflag = ((Integer)DwgUtil.getBits(data, 2, bitPos)).intValue();
		bitPos = bitPos + 2;
		if (dflag==0x0) {
			v = DwgUtil.getRawDouble(data, bitPos);
			bitPos = ((Integer)v.get(0)).intValue();
			x = ((Double)v.get(1)).doubleValue();
			v = DwgUtil.getDefaultDouble(data, bitPos, x);
			bitPos = ((Integer)v.get(0)).intValue();
			y = ((Double)v.get(1)).doubleValue();
			v = DwgUtil.getDefaultDouble(data, bitPos, x);
			bitPos = ((Integer)v.get(0)).intValue();
			z = ((Double)v.get(1)).doubleValue();
		} else if (dflag==0x1) {
			x = 1.0;
			v = DwgUtil.getDefaultDouble(data, bitPos, x);
			bitPos = ((Integer)v.get(0)).intValue();
			y = ((Double)v.get(1)).doubleValue();
			v = DwgUtil.getDefaultDouble(data, bitPos, x);
			bitPos = ((Integer)v.get(0)).intValue();
			z = ((Double)v.get(1)).doubleValue();
		} else if (dflag==0x2) {
			v = DwgUtil.getRawDouble(data, bitPos);
			bitPos = ((Integer)v.get(0)).intValue();
			x = ((Double)v.get(1)).doubleValue();
			z = x;
			y = z;
		} else {
			z = 1.0;
			y = z;
			x = y;
		}
		coord = new double[]{x, y, z};
		ins.setScale(coord);
		v = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer)v.get(0)).intValue();
		double rot = ((Double)v.get(1)).doubleValue();
		ins.setRotation(rot);
		v = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer)v.get(0)).intValue();
		x = ((Double)v.get(1)).doubleValue();
		v = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer)v.get(0)).intValue();
		y = ((Double)v.get(1)).doubleValue();
		v = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer)v.get(0)).intValue();
		z = ((Double)v.get(1)).doubleValue();
		coord = new double[]{x, y, z};
		ins.setExtrusion(coord);
		v = DwgUtil.testBit(data, bitPos);
		bitPos = ((Integer)v.get(0)).intValue();
		boolean hasattr = ((Boolean)v.get(1)).booleanValue();
		bitPos = headTailReader.readObjectTailer(data, bitPos, ins);
		
		DwgHandleReference hr = new DwgHandleReference();
		bitPos = hr.read(data, bitPos);
		ins.setBlockHeaderHandle(hr);
		
		if (hasattr) {
			
			hr = new DwgHandleReference();
			bitPos = hr.read(data, bitPos);
			ins.setFirstAttribHandle(hr);
			
			hr = new DwgHandleReference();
			bitPos = hr.read(data, bitPos);
			ins.setLastAttribHandle(hr);
			
			hr = new DwgHandleReference();
			bitPos = hr.read(data, bitPos);
			ins.setSeqendHandle(hr);
		}
	}

	

}
