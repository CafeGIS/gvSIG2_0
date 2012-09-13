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
package org.gvsig.raster;

import org.gvsig.raster.buffer.BufferInterpolation;
import org.gvsig.raster.util.process.ClippingProcess;
import org.gvsig.rastertools.clipping.ClippingBaseTest;

/**
 * Test de pruebas del proceso de recorte
 * https://gvsig.org/web/docdev/docs/desarrollo/plugins/raster-tools/funcionalidades/recorte-de-raster/caracteristicas?portal_status_message=Changes%20saved.
 * 
 * 07/05/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class UniqueProcessTest extends ClippingBaseTest {
		//ulx, lrx, lry, uly
		protected int[]                coords             = new int[]{0, 4000, 4000, 0};
		
		/*
		 * (non-Javadoc)
		 * @see junit.framework.TestCase#setUp()
		 */
		//public void setUp() {
		public static void main(String[] args) {
			System.err.println("******************************************");
			System.err.println("*** UniqueProcessTest running... ***");
			System.err.println("******************************************");
			UniqueProcessTest t = new UniqueProcessTest();
			t.test();
		}
		
		//public void testStack() {
		public void test() {
			openLayer(ecw);
			
			try {
				//lectura de todas las bandas en un raster
				ClippingProcess cp1 = clippingProcess(new int[]{0, 1, 2}, false, BufferInterpolation.INTERPOLATION_Bilinear, getColorInterpretation(3), (coords[2] - coords[0]), (coords[1] - coords[3]), coords);
				Thread.sleep(100);
				ClippingProcess cp2 = clippingProcess(new int[]{0, 1, 2}, false, BufferInterpolation.INTERPOLATION_Bilinear, getColorInterpretation(3), (coords[2] - coords[0]), (coords[1] - coords[3]), coords);
				Thread.sleep(100);
				ClippingProcess cp3 = clippingProcess(new int[]{0, 1, 2}, false, BufferInterpolation.INTERPOLATION_Bilinear, getColorInterpretation(3), (coords[2] - coords[0]), (coords[1] - coords[3]), coords);
				Thread.sleep(100);
				ClippingProcess cp4 = clippingProcess(new int[]{0, 1, 2}, false, BufferInterpolation.INTERPOLATION_Bilinear, getColorInterpretation(3), (coords[2] - coords[0]), (coords[1] - coords[3]), coords);
				UniqueProcessQueue queue = UniqueProcessQueue.getSingleton();
				queue.add(cp1);
				queue.add(cp2);
				queue.add(cp3);
				queue.add(cp4);
			} catch (Exception e) {
				e.printStackTrace();
			}

			System.err.println("******************************************");
			//System.err.println("*** Time:" + getTime());
			System.err.println("*** UniqueProcessTest ending...  ***");
			System.err.println("******************************************");	
			
		}
		
}
