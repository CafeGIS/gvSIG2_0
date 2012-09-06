/* gvSIG. Sistema de Informaciï¿½n Geogrï¿½fica de la Generalitat Valenciana
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
package org.gvsig.raster.dataset.io;

import junit.framework.TestCase;

import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.dataset.GeoRasterWriter;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.Params;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.dataset.Params.Param;

/**
 * Prueba para la inserción y extracción de parámetros de los driver de escritura.
 * Lee primero los valores asignados en la inicialización y luego hace algunas
 * variaciones comprobando que los resultados sean correctos.
 *
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class TestWriterParams extends TestCase {
	private GeoRasterWriter grw = null;
	private Params wp = null;

	public void start() {
		this.setUp();
		this.testStack();
	}

	static {
		RasterLibrary.wakeUp();
	}

	public void setUp() {
		System.err.println("TestWriterParams running...");
		//Extensión gestionada por gdal
		try {
			grw = GeoRasterWriter.getWriter("prueba.tif");
		} catch (NotSupportedExtensionException e) {
			e.printStackTrace();
		} catch (RasterDriverException e) {
			e.printStackTrace();
		}
		wp = grw.getParams();
	}

	public void testStack(){
		Param param;
//		String blockSize = "512";
//		Param param = wp.getParamById("blocksize");
//		blockSize = param.list[((Integer) param.defaultValue).intValue()];

		String photometric = "RGB";
		param = wp.getParamById("photometric");
		photometric = param.list[((Integer) param.defaultValue).intValue()];

		String interleave = "BAND";
		param = wp.getParamById("interleave");
		interleave = param.list[((Integer) param.defaultValue).intValue()];

		String compression = "NONE";
		param = wp.getParamById("compression");
		compression = param.list[((Integer) param.defaultValue).intValue()];

		String tfw = (String) wp.getParamById("tfw").defaultValue.toString();

//		assertEquals(blockSize, "512");
//		assertEquals(georef, "true");
		assertEquals(photometric, "RGB");
		assertEquals(interleave, "BAND");
		assertEquals(compression, "NONE");
		assertEquals(tfw, "false");

		wp.changeParamValue("blocksize", "6"/*"256"*/);
		//wp.changeParamValue("georef", "false");
		wp.changeParamValue("photometric", "1"/*"MINISBLACK"*/);
		wp.changeParamValue("interleave", "1"/*"PIXEL"*/);
		wp.changeParamValue("compression", "0"/*LZW*/);
		wp.changeParamValue("tfw", "true");

//		param = wp.getParamById("blocksize");
//		blockSize = param.list[((Integer)param.defaultValue).intValue()];
		param = wp.getParamById("photometric");
		photometric = param.list[Integer.parseInt((String) param.defaultValue)];
		param = wp.getParamById("interleave");
		interleave = param.list[Integer.parseInt((String) param.defaultValue)];
		param = wp.getParamById("compression");
		compression = param.list[Integer.parseInt((String) param.defaultValue)];
		tfw = (String) wp.getParamById("tfw").defaultValue.toString();

//		assertEquals(blockSize, "256");
//		assertEquals(georef, "false");
		assertEquals(photometric, "MINISBLACK");
		assertEquals(interleave, "PIXEL");
		assertEquals(compression, "LZW");
		assertEquals(tfw, "true");
	}
}
