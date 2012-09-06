package org.gvsig.tools.persistence;

import java.io.Reader;
import java.io.Writer;
import java.util.Iterator;

public interface PersistentState {
	/**
	 * Gets the name of the class corresponding to this persistent state
	 *
	 * @return The class name of the class represented by this state
	 */
	public String getTheClassName();

	/**
	 * Gets an <code>int</code> property.
	 * @param name The name of the property to get
	 * 
	 * @return The <code>int</code> property associated to the provided name
	 * @throws PersistenceValueNotFoundException
	 */
	public int getInt(String name) throws PersistenceValueNotFoundException;

	/**
	 * Gets an <code>long</code> property.
	 * @param name The name of the property to get
	 * 
	 * @return The <code>long</code> property associated to the provided name
	 * @throws PersistenceValueNotFoundException
	 */
	public long getLong(String name) throws PersistenceValueNotFoundException;

	/**
	 * Gets a <code>double</code> property.
	 * @param name The name of the property to get
	 * 
	 * @return The <code>double</code> property associated to the provided name
	 * @throws PersistenceValueNotFoundException
	 */
	public double getDouble(String name)
			throws PersistenceValueNotFoundException;

	/**
	 * Gets a <code>float</code> property.
	 * @param name The name of the property to get
	 * 
	 * @return The <code>float</code> property associated to the provided name
	 * @throws PersistenceValueNotFoundException
	 */
	public float getFloat(String name) throws PersistenceValueNotFoundException;

	/**
	 * Gets a <code>boolean</code> property.
	 * @param name The name of the property to get
	 * 
	 * @return The <code>boolean</code> property associated to the provided name
	 * @throws PersistenceValueNotFoundException
	 */
	public boolean getBoolean(String name)
			throws PersistenceValueNotFoundException;

	/**
	 * Gets a <code>String</code> property.
	 * @param name The name of the property to get
	 * 
	 * @return The <code>String</code> property associated to the provided name
	 * @throws PersistenceValueNotFoundException
	 */
	public String getString(String name)
			throws PersistenceValueNotFoundException;

	/**
	 * Gets an <code>Object</code> property.
	 * @param name The name of the property to get
	 * 
	 * @return The <code>Object</code> property associated to the provided name
	 * @throws PersistenceValueNotFoundException
	 */
	public Object get(String name) throws PersistenceValueNotFoundException,
			PersistenceException;

	/**
	 * <p>Gets an <code>Iterator</code> property.</p>
	 * 
	 * @param name The name of the property to get
	 * 
	 * @return The <code>Iterator</code> property associated to the provided name
	 * @throws PersistenceValueNotFoundException
	 * @throws PersistenceException 
	 */
	public Iterator getIterator(String name) throws PersistenceValueNotFoundException, PersistenceException;

	/**
	 * <p>Sets a property of type String.</p>
	 * 
	 * @param name The name of the property to store
	 * @param it The String object to be stored in the state.
	 * 
	 * @throws PersistenceException
	 */
	public void set(String name, String value);

	/**
	 * <p>Sets a property of type int.</p>
	 * 
	 * @param name The name of the property to store
	 * @param it The int value to be stored in the state.
	 * 
	 * @throws PersistenceException
	 */
	public void set(String name, int value);

	/**
	 * <p>Sets a property of type long.</p>
	 * 
	 * @param name The name of the property to store
	 * @param it The long value to be stored in the state.
	 * 
	 * @throws PersistenceException
	 */
	public void set(String name, long value);

	/**
	 * <p>Sets a property of type double.</p>
	 * 
	 * @param name The name of the property to store
	 * @param it The double value to be stored in the state.
	 * 
	 * @throws PersistenceException
	 */
	public void set(String name, double value);

	/**
	 * <p>Sets a property of type float.</p>
	 * 
	 * @param name The name of the property to store
	 * @param it The float value to be stored in the state.
	 * 
	 * @throws PersistenceException
	 */
	public void set(String name, float value);

	/**
	 * <p>Sets a property of type boolean.</p>
	 * 
	 * @param name The name of the property to store
	 * @param it The boolean value to be stored in the state.
	 * 
	 * @throws PersistenceException
	 */
	public void set(String name, boolean value);

	/**
	 * <p>Sets a property of type Persistent.</p>
	 * 
	 * @param name The name of the property to store
	 * @param it The Persistent object to be stored in the state.
	 * 
	 * @throws PersistenceException
	 */
	public void set(String name, Persistent obj)
			throws PersistenceException;

	/**
	 * <p>Sets a property of type Object. Only the following types can be
	 * stored: Persistent, PersistentState, Iterator, Boolean, Integer, Long,
	 * Float, Double or String.</p>
	 * 
	 * @param name The name of the property to store
	 * @param it The object to be stored in the state.
	 * 
	 * @throws PersistenceException
	 */
	public void set(String name, Object obj)
			throws PersistenceException;

	/**
	 * <p>Sets a property of type Iterator. This method is useful
	 * to store arrays or lists.</p>
	 * 
	 * @param name The name of the property to store
	 * @param it The iterator to be stored in the state.
	 * 
	 * @throws PersistenceException
	 */
	public void set(String name, Iterator it)
			throws PersistenceException;

	/**
	 * <p>Gets an iterator over the names of the properties contained
	 * in this PersistentState.</p>
	 * 
	 * @return An iterator which provides the name of all the
	 * properties contained in this state.
	 */
	public Iterator getNames();

	/**
	 * <p>Serializes this state and writes the serialized data
	 * in the provided <code>writer</code>. Depending on the implementation
	 * the serialized data may have different formats, such as XML or
	 * binary data.</p>
	 *
	 * @param writer
	 */
	public void save(Writer writer) throws PersistenceException;

	/**
	 * <p>De-serializes the data read from the provided <code>reader</code>. Depending
	 * on the implementation the serialized data may have different formats, such as XML or
	 * binary data.</p>
	 * 
	 * <p>Note that a particular implementation will only be able to
	 * de-serialize data which has been serialized by the same
	 * implementation.</p>
	 *
	 * @param reader
	 */
	public void load(Reader reader) throws PersistenceException;
}