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
package org.gvsig.rastertools.clipping;

import java.awt.geom.Point2D;

import org.gvsig.raster.buffer.BufferInterpolation;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.MultiRasterDataset;

/**
 * Test de pruebas del proceso de recorte.
 * Este test recorta una imagen e interpola por los métodos bilinear, bspline y distancia inversa. 
 * Finalmente compara el resultado con un resultado esperado.
 * https://gvsig.org/web/docdev/docs/desarrollo/plugins/raster-tools/funcionalidades/recorte-de-raster/caracteristicas?portal_status_message=Changes%20saved.
 * 
 * 07/05/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class InterpolateClippingProcessTest extends ClippingBaseTest {
		
		/*
		 * (non-Javadoc)
		 * @see junit.framework.TestCase#setUp()
		 */
		public void setUp() {
			resetTime();
			System.err.println("*************************************************");
			System.err.println("*** InterpolateClippingProcessTest running... ***");
			System.err.println("*************************************************");
		}
		
		public void testStack() {
			openLayer(byteImg);
			
			//Interpolación bilinear al doble de la resolución. 
			clipping(new int[]{0}, false, BufferInterpolation.INTERPOLATION_Bilinear, getColorInterpretation(1), 200, 200);
			MultiRasterDataset d1 = open(clipBilinear);
			MultiRasterDataset d2 = open(out + ".tif");
			compareDatasets(d1, d2, new Point2D.Double(0, 0), 1, new int[]{0}, IBuffer.TYPE_BYTE);
			dataset.close();
			d1.close();
			d2.close();
			
			//Interpolación distancia inversa al doble de la resolución. 
			clipping(new int[]{0}, false, BufferInterpolation.INTERPOLATION_InverseDistance, getColorInterpretation(1), 200, 200);
			d1 = open(clipDistInv);
			d2 = open(out + ".tif");
			compareDatasets(d1, d2, new Point2D.Double(0, 0), 1, new int[]{0}, IBuffer.TYPE_BYTE);
			dataset.close();
			d1.close();
			d2.close();
			
			//Interpolación b spline al doble de la resolución. 
			clipping(new int[]{0}, false, BufferInterpolation.INTERPOLATION_BSpline, getColorInterpretation(1), 200, 200);
			d1 = open(clipBSpline);
			d2 = open(out + ".tif");
			compareDatasets(d1, d2, new Point2D.Double(0, 0), 1, new int[]{0}, IBuffer.TYPE_BYTE);
			dataset.close();
			d1.close();
			d2.close();
			
			if(lyr != null)
				lyr.removeLayerListener(null);
			
			System.err.println("*************************************************");
			System.err.println("*** Time:" + getTime());
			System.err.println("*** InterpolateClippingProcessTest ending...  ***");
			System.err.println("*************************************************");	
			
		}
		
}
