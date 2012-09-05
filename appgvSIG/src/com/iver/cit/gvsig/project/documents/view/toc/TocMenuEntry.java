/*
 * Created on 20-dic-2004
 *
 * gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
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
package com.iver.cit.gvsig.project.documents.view.toc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.layers.FLayer;

import com.iver.cit.gvsig.project.documents.view.toc.gui.FPopupMenu;

/**
 * @author Luis W. Sevilla (sevilla_lui@gva.es)ç
 * @deprecated
 */
public abstract class TocMenuEntry implements ActionListener {
	private FPopupMenu menu;
	//private Class [] layerTypes = null;
	public TocMenuEntry() {
	}

	/**
	 * Se utiliza para inicializar el menu. Dentro comprobará
	 * encima de qué tipo de tocItem hemos pinchado, y se
	 * mostrará la opción de menu o no en función de si
	 * es un branch o un leaf, o incluso de algún otro
	 * parámetro interno.
	 * @param menu
	 */
	public void initialize(FPopupMenu m) {
		menu = m;
	}

	/**
	 * Código que se ejecuta cuando selecciona una opción
	 * de menu. Recibe el tocItem sobre el que has pinchado.
	 * @param tocItem
	 */
	public abstract void actionPerformed(ActionEvent e);

	public FPopupMenu getMenu() { return menu; }

	public MapContext getMapContext() {
		return menu.getMapContext();
	}
	public Object getNodeUserObject() {
		if (menu.getNode() == null) {
			return null;
		}
		return menu.getNode().getUserObject();
	}

	public FLayer getNodeLayer() {
		if (isTocItemBranch()) {
			return ((TocItemBranch) getNodeUserObject()).getLayer();
		}
		return null;
	}
	public boolean isTocItemLeaf() {
		return getNodeUserObject() instanceof TocItemLeaf;
	}

	public boolean isTocItemBranch() {
		return getNodeUserObject() instanceof TocItemBranch;
	}
}
