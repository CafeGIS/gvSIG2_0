/*
 * Created on 25-ene-2007 by azabala
 *
 */
package org.gvsig.dwg.lib.readers.v1314;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import org.gvsig.dwg.lib.CorruptedDwgEntityException;
import org.gvsig.dwg.lib.DwgObject;
import org.gvsig.dwg.lib.DwgUtil;
import org.gvsig.dwg.lib.objects.DwgLwPolyline;


/**
 * @author alzabord
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DwgLwPolylineReader1314 extends AbstractDwg1314Reader{

	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.readers.IDwgObjectReader#readSpecificObj(int[], int, com.iver.cit.jdwglib.dwg.DwgObject)
	 */
	public void readSpecificObj(int[] data, int offset, DwgObject dwgObj) throws RuntimeException, CorruptedDwgEntityException {
		if(! (dwgObj instanceof DwgLwPolyline))
	    	throw new RuntimeException("ArcReader 14 solo puede leer DwgLwPolyline");
		DwgLwPolyline l = (DwgLwPolyline) dwgObj;
		int bitPos = offset;
		
		bitPos = headTailReader.readObjectHeader(data, bitPos, l);
		
	
		List val = DwgUtil.getBitShort(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		int flag = ((Integer) val.get(1)).intValue();
		l.setFlag(flag);
		
		double dVal = 0d;
		if((flag & 0x4) > 0){
			val = DwgUtil.getBitDouble(data, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dVal = ((Double) val.get(1)).doubleValue();
		}
		l.setConstWidth(dVal);
		
		dVal = 0d;
		if((flag & 0x8) > 0){
			val = DwgUtil.getBitDouble(data, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dVal = ((Double) val.get(1)).doubleValue();
		}
		l.setElevation(dVal);
		
		dVal = 0d;
		if ((flag & 0x2) > 0){
			val = DwgUtil.getBitDouble(data, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dVal = ((Double) val.get(1)).doubleValue();
		}
		l.setThickness(dVal);
		
		double x, y, z ;
		x = 0d;
		y = 0d; 
		z = 0d;
		
		if ((flag & 0x1) > 0){
			val = DwgUtil.getBitDouble(data, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			x = ((Double) val.get(1)).doubleValue();
			
			val = DwgUtil.getBitDouble(data, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			y = ((Double) val.get(1)).doubleValue();
			
			val = DwgUtil.getBitDouble(data, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			z = ((Double) val.get(1)).doubleValue();
		}
		l.setNormal(new double[]{x, y, z});
		
		val = DwgUtil.getBitLong(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		int np = ((Integer) val.get(1)).intValue();
		
		int nb = 0;
		if((flag & 0x10) > 0){
			val = DwgUtil.getBitLong(data, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			nb = ((Integer) val.get(1)).intValue();
		}
		if(nb > 10000)
    		throw new CorruptedDwgEntityException("LwPolyline corrupta");
		
		int nw = 0;
		if((flag & 0x20) > 0){
			val = DwgUtil.getBitLong(data, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			nw = ((Integer) val.get(1)).intValue();
		}
		
		if(nw > 10000)
    		throw new CorruptedDwgEntityException("LwPolyline corrupta");
		
		if(np > 0){
			if(np > 10000)
	    		throw new CorruptedDwgEntityException("LwPolyline corrupta");
			List points = new ArrayList();
			for(int i = 0; i < np; i++){
				val = DwgUtil.getRawDouble(data, bitPos);
				bitPos = ((Integer) val.get(0)).intValue();
				x = ((Double) val.get(1)).doubleValue();
				
				val = DwgUtil.getRawDouble(data, bitPos);
				bitPos = ((Integer) val.get(0)).intValue();
				y = ((Double) val.get(1)).doubleValue();
				
				points.add(new double[]{x,y});
			}//for
			l.setVertices(points);
		}//if np
		
		if(nb > 0){
			double[] bulges = new double[nb];
			for(int i = 0; i < nb; i++){
				val = DwgUtil.getRawDouble(data, bitPos);
				bitPos = ((Integer) val.get(0)).intValue();
				bulges[i] = ((Double) val.get(1)).doubleValue();
			}//for
			l.setBulges(bulges);
		}//if nb
		
		if(nw > 0){
			double[][] widths = new double[nw][2];
			for(int i = 0; i < nw; i++){
				val = DwgUtil.getBitDouble(data, bitPos);
				bitPos = ((Integer) val.get(0)).intValue();
				double sw = ((Double) val.get(1)).doubleValue();
				
				val = DwgUtil.getBitDouble(data, bitPos);
				bitPos = ((Integer) val.get(0)).intValue();
				double ew = ((Double) val.get(1)).doubleValue();
				
				widths[i][0] = sw;
				widths[i][1] = ew;
			}//for
			l.setWidths(widths);
		}
		bitPos = headTailReader.readObjectTailer(data, bitPos, l);
	}
}
