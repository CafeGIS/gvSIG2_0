package org.cresques;

import org.gvsig.fmap.crs.CRSFactory;
import org.gvsig.tools.locator.BaseLibrary;
import org.gvsig.tools.locator.ReferenceNotRegisteredException;

public class ProjectionLibrary extends BaseLibrary {

	public void postInitialize() throws ReferenceNotRegisteredException {
		super.postInitialize();
		if (CRSFactory.cp==null) {
			// TODO build exception with right parameters
			throw new ReferenceNotRegisteredException("what", null);
		}
	}

}
