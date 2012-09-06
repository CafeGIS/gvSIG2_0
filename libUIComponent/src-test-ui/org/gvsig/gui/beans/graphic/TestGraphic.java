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
package org.gvsig.gui.beans.graphic;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import org.gvsig.gui.beans.buttonspanel.ButtonsPanel;
import org.gvsig.gui.beans.defaultbuttonspanel.DefaultButtonsPanel;
import org.gvsig.gui.beans.table.exceptions.NotInitializeException;

public class TestGraphic implements GraphicListener {
	private JFrame 				frame=new JFrame();
	private DefaultButtonsPanel defaultButtonsPanel = null;
	private GraphicContainer	graphic = null;

	public TestGraphic() throws NotInitializeException{
		graphic = new GraphicContainer(true);


		int nSeries = 3;

		String[] names = new String[nSeries];
		int[][] series = new int[nSeries][256];

		for(int iSerie = 0; iSerie < nSeries; iSerie++){
			names[iSerie] = "Band " + iSerie;
			for (int i = 0; i < 256; i++)
				series[iSerie][i] = i * (iSerie + 1);
		}
		graphic.getPGraphic().setNewChart(series, names);


		graphic.addValueChangedListener(this);
		defaultButtonsPanel = new DefaultButtonsPanel(ButtonsPanel.BUTTONS_CLOSE);
		defaultButtonsPanel.setLayout(new BorderLayout(5, 5));
		defaultButtonsPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
		defaultButtonsPanel.add(graphic, BorderLayout.CENTER);
		frame.getContentPane().add(defaultButtonsPanel);
		frame.setSize(640, 480);
		frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		try {
			new TestGraphic();
		} catch (NotInitializeException ex){
			System.out.println("Tabla no inicializada");
		}
	}

	public void actionValueChanged(GraphicEvent e) {
		System.out.println(graphic.getX1() + ":" + graphic.getX2());
	}
}