package org.gvsig.tools.persistence.impl;

import java.io.Reader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.gvsig.tools.dynobject.DynStruct;
import org.gvsig.tools.persistence.PersistenceException;
import org.gvsig.tools.persistence.PersistenceManager;
import org.gvsig.tools.persistence.Persistent;
import org.gvsig.tools.persistence.PersistentState;
import org.gvsig.tools.persistence.impl.validation.DefaultValidationResult;
import org.gvsig.tools.persistence.validation.ValidationResult;

public abstract class AbstractPersistenceManager implements PersistenceManager {
	protected HashMap definitions = new HashMap();
	protected Map alias;
	
	public PersistentState getState(Persistent obj)
			throws PersistenceException {
		PersistentState state = this.createStateInstance();
		obj.saveToState(state);
		return state;
	}

	protected AbstractPersistenceManager() {
		alias = new HashMap();
	}

	public void addAlias(String name, Class aClass) {
		alias.put(name, aClass);
	}
	
	public abstract PersistentState createStateInstance();

	public Object create(PersistentState state) throws PersistenceException {
		try {
			AbstractPersistentState myState = (AbstractPersistentState) state;
			String className = myState.getTheClassName();
			if (className == null) {
				throw new PersistenceException("The class name is not stored in the state.");
			}
			try {
				Class theClass;

				Object x = alias.get(className);
				if (x instanceof Class) {
					theClass = (Class) x;
				} else if (x instanceof String) {
					theClass = Class.forName((String) x);
				} else { // x is null
					theClass = Class.forName(className);
				}
				Persistent obj = (Persistent) theClass.newInstance();
				obj.loadFromState(state);
				return obj;
			} catch (ClassNotFoundException e) {
				throw new PersistenceException(e);
			} catch (InstantiationException e) {
				throw new PersistenceException(e);
			} catch (IllegalAccessException e) {
				throw new PersistenceException(e);
			}

		}
		catch (ClassCastException ex) {
			throw new PersistenceException(ex);
		}
	}

	public void addDefinition(Class persistentClass, DynStruct definition) {
		definitions.put(persistentClass, definition);
	}

	public DynStruct getDefinition(Class persistentClass) {
		return (DynStruct) definitions.get(persistentClass);
	}

	public Iterator getPersistentClasses() {
		return definitions.keySet().iterator();
	}

	public void removeDefinition(Class persistentClass) {
		definitions.remove(persistentClass);
	}

	public ValidationResult validate(PersistentState state) {
		return new DefaultValidationResult();
	}

	public PersistentState loadState(Reader reader) throws PersistenceException {
		PersistentState state = createStateInstance();
		state.load(reader);
		return state;
	}

	public int getAutoValidation() {
		return DISABLED;
	}

	public void setAutoValidation(int validationMode) throws PersistenceException {
		if (validationMode!=DISABLED) {
			throw new PersistenceException("Validation not implemented");
		}
	}
}
