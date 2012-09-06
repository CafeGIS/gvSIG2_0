package org.gvsig.raster.buffer.cache;

import org.gvsig.raster.BaseTestCase;
import org.gvsig.raster.dataset.IBuffer;
/**
 * Test para probar la operación de asignar una constante a una banda de un buffer cacheado y
 * el intercambio de 2 bandas para el mismo buffer. Con esto se prueban las operaciones de assign, getBand,
 * assignBand, assignBandToNotValid, interchangeBands y getBandBuffer
 *
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class TestAssignInterchangeBandsCache extends BaseTestCase {

	private RasterCache rc = null;

	public void start() {
		this.setUp();
		this.testStack();
	}

	public void setUp() {
		System.err.println("TestBandOperationCache running...");
		rc = new RasterCache(IBuffer.TYPE_BYTE, 6000, 5000, 3);
	}

	public void testStack() {
//		long t2, t3, t4;
//		long t1 = new Date().getTime();
		rc.setNotValidValue(40);
		rc.assign(0, (byte) 20);
		rc.assign(1, (byte) 30);
		rc.assignBandToNotValid(2);
//		t2 = new Date().getTime();
//		System.out.println("Time: asignar buffer a un valor cte: " + ((t2 - t1) / 1000D) + ", secs.");

		rc.interchangeBands(0, 1);
		testValues(30, 20, 40);

//		t3 = new Date().getTime();
//		System.out.println("Time: asserts del valor por banda cacheado: " + ((t3 - t2) / 1000D) + ", secs.");

		IBuffer buf = rc.getBandBuffer(2);
		testValue((int) rc.getNotValidValue(), buf);

//		t4 = new Date().getTime();
//		System.out.println("Time: getBandBuffer: " + ((t4 - t3) / 1000D) + ", secs.");
	}

	private void testValues(int v1, int v2, int v3) {
		for (int row = 0; row < rc.getHeight(); row++)
			for (int col = 0; col < rc.getWidth(); col++) {
				assertEquals(v1, rc.getElemByte(row, col, 0));
				assertEquals(v2, rc.getElemByte(row, col, 1));
				assertEquals(v3, rc.getElemByte(row, col, 2));
			}
	}

	private void testValue(int value, IBuffer buf) {
		for (int row = 0; row < buf.getHeight(); row++)
			for (int col = 0; col < buf.getWidth(); col++)
				assertEquals(value, buf.getElemByte(row, col, 0));
	}
}