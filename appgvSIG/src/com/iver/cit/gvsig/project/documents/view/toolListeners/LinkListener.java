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
package com.iver.cit.gvsig.project.documents.view.toolListeners;

import java.awt.Component;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.net.URI;

import javax.swing.JOptionPane;

import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.fmap.mapcontrol.tools.BehaviorException;
import org.gvsig.fmap.mapcontrol.tools.Events.PointEvent;
import org.gvsig.fmap.mapcontrol.tools.Listeners.PointListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.gui.panels.LinkPanel;
import com.iver.cit.gvsig.project.documents.view.gui.View;


/**
 * <p>Listener that gets, if exists, the associated link at the feature that's at the position selection of an active, selected
 *  and vector layer of the associated <code>MapControl</code>; and displays
 *  that linked data (image, text, ...) on a dialog.</p>
 *
 * <p>Listens a single click of any mouse's button.</p>
 *
 * @author Vicente Caballero Navarro
 */
public class LinkListener implements PointListener {
	/**
	 * Object used to log messages for this listener.
	 */
	private static final Logger logger = LoggerFactory
            .getLogger(InfoListener.class);

	/**
	 * The image to display when the cursor is active.
	 */
	private final Image img = PluginServices.getIconTheme()
		.get("cursor-hiperlink").getImage();

	/**
	 * The cursor used to work with this tool listener.
	 *
	 * @see #getCursor()
	 */
//	private Cursor cur = Toolkit.getDefaultToolkit().createCustomCursor(img,
//			new Point(16, 16), "");

	/**
	 * Reference to the <code>MapControl</code> object that uses.
	 */
	private MapControl mapCtrl;

	/**
	 * Identifies the link as an image.
	 */
	public static final int TYPELINKIMAGE=0;

	/**
	 * Identifies the link as text.
	 */
	public static final int TYPELINKTEXT=1;

	/**
	 * <p>Creates a new <code>LinkListener</code> object.</p>
	 *
	 * @param mc the <code>MapControl</code> where will be applied the changes
	 */
	public LinkListener(MapControl mc) {
		this.mapCtrl = mc;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Listeners.PointListener#point(com.iver.cit.gvsig.fmap.tools.Events.PointEvent)
	 */
	public void point(PointEvent event) throws BehaviorException {
		Point2D pReal = mapCtrl.getMapContext().getViewPort().toMapPoint(event.getPoint());
		View view=(View)PluginServices.getMDIManager().getActiveWindow();
		FLayer[] sel = mapCtrl.getMapContext().getLayers().getActives();

		URI[] uri=null;

		for (int i = 0; i < sel.length; i++) {
			FLayer laCapa = sel[i];
            if (laCapa.allowLinks())
            {
            	FLyrVect lyrVect = (FLyrVect) laCapa;
            	double tol = mapCtrl.getViewPort().toMapDistance(3);
            	try {
					uri=lyrVect.getLink(pReal,tol);
				} catch (ReadException e) {
					JOptionPane.showMessageDialog((Component)PluginServices.getMainFrame(),"Error" +
    				" Error accediendo a la fuente de datos.");
    		return;
				}
            	if (uri == null){
            		JOptionPane.showMessageDialog((Component)PluginServices.getMainFrame(),"Error" +
            				" no has seleccionado una geometr�a para realizar HyperLink.");
            		return;
            	}
            	for(int j=0; j<uri.length;j++){
            		if(uri[j]!=null){
            			LinkPanel lpanel = new LinkPanel (uri[j], lyrVect.getLinkProperties().getType());
            			PluginServices.getMDIManager().addWindow(lpanel);
            		}
            	}
            }
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Listeners.ToolListener#getImageCursor()
	 */
	public Image getImageCursor() {
		return img;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Listeners.ToolListener#cancelDrawing()
	 */
	public boolean cancelDrawing() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Listeners.PointListener#pointDoubleClick(com.iver.cit.gvsig.fmap.tools.Events.PointEvent)
	 */
	public void pointDoubleClick(PointEvent event) throws BehaviorException {
	}
}
