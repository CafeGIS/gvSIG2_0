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
package org.gvsig.raster.grid;

import junit.framework.TestCase;

import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.buffer.RasterBufferInvalidException;
import org.gvsig.raster.dataset.IRasterDataSource;
import org.gvsig.raster.dataset.MultiRasterDataset;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.datastruct.Extent;

/**
 * 
 * 10/06/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class GridInstanciationTest extends TestCase {
	private String baseDir = "./test-images/";
	private String path1 = baseDir + "miniRaster28x25F32.tif";
	private String outImage = "prueba.asc";
	private IRasterDataSource f1 = null;
	private Grid grid = null; 
	
	static{
		RasterLibrary.wakeUp();
	}
	
	public void start(){
		this.setUp();
		this.testStack();
	}
	
	public void setUp() {
		System.err.println("GridInstanciationTest running...");
		try {
			f1 = MultiRasterDataset.open(null, path1);
			Extent ext = f1.getExtent();
			GridExtent windowExtent = new GridExtent(ext, 5, -5);		
			grid = new Grid(f1, new int[]{0, 1, 2}, windowExtent);
			
			grid.loadWriterData();
			grid.ExportToArcViewASCIIFile(outImage);
		} catch (NotSupportedExtensionException e) {
			e.printStackTrace();
		} catch (RasterDriverException e) {
			e.printStackTrace();
		} catch (RasterBufferInvalidException e) {
			e.printStackTrace();
		} catch (GridException e) {
			e.printStackTrace();
		}
	}
	
	public void testStack() {
		
	}
}
