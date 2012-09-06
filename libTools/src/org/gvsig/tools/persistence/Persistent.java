package org.gvsig.tools.persistence;

public interface Persistent {

	/**
	 * Saves the internal state of the object on the provided
	 * PersistentState object.
	 * 
	 * @param state
	 */
	public void saveToState(PersistentState state) throws PersistenceException;

	/**
	 * Set the state of the object from the state passed as parameter.
	 *
	 * @param state
	 */
	public void loadFromState(PersistentState state) throws PersistenceException;

}
