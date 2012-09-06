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
package org.gvsig.gui.beans.textincreaser;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
/**
 * Nacho Brodin (brodin_ign@gva.es)
 */
public class TextIncreaserContainer extends JPanel implements ActionListener, KeyListener{
	private static final long serialVersionUID = 7570162018139822874L;
	private ArrayList actionCommandListeners = new ArrayList();
	private JTextField tText            = null;
	private JPanel     pButtons         = null;
	private JButton    bmas             = null;
	private JButton    bmenos           = null;
	private JPanel     pGeneral         = null;
	private double     minValue         = 0;
	private double     maxValue         = 100;
	private double     value            = 0.0;
	private String     pathToImages     = "images/"; // "/com/iver/cit/gvsig/gui/panels/images/";
	/**
	 * Variable que está a true si los controles están a la derecha
	 */
	private boolean    isRight          = true;

	private boolean    bDoCallListeners = true;
	static private int eventId          = Integer.MIN_VALUE;

	/**
	 * Creación de un componente TextIncrearserContainer
	 * @param width Ancho del componente
	 * @param minValue Mínimo valor que puede tomar el texto del control
	 * @param maxValue Máximo valor que puede tomar el texto del control
	 * @param right	a true si queremos que los controles se muestren a la derecha del TextBox
	 * y false si los queremos a la izquierda
	 */
	public TextIncreaserContainer(int width, double minValue, double maxValue, double init, boolean right) {
		super();
		getTText().setPreferredSize(new Dimension(width - 25, (int) getTText().getPreferredSize().getHeight()));
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.isRight = right;
		setValue(init);
		initialize();
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		FlowLayout flowLayout = new FlowLayout();
		flowLayout.setHgap(0);
		flowLayout.setVgap(0);
		FlowLayout flowLayout5 = new FlowLayout();
		flowLayout5.setHgap(0);
		flowLayout5.setVgap(0);
		this.setLayout(flowLayout5);
		this.add(getPGeneral(), null);
	}

	/**
	 * Asigna un valor al textBox
	 * @param value
	 */
	public void setValue(double value){
		this.value = value;
		getTText().setText(Double.toString(value));
	}

	/**
	 * Obtiene el valor del textBox
	 * @return value
	 */
	public double getValue(){
		return value;
	}

	/**
	 * This method initializes jTextField
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getTText() {
		if (tText == null) {
			tText = new JTextField();
			tText.setText(Double.toString(value));
			tText.setPreferredSize(new java.awt.Dimension(45, (int) tText.getPreferredSize().getHeight()));
			tText.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
			tText.addKeyListener(this);
		}
		return tText;
	}

	/**
	 * This method initializes jPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getPButtons() {
		if (pButtons == null) {
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.insets = new java.awt.Insets(0,0,0,0);
			gridBagConstraints3.gridy = 1;
			gridBagConstraints3.gridx = 0;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.insets = new java.awt.Insets(0,0,0,0);
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.gridx = 0;
			pButtons = new JPanel();
			pButtons.setLayout(new GridBagLayout());
			pButtons.add(getBmas(), gridBagConstraints2);
			pButtons.add(getBmenos(), gridBagConstraints3);
		}
		return pButtons;
	}

	/**
	 * This method initializes jButton
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getBmas() {
		if (bmas == null) {
			bmas = new JButton();
			bmas.setPreferredSize(new java.awt.Dimension(16, (int) getTText().getPreferredSize().getHeight()/2));
			bmas.addActionListener(this);
			bmas.setIcon(new ImageIcon(getClass().getResource(pathToImages + "mas.png")));
		}
		return bmas;
	}

	/**
	 * This method initializes jButton1
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getBmenos() {
		if (bmenos == null) {
			bmenos = new JButton();
			bmenos.setPreferredSize(new java.awt.Dimension(16, (int) getTText().getPreferredSize().getHeight()/2));
			bmenos.addActionListener(this);
			bmenos.setIcon(new ImageIcon(getClass().getResource(pathToImages + "menos.png")));
		}
		return bmenos;
	}

	/**
	 * This method initializes jPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getPGeneral() {
		if (pGeneral == null) {
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();

			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			if(isRight){
				gridBagConstraints1.gridx = 1;
				gridBagConstraints.gridx = 0;
			}else{
				gridBagConstraints1.gridx = 0;
				gridBagConstraints.gridx = 1;
			}
			gridBagConstraints.gridy = 0;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints.weightx = 1.0;
			pGeneral = new JPanel();
			pGeneral.setLayout(new GridBagLayout());
			pGeneral.add(getTText(), gridBagConstraints);
			pGeneral.add(getPButtons(), gridBagConstraints1);
		}
		return pGeneral;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == bmas) {
			value ++;
			checkValues();
		}

		if (e.getSource() == bmenos) {
			value --;
			checkValues();
		}
		callValueChangedListeners();
	}

	public void addValueChangedListener(TextIncreaserListener listener) {
		if (!actionCommandListeners.contains(listener))
			actionCommandListeners.add(listener);
	}

	public void removeValueChangedListener(TextIncreaserListener listener) {
		actionCommandListeners.remove(listener);
	}

	private void callValueChangedListeners() {
		if (!bDoCallListeners)
			return;
		Iterator acIterator = actionCommandListeners.iterator();
		while (acIterator.hasNext()) {
			TextIncreaserListener listener = (TextIncreaserListener) acIterator.next();
			listener.actionValueChanged(new TextIncreaserEvent(this));
		}
		eventId++;
	}

	private void checkValues() {
		if (value >= maxValue) value = maxValue;
		if (value <= minValue) value = minValue;
		getTText().setText(Double.toString(value));
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == 10) {
			try {
				value = new Double(getTText().getText()).doubleValue();
			} catch (NumberFormatException exc) {
			}

			checkValues();
			callValueChangedListeners();
		}
	}

	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
}