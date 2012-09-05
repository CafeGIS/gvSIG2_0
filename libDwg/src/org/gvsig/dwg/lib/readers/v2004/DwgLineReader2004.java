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
import org.gvsig.dwg.lib.objects.DwgLine;


/**
 * @author alzabord
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DwgLineReader2004 extends AbstractDwg2004Reader{

	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.readers.IDwgObjectReader#readSpecificObj(int[], int, com.iver.cit.jdwglib.dwg.DwgObject)
	 */
	public void readSpecificObj(int[] data, int offset, DwgObject dwgObj) throws RuntimeException, CorruptedDwgEntityException {
		if(! (dwgObj instanceof DwgLine))
			throw new RuntimeException("LineReader 2004 solo puede leer DwgLine");
		DwgLine line = (DwgLine) dwgObj;
		line.inserta();

		int bitPos = offset;
		ArrayList v;

		bitPos = readObjectHeader(data, bitPos, line);

		v = DwgUtil.testBit(data, bitPos);
		bitPos = ((Integer)v.get(0)).intValue();
		line.setZflag(((Boolean)v.get(1)).booleanValue());

		v = DwgUtil.getRawDouble(data, bitPos);
		bitPos = ((Integer)v.get(0)).intValue();
		double x1 = ((Double)v.get(1)).doubleValue();
		v = DwgUtil.getDefaultDouble(data, bitPos, x1);
		bitPos = ((Integer)v.get(0)).intValue();
		double x2 = ((Double)v.get(1)).doubleValue();
		v = DwgUtil.getRawDouble(data, bitPos);
		bitPos = ((Integer)v.get(0)).intValue();
		double y1 = ((Double)v.get(1)).doubleValue();
		v = DwgUtil.getDefaultDouble(data, bitPos, y1);
		bitPos = ((Integer)v.get(0)).intValue();
		double y2 = ((Double)v.get(1)).doubleValue();
		double[] p1;
		double[] p2;
	    if (!line.isZflag()) {
			v = DwgUtil.getRawDouble(data, bitPos);
			bitPos = ((Integer)v.get(0)).intValue();
			double z1 = ((Double)v.get(1)).doubleValue();
			v = DwgUtil.getDefaultDouble(data, bitPos, z1);
			bitPos = ((Integer)v.get(0)).intValue();
			double z2 = ((Double)v.get(1)).doubleValue();
			p1 = new double[]{x1, y1, z1};
			p2 = new double[]{x2, y2, z2};
		} else {
			p1 = new double[]{x1, y1, 0d};
			p2 = new double[]{x2, y2, 0d};
		}
	    line.setP1(p1);
	    line.setP2(p2);

		v = DwgUtil.testBit(data, bitPos);
		bitPos = ((Integer)v.get(0)).intValue();
		boolean flag = ((Boolean)v.get(1)).booleanValue();
	    double val;
		if (flag) {
			val=0.0;
		} else {
			v = DwgUtil.getBitDouble(data, bitPos);
			bitPos = ((Integer)v.get(0)).intValue();
			val = ((Double)v.get(1)).doubleValue();
		}
		line.setThickness(val);

		v = DwgUtil.testBit(data, bitPos);
		bitPos = ((Integer)v.get(0)).intValue();
		flag = ((Boolean)v.get(1)).booleanValue();
		double x, y, z;
	    if (flag) {
			 x = y = 0.0;
			 z = 1.0;
		} else {
			v = DwgUtil.getBitDouble(data, bitPos);
			bitPos = ((Integer)v.get(0)).intValue();
			x = ((Double)v.get(1)).doubleValue();
			v = DwgUtil.getBitDouble(data, bitPos);
			bitPos = ((Integer)v.get(0)).intValue();
			y = ((Double)v.get(1)).doubleValue();
			v = DwgUtil.getBitDouble(data, bitPos);
			bitPos = ((Integer)v.get(0)).intValue();
			z = ((Double)v.get(1)).doubleValue();
		}
		double[] coord = new double[]{x, y, z};
		line.setExtrusion(coord);
		if(line.getAvanzar()) bitPos++;
		bitPos = readObjectTailer(data, bitPos, line);
	}

}
