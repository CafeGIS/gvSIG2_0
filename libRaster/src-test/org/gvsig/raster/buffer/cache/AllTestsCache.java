package org.gvsig.raster.buffer.cache;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTestsCache {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.gvsig.fmap.cache");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestRasterReadOnlyBuffer.class);
		suite.addTestSuite(TestRasterCache.class);
		suite.addTestSuite(TestSaveAndLoadPages.class);
		suite.addTestSuite(TestStructCache.class);
		suite.addTestSuite(TestWorldCoordHDDPages.class);
		suite.addTestSuite(TestAssignInterchangeBandsCache.class);
		//suite.addTestSuite(TestReadOnlyCache.class);
		//$JUnit-END$
		return suite;
	}

}
