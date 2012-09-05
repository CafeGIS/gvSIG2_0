package org.gvsig.fmap.dal.store.dbf;

import java.io.File;
import java.io.IOException;

import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.NewDataStoreParameters;
import org.gvsig.fmap.dal.exception.CreateException;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.FileNotFoundException;
import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.exception.RemoveException;
import org.gvsig.fmap.dal.feature.NewFeatureStoreParameters;
import org.gvsig.fmap.dal.resource.file.FileResource;
import org.gvsig.fmap.dal.resource.spi.ResourceConsumer;
import org.gvsig.fmap.dal.resource.spi.ResourceProvider;
import org.gvsig.fmap.dal.serverexplorer.filesystem.spi.FilesystemServerExplorerProvider;
import org.gvsig.fmap.dal.serverexplorer.filesystem.spi.FilesystemServerExplorerProviderServices;

public class DBFFilesystemServerProvider implements FilesystemServerExplorerProvider,
		ResourceConsumer {

	protected FilesystemServerExplorerProviderServices serverExplorer;

	public String getDataStoreProviderName() {
		return DBFStoreProvider.NAME;
	}

	public boolean accept(File pathname) {
		return (pathname.getName().toLowerCase().endsWith(".dbf"));
	}

	public String getDescription() {
		return DBFStoreProvider.DESCRIPTION;
	}

	public DataStoreParameters getParameters(File file) throws DataException {
		DataManager manager = DALLocator.getDataManager();
		DBFStoreParameters params = (DBFStoreParameters) manager
				.createStoreParameters(this.getDataStoreProviderName());
		params.setDBFFileName(file.getPath());
		return params;
	}

	public boolean canCreate() {
		return true;
	}

	public boolean canCreate(NewDataStoreParameters parameters) {
		DBFNewStoreParameters params = (DBFNewStoreParameters) parameters;
		if (params.getDBFFile().getParentFile().canWrite()) {
			return false;
		}
		if (params.getDBFFile().exists()) {
			if (!params.getDBFFile().canWrite()) {
				return false;
			}
		}
		if (params.getDefaultFeatureType() == null) {
			return false;
		}
		return true;
	}

	public void create(NewDataStoreParameters parameters, boolean overwrite)
			throws CreateException {

		DBFNewStoreParameters params = (DBFNewStoreParameters) parameters;
		File file = params.getDBFFile();

		if (file.exists()) {
			if (overwrite) {
//				if (!file.delete()) {
//					throw new CreateException(this.getDataStoreProviderName(),
//							new IOException(
//							"cannot delete file"));
//				}
			} else {
				throw new CreateException(this.getDataStoreProviderName(),
						new IOException(
						"file already exist"));
			}
		}

		FileResource resource;
		try {

			resource = (FileResource) this.serverExplorer
					.getServerExplorerProviderServices().createResource(
							FileResource.NAME,
					new Object[] { file.getAbsolutePath() });
		} catch (InitializeException e1) {
			throw new CreateException(params.getDBFFileName(), e1);
		}
		resource.addConsumer(this);

		try {
			resource.begin();

			DBFFeatureWriter writer = new DBFFeatureWriter(this
					.getDataStoreProviderName());

			writer.begin(params, params.getDefaultFeatureType(), 0);
			writer.end();
			writer.dispose();

			resource.notifyChanges();
		} catch (Exception e) {
			throw new CreateException(this.getDataStoreProviderName(), e);
		} finally {
			resource.end();
			resource.removeConsumer(this);
		}


	}


	protected NewDataStoreParameters createInstanceNewDataStoreParameters() {
		return new DBFNewStoreParameters();
	}


	public NewDataStoreParameters getCreateParameters() {
		NewFeatureStoreParameters params = (NewFeatureStoreParameters) this
				.createInstanceNewDataStoreParameters();
		params.setDefaultFeatureType(this.serverExplorer
				.getServerExplorerProviderServices().createNewFeatureType());
		return params;
	}

	public void initialize(FilesystemServerExplorerProviderServices serverExplorer) {
		this.serverExplorer = serverExplorer;

	}

	public void remove(DataStoreParameters parameters) throws RemoveException {
		DBFStoreParameters params = (DBFStoreParameters) parameters;
		File file = params.getDBFFile();
		if (!file.exists()) {
			throw new RemoveException(this.getDataStoreProviderName(),
					new FileNotFoundException(params.getDBFFile()));
		}
		if (!file.delete()) {
			throw new RemoveException(this.getDataStoreProviderName(),
					new IOException()); // FIXME Exception
		}
	}

	public boolean closeResourceRequested(ResourceProvider resource) {
		// while it is using a resource anyone can't close it
		return !(this.equals(resource));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.gvsig.fmap.dal.resource.spi.ResourceConsumer#resourceChanged(org.
	 * gvsig.fmap.dal.resource.spi.ResourceProvider)
	 */
	public void resourceChanged(ResourceProvider resource) {
		// Do nothing
	}

}
