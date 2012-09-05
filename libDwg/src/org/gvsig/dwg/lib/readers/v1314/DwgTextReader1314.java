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
import org.gvsig.dwg.lib.objects.DwgText;


/**
 * It reads Text of dwg 13-14 version
 * @author azabala
 */
public class DwgTextReader1314 extends AbstractDwg1314Reader {
	
	

	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.readers.IDwgObjectReader#readSpecificObj(int[], int, com.iver.cit.jdwglib.dwg.DwgObject)
	 */
	public void readSpecificObj(int[] data, int offset, DwgObject dwgObj) throws RuntimeException, CorruptedDwgEntityException {
		
		 if(! (dwgObj instanceof DwgText))
	    	throw new RuntimeException("ArcReader 14 solo puede leer DwgHatch");
		 DwgText txt = (DwgText) dwgObj;
		
		int bitPos = offset;
		bitPos = headTailReader.readObjectHeader(data, bitPos, dwgObj);
		
		List val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		double elevation = ((Double)val.get(1)).doubleValue();
		txt.setElevation(elevation);
		
		val = DwgUtil.getRawDouble(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		double x = ((Double)val.get(1)).doubleValue();
		
		val = DwgUtil.getRawDouble(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		double y = ((Double)val.get(1)).doubleValue();
		txt.setInsertionPoint(new Point2D.Double(x, y));
		
		val = DwgUtil.getRawDouble(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		x = ((Double)val.get(1)).doubleValue();
		
		val = DwgUtil.getRawDouble(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		y = ((Double)val.get(1)).doubleValue();
		txt.setAlignmentPoint(new Point2D.Double(x,y));
		
		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		x = ((Double)val.get(1)).doubleValue();
		
		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		y = ((Double)val.get(1)).doubleValue();
		
		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		double z = ((Double)val.get(1)).doubleValue();
		txt.setExtrusion(new double[]{x, y, z});
		
		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		double thickness = ((Double)val.get(1)).doubleValue();
		txt.setThickness(thickness);
		
		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		double obliqueAngle = ((Double)val.get(1)).doubleValue();
		txt.setObliqueAngle(obliqueAngle);
		
		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		double rotationAngle = ((Double)val.get(1)).doubleValue();
		txt.setRotationAngle(rotationAngle);
		
		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		double height = ((Double)val.get(1)).doubleValue();
		txt.setHeight(height);
		
		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		double width = ((Double)val.get(1)).doubleValue();
		txt.setWidthFactor(width);
		
		val = DwgUtil.getTextString(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		String text = (String)val.get(1);
		txt.setText(text);
		
		val = DwgUtil.getBitShort(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		int generation = ((Integer)val.get(1)).intValue();
		txt.setGeneration(generation);
		
		val = DwgUtil.getBitShort(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		int halign = ((Integer)val.get(1)).intValue();
		txt.setHalign(halign);
		
		val = DwgUtil.getBitShort(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		int valign = ((Integer)val.get(1)).intValue();
		txt.setValign(valign);
		
		bitPos = headTailReader.readObjectTailer(data, bitPos, txt);
		
		DwgHandleReference styleHdl = new DwgHandleReference();
		styleHdl.read(data, bitPos);
		txt.setStyleHandle(styleHdl);
	}

}
