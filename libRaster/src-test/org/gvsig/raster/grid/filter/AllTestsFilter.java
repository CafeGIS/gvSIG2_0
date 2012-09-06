package org.gvsig.raster.grid.filter;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTestsFilter {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.gvsig.fmap.grid.filter");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestRasterFilterList.class);
		suite.addTestSuite(TestControlTypes.class);
		//$JUnit-END$
		return suite;
	}

}
