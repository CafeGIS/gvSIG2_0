package com.iver.cit.gvsig.wms;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.iver.cit.gvsig.fmap.layers.SequenceDimensionTest;
import com.iver.cit.gvsig.fmap.layers.TimeDimensionTest;
import com.iver.cit.gvsig.wmc.WebMapContextTest;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for com.iver.cit.gvsig.wms");
		//$JUnit-BEGIN$
		suite.addTestSuite(SequenceDimensionTest.class);
		suite.addTestSuite(TimeDimensionTest.class);
		suite.addTestSuite(WebMapContextTest.class);
		//$JUnit-END$
		return suite;
	}

}
