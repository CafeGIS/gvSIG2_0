/*
 * Created on 31-ene-2005
 *
 * gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
USA.
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
package com.iver.utiles.console.test;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.UIManager;

import com.iver.utiles.console.JConsole;
import com.iver.utiles.console.JDockPanel;
import com.iver.utiles.console.ResponseListener;
public class JConsoleTest extends JFrame {

	private javax.swing.JPanel jContentPane = null;

	private JConsole jConsole = null;
	private JButton jButton = null;
	/**
	 * This is the default constructor
	 */
	public JConsoleTest() {
		super();
		initialize();
	}
	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		this.setSize(300,200);
		this.setContentPane(getJContentPane());
		this.setTitle("JFrame");
	}
	/**
	 * This method initializes jContentPane
	 *
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if(jContentPane == null) {
			jContentPane = new javax.swing.JPanel();
			jContentPane.setLayout(new java.awt.BorderLayout());
			JDockPanel dock = new JDockPanel(getJConsole());
			jContentPane.add(dock, java.awt.BorderLayout.SOUTH);
		}
		return jContentPane;
	}
	/**
	 * This method initializes jConsole
	 *
	 * @return com.iver.utiles.console.JConsole
	 */
	private JConsole getJConsole() {
		if (jConsole == null) {
			jConsole = new JConsole();
			// jConsole.add(getJButton(), java.awt.BorderLayout.NORTH);
		}
		return jConsole;
	}
	/**
	 * This method initializes jButton
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setPreferredSize(new java.awt.Dimension(34,25));
		}
		return jButton;
	}

	public static void main(String[] args) {
		MyBoolean responsed = new MyBoolean();
		
		//Se pone el lookAndFeel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {

		}


		JConsoleTest ct = new JConsoleTest();
		ct.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ct.show();
		ResponseHandler listener = new ResponseHandler(responsed, ct);
		ct.getJConsole().addResponseListener(listener);
		while (true){
			responsed.value = false;
			ct.getJConsole().addText("Como vas? ",JConsole.MESSAGE);

			while (!responsed.value){

			}
		}
	}

	private static class MyBoolean{
		public boolean value;
	}

	private static class ResponseHandler implements ResponseListener {

		private MyBoolean responsed;
		private JConsoleTest ct;

		public ResponseHandler(MyBoolean responsed, JConsoleTest ct){
			this.responsed = responsed;
			this.ct = ct;
		}

		/**
		 * @throws InvalidResponseException
		 * @see com.iver.utiles.console.ResponseListener#acceptResponse(java.lang.String)
		 */
		public void acceptResponse(String response){
			ct.jButton.setText(response);
			responsed.value = true;
		}

	}
  }
