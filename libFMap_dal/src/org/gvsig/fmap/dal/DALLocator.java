package org.gvsig.fmap.dal;

import org.gvsig.fmap.dal.resource.ResourceManager;
import org.gvsig.tools.locator.AbstractLocator;
import org.gvsig.tools.locator.Locator;
import org.gvsig.tools.locator.LocatorException;

/**
 * 
 * This locator is the entry point of gvSIG's DAL, providing access to all DAL services. 
 * DAL services are grouped in two managers {@link DataManager} and {@link ResourceManager}.
 *  
 * This locator offers methods for registering as well as for obtaining both managers' unique instances.
 *
 * @see Locator
 */
public class DALLocator extends AbstractLocator {

	private static final String LOCATOR_NAME = "DALLocator";

	/**
	 * DataManager name used by the locator to access the instance
	 */
	public static final String DATA_MANAGER_NAME = "DataManager";

	private static final String DATA_MANAGER_DESCRIPTION = "DataManager of gvSIG Data Access Library";

	/**
	 * ResourceManager name used by the locator to access the instance
	 */
	public static final String RESOURCE_MANAGER_NAME = "ResourceManager";

	private static final String RESOURCE_MANAGER_DESCRIPTION = "ResourceManager of gvSIG Data Access Library";

	/**
	 * Unique instance.
	 */
	private static final DALLocator instance = new DALLocator();

	/**
	 * Return the singleton instance.
	 *
	 * @return the singleton instance
	 */
	public static DALLocator getInstance() {
		return instance;
	}

	/**
	 * Returns the Locator name.
	 * 
	 * @return String containing the locator name.
	 */
	public String getLocatorName() {
		return LOCATOR_NAME;
	}

	/**
	 * Return a reference to DataManager.
	 *
	 * @return a reference to DataManager
	 * @throws LocatorException
	 *             if there is no access to the class or the class cannot be
	 *             instantiated
	 * @see Locator#get(String)
	 */
	public static DataManager getDataManager() throws LocatorException {
		return (DataManager) getInstance().get(DATA_MANAGER_NAME);
	}

	/**
	 * Registers the Class implementing the DataManager interface.
	 *
	 * @param clazz
	 *            implementing the DataManager interface
	 */
	public static void registerDataManager(Class clazz) {
		getInstance().register(DATA_MANAGER_NAME, DATA_MANAGER_DESCRIPTION,
				clazz);
	}
	
	/**
	 * Registers a class as the default DataManager.
	 * 
	 * @param clazz
	 * 			  implementing the DataManager interface
	 */
	public static void registerDefaultDataManager(Class clazz) {
		getInstance().registerDefault(DATA_MANAGER_NAME, DATA_MANAGER_DESCRIPTION,
				clazz);
	}

	/**
	 * Return a reference to ResourceManager.
	 *
	 * @return a reference to ResourceManager
	 * @throws LocatorException
	 *             if there is no access to the class or the class cannot be
	 *             instantiated
	 * @see Locator#get(String)
	 */
	public static ResourceManager getResourceManager() throws LocatorException {
		return (ResourceManager) getInstance().get(RESOURCE_MANAGER_NAME);
	}

	/**
	 * Registers the Class implementing the MDManager interface.
	 *
	 * @param clazz
	 *            implementing the MDManager interface
	 */
	public static void registerResourceManager(Class clazz) {
		getInstance().register(RESOURCE_MANAGER_NAME,
				RESOURCE_MANAGER_DESCRIPTION, clazz);
	}

}
