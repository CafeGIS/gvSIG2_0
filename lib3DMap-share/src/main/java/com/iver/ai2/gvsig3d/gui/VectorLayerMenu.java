/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */
package com.iver.ai2.gvsig3d.gui;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.gvsig.gui.beans.swing.JButton;

import com.iver.ai2.gvsig3d.map3d.layers.Layer3DProps;
import com.iver.andami.PluginServices;

/**
 * This dialog shows the preferences about a 3D layers
 * 
 * @author Julio Campos Alvarez
 * @author Angel Fraile Griñan
 */
public class VectorLayerMenu extends JDialog implements MouseListener,
		ItemListener {

	private int width = 285;

	private int height = 190;

	private Checkbox aButton; // @jve:decl-index=0:visual-constraint="359,41"

	private Checkbox bButton;

	private JTextField heigthField;

	private Layer3DProps props3D;

	private Object panel;

	private JPanel jContentPane = null;

	private JButton boton;

	private JLabel jLabel1;

	private String name;

	/**
	 * This is the default constructor
	 * 
	 * @param nombre
	 * 
	 * @param f
	 *            Frame padre del dialogo
	 * @param v
	 *            Vista que se representa
	 */
	public VectorLayerMenu(Layer3DProps props, String nombre) {

		props3D = props;

		name = nombre;

		initialize();

	}

	/**
	 * This method initializes this class
	 */
	private void initialize() {
		// Inicialize component
		this.setTitle(PluginServices.getText(this, "Layer_Properties") + ":"
				+ name);
		// Introducing the margin
		this.setSize(new Dimension(width, height));
		// Dimension of the panel only for java 1.5
		// this.setPreferredSize(new Dimension(width, height));
		this.setResizable(false);

		JFrame a = (JFrame) PluginServices.getMainFrame();
		double posX = a.getBounds().getCenterX();
		double posY = a.getBounds().getCenterY();
		this.setLocation(((int) posX) - (width / 2), ((int) posY)
				- (height / 2));
		panel = new JPanel();
		this.getContentPane().add((Component) panel, BorderLayout.CENTER);

		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.insets = new Insets(0, 25, 0, 0);
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.fill = GridBagConstraints.VERTICAL;
		gridBagConstraints.gridy = 0;
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.gridx = 0;
		gridBagConstraints3.insets = new Insets(12, 0, 0, 0);
		gridBagConstraints3.gridy = 4;
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.fill = GridBagConstraints.VERTICAL;
		gridBagConstraints2.gridy = 3;
		gridBagConstraints2.weightx = 1.0;
		gridBagConstraints2.gridwidth = 1;
		gridBagConstraints2.ipadx = 93;
		gridBagConstraints2.insets = new Insets(8, 0, 0, 0);
		gridBagConstraints2.gridx = 0;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.insets = new Insets(0, 25, 0, 0);
		gridBagConstraints1.anchor = GridBagConstraints.WEST;
		gridBagConstraints1.fill = GridBagConstraints.VERTICAL;
		gridBagConstraints1.gridy = 1;

		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.gridx = 0;
		gridBagConstraints4.insets = new Insets(0, 25, 0, 0);
		gridBagConstraints4.anchor = GridBagConstraints.WEST;
		gridBagConstraints4.fill = GridBagConstraints.VERTICAL;
		gridBagConstraints4.gridy = 2;

		jContentPane = new JPanel();
		jContentPane.setLayout(new GridBagLayout());
		jContentPane.add(getJCheckBox1(), gridBagConstraints1);
		jLabel1 = new JLabel("Altura en metros:");
		jLabel1.setEnabled(false);
		jContentPane.add(jLabel1, gridBagConstraints4);
		jContentPane.add(getJTextField(), gridBagConstraints2);
		jContentPane.add(getJButton(), gridBagConstraints3);
		jContentPane.add(getJCheckBox(), gridBagConstraints);
		this.setModal(true);
		this.setAlwaysOnTop(true);
		this.getContentPane().add(jContentPane);

	}

	private Checkbox getJCheckBox1() {
		if (bButton == null) {
			bButton = new Checkbox(PluginServices.getText(this, "Heigth_Z"),
					false);
			bButton.setEnabled(false);
			bButton.addItemListener(this);
		}
		return bButton;
	}

	/**
	 * This method initializes jTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextField() {
		if (heigthField == null) {
			heigthField = new JTextField();
			heigthField.setText("10000");
			heigthField.setEnabled(false);

		}
		return heigthField;
	}

	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButton() {
		if (boton == null) {
			boton = new JButton(PluginServices.getText(this, "Accept"));

			boton.addMouseListener(this);
		}
		return boton;
	}

	/**
	 * This method initializes jCheckBox
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private Checkbox getJCheckBox() {
		if (aButton == null) {
			aButton = new Checkbox(
					PluginServices.getText(this, "Raster_layer"), true);
			aButton.addItemListener(this);
		}
		return aButton;
	}

	private Checkbox getJLabel() {
		if (jLabel1 == null) {
			jLabel1 = new JLabel();
			jLabel1.setText("Altura en metros:");
		}
		return aButton;
	}

	public void itemStateChanged(ItemEvent arg0) {

		bButton.setEnabled(!aButton.getState());
		aButton.setEnabled(!bButton.getState());
		heigthField.setEnabled(!aButton.getState());
		heigthField.setEnabled(!(bButton.getState() || aButton.getState()));

		jLabel1.setEnabled(!aButton.getState());
		jLabel1.setEnabled(!(bButton.getState() || aButton.getState()));

	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

		if (aButton.getState()) {
			props3D.setType(Layer3DProps.layer3DImage);
		} else {
			props3D.setType(Layer3DProps.layer3DVector);
			if (!bButton.getState()) {
				props3D.setZEnable(false);
				int h = Integer.parseInt(heigthField.getText());
				if (h >= 0)
					props3D.setHeigth(h);
			} else {
				props3D.setZEnable(true);
			}
		}
		this.dispose();
	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(bButton, BorderLayout.CENTER);
		}
		return jContentPane;
	}
} // @jve:decl-index=0:visual-constraint="10,10"
