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
package org.gvsig.raster.gui;

import javax.swing.Icon;

import org.gvsig.fmap.mapcontext.layers.FLayer;

import com.iver.cit.gvsig.project.documents.view.toc.ITocItem;
/**
 * Interfaz que debe implementar quien quiera aparecer en la barra de
 * herramientas gen�rica.
 * 
 * @version 06/02/2008
 * @author BorSanZa - Borja S�nchez Zamorano (borja.sanchez@iver.es)
 */
public interface IGenericToolBarMenuItem {
	/**
	 * Devuelve el nombre del grupo al que pertenece el item de menu
	 * @return
	 */
	public String getGroup();
	
	/**
	 * Devuelve el orden en el que aparecer� en el men�
	 * @return
	 */
	public int getOrder();

	/**
	 * Devuelve el orden en el que aparecer� en el men�
	 * @return
	 */
	public int getGroupOrder();
	
	/**
	 * Devuelve el texto que se ver� en el men�
	 * @return
	 */
	public String getText();
	
	/**
	 * Devuelve el icono del item del menu
	 * @return
	 */
	public Icon getIcon();

	/**
	 * Dice si es visible el item de menu para dicha entrada
	 * @param item
	 * @param selectedItems
	 * @return
	 */
	public boolean isVisible(ITocItem item, FLayer[] selectedItems);
	
	/**
	 * Dice si el item actual esta habilitado
	 * @param item
	 * @param selectedItems
	 * @return
	 */
	public boolean isEnabled(ITocItem item, FLayer[] selectedItems);
	
	/**
	 * Metodo que sera invocado cuando el item del menu sea presionado
	 * @param item
	 * @param selectedItems
	 */
	public void execute(ITocItem item, FLayer[] selectedItems);
}