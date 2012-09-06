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

import java.util.ArrayList;
import java.util.EventObject;

import org.gvsig.gui.beans.TestUI;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanel;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelEvent;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelListener;
import org.gvsig.gui.beans.propertiespanel.ui.TestSlider;
/**
 * Test para comprobar que se puede integrar un JPanelProperty dentro del
 * PropertiesComponent
 *
 * @version 06/06/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class TestPropertiesPanel_JPanel implements ButtonsPanelListener, PropertiesComponentListener {
	private TestUI          jFrame = null;
	private PropertiesPanel pd     = null;

	public TestPropertiesPanel_JPanel() {
		initialize();
	}

	public void initialize() {
		jFrame = new TestUI("TestPropertiesPanel_JPanel");
		pd = new PropertiesPanel();

		pd.addButtonPressedListener(this);
		TestSlider ts = new TestSlider();
		pd.addValue("Protocolo", "protocolo", "", null);
		pd.addValue("Integer", "var1", new Integer(50), null);
		pd.addValue("Mi panel", "mipanel", ts, null);

		jFrame.getContentPane().add(pd);
		pd.addStateChangedListener(this);
		jFrame.pack();
		jFrame.setVisible(true);
	}

	public static void main(String[] args) {
		new TestPropertiesPanel_JPanel();
	}

	public void actionButtonPressed(ButtonsPanelEvent e) {
		if (e.getButton() == ButtonsPanel.BUTTON_ACCEPT) {
			ArrayList values = pd.getValues();
			System.out.println("-----");
			for (int i = 0; i < values.size(); i++) {
				System.out.println(((PropertyStruct)values.get(i)).getKey()
					+ ": '" + ((PropertyStruct)values.get(i)).getOldValue().toString()
					+ "', '" + ((PropertyStruct)values.get(i)).getNewValue() + "'");
			}
		}
	}

	public void actionChangeProperties(EventObject e) {
		System.out.println("Ha cambiado");
	}
}