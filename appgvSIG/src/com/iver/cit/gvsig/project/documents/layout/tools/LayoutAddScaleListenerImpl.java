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
import java.awt.geom.Rectangle2D;

import org.gvsig.fmap.mapcontrol.tools.BehaviorException;
import org.gvsig.fmap.mapcontrol.tools.Events.EnvelopeEvent;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.documents.layout.FLayoutUtilities;
import com.iver.cit.gvsig.project.documents.layout.fframes.FFrameScaleBar;
import com.iver.cit.gvsig.project.documents.layout.fframes.FFrameScaleBarFactory;
import com.iver.cit.gvsig.project.documents.layout.fframes.FrameFactory;
import com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame;
import com.iver.cit.gvsig.project.documents.layout.fframes.gui.dialogs.FFrameScaleBarDialog;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;
import com.iver.cit.gvsig.project.documents.layout.tools.listener.LayoutAddRectangleListener;


/**
 * Implementaci�n de la interfaz LayoutRectangleListener como herramienta para
 * realizar una inserci�n por rect�ngulo.
 *
 * @author Vicente Caballero Navarro
 */
public class LayoutAddScaleListenerImpl extends LayoutAddRectangleListener {
	private final Image img = PluginServices.getIconTheme()
	.get("rect-select-cursor").getImage();

	/**
	 * Crea un nuevo LayoutAddRectangleListener.
	 *
	 * @param l Layout.
	 */
	public LayoutAddScaleListenerImpl(Layout l) {
		super(l);
	}

	/**
	 * @see org.gvsig.fmap.mapcontrol.tools.Listeners.RectangleListener#rectangle(org.gvsig.fmap.mapcontrol.tools.Events.EnvelopeEvent)
	 */
	public void rectangle(EnvelopeEvent event) throws BehaviorException {
		FFrameScaleBar fframe =(FFrameScaleBar)FrameFactory.createFrameFromName(FFrameScaleBarFactory.registerName);

		fframe.setLayout(layout);
		Rectangle2D r = new Rectangle2D.Double(); //rectOrigin.x+m_PointAnt.x,rectOrigin.y+m_PointAnt.y,m_LastPoint.x-m_PointAnt.x,m_LastPoint.y-m_PointAnt.y);

		r=getRectangle(TOLERANCE);
		fframe.setBoundBox(FLayoutUtilities.toSheetRect(r, layout.getLayoutControl().getAT()));
		FFrameScaleBarDialog fframedialog = new FFrameScaleBarDialog(layout, fframe);
		 if (fframedialog != null) {
	            fframedialog.setRectangle(fframe.getBoundingBox(layout.getLayoutControl().getAT()));
	            PluginServices.getMDIManager().addWindow(fframedialog);
	        }

	    IFFrame newFrame= fframedialog.getFFrame();
		if (newFrame!=null) {
			layout.getLayoutContext().addFFrame(newFrame, true,true);
		}
        PluginServices.getMainFrame().enableControls();
        layout.getLayoutControl().refresh();
	}

	/**
	 * @see org.gvsig.fmap.mapcontrol.tools.Listeners.ToolListener#getCursor()
	 */
	public Image getImageCursor() {
		return img;
	}

	/**
	 * @see org.gvsig.fmap.mapcontrol.tools.Listeners.ToolListener#cancelDrawing()
	 */
	public boolean cancelDrawing() {
		return false;
	}
}
