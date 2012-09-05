package org.gvsig.fmap.dal.store.dxf;

import java.io.File;
import java.io.IOException;

import org.cresques.cts.IProjection;
import org.cresques.io.DxfFile;
import org.cresques.px.dxf.DxfEntityMaker;
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

public class DXFFilesystemServerProvider implements
		FilesystemServerExplorerProvider, ResourceConsumer {

	private FilesystemServerExplorerProviderServices serverExplorer;

	public String getDataStoreProviderName() {
		return DXFStoreProvider.NAME;
	}

	public boolean accept(File pathname) {
		return (pathname.getName().toLowerCase().endsWith(".dxf"));
	}

	public String getDescription() {
		return DXFStoreProvider.DESCRIPTION;
	}

	public DataStoreParameters getParameters(File file) throws DataException {
		DataManager manager = DALLocator.getDataManager();
		DXFStoreParameters params = (DXFStoreParameters) manager
				.createStoreParameters(this
				.getDataStoreProviderName());
		params.setFileName(file.getPath());
		return params;
	}

	public boolean canCreate() {
		return true;
	}

	public boolean canCreate(NewDataStoreParameters parameters) {
		if (!(parameters instanceof DXFStoreParameters)) {
			throw new IllegalArgumentException(); // FIXME ???
		}
		DXFStoreParameters dxfParams = (DXFStoreParameters) parameters;
		// TODO comporbar si el ftype es correcto (para este formato es fijo)
		File file = new File(dxfParams.getFileName());

		if (dxfParams.getSRSID() == null) {
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

		IProjection projection = null;
		DXFStoreParameters params = (DXFStoreParameters) parameters;

		if (params.getFile().exists()) {
			if (overwrite) {
				if (!params.getFile().delete()) {
					throw new CreateException(this.getDataStoreProviderName(),
							new IOException(
							"cannot delete file"));
				}
			} else {
				throw new CreateException(this.getDataStoreProviderName(),
						new IOException(
						"file already exist"));
			}
		}

		//		projection = CRSFactory.getCRS(params.getSRSID());

		File file = params.getFile();
		FileResource resource;
		try {
			resource = (FileResource) this.serverExplorer
					.getServerExplorerProviderServices().createResource(
							FileResource.NAME,
					new Object[] { file.getAbsolutePath() });
		} catch (InitializeException e1) {
			throw new CreateException(params.getFileName(), e1);
		}
		resource.addConsumer(this);

		try {
			resource.begin();
			Builder builder = new Builder().initialice(resource.getFileName(),
					projection);
			resource.notifyOpen();
			builder.begin();
			builder.create();
			builder.end();
			resource.notifyClose();

			resource.setData(null); // FIXME: Seguro que hay que ponerlo a null
			// ??
			resource.notifyChanges();
		} catch (Exception e) {
			throw new CreateException(params.getFileName(), e);
		} finally {
			resource.end();
			resource.removeConsumer(this);
		}
	}

	public class Builder {
		private String fileName;
		private IProjection projection;

		public Builder initialice(String fileName, IProjection projection) {
			this.fileName = fileName;
			this.projection = projection;
			return this;
		}

		public void begin() {

		}

		public void end() {

		}

		public void create() throws IOException {
			DxfEntityMaker entityMaker = new DxfEntityMaker(projection);
			DxfFile dxfFile = new DxfFile(null, fileName, entityMaker);
			dxfFile.setCadFlag(true);
			dxfFile.setDxf3DFlag(false);
			dxfFile.save(fileName);
		}
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
		File file = new File(((DXFStoreParameters) parameters).getFileName());
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
