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
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.gvsig.fmap.mapcontrol.tools.BehaviorException;
import org.gvsig.fmap.mapcontrol.tools.Events.PointEvent;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.documents.layout.FLayoutGraphics;
import com.iver.cit.gvsig.project.documents.layout.FLayoutUtilities;
import com.iver.cit.gvsig.project.documents.layout.LayoutControl;
import com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame;
import com.iver.cit.gvsig.project.documents.layout.fframes.IFFrameGroupSelectable;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;
import com.iver.cit.gvsig.project.documents.layout.tools.listener.LayoutMoveListener;


/**
 * Implementaci�n de la interfaz LayoutPanListener como herramienta para realizar una
 * selecci�n.
 *
 * @author Vicente Caballero Navarro
 */
public class LayoutSelectListenerImpl implements LayoutMoveListener {
	public static final Image icrux = PluginServices.getIconTheme()
	.get("crux-cursor").getImage();
	private Layout layout;
	private Point2D m_pointSelected;
	private int index = 0;
	 private ArrayList lastSelect = new ArrayList();
	/**
	 * Crea un nuevo LayoutSelectionListenerImpl.
	 *
	 * @param l Layout.
	 */
	public LayoutSelectListenerImpl(Layout l) {
		this.layout = l;
	}

	/**
	 * @see org.gvsig.fmap.mapcontrol.tools.Listeners.PanListener#move(java.awt.geom.Point2D,
	 * 		java.awt.geom.Point2D)
	 */
	public void drag(PointEvent event) {
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
		m_pointSelected = event.getPoint();
		layout.getLayoutControl().setIsReSel(true);
		IFFrame[] fframes=layout.getLayoutContext().getFFrames();
        for (int i = 0; i < fframes.length; i++) {
            IFFrame fframe = fframes[i];

            if (m_pointSelected != null) {
                if (!event.getEvent().isShiftDown()) {
                    if ((fframe.getSelected() != IFFrame.NOSELECT)) {
                        fframe.setSelected(m_pointSelected,event.getEvent());
                    }
                }else if (fframe instanceof IFFrameGroupSelectable){
                	    fframe.setSelected(m_pointSelected,event.getEvent());
                }
            }

            if (fframe.getSelected() != IFFrame.NOSELECT) {
                layout.getLayoutControl().setIsReSel(false);
            }


        }

        if ((layout.getLayoutControl().getLastPoint() != null) &&
                (layout.getLayoutControl().getFirstPoint() != null)) {
            layout.getLayoutControl().getLastPoint().setLocation(layout.getLayoutControl().getFirstPoint());
        }

        if (event.getEvent().getClickCount() < 2) {
            layout.getLayoutControl().setStatus(LayoutControl.ACTUALIZADO);
            layout.repaint();
        }

	}

	public void release(PointEvent event) throws BehaviorException {
		layout.getLayoutControl().getLayoutFunctions().setSelect();
        layout.getLayoutControl().refresh();
        layout.getLayoutControl().setIsReSel(false);
	}
	public void click(PointEvent event) {
		 if (event.getEvent().getButton() == MouseEvent.BUTTON1) {
                 m_pointSelected = event.getPoint();
                 IFFrame[] fframes=layout.getLayoutContext().getFFrames();
                 if (fframes.length > 0) {
                     ArrayList listSelect = new ArrayList();
                     for (int j = 0; j < fframes.length; j++) {
                         if (fframes[j].getContains(
                                     m_pointSelected) != IFFrame.NOSELECT) {
                             listSelect.add(fframes[j]);
                         }
                     }

                     if (listSelect.size() > 0) {
                         for (int k = 0; k < listSelect.size(); k++) {
                             if (((IFFrame) listSelect.get(k)).getSelected() != IFFrame.NOSELECT) {
                                 index = listSelect.size() - k;

                                 break;
                             }
                         }

                         if (!FLayoutUtilities.isEqualList(listSelect,
                                     lastSelect) ||
                                 (index > (listSelect.size() - 1))) {
                             index = 0;
                         }
                         for (int j = 0; j < fframes.length;
                                 j++) {
                             IFFrame fframe = fframes[j];

                             if (!event.getEvent().isShiftDown()) {
                                 fframe.setSelected(false);
                             } else {
                                 if (fframe.getSelected() != IFFrame.NOSELECT) {
                                     if (fframe.getContains(m_pointSelected) != IFFrame.NOSELECT) {
                                         fframe.setSelected(false);
                                     }
                                 }
                             }
                         }

                         ((IFFrame) listSelect.get((listSelect.size() - 1 -
                             index))).setSelected(true);
                         index++;
                         lastSelect = listSelect;
                     }

                     layout.getLayoutControl().setStatus(LayoutControl.ACTUALIZADO);
                     layout.repaint();

                     if (event.getEvent().getClickCount() > 1) {
                         FLayoutGraphics flg = new FLayoutGraphics(layout);
                         flg.openFFrameDialog();
                         layout.getLayoutContext().updateFFrames();
         				 layout.getLayoutContext().callLayoutDrawListeners();
                     }
                 }

                 PluginServices.getMainFrame().enableControls();
         }
	}

	public void move(PointEvent event) throws BehaviorException {
		 Image cursor = null;
         Point2D p = event.getPoint();
         IFFrame[] fframes=layout.getLayoutContext().getFFrameSelected();
         for (int i = 0; i < fframes.length; i++) {
             if (fframes[i].getContains(p)!=IFFrame.NOSELECT){
             	cursor=fframes[i].getMapCursor(p);
             }
             if (cursor != null) {
                 layout.getLayoutControl().setMapCursor(cursor);
             }
//             else {
//                 layout.setMapCursor(Layout.icrux);
//             }
         }
  	}

}
