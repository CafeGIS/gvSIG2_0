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
package org.gvsig.georeferencing.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.gvsig.georeferencing.ui.zoom.IExtensionRequest;
import org.gvsig.georeferencing.ui.zoom.IGraphicLayer;
import org.gvsig.georeferencing.ui.zoom.ViewControl;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;

/**
 * Panel que contiene el control de zoom para el raster a georreferenciar.
 *
 * 22/12/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class ZoomDialog extends BaseZoomView implements IWindow {
	private static final long         serialVersionUID = 1L;

	private int                       w = 320;
	private int                       h = 100;
	private int                       posX = 0;
	private int                       posY = 0;

	/**
	 * Constructor.
	 * Crea la composición de controles de zoom.
	 */
	public ZoomDialog(int posX, int posY, int w, int h) {
		setPosition(posX, posY);
		setWindowsSize(w, h);
		init();
	}

	/**
	 * Asigna la posición de la ventana
	 * @param posX Posición en X
	 * @param posY Posición en Y
	 */
	public void setPosition(int posX, int posY) {
		this.posX = posX;
		this.posY = posY;
	}

	/**
	 * Asigna la posición de la ventana
	 * @param posX Posición en X
	 * @param posY Posición en Y
	 */
	public void setWindowsSize(int w, int h) {
		this.w = w;
		this.h = h;
	}

	/**
	 * Asigna el valor para el flag minxMaxyUL. Este flag informa de que la esquina
	 * superior izquierda corresponde con el valor de mínimo X y máximo Y. En caso
	 * de ser false esta esquina sería de mínimo X y mínimo Y.
	 * @param v
	 */
	public void setMinxMaxyUL(boolean v) {
		getCanvas().setMinxMaxyUL(v);
	}

	/**
	 * Activa o desactiva el mostrado de información
	 * @param showInfo
	 */
	public void setShowInfo(boolean show) {
		zoomPixelControl.getCanvas().setShowInfo(show);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.georeferencing.view.BaseZoomView#addGraphicLayer(org.gvsig.rastertools.georeferencing.ui.zoom.IGraphicLayer)
	 */
	public void addGraphicLayer(IGraphicLayer graphicLayer) {
		getControl().setGraphicLayer(graphicLayer);
	}

	/**
	 * Inicialización de los componentes gráficos
	 */
	private void init() {
		setLayout(new GridBagLayout());
		setPreferredSize(new java.awt.Dimension(w, h));

		GridBagConstraints gb = new GridBagConstraints();
		gb.insets = new java.awt.Insets(0, 0, 0, 0);
		gb.gridy = 0;
		gb.gridx = 0;
		gb.weightx = 1D; //El espacio sobrante se distribuye horizontalmente
		gb.weighty = 1D; //El espacio sobrante se distribuye verticalmente
		gb.fill = GridBagConstraints.BOTH; //El componente se hace tan ancho como espacio disponible tiene
		gb.anchor = GridBagConstraints.NORTH; //Alineamos las cajas arriba
		add(getControl(), gb);
	}

	/**
	 * Registra un objeto IExtensionRequest para que no se aplique un escalado sobre
	 * el buffer pasado por parámetro. Alternativamente a la aplicación de este escalado
	 * se ejecutará el método request del interfaz para que el cliente pueda pasar un
	 * nuevo buffer con escala 1:1 y con la extensión correspondiente al zoom.
	 * @param er
	 */
	public void setExtensionRequest(IExtensionRequest er) {
		zoomPixelControl.setExtensionRequest(er);
	}

	/**
	 * Obtiene el panel de control de zoom de coordenadas pixel
	 * @return
	 */
	public ViewControl getControl() {
		if(zoomPixelControl == null) {
			zoomPixelControl = new ViewControl(ViewControl.LEFT_CONTROL);
			zoomPixelControl.hideButton(ViewControl.PREV_ZOOM);
			zoomPixelControl.hideButton(ViewControl.FULL_VIEW);
			zoomPixelControl.hideButton(ViewControl.SELECT_ZOOM_AREA);
			zoomPixelControl.hideButton(ViewControl.ZOOM_INCREASE);
			zoomPixelControl.hideButton(ViewControl.ZOOM_DECREASE);
			zoomPixelControl.hideButton(ViewControl.PAN);
		}
		return zoomPixelControl;
	}

	/**
	 * Asigna los parámetros de dibujado para el raster
	 * @param img Buffer con un área de datos
	 * @param ext Envelope del área de datos dada
	 * @param pixelSize Tamaño de pixel
	 * @param center Punto del área de datos donde se quiere centrar el dibujado del buffer
	 */
	public void setDrawParams(BufferedImage img, Rectangle2D ext, double pixelSize, Point2D center) {
		getControl().setDrawParams(img, ext, pixelSize, center) ;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.andami.ui.mdiManager.IWindow#getWindowInfo()
	 */
	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo = new WindowInfo(WindowInfo.MODELESSDIALOG);
		//if (getClippingPanel().getFLayer() != null)
			//m_viewinfo.setAdditionalInfo(getClippingPanel().getFLayer().getName());
		m_viewinfo.setTitle(PluginServices.getText(this, "zooms_control"));
		m_viewinfo.setX(posX);
		m_viewinfo.setY(posY);
		m_viewinfo.setHeight(h);
		m_viewinfo.setWidth(w);
		return m_viewinfo;
	}
	public Object getWindowProfile(){
		return WindowInfo.TOOL_PROFILE;
	}
}
