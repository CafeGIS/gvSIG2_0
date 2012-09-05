/*
 * Created on 25-ene-2007 by azabala
 *
 */
package org.gvsig.dwg.lib.readers.v15;

import java.util.ArrayList;

import org.gvsig.dwg.lib.CorruptedDwgEntityException;
import org.gvsig.dwg.lib.DwgObject;
import org.gvsig.dwg.lib.DwgUtil;
import org.gvsig.dwg.lib.objects.DwgArc;


/**
 * @author alzabord
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DwgArcReader15 extends AbstractDwg15Reader{
	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.readers.IDwgObjectReader#readSpecificObj(int[], int, com.iver.cit.jdwglib.dwg.DwgObject)
	 */
	public void readSpecificObj(int[] data, int offset, DwgObject dwgObj) throws RuntimeException, CorruptedDwgEntityException {
		    if(! (dwgObj instanceof DwgArc))
		    	throw new RuntimeException("ArcReader 15 solo puede leer DwgArc");
		    DwgArc arc = (DwgArc) dwgObj;
		    
			int bitPos = offset;
			bitPos = headTailReader.readObjectHeader(data, bitPos, arc);
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
			arc.setCenter(coord);
			v = DwgUtil.getBitDouble(data, bitPos);
			bitPos = ((Integer)v.get(0)).intValue();
			double val = ((Double)v.get(1)).doubleValue();
			arc.setRadius(val);
			v = DwgUtil.testBit(data, bitPos);
			bitPos = ((Integer)v.get(0)).intValue();
			boolean flag = ((Boolean)v.get(1)).booleanValue();
			if (flag) {
				val=0.0;
			} else {
				v = DwgUtil.getBitDouble(data, bitPos);
				bitPos = ((Integer)v.get(0)).intValue();
				val = ((Double)v.get(1)).doubleValue();
			}
			arc.setThickness(val);
			v = DwgUtil.testBit(data, bitPos);
			bitPos = ((Integer)v.get(0)).intValue();
			flag = ((Boolean)v.get(1)).booleanValue();
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
			coord = new double[]{x, y, z};
			arc.setExtrusion(coord);
			v = DwgUtil.getBitDouble(data, bitPos);
			bitPos = ((Integer)v.get(0)).intValue();
			val = ((Double)v.get(1)).doubleValue();
			arc.setInitAngle(val);
			v = DwgUtil.getBitDouble(data, bitPos);
			bitPos = ((Integer)v.get(0)).intValue();
			val = ((Double)v.get(1)).doubleValue();
			arc.setEndAngle(val);
			bitPos = this.headTailReader.readObjectTailer(data, bitPos, arc);
		}
	

	
}
