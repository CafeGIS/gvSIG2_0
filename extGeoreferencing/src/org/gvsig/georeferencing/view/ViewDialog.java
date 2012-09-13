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

import java.awt.BorderLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.gvsig.georeferencing.ui.zoom.IExtensionRequest;
import org.gvsig.georeferencing.ui.zoom.IGraphicLayer;
import org.gvsig.georeferencing.ui.zoom.ViewControl;
import org.gvsig.georeferencing.ui.zoom.ViewListener;
import org.gvsig.georeferencing.ui.zoom.layers.ZoomCursorGraphicLayer;
import org.gvsig.georeferencing.ui.zoom.tools.ToolListener;
import org.gvsig.raster.util.RasterToolsUtil;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;

/**
 * Panel que contiene el panel con la vista a georreferenciar
 *
 * 22/12/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class ViewDialog extends BaseZoomView implements IWindow, ComponentListener {
	private static final long         serialVersionUID = 1L;
	private ZoomCursorGraphicLayer    zoomCursorGraphic = null;
	private ToolListener              zoomCursorGraphicListener = null;
	private int                       w = 640;
	private int                       h = 100;
	private int                       posX = 0;
	private int                       posY = 0;

	/**
	 * Constructor.
	 * Crea la composición de controles de zoom.
	 * @param posX Posición en X
	 * @param posY Posición en Y
	 * @param w Tamaño en pixeles de ancho
	 * @param h Tamaño en pixeles de alto
	 */
	public ViewDialog(int posX, int posY, int w, int h, ToolListener zoomCursorGraphicListener) {
		setPosition(posX, posY);
		setWindowsSize(w, h);
		this.zoomCursorGraphicListener = zoomCursorGraphicListener;
		init();
	}

	/**
	 * Asigna el listener de eventos de la vista
	 * @param listener
	 */
	public void setViewListener(ViewListener listener) {
		getControl().setViewListener(listener);
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
	 * Asigna el tamaño de la ventana
	 * @param w Tamaño en pixeles de ancho
	 * @param h Tamaño en pixeles de alto
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
	 * Asigna un nuevo centro de visualización. Vuelve a realizar la petición
	 * @param center
	 */
	public void setCenter(Point2D center) {
		//TODO: Calcular las coordenadas del cursor gráfico
		getControl().setCenter(center);
	}

	/**
	 * Inicialización de los componentes gráficos
	 */
	private void init() {
		setLayout(new BorderLayout());
		add(getControl(), BorderLayout.CENTER);
		getZoomCursorGraphicLayer();
	}

	/**
	 * Asigna los parámetros de dibujado para el raster
	 * @param img Buffer con un área de datos
	 * @param ext Rectangle2D del área de datos dada
	 * @param pixelSize Tamaño de pixel
	 * @param center Punto del área de datos donde se quiere centrar el dibujado del buffer
	 */
	public void setDrawParams(BufferedImage img, Rectangle2D ext, double pixelSize, Point2D center) {
		getControl().setDrawParams(img, ext, pixelSize, center) ;
	}

	/**
	 * Obtiene el panel de control de zoom de coordenadas pixel
	 * @return
	 */
	public ViewControl getControl() {
		if(zoomPixelControl == null) {
			zoomPixelControl = new ViewControl(ViewControl.RIGHT_CONTROL);
			zoomPixelControl.addComponentListener(this);
		}
		return zoomPixelControl;
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

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.georeferencing.view.BaseZoomView#addGraphicLayer(org.gvsig.rastertools.georeferencing.ui.zoom.IGraphicLayer)
	 */
	public void addGraphicLayer(IGraphicLayer graphicLayer) {
		getControl().setGraphicLayer(graphicLayer);
	}

	/**
	 * Obtiene el la capa gráfica con la ventana de zoom
	 * @return
	 */
	public ZoomCursorGraphicLayer getZoomCursorGraphicLayer() {
		if(zoomCursorGraphic == null) {
			zoomCursorGraphic = new ZoomCursorGraphicLayer(w / 2, h / 2, 12, 6, zoomCursorGraphicListener);
			zoomCursorGraphic.setCanvas(getControl().getCanvas());
			getControl().setGraphicLayer(zoomCursorGraphic);
		}
		return zoomCursorGraphic;
	}

	/**
	 * Asigna la posición del cursor en el canvas
	 * @param x Posición en X
	 * @param y Posición en Y
	 */
	public void setCursorPosition(int x, int y) {
		getZoomCursorGraphicLayer().setCursorPosition(x, y);
	}

	/**
	 * Asigna el tamaño del cursor en pixeles del canvas
	 * @param w Ancho
	 * @param h Alto
	 */
	public void setCursorSize(int w, int h) {
		getZoomCursorGraphicLayer().setCursorSize(w, h);
	}

	/**
	 * Activa o desactiva el mostrado de información
	 * @param showInfo
	 */
	public void setShowInfo(boolean show) {
		zoomPixelControl.getCanvas().setShowInfo(show);
	}

	/**
	 * Obtiene las coordenadas de la ventana de zoom. Las coordenadas son devueltas
	 * en referencia a las coordenadas del mundo.
	 * @return
	 */
	public Rectangle2D getCursorWorldCoordinates() {
		Rectangle2D r = getZoomCursorGraphicLayer().getCursorViewCoordinates();
		Point2D p1 = zoomPixelControl.getCanvas().viewCoordsToWorld(new Point2D.Double(r.getX(), r.getY()));
		Point2D p2 = zoomPixelControl.getCanvas().viewCoordsToWorld(new Point2D.Double(r.getX() + r.getWidth(), r.getY() + r.getHeight()));
		return new Rectangle2D.Double(	Math.min(p1.getX(), p2.getX()), 
										Math.max(p1.getY(), p2.getY()), 
										Math.abs(p2.getX() - p1.getX()), 
										Math.abs(p2.getY() - p1.getY()));
	}

	/**
	 * Obtiene las coordenadas de la ventana de zoom. Las coordenadas son devueltas
	 * en referencia a las coordenadas del mundo. Esta llamada ajusta el tamaño de la ventana a la
	 * proporción pasada por parámetro. Es decir, si pasamos una proporción en la que el ancho es 3 veces
	 * mayor que el alto se ajustará el zoom a esta proporción ya que presupone que la ventana de
	 * destino del zoom va a tener esas proporciones.
	 * @return
	 */
	public Rectangle2D getCursorAdjustedWorldCoordinates(int wWindow, int hWindow) {
		Rectangle2D r = getZoomCursorGraphicLayer().getCursorViewCoordinates();
		double x = r.getX(), y = r.getY(), w = r.getWidth(), h = r.getHeight();
		
		try {
			if(wWindow < hWindow) { //Si la de destino es más alta que ancha
				if((r.getWidth() / r.getHeight()) >= (wWindow / hWindow)) {
					h = (hWindow * r.getWidth()) / wWindow;
					y = r.getCenterY() - (h / 2);
				} else {
					w = (wWindow * r.getHeight()) / hWindow;
					x = r.getCenterX() - (w / 2);
				}
			} else { //Si la de destino es más ancha que alta
				if((r.getWidth() / r.getHeight()) <= (wWindow / hWindow)) {
					w = (wWindow * r.getHeight()) / hWindow;
					x = r.getCenterX() - (w / 2);
				} else {
					h = (hWindow * r.getWidth()) / wWindow;
					y = r.getCenterY() - (h / 2);
				}
			}
		}catch(ArithmeticException ex) {
			RasterToolsUtil.debug("Redimensión de las ventanas. La altura de estas es menor que cero", this, ex);
			return r;
		}
		if(zoomPixelControl.getCanvas().getMinxMaxyUL())
			r = new Rectangle2D.Double(x, y + h, w, h);
		else
			r = new Rectangle2D.Double(x, y - h, w, h);
		Point2D p1 = zoomPixelControl.getCanvas().viewCoordsToWorld(new Point2D.Double(r.getX(), r.getY()));
		Point2D p2 = zoomPixelControl.getCanvas().viewCoordsToWorld(new Point2D.Double(r.getX() + r.getWidth(), r.getY() + r.getHeight()));
		return new Rectangle2D.Double(	Math.min(p1.getX(), p2.getX()), 
										Math.max(p1.getY(), p2.getY()), 
										Math.abs(p2.getX() - p1.getX()), 
										Math.abs(p2.getY() - p1.getY()));
	}

	/**
	 * Obtiene el extent del canvas en coordenadas del mundo real
	 * @return Envelope
	 */
	public Rectangle2D getViewEnvelope() {
		return zoomPixelControl.getCanvas().getEnvelope();
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.andami.ui.mdiManager.IWindow#getWindowInfo()
	 */
	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo = new WindowInfo(WindowInfo.MODELESSDIALOG | WindowInfo.RESIZABLE | WindowInfo.MAXIMIZABLE);
		//if (getClippingPanel().getFLayer() != null)
		//m_viewinfo.setAdditionalInfo(getClippingPanel().getFLayer().getName());
		m_viewinfo.setTitle(PluginServices.getText(this, "view_panel"));
		m_viewinfo.setX(posX);
		m_viewinfo.setY(posY);
		m_viewinfo.setHeight(h);
		m_viewinfo.setWidth(w);
		return m_viewinfo;
	}

	public Object getWindowProfile(){
		return WindowInfo.TOOL_PROFILE;
	}

	public void componentHidden(ComponentEvent e) {
	}

	public void componentMoved(ComponentEvent e) {
	}

	public void componentResized(ComponentEvent e) {
		zoomPixelControl.reload();
	}

	public void componentShown(ComponentEvent e) {
	}
}

