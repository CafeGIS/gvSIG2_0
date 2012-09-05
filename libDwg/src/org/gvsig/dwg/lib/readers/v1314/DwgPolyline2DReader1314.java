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
import org.gvsig.dwg.lib.objects.DwgPolyline2D;


/**
 * @author alzabord
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DwgPolyline2DReader1314 extends AbstractDwg1314Reader{

	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.readers.IDwgObjectReader#readSpecificObj(int[], int, com.iver.cit.jdwglib.dwg.DwgObject)
	 */
	public void readSpecificObj(int[] data, int offset, DwgObject dwgObj) throws RuntimeException, CorruptedDwgEntityException {
		if(! (dwgObj instanceof DwgPolyline2D))
	    	throw new RuntimeException("ArcReader 14 solo puede leer DwgPolyline2d");
		DwgPolyline2D l = (DwgPolyline2D)dwgObj;
		
		int bitPos = offset;
		bitPos = headTailReader.readObjectHeader(data, offset, l);
		
		List val = DwgUtil.getBitShort(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		int flags = ((Integer) val.get(1)).intValue();
		l.setFlags(flags);
		
		val = DwgUtil.getBitShort(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		int curveType = ((Integer) val.get(1)).intValue();
		l.setCurveType(curveType);
		
		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		double startWidth = ((Double) val.get(1)).doubleValue();
		l.setInitWidth(startWidth);
		
		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		double endWidth = ((Double) val.get(1)).doubleValue();
		l.setEndWidth(endWidth);
		
		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		double thickness = ((Double) val.get(1)).doubleValue();
		l.setThickness(thickness);
		
		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		double elevation = ((Double) val.get(1)).doubleValue();
		l.setElevation(elevation);
		
		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		double x = ((Double) val.get(1)).doubleValue();
		
		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		double y = ((Double) val.get(1)).doubleValue();
		
		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		double z = ((Double) val.get(1)).doubleValue();
		l.setExtrusion(new double[]{x, y, z});
		
		bitPos = headTailReader.readObjectTailer(data, offset, l);
		
		DwgHandleReference firstVerHdl = new DwgHandleReference();
		bitPos = firstVerHdl.read(data, bitPos);
		l.setFirstVertexHandle(firstVerHdl);
		
		DwgHandleReference lastVerHdl = new DwgHandleReference();
		bitPos = lastVerHdl.read(data, bitPos);
		l.setLastVertexHandle(lastVerHdl);
		
		DwgHandleReference seqendHdl = new DwgHandleReference();
		bitPos = seqendHdl.read(data, bitPos);
		l.setSeqendHandle(seqendHdl);
	}
}
