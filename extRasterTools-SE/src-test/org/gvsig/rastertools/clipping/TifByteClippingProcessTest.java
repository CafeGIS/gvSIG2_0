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

/**
 * Test de pruebas del proceso de recorte
 * https://gvsig.org/web/docdev/docs/desarrollo/plugins/raster-tools/funcionalidades/recorte-de-raster/caracteristicas?portal_status_message=Changes%20saved.
 * 
 * 07/05/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class TifByteClippingProcessTest extends ClippingBaseTest {
		
		/*
		 * (non-Javadoc)
		 * @see junit.framework.TestCase#setUp()
		 */
		public void setUp() {
			resetTime();
			System.err.println("**********************************************");
			System.err.println("*** TifByte ClippingProcessTest running... ***");
			System.err.println("**********************************************");
		}
		
		public void testStack() {
			//lectura de todas las bandas en un raster
			openLayer(byteImg);
			clipping(new int[]{0}, false, BufferInterpolation.INTERPOLATION_NearestNeighbour, getColorInterpretation(1));
			dataset = open(out + ".tif");
			compareDatasets(lyr.getDataSource(), dataset, new Point2D.Double(coords[0], coords[3]), 1, new int[]{0}, IBuffer.TYPE_BYTE);
			dataset.close();
			
			//Doblar la resolución por vecino más próximo
			clipping(new int[]{0}, false, BufferInterpolation.INTERPOLATION_NearestNeighbour, getColorInterpretation(1), ((coords[2] - coords[0]) + 1) * 2, ((coords[1] - coords[3]) + 1) * 2);
			dataset = open(out + ".tif");
			compareDatasets(lyr.getDataSource(), dataset, new Point2D.Double(coords[0], coords[3]), 2, new int[]{0}, IBuffer.TYPE_BYTE);
			dataset.close();
			
			if(lyr != null)
				lyr.removeLayerListener(null);
			
			System.err.println("**********************************************");
			System.err.println("*** Time:" + getTime());
			System.err.println("*** TifByte ClippingProcessTest ending...  ***");
			System.err.println("**********************************************");	
		}
		
}
