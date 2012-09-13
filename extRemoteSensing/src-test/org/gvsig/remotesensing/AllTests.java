package org.gvsig.remotesensing;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.gvsig.remotesensing.decisontrees.DecisionTreesTest;
import org.gvsig.remotesensing.processtest.TClassificationProcessTest;
import org.gvsig.remotesensing.processtest.TGridMathProcessTest;
import org.gvsig.remotesensing.processtest.TPCImageProcess;
import org.gvsig.remotesensing.processtest.TPCStatisticProcess;
import org.gvsig.remotesensing.processtest.TTasseledCapProcess;

public class AllTests extends TestCase {
	
	public static Test suite() {
		TestSuite suite = new TestSuite("Test for extRemoteSensing");
		//$JUnit-BEGIN$
		suite.addTestSuite(TGridMathProcessTest.class);
		suite.addTestSuite(TPCStatisticProcess.class);
		suite.addTestSuite(TPCImageProcess.class);
		suite.addTestSuite(TTasseledCapProcess.class);
		suite.addTestSuite(TClassificationProcessTest.class);
		suite.addTestSuite(DecisionTreesTest.class);
		//$JUnit-END$
		return suite;
	}

}
