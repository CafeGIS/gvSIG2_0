package org.gvsig.tools.persistence.xmlentity;

import org.gvsig.tools.persistence.PersistenceException;
import org.gvsig.tools.persistence.Persistent;
import org.gvsig.tools.persistence.PersistentState;
import org.gvsig.tools.persistence.impl.AbstractPersistenceManager;

import com.iver.utiles.XMLEntity;

public class XMLEntityManager extends AbstractPersistenceManager {
	public PersistentState getState(Persistent obj) throws PersistenceException {
		XMLEntityState state = (XMLEntityState) super.getState(obj);
		state.setTheClass(obj.getClass().getName());
		return state;
	}
	
	public PersistentState getState(Object obj) throws PersistenceException {
		XMLEntityState state;
		if (obj instanceof Persistent) {
			state = (XMLEntityState) getState((Persistent)obj);
		}
		else {
			// perform things for Strings, PersistentStates, Iterators, arrays, etc
			state = createStateInstance();
			state.setTheClass(obj.getClass().getName());
		}
		return state;
	}
	
	public PersistentState createState(XMLEntity xmlEntity)
			throws PersistenceException {
		return new XMLEntityState(this, xmlEntity);
	}

	public XMLEntityState createStateInstance() {
		return new XMLEntityState(this);
	}

}
