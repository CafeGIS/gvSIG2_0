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
package org.gvsig.gui.beans.slidertext;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.gvsig.gui.beans.doubleslider.DoubleSlider;
import org.gvsig.gui.beans.doubleslider.DoubleSliderEvent;
import org.gvsig.gui.beans.doubleslider.DoubleSliderListener;
/**
 * Barra de deslizamiento con una ventana de texto que tiene el valor de la
 * posición de la barra. En este control podrá controlarse mediante la entrada
 * de datos por la caja de texto la posibilidad de introducir valores decimales.
 *
 * @version 15/06/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class ColorSliderTextContainer extends JPanel implements ChangeListener, DoubleSliderListener {
	private static final long serialVersionUID = 1876415954410511634L;
	private ArrayList<DoubleSliderListener> actionCommandListeners = new ArrayList<DoubleSliderListener>();

	private JPanel       pText            = null;
	private DoubleSlider slider           = null;
	private JSpinner     jSpinner         = null;
	private int          min              = 0;
	private int          max              = 255;
	private int          defaultPos       = 0;

	/**
	 * Contructor
	 * @param min Valor mínimo de la barra
	 * @param max Valor máximo de la barra
	 * @param defaultPos Posición por defecto
	 */
	public ColorSliderTextContainer(int min, int max, int defaultPos) {
		super();
		this.min = min;
		this.max = max;
		this.defaultPos = defaultPos;

		initialize();
		slider.setTwoSliders(false);
	}

	/**
	 * Constructor vacio
	 */
	public ColorSliderTextContainer() {
		this(0, 100, 0);
	}

	/**
	 * This method initializes this
	 *
	 */
	private void initialize() {
		this.setLayout(new BorderLayout(5, 5));
		this.add(getSlider(), BorderLayout.CENTER);
		this.add(getPText(), BorderLayout.EAST);
	}

	/**
	 * This method initializes jPanel1
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getPText() {
		if (pText == null) {
			pText = new JPanel();
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.insets = new java.awt.Insets(0, 10, 8, 0);
			pText.setLayout(new GridBagLayout());
			pText.add(getJSpinner(), gridBagConstraints1);
		}
		return pText;
	}

	/**
	 * This method initializes jSlider
	 *
	 * @return javax.swing.JSlider
	 */
	public DoubleSlider getSlider() {
		if (slider == null) {
			slider = new DoubleSlider();
			slider.setMinimum(min);
			slider.setMaximum(max);
			slider.setValue(defaultPos);
			slider.addValueChangedListener(this);
		}
		return slider;
	}

	/**
	 * This method initializes jTextField
	 *
	 * @return javax.swing.JTextField
	 */
	public JSpinner getJSpinner() {
		if (jSpinner == null) {
			jSpinner = new JSpinner();
			jSpinner.setValue(new Integer(defaultPos));
			jSpinner.setPreferredSize(new Dimension(50, 26));
			jSpinner.setMinimumSize(new Dimension(50, 26));
			jSpinner.addChangeListener(this);
		}
		return jSpinner;
	}

	public void setComponentSize(int w, int h){
	}

	/**
	 * Obtiene el valor del control.
	 * @return Valor del control en formato double.
	 */
	public int getValue() {
		return new Integer(getJSpinner().getValue() + "").intValue();
	}

	/**
	 * Asigna el valor del control.
	 * @return Valor del control en formato double.
	 */
	public void setValue(int value) {
		getJSpinner().setValue(new Integer(value));
		getSlider().setValue(value);
	}

	/**
	 * Activa o desactiva el control del panel
	 * @param active
	 */
	public void setControlEnabled(boolean active){
		getSlider().setEnabled(active);
		getJSpinner().setEnabled(active);
	}

	/**
	 * Obtiene el valor máximo del slider
	 * @return Entero con el valor máximo
	 */
	public int getMax() {
		return max;
	}

	/**
	 * Asigna el valor máximo del slider
	 * @param Entero con el valor máximo
	 * @deprecated Usar setMaximum en su lugar
	 */
	public void setMax(int max) {
		this.setMaximum(max);
	}

	/**
	 * Asigna el valor máximo del slider
	 * @param Entero con el valor máximo
	 */
	public void setMaximum(int max) {
		this.max = max;
		updateInterval();
	}

	/**
	 * Obtiene el valor mínimo del slider
	 * @return Entero con el valor mínimo
	 */
	public int getMin() {
		return min;
	}

	/**
	 * Asigna el valor mínimo del slider
	 * @param Entero con el valor mínimo
	 * @deprecated Usar setMinimum
	 */
	public void setMin(int min) {
		this.setMinimum(min);
	}

	/**
	 * Asigna el valor mínimo del slider
	 * @param Entero con el valor mínimo
	 */
	public void setMinimum(int min) {
		this.min = min;
		updateInterval();
	}

	private void updateInterval() {
		int aux = this.getValue();
		getSlider().setMinimum(min);
		getSlider().setMaximum(max);
		setValue(aux);
	}

	/**
	 * Especificar el color izquierdo del control
	 * @param color
	 */
	public void setColor1(Color color, boolean refresh) {
		slider.setColor1(color, refresh);
	}

	/**
	 * Especificar el color derecho del control
	 * @param color
	 */
	public void setColor2(Color color, boolean refresh) {
		slider.setColor2(color, refresh);
	}

	/**
	 * Controla cuando cambia el spinner
	 */
	public void stateChanged(ChangeEvent e) {
		int value = new Integer(getJSpinner().getValue().toString()).intValue();
		if (value != getSlider().getValue())
			getSlider().setValue(value);
		if (new Integer(getJSpinner().getValue().toString()).intValue() != getSlider().getValue())
			getJSpinner().setValue(new Integer(getSlider().getValue()));

		if (spinnerEvent)
			callChangeValue();
	}

	/**
	 * Dispara el evento del cambio del control
	 */
	protected void callChangeValue() {
		Iterator<DoubleSliderListener> iterator = actionCommandListeners.iterator();
		while (iterator.hasNext()) {
			DoubleSliderListener listener = iterator.next();
			listener.actionValueChanged(new DoubleSliderEvent(this));
		}
	}

	/**
	 * Dispara el evento del cambio del control
	 */
	protected void callDraggedValue() {
		Iterator<DoubleSliderListener> iterator = actionCommandListeners.iterator();
		while (iterator.hasNext()) {
			DoubleSliderListener listener = iterator.next();
			listener.actionValueDragged(new DoubleSliderEvent(this));
		}
	}

	/**
	 * Añadir un listener a la lista de eventos
	 * @param listener
	 */
	public void addValueChangedListener(DoubleSliderListener listener) {
		if (!actionCommandListeners.contains(listener))
			actionCommandListeners.add(listener);
	}

	/**
	 * Borrar un listener de la lista de eventos
	 * @param listener
	 */
	public void removeValueChangedListener(DoubleSliderListener listener) {
		actionCommandListeners.remove(listener);
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setEnabled(boolean)
	 */
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		jSpinner.setEnabled(enabled);
		slider.setEnabled(enabled);
	}

	/**
	 * Controla cuando cambia el slider
	 */
	public void actionValueChanged(DoubleSliderEvent e) {
		int value = getSlider().getValue();
		getJSpinner().setValue(new Integer(value));

		if (isEnabled())
			callChangeValue();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.doubleslider.DoubleSliderListener#actionValueDragged(java.util.EventObject)
	 */
	boolean spinnerEvent = true;
	int lastDragged = -1;
	public void actionValueDragged(DoubleSliderEvent e) {
		int value = getSlider().getValue();
		if (lastDragged != value) {
			lastDragged = value;
			spinnerEvent = false;
			getJSpinner().setValue(new Integer(value));
			spinnerEvent = true;

			if (isEnabled())
				callDraggedValue();
		}
	}
}