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
 */
package org.gvsig.raster.util;

import java.io.File;
import java.util.ArrayList;

import javax.swing.filechooser.FileFilter;

import com.iver.andami.PluginServices;
/**
 * ExtendedFileFilter es una clase para usarla junto a los JFileChooser.
 * Ofrece una funcionalidad simple para poder agregar extensiones de manera
 * comoda y rapida. Las descripciones ya las pone con un formato, asi que esto
 * es opcional poner una descripción especifica.
 * 
 * Un ejemplo típico de como se usaria:
 * <pre>
 * // Usamos el JFileChooser de libUIComponents
 * JFileChooser chooser = new JFileChooser(this.getClass().toString(), (File) null);
 * // Desactivamos el modo de ver todos los ficheros
 * chooser.setAcceptAllFileFilterUsed(false);
 * // Activamos la multiseleccion
 * chooser.setMultiSelectionEnabled(true);
 * // Nos guardamos cada tipo de fichero en uno que contenga todos
 * ExtendedFileFilter allFilters = new ExtendedFileFilter();
 * for (int i = 0; i < formats.length; i++) {
 *   ExtendedFileFilter fileFilter = new ExtendedFileFilter();
 *   fileFilter.addExtension(formats[i]);
 *   // Agregamos el filefilter al JFileChooser
 *   chooser.addChoosableFileFilter(fileFilter);
 *   // Agregamos el mismo filtro a un ExtendedFileFilter global 
 *   allFilters.addExtension(formats[i]);
 * }
 * // Poner una descripcion (OPCIONAL) para todos los ficheros.
 * allFilters.setDescription(PluginServices.getText(this, "todos_soportados"));
 * // Lo añadimos
 * chooser.addChoosableFileFilter(allFilters);
 * // Y lo dejamos seleccionado por defecto
 * chooser.setFileFilter(allFilters);
 * </pre>
 * 
 * @version 21/11/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class ExtendedFileFilter extends FileFilter {
	String description = null;

	ArrayList extensions = new ArrayList();

	/**
	 * Constructor de un ExtendedFileFilter
	 */
	public ExtendedFileFilter() {
	}

	/**
	 * Construye un ExtendedFileFilter con una extensión ya agregada
	 * @param extension
	 */
	public ExtendedFileFilter(String extension) {
		addExtension(extension);
	}

	/**
	 * Añade una extensión a la lista de extensiones soportadas
	 * @param extension
	 */
	public void addExtension(String extension) {
		if (extension == null)
			return;

		extensions.add(extension);
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
	 */
	public boolean accept(File f) {
		if (f.isDirectory())
			return true;

		String s = f.getName();
		int i = s.lastIndexOf('.');

		if (i > 0 && i < s.length() - 1) {
			String extension = s.substring(i + 1).toLowerCase();
			for (int j = 0; j < extensions.size(); j++) {
				if (extensions.get(j).toString().toLowerCase().equals(extension))
					return true;
			}
		}

		return false;
	}

	/**
	 * Normaliza el nombre de un fichero, añadiendo la extension si fuera
	 * necesario
	 * @param file
	 * @return
	 */
	public String getNormalizedFilename(File file) {
		String s = file.getName();
		int i = s.lastIndexOf('.');

		if (i > 0 && i < s.length() - 1) {
			String extension = s.substring(i + 1).toLowerCase();
			for (int j = 0; j < extensions.size(); j++) {
				if (extensions.get(j).toString().toLowerCase().equals(extension))
					return file.toString();
			}
		}

		return file.toString() + "." + extensions.get(0).toString().toLowerCase();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.filechooser.FileFilter#getDescription()
	 */
	public String getDescription() {
		String format1 = "";
		String format2 = "";
		for (int j = 0; j < extensions.size(); j++) {
			if (format1.length() != 0) {
				format1 = format1 + ", ";
				format2 = format2 + "; ";
			}
			// Files JPG, GIF, ... (*.jpg; *.gif ...)
			if (j >= 4) {
				format1 = "...";
				format2 = "...";
				break;
			}
			format1 = format1 + extensions.get(j).toString().toUpperCase();
			format2 = format2 + "*." + extensions.get(j).toString().toLowerCase();
		}
		if (description == null)
			return PluginServices.getText(this, "files") + " " + format1 + " (" + format2 + ")";

		return description + " (" + format2 + ")";
	}

	/**
	 * Especifica la descripcion del item
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Borra una extension de la lista de extensiones
	 * @param extension
	 */
	public void removeExtension(String extension){
		extensions.remove(extension);
 }

	/**
	 * Borra todas las extensiones existentes
	 */
	public void clearExtensions(){
		extensions.clear();
	}

	/**
	 * Devuelve una lista con las extensiones disponibles
	 * @return
	 */
	public ArrayList getExtensions(){
		return extensions;
	}
}