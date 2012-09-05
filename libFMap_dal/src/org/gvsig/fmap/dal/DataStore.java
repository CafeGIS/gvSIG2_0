package org.gvsig.fmap.dal;

import java.util.Iterator;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.ValidateDataParametersException;
import org.gvsig.metadata.Metadata;
import org.gvsig.tools.observer.ComplexWeakReferencingObservable;
import org.gvsig.tools.observer.Observer;
import org.gvsig.tools.persistence.Persistent;

/**
 * <p>
 * This is the basic interface for all data stores. Depending on the context, it
 * can represent a geographic layer, an alphanumeric database table or any data
 * file. DataStore offers generic services like:
 * <ul>
 * <li>Open and close data stores</li>
 * <li>Access to data sets, with the possibility of loading data in background.</li>
 * <li>Use of selection and locks, as well as data sets</li>
 * <li>Editing</li>
 * <li>Register event observers through the Observable interface</li>
 * <li>Access to embedded data stores (like GML)</li>
 * <li>Information about the Spatial Reference Systems used by the data store</li>
 * </ul>
 * </p>
 * <br>
 *
 */
public interface DataStore extends ComplexWeakReferencingObservable, Persistent,
		Metadata {

	public static final String DYNCLASS_NAME = "DataStore";

	/**
	 * Returns this store's name.
	 *
	 * @return String containing this store's name.
	 */
	public String getName();

	/**
	 * Return the of parameters of this store
	 *
	 * @return parameters of this store
	 */
	public DataStoreParameters getParameters();

	/**
	 * Refreshes this store state.
	 *
	 * @throws DataException
	 */
	public void refresh() throws DataException;

	/**
	 * Frees this store's resources
	 *
	 * @throws DataException
	 */
	public void dispose() throws DataException;

	/**
	 * Returns all available data.
	 *
	 * @return a set of data
	 * @throws DataException
	 *             if there is any error while loading the data
	 */
	DataSet getDataSet() throws DataException;

	/**
	 * Returns a subset of data taking into account the properties and
	 * restrictions of the DataQuery.
	 *
	 * @param dataQuery
	 *            defines the properties of the requested data
	 * @return a set of data
	 * @throws DataException
	 *             if there is any error while loading the data
	 */
	DataSet getDataSet(DataQuery dataQuery) throws DataException;

	/**
	 * Loads all available data and notifies the observer for each loaded block of data.
	 *
	 * @param observer
	 *            to be notified for each block of data loaded
	 * @throws DataException
	 *             if there is any error while loading the data
	 */
	void getDataSet(Observer observer) throws DataException;

	/**
	 * Loads a subset of data taking into account the properties and
	 * restrictions of the DataQuery. Data loading is performed by calling the
	 * Observer, once each data block is loaded.
	 *
	 * @param dataQuery
	 *            defines the properties of the requested data
	 * @param observer
	 *            to be notified for each block of data loaded
	 * @throws DataException
	 *             if there is any error while loading the data
	 */
	void getDataSet(DataQuery dataQuery, Observer observer) throws DataException;

	/**
	 * Returns the selected set of data
	 *
	 * @return DataSet
	 */

	public DataSet getSelection() throws DataException;

	/**
	 * Sets the current data selection with the given data set.
	 *
	 * @param DataSet
	 *            selection
	 * @throws DataException
	 */
	public void setSelection(DataSet selection) throws DataException;

	/**
	 * Creates a new selection.
	 *
	 * @return DataSet that contains the selection
	 *
	 * @throws DataException
	 */
	public DataSet createSelection() throws DataException;

	/**
	 * Returns an iterator over this store children
	 *
	 * @return Iterator over this DataStore children
	 */
	public Iterator getChildren();

	/**
	 * Returns the DataServerExplorer to which this DataStore belongs, if there
	 * is any.
	 * 
	 * @return DataServerExplorer to which this DataStore belongs, or
	 *         <code>null</code> if this was not accessed through any
	 *         DataServerExplorer.
	 * 
	 * @throws DataException
	 * @throws ValidateDataParametersException
	 */
	public DataServerExplorer getExplorer() throws DataException,
			ValidateDataParametersException;


	/**
	 * Returns a new instance of a {@link DataQuery}.
	 *
	 * @return new {@link DataQuery} instance.
	 *
	 * @throws DataException
	 */
	public DataQuery createQuery();
}

