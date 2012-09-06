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

import junit.framework.Test;
import junit.framework.TestSuite;

import org.gvsig.raster.buffer.TDSDoubleAdjustToExtentBufferGdal;
import org.gvsig.raster.buffer.TDSDoubleAdjustToExtentEcw;
import org.gvsig.raster.buffer.TDSDoubleAdjustToExtentGdal;
import org.gvsig.raster.buffer.TDSIntBufferGdal;
import org.gvsig.raster.buffer.TDSIntEcw;
import org.gvsig.raster.buffer.TDSIntGdal;
import org.gvsig.raster.buffer.TestNotAdjustToExtent;
import org.gvsig.raster.buffer.TestBufferInterpolation;
import org.gvsig.raster.buffer.TestDataSourceMultiFile;
import org.gvsig.raster.buffer.TestGdalByteHistogram;
import org.gvsig.raster.buffer.TestGdalByteMultiBandHistogram;
import org.gvsig.raster.buffer.TestGdalFloatHistogram;
import org.gvsig.raster.buffer.cache.TestAssignInterchangeBandsCache;
import org.gvsig.raster.buffer.cache.TestRasterCache;
import org.gvsig.raster.buffer.cache.TestRasterReadOnlyBuffer;
import org.gvsig.raster.buffer.cache.TestSaveAndLoadPages;
import org.gvsig.raster.buffer.cache.TestStructCache;
import org.gvsig.raster.buffer.cache.TestWorldCoordHDDPages;
import org.gvsig.raster.dataset.TestBandList;
import org.gvsig.raster.dataset.TestDataByPixelEcw;
import org.gvsig.raster.dataset.TestDataByPixelGdal;
import org.gvsig.raster.dataset.TestDataByPixelMrSID;
import org.gvsig.raster.dataset.TestGetWindowRasterBufferSizeMrSID;
import org.gvsig.raster.dataset.TestGetWindowRasterMrSID;
import org.gvsig.raster.dataset.TestHistogramSerializer;
import org.gvsig.raster.dataset.TestReadBlockMrSID;
import org.gvsig.raster.dataset.TestReadLineEcw;
import org.gvsig.raster.dataset.TestReadLineGdal;
import org.gvsig.raster.dataset.TestReadLineMrSID;
import org.gvsig.raster.dataset.TestStatisticMultiFile;
import org.gvsig.raster.dataset.TestStatistics;
import org.gvsig.raster.dataset.io.TestGdalWriter;
import org.gvsig.raster.dataset.io.TestWriterParams;
import org.gvsig.raster.dataset.io.rmf.TestRmfRead;
import org.gvsig.raster.dataset.io.rmf.TestRmfWrite;
import org.gvsig.raster.datastruct.TestColorTable;
import org.gvsig.raster.grid.GridInstanciationTest;
import org.gvsig.raster.grid.TGEmptyBufferForWrite;
import org.gvsig.raster.grid.TGOperations;
import org.gvsig.raster.grid.TGReadingFullDatasource;
import org.gvsig.raster.grid.TGReadingFullDatasourceSelectingBands;
import org.gvsig.raster.grid.filter.TestControlTypes;
import org.gvsig.raster.grid.filter.TestRasterFilterList;
import org.gvsig.raster.grid.render.FormatArrayRenderTest;
import org.gvsig.raster.util.TestAdjustExtentToRotateRaster;
import org.gvsig.raster.util.TestIsInsideRaster;
import org.gvsig.raster.util.TransparencyRangeTest;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.gvsig.raster");
		//$JUnit-BEGIN$
		
		suite.addTestSuite(TestColorTable.class);

//		En linux y en windows dan valores distintos al pedir un ecw interpolado		
//		suite.addTestSuite(TDSDoubleAdjustToExtentBufferEcw.class);
	
		suite.addTestSuite(TDSDoubleAdjustToExtentBufferGdal.class);
		suite.addTestSuite(TDSDoubleAdjustToExtentEcw.class);
		suite.addTestSuite(TDSDoubleAdjustToExtentGdal.class);
		suite.addTestSuite(TDSIntEcw.class);
		suite.addTestSuite(TDSIntGdal.class);
		suite.addTestSuite(TDSIntBufferGdal.class);
		suite.addTestSuite(TestDataSourceMultiFile.class);

//    No se debe usar este test ya que los valores que da en los arrays son ilogicos
//		suite.addTestSuite(TestIOMemoryRasterDriver.class);

		suite.addTestSuite(TestGdalByteHistogram.class);
		suite.addTestSuite(TestHistogramSerializer.class);
		suite.addTestSuite(TestRmfRead.class);
		suite.addTestSuite(TestRmfWrite.class);
		suite.addTestSuite(TestRasterReadOnlyBuffer.class);
		suite.addTestSuite(TestGdalByteMultiBandHistogram.class);
		suite.addTestSuite(TestGdalFloatHistogram.class);

		suite.addTestSuite(TestDataByPixelEcw.class);
		suite.addTestSuite(TestReadLineEcw.class);
		suite.addTestSuite(TestStatisticMultiFile.class);
		suite.addTestSuite(TestStatistics.class);
		suite.addTestSuite(TestWriterParams.class);
		suite.addTestSuite(TestReadBlockMrSID.class);
		suite.addTestSuite(TestGetWindowRasterMrSID.class);
		suite.addTestSuite(TestGetWindowRasterBufferSizeMrSID.class);

		suite.addTestSuite(GridInstanciationTest.class);
		suite.addTestSuite(TGEmptyBufferForWrite.class);
		suite.addTestSuite(TGReadingFullDatasource.class);
		suite.addTestSuite(TGReadingFullDatasourceSelectingBands.class);
		suite.addTestSuite(TGOperations.class);

		suite.addTestSuite(TestRasterFilterList.class);

		suite.addTestSuite(FormatArrayRenderTest.class);
		
		/*
		 * Other Tests present in libRaster (cesar)
		 * Remove them from here and the src-test dir if they are not
		 * useful anymore.
		 */
		suite.addTestSuite(TestAdjustExtentToRotateRaster.class);
		suite.addTestSuite(TestNotAdjustToExtent.class);
		suite.addTestSuite(TestBandList.class);
		suite.addTestSuite(TestBufferInterpolation.class);
		suite.addTestSuite(TestControlTypes.class);
		suite.addTestSuite(TestDataByPixelGdal.class);
		suite.addTestSuite(TestDataByPixelMrSID.class);
		suite.addTestSuite(TestGdalWriter.class);
		suite.addTestSuite(TestReadLineGdal.class);
		suite.addTestSuite(TestReadLineMrSID.class);
		suite.addTestSuite(TestRasterReadOnlyBuffer.class);
		suite.addTestSuite(TestTemplate.class);
		suite.addTestSuite(TransparencyRangeTest.class);
		suite.addTestSuite(TestIsInsideRaster.class);

		suite.addTestSuite(TestRasterCache.class);
		suite.addTestSuite(TestSaveAndLoadPages.class);
		suite.addTestSuite(TestStructCache.class);
		suite.addTestSuite(TestWorldCoordHDDPages.class);
		suite.addTestSuite(TestAssignInterchangeBandsCache.class);
		
		//$JUnit-END$
		return suite;
	}
}