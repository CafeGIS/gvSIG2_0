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
package org.gvsig.raster.buffer.cache;

import junit.framework.TestCase;

import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.dataset.IBuffer;
/**
 * Test para comprobar la inicialización de la caché. El constructor de CacheStruct es el 
 * encargado de crear la estructura de cache. Esta clase comprueba que esta creación
 * sea correcta. Comprueba altura de página, número de estas, y tamaño de las mismas, así
 * como el de la cache completa.
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class TestStructCache extends TestCase {
	private boolean     showCode = false;
	private CacheStruct cs       = null;

	static {
		RasterLibrary.wakeUp();
	}

	public void start() {
		this.setUp();
		this.testStack();
	}

	public void setUp() {
		System.err.println("TestStructCache running...");
	}
	
	public void testStack(){
		// Test para los siguientes valores
		// Caché size = 20M
		// Pags Per Group = 5
		// Page size = 2M

		cs = new CacheStruct(3, IBuffer.TYPE_BYTE, 2048, 2048, 20, 2);
		if (showCode)
			print(cs, "Size = 20M; Pags Per Group = 5; Page size = 2M", 2048, 2048, IBuffer.TYPE_BYTE);
		assertEquals(cs.getHPag(), 256);
		assertEquals(cs.getNBands(), 3);
		assertEquals(cs.getCacheSize(), 23592960);
		assertEquals(cs.getPagsPerGroup(), 5);
		assertEquals(cs.getPagSize(), 1572864);
		assertEquals(cs.getNPags(), 15);
		assertEquals(cs.getNGroups(), 3);
		if (showCode)
			System.out.println("**********************************************");

		cs = new CacheStruct(4, IBuffer.TYPE_INT, 5000, 5000, 20, 2);
		if (showCode)
			print(cs, "Size = 20M; Pags Per Group = 5; Page size = 2M", 5000, 5000, IBuffer.TYPE_INT);
		assertEquals(cs.getHPag(), 16);
		assertEquals(cs.getNBands(), 4);
		assertEquals(cs.getCacheSize(), 25600000);
		assertEquals(cs.getPagsPerGroup(), 5);
		assertEquals(cs.getPagSize(), 1280000);
		assertEquals(cs.getNPags(), 20);
		assertEquals(cs.getNGroups(), 4);
		if (showCode)
			System.out.println("**********************************************");

		cs = new CacheStruct(3, IBuffer.TYPE_FLOAT, 42000, 42000, 20, 2);
		if (showCode)
			print(cs, "Size = 20M; Pags Per Group = 5; Page size = 2M", 42000, 42000, IBuffer.TYPE_FLOAT);
		assertEquals(cs.getHPag(), 4);
		assertEquals(cs.getNBands(), 3);
		assertEquals(cs.getCacheSize(), 20160000);
		assertEquals(cs.getPagsPerGroup(), 5);
		assertEquals(cs.getPagSize(), 2016000);
		assertEquals(cs.getNPags(), 10);
		assertEquals(cs.getNGroups(), 2);
		if (showCode)
			System.out.println("**********************************************");

		cs = new CacheStruct(1, IBuffer.TYPE_FLOAT, 105000, 105000, 20, 2);
		if (showCode)
			print(cs, "Size = 20M; Pags Per Group = 5; Page size = 2M", 105000, 105000, IBuffer.TYPE_FLOAT);
		assertEquals(cs.getHPag(), 4);
		assertEquals(cs.getNBands(), 1);
		assertEquals(cs.getCacheSize(), 25200000);
		assertEquals(cs.getPagsPerGroup(), 5);
		assertEquals(cs.getPagSize(), 1680000);
		assertEquals(cs.getNPags(), 15);
		assertEquals(cs.getNGroups(), 3);
		if (showCode)
			System.out.println("**********************************************");
	}
	
	public void print(CacheStruct cs, String initValues, int w, int h, int type) {
		System.out.println(initValues);
		System.out.println("W x H = " + w + " x " + h);
		cs.show();
	}
}