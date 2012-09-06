package org.gvsig.raster.dataset;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.gvsig.raster.dataset.io.TestWriterParams;

public class AllTestsDriver {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.gvsig.fmap.driver");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestReadLineEcw.class);
		suite.addTestSuite(TestStatisticMultiFile.class);
		suite.addTestSuite(TestStatistics.class);
		suite.addTestSuite(TestDataByPixelEcw.class);
		suite.addTestSuite(TestWriterParams.class);
		suite.addTestSuite(TestHistogramSerializer.class);
		suite.addTestSuite(TestReadBlockMrSID.class);
		suite.addTestSuite(TestGetWindowRasterMrSID.class);
		suite.addTestSuite(TestGetWindowRasterBufferSizeMrSID.class);
		//$JUnit-END$
		return suite;
	}

}
