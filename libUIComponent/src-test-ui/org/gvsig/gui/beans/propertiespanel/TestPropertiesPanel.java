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
package org.gvsig.gui.beans.propertiespanel;

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.EventObject;

import org.gvsig.gui.beans.TestUI;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanel;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelEvent;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelListener;

public class TestPropertiesPanel implements ButtonsPanelListener, PropertiesComponentListener {
	TestUI frame = null;
	PropertiesPanel pd = null;

	public TestPropertiesPanel() {
		initialize();
	}

	public void initialize() {
		Toolkit.getDefaultToolkit().setDynamicLayout(true);
		frame = new TestUI("TestPropertiesPanel");
		pd = new PropertiesPanel();

		pd.addButtonPressedListener(this);
		pd.addValue("Protocolo", "protocolo", "", null);
		pd.addValue("Integer", "var1", new Integer(50), null);
		{
			Object[] types = {new Integer(PropertiesComponent.TYPE_SLIDER), new Integer(-255), new Integer(255)};
			pd.addValue("Slider", "slider1", new Integer(25), types); // Slider
			Object[] types2 = {new Integer(PropertiesComponent.TYPE_SLIDER), new Integer(0), new Integer(50)};
			pd.addValue("Slider", "slider2", new Integer(25), types2); // Slider
			Object[] types3 = {new Integer(PropertiesComponent.TYPE_SLIDER), new Integer(0), new Integer(100)};
			pd.addValue("Slider", "slider3", new Double(25), types3); // Slider
		}
		pd.addValue("Activo", "check1", new Boolean(true), null); // Slider
		{
			ArrayList lista = new ArrayList();
			lista.add("Primer elemento");
			lista.add("Segundo elemento");
			lista.add("Tercer elemento");
			PropertyStruct property = new PropertyStruct();
			property.setTextLabel("Combo");
			property.setKey("combo1");
			property.setOldValue(new Integer(1));
			Object[] types = {new Integer(PropertiesComponent.TYPE_COMBO), lista};
			property.setExtras(types);
			pd.addPropertyStruct(property);
		}
		frame.getContentPane().add(pd);
		frame.pack();
		pd.addStateChangedListener(this);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		new TestPropertiesPanel();
	}

	public void actionButtonPressed(ButtonsPanelEvent e) {
		if (e.getButton() == ButtonsPanel.BUTTON_ACCEPT) {
			ArrayList values = pd.getValues();
			System.out.println("-----");
			for (int i=0; i<values.size(); i++) {
				System.out.println(((PropertyStruct)values.get(i)).getKey()
						+ ": '" + ((PropertyStruct)values.get(i)).getOldValue().toString()
						+ "', '" + ((PropertyStruct)values.get(i)).getNewValue().toString() + "'");
				System.out.println(pd.getComponentUI(((PropertyStruct)values.get(i)).getKey()).toString());
			}
		}
	}

	public void actionChangeProperties(EventObject e) {
		System.out.println("Ha cambiado");
		System.out.println(pd.getProperties().get("slider3"));
	}
}