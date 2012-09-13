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
package com.iver.cit.gvsig.gui.panels;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import org.gvsig.gui.beans.controls.dnd.JDnDList;

import com.iver.cit.gvsig.fmap.layers.WMSLayerNode;

/**
 * Class implementing a JList component adapted to the WMSLayerNode
 * needs (use it as a JList with drag'n'drop capabilities).
 * 
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 *
 */
public class LayerList extends JDnDList {
	public boolean showLayerNames = false;
	private int count = 0; 
	public LayerList() {
		super();
		setCellRenderer(new MyRenderer());
	}
	
	private class MyRenderer extends DefaultListCellRenderer {
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			if (value instanceof WMSLayerNode){
                WMSLayerNode layer = (WMSLayerNode) value;
                
                if (!showLayerNames) {
                	if (layer.getName() != null || layer.getName() == "") {
                		String text = layer.toString();
                		text = text.substring(text.indexOf(']')+2, text.length());
                		setText(text);
                	}
                }
                
                Dimension sz  = getPreferredSize();
                sz.setSize((sz.getWidth()+50) - (50*count), sz.getHeight());
                setPreferredSize(sz);
                count++;
            }  
			return this;
		}
	}
}
