package org.gvsig.fmap.dal.spi;

import java.util.Iterator;

import org.gvsig.fmap.dal.DataServerExplorer;
import org.gvsig.fmap.dal.exception.CloseException;
import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.exception.OpenException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.exception.ValidateDataParametersException;
import org.gvsig.tools.dynobject.DynObject;

public interface DataStoreProvider extends DynObject {

	/**
	 * Retruns an {@link java.util.Iterator} of SubStores from this store. it
	 * this hasn't SubStores returns <code>null</code>.
	 * 
	 * @return SubStores iterator
	 */
	public abstract Iterator getChilds();

	/**
	 * Create a {@link DataServerExplorer} from the same source that this store.
	 *
	 * @return ServerExplorer
	 * @throws ReadException
	 * @throws ValidateDataParametersException
	 */
	public abstract DataServerExplorer getExplorer() throws ReadException,
			ValidateDataParametersException;

	/**
	 * Open store. You must call it before do anything whith store.<br>
	 * This method can be called repeatly.
	 *
	 * @throws OpenException
	 */
	public abstract void open() throws OpenException;

	/**
	 * Request to close de source
	 *
	 * @throws CloseException
	 */
	public abstract void close() throws CloseException;

	/**
	 * Force to reload information of Store
	 *
	 * @throws OpenException
	 * @throws InitializeException
	 */
	public abstract void refresh() throws OpenException, InitializeException;

	/**
	 * Prepares store for be removed from memory
	 *
	 * @throws CloseException
	 */
	public abstract void dispose() throws CloseException;

	/**
	 * Returns the unique identifier of the Store
	 *
	 * FIXME add examples
	 *
	 * @return
	 */
	public abstract Object getSourceId();

}