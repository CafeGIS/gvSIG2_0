/* gvSIG. Sistema de Informaciï¿½n Geogrï¿½fica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
package org.gvsig.raster.grid.filter;

import junit.framework.TestCase;

import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.buffer.RasterBuffer;
import org.gvsig.raster.dataset.FileNotOpenException;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.RasterDataset;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.grid.filter.enhancement.BrightnessContrastListManager;
import org.gvsig.raster.grid.filter.enhancement.EnhancementStretchListManager;
import org.gvsig.raster.grid.filter.enhancement.LinearStretchEnhancementFilter;
import org.gvsig.raster.grid.filter.enhancement.LinearStretchParams;
/**
 * Dada una pila de filtros con filtros de tipos heterogeneos. Elimina uno del 
 * medio y ejecuta controlTypes de RasterFilterListManager. Después comprueba 
 * que los tipo siguen siendo correctos.
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class TestControlTypes extends TestCase{

	private RasterFilterList		list = new RasterFilterList();
	private String baseDir = "./test-images/";
	private String path1 = baseDir + "miniRaster28x25F32.tif";
	private RasterDataset f1 = null;

	static{
		RasterLibrary.wakeUp();
	}

	public void setUp() {
		System.err.println("TestControlTypes running...");
		try {
			f1 = RasterDataset.open(null, path1);
		} catch (NotSupportedExtensionException e) {
			e.printStackTrace();
		} catch (RasterDriverException e) {
			e.printStackTrace();
		}
	}

	public void start() {
		this.setUp();
		this.testStack();
	}

	public void testStack() {
		IBuffer buf = RasterBuffer.getBuffer(IBuffer.TYPE_DOUBLE, 10, 10, 3, true);
		list.setInitRasterBuf(buf);
		RasterFilterListManager manager = new RasterFilterListManager(list);
		EnhancementStretchListManager m0 = (EnhancementStretchListManager) manager.getManagerByClass(EnhancementStretchListManager.class);
		BrightnessContrastListManager m1 = (BrightnessContrastListManager)manager.getManagerByClass(BrightnessContrastListManager.class);

		try {
			// Metemos Short, Short, Byte, Byte
			list.clear();
			buf = RasterBuffer.getBuffer(IBuffer.TYPE_SHORT, 10, 10, 3, true);
			list.setInitRasterBuf(buf);
			m1.addContrastFilter(12);
			m1.addBrightnessFilter(10);
			try {
				m0.addEnhancedStretchFilter(LinearStretchParams.createStandardParam(new int[] { 0,	1, 2 }, 0.0, f1.getStatistics(), true), f1.getStatistics(), new int[] { 0,	1, 2 }, false);
			} catch (FileNotOpenException e) {
				e.printStackTrace();
			} catch (RasterDriverException e) {
				e.printStackTrace();
			}
			m1.addBrightnessFilter(10);

			// Eliminamos el filtro de realce
			list.remove(LinearStretchEnhancementFilter.names[0]);
			manager.controlTypes();

			// Ahora los tres que quedan deben ser byte
			for (int i = 0; i < list.lenght(); i++)
				assertEquals(list.getOutDataType(), IBuffer.TYPE_BYTE);
		} catch (FilterTypeException exc) {

		}
	}
}