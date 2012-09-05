/*
 * Created on 25-ene-2007 by azabala
 *
 */
package org.gvsig.dwg.lib.readers.v1314;

import java.util.List;

import org.gvsig.dwg.lib.CorruptedDwgEntityException;
import org.gvsig.dwg.lib.DwgObject;
import org.gvsig.dwg.lib.DwgUtil;
import org.gvsig.dwg.lib.objects.DwgSpline;


/**
 * @author azabala
 */
public class DwgSplineReader1314 extends AbstractDwg1314Reader{

	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.readers.IDwgObjectReader#readSpecificObj(int[], int, com.iver.cit.jdwglib.dwg.DwgObject)
	 */
	public void readSpecificObj(int[] data, int offset, DwgObject dwgObj) throws RuntimeException, CorruptedDwgEntityException {
		if(! (dwgObj instanceof DwgSpline))
	    	throw new RuntimeException("ArcReader 14 solo puede leer DwgSpline");
		DwgSpline spline = (DwgSpline) dwgObj;
		
		int bitPos = offset;
		bitPos = headTailReader.readObjectHeader(data, bitPos, spline );
		
		List val = DwgUtil.getBitShort(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		int scenario = ((Integer) val.get(1)).intValue();
		
		val = DwgUtil.getBitShort(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		int degree = ((Integer) val.get(1)).intValue();
		spline.setDegree(degree);
		
		int knots = 0;
		int numCtPts = 0;
		int numFitPts = 0;
		
		boolean weight = false;
		
		switch(scenario){
			case 2:
				val = DwgUtil.getBitDouble(data, bitPos);
				bitPos = ((Integer) val.get(0)).intValue();
				double fitTolerance = ((Integer) val.get(1)).intValue();
				spline.setFitTolerance(fitTolerance);
				
				val = DwgUtil.getBitDouble(data, bitPos);
				bitPos = ((Integer) val.get(0)).intValue();
				double x = ((Integer) val.get(1)).intValue();
				
				val = DwgUtil.getBitDouble(data, bitPos);
				bitPos = ((Integer) val.get(0)).intValue();
				double y = ((Integer) val.get(1)).intValue();
				
				val = DwgUtil.getBitDouble(data, bitPos);
				bitPos = ((Integer) val.get(0)).intValue();
				double z = ((Integer) val.get(1)).intValue();
				spline.setBeginTanVector(new double[]{x, y, z});
				
				val = DwgUtil.getBitDouble(data, bitPos);
				bitPos = ((Integer) val.get(0)).intValue();
				x = ((Integer) val.get(1)).intValue();
				
				val = DwgUtil.getBitDouble(data, bitPos);
				bitPos = ((Integer) val.get(0)).intValue();
				y = ((Integer) val.get(1)).intValue();
				
				val = DwgUtil.getBitDouble(data, bitPos);
				bitPos = ((Integer) val.get(0)).intValue();
				z = ((Integer) val.get(1)).intValue();
				spline.setEndTanVector(new double[]{x, y, z});
				
				val = DwgUtil.getBitShort(data, bitPos);
				bitPos = ((Integer) val.get(0)).intValue();
				numFitPts = ((Integer) val.get(1)).intValue();
			break;
			
			case 1:
				val = DwgUtil.testBit(data, bitPos);
				bitPos = ((Integer) val.get(0)).intValue();
				boolean rational  = ((Boolean) val.get(1)).booleanValue();
				
				val = DwgUtil.testBit(data, bitPos);
				bitPos = ((Integer) val.get(0)).intValue();
				boolean closed  = ((Boolean) val.get(1)).booleanValue();
				
				val = DwgUtil.testBit(data, bitPos);
				bitPos = ((Integer) val.get(0)).intValue();
				boolean periodic  = ((Boolean) val.get(1)).booleanValue();
				
				val = DwgUtil.getBitDouble(data, bitPos);
				bitPos = ((Integer) val.get(0)).intValue();
				double knotTolerance = ((Double) val.get(1)).doubleValue();
				spline.setKnotTolerance(knotTolerance);
				
				val = DwgUtil.getBitDouble(data, bitPos);
				bitPos = ((Integer) val.get(0)).intValue();
				double controlTolerance = ((Double) val.get(1)).doubleValue();
				spline.setControlTolerance(controlTolerance);
				
				val = DwgUtil.getBitLong(data, bitPos);
				bitPos = ((Integer) val.get(0)).intValue();
				knots = ((Integer) val.get(1)).intValue();
				
				val = DwgUtil.getBitLong(data, bitPos);
				bitPos = ((Integer) val.get(0)).intValue();
				numCtPts = ((Integer) val.get(1)).intValue();
				
				val = DwgUtil.testBit(data, bitPos);
				bitPos = ((Integer) val.get(0)).intValue();
				weight  = ((Boolean) val.get(1)).booleanValue();
				
			break;
		
		
			default:
				System.out.println("Unexpected spline scenario "+scenario);
			break;
		}
		
		if(knots > 0){
			double[] knotsArray = new double[knots];
			for(int i = 0; i < knots; i++){
				val = DwgUtil.getBitDouble(data, bitPos);
				bitPos = ((Integer) val.get(0)).intValue();
				knotsArray[i] = ((Double) val.get(1)).doubleValue();
			}
			spline.setKnotPoints(knotsArray);
		}
		
		if(numCtPts > 0){
			double[][] ctrlPoint = new double[3][numCtPts];
			double[] weights = new double[numCtPts];
			for(int i = 0; i < numCtPts; i++){
				val = DwgUtil.getBitDouble(data, bitPos);
				bitPos = ((Integer) val.get(0)).intValue();
				double x = ((Integer) val.get(1)).intValue();
				
				val = DwgUtil.getBitDouble(data, bitPos);
				bitPos = ((Integer) val.get(0)).intValue();
				double y = ((Integer) val.get(1)).intValue();
				
				val = DwgUtil.getBitDouble(data, bitPos);
				bitPos = ((Integer) val.get(0)).intValue();
				double z = ((Integer) val.get(1)).intValue();
				
				ctrlPoint[0][i] = x;
				ctrlPoint[1][i] = y;
				ctrlPoint[2][i] = z;
				
				if(weight){
					val = DwgUtil.getBitDouble(data, bitPos);
					bitPos = ((Integer) val.get(0)).intValue();
					weights[i] = ((Integer) val.get(1)).intValue();
				}
			}//for
			spline.setControlPoints(ctrlPoint);
			spline.setWeights(weights);
		}//if
		
		
		if(numFitPts > 0){
			double[][] fitPoint = new double[3][numCtPts];
			for(int i = 0; i < numFitPts; i++){
				val = DwgUtil.getBitDouble(data, bitPos);
				bitPos = ((Integer) val.get(0)).intValue();
				double x = ((Integer) val.get(1)).intValue();
				
				val = DwgUtil.getBitDouble(data, bitPos);
				bitPos = ((Integer) val.get(0)).intValue();
				double y = ((Integer) val.get(1)).intValue();
				
				val = DwgUtil.getBitDouble(data, bitPos);
				bitPos = ((Integer) val.get(0)).intValue();
				double z = ((Integer) val.get(1)).intValue();
				
				fitPoint[0][i] = x;
				fitPoint[1][i] = y;
				fitPoint[2][i] = z;
			}
			spline.setFitPoints(fitPoint);
		}
		bitPos = headTailReader.readObjectTailer(data, bitPos, spline);
	}
}
