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
package org.gvsig.gui.beans.colorbutton;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;

import javax.swing.JComponent;

import org.gvsig.gui.beans.colorchooser.ColorChooserListener;
/**
 *
 * @version 03/08/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class ColorButton extends JComponent implements MouseListener, ColorChooserListener {
	private static final long serialVersionUID = 6003841064431749542L;

	private ArrayList<ColorButtonListener> actionCommandListeners = new ArrayList<ColorButtonListener>();

	private Color             color            = Color.black;
	private ColorButtonDialog colorButtonPanel = null;
	private Image             bufferImage      = null;
	private int               width            = 0;
	private int               height           = 0;
	private Graphics          bufferGraphics   = null;

	public ColorButton() {
		setPreferredSize(new Dimension(40, 22));
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		setColor(Color.black);
		addMouseListener(this);
	}

	public void setColor(Color value) {
		color = value;
		refreshImage();
	}

	public Color getColor() {
		return color;
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {
		if (!isEnabled())
			return;
		colorButtonPanel = new ColorButtonDialog();
		Color oldColor = getColor();
		colorButtonPanel.setColor(oldColor);
		colorButtonPanel.addValueChangedListener(this);
		if (!colorButtonPanel.showDialog()) {
			setColor(oldColor);
			callValueChangedListeners();
		}
	}

	public void actionValueChanged(EventObject e) {
		setColor(colorButtonPanel.getColor());
		callValueChangedListeners();
	}

	public void actionValueDragged(EventObject e) {
		setColor(colorButtonPanel.getColor());
		callValueDraggedListeners();
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

		if ((width != width2) || (height != height2)) {
			bufferImage = createImage(width2, height2);
			if (bufferImage == null)
				return null;
			bufferGraphics = bufferImage.getGraphics();
		}

		width = width2;
		height = height2;

		return bufferGraphics;
	}

	/**
	 * Redibujar el componente en el graphics temporal
	 */
	private void redrawBuffer() {
		if (getBufferGraphics() == null)
			return;

		Color newColor = color;
		if (isEnabled()) {
			Shape oldClip = getBufferGraphics().getClip();
			getBufferGraphics().setClip(1, 1, width - 2, height - 2);
			if ((color == null) || (color.getAlpha() != 255)) {
				for (int i = 0; (i * 4) <= width; i++) {
					for (int j = 0; (j * 4) <= height; j++) {
						if ((i + j) % 2 == 0)
							getBufferGraphics().setColor(Color.white);
						else
							getBufferGraphics().setColor(new Color(204, 204, 204));
						getBufferGraphics().fillRect(1 + i * 4, 1 + j * 4, 4, 4);
					}
				}
			}
			getBufferGraphics().setClip(oldClip);

			newColor = color;
		} else {
			newColor = getBackground();
		}

		if (newColor != null) {
			getBufferGraphics().setColor(newColor);
			getBufferGraphics().fillRect(1, 1, width - 2, height - 2);
		}

		if (isEnabled())
			getBufferGraphics().setColor(Color.black);
		else
			getBufferGraphics().setColor(new Color(172, 168, 153));
		getBufferGraphics().drawRect(0, 0, width - 1, height - 1);
	}

	/**
	 * Redibujar el componente en el graphics temporal y representarlo en el
	 * componente
	 */
	private void refreshImage() {
		redrawBuffer();
		if (bufferImage != null)
			getGraphics().drawImage(bufferImage, 0, 0, this);
		super.paint(getGraphics());
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

	/**
	 * Añadir un listener a la lista de eventos
	 * @param listener
	 */
	public void addValueChangedListener(ColorButtonListener listener) {
		if (!actionCommandListeners.contains(listener))
			actionCommandListeners.add(listener);
	}

	/**
	 * Borrar un listener de la lista de eventos
	 * @param listener
	 */
	public void removeValueChangedListener(ColorButtonListener listener) {
		actionCommandListeners.remove(listener);
	}

	/**
	 * Invocar a los eventos asociados al componente
	 */
	private void callValueChangedListeners() {
		Iterator<ColorButtonListener> acIterator = actionCommandListeners.iterator();
		while (acIterator.hasNext()) {
			ColorButtonListener listener = acIterator.next();
			listener.actionValueChanged(new ColorButtonEvent(this));
		}
	}

	/**
	 * Invocar a los eventos asociados al componente
	 */
	private void callValueDraggedListeners() {
		Iterator<ColorButtonListener> acIterator = actionCommandListeners.iterator();
		while (acIterator.hasNext()) {
			ColorButtonListener listener = acIterator.next();
			listener.actionValueDragged(new ColorButtonEvent(this));
		}
	}


	public void mouseClicked(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
}