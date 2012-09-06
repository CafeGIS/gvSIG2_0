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
package org.gvsig.gui.beans.colorslideredition;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
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
public class ColorSliderEdition extends JComponent implements MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 663355422780987493L;

	private ArrayList<ColorSliderListener> actionCommandListeners   = new ArrayList<ColorSliderListener>();
	private ArrayList<ColorSliderListener> actionSelectionListeners = new ArrayList<ColorSliderListener>();
	private ArrayList<ItemColorSlider>     items                    = new ArrayList<ItemColorSlider>();

	private final int LEFT_PAD       = 3;
	private final int RIGHT_PAD      = 3;

	private Image     bufferImage    = null;
	private int       width          = 0;
	private int       height         = 0;
	private Graphics  bufferGraphics = null;
	private boolean   interpolated   = true;

	/**
	 * Crea un DoubleSlider con las opciones por defecto.
	 */
	public ColorSliderEdition() {
		this.setPreferredSize(new Dimension(100, 46));
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.JComponent#addNotify()
	 */
	public void addNotify() {
		super.addNotify();

		refreshImage();
	}

	public void addItem(ItemColorSlider value) {
		addItem(value, true);
	}

	public void addItem(ItemColorSlider value, boolean repaint) {
		items.add(value);
		if (repaint)
			refreshImage();
	}

	/**
	 * Devuelve un color de interpolacion entre dos colores
	 * @param value
	 * @param pos
	 * @return
	 */
	private Color interpolatedColor(ArrayList newItems, double value, int pos) {
		if (newItems.size() <= 0)
			return Color.black;

		if ((pos + 1) == newItems.size())
			return ((ItemColorSlider) newItems.get(pos)).getColor();

		if (value <= ((ItemColorSlider) newItems.get(0)).getValue())
			return ((ItemColorSlider) newItems.get(0)).getColor();

		ItemColorSlider item1 = (ItemColorSlider) newItems.get(pos);
		ItemColorSlider item2 = (ItemColorSlider) newItems.get(pos + 1);

		double percValue = ((value - item1.getValue()) * 100) / (item2.getValue() - item1.getValue());

		Color halfColor = new Color(
				(item2.getColor().getRed() + item1.getColor().getRed()) >> 1,
				(item2.getColor().getGreen() + item1.getColor().getGreen()) >> 1,
				(item2.getColor().getBlue() + item1.getColor().getBlue()) >> 1,
				(item2.getColor().getAlpha() + item1.getColor().getAlpha()) >> 1);

		Color color1, color2;
		double perc1, perc2;

		if (percValue > item2.getInterpolated()) {
			color1 = halfColor;
			color2 = item2.getColor();
			perc1 = item2.getInterpolated();
			perc2 = 100;
		} else {
			color1 = item1.getColor();
			color2 = halfColor;
			perc1 = 0;
			perc2 = item2.getInterpolated();
		}

		double percNew = (percValue - perc1) / (perc2 - perc1);

		Color newColor = new Color(
				(int) (color1.getRed() + ((color2.getRed() - color1.getRed()) * percNew)) & 0xff,
				(int) (color1.getGreen() + ((color2.getGreen() - color1.getGreen()) * percNew)) & 0xff,
				(int) (color1.getBlue() + ((color2.getBlue() - color1.getBlue()) * percNew)) & 0xff,
				(int) (color1.getAlpha() + ((color2.getAlpha() - color1.getAlpha()) * percNew)) & 0xff);


		return newColor;
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

	private void sortItems() {
		for (int i = 0; i < items.size(); i++) {
			for (int j = i + 1; j < items.size(); j++) {
				if (items.get(j).getValue() < items.get(i).getValue()) {
					ItemColorSlider aux = items.get(i);
					items.set(i, items.get(j));
					items.set(j, aux);
				}
			}
		}
	}

	private int getPosForValue(double value, ArrayList newArray) {
		int pos = 0;
		for (int j = 1; j <= newArray.size(); j++) {
			if (j < newArray.size()) {
				if (value < ((ItemColorSlider) newArray.get(j)).getValue()) {
					pos = j - 1;
					break;
				}
			} else {
				pos = j - 1;
				break;
			}
		}
		return pos;
	}

	/**
	 * Convierte a gris y aclara el color si esta inactivo el componente, en caso
	 * contrario devuelve el color tal cual
	 */
	private Color convertColor(Color value) {
		if (isEnabled())
			return value;

		int aux = (value.getRed() + value.getGreen() + value.getBlue()) / 3;

		aux = (int) ((aux * 91.0 / 255.0) + 164.0);

		return new Color(aux, aux, aux, value.getAlpha());
	}

	/**
	 * Redibujar el componente en el graphics temporal
	 */
	private void redrawBuffer() {
		if (getBufferGraphics() == null)
			return;

		sortItems();

		getBufferGraphics().setColor(this.getBackground());

		getBufferGraphics().fillRect(0, 0, width, height);

		getBufferGraphics().setColor(Color.black);
		getBufferGraphics().drawRect(LEFT_PAD, 0, width - 1 - LEFT_PAD - RIGHT_PAD, height - 18);

		Shape oldClip = getBufferGraphics().getClip();
		getBufferGraphics().setClip(LEFT_PAD + 2, 2, width - 4 - LEFT_PAD - RIGHT_PAD, height - 21);
		for (int i = 0; (i * 4 + 2) <= (width - 3 - LEFT_PAD - RIGHT_PAD); i++) {
			for (int j = 0; (j * 4 + 2) <= (height - 20); j++) {
				if ((i + j) % 2 == 0)
					getBufferGraphics().setColor(Color.white);
				else
					getBufferGraphics().setColor(new Color(204, 204, 204));
				getBufferGraphics().fillRect(i * 4 + 2 + LEFT_PAD, j * 4 + 2, 4, 4);
			}
		}
		Color newColor = Color.black;

		ArrayList<ItemColorSlider> newArray = getItemsShowed();

		for (int i = LEFT_PAD + 2; i <= width - 2 - RIGHT_PAD; i++) {
			int pos = getPosForValue(pixelToValue(i), newArray);

			if (isInterpolated()) {
				newColor = interpolatedColor(newArray, pixelToValue(i), pos);
			} else {
				if ((pos + 1) < newArray.size()) {
					double min = newArray.get(pos).getValue();
					double max = newArray.get(pos + 1).getValue();
					if ((min + ((max - min) * newArray.get(pos + 1).getInterpolated() / 100)) < pixelToValue(i))
						pos++;
				}
				if (pos < newArray.size())
					newColor = newArray.get(pos).getColor();
			}
			if (newColor != null) {
				getBufferGraphics().setColor(convertColor(newColor));
				getBufferGraphics().drawLine(i, 2, i, height - 18);
			}
		}

		getBufferGraphics().setClip(oldClip);

		boolean paintNext = false;
		for (int i = 0; i < items.size(); i++) {
			ItemColorSlider aux = items.get(i);
			if (!aux.isVisible())
				continue;
			// Dibujar los deslizadores de la interpolacion
			if (aux.getSelected() != -1)
				paintNext = true;
			if (paintNext && isEnabled()) {
				if (i != 0) {
					double value = items.get(i - 1).getValue() + ((items.get(i).getValue() - items.get(i - 1).getValue()) * items.get(i).getInterpolated() / 100);

					drawSliderInterpolation(valueToPixel(value), height - 17, aux.getColor(), (aux.getSelected() == 2));
				}
				paintNext = false;
			}
			if (aux.getSelected() == 1)
				paintNext = true;
		}

		for (int i = items.size() - 1; i >= 0; i--) {
			ItemColorSlider aux = items.get(i);
			if (!aux.isVisible())
				continue;
			drawSliderColor(valueToPixel(aux.getValue()), height - 17, convertColor(aux.getColor()), (aux.getSelected() == 1) && (isEnabled()));
		}
	}

	/**
	 * Convierte un porcentaje al valor pixel en X
	 * @param value
	 * @return
	 */
	private int valueToPixel(double value) {
		if (value < 0)
			value = 0;
		if (value > 100)
			value = 100;
		return (int) (((width - 5 - LEFT_PAD - RIGHT_PAD) * value) / 100) + 2 + LEFT_PAD;
	}

	/**
	 * Convierte un valor en X en porcentaje
	 * @param value
	 * @return
	 */
	private double pixelToValue(int value) {
		double aux = value - LEFT_PAD - 1;
		return (aux * 100.0f) / (width - LEFT_PAD - RIGHT_PAD - 4.0f);
	}

	/**
	 * Dibujar un triangulo, un triangulo es un deslizador del componente. Puedes
	 * indicarle que color tendra y en que posición estará.
	 * @param x
	 * @param color
	 */
	private void drawSliderColor(int x, int y, Color color, boolean isSelected) {
		if (color == null)
			return;
		Polygon p = new Polygon();
		p.addPoint(x, y);
		p.addPoint(x - 5, y + 6);
		p.addPoint(x - 5, y + 16);
		p.addPoint(x + 5, y + 16);
		p.addPoint(x + 5, y + 6);
		if (!isSelected) {
			getBufferGraphics().setColor(this.getBackground());
			getBufferGraphics().fillPolygon(p);
			getBufferGraphics().setColor(new Color(172, 168, 153));
			getBufferGraphics().drawLine(x + 1, y + 2, x + 4, y + 6);
			getBufferGraphics().drawLine(x - 4, y + 6, x + 4, y + 6);
		} else {
			getBufferGraphics().setColor(Color.black);
			getBufferGraphics().fillPolygon(p);
		}

		getBufferGraphics().setColor(Color.black);
		getBufferGraphics().drawPolygon(p);

		getBufferGraphics().setColor(new Color(172, 168, 153));
		getBufferGraphics().drawLine(x - 3, y + 15, x + 4, y + 15);
		getBufferGraphics().drawLine(x + 4, y + 8, x + 4, y + 15);

		getBufferGraphics().setColor(Color.white);
		getBufferGraphics().drawLine(x - 4, y + 7, x - 4, y + 15);
		getBufferGraphics().drawLine(x - 4, y + 7, x + 4, y + 7);

		getBufferGraphics().setColor(new Color(color.getRed(), color.getGreen(), color.getBlue()));
		getBufferGraphics().fillRect(x - 3, y + 8, 7, 7);
	}

	/**
	 * Dibujar un triangulo, un triangulo es un deslizador del componente. Puedes
	 * indicarle que color tendra y en que posición estará.
	 * @param x
	 * @param color
	 */
	private void drawSliderInterpolation(int x, int y, Color color, boolean isSelected) {
		Polygon p = new Polygon();
		p.addPoint(x, y);
		p.addPoint(x - 3, y + 3);
		p.addPoint(x, y + 6);
		p.addPoint(x + 3, y + 3);
		if (isSelected)
			getBufferGraphics().setColor(new Color(color.getRed(), color.getGreen(), color.getBlue()));
		else
			getBufferGraphics().setColor(this.getBackground());

		getBufferGraphics().fillPolygon(p);

		getBufferGraphics().setColor(Color.black);
		getBufferGraphics().drawPolygon(p);
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

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {
		if (!isEnabled())
			return;
		int onmask = MouseEvent.BUTTON1_DOWN_MASK;
		if ((e.getModifiersEx() & onmask) != onmask)
			return;
		onmask = MouseEvent.ALT_DOWN_MASK;
		boolean newItem = false;
		if ((e.getModifiersEx() & onmask) == onmask)
			newItem = true;

		if ((e.getY() > height) || (e.getY() < (height - 18))) {
			clearSelected();
			refreshImage();
			return;
		}

		int type = 1;
		ItemColorSlider itemSelected = getItem(e.getX(), e.getY());
		if ((itemSelected == null) || (newItem)) {
			itemSelected = getItemInterpolated(e.getX(), e.getY());
			if ((itemSelected == null) || (newItem)) {
				int pos = getPosForValue(pixelToValue(e.getX()), items);

				Color newColor = interpolatedColor(items, pixelToValue(e.getX()), pos);

				itemSelected = new ItemColorSlider(pixelToValue(e.getX()), newColor);
				items.add(itemSelected);
			} else {
				type = 2;
			}
		}
		setItemSelected(itemSelected, type);

		callSelectionChangedListeners();
		callValueChangedListeners();

		refreshImage();
	}

	private void clearSelected() {
		for (int i = items.size() - 1; i >= 0; i--)
			items.get(i).setSelected(-1);
	}

	private void setItemSelected(ItemColorSlider aux, int type) {
		clearSelected();
		aux.setSelected(type);
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {
		if (!isEnabled())
			return;
		try {
			for (int i = items.size() - 1; i >= 0; i--) {
				if (items.size() <= 2)
					return;
				if (items.get(i).isVisible() == false)
					items.remove(i);
			}
		} finally {
			callValueChangedListeners();
		}
	}

	public ItemColorSlider getSelectedItem() {
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i).getSelected() != -1)
				return items.get(i);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 */
	public void mouseDragged(MouseEvent e) {
		if (!isEnabled())
			return;
		ItemColorSlider aux = getSelectedItem();
		if (aux != null) {
			if (aux.getSelected() == 1) {
				aux.setValue(pixelToValue(e.getX()));
				refreshImage();
				setMouseCursor(e.getX(), e.getY());
				aux.setVisible((e.getY() <= height) && (e.getY() >= 0) || items.size() <= 2);
				callValueDraggedListeners();
				return;
			}
			if (aux.getSelected() == 2) {
				int pos = -1;
				for (int i = 0; i < items.size(); i++) {
					if (items.get(i) == aux)
						pos = i;
				}
				if (pos > 0) {
					double min = items.get(pos - 1).getValue();
					double max = items.get(pos).getValue();

					double newValue = 100 * (pixelToValue(e.getX()) - min) / (max - min);

					aux.setInterpolated(newValue);
					refreshImage();
					setMouseCursor(e.getX(), e.getY());
					callValueDraggedListeners();
				}
				return;
			}
		}
	}

	private ItemColorSlider getItem(int x, int y) {
		for (int i = 0; i < items.size(); i++) {
			if ((x >= (valueToPixel(items.get(i).getValue()) - 5)) &&
					(x <= (valueToPixel(items.get(i).getValue()) + 5)) &&
					(y <= height) &&
					(y >= (height - 18))) {
				return items.get(i);
			}
		}
		return null;
	}

	private ItemColorSlider getItemInterpolated(int x, int y) {
		for (int i = 1; i < items.size(); i++) {
			int value = valueToPixel(items.get(i - 1).getValue() + ((items.get(i).getValue() - items.get(i - 1).getValue()) * items.get(i).getInterpolated() / 100));

			if ((x >= value - 3) &&
					(x <= value + 3) &&
					(y <= (height - 11)) &&
					(y >= (height - 18))) {
				if (items.get(i).getSelected() != -1)
					return items.get(i);
				if (items.get(i - 1).getSelected() == 1)
					return items.get(i);
			}
		}
		return null;
	}

	private void setMouseCursor(int x, int y) {
		if (getItem(x, y) != null) {
			setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
			return;
		}
		if (getItemInterpolated(x, y) != null) {
			setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
			return;
		}
		if ((y <= height) && (y >= (height - 18))) {
			setCursor(new Cursor(Cursor.HAND_CURSOR));
			return;
		}
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		return;
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	public void mouseMoved(MouseEvent e) {
		if (!isEnabled())
			return;
		setMouseCursor(e.getX(), e.getY());
	}

	/**
	 * @return the interpolated
	 */
	public boolean isInterpolated() {
		return interpolated;
	}

	/**
	 * @param interpolated the interpolated to set
	 */
	public void setInterpolated(boolean interpolated) {
		this.interpolated = interpolated;
		refreshImage();
	}

	/**
	 * Añadir un listener a la lista de eventos
	 * @param listener
	 */
	public void addValueChangedListener(ColorSliderListener listener) {
		if (!actionCommandListeners.contains(listener))
			actionCommandListeners.add(listener);
	}

	/**
	 * Borrar un listener de la lista de eventos
	 * @param listener
	 */
	public void removeValueChangedListener(ColorSliderListener listener) {
		actionCommandListeners.remove(listener);
	}

	/**
	 * Añadir un listener a la lista de eventos
	 * @param listener
	 */
	public void addSelectionChangedListener(ColorSliderListener listener) {
		if (!actionSelectionListeners.contains(listener))
			actionSelectionListeners.add(listener);
	}

	/**
	 * Borrar un listener de la lista de eventos
	 * @param listener
	 */
	public void removeSelectionChangedListener(ColorSliderListener listener) {
		actionSelectionListeners.remove(listener);
	}

	/**
	 * Invocar a los eventos asociados al componente
	 */
	private void callSelectionChangedListeners() {
		Iterator<ColorSliderListener> acIterator = actionSelectionListeners.iterator();
		while (acIterator.hasNext()) {
			ColorSliderListener listener = acIterator.next();
			listener.actionSelectionChanged(new ColorSliderEvent(this));
		}
	}

	/**
	 * Invocar a los eventos asociados al componente
	 */
	private void callValueChangedListeners() {
		Iterator<ColorSliderListener> acIterator = actionCommandListeners.iterator();
		while (acIterator.hasNext()) {
			ColorSliderListener listener = acIterator.next();
			listener.actionValueChanged(new ColorSliderEvent(this));
		}
	}

	/**
	 * Invocar a los eventos asociados al componente
	 */
	private void callValueDraggedListeners() {
		Iterator<ColorSliderListener> acIterator = actionCommandListeners.iterator();
		while (acIterator.hasNext()) {
			ColorSliderListener listener = acIterator.next();
			listener.actionValueDragged(new ColorSliderEvent(this));
		}
	}

	public void removeAllItems() {
		items.clear();
	}

	/**
	 * @return the items
	 */
	public ArrayList getItems() {
		return items;
	}

	/**
	 * Devuelve los items que estan visibles en el componente
	 * @return the items
	 */
	public ArrayList getItemsShowed() {
		ArrayList newArray = (ArrayList) items.clone();
		for (int i = newArray.size() - 1; i >= 0; i--) {
			if (((ItemColorSlider) newArray.get(i)).isVisible() == false)
				newArray.remove(i);
		}

		return newArray;
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setEnabled(boolean)
	 */
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		refreshImage();
	}

	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
}