/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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
 */
package org.gvsig.raster.gui.wizards;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.filechooser.FileFilter;
/**
 * Clase abstracta que deben implementar todas aquellas extensiones que quieran
 * aparecer en un punto com�n del cuadro de dialogo de apertura de ficheros.
 *
 * @version 19/11/2007
 * @author BorSanZa - Borja S�nchez Zamorano (borja.sanchez@iver.es)
 */
public abstract class AbstractFileOpen implements IFileOpen {
	private ArrayList<FileFilter> arrayFileFilter = null;

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.addlayer.fileopen.IFileOpen#pre()
	 */
	public void pre() {
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.addlayer.fileopen.IFileOpen#post(java.io.File)
	 */
	public File post(File file) {
		return file;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.gui.wizards.IFileOpen#getFileFilter()
	 */
	public final List getFileFilter() {
		if (arrayFileFilter == null) {
			arrayFileFilter = new ArrayList<FileFilter>();
		}
		return arrayFileFilter;
	}
}
