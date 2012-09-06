package org.gvsig.remoteClient;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.gvsig.remoteClient.wcs.wcs_1_0_0.WCSProtocolHandler_1_0_0Test;
import org.gvsig.remoteClient.wfs.wfs_1_0_0.WFSProtocolHandlerTest1_0_0;
import org.gvsig.remoteClient.wfs.wfs_1_1_0.WFSProtocolHandlerTest1_1_0;
import org.gvsig.remoteClient.wms.WMSStatusTest;
import org.gvsig.remoteClient.wms.wms_1_1_1.WMSProtocolHandler_1_1_1Test;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.gvsig.remoteClient");
		//$JUnit-BEGIN$
		suite.addTestSuite(Test.class);
		
		//commented until there is a way to simulate the needed servers.
		//suite.addTestSuite(URLRetrieveTest.class);
		suite.addTestSuite(WCSProtocolHandler_1_0_0Test.class);
		suite.addTestSuite(WFSProtocolHandlerTest1_0_0.class);
		suite.addTestSuite(WFSProtocolHandlerTest1_1_0.class);
		suite.addTestSuite(WMSProtocolHandler_1_1_1Test.class);
		suite.addTestSuite(WMSStatusTest.class);
		
		//$JUnit-END$
		return suite;
	}

}
