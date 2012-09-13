/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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
package org.gvsig.rastertools.filters;

import java.util.ArrayList;

import org.gvsig.raster.beans.previewbase.ParamStruct;
import org.gvsig.raster.buffer.BufferFactory;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.IRasterDataSource;
import org.gvsig.raster.dataset.Params;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.grid.filter.RasterFilter;
import org.gvsig.raster.grid.filter.enhancement.BrightnessContrastListManager;
import org.gvsig.raster.grid.filter.enhancement.BrightnessFilter;
import org.gvsig.raster.util.process.FilterProcess;
import org.gvsig.rastertools.BaseTest;
/**
 * Test para comprobar el buen funcionamiento del panel de filtros
 * 
 * @version 07/05/2008
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class FilterProcessTest extends BaseTest {

	/*
	 * (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	public void setUp() {
		resetTime();
		System.err.println("************************************");
		System.err.println("*** FilterProcessTest running... ***");
		System.err.println("************************************");
	}

	public void testStack(){
		testLayer1();

		System.err.println("************************************");
		System.err.println("*** Time:" + getTime());
		System.err.println("*** FilterProcessTest ending...  ***");
		System.err.println("************************************");
	}
	
	protected void filtering(String file, int up, int center, int down) throws InterruptedException, RasterDriverException {
		openLayer(file);
		FilterProcess filterProcess = new FilterProcess();
		filterProcess.addParam("rendering", null);
		filterProcess.addParam("filename", getFileTemp() + ".tif");
		filterProcess.addParam("rasterdatasource", lyr.getDataSource());
		ArrayList listFilterUsed = new ArrayList();
		RasterFilter filter = BrightnessContrastListManager.createBrightnessFilter(100);
		filter.pre();
		try {
			Params params = (Params) filter.getUIParams(filter.getName()).clone();
			// Añado el parametro RenderBands a los parametros del filtro
			String rgb = lyr.getRender().getRenderBands()[0] + " " + lyr.getRender().getRenderBands()[1] + " " + lyr.getRender().getRenderBands()[2];
			params.setParam("RenderBands", rgb, 0, null);
			
			ParamStruct newParam = new ParamStruct();
			newParam.setFilterClass(BrightnessFilter.class);
			newParam.setFilterName(filter.getName());
			newParam.setFilterParam(params);
			listFilterUsed.add(newParam);
		} catch (CloneNotSupportedException e) {
		}
		
		filterProcess.addParam("listfilterused", listFilterUsed);
		try {
			filterProcess.execute();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		openLayer(out + ".tif");
		
		IRasterDataSource dataSource = lyr.getDataSource();
		BufferFactory bufferFactory = new BufferFactory(dataSource);
		bufferFactory.setDrawableBands(new int[]{0});
		bufferFactory.setAreaOfInterest();
		IBuffer buffer = bufferFactory.getRasterBuf();

		float value;
		switch (buffer.getDataType()) {
			case IBuffer.TYPE_BYTE:
				value = buffer.getElemByte(0, 0, 0);
				assertEquals((int) value & 0xff, up);
				System.out.println((int) value & 0xff);
				value = buffer.getElemByte(buffer.getHeight() >> 1, buffer.getWidth() >> 1, 0);
				assertEquals((int) value & 0xff, center);
				System.out.println((int) value & 0xff);
				value = buffer.getElemByte(buffer.getHeight() - 1, buffer.getWidth() - 1, 0);
				assertEquals((int) value & 0xff, down);
				System.out.println((int) value & 0xff);
				break;
		}
		System.out.println(out + ".tif");
	}
	
	public void testLayer1() {
		try {
			filtering(byteImg, 225, 143, 255);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (RasterDriverException e) {
			e.printStackTrace();
		}
	}
}