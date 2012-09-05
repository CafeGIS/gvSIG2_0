/*
 * Created on 25-ene-2007 by azabala
 *
 */
package org.gvsig.dwg.lib.readers.v1314;

import java.util.List;

import org.gvsig.dwg.lib.CorruptedDwgEntityException;
import org.gvsig.dwg.lib.DwgObject;
import org.gvsig.dwg.lib.DwgUtil;
import org.gvsig.dwg.lib.objects.DwgVertex3D;


/**
 * @author alzabord
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DwgVertex3DReader1314 extends AbstractDwg1314Reader{

	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.readers.IDwgObjectReader#readSpecificObj(int[], int, com.iver.cit.jdwglib.dwg.DwgObject)
	 */
	public void readSpecificObj(int[] data, int offset, DwgObject dwgObj) throws RuntimeException, CorruptedDwgEntityException {
		 if(! (dwgObj instanceof DwgVertex3D))
		    	throw new RuntimeException("ArcReader 14 solo puede leer DwgVertex3D");
		 DwgVertex3D v = (DwgVertex3D) dwgObj;
		 int bitPos = offset;
		 bitPos = headTailReader.readObjectHeader(data, bitPos, v);
		 
		 List val = DwgUtil.getRawChar(data, bitPos); 
		 bitPos = ((Integer) val.get(0)).intValue();
		 int flags = ((Integer) val.get(1)).intValue();
		 v.setFlags(flags);
		 
		 val = DwgUtil.getBitDouble(data, bitPos); 
		 bitPos = ((Integer) val.get(0)).intValue();
		 double x = ((Double) val.get(1)).doubleValue();
		 
		 val = DwgUtil.getBitDouble(data, bitPos); 
		 bitPos = ((Integer) val.get(0)).intValue();
		 double y = ((Double) val.get(1)).doubleValue();
		 
		 val = DwgUtil.getBitDouble(data, bitPos); 
		 bitPos = ((Integer) val.get(0)).intValue();
		 double z = ((Double) val.get(1)).doubleValue();
		 v.setPoint(new double[]{x, y, z});
		 
		 bitPos = headTailReader.readObjectTailer(data, bitPos, v);
	}

}
