/*
 * Created on 25-ene-2007 by azabala
 *
 */
package org.gvsig.dwg.lib.readers.v1314;

import java.util.List;

import org.gvsig.dwg.lib.CorruptedDwgEntityException;
import org.gvsig.dwg.lib.DwgHandleReference;
import org.gvsig.dwg.lib.DwgObject;
import org.gvsig.dwg.lib.DwgUtil;
import org.gvsig.dwg.lib.objects.DwgMText;


/**
 * @author alzabord
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DwgMTextReader1314 extends AbstractDwg1314Reader{

	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.readers.IDwgObjectReader#readSpecificObj(int[], int, com.iver.cit.jdwglib.dwg.DwgObject)
	 */
	public void readSpecificObj(int[] data, int offset, DwgObject dwgObj) throws RuntimeException, CorruptedDwgEntityException {
		if(! (dwgObj instanceof DwgMText))
	    	throw new RuntimeException("ArcReader 14 solo puede leer DwgMText");
		DwgMText txt = (DwgMText) dwgObj;
		
		int bitPos = offset;
		bitPos = headTailReader.readObjectHeader(data, bitPos, txt);
		
		List val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		double x = ((Double)val.get(1)).doubleValue();
		
		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		double y = ((Double)val.get(1)).doubleValue();
		
		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		double z = ((Double)val.get(1)).doubleValue();
		txt.setInsertionPoint(new double[]{x, y, z});
		
		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		x = ((Double)val.get(1)).doubleValue();
		
		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		y = ((Double)val.get(1)).doubleValue();
		
		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		z = ((Double)val.get(1)).doubleValue();
		txt.setExtrusion(new double[]{x, y, z});
		
		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		x = ((Double)val.get(1)).doubleValue();
		
		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		y = ((Double)val.get(1)).doubleValue();
		
		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		z = ((Double)val.get(1)).doubleValue();
		txt.setXAxisDirection(new double[]{x, y, z});
		
		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		double width = ((Double)val.get(1)).doubleValue();
		txt.setWidth(width);
		
		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		double height = ((Double)val.get(1)).doubleValue();
		txt.setHeight(height);
		
		val = DwgUtil.getBitShort(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		int attachment = ((Integer)val.get(1)).intValue();
		txt.setAttachment(attachment);
		
		val = DwgUtil.getBitShort(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		int drawingDir = ((Integer)val.get(1)).intValue();
		txt.setAttachment(drawingDir);
		
		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		double extHeight = ((Double)val.get(1)).doubleValue();
		txt.setExtHeight(extHeight);
		
		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		double extWidth = ((Double)val.get(1)).doubleValue();
		txt.setExtWidth(extWidth);
		
		val = DwgUtil.getTextString(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		txt.setText((String) val.get(1));
		
		bitPos = headTailReader.readObjectTailer(data, bitPos, txt);
		
		DwgHandleReference handle = new DwgHandleReference();
		bitPos = handle.read(data, bitPos);
		txt.setStyleHandle(handle);	
	}

}
