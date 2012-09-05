package org.gvsig.fmap.dal;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.gvsig.fmap.dal.commands.CommandTest;
import org.gvsig.fmap.dal.feature.FeatureTest;

public class AllTests extends TestCase{
	public static Test suite() {
		TestSuite suite = new TestSuite("Test for libDataSource");
		//$JUnit-BEGIN$
		suite.addTestSuite(CommandTest.class);
		suite.addTestSuite(DataStoreTest.class);
		suite.addTestSuite(FeatureTest.class);

		//$JUnit-END$
		return suite;
	}
}