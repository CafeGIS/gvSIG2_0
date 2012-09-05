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
import org.gvsig.dwg.lib.objects.DwgBlockHeader;


/**
 * @author alzabord
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DwgBlockHeaderReader15 extends AbstractDwg15Reader{

	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.readers.IDwgObjectReader#readSpecificObj(int[], int, com.iver.cit.jdwglib.dwg.DwgObject)
	 */
	public void readSpecificObj(int[] data, int offset, DwgObject dwgObj) throws RuntimeException, CorruptedDwgEntityException {
		 if(! (dwgObj instanceof DwgBlockHeader))
		    	throw new RuntimeException("DwgBlockHeaderReader15 solo puede leer DwgBlockControl");
		DwgBlockHeader hdr = (DwgBlockHeader) dwgObj;
		int bitPos = offset;
		ArrayList v = DwgUtil.getBitLong(data, bitPos);
		bitPos = ((Integer)v.get(0)).intValue();
		int numReactors = ((Integer)v.get(1)).intValue();
		hdr.setNumReactors(numReactors);

		v = DwgUtil.getTextString(data, bitPos);
		bitPos = ((Integer)v.get(0)).intValue();
		String name = (String)v.get(1);
		hdr.setName(name);

		v = DwgUtil.testBit(data, bitPos);
		bitPos = ((Integer)v.get(0)).intValue();
		boolean flag = ((Boolean)v.get(1)).booleanValue();
		hdr.setFlag64(flag);

		v = DwgUtil.getBitShort(data, bitPos);
		bitPos = ((Integer)v.get(0)).intValue();
		int xrefplus1 = ((Integer)v.get(1)).intValue();
		hdr.setXRefPlus(xrefplus1);

		v = DwgUtil.testBit(data, bitPos);
		bitPos = ((Integer)v.get(0)).intValue();
		boolean xdep = ((Boolean)v.get(1)).booleanValue();
		hdr.setXdep(xdep);

		v = DwgUtil.testBit(data, bitPos);
		bitPos = ((Integer)v.get(0)).intValue();
		boolean anon = ((Boolean)v.get(1)).booleanValue();
		hdr.setAnonymous(anon);

		v = DwgUtil.testBit(data, bitPos);
		bitPos = ((Integer)v.get(0)).intValue();
		boolean hasatts = ((Boolean)v.get(1)).booleanValue();
		hdr.setHasAttrs(hasatts);

		v = DwgUtil.testBit(data, bitPos);
		bitPos = ((Integer)v.get(0)).intValue();
		boolean bxref = ((Boolean)v.get(1)).booleanValue();
		hdr.setBlkIsXRef(bxref);

		v = DwgUtil.testBit(data, bitPos);
		bitPos = ((Integer)v.get(0)).intValue();
		boolean xover = ((Boolean)v.get(1)).booleanValue();
		hdr.setXRefOverLaid(xover);

		v = DwgUtil.testBit(data, bitPos);
		bitPos = ((Integer)v.get(0)).intValue();
		boolean loaded = ((Boolean)v.get(1)).booleanValue();
		hdr.setLoaded(loaded);

		v = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer)v.get(0)).intValue();
		double bx = ((Double)v.get(1)).doubleValue();

		v = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer)v.get(0)).intValue();
		double by = ((Double)v.get(1)).doubleValue();

		v = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer)v.get(0)).intValue();
		double bz = ((Double)v.get(1)).doubleValue();
		double[] coord = new double[]{bx, by, bz};
		hdr.setBasePoint(coord);

		v = DwgUtil.getTextString(data, bitPos);
		bitPos = ((Integer)v.get(0)).intValue();
		String pname = (String)v.get(1);
		hdr.setXRefPName(pname);
		int icount = 0;
		while (true) {
			v = DwgUtil.getRawChar(data, bitPos);
			bitPos = ((Integer)v.get(0)).intValue();
			int val = ((Integer)v.get(1)).intValue();
			if (val==0) {
				break;
			}
			icount++;
		}

		v = DwgUtil.getTextString(data, bitPos);
		bitPos = ((Integer)v.get(0)).intValue();
		String desc = (String)v.get(1);
		hdr.setBlockDescription(desc);
		v = DwgUtil.getBitLong(data, bitPos);
		bitPos = ((Integer)v.get(0)).intValue();
		int pdsize = ((Integer)v.get(1)).intValue();
		if (pdsize>0) {
			int count = pdsize + icount;
			//int pdata = ((Integer)DwgUtil.getBits(data, count, bitPos)).intValue();
			//previewData = pdata;
			bitPos = bitPos + count;
		}

		DwgHandleReference blkCtrlHdl = new DwgHandleReference();
		bitPos = blkCtrlHdl.read(data, bitPos);
		hdr.setBlockControlHandle(blkCtrlHdl);

		for (int i=0;i<numReactors;i++) {
			DwgHandleReference reactor = new DwgHandleReference();
			bitPos = reactor.read(data, bitPos);

		}

		DwgHandleReference xdicObjHdl = new DwgHandleReference();
		bitPos = xdicObjHdl.read(data, bitPos);
		hdr.setXDicObjHandle(xdicObjHdl);

		DwgHandleReference handle = new DwgHandleReference();
		bitPos = handle.read(data, bitPos);
		hdr.setNullHandle(handle);

		handle = new DwgHandleReference();
		bitPos = handle.read(data, bitPos);
		hdr.setBlockEntityHandle(handle);

		if ((!bxref) && (!xover)) {
			handle = new DwgHandleReference();
			bitPos = handle.read(data, bitPos);
			hdr.setFirstEntityHandle(handle);

			handle = new DwgHandleReference();
			bitPos = handle.read(data, bitPos);
			hdr.setLastEntityHandle(handle);
		}//si el bloque es una referencia externa, es un fichero entero
		//(no ninguna entidad del fichero actual)

		handle = new DwgHandleReference();
		bitPos = handle.read(data, bitPos);
		hdr.setEndBlkEntityHandle(handle);

		if(icount > 0){
			DwgHandleReference[] insertHandles = new
				DwgHandleReference[icount];
			for(int i = 0; i < icount; i++){
				insertHandles[i] = new DwgHandleReference();
				bitPos = insertHandles[i].read(data, bitPos);
			}
			hdr.setInsertHandles(insertHandles);
		}

		handle = new DwgHandleReference();
		bitPos = handle.read(data, bitPos);
		hdr.setLayoutHandle(handle);
	}
}
