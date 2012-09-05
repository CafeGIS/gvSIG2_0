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
package com.iver.cit.gvsig.project.documents.layout.contextmenu.gui;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.documents.layout.LayoutContext;
import com.iver.cit.gvsig.project.documents.layout.fframes.FFrameGroup;
import com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame;


/**
 * Coloca detrás de todos los fframes el fframe seleccionado.
 *
 * @author Vicente Caballero Navarro
 */
public class BehindLayoutMenuEntry extends AbstractLayoutContextMenuAction {
	public String getGroup() {
		return "position";
	}

	public int getGroupOrder() {
		return 5;
	}

	public int getOrder() {
		return 2;
	}

	public String getText() {
		return PluginServices.getText(this, "colocar_detras");
	}

	public boolean isEnabled(LayoutContext layoutContext, IFFrame[] selectedFrames) {
		return true;
	}

	public boolean isVisible(LayoutContext layoutContext, IFFrame[] selectedFrames) {
		if (selectedFrames.length==1 && !(getLayout().getLayoutControl().getGeometryAdapter().getPoints().length>0)) {
			return true;
		}
		return false;
	}


	public void execute(LayoutContext layoutContext, IFFrame[] selectedFrames) {
		layoutContext.getFrameCommandsRecord().startComplex(PluginServices.getText(this,"change_behind"));
		for (int i = selectedFrames.length - 1; i >= 0; i--) {
			IFFrame fframe = selectedFrames[i];
			if (fframe instanceof FFrameGroup) {
				((FFrameGroup) fframe).setAt(layoutContext.getAT());
			}

			IFFrame fframeAux=fframe.cloneFFrame(getLayout());
			fframeAux.setLevel(layoutContext.getNumBehind());
			layoutContext.getFrameCommandsRecord().update(fframe,fframeAux);
			fframeAux.getBoundingBox(layoutContext.getAT());

		}
		layoutContext.getFrameCommandsRecord().endComplex();
		layoutContext.updateFFrames();
		layoutContext.callLayoutDrawListeners();
	}
}
