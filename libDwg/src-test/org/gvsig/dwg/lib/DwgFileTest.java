package org.gvsig.dwg.lib;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import junit.framework.TestCase;

public class DwgFileTest extends TestCase {
	private File baseDataPath;

	protected void setUp() throws Exception {
		super.setUp();
		URL url = this.getClass().getResource("data");
		if (url == null) {
			throw new Exception("Can't find 'data' dir");
		}

		baseDataPath = new File(url.getFile());
		if (!baseDataPath.exists()) {
			throw new Exception("Can't find 'data' dir");
		}

	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	public void test1() throws IOException, DwgVersionNotSupportedException {
//		String fileName = baseDataPath.getAbsolutePath()+"/Un punto.dwg";
//		DwgFile dwg = new DwgFile(fileName);
//
//		dwg.read();
//		dwg.calculateGisModelDwgPolylines();
//		dwg.blockManagement();
//		LinkedList dwgObjects = dwg.getDwgObjects();
	}

	public void test2() throws RuntimeException, CorruptedDwgEntityException{
        //Dwg Object that is supposed to be
        //an LWPOLYLINE in V14 version
		/*
        int[] data = new int[]{61,64,64,183,19,227,104,16
                            ,0,5,8,59,72,32,252,47,90,142,
                            234,145,50,10,71,11,213,36,229,
                            162,130,10,228,126,23,174,130,
                            145,50,15,98,141,196,244,229,
                            162,130,12,126,23,169,66,58,
                            145,50,12,47,90,138,68,229,
                            162,130,8,0,0,4,7,74,137,50,
                            15,177,66,231,252,221,162,130,
                            9,130,151,21,242,151,21,190,
                            8,21,8,56};
         */
//		int[] data = new int[]{62,128,64,71,99,
//							   40,48,0,5,8,27,72,
//							   100,126,23,169,68,
//							   178,105,50,13,114,
//							   63,11,82,165,162,130,
//							   13,114,63,11,210,138,
//							   105,50,8,173,114,59,
//							   138,205,162,130,15,98,
//							   141,192,241,58,105,50,
//							   11,51,51,52,178,229,162,
//							   130,14,110,102,98,97,234,
//							   105,50,11,51,51,52,179,21,
//							   162,130,10,149,192,240,42,
//							   162,105,50,14,189,27,131,
//							   107,69,162,130,14,31,169,
//							   66,227,74,105,50,9,240,86,
//							   185,27,117,162,130,11,59,
//							   51,51,52,234,105,50,13,114,
//							   63,11,83,85,162,130,9,74,228,
//							   126,22,186,105,50,11,51,51,50,
//							   51,53,162,130,11,137,232,82,190,
//							   58,105,50,9,74,228,122,147,13,162,
//							   130,11,137,232,82,189,106,105,50,9,
//							   74,228,122,146,213,162,130,9,74,228,122,20,202,105,50,12,126,23,171,194,173,162,130,12,126,23,169,68,178,105,50,13,114,63,11,82,165,162,130,9,130,151,22,10,136,182,8,21,8,120};
//        //1er intento: suponemos que la LWPOLYLINE cumple la especificación
//        //a rajatabla
//        int bitPos = 0;
//        List val = DwgUtil.getBitShort(data, bitPos);
//        bitPos = ((Integer) val.get(0)).intValue();
//        int type = ((Integer) val.get(1)).intValue();
//        System.out.println("type = " + type);
//
//        DwgHandleReference hr = new DwgHandleReference();
//        bitPos = hr.read(data, bitPos);
//        System.out.println("handle, code="+
//                    hr.getCode()+
//                    " ,offset="+
//                    hr.getOffset());
//
//        //Ahora pasamos a la extended data
//        val = DwgUtil.getBitShort(data, bitPos);
//        bitPos = ((Integer) val.get(0)).intValue();
//        int extendedDataSize = ((Integer) val.get(1)).intValue();
//        System.out.println("EED size="+extendedDataSize);
//        //como el size es 0, me lo salto
//
//        //ver si tiene datos graficos
//        val = DwgUtil.testBit(data, bitPos);
//        bitPos = ((Integer) val.get(0)).intValue();
//        boolean hasGraphicsData = ((Boolean) val.get(1))
//                .booleanValue();
//        System.out.println("graphics = "+hasGraphicsData);
//
//        //como se que no hay graphics me lo salto
//        //tamaño en bits
//        val = DwgUtil.getRawLong(data, bitPos);
//        bitPos = ((Integer) val.get(0)).intValue();
//        int sizeInBits = ((Integer) val.get(1)).intValue();
//        System.out.println("sizeInBits = "+sizeInBits);
//
//        /*
//         * Ahora, lo que viene es lo que en la spec se dice
//         * "Common entity data". Revisar bien pues PythonCAD no lo lee
//         * como en la spec.
//         *
//         * pag 42.
//          R13-R14 Only:
//          	RL	:	Size of object data in bits
//          	6B	:	Flags (FEDCBA)
//          	6B	:	Common parameters (CCSSII)
//          	Segun esto, deberia leer 6 bits y 6 bits
//
//          	FLAGS
//	      	Mas adelante (pag 43), dice:
//	      	DC	:	This is the number of reactors attached to an
//	      	entity as a bitshort.
//	      	This feature may have been dormant in R13,
//	      	but it appears in R14, and in files saved as R13 by R14.
//
//	      	Ahora bien, pythoncad las está leyendo como bitLong
//          	¿En que quedamos, son 2 bits, 1 bitLong o 1 bitShort?
//          	TODO REVISAR
//
//          	COMMON PARAMETERS
//          	Al principio, dice que son 6 bits (CC, SS, II)
//          	pero luego dice (pag 43):
//          	CC	:	Color bitshort
//          	SS	:	Linetype scale bitdouble
//          	II	:	"Invisible" flag bitshort
//
//			Pythoncad, en vez de como 2 bits, los está leyendo
//			como BitShort, BitDouble y BitShort
//
//         * */
//
//        Integer mode = (Integer) DwgUtil.getBits(data, 2, bitPos);
//		bitPos += 2;
//		System.out.println("mode = "+mode);
//
//	/*
//		val = DwgUtil.getBitLong(data, bitPos);
//		bitPos = ((Integer) val.get(0)).intValue();
//		int rnum = ((Integer) val.get(1)).intValue();
//		System.out.println("numReactors = "+rnum);
//*/
//		val = DwgUtil.getBitShort(data, bitPos);
//		bitPos = ((Integer) val.get(0)).intValue();
//		int rnum = ((Integer) val.get(1)).intValue();
//		System.out.println("numReactors = "+rnum);
//
//
//		val = DwgUtil.testBit(data, bitPos);
//		bitPos = ((Integer) val.get(0)).intValue();
//		boolean isLyrByLineType = ((Boolean) val.get(1)).booleanValue();
//		System.out.println("isLyrByLineType="+isLyrByLineType);
//
//		val = DwgUtil.testBit(data, bitPos);
//		bitPos = ((Integer) val.get(0)).intValue();
//		boolean noLinks = ((Boolean) val.get(1)).booleanValue();
//		System.out.println("noLinks="+noLinks);
//
//
//		val = DwgUtil.getBitShort(data, bitPos);
//		bitPos = ((Integer) val.get(0)).intValue();
//		int color = ((Integer) val.get(1)).intValue();
//		System.out.println("color="+color);
//
//
//		val = DwgUtil.getBitDouble(data, bitPos);
//		bitPos = ((Integer) val.get(0)).intValue();
//		float ltscale = ((Double) val.get(1)).floatValue();
//		System.out.println("ltscale="+ltscale);
//
//		val = DwgUtil.getBitShort(data, bitPos);
//		bitPos = ((Integer) val.get(0)).intValue();
//		int invis = ((Integer) val.get(1)).intValue();
//		System.out.println("invis="+invis);
//
//		val = DwgUtil.getBitShort(data, bitPos);
//		bitPos = ((Integer) val.get(0)).intValue();
//		int flag = ((Integer) val.get(1)).intValue();
//		System.out.println("flag="+flag);
//
//		double dVal = 0d;
//		if((flag & 0x4) > 0){
//			val = DwgUtil.getBitDouble(data, bitPos);
//			bitPos = ((Integer) val.get(0)).intValue();
//			dVal = ((Double) val.get(1)).doubleValue();
//		}
//		System.out.println("constWidth="+dVal);
//
//		dVal = 0d;
//		if((flag & 0x8) > 0){
//			val = DwgUtil.getBitDouble(data, bitPos);
//			bitPos = ((Integer) val.get(0)).intValue();
//			dVal = ((Double) val.get(1)).doubleValue();
//		}
//		System.out.println("elevation="+dVal);
//
//		dVal = 0d;
//		if ((flag & 0x2) > 0){
//			val = DwgUtil.getBitDouble(data, bitPos);
//			bitPos = ((Integer) val.get(0)).intValue();
//			dVal = ((Double) val.get(1)).doubleValue();
//		}
//		System.out.println("thickness="+dVal);
//
//		double x, y, z ;
//		x = 0d;
//		y = 0d;
//		z = 0d;
//
//		if ((flag & 0x1) > 0){
//			val = DwgUtil.getBitDouble(data, bitPos);
//			bitPos = ((Integer) val.get(0)).intValue();
//			x = ((Double) val.get(1)).doubleValue();
//
//			val = DwgUtil.getBitDouble(data, bitPos);
//			bitPos = ((Integer) val.get(0)).intValue();
//			y = ((Double) val.get(1)).doubleValue();
//
//			val = DwgUtil.getBitDouble(data, bitPos);
//			bitPos = ((Integer) val.get(0)).intValue();
//			z = ((Double) val.get(1)).doubleValue();
//		}
//		System.out.println("normal="+x+","+y+","+z);
//
//		val = DwgUtil.getBitLong(data, bitPos);
//		bitPos = ((Integer) val.get(0)).intValue();
//		int np = ((Integer) val.get(1)).intValue();
//		System.out.println("numPoints="+np);
//
//		int nb = 0;
//		if((flag & 0x10) > 0){
//			val = DwgUtil.getBitLong(data, bitPos);
//			bitPos = ((Integer) val.get(0)).intValue();
//			nb = ((Integer) val.get(1)).intValue();
//		}
//		System.out.println("numBulges="+nb);
//		int nw = 0;
//		if((flag & 0x20) > 0){
//			val = DwgUtil.getBitLong(data, bitPos);
//			bitPos = ((Integer) val.get(0)).intValue();
//			nw = ((Integer) val.get(1)).intValue();
//		}
//		System.out.println("numWidths="+nw);
//		if(np > 0){
//			Point2D[] points = new Point2D[np];
//			for(int i = 0; i < np; i++){
//				val = DwgUtil.getRawDouble(data, bitPos);
//				bitPos = ((Integer) val.get(0)).intValue();
//				x = ((Double) val.get(1)).doubleValue();
//
//				val = DwgUtil.getRawDouble(data, bitPos);
//				bitPos = ((Integer) val.get(0)).intValue();
//				y = ((Double) val.get(1)).doubleValue();
//
//				points[i] = new Point2D.Double(x, y);
//				System.out.println("Punto"+i+"="+x+","+y);
//			}//for
//		}//if np
//
//		if(nb > 0){
//			double[] bulges = new double[nb];
//			for(int i = 0; i < nb; i++){
//				val = DwgUtil.getRawDouble(data, bitPos);
//				bitPos = ((Integer) val.get(0)).intValue();
//				bulges[i] = ((Double) val.get(1)).doubleValue();
//				System.out.println("Bulge"+i+"="+bulges[i]);
//			}//for
//
//		}//if nb
//
//		if(nw > 0){
//			double[][] widths = new double[nw][2];
//			for(int i = 0; i < nw; i++){
//				val = DwgUtil.getBitDouble(data, bitPos);
//				bitPos = ((Integer) val.get(0)).intValue();
//				double sw = ((Double) val.get(1)).doubleValue();
//
//				val = DwgUtil.getBitDouble(data, bitPos);
//				bitPos = ((Integer) val.get(0)).intValue();
//				double ew = ((Double) val.get(1)).doubleValue();
//
//				widths[i][0] = sw;
//				widths[i][1] = ew;
//				System.out.println("Width"+i+"="+sw+","+ew);
//			}//for
//		}
     }

	public void test3(){
		//test of extrusion
		double[] coord = null;
		double[] extrusion = null;
		double[] extrusion2 = new double[]{0, 0, 1};

		coord = new double[]{790089.65, 4477974.75, 9.560000000114087};
		extrusion = new double[]{-0.5037965987025721, 0.07005064807841195, 0.8609772899673451};
		//1. algoritmo original, vector normal distinto
//		double[] newCoord = AcadExtrusionCalculator.extrude(coord, extrusion);
//		newCoord = AcadExtrusionCalculator.extrude3(coord, extrusion);
//
		//2º ahora con vector normal coincidente con eje Z
//		newCoord = AcadExtrusionCalculator.extrude(coord, extrusion2);
//		newCoord = AcadExtrusionCalculator.extrude3(coord, extrusion2);
	}
}
