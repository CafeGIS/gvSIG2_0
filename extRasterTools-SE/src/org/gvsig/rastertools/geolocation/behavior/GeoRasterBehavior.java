/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2006 IVER T.I. and Generalitat Valenciana.
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
package org.gvsig.rastertools.geolocation.behavior;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import org.gvsig.fmap.mapcontext.layers.FLayers;
import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.fmap.mapcontrol.tools.BehaviorException;
import org.gvsig.fmap.mapcontrol.tools.Behavior.Behavior;
import org.gvsig.fmap.mapcontrol.tools.Listeners.PanListener;
import org.gvsig.fmap.mapcontrol.tools.Listeners.ToolListener;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.project.documents.view.gui.IView;


/**
 * Behavior que espera un listener de tipo GeoMoveListener.
 * Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class GeoRasterBehavior extends Behavior {
	private Image                          	curImage = null;
	private IView                           theView = null;
	private TransformationBehavior[]		behavior = new TransformationBehavior[4];
	private FLyrRasterSE                    lyr = null;

	/**
	 * Crea un nuevo GeoRasterBehavior.
	 *
	 * @param zili listener.
	 * @param windowIO
	 */
	public GeoRasterBehavior(PanListener zili, ITransformIO windowIO) {
		curImage = zili.getImageCursor();
		behavior[0] = new TranslateBehavior(this, curImage, windowIO);
		behavior[1] = new ScaleBehavior(this, curImage, windowIO);
		behavior[2] = new RotationBehavior(this, curImage, windowIO);
		behavior[3] = new ShearBehavior(this, curImage, windowIO);
	}

	/**
	 * Crea un nuevo RectangleBehavior.
	 *
	 * @param zili listener.
	 */
	public GeoRasterBehavior(PanListener zili, ITransformIO windowIO, FLyrRasterSE lyr) {
		this(zili, windowIO);
		this.lyr = lyr;
	}

	/**
	 * Asigna la capa raster
	 * @param lyr
	 */
	public void setLayer(FLyrRasterSE lyr) {
		this.lyr = lyr;
	}

	/**
	 * Asigna el objeto externo para variar la transformación
	 * @param gld
	 */
	public void setITransformIO(ITransformIO gld) {
		for (int i = 0; i < behavior.length; i++)
			behavior[i].setITransformIO(gld);
	}

	/**
	 * Función que carga la capa si todavía no lo está.
	 */
	public FLyrRasterSE getLayer() {
		//Cuando cargamos desde el menú contextual la capa es asignada de forma fija
		if(lyr != null)
			return lyr;

		//Cuando cargamos desde la barra hay que buscar la capa activa.

		IWindow w = PluginServices.getMDIManager().getActiveWindow();
		if(w instanceof IView) {
			theView = (IView)w;
			FLayers lyrs = theView.getMapControl().getMapContext().getLayers();
			for (int i = 0; i < lyrs.getActives().length; i++)
				if(lyrs.getActives()[i] instanceof FLyrRasterSE)
					return (FLyrRasterSE)lyrs.getActives()[i];
		}
		return null;
	}

	/**
	 * Coloca el cursor del ratón con el icono adecuado cuando entra dentro de la
	 * imagen.
	 */
	public void mouseMoved(MouseEvent e) throws BehaviorException {
		behavior[0].setActiveTool(false);
		behavior[1].setActiveTool(false);
		behavior[2].setActiveTool(false);
		behavior[3].setActiveTool(false);
		if(!behavior[1].mouseMoved(e))
			if(!behavior[0].mouseMoved(e))
				if(!behavior[2].mouseMoved(e))
					if(!behavior[3].mouseMoved(e));
	}

	/**
	 * Si no está activo el cursor por defecto capturamos el punto
	 * seleccionado en coordenadas del mundo real.
	 */
	public void mousePressed(MouseEvent e) throws BehaviorException {
		if(behavior[1].isActiveTool())
			behavior[1].mousePressed(e);
		else if(behavior[0].isActiveTool())
			behavior[0].mousePressed(e);
		else if(behavior[2].isActiveTool())
			behavior[2].mousePressed(e);
		else if(behavior[3].isActiveTool())
			behavior[3].mousePressed(e);
	}

	/**
	 *  Cuando soltamos el botón del ratón desplazamos la imagen a la posición
	 * de destino calculando el extent nuevamente.
	 */
	public void mouseReleased(MouseEvent e) throws BehaviorException {
		if(behavior[1].isActiveTool())
			behavior[1].mouseReleased(e);
		else if(behavior[0].isActiveTool())
			behavior[0].mouseReleased(e);
		else if(behavior[2].isActiveTool())
			behavior[2].mouseReleased(e);
		else if(behavior[3].isActiveTool())
			behavior[3].mouseReleased(e);
	}

	/**
	 * Función de pintado del canvas.Pintamos un marco a la imagen para saber
	 * donde la movemos.
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(behavior[1].isActiveTool())
			behavior[1].paintComponent(g);
		else if(behavior[0].isActiveTool())
			behavior[0].paintComponent(g);
		else if(behavior[2].isActiveTool())
			behavior[2].paintComponent(g);
		else if(behavior[3].isActiveTool())
			behavior[3].paintComponent(g);
	}

	/**
	 * Esta función repinta el canvas si se está arrasrtando la imagen para
	 * poder ver el marco de arrastre.
	 */
	public void mouseDragged(MouseEvent e) throws BehaviorException {
		if(behavior[1].isActiveTool())
			behavior[1].mouseDragged(e);
		else if(behavior[0].isActiveTool())
			behavior[0].mouseDragged(e);
		else if(behavior[2].isActiveTool())
			behavior[2].mouseDragged(e);
		else if(behavior[3].isActiveTool())
			behavior[3].mouseDragged(e);
	}

	/* (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Behavior.Behavior#getListener()
	 */
	public ToolListener getListener() {
		return null;
	}
	/* (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Behavior.Behavior#getMapControl()
	 */
	public MapControl getMapControl() {
		return super.getMapControl();
	}
	/* (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Behavior.Behavior#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) throws BehaviorException {
		super.mouseClicked(e);
	}

	/* (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Behavior.Behavior#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent e) throws BehaviorException {
		super.mouseEntered(e);
	}
	/* (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Behavior.Behavior#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent e) throws BehaviorException {
		super.mouseExited(e);
	}

	/* (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Behavior.Behavior#mouseWheelMoved(java.awt.event.MouseWheelEvent)
	 */
	public void mouseWheelMoved(MouseWheelEvent e) throws BehaviorException {
		super.mouseWheelMoved(e);
	}

	/* (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Behavior.Behavior#setMapControl(com.iver.cit.gvsig.fmap.MapControl)
	 */
	public void setMapControl(MapControl mc) {
		super.setMapControl(mc);
	}

	public Image getImageCursor() {
		return this.curImage;
	}

}
