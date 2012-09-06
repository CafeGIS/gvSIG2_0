/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */
package org.gvsig.gui.beans.swing;

import java.awt.Component;
import java.awt.HeadlessException;
import java.io.File;
import java.util.Hashtable;

import javax.swing.filechooser.FileSystemView;

/**
 * 
 * JFileChooser.java
 * <p>JFileChooser that tracks the last visited directory for a given ID. It allows you to
 * open the JFileChooser in the same directory where you were the last time you open a file
 * in the file system.<br>
 * </p>
 * <p>
 * It needs to specify a string defining the JFileChooser ID in order to know what kind of 
 * files you are going to open. For example after creating a JFileChooser by doing this:<br>
 * <b><code>new JFileChooser("TEMPLATES_FILECHOOSER", defaultDirectory);</code></b><br> each time
 * you create another JFileChooser anywhere giving the same ID, it will point directly to
 * the last directory where you opened a file using this JFileChooser with an equal ID.
 * </p>
 * 
 * 
 * @author jaume dominguez faus - jaume.dominguez@iver.es Dec 5, 2007
 *
 */
public class JFileChooser extends javax.swing.JFileChooser {
	private static final long serialVersionUID = -2419775752576400974L;
	private static Hashtable<String, File> jfcLastPaths = new Hashtable<String, File>();
	private String fileChooserID;
	
	/**
	 * Creates a new JFileChooser with the remind last path feature. 
	 * @param fileChooserID, the id that distinguishes the wanted files (i.e. "TEMPLATES_FILECHOOSER")
	 * @param defaultDirectory, the default directory to go for the first time. It allows null, which
	 *        means the user's home directory. 
	 */
	public JFileChooser(String fileChooserID, File defaultDirectory) {
		super();
		setDragEnabled(true);
		if (fileChooserID == null) 
			throw new IllegalArgumentException("JFileChooser's ID cannot be null");
		
		this.fileChooserID = fileChooserID;

		setCurrentDirectory(getLastPath(fileChooserID, defaultDirectory));

//		if (defaultDirectory == null)
//			defaultDirectory = new File(System.getProperty("user.home"));
//		File currentDirectory;
//		if (jfcLastPaths.get(fileChooserID) != null)
//			currentDirectory = new File(jfcLastPaths.get(fileChooserID));
//		else
//			currentDirectory = defaultDirectory;
//		setCurrentDirectory(currentDirectory);
//		if (defaultDirectory != null)
//			jfcLastPaths.put(fileChooserID, currentDirectory.getAbsolutePath());
	}

	/**
	 * Returns the Last Path for this fileChooserID
	 * @param fileChooserID, the id that distinguishes the wanted files (i.e. "TEMPLATES_FILECHOOSER")
	 * @param defaultDirectory, the default directory to go for the first time. It allows null, which
	 *        means the user's home directory.
	 * @return
	 */
	static public File getLastPath(String fileChooserID, File defaultDirectory) {
		File path = jfcLastPaths.get(fileChooserID);

		if (path != null)
			return path;

		if (defaultDirectory != null)
			return defaultDirectory;

		return FileSystemView.getFileSystemView().getHomeDirectory();
	}

	/**
	 * Returns the Last Path for this JFileChooser
	 * @param defaultDirectory, the default directory to go for the first time. It allows null, which
	 *        means the user's home directory.
	 * @return
	 */
	public File getLastPath(File defaultDirectory) {
		return getLastPath(fileChooserID, defaultDirectory);
	}

	/**
	 * Returns the Last Path for this JFileChooser
	 * @return
	 */
	public File getLastPath() {
		return getLastPath(fileChooserID, null);
	}

	/**
	 * Save the Last Path for this fileChooserID
	 * @param fileChooserID
	 * @param path
	 */
	static public void setLastPath(String fileChooserID, File path) {
		jfcLastPaths.put(fileChooserID, path);
	}

	/**
	 * Save the Last Path for this JFileChooser
	 * @param path
	 */
	public void setLastPath(File path) {
		setLastPath(fileChooserID, path);
	}

	/**
	 * Creates a new JFileChooser with the remind last path feature. 
	 * @param fileChooserID, the id that distinguishes the wanted files (i.e. "TEMPLATES_FILECHOOSER")
	 * @param defaultDirectory, the default directory to go for the first time. It allows null, which
	 *        means the user's home directory. 
	 */
	public JFileChooser(String fileChooserID, String defaultDirectory) {
		this(fileChooserID, defaultDirectory!=null ? new File(defaultDirectory) : null);
	}

    /**
     * Constructs a <code>JFileChooser</code> using the given
     * <code>FileSystemView</code>.
     */
    public JFileChooser(String fileChooserID, FileSystemView fsv) {
	this(fileChooserID, (File) null, fsv);
    }


    /**
     * Constructs a <code>JFileChooser</code> using the given current directory
     * and <code>FileSystemView</code>.
     */
    public JFileChooser(String fileChooserID, File defaultDirectory, FileSystemView fsv) {
    	this(fileChooserID, defaultDirectory);
		setup(fsv);
    }

    /**
     * Constructs a <code>JFileChooser</code> using the given current directory
     * path and <code>FileSystemView</code>.
     */
    public JFileChooser(String fileChooserID, String defaultDirectoryPath, FileSystemView fsv) {
	this(fileChooserID, new File(defaultDirectoryPath), fsv);
		}

	@Override
	public int showDialog(Component parent, String approveButtonText) throws HeadlessException {
		int response = super.showDialog(parent, approveButtonText);
		if ((getSelectedFile() != null) && (getSelectedFile().isDirectory()))
			setLastPath(fileChooserID, getSelectedFile());
		else
			setLastPath(fileChooserID, getCurrentDirectory());

			return response;
		}

/* with this version it only reminds when a file was choosen and accepted (ok button clicked)
     @Override
    public int showDialog(Component parent, String approveButtonText)
    		throws HeadlessException {
    	int response = super.showDialog(parent, approveButtonText);
    	if (response == javax.swing.JFileChooser.APPROVE_OPTION) {
    		File[] ff = super.getSelectedFiles();
    		if (ff.length>0) {
    			String theFilePath = ff[0].getAbsolutePath().
    				substring(0, ff[0].getAbsolutePath().lastIndexOf(File.pathSeparator));
    			jfcLastPaths.put(fileChooserID, theFilePath);
    		} else if (super.getSelectedFile() != null) {
    			File f = super.getSelectedFile() ;
    			String theFilePath = f.getAbsolutePath().
						substring(0, f.getAbsolutePath().lastIndexOf(File.separator));
    			jfcLastPaths.put(fileChooserID, theFilePath);

    		}
    	}
*/
}
