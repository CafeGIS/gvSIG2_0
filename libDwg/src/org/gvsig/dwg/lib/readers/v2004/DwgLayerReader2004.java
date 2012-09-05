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
import org.gvsig.dwg.lib.objects.DwgLayer;


/**
 * @author alzabord
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DwgLayerReader2004 extends AbstractDwg2004Reader{

	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.readers.IDwgObjectReader#readSpecificObj(int[], int, com.iver.cit.jdwglib.dwg.DwgObject)
	 */
	public void readSpecificObj(int[] data, int offset, DwgObject dwgObj) throws RuntimeException, CorruptedDwgEntityException {
		if(! (dwgObj instanceof DwgLayer))
			throw new RuntimeException("LayerReader 2004 solo puede leer DwgLayer");

		DwgLayer lyr = (DwgLayer) dwgObj;
		int bitPos = offset;
		ArrayList v;

		v = DwgUtil.getBitLong(data, bitPos);
		bitPos = ((Integer)v.get(0)).intValue();
		int numReactors = ((Integer)v.get(1)).intValue();
		lyr.setNumReactors(numReactors);

		v = DwgUtil.testBit(data, bitPos);
		bitPos = ((Integer)v.get(0)).intValue();
		boolean XdicFlag = ((Boolean) v.get(1)).booleanValue();

		v = DwgUtil.getTextString(data, bitPos);
		bitPos = ((Integer)v.get(0)).intValue();
		String name = (String)v.get(1);
		lyr.setName(name);

		v = DwgUtil.testBit(data, bitPos);
		bitPos = ((Integer)v.get(0)).intValue();
		boolean flag = ((Boolean)v.get(1)).booleanValue();
		lyr.setFlag64(flag);

		v = DwgUtil.getBitShort(data, bitPos);
		bitPos = ((Integer)v.get(0)).intValue();
		int xrefplus1 = ((Integer)v.get(1)).intValue();
		lyr.setXRefPlus(xrefplus1);

		v = DwgUtil.testBit(data, bitPos);
		bitPos = ((Integer)v.get(0)).intValue();
		boolean xdep = ((Boolean)v.get(1)).booleanValue();
		lyr.setXdep(xdep);

		v = DwgUtil.getBitShort(data, bitPos);
		bitPos = ((Integer)v.get(0)).intValue();
		int flags = ((Integer)v.get(1)).intValue();
		lyr.setFlags(flags);

//		v = DwgUtil.getBitShort(data, bitPos);
//		bitPos = ((Integer)v.get(0)).intValue();
//		int color = ((Integer)v.get(1)).intValue();
//		lyr.setColor(color);

		//FIXME: De momento solo guardo el indice del color por compatibilidad.
		// hay que ver qué debemos hacer con el color
		v = DwgUtil.getCmColor(data, bitPos, lyr.getVersion());
		bitPos = ((Integer)v.get(0)).intValue();
		int color = ((Integer)v.get(1)).intValue();
		lyr.setColor(color);


		DwgHandleReference hr = new DwgHandleReference();
		bitPos = hr.read(data, bitPos);
		lyr.setLayerControlHandle(hr);

		lyr.inserta();
		/*
		 * Reactors handles
		 * DwgObject
		 */
		DwgHandleReference reactorHandle;
		for (int i = 0; i < lyr.getNumReactors(); i++) {
			reactorHandle = new DwgHandleReference();
			bitPos = reactorHandle.read(data, bitPos);
			lyr.addReactorHandle(reactorHandle);
		}

		/*
		 * XDICOBJHANDLE
		 */
		if(!XdicFlag){
			DwgHandleReference xDicObjHandle = new DwgHandleReference();
			bitPos = xDicObjHandle.read(data, bitPos);
			lyr.setXDicObjHandle(xDicObjHandle);
		}

		DwgHandleReference nullHandle = new DwgHandleReference();
		bitPos = nullHandle.read(data, bitPos);
		lyr.setNullHandle(nullHandle);

		DwgHandleReference plotStyleHandle = new DwgHandleReference();
		bitPos = plotStyleHandle.read(data, bitPos);
		lyr.setPlotStyleHandle(plotStyleHandle);


		DwgHandleReference lineTypeHandle = new DwgHandleReference();
		bitPos = lineTypeHandle.read(data, bitPos);
		lyr.setLineTypeHandle(lineTypeHandle);


	}

}
