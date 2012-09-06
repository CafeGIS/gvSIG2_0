/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2007 IVER T.I. and Generalitat Valenciana.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 */
package org.gvsig.raster.dataset;

import junit.framework.TestCase;

import org.gvsig.raster.RasterLibrary;
/**
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class TestBandList extends TestCase {
	private boolean  showCode = false;
	private BandList bandList = null;

	static {
		RasterLibrary.wakeUp();
	}

	public void start() {
		this.setUp();
		this.testStack();
	}

	public void setUp() {
		System.err.println("TestBandList running...");
		bandList = new BandList();
		for (int i = 0; i < 5; i++) {
			try {
				Band band = new Band("fileName", i, IBuffer.TYPE_BYTE);
				bandList.addBand(band, i);
			} catch (BandNotFoundInListException ex) {
				// No añadimos la banda
			}
		}
	}

	public void testStack() {
		bandList.clearDrawableBands();
		bandList.addDrawableBand(0, 1);
		bandList.addDrawableBand(1, 4);
		bandList.addDrawableBand(2, 3);

		bandList.clearDrawableBands();
		bandList.addDrawableBand(0, 3);
		bandList.addDrawableBand(1, 2);
		bandList.addDrawableBand(2, 4);
		bandList.addDrawableBand(3, 4);
		if (showCode)
			print();
	}

//	private void test2() {
//		assertEquals(bandList.getBandCount(), 5);
//		assertEquals(bandList.getDrawableBandsCount(), 4);
//		for (int i = 0; i < bandList.getBandCount(); i++) {
//
//			assertEquals((((Band) bandList.getBand(i)).getDataType()), 0);
//			assertEquals((((Band) bandList.getBand(i)).getFileName()), "fileName");
//			assertEquals((((Band) bandList.getBand(i)).getPosition()), i);
//		}
//	}

	/**
	 * Muestra la lista de bandas en modo texto
	 */
	public void print() {
		System.out.println("BandCount: " + bandList.getBandCount());
		System.out.println("DrawableBandsCount: " + bandList.getDrawableBandsCount());
		for (int i = 0; i < bandList.getBandCount(); i++) {
			System.out.println("");
			System.out.println("***********************");
			System.out.println("Band: " + i);
			System.out.println("DataType: " + ((Band) bandList.getBand(i)).getDataType());
			System.out.println("FileName: " + ((Band) bandList.getBand(i)).getFileName());
			System.out.println("Position: " + ((Band) bandList.getBand(i)).getPosition());
			if (((Band) bandList.getBand(i)).getBufferBandListToDraw() != null) {
				System.out.print("Band Dst: ");
				for (int j = 0; j < ((Band) bandList.getBand(i)).getBufferBandListToDraw().length; j++)
					System.out.print(((Band) bandList.getBand(i)).getBufferBandListToDraw()[j] + " ");
			}
		}
	}
}
