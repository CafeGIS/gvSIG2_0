/*
 * Created on 25-ene-2007 by azabala
 *
 */
package org.gvsig.dwg.lib.readers.v15;

import java.util.ArrayList;

import org.gvsig.dwg.lib.CorruptedDwgEntityException;
import org.gvsig.dwg.lib.DwgObject;
import org.gvsig.dwg.lib.DwgUtil;
import org.gvsig.dwg.lib.objects.DwgSpline;


/**
 * @author alzabord
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DwgSplineReader15 extends AbstractDwg15Reader{

	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.readers.IDwgObjectReader#readSpecificObj(int[], int, com.iver.cit.jdwglib.dwg.DwgObject)
	 */
	public void readSpecificObj(int[] data, int offset, DwgObject dwgObj) throws RuntimeException, CorruptedDwgEntityException {
		if(! (dwgObj instanceof DwgSpline))
			throw new RuntimeException("ArcReader 15 solo puede leer DwgSpline");
		DwgSpline spline = (DwgSpline) dwgObj;
		int bitPos = offset;
		bitPos = headTailReader.readObjectHeader(data, bitPos, spline);
		ArrayList v = DwgUtil.getBitShort(data, bitPos);
		bitPos = ((Integer)v.get(0)).intValue();
		int sc = ((Integer)v.get(1)).intValue();
		spline.setScenario(sc);
		v = DwgUtil.getBitShort(data, bitPos);
		bitPos = ((Integer)v.get(0)).intValue();
		int deg = ((Integer)v.get(1)).intValue();
		spline.setDegree(deg);
		int knotsNumber = 0;
		int controlPointsNumber = 0;
		int fitPointsNumber = 0;
		boolean weight = false;
		if (sc==2) {
			v = DwgUtil.getBitDouble(data, bitPos);
			bitPos = ((Integer)v.get(0)).intValue();
			double ft = ((Double)v.get(1)).doubleValue();
			spline.setFitTolerance(ft);
			v = DwgUtil.getBitDouble(data, bitPos);
			bitPos = ((Integer)v.get(0)).intValue();
			double x = ((Double)v.get(1)).doubleValue();
			v = DwgUtil.getBitDouble(data, bitPos);
			bitPos = ((Integer)v.get(0)).intValue();
			double y = ((Double)v.get(1)).doubleValue();
			v = DwgUtil.getBitDouble(data, bitPos);
			bitPos = ((Integer)v.get(0)).intValue();
			double z = ((Double)v.get(1)).doubleValue();
			double[] coord = new double[]{x, y, z};
			spline.setBeginTanVector(coord);
			v = DwgUtil.getBitDouble(data, bitPos);
			bitPos = ((Integer)v.get(0)).intValue();
			x = ((Double)v.get(1)).doubleValue();
			v = DwgUtil.getBitDouble(data, bitPos);
			bitPos = ((Integer)v.get(0)).intValue();
			y = ((Double)v.get(1)).doubleValue();
			v = DwgUtil.getBitDouble(data, bitPos);
			bitPos = ((Integer)v.get(0)).intValue();
			z = ((Double)v.get(1)).doubleValue();
			coord = new double[]{x, y, z};
			spline.setEndTanVector(coord);
			v = DwgUtil.getBitShort(data, bitPos);
			bitPos = ((Integer)v.get(0)).intValue();
			fitPointsNumber = ((Integer)v.get(1)).intValue();
		} else if (sc==1) {
			v = DwgUtil.testBit(data, bitPos);
			bitPos = ((Integer)v.get(0)).intValue();
			boolean rat = ((Boolean)v.get(1)).booleanValue();
			spline.setRational(rat);
			v = DwgUtil.testBit(data, bitPos);
			bitPos = ((Integer)v.get(0)).intValue();
			boolean closed = ((Boolean)v.get(1)).booleanValue();
			spline.setClosed(closed);
			v = DwgUtil.testBit(data, bitPos);
			bitPos = ((Integer)v.get(0)).intValue();
			boolean per = ((Boolean)v.get(1)).booleanValue();
			spline.setPeriodic(per);
			v = DwgUtil.getBitDouble(data, bitPos);
			bitPos = ((Integer)v.get(0)).intValue();
			double ktol = ((Double)v.get(1)).doubleValue();
			spline.setKnotTolerance(ktol);
			v = DwgUtil.getBitDouble(data, bitPos);
			bitPos = ((Integer)v.get(0)).intValue();
			double ctol = ((Double)v.get(1)).doubleValue();
			spline.setControlTolerance(ctol);
			v = DwgUtil.getBitLong(data, bitPos);
			bitPos = ((Integer)v.get(0)).intValue();
			knotsNumber = ((Integer)v.get(1)).intValue();
			v = DwgUtil.getBitLong(data, bitPos);
			bitPos = ((Integer)v.get(0)).intValue();
			controlPointsNumber = ((Integer)v.get(1)).intValue();
			v = DwgUtil.testBit(data, bitPos);
			bitPos = ((Integer)v.get(0)).intValue();
			weight = ((Boolean)v.get(1)).booleanValue();
		} else {
			System.out.println("ERROR: Escenario desconocido");
		}
		if (knotsNumber>0) {
			double[] knotpts = new double[knotsNumber];
			for (int i=0;i<knotsNumber;i++) {
				v = DwgUtil.getBitDouble(data, bitPos);
				bitPos = ((Integer)v.get(0)).intValue();
				knotpts[i] = ((Double)v.get(1)).doubleValue();
			}
			spline.setKnotPoints(knotpts);
		}
		if (controlPointsNumber>0) {
			// Si el n?mero de weights no coincide con el de ctrlpts habr? problemas ...
			double[][] ctrlpts = new double[controlPointsNumber][3];
			double[] weights = new double[controlPointsNumber];
			for (int i=0;i<controlPointsNumber;i++) {
				v = DwgUtil.getBitDouble(data, bitPos);
				bitPos = ((Integer)v.get(0)).intValue();
				double x = ((Double)v.get(1)).doubleValue();
				v = DwgUtil.getBitDouble(data, bitPos);
				bitPos = ((Integer)v.get(0)).intValue();
				double y = ((Double)v.get(1)).doubleValue();
				v = DwgUtil.getBitDouble(data, bitPos);
				bitPos = ((Integer)v.get(0)).intValue();
				double z = ((Double)v.get(1)).doubleValue();
				//double[] coord = new double[]{x, y, z};
				ctrlpts[i][0] = x;
				ctrlpts[i][1] = y;
				ctrlpts[i][2] = z;
				if (weight) {
					v = DwgUtil.getBitDouble(data, bitPos);
					bitPos = ((Integer)v.get(0)).intValue();
					weights[i] = ((Double)v.get(1)).doubleValue();
				}
			}
			spline.setControlPoints(ctrlpts);
			if (weight) {
				spline.setWeights(weights);
			}
		}
		if (fitPointsNumber>0) {
			double[][] fitpts = new double[fitPointsNumber][3];
			for (int i=0;i<fitPointsNumber;i++) {
				v = DwgUtil.getBitDouble(data, bitPos);
				bitPos = ((Integer)v.get(0)).intValue();
				double x = ((Double)v.get(1)).doubleValue();
				v = DwgUtil.getBitDouble(data, bitPos);
				bitPos = ((Integer)v.get(0)).intValue();
				double y = ((Double)v.get(1)).doubleValue();
				v = DwgUtil.getBitDouble(data, bitPos);
				bitPos = ((Integer)v.get(0)).intValue();
				double z = ((Double)v.get(1)).doubleValue();
				fitpts[i][0] = x;
				fitpts[i][1] = y;
				fitpts[i][2] = z;
			}
			spline.setFitPoints(fitpts);
		}
		bitPos = headTailReader.readObjectTailer(data, bitPos, spline);
	}


}
