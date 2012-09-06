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
package org.gvsig.gui.beans.controls.combobutton;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.gvsig.gui.beans.controls.IControl;
/**
 * Boton destinado a ser usado en un JToolBar que ofrece un desplegable de 
 * items de menu.
 *
 * @version 06/02/2008
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class ComboButton extends JButton implements IControl, MouseListener, MouseMotionListener, ActionListener {
	private static final long serialVersionUID = -1412453774004951410L;
	private JPopupMenu popMenu         = new JPopupMenu();
	private static int BORDER          = 6;
	private int        offsetTrianglex = -4;
	private int        offsetTriangley = -4;
	private int        triangleWidth   = 8;
	private int        triangleHeight  = 7;
	private boolean    showToolTipText = true;
	private boolean    showMenuAlways  = true;
	private boolean    alwaysMenuOnClick = false;
	
	private ArrayList<ComboButtonListener> actionCommandListeners = new ArrayList<ComboButtonListener>();
	
	/**
	 * Indica si en la siguiente agregacion de un item ha de llevar un separador
	 * previo
	 */
	private boolean nextSeparator = false;

	public ComboButton() {
		addMouseListener(this);
		addMouseMotionListener(this);
		addActionListener(this);
	}
	
	/**
	 * @deprecated Mantego el metodo para posibles compatibilidades
	 * @param mode
	 * @return
	 */
	public boolean selectMode(int mode) {
		String modeText = mode + "";

		for (int i = 0; i < popMenu.getSubElements().length; i++) {
			JMenuItem mi = (JMenuItem) popMenu.getSubElements()[i];
			// found item for mode?
			if (mi.getActionCommand().equals(modeText)) {
				selectItem(mi);
				return true;
			}
		}
		return false;
	}

	private void selectItem(JMenuItem mi) {
		setIcon(mi.getIcon());
		setToolTipText(mi.getText());
		setActionCommand(mi.getActionCommand());
//		requestFocus();
	}
	
	public void setSelectedItem(String actionCommand) {
		for (int i = 0; i < popMenu.getSubElements().length; i++) {
			if (((JMenuItem) popMenu.getSubElements()[i].getComponent()).getActionCommand().equals(actionCommand)) {
				selectItem((JMenuItem) popMenu.getSubElements()[i].getComponent());
				break;
			}
		}
//		popMenu.requestFocus();
	}

	/**
	 * Borra todos los items de la lista desplegable
	 */
	public void clearButtons() {
		popMenu.removeAll();
	}
	
	/**
	 * Añade un JMenuItem al menu desplegable
	 * @param menu
	 */
	public void addButton(JButton menu) {
		JMenuItem mi = new JMenuItem();
		mi.setText(menu.getText());
		mi.setIcon(menu.getIcon());
		mi.setEnabled(menu.isEnabled());
		mi.setActionCommand(menu.getActionCommand());
		mi.addActionListener(this);
		
		if (nextSeparator) {
			popMenu.addSeparator();
			nextSeparator = false;
		}

		popMenu.add(mi);

		if (popMenu.getSubElements().length == 1) {
			// init tbutton
			setIcon(menu.getIcon());
			setActionCommand(menu.getActionCommand());
			setToolTipText(menu.getText());
		}
	}

	/**
	 * Indica que en la siguiente agregación de un item al menu ha de llevar un
	 * separador
	 */
	public void addSeparator() {
		nextSeparator = true;
	}

	/**
	 * Muestra/Oculta el menu
	 * @param flag
	 */
	public void setPopupVisible(boolean flag) {
		if (flag) {
			if (popMenu.isShowing())
				return;
			popMenu.show(this, 0, getHeight());
		} else {
			popMenu.setVisible(false);
		}
	}

	public void setAction(ActionEvent action) {
		for (int i = 0; i < popMenu.getSubElements().length; i++) {
			if (((JMenuItem) popMenu.getSubElements()[i].getComponent()).getActionCommand().equals(action.getActionCommand())) {
				((JMenuItem) popMenu.getSubElements()[i].getComponent()).doClick();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.controls.IControl#setValue(java.lang.Object)
	 */
	public Object setValue(Object value) {
		return value;
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		for (int i = 0; i < popMenu.getSubElements().length; i++) {
			if (e.getSource() instanceof JMenuItem && popMenu.getSubElements()[i] == e.getSource()) {
				selectItem((JMenuItem) e.getSource());
				callComboButtonClickedListeners();
				return;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g) {
		super.paint(g);

		Graphics2D g2 = (Graphics2D) g;
		if (showMenuAlways || popMenu.getSubElements().length > 1) {
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			// draw little arrow (for popup menu)
			drawTriangle(g2);
		}
	}

	private void drawTriangle(Graphics2D g2) {
		GeneralPath gp = new GeneralPath();

		int x = 0;
		if (offsetTrianglex < 0)
			x = this.getWidth() + offsetTrianglex - BORDER;
		else
			x = offsetTrianglex + BORDER;

		int y = 0;
		if (offsetTriangley < 0)
			y = this.getHeight() + offsetTriangley - BORDER;
		else
			y = offsetTriangley + BORDER;

		gp.moveTo(x, y);
		gp.lineTo(x + triangleWidth, y);
		gp.lineTo(x + (triangleWidth / 2), y + triangleHeight);
		gp.closePath();

		g2.setColor(Color.white);
		g2.fill(gp);
		g2.setColor(new Color(0, 0, 0, 130));
		g2.draw(gp);
	}

	private boolean popupTriangleClicked(int x, int y) {
		if (alwaysMenuOnClick)
			return true;

		if (!showMenuAlways && (popMenu.getSubElements().length <= 1))
			return false;

		if (offsetTrianglex < 0) {
			if (x < (this.getWidth() - BORDER + offsetTrianglex))
				return false;
		} else {
			if (x > (BORDER + offsetTrianglex + triangleWidth))
				return false;
		}
	
		if (offsetTriangley < 0) {
			if (y < (this.getHeight() - BORDER + offsetTriangley))
				return false;
		} else {
			if (y > (BORDER + offsetTriangley + triangleHeight))
				return false;
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {
		boolean showPopup = popupTriangleClicked(e.getX(), e.getY());
		if (showPopup)
			setPopupVisible(showPopup);
		else
			callComboButtonClickedListeners();
	}
	
	/**
	 * Añadir un listener a la lista de eventos
	 * @param listener
	 */
	public void addComboButtonClickedListener(ComboButtonListener listener) {
		if (!actionCommandListeners.contains(listener))
			actionCommandListeners.add(listener);
	}

	/**
	 * Borrar un listener de la lista de eventos
	 * @param listener
	 */
	public void removeComboButtonClickedListener(ComboButtonListener listener) {
		actionCommandListeners.remove(listener);
	}
	
	/**
	 * Invocar a los eventos asociados al componente
	 */
	private void callComboButtonClickedListeners() {
		Iterator<ComboButtonListener> acIterator = actionCommandListeners.iterator();
		while (acIterator.hasNext()) {
			ComboButtonListener listener = acIterator.next();
			listener.actionComboButtonClicked(new ComboButtonEvent(this));
		}
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.JComponent#getToolTipText()
	 */
	public String getToolTipText() {
		if (showToolTipText)
			return super.getToolTipText();
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 */
	public void mouseDragged(MouseEvent e) {
		if (popupTriangleClicked(e.getX(), e.getY()))
			setPopupVisible(true);
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	public void mouseMoved(MouseEvent e) {
		showToolTipText = !popMenu.isShowing();
	}

	/**
	 * Devuelve si esta visible siempre
	 * @return the showMenuAlways
	 */
	public boolean isShowMenuAlways() {
		return showMenuAlways;
	}

	/**
	 * Especifica si el menu se ha de visualizar siempre o solo cuando haya mas de
	 * un item
	 * @param showMenuAlways the showMenuAlways to set
	 */
	public void setShowMenuAlways(boolean showMenuAlways) {
		this.showMenuAlways = showMenuAlways;
	}
	
	public void mouseReleased(MouseEvent e) {
		
	}
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}

	/**
	 * @return the alwaysMenuOnClick
	 */
	public boolean isAlwaysMenuOnClick() {
		return alwaysMenuOnClick;
	}

	/**
	 * @param alwaysMenuOnClick the alwaysMenuOnClick to set
	 */
	public void setAlwaysMenuOnClick(boolean alwaysMenuOnClick) {
		this.alwaysMenuOnClick = alwaysMenuOnClick;
	}
}