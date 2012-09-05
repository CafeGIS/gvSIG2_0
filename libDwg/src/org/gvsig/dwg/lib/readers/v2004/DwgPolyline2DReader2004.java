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
import org.gvsig.dwg.lib.objects.DwgPolyline2D;


/**
 * @author alzabord
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DwgPolyline2DReader2004 extends AbstractDwg2004Reader{

	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.readers.IDwgObjectReader#readSpecificObj(int[], int, com.iver.cit.jdwglib.dwg.DwgObject)
	 */
	public void readSpecificObj(int[] data, int offset, DwgObject dwgObj) throws RuntimeException, CorruptedDwgEntityException {
		if(! (dwgObj instanceof DwgPolyline2D))
			throw new RuntimeException(this.getClass().getName()+" solo puede leer DwgPolyline2D");
		DwgPolyline2D ln = (DwgPolyline2D) dwgObj;
		int bitPos = offset;
		boolean dontRead = false;
		ArrayList v;


		bitPos = readObjectHeader(data, bitPos, ln);

		v = DwgUtil.getBitShort(data, bitPos);
		bitPos = ((Integer)v.get(0)).intValue();
		int flags = ((Integer)v.get(1)).intValue();
		ln.setFlags(flags);

		v = DwgUtil.getBitShort(data, bitPos);
		bitPos = ((Integer)v.get(0)).intValue();
		int ctype = ((Integer)v.get(1)).intValue();
		ln.setCurveType(ctype);

		v = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer)v.get(0)).intValue();
		double sw = ((Double)v.get(1)).doubleValue();
		ln.setInitWidth(sw);

		v = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer)v.get(0)).intValue();
		double ew = ((Double)v.get(1)).doubleValue();
		ln.setEndWidth(ew);

		v = DwgUtil.testBit(data, bitPos);
		bitPos = ((Integer)v.get(0)).intValue();
		boolean flag = ((Boolean)v.get(1)).booleanValue();
	    double th = 0.0;
	    if (!flag) {
			v = DwgUtil.getBitDouble(data, bitPos);
			bitPos = ((Integer)v.get(0)).intValue();
			th = ((Double)v.get(1)).doubleValue();
	    }
	    ln.setThickness(th);

		v = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer)v.get(0)).intValue();
		double elev = ((Double)v.get(1)).doubleValue();
		ln.setElevation(elev);

		v = DwgUtil.testBit(data, bitPos);
		bitPos = ((Integer)v.get(0)).intValue();
		flag = ((Boolean)v.get(1)).booleanValue();
	    double ex, ey, ez = 0.0;
	    if (flag) {
	    	ex = 0.0;
	    	ey = 0.0;
	    	ez = 1.0;
	    } else {
			v = DwgUtil.getBitDouble(data, bitPos);
			bitPos = ((Integer)v.get(0)).intValue();
			ex = ((Double)v.get(1)).doubleValue();
			v = DwgUtil.getBitDouble(data, bitPos);
			bitPos = ((Integer)v.get(0)).intValue();
			ey = ((Double)v.get(1)).doubleValue();
			v = DwgUtil.getBitDouble(data, bitPos);
			bitPos = ((Integer)v.get(0)).intValue();
			ez = ((Double)v.get(1)).doubleValue();
	    }
	    ln.setExtrusion(new double[]{ex, ey, ez});

	    v = DwgUtil.getBitLong(data, bitPos); //OWNED OBJECT COUNT
		bitPos = ((Integer)v.get(0)).intValue();
		int OwnedObj = ((Integer)v.get(1)).intValue();

	    bitPos = readObjectTailer(data, bitPos, ln);

		DwgHandleReference hr;
		if (OwnedObj>0) {
			for (int i=0;i<OwnedObj;i++) {
				hr = new DwgHandleReference();
				bitPos = hr.read(data, bitPos);
				ln.addVertexHandle(hr);
			}
		}

		hr = new DwgHandleReference();
		bitPos = hr.read(data, bitPos);
	    ln.setSeqendHandle(hr);
	    ln.inserta();

	}

}
