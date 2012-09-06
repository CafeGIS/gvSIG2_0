package org.cresques.impl;

import org.cresques.impl.cts.ProjectionPool;
import org.gvsig.fmap.crs.CRSFactory;
import org.gvsig.tools.locator.BaseLibrary;

public class CresquesCtsLibrary extends BaseLibrary {

	public void initialize() {
		super.initialize();
		CRSFactory.cp = new ProjectionPool();
	}
}
