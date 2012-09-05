/*
 * Created on 25-ene-2007 by azabala
 *
 */
package org.gvsig.dwg.lib.readers.v1314;

import java.awt.geom.Point2D;
import java.util.List;

import org.gvsig.dwg.lib.CorruptedDwgEntityException;
import org.gvsig.dwg.lib.DwgHandleReference;
import org.gvsig.dwg.lib.DwgObject;
import org.gvsig.dwg.lib.DwgUtil;
import org.gvsig.dwg.lib.objects.DwgAttrib;


/**
 * @author alzabord
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DwgAttribReader1314 extends AbstractDwg1314Reader {

	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.readers.IDwgObjectReader#readSpecificObj(int[], int, com.iver.cit.jdwglib.dwg.DwgObject)
	 */
	public void readSpecificObj(int[] data, int offset, DwgObject dwgObj) throws RuntimeException, CorruptedDwgEntityException {
		
		 if(! (dwgObj instanceof DwgAttrib))
		    	throw new RuntimeException("ArcReader 14 solo puede leer DwgAtrib");
		DwgAttrib att = (DwgAttrib) dwgObj;
		int bitPos = offset;
		bitPos = headTailReader.readObjectHeader(data, offset, att);
		
		List val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		double elevation = ((Double)val.get(1)).doubleValue();
		att.setElevation(elevation);
		
		val = DwgUtil.getRawDouble(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		double x = ((Double)val.get(1)).doubleValue();
		
		val = DwgUtil.getRawDouble(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		double y = ((Double)val.get(1)).doubleValue();
		att.setInsertionPoint(new Point2D.Double(x,y));
		
		val = DwgUtil.getRawDouble(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		x = ((Double)val.get(1)).doubleValue();
		
		val = DwgUtil.getRawDouble(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		y = ((Double)val.get(1)).doubleValue();
		att.setAlignmentPoint(new Point2D.Double(x,y));
		
		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		x = ((Double)val.get(1)).doubleValue();
		
		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		y = ((Double)val.get(1)).doubleValue();
		
		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		double z = ((Double)val.get(1)).doubleValue();
		att.setExtrusion(new double[]{x, y, z});
		
		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		double thickness = ((Double)val.get(1)).doubleValue();
		att.setThickness(thickness);
		
		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		double obliqueAngle = ((Double)val.get(1)).doubleValue();
		att.setObliqueAngle(obliqueAngle);
		
		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		double rotationAngle = ((Double)val.get(1)).doubleValue();
		att.setRotationAngle(rotationAngle);
		
		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		double height = ((Double)val.get(1)).doubleValue();
		att.setHeight(height);
		
		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		double width = ((Double)val.get(1)).doubleValue();
		att.setWidthFactor(width);
		
		val = DwgUtil.getTextString(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		String text = (String)val.get(1);
		att.setText(text);
		
		val = DwgUtil.getBitShort(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		int generation = ((Integer)val.get(1)).intValue();
		att.setGeneration(generation);
		
		val = DwgUtil.getBitShort(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		int halign = ((Integer)val.get(1)).intValue();
		att.setHalign(halign);
		
		val = DwgUtil.getBitShort(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		int valign = ((Integer)val.get(1)).intValue();
		att.setValign(valign);
		
		val = DwgUtil.getTextString(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		String tag = (String)val.get(1);
		att.setTag(tag);
		
		val = DwgUtil.getBitShort(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		int fieldLength = ((Integer)val.get(1)).intValue();
		att.setFieldLength(fieldLength);
		
		val = DwgUtil.getRawChar(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		int flags = ((Integer)val.get(1)).intValue();
		att.setFlags(flags);
		
		bitPos = headTailReader.readObjectTailer(data, bitPos, att);
		DwgHandleReference handle = new DwgHandleReference();
		bitPos = handle.read(data, bitPos);
		att.setStyleHandle(handle);
		
	}
}
