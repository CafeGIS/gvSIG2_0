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
package org.gvsig.rastertools;

import java.io.File;

import org.gvsig.fmap.mapcontext.exceptions.LoadLayerException;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.RasterDriverException;
/**
 * Clase base para todos los tests. Contiene métodos de uso común.
 * Los errores no se capturan. Se lanzan en una traza por consola.
 * 
 * 07/05/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class BaseTest extends org.gvsig.raster.BaseTestCase {
	/**
	 * Imagenes de test
	 */
	protected String               band1         = baseDir + "band1-30x28byte.tif";
	protected String               band2         = baseDir + "band2-30x28byte.tif";
	protected String               band3         = baseDir + "band3-30x28byte.tif";
	protected String               ecw           = baseDir + "ecwtest.ecw";
	protected String               mrsid         = baseDir + "mrsidtest.sid";
	protected String               byteImg       = baseDir + "byte.tif";
	protected String               shortImg      = baseDir + "short.tif";
	protected String               intImg        = baseDir + "integer.tif";
	protected String               floatImg      = baseDir + "float.tif";
	protected String               doubleImg     = baseDir + "double.tif";
	
	public FLyrRasterSE            lyr           = null;
	
	/**
	 * Carga una capa desde un fichero de disco.
	 * @param s
	 * @return
	 */
	protected FLyrRasterSE openLayer(String s) {
		try {
			lyr = FLyrRasterSE.createLayer("tempLayer", s, null);
		} catch (LoadLayerException e) {
			e.printStackTrace();
		}
		return lyr;
	}
	
	/**
	 * Carga una capa desde un fichero de disco.
	 * @param s
	 * @return
	 */
	protected FLyrRasterSE openLayer(File f) {
		try {
			lyr = FLyrRasterSE.createLayer("tempLayer", f, null);
		} catch (LoadLayerException e) {
			e.printStackTrace();
		}
		return lyr;
	}
	
	/**
	 * Carga una capa desde una lista de ficheros de disco.
	 * @param s
	 * @return
	 */
	protected FLyrRasterSE openLayer(String[] s) {
		try {
			if(s == null)
				return null;
			lyr = FLyrRasterSE.createLayer("tempLayer", s[0], null);
			for (int i = 1; i < s.length; i++)
				lyr.addFile(s[i]);
		} catch (LoadLayerException e) {
			e.printStackTrace();
		} catch (NotSupportedExtensionException e) {
			e.printStackTrace();
		} catch (RasterDriverException e) {
			e.printStackTrace();
		}
		return lyr;
	}
}