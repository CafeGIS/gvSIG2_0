/*
 * Created on 25-ene-2007 by azabala
 *
 */
package org.gvsig.dwg.lib.readers.v15;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.gvsig.dwg.lib.CorruptedDwgEntityException;
import org.gvsig.dwg.lib.DwgHandleReference;
import org.gvsig.dwg.lib.DwgObject;
import org.gvsig.dwg.lib.DwgUtil;
import org.gvsig.dwg.lib.objects.DwgArc;
import org.gvsig.dwg.lib.objects.DwgAttrib;
import org.gvsig.dwg.lib.readers.IDwgFileReader;
import org.gvsig.dwg.lib.readers.IDwgObjectReader;


/**
 * @author alzabord
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class DwgAttribReader15 extends AbstractDwg15Reader {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.iver.cit.jdwglib.dwg.readers.IDwgObjectReader#readSpecificObj(int[],
	 *      int, com.iver.cit.jdwglib.dwg.DwgObject)
	 */
	public void readSpecificObj(int[] data, int offset, DwgObject dwgObj) throws RuntimeException, CorruptedDwgEntityException {

		if (!(dwgObj instanceof DwgAttrib))
			throw new RuntimeException("ArcReader 15 solo puede leer DwgAttrib");
		DwgAttrib att = (DwgAttrib) dwgObj;

		int bitPos = offset;
		bitPos = headTailReader.readObjectHeader(data, bitPos, att);
		ArrayList v = DwgUtil.getRawChar(data, bitPos);
		bitPos = ((Integer) v.get(0)).intValue();
		int dflag = ((Integer) v.get(1)).intValue();
		att.setDataFlag(dflag);
		if ((dflag & 0x1) == 0) {
			v = DwgUtil.getRawDouble(data, bitPos);
			bitPos = ((Integer) v.get(0)).intValue();
			double elev = ((Double) v.get(1)).doubleValue();
			att.setElevation(elev);
		}
		v = DwgUtil.getRawDouble(data, bitPos);
		bitPos = ((Integer) v.get(0)).intValue();
		double x1 = ((Double) v.get(1)).doubleValue();
		v = DwgUtil.getRawDouble(data, bitPos);
		bitPos = ((Integer) v.get(0)).intValue();
		double y1 = ((Double) v.get(1)).doubleValue();
		att.setInsertionPoint(new Point2D.Double(x1, y1));
		double x = 0, y = 0, z = 0;
		if ((dflag & 0x2) == 0) {
			v = DwgUtil.getDefaultDouble(data, bitPos, x1);
			bitPos = ((Integer) v.get(0)).intValue();
			x = ((Double) v.get(1)).doubleValue();
			v = DwgUtil.getDefaultDouble(data, bitPos, y1);
			bitPos = ((Integer) v.get(0)).intValue();
			y = ((Double) v.get(1)).doubleValue();
		}
		att.setAlignmentPoint(new Point2D.Double(x, y));
		v = DwgUtil.testBit(data, bitPos);
		bitPos = ((Integer) v.get(0)).intValue();
		boolean flag = ((Boolean) v.get(1)).booleanValue();
		if (flag) {
			y = 0.0;
			x = y;
			z = 1.0;
		} else {
			v = DwgUtil.getBitDouble(data, bitPos);
			bitPos = ((Integer) v.get(0)).intValue();
			x = ((Double) v.get(1)).doubleValue();
			v = DwgUtil.getBitDouble(data, bitPos);
			bitPos = ((Integer) v.get(0)).intValue();
			y = ((Double) v.get(1)).doubleValue();
			v = DwgUtil.getBitDouble(data, bitPos);
			bitPos = ((Integer) v.get(0)).intValue();
			z = ((Double) v.get(1)).doubleValue();
		}
		att.setExtrusion(new double[] { x, y, z });
		v = DwgUtil.testBit(data, bitPos);
		bitPos = ((Integer) v.get(0)).intValue();
		flag = ((Boolean) v.get(1)).booleanValue();
		double th;
		if (flag) {
			th = 0.0;
		} else {
			v = DwgUtil.getBitDouble(data, bitPos);
			bitPos = ((Integer) v.get(0)).intValue();
			th = ((Double) v.get(1)).doubleValue();
		}
		att.setThickness(th);
		if ((dflag & 0x4) == 0) {
			v = DwgUtil.getRawDouble(data, bitPos);
			bitPos = ((Integer) v.get(0)).intValue();
			double oblique = ((Double) v.get(1)).doubleValue();
			att.setObliqueAngle(oblique);
		}
		if ((dflag & 0x8) == 0) {
			v = DwgUtil.getRawDouble(data, bitPos);
			bitPos = ((Integer) v.get(0)).intValue();
			double rot = ((Double) v.get(1)).doubleValue();
			att.setRotationAngle(rot);
		}
		v = DwgUtil.getRawDouble(data, bitPos);
		bitPos = ((Integer) v.get(0)).intValue();
		double height = ((Double) v.get(1)).doubleValue();
		att.setHeight(height);
		if ((dflag & 0x10) == 0) {
			v = DwgUtil.getRawDouble(data, bitPos);
			bitPos = ((Integer) v.get(0)).intValue();
			double width = ((Double) v.get(1)).doubleValue();
			att.setWidthFactor(width);
		}
		v = DwgUtil.getTextString(data, bitPos);
		bitPos = ((Integer) v.get(0)).intValue();
		String text = (String) v.get(1);
		att.setText(text);
		if ((dflag & 0x20) == 0) {
			v = DwgUtil.getBitShort(data, bitPos);
			bitPos = ((Integer) v.get(0)).intValue();
			int gen = ((Integer) v.get(1)).intValue();
			att.setGeneration(gen);
		}
		if ((dflag & 0x40) == 0) {
			v = DwgUtil.getBitShort(data, bitPos);
			bitPos = ((Integer) v.get(0)).intValue();
			int halign = ((Integer) v.get(1)).intValue();
			att.setHalign(halign);
		}
		if ((dflag & 0x80) == 0) {
			v = DwgUtil.getBitShort(data, bitPos);
			bitPos = ((Integer) v.get(0)).intValue();
			int valign = ((Integer) v.get(1)).intValue();
			att.setValign(valign);
		}
		v = DwgUtil.getTextString(data, bitPos);
		bitPos = ((Integer) v.get(0)).intValue();
		String tag = (String) v.get(1);
		att.setTag(tag);
		v = DwgUtil.getBitShort(data, bitPos);
		bitPos = ((Integer) v.get(0)).intValue();
		int fl = ((Integer) v.get(1)).intValue();
		att.setFieldLength(fl);
		v = DwgUtil.getRawChar(data, bitPos);
		bitPos = ((Integer) v.get(0)).intValue();
		int flags = ((Integer) v.get(1)).intValue();
		att.setFlags(flags);
		bitPos = headTailReader.readObjectTailer(data, bitPos, att);
		DwgHandleReference styleHandle = new DwgHandleReference();
		bitPos = styleHandle.read(data, bitPos);
		att.setStyleHandle(styleHandle);
	}
}
