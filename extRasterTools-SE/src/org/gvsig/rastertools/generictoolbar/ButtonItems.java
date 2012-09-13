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
package org.gvsig.rastertools.generictoolbar;

import java.util.ArrayList;

import javax.swing.JButton;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.gui.beans.controls.combobutton.ComboButton;
import org.gvsig.raster.gui.IGenericToolBarMenuItem;
/**
 * Clase para gestionar y ordenar los items de un ComboButton
 *  
 * @version 13/02/2008
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class ButtonItems {
	private ComboButton comboButton = null;
	private FLayer[] layers = null;
	private ArrayList arrayList = new ArrayList();
	private ArrayList arrayKeys = new ArrayList();

	public ButtonItems(ComboButton comboButton, FLayer[] layers) {
		this.comboButton = comboButton;
		this.layers = layers;
	}
	
	/**
	 * Añade un item al ComboButton
	 * @param menuItem
	 * @param key
	 */
	public void addButton(IGenericToolBarMenuItem menuItem, String key) {
		int pos = 0;
		boolean find = false;
		for (int i = 0; i < arrayList.size(); i++) {
			pos = i;
			IGenericToolBarMenuItem aux = (IGenericToolBarMenuItem) arrayList.get(i);
			if (aux.getGroupOrder() > menuItem.getGroupOrder()) {
				find = true;
				break;
			}
			if (aux.getGroupOrder() == menuItem.getGroupOrder()) {
				if (aux.getOrder() > menuItem.getOrder()) {
					find = true;
					break;
				}
			}
		}
		if (!find)
			pos = arrayList.size();
		arrayList.add(pos, menuItem);
		arrayKeys.add(pos, key);
	}
	
	/**
	 * Refresca los items del ComboButton
	 */
	public void refresh() {
		String actionCommand = comboButton.getActionCommand();
		comboButton.clearButtons();
		int group = -1;
		for (int i = 0; i < arrayList.size(); i++) {
			IGenericToolBarMenuItem aux = (IGenericToolBarMenuItem) arrayList.get(i);

			if ((group != -1) && (group != aux.getGroupOrder()))
				comboButton.addSeparator();

			JButton button2 = new JButton(aux.getText(), aux.getIcon());

			if (!aux.isVisible(null, layers))
				button2.setEnabled(false);

			if (!aux.isEnabled(null, layers))
				button2.setEnabled(false);
			
			button2.setActionCommand((String) arrayKeys.get(i));
			comboButton.addButton(button2);
			group = aux.getGroupOrder();
		}
		comboButton.setSelectedItem(actionCommand);
	}
}