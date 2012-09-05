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
package org.gvsig.fmap.mapcontrol.tools;

import java.awt.Image;

import javax.swing.ImageIcon;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.FeatureSelection;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.fmap.mapcontrol.tools.Events.EnvelopeEvent;
import org.gvsig.fmap.mapcontrol.tools.Listeners.RectangleListener;

/**
 * <p>Listener that selects all features of the active and vector layers which intersect with the defined
 *  rectangle area in the associated {@link MapControl MapControl} object.</p>
 *
 * @author Vicente Caballero Navarro
 */
public class RectangleSelectionListener implements RectangleListener {
	/**
	 * The image to display when the cursor is active.
	 */
	private final Image img = new ImageIcon(MapControl.class.getResource(
				"images/RectSelectCursor.gif")).getImage();

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
  	 * <p>Creates a new <code>RectangleSelectionListener</code> object.</p>
	 *
	 * @param mc the <code>MapControl</code> where is defined the rectangle
	 */
	public RectangleSelectionListener(MapControl mc) {
		this.mapCtrl = mc;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Listeners.RectangleListener#rectangle(com.iver.cit.gvsig.fmap.tools.Events.RectangleEvent)
	 */
	public void rectangle(EnvelopeEvent event) throws BehaviorException {
		try {
			// mapCtrl.getMapContext().selectByRect(event.getWorldCoordRect());
            Envelope rect = event.getWorldCoordRect();
            FLayer[] actives = mapCtrl.getMapContext()
                .getLayers().getActives();
            for (int i=0; i < actives.length; i++){
                if (actives[i] instanceof FLyrVect) {
                    FLyrVect lyrVect = (FLyrVect) actives[i];
                    FeatureSelection oldSelection = (FeatureSelection) lyrVect.getFeatureStore().getSelection();
                    FeatureSet newSelection = lyrVect.queryByEnvelope(rect, lyrVect.getFeatureStore().getDefaultFeatureType());
                    if (event.getEvent().isControlDown()){
                    	oldSelection.select(newSelection);
                    } else {
						lyrVect.getDataStore().setSelection(newSelection);
                    }
                }
            }

		} catch (DataException e) {
			throw new BehaviorException("No se pudo hacer la selección", e);
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
}
