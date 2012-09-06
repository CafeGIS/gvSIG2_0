package org.gvsig.gui.beans.openfile;

import java.io.File;

public abstract class FileFilter extends javax.swing.filechooser.FileFilter {

	/**
	 * <p>Gets the default file extension, which will be
	 * added to the selected file if its extension is not
	 * valid.</p>
	 * 
	 * @return
	 */
	public abstract String getDefaultExtension();
}
