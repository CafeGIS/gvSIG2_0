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
package org.gvsig.gui.beans.swing;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

/**
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 */
public class JComboBoxFontSizes extends JComboBox {
  private static final long serialVersionUID = 3256519971028107511L;
		private static ArrayList sizes;
    private static float DEFAULT_FONT_SIZE = 10;
    public JComboBoxFontSizes() {
        super();
        if (sizes == null) {
            sizes = new ArrayList();
            sizes.add(new Integer(5));
            sizes.add(new Integer(6));
            sizes.add(new Integer(7));
            sizes.add(new Integer(8));
            sizes.add(new Integer(9));
            sizes.add(new Integer(10));
            sizes.add(new Integer(11));
            sizes.add(new Integer(12));
            sizes.add(new Integer(13));
            sizes.add(new Integer(14));
            sizes.add(new Integer(16));
            sizes.add(new Integer(18));
            sizes.add(new Integer(20));
            sizes.add(new Integer(22));
            sizes.add(new Integer(24));
            sizes.add(new Integer(28));
            sizes.add(new Integer(36));
            sizes.add(new Integer(48));
            sizes.add(new Integer(72));
        }

        setModel(new DefaultComboBoxModel((Integer[]) sizes.toArray(new Integer[sizes.size()])));
        setSelectedIndex(7);
        setEditable(true);
        setPreferredSize(new Dimension(50, 20));
    }

    public float getSelectedValue() {
        Object v = getSelectedItem();
        if (v instanceof Integer) {
            return ((Integer) v).floatValue();
        }
        if (v instanceof String) {
            try{
                return Float.parseFloat((String) v);
            } catch (Exception e) {
                return DEFAULT_FONT_SIZE;
            }
        }
        if (v instanceof Double || v instanceof Float) {
            return ((Double) v).floatValue();
        }
        return DEFAULT_FONT_SIZE;
    }

    public void setSelectedItem(Object anObject) {
    	 if (anObject instanceof Double || anObject instanceof Float) {
              if (((Double) anObject).doubleValue() == 10) {
            	  super.setSelectedItem(new Integer(10));
            	  return;
              }
              super.setSelectedItem(anObject);
         } else if (anObject instanceof String) {
        	 setSelectedItem( new Double((String)anObject));
         } else {
        	 super.setSelectedItem(anObject);
         }
    }
}
