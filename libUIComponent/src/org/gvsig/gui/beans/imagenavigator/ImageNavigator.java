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
 */
package org.gvsig.gui.beans.imagenavigator;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

import org.gvsig.gui.beans.Messages;
/**
 * <code>ImageNavigator</code> es un componente que representa un manejador
 * de imágenes. En él se puede desplazar, hacer un zoom out o un zoom in a una
 * imagen virtual. El componente no trata la imagen en si, solo lanza los
 * eventos indicando la nueva posición y zoom de la imagen, luego es el usuario
 * el que se encargará de dibujar esa imagen en la posición correspondiente.
 *
 * El modo de uso es el siguiente:
 * - Se puede desplazar una imagen con el botón izquierdo del ratón.
 * - Se puede hacer zoom in/out con las teclas +/- del teclado.
 * - Se puede hacer zoom in/out con la rueda del ratón teniendo en cuenta la
 * posición del mismo.
 * - Se puede resetear los valores con las teclas 'Espacio' o 0;
 * - Las teclas 1, 2, 3, 4 y 5 equivalen a zoom 1, 2, 4, 8 y 16 respectivamente.
 * - La tecla C sirve para centrar la imagen.
 * - La tecla B sirve para mostrar u ocultar los cuadros del fondo. Útil para
 *   mostrar imagenes con transparencia.
 * - La tecla H muestra la ayuda.
 *
 * @version 04/05/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class ImageNavigator extends JComponent implements KeyListener, MouseMotionListener, MouseListener, MouseWheelListener {
	private static final long serialVersionUID = 1164788214432359272L;
	private IClientImageNavigator iClient         = null;

	private Image                 image           = null;
	private Graphics2D            widgetGraphics  = null;
	private Image                 imageCache      = null;
	private Graphics2D            cacheGraphics   = null;

	private double                zoom            = 1.0;
	private double                x1              = 0.0;
	private double                y1              = 0.0;
	private boolean               yInverted       = false;
	private boolean               xInverted       = false;
	private int                   width           = 0;
	private int                   height          = 0;
	private boolean               showHelp        = false;
	private boolean               showBackground  = false;
	private Color                 backgroundColor = new Color(224, 224, 224);
	private ImageIcon             imageIconClose  = null;
	private ImageIcon             imageIconHelp   = null;
	private ImageIcon             imageIconError  = null;

	private double                initX1          = 0.0;
	private double                initY1          = 0.0;
	private double                initX2          = 100.0;
	private double                initY2          = 100.0;
	private double                initZoom        = 1.0;
	private boolean               autoAdjusted    = true;

	private boolean               errorDrawing    = false;
	private String                errorDrawingMsg = null;


	/**
	 * Crea un <code>ImageNavigator</code>
	 */
	public ImageNavigator() {
		this.setFocusable(true);
		this.addKeyListener(this);
		this.addMouseMotionListener(this);
		this.addMouseListener(this);
		this.addMouseWheelListener(this);
	}
	
	/**
	 * Crea un <code>ImageNavigator</code> especificandole quien pintara el
	 * componente
	 * @param iClient
	 */
	public ImageNavigator(IClientImageNavigator iClient) {
		this();
		setClientImageNavigator(iClient);
	}
	
	public void setClientImageNavigator(IClientImageNavigator iClient) {
		this.iClient = iClient;
	}

	/**
	 * Actualiza las dimensiones para ajustar la imagen a los bordes especificados
	 * con setViewDimensions.
	 */
	private void updateDimensions() {
		double factor = this.getWidth() / (initX2 - initX1);
		if (factor > (this.getHeight() / (initY2 - initY1)))
			factor = this.getHeight() / (initY2 - initY1);
		zoom = factor;
		imageCenter();
	}

	/**
	 * Centra la imagen
	 */
	public void imageCenter() {
		x1 = initX1;
		y1 = initY1;

		if (isXInverted())
			x1 -= ((initX2 - initX1) - this.getWidth() / zoom) / 2.0;
		else
			x1 += ((initX2 - initX1) - this.getWidth() / zoom) / 2.0;
		if (isYInverted())
			y1 -= ((initY2 - initY1) - this.getHeight() / zoom) / 2.0;
		else
			y1 += ((initY2 - initY1) - this.getHeight() / zoom) / 2.0;
	}

	/**
	 * Especifica el rectangulo de la imagen a visualizar, pudiendo tener
	 * cualquiera de los ejes X e Y invertidos
	 *
	 * @param x1 Coordenada izquierda
	 * @param y1 Coordenada superior
	 * @param x2 Coordenada derecha
	 * @param y2 Coordenada inferior
	 */
	public void setViewDimensions(double x1, double y1, double x2, double y2) {
		this.initX1 = x1;
		this.initX2 = x2;
		this.initY1 = y1;
		this.initY2 = y2;

		yInverted = (y2 < y1);
		if (yInverted) {
			this.initY1 = y2;
			this.initY2 = y1;
		}

		xInverted = (x2 < x1);
		if (xInverted) {
			this.initX1 = x2;
			this.initX2 = x1;
		}

		this.updateDimensions();
	}

	/**
	 * Hace un forzado de pintado del buffer temporal de la imagen. Este método
	 * forzará una llamada a la función de pintado del cliente.
	 */
	public void updateBuffer() {
		updateImageCache(true);
		refreshImage(0, 0);
	}

	/**
	 * Especifica el zoom que usará por defecto el componente.
	 * @param zoom
	 */
	public void setZoom(double zoom) {
		initZoom = zoom;
		this.zoom = initZoom;
		autoAdjusted = false;
		imageCenter();
	}

	/**
	 * Especifica el zoom que usará por defecto el componente.
	 * @param zoom
	 */
	public void setAutoAdjusted() {
		autoAdjusted = true;
		updateDimensions();
		updateImageCache(true);
		refreshImage(0, 0);
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.JComponent#addNotify()
	 */
	public void addNotify() {
		super.addNotify();

		updateImageCache(true);
		refreshImage(0, 0);
	}

	/**
	 * Hace un zoom de aumento en las coordenadas especificadas
	 * @param x
	 * @param y
	 */
	private void ZoomIn(double x, double y) {
		double xcent = (x / zoom);
		double ycent = (y / zoom);
		if (isXInverted())
			x1 -= xcent;
		else
			x1 += xcent;
		if (isYInverted())
			y1 -= ycent;
		else
			y1 += ycent;
		zoom = zoom * 2.0;
		xcent = (x / zoom);
		ycent = (y / zoom);
		if (isXInverted())
			x1 += xcent;
		else
			x1 -= xcent;
		if (isYInverted())
			y1 += ycent;
		else
			y1 -= ycent;
		updateImageCache(true);
		refreshImage(0, 0);
	}

	/**
	 * Hace un zoom hacia afuera en las coordenadas especificadas
	 * @param x
	 * @param y
	 */
	private void ZoomOut(double x, double y) {
		double xcent = (x / zoom);
		double ycent = (y / zoom);
		if (isXInverted())
			x1 -= xcent;
		else
			x1 += xcent;
		if (isYInverted())
			y1 -= ycent;
		else
			y1 += ycent;
		zoom = zoom / 2.0;
		xcent = (x / zoom);
		ycent = (y / zoom);
		if (isXInverted())
			x1 += xcent;
		else
			x1 -= xcent;
		if (isYInverted())
			y1 += ycent;
		else
			y1 -= ycent;
		updateImageCache(true);
		refreshImage(0, 0);
	}

	/**
	 * Mostrar o ocultar la ayuda
	 *
	 */
	private void callShowHelp() {
		showHelp = !showHelp;
		updateImageCache(true);
		refreshImage(0, 0);
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyChar()) {
			case 'h':
			case 'H':
				callShowHelp();
				break;
			case '+':
				ZoomIn(width / 2.0, height / 2.0);
				autoAdjusted = false;
				break;
			case '-':
				ZoomOut(width / 2.0, height / 2.0);
				autoAdjusted = false;
				break;
			case '1':
				autoAdjusted = false;
				this.zoom = initZoom;
				imageCenter();
				updateImageCache(true);
				refreshImage(0, 0);
				break;
			case '2':
				autoAdjusted = false;
				this.zoom = initZoom * 2.0;
				imageCenter();
				updateImageCache(true);
				refreshImage(0, 0);
				break;
			case '3':
				autoAdjusted = false;
				this.zoom = initZoom * 4.0;
				imageCenter();
				updateImageCache(true);
				refreshImage(0, 0);
				break;
			case '4':
				autoAdjusted = false;
				this.zoom = initZoom * 8.0;
				imageCenter();
				updateImageCache(true);
				refreshImage(0, 0);
				break;
			case '5':
				autoAdjusted = false;
				this.zoom = initZoom * 16.0;
				imageCenter();
				updateImageCache(true);
				refreshImage(0, 0);
				break;
			case 'c':
			case 'C':
				imageCenter();
				updateImageCache(true);
				refreshImage(0, 0);
				break;
			case 'b':
			case 'B':
				setShowBackground(!isShowBackground());
				break;
			case '0':
			case ' ':
				setAutoAdjusted();
				break;
		}
	}

	double updateWidth = 0;
	double updateHeight = 0;
	/**
	 * Método que hara la invocación al cliente del pintado del trozo de imagen a
	 * visualizar
	 * @param forceUpdate
	 */
	private void updateImageCache(boolean forceUpdate) {
		if (getWidgetImage() == null ||
				(updateWidth == getWidgetImage().getWidth(this) &&
				updateHeight == getWidgetImage().getHeight(this) &&
				!forceUpdate))
			return;
		updateWidth = getWidgetImage().getWidth(this);
		updateHeight = getWidgetImage().getHeight(this);

		if (showBackground) {
			for (int i = 0; (i * 4) <= width; i++) {
				for (int j = 0; (j * 4) <= height; j++) {
					if ((i + j) % 2 == 0)
						getCacheGraphics().setColor(Color.white);
					else
						getCacheGraphics().setColor(getBackgroundColor());
					getCacheGraphics().fillRect(i * 4, j * 4, 4, 4);
				}
			}
		} else {
			getCacheGraphics().setColor(Color.white);
			getCacheGraphics().fillRect(0, 0, width, height);
		}

		double newY1 = 0.0;
		double newY2 = 0.0;
		double newX1 = 0.0;
		double newX2 = 0.0;

		if (isYInverted()) {
			newY1 = y1 + this.getHeight() / zoom - ((y1 - initY1) * 2.0);
			newY2 = newY1 - this.getHeight() / zoom;
		} else {
			newY1 = y1;
			newY2 = y1 + this.getHeight() / zoom;
		}

		if (isXInverted()) {
			newX1 = x1 + this.getWidth() / zoom - ((x1 - initX1) * 2.0);
			newX2 = newX1 - this.getWidth() / zoom;
		} else {
			newX1 = x1;
			newX2 = x1 + this.getWidth() / zoom;
		}

		if ((Double.isNaN(newX1)) || (Double.isNaN(newY1)))
			return;

		if (iClient != null)
			try {
				errorDrawing = false;
				iClient.drawImage(getCacheGraphics(), newX1, newY1, newX2, newY2, zoom, this.getWidth(), this.getHeight());
			} catch (ImageUnavailableException e) {
				getCacheGraphics().setColor(new Color(224, 224, 224));
				getCacheGraphics().fillRect(0, 0, width, height);
				errorDrawing = true;
				errorDrawingMsg = e.getMessage();
			}
	}

	private Image getWidgetImage() {
		int width2 = getBounds().width;
		int height2 = getBounds().height;
		if (width2 <= 0)
			width2 = 1;
		if (height2 <= 0)
			height2=1;

		if ((width != width2) || (height != height2)) {
			image = createImage(width2, height2);
			imageCache = createImage(width2, height2);
			if (image == null)
				return null;
			widgetGraphics = (Graphics2D) image.getGraphics();
			cacheGraphics = (Graphics2D) imageCache.getGraphics();

			RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			hints.add(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
			cacheGraphics.setRenderingHints(hints);
		}

		width = width2;
		height = height2;
		return image;
	}

	private Graphics2D getWidgetGraphics() {
		getWidgetImage();
		return widgetGraphics;
	}

	private Graphics2D getCacheGraphics() {
		getWidgetImage();
		return cacheGraphics;
	}

	/**
	 * Redibujar el componente en el graphics temporal
	 */
	private void redrawBuffer(int x, int y) {
		if (showBackground) {
			for (int i = -2; ((i - 2) * 4) <= width; i++) {
				for (int j = -2; ((j - 2) * 4) <= height; j++) {
					if ((i + j) % 2 == 0)
						getWidgetGraphics().setColor(Color.white);
					else
						getWidgetGraphics().setColor(getBackgroundColor());
					getWidgetGraphics().fillRect((i * 4) + (x % 8), (j * 4) + (y % 8), 4, 4);
				}
			}
		} else {
			getWidgetGraphics().setColor(Color.white);
			getWidgetGraphics().fillRect(0, 0, width, height);
		}

		getWidgetGraphics().drawImage(imageCache, x, y, null);

		if (errorDrawing) {
			paintError((Graphics2D) getWidgetGraphics());
		} else {
			if (showHelp) {
				paintHelp((Graphics2D) getWidgetGraphics());
			} else {
				getWidgetGraphics().drawImage(getIconHelp().getImage(), width - getIconHelp().getIconWidth() - 4, 3, null);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g) {
		if (autoAdjusted) updateDimensions();
		Graphics2D g2d = (Graphics2D) g;
		RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		hints.add(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
		g2d.setRenderingHints(hints);

		updateImageCache(false);

		redrawBuffer(0, 0);

		if (image != null) {
			if (isEnabled()) {
				g.drawImage(image, 0, 0, this);
			} else {
				// Dibujar en escala de grises y aclarado para cuando esta inactivo
				BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

				Graphics big = bi.createGraphics();
				big.drawImage(image, 0, 0, this);

				ColorConvertOp colorConvert = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
				colorConvert.filter(bi, bi);

				big.setColor(new Color(255, 255, 255, 164));
				big.fillRect(0, 0, width, height);

				g.drawImage(bi, getVisibleRect().x, getVisibleRect().y, this);
			}
		}
	}

	/**
	 * Redibujar el componente en el graphics temporal y representarlo en el
	 * componente
	 */
	private void refreshImage(int x, int y) {
		Graphics2D g2d = (Graphics2D) getGraphics();
		if (g2d == null)
			return;
		RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		hints.add(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
		g2d.setRenderingHints(hints);
		redrawBuffer(x, y);

		if (image != null)
			getGraphics().drawImage(image, 0, 0, this);
	}

	/**
	 * Devuelve el icono de cerrar
	 * @return
	 */
	private ImageIcon getIconClose() {
		if (imageIconClose == null) {
			imageIconClose = new ImageIcon(ImageNavigator.class.getResource("images/close.png"));
		}
		return imageIconClose;
	}

	/**
	 * Devuelve el icono de error
	 * @return
	 */
	private ImageIcon getIconError() {
		if (imageIconError == null) {
			imageIconError = new ImageIcon(ImageNavigator.class.getResource("images/error.png"));
		}
		return imageIconError;
	}

	/**
	 * Devuelve el icono ayuda
	 * @return
	 */
	private ImageIcon getIconHelp() {
		if (imageIconHelp == null) {
			imageIconHelp = new ImageIcon(ImageNavigator.class.getResource("images/help.png"));
		}
		return imageIconHelp;
	}

	private void paintHelp(Graphics2D g) {
		int sep = 13;
		int pos = sep + 1;

		int alto = sep * 8 + 6;

		Image image2 = createImage(width, alto);
		Graphics2D graphics2 = (Graphics2D) image2.getGraphics();

		RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		hints.add(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
		graphics2.setRenderingHints(hints);

		alto--;

		Color color1 = new Color(255, 255, 178);
		Color color2 = new Color(255, 255, 74);
		graphics2.setPaint(new GradientPaint(0, 0, color1, 0, alto, color2, false));
		graphics2.fillRect(0, 0, width, alto);

		graphics2.setColor(new Color(0, 0, 0));

		graphics2.setFont(new java.awt.Font("Tahoma", 1, sep - 2));

		graphics2.drawString(Messages.getText("teclas") + ":", 10, pos);

		graphics2.setFont(new java.awt.Font("Tahoma", 0, sep - 2));
		pos += sep;
		graphics2.drawString(Messages.getText("ayuda_c"), 20, pos);
		pos += sep;
		graphics2.drawString(Messages.getText("ayuda_0"), 20, pos);
		pos += sep;
		graphics2.drawString(Messages.getText("ayuda_1_5"), 20, pos);
		pos += sep;
		graphics2.drawString(Messages.getText("ayuda_more_less"), 20, pos);
		pos += sep;
		graphics2.drawString(Messages.getText("ayuda_wheel"), 20, pos);
		pos += sep;
		graphics2.drawString(Messages.getText("ayuda_background"), 20, pos);
		pos += sep;
		graphics2.drawString(Messages.getText("ayuda_h"), 20, pos);

		graphics2.setColor(new Color(185, 185, 185));
		graphics2.drawLine(0, alto, width, alto);

		graphics2.drawImage(getIconClose().getImage(), width - getIconClose().getIconWidth() - 4, 3, null);

		AlphaComposite myAlpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f);
		g.setComposite(myAlpha);

		g.drawImage(image2, 0, 0, this);

		myAlpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f);
		g.setComposite(myAlpha);
	}
	
	private ArrayList<String> splitString4Width(Graphics2D g, String msg, int width) {
		String words[] = msg.split(" ");

		ArrayList<String> newString = new ArrayList<String>();
		String aux = "";
		for (int i = 0; i < words.length; i++) {
			if (aux.length() == 0) {
				aux = words[i];
				continue;
			}
			if (g.getFontMetrics().getStringBounds(aux + " " + words[i], g).getWidth() <= width) {
				aux += " " + words[i];
				continue;
			}

			newString.add(aux);
			aux = words[i];
		}
		newString.add(aux);

		return newString;
	}

	private ArrayList<String> splitString4Intro(Graphics2D g, String msg, int width) {
		String words[] = msg.split("\n");
		ArrayList<String> newString = new ArrayList<String>();
		for (int i = 0; i < words.length; i++) {
			ArrayList<String> more = splitString4Width(g, words[i], width);
			for (int j = 0; j < more.size(); j++) {
				newString.add(more.get(j));
			}
		}

		return newString;
	}
	
	private void paintError(Graphics2D g) {
		ArrayList<String> errors = splitString4Intro(g, errorDrawingMsg, width - 30);

		int size = errors.size();
		if (size < 6)
			size = 6;
		
		int sep = 13;
		int pos = sep + 2;

		int alto = sep * size + 8;

		Image image2 = createImage(width, alto);
		Graphics2D graphics2 = (Graphics2D) image2.getGraphics();

		alto--;

		Color color1 = new Color(255, 152, 152);
		Color color2 = new Color(255, 100, 100);
		graphics2.setPaint(new GradientPaint(0, 0, color1, 0, alto, color2, false));
		graphics2.fillRect(0, 0, width, alto);

		graphics2.setColor(new Color(0, 0, 0));

		graphics2.setFont(new java.awt.Font("Tahoma", 0, sep - 2));

		for (int i=0; i<errors.size(); i++) { 
			graphics2.drawString((String) errors.get(i), 24, pos);
			pos+=sep;
		}
		
		graphics2.drawImage(getIconError().getImage(), 4, 4, null);

		graphics2.setColor(new Color(185, 185, 185));
		graphics2.drawLine(0, alto, width, alto);

		g.drawImage(image2, 0, 0, this);
	}


	Point mouse = null;
	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {
		if (!isInteractiveEnabled())
			return;
		requestFocus();

		if ((e.getX() > (width - 20)) && (e.getY() < 20))
			return;

		if ((e.getButton() != MouseEvent.BUTTON1) && (e.getButton() != MouseEvent.BUTTON2))
			return;

		// Oscurece la imagen cuando se mueve
		Color gris = new Color(0, 0, 0, 16);
		getCacheGraphics().setColor(gris);
		getCacheGraphics().fillRect(0, 0, width-1, height-1);

		// Pone un borde a la imagen cuando se mueve
		getCacheGraphics().setColor(Color.gray);
		getCacheGraphics().drawRect(0, 0, width-1, height-1);

		mouse = new Point(e.getX(), e.getY());
		changePos(e.getX(), e.getY());
		autoAdjusted = false;
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 */
	public void mouseDragged(MouseEvent e) {
		if (!isInteractiveEnabled())
			return;
		changePos(e.getX(), e.getY());
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {
		if (!isInteractiveEnabled())
			return;
		if (mouse != null) {
			x1 = x1 - ((e.getX() - mouse.getX())/zoom);
			y1 = y1 - ((e.getY() - mouse.getY())/zoom);
			updateImageCache(true);
			refreshImage(0, 0);
		}
		mouse = null;
	}

	private void changePos(int x, int y) {
		if (mouse != null)
			refreshImage((int) (x - mouse.getX()), (int) (y - mouse.getY()));
	}

	/**
	 * Evento de la rueda del ratón para hacer Zoom In o Zoom Out en la posición
	 * del puntero.
	 */
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (!isInteractiveEnabled())
			return;

		if (e.getWheelRotation() > 0) {
			ZoomOut(isXInverted() ? this.getWidth() - e.getX() : e.getX(), isYInverted() ? this.getHeight() - e.getY() : e.getY());
			autoAdjusted = false;
		}
		if (e.getWheelRotation() < 0) {
			ZoomIn(isXInverted() ? this.getWidth() - e.getX() : e.getX(), isYInverted() ? this.getHeight() - e.getY() : e.getY());
			autoAdjusted = false;
		}
	}

	/**
	 * Obtener si el eje de las Y esta invertido
	 * @return
	 */
	private boolean isYInverted() {
		return yInverted;
	}

	/**
	 * Obtener si el eje de las X esta invertido
	 * @return
	 */
	private boolean isXInverted() {
		return xInverted;
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	public void mouseMoved(MouseEvent e) {
		if (!isInteractiveEnabled())
			return;
		if ((e.getX() > (width - 20)) && (e.getY() < 20)) {
			setCursor(new Cursor(Cursor.HAND_CURSOR));
			if (showHelp)
				setToolTipText(Messages.getText("cerrar"));
			else
				setToolTipText(Messages.getText("ayuda"));
		} else {
			setCursor(new Cursor(Cursor.MOVE_CURSOR));
			setToolTipText(null);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {
		if (!isInteractiveEnabled())
			return;
		if ((e.getX() > (width - 20)) && (e.getY() < 20))
			callShowHelp();
	}

	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}

	/**
	 * Define el color de los recuadros del fondo
	 *
	 * @param backgroundColor the backgroundColor to set
	 */
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	/**
	 * Devuelve el color de los recuadros del fondo
	 * @return the backgroundColor
	 */
	public Color getBackgroundColor() {
		return backgroundColor;
	}

	/**
	 * Devuelve si se esta mostrando o no la cuadricula de fondo.
	 * @return the showBackground
	 */
	public boolean isShowBackground() {
		return showBackground;
	}

	/**
	 * Define si se muestra o no la cuadricula de fondo. Util para imagenes
	 * transparentes
	 * @param showBackground the showBackground to set
	 */
	public void setShowBackground(boolean showBackground) {
		this.showBackground = showBackground;
		updateImageCache(true);
		refreshImage(0, 0);
	}
	
	/**
	 * Devuelve si se pueden gestionar los eventos segun el estado del componente
	 * @return
	 */
	private boolean isInteractiveEnabled() {
		if (isEnabled() && !errorDrawing)
			return true;
		return false;
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setEnabled(boolean)
	 */
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		updateImageCache(true);
		refreshImage(0, 0);
	}
}