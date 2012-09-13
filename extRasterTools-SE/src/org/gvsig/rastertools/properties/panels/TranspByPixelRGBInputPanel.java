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
package org.gvsig.rastertools.properties.panels;

import java.awt.Color;

import javax.swing.JPanel;

import org.gvsig.gui.beans.checkslidertext.CheckColorSliderTextContainer;
import org.gvsig.gui.beans.doubleslider.DoubleSliderEvent;
import org.gvsig.gui.beans.doubleslider.DoubleSliderListener;
/**
 * Panel con los 3 campos para introducir listas de valores RGB. Este gestiona
 * los eventos de FocusListener para validar que los valores que se introducen
 * son correctos. Si el TextField pierde el foco y tiene un valor incorrecto
 * resetea el contenido. Los métodos publicos getRangeXXX devuelven el intervalo
 * de valores en forma de array multidimensional de 2xN elementos. Cada pareja
 * de valores es un rango de color en ese valor de pixel. Un rango de valores
 * podria ser por ejemplo {{1,3},{15,30},{200, 255}}
 *
 * @author Nacho Brodin (brodin_ign@gva.es)
 */
public class TranspByPixelRGBInputPanel extends JPanel implements DoubleSliderListener {
	private static final long     serialVersionUID = 5858119425941331458L;
	private int[]                 rangeRed   = null;
	private int[]                 rangeGreen = null;
	private int[]                 rangeBlue  = null;
	CheckColorSliderTextContainer tRed       = null;
	CheckColorSliderTextContainer tGreen     = null;
	CheckColorSliderTextContainer tBlue      = null;
	CheckColorSliderTextContainer tAlpha     = null;

	/**
	 * This is the default constructor
	 */
	public TranspByPixelRGBInputPanel() {
		super();
		initialize();
		updateColors();
		validateValues();
	}

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		this.setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));
		this.add(getTRed());
		this.add(getTGreen());
		this.add(getTBlue());
		this.add(getTAlpha());
		this.getTBlue().addValueChangedListener(this);
		this.getTGreen().addValueChangedListener(this);
		this.getTRed().addValueChangedListener(this);
		this.getTAlpha().addValueChangedListener(this);
	}

	/**
	 * This method initializes jTextField
	 *
	 * @return javax.swing.JTextField
	 */
	public CheckColorSliderTextContainer getTRed() {
		if (tRed == null) {
			tRed = new CheckColorSliderTextContainer(0, 255, 0, "R", true);
		}
		return tRed;
	}

	/**
	 * This method initializes jTextField
	 *
	 * @return javax.swing.JTextField
	 */
	public CheckColorSliderTextContainer getTAlpha() {
		if (tAlpha == null) {
			tAlpha = new CheckColorSliderTextContainer(0, 255, 255, "A", false);
			tAlpha.setColor2(this.getBackground(), true);
		}
		return tAlpha;
	}

	/**
	 * This method initializes jTextField1
	 *
	 * @return javax.swing.JTextField
	 */
	public CheckColorSliderTextContainer getTGreen() {
		if (tGreen == null) {
			tGreen = new CheckColorSliderTextContainer(0, 255, 0, "G", true);
		}
		return tGreen;
	}

	/**
	 * This method initializes jTextField2
	 *
	 * @return javax.swing.JTextField
	 */
	public CheckColorSliderTextContainer getTBlue() {
		if (tBlue == null) {
			tBlue = new CheckColorSliderTextContainer(0, 255, 0, "B", true);
		}
		return tBlue;
	}

	/**
	 * Obtiene la lista de valores del campo Blue.
	 *
	 * @return array multidimensional con los intervalos
	 */
	public int[] getRangeBlue() {
		return rangeBlue;
	}

	/**
	 * Obtiene la lista de valores del campo Green.
	 * @return array multidimensional con los intervalos
	 */
	public int[] getRangeGreen() {
		return rangeGreen;
	}

	/**
	 * Obtiene la lista de valores del campo Red.
	 * @return array multidimensional con los intervalos
	 */
	public int[] getRangeRed() {
		return rangeRed;
	}

	/**
	 * Activa o desactiva el control
	 * @param enable True activa el control y false lo desactiva
	 */
	public void setControlEnabled(boolean enabled){
		getTRed().setEnabled(enabled);
		getTGreen().setEnabled(enabled);
		getTBlue().setEnabled(enabled);
		getTAlpha().setEnabled(enabled);
	}

	/**
	 * Asigna el número de bandas activas
	 * @param n Número de bandas
	 */
	public void setActiveBands(int n){
		this.getTRed().setEnabled(true);
		switch (n) {
			case 1:
				this.getTGreen().setEnabled(false);
				this.getTBlue().setEnabled(false);
				break;
			case 2:
				this.getTGreen().setEnabled(true);
				this.getTBlue().setEnabled(false);
				break;
			case 3:
				this.getTGreen().setEnabled(true);
				this.getTBlue().setEnabled(true);
				break;
		}
	}

	/**
	 * Limpia el valor de las cajas de texto R, G y B
	 */
	public void clear(){
		getTRed().setValue(0);
		getTGreen().setValue(0);
		getTBlue().setValue(0);
		getTRed().setControlEnabled(true);
		getTGreen().setControlEnabled(true);
		getTBlue().setControlEnabled(true);
	}

	/**
	 * Actualiza los colores de los componentes de seleccion de color, el ultimo
	 * parametro true o false es para que haga o no un refresco del componente, lo
	 * hacemos solo una vez por componente para que consuma menos CPU
	 */
	public void updateColors() {
		int r = tRed.getValue();
		int g = tGreen.getValue();
		int b = tBlue.getValue();

		if (!tRed.isChecked()) r = 0;
		if (!tGreen.isChecked()) g = 0;
		if (!tBlue.isChecked()) b = 0;

		tRed.setColor1(new Color(0, g, b), false);
		tRed.setColor2(new Color(255, g, b), true);
		tGreen.setColor1(new Color(r, 0, b), false);
		tGreen.setColor2(new Color(r, 255, b), true);
		tBlue.setColor1(new Color(r, g, 0), false);
		tBlue.setColor2(new Color(r, g, 255), true);
	}

	/**
	 * En cada cambio hay que validar los datos y refrescarlos, este método se
	 * encarga de la validación de datos y actualizar los rangos de selección
	 */
	public void validateValues() {
		rangeRed = new int[] {0, 255};
		rangeGreen = new int[] {0, 255};
		rangeBlue = new int[] {0, 255};

		if (getTRed().isChecked())
			rangeRed[0] = rangeRed[1] = getTRed().getValue();

		if (getTGreen().isChecked())
			rangeGreen[0] = rangeGreen[1] = getTGreen().getValue();

		if (getTBlue().isChecked())
			rangeBlue[0] = rangeBlue[1] = getTBlue().getValue();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.doubleslider.DoubleSliderListener#actionValueChanged(org.gvsig.gui.beans.doubleslider.DoubleSliderEvent)
	 */
	public void actionValueChanged(DoubleSliderEvent e) {
		updateColors();
		validateValues();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.doubleslider.DoubleSliderListener#actionValueDragged(org.gvsig.gui.beans.doubleslider.DoubleSliderEvent)
	 */
	public void actionValueDragged(DoubleSliderEvent e) {
		updateColors();
		validateValues();
	}
}