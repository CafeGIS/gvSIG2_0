package org.gvsig.fmap.dal;

import java.util.List;

import org.gvsig.fmap.dal.exception.DataException;

/**
 * DataServerExplorer is an abstraction for any type of data server. It allows
 * connecting to the server and browsing its contents.
 *
 * More specifically, this interface provides a list of the available data
 * stores in a server.
 */
public interface DataServerExplorer {

	/**
	 * Returns the DataServerExplorer's name
	 *
	 * @return String containing this DataServerExplorer's name
	 */
	public String getName();

	/**
	 * Indicates whether this DataServerExplorer can create a new DataStore in the
	 * server.
	 *
	 * @return true if this DataServerExplorer can be created or false otherwise.
	 */
	public boolean canAdd();

	/**
	 * Indicates whether this DataServerExplorer can create a new DataStore in
	 * the server, given the store name.
	 *
	 * @param storeName
	 *            store name.
	 *
	 * @return true if this DataServerExplorer can create a new store or false
	 *         otherwise.
	 *
	 * @throws DataException
	 */
	public boolean canAdd(String storeName)
			throws DataException;

	/**
	 * Provides a list of available stores in the server.
	 *
	 * @return list of DataStoreParameters
	 *
	 * @throws DataException
	 */
	public List list() throws DataException;

	public static final int MODE_ALL = 0;
	public static final int MODE_FEATURE = 1;
	public static final int MODE_FEATURE_GEOMETRY = 2;
	public static final int MODE_RASTER = 3;

	/**
	 * Provides a list of available stores in the server of a type.
	 * 
	 * @param mode
	 *            , filter store from a type: {@link #MODE_ALL},
	 *            {@link #MODE_FEATURE}, {@link #MODE_FEATURE_GEOMETRY},
	 *            {@link #MODE_RASTER}
	 * 
	 * @return list of DataStoreParameters
	 * 
	 * @throws DataException
	 */
	public List list(int mode) throws DataException;

	/**
	 * Creates a new DataStore in this server.
	 *
	 * @param parameters
	 *            , an instance of DataStoreParameters from
	 *            {@link DataServerExplorer#getAddParameters(String)} that
	 *            describes the new DataStore.
	 * @param overwrite
	 *            if the store already exists
	 *
	 * @return true if the DataStoreParameters were successfully added, false
	 *         otherwise.
	 *
	 * @throws DataException
	 */
	public boolean add(NewDataStoreParameters parameters, boolean overwrite)
			throws DataException;

	/**
	 * Removes a store from the server given its DataStoreParameters. If the
	 * store is a file then this method deletes the file, if it is a table in a
	 * database then this method drops the table, and so on.
	 *
	 * @param parameters
	 * @throws DataException
	 */
	void remove(DataStoreParameters parameters) throws DataException;

	/**
	 * Given the store's name, returns its parameters for creation.
	 *
	 * @param storeName
	 *
	 * @return parameters to create a store
	 *
	 * @throws DataException
	 */
	public NewDataStoreParameters getAddParameters(String storeName)
			throws DataException;

	/**
	 * Frees the resources used by this DataServerExplorer.
	 *
	 * @throws DataException
	 */
	public void dispose() throws DataException;

	/**
	 * Returns this DataServerExplorer parameters
	 *
	 * @return an instance of DataServerExplorerParameters containing this
	 *         DataServerExplorer parameters.
	 */
	public DataServerExplorerParameters getParameters();

}
