/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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
 *   Av. Blasco Ib��ez, 50
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
package com.iver.cit.gvsig.project.documents.layout.fframes.gui;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.iver.andami.PluginServices;

public class JPRotation extends JPanel {

	private JPRotationView pRotationView = null;
	private JPanel pButtons = null;
	private JButton bLeft = null;
	private JTextField txtRotation = null;
	private JButton bRight = null;
	public void setRotation(double rot){
		getPRotationView().setRotation(rot);
		//getTxtRotation().setText(String.valueOf(rot));
		setTextRotation(rot);
	}
	private void setTextRotation(double d) {
		String s=String.valueOf(d);
		if (s.endsWith(".0")) {
			txtRotation.setText(s.substring(0,s.length()-2));
		}else {
			txtRotation.setText(s);
		}
	}
	public double getRotation(){
		return getPRotationView().getRotation();
	}
	/**
	 * This method initializes pRotationView
	 *
	 * @return javax.swing.JPanel
	 */
	private JPRotationView getPRotationView() {
		if (pRotationView == null) {
			pRotationView = new JPRotationView();
			pRotationView.setForeground(java.awt.Color.black);
			pRotationView.setBackground(java.awt.SystemColor.controlShadow);
			pRotationView.setPreferredSize(new java.awt.Dimension(80,50));
		}
		return pRotationView;
	}

	/**
	 * This method initializes pButtons
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getPButtons() {
		if (pButtons == null) {
			pButtons = new JPanel();
			pButtons.add(getBLeft(), null);
			pButtons.add(getTxtRotation(), null);
			pButtons.add(getBRight(), null);
		}
		return pButtons;
	}

	/**
	 * This method initializes bLeft
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getBLeft() {
		if (bLeft == null) {
			bLeft = new JButton();
			bLeft.setPreferredSize(new java.awt.Dimension(24,24));
			bLeft.setIcon(PluginServices.getIconTheme().get("left-rotation-icon"));
			bLeft.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					setRotation(Double.parseDouble(txtRotation.getText())+1);
					getTxtRotation().setText(txtRotation.getText());
					getPRotationView().repaint();
				}
			});
		}
		return bLeft;
	}

	/**
	 * This method initializes txtRotation
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getTxtRotation() {
		if (txtRotation == null) {
			txtRotation = new JTextField();
			txtRotation.setPreferredSize(new java.awt.Dimension(45,24));
			txtRotation.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyReleased(java.awt.event.KeyEvent e) {
					//ensureDouble(txtRotation);
					//if (e.getKeyCode()==KeyEvent.VK_ENTER){
					if (txtRotation.getText().endsWith(".")) {
						return;
					}
					try{
					setRotation(Double.parseDouble(txtRotation.getText()));
					}catch (NumberFormatException e1) {
						JOptionPane.showMessageDialog((Component)PluginServices.getMainFrame(),PluginServices.getText(this,"numero_incorrecto"));
					}
					getPRotationView().repaint();
					//}
				}
			});
			//txtRotation.setText(String.valueOf(getRotation()));
			setTextRotation(getRotation());
		}
		return txtRotation;
	}

	/**
	 * Asegura cutremente que no se meten valores que no sean.
	 * El funcionamiento consiste en si el �ltimo car�cter escrito
	 * no vale para formar un int entonces se elimina.
	 *
	 * enteros.
	 * @param tf
	 */
	/*private boolean ensureDouble(JTextField tf){
	    String s = tf.getText();
	    try {
	        Double.parseDouble(s);
	        return true;
	    } catch (Exception e){
	        if (s.length()!=0){
	           // tf.setText(s.substring(0, s.length()-1));
	        }else {
	        	//tf.setText("0");
	        }
	        return true;
	    }
	}*/
	/**
	 * This method initializes bRight
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getBRight() {
		if (bRight == null) {
			bRight = new JButton();
			bRight.setPreferredSize(new java.awt.Dimension(24,24));
			bRight.setIcon(PluginServices.getIconTheme().get("right-rotation-icon"));
			bRight.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					setRotation(Double.parseDouble(txtRotation.getText())-1);
					getTxtRotation().setText(txtRotation.getText());
					getPRotationView().repaint();
				}
			});
		}
		return bRight;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

	/**
	 * This is the default constructor
	 */
	public JPRotation() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.setSize(122, 112);
		this.setBorder(javax.swing.BorderFactory.createTitledBorder(null, PluginServices.getText(this,"grados"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
		this.setPreferredSize(new java.awt.Dimension(102,80));
		this.add(getPRotationView(), java.awt.BorderLayout.NORTH);
		this.add(getPButtons(), java.awt.BorderLayout.SOUTH);
	}

}  //  @jve:decl-index=0:visual-constraint="10,26"
