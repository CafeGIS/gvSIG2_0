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
package org.gvsig.rastertools.vectorizacion;

import java.awt.FlowLayout;

import javax.swing.JButton;

import org.gvsig.raster.util.BasePanel;

/**
 * Panel con los controles de siguiente y anterior panel.
 * 
 * 09/06/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class NextPrevPanel extends BasePanel {
	private static final long    serialVersionUID   = 1L;
	private JButton              next               = null;
	private JButton              prev               = null;
		
	/**
	 *Inicializa componentes gráficos y traduce
	 */
	public NextPrevPanel() {
		init();
		translate();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.util.BasePanel#init()
	 */
	protected void init() {
		FlowLayout fl = new FlowLayout();
		setLayout(fl);
		add(getPrev(), fl);
		add(getNext(), fl);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.util.BasePanel#translate()
	 */
	protected void translate() {
		getNext().setText(getText(this, "next"));
		getPrev().setText(getText(this, "prev"));
	}
	
	/**
	 * Obtiene el botón de siguiente 
	 * @return JButton
	 */
	public JButton getNext() {
		if(next == null) {
			next = new JButton();
			next.setEnabled(false);
		}
		return next;
	}
		
	/**
	 * Obtiene el botón de siguiente 
	 * @return JButton
	 */
	public JButton getPrev() {
		if(prev == null) {
			prev = new JButton();
			prev.setEnabled(false);
		}
		return prev;
	}
}
