package org.gvsig.gui.beans.defaultbuttonspanel;

/* gvSIG. Geographic Information System of the Valencian Government
 *
 * Copyright (C) 2007-2008 Infrastructures and Transports Department
 * of the Valencian Government (CIT)
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 * 
 */

import java.awt.Component;
import java.awt.Container;
import java.awt.LayoutManager;
import java.awt.Window;

import javax.swing.JPanel;

import org.gvsig.gui.beans.buttonspanel.ButtonsPanel;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelListener;
/**
 * <code>DefaultButtonsPanel</code> es un Panel que hereda de <code>JPanel</code> con
 * el añadido de poder definir una botonera por defecto.
 *
 * @version 23/04/2008
 * @author BorSanZa - Borja Sanchez Zamorano (borja.sanchez@iver.es)
 */
public class DefaultButtonsPanel extends JPanel {
	private static final long serialVersionUID = 1519536113682350563L;
	ButtonsPanel bp = null;
	JPanel content = null;

	/**
	 * Crea el <code>DefaultButtonsPanel</code> con los botones por defecto.
	 */
	public DefaultButtonsPanel() {
		super.setLayout(new java.awt.BorderLayout(0, 0));
		getButtonsPanel().addAccept();
		getButtonsPanel().addCancel();
		getButtonsPanel().addApply();
		super.add(getButtonsPanel(), java.awt.BorderLayout.SOUTH);
		super.add(getContent(), java.awt.BorderLayout.CENTER);
		this.setBorder(javax.swing.BorderFactory.createEmptyBorder(11, 11, 11, 11));
	}

	/**
	 * Crea el <code>DialogPanel</code> con los botones que definamos de la clase
	 * <code>ButtonsPanel</code>
	 *
	 * @param buttons Constante para definir que botones se crearán
	 */
	public DefaultButtonsPanel(int buttons) {
		super.setLayout(new java.awt.BorderLayout(0, 0));
		bp = new ButtonsPanel(buttons);
		super.add(getButtonsPanel(), java.awt.BorderLayout.SOUTH);
		super.add(getContent(), java.awt.BorderLayout.CENTER);
		this.setBorder(javax.swing.BorderFactory.createEmptyBorder(11, 11, 11, 11));
	}

	/**
	 * Obtener el objeto <code>ButtonsPanel</code> del <code>DialogPanel</code>.
	 * En caso de no estar creado, lo creará.
	 *
	 * @return El componente bp
	 */
	public ButtonsPanel getButtonsPanel() {
		if (bp == null)
			bp = new ButtonsPanel();
		return bp;
	}

	/**
	 * Obtener el contenido del <code>DialogPanel</code>. En caso de no estar creado,
	 * lo creará.
	 *
	 * @return El componente content
	 */
	public JPanel getContent() {
		if (content == null)
			content = new JPanel();
		return content;
	}

	public void addButtonPressedListener(ButtonsPanelListener listener) {
		getButtonsPanel().addButtonPressedListener(listener);
	}

	public void removeButtonPressedListener(ButtonsPanelListener listener) {
		getButtonsPanel().removeButtonPressedListener(listener);
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.Container#getLayout()
	 */
	public LayoutManager getLayout() {
		return getContent().getLayout();
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.Container#setLayout(java.awt.LayoutManager)
	 */
	public void setLayout(LayoutManager mgr) {
		getContent().setLayout(mgr);
	}

	/**
	 * Devuelve el Window que contiene dicha ventana o null en caso de que no sea
	 * asi
	 * @return
	 */
	public Window getWindow() {
		Container container = getParent();
		while (container != null) {
			if (container instanceof Window)
				return (Window) container;
			container = container.getParent();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.Container#add(java.awt.Component)
	 */
	public Component add(Component comp) {
		return getContent().add(comp);
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.Container#add(java.awt.Component, int)
	 */
	public Component add(Component comp, int index) {
		return getContent().add(comp, index);
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.Container#add(java.awt.Component, java.lang.Object)
	 */
	public void add(Component comp, Object constraints) {
		getContent().add(comp, constraints);
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.Container#add(java.awt.Component, java.lang.Object, int)
	 */
	public void add(Component comp, Object constraints, int index) {
		getContent().add(comp, constraints, index);
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.Container#add(java.lang.String, java.awt.Component)
	 */
	public Component add(String name, Component comp) {
		return getContent().add(name, comp);
	}
}