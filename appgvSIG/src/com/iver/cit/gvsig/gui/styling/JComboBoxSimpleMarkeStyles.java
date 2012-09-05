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
package com.iver.cit.gvsig.gui.styling;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;

import org.gvsig.fmap.mapcontext.rendering.symbols.SimpleMarkerSymbol;

import com.iver.utiles.swing.JComboBox;

/**
 * JComboBox used by the user to select the different styles of simple marker symbols.
 * The available options are: circle style,square style,cross style, diamond style,x style
 * and triangle style.
 *
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 */
public class JComboBoxSimpleMarkeStyles extends JComboBox{
	private Color symColor = Color.BLACK;
	private Color outlineColor = Color.BLACK;
	private boolean outlined = false;
	static MyItem[] pointTypes = new MyItem[] {
		new MyItem(SimpleMarkerSymbol.CIRCLE_STYLE),
		new MyItem(SimpleMarkerSymbol.SQUARE_STYLE),
		new MyItem(SimpleMarkerSymbol.CROSS_STYLE),
		new MyItem(SimpleMarkerSymbol.DIAMOND_STYLE),
		new MyItem(SimpleMarkerSymbol.X_STYLE),
		new MyItem(SimpleMarkerSymbol.TRIANGLE_STYLE),
		new MyItem(SimpleMarkerSymbol.STAR_STYLE),
	};

	/**
	 * Constructor method
	 *
	 */

	public JComboBoxSimpleMarkeStyles() {
		super();
		removeAllItems();
		for (int i = 0; i < pointTypes.length; i++) {
			addItem(pointTypes[i]);
		}

		setEditable(false);
		setRenderer(new ListCellRenderer() {

			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				SymbolPreviewer preview = new SymbolPreviewer() ;
				SimpleMarkerSymbol mySymbol = new SimpleMarkerSymbol();
				mySymbol.setColor(symColor);
				mySymbol.setOutlined(outlined);
				mySymbol.setOutlineColor(outlineColor);
				if (value instanceof MyItem) {
					mySymbol.setStyle(((MyItem) value).style);
				} else {
					mySymbol.setStyle(((Integer) value).intValue());
				}

				mySymbol.setUnit(-1); // pixel
				mySymbol.setSize(10);
				preview.setForeground(UIManager.getColor(isSelected
						? "ComboBox.selectionForeground"
								: "ComboBox.foreground"));
				preview.setBackground(UIManager.getColor(isSelected
						? "ComboBox.selectionBackground"
								: "ComboBox.background"));
				preview.setSymbol(mySymbol);
				preview.setSize(preview.getWidth(), 20);
				preview.setPreferredSize(new Dimension(preview.getWidth(), 20));
				return preview;
			}
		});
	}

	/**
	 * Establishes the color of the simple marker symbol.
	 * @param c,Color
	 */
	public void setSymbolColor(Color c) {
		this.symColor = c;
	}

	/**
	 * Sets the color for the outline of the simple marker symbol
	 * @param c,Color
	 */
	public void setOutlineColor(Color c) {
		outlined = c!=null;
		outlineColor = c;
	}

	public Object getSelectedItem() {
		return new Integer(((MyItem) super.getSelectedItem()).style);
	}

	public void setSelectedItem(Object item) {
		if (item instanceof Integer) {
			int myItem = ((Integer) item).intValue();
			for (int i = 0; i < pointTypes.length; i++) {
				if (myItem == pointTypes[i].style)
					setSelectedIndex(i);
			}
		}
		super.setSelectedItem(item);
	}
}
/**
 * Used to store the different options that the JComboBoxsimplemarkerStyles shows to
 * the user.
 *
 */
class MyItem {
	int style;

	/**
	 * Constructor method
	 * @param style
	 */
	MyItem(int style) {
		this.style = style;
	}

	public boolean equals(Object obj) {
		if (obj instanceof Integer) {
			Integer integer = (Integer) obj;
			return integer.intValue()==style;
		}

		if (obj instanceof MyItem) {
			return ((MyItem) obj).style == style;

		}
		return super.equals(obj);
	}
}
