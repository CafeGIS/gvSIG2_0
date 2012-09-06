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
package org.gvsig.gui.beans.listview;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import org.gvsig.gui.beans.listview.painters.LargeIcon;
import org.gvsig.gui.beans.listview.painters.PaintList;
import org.gvsig.gui.beans.listview.painters.SmallIcon;
/**
 * Componente grafico para representar una lista de valores
 *
 * @version 28/06/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class ListViewComponent extends JComponent implements MouseListener, MouseMotionListener, ActionListener, KeyListener, FocusListener, AncestorListener {
	private static final long serialVersionUID = 6177600314634665863L;

	/**
	 * Lista de los tipos de vista existentes
	 */
	private ArrayList<IListViewPainter> painters = new ArrayList<IListViewPainter>();
	private ArrayList<JRadioButtonMenuItem> paintersMenu = new ArrayList<JRadioButtonMenuItem>();

	/**
	 * Lista de items
	 */
	private ArrayList<ListViewItem> items = new ArrayList<ListViewItem>();

	/**
	 * Selecciona el tipo de vista
	 */
	private int         view           = 0;

	/**
	 * Booleano para saber si se permite la multiselección
	 */
	private boolean     multiSelect    = false;

	private Image       image          = null;
	private int         width          = 0;
	private int         height         = 0;
	private Graphics2D  widgetGraphics = null;
	private JMenu       jMenu          = null;
	private ButtonGroup buttonGroup    = null;
	private JPopupMenu  jPopupMenu     = null;

	private JTextField  jRenameEdit    = null;


	private int         itemEdited     = -1;
	private int         lastSelected   = -1;
	private int         cursorPos      = -1;


	private boolean     editable       = false;

	private ArrayList<ListViewListener> actionCommandListeners = new ArrayList<ListViewListener>();

	private ListViewItem  lastSelectedItem = null;

	/**
	 * Construye un <code>ListViewComponent</code>
	 *
	 */
	public ListViewComponent() {
		setFocusable(true);

		initialize();
	}

	/**
	 * Inicializa el <code>ListViewComponent</code>
	 */
	private void initialize() {
		addListViewPainter(new PaintList(items));
		addListViewPainter(new SmallIcon(items));
		addListViewPainter(new LargeIcon(items));

		addAncestorListener(this);

		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	/**
	 * Obtiene que vista se esta usando en el componente
	 * @return
	 */
	public int getView() {
		return view;
	}

	/**
	 * Define que vista es la que se va a usar
	 * @param view
	 */
	public void setView(int view) {
		this.view = view;
	}

	/**
	 * Agrega una vista al componente
	 * @param item
	 */
	public void addListViewPainter(IListViewPainter item) {
		painters.add(item);

		JRadioButtonMenuItem jRadioButtonMenuItem = new JRadioButtonMenuItem();
		getButtonGroup().add(jRadioButtonMenuItem);

		jRadioButtonMenuItem.setText(item.getName());
		if (paintersMenu.size() == 0)
			jRadioButtonMenuItem.setSelected(true);
		getJMenu().add(jRadioButtonMenuItem);

		jRadioButtonMenuItem.addActionListener(this);

		paintersMenu.add(jRadioButtonMenuItem);
	}

	/**
	 * Agrega un item al componente
	 * @param item
	 */
	public void addItem(ListViewItem item) {
		addItem(item, false);
	}

	/**
	 * Agrega un item al componente, si acceptRepeatNames es false no se aceptaran
	 * nombres repetidos
	 * @param item
	 * @param acceptRepeatNames
	 */
	public void addItem(ListViewItem item, boolean acceptRepeatNames) {
		items.add(item);
		if (!acceptRepeatNames)
			changeName(item.getName(), items.size() - 1);

		viewItem(items.size() - 1);
	}

	/**
	 * Agrega el item en la posicion especificada de la lista.
	 * @param pos
	 * @param item
	 */
	public void addItem(int pos, ListViewItem item) {
		items.add(pos, item);
		changeName(item.getName(), pos);

		viewItem(pos);
	}

	/**
	 * Agrega un item al componente
	 * @param item
	 */
	public void removeItem(int index) {
		items.remove(index);
		repaint();
	}

	/**
	 * Borra todos los items seleccionados
	 */
	public void removeSelecteds() {
		for (int i = (items.size()-1); i>=0; i--)
			if (((ListViewItem) items.get(i)).isSelected())
				items.remove(i);

		repaint();
	}

	/**
	 * Devuelve un ArrayList con todos los items
	 * @return
	 */
	public ArrayList getItems() {
		return items;
	}

	private Graphics2D getWidgetGraphics() {
		getWidgetImage();
		return widgetGraphics;
	}

	private Image getWidgetImage() {
		int width2 = getVisibleRect().width;
		int height2 = getVisibleRect().height;
		if (width2 <= 0)
			width2 = 1;
		if (height2 <= 0)
			height2=1;

		if ((width != width2) || (height != height2)) {
			image = createImage(width2, height2);
			if (image == null)
				return null;
			widgetGraphics = (Graphics2D) image.getGraphics();
		}

		width = width2;
		height = height2;
		return image;
	}

	/**
	 * Redibujar el componente en el graphics temporal
	 */
	private void redrawBuffer() {
		if (getWidgetGraphics() == null)
			return;

		/** desactivación del anti-aliasing */
		getWidgetGraphics().setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		getWidgetGraphics().setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

		/** demanda de rendimiento rápido */
		getWidgetGraphics().setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
		getWidgetGraphics().setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
		getWidgetGraphics().setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
		getWidgetGraphics().setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);

		getWidgetGraphics().translate(-getVisibleRect().x, -getVisibleRect().y);
		getWidgetGraphics().setColor(Color.white);
		getWidgetGraphics().fillRect(getVisibleRect().x, getVisibleRect().y, getVisibleRect().width, getVisibleRect().height);

		((IListViewPainter) painters.get(view)).paint((Graphics2D) getWidgetGraphics(), getVisibleRect());
		getWidgetGraphics().translate(getVisibleRect().x, getVisibleRect().y);
	}

	public void paint(Graphics g) {
		redrawBuffer();

		if (image != null) {
			if (isEnabled()) {
				g.drawImage(image, getVisibleRect().x, getVisibleRect().y, this);
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


		Dimension size = getPreferredSize();
		Dimension aux = ((IListViewPainter) painters.get(view)).getPreferredSize();
		if (!size.equals(aux)) {
			setPreferredSize(aux);
			setSize(aux);
		}

		if (getParent() instanceof JViewport) {
			JViewport jViewport = (JViewport) getParent();
			if (jViewport.getParent() instanceof JScrollPane) {
				if (items.size() > 0)
					((JScrollPane) jViewport.getParent()).getVerticalScrollBar().setUnitIncrement(((ListViewItem) items.get(0)).getItemRectangle().height);
			}
		}
	}

	public boolean isMultiSelect() {
		return multiSelect;
	}

	public void setMultiSelect(boolean multiSelect) {
		if (multiSelect == false) {
			for (int i = 0; i<items.size(); i++)
				((ListViewItem) items.get(i)).setSelected(false);
			if ((lastSelected != -1) && (lastSelected < items.size()))
				((ListViewItem) items.get(lastSelected)).setSelected(true);
		}

		this.multiSelect = multiSelect;
		repaint();
	}

	private int getItem(int x, int y) {
		Point point = new Point(x, y);
		Rectangle rectangle = null;
		for (int i = 0; i < items.size(); i++) {
			rectangle = ((ListViewItem) items.get(i)).getItemRectangle();
			if ((rectangle != null) && (rectangle.getBounds().contains(point)))
				return i;
		}

		return -1;
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {
		if (!isEnabled())
			return;
		requestFocus();

		try {
			if ((e.getModifiers() & InputEvent.BUTTON1_MASK) != InputEvent.BUTTON1_MASK)
				return;

			cursorPos = getItem(e.getX(), e.getY());
			viewItem(cursorPos);
			if (cursorPos == -1)
				return;

			if (isMultiSelect()) {
				if ((e.getModifiers() & InputEvent.SHIFT_MASK) == InputEvent.SHIFT_MASK) {
					int pos1 = cursorPos;
					int pos2 = lastSelected;
					if (pos2 < pos1) {
						pos1 = lastSelected;
						pos2 = cursorPos;
					}

					if ((e.getModifiers() & InputEvent.CTRL_MASK) != InputEvent.CTRL_MASK)
						for (int i = 0; i < items.size(); i++)
							((ListViewItem) items.get(i)).setSelected(false);

					for (int i = pos1; i <= pos2; i++)
						((ListViewItem) items.get(i)).setSelected(true);
					return;
				}

				lastSelected = cursorPos;

				if ((e.getModifiers() & InputEvent.CTRL_MASK) == InputEvent.CTRL_MASK) {
					((ListViewItem) items.get(cursorPos)).setSelected(!((ListViewItem) items.get(cursorPos)).isSelected());
					return;
				}

				for (int i = 0; i < items.size(); i++)
					((ListViewItem) items.get(i)).setSelected(false);

				((ListViewItem) items.get(cursorPos)).setSelected(true);
			} else {
				boolean selected = true;

				lastSelected = cursorPos;

				if ((e.getModifiers() & InputEvent.CTRL_MASK) == InputEvent.CTRL_MASK)
					selected = !((ListViewItem) items.get(cursorPos)).isSelected();

				for (int i = 0; i < items.size(); i++)
					((ListViewItem) items.get(i)).setSelected(false);

				((ListViewItem) items.get(cursorPos)).setSelected(selected);
			}
		} finally {
			repaint();
		}
	}

	/**
	 * Establece que un item debe estar visible en la vista
	 * @param pos
	 */
	private void viewItem(int pos) {
		if ((pos == -1) || (items.size()==0))
			return;
		redrawBuffer();
		Dimension aux = ((IListViewPainter) painters.get(view)).getPreferredSize();
		setPreferredSize(aux);
		setSize(aux);

		if (pos < 0)
			pos = 0;

		if (pos >= items.size())
			pos = items.size() - 1;

		if (getParent() instanceof JViewport) {
			JViewport jViewport = (JViewport) getParent();

			if (jViewport.getParent() instanceof JScrollPane) {
				ListViewItem lvi = ((ListViewItem) items.get(pos));
				Rectangle rectangle = (Rectangle) lvi.getItemRectangle().clone();
				if (rectangle == null)
					return;
				rectangle.setLocation((int) rectangle.getX() - getVisibleRect().x, (int) rectangle.getY() - getVisibleRect().y);
				jViewport.scrollRectToVisible(rectangle);
				((JScrollPane)jViewport.getParent()).getVerticalScrollBar().paintImmediately(((JScrollPane)jViewport.getParent()).getVerticalScrollBar().getVisibleRect());
			}
		}
	}

	public void mouseDragged(MouseEvent e) {
		if (!isEnabled())
			return;
		if ((e.getModifiers() & InputEvent.BUTTON1_MASK) != InputEvent.BUTTON1_MASK)
			return;

		if (isMultiSelect()) {
			if ((e.getModifiers() & InputEvent.SHIFT_MASK) == InputEvent.SHIFT_MASK)
				return;
			if ((e.getModifiers() & InputEvent.CTRL_MASK) == InputEvent.CTRL_MASK)
				return;
		}

		int itemSelected = getItem(e.getX(), e.getY());

		if (itemSelected == -1)
			return;

		lastSelected = itemSelected;
		cursorPos = itemSelected;

		for (int i = 0; i<items.size(); i++)
			((ListViewItem) items.get(i)).setSelected(false);

		((ListViewItem) items.get(itemSelected)).setSelected(true);

		repaint();

		viewItem(itemSelected);
	}

	private JPopupMenu getPopupMenu() {
		if (jPopupMenu == null) {
			jPopupMenu = new JPopupMenu();
			getJMenu().setText("View");

			jPopupMenu.add(getJMenu());
		}
		return jPopupMenu;
	}

	public void mouseReleased(MouseEvent e) {
		if (!isEnabled())
			return;
		if ((e.getModifiers() & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK)
			getPopupMenu().show(this, e.getX (), e.getY () );

		if ((e.getModifiers() & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK) {
			fireSelectionValueChanged();
		}
	}

	public void mouseMoved(MouseEvent e) {
		if (!isEnabled())
			return;
		int itemSelected = getItem(e.getX(), e.getY());

		if (itemSelected == -1) {
			setToolTipText(null);
			return;
		}
		if (((ListViewItem) items.get(itemSelected)).isShowTooltip())
			setToolTipText(((ListViewItem) items.get(itemSelected)).getName());
		else
			setToolTipText(null);
	}

	private ButtonGroup getButtonGroup() {
		if (buttonGroup == null)
			buttonGroup = new ButtonGroup();
		return buttonGroup;
	}

	private JMenu getJMenu() {
		if (jMenu == null)
			jMenu = new JMenu();
		return jMenu;
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		int pos = paintersMenu.indexOf(e.getSource());
		view = pos;
		viewItem(cursorPos);
	}

	/**
	 * Returns an array of the values for the selected cells. The returned values
	 * are sorted in increasing index order.
	 *
	 * @return the selected values or an empty list if nothing is selected
	 */
	public ListViewItem[] getSelectedValues() {
		int cont = 0;
		for (int i = 0; i < items.size(); i++) {
			if (((ListViewItem) items.get(i)).isSelected())
				cont++;
		}
		ListViewItem[] values = new ListViewItem[cont];
		cont = 0;
		for (int i = 0; i < items.size(); i++) {
			if (((ListViewItem) items.get(i)).isSelected()) {
				values[cont] = (ListViewItem) items.get(i);
				cont++;
			}
		}

		return values;
	}

	/**
	 * Returns the first selected index; returns -1 if there is no selected item.
	 *
	 * @return the value of <code>getMinSelectionIndex</code>
	 */
	public int getSelectedIndex() {
		for (int i = 0; i < items.size(); i++) {
			if (((ListViewItem) items.get(i)).isSelected())
				return i;
		}
		return -1;
	}


	/**
	 * Select the index value
	 *
	 * @return the value of <code>getMinSelectionIndex</code>
	 */
	public void setSelectedIndex(int value) {
		if (value < 0)
			value = 0;
		if (value >= items.size())
			value = items.size() - 1;
		for (int i = 0; i < items.size(); i++) {
			((ListViewItem) items.get(i)).setSelected(i == value);
		}
		lastSelectedItem = getSelectedValue();
		viewItem(value);
		update(getGraphics());
	}

	/**
	 * Returns the first selected value, or <code>null</code> if the selection
	 * is empty.
	 *
	 * @return the first selected value
	 */
	public ListViewItem getSelectedValue() {
		for (int i = 0; i < items.size(); i++) {
			if (((ListViewItem) items.get(i)).isSelected())
				return (ListViewItem) items.get(i);
		}
		return null;
	}

	/**
	 * Returns an array of all of the selected indices in increasing order.
	 *
	 * @return all of the selected indices, in increasing order
	 */
	public int[] getSelectedIndices() {
		int cont = 0;
		for (int i = 0; i < items.size(); i++) {
			if (((ListViewItem) items.get(i)).isSelected())
				cont++;
		}
		int[] values = new int[cont];
		cont = 0;
		for (int i = 0; i < items.size(); i++) {
			if (((ListViewItem) items.get(i)).isSelected()) {
				values[cont] = i;
				cont++;
			}
		}

		return values;
	}

	/**
	 * Añadir un listener a la lista de eventos
	 * @param listener
	 */
	public void addListSelectionListener(ListViewListener listener) {
		if (!actionCommandListeners.contains(listener))
			actionCommandListeners.add(listener);
	}

	/**
	 * Borrar un listener de la lista de eventos
	 * @param listener
	 */
	public void removeListSelectionListener(ListViewListener listener) {
		actionCommandListeners.remove(listener);
	}

	/**
	 * Invocar a los eventos asociados al componente
	 */
	private void fireSelectionValueChanged() {
		lastSelectedItem = getSelectedValue();
		Iterator<ListViewListener> acIterator = actionCommandListeners.iterator();
		EventObject e = null;
		while (acIterator.hasNext()) {
			ListViewListener listener = acIterator.next();
			if (e == null)
				e = new EventObject(this);
			listener.actionValueChanged(e);
		}
	}

	/**
	 * Invocar a los eventos asociados al componente
	 */
	private void fireItemNameChanged(String oldName, ListViewItem item) {
		Iterator<ListViewListener> acIterator = actionCommandListeners.iterator();
		EventObject e = null;
		while (acIterator.hasNext()) {
			ListViewListener listener = acIterator.next();
			if (e == null)
				e = new EventObject(this);
			listener.actionItemNameChanged(e, oldName, item);
		}
	}
	public void renameItem(int item) {
		if (!isEditable())
			return;

		if ((item >= 0) && (item < items.size())) {
			if (((ListViewItem) items.get(item)).isSelected()) {
				Rectangle rectangle = ((ListViewItem) items.get(item)).getNameRectangle();

				if (rectangle != null) {
					itemEdited = item;
					((ListViewItem) items.get(itemEdited)).setSelected(false);
					repaint();
					this.setLayout(null);
					getJRenameEdit().setText(((ListViewItem) items.get(item)).getName());
					this.add(getJRenameEdit());
					getJRenameEdit().setBounds(rectangle);
					getJRenameEdit().addFocusListener(this);
					getJRenameEdit().addKeyListener(this);
					getJRenameEdit().requestFocus();
					getJRenameEdit().setSelectionStart(0);
					getJRenameEdit().setSelectionEnd(getJRenameEdit().getText().length());
				}
			}
		}
	}

	public JTextField getJRenameEdit() {
		if (jRenameEdit == null) {
			jRenameEdit = new JTextField();
		}
		return jRenameEdit;
 	}

	public void changeName(String newName, int pos) {
		newName = newName.trim();
		if (newName.length() == 0)
			return;
		String newNameAux = newName;
		boolean isItem;
		int newNumber = 0;
		do {
			isItem = false;
			for (int i = 0; i < items.size(); i++) {
				if ((i != pos) && (((ListViewItem) items.get(i)).getName().equals(newNameAux))) {
					isItem = true;
					newNumber++;
					newNameAux = newName + "_" + newNumber;
					break;
				}
			}
		} while (isItem);
		((ListViewItem) items.get(pos)).setName(newNameAux);
	}

	public void closeRenameEdit() {
		if (jRenameEdit == null)
			return;

		if (itemEdited != -1) {
			String oldName = ((ListViewItem) items.get(itemEdited)).getName();

			changeName(getJRenameEdit().getText(), itemEdited);

			fireItemNameChanged(oldName, (ListViewItem) items.get(itemEdited));

			((ListViewItem) items.get(cursorPos)).setSelected(true);
			itemEdited = -1;
			repaint();
		}
		this.remove(getJRenameEdit());
		jRenameEdit = null;
		this.requestFocus();
	}

	/**
	 * Mueve el cursor hacia abajo
	 */
	private void moveDown() {
		int selItem = -1;

		for (int i = 0; i < items.size(); i++) {
			if (cursorPos == i)
				continue;
			if (((ListViewItem) items.get(i)).getItemRectangle().y >= (((ListViewItem) items.get(cursorPos)).getItemRectangle().y + ((ListViewItem) items.get(cursorPos)).getItemRectangle().height)) {
				if (((ListViewItem) items.get(i)).getItemRectangle().x == ((ListViewItem) items.get(cursorPos)).getItemRectangle().x) {
					selItem = i;
					break;
				}
			}
		}

		if (selItem != -1) {
			cursorPos = selItem;
			setSelectedIndex(selItem);
		}
	}

	/**
	 * Mueve el cursor hacia la izquierda
	 */
	private void moveLeft() {
		int selItem = -1;
		for (int i = items.size() - 1; i >= 0; i--) {
			if (cursorPos == i)
				continue;
			if ((((ListViewItem) items.get(i)).getItemRectangle().x + ((ListViewItem) items.get(i)).getItemRectangle().width) <= ((ListViewItem) items.get(cursorPos)).getItemRectangle().x) {
				if (((ListViewItem) items.get(i)).getItemRectangle().y == ((ListViewItem) items.get(cursorPos)).getItemRectangle().y) {
					selItem = i;
					break;
				}
			}
		}

		if (selItem != -1) {
			cursorPos = selItem;
			setSelectedIndex(selItem);
		}
	}

	/**
	 * Mueve el cursor hacia la derecha
	 */
	private void moveRight() {
		int selItem = -1;
		for (int i = 0; i < items.size(); i++) {
			if (cursorPos == i)
				continue;
			if (((ListViewItem) items.get(i)).getItemRectangle().x >= (((ListViewItem) items.get(cursorPos)).getItemRectangle().x + ((ListViewItem) items.get(cursorPos)).getItemRectangle().width)) {
				if (((ListViewItem) items.get(i)).getItemRectangle().y == ((ListViewItem) items.get(cursorPos)).getItemRectangle().y) {
					selItem = i;
					break;
				}
			}
		}

		if (selItem != -1) {
			cursorPos = selItem;
			setSelectedIndex(selItem);
		}
	}

	/**
	 * Mueve el cursor hacia arriba
	 */
	private void moveUp() {
		int selItem = -1;
		for (int i = items.size() - 1; i >= 0; i--) {
			if (cursorPos == i)
				continue;
			if ((((ListViewItem) items.get(i)).getItemRectangle().y + ((ListViewItem) items.get(i)).getItemRectangle().height) <= ((ListViewItem) items.get(cursorPos)).getItemRectangle().y) {
				if (((ListViewItem) items.get(i)).getItemRectangle().x == ((ListViewItem) items.get(cursorPos)).getItemRectangle().x) {
					selItem = i;
					break;
				}
			}
		}

		if (selItem != -1) {
			cursorPos = selItem;
			setSelectedIndex(selItem);
		}
	}

	public void keyPressed(KeyEvent e) {
		if (e.getSource() == this) {
			switch (e.getKeyCode()) {
				case KeyEvent.VK_F2:
					renameItem(cursorPos);
					break;
				case KeyEvent.VK_DOWN:
					moveDown();
					break;
				case KeyEvent.VK_LEFT:
					moveLeft();
					break;
				case KeyEvent.VK_RIGHT:
					moveRight();
					break;
				case KeyEvent.VK_UP:
					moveUp();
					break;
				case KeyEvent.VK_HOME:
					cursorPos = 0;
					setSelectedIndex(cursorPos);
					break;
				case KeyEvent.VK_END:
					cursorPos = items.size() - 1;
					setSelectedIndex(cursorPos);
					break;
				case KeyEvent.VK_PAGE_UP:
					if (items.size() > 0) {
						int cont = (int) Math.floor(this.getVisibleRect().getHeight() / ((ListViewItem) items.get(0)).getItemRectangle().height);
						for (int i = 0; i < cont; i++)
							moveUp();
					}
					break;
				case KeyEvent.VK_PAGE_DOWN:
					if (items.size() > 0) {
						int cont = (int) Math.floor(this.getVisibleRect().getHeight() / ((ListViewItem) items.get(0)).getItemRectangle().height);
						for (int i = 0; i < cont; i++)
							moveDown();
					}
					break;
			}
			return;
		}
		if (e.getSource() == getJRenameEdit()) {
			switch (e.getKeyCode()) {
				case KeyEvent.VK_ESCAPE:
					getJRenameEdit().setText(((ListViewItem) items.get(itemEdited)).getName());
					closeRenameEdit();
					break;
				case KeyEvent.VK_ENTER:
					closeRenameEdit();
					break;
			}
		}
	}

	public void keyReleased(KeyEvent e) {
		if (e.getSource() == this) {
			switch (e.getKeyCode()) {
				case KeyEvent.VK_DOWN:
				case KeyEvent.VK_UP:
				case KeyEvent.VK_LEFT:
				case KeyEvent.VK_RIGHT:
				case KeyEvent.VK_HOME:
				case KeyEvent.VK_END:
				case KeyEvent.VK_PAGE_UP:
				case KeyEvent.VK_PAGE_DOWN:
					fireSelectionValueChanged();
					break;
			}
		}
	}

	public void focusLost(FocusEvent e) {
		closeRenameEdit();
	}

	public void mouseClicked(MouseEvent e) {
		if (!isEnabled())
			return;
		if (e.getSource() == this)
			// Si es doble click y hay algún elemento seleccionado en la lista lo eliminamos
			if (e.getClickCount() == 2) {
				renameItem(cursorPos);
			}
	}

	/**
	 * Devuelve si se puede cambiar el nombre de los items
	 * @return
	 */
	public boolean isEditable() {
		return editable;
	}

	/**
	 * Define si se puede cambiar el nombre de los items
	 * @param editable
	 */
	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	/**
	 * Quito los eventos del JScrollPane para gestionarlos yo
	 */
	public void ancestorAdded(AncestorEvent event) {
		if (getParent() instanceof JViewport) {
			JViewport jViewport = (JViewport) getParent();
			if (jViewport.getParent() instanceof JScrollPane) {
				((JScrollPane) jViewport.getParent()).setActionMap(null);
			}
		}
	}

	/**
	 * Devuelve el último item seleccionado. Solo el que provoco el evento.
	 * @return
	 */
	public ListViewItem getLastSelectedItem() {
		return lastSelectedItem;
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setEnabled(boolean)
	 */
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		update(getGraphics());
	}

	//[start] Codigo no usado
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void keyTyped(KeyEvent e) {}
	public void focusGained(FocusEvent e) {}
	public void ancestorMoved(AncestorEvent event) {}
	public void ancestorRemoved(AncestorEvent event) {}
	//[end]
}