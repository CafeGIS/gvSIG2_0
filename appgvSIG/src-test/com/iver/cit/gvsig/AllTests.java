package com.iver.cit.gvsig;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.iver.cit.gvsig.gui.filter.TestFilterExpressionFromWhereIsEmpty_Method;
import com.iver.cit.gvsig.panelGroup.Test2ExceptionsUsingTabbedPanel;
import com.iver.cit.gvsig.panelGroup.Test2ExceptionsUsingTreePanel;
import com.iver.cit.gvsig.panelGroup.TestPanelGroupLoaderFromExtensionPoint;
import com.iver.cit.gvsig.project.ProjectTest;
import com.iver.cit.gvsig.project.TableTest;
import com.iver.cit.gvsig.sqlQueryValidation.TestSQLQueryValidation;
import com.iver.cit.gvsig.test.Persistence;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for com.iver.cit.gvsig");
		//$JUnit-BEGIN$
		suite.addTestSuite(Persistence.class);
		suite.addTestSuite(ProjectTest.class);
		suite.addTestSuite(TableTest.class);
		suite.addTestSuite(TestFilterExpressionFromWhereIsEmpty_Method.class);
		suite.addTestSuite(TestSQLQueryValidation.class);
		suite.addTestSuite(TestPanelGroupLoaderFromExtensionPoint.class);
		suite.addTestSuite(Test2ExceptionsUsingTabbedPanel.class);
		suite.addTestSuite(Test2ExceptionsUsingTreePanel.class);
		
		//$JUnit-END$
		return suite;
	}

}
