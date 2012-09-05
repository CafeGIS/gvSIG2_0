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
import org.gvsig.dwg.lib.objects.DwgLayer;


/**
 * @author alzabord
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DwgLayerReader15 extends AbstractDwg15Reader{

	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.readers.IDwgObjectReader#readSpecificObj(int[], int, com.iver.cit.jdwglib.dwg.DwgObject)
	 */
	public void readSpecificObj(int[] data, int offset, DwgObject dwgObj) throws RuntimeException, CorruptedDwgEntityException {
		if(! (dwgObj instanceof DwgLayer))
			throw new RuntimeException("LayerReader 15 solo puede leer DwgLayer");
		//System.out.println("DwgLayer.readDwgLayerV15: offset = "+offset);
		DwgLayer lyr = (DwgLayer) dwgObj;
		int bitPos = offset;
		ArrayList v = DwgUtil.getBitLong(data, bitPos);
		bitPos = ((Integer)v.get(0)).intValue();
		int numReactors = ((Integer)v.get(1)).intValue();
		//System.out.println("DwgLayer.readDwgLayerV15: numReactors = "+numReactors);
		lyr.setNumReactors(numReactors);
		v = DwgUtil.getTextString(data, bitPos);
		bitPos = ((Integer)v.get(0)).intValue();
		String name = (String)v.get(1);
		//System.out.println("DwgLayer.readDwgLayerV15: name = "+name);
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
		v = DwgUtil.getBitShort(data, bitPos);
		bitPos = ((Integer)v.get(0)).intValue();
		int color = ((Integer)v.get(1)).intValue();
		lyr.setColor(color);
		
		DwgHandleReference hr = new DwgHandleReference();
		bitPos = hr.read(data, bitPos);
		lyr.setLayerControlHandle(hr);
		
	    //System.out.println("DwgLayer.readDwgLayerV15: Layer Control: " + layerControlHandle);
		
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
		DwgHandleReference xDicObjHandle = new DwgHandleReference();
		bitPos = xDicObjHandle.read(data, bitPos);
		lyr.setXDicObjHandle(xDicObjHandle);
	    //System.out.println("DwgLayer.readDwgLayerV15: xdicobjhandle:" + xdicobjhandle);

		DwgHandleReference nullHandle = new DwgHandleReference();
		bitPos = nullHandle.read(data, bitPos);
		lyr.setNullHandle(nullHandle);

		DwgHandleReference plotStyleHandle = new DwgHandleReference();
		bitPos = plotStyleHandle.read(data, bitPos);
		lyr.setPlotStyleHandle(plotStyleHandle);
			
	    //System.out.println("DwgLayer.readDwgLayerV15: Plotstyle: " + plotstyleHandle);
		DwgHandleReference lineTypeHandle = new DwgHandleReference();
		bitPos = lineTypeHandle.read(data, bitPos);
		lyr.setLineTypeHandle(lineTypeHandle);
	    //System.out.println("DwgLayer.readDwgLayerV15: Linetype: " + linetypeHandle);
	    
	}

}
