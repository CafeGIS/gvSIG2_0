package org.gvsig.tools;

import org.gvsig.tools.dynobject.DynObjectManager;
import org.gvsig.tools.extensionpoint.ExtensionPointManager;
import org.gvsig.tools.extensionpoint.impl.DefaultExtensionPointManager;
import org.gvsig.tools.locator.AbstractLocator;
import org.gvsig.tools.locator.Locator;
import org.gvsig.tools.locator.LocatorException;
import org.gvsig.tools.operations.OperationManager;
import org.gvsig.tools.persistence.PersistenceManager;
import org.gvsig.tools.task.TaskManager;

public class ToolsLocator extends AbstractLocator {

	private static final String LOCATOR_NAME = "ToolsLocator";

	public static final String PERSISTENCE_MANAGER_NAME = "toolslocator.manager.persistence";

	private static final String PERSISTENCE_MANAGER_DESCRIPTION = "PersistenceManager of gvSIG";

	public static final String OPERATION_MANAGER_NAME = "toolslocator.manager.operation";

	private static final String OPERATION_MANAGER_DESCRIPTION = "OperationManager of gvSIG";

    public static final String DYNOBJECT_MANAGER_NAME = "toolslocator.manager.dynobject";

    private static final String DYNOBJECT_MANAGER_DESCRIPTION = "DynObjectManager of gvSIG";

	public static final String TASK_MANAGER_NAME = "toolslocator.manager.task";

	private static final String TASK_MANAGER_DESCRIPTION = "TaskManager";

	/**
	 * Unique instance.
	 */
	private static final ToolsLocator instance = new ToolsLocator();

	/**
	 * Return the singleton instance.
	 *
	 * @return the singleton instance
	 */
	public static ToolsLocator getInstance() {
		return instance;
	}

	public String getLocatorName() {
		return LOCATOR_NAME;
	}

	/**
	 * Return a reference to PersistenceManager.
	 *
	 * @return a reference to PersistenceManager
	 * @throws LocatorException
	 *             if there is no access to the class or the class cannot be
	 *             instantiated
	 * @see Locator#get(String)
	 */
	public static PersistenceManager getPersistenceManager()
			throws LocatorException {
		return (PersistenceManager) getInstance().get(PERSISTENCE_MANAGER_NAME);
	}

	/**
	 * Registers the Class implementing the PersistenceManager interface.
	 *
	 * @param clazz
	 *            implementing the PersistenceManager interface
	 */
	public static void registerPersistenceManager(Class clazz) {
		getInstance().register(PERSISTENCE_MANAGER_NAME,
				PERSISTENCE_MANAGER_DESCRIPTION,
				clazz);
	}

	public static void registerDefaultPersistenceManager(Class clazz) {
		getInstance().registerDefault(PERSISTENCE_MANAGER_NAME,
				PERSISTENCE_MANAGER_DESCRIPTION, clazz);
	}

	/**
	 * Return a reference to OperationManager.
	 *
	 * @return a reference to OperationManager
	 * @throws LocatorException
	 *             if there is no access to the class or the class cannot be
	 *             instantiated
	 * @see Locator#get(String)
	 */
	public static OperationManager getOperationManager()
			throws LocatorException {
		return (OperationManager) getInstance().get(OPERATION_MANAGER_NAME);
	}

	/**
	 * Registers the Class implementing the OperationManager interface.
	 *
	 * @param clazz
	 *            implementing the OperationManager interface
	 */
	public static void registerOperationManager(Class clazz) {
		getInstance().register(OPERATION_MANAGER_NAME,
				OPERATION_MANAGER_DESCRIPTION, clazz);
	}

	public static void registerDefaultOperationManager(Class clazz) {
		getInstance().registerDefault(OPERATION_MANAGER_NAME,
				OPERATION_MANAGER_DESCRIPTION, clazz);
	}

	public static ExtensionPointManager getExtensionPointManager() {
		return DefaultExtensionPointManager.getManager();
	}

    /**
     * Return a reference to DynObjectManager.
     * 
     * @return a reference to DynObjectManager
     * @throws LocatorException
     *             if there is no access to the class or the class cannot be
     *             instantiated
     * @see Locator#get(String)
     */
    public static DynObjectManager getDynObjectManager()
            throws LocatorException {
        return (DynObjectManager) getInstance().get(DYNOBJECT_MANAGER_NAME);
    }

    /**
     * Registers the Class implementing the DynObjectManager interface.
     * 
     * @param clazz
     *            implementing the DynObjectManager interface
     */
    public static void registerDynObjectManager(Class clazz) {
        getInstance().register(DYNOBJECT_MANAGER_NAME,
                DYNOBJECT_MANAGER_DESCRIPTION, clazz);
    }

	/**
	 * Return a reference to TaskManager.
	 *
	 * @return a reference to PersistenceManager
	 * @throws LocatorException
	 *             if there is no access to the class or the class cannot be
	 *             instantiated
	 * @see Locator#get(String)
	 */
	public static TaskManager getTaskManager()
			throws LocatorException {
		return (TaskManager) getInstance().get(TASK_MANAGER_NAME);
	}

	/**
	 * Registers the Class implementing the PersistenceManager interface.
	 *
	 * @param clazz
	 *            implementing the PersistenceManager interface
	 */
	public static void registerTaskManager(Class clazz) {
		getInstance().register(TASK_MANAGER_NAME,
				TASK_MANAGER_DESCRIPTION,
				clazz);
	}

	public static void registerDefaultTaskManager(Class clazz) {
		getInstance().registerDefault(TASK_MANAGER_NAME,
				TASK_MANAGER_DESCRIPTION, clazz);
	}


}