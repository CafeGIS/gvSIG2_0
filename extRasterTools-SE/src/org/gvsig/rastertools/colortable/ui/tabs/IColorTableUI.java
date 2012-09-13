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
package org.gvsig.rastertools.colortable.ui.tabs;

import javax.swing.JPanel;

import org.gvsig.raster.datastruct.ColorTable;
/**
 * Interfaz para poder hacer paneles de edicion de tablas de color
 * @version 04/01/2008
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public interface IColorTableUI {
	
	/**
	 * Devuelve el JPanel del panel de edicion
	 * @return
	 */
	public JPanel getPanel();

	/**
	 * Devuelve la Tabla de color que se esta visualizando en el Panel en ese
	 * momento
	 * @return
	 */
	public ColorTable getColorTable();
	
	/**
	 * Define que Tabla de color se ha de mostrar en el Panel
	 * @param colorTable
	 */
	public void setColorTable(ColorTable colorTable);
	
	/**
	 * Añade la gestion de eventos para cuando cambia el panel
	 * @param listener
	 */
	public void addColorTableUIChangedListener(ColorTableUIListener listener);
	
	/**
	 * Elimina la gestion de eventos para cuando cambia el panel
	 * @param listener
	 */
	public void removeColorTableUIChangedListener(ColorTableUIListener listener);
}