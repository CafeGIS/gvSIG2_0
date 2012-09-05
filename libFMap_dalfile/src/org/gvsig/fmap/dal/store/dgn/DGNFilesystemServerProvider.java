package org.gvsig.fmap.dal.store.dgn;

import java.io.File;

import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.NewDataStoreParameters;
import org.gvsig.fmap.dal.exception.CreateException;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.FileNotFoundException;
import org.gvsig.fmap.dal.exception.RemoveException;
import org.gvsig.fmap.dal.resource.spi.ResourceConsumer;
import org.gvsig.fmap.dal.resource.spi.ResourceProvider;
import org.gvsig.fmap.dal.serverexplorer.filesystem.spi.FilesystemServerExplorerProvider;
import org.gvsig.fmap.dal.serverexplorer.filesystem.spi.FilesystemServerExplorerProviderServices;

public class DGNFilesystemServerProvider implements
		FilesystemServerExplorerProvider, ResourceConsumer {

	private FilesystemServerExplorerProviderServices serverExplorer;

	public String getDataStoreProviderName() {
		return DGNStoreProvider.NAME;
	}

	public boolean accept(File pathname) {
		return (pathname.getName().toLowerCase().endsWith(".dgn"));
	}

	public String getDescription() {
		return DGNStoreProvider.DESCRIPTION;
	}

	public DataStoreParameters getParameters(File file) throws DataException {
		DataManager manager = DALLocator.getDataManager();
		DGNStoreParameters params = (DGNStoreParameters) manager
				.createStoreParameters(this
				.getDataStoreProviderName());
		params.setFileName(file.getPath());
		return params;
	}

	public boolean canCreate() {
		return false;
	}

	public boolean canCreate(NewDataStoreParameters parameters) {
		return this.canCreate();
	}

	public void create(NewDataStoreParameters parameters, boolean overwrite)
			throws CreateException {
		// FIXME Exception
		throw new UnsupportedOperationException();
	}

	public NewDataStoreParameters getCreateParameters() throws DataException {
		return null;
	}

	public void initialize(
			FilesystemServerExplorerProviderServices serverExplorer) {
		this.serverExplorer = serverExplorer;
	}

	public void remove(DataStoreParameters parameters) throws RemoveException {
		File file = new File(((DGNStoreParameters) parameters).getFileName());
		if (!file.exists()) {
			throw new RemoveException(this.getDataStoreProviderName(),
					new FileNotFoundException(file));
		}
		if (!file.delete()) {
			// FIXME throws ???
		}

	}

	public boolean closeResourceRequested(ResourceProvider resource) {
		// while it is using a resource anyone can't close it
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.gvsig.fmap.dal.resource.spi.ResourceConsumer#resourceChanged(org.
	 * gvsig.fmap.dal.resource.spi.ResourceProvider)
	 */
	public void resourceChanged(ResourceProvider resource) {
		//Do nothing

	}


}
