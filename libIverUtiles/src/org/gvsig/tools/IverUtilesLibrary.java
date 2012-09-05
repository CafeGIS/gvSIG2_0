package org.gvsig.tools;

import org.gvsig.tools.locator.BaseLibrary;
import org.gvsig.tools.locator.ReferenceNotRegisteredException;
import org.gvsig.tools.persistence.xmlentity.XMLEntityManager;


public class IverUtilesLibrary extends BaseLibrary {

	public void initialize() throws ReferenceNotRegisteredException {
		super.initialize();
		ToolsLocator.registerDefaultPersistenceManager(XMLEntityManager.class);
	}
	
	public void postInitialize() throws ReferenceNotRegisteredException {
		super.postInitialize();
	}
}
