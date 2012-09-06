package org.gvsig.raster.grid;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTestsGrid {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.gvsig.fmap.grid");
		//$JUnit-BEGIN$
		suite.addTestSuite(GridInstanciationTest.class);
		suite.addTestSuite(TGEmptyBufferForWrite.class);
		suite.addTestSuite(TGReadingFullDatasource.class);
		suite.addTestSuite(TGReadingFullDatasourceSelectingBands.class);
		suite.addTestSuite(TGOperations.class);
		//$JUnit-END$
		return suite;
	}

}
