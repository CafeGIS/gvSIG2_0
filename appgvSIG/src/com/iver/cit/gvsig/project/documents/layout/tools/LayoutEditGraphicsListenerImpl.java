/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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
 *   Av. Blasco Ib��ez, 50
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
package com.iver.cit.gvsig.project.documents.layout.tools;

import java.awt.Image;

import org.gvsig.fmap.mapcontrol.tools.BehaviorException;
import org.gvsig.fmap.mapcontrol.tools.Events.PointEvent;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.documents.layout.FLayoutUtilities;
import com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame;
import com.iver.cit.gvsig.project.documents.layout.fframes.IFFrameEditableVertex;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;
import com.iver.cit.gvsig.project.documents.layout.tools.listener.LayoutMoveListener;


/**
 * Implementaci�n de la interfaz LayoutMoveListener como herramienta para editar
 * una geometr�a.
 *
 * @author Vicente Caballero Navarro
 */
public class LayoutEditGraphicsListenerImpl implements LayoutMoveListener {
	public static final Image icrux = PluginServices.getIconTheme()
		.get("crux-cursor").getImage();
	private Layout layout;
	/**
	 * Crea un nuevo LayoutSelectionListenerImpl.
	 *
	 * @param l Layout.
	 */
	public LayoutEditGraphicsListenerImpl(Layout l) {
		this.layout = l;
	}

	/**
	 * @see org.gvsig.fmap.mapcontrol.tools.Listeners.PanListener#move(java.awt.geom.Point2D,
	 * 		java.awt.geom.Point2D)
	 */
	public void drag(PointEvent event) {
		IFFrame[] fframes=layout.getLayoutContext().getFFrames();
    	for (int i=0;i<fframes.length;i++){
    		IFFrame frame=fframes[i];
    		if (frame instanceof IFFrameEditableVertex){
    			((IFFrameEditableVertex)frame).pointDragged(FLayoutUtilities.toSheetPoint(event.getPoint(),layout.getLayoutControl().getAT()));
    			//layout.setStatus(Layout.GRAPHICS);

    		}
    	}
	}

	/**
	 * @see org.gvsig.fmap.mapcontrol.tools.Listeners.ToolListener#getCursor()
	 */
	public Image getImageCursor() {
		return icrux;
	}

	/**
	 * @see org.gvsig.fmap.mapcontrol.tools.Listeners.ToolListener#cancelDrawing()
	 */
	public boolean cancelDrawing() {
		return true;
	}

	public void press(PointEvent event) throws BehaviorException {
		IFFrame[] fframes=layout.getLayoutContext().getFFrames();
    	for (int i=0;i<fframes.length;i++){
    		IFFrame frame=fframes[i];
    		if (frame instanceof IFFrameEditableVertex){
    			((IFFrameEditableVertex)frame).pointPressed(FLayoutUtilities.toSheetPoint(event.getPoint(),layout.getLayoutControl().getAT()));
    		}
    	}
	}

	public void release(PointEvent event) throws BehaviorException {
		IFFrame[] fframes=layout.getLayoutContext().getFFrames();
    	for (int i=0;i<fframes.length;i++){
    		IFFrame frame=fframes[i];
    		if (frame instanceof IFFrameEditableVertex){
    			if (frame.getSelected()!=IFFrame.NOSELECT && ((IFFrameEditableVertex)frame).isEditing()){
    				IFFrame fframeAux=frame.cloneFFrame(layout);
    				((IFFrameEditableVertex)fframeAux).startEditing();
    				((IFFrameEditableVertex)fframeAux).pointReleased(FLayoutUtilities.toSheetPoint(event.getPoint(),layout.getLayoutControl().getAT()),((IFFrameEditableVertex)frame).getGeometry());
    				layout.getLayoutContext().getFrameCommandsRecord().update(frame,fframeAux);
    				fframeAux.getBoundingBox(layout.getLayoutControl().getAT());
    				layout.getLayoutContext().updateFFrames();
    			}
    		}
    	}
    	layout.getLayoutControl().refresh();
	}

	public void click(PointEvent event) {
	}

	public void move(PointEvent event) throws BehaviorException {
	}

}
