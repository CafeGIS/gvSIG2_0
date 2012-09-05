package org.gvsig.i18n;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.gvsig.i18n");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestMessages.class);
		//$JUnit-END$
		return suite;
	}

}
