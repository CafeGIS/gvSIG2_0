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

import java.awt.geom.AffineTransform;

import org.gvsig.raster.RasterProcess;
import org.gvsig.raster.buffer.WriterBufferServer;
import org.gvsig.raster.dataset.properties.DatasetColorInterpretation;
import org.gvsig.raster.util.process.ClippingProcess;
import org.gvsig.rastertools.BaseTest;

/**
 * Clase base para los tests de recorte
 * 08/05/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class ClippingBaseTest extends BaseTest {
	//ulx, lrx, lry, uly
	protected int[]                coords             = new int[]{100, 200, 200, 100};
	protected int                  pause              = 2000;
	
	/**
	 * Imagenes de test
	 */
	protected String               clipBilinear       = baseDir + "bilinear.tif";
	protected String               clipBSpline        = baseDir + "bSpline.tif";
	protected String               clipDistInv        = baseDir + "distInversa.tif";
	
	/**
	 * Proceso de recorte de la imagen en secuencial.
	 * @param drawBands
	 * @param onePerBand
	 * @param interp
	 */
	protected void clipping(int[] drawBands, boolean onePerBand, int interp, DatasetColorInterpretation ci, int resX, int resY) {
		RasterProcess clippingProcess = new ClippingProcess();
		clippingProcess.addParam("viewname", null);
		clippingProcess.addParam("pixelcoordinates", coords);
		clippingProcess.addParam("filename", getFileTemp());
		clippingProcess.addParam("datawriter", new WriterBufferServer());
		clippingProcess.addParam("layer", lyr);
		clippingProcess.addParam("drawablebands", drawBands);
		clippingProcess.addParam("onelayerperband", new Boolean(onePerBand));
		clippingProcess.addParam("interpolationmethod", new Integer(interp));
		clippingProcess.addParam("affinetransform", new AffineTransform());
		clippingProcess.addParam("colorInterpretation", ci);
		clippingProcess.addParam("resolution", new int[]{resX, resY});
		try {
			clippingProcess.execute();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Proceso de recorte de la imagen en secuencial.
	 * @param drawBands
	 * @param onePerBand
	 * @param interp
	 */
	protected ClippingProcess clippingProcess(int[] drawBands, boolean onePerBand, int interp, DatasetColorInterpretation ci, int resX, int resY, int[] coords) {
		ClippingProcess clippingProcess = new ClippingProcess();
		clippingProcess.addParam("viewname", null);
		clippingProcess.addParam("pixelcoordinates", coords);
		clippingProcess.addParam("filename", getFileTemp());
		clippingProcess.addParam("datawriter", new WriterBufferServer());
		clippingProcess.addParam("layer", lyr);
		clippingProcess.addParam("drawablebands", drawBands);
		clippingProcess.addParam("onelayerperband", new Boolean(onePerBand));
		clippingProcess.addParam("interpolationmethod", new Integer(interp));
		clippingProcess.addParam("affinetransform", new AffineTransform());
		clippingProcess.addParam("colorInterpretation", ci);
		clippingProcess.addParam("resolution", new int[]{resX, resY});
		return clippingProcess;
	}
	
	/**
	 * Proceso de recorte de la imagen en secuencial.
	 * @param drawBands
	 * @param onePerBand
	 * @param interp
	 */
	protected void clipping(int[] drawBands, boolean onePerBand, int interp, DatasetColorInterpretation ci) {
		clipping(drawBands, onePerBand, interp, ci, (coords[2] - coords[0]) + 1, (coords[1] - coords[3]) + 1);
	}
}
