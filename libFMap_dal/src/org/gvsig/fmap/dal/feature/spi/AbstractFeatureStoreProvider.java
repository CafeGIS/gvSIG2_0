package org.gvsig.fmap.dal.feature.spi;

import java.util.Iterator;

import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataServerExplorer;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.exception.CloseException;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.exception.OpenException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.exception.ValidateDataParametersException;
import org.gvsig.fmap.dal.feature.FeatureLocks;
import org.gvsig.fmap.dal.feature.FeatureSelection;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.resource.ResourceManager;
import org.gvsig.fmap.dal.resource.spi.ResourceManagerProviderServices;
import org.gvsig.fmap.dal.resource.spi.ResourceProvider;
import org.gvsig.fmap.dal.spi.DataStoreProviderServices;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.tools.dynobject.DelegatedDynObject;
import org.gvsig.tools.dynobject.DynClass;
import org.gvsig.tools.dynobject.DynObject;
import org.gvsig.tools.dynobject.exception.DynFieldNotFoundException;
import org.gvsig.tools.dynobject.exception.DynMethodException;


public abstract class AbstractFeatureStoreProvider implements
		FeatureStoreProvider {

	private FeatureStoreProviderServices store;
	private DelegatedDynObject metadata;
	private DataStoreParameters parameters;

	/**
	 * Default Constructor.
	 *
	 * @param params
	 * @param storeServices
	 * @param metadata
	 */
	protected AbstractFeatureStoreProvider(DataStoreParameters params,
			DataStoreProviderServices storeServices, DynObject metadata) {
		this.store = (FeatureStoreProviderServices) storeServices;
		this.metadata = (DelegatedDynObject) metadata;
		this.parameters = params;
	}

	/**
	 * Constructor when cannot create metada in constrution time. <br>
	 * <br>
	 * <strong>Note: </strong> Don't use it if not is necesary. Set metada
	 * <strong>as soon as posible</strong> by
	 * {@link AbstractFeatureStoreProvider#setMetadata(DynObject)}
	 *
	 * @param params
	 * @param storeServices
	 */
	protected AbstractFeatureStoreProvider(DataStoreParameters params,
			DataStoreProviderServices storeServices) {
		this.store = (FeatureStoreProviderServices) storeServices;
		this.metadata = null;
		this.parameters = params;
	}

	/**
	 * Set metada container if this not set at construction time and only in one
	 * time. In other case an Exception will be throw
	 *
	 * @param metadata
	 */
	protected void setMetadata(DynObject metadata) {
		if (this.metadata != null) {
			// FIXME Exception
			throw new IllegalStateException();
		}
		this.metadata = (DelegatedDynObject) metadata;
	}

	/**
	 * @return the parameters
	 */
	public DataStoreParameters getParameters() {
		return parameters;
	}

	/**
	 * Create or get a resource of <code>type</code> for <code>params</code> in
	 * {@link ResourceManager}
	 *
	 * @param type
	 * @param params
	 * @return
	 * @throws InitializeException
	 */
	protected ResourceProvider createResource(String type, Object[] params)
			throws InitializeException {
		ResourceManagerProviderServices manager = (ResourceManagerProviderServices) DALLocator
				.getResourceManager();
		ResourceProvider resource = manager.createResource(type, params);
		return resource;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.gvsig.fmap.dal.feature.spi.FeatureStoreProvider#getStoreServices()
	 */
	public FeatureStoreProviderServices getStoreServices() {
		return this.store;
	}

	public FeatureStore getFeatureStore() {
		return this.store.getFeatureStore();
	}

	/**
	 * unsupported by default, override this otherwise
	 *
	 * @see org.gvsig.fmap.dal.feature.spi.FeatureStoreProvider#allowWrite()
	 */
	public boolean allowWrite() {
		return false;
	}


	/**
	 * unsupported by default, override this otherwise
	 *
	 * @see org.gvsig.fmap.dal.feature.spi.FeatureStoreProvider#performChanges(Iterator,
	 *      Iterator, Iterator, Iterator)
	 */

	public void performChanges(Iterator deleteds, Iterator inserteds,
			Iterator updateds, Iterator featureTypesChanged)
			throws DataException {
		// FIXME exception
		throw new UnsupportedOperationException();

	}

	/**
	 * unsupported by default, override this otherwise
	 *
	 * @see org.gvsig.fmap.dal.feature.spi.FeatureStoreProvider#isLocksSupported()
	 */
	public boolean isLocksSupported() {
		return false;
	}

	/**
	 * Default Factory of {@link FeatureProvider}. Create a new default
	 * {@link FeatureProvider} instance.<br>
	 *
	 * Override this if you need an special implemtation of
	 * {@link FeatureProvider}.
	 *
	 * @return
	 * @throws DataException
	 *
	 * @see {@link org.gvsig.fmap.dal.feature.spi.FeatureStoreProvider#createFeatureProvider(FeatureType)}
	 */

	public FeatureProvider createFeatureProvider(FeatureType type)
			throws DataException {
		return this.store.createDefaultFeatureProvider(type);
	}

	/**
	 * unsupported by default (return <code>null</code>), override this
	 * otherwise
	 *
	 * @see org.gvsig.fmap.dal.feature.spi.FeatureStoreProvider#createFeatureLocks()
	 */
	public FeatureLocks createFeatureLocks() throws DataException {
		return null;
	}

	/**
	 * Default Factory of {@link FeatureSelection}. Create a new default
	 * {@link FeatureSelection} instance.<br>
	 *
	 * Override this if you need an special implemtation of
	 * {@link FeatureSelection}.
	 *
	 * @return
	 * @throws DataException
	 *
	 * @see org.gvsig.fmap.dal.feature.spi.FeatureStoreProvider#createFeatureSelection()
	 */
	public FeatureSelection createFeatureSelection() throws DataException {
		return this.store.createDefaultFeatureSelection();
	}

	/**
	 * do nothing by default, override this otherwise
	 *
	 * @see org.gvsig.fmap.dal.feature.spi.FeatureStoreProvider#refresh()
	 */
	public void refresh() throws OpenException {
		// Do nothing by default
	}

	/**
	 * do nothing by default, override this otherwise
	 *
	 * @see org.gvsig.fmap.dal.feature.spi.FeatureStoreProvider#close()
	 */
	public void close() throws CloseException {
		// Do nothing by default
	}

	public void dispose() throws CloseException {
		this.metadata = null;
		this.store = null;
	}

	/**
	 * unsupported geometry by default (return <code>null</code>), override this
	 * otherwise
	 *
	 * @see org.gvsig.fmap.dal.feature.spi.FeatureStoreProvider#getEnvelope()
	 */
	public Envelope getEnvelope() throws DataException {
		return null;
	}

	/**
	 * unsupported geometry write by default (return <code>false</code>),
	 * override this otherwise
	 *
	 * @see org.gvsig.fmap.dal.feature.spi.FeatureStoreProvider#canWriteGeometry(int,
	 *      int)
	 */
	public boolean canWriteGeometry(int geometryType, int geometrySubType)
			throws DataException {
		return false;
	}

	// --- Metadata methods ---


	public void delegate(DynObject dynObject) {
		if (this.metadata == null) {
			return;
		}
		this.metadata.delegate(dynObject);
	}

	public DynClass getDynClass() {
		if (this.metadata == null) {
			return null;
		}
		return this.metadata.getDynClass();
	}

	public Object getDynValue(String name) throws DynFieldNotFoundException {
		if (this.metadata == null) {
			return null;
		}
		// TODO this.open??
		return this.metadata.getDynValue(name);
	}

	public boolean hasDynValue(String name) {
		if (this.metadata == null) {
			return false;
		}
		// TODO this.open??
		return this.metadata.hasDynValue(name);
	}

	public void implement(DynClass dynClass) {
		if (this.metadata == null) {
			return;
		}
		this.metadata.implement(dynClass);

	}

	public Object invokeDynMethod(int code, DynObject context)
			throws DynMethodException {
		if (this.metadata == null) {
			return null;
		}
		// TODO this.open??
		return this.metadata.invokeDynMethod(this, code, context);
	}

	public Object invokeDynMethod(String name, DynObject context)
			throws DynMethodException {
		if (this.metadata == null) {
			return null;
		}
		// TODO this.open??
		return this.metadata.invokeDynMethod(this, name, context);
	}

	public void setDynValue(String name, Object value)
			throws DynFieldNotFoundException {
		if (this.metadata == null) {
			return;
		}
		// TODO this.open??
		this.metadata.setDynValue(name, value);
	}

	// --- end Metadata methods ---

	/**
	 * unsupported by default, override this otherwise
	 *
	 * @see org.gvsig.fmap.dal.feature.spi.FeatureStoreProvider#allowAutomaticValues()
	 */
	public boolean allowAutomaticValues() {
		return false;

	}

	/**
	 * unsupported by default, override this otherwise
	 *
	 * @see org.gvsig.fmap.dal.feature.spi.FeatureStoreProvider#append(org.gvsig.
	 *      fmap.dal.feature.spi.FeatureProvider)
	 */
	public void append(FeatureProvider featureProvider) throws DataException {
		// FIXME exception
		throw new UnsupportedOperationException();
	}

	/**
	 * unsupported by default, override this otherwise
	 *
	 * @see org.gvsig.fmap.dal.feature.spi.FeatureStoreProvider#beginAppend()
	 */
	public void beginAppend() throws DataException {
		// FIXME exception
		throw new UnsupportedOperationException();
	}

	/**
	 * unsupported by default, override this otherwise
	 *
	 * @see org.gvsig.fmap.dal.feature.spi.FeatureStoreProvider#endAppend()
	 */
	public void endAppend() throws DataException {
		// FIXME exception
		throw new UnsupportedOperationException();
	}

	/**
	 * unsupported by default, override this otherwise
	 *
	 * @see org.gvsig.fmap.dal.feature.spi.FeatureStoreProvider#supportsAppendMode()
	 */
	public boolean supportsAppendMode() {
		return false;
	}

	/**
	 * unsupported by default (return null), override this otherwise
	 *
	 * @see org.gvsig.fmap.dal.spi.DataStoreProvider#getChilds()
	 */
	public Iterator getChilds() {
		return null;
	}

	/**
	 * unsupported by default (return null), override this otherwise
	 *
	 * @see org.gvsig.fmap.dal.spi.DataStoreProvider#getExplorer()
	 */
	public DataServerExplorer getExplorer() throws ReadException,
			ValidateDataParametersException {
		return null;
	}


}
