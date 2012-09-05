/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
package com.iver.utiles;

import java.io.File;

import javax.swing.filechooser.FileFilter;


/**
 * Filtro de fichero genérico para los JFileChooser
 *
 * @author Fernando González Cortés
 */
public class GenericFileFilter extends FileFilter {
	private String[] extensiones=new String[1];
	private String description;
	private boolean dirs = true;

	/**
	 * Crea un nuevo GenericFileFilter.
	 *
	 * @param ext DOCUMENT ME!
	 * @param desc DOCUMENT ME!
	 */
	public GenericFileFilter(String[] ext, String desc) {
		extensiones = ext;
		description = desc;
	}
	/**
	 * Crea un nuevo GenericFileFilter.
	 *
	 * @param ext DOCUMENT ME!
	 * @param desc DOCUMENT ME!
	 */
	public GenericFileFilter(String ext, String desc) {
		extensiones[0] = ext;
		description = desc;
	}

	/**
	 * Crea un nuevo GenericFileFilter.
	 *
	 * @param ext DOCUMENT ME!
	 * @param desc DOCUMENT ME!
	 * @param dirs DOCUMENT ME!
	 */
	public GenericFileFilter(String ext, String desc, boolean dirs) {
		extensiones[0] = ext;
		description = desc;
		this.dirs = dirs;
	}

	/**
	 * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
	 */
	public boolean accept(File f) {
		if (f.isDirectory()) {
			if (dirs) {
				return true;
			} else {
				return false;
			}
		}
		boolean endsWith=false;
		for (int i=0;i<extensiones.length;i++){
			if (f.getName().toUpperCase().endsWith(extensiones[i].toUpperCase())){
				endsWith=true;
			}
		}
		
		return endsWith;
	}

	/**
	 * @see javax.swing.filechooser.FileFilter#getDescription()
	 */
	public String getDescription() {
		return description;
	}
}
