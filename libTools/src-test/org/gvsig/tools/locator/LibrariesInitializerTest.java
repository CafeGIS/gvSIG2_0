package org.gvsig.tools.locator;

import junit.framework.TestCase;

import org.gvsig.tools.ToolsLocator;

public class LibrariesInitializerTest extends TestCase{
	public void testInitializer(){
		new LibrariesInitializer().initializeAndPostinitializeAll();
		
		assertNotNull(ToolsLocator.getOperationManager());
	}
}
