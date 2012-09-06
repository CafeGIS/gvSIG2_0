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
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.gvsig.gui.beans.slidertext.listeners.SliderEvent;
import org.gvsig.gui.beans.slidertext.listeners.SliderListener;
/**
 * Barra de deslizamiento con una ventana de texto que tiene el valor de la
 * posición de la barra. En este control podrá controlarse mediante la entrada
 * de datos por la caja de texto la posibilidad de introducir valores decimales.
 *
 * Nacho Brodin (nachobrodin@gmail.com)
 */
public class SliderTextContainer extends JPanel implements ChangeListener, FocusListener, KeyListener, MouseListener {
	private static final long serialVersionUID = 1876415954410511634L;
	private ArrayList<SliderListener> actionCommandListeners = new ArrayList<SliderListener>();
	private JPanel     pText           = null;
	private JSlider    slider          = null;
	private JTextField text            = null;
	private int        min             = 0;
	private int        max             = 255;
	private double     interval        = 1.0;
	private int        defaultPos      = 0;
	private boolean    decimal         = false;
	private boolean    disconnectEvent = false;
	private boolean    dragged         = false;
	private boolean    showBorder      = false;

	/**
	 * Contructor
	 * @param min Valor mínimo de la barra
	 * @param max Valor máximo de la barra
	 * @param defaultPos Posición por defecto
	 * @deprecated Es recomendable el uso del constructor con el parámetro border. Tiene la misma funcionalidad 
	 * si se pone ese parámetro a true. Se mantiene este constructor por compatibilidad con versiones 
	 * anteriores.
	 */
	public SliderTextContainer(int min, int max, int defaultPos) {
		this(min, max, defaultPos, false);
	}
	
	/**
	 * Contructor
	 * @param min Valor mínimo de la barra
	 * @param max Valor máximo de la barra
	 * @param defaultPos Posición por defecto
	 * @param border Flag que obliga a mostrar u ocultar el borde
	 */
	public SliderTextContainer(int min, int max, int defaultPos, boolean border) {
		super();
		this.min = min;
		this.max = max;
		this.defaultPos = defaultPos;
		this.showBorder = border;
		initialize();
	}


	/**
	 * Constructor vacio
	 */
	public SliderTextContainer() {
		this(0, 100, 0);
	}

	/**
	 * Dispara el evento del cambio del control
	 */
	protected void callChangeValue(boolean forceEvent) {
		if (!getSlider().isEnabled() && !forceEvent)
			return;
		Iterator<SliderListener> acIterator = actionCommandListeners.iterator();
		while (acIterator.hasNext()) {
			SliderListener listener = (SliderListener) acIterator.next();
			listener.actionValueChanged(new SliderEvent(this));
		}
	}

	/**
	 * Dispara el evento del cambio del control
	 */
	protected void callDraggedValue() {
		Iterator<SliderListener> acIterator = actionCommandListeners.iterator();
		while (acIterator.hasNext()) {
			SliderListener listener = (SliderListener) acIterator.next();
			listener.actionValueDragged(new SliderEvent(this));
		}
	}

	/**
	 * Añadir un listener a la lista de eventos
	 * @param listener
	 */
	public void addValueChangedListener(SliderListener listener) {
		if (!actionCommandListeners.contains(listener))
			actionCommandListeners.add(listener);
	}

	/**
	 * Borrar un listener de la lista de eventos
	 * @param listener
	 */
	public void removeValueChangedListener(SliderListener listener) {
		actionCommandListeners.remove(listener);
	}

	/**
	 * Asigna un borde al componente con el texto pasado como
	 * parámetro
	 * @param name
	 */
	public void setBorder(String name){
		setBorder(BorderFactory.createTitledBorder(null, name, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
	}

	/**
	 * This method initializes this
	 *
	 */
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.add(getSlider(), BorderLayout.CENTER);
		this.add(getPText(), BorderLayout.EAST);
		if(showBorder)
			this.setBorder(BorderFactory.createLineBorder(java.awt.Color.gray,1));
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
			pText.add(getTextField(), gridBagConstraints1);
		}
		return pText;
	}

	/**
	 * This method initializes jSlider
	 *
	 * @return javax.swing.JSlider
	 */
	public JSlider getSlider() {
		if (slider == null) {
			slider = new JSlider();
			slider.setMinimum(0);
			slider.setMaximum((int) ((max - min) / interval));
			slider.setValue((int) ((defaultPos - min) / interval));
			updateTicks();
			slider.addChangeListener(this);
			slider.addMouseListener(this);
		}
		return slider;
	}

