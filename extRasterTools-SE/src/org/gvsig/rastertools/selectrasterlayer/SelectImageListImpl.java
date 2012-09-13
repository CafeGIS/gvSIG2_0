/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
*
* Copyright (C) 2008 IVER T.I. and Generalitat Valenciana.
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
package org.gvsig.rastertools.selectrasterlayer;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.geom.Point2D;

import javax.swing.ImageIcon;

import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.fmap.mapcontrol.tools.Events.PointEvent;
import org.gvsig.fmap.mapcontrol.tools.Listeners.PointListener;


/**
 * <p>Listener to select the upper layer with raster data, that has information in the associated <code>MapControl</code> object, down
 *  the position selected by the mouse.</p>
 *
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class SelectImageListImpl implements PointListener {
	/**
	 * The image to display when the cursor is active.
	 */
	private final Image isaveraster = new ImageIcon(MapControl.class.getResource(
				"images/PointSelectCursor.gif")).getImage();

	/**
	 * The cursor used to work with this tool listener.
	 * 
	 * @see #getCursor()
	 */
	private Cursor cur = Toolkit.getDefaultToolkit().createCustomCursor(isaveraster,
			new Point(16, 16), "");

	/**
	 * Reference to the <code>MapControl</code> object that uses.
	 */
	protected MapControl mapCtrl;

	/**
	 * World equivalent coordinates of the point 2D 
	 */
	protected Point2D wcPoint = null;

	/**
	 * <p>Creates a new <code>SelectImageListenerImpl</code> object.</p>
	 * 
	 * @param mapCtrl the <code>MapControl</code> where are stored the layers
	 */
	public SelectImageListImpl(MapControl mapCtrl) {
		this.mapCtrl = mapCtrl;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Listeners.PointListener#point(com.iver.cit.gvsig.fmap.tools.Events.PointEvent)
	 */
	public void point(PointEvent event) {

	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Listeners.ToolListener#getCursor()
	 */
	public Cursor getCursor() {
		return cur;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Listeners.ToolListener#cancelDrawing()
	 */
	public boolean cancelDrawing() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Listeners.PointListener#pointDoubleClick(com.iver.cit.gvsig.fmap.tools.Events.PointEvent)
	 */
	public void pointDoubleClick(PointEvent event) {
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.mapcontrol.tools.Listeners.ToolListener#getImageCursor()
	 */
	public Image getImageCursor() {
		return isaveraster;
	}
}
