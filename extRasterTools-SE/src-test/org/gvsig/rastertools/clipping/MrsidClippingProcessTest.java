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
 * Test de pruebas del proceso de recorte
 * https://gvsig.org/web/docdev/docs/desarrollo/plugins/raster-tools/funcionalidades/recorte-de-raster/caracteristicas?portal_status_message=Changes%20saved.
 * 
 * 07/05/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class MrsidClippingProcessTest extends ClippingBaseTest {

		/*
		 * (non-Javadoc)
		 * @see junit.framework.TestCase#setUp()
		 */
		public void setUp() {
			resetTime();
			System.err.println("******************************************");
			System.err.println("***MrSID ClippingProcessTest running...***");
			System.err.println("******************************************");
		}
		
		public void testStack() {
			openLayer(mrsid);
			
			//lectura de todas las bandas en un raster
			clipping(new int[]{0, 1, 2}, false, BufferInterpolation.INTERPOLATION_NearestNeighbour, getColorInterpretation(3));
			dataset = open(out + ".tif");
			compareDatasets(lyr.getDataSource(), dataset, new Point2D.Double(coords[0], coords[3]), 1, new int[]{0, 1, 2}, IBuffer.TYPE_BYTE);
			dataset.close();
			
			//Lectura de bandas en raster separados
			clipping(new int[]{0, 1, 2}, true, BufferInterpolation.INTERPOLATION_NearestNeighbour, getColorInterpretation(1));
			MultiRasterDataset d0 = open(out + "_B0.tif");
			compareDatasets(0, lyr.getDataSource(), d0, new Point2D.Double(coords[0], coords[3]), 1, new int[]{0, 1, 2}, IBuffer.TYPE_BYTE);
			d0.close();
			MultiRasterDataset d1 = open(out + "_B1.tif");
			compareDatasets(1, lyr.getDataSource(), d1, new Point2D.Double(coords[0], coords[3]), 1, new int[]{0, 1, 2}, IBuffer.TYPE_BYTE);
			d1.close();
			MultiRasterDataset d2 = open(out + "_B2.tif");
			compareDatasets(2, lyr.getDataSource(), d2, new Point2D.Double(coords[0], coords[3]), 1, new int[]{0, 1, 2}, IBuffer.TYPE_BYTE);
			d2.close();	
			
			//Doblar la resolución por vecino más próximo
			clipping(new int[]{0, 1, 2}, false, BufferInterpolation.INTERPOLATION_NearestNeighbour, getColorInterpretation(3), (coords[2] - coords[0]) * 2, (coords[1] - coords[3]) * 2);
			dataset = open(out + ".tif");
			compareDatasets(lyr.getDataSource(), dataset, new Point2D.Double(coords[0], coords[3]), 2, new int[]{0, 1, 2}, IBuffer.TYPE_BYTE);
			dataset.close();
			
			if(lyr != null)
				lyr.removeLayerListener(null);
			
			System.err.println("******************************************");
			System.err.println("*** Time:" + getTime());
			System.err.println("***MrSID ClippingProcessTest ending... ***");
			System.err.println("******************************************");	
		}
}
