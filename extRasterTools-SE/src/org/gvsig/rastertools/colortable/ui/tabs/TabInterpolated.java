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
package org.gvsig.rastertools.colortable.ui.tabs;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.gvsig.gui.beans.colorbutton.ColorButton;
import org.gvsig.gui.beans.colorbutton.ColorButtonEvent;
import org.gvsig.gui.beans.colorbutton.ColorButtonListener;
import org.gvsig.gui.beans.colorslideredition.ColorSliderEdition;
import org.gvsig.gui.beans.colorslideredition.ColorSliderEvent;
import org.gvsig.gui.beans.colorslideredition.ColorSliderListener;
import org.gvsig.gui.beans.colorslideredition.ItemColorSlider;
import org.gvsig.raster.beans.previewbase.IUserPanelInterface;
import org.gvsig.raster.datastruct.ColorItem;
import org.gvsig.raster.datastruct.ColorTable;
import org.gvsig.raster.util.BasePanel;
import org.gvsig.raster.util.MathUtils;
import org.gvsig.raster.util.RasterToolsUtil;
/**
 * Pestaña para las interpolaciones de la tabla de color, aqui se tratarán
 * los gradientes de color, etc...
 *
 * @version 27/06/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class TabInterpolated extends BasePanel implements IColorTableUI, ColorSliderListener, ColorButtonListener, FocusListener, KeyListener, IUserPanelInterface {
	private static final long serialVersionUID = -5208861410196059899L;

	private ArrayList          actionCommandListeners = new ArrayList();
	private boolean            listenerEnabled        = true;
	private double             min                    = Double.POSITIVE_INFINITY;
	private double             max                    = Double.NEGATIVE_INFINITY;
	
	/**
	 * Tabla de color interna que se esta utilizando actualmente
	 */	
	private ColorTable         colorTable             = new ColorTable();

	private ColorSliderEdition colorSliderEdition     = null;
	private ColorButton        colorButton            = null;
	private JTextField         jTextClassName         = null;
	private JTextField         jTextValue             = null;
	private JLabel             jLabelColor            = null;
	private JLabel             jLabelClassName        = null;
	private JLabel             jLabelValue            = null;

	/**
	 *Inicializa componentes gráficos y traduce
	 */
	public TabInterpolated() {
		init();
		translate();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.util.BasePanel#init()
	 */
	protected void init() {
		setLayout(new GridBagLayout());

		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(5, 5, 2, 5);
		add(getColorSliderEdition(), gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.weightx = 0.5;
		gridBagConstraints.insets = new Insets(2, 5, 2, 2);
		add(getJLabelColor(), gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.weightx = 0.5;
		gridBagConstraints.insets = new Insets(2, 2, 2, 5);
		add(getColorButton(), gridBagConstraints);
		
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.insets = new Insets(2, 5, 2, 2);
		add(getJLabelClassName(), gridBagConstraints);
		
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(2, 2, 2, 5);
		add(getJTextClassName(), gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.insets = new Insets(2, 5, 2, 2);
		add(getJLabelValue(), gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(2, 2, 2, 5);
		add(getJTextValue(), gridBagConstraints);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.util.BasePanel#translate()
	 */
	protected void translate() {
	}

	private JLabel getJLabelColor() {
		if (jLabelColor == null) {
			jLabelColor = new JLabel(getText(this, "tabinterpolated_color") + ": ");
			jLabelColor.setEnabled(false);
		}
		return jLabelColor;
	}
	
	private JLabel getJLabelClassName() {
		if (jLabelClassName == null) {
			jLabelClassName = new JLabel(getText(this, "clase") + ": ");
			jLabelClassName.setEnabled(false);
		}
		return jLabelClassName;
	}

	private JLabel getJLabelValue() {
		if (jLabelValue == null) {
			jLabelValue = new JLabel(getText(this, "value") + ": ");
			jLabelValue.setEnabled(false);
		}
		return jLabelValue;
	}

	private ColorSliderEdition getColorSliderEdition() {
		if (colorSliderEdition == null) {
			colorSliderEdition = new ColorSliderEdition();
			colorSliderEdition.addValueChangedListener(this);
			colorSliderEdition.addSelectionChangedListener(this);
		}
		return colorSliderEdition;
	}

	private ColorButton getColorButton() {
		if (colorButton == null) {
			colorButton = new ColorButton();
			colorButton.addValueChangedListener(this);
		}
		return colorButton;
	}

	private JTextField getJTextClassName() {
		if (jTextClassName == null) {
			jTextClassName = new JTextField();
			jTextClassName.setEnabled(false);
			jTextClassName.addKeyListener(this);
		}
		return jTextClassName;
	}

	private JTextField getJTextValue() {
		if (jTextValue == null) {
			jTextValue = new JTextField();
			jTextValue.setEnabled(false);
			jTextValue.addKeyListener(this);
			jTextValue.addFocusListener(this);
		}
		return jTextValue;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.colorslideredition.ColorSliderListener#actionSelectionChanged(org.gvsig.gui.beans.colorslideredition.ColorSliderEvent)
	 */
	public void actionSelectionChanged(ColorSliderEvent e) {
		ItemColorSlider itemColorSlider = getColorSliderEdition().getSelectedItem();
		boolean enabled = false;
		if ((itemColorSlider != null) && (itemColorSlider.getSelected() == 1)) {
			getColorButton().setColor(itemColorSlider.getColor());
			getJTextClassName().setText(itemColorSlider.getName());
			enabled = true;
		}
		setControlsEnabled(enabled);
	}

	/**
	 * Acción que se ejecuta cuando cambia el color el usuario. Este metodo
	 * actualiza el color del ItemColorSlider segun la seleccion del usuario.
	 */
	private void updateItemSelectedToColorSlider() {
		ItemColorSlider itemColorSlider = getColorSliderEdition().getSelectedItem();
		if (itemColorSlider != null) {
			itemColorSlider.setColor(getColorButton().getColor());
			itemColorSlider.setName(getJTextClassName().getText());
			getColorSliderEdition().repaint();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.colorbutton.ColorButtonListener#actionValueDragged(org.gvsig.gui.beans.colorbutton.ColorButtonEvent)
	 */
	public void actionValueDragged(ColorButtonEvent e) {
		updateItemSelectedToColorSlider();
	}

	/**
	 * Convierte el slider de la paleta en un array de objetos para poder crear
	 * con él el objeto Palette
	 * @return
	 */
	private ArrayList getPalette() {
		double min2 = min;
		double max2 = max;

		if (min2 > max2) {
			double aux = min2;
			min2 = max2;
			max2 = aux;
		}

		ArrayList arrayList = new ArrayList();
		ArrayList items = getColorSliderEdition().getItemsShowed();

		// Añadir el minimo
		ItemColorSlider item;
		if (items.size() > 0)
			item = (ItemColorSlider) items.get(0);
		else
			item = new ItemColorSlider(0, Color.black);
		ColorItem colorItem = new ColorItem();
		colorItem.setColor(item.getColor());
		colorItem.setInterpolated(50);
		colorItem.setNameClass("");
		colorItem.setValue(min2);
		arrayList.add(colorItem);

		for (int i = 0; i < items.size(); i++) {
			item = (ItemColorSlider) items.get(i);
			colorItem = new ColorItem();
			colorItem.setColor(item.getColor());
			colorItem.setInterpolated((int) item.getInterpolated());
			colorItem.setNameClass(item.getName());

			colorItem.setValue(min2 + ((item.getValue() * (max2 - min2)) / 100.0d));

			arrayList.add(colorItem);
		}

		// Añadir el maximo
		if (items.size() > 0)
			item = (ItemColorSlider) items.get(items.size() - 1);
		else
			item = new ItemColorSlider(255, Color.white);
		colorItem = new ColorItem();
		colorItem.setColor(item.getColor());
		colorItem.setInterpolated(50);
		colorItem.setNameClass("");
		colorItem.setValue(max2);
		arrayList.add(colorItem);


		return arrayList;
	}	
	
	/**
	 * Refresco de los items del panel de color
	 * @param colorItems
	 */
	private void refreshItems() {
		colorTable.createPaletteFromColorItems(getPalette(), false);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.colorbutton.ColorButtonListener#actionValueChanged(org.gvsig.gui.beans.colorbutton.ColorButtonEvent)
	 */
	public void actionValueChanged(ColorButtonEvent e) {
		updateItemSelectedToColorSlider();
		refreshItems();
		callColorTableUIChangedListener();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.colorslideredition.ColorSliderListener#actionValueChanged(org.gvsig.gui.beans.colorslideredition.ColorSliderEvent)
	 */
	public void actionValueChanged(ColorSliderEvent e) {
		refreshItems();
		changeTextValue();
		callColorTableUIChangedListener();
	}

	private void initialState() {
		getColorSliderEdition().repaint();
		setControlsEnabled(false);
	}
	
	private void setControlsEnabled(boolean value) {
		getColorButton().setEnabled(value);
		getJLabelColor().setEnabled(value);
		getJLabelClassName().setEnabled(value);
		getJTextClassName().setEnabled(value);
		getJLabelValue().setEnabled(value);
		getJTextValue().setEnabled(value);
		if (value == false)
			getJTextClassName().setText("");
	}

	/**
	 * Carga inicial de los items del panel de color
	 * @param colorItems
	 */
	private void reloadItems() {
		ArrayList colorItems = colorTable.getColorItems();

		min = Double.POSITIVE_INFINITY;
		max = Double.NEGATIVE_INFINITY;
		for (int i = 0; i < colorItems.size(); i++) {
			ColorItem c1 = (ColorItem) colorItems.get(i);
			if (c1.getValue() > max)
				max = c1.getValue();
			if (c1.getValue() < min)
				min = c1.getValue();
		}

		clearTable();
		for (int i = 0; i < colorItems.size(); i++) {
			ColorItem c1 = (ColorItem) colorItems.get(i);
			double percent = (((c1.getValue() - min) * 100) / (max - min));

			ItemColorSlider aux = new ItemColorSlider(percent, c1.getColor());
			aux.setInterpolated(c1.getInterpolated());
			aux.setName(c1.getNameClass());

			getColorSliderEdition().addItem(aux, false);
		}
		initialState();
		if (min > max) {
			min = 0;
			max = 255;
		}
		refreshItems();
	}

	/**
	 * Borra todas las filas de la tabla.
	 */
	private void clearTable() {
		getColorSliderEdition().removeAllItems();
	}

	private void updateChangedValue() {
		double min2 = Double.POSITIVE_INFINITY;
		double max2 = Double.NEGATIVE_INFINITY;
		ArrayList list = getColorSliderEdition().getItems();
		for (int i = 0; i < list.size(); i++) {
			ItemColorSlider itemColorSlider = (ItemColorSlider) list.get(i);
			
			double value = SliderToValue(itemColorSlider.getValue());
			
			itemColorSlider.setTag(new Double(value));
			
			if (itemColorSlider.getSelected() != 1) {
				if (min2 > value)
					min2 = value;
	
				if (max2 < value)
					max2 = value;
			}
		}
		
		double value;
		try {
			value = Double.parseDouble(getJTextValue().getText());
			if (min2 > value)
				min2 = value;
			if (max2 < value)
				max2 = value;
		} catch (NumberFormatException e1) {
		}
		
		min = min2;
		max = max2;

		for (int i = 0; i < list.size(); i++) {
			ItemColorSlider itemColorSlider = (ItemColorSlider) list.get(i);
			
			value = ValueToSlider(((Double) itemColorSlider.getTag()).doubleValue());
			
			itemColorSlider.setValue(value);

			if (itemColorSlider.getSelected() == 1) {
				try {
					value = Double.parseDouble(getJTextValue().getText());
					itemColorSlider.setValue(ValueToSlider(value));
				} catch (NumberFormatException e1) {
				}
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
	 */
	public void focusLost(FocusEvent e) {
		if (e.getSource() == getJTextValue())
			updateChangedValue();

		updateItemSelectedToColorSlider();
		refreshItems();
		callColorTableUIChangedListener();
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	public void keyReleased(KeyEvent e) {
		if (e.getSource() == getJTextValue()) {
			updateChangedValue();
			updateItemSelectedToColorSlider();
			refreshItems();
			callColorTableUIChangedListener();
		}
		
		if (e.getSource() == getJTextClassName()) {
			updateItemSelectedToColorSlider();
			refreshItems();
			callColorTableUIChangedListener();
		}
	}

	private void changeTextValue() {
		if (getColorSliderEdition().getSelectedItem() != null) {
			double value = SliderToValue(getColorSliderEdition().getSelectedItem().getValue());
			getJTextValue().setText(MathUtils.format(value, 3) + "");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.colorslideredition.ColorSliderListener#actionValueDragged(org.gvsig.gui.beans.colorslideredition.ColorSliderEvent)
	 */
	public void actionValueDragged(ColorSliderEvent e) {
		changeTextValue();
	}
	
	/**
	 * Devuelve los limites de la tabla de color, minimo y maximo
	 * @return
	 */
	private double[] getLimits() {
		double min2 = min;
		double max2 = max;

		if (min2 > max2) {
			double aux = min2;
			min2 = max2;
			max2 = aux;
		}
		
		if (min2 == max2)
			max2++;

		double limits[] = {min2, max2};
		
		return limits;
	}
	
	/**
	 * Convierte un valor de la tabla de color a un porcentaje para el Slider
	 * @param value
	 * @return
	 */
	private double ValueToSlider(double value) {
		double limits[] = getLimits();
		return ((value - limits[0]) * 100.0d) / (limits[1] - limits[0]);
	}

	/**
	 * Convierte un porcentaje del slider a un valor de la tabla de color
	 * @param value
	 * @return
	 */
	private double SliderToValue(double value) {
		double limits[] = getLimits();
		return limits[0] + ((value * (limits[1] - limits[0])) / 100.0d);		
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.colortable.panels.IColorTableUI#getColorTable()
	 */
	public ColorTable getColorTable() {
		return colorTable;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.colortable.panels.IColorTableUI#setColorTable(org.gvsig.raster.datastruct.ColorTable)
	 */
	public void setColorTable(ColorTable colorTable) {
		listenerEnabled = false;
		this.colorTable = colorTable;
		reloadItems();
		getColorSliderEdition().setInterpolated(colorTable.isInterpolated());
		listenerEnabled = true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.colortable.panels.IColorTableUI#addColorTableUIChangedListener(org.gvsig.rastertools.colortable.panels.ColorTableUIListener)
	 */
	public void addColorTableUIChangedListener(ColorTableUIListener listener) {
		if (!actionCommandListeners.contains(listener))
			actionCommandListeners.add(listener);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.colortable.panels.IColorTableUI#removeColorTableUIChangedListener(org.gvsig.rastertools.colortable.panels.ColorTableUIListener)
	 */
	public void removeColorTableUIChangedListener(ColorTableUIListener listener) {
		actionCommandListeners.remove(listener);
	}

	/**
	 * Invoca el evento de cambio de un ColorTable
	 * @param colorTable
	 */
	private void callColorTableUIChangedListener() {
		if (!listenerEnabled)
			return;

		Iterator acIterator = actionCommandListeners.iterator();
		while (acIterator.hasNext()) {
			ColorTableUIListener listener = (ColorTableUIListener) acIterator.next();
			listener.actionColorTableUIChanged(this);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == 38) {
			try {
				double value = Double.parseDouble(getJTextValue().getText());
				value+=10;
				getJTextValue().setText(Double.valueOf(value).toString());
				updateChangedValue();
				updateItemSelectedToColorSlider();
				refreshItems();
				callColorTableUIChangedListener();
			} catch (NumberFormatException e1) {
			}
		}
		if (e.getKeyCode() == 40) {
			try {
				double value = Double.parseDouble(getJTextValue().getText());
				value-=10;
				getJTextValue().setText(Double.valueOf(value).toString());
				updateChangedValue();
				updateItemSelectedToColorSlider();
				refreshItems();
				callColorTableUIChangedListener();
			} catch (NumberFormatException e1) {
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.beans.previewbase.IUserPanelInterface#getTitle()
	 */
	public String getTitle() {
		return RasterToolsUtil.getText(this, "rampa");
	}
	
	public void focusGained(FocusEvent e) {}
	public void keyTyped(KeyEvent e) {}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.colortable.ui.tabs.IColorTableUI#getPanel()
	 */
	public JPanel getPanel() {
		return this;
	}
}