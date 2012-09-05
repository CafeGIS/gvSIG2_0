package org.gvsig.fmap.dal.serverexplorer.filesystem.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.gvsig.fmap.dal.DALFileLocator;
import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataServerExplorerParameters;
import org.gvsig.fmap.dal.DataStore;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.NewDataStoreParameters;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.FileNotFoundException;
import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.exception.ProviderNotRegisteredException;
import org.gvsig.fmap.dal.exception.RemoveException;
import org.gvsig.fmap.dal.exception.ValidateDataParametersException;
import org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemFileFilter;
import org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemServerExplorer;
import org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemServerExplorerParameters;
import org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemStoreParameters;
import org.gvsig.fmap.dal.serverexplorer.filesystem.spi.FilesystemServerExplorerProvider;
import org.gvsig.fmap.dal.serverexplorer.filesystem.spi.FilesystemServerExplorerProviderServices;
import org.gvsig.fmap.dal.spi.DataManagerProviderServices;
import org.gvsig.fmap.dal.spi.DataServerExplorerProviderServices;
import org.gvsig.tools.extensionpoint.ExtensionPoint.Extension;


public class DefaultFilesystemServerExplorer implements
		FilesystemServerExplorerProviderServices, FilesystemFileFilter {
	// FIXME: , IPersistence{


	FilesystemServerExplorerParameters parameters;
	private File root;

	private File current;

	private DataServerExplorerProviderServices providerServices;

	private List serverProviders;


	public DefaultFilesystemServerExplorer(FilesystemServerExplorerParameters parameters)
			throws InitializeException {
		this.parameters = parameters;
		if (this.parameters.getRoot() != null) {
			this.root = new File(this.parameters.getRoot());
		}
		if (this.parameters.getInitialpath() != null) {
			this.current = new File(this.parameters.getInitialpath());
		}
		if (this.root == null && this.current == null){
			//			throw new InitializeException(this.getName(),
			//					new IllegalArgumentException());
		} else if (this.current == null) {
			this.current = new File(this.parameters.getRoot());
		}
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemServerExplorer#getParameters()
	 */
	public DataServerExplorerParameters getParameters() {
		return parameters;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemServerExplorer#dispose()
	 */
	public void dispose() throws DataException {
		this.parameters = null;
		this.root = null;
	}

	public List list(int mode) throws DataException {
		//TODO
		return list();
	}


	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemServerExplorer#list()
	 */
	public List list() throws DataException {
		if (this.current == null){
			throw new IllegalStateException(); //FIXME
		}
		if (!this.current.exists()) {
			//TODO crear excepcion de Data??
			new org.gvsig.fmap.dal.exception.FileNotFoundException(this.current);
		}

		if (!this.current.isDirectory()) {
			new IllegalArgumentException(this.getName()
					+ ": Path not a directory '"
					+ this.current + "'");
		}
		// DataManager dsm=DataManager.getManager();

		String files[] = this.current.list();
		int i;
		File theFile;
		ArrayList list = new ArrayList();
		DataStoreParameters dsp = null;

		for (i = 0; i < files.length; i++) {
			theFile = new File(this.root, files[i]);
			dsp = this.getParametersFor(theFile);
			if (dsp != null) {
				list.add(dsp);
			}
		}
		return list;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemServerExplorer#setCurrentPath(java.io.File)
	 */
	public void setCurrentPath(File path) throws FileNotFoundException {
		// FIXME Comprobar si es un directorio existente
		if (!path.exists()) {
			throw new FileNotFoundException(path);
		}
		if (!path.isDirectory()) {
			throw new IllegalArgumentException(path.getPath()
					+ " is not a directory");
		}
		if (!isFromRoot(path)) {
			throw new IllegalArgumentException(path.getPath()
					+ " is not from root");

		}

		this.current = path;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemServerExplorer#getCurrentPath()
	 */
	public File getCurrentPath() {
		return this.current;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemServerExplorer#getRoot()
	 */
	public File getRoot() {
		return this.root;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemServerExplorer#remove(org.gvsig.fmap.dal.DataStoreParameters)
	 */
	public void remove(DataStoreParameters dsp) throws RemoveException {
		String providerName = dsp.getDataStoreName();
		try {
			this.checkIsMine(dsp);
			FilesystemServerExplorerProvider provider = this.getProvider(providerName);

			provider.remove(dsp);
		} catch (DataException e) {
			throw new RemoveException(this.getName(), e);
		}
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemServerExplorer#add(org.gvsig.fmap.dal.NewDataStoreParameters, boolean)
	 */
	public boolean add(NewDataStoreParameters ndsp, boolean overwrite)
			throws DataException {
		String providerName = ndsp.getDataStoreName();

		try {
			this.checkIsMine(ndsp);
			FilesystemServerExplorerProvider provider = this.getProvider(providerName);

			provider.create(ndsp, overwrite);
			return true; // TODO debería devolver un booleano el provider o esto ser un void
		} catch (DataException e) {
			throw new RemoveException(this.getName(), e);
		}
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemServerExplorer#canAdd()
	 */
	public boolean canAdd() {
		return this.root.canWrite();
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemServerExplorer#getName()
	 */
	public String getName() {
		return FilesystemServerExplorer.NAME;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemServerExplorer#getAddParameters(java.lang.String)
	 */
	public NewDataStoreParameters getAddParameters(String storeName)
			throws DataException {
		FilesystemServerExplorerProvider provider = this.getProvider(storeName);
		if (provider == null) {
			throw new IllegalArgumentException(
					"Not registered in this explorer"); // FIXME
		}

		NewDataStoreParameters nParams = provider.getCreateParameters();
		//		nParams.setAttribute("path", this.getCurrentPath().getPath());
		return nParams;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemServerExplorer#canAdd(java.lang.String)
	 */
	public boolean canAdd(String storeName)
			throws DataException {
		if (storeName == null) {
			return false;//CanAdd with genericFilter
		}
		FilesystemServerExplorerProvider provider = this.getProvider(storeName);
		if (provider == null) {
			throw new IllegalArgumentException(
					"Not registered in this explorer"); // FIXME
		}

		return provider.canCreate();

	}

	// ==========================================

	private FilesystemServerExplorerProvider getProvider(String storeName)
			throws InitializeException, ProviderNotRegisteredException {
		Iterator providers = getProviders().iterator();
		FilesystemServerExplorerProvider provider;
		while (providers.hasNext()) {
			provider = (FilesystemServerExplorerProvider) providers.next();
			if (provider.getDataStoreProviderName().equals(storeName)) {
				return provider;
			}
		}
		return null;
	}

	private DataManagerProviderServices getManager() {
		return (DataManagerProviderServices) DALLocator.getDataManager();
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemServerExplorer#getParametersFor(java.io.File)
	 */
	public DataStoreParameters getParametersFor(File file)
			throws DataException {

		return this.getParametersFor(file, true);
	}

	public DataStoreParameters getParametersFor(File file, boolean checksExist)
			throws DataException {

		if (checksExist) {
			if (!file.exists()) {
				return null;
			}
			if (!file.isFile()) {
				return null;
			}
			if (!file.canRead()) {
				return null;
			}
			if (file.isHidden()) { // XXX ???
				return null;
			}
		}
		Iterator filters = getFilters();
		while (filters.hasNext()) {
			FilesystemFileFilter filter = (FilesystemFileFilter) filters.next();
			if (filter.accept(file)) {
				DataStoreParameters params = this.getManager()
						.createStoreParameters(
								filter.getDataStoreProviderName());
				((FilesystemStoreParameters) params).setFile(file);
				return params;
			}
		}
		return null;
	}

	private void checkIsMine(DataStoreParameters dsp)
			throws IllegalArgumentException, DataException {
		// FIXME Exception ???
		if (!(dsp instanceof FilesystemStoreParameters)) {
			new IllegalArgumentException(
					"not instance of FilesystemStoreParameters");
		}
		Iterator filters = getFilters();
		File file = ((FilesystemStoreParameters) dsp).getFile();
		if (!this.isFromRoot(file)) {
			throw new IllegalArgumentException("worng explorer");
		}
		FilesystemFileFilter filter;
		while (filters.hasNext()) {
			filter = (FilesystemFileFilter) filters.next();
			if (dsp.getDataStoreName().equals(filter.getDataStoreProviderName())) {
				return;
			}
		}
		throw new IllegalArgumentException("worng explorer");
	}

	private boolean isFromRoot(File file) {
		if (this.root == null) {
			return true;
		}
		return file.getAbsolutePath().startsWith(this.root.getAbsolutePath());
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemServerExplorer#initialize(org.gvsig.fmap.dal.spi.DataServerExplorerProviderServices)
	 */
	public void initialize(
			DataServerExplorerProviderServices dataServerExplorerProviderServices) {
		this.providerServices = dataServerExplorerProviderServices;

	}

	public List getProviders() {
		if (this.serverProviders == null) {
			Iterator iter = DALFileLocator
					.getFilesystemServerExplorerManager()
			.getRegisteredProviders();
			this.serverProviders = new ArrayList();
			Extension ext;
			FilesystemServerExplorerProvider provider;
			while (iter.hasNext()) {
				ext = (Extension) iter.next();
				try {
					provider = (FilesystemServerExplorerProvider) ext.create();
				} catch (Exception e) {
					throw new RuntimeException(e);// FIXME !!!
				}
				provider.initialize(this);
				this.serverProviders.add(provider);
			}
		}
		return this.serverProviders;
	}

	public Iterator getFilters() {
		return this.getProviders().iterator();
	}

	public FilesystemFileFilter getGenericFilter() {
		return this;
	}

	public String getDataStoreProviderName() {
		return null;
	}

	public String getDescription() {
		return "All supporteds";
	}

	public boolean accept(File pathname) {
		try {
			return this.getParametersFor(pathname) != null;
		} catch (DataException e) {
			throw new RuntimeException(e); // FIXME excpetion??
		}
	}

	public DataServerExplorerProviderServices getServerExplorerProviderServices() {
		return this.providerServices;
	}

	public DataStore open(File file) throws DataException,
			ValidateDataParametersException {
		DataStoreParameters params = this.getParametersFor(file);
		if (params == null){
			return null;
		}
		return DALLocator.getDataManager().createStore(params);
	}

	public NewDataStoreParameters getAddParameters(File file)
			throws DataException {
		DataStoreParameters params = this.getParametersFor(file, false);
		NewDataStoreParameters newParams = this.getAddParameters(params.getDataStoreName());
		((FilesystemStoreParameters) newParams).setFile(((FilesystemStoreParameters)params).getFile());
		return newParams;
	}


}
