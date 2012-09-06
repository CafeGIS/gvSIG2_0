package org.gvsig.gui.beans.filterPanel.filterQueryPanel.jLabelAsCell;

import javax.swing.JLabel;

/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */


/**
 * This class has some methods for classes which will be a JLabel as a cell
 * 
 * @author Pablo Piqueras Bartolomé (p_queras@hotmail.com)
 */
public abstract class AbstractJLabelAsCellLoaded extends JLabel {
	/**
	 * @see JLabel#JLabel()
	 */
	public AbstractJLabelAsCellLoaded() {
		super();
	}
	
	/**
	 * @see JLabel#JLabel(String)
	 */
	public AbstractJLabelAsCellLoaded(String text) {
		super(text);
	}
	
	/**
	 * Configures some characteristics 
	 */
	protected abstract void configure();
	
	/*
	 *  (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object o) {
		// It's supposed that 'o' is an string
		return this.getText().compareTo((String)o);
	}
	
	/*
	 *  (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return this.getText();
	}
}