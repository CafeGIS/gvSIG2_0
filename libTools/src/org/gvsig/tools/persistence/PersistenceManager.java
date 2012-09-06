package org.gvsig.tools.persistence;

import java.io.Reader;
import java.util.Iterator;

import org.gvsig.tools.dynobject.DynStruct;
import org.gvsig.tools.persistence.validation.ValidationResult;

/**
 * <p>This interface contains the necessary methods to get a persistent representation
 * of an object, suitable for storage or transmission, and to create a an object from
 * its persistent representation.</p>
 *
 * @author The gvSIG project <http://www.gvsig.org>
 * @author Cesar Martinez Izquierdo <cesar.martinez@iver.es>
 * @author IVER T.I. <http://www.iver.es>
 */
public interface PersistenceManager {
	/**
	 * <p>Validation Mode -- MANDATORY: Validation is active, so 
	 * {@link PersistenceManager#create(org.gvsig.tools.persistence.PersistentState)}
	 * and
	 * {@link PersistenceManager#getState(org.gvsig.tools.persistence.Persistent)}
	 * will throw validation exceptions if validation errors are found.</p>
	 * <p>If an undeclared attribute or class is found, this will be considered
	 * a validation error.</p>
	 */
	static public final int MANDATORY = 3;
	/**
	 * <p>Validation Mode -- MANDATORY_IF_DECLARED: Validation is active, but
	 *  it will be only applied to Persistent objectswhich have been registered.
	 * {@link PersistenceManager#create(org.gvsig.tools.persistence.PersistentState)}
	 * and
	 * {@link PersistenceManager#getState(org.gvsig.tools.persistence.Persistent)}
	 * methods will throw validation exceptions if validation errors are found.</p>
	 */
	static public final int MANDATORY_IF_DECLARED = 2;
	/**
	 * <p>Validation Mode - DISABLED: No validation is performed at all.
	 * In this mode, {@link PersistenceManager#ge}</p>
	 */
	static public final int DISABLED = 0;

	/**
	 * <p>Creates a persistent state from an Persistent object.</p>
	 * 
	 * @param obj The Persistent object to be persisted
	 * 
	 * @return A PersistentState object, which stores the state of the
	 * provided Persistent Object.
	 * @throws PersistenceException
	 */
	public PersistentState getState(Persistent obj) throws PersistenceException;

	/**
	 * <p>Instantiates an object from a persistent state. The PersistentState object knows
	 * the class of the persisted object, and instantiates it by using introspection. The
	 * object must implement the Persistent interface so that it can understand the
	 * PersistentState.</p>
	 * 
	 * @param state The state of the object to be instantiated
	 * @return A new object whose state is the same as the provided <code>state</code> object.
	 * 
	 * @throws PersistenceException
	 */
	public Object create(PersistentState state) throws PersistenceException;

	/**
	 * <p>Associates an alias with a class. This is similar to a symbolic link, which allows
	 * to access the class by means of its alias.</p>
	 * 
	 * <p>When an alias is defined, it replaces any
	 * class whose qualified name is equal to the alias. Therefore, this class will never
	 * be instantiated, and instead the class pointed by the the alias will be instantiated.</p>
	 * 
	 * <p>For example, if the following alias is defined:</p>
	 *
	 * <pre>Class aClass = org.gvsig.fmap.mapcontext.rendering.symbols.SimpleMarkerSymbol.class;
	 * manager.addAlias("org.gvsig.fmap.mapcontext.rendering.symbols.ArrowMarkerSymbol", aClass);
	 * </pre>
	 * <p>then, SimpleMarkerSymbol will be instantiated instead of ArrowMarkerSymbol from any
	 * PersistentState which references ArrowMarkerSymbol.</p>
	 * 
	 * <p>Aliases are useful to provided backward-compatibility paths (as old unexisting classes
	 * may be aliased to substitution classes), but are also useful to avoid limitations on
	 * ClassLoaders. As a Class object is provided, it will be possible to instantiate it even
	 * if the current ClassLoader has no direct visibility of the class to instantiate.</p>
	 *
	 * @param alias The alias to reference a class
	 * @param aClass The class to be referenced by the alias
	 */
	public void addAlias(String alias, Class aClass);

