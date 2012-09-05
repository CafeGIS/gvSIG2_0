package com.iver.utiles;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.iver.utiles.search.TestBinarySearchUsingFirstCharacters;
import com.iver.utiles.stringNumberUtilities.TestStringNumberUtilities;
import com.iver.utiles.vectorUtilities.TestVectorUtilities;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for com.iver.utiles");
		//$JUnit-BEGIN$
		suite.addTestSuite(DoubleUtilitiesTest.class);
		suite.addTestSuite(TestBinarySearchUsingFirstCharacters.class);
//		suite.addTestSuite(TestExtensionPoint.class);
//		suite.addTestSuite(TestExtensionPoints.class);
		suite.addTestSuite(TestStringNumberUtilities.class);
		suite.addTestSuite(TestStringUtilities.class);
		suite.addTestSuite(TestVectorUtilities.class);
		suite.addTestSuite(TestXMLEntity.class);
		suite.addTestSuite(TestXMLEntityPersistenceManager.class);
		//$JUnit-END$
		return suite;
	}

}
