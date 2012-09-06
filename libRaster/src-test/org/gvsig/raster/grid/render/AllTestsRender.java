package org.gvsig.raster.grid.render;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTestsRender {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.gvsig.fmap.grid.render");
		//$JUnit-BEGIN$
		suite.addTestSuite(FormatArrayRenderTest.class);
		//$JUnit-END$
		return suite;
	}

}
