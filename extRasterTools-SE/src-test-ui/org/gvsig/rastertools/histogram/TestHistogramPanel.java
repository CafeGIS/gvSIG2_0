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
package org.gvsig.rastertools.histogram;

import java.awt.Dimension;

import org.gvsig.rastertools.TestUI;
import org.gvsig.rastertools.histogram.ui.HistogramPanel;
/**
 * Clase para poder ver la ventana de HistrogramPanel
 *
 * @version 17/04/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class TestHistogramPanel {
	private TestUI 			frame = new TestUI("TestHistogramPanel");
	private HistogramPanel	hp = null;

	public TestHistogramPanel() {
		super();
		initialize();
	}

	public static void main(String[] args){
		new TestHistogramPanel();
	}

	private void initialize() {
		hp = new HistogramPanel();
		frame.setSize(new Dimension(800, 600));

	//Parámetros de inicialización del histograma
		hp.setRGBInBandList(true); //Asignación R,G,B en el combo
		hp.firstRun(); // Mostar por primera vez el histograma

		frame.setContentPane(hp);
		frame.setResizable(true);
		frame.setVisible(true);
	}
}