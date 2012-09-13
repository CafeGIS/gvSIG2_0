/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
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

package com.iver.cit.gvsig.gui.toc;

import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;

import javax.swing.JMenuItem;

import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.mapcontext.layers.FLayer;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.FLyrWCS;
import com.iver.cit.gvsig.project.documents.view.toc.TocMenuEntry;
import com.iver.cit.gvsig.project.documents.view.toc.gui.FPopupMenu;

/**
 * This class implements the TOC meny entry to do a zoom to pixel. Zooming to a
 * pixel causes to change the view's zoom to fit in the coverage's native
 * resolution.
 *
 * Entrada de menú para la activación de la funcionalidad de zoom a un pixel.
 *
 * @author Jaume - jaume.dominguez@iver.es
 *
 * @deprecated not used
 */
public class WCSZoomToPixelTocMenuEntry extends TocMenuEntry {
	public static final int ZOOM_TO_VIEW_CENTER = 0x2;
	private JMenuItem properties;
	FLayer lyr = null;
	public int zoomType = ZOOM_TO_VIEW_CENTER;

	public void initialize(FPopupMenu m) {
		super.initialize(m);

		if (isTocItemBranch()) {
			lyr = getNodeLayer();
    		// Opciones para capas raster
    		if ((lyr instanceof FLyrWCS)) {
    			properties = new JMenuItem(PluginServices.getText(this, "Zoom_pixel"));
    			getMenu().add( properties );
    			properties.setFont(FPopupMenu.theFont);
    			getMenu().setEnabled(true);
    			// LWS getMenu().addSeparator();
    			properties.addActionListener(this);
     		}
		}
	}
	/**
	 * Calculates the zoom that fits the coverage native resolution.
	 *
	 * TODO fix the little loss of accuracy caused by a floating point issue
	 */
	public void actionPerformed(ActionEvent e) {
		//System.out.println("Zoom a un pixel");

    	ViewPort v = getMapContext().getViewPort();

    	double w2 = v.getImageWidth()/2D;
    	double h2 = v.getImageHeight()/2D;
    	double wcOriginCenterX = v.getExtent().getMinX()+((v.getExtent().getMaxX()-v.getExtent().getMinX())/2D);
    	double wcOriginCenterY = v.getExtent().getMinY()+((v.getExtent().getMaxY()-v.getExtent().getMinY())/2D);

    	Point2D maxRes = ((FLyrWCS) lyr).getMaxResolution();
    	//double wcDstMinX = wcOriginCenterX-w2*maxRes;
    	double wcDstMinX = wcOriginCenterX-w2*maxRes.getX();
    	//double wcDstMinY = wcOriginCenterY+h2*maxRes;
    	double wcDstMinY = wcOriginCenterY+h2*maxRes.getY();
    	//double wcDstWidth = w2*maxRes*2D;
    	double wcDstWidth = w2*maxRes.getX()*2D;
       	//double wcDstHeight = h2*maxRes*2D;
    	double wcDstHeight = h2*maxRes.getY()*2D;


    	GeometryManager geoMan = GeometryLocator.getGeometryManager();
		Envelope rAux = null;
		try {
			rAux = geoMan.createEnvelope(wcDstMinX, wcDstMinY, wcDstMinX
					+ wcDstWidth, wcDstMinY + wcDstHeight, SUBTYPES.GEOM2D);

    	} catch (Exception e1) {
			e1.printStackTrace();
		}
		getMapContext().getViewPort().setEnvelope(rAux);

	}
}

