/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2007 IVER T.I. and Generalitat Valenciana.
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
package org.gvsig.rastertools.pixelincrease;

import java.awt.event.MouseEvent;

import org.gvsig.fmap.mapcontrol.tools.BehaviorException;
import org.gvsig.fmap.mapcontrol.tools.Behavior.Behavior;
import org.gvsig.fmap.mapcontrol.tools.Listeners.ToolListener;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.project.documents.view.gui.IView;

/**
 * Behavior para que se añada a los Behavior del Mapcontrol y poder
 * gestionar el evento de mover el ratón sobre la vista de gvSIG.
 *
 * 12-may-2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class PixelIncreaseBehavior extends Behavior {

	private PixelIncreaseDialog dialog = null;
	
	/**
	 * Constructor. Asigna el dialogo
	 * @param dialog
	 */
	public PixelIncreaseBehavior(PixelIncreaseDialog dialog) {
		this.dialog = dialog;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Behavior.IBehavior#getListener()
	 */
	public ToolListener getListener() {
		return null;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Behavior.IBehavior#mouseMoved(java.awt.event.MouseEvent)
	 */
	public void mouseMoved(MouseEvent e) throws BehaviorException {
		IView view = null;
		IWindow active = PluginServices.getMDIManager().getActiveWindow();
		if(active instanceof IView) {
			//System.out.println(((IView)active).getMapControl().getX() + " " + ((IView)active).getMapControl().getWidth());
			//System.out.println(((IView)active).getMapControl().getY() + " " + ((IView)active).getMapControl().getHeight());
			//((IView)active).getMapControl().getCurrentMapTool().ge
			dialog.setClear(false);
			view = (IView)active;
			if(!dialog.getView().equals(view))
				dialog.setView(view);
			//Calcula la posición donde se empezará a dibujar en el componente. Para ello en ancho y el alto del componente
			//se ponen en la misma escala que la vista (w / scale), se calcula el punto medio del componente y se le resta el punto seleccionado
			//en la vista
			int pX = ((dialog.getWidth() / dialog.getScale()) >> 1) - e.getX();
			int pY = ((dialog.getHeight() / dialog.getScale()) >> 1) - e.getY();
			dialog.pixX = (int)e.getPoint().getX();
			dialog.pixY = (int)e.getPoint().getY();
			dialog.setPosX(pX);
			dialog.setPosY(pY);
			dialog.repaint();
		}	
	}

}
