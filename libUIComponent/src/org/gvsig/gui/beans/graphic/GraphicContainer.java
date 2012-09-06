/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2006 IVER T.I. and Generalitat Valenciana.
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
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.gvsig.gui.beans.doubleslider.DoubleSlider;
import org.gvsig.gui.beans.doubleslider.DoubleSliderEvent;
import org.gvsig.gui.beans.doubleslider.DoubleSliderListener;
import org.gvsig.gui.beans.textincreaser.TextIncreaserEvent;
import org.gvsig.gui.beans.textincreaser.TextIncreaserListener;
/**
 * Control para el manejo de un gráfico.
 * 
 * @author Nacho Brodin (brodin_ign@gva.es)
 */
public class GraphicContainer extends JPanel implements DoubleSliderListener, TextIncreaserListener {
	private static final long serialVersionUID = -6230083498345786500L;
	private ArrayList actionCommandListeners = new ArrayList();

	private JPanel            pGeneral         = null;
	private GraphicChartPanel pGraphic         = null;
	private JPanel            panelSlider      = null;
	private BoxesPanel        pBoxes           = null;
	private DoubleSlider      doubleSlider     = null;

	private boolean           bDoCallListeners = true;
	static private int        eventId          = Integer.MIN_VALUE;

	public GraphicContainer() {
		initialize();
	}

	public GraphicContainer(boolean showSlider) {
		getPDoubleSlider().setVisible(showSlider);
		initialize();
	}


	private void initialize() {
		this.setLayout(new BorderLayout(0, 4));
		this.add(getPGraphic(), BorderLayout.CENTER);
		this.add(getPGeneral(), BorderLayout.SOUTH);
	}

	/**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPGeneral() {
		if (pGeneral == null) {
			pGeneral = new JPanel();
			pGeneral.setLayout(new BorderLayout(0, 2));
			getPDoubleSlider().setPreferredSize(new Dimension(50, 25));
			pGeneral.add(getPDoubleSlider(), BorderLayout.NORTH);
			pGeneral.add(getPBoxes(), BorderLayout.SOUTH);
		}
		return pGeneral;
	}

	/**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	public GraphicChartPanel getPGraphic() {
		if (pGraphic == null) {
			pGraphic = new GraphicChartPanel();
		}
		return pGraphic;
	}

	/**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JComponent getPDoubleSlider() {
		if (panelSlider == null) {
			panelSlider = new JPanel();
			panelSlider.setLayout(new BorderLayout());
			panelSlider.add(getDoubleSlider(), BorderLayout.CENTER);
		}
		return panelSlider;
	}
	
	/**
	 * Devuelve el componente DoubleSlider
	 * @return
	 */
	private DoubleSlider getDoubleSlider() {
		if (doubleSlider == null) {
			doubleSlider = new DoubleSlider();
			doubleSlider.addValueChangedListener(this);
		}
		return doubleSlider;
	}

	/**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private BoxesPanel getPBoxes() {
		if (pBoxes == null) {
			pBoxes = new BoxesPanel();
			pBoxes.getControlLeft().addValueChangedListener(this);
			pBoxes.getControlRight().addValueChangedListener(this);
		}
		return pBoxes;
	}

	//****************************************************
	//MÉTODOS DEL CONTROL

	public double getX1() {
		double value = getPBoxes().getControlLeft().getValue();
		
		if (value > 100)
			value = 100;

		if (value < 0)
			value = 0;

		return value;
	}

	public double getX2() {
		double value = getPBoxes().getControlRight().getValue();

		if (value > 100)
			value = 100;

		if (value < 0)
			value = 0;

		return value;
	}

	public void actionValueChanged(TextIncreaserEvent e) {
		if (e.getSource() == getPBoxes().getControlLeft()) {
			if (getPBoxes().getControlLeft().getValue() > getPBoxes().getControlRight().getValue())
				getPBoxes().getControlRight().setValue(getPBoxes().getControlLeft().getValue());
		}
		if (e.getSource() == getPBoxes().getControlRight()) {
			if (getPBoxes().getControlRight().getValue() < getPBoxes().getControlLeft().getValue())
				getPBoxes().getControlLeft().setValue(getPBoxes().getControlRight().getValue());
		}
		getDoubleSlider().setX1((int) getPBoxes().getControlLeft().getValue());
		getDoubleSlider().setX2((int) getPBoxes().getControlRight().getValue());
		callValueChangedListeners();
	}

	public void addValueChangedListener(GraphicListener listener) {
		if (!actionCommandListeners.contains(listener))
			actionCommandListeners.add(listener);
	}

	public void removeValueChangedListener(GraphicListener listener) {
		actionCommandListeners.remove(listener);
	}

	private void callValueChangedListeners() {
		if (!bDoCallListeners)
			return;
		Iterator acIterator = actionCommandListeners.iterator();
		while (acIterator.hasNext()) {
			GraphicListener listener = (GraphicListener) acIterator.next();
			listener.actionValueChanged(new GraphicEvent(this));
		}
		eventId++;
	}

	public void setBandVisible(int band, boolean visible) {
		getPGraphic().getChart().getChart().getXYPlot().getRenderer().setSeriesVisible(band, Boolean.valueOf(visible));
	}

	public void setBandColor(int band, Color color) {
		getPGraphic().getChart().getChart().getXYPlot().getRenderer().setSeriesPaint(band, color);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.doubleslider.DoubleSliderListener#actionValueChanged(java.util.EventObject)
	 */
	public void actionValueChanged(DoubleSliderEvent e) {
		getPBoxes().getControlLeft().setValue(((DoubleSlider) e.getSource()).getX1());
		getPBoxes().getControlRight().setValue(((DoubleSlider) e.getSource()).getX2());
		callValueChangedListeners();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.doubleslider.DoubleSliderListener#actionValueDragged(java.util.EventObject)
	 */
	public void actionValueDragged(DoubleSliderEvent e) {
		actionValueChanged(e);
	}
}