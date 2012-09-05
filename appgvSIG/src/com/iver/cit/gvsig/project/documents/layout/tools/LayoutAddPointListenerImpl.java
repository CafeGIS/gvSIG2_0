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
import java.awt.geom.Rectangle2D;

import org.gvsig.fmap.mapcontrol.tools.Events.PointEvent;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.documents.layout.fframes.FFrameGraphics;
import com.iver.cit.gvsig.project.documents.layout.fframes.FFrameGraphicsFactory;
import com.iver.cit.gvsig.project.documents.layout.fframes.FrameFactory;
import com.iver.cit.gvsig.project.documents.layout.geometryadapters.GeometryAdapter;
import com.iver.cit.gvsig.project.documents.layout.geometryadapters.PointAdapter;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;
import com.iver.cit.gvsig.project.documents.layout.tools.listener.ILayoutGraphicListener;
import com.iver.cit.gvsig.project.documents.layout.tools.listener.LayoutPointListener;


/**
 * Implementaci�n de la interfaz LayoutPointListener como herramienta para realizar
 * un zoom menos.
 *
 * @author Vicente Caballero Navarro
 */
public class LayoutAddPointListenerImpl implements LayoutPointListener, ILayoutGraphicListener {
	public static final Image iPoint = PluginServices.getIconTheme()
		.get("point-cursor").getImage();

	private Layout layout;

	/**
	 * Crea un nuevo LayoutZoomOutListenerImpl.
	 *
	 * @param mapControl MapControl.
	 */
	public LayoutAddPointListenerImpl(Layout l) {
		this.layout = l;
	}

	/**
	 * @see org.gvsig.fmap.mapcontrol.tools.Listeners.PointListener#point(com.iver.cit.gvsig.fmap.tools.PointEvent)
	 */
	public void point(PointEvent event) {
		if (event.getEvent().getButton() == MouseEvent.BUTTON1) {
			endGraphic();
		}
	}

	/**
	 * @see org.gvsig.fmap.mapcontrol.tools.Listeners.ToolListener#getCursor()
	 */
	public Image getImageCursor() {
		return iPoint;
	}

	/**
	 * @see org.gvsig.fmap.mapcontrol.tools.Listeners.ToolListener#cancelDrawing()
	 */
	public boolean cancelDrawing() {
	    System.out.println("cancelDrawing del ZoomOutListenerImpl");
		return true;
	}

	public void pointDoubleClick(PointEvent event) {
		// TODO Auto-generated method stub

	}

	public void endGraphic() {
		layout.getLayoutControl().addGeometryAdapterPoint();
    	layout.getLayoutControl().getGeometryAdapter().end();
        PluginServices.getMainFrame().enableControls();
        FFrameGraphics fframe =(FFrameGraphics)FrameFactory.createFrameFromName(FFrameGraphicsFactory.registerName);

		fframe.setLayout(layout);
        fframe.setGeometryAdapter(layout.getLayoutControl().getGeometryAdapter());
		fframe.update(FFrameGraphics.POINT, layout.getLayoutControl().getAT());
		Rectangle2D r=layout.getLayoutControl().getGeometryAdapter().getBounds2D();
		double d=0.5;//FLayoutUtilities.toSheetDistance(50,layout.getAT());
		r=new Rectangle2D.Double(r.getX()-(d/2),r.getY()-d,d*2,d*2);
		fframe.setBoundBox(r);
		layout.getLayoutContext().addFFrame(fframe, true,true);
		layout.getLayoutControl().setGeometryAdapter(new PointAdapter());
        layout.getLayoutControl().refresh();
	}

	public GeometryAdapter createGeometryAdapter() {
		return new PointAdapter();
	}
}
