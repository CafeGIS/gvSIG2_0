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
package org.gvsig.gui.beans.colorchooser;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;

import javax.swing.JPanel;

import org.gvsig.gui.beans.checkslidertext.CheckColorSliderTextContainer;
import org.gvsig.gui.beans.doubleslider.DoubleSliderEvent;
import org.gvsig.gui.beans.doubleslider.DoubleSliderListener;
/**
 *
 * @version 03/08/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class ColorChooser extends JPanel implements DoubleSliderListener {
	private static final long serialVersionUID = 2397304969793748653L;

	private ArrayList<ColorChooserListener> actionCommandListeners = new ArrayList<ColorChooserListener>();

	private CheckColorSliderTextContainer sliderRed   = null;
	private CheckColorSliderTextContainer sliderGreen = null;
	private CheckColorSliderTextContainer sliderBlue  = null;
	private CheckColorSliderTextContainer sliderAlpha = null;

	public ColorChooser() {
		initialize();
	}

	private void initialize() {
		setLayout(new GridBagLayout());
		sliderRed = new CheckColorSliderTextContainer(0, 255, 0, "R", true);
		sliderGreen = new CheckColorSliderTextContainer(0, 255, 0, "G", true);
		sliderBlue = new CheckColorSliderTextContainer(0, 255, 0, "B", true);
		sliderAlpha = new CheckColorSliderTextContainer(0, 255, 255, "A", true);
		sliderRed.setCheckboxVisible(false);
		sliderGreen.setCheckboxVisible(false);
		sliderBlue.setCheckboxVisible(false);
		sliderAlpha.setCheckboxVisible(false);
		sliderRed.addValueChangedListener(this);
		sliderGreen.addValueChangedListener(this);
		sliderBlue.addValueChangedListener(this);
		sliderAlpha.addValueChangedListener(this);
		setColor(Color.black);
		updateColors();

		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		add(sliderRed, gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		add(sliderGreen, gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		add(sliderBlue, gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		add(sliderAlpha, gridBagConstraints);
	}

	public void actionValueChanged(DoubleSliderEvent e) {
		updateColors();
		callChangeValue();
	}

	private void updateColors() {
		sliderRed.setColor1(new Color(0, sliderGreen.getValue(), sliderBlue.getValue(), sliderAlpha.getValue()), false);
		sliderRed.setColor2(new Color(255, sliderGreen.getValue(), sliderBlue.getValue(), sliderAlpha.getValue()), true);
		sliderGreen.setColor1(new Color(sliderRed.getValue(), 0, sliderBlue.getValue(), sliderAlpha.getValue()), false);
		sliderGreen.setColor2(new Color(sliderRed.getValue(), 255, sliderBlue.getValue(), sliderAlpha.getValue()), true);
		sliderBlue.setColor1(new Color(sliderRed.getValue(), sliderGreen.getValue(), 0, sliderAlpha.getValue()), false);
		sliderBlue.setColor2(new Color(sliderRed.getValue(), sliderGreen.getValue(), 255, sliderAlpha.getValue()), true);
		sliderAlpha.setColor1(new Color(sliderRed.getValue(), sliderGreen.getValue(), sliderBlue.getValue(), 0), false);
		sliderAlpha.setColor2(new Color(sliderRed.getValue(), sliderGreen.getValue(), sliderBlue.getValue(), 255), true);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.doubleslider.DoubleSliderListener#actionValueDragged(org.gvsig.gui.beans.doubleslider.DoubleSliderEvent)
	 */
	public void actionValueDragged(DoubleSliderEvent e) {
		updateColors();
		callDraggedValue();
	}

	public Color getColor() {
		return new Color(sliderRed.getValue(), sliderGreen.getValue(), sliderBlue.getValue(), sliderAlpha.getValue());
	}

	public void setColor(Color value) {
		sliderRed.setValue(value.getRed());
		sliderGreen.setValue(value.getGreen());
		sliderBlue.setValue(value.getBlue());
		sliderAlpha.setValue(value.getAlpha());
		updateColors();
	}

	/**
	 * Añadir un listener a la lista de eventos
	 * @param listener
	 */
	public void addValueChangedListener(ColorChooserListener listener) {
		if (!actionCommandListeners.contains(listener))
			actionCommandListeners.add(listener);
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setEnabled(boolean)
	 */
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		sliderRed.setEnabled(enabled);
		sliderGreen.setEnabled(enabled);
		sliderBlue.setEnabled(enabled);
		sliderAlpha.setEnabled(enabled);
	}

	/**
	 * Borrar un listener de la lista de eventos
	 * @param listener
	 */
	public void removeValueChangedListener(ColorChooserListener listener) {
		actionCommandListeners.remove(listener);
	}


	/**
	 * Dispara el evento del cambio del control
	 */
	protected void callChangeValue() {
		Iterator<ColorChooserListener> acIterator = actionCommandListeners.iterator();
		while (acIterator.hasNext()) {
			ColorChooserListener listener = acIterator.next();
			listener.actionValueChanged(new EventObject(this));
		}
	}

	/**
	 * Dispara el evento del cambio del control
	 */
	protected void callDraggedValue() {
		Iterator<ColorChooserListener> acIterator = actionCommandListeners.iterator();
		while (acIterator.hasNext()) {
			ColorChooserListener listener = acIterator.next();
			listener.actionValueDragged(new EventObject(this));
		}
	}
}