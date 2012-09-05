/*
 * Created on 06-jul-2004
 *
 */
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
package com.iver.cit.gvsig.project.documents.layout.fframes.gui.dialogs;

import java.awt.geom.Rectangle2D;

import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame;


/**
 * Interface para la creación de los diálogos que añaden elementos al Layout.
 *
 * @author Vicente Caballero Navarro
 */
public interface IFFrameDialog extends IWindow {
	/**
	 * Rellena el Rectángulo que representa el tamaño y posición del fframe.
	 *
	 * @param r BoundingBox del fframe.
	 */
	public void setRectangle(Rectangle2D r);

	/**
	 * Devuelve la información de la ventana.
	 *
	 * @return Información referente a la ventana.
	 */
	public WindowInfo getWindowInfo();

	/**
	 * Devuelve true si ha sido aceptado el diálogo.
	 *
	 * @return True si se ha aceptado.
	 */
	public boolean getIsAcepted();

	public IFFrame getFFrame();
}
