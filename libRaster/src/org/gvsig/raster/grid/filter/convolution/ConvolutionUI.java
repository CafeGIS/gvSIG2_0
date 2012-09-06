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
package org.gvsig.raster.grid.filter.convolution;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import org.gvsig.raster.dataset.Params;
import org.gvsig.raster.grid.filter.RegistrableFilterListener;
import org.gvsig.raster.grid.filter.RasterFilter.Kernel;

/**
 * Panel del filtro personalizado de convolución. Consta de una matriz de cajas de texto
 * de 3x3 o de 5x5 seleccionable desde un combo.
 *
 * 28/09/2007
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class ConvolutionUI extends RegistrableFilterListener implements ActionListener, FocusListener {

	private static final long    serialVersionUID = 7102390746959243164L;
	private JTextField[][]       matrix = new JTextField[5][5];
	//Panel superior
	private JPanel               panelMatrixSelector = null;
	private JPanel               panelMatrix = null;
	private JPanel               panelSelector = null;
	//Panel inferior
	private JPanel               panelTest = null;
	private JComboBox            selector = null;
	private JButton              buttonTest = null;
	private JFormattedTextField  divisor = null;
	private double               lastValue = 0;

	/**
	 * Constructor.
	 */
	public ConvolutionUI(Kernel k) {
		getPanelMatrixSelector().setLayout(new BorderLayout());
		getPanelTest().setLayout(new BorderLayout());

		GridLayout gridLayout = new GridLayout(5, 5);
		getPanelMatrix().setLayout(gridLayout);
		gridLayout.setVgap(2);
		gridLayout.setHgap(2);

		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				if(k == null) { //Caso en que no se pase ningún kernel. Se ponen todos a 0 y el del centro a 1.
					matrix[i][j] = new JTextField("0");
					if(i == Math.ceil(matrix.length / 2) && j == Math.ceil(matrix[0].length / 2))
						matrix[i][j].setText("1");
				} else {
					if(k.kernel.length == 5) {
						String value = (k.kernel[i][j] == (int)k.kernel[i][j]) ? ((int)k.kernel[i][j]) + "" : k.kernel[i][j] + "";
						matrix[i][j] = new JTextField(value + "");
					} else {
						if(k.kernel.length == 3) {
							if(i > 0 && j > 0 && i < 4 && j < 4) {
								String value = (k.kernel[i - 1][j - 1] == (int)k.kernel[i - 1][j - 1]) ? ((int)k.kernel[i - 1][j - 1]) + "" : k.kernel[i - 1][j - 1] + "";
								matrix[i][j] = new JTextField(value + "");
							} else {
								matrix[i][j] = new JTextField("0");
								matrix[i][j].setVisible(false);
							}
						}
					}
				}
				matrix[i][j].addFocusListener(this);
				getPanelMatrix().add(matrix[i][j]);
			}
		}

		getPanelSelector().add(new JLabel("Kernel: "));
		getPanelSelector().add(getSelector());
		getPanelSelector().add(new JLabel("Divisor: "));
		getPanelSelector().add(getDivisor());

		getPanelMatrixSelector().add(getPanelMatrix(), BorderLayout.NORTH);
		getPanelMatrixSelector().add(getPanelSelector(), BorderLayout.SOUTH);
		if(k != null && k.kernel.length == 3)
			getSelector().setSelectedIndex(0);
		else if(k != null && k.kernel.length == 5)
			getSelector().setSelectedIndex(1);

		getPanelTest().add(getButtonTest(), BorderLayout.SOUTH);

		this.setLayout(new BorderLayout());
		this.add(getPanelMatrixSelector(), BorderLayout.CENTER);
		this.add(getPanelTest(), BorderLayout.SOUTH);
	}

	/**
	 * Obtiene el botón de test
	 * @return JPanel
	 */
	private JButton getButtonTest() {
		if(buttonTest == null) {
			buttonTest = new JButton("Test");
			buttonTest.addActionListener(this);
		}
		return buttonTest;
	}

	/**
	 * Obtiene el panel con el botón de test
	 * @return JPanel
	 */
	private JPanel getPanelTest() {
		if(panelTest == null)
			panelTest = new JPanel();
		return panelTest;
	}

	/**
	 * Obtiene el panel de la matriz y el selector
	 * @return JPanel
	 */
	private JPanel getPanelMatrixSelector() {
		if(panelMatrixSelector == null)
			panelMatrixSelector = new JPanel();
		return panelMatrixSelector;
	}

	/**
	 * Obtiene el panel de la matriz
	 * @return JPanel
	 */
	private JPanel getPanelMatrix() {
		if(panelMatrix == null)
			panelMatrix = new JPanel();
		return panelMatrix;
	}

	/**
	 * Obtiene el panel que contiene el selector de lado
	 * @return JPanel
	 */
	private JPanel getPanelSelector() {
		if(panelSelector == null) {
			panelSelector = new JPanel();
		}
		return panelSelector;
	}

	/**
	 * Obtiene el selector de lado
	 * @return JComboBox
	 */
	private JComboBox getSelector() {
		if(selector == null) {
			selector = new JComboBox(new String[]{"3", "5"});
			selector.setSelectedIndex(1);
			selector.addActionListener(this);
		}
		return selector;
	}

	/**
	 * Obtiene el selector de lado
	 * @return JComboBox
	 */
	private JFormattedTextField getDivisor() {
		if(divisor == null) {
			NumberFormat doubleDisplayFormat = NumberFormat.getNumberInstance();
			doubleDisplayFormat.setMinimumFractionDigits(0);
			NumberFormat doubleEditFormat = NumberFormat.getNumberInstance();
			divisor = new JFormattedTextField(new DefaultFormatterFactory(
					new NumberFormatter(doubleDisplayFormat),
					new NumberFormatter(doubleDisplayFormat),
					new NumberFormatter(doubleEditFormat)));
			divisor.setBackground(Color.white);
			divisor.setValue(new Double(0));
			divisor.setColumns(3);
		}
		return divisor;
	}

	/**
	 * Convierte la matrix de JTextField en un objeto de tipo Kernel
	 * @return Kernel
	 */
	private Kernel getKernel() {
		Kernel k = null;
		double[][] values = null;
		if(((String)selector.getSelectedItem()).equals("3")) {
			values = new double[matrix.length - 2][matrix[0].length - 2];
			for (int i = 1; i < matrix.length - 1; i++)
				for (int j = 1; j < matrix[0].length - 1; j++)
					values[i - 1][j - 1] = new Double(matrix[i][j].getText()).doubleValue();

		}
		if(((String)selector.getSelectedItem()).equals("5")) {
			values = new double[matrix.length][matrix[0].length];
			for (int i = 0; i < matrix.length; i++)
				for (int j = 0; j < matrix[0].length; j++)
					values[i][j] = new Double(matrix[i][j].getText()).doubleValue();
		}
		double value = ((Number)divisor.getValue()).doubleValue();
		if(value != 0)
			k = new Kernel(values, value);
		else
			k = new Kernel(values);
		return k;
	}

	/**
	 * Eventos lanzados por el botón de test y el combo de selección de
	 * lados del kernel.
	 */
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(getSelector())) {
			if(((String)selector.getSelectedItem()).equals("3")) {
				for (int i = 0; i < matrix.length; i++) {
					for (int j = 0; j < matrix[0].length; j++) {
						if(i == 0 || i == (matrix.length - 1) || j == 0 || j == (matrix[0].length - 1))
							matrix[i][j].setVisible(false);
						else
							matrix[i][j].setVisible(true);
					}
				}
			}
			if(((String)selector.getSelectedItem()).equals("5")) {
				for (int i = 0; i < matrix.length; i++)
					for (int j = 0; j < matrix[0].length; j++)
						matrix[i][j].setVisible(true);
			}
		}

		//Si es el botón de test cargamos el objeto Params y llamamos al listener.
		if(e.getSource().equals(getButtonTest())) {
			callStateChanged();
		}
	}

	/**
	 * Sobrecargamos el método getParams para que siempre devuelva
	 * algo.
	 */
	public Params getParams() {
		params = new Params();
		int lado = 0;
		if(((String)selector.getSelectedItem()).equals("5"))
			lado = 1;
		params.setParam("LadoVentana",
				new Integer(lado),
				Params.CHOICE,
				new String[] {"3","5","7"});
		params.setParam("Kernel",
				getKernel(),
				Params.CHOICE,
				null);
		params.setParam("FilterName",
				"personalizado",
				-1,
				null);
		return params;
	}

	/**
	 * Cuando un elemento de la matriz tiene el foco se salva el valor
	 * que hay dentro.
	 */
	public void focusGained(FocusEvent e) {
		if(e.getSource() instanceof JTextField) {
			try {
				lastValue = Double.parseDouble(((JTextField)e.getSource()).getText());
			} catch (NumberFormatException ex) {
				lastValue = 0;
				((JTextField)e.getSource()).setText("0");
			}
		}
	}

	/**
	 * Cuando un elemento de la matriz pierde el foco se comprueba
	 * que el nuevo elemento sea double. Si no lo es se restaura el antiguo valor.
	 */
	public void focusLost(FocusEvent e) {
		if(e.getSource() instanceof JTextField) {
			try {
				Double.parseDouble(((JTextField)e.getSource()).getText());
			} catch (NumberFormatException ex) {
				if(lastValue == (int)lastValue)
					((JTextField)e.getSource()).setText(((int)lastValue) + "");
				else
					((JTextField)e.getSource()).setText(lastValue + "");
			}
		}
	}
}
