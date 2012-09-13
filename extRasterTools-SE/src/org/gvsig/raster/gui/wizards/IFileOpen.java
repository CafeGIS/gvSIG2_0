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
package org.gvsig.raster.gui.wizards;

import java.io.File;
import java.util.List;

import javax.swing.filechooser.FileFilter;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.mapcontrol.MapControl;

/**
 * Interfaz que deben implementar todas aquellas extensiones que quieran
 * aparecer en un punto común del cuadro de dialogo de apertura de ficheros.
 *
 * @version 03/09/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public interface IFileOpen {
	/**
	 * Metodo que se invocará justo antes de abrir el panel de ficheros.
	 */
	public void pre();

	/**
	 * Metodo que se invocará despues de haber aceptado los ficheros en el cuadro
	 * de diálogo, para poder intentar configurar los parámetros de cada fichero.
	 * Se pasa un fichero por parametro, si se devuelve null, el fichero no se
	 * tomará en cuenta, en caso contrario, se devolverá el fichero real que se
	 * desea tratar (puede ser distinto al fichero de entrada).
	 *
	 * @param file
	 * @return
	 */

	public File post(File file);

	/**
	 * Metodo que se invocará al aceptar los ficheros. Se ejecuta después del
	 * pre() y el post(), es cuando se le da definitivamente a añadir los
	 * ficheros.
	 * @param file
	 * @param mapControl
	 * @param driverName
	 * @return
	 */
	public Envelope createLayer(File file, MapControl mapControl,
			FileFilter driverName, IProjection proj);

	/**
	 * Devolverá todos los FileFilter que puede manejar dicha extensión
	 * @return
	 */
	public List<FileFilter> getFileFilter();
}