	/**
	 * This method initializes jTextField
	 *
	 * @return javax.swing.JTextField
	 */
	public JTextField getTextField() {
		if (text == null) {
			text = new JTextField();
			text.setText(defaultPos + "");
			text.setPreferredSize(new Dimension(40, 26));
			text.setMinimumSize(new Dimension(40, 26));
			text.addFocusListener(this);
			text.addKeyListener(this);
		}
		return text;
	}

	public void setComponentSize(int w, int h){
	}

	/**
	 * Obtiene el valor del control.
	 * @return Valor del control en formato double.
	 */
	public double getValue() {
		return new Double(getTextField().getText()).doubleValue();
	}

	/**
	 * Asigna el valor del control.
	 * @return Valor del control en formato double.
	 */
	public void setValue(double value){
		if(decimal)
			getTextField().setText(String.valueOf(value));
		else
			getTextField().setText(String.valueOf((int)value));
		getSlider().setValue((int) ((value - min) / interval));
	}

	/**
	 * Activa o desactiva el control del panel
	 * @param active
	 */
	public void setControlEnabled(boolean active){
		getSlider().setEnabled(active);
		getTextField().setEnabled(active);
		if (active == false) {
			getTextField().setBackground(pText.getBackground());
		} else {
			getTextField().setBackground(java.awt.Color.white);
		}
	}

	/**
	 * Asigna el flag que dice si el valor del campo de texto será
	 * decimal o entero
	 * @param dec true si se admiten valores decimales y false si no se admiten
	 */
	public void setDecimal (boolean dec){
		decimal = dec;
		String s = getTextField().getText();
		if(dec)
			getTextField().setText((s +".0"));
		else{
			int index = s.lastIndexOf(".");
			if(index == -1 || index == 0)
				index = s.length() - 1;
			getTextField().setText(s.substring(0, index + 1));
		}
	}

	/**
	 * Obtiene el flag que dice si el valor del campo de texto es
	 * decimal o entero
	 * @return true si se admiten valores decimales y false si no se admiten
	 */
	public boolean getDecimal (){
		return decimal;
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

	/**
	 * Actualizar la separacion entre los Ticks
	 */
	private void updateTicks() {
		int ticks = (int) ((max-min)/interval) / 40;
		getSlider().setMajorTickSpacing(ticks * 4);
		slider.setPaintTicks(true);
		getSlider().setMinorTickSpacing(ticks);
	}

	private void updateInterval() {
		double aux = this.getValue();
		getSlider().setMinimum(0);
		getSlider().setMaximum((int) ((max - min) / interval));
		setValue(aux);
		updateTicks();
	}
	/**
	 * Definir un intervalo para el slider
	 * @param value
	 */
	public void setInterval(double value) {
		interval = value;
		updateInterval();
	}

	/**
	 * Obtener el intervalo definido para el slider
	 * @return
	 */
	public double getInterval() {
		return interval;
	}

	/**
	 * Controla que si el formato introducido en el textfield es numerico. Si lo
	 * es se actualiza la posición del slider
	 */
	private void checkDecimalTextAndUpdateSlider() {
		String text = getTextField().getText();
		double value = 0;
		disconnectEvent = true;

		try {
			if (!text.equals("")) {
				value = (Double.parseDouble(text) * 100) / (double) (getMax() - getMin());
				value = Double.valueOf(text).doubleValue();
				if (value < getMin())
					value = getMin();
				if (value > getMax())
					value = getMax();
			}
			setValue(value);
			callChangeValue(true);
		} catch (NumberFormatException exc) {
			setValue(getSlider().getValue()*interval + min);
		}
	}

	/**
	 * Control del evento de cambio en la posición del slider. Este cambio
	 * actualiza el valor de la caja de texto y ejecuta el método stateChanged de
	 * los listener registrados.
	 */
	public void stateChanged(ChangeEvent e) {
		//Modificamos la caja de texto con el valor de la posición del slider
		if (!disconnectEvent) {
			double value = (getSlider().getValue() * getInterval()) + getMin();
			if (!getDecimal())
				getTextField().setText(((int) value) + "");
			else
				getTextField().setText(((int) (value * 10)) / 10.0 + "");
			if (dragged)
				callDraggedValue();
			else
				callChangeValue(true);
		}
		disconnectEvent = false;
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
	 */
	public void focusLost(FocusEvent e) {
		checkDecimalTextAndUpdateSlider();
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == 10)
			checkDecimalTextAndUpdateSlider();
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {
		dragged = true;
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {
		dragged = false;
		callChangeValue(false);
	}

	public void focusGained(FocusEvent e) {}
	public void keyPressed(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
}