/*
 * Created on 25-ene-2007 by azabala
 *
 */
package org.gvsig.dwg.lib.readers.v1314;

import java.util.List;

import org.gvsig.dwg.lib.CorruptedDwgEntityException;
import org.gvsig.dwg.lib.DwgObject;
import org.gvsig.dwg.lib.DwgUtil;
import org.gvsig.dwg.lib.objects.DwgSolid;


/**
 * @author alzabord
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DwgSolidReader1314 extends AbstractDwg1314Reader{

	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.readers.IDwgObjectReader#readSpecificObj(int[], int, com.iver.cit.jdwglib.dwg.DwgObject)
	 */
	public void readSpecificObj(int[] data, int offset, DwgObject dwgObj) throws RuntimeException, CorruptedDwgEntityException {
		if(! (dwgObj instanceof DwgSolid))
	    	throw new RuntimeException("ArcReader 14 solo puede leer DwgSeqEnd");
		DwgSolid s = (DwgSolid)dwgObj;
		int bitPos = offset;
		bitPos = headTailReader.readObjectHeader(data, offset, dwgObj );
		
		List val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		double thickness = ((Double) val.get(1)).doubleValue(); 
		s.setThickness(thickness);
		
		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		double elevation = ((Double) val.get(1)).doubleValue(); 
		s.setElevation(elevation);
		
		val = DwgUtil.getRawDouble(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		double x = ((Double) val.get(1)).doubleValue(); 
		
		val = DwgUtil.getRawDouble(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		double y = ((Double) val.get(1)).doubleValue(); 
		s.setCorner1(new double[]{x, y, elevation});
		
		val = DwgUtil.getRawDouble(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		x = ((Double) val.get(1)).doubleValue(); 
		
		val = DwgUtil.getRawDouble(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		y = ((Double) val.get(1)).doubleValue(); 
		s.setCorner2(new double[]{x, y, elevation});
		
		val = DwgUtil.getRawDouble(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		x = ((Double) val.get(1)).doubleValue(); 
		
		val = DwgUtil.getRawDouble(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		y = ((Double) val.get(1)).doubleValue(); 
		s.setCorner3(new double[]{x, y, elevation});
		
		val = DwgUtil.getRawDouble(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		x = ((Double) val.get(1)).doubleValue(); 
		
		val = DwgUtil.getRawDouble(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		y = ((Double) val.get(1)).doubleValue(); 
		s.setCorner4(new double[]{x, y, elevation});
		
		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		x = ((Double) val.get(1)).doubleValue(); 
		
		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		y = ((Double) val.get(1)).doubleValue(); 
		s.setCorner3(new double[]{x, y, elevation});
		
		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		double z = ((Double) val.get(1)).doubleValue(); 
		s.setExtrusion(new double[]{x, y, z});
		
		bitPos = headTailReader.readObjectTailer(data, bitPos, s);
	}

}
