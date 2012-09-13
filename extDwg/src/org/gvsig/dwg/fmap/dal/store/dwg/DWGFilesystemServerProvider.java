package org.gvsig.dwg.fmap.dal.store.dwg;

import java.io.File;

import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.NewDataStoreParameters;
import org.gvsig.fmap.dal.exception.CreateException;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.FileNotFoundException;
import org.gvsig.fmap.dal.exception.RemoveException;
import org.gvsig.fmap.dal.feature.NewFeatureStoreParameters;
import org.gvsig.fmap.dal.resource.spi.ResourceConsumer;
import org.gvsig.fmap.dal.resource.spi.ResourceProvider;
import org.gvsig.fmap.dal.serverexplorer.filesystem.spi.FilesystemServerExplorerProvider;
import org.gvsig.fmap.dal.serverexplorer.filesystem.spi.FilesystemServerExplorerProviderServices;

public class DWGFilesystemServerProvider implements
		FilesystemServerExplorerProvider, ResourceConsumer {

	private FilesystemServerExplorerProviderServices serverExplorer;

	public String getDataStoreProviderName() {
		return DWGStoreProvider.NAME;
	}

	public boolean accept(File pathname) {
		return (pathname.getName().toLowerCase().endsWith(".dwg"));
	}

	public String getDescription() {
		return DWGStoreProvider.DESCRIPTION;
	}

	public DataStoreParameters getParameters(File file) throws DataException {
		DataManager manager = DALLocator.getDataManager();
		DWGStoreParameters params = (DWGStoreParameters) manager
				.createStoreParameters(this
				.getDataStoreProviderName());
		params.setFileName(file.getPath());
		return params;
	}

	public boolean canCreate() {
		return true;
	}

	public boolean canCreate(NewDataStoreParameters parameters) {
		if (!(parameters instanceof DWGStoreParameters)) {
			throw new IllegalArgumentException(); // FIXME ???
		}
		DWGStoreParameters dwgParams = (DWGStoreParameters) parameters;
		// TODO comporbar si el ftype es correcto (para este formato es fijo)
		File file = new File(dwgParams.getFileName());

		if (dwgParams.getSRSID() == null) {
			return false;
		}
		// TODO comprobamos extension del fichero ??
		if (file.exists()) {
			return file.canWrite();
		} else {
			return file.getParentFile().canWrite();
		}
	}

	public void create(NewDataStoreParameters parameters, boolean overwrite)
			throws CreateException {
		// FIXME Exception
		throw new UnsupportedOperationException();
	}

	public NewDataStoreParameters getCreateParameters() throws DataException {
		return (NewFeatureStoreParameters) DALLocator.getDataManager()
				.createStoreParameters(this.getDataStoreProviderName());
	}

	public void initialize(
			FilesystemServerExplorerProviderServices serverExplorer) {
		this.serverExplorer = serverExplorer;
	}

	public void remove(DataStoreParameters parameters) throws RemoveException {
		File file = new File(((DWGStoreParameters) parameters).getFileName());
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
