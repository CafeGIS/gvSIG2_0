/*
 * Created on 25-ene-2007 by azabala
 *
 */
package org.gvsig.dwg.lib.readers.v2004;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.gvsig.dwg.lib.CorruptedDwgEntityException;
import org.gvsig.dwg.lib.DwgObject;
import org.gvsig.dwg.lib.DwgUtil;
import org.gvsig.dwg.lib.objects.DwgText;
import org.gvsig.dwg.lib.util.TextToUnicodeConverter;


/**
 * @author alzabord
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DwgTextReader2004 extends AbstractDwg2004Reader {

	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.readers.IDwgObjectReader#readSpecificObj(int[], int, com.iver.cit.jdwglib.dwg.DwgObject)
	 */
	public void readSpecificObj(int[] data, int offset, DwgObject dwgObj) throws RuntimeException, CorruptedDwgEntityException {
		if(! (dwgObj instanceof DwgText))
			throw new RuntimeException("TextReader 2004 solo puede leer DwgText");
		DwgText txt = (DwgText) dwgObj;
		int bitPos = offset;
		bitPos = readObjectHeader(data, bitPos, txt);

		ArrayList v = DwgUtil.getRawChar(data, bitPos);
		bitPos = ((Integer)v.get(0)).intValue();
		int dflag = ((Integer)v.get(1)).intValue();
		txt.setDataFlag(dflag);

		if ((dflag & 0x1)==0) {
			v = DwgUtil.getRawDouble(data, bitPos);
			bitPos = ((Integer)v.get(0)).intValue();
			double elev = ((Double)v.get(1)).doubleValue();
			txt.setElevation(elev);
		}

		v = DwgUtil.getRawDouble(data, bitPos);
		bitPos = ((Integer)v.get(0)).intValue();
		double x1 = ((Double)v.get(1)).doubleValue();
		v = DwgUtil.getRawDouble(data, bitPos);
		bitPos = ((Integer)v.get(0)).intValue();
		double y1 = ((Double)v.get(1)).doubleValue();
		txt.setInsertionPoint(new Point2D.Double(x1, y1));

		if ((dflag & 0x2)==0) {
			v = DwgUtil.getDefaultDouble(data, bitPos, x1);
			bitPos = ((Integer)v.get(0)).intValue();
			double xa = ((Double)v.get(1)).doubleValue();
			v = DwgUtil.getDefaultDouble(data, bitPos, y1);
			bitPos = ((Integer)v.get(0)).intValue();
			double ya = ((Double)v.get(1)).doubleValue();
			txt.setAlignmentPoint(new Point2D.Double(xa, ya));
		}

		v = DwgUtil.testBit(data, bitPos);
		bitPos = ((Integer)v.get(0)).intValue();
		boolean flag = ((Boolean)v.get(1)).booleanValue();
		double x, y, z;
		if (flag) {
			x = 0.0;
			y = 0.0;
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
		txt.setExtrusion(new double[]{x, y, z});

		v = DwgUtil.testBit(data, bitPos);
		bitPos = ((Integer)v.get(0)).intValue();
		flag = ((Boolean)v.get(1)).booleanValue();
	    double th;
		if (flag) {
			th=0.0;
		} else {
			v = DwgUtil.getBitDouble(data, bitPos);
			bitPos = ((Integer)v.get(0)).intValue();
			th = ((Double)v.get(1)).doubleValue();
		}
		txt.setThickness(th);

		if ((dflag & 0x4) == 0) {
			v = DwgUtil.getRawDouble(data, bitPos);
			bitPos = ((Integer)v.get(0)).intValue();
			double oblique = ((Double)v.get(1)).doubleValue();
			txt.setObliqueAngle(oblique);
		}

		if ((dflag & 0x8) == 0) {
			v = DwgUtil.getRawDouble(data, bitPos);
			bitPos = ((Integer)v.get(0)).intValue();
			double rot = ((Double)v.get(1)).doubleValue();
			txt.setRotationAngle(rot);
		}

		v = DwgUtil.getRawDouble(data, bitPos);
		bitPos = ((Integer)v.get(0)).intValue();
		double height = ((Double)v.get(1)).doubleValue();
		txt.setHeight(height);

		if ((dflag & 0x10) == 0) {
			v = DwgUtil.getRawDouble(data, bitPos);
			bitPos = ((Integer)v.get(0)).intValue();
			double width = ((Double)v.get(1)).doubleValue();
			txt.setWidthFactor(width);
		}

		v = DwgUtil.getTextString(data, bitPos);
		bitPos = ((Integer)v.get(0)).intValue();
		String text = (String)v.get(1);
		text = TextToUnicodeConverter.convertText(text);
		txt.setText(text);

		if ((dflag & 0x20) == 0) {
			v = DwgUtil.getBitShort(data, bitPos);
			bitPos = ((Integer)v.get(0)).intValue();
			int gen = ((Integer)v.get(1)).intValue();
			txt.setGeneration(gen);
		}

		if ((dflag & 0x40) == 0) {
			v = DwgUtil.getBitShort(data, bitPos);
			bitPos = ((Integer)v.get(0)).intValue();
			int halign = ((Integer)v.get(1)).intValue();
			txt.setHalign(halign);
		}

		if ((dflag & 0x80) == 0) {
			v = DwgUtil.getBitShort(data, bitPos);
			bitPos = ((Integer)v.get(0)).intValue();
			int valign = ((Integer)v.get(1)).intValue();
			txt.setValign(valign);
		}
		txt.inserta();
		bitPos = readObjectTailer(data, bitPos, txt);
	}

}
