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
package org.gvsig.georeferencing.ui.zoom;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.georeferencing.ui.zoom.tools.BaseViewTool;
import org.gvsig.raster.util.MathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Zona de dibujado del raster
 * 21/12/2007
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class CanvasZone extends JPanel implements MouseListener, MouseMotionListener {
	private static final GeometryManager geomManager  	= GeometryLocator.getGeometryManager();
	private static final Logger    logger 				= LoggerFactory.getLogger(CanvasZone.class);
	private static final long      serialVersionUID     = 1308683333757367640L;

	private BufferedImage          image                = null;
	private double                 scale                = 1;
	private Rectangle2D            extent               = null;
	private double                 pixelSize            = 1;
	private Point2D                center               = null;
	private ArrayList              graphicLayers        = new ArrayList();
	/**
	 * Último extent aplicado. Si no ha variado el siguiente a aplicar no hace falta que releamos de nuevo
	 */
	private Rectangle2D            lastExtent           = null;
	//Ultimo Image con la capa dibujada y con la transformación que tiene en cuenta los desplazamientos dentro de un pixel
	//Este buffer no varia hasta que se hace la siguiente petición de setDrawParams
	private BufferedImage          lastImage            = null;
	//lastImage sobre el que se pintan las capas gráficas. Este buffer varia en cada repaint. El orden en el que se carga
	//es: se vuelva el lastImage, se pintan las capas gráficas, se pintan las tools.
	private BufferedImage          lastImageWithLayers  = null;

    private boolean                clear                = false;
    private BaseViewTool           selectedTool         = null;

    private Point2D                realCoord            = new Point2D.Double(0, 0);
    //private Point2D                viewCoord = new Point2D.Double(0, 0);
    private boolean                showInfo             = false;
    /**
     * Informa de que la esquina superior izquierda corresponde con el valor de mínimo X y
     * máximo Y. En caso de ser false esta esquina sería de mínimo X y mínimo Y.
     */
    private boolean                minxMaxyUL           = true;

    private Color                  backgroundColor      = Color.BLACK;
    private Color                  textColor            = Color.RED;

    /**
     * Normalmente no se hace una petición al dibujado del raster si el extent no ha variado. Si esta variable
     * está a true fuerza que haya una petición de redibujado aunque el extent del raster no haya cambiado.
     * Esto solo se hace para una petición. La siguiente vuelve a estar a false.
     */
    private boolean                forceRequest         = false;

    private ViewListener           viewListener         = null;
    /**
     * Creamos una sola instancia de Event para no tener que malgastar recursos
     * creando una en cada dibujado
     */
    private ViewEvent              viewEvent            = null;

    /**
	 * Asigna los parámetros de dibujado
	 * @param img Buffer con un área de datos
	 * @param ext Rectangle2D del área de datos dada
	 * @param pixelSize Tamaño de pixel
	 * @param center Punto del área de datos donde se quiere centrar el dibujado del buffer
	 */
	public void setDrawParams(BufferedImage img, Rectangle2D ext, double pixelSize, Point2D center) {
		this.image = img;
		this.extent = ext;
		this.pixelSize = pixelSize;
		this.center = center;
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		repaint();
	}

	/**
	 * Asigna el listener de eventos de la vista
	 * @param listener
	 */
	public void setViewListener(ViewListener listener) {
		this.viewListener = listener;
		viewEvent = new ViewEvent(this);
	}

	/**
	 * Asigna un nuevo centro de visualización
	 * @param center
	 */
	public void setCenter(Point2D center) {
		this.center = center;
		repaint();
	}

	/**
	 * Conversión de un punto en coordenadas del canvas a reales
	 * @param p
	 * @return
	 */
	public Point2D viewCoordsToWorld(Point2D p) {
		int w = getVisibleRect().width;
		int h = getVisibleRect().height;
		double cx = extent.getMinX() + ((p.getX() * extent.getWidth()) / w);
		double cy = 0;
		if(minxMaxyUL) //Cuando las Y decrecen de arriba a abajo
			cy = extent.getMaxY() - (p.getY() * extent.getHeight()) / h;
		else //Cuando las Y crecen de arriba a abajo
			cy = extent.getMinY() + (p.getY() * extent.getHeight()) / h;
		return new Point2D.Double(cx, cy);
	}

	/**
	 * Conversión de un punto en coordenadas del canvas a reales
	 * @param p
	 * @return
	 */
	public Point2D viewCoordsFromWorld(Point2D p) {
		int w = getVisibleRect().width;
		int h = getVisibleRect().height;
		double cx = ((p.getX() - extent.getMinX()) * w) / extent.getWidth();
		double cy = 0;
		if(minxMaxyUL) //Cuando las Y decrecen de arriba a abajo
			cy = ((extent.getMaxY() - p.getY()) * h) / extent.getHeight();
		else //Cuando las Y crecen de arriba a abajo
			cy = ((p.getY() - extent.getMinY()) * h) / extent.getHeight();
		return new Point2D.Double(cx, cy);
	}

	/**
	 * Obtiene el extent del canvas en coordenadas del mundo real
	 * @return Rectangle2D
	 */
	public Rectangle2D getEnvelope() {
		if(lastExtent == null) {
			return extent;
		}
		return lastExtent;
	}

	/**
	 * Asigna un nuevo centro de visualización en coordenadas del
	 * componente.
	 * @param center
	 */
	public void setViewCenter(Point2D c) {
		int w = getVisibleRect().width;
		int h = getVisibleRect().height;
		
		double cx = (c.getX() * lastExtent.getWidth()) / w;
		double cy = (c.getY() * lastExtent.getHeight()) / h;
		setPixelCenter(new Point2D.Double(cx, cy));
	}

	/**
	 * Asigna un nuevo centro de visualización en coordenadas pixel
	 * del área de dibujado (canvas). El nuevo centro será calculado en coordenadas
	 * del mapa.
	 * @param center
	 */
	public void setPixelCenter(Point2D c) {
		int w = getVisibleRect().width;
		int h = getVisibleRect().height;

		//Calculamos el extent del canvas
		Rectangle2D ext = getCanvasEnvelope(w, h, scale);

		//Calculamos el nuevo centro en coordenadas reales
		double wWC = (c.getX() / scale) * pixelSize;
		double hWC = (c.getY() / scale) * pixelSize;
		this.center = new Point2D.Double(ext.getMinX() + wWC,
										 ext.getMinY() - hWC);
		repaint();
	}

	/**
	 * Asigna un nuevo centro de visualización en coordenadas pixel. Esta llamada tiene
	 * en cuenta solo píxeles completos. No centra sobre porciones de pixel cuando el zoom es
	 * mayor de 1:1. El nuevo centro es en coordenadas del mapa pero siempre centrará
	 * en la esquina inferior izquierda del pixel.
	 * @param center
	 */
	public void setPixelCenter(int x, int y) {
		int w = getVisibleRect().width;
		int h = getVisibleRect().height;
		
		//Calculamos el extent del canvas 
		Rectangle2D ext = getCanvasEnvelope(w, h, scale);
		
		//Calculamos el nuevo centro en coordenadas reales
		double wWC = (x / scale) * pixelSize;
		double hWC = (y / scale) * pixelSize;
		Point2D center = new Point2D.Double(ext.getMinX() + wWC,
				 						 	ext.getMinY() - hWC);
		
		//Calculamos la coordena pixel a la que pertenece esa coordenada real
		int pxX = (int)((center.getX() * (w / scale)) / ext.getWidth());
		int pxY = (int)((center.getY() * (h / scale)) / ext.getHeight());
		
		//Después de haber convertido a pixel y redondeado la coordenada a entero volvemos a convertirla en real
		double wcX = (pxX * ext.getWidth()) / (w / scale);
		double wcY = (pxY * ext.getHeight()) / (h / scale);

		this.center = new Point2D.Double(wcX, wcY);
		repaint();
	}

	/**
	 * Asigna una capa gráfica
	 * @param layer IGraphicLayer
	 */
	public void setGraphicLayer(IGraphicLayer layer) {
		graphicLayers.add(layer);
	}

	/**
	 * Asigna la escala para el nuevo zoom
	 * @param scale
	 */
	public void setZoom(double scale) {
		this.scale = scale;
		repaint();
	}

	/**
	 * Obtiene la escala aplicada en el dibujado
	 * @return double
	 */
	public double getZoom() {
		return scale;
	}

	/**
	 * Obtiene el extent actual asignado al canvas
	 * @return Rectangle2D
	 */
	public Rectangle2D getCanvasEnvelope() {
		return lastExtent;
	}

	/**
	 * Asigna el extent del canvas
	 * @param r
	 */
	public void setCanvasEnvelope(Rectangle2D r) {
		this.lastExtent = r;
	}

	/**
	 * Obtiene el tamaño de pixel
	 * @return double
	 */
	public double getPixelSize() {
		return pixelSize;
	}

	/**
	 * Obtiene el centro del canvas
	 * @return Point2D
	 */
	public Point2D getCenter() {
		return center;
	}

	/**
	 * Obtiene el buffer de la vista activa y lo dibuja sobre el panel
	 * con los datos de escala y desplazamiento seleccionados.
	 */
	protected void paintComponent(Graphics g) {
		if(image == null) {
			return;
		}

		if(viewListener != null) {
			viewListener.startDraw(viewEvent);
		}

		int w = getVisibleRect().width;
		int h = getVisibleRect().height;
		Rectangle2D ext = getCanvasEnvelope(w, h, scale);

		if(lastImage == null || !equal(lastExtent, ext)) {
			lastImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
			lastImageWithLayers = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		}

		if(clear) {
			g.setColor(backgroundColor);
			g.fillRect(0, 0, w, h);
			return;
		}

		//Dibujamos el buffer sobre el grafics
		Graphics graphicsDB = draw(ext, w, h);

		//Dibujamos todas las capas registradas
		for (int i = 0; i < graphicLayers.size(); i++) {
			((IGraphicLayer)graphicLayers.get(i)).draw((Graphics2D)graphicsDB, ext, w, h);
		}

		lastExtent = ext;

		if(selectedTool != null) {
			selectedTool.draw(graphicsDB);
		}

		if(showInfo) {
			showInfo(graphicsDB);
		}

		g.drawImage(lastImageWithLayers, 0, 0, this);
		graphicsDB.dispose();

		if(viewListener != null) {
			viewListener.endDraw(viewEvent);
		}
	}

	/**
	 * Muestra información sobre la vista
	 * @param g
	 */
	private void showInfo(Graphics g) {
		g.setColor(textColor);
		g.drawString("X: " + MathUtils.clipDecimals(realCoord.getX(), 3), 12, 18);
		g.drawString("Y: " + MathUtils.clipDecimals(realCoord.getY(), 3), 12, 32);
		//g.drawString("X': " + MathUtils.tailDecimals(viewCoord.getX(), 3), 12, 46);
		//g.drawString("Y': " + MathUtils.tailDecimals(viewCoord.getY(), 3), 12, 60);
	}

	/**
	 * Compara dos extents y devuelve true si son iguales y false si son distintos
	 * @param e1 Extent 1
	 * @param e2 Extent 2
	 * @return true si son iguales y false si son diferentes
	 */
	private boolean equal(Rectangle2D e1, Rectangle2D e2) {
		return (e1 != null && e2 != null && e1.getMinX() == e2.getMinX() && e1.getMinY() == e2.getMinY() 
				&& e1.getMaxX() == e2.getMaxX() && e1.getMaxY() == e2.getMaxY());
	}

	/**
	 * Obtiene el Extent del canvas. Este canvas tiene un ancho en pixeles
	 * de w y un alto de h. Tiene en cuenta la escala a la que se quiere dibujar
	 * para devolver el extent cuando el zoom ya está aplicado.
	 * @param w Ancho del canvas en píxeles
	 * @param h Alto del canvas en píxeles
	 * @return Rectangle2D
	 */
	private Rectangle2D getCanvasEnvelope(double w, double h, double scale) {
		double tW = ((w / scale) / 2) * pixelSize;
		double tH = ((h / scale) / 2) * pixelSize;
		double minX = center.getX() - tW;
		double minY = center.getY() - tH;
		double width = Math.abs((center.getX() + tW) - minX);
		double height = Math.abs((center.getY() + tH) - minY);
		return new Rectangle2D.Double(minX, minY, width, height);
	}

	/**
	 * <P>
	 * Dibujado del buffer de datos sobre el Graphics.
	 * </P><P>
	 * No podemos aplicar un escalado al
	 * Graphics y dibujar porque cuando el zoom es mayor a 1 los pixeles no empiezan a dibujarse
	 * siempre en la esquina superior izquierda y al Graphics solo podemos ordenarle el dibujado
	 * en coordenadas enteras. Para solucionarlo debemos copiar el trozo de buffer a dibujar teniendo
	 * en cuenta el desplazamiento de la esquina superior izquierda de un pixel.
	 * </P>
	 * @param g
	 * @param ext
	 * @param w
	 * @param h
	 */
	private Graphics draw(Rectangle2D ext, double w, double h) {
		if(!equal(lastExtent, ext)  || forceRequest) {
			//Hallamos la coordenada pixel del buffer de la esquina superior izquierda del extent
			double pxX = ((ext.getMinX() - extent.getMinX()) * (w / scale)) / ext.getWidth();
			double pxY = ((extent.getMinY() - ext.getMinY()) * (h / scale)) / ext.getHeight();

			//Creamos el buffer y lo cargamos teniendo en cuenta el desplazamiento inicial
			double step = 1 / scale;

			double xValue = pxX;
			double yValue = pxY;

			for (int i = 0; i < w; i++) {
				yValue = pxY;
				for (int j = 0; j < h; j++) {
					if((int)xValue >= 0 && (int)yValue >= 0 && (int)xValue < image.getWidth() && (int)yValue < image.getHeight()) {
						lastImage.setRGB(i, j, image.getRGB((int)xValue, (int)yValue));
						lastImageWithLayers.setRGB(i, j, image.getRGB((int)xValue, (int)yValue));
					} else {
						lastImage.setRGB(i, j, 0xffffffff);
						lastImageWithLayers.setRGB(i, j, 0xffffffff);
					}
					yValue += step;
				}
				xValue += step;
			}
			if(viewListener != null) {
				viewListener.zoomViewChanged(viewEvent);
			}
			forceRequest = false;
		} else {
			((Graphics2D)lastImageWithLayers.getGraphics()).drawImage(lastImage, 0, 0, null);
		}

		return lastImageWithLayers.getGraphics();
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {
		for (int i = 0; i < graphicLayers.size(); i++) {
			((IGraphicLayer)graphicLayers.get(i)).mouseClicked(e);
		}
		repaint();
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent e) {
		for (int i = 0; i < graphicLayers.size(); i++) {
			((IGraphicLayer)graphicLayers.get(i)).mouseEntered(e);
		}
		repaint();
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent e) {
		for (int i = 0; i < graphicLayers.size(); i++) {
			((IGraphicLayer)graphicLayers.get(i)).mouseExited(e);
		}
		repaint();
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {
		for (int i = 0; i < graphicLayers.size(); i++) {
			((IGraphicLayer)graphicLayers.get(i)).mousePressed(e);
		}
		repaint();
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {
		for (int i = 0; i < graphicLayers.size(); i++) {
			((IGraphicLayer)graphicLayers.get(i)).mouseReleased(e);
		}
		repaint();
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 */
	public void mouseDragged(MouseEvent e) {
		for (int i = 0; i < graphicLayers.size(); i++) {
			((IGraphicLayer)graphicLayers.get(i)).mouseDragged(e);
		}
		repaint();
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	public void mouseMoved(MouseEvent e) {
		realCoord = viewCoordsToWorld(e.getPoint());
		//viewCoord = e.getPoint();
		for (int i = 0; i < graphicLayers.size(); i++) {
			((IGraphicLayer)graphicLayers.get(i)).mouseMoved(e);
		}
		repaint();
	}

	/**
	 * Asigna la tool seleccionada
	 * @param selectedTool
	 */
	public void setSelectedTool(BaseViewTool selectedTool) {
		this.selectedTool = selectedTool;
	}

	/**
	 * Obtiene la herramienta seleccionada
	 * @return BaseViewTool
	 */
	public BaseViewTool getSelectedTool() {
		return selectedTool;
	}

	/**
	 * Activa o desactiva el mostrado de información
	 * @param showInfo
	 */
	public void setShowInfo(boolean showInfo) {
		this.showInfo = showInfo;
	}

	/**
	 * Asigna el valor para el flag minxMaxyUL. Este flag informa de que la esquina
	 * superior izquierda corresponde con el valor de mínimo X y máximo Y. En caso
	 * de ser false esta esquina sería de mínimo X y mínimo Y.
	 * @param v
	 */
	public void setMinxMaxyUL(boolean v) {
		this.minxMaxyUL = v;
	}

	/**
	 * Obtiene el valor para el flag minxMaxyUL. Este flag informa de que la esquina
	 * superior izquierda corresponde con el valor de mínimo X y máximo Y. En caso
	 * de ser false esta esquina sería de mínimo X y mínimo Y.
	 * @param v
	 */
	public boolean getMinxMaxyUL() {
		return minxMaxyUL;
	}

	/**
	 * Asigna el color del texto
	 * @param textColor
	 */
	public void setTextColor(Color textColor) {
		this.textColor = textColor;
		repaint();
	}

	/**
	 * Obtiene el color del texto
	 * @return
	 */
	public Color getTextColor() {
		return textColor;
	}

	/**
	 * Obtiene el color de fondo
	 * @return
	 */
	public Color getBackgroundColor() {
		return backgroundColor;
	}

	/**
	 * Asigna el color de fondo
	 * @param backgroundColor
	 */
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	/**
	 * Normalmente no se hace una petición al dibujado del raster si el extent no ha variado. Si esta variable
     * está a true fuerza que haya una petición de redibujado aunque el extent del raster no haya cambiado.
     * Esto solo se hace para una petición. La siguiente vuelve a estar a false.
	 * @return
	 */
	public boolean isForceRequest() {
		return forceRequest;
	}

	/**
	 * Normalmente no se hace una petición al dibujado del raster si el extent no ha variado. Si esta variable
     * está a true fuerza que haya una petición de redibujado aunque el extent del raster no haya cambiado.
     * Esto solo se hace para una petición. La siguiente vuelve a estar a false.
	 * @param forceRequest
	 */
	public void setForceRequest(boolean forceRequest) {
		this.forceRequest = forceRequest;
	}
}
