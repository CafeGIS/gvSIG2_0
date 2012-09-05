package org.gvsig.tools.evaluator.sqljep;

import org.gvsig.tools.evaluator.sqljep.function.Boundary;
import org.gvsig.tools.evaluator.sqljep.function.Equals;
import org.gvsig.tools.evaluator.sqljep.function.GeomFromText;
import org.gvsig.tools.evaluator.sqljep.function.Intersects;
import org.gvsig.tools.evaluator.sqljep.function.Overlaps;
import org.gvsig.tools.locator.BaseLibrary;
import org.gvsig.tools.locator.ReferenceNotRegisteredException;
import org.medfoster.sqljep.BaseJEP;

public class SQLJEPLibrary extends BaseLibrary {

	public void initialize() throws ReferenceNotRegisteredException {
		super.initialize();
	}

	public void postInitialize() throws ReferenceNotRegisteredException {
		super.postInitialize();
		BaseJEP.addFunction(Equals.NAME, new Equals());
		BaseJEP.addFunction(GeomFromText.NAME, new GeomFromText());
		BaseJEP.addFunction(Intersects.NAME, new Intersects());
		BaseJEP.addFunction(Overlaps.NAME, new Overlaps());
		BaseJEP.addFunction(Boundary.NAME, new Boundary());
	}
}