	/**
	 * <p>Registers persistentClass as Persistent, and associates an attribute definition to that
	 * class. During a persistence process, the PersistenceManager will validate that a class
	 * only persists attributes which has been defined in its DynStruct definition.</p>
	 * 
	 * @param persistentClass The class to register
	 * @param definition The definition of the attributes allowed for the persistentClass
	 */
	public void addDefinition(Class persistentClass, DynStruct definition);

	/**
	 * <p>De-registers a class which has been previously registered using
	 *  <code>{@link #addDefinition(Class, DynStruct)}</code></p>
	 * 
	 * @param persistentClass
	 */
	public void removeDefinition(Class persistentClass);

	/**
	 * <p>Validates a persistent state by using the corresponding registered
	 * attribute definition. If there is no registered definition for the class
	 * represented by the PersistenteState, validation should fail.</p>
	 *
	 * <p>Some manager implementations may not support validation. In this case,
	 * they should always return a positive validation.</p>
	 *
	 * @param state
	 *
	 * @return The validation result
	 */
	public ValidationResult validate(PersistentState state);

	/**
	 * <p>If the provided persistent class has registered an attribute definition in
	 * this manager, then this method returns that definition. Otherwise, it returns
	 * null.</p>
	 *
	 * @param persistentClass The class whose corresponding attribute definition is
	 * to be retrieved.
	 *
	 * @return The attribute definition corresponding to the provided persistent class,
	 * or null otherwise.
	 */
	public DynStruct getDefinition(Class persistentClass);

	/**
	 * <p>Gets a list of the registered Persistent classes.</p>
	 *
	 * @return An iterator over the registered Persistent classes.
	 */
	public Iterator getPersistentClasses();

	/**
	 * <p>De-serializes an state from the data read from the provided
	 * <code>reader</code>. Depending on the implementation the serialized
	 * data may have different formats, such as XML or binary data.</p>
	 * 
	 * <p>Note that a particular implementation will only be able to
	 * de-serialize data which has been serialized by the same
	 * implementation.</p>
	 * 
	 * @param reader
	 * @return
	 */
	public PersistentState loadState(Reader reader) throws PersistenceException;
	
	/**
	 * <p>Sets the validation which will be applied in
	 * {@link #getState(Persistent)}, {@link #create(PersistentState)}
	 * methods. Validation ensures that persisted or de-persisted objects
	 * match the declared definition (which must have been previously
	 * registered by using {@link #addDefinition(Class, DynStruct)}).</p>
	 * 
	 * <p>When automatic validation is enabled (MANDATORY or
	 * MANDATORY_IF_DECLARED), a ValidationException will be thrown by
	 * {@link #getState(Persistent)}, {@link #create(PersistentState)}
	 * if a validation error is found.</p>
	 * 
	 * @param validationMode On of the following values:
	 * {@link #DISABLED}, {@link #MANDATORY}
	 * or {@link #MANDATORY_IF_DECLARED}

	 * @see #validate(PersistentState)
	 * @see #addDefinition(Class, DynStruct)
	 * 
	 * @throws PersistenceException If the mode is not supported by this manager
	 */
	public void setAutoValidation(int validationMode) throws PersistenceException;
	
	/**
	 * <p>Gets the validation which will be applied in
	 * {@link #getState(Persistent)}, {@link #create(PersistentState)} methods.
	 *
	 * @return The current mode for automatic validation: {@link #DISABLED},
	 * {@link #MANDATORY} or {@link #MANDATORY_IF_DECLARED}
	 * 
	 * @see #validate(PersistentState)
	 * @see #addDefinition(Class, DynStruct)
	 */
	public int getAutoValidation();

}
