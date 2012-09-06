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
package org.gvsig.gui.beans.doubleslider;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JComponent;
/**
 * <code>DoubleSlider</code> representa un componente que tiene dos
 * deslizadores. Se puede definir un máximo y un mínimo.
 *
 * @version 04/05/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class DoubleSlider extends JComponent implements MouseMotionListener, MouseListener, MouseWheelListener {
	private static final long serialVersionUID = 663355422780987493L;

	private ArrayList<DoubleSliderListener> actionCommandListeners = new ArrayList<DoubleSliderListener>();

	private final int LEFT_PAD               = 2;
	private final int RIGHT_PAD              = 2;

	private Image     bufferImage            = null;
	private int       width                  = 0;
	private int       height                 = 0;
	private Graphics  bufferGraphics;
	private double    x1                     = 0;
	private double    x2                     = 100;

	private Color     color1                 = Color.BLACK;
	private Color     color2                 = Color.WHITE;

	private double    minimum                = 0;
	private double    maximum                = 100;

	private boolean   twoSliders             = true;

	/**
	 * Crea un DoubleSlider con las opciones por defecto.
	 */
	public DoubleSlider() {
		this.setPreferredSize(new Dimension(100, 21));
		addMouseMotionListener(this);
		addMouseListener(this);
		addMouseWheelListener(this);
	}

	/**
	 * Establece el máximo valor que puede tomar el componente
	 * @param value
	 */
	public void setMaximum(int value) {
		maximum = value;
		validateValues();
		refreshImage();
	}

	/**
	 * Establece el mínimo valor que puede tomar el componente
	 * @param value
	 */
	public void setMinimum(int value) {
		minimum = value;
		validateValues();
		refreshImage();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.JComponent#addNotify()
	 */
	public void addNotify() {
		super.addNotify();

		refreshImage();
	}

	/**
	 * Crea un graphics con las dimensiones del componente si no estaba creado y
	 * lo devuelve para poder usarlo para dibujar en él.
	 * @return Graphics
	 */
	private Graphics getBufferGraphics() {
		int width2 = getBounds().width;
		int height2 = getBounds().height;
		if (width2 <= 0)
			width2 = 1;
		if (height2 <= 0)
			height2 = 1;

		if ((width!=width2) || (height!=height2)) {
			bufferImage = createImage(width2, height2);
			if (bufferImage == null) return null;
			bufferGraphics = bufferImage.getGraphics();
		}

		width = width2;
		height = height2;

		return bufferGraphics;
	}

	public Color getColorPosition(int pos) {
		int r, g, b, a;

		Color color1 = this.color1;
		Color color2 = this.color2;
		if (!isEnabled()) {
			r = Color.DARK_GRAY.getRed();
			color1 = new Color(r, r, r);
			r = Color.WHITE.getRed();
			color2 = new Color(r, r, r);
		}

		double value = (pixeltovalue(pos) - minimum) / (maximum - minimum);
		if (value < 0)
			value = 0;
		if (value > 1)
			value = 1;

		r = (int) (color1.getRed() + ((color2.getRed() - color1.getRed()) * value));
		g = (int) (color1.getGreen() + ((color2.getGreen() - color1.getGreen()) * value));
		b = (int) (color1.getBlue() + ((color2.getBlue() - color1.getBlue()) * value));
		a = (int) (color1.getAlpha() + ((color2.getAlpha() - color1.getAlpha()) * value));
		return new Color(r, g, b, a);
	}
	/**
	 * Redibujar el componente en el graphics temporal
	 */
	public void redrawBuffer() {
		if (getBufferGraphics() == null)
			return;

		getBufferGraphics().setColor(this.getBackground());
		getBufferGraphics().fillRect(0, 0, width, height);

		int top = (height - 11) / 2;
		top -= 6;

		Shape oldClip = getBufferGraphics().getClip();
		getBufferGraphics().setClip(LEFT_PAD + 1, top + 1, width - 2 - LEFT_PAD - RIGHT_PAD, 9);

		if ((color1.getAlpha() != 255) || (color2.getAlpha() != 255)) {
			for (int i = 0; (i * 4) <= (width - 1 - RIGHT_PAD); i++) {
				for (int j = 0; (j * 4) <= (top + 6); j++) {
					if ((i + j) % 2 == 0)
						getBufferGraphics().setColor(Color.white);
					else
						getBufferGraphics().setColor(new Color(204, 204, 204));
					getBufferGraphics().fillRect(LEFT_PAD + 1 + i * 4, top + 1 + j * 4, 4, 4);
				}
			}
		}

		for (int i = LEFT_PAD + 1; i < (width - 1 - RIGHT_PAD); i++) {
			getBufferGraphics().setColor(getColorPosition(i));
			getBufferGraphics().drawLine(i, top, i, top + 10);
		}
		getBufferGraphics().setClip(oldClip);

		if (!isEnabled())
			getBufferGraphics().setColor(Color.LIGHT_GRAY);
		else
			getBufferGraphics().setColor(Color.BLACK);
		getBufferGraphics().drawRect(LEFT_PAD, top, width - 1 - LEFT_PAD - RIGHT_PAD, 10);


		drawTriangle(valuetopixel(x1), top, getColorPosition(valuetopixel(x1)));
		if (twoSliders)
			drawTriangle(valuetopixel(x2), top, getColorPosition(valuetopixel(x2)));
	}

	/**
	 * Dibujar un triangulo, un triangulo es un deslizador del componente. Puedes
	 * indicarle que color tendra y en que posición estará.
	 * @param x
	 * @param color
	 */
	private void drawTriangle(int x, int y, Color color) {
		if (isEnabled()) {
			getBufferGraphics().setColor(color);
			getBufferGraphics().drawLine(x, y + 12, x, y + 16);
			getBufferGraphics().drawLine(x-1, y + 14, x-1, y + 16);
			getBufferGraphics().drawLine(x+1, y + 14, x+1, y + 16);
			getBufferGraphics().drawLine(x-2, y + 16, x-2, y + 16);
			getBufferGraphics().drawLine(x+2, y + 16, x+2, y + 16);
		}

		if (isEnabled()) {
			getBufferGraphics().setColor(Color.BLACK);
		} else {
			getBufferGraphics().setColor(Color.GRAY);
		}
		getBufferGraphics().drawLine(x, y + 10, x-3, y + 17);
		getBufferGraphics().drawLine(x, y + 10, x+3, y + 17);
		getBufferGraphics().drawLine(x-3, y + 17, x+3, y + 17);
	}

	/**
	 * Redibujar el componente en el graphics temporal y representarlo en el
	 * componente
	 */
	public void refreshImage() {
		redrawBuffer();
		if (getGraphics() == null)
			return;
		if (bufferImage != null)
			getGraphics().drawImage(bufferImage, 0, 0, this);
		super.paint(getGraphics());
	}

	/**
	 * Convierte un valor a la coordenada pixel correspondiente
	 * @param value
	 * @return
	 */
	private int valuetopixel(double value) {
		return (int) (((value - minimum) * (width - 3.0 - LEFT_PAD - RIGHT_PAD) / (maximum - minimum)) + 1.0 + LEFT_PAD);
	}

	/**
	 * Convierte un pixel al valor que debería tener
	 * @param value
	 * @return
	 */
	private double pixeltovalue(int value) {
		return ((((value - 1 - LEFT_PAD) * (maximum - minimum)) / (width - 3.0 - LEFT_PAD - RIGHT_PAD)) + minimum);
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g) {
		redrawBuffer();
		g.drawImage(bufferImage, 0, 0, this);
		super.paint(g);
	}

	int valuePressed = 0;

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {
		if (!isEnabled())
			return;
		if (e.getButton() != MouseEvent.BUTTON1) return;

		double aux = pixeltovalue(e.getX());
		double aux2 = aux - x1;
		double aux3 = x2 - aux;
		if (aux3 < aux2)
			valuePressed = 2;
		else
			valuePressed = 1;

		if (!twoSliders)
			valuePressed = 1;

		changeValue(e.getX());
		callValueDraggedListeners();
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {
		valuePressed = 0;
		callValueChangedListeners();
	}

	/**
	 * Establecer los límites de los valores en caso de que sean incorrectos
	 */
	private void validateValues() {
		if (x1 < minimum) x1 = minimum;
		if (x1 > maximum) x1 = maximum;
		if (twoSliders) {
			if (x2 < minimum) x2 = minimum;
			if (x2 > maximum) x2 = maximum;
		}
	}

	/**
	 * Establecer el valor del extremo izquierdo del slider
	 * @param value
	 */
	public void setX1(int value) {
		setX1((double) value);
	}

	/**
	 * Establecer el valor del extremo izquierdo del slider
	 * @param value
	 */
	public void setX1(double value) {
		x1 = value;
		if (twoSliders)
			if (x1 > x2)
				x2 = x1;
		validateValues();
		refreshImage();
	}

	/**
	 * Es lo mismo que setX1()
	 * @param value
	 */
	public void setValue(int value) {
		setX1(value);
	}

	/**
	 * Establecer el valor del extremo derecho del slider
	 * @param value
	 */
	public void setX2(int value) {
		setX2((double) value);
	}

	/**
	 * Establecer el valor del extremo derecho del slider
	 * @param value
	 */
	public void setX2(double value) {
		x2 = value;
		if (x2 < x1) x1 = x2;
		validateValues();
		refreshImage();
	}

	/**
	 * Obtener el valor del extremo izquierdo del componente
	 * @return
	 */
	public int getX1() {
		return (int) x1;
	}

	/**
	 * Devuelve lo mismo que getX1()
	 * @return
	 */
	public int getValue() {
		return getX1();
	}

	/**
	 * Obtener el valor del extremo derecho del componente
	 * @return
	 */
	public int getX2() {
		return (int) x2;
	}

	/**
	 * Método usado por los eventos del ratón para establecer una nueva posición
	 * del slider
	 * @param pos
	 */
	private void changeValue(int pos) {
		if (!isEnabled())
			return;
		if (valuePressed == 0) return;

		double aux = pixeltovalue(pos);

		if (valuePressed == 1) setX1(aux);
		if (valuePressed == 2) setX2(aux);
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 */
	public void mouseDragged(MouseEvent arg0) {
		if (!isEnabled())
			return;
		changeValue(arg0.getX());
		callValueDraggedListeners();
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	public void mouseMoved(MouseEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent e) {
	}

	/**
	 * Añadir un listener a la lista de eventos
	 * @param listener
	 */
	public void addValueChangedListener(DoubleSliderListener listener) {
		if (!actionCommandListeners.contains(listener))
			actionCommandListeners.add(listener);
	}

	/**
	 * Borrar un listener de la lista de eventos
	 * @param listener
	 */
	public void removeValueChangedListener(DoubleSliderListener listener) {
		actionCommandListeners.remove(listener);
	}

	/**
	 * Invocar a los eventos asociados al componente
	 */
	private void callValueChangedListeners() {
		Iterator<DoubleSliderListener> iterator = actionCommandListeners.iterator();
		while (iterator.hasNext()) {
			DoubleSliderListener listener = iterator.next();
			listener.actionValueChanged(new DoubleSliderEvent(this));
		}
	}

	/**
	 * Invocar a los eventos asociados al componente
	 */
	private void callValueDraggedListeners() {
		Iterator<DoubleSliderListener> iterator = actionCommandListeners.iterator();
		while (iterator.hasNext()) {
			DoubleSliderListener listener = iterator.next();
			listener.actionValueDragged(new DoubleSliderEvent(this));
		}
	}

	/**
	 * @return the twoSliders
	 */
	public boolean isTwoSliders() {
		return twoSliders;
	}

	/**
	 * @param twoSliders the twoSliders to set
	 */
	public void setTwoSliders(boolean twoSliders) {
		this.twoSliders = twoSliders;
		refreshImage();
	}

	/**
	 * @param color1 the color1 to set
	 */
	public void setColor1(Color color1, boolean refresh) {
		this.color1 = color1;
		if (refresh)
			refreshImage();
	}

	/**
	 * @param color2 the color2 to set
	 */
	public void setColor2(Color color2, boolean refresh) {
		this.color2 = color2;
		if (refresh)
			refreshImage();
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setEnabled(boolean)
	 */
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		refreshImage();
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		if (!isEnabled())
			return;

		double aux = pixeltovalue(e.getX());
		double aux2 = aux - x1;
		double aux3 = x2 - aux;
		if (aux3 < aux2)
			valuePressed = 2;
		else
			valuePressed = 1;

		if (!twoSliders)
			valuePressed = 1;

		if (valuePressed == 1)
			setX1(getX1() - e.getWheelRotation());
		else
			setX2(getX2() - e.getWheelRotation());
		valuePressed = 0;

		callValueChangedListeners();
	}
}