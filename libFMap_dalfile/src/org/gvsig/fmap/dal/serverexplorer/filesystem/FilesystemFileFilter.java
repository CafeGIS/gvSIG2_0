package org.gvsig.fmap.dal.serverexplorer.filesystem;

import java.io.FileFilter;

public interface FilesystemFileFilter extends FileFilter {

	public String getDataStoreProviderName();
	public String getDescription();

}
