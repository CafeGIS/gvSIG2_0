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
package org.gvsig.georeferencing.ui.launcher;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.gvsig.georeferencing.main.Georeferencing;
import org.gvsig.raster.grid.GridInterpolated;
import org.gvsig.raster.util.RasterToolsUtil;

import com.iver.utiles.swing.JComboBox;

/**
 * Panel de selección de algoritmo de georreferenciación.
 * 
 * 10/01/2008
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class AlgorithmSelectionPanel extends JPanel implements ActionListener {
	private static final long   serialVersionUID    = 1L;	
	private int                 polynomialDegree;
	
	private JRadioButton        affine              = null;
	private JRadioButton        polynomial          = null;
	private JComboBox           degreeList          = null;
	private JComboBox           interpolationMethod = null;
		
	/**
	 * Constructor. Asigna la lista de nombres de vistas para el selector. 
	 * @param viewList
	 */
	public AlgorithmSelectionPanel(int polynomialDegree) {
		this.polynomialDegree = polynomialDegree;
		init();
	}
	
	/**
	 * Acciones de inicialización del panel
	 */
	public void init() {
		ButtonGroup group = new ButtonGroup();
	    group.add(getAffine());
	    group.add(getPolynomial());
	    getPolynomial().addActionListener(this);
	    getAffine().addActionListener(this);
	    
		GridBagLayout gl = new GridBagLayout();
		setLayout(gl);
		setBorder(javax.swing.BorderFactory.createTitledBorder(null, RasterToolsUtil.getText(this, "georef_algorithm"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(0, 5, 8, 0);
		add(getAffine(), gbc);
		
		gbc.gridy = 1;
		gbc.insets = new Insets(0, 5, 8, 0);
		add(getPolynomial(), gbc);
		
		gbc.gridy = 2;
		gbc.insets = new Insets(0, 35, 8, 0);
		add(getDegreeList(), gbc);
		
		gbc.gridy = 3;
		gbc.insets = new Insets(0, 35, 8, 0);
		add(getInterpolationMethod(), gbc);
	}
	
	/**
	 * Obtiene el radio butón de la selección de transformación afín
	 * @return JRadioButton
	 */
	public JRadioButton getAffine() {
		if(affine == null) {
			affine = new JRadioButton(RasterToolsUtil.getText(this, "affine_algorithm"));
			affine.setSelected(true);
			actionPerformed(null);
		}
		return affine;
	}
	
	/**
	 * Obtiene el radio butón de la selección de transformación polinomial
	 * @return JRadioButton
	 */
	public JRadioButton getPolynomial() {
		if(polynomial == null)
			polynomial = new JRadioButton(RasterToolsUtil.getText(this, "polynomial_algorithm"));
		return polynomial;
	}
	
	/**
	 * Obtiene el combo con la lista de vistas para la selección de la que contiene
	 * la cartografía de referencia.
	 * @return JComboBox
	 */
	public JComboBox getDegreeList() {
		if(degreeList == null) {
			degreeList = new JComboBox();
			for (int i = 1; i <= polynomialDegree; i++) 
				degreeList.addItem(RasterToolsUtil.getText(this, "degree") + ": " + i);
		}
		return degreeList;
	}
	
	/**
	 * Obtiene el combo con la lista de metodos de interpolación disponibles
	 * @return JComboBox
	 */
	public JComboBox getInterpolationMethod() {
		if(interpolationMethod == null) {
			interpolationMethod = new JComboBox(); 
			interpolationMethod.addItem(RasterToolsUtil.getText(this, "vecino_+_proximo"));
			interpolationMethod.addItem(RasterToolsUtil.getText(this, "bilinear"));
			interpolationMethod.addItem(RasterToolsUtil.getText(this, "bicubico"));
		}
		return interpolationMethod;
	}
	
	/**
	 * Eventos de selección en los RadioButton de selección de Algoritmo
	 */
	public void actionPerformed(ActionEvent e) {
		getDegreeList().setEnabled(getPolynomial().isSelected());
		getInterpolationMethod().setEnabled(getPolynomial().isSelected());
	}

	//-------Consulta de propiedades seleccionadas---------
	
	/**
	 * Obtiene el grado seleccionado
	 * @return valor del grado seleccionado
	 */
	public int getSelectedDegree() {
		if(degreeList != null && getPolynomial().isSelected()) {
			String s = (String)degreeList.getSelectedItem();
			s = s.substring(s.lastIndexOf(" ") + 1, s.length());
			try {
				return Integer.valueOf(s).intValue();
			} catch (NumberFormatException e) {
				return 0;
			}
		}
		return 0;
	}
	
	/**
	 * Obtiene el método de interpolación
	 * 0 para vecino más cercano, 1 para bilinear y 2 para convoluvión cúbica
	 * @return valor de método de interpolación
	 */
	public int getSelectedInterpolationMethod() {
		if(interpolationMethod != null && getPolynomial().isSelected()) {
			switch (getInterpolationMethod().getSelectedIndex()) {
			case 0: return GridInterpolated.INTERPOLATION_NearestNeighbour;
			case 1: return GridInterpolated.INTERPOLATION_Bilinear;
			case 2: return GridInterpolated.INTERPOLATION_BicubicSpline;
			}
		}
		return -1;
	}
	
	/**
	 * Asigna el método de interpolación
	 * @param interpolationMethod
	 */
	public void setInterpolationMethod(int interpolationMethod) {
		switch (interpolationMethod) {
		case GridInterpolated.INTERPOLATION_NearestNeighbour: getInterpolationMethod().setSelectedIndex(0);break;
		case GridInterpolated.INTERPOLATION_Bilinear: getInterpolationMethod().setSelectedIndex(1);break;
		case GridInterpolated.INTERPOLATION_BicubicSpline: getInterpolationMethod().setSelectedIndex(2);break;
		}
	}
	
	/**
	 * Obtiene el algoritmo seleccionado
	 * @return constante definida en Georeferencing con el algoritmo seleccionado
	 */
	public int getAlgorithm() {
		if(getPolynomial().isSelected())
			return Georeferencing.POLYNOMIAL;
		if(getAffine().isSelected())
			return Georeferencing.AFFINE;
		return Georeferencing.UNDEFINED;
	}
	
	/**
	 * Asigna el grado del polinomio seleccionado
	 * @param grado del polinomio
	 */
	public void setDegree(int degree) {
		for (int i = 0; i < degreeList.getItemCount(); i++) {
			String item = (String)degreeList.getItemAt(i);
			if(item.endsWith(": " + degree)) {
				degreeList.setSelectedIndex(i);
				return;
			}
		}
	}
	
	/**
	 * Asigna el algoritmo
	 * @param alg
	 */
	public void setAlgorithm(int alg) {
		switch (alg) {
		case Georeferencing.AFFINE: 
				getAffine().setSelected(true);
				getPolynomial().setSelected(false);
				actionPerformed(new ActionEvent(getAffine(), 0, ""));
				break;
		case Georeferencing.POLYNOMIAL:
				getPolynomial().setSelected(true);
				getAffine().setSelected(false);
				actionPerformed(new ActionEvent(getPolynomial(), 0, ""));
				break;
		}
	}
}
