package org.gvsig.gui.beans;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.gvsig.gui.beans.panelGroup.Test1ExceptionsUsingTabbedPanel;
import org.gvsig.gui.beans.panelGroup.Test1ExceptionsUsingTreePanel;
import org.gvsig.gui.beans.panelGroup.TestPanelGroupLoaderFromList;
import org.gvsig.gui.beans.panelGroup.TestPanelGroupManager;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.gvsig.gui.beans");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestPanelGroupLoaderFromList.class);
		suite.addTestSuite(TestPanelGroupManager.class);
		suite.addTestSuite(Test1ExceptionsUsingTabbedPanel.class);
		suite.addTestSuite(Test1ExceptionsUsingTreePanel.class);
		//$JUnit-END$
		return suite;
	}

}
