package org.gvsig.tools.persistence.impl;

import org.gvsig.tools.persistence.PersistentState;

public abstract class AbstractPersistentState implements PersistentState {
	
	public abstract String getTheClassName();
	
	public abstract void setTheClass(String className);

}
