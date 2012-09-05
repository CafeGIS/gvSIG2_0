package com.iver.ai2.gvsig3d.resources;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * 
 * @version 14/08/2007
 * @author Angel Fraile
 * 
 */
public class MyFileFilter3D extends FileFilter {

	private String[] extensiones = new String[1];
	private String description;
	private boolean dirs = true;
	private String info = null;

	public MyFileFilter3D(String[] ext, String desc) {
		extensiones = ext;
		description = desc;
	}

	public MyFileFilter3D(String[] ext, String desc, String info) {
		extensiones = ext;
		description = desc;
		this.info = info;
	}

	public MyFileFilter3D(String ext, String desc) {
		extensiones[0] = ext;
		description = desc;
	}

	public MyFileFilter3D(String ext, String desc, String info) {
		extensiones[0] = ext;
		description = desc;
		this.info = info;
	}

	public MyFileFilter3D(String ext, String desc, boolean dirs) {
		extensiones[0] = ext;
		description = desc;
		this.dirs = dirs;
	}

	public MyFileFilter3D(String ext, String desc, boolean dirs, String info) {
		extensiones[0] = ext;
		description = desc;
		this.dirs = dirs;
		this.info = info;
	}

	public boolean accept(File f) {
		if (f.isDirectory()) {
			if (dirs) {
				return true;
			} else {
				return false;
			}
		}
		for (int i = 0; i < extensiones.length; i++) {
			if (extensiones[i].equals("")) {
				continue;
			}
			if (getExtensionOfAFile(f).equalsIgnoreCase(extensiones[i])) {
				return true;
			}
		}

		return false;
	}

	/**
	 * @see javax.swing.filechooser.FileFilter#getDescription()
	 */
	public String getDescription() {
		return description;
	}

	public String[] getExtensions() {
		return extensiones;
	}

	public boolean isDirectory() {
		return dirs;
	}

	private String getExtensionOfAFile(File file) {
		String name;
		int dotPos;
		name = file.getName();
		dotPos = name.lastIndexOf(".");
		if (dotPos < 1) {
			return "";
		}
		return name.substring(dotPos + 1);
	}

	public File normalizeExtension(File file) {
		String ext = getExtensionOfAFile(file);
		if (ext.equals("") || !(this.accept(file))) {
			return new File(file.getAbsolutePath() + "." + extensiones[0]);
		}
		return file;
	}

	public String getInfo() {
		return this.info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

}